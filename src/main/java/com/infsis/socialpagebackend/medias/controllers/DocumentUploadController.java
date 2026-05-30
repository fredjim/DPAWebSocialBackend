package com.infsis.socialpagebackend.medias.controllers;

import com.infsis.socialpagebackend.enums.FileCategory;
import com.infsis.socialpagebackend.medias.dtos.UploadedFileDTO;
import com.infsis.socialpagebackend.medias.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@Validated
public class DocumentUploadController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentUploadController.class);

    private static final String DOCUMENTS_DIRECTORY = System.getProperty("user.dir") + "/storage/institution/posts/documents/";
    private static final String DOCUMENTS_PATH      = "/api/v1/documents/";

    @Autowired
    private FileStorageService fileStorageService;

    @PreAuthorize("hasAuthority('UPLOAD_DOCUMENT')")
    @PostMapping("/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UploadedFileDTO> handleFileUploadDocs(
            @RequestParam(name = "files") List<MultipartFile> files) {
        return fileStorageService.storeFiles(files, FileCategory.DOCUMENT, DOCUMENTS_DIRECTORY, DOCUMENTS_PATH);
    }

    @PreAuthorize("hasAuthority('UPLOAD_DOCUMENT')")
    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UploadedFileDTO> handleFileUpload(
            @RequestParam(name = "files") List<MultipartFile> files) {
        return fileStorageService.storeFiles(files, FileCategory.DOCUMENT, DOCUMENTS_DIRECTORY, DOCUMENTS_PATH);
    }

    @GetMapping("/{documentUuid}/info")
    public UploadedFileDTO getDocumentInfo(@PathVariable String documentUuid) {
        logger.info("Document info request for UUID: {}", documentUuid);
        return fileStorageService.getFileInfo(documentUuid);
    }

    @GetMapping("/{documentUuid}")
    public ResponseEntity<Resource> getResourceDocument(@PathVariable String documentUuid) throws IOException {
        logger.info("Document download request for UUID: {}", documentUuid);

        UploadedFileDTO info = fileStorageService.getFileInfo(documentUuid);
        Resource resource = fileStorageService.loadAsResource(documentUuid, DOCUMENTS_DIRECTORY);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(info.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + info.getName() + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "max-age=31536000")
                .body(resource);
    }

    @PreAuthorize("hasAuthority('DELETE_DOCUMENT')")
    @DeleteMapping("/{documentUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(@PathVariable String documentUuid) {
        logger.info("Document deletion request for UUID: {}", documentUuid);
        fileStorageService.deleteFile(documentUuid, DOCUMENTS_DIRECTORY);
    }
}
