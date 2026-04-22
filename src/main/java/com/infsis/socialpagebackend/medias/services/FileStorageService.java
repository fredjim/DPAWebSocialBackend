package com.infsis.socialpagebackend.medias.services;

import com.infsis.socialpagebackend.enums.FileCategory;
import com.infsis.socialpagebackend.enums.FileStatus;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.medias.dtos.UploadedFileDTO;
import com.infsis.socialpagebackend.medias.exceptions.FileStorageException;
import com.infsis.socialpagebackend.medias.mappers.UploadedFileMapper;
import com.infsis.socialpagebackend.medias.models.UploadedFile;
import com.infsis.socialpagebackend.medias.repositories.UploadedFileRepository;
import com.infsis.socialpagebackend.medias.storage.FileStorageStrategy;
import com.infsis.socialpagebackend.posts.repositories.MediaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Autowired
    private UploadedFileMapper uploadedFileMapper;

    @Autowired
    @Qualifier("localFileStorage")
    private FileStorageStrategy fileStorageStrategy;

    @Autowired
    private MediaRepository mediaRepository;

    /**
     * Store a batch of files.
     *
     * @param files    files to store
     * @param category FILE_CATEGORY discriminator (IMAGE, VIDEO, DOCUMENT)
     * @param directory absolute path on disk where files will be written
     * @param apiPath  relative API path prefix used to build urlResource (e.g. "/api/v1/images/posts/")
     */
    @Transactional
    public List<UploadedFileDTO> storeFiles(List<MultipartFile> files, FileCategory category,
                                            String directory, String apiPath) {
        logger.info("Storing {} file(s) [category={}]", files.size(), category);
        List<UploadedFileDTO> results = new ArrayList<>();
        for (MultipartFile file : files) {
            results.add(processSingleFile(file, category, directory, apiPath));
        }
        logger.info("Stored {} file(s) successfully", results.size());
        return results;
    }

    /**
     * Convenience wrapper for a single file.
     */
    @Transactional
    public UploadedFileDTO storeFile(MultipartFile file, FileCategory category,
                                     String directory, String apiPath) {
        return storeFiles(List.of(file), category, directory, apiPath).get(0);
    }

    /**
     * Return the metadata record for a stored file.
     */
    public UploadedFileDTO getFileInfo(String uuid) {
        logger.info("Retrieving file info for uuid={}", uuid);
        UploadedFile file = uploadedFileRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("File", uuid));
        return uploadedFileMapper.toDTO(file);
    }

    /**
     * Load the raw {@link Resource} for a stored file.
     * Use this when the controller needs to build a custom HTTP response.
     */
    public Resource loadAsResource(String uuid, String directory) throws IOException {
        uploadedFileRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("File", uuid));
        try {
            return fileStorageStrategy.loadFileAsResource(uuid, directory);
        } catch (IOException e) {
            throw new FileStorageException("Error loading file: " + uuid, e);
        }
    }

    /**
     * Build a ready-to-return HTTP response for a stored file.
     * Detects content type from the {@link UploadedFile} record.
     */
    public ResponseEntity<Resource> getFileResponse(String uuid, String directory) {
        UploadedFile file = uploadedFileRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("File", uuid));
        try {
            Resource resource = fileStorageStrategy.loadFileAsResource(uuid, directory);
            MediaType contentType = resolveMediaType(file.getMimeType());
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(resource);
        } catch (IOException e) {
            logger.error("Error loading file uuid={}", uuid, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Delete a stored file from disk, its Media row, and the UploadedFile record.
     * Used by the individual DELETE /images, /videos, /documents endpoints.
     */
    @Transactional
    public void deleteFile(String uuid, String directory) {
        logger.info("Deleting file uuid={}", uuid);
        UploadedFile file = uploadedFileRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("File", uuid));
        try {
            mediaRepository.findByUploadedFileUuid(uuid)
                    .ifPresent(mediaRepository::delete);
            fileStorageStrategy.deleteFile(uuid, directory);
            uploadedFileRepository.delete(file);
            logger.info("File deleted uuid={}", uuid);
        } catch (IOException e) {
            throw new FileStorageException("Error deleting file: " + uuid, e);
        }
    }

    /**
     * Delete only the physical file and the UploadedFile record.
     * The caller is responsible for deleting the Media row separately.
     * Used when deleting a Post, Article, or individual Media during update.
     */
    @Transactional
    public void deleteFileOnly(String uuid, String directory) {
        logger.info("Deleting file (only) uuid={}", uuid);
        UploadedFile file = uploadedFileRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("File", uuid));
        try {
            fileStorageStrategy.deleteFile(uuid, directory);
            uploadedFileRepository.delete(file);
            logger.info("File (only) deleted uuid={}", uuid);
        } catch (IOException e) {
            logger.warn("Error deleting physical file uuid={}, removing DB record anyway", uuid);
            uploadedFileRepository.delete(file);
        }
    }

    // ── private helpers ──────────────────────────────────────────────────────

    private UploadedFileDTO processSingleFile(MultipartFile file, FileCategory category,
                                              String directory, String apiPath) {
        String uuid = UUID.randomUUID().toString();
        logger.info("Processing file name={} uuid={}", file.getOriginalFilename(), uuid);
        try {
            fileStorageStrategy.storeFile(file, directory, uuid);

            String relativePath = apiPath + uuid;

            UploadedFile entity = new UploadedFile();
            entity.setUuid(uuid);
            entity.setName(file.getOriginalFilename());
            entity.setUrlResource(relativePath);
            entity.setCategory(category);
            entity.setMimeType(file.getContentType());
            entity.setStatus(FileStatus.SAVED_SUCCESSFULLY.name());

            uploadedFileRepository.save(entity);
            logger.info("File stored uuid={} size={} bytes", uuid, file.getSize());
            return uploadedFileMapper.toDTO(entity);
        } catch (IOException e) {
            throw new FileStorageException("Error storing file: " + file.getOriginalFilename(), e);
        }
    }

    private MediaType resolveMediaType(String mimeType) {
        if (mimeType == null) return MediaType.APPLICATION_OCTET_STREAM;
        try {
            return MediaType.parseMediaType(mimeType);
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
