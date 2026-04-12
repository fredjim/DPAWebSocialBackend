package com.infsis.socialpagebackend.medias.mappers;

import com.infsis.socialpagebackend.configuration.AppUrlProperties;
import com.infsis.socialpagebackend.medias.dtos.ImageFileDTO;
import com.infsis.socialpagebackend.medias.models.ImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageFileMapper {

    @Autowired
    private AppUrlProperties appUrlProperties;

    public ImageFileDTO toDTO(ImageFile imageFile) {
        ImageFileDTO imageFileDTO = new ImageFileDTO();
        imageFileDTO.setUuid(imageFile.getUuid());
        imageFileDTO.setName(imageFile.getName());
        //imageFileDTO.setUrlResource(appUrlProperties.buildResourceUrl(imageFile.getUrl_resource()));
        imageFileDTO.setUrlResource(imageFile.getUrl_resource());
        imageFileDTO.setType(imageFile.getType());
        imageFileDTO.setStatus(imageFile.getStatus());

        return imageFileDTO;
    }

    public ImageFile getFile(ImageFileDTO imageFileDTO) {
        ImageFile imageFile = new ImageFile();
        imageFile.setUuid(imageFileDTO.getUuid());
        imageFile.setName(imageFileDTO.getName());
        imageFile.setUrl_resource(imageFileDTO.getUrlResource());
        imageFile.setType(imageFileDTO.getType());
        imageFile.setStatus(imageFileDTO.getStatus());

        return imageFile;
    }
}
