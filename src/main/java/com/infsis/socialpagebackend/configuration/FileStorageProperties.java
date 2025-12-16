package com.infsis.socialpagebackend.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties for file storage settings.
 * Centralizes all file storage related configurations to avoid duplication
 * and make it easy to switch between local and cloud storage.
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageProperties {

    /**
     * Base directory for document uploads
     */
    private String documentUploadDir = System.getProperty("user.dir") + "/storage/institution/posts/documents/";

    /**
     * Base directory for image uploads
     */
    private String imageUploadDir = System.getProperty("user.dir") + "/storage/institution/posts/photos/";

    /**
     * Base directory for video uploads
     */
    private String videoUploadDir = System.getProperty("user.dir") + "/storage/institution/posts/videos/";

    /**
     * Base directory for user profile photos
     */
    private String userProfileUploadDir = System.getProperty("user.dir") + "/storage/users/photos/";

    /**
     * Maximum file size in bytes (default: 50MB)
     */
    private long maxFileSize = 52428800;

    /**
     * Maximum request size in bytes (default: 100MB)
     */
    private long maxRequestSize = 104857600;

    /**
     * Allowed document types
     */
    private List<String> allowedDocumentTypes = List.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    /**
     * Allowed image types
     */
    private List<String> allowedImageTypes = List.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    );

    /**
     * Allowed video types
     */
    private List<String> allowedVideoTypes = List.of(
            "video/mp4",
            "video/avi",
            "video/mov",
            "video/wmv"
    );
}

