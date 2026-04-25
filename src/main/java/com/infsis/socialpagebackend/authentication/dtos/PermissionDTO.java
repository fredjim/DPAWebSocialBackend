package com.infsis.socialpagebackend.authentication.dtos;

import lombok.Data;

@Data
public class PermissionDTO {
    private Long id;
    private String name;
    private boolean isSystemOnly;
}
