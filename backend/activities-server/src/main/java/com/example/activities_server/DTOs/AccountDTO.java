package com.example.activities_server.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDTO {
    private String id;
    private String userId;
    private Double balance;
    private String cvu;
    private String alias;
    private String name;

}
