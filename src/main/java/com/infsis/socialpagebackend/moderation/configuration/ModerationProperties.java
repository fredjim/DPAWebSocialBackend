package com.infsis.socialpagebackend.moderation.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuración estática del pipeline de moderación.
 * Los valores se leen de application.properties y pueden sobreescribirse
 * con variables de entorno.
 */
@Data
@Component
@ConfigurationProperties(prefix = "moderation")
public class ModerationProperties {

    private Blacklist blacklist = new Blacklist();
    private Perspective perspective = new Perspective();
    private Llm llm = new Llm();

    @Data
    public static class Blacklist {
        /** Habilitar/deshabilitar el filtro de blacklist */
        private boolean enabled = true;
    }

    @Data
    public static class Perspective {
        /** Habilitar/deshabilitar el filtro de Perspective API */
        private boolean enabled = true;

        /** API Key de Google Cloud (Perspective API) */
        private String apiKey = "";

        /** Endpoint de la API */
        private String url = "https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze";

        /**
         * Umbral de toxicidad para rechazar (0.0 - 1.0).
         * 0.7 = rechaza si alguna categoría supera el 70% de probabilidad de toxicidad.
         */
        private double toxicityThreshold = 0.7;

        /** Timeout en ms para las llamadas a la API */
        private int timeoutMs = 5000;

        /** Longitud mínima del texto para llamar a la API (textos muy cortos no vale la pena) */
        private int minTextLength = 3;
    }

    @Data
    public static class Llm {
        /** Habilitar/deshabilitar el filtro LLM */
        private boolean enabled = true;

        /**
         * Endpoint base compatible con OpenAI Chat Completions API.
         *
         * Ejemplos:
         *   Local (Ollama):  http://localhost:11434/v1
         *   DeepSeek:        https://api.deepseek.com/v1
         *   OpenAI:          https://api.openai.com/v1
         */
        private String endpoint = "http://localhost:11434/v1";

        /**
         * API Key del proveedor.
         * Para Ollama local puede dejarse vacío o poner cualquier valor.
         */
        private String apiKey = "";

        /**
         * Modelo a usar. Depende del proveedor activo.
         *
         * Ejemplos:
         *   Ollama local:    llama3.2 / mistral / qwen2.5
         *   DeepSeek:        deepseek-chat (económico y rápido)
         *   OpenAI:          gpt-4o-mini (el más económico de OpenAI)
         */
        private String model = "llama3.2";

        /**
         * Nombre descriptivo del proveedor activo — solo para logs y auditoría.
         * Ejemplos: "ollama-local", "deepseek", "openai"
         */
        private String providerName = "ollama-local";

        /**
         * Umbral de confianza mínimo para aceptar la decisión del LLM (0.0 - 1.0).
         * Si el score es menor, el contenido pasa a revisión manual en lugar de aprobarse.
         */
        private double confidenceThreshold = 0.8;

        /** Máximo de tokens a generar en la respuesta del LLM */
        private int maxTokens = 256;

        /** Temperatura del modelo — 0.1 para respuestas deterministas */
        private double temperature = 0.1;

        /** Timeout en ms para las llamadas al LLM */
        private int timeoutMs = 15000;
    }
}
