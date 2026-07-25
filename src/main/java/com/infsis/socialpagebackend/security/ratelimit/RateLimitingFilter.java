package com.infsis.socialpagebackend.security.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infsis.socialpagebackend.exceptions.ErrorResponse;
import com.infsis.socialpagebackend.security.ratelimit.RateLimitProperties.Rule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de rate limiting por IP para los endpoints de autenticación sensibles a
 * fuerza bruta y abuso de recursos. Se ejecuta antes de la autenticación.
 *
 * <p>Solo intercepta las rutas con regla definida ({@link #shouldNotFilter}); el resto
 * del tráfico pasa sin overhead. Al superar el límite responde {@code 429 Too Many
 * Requests} con cabecera {@code Retry-After} y un cuerpo JSON consistente con
 * {@link ErrorResponse}.</p>
 */
@Component
@Slf4j
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitProperties properties;
    private final RateLimitService rateLimitService;
    private final ObjectMapper objectMapper;

    public RateLimitingFilter(RateLimitProperties properties,
                              RateLimitService rateLimitService,
                              ObjectMapper objectMapper) {
        this.properties = properties;
        this.rateLimitService = rateLimitService;
        this.objectMapper = objectMapper;
    }

    /** Nombre lógico de la regla por (método + ruta). Sirve como prefijo de la clave. */
    private String ruleNameFor(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        if ("POST".equals(method)) {
            switch (path) {
                case "/api/auth/login":           return "login";
                case "/api/auth/root/login":      return "rootLogin";
                case "/api/auth/register":        return "register";
                case "/api/auth/forgot-password": return "forgotPassword";
                case "/api/auth/reset-password":  return "resetPassword";
                case "/api/auth/refresh":         return "refresh";
                default:                          return null;
            }
        }
        if ("GET".equals(method) && "/api/auth/verify-email".equals(path)) {
            return "verifyEmail";
        }
        return null;
    }

    private Rule ruleFor(String name) {
        return switch (name) {
            case "login"          -> properties.getLogin();
            case "rootLogin"      -> properties.getRootLogin();
            case "register"       -> properties.getRegister();
            case "forgotPassword" -> properties.getForgotPassword();
            case "resetPassword"  -> properties.getResetPassword();
            case "verifyEmail"    -> properties.getVerifyEmail();
            case "refresh"        -> properties.getRefresh();
            default               -> null;
        };
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !properties.isEnabled() || ruleNameFor(request) == null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String ruleName = ruleNameFor(request);
        Rule rule = ruleFor(ruleName);
        String clientIp = resolveClientIp(request);
        String key = ruleName + "|" + clientIp;

        RateLimitService.Result result =
                rateLimitService.tryConsume(key, rule.getCapacity(), rule.getWindowSeconds());

        if (!result.allowed()) {
            log.warn("RATE_LIMIT_EXCEDIDO endpoint={} ip={} retryAfterSeg={}",
                    ruleName, clientIp, result.retryAfterSeconds());
            writeTooManyRequests(request, response, result.retryAfterSeconds());
            return;
        }

        chain.doFilter(request, response);
    }

    /** Obtiene la IP real del cliente respetando el reverse-proxy (X-Forwarded-For / X-Real-IP). */
    private String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwardedFor)) {
            // Puede contener "cliente, proxy1, proxy2"; la primera IP es la del cliente original.
            return forwardedFor.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp)) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }

    private void writeTooManyRequests(HttpServletRequest request, HttpServletResponse response, long retryAfter)
            throws IOException {
        response.setStatus(429); // 429 Too Many Requests
        response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(retryAfter));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse body = new ErrorResponse(
                429,
                "Demasiadas solicitudes. Intenta de nuevo en " + retryAfter + " segundos.",
                request.getRequestURI());
        objectMapper.writeValue(response.getWriter(), body);
    }
}
