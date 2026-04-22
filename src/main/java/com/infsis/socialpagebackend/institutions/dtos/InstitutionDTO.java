package com.infsis.socialpagebackend.institutions.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionDTO {

    private String uuid;

    @NotBlank
    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "El slug solo puede contener letras minúsculas, números y guiones, sin empezar ni terminar con guión")
    private String slug;

    @NotBlank
    @Size(min = 3, max = 150)
    private String name;

    @NotBlank
    @Size(min = 3, max = 300)
    private String description;

    @NotBlank
    @Size(min = 3, max = 300)
    private String location;

    @NotBlank
    @Size(min = 3, max = 100)
    private String category;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 7, max = 15)
    private String phone;

    @NotBlank
    @Size(min = 3, max = 80)
    private String url;

    @NotBlank
    @Size(min = 3, max = 100)
    private String logo_url;

    @NotBlank
    @Size(min = 3, max = 100)
    private String background_url;

}
