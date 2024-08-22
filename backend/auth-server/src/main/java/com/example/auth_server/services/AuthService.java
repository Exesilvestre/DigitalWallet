package com.example.auth_server.services;

import com.example.auth_server.DTOs.TokenResponseDTO;
import com.example.auth_server.DTOs.UserLoginDTO;
import com.example.auth_server.entities.User;
import com.example.auth_server.exceptions.BadRequestException;
import com.example.auth_server.exceptions.ResourceNotFoundException;
import com.example.auth_server.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public TokenResponseDTO loginUser(UserLoginDTO loginRequestDTO) {
        try {
            User user = this.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequestDTO.getEmail()));

            System.out.println(user);

            if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
                throw new BadRequestException("Incorrect password");
            }
            System.out.println("paso password");

            String accessToken = jwtTokenProvider.generateAccessToken(user);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user);

            return new TokenResponseDTO(accessToken, refreshToken);
        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error during login: " + e.getMessage());
        }
    }

    public TokenResponseDTO refreshToken(String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String email = jwtTokenProvider.getClaims(refreshToken).getSubject();
            User user = findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

            // Generar un nuevo access token
            String newAccessToken = jwtTokenProvider.generateAccessToken(user);
            // Opcionalmente, generar un nuevo refresh token
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

            return new TokenResponseDTO(newAccessToken, newRefreshToken);
        } else {
            throw new BadRequestException("Invalid refresh token");
        }
    }
}
