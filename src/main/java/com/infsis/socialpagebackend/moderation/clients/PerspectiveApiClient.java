package com.infsis.socialpagebackend.moderation.clients;

import com.infsis.socialpagebackend.moderation.configuration.ModerationProperties;
import com.infsis.socialpagebackend.moderation.dtos.PerspectiveResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Cliente HTTP para Google Perspective API.
 *
 * Documentación: https://developers.perspectiveapi.com/s/docs-get-started
 *
 * Atributos evaluados:
 *   - TOXICITY:        probabilidad general de ser percibido como rudo o irrespetuoso
 *   - SEVERE_TOXICITY: contenido muy ofensivo o dañino
 *   - INSULT:          lenguaje degradante o humillante
 *   - PROFANITY:       palabras soeces o obscenas
 *   - THREAT:          amenazas de daño físico
 *
 * Idiomas soportados: es, en (y muchos más)
 */
@Slf4j
@Service
public class PerspectiveApiClient {

    private final RestTemplate restTemplate;
    private final ModerationProperties properties;

    public PerspectiveApiClient(RestTemplate restTemplate, ModerationProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    /**
     * Envía el texto a Perspective API y retorna los scores de toxicidad.
     *
     * @param text texto a analizar (comentario o reply)
     * @return PerspectiveResult con los scores parseados
     * @throws PerspectiveApiException si la API falla o devuelve error
     */
    public PerspectiveResult analyze(String text) {
        String apiKey = properties.getPerspective().getApiKey();
        String url    = properties.getPerspective().getUrl() + "?key=" + apiKey;
        double threshold = properties.getPerspective().getToxicityThreshold();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
            "comment", Map.of("text", text),
            "languages", List.of("es", "en"),
            "requestedAttributes", Map.of(
                "TOXICITY",        Map.of(),
                "SEVERE_TOXICITY", Map.of(),
                "INSULT",          Map.of(),
                "PROFANITY",       Map.of(),
                "THREAT",          Map.of()
            )
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            log.debug("PerspectiveApiClient: analyzing text of {} chars", text.length());

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PerspectiveResult result = PerspectiveResult.fromApiResponse(response.getBody(), threshold);
                log.debug("PerspectiveApiClient: maxScore={}, triggered={}",
                        String.format("%.2f", result.getMaxScore()),
                        result.getTriggeredCategories());
                return result;
            }

            log.warn("PerspectiveApiClient: unexpected status {}", response.getStatusCode());
            throw new PerspectiveApiException("Unexpected response status: " + response.getStatusCode());

        } catch (HttpClientErrorException e) {
            // 400: texto vacío, idioma no soportado, etc.
            // 403: API key inválida o cuota excedida
            log.error("PerspectiveApiClient: HTTP error {} — {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new PerspectiveApiException("Perspective API client error: " + e.getMessage(), e);

        } catch (ResourceAccessException e) {
            // Timeout o fallo de red
            log.error("PerspectiveApiClient: connection error — {}", e.getMessage());
            throw new PerspectiveApiException("Perspective API connection error: " + e.getMessage(), e);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Exception
    // ─────────────────────────────────────────────────────────────────────────

    public static class PerspectiveApiException extends RuntimeException {
        public PerspectiveApiException(String message) { super(message); }
        public PerspectiveApiException(String message, Throwable cause) { super(message, cause); }
    }
}
