package com.example.auth_server.services;

import com.example.auth_server.DTOs.TokenResponseDTO;
import com.example.auth_server.DTOs.UserLoginDTO;
import com.example.auth_server.entities.User;
import com.example.auth_server.exceptions.BadRequestException;
import com.example.auth_server.exceptions.ResourceNotFoundException;
import com.example.auth_server.repositories.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public TokenResponseDTO loginUser(UserLoginDTO loginRequestDTO) {
        try {
            User user = this.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequestDTO.getEmail()));


            if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
                throw new BadRequestException("Incorrect password");
            }

            String accessToken = jwtTokenProvider.generateAccessToken(user);

            return new TokenResponseDTO(accessToken);
        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error during login: " + e.getMessage());
        }
    }

    public void logoutUser(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Remover el prefijo "Bearer "
        }

        if (!jwtTokenProvider.isTokenExpired(token)) {
            long expirationInSeconds = jwtTokenProvider.getExpirationDuration(token);
            redisTemplate.opsForValue().set(token, "invalidated", Duration.ofSeconds(expirationInSeconds));
        }
    }

}
