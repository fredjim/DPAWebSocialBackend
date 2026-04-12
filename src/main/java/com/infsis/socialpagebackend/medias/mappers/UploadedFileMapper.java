package com.infsis.socialpagebackend.medias.mappers;

import com.infsis.socialpagebackend.medias.dtos.UploadedFileDTO;
import com.infsis.socialpagebackend.medias.models.UploadedFile;
import org.springframework.stereotype.Component;

@Component
public class UploadedFileMapper {

    public UploadedFileDTO toDTO(UploadedFile file) {
        UploadedFileDTO dto = new UploadedFileDTO();
        dto.setUuid(file.getUuid());
        dto.setName(file.getName());
        dto.setUrlResource(file.getUrlResource());
        dto.setCategory(file.getCategory().name());
        dto.setMimeType(file.getMimeType());
        dto.setStatus(file.getStatus());
        return dto;
    }
}
