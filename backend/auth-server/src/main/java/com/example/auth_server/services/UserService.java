package com.example.auth_server.services;

import com.example.auth_server.DTOs.TokenResponseDTO;
import com.example.auth_server.DTOs.UserLoginDTO;
import com.example.auth_server.DTOs.UserRegistrationDTO;
import com.example.auth_server.entities.User;
import com.example.auth_server.exceptions.BadRequestException;
import com.example.auth_server.exceptions.ResourceNotFoundException;
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

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public TokenResponseDTO loginUser(UserLoginDTO loginRequestDTO) {
        try {
            String emailToFind = loginRequestDTO.getEmail();
            User user = this.findByEmail(emailToFind)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequestDTO.getEmail()));

            System.out.println(user);

            if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
                throw new BadRequestException("Incorrect password");
            }

            String token = jwtTokenProvider.generateToken(user);

            return new TokenResponseDTO(token);
        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error during login: " + e.getMessage());
        }
    }
}
