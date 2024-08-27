package com.example.accounts_server.services;

import com.example.accounts_server.DTOs.AccountDTO;
import com.example.accounts_server.DTOs.ActivityDTO;
import com.example.accounts_server.DTOs.CardDTO;
import com.example.accounts_server.DTOs.UserDTO;
import com.example.accounts_server.clients.ActivitiesClient;
import com.example.accounts_server.clients.CardsClient;
import com.example.accounts_server.entities.Account;
import com.example.accounts_server.exceptions.ResourceNotFoundException;
import com.example.accounts_server.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ActivitiesClient activitiesClient;
    private final CardsClient cardsClient;
    private final AccountUtils accountUtils;
    private final Random random = new Random();

    @Value("C:\\Users\\Usuario-\\Desktop\\exe\\ProyectoFinalDH\\backend\\accounts-server\\src\\main\\java\\com\\example\\accounts_server\\services\\wordsAlias.txt")
    private String wordsAliasPath;

    public AccountService(AccountRepository accountRepository, ActivitiesClient activitiesClient, CardsClient cardsClient, AccountUtils accountUtils) {
        this.accountRepository = accountRepository;
        this.activitiesClient = activitiesClient;
        this.cardsClient = cardsClient;
        this.accountUtils = accountUtils;
    }

    public AccountDTO getAccountByUserId(Long userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for userId " + userId));
        return new AccountDTO(account);
    }

    public AccountDTO updateAccountByUserId(Long userId, AccountDTO accountDTO) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for userId " + userId));

        account.update(accountDTO);
        Account updatedAccount = accountRepository.save(account);
        return new AccountDTO(updatedAccount);
    }

    public Account createAccount(UserDTO userDTO) {
        String cvu = accountUtils.generateRandomCVU();
        String alias = accountUtils.generateAlias();

        Account account = new Account(userDTO, cvu, alias);


        return accountRepository.save(account);
    }

    public List<ActivityDTO> getLatestTransactionsByUserId(Long userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for userId " + userId));

        return activitiesClient.getLatestActivitiesByUserId(account.getUserId());
    }

    public List<CardDTO> getCardsByUserId(Long userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for userId " + userId));

        List<CardDTO> cards = cardsClient.getCardsByUserId(userId);

        if (cards.isEmpty()) {
            throw new ResourceNotFoundException("No cards found for userId " + userId);
        }

        return cards;
    }

    public CardDTO getCardById(Long userId, Long cardId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for userId " + userId));

        CardDTO card = cardsClient.getCardById(cardId);

        if (!card.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Card not found for userId " + userId + " and cardId " + cardId);
        }

        return card;
    }

    public ResponseEntity<CardDTO> createCardForUser(Long userId, CardDTO cardDTO) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for userId " + userId));

        List<CardDTO> existingCards = cardsClient.getCardsByUserId(userId);
        for (CardDTO existingCard : existingCards) {
            if (existingCard.getNumber().equals(cardDTO.getNumber())) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
        }

        cardDTO.setUserId(userId);
        return cardsClient.createCard(cardDTO);
    }

    public ResponseEntity<Void> deleteCardForUser(Long userId, Long cardId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for userId " + userId));

        CardDTO card = cardsClient.getCardById(cardId);
        if (!card.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Card not found for userId " + userId + " and cardId " + cardId);
        }

        ResponseEntity<Void> response = cardsClient.deleteCard(cardId);

        if (response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
