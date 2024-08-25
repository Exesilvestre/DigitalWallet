package com.example.auth_server.DTOs;

import lombok.Data;

@Data
public class TokenResponseDTO {
    private String accessToken;
    private String message;

    public TokenResponseDTO(String accessToken) {
        this.accessToken = accessToken;
        this.message = null; // Por defecto, el mensaje es null
    }

    public TokenResponseDTO(String accessToken, String message) {
        this.accessToken = accessToken;
        this.message = message;
    }

    public TokenResponseDTO() {
        this.accessToken = null;
        this.message = null;
    }
}
