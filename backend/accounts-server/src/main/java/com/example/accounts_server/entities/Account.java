package com.example.accounts_server.entities;

import com.example.accounts_server.DTOs.AccountDTO;
import com.example.accounts_server.DTOs.UpdateDTO;
import com.example.accounts_server.DTOs.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Long userId;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "cvu", nullable = false, unique = true)
    @NotBlank
    private String cvu;

    @Column(name = "alias", nullable = false)
    @NotBlank
    private String alias;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    public Account(UserDTO userDTO, String cvu, String alias){
        this.userId = userDTO.getUserId();
        this.balance = 0.00;
        this.cvu = cvu;
        this.alias = alias;
        this.name = userDTO.getFirstName() + " " + userDTO.getLastName();
    }

    public void update(UpdateDTO updateDTO) {
        this.alias = updateDTO.getAlias();
    }
}