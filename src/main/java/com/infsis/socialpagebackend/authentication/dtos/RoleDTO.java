package com.infsis.socialpagebackend.authentication.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RoleDTO {

    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[A-Z][A-Z0-9_]*$", message = "Formato: MAYUSCULAS_CON_GUION_BAJO")
    private String name;

    private boolean isSystemRole;

    private Set<Long> permissionIds = new HashSet<>();
}
