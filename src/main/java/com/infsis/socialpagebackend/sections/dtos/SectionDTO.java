package com.infsis.socialpagebackend.sections.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class SectionDTO {
    private String uuid;

    private String institution_id;

    @Size(min = 30, max = 40)
    private String user_id;

    @Size(min = 30, max = 40)
    private String nav_item_id;

    @NotNull
    private String name;

    @NotBlank
    @Size(max = 50)
    private String path;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date date;

    // Permitir null: si no se envía, el service lo calcula automáticamente (MAX + 1 dentro del nav_item).
    // El orden empieza en 1, no en 0.
    @Min(1)
    private Integer orderIndex;

}
