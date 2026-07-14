package com.infsis.socialpagebackend.posts.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaItemDTO {
    private String uuid_post;
    private String fileUuid;
    private String path;
    private String fileName;
    private String mimeType;
    private String fileType;
}
