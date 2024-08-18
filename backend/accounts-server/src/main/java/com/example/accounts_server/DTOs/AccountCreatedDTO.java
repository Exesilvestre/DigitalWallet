package com.example.accounts_server.DTOs;

import com.example.accounts_server.entities.Account;
import lombok.Data;

@Data
public class AccountCreatedDTO {
    private Long userId;
    private Double balance;
    private String cvu;
    private String alias;
    private String name;

    public AccountCreatedDTO(Account account){
        this.userId = account.getUserId();
        this.balance = account.getBalance();
        this.cvu = account.getCvu();
        this.alias = account.getAlias();
        this.name = account.getName();
    }

}
