package com.example.activities_server.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoadMoneyRequest {
    @NotNull
    private Double amount;

    @NotNull
    private String cardNumber;

    @NotNull
    private String alias;
}
