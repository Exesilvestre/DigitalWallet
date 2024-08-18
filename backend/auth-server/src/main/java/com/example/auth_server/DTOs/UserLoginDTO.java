package com.example.auth_server.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
