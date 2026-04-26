package com.infsis.socialpagebackend.posts.clients;

import com.infsis.socialpagebackend.posts.dtos.FacebookPhotoResponseDTO;
import com.infsis.socialpagebackend.posts.dtos.MediaDTO;
import com.infsis.socialpagebackend.posts.dtos.PostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacebookApiClient {

    private static final Logger logger = LoggerFactory.getLogger(FacebookApiClient.class);

    @Value("${meta.graph-api.url}")
    private String graphApiUrl;

    @Value("${meta.graph-api.video-url}")
    private String graphVideoApiUrl;

    private final RestTemplate restTemplate;

    public FacebookApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void postPublication(PostDTO postDTO, String pageId, String accessToken) {
        postPublication(postDTO, postDTO.getContent().getText(), null, pageId, accessToken);
    }

    /**
     * @param message texto del post (puede incluir URLs de documentos adjuntas)
     * @param link    URL de documento para previsualización (nullable; no combinar con attached_media)
     */
    public void postPublication(PostDTO postDTO, String message, String link,
                                String pageId, String accessToken) {
        if (Boolean.TRUE.equals(postDTO.getIs_fb_posted())) {
            logger.info("Post {} ya fue publicado en Facebook, se omite.", postDTO.getUuid());
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("access_token", accessToken);
        params.add("message", message != null ? message : "");

        if (link != null && !link.isBlank()) {
            params.add("link", link);
        }

        int i = 0;
        for (MediaDTO media : postDTO.getContent().getMedia()) {
            if (media.getFb_media_id() != null && !media.getFb_media_id().isBlank()) {
                params.add("attached_media[" + i + "]",
                        "{\"media_fbid\":\"" + media.getFb_media_id() + "\"}");
                i++;
            }
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                graphApiUrl + "/" + pageId + "/feed", request, String.class);

        logger.info("Post publicado en Facebook. Status: {} | Body: {}",
                response.getStatusCode(), response.getBody());
    }

    /**
     * Sube un video a /{page-id}/videos con published=true.
     * El texto del post va como 'description'. Este endpoint crea el post directamente.
     */
    public void postVideo(Path videoPath, String mimeType, String originalFilename,
                          String description, String pageId, String accessToken) {
        try {
            byte[] fileBytes = Files.readAllBytes(videoPath);
            ByteArrayResource fileResource = new ByteArrayResource(fileBytes) {
                @Override
                public String getFilename() {
                    return originalFilename;
                }
            };

            HttpHeaders filePartHeaders = new HttpHeaders();
            filePartHeaders.setContentType(MediaType.parseMediaType(mimeType));

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("access_token", accessToken);
            body.add("description", description != null ? description : "");
            body.add("published", "true");
            body.add("source", new HttpEntity<>(fileResource, filePartHeaders));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    graphVideoApiUrl + "/" + pageId + "/videos", request, String.class);

            logger.info("Video publicado en Facebook. Status: {} | Body: {}",
                    response.getStatusCode(), response.getBody());

        } catch (IOException e) {
            logger.error("Error leyendo video para subir a Facebook: {}", videoPath, e);
        }
    }

    /**
     * Sube cada imagen como binario (multipart/form-data) a /{page-id}/photos con published=false.
     * Retorna la lista de Facebook photo IDs en el mismo orden que imagePaths.
     */
    public List<String> postLinkImages(List<Path> imagePaths, List<String> mimeTypes,
                                       List<String> originalFilenames, String pageId, String accessToken) {
        List<String> photoIds = new ArrayList<>();

        for (int i = 0; i < imagePaths.size(); i++) {
            Path filePath = imagePaths.get(i);
            String mimeType = mimeTypes.get(i);
            String filename = originalFilenames.get(i);
            try {
                byte[] fileBytes = Files.readAllBytes(filePath);
                ByteArrayResource fileResource = new ByteArrayResource(fileBytes) {
                    @Override
                    public String getFilename() {
                        return filename;
                    }
                };

                HttpHeaders filePartHeaders = new HttpHeaders();
                filePartHeaders.setContentType(MediaType.parseMediaType(mimeType));

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("access_token", accessToken);
                body.add("published", "false");
                body.add("source", new HttpEntity<>(fileResource, filePartHeaders));

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
                ResponseEntity<FacebookPhotoResponseDTO> response = restTemplate.postForEntity(
                        graphApiUrl + "/" + pageId + "/photos", request, FacebookPhotoResponseDTO.class);

                String photoId = response.getBody() != null ? response.getBody().getId() : "";
                photoIds.add(photoId != null ? photoId : "");
                logger.info("Imagen subida a Facebook. Status: {} | Photo ID: {}",
                        response.getStatusCode(), photoId);

            } catch (IOException e) {
                logger.error("Error leyendo archivo para subir a Facebook: {}", filePath, e);
                photoIds.add("");
            }
        }

        return photoIds;
    }
}
