package com.example.cards_server.entities;

import com.example.cards_server.DTOs.CardCreateDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expiration", nullable = false)
    @NotNull
    private  String expiration;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "cvc", nullable = false)
    @NotBlank
    private String cvc;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public Card(CardCreateDTO cardCreateDTO) {
        this.expiration = cardCreateDTO.getExpiration();
        this.number = cardCreateDTO.getNumber();
        this.name = cardCreateDTO.getName();
        this.cvc = cardCreateDTO.getCvc();
        this.userId = cardCreateDTO.getUserId();
    }

}
