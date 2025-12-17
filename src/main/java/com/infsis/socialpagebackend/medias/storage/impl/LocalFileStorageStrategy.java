package com.infsis.socialpagebackend.medias.storage.impl;

import com.infsis.socialpagebackend.medias.storage.FileStorageStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Local file system implementation of FileStorageStrategy.
 * Stores files in the local file system using java.nio.file operations.
 */
@Service("localFileStorage")
public class LocalFileStorageStrategy implements FileStorageStrategy {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileStorageStrategy.class);

    @Override
    public String storeFile(MultipartFile file, String directory, String fileName) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Cannot store empty file: " + fileName);
        }

        logger.info("Storing file: {} in directory: {}", fileName, directory);

        // Create directory if it doesn't exist
        Path uploadPath = Paths.get(directory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            logger.info("Created directory: {}", uploadPath);
        }

        // Resolve the file path
        Path filePath = uploadPath.resolve(fileName);

        // Ensure we don't overwrite existing files
        if (Files.exists(filePath)) {
            logger.warn("File already exists, will be overwritten: {}", filePath);
        }

        // Copy file to the target location
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        logger.info("Successfully stored file: {} (size: {} bytes)", fileName, file.getSize());
        return filePath.toString();
    }

    @Override
    public Resource loadFileAsResource(String fileName, String directory) throws IOException {
        try {
            Path filePath = Paths.get(directory).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                logger.info("Loading file: {}", filePath);
                return resource;
            } else {
                logger.error("File not found or not readable: {}", filePath);
                throw new IOException("File not found or not readable: " + fileName);
            }
        } catch (MalformedURLException e) {
            logger.error("Malformed URL for file: {}", fileName, e);
            throw new IOException("Malformed URL for file: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(String fileName, String directory) throws IOException {
        Path filePath = Paths.get(directory).resolve(fileName);

        logger.info("Attempting to delete file: {}", filePath);

        boolean deleted = Files.deleteIfExists(filePath);
        if (deleted) {
            logger.info("Successfully deleted file: {}", filePath);
        } else {
            logger.warn("File not found for deletion: {}", filePath);
        }
    }

    @Override
    public boolean fileExists(String fileName, String directory) {
        Path filePath = Paths.get(directory).resolve(fileName);
        boolean exists = Files.exists(filePath);

        logger.debug("File exists check for {}: {}", filePath, exists);
        return exists;
    }

    @Override
    public String getContentType(String fileName, String directory) throws IOException {
        Path filePath = Paths.get(directory).resolve(fileName);

        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + fileName);
        }

        try {
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                // Fallback to determine content type based on file extension
                contentType = determineContentTypeByExtension(fileName);
            }

            logger.debug("Determined content type for {}: {}", fileName, contentType);
            return contentType;
        } catch (IOException e) {
            logger.error("Error determining content type for file: {}", fileName, e);
            throw e;
        }
    }

    /**
     * Fallback method to determine content type based on file extension
     */
    private String determineContentTypeByExtension(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();

        return switch (extension) {
            case "pdf" -> "application/pdf";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            case "mp4" -> "video/mp4";
            case "avi" -> "video/avi";
            case "mov" -> "video/mov";
            case "wmv" -> "video/wmv";
            default -> "application/octet-stream";
        };
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex + 1);
    }
}
