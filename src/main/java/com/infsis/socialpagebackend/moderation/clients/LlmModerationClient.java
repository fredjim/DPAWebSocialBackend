package com.infsis.socialpagebackend.moderation.clients;

import com.infsis.socialpagebackend.moderation.configuration.ModerationProperties;
import com.infsis.socialpagebackend.moderation.dtos.LlmModerationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Cliente HTTP universal para cualquier LLM compatible con la API de OpenAI
 * (Chat Completions endpoint).
 *
 * Proveedores compatibles (cambiar solo endpoint + apiKey + model en properties):
 *
 *   Ollama (local)  → endpoint: http://localhost:11434/v1
 *                     apiKey:   cualquier valor (o vacío)
 *                     model:    llama3.2 | mistral | qwen2.5 | phi4 | etc.
 *
 *   DeepSeek        → endpoint: https://api.deepseek.com/v1
 *                     apiKey:   sk-xxx (desde platform.deepseek.com)
 *                     model:    deepseek-chat (económico) | deepseek-reasoner
 *
 *   OpenAI          → endpoint: https://api.openai.com/v1
 *                     apiKey:   sk-xxx (desde platform.openai.com)
 *                     model:    gpt-4o-mini (económico) | gpt-4o
 *
 *   Groq (rápido)   → endpoint: https://api.groq.com/openai/v1
 *                     apiKey:   gsk_xxx (desde console.groq.com)
 *                     model:    llama-3.3-70b-versatile | mixtral-8x7b-32768
 *
 * Para cambiar de proveedor basta con actualizar las variables de entorno:
 *   LLM_ENDPOINT / LLM_API_KEY / LLM_MODEL
 */
@Slf4j
@Service
public class LlmModerationClient {

    private static final String CHAT_COMPLETIONS_PATH = "/chat/completions";

    private final RestTemplate restTemplate;
    private final ModerationProperties properties;

    public LlmModerationClient(RestTemplate restTemplate, ModerationProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    /**
     * Llama al LLM configurado con el system prompt y el mensaje del usuario.
     *
     * @param systemPrompt instrucciones de moderación para el LLM
     * @param userMessage  contenido a evaluar
     * @return resultado de moderación parseado del JSON del LLM
     * @throws LlmClientException si la llamada falla
     */
    public LlmModerationResult moderate(String systemPrompt, String userMessage) {
        ModerationProperties.Llm config = properties.getLlm();
        String url = config.getEndpoint().stripTrailing() + CHAT_COMPLETIONS_PATH;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // API key: para Ollama local puede ser cualquier string
        String apiKey = config.getApiKey();
        if (apiKey != null && !apiKey.isBlank()) {
            headers.setBearerAuth(apiKey);
        }

        Map<String, Object> body = Map.of(
            "model",       config.getModel(),
            "max_tokens",  config.getMaxTokens(),
            "temperature", config.getTemperature(),
            "messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user",   "content", userMessage)
            )
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            log.debug("LlmModerationClient: calling {} with model={}",
                    config.getProviderName(), config.getModel());

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                LlmModerationResult result = LlmModerationResult.fromApiResponse(response.getBody());
                log.debug("LlmModerationClient: decision={}, confidence={}",
                        result.getDecision(), result.getConfidence());
                return result;
            }

            throw new LlmClientException("Unexpected status: " + response.getStatusCode());

        } catch (HttpClientErrorException e) {
            log.error("LlmModerationClient: HTTP {} from provider '{}' — {}",
                    e.getStatusCode(), config.getProviderName(), e.getResponseBodyAsString());
            throw new LlmClientException(
                    "LLM HTTP error from " + config.getProviderName() + ": " + e.getMessage(), e);

        } catch (ResourceAccessException e) {
            log.error("LlmModerationClient: connection error to '{}' — {}",
                    config.getProviderName(), e.getMessage());
            throw new LlmClientException(
                    "LLM connection error to " + config.getProviderName() + ": " + e.getMessage(), e);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Exception
    // ─────────────────────────────────────────────────────────────────────────

    public static class LlmClientException extends RuntimeException {
        public LlmClientException(String message) { super(message); }
        public LlmClientException(String message, Throwable cause) { super(message, cause); }
    }
}
