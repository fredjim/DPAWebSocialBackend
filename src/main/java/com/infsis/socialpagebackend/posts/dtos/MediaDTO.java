package com.infsis.socialpagebackend.posts.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MediaDTO {

    private String uuid;

    @NotNull
    private Integer number;

    private String type;

    private String name;

    private String uploaded_file_uuid;

    private String path;

    private String fb_media_id;

}
