package com.example.accounts_server.DTOs;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardDTO {
    private Long id;
    private String expiration;
    private String number;
    private String name;
    private Long userId;

    // Constructor
    public CardDTO(Long id, String expiration, String number, String name, Long userId) {
        this.id = id;
        this.expiration = expiration;
        this.number = number;
        this.name = name;
        this.userId = userId;
    }

}
