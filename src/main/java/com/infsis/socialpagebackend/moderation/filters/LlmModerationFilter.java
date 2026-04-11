package com.infsis.socialpagebackend.moderation.filters;

import com.infsis.socialpagebackend.moderation.clients.LlmModerationClient;
import com.infsis.socialpagebackend.moderation.clients.LlmModerationClient.LlmClientException;
import com.infsis.socialpagebackend.moderation.configuration.ModerationProperties;
import com.infsis.socialpagebackend.moderation.dtos.LlmModerationResult;
import com.infsis.socialpagebackend.moderation.dtos.LlmModerationResult.Decision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Filtro 3 del pipeline de moderación.
 *
 * Usa un LLM (local o en la nube) para análisis contextual profundo:
 * - Sarcasmo disfrazado de texto inofensivo
 * - Doble sentido o manipulación sutil
 * - Acoso que no usa palabras explícitas
 * - Contenido que viola políticas específicas de la plataforma
 *
 * Proveedor configurable sin cambiar código:
 *   moderation.llm.endpoint / moderation.llm.api-key / moderation.llm.model
 *
 * Comportamiento ante fallos (fail-open):
 *   Si el LLM falla (timeout, conexión, parse error) → NEEDS_REVIEW en lugar de bloquear.
 *
 * Latencia esperada:
 *   Local (Ollama 7B): ~1-5s
 *   DeepSeek API:      ~500-1500ms
 *   OpenAI gpt-4o-mini: ~400-1000ms
 */
@Slf4j
@Component
public class LlmModerationFilter {

    /**
     * System prompt optimizado para moderación en contexto institucional/educativo.
     * Instruye al LLM a responder SOLO en JSON, sin texto extra.
     */
    private static final String SYSTEM_PROMPT = """
        Eres un moderador de contenido para una red social institucional/educativa.
        Tu tarea es evaluar si el siguiente comentario viola alguna de estas reglas:

        1. Discurso de odio, discriminación o bullying
        2. Contenido sexual explícito o sugestivo
        3. Amenazas o incitación a la violencia
        4. Spam o publicidad no solicitada
        5. Desinformación peligrosa o malintencionada
        6. Acoso o intimidación personal (directa o sutil)
        7. Contenido que promueva autolesión o suicidio
        8. Lenguaje ofensivo o irrespetuoso hacia instituciones o personas

        Considera el contexto educativo/institucional. Sé estricto pero justo.
        Textos con crítica constructiva, opinión o debate respetuoso deben ser APPROVED.

        Responde EXCLUSIVAMENTE en este formato JSON (sin texto adicional, sin markdown):
        {
            "decision": "APPROVED" | "REJECTED" | "NEEDS_REVIEW",
            "confidence": 0.0,
            "reason": "explicación breve en español (máx 100 caracteres)",
            "categories": []
        }

        Valores de confidence:
          0.9-1.0 = muy seguro de la decisión
          0.7-0.9 = bastante seguro
          0.5-0.7 = algo de incertidumbre
          <0.5    = muy incierto, preferir NEEDS_REVIEW

        Categorías posibles: HATE_SPEECH, SEXUAL_CONTENT, THREAT, SPAM,
        MISINFORMATION, HARASSMENT, SELF_HARM, DISRESPECT
        """;

    private final LlmModerationClient llmClient;
    private final ModerationProperties properties;

    public LlmModerationFilter(LlmModerationClient llmClient, ModerationProperties properties) {
        this.llmClient = llmClient;
        this.properties = properties;
    }

