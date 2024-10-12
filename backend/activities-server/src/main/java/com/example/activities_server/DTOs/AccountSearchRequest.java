package com.example.activities_server.DTOs;

import lombok.Data;

@Data
public class AccountSearchRequest {
    private String cvu;
    private String alias;
}
