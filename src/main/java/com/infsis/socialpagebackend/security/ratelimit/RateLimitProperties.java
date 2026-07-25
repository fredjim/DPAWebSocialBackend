package com.infsis.socialpagebackend.security.ratelimit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuración de rate limiting para los endpoints de autenticación.
 * Todos los valores son sobreescribibles vía application.properties con el prefijo
 * {@code security.rate-limit.*} (relaxed binding: rootLogin ← root-login, etc.).
 *
 * Cada {@link Rule} define cuántas peticiones se permiten ({@code capacity})
 * dentro de una ventana de tiempo ({@code windowSeconds}) por dirección IP.
 */
@Component
@ConfigurationProperties(prefix = "security.rate-limit")
@Data
public class RateLimitProperties {

    /** Interruptor global. Si es false, el filtro no aplica ningún límite. */
    private boolean enabled = true;

    /** POST /api/auth/login — fuerza bruta de contraseñas. */
    private Rule login = new Rule(5, 60);

    /** POST /api/auth/root/login — fuerza bruta sobre la cuenta ROOT. */
    private Rule rootLogin = new Rule(5, 60);

    /** POST /api/auth/register — creación masiva de cuentas / spam de emails. */
    private Rule register = new Rule(5, 3600);

    /** POST /api/auth/forgot-password — email bombing hacia la víctima. */
    private Rule forgotPassword = new Rule(3, 3600);

    /** POST /api/auth/reset-password — defensa en profundidad sobre el token. */
    private Rule resetPassword = new Rule(10, 900);

    /** GET /api/auth/verify-email — defensa en profundidad sobre el token. */
    private Rule verifyEmail = new Rule(10, 3600);

    /** POST /api/auth/refresh — clientes legítimos refrescan periódicamente. */
    private Rule refresh = new Rule(60, 3600);

    /** Bloqueo de cuenta (account lockout) por intentos fallidos de login. */
    private Lockout lockout = new Lockout();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rule {
        /** Número máximo de peticiones permitidas dentro de la ventana. */
        private int capacity;
        /** Duración de la ventana en segundos. */
        private int windowSeconds;
    }

    @Data
    public static class Lockout {
        /** Intentos fallidos consecutivos (por email) antes de bloquear. */
        private int maxFailedAttempts = 5;
        /** Minutos que dura el bloqueo de la cuenta. */
        private int lockMinutes = 15;
    }
}
