package com.example.users_server.DTOs;

import lombok.Data;

@Data
public class AccountCreatedDTO {
    private Long userId;
    private Double balance;
    private String cvu;
    private String alias;
    private String name;

}
