package com.infsis.socialpagebackend.medias.mappers;

import com.infsis.socialpagebackend.configuration.AppUrlProperties;
import com.infsis.socialpagebackend.medias.dtos.UploadedFileDTO;
import com.infsis.socialpagebackend.medias.models.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UploadedFileMapper {

    @Autowired
    private AppUrlProperties appUrlProperties;

    public UploadedFileDTO toDTO(UploadedFile file) {
        UploadedFileDTO dto = new UploadedFileDTO();
        dto.setUuid(file.getUuid());
        dto.setName(file.getName());
        dto.setUrlResource(appUrlProperties.buildResourceUrl(file.getUrlResource()));
        dto.setCategory(file.getCategory().name());
        dto.setMimeType(file.getMimeType());
        dto.setStatus(file.getStatus());
        return dto;
    }
}
