package com.example.users_server.services;

import com.example.users_server.DTOs.*;
import com.example.users_server.clients.AccountServiceClient;
import com.example.users_server.entities.User;
import com.example.users_server.exceptions.BadRequestException;
import com.example.users_server.exceptions.ResourceNotFoundException;
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

    public UserRegisteredDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        try {
            // Verificar si el correo ya está registrado
            if (userRepository.findByEmail(userRegistrationDTO.getEmail()).isPresent()) {
                throw new BadRequestException("Email already registered");
            }

            // Crear el objeto User usando el constructor
            User user = new User(userRegistrationDTO, passwordEncoder.encode(userRegistrationDTO.getPassword()));
            User savedUser = userRepository.save(user);

            // Crear la cuenta para el usuario a través de Feign
            AccountToCreateDTO accountToCreateDTO = new AccountToCreateDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName());
            AccountCreatedDTO accountCreatedDTO = accountServiceClient.createAccount(accountToCreateDTO);

            return new UserRegisteredDTO(savedUser, accountCreatedDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage());
        }
    }

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        return new UserDTO(user);
    }

    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        user.update(userDTO);
        User updatedUser = userRepository.save(user);
        return new UserDTO(updatedUser);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
