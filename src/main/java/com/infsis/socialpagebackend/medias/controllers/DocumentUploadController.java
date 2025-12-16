package com.infsis.socialpagebackend.medias.controllers;

import com.infsis.socialpagebackend.medias.dtos.DocumentFileDTO;
import com.infsis.socialpagebackend.medias.services.DocumentStorageService;
import com.infsis.socialpagebackend.validation.ValidDocumentFile;
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

@RestController
@RequestMapping("/api/v1/documents")
@Validated
public class DocumentUploadController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentUploadController.class);

    @Autowired
    private DocumentStorageService documentStorageService;

    @PreAuthorize("hasAuthority('UPLOAD_DOCUMENT')")
    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentFileDTO handleFileUpload(@RequestParam("file") @ValidDocumentFile MultipartFile file) {
        logger.info("Document upload request received: {}", file.getOriginalFilename());
        return documentStorageService.storeFile(file);
    }

    @GetMapping("/{documentUuid}/info")
    public DocumentFileDTO getDocumentInfo(@PathVariable String documentUuid) {
        logger.info("Document info request for UUID: {}", documentUuid);
        return documentStorageService.getDocument(documentUuid);
    }

    @GetMapping("/{documentUuid}")
    public ResponseEntity<Resource> getResourceDocument(@PathVariable String documentUuid) throws IOException {
        logger.info("Document download request for UUID: {}", documentUuid);

        Resource resource = documentStorageService.getDocumentResource(documentUuid);
        String contentType = documentStorageService.getDocumentContentType(documentUuid);

        // Get document info for additional headers
        DocumentFileDTO documentInfo = documentStorageService.getDocument(documentUuid);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                       "inline; filename=\"" + documentInfo.getName() + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "max-age=31536000") // 1 year cache
                .body(resource);
    }

    @PreAuthorize("hasAuthority('DELETE_DOCUMENT')")
    @DeleteMapping("/{documentUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(@PathVariable String documentUuid) {
        logger.info("Document deletion request for UUID: {}", documentUuid);
        documentStorageService.deleteDocument(documentUuid);
    }
}