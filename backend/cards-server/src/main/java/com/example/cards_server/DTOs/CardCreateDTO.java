package com.example.cards_server.DTOs;

import com.example.cards_server.entities.Card;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardCreateDTO {
    private String expiration;
    private String number;
    private String name;
    private String cvc;
    private Long userId;

    public CardCreateDTO(String expiration, String number, String name, String cvc, Long userId) {
        this.expiration = expiration;
        this.number = number;
        this.name = name;
        this.cvc = cvc;
        this.userId = userId;
    }

    public CardCreateDTO(Card card) {
        this.expiration = card.getExpiration();
        this.number = card.getNumber();
        this.name = card.getName();
        this.cvc = card.getCvc();
        this.userId = card.getUserId();
    }
}
