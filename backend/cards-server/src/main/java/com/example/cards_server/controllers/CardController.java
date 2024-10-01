package com.example.cards_server.controllers;

import com.example.cards_server.DTOs.CardCreateDTO;
import com.example.cards_server.DTOs.CardDTO;
import com.example.cards_server.exceptions.CardAlreadyExistsException;
import com.example.cards_server.exceptions.ResourceNotFoundException;
import com.example.cards_server.services.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/accounts/{userId}/cards/{cardId}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable Long userId, @PathVariable Long cardId) {
        try {
            CardDTO card = cardService.getCardById(userId, cardId);
            return new ResponseEntity<>(card, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/accounts/{userId}/cards")
    public ResponseEntity<List<CardDTO>> getCardsByUserId(@PathVariable Long userId) {
        try {
            List<CardDTO> cards = cardService.getCardsByUserId(userId);
            if (cards.isEmpty()) {
                return new ResponseEntity<>(cards, HttpStatus.OK);
            }
            return new ResponseEntity<>(cards, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/accounts/{userId}/cards")
    public ResponseEntity<CardDTO> createCardForUser(@PathVariable Long userId, @RequestBody CardCreateDTO cardCreateDTO) {
        try {
            CardDTO createdCard = cardService.createCardForUser(userId, cardCreateDTO);
            return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
        } catch (CardAlreadyExistsException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/accounts/{userId}/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long userId, @PathVariable Long cardId) {
        try {
            cardService.deleteCardForUser(userId, cardId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/accounts/{userId}/cards/by-number/{cardNumber}")
    public ResponseEntity<CardDTO> getCardByLastFourNumbers(
            @PathVariable Long userId,
            @PathVariable String cardNumber) {
        try {
            CardDTO card = cardService.getCardByNumber(userId, cardNumber);
            return new ResponseEntity<>(card, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
