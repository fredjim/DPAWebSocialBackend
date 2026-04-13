package com.infsis.socialpagebackend.medias.dtos;

import lombok.Data;

@Data
public class UploadedFileDTO {

    private String uuid;

    private String name;

    private String urlResource;

    private String category;

    private String mimeType;

    private String status;
}
