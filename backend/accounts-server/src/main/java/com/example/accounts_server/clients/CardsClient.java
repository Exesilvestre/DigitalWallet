package com.example.accounts_server.clients;

import com.example.accounts_server.DTOs.CardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "cards-service")
public interface CardsClient {

    @GetMapping("/cards")
    List<CardDTO> getCardsByUserId(@RequestParam("userId") Long userId);

    @GetMapping("/cards/{cardId}")
    CardDTO getCardById(@PathVariable("cardId") Long cardId);

    @PostMapping("/cards")
    ResponseEntity<CardDTO> createCard(@RequestBody CardDTO cardDTO);

    @DeleteMapping("/cards/{cardId}")
    ResponseEntity<Void> deleteCard(@PathVariable("cardId") Long cardId);
}
