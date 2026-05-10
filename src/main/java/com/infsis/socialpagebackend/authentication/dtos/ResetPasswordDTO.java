package com.infsis.socialpagebackend.authentication.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO(
        @NotBlank String token,
        @NotBlank @Size(min = 8) String newPassword
) {}
