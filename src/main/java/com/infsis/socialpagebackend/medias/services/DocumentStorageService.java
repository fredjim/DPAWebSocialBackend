package com.infsis.socialpagebackend.medias.services;

import com.infsis.socialpagebackend.configuration.AppUrlProperties;
import com.infsis.socialpagebackend.configuration.FileStorageProperties;
import com.infsis.socialpagebackend.configuration.ServerProperties;
import com.infsis.socialpagebackend.enums.*;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.medias.dtos.DocumentFileDTO;
import com.infsis.socialpagebackend.medias.exceptions.FileStorageException;
import com.infsis.socialpagebackend.medias.exceptions.InvalidFileException;
import com.infsis.socialpagebackend.medias.mappers.DocumentFileMapper;
import com.infsis.socialpagebackend.medias.models.DocumentFile;
import com.infsis.socialpagebackend.medias.repositories.DocumentFileRepository;
import com.infsis.socialpagebackend.medias.storage.FileStorageStrategy;
import com.infsis.socialpagebackend.posts.repositories.MediaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Service
public class DocumentStorageService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentStorageService.class);

    @Autowired
    private DocumentFileRepository documentFileRepository;

    @Autowired
    private DocumentFileMapper documentFileMapper;

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Autowired
    private AppUrlProperties appUrlProperties;

    @Autowired
    @Qualifier("localFileStorage")
    private FileStorageStrategy fileStorageStrategy;

    public DocumentFileDTO getDocument(String documentUuid) {
        logger.info("Retrieving document with UUID: {}", documentUuid);

        DocumentFile documentFile = documentFileRepository.findOneByUuid(documentUuid);
        if(documentFile == null) {
            logger.warn("Document not found with UUID: {}", documentUuid);
            throw new NotFoundException("Document", documentUuid);
        }

        logger.info("Document retrieved successfully: {}", documentUuid);
        return documentFileMapper.toDTO(documentFile);
    }

    public Resource getDocumentResource(String documentUuid) throws IOException {
        logger.info("Loading document resource for UUID: {}", documentUuid);

        DocumentFile documentFile = documentFileRepository.findOneByUuid(documentUuid);
        if(documentFile == null) {
            logger.warn("Document not found with UUID: {}", documentUuid);
            throw new NotFoundException("Document", documentUuid);
        }

        try {
            Resource resource = fileStorageStrategy.loadFileAsResource(documentUuid, fileStorageProperties.getDocumentUploadDir());
            logger.info("Document resource loaded successfully: {}", documentUuid);
            return resource;
        } catch (IOException e) {
            logger.error("Error loading document resource for UUID: {}", documentUuid, e);
            throw new FileStorageException("Error loading document: " + documentUuid, e);
        }
    }

    public String getDocumentContentType(String documentUuid) throws IOException {
        try {
            return fileStorageStrategy.getContentType(documentUuid, fileStorageProperties.getDocumentUploadDir());
        } catch (IOException e) {
            logger.error("Error determining content type for document: {}", documentUuid, e);
            throw new FileStorageException("Error determining content type for document: " + documentUuid, e);
        }
    }

    @Transactional
    public DocumentFileDTO storeFile(MultipartFile file) {
        logger.info("Starting file upload process for: {}", file.getOriginalFilename());

        // Validate file
        validateFile(file);

        String uniqueFileName = UUID.randomUUID().toString();
        logger.info("Generated UUID for file: {} -> {}", file.getOriginalFilename(), uniqueFileName);

        try {
            // Store file using strategy
            String storedFilePath = fileStorageStrategy.storeFile(
                file,
                fileStorageProperties.getDocumentUploadDir(),
                uniqueFileName
            );

            // Build download URL using configuration
            String downloadUrl = appUrlProperties.buildDocumentUrl(uniqueFileName);

            // Create DTO
            DocumentFileDTO documentFileDTO = new DocumentFileDTO();
            documentFileDTO.setUuid(uniqueFileName);
            documentFileDTO.setName(file.getOriginalFilename());
            documentFileDTO.setStatus(FileStatus.SAVED_SUCCESSFULLY.name());
            documentFileDTO.setType(file.getContentType());
            documentFileDTO.setUrlResource(downloadUrl);

            // Save to database
            documentFileRepository.save(documentFileMapper.getFile(documentFileDTO));

            logger.info("File uploaded successfully: {} (size: {} bytes)",
                       uniqueFileName, file.getSize());

            return documentFileDTO;

        } catch (IOException e) {
            logger.error("Error storing file: {}", file.getOriginalFilename(), e);
            throw new FileStorageException("Error storing file: " + file.getOriginalFilename(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }

        if (file.getSize() > fileStorageProperties.getMaxFileSize()) {
            throw new InvalidFileException("File size exceeds maximum allowed size of " +
                                         fileStorageProperties.getMaxFileSize() + " bytes");
        }

        String contentType = file.getContentType();
        if (contentType == null || !fileStorageProperties.getAllowedDocumentTypes().contains(contentType)) {
            throw new InvalidFileException("File type not allowed: " + contentType);
        }

        logger.debug("File validation passed for: {}", file.getOriginalFilename());
    }

    @Transactional
    public void deleteDocument(String documentUuid) {
        logger.info("Starting document deletion for UUID: {}", documentUuid);

        DocumentFile documentFile = documentFileRepository.findOneByUuid(documentUuid);
        if (documentFile == null) {
            logger.warn("Document not found for deletion with UUID: {}", documentUuid);
            throw new NotFoundException("Document", documentUuid);
        }

        String urlResource = documentFile.getUrl_resource();

        try {
            // Delete file from storage
            fileStorageStrategy.deleteFile(documentUuid, fileStorageProperties.getDocumentUploadDir());
            logger.info("File deleted from storage: {}", documentUuid);

            // Delete from database
            documentFileRepository.delete(documentFile);
            logger.info("Document record deleted from database: {}", documentUuid);

            // Delete media reference
            mediaRepository.deleteByFilePath(urlResource);
            logger.info("Media reference deleted: {}", urlResource);

            logger.info("Document deletion completed successfully: {}", documentUuid);

        } catch (IOException e) {
            logger.error("Error deleting document: {}", documentUuid, e);
            throw new FileStorageException("Error deleting document: " + documentUuid, e);
        }
    }
}