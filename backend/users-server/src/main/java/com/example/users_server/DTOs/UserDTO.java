package com.example.users_server.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
}
