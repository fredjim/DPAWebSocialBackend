package com.infsis.socialpagebackend.medias.controllers;

import com.infsis.socialpagebackend.enums.FileCategory;
import com.infsis.socialpagebackend.medias.dtos.UploadedFileDTO;
import com.infsis.socialpagebackend.medias.services.FileStorageService;
import com.infsis.socialpagebackend.validation.ValidVideoFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/videos")
@Validated
public class VideoUploadController {

    private static final String VIDEOS_DIRECTORY = System.getProperty("user.dir") + "/storage/institution/posts/videos/";
    private static final String VIDEOS_PATH      = "/api/v1/videos/posts/";

    @Autowired
    private FileStorageService fileStorageService;

    @PreAuthorize("hasAuthority('UPLOAD_VIDEO')")
    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UploadedFileDTO> handleVideoUpload(
            @RequestParam("videos") @ValidVideoFile List<MultipartFile> videos) {
        return fileStorageService.storeFiles(videos, FileCategory.VIDEO, VIDEOS_DIRECTORY, VIDEOS_PATH);
    }

    @GetMapping("/posts/{uuid}")
    public ResponseEntity<Resource> getPostVideo(@PathVariable String uuid) {
        return fileStorageService.getFileResponse(uuid, VIDEOS_DIRECTORY);
    }

    @PreAuthorize("hasAuthority('DELETE_VIDEO')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVideo(@PathVariable String uuid) {
        fileStorageService.deleteFile(uuid, VIDEOS_DIRECTORY);
    }
}
