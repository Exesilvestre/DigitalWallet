package com.example.auth_server.DTOs;

import lombok.Data;

@Data
public class TokenResponseDTO {
    private String token;
    private String message;

    public TokenResponseDTO(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public TokenResponseDTO(String token) {
        this.token = token;
        this.message = null; // Por defecto, el mensaje es null
    }

    public TokenResponseDTO() {
        this.token = null;
        this.message = null;
    }
}
