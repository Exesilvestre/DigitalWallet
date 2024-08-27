package com.example.cards_server.services;


import com.example.cards_server.DTOs.CardCreateDTO;
import com.example.cards_server.DTOs.CardDTO;
import com.example.cards_server.entities.Card;
import com.example.cards_server.exceptions.CardAlreadyExistsException;
import com.example.cards_server.exceptions.ResourceNotFoundException;
import com.example.cards_server.repositories.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<CardDTO> getCardsByUserId(Long userId) {
        List<Card> cards = cardRepository.findAll().stream()
                .filter(card -> card.getUserId().equals(userId))
                .collect(Collectors.toList());

        if (cards.isEmpty()) {
            throw new ResourceNotFoundException("No cards found for userId " + userId);
        }

        return cards.stream().map(CardDTO::new).collect(Collectors.toList());
    }

    public CardDTO getCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found for cardId " + cardId));

        return new CardDTO(card);
    }

    public CardDTO createCard(CardCreateDTO cardCreateDTO) {
        Optional<Card> existingCard = cardRepository.findByNumber(cardCreateDTO.getNumber());
        if (existingCard.isPresent()) {
            throw new CardAlreadyExistsException("Card with number " + cardCreateDTO.getNumber() + " already exists.");
        }

        Card card = new Card(cardCreateDTO);
        Card savedCard = cardRepository.save(card);

        return new CardDTO(savedCard);
    }

    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new ResourceNotFoundException("Card not found for cardId " + cardId);
        }

        cardRepository.deleteById(cardId);
    }
}
