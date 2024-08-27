package com.example.accounts_server.controller;

import com.example.accounts_server.DTOs.*;
import com.example.accounts_server.entities.Account;
import com.example.accounts_server.exceptions.ResourceNotFoundException;
import com.example.accounts_server.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<AccountCreatedDTO> createAccount(@Valid @RequestBody UserDTO userDTO) {

        // Registrar el nuevo usuario
        Account accountCreated = accountService.createAccount(userDTO);

        // Crear el DTO de respuesta
        AccountCreatedDTO responseDTO = new AccountCreatedDTO(accountCreated);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    @GetMapping("/account/{userId}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long userId) {
        try {
            AccountDTO accountDTO = accountService.getAccountByUserId(userId);
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping("/account/{userId}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long userId, @Valid @RequestBody AccountDTO accountDTO) {
        try {
            AccountDTO accountupdatedDTO = accountService.updateAccountByUserId(userId, accountDTO);
            return new ResponseEntity<>(accountupdatedDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/account/{userId}/transactions")
    public ResponseEntity<List<ActivityDTO>> getLatestTransactions(@PathVariable Long userId) {
        try {
            List<ActivityDTO> activities = accountService.getLatestTransactionsByUserId(userId);
            return new ResponseEntity<>(activities, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/accounts/{userId}/cards/{cardId}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable Long userId, @PathVariable Long cardId) {
        try {
            CardDTO card = accountService.getCardById(userId, cardId);
            return new ResponseEntity<>(card, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/accounts/{userId}/cards")
    public ResponseEntity<CardDTO> createCardForUser(@PathVariable Long userId, @RequestBody CardDTO cardDTO) {
        ResponseEntity<CardDTO> response = accountService.createCardForUser(userId, cardDTO);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
        } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/accounts/{userId}/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long userId, @PathVariable Long cardId) {
        try {
            return accountService.deleteCardForUser(userId, cardId);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
