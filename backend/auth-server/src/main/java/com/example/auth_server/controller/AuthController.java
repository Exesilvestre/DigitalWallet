package com.example.auth_server.controller;

import com.example.auth_server.DTOs.*;
import com.example.auth_server.exceptions.BadRequestException;
import com.example.auth_server.exceptions.ResourceNotFoundException;
import com.example.auth_server.services.JwtTokenProvider;
import com.example.auth_server.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> loginUser(@RequestBody UserLoginDTO loginRequestDTO) {
        try {
            // Autenticar al usuario y generar el token
            TokenResponseDTO tokenResponseDTO = authService.loginUser(loginRequestDTO);

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

    @PostMapping("/auth/refresh")
    public TokenResponseDTO refreshToken(@RequestBody TokenRequestDTO tokenRequest) {
        return authService.refreshToken(tokenRequest.getRefreshToken());
    }
}
