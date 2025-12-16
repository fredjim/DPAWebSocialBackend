package com.infsis.socialpagebackend.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for application URLs.
 * Allows flexible URL configuration for different environments
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppUrlProperties {

    /**
     * Base URL for the application (environment specific)
     * Examples:
     * - Development: http://localhost:9091
     * - Production: https://api.midominio.com
     * - Using environment variable: ${BASE_URL:http://localhost:9091}
     */
    private String baseUrl = "http://localhost:9091";

    /**
     * Whether to use relative URLs instead of absolute URLs
     * Useful for CDN, reverse proxy, or load balancer scenarios
     */
    private boolean useRelativeUrls = false;

    /**
     * API configurations
     */
    private Api api = new Api();

    @Data
    public static class Api {
        /**
         * Base path for API endpoints
         */
        private String basePath = "/api/v1";

        /**
         * Path for document endpoints
         */
        private String documentsPath = "/documents";

        /**
         * Path for image endpoints
         */
        private String imagesPath = "/images";

        /**
         * Path for video endpoints
         */
        private String videosPath = "/videos";

        /**
         * Get full document API path
         */
        public String getFullDocumentsPath() {
            return basePath + documentsPath;
        }

        /**
         * Get full images API path
         */
        public String getFullImagesPath() {
            return basePath + imagesPath;
        }

        /**
         * Get full videos API path
         */
        public String getFullVideosPath() {
            return basePath + videosPath;
        }
    }

    /**
     * Build complete URL for a document
     * @param documentUuid the document UUID
     * @return complete URL (absolute or relative based on configuration)
     */
    public String buildDocumentUrl(String documentUuid) {
        String path = api.getFullDocumentsPath() + "/" + documentUuid;

        if (useRelativeUrls) {
            return path;
        } else {
            return baseUrl + path;
        }
    }

    /**
     * Build complete URL for any resource
     * @param resourcePath the resource path (should start with /)
     * @return complete URL (absolute or relative based on configuration)
     */
    public String buildResourceUrl(String resourcePath) {
        if (useRelativeUrls) {
            return resourcePath;
        } else {
            return baseUrl + resourcePath;
        }
    }
}
