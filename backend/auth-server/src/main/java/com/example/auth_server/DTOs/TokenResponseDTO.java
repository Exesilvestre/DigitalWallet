package com.example.auth_server.DTOs;

import lombok.Data;

@Data
public class TokenResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String message;

    public TokenResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = null; // Por defecto, el mensaje es null
    }

    public TokenResponseDTO(String accessToken, String refreshToken, String message) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
    }

    public TokenResponseDTO() {
        this.accessToken = null;
        this.refreshToken = null;
        this.message = null;
    }
}
