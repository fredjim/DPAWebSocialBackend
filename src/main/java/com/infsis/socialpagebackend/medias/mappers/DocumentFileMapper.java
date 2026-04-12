package com.infsis.socialpagebackend.medias.mappers;

import com.infsis.socialpagebackend.configuration.AppUrlProperties;
import com.infsis.socialpagebackend.medias.dtos.DocumentFileDTO;
import com.infsis.socialpagebackend.medias.models.DocumentFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentFileMapper {

    @Autowired
    private AppUrlProperties appUrlProperties;

    public DocumentFileDTO toDTO(DocumentFile documentFile) {
        DocumentFileDTO documentFileDTO = new DocumentFileDTO();
        documentFileDTO.setUuid(documentFile.getUuid());
        documentFileDTO.setName(documentFile.getName());
        documentFileDTO.setUrlResource(appUrlProperties.buildResourceUrl(documentFile.getUrl_resource()));
        documentFileDTO.setType(documentFile.getType());
        documentFileDTO.setStatus(documentFile.getStatus());

        return documentFileDTO;
    }

    public DocumentFile getFile(DocumentFileDTO documentFileDTO) {
        DocumentFile documentFile = new DocumentFile();
        documentFile.setUuid(documentFileDTO.getUuid());
        documentFile.setName(documentFileDTO.getName());
        documentFile.setUrl_resource(documentFileDTO.getUrlResource());
        documentFile.setType(documentFileDTO.getType());
        documentFile.setStatus(documentFileDTO.getStatus());

        return documentFile;
    }
}
