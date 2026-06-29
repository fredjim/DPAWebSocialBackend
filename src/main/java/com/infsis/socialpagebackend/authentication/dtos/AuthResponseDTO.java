package com.infsis.socialpagebackend.authentication.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class AuthResponseDTO {
    private String accessToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;

    private String tokenType = "Bearer";

    public AuthResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}