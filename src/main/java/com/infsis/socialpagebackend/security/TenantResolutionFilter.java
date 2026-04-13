package com.infsis.socialpagebackend.security;

import com.infsis.socialpagebackend.institutions.repositories.InstitutionRepository;
import com.infsis.socialpagebackend.multitenant.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
            String tenantId = resolveFromJwt(request);

            if (tenantId == null) {
                tenantId = resolveFromSlugHeader(request);
            }

            if (tenantId != null) {
                TenantContext.setCurrentTenant(tenantId);
            }

            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
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

    private String resolveFromSlugHeader(HttpServletRequest request) {
        String slug = request.getHeader("X-Tenant-Slug");
        if (slug == null || slug.isBlank()) return null;

        return institutionRepository.findBySlug(slug.trim().toLowerCase())
                .map(institution -> institution.getUuid())
                .orElse(null); // slug inválido → tenant null → respuestas vacías, no 500
    }
}
