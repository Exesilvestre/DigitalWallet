package com.example.accounts_server.controller;

import com.example.accounts_server.DTOs.AccountCreatedDTO;
import com.example.accounts_server.DTOs.UserDTO;
import com.example.accounts_server.entities.Account;
import com.example.accounts_server.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
