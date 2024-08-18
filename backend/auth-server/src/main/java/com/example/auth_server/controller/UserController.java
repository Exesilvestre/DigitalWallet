package com.example.auth_server.controller;

import com.example.auth_server.DTOs.TokenResponseDTO;
import com.example.auth_server.DTOs.UserLoginDTO;
import com.example.auth_server.DTOs.UserRegisteredDTO;
import com.example.auth_server.DTOs.UserRegistrationDTO;
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

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> loginUser(@RequestBody UserLoginDTO loginRequestDTO) {
        try {
            // Autenticar al usuario y generar el token
            TokenResponseDTO tokenResponseDTO = userService.loginUser(loginRequestDTO);

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
