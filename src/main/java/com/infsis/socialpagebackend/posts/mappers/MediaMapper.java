package com.infsis.socialpagebackend.posts.mappers;

import com.infsis.socialpagebackend.configuration.AppUrlProperties;
import com.infsis.socialpagebackend.exceptions.NotFoundException;
import com.infsis.socialpagebackend.medias.models.UploadedFile;
import com.infsis.socialpagebackend.medias.repositories.UploadedFileRepository;
import com.infsis.socialpagebackend.posts.models.Content;
import com.infsis.socialpagebackend.posts.models.Media;
import com.infsis.socialpagebackend.posts.dtos.MediaDTO;
import com.infsis.socialpagebackend.sections.models.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MediaMapper {

    @Autowired
    private AppUrlProperties appUrlProperties;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    public MediaDTO toDTO(Media media) {
        MediaDTO mediaDTO = new MediaDTO();
        mediaDTO.setUuid(media.getUuid());
        mediaDTO.setNumber(media.getNumber());
        mediaDTO.setType(media.getFile_type());
        mediaDTO.setName(media.getFile_name());
        if (media.getUploadedFile() != null) {
            mediaDTO.setPath(appUrlProperties.buildResourceUrl(media.getUploadedFile().getUrlResource()));
        }
        return mediaDTO;
    }

    public Media getMedia(MediaDTO mediaDTO, Content content) {
        Media media = buildBaseMedia(mediaDTO);
        media.setContent(content);
        return media;
    }

    public Media getMedia(MediaDTO mediaDTO, Article article) {
        Media media = buildBaseMedia(mediaDTO);
        media.setArticle(article);
        return media;
    }

    private Media buildBaseMedia(MediaDTO mediaDTO) {
        Media media = new Media();
        media.setNumber(mediaDTO.getNumber());
        media.setFile_type(mediaDTO.getType());

        if (mediaDTO.getUploaded_file_uuid() != null) {
            UploadedFile uploadedFile = uploadedFileRepository.findByUuid(mediaDTO.getUploaded_file_uuid())
                    .orElseThrow(() -> new NotFoundException("UploadedFile", mediaDTO.getUploaded_file_uuid()));
            media.setUploadedFile(uploadedFile);
            media.setFile_name(mediaDTO.getName() != null ? mediaDTO.getName() : uploadedFile.getName());
        } else {
            media.setFile_name(mediaDTO.getName());
        }

        return media;
    }
}
