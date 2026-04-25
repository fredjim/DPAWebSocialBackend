package com.infsis.socialpagebackend.authentication.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserDTO {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Solo letras y espacios")
    private String name;

    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Solo letras y espacios")
    private String lastName;

    @Pattern(regexp = "\\d{8}", message = "Exactamente 8 dígitos")
    private String phone;

    @NotNull
    private Long roleId;

    private String institutionId;
}
