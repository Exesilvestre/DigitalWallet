package com.example.activities_server.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "amount", nullable = false)
    @NotNull
    private Double amount;

    @Column(name = "dated", nullable = false)
    @NotBlank
    private String dated;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "origin", nullable = false)
    @NotBlank
    private String origin;

    @Column(name = "destination", nullable = false)
    @NotBlank
    private String destination;

    @Column(name = "type", nullable = false)
    @NotBlank
    private String type;
}