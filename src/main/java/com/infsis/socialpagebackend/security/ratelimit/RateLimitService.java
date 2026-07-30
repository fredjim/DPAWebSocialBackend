package com.infsis.socialpagebackend.security.ratelimit;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Rate limiter in-memory basado en ventana fija (fixed window), sin dependencias externas.
 *
 * <p>Mantiene un contador por clave (típicamente {@code endpoint|ip}) que se reinicia
 * al terminar la ventana. Es adecuado para proteger endpoints de autenticación de bajo
 * volumen frente a fuerza bruta.</p>
 *
 * <p><b>Alcance:</b> el estado vive en la memoria de esta instancia. Para un despliegue
 * con múltiples instancias del backend, reemplazar el {@code ConcurrentHashMap} por un
 * almacén compartido (Redis) manteniendo la misma interfaz.</p>
 */
@Service
public class RateLimitService {

    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    /** Barrido de entradas expiradas cada 5 minutos como máximo (lazy, sin scheduler). */
    private static final long SWEEP_INTERVAL_MS = 5 * 60 * 1000L;
    private final AtomicLong lastSweep = new AtomicLong(System.currentTimeMillis());

    /**
     * Intenta consumir una petición para la clave dada.
     *
     * @param key           identificador único (ej. "login|203.0.113.5")
     * @param capacity      máximo de peticiones por ventana
     * @param windowSeconds duración de la ventana en segundos
     * @return resultado con {@code allowed} y, si se bloquea, {@code retryAfterSeconds}
     */
    public Result tryConsume(String key, int capacity, int windowSeconds) {
        final long now = System.currentTimeMillis();
        maybeSweep(now);

        Window window = windows.compute(key, (k, existing) -> {
            if (existing == null || now >= existing.resetAt) {
                return new Window(1, now + windowSeconds * 1000L);
            }
            existing.count++;
            return existing;
        });

        if (window.count > capacity) {
            long retryAfter = Math.max(1L, (window.resetAt - now) / 1000L);
            return new Result(false, retryAfter);
        }
        return new Result(true, 0L);
    }

    /** Elimina periódicamente las ventanas ya expiradas para acotar el uso de memoria. */
    private void maybeSweep(long now) {
        long previous = lastSweep.get();
        if (now - previous < SWEEP_INTERVAL_MS) {
            return;
        }
        // Solo un hilo ejecuta el barrido; los demás continúan sin bloquearse.
        if (lastSweep.compareAndSet(previous, now)) {
            windows.values().removeIf(w -> now >= w.resetAt);
        }
    }

    /** Ventana mutable protegida por la sección atómica de {@link ConcurrentHashMap#compute}. */
    private static final class Window {
        private int count;
        private final long resetAt;

        private Window(int count, long resetAt) {
            this.count = count;
            this.resetAt = resetAt;
        }
    }

    /** Resultado de un intento de consumo. */
    public record Result(boolean allowed, long retryAfterSeconds) {}
}
