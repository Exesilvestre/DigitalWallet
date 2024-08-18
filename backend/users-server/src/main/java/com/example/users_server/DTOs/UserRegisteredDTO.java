package com.example.users_server.DTOs;

import com.example.users_server.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private String phone;

    public UserRegisteredDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dni = user.getDni();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }
}