    /**
     * Evalúa el contenido con el LLM configurado.
     *
     * @param text    contenido del comentario o reply
     * @param context contexto adicional (nombre del post, institución, etc.) — puede ser null
     * @return resultado de la evaluación
     */
    public LlmResult evaluate(String text, String context) {
        long start = System.currentTimeMillis();
        double confidenceThreshold = properties.getLlm().getConfidenceThreshold();
        String providerName = properties.getLlm().getProviderName();

        String userMessage = buildUserMessage(text, context);

        try {
            LlmModerationResult result = llmClient.moderate(SYSTEM_PROMPT, userMessage);
            long elapsed = System.currentTimeMillis() - start;

            log.info("LlmModerationFilter [{}]: decision={}, confidence={}, elapsed={}ms",
                    providerName, result.getDecision(),
                    String.format("%.2f", result.getConfidence()), elapsed);

            // Rechazado con suficiente confianza → REJECTED
            if (result.isRejected() && result.getConfidence() >= confidenceThreshold) {
                return LlmResult.rejected(result, elapsed, providerName);
            }

            // Rechazado pero con poca confianza → revisión manual
            if (result.isRejected() && result.getConfidence() < confidenceThreshold) {
                return LlmResult.needsReview(result,
                        "LLM rechazó con baja confianza (" +
                        String.format("%.0f%%", result.getConfidence() * 100) + ")",
                        elapsed, providerName);
            }

            // NEEDS_REVIEW explícito del LLM → revisión manual
            if (result.isNeedsReview()) {
                return LlmResult.needsReview(result, result.getReason(), elapsed, providerName);
            }

            // APPROVED → pasa
            return LlmResult.passed(result, elapsed, providerName);

        } catch (LlmClientException e) {
            // Fail-open: fallo de infraestructura → no bloquear al usuario
            long elapsed = System.currentTimeMillis() - start;
            log.warn("LlmModerationFilter [{}]: client error after {}ms — failing open: {}",
                    providerName, elapsed, e.getMessage());
            return LlmResult.providerError(e.getMessage(), elapsed, providerName);
        }
    }

    /** Versión simplificada sin contexto adicional */
    public LlmResult evaluate(String text) {
        return evaluate(text, null);
    }

    private String buildUserMessage(String text, String context) {
        StringBuilder sb = new StringBuilder();
        sb.append("Comentario a evaluar:\n\"").append(text).append("\"");

        if (context != null && !context.isBlank()) {
            sb.append("\n\nContexto adicional: ").append(context);
        } else {
            sb.append("\n\nContexto: comentario en plataforma institucional educativa.");
        }

        return sb.toString();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Result inner class
    // ─────────────────────────────────────────────────────────────────────────

    public static class LlmResult {

        public enum Status { REJECTED, NEEDS_REVIEW, PASSED, PROVIDER_ERROR }

        private final Status status;
        private final LlmModerationResult llmResult;
        private final String reason;
        private final long processingTimeMs;
        private final String providerName;

        private LlmResult(Status status, LlmModerationResult llmResult,
                          String reason, long ms, String providerName) {
            this.status = status;
            this.llmResult = llmResult;
            this.reason = reason;
            this.processingTimeMs = ms;
            this.providerName = providerName;
        }

        public static LlmResult rejected(LlmModerationResult r, long ms, String provider) {
            return new LlmResult(Status.REJECTED, r, r.getReason(), ms, provider);
        }

        public static LlmResult needsReview(LlmModerationResult r, String reason, long ms, String provider) {
            return new LlmResult(Status.NEEDS_REVIEW, r, reason, ms, provider);
        }

        public static LlmResult passed(LlmModerationResult r, long ms, String provider) {
            return new LlmResult(Status.PASSED, r, null, ms, provider);
        }

        public static LlmResult providerError(String errorMsg, long ms, String provider) {
            return new LlmResult(Status.PROVIDER_ERROR, null,
                    "LLM error: " + errorMsg, ms, provider);
        }

        public boolean isRejected()      { return status == Status.REJECTED; }
        public boolean isNeedsReview()   { return status == Status.NEEDS_REVIEW; }
        public boolean isPassed()        { return status == Status.PASSED || status == Status.PROVIDER_ERROR; }
        public boolean isProviderError() { return status == Status.PROVIDER_ERROR; }

        public Status getStatus()                    { return status; }
        public LlmModerationResult getLlmResult()    { return llmResult; }
        public String getReason()                    { return reason; }
        public long getProcessingTimeMs()            { return processingTimeMs; }
        public String getProviderName()              { return providerName; }

        public double getConfidence() {
            return llmResult != null ? llmResult.getConfidence() : 0.0;
        }
    }
}
