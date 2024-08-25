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
    private String cvu;
    private String alias;

    public UserRegisteredDTO(User user, AccountCreatedDTO accountCreatedDTO) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dni = user.getDni();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.cvu = accountCreatedDTO.getCvu();
        this.alias = accountCreatedDTO.getAlias();
    }
}


