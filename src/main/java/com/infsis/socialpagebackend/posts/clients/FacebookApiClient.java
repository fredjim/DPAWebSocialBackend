package com.infsis.socialpagebackend.posts.clients;

import com.infsis.socialpagebackend.posts.dtos.FacebookPhotoResponseDTO;
import com.infsis.socialpagebackend.posts.dtos.MediaDTO;
import com.infsis.socialpagebackend.posts.dtos.PostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacebookApiClient {

    private static final Logger logger = LoggerFactory.getLogger(FacebookApiClient.class);

    @Value("${meta.graph-api.url}")
    private String graphApiUrl;

    private final RestTemplate restTemplate;

    public FacebookApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void postPublication(PostDTO postDTO, String pageId, String accessToken) {
        if (Boolean.TRUE.equals(postDTO.getIs_fb_posted())) {
            logger.info("Post {} ya fue publicado en Facebook, se omite.", postDTO.getUuid());
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("access_token", accessToken);
        params.add("message", postDTO.getContent().getText());

        int i = 0;
        for (MediaDTO media : postDTO.getContent().getMedia()) {
            params.add("attached_media[" + i + "]",
                    "{'media_fbid':'" + media.getFb_media_id() + "'}");
            i++;
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                graphApiUrl + "/" + pageId + "/feed", request, String.class);

        logger.info("Post publicado en Facebook. Status: {} | Body: {}",
                response.getStatusCode(), response.getBody());
    }

    public List<String> postLinkImages(List<MediaDTO> photos, String pageId, String accessToken) {
        List<String> postedImages = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        for (MediaDTO mediaDTO : photos) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("access_token", accessToken);
            params.add("published", "false");
            params.add("url", mediaDTO.getPath());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<FacebookPhotoResponseDTO> response = restTemplate.postForEntity(
                    graphApiUrl + "/" + pageId + "/photos", request, FacebookPhotoResponseDTO.class);

            String photoId = response.getBody() != null ? response.getBody().getId() : "";
            postedImages.add(photoId != null ? photoId : "");
            logger.info("Imagen subida a Facebook. Status: {} | Photo ID: {}",
                    response.getStatusCode(), photoId);
        }

        return postedImages;
    }
}
