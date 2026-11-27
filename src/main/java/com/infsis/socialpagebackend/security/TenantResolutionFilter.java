package com.infsis.socialpagebackend.security;

import com.infsis.socialpagebackend.institutions.repositories.InstitutionRepository;
import com.infsis.socialpagebackend.multitenant.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Resuelve el tenant (institution_id) de cada request y lo carga en TenantContext.
 *
 * Prioridad de resolución:
 *   1. JWT claim "institutionId"  → usuario autenticado
 *   2. Header "X-Tenant-Slug"    → acceso público (frontend sin JWT)
 *
 * Corre antes que todos los demás filtros. Siempre limpia TenantContext en el finally.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantResolutionFilter extends OncePerRequestFilter {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        try {
            // traceId: reutiliza X-Request-Id si el cliente/proxy lo envía; si no, genera uno.
            String traceId = request.getHeader("X-Request-Id");
            if (traceId == null || traceId.isBlank()) {
                traceId = UUID.randomUUID().toString();
            }
            MDC.put("traceId", traceId);

            String tenantId = resolveFromJwt(request);

            if (tenantId == null) {
                tenantId = resolveFromSlugHeader(request);
            }

            if (tenantId != null) {
                TenantContext.setCurrentTenant(tenantId);
                MDC.put("institutionId", tenantId);
            }

            // Independiente de tenantId: un ROOT puede navegar desde el subdominio de una
            // institución (X-Tenant-Slug presente) sin dejar de ser ROOT. Ver TenantContext.
            TenantContext.setRootFlag(resolveIsRootFromJwt(request));

            String userId = resolveUserIdFromJwt(request);
            if (userId != null) {
                MDC.put("userId", userId);
            }

            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
            // Imprescindible: limpiar el MDC para no filtrar contexto entre requests del pool de hilos.
            MDC.clear();
        }
    }

    private String resolveFromJwt(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer == null || !bearer.startsWith("Bearer ")) return null;
        try {
            return jwtGenerator.extractInstitutionId(bearer.substring(7));
        } catch (Exception e) {
            // Token malformado o expirado — Spring Security lo rechazará después
            return null;
        }
    }

    private String resolveUserIdFromJwt(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer == null || !bearer.startsWith("Bearer ")) return null;
        try {
            return jwtGenerator.extractUserId(bearer.substring(7));
        } catch (Exception e) {
            return null;
        }
    }

    private boolean resolveIsRootFromJwt(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer == null || !bearer.startsWith("Bearer ")) return false;
        try {
            return jwtGenerator.extractIsRoot(bearer.substring(7));
        } catch (Exception e) {
            return false;
        }
    }

    private String resolveFromSlugHeader(HttpServletRequest request) {
        String slug = request.getHeader("X-Tenant-Slug");
        if (slug == null || slug.isBlank()) return null;

        return institutionRepository.findBySlug(slug.trim().toLowerCase())
                .map(institution -> institution.getUuid())
                .orElse(null); // slug inválido → tenant null → respuestas vacías, no 500
    }
}
