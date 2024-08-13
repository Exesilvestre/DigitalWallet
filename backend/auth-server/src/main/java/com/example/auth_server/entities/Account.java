package com.example.auth_server.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
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

    public Account (User user, String cvu, String alias){
        this.userId = user.getId();
        this.balance = 0.00;
        this.cvu = cvu;
        this.alias = alias;
        this.name = user.getFirstName() + " " + user.getLastName();
    }

}