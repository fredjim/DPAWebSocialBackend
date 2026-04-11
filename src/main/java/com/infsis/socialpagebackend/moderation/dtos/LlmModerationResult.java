package com.infsis.socialpagebackend.moderation.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Resultado parseado de la respuesta del LLM.
 *
 * El LLM debe responder EXCLUSIVAMENTE en este JSON:
 * {
 *   "decision":   "APPROVED" | "REJECTED" | "NEEDS_REVIEW",
 *   "confidence": 0.0 - 1.0,
 *   "reason":     "explicación breve en español",
 *   "categories": ["HATE_SPEECH", "THREAT", ...]
 * }
 */
@Slf4j
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LlmModerationResult {

    public enum Decision { APPROVED, REJECTED, NEEDS_REVIEW }

    @JsonProperty("decision")
    private Decision decision = Decision.NEEDS_REVIEW;

    @JsonProperty("confidence")
    private double confidence = 0.0;

    @JsonProperty("reason")
    private String reason = "";

    @JsonProperty("categories")
    private List<String> categories = new ArrayList<>();

    // ─────────────────────────────────────────────────────────────────────────
    // Parsing
    // ─────────────────────────────────────────────────────────────────────────

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Parsea la respuesta raw de la API OpenAI-compatible y extrae el JSON del LLM.
     *
     * Respuesta de la API:
     * {
     *   "choices": [
     *     { "message": { "content": "{\"decision\": \"APPROVED\", ...}" } }
     *   ]
     * }
     */
    @SuppressWarnings("unchecked")
    public static LlmModerationResult fromApiResponse(Map<String, Object> apiResponse) {
        try {
            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) apiResponse.get("choices");

            if (choices == null || choices.isEmpty()) {
                log.warn("LlmModerationResult: no choices in API response");
                return fallback("No choices in API response");
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            if (message == null) {
                log.warn("LlmModerationResult: no message in first choice");
                return fallback("No message in first choice");
            }

            String content = (String) message.get("content");
            if (content == null || content.isBlank()) {
                log.warn("LlmModerationResult: empty content from LLM");
                return fallback("Empty content from LLM");
            }

            // El LLM puede responder con markdown (```json ... ```) — extraer solo el JSON
            String json = extractJson(content);
            return MAPPER.readValue(json, LlmModerationResult.class);

        } catch (Exception e) {
            log.error("LlmModerationResult: error parsing LLM response — {}", e.getMessage());
            return fallback("Error parsing LLM response: " + e.getMessage());
        }
    }

    /**
     * Resultado de fallback cuando no se puede parsear la respuesta.
     * Devuelve NEEDS_REVIEW para que un moderador humano revise.
     */
    private static LlmModerationResult fallback(String reason) {
        LlmModerationResult result = new LlmModerationResult();
        result.setDecision(Decision.NEEDS_REVIEW);
        result.setConfidence(0.0);
        result.setReason("LLM parse error — enviado a revisión manual: " + reason);
        return result;
    }

    /**
     * Extrae el JSON de la respuesta del LLM, que puede venir envuelta en markdown:
     * ```json
     * { ... }
     * ```
     */
    private static String extractJson(String content) {
        String trimmed = content.trim();

        // Quitar bloques de markdown ```json ... ``` o ``` ... ```
        if (trimmed.startsWith("```")) {
            int start = trimmed.indexOf('\n');
            int end = trimmed.lastIndexOf("```");
            if (start != -1 && end > start) {
                return trimmed.substring(start, end).trim();
            }
        }

        // Intentar encontrar el primer { y último } por si hay texto extra
        int firstBrace = trimmed.indexOf('{');
        int lastBrace  = trimmed.lastIndexOf('}');
        if (firstBrace != -1 && lastBrace > firstBrace) {
            return trimmed.substring(firstBrace, lastBrace + 1);
        }

        return trimmed;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    public boolean isApproved()    { return decision == Decision.APPROVED; }
    public boolean isRejected()    { return decision == Decision.REJECTED; }
    public boolean isNeedsReview() { return decision == Decision.NEEDS_REVIEW; }
}
