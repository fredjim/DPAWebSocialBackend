package com.infsis.socialpagebackend.posts.mappers;

import com.infsis.socialpagebackend.configuration.AppUrlProperties;
import com.infsis.socialpagebackend.posts.models.Content;
import com.infsis.socialpagebackend.posts.models.Media;
import com.infsis.socialpagebackend.posts.dtos.MediaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MediaMapper {

    @Autowired
    private AppUrlProperties appUrlProperties;

    public MediaDTO toDTO(Media media) {
        MediaDTO mediaDTO = new MediaDTO();
        mediaDTO.setNumber(media.getNumber());
        mediaDTO.setType(media.getFile_type());
        mediaDTO.setName(media.getFile_name());
        mediaDTO.setPath(appUrlProperties.buildResourceUrl(media.getFile_path()));
        return mediaDTO;
    }

    public Media getMedia(MediaDTO mediaDTO, Content content) {
        Media media = new Media();

        media.setContent(content);
        media.setNumber(mediaDTO.getNumber());
        media.setFile_type(mediaDTO.getType());
        media.setFile_name(mediaDTO.getName());
        media.setFile_path(mediaDTO.getPath());
        return media;
    }
}
