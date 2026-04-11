package com.infsis.socialpagebackend.moderation.dtos;

import com.infsis.socialpagebackend.moderation.enums.BlacklistCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlacklistWordRequest {

    @NotBlank(message = "La palabra no puede estar vacía")
    @Size(max = 200, message = "La palabra no puede superar los 200 caracteres")
    private String word;

    @NotNull(message = "La categoría es obligatoria")
    private BlacklistCategory category;

    private boolean excludeFromFile = false;

    @Size(max = 300, message = "Las notas no pueden superar los 300 caracteres")
    private String notes;
}
