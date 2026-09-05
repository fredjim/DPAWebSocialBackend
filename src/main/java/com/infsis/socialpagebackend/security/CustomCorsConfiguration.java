package com.infsis.socialpagebackend.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Valida el Origin contra una lista de orígenes exactos (para entornos sin subdominio real,
 * como localhost) y contra dominios base (para producción/dev por subdominio de tenant).
 * Un dominio base como "umss.dev" permite automáticamente cualquier subdominio
 * (dpa.umss.dev, dube.umss.dev, nueva-institucion.umss.dev, ...) sin tocar código ni config
 * al agregar una institución nueva.
 */
@Component
public class CustomCorsConfiguration implements CorsConfigurationSource {

    private final List<String> allowedOrigins;
    private final List<Pattern> allowedDomainPatterns;

    public CustomCorsConfiguration(
            @Value("${app.cors.allowed-origins}") List<String> allowedOrigins,
            @Value("${app.cors.allowed-domains}") List<String> allowedDomains) {
        this.allowedOrigins = allowedOrigins;
        this.allowedDomainPatterns = allowedDomains.stream()
                .map(String::trim)
                .filter(domain -> !domain.isEmpty())
                .map(domain -> Pattern.compile("^https://([a-z0-9-]+\\.)?" + Pattern.quote(domain) + "$"))
                .collect(Collectors.toList());
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();

        String origin = request.getHeader("Origin");
        if (origin != null && isAllowedOrigin(origin)) {
            config.setAllowedOrigins(List.of(origin));
        }

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        return config;
    }

    /**
     * Expuesto para que otros componentes (ej. servicios de email que necesitan construir un
     * link seguro hacia el frontend del tenant actual) puedan reusar la misma lista de dominios
     * confiables que ya valida las peticiones CORS, en vez de mantener una segunda lista aparte.
     */
    public boolean isAllowedOrigin(String origin) {
        if (allowedOrigins.contains(origin)) {
            return true;
        }
        return allowedDomainPatterns.stream().anyMatch(pattern -> pattern.matcher(origin).matches());
    }
}