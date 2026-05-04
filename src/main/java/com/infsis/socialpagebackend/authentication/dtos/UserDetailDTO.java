package com.infsis.socialpagebackend.authentication.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDetailDTO {

    private String uuid;

    @Size(min = 3, max = 100)
    private String name;

    @Size(min = 3, max = 100)
    private String lastName;

    @Size(min = 8, max = 80)
    private String password;

    @Size(max = 50)
    @Email
    private String email;

    @Size(min = 7, max = 15)
    private String phone;

    // ── Input: UUID del UploadedFile obtenido al subir la imagen ─────────────
    private String photoProfileFileUuid;

    private String photoCoverFileUuid;

    // ── Output: URL completa construida por el mapper (solo lectura) ──────────
    private String photo_profile_path;

    private String photo_cover_path;

    private String role;

    private String institutionId;
}