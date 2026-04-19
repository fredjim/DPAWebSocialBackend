package com.infsis.socialpagebackend.navigation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NavItemDTO {
    private String uuid;

    @NotBlank
    @Size(min = 30, max = 40)
    private String institution_id;

    @Size(min = 30, max = 40)
    private String user_id;

    @NotBlank
    private String label;

    @NotBlank
    private String path;

    // Permitir null para actualizaciones parciales
    private Boolean visible;

    private Integer orderIndex;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date createdDate;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date lastModifiedDate;
}

