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

    @GetMapping("/cards")
    public ResponseEntity<List<CardDTO>> getCardsByUserId(@RequestParam("userId") Long userId) {
        try {
            List<CardDTO> cards = cardService.getCardsByUserId(userId);
            return new ResponseEntity<>(cards, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cards/{cardId}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable("cardId") Long cardId) {
        try {
            CardDTO card = cardService.getCardById(cardId);
            return new ResponseEntity<>(card, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cards")
    public ResponseEntity<CardDTO> createCard(@RequestBody CardCreateDTO cardCreateDTO) {
        try {
            CardDTO cardDTO = cardService.createCard(cardCreateDTO);
            return new ResponseEntity<>(cardDTO, HttpStatus.CREATED);
        } catch (CardAlreadyExistsException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        try {
            cardService.deleteCard(cardId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
