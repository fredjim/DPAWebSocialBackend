package com.infsis.socialpagebackend.medias.controllers;

import com.infsis.socialpagebackend.enums.FileCategory;
import com.infsis.socialpagebackend.medias.dtos.UploadedFileDTO;
import com.infsis.socialpagebackend.medias.services.FileStorageService;
import com.infsis.socialpagebackend.validation.ValidImageFile;
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
@RequestMapping("/api/v1/images")
@Validated
public class ImageUploadController {

    private static final String POSTS_PHOTOS_DIRECTORY     = System.getProperty("user.dir") + "/storage/institution/posts/photos/";
    private static final String INST_PROFILE_PHOTO_DIR     = System.getProperty("user.dir") + "/storage/institution/profile/photos/";
    private static final String INST_COVER_DIR             = System.getProperty("user.dir") + "/storage/institution/cover/photos/";
    private static final String USER_PROFILE_PHOTO_DIR     = System.getProperty("user.dir") + "/storage/users/photos/";
    private static final String IMAGES_POSTS_PATH          = "/api/v1/images/posts/";
    private static final String IMAGES_INSTITUTION_PROFILE_PATH = "/api/v1/images/inst-profile/";
    private static final String IMAGES_INSTITUTION_COVER_PATH   = "/api/v1/images/inst-cover/";
    private static final String IMAGES_USER_PATH           = "/api/v1/images/users/";

    @Autowired
    private FileStorageService fileStorageService;

    @PreAuthorize("hasAuthority('UPLOAD_POST_IMAGE')")
    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UploadedFileDTO> handleImageUpload(
            @RequestParam("images") @ValidImageFile List<MultipartFile> images) {
        return fileStorageService.storeFiles(images, FileCategory.IMAGE, POSTS_PHOTOS_DIRECTORY, IMAGES_POSTS_PATH);
    }

    @PreAuthorize("hasAuthority('UPLOAD_INST_PROFILE_IMAGE')")
    @PostMapping("/inst-profile")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UploadedFileDTO> uploadInstProfilePhoto(
            @RequestParam("image") @ValidImageFile MultipartFile image) {
        return fileStorageService.storeFiles(List.of(image), FileCategory.IMAGE, INST_PROFILE_PHOTO_DIR, IMAGES_INSTITUTION_PROFILE_PATH);
    }

    @PreAuthorize("hasAuthority('UPLOAD_INST_COVER_IMAGE')")
    @PostMapping("/inst-cover")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UploadedFileDTO> uploadInstCoverPhoto(
            @RequestParam("image") @ValidImageFile MultipartFile image) {
        return fileStorageService.storeFiles(List.of(image), FileCategory.IMAGE, INST_COVER_DIR, IMAGES_INSTITUTION_COVER_PATH);
    }

    @PreAuthorize("hasAuthority('UPLOAD_USER_PROFILE_IMAGE')")
    @PostMapping("/user-profile")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UploadedFileDTO> uploadUserPhoto(
            @RequestParam("image") @ValidImageFile MultipartFile image) {
        return fileStorageService.storeFiles(List.of(image), FileCategory.IMAGE, USER_PROFILE_PHOTO_DIR, IMAGES_USER_PATH);
    }

    @GetMapping("/posts/{uuid}")
    public ResponseEntity<Resource> getPostImage(@PathVariable String uuid) {
        return fileStorageService.getFileResponse(uuid, POSTS_PHOTOS_DIRECTORY);
    }

    @GetMapping("/inst-profile/{uuid}")
    public ResponseEntity<Resource> getInstProfImage(@PathVariable String uuid) {
        return fileStorageService.getFileResponse(uuid, INST_PROFILE_PHOTO_DIR);
    }

    @GetMapping("/inst-cover/{uuid}")
    public ResponseEntity<Resource> getInstCoverImage(@PathVariable String uuid) {
        return fileStorageService.getFileResponse(uuid, INST_COVER_DIR);
    }

    @GetMapping("/users/{uuid}")
    public ResponseEntity<Resource> getUserImage(@PathVariable String uuid) {
        return fileStorageService.getFileResponse(uuid, USER_PROFILE_PHOTO_DIR);
    }

    @PreAuthorize("hasAnyAuthority('DELETE_POST_IMAGE', 'DELETE_INST_IMAGE', 'DELETE_USER_IMAGE')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable String uuid) {
        fileStorageService.deleteFileByUuid(uuid);
    }
}
