package com.infsis.socialpagebackend.moderation.filters;

import com.infsis.socialpagebackend.moderation.clients.PerspectiveApiClient;
import com.infsis.socialpagebackend.moderation.clients.PerspectiveApiClient.PerspectiveApiException;
import com.infsis.socialpagebackend.moderation.configuration.ModerationProperties;
import com.infsis.socialpagebackend.moderation.dtos.PerspectiveResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Filtro 2 del pipeline de moderación.
 *
 * Usa Google Perspective API para detectar toxicidad con ML:
 * insultos, amenazas, profanidad, contenido severamente tóxico.
 *
 * Comportamiento ante fallos (fail-open):
 *   Si la API falla (timeout, cuota, error de red), el filtro deja pasar el contenido
 *   y lo registra como advertencia — nunca bloquea al usuario por un fallo de infraestructura.
 *
 * Latencia esperada: ~100-300ms
 * Costo: gratuito hasta 1 QPS (quota ampliable con solicitud a Google)
 */
@Slf4j
@Component
public class ToxicityApiFilter {

    private final PerspectiveApiClient perspectiveClient;
    private final ModerationProperties properties;

    public ToxicityApiFilter(PerspectiveApiClient perspectiveClient,
                             ModerationProperties properties) {
        this.perspectiveClient = perspectiveClient;
        this.properties = properties;
    }

    /**
     * Evalúa si el texto es tóxico según Perspective API.
     *
     * @param text contenido del comentario o reply
     * @return resultado de la evaluación
     */
    public ToxicityResult evaluate(String text) {
        long start = System.currentTimeMillis();
        double threshold = properties.getPerspective().getToxicityThreshold();
        int minLength = properties.getPerspective().getMinTextLength();

        // Omitir textos demasiado cortos — la API no agrega valor en este caso
        if (text == null || text.strip().length() < minLength) {
            long elapsed = System.currentTimeMillis() - start;
            log.debug("ToxicityApiFilter: SKIPPED (text too short) in {}ms", elapsed);
            return ToxicityResult.skipped(elapsed);
        }

        try {
            PerspectiveResult result = perspectiveClient.analyze(text);
            long elapsed = System.currentTimeMillis() - start;

            if (result.isAboveThreshold(threshold)) {
                String reason = String.format(
                    "Toxicity score: %.2f (umbral: %.2f). Categorías: %s",
                    result.getMaxScore(),
                    threshold,
                    result.getTriggeredCategories()
                );
                log.info("ToxicityApiFilter: REJECTED in {}ms — {}", elapsed, reason);
                return ToxicityResult.rejected(result, reason, elapsed);
            }

            log.debug("ToxicityApiFilter: PASSED in {}ms — maxScore={}",
                    elapsed, String.format("%.2f", result.getMaxScore()));
            return ToxicityResult.passed(result, elapsed);

        } catch (PerspectiveApiException e) {
            // Fail-open: si la API falla, el contenido pasa al siguiente filtro
            long elapsed = System.currentTimeMillis() - start;
            log.warn("ToxicityApiFilter: API error after {}ms — failing open. Reason: {}",
                    elapsed, e.getMessage());
            return ToxicityResult.apiError(e.getMessage(), elapsed);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Result inner class
    // ─────────────────────────────────────────────────────────────────────────

    public static class ToxicityResult {

        public enum Status { REJECTED, PASSED, SKIPPED, API_ERROR }

        private final Status status;
        private final PerspectiveResult perspectiveResult;
        private final String reason;
        private final long processingTimeMs;

        private ToxicityResult(Status status, PerspectiveResult perspectiveResult,
                               String reason, long processingTimeMs) {
            this.status = status;
            this.perspectiveResult = perspectiveResult;
            this.reason = reason;
            this.processingTimeMs = processingTimeMs;
        }

        public static ToxicityResult rejected(PerspectiveResult result, String reason, long ms) {
            return new ToxicityResult(Status.REJECTED, result, reason, ms);
        }

        public static ToxicityResult passed(PerspectiveResult result, long ms) {
            return new ToxicityResult(Status.PASSED, result, null, ms);
        }

        public static ToxicityResult skipped(long ms) {
            return new ToxicityResult(Status.SKIPPED, null, "Text too short", ms);
        }

        public static ToxicityResult apiError(String errorMsg, long ms) {
            return new ToxicityResult(Status.API_ERROR, null,
                    "Perspective API error: " + errorMsg, ms);
        }

        public boolean isRejected()  { return status == Status.REJECTED; }
        public boolean isPassed()    { return status == Status.PASSED || status == Status.SKIPPED; }
        public boolean isApiError()  { return status == Status.API_ERROR; }

        public Status getStatus()                      { return status; }
        public PerspectiveResult getPerspectiveResult() { return perspectiveResult; }
        public String getReason()                      { return reason; }
        public long getProcessingTimeMs()              { return processingTimeMs; }

        /** Score de toxicidad para loggear/guardar. 0.0 si no aplica. */
        public double getToxicityScore() {
            return perspectiveResult != null ? perspectiveResult.getMaxScore() : 0.0;
        }
    }
}
