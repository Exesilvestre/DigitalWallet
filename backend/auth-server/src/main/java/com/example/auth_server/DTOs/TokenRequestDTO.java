package com.example.auth_server.DTOs;

import lombok.Data;

@Data
public class TokenRequestDTO {
    private String refreshToken;

    public TokenRequestDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenRequestDTO() {
        this.refreshToken = null;
    }
}
