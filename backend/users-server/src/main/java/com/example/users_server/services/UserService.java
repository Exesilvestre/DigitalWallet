package com.example.users_server.services;

import com.example.users_server.DTOs.UserDTO;
import com.example.users_server.DTOs.UserRegistrationDTO;
import com.example.users_server.clients.AccountServiceClient;
import com.example.users_server.entities.User;
import com.example.users_server.exceptions.BadRequestException;
import com.example.users_server.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountServiceClient accountServiceClient;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AccountServiceClient accountServiceClient,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountServiceClient = accountServiceClient;
        this.passwordEncoder = passwordEncoder;
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

            // Crear la cuenta para el usuario a través de Feign
            UserDTO userDTO = new UserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName());
            accountServiceClient.createAccount(userDTO);

            return savedUser;
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage());
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
