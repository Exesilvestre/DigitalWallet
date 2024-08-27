package com.example.cards_server.DTOs;


import com.example.cards_server.entities.Card;
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
    public CardDTO(Card card) {
        this.id = card.getId();
        this.expiration = card.getExpiration();
        this.number = card.getNumber();
        this.name = card.getName();
        this.userId = card.getUserId();
    }
}
