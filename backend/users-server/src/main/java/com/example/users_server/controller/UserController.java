package com.example.users_server.controller;

import com.example.users_server.DTOs.UserRegisteredDTO;
import com.example.users_server.DTOs.UserRegistrationDTO;
import com.example.users_server.entities.User;
import com.example.users_server.exceptions.BadRequestException;
import com.example.users_server.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisteredDTO> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        // Verificar si el correo ya está registrado
        userService.findByEmail(userRegistrationDTO.getEmail())
                .ifPresent(user -> {
                    throw new BadRequestException("Email already registered");
                });

        // Registrar el nuevo usuario
        User registeredUser = userService.registerUser(userRegistrationDTO);

        // Crear el DTO de respuesta
        UserRegisteredDTO responseDTO = new UserRegisteredDTO(registeredUser);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
