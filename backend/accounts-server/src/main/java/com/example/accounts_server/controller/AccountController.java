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

        Account accountCreated = accountService.createAccount(userDTO);

        AccountCreatedDTO responseDTO = new AccountCreatedDTO(accountCreated);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    @GetMapping("/accounts/{userId}")
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


    @PatchMapping("/accounts/{userId}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long userId, @Valid @RequestBody UpdateDTO updateDTO) {
        try {
            AccountDTO accountupdatedDTO = accountService.updateAccountByUserId(userId, updateDTO);
            return new ResponseEntity<>(accountupdatedDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/accounts/{userId}/balance")
    public ResponseEntity<AccountDTO> updateAccountBalance(@PathVariable Long userId, @RequestBody Double amount) {
        try {
            System.out.println("Llegó al controlador");
            Account updatedAccount = accountService.updateAccountBalance(userId, amount);
            return new ResponseEntity<>(new AccountDTO(updatedAccount), HttpStatus.OK); // Devuelve el DTO de la cuenta actualizada
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/accounts/getAll")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        try {
            List<AccountDTO> accounts = accountService.getAllAccounts();
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
