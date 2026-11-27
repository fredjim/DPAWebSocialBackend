package com.infsis.socialpagebackend.authentication.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Datos que un administrador puede modificar sobre otro usuario.
 * Todos los campos son opcionales: los que lleguen en null no se modifican.
 */
@Data
public class AdminUpdateUserDTO {

    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "El nombre solo puede contener letras y espacios")
    private String name;

    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "El apellido solo puede contener letras y espacios")
    private String lastName;

    @Size(max = 50)
    @Email
    private String email;

    @Pattern(regexp = "\\d{8}", message = "El número de teléfono debe contener exactamente 8 dígitos")
    private String phone;

    @Size(min = 8, max = 20)
    private String password;

    private Boolean emailVerified;

    // Solo aplicable por usuarios ROOT
    private String institutionId;
}
