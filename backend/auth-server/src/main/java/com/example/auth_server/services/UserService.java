package com.example.auth_server.services;

import com.example.auth_server.DTOs.TokenResponseDTO;
import com.example.auth_server.dto.UserRegistrationDTO;
import com.example.auth_server.entities.User;
import com.example.auth_server.exceptions.BadRequestException;
import com.example.auth_server.exceptions.ResourceNotFoundException;
import com.example.auth_server.repositories.AccountRepository;
import com.example.auth_server.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, AccountService accountService,
                       BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    public User registerUser(UserRegistrationDTO userRegistrationDTO) {
        try {
            // Verificar si el correo ya está registrado
            if (userRepository.findByEmail(userRegistrationDTO.getEmail()).isPresent()) {
                throw new BadRequestException("Email already registered");
            }

            // Crear el objeto User usando el constructor
            User user = new User(userRegistrationDTO, passwordEncoder.encode(userRegistrationDTO.getPassword()));
            User savedUser = userRepository.save(user);

            // Crear la cuenta para el usuario
            accountService.createAccount(savedUser);

            return savedUser;
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage());
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public TokenResponseDTO loginUser(String email, String password) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadRequestException("Incorrect password");
            }

            // Generar el token
            String token = jwtTokenProvider.generateToken(user);

            return new TokenResponseDTO(token);
        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e; // Propagar excepciones específicas
        } catch (Exception e) {
            throw new RuntimeException("Error during login: " + e.getMessage());
        }
    }
}
