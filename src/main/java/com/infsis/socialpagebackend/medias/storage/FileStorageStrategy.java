package com.infsis.socialpagebackend.medias.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Storage strategy interface for file operations.
 * This abstraction allows easy switching between local file system,
 * AWS S3, Azure Blob Storage, or other cloud storage providers.
 */
public interface FileStorageStrategy {

    /**
     * Store a file in the specified directory
     *
     * @param file the file to store
     * @param directory the target directory
     * @param fileName the name to give to the file (usually UUID)
     * @return the stored file path or URL
     * @throws IOException if storage operation fails
     */
    String storeFile(MultipartFile file, String directory, String fileName) throws IOException;

    /**
     * Load a file as a Resource
     *
     * @param fileName the name of the file to load
     * @param directory the directory where the file is located
     * @return the file as a Resource
     * @throws IOException if file loading fails
     */
    Resource loadFileAsResource(String fileName, String directory) throws IOException;

    /**
     * Delete a file from storage
     *
     * @param fileName the name of the file to delete
     * @param directory the directory where the file is located
     * @throws IOException if deletion fails
     */
    void deleteFile(String fileName, String directory) throws IOException;

    /**
     * Check if a file exists in storage
     *
     * @param fileName the name of the file to check
     * @param directory the directory to check in
     * @return true if file exists, false otherwise
     */
    boolean fileExists(String fileName, String directory);

    /**
     * Get the MIME type of a file
     *
     * @param fileName the name of the file
     * @param directory the directory where the file is located
     * @return the MIME type of the file
     * @throws IOException if cannot determine MIME type
     */
    String getContentType(String fileName, String directory) throws IOException;
}
