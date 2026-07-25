package com.infsis.socialpagebackend.security.ratelimit;

import com.infsis.socialpagebackend.exceptions.TooManyRequestsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bloqueo de cuenta (account lockout) por intentos fallidos de login.
 *
 * <p>Complementa al rate limiting por IP: mientras el filtro limita cuántas peticiones
 * puede hacer una IP, este servicio bloquea una cuenta concreta tras N contraseñas
 * incorrectas, resistiendo así ataques distribuidos (muchas IPs contra un mismo email).</p>
 *
 * <p>El estado es in-memory por instancia. Para múltiples instancias, respaldar en Redis.</p>
 */
@Service
@Slf4j
public class LoginAttemptService {

    private final RateLimitProperties properties;
    private final ConcurrentHashMap<String, Attempt> attempts = new ConcurrentHashMap<>();

    public LoginAttemptService(RateLimitProperties properties) {
        this.properties = properties;
    }

    /**
     * Verifica si la cuenta está bloqueada; si lo está, lanza 429 con el tiempo restante.
     * Debe llamarse ANTES de intentar autenticar.
     */
    public void assertNotLocked(String email) {
        Attempt attempt = attempts.get(key(email));
        if (attempt == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (attempt.lockedUntil > now) {
            long retryAfter = Math.max(1L, (attempt.lockedUntil - now) / 1000L);
            log.warn("LOGIN_BLOQUEADO email={} motivo=demasiados_intentos retryAfterSeg={}", email, retryAfter);
            throw new TooManyRequestsException(
                    "Cuenta bloqueada temporalmente por demasiados intentos fallidos. "
                            + "Intenta de nuevo en " + retryAfter + " segundos.",
                    retryAfter);
        }
    }

    /** Registra un intento fallido; al alcanzar el máximo, bloquea la cuenta. */
    public void recordFailure(String email) {
        long now = System.currentTimeMillis();
        int maxAttempts = properties.getLockout().getMaxFailedAttempts();
        long lockMillis = properties.getLockout().getLockMinutes() * 60_000L;

        attempts.compute(key(email), (k, existing) -> {
            Attempt attempt = (existing == null || now >= existing.lockedUntil && existing.count >= maxAttempts)
                    ? new Attempt()
                    : existing;
            attempt.count++;
            if (attempt.count >= maxAttempts) {
                attempt.lockedUntil = now + lockMillis;
                log.warn("LOGIN_LOCKOUT_ACTIVADO email={} intentos={} bloqueadoPorMin={}",
                        email, attempt.count, properties.getLockout().getLockMinutes());
            }
            return attempt;
        });
    }

    /** Limpia el contador tras un login exitoso. */
    public void reset(String email) {
        attempts.remove(key(email));
    }

    private String key(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private static final class Attempt {
        private int count;
        private long lockedUntil;
    }
}
