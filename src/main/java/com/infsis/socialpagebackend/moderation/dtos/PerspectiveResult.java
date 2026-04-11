package com.infsis.socialpagebackend.moderation.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Resultado parseado de la respuesta de Perspective API.
 *
 * Respuesta cruda de la API:
 * {
 *   "attributeScores": {
 *     "TOXICITY":        { "summaryScore": { "value": 0.95 } },
 *     "SEVERE_TOXICITY": { "summaryScore": { "value": 0.62 } },
 *     "INSULT":          { "summaryScore": { "value": 0.88 } },
 *     "PROFANITY":       { "summaryScore": { "value": 0.91 } },
 *     "THREAT":          { "summaryScore": { "value": 0.10 } }
 *   }
 * }
 */
@Data
public class PerspectiveResult {

    private double toxicityScore;
    private double severeToxicityScore;
    private double insultScore;
    private double profanityScore;
    private double threatScore;

    /** El score más alto entre todas las categorías evaluadas */
    private double maxScore;

    /** Categorías que superaron el umbral de toxicidad */
    private List<String> triggeredCategories = new ArrayList<>();

    /**
     * Parsea la respuesta raw de la Perspective API y construye el resultado.
     *
     * @param body respuesta como Map (deserializada por RestTemplate)
     * @param threshold umbral configurado para marcar categorías como triggered
     */
    @SuppressWarnings("unchecked")
    public static PerspectiveResult fromApiResponse(Map<String, Object> body, double threshold) {
        PerspectiveResult result = new PerspectiveResult();

        if (body == null || !body.containsKey("attributeScores")) {
            return result;
        }

        Map<String, Object> attributeScores = (Map<String, Object>) body.get("attributeScores");

        result.toxicityScore       = extractScore(attributeScores, "TOXICITY");
        result.severeToxicityScore = extractScore(attributeScores, "SEVERE_TOXICITY");
        result.insultScore         = extractScore(attributeScores, "INSULT");
        result.profanityScore      = extractScore(attributeScores, "PROFANITY");
        result.threatScore         = extractScore(attributeScores, "THREAT");

        result.maxScore = Math.max(
            Math.max(result.toxicityScore, result.severeToxicityScore),
            Math.max(result.insultScore,
                Math.max(result.profanityScore, result.threatScore))
        );

        if (result.toxicityScore >= threshold)       result.triggeredCategories.add("TOXICITY");
        if (result.severeToxicityScore >= threshold) result.triggeredCategories.add("SEVERE_TOXICITY");
        if (result.insultScore >= threshold)         result.triggeredCategories.add("INSULT");
        if (result.profanityScore >= threshold)      result.triggeredCategories.add("PROFANITY");
        if (result.threatScore >= threshold)         result.triggeredCategories.add("THREAT");

        return result;
    }

    public boolean isAboveThreshold(double threshold) {
        return maxScore >= threshold;
    }

    @SuppressWarnings("unchecked")
    private static double extractScore(Map<String, Object> attributeScores, String attribute) {
        if (!attributeScores.containsKey(attribute)) return 0.0;

        Map<String, Object> attrData = (Map<String, Object>) attributeScores.get(attribute);
        Map<String, Object> summaryScore = (Map<String, Object>) attrData.get("summaryScore");

        if (summaryScore == null) return 0.0;

        Object value = summaryScore.get("value");
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }
}
