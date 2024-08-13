package com.example.auth_server.controller;

import com.example.auth_server.DTOs.TokenResponseDTO;
import com.example.auth_server.DTOs.UserRegisteredDTO;
import com.example.auth_server.dto.UserRegistrationDTO;
import com.example.auth_server.entities.User;
import com.example.auth_server.exceptions.BadRequestException;
import com.example.auth_server.exceptions.ResourceNotFoundException;
import com.example.auth_server.services.JwtTokenProvider;
import com.example.auth_server.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
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

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> loginUser(@RequestParam String email, @RequestParam String password) {
        try {
            // Autenticar al usuario y generar el token
            TokenResponseDTO tokenResponseDTO = userService.loginUser(email, password);

            return new ResponseEntity<>(tokenResponseDTO, HttpStatus.OK);
        } catch (BadRequestException e) {
            // Manejar errores de credenciales incorrectas
            return new ResponseEntity<>(new TokenResponseDTO(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            // Manejar errores de usuario no encontrado
            return new ResponseEntity<>(new TokenResponseDTO(null, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Manejar otros errores
            return new ResponseEntity<>(new TokenResponseDTO(null, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validate-token")
    public Mono<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return Mono.just(jwtTokenProvider.validateToken(token));
        }
        return Mono.just(false);
    }
}
