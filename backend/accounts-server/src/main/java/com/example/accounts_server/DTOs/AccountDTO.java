package com.example.accounts_server.DTOs;

import com.example.accounts_server.entities.Account;
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

    public AccountDTO(Account account){
        this.id = String.valueOf(account.getId());
        this.userId = String.valueOf(account.getUserId());
        this.balance = account.getBalance();
        this.cvu = account.getCvu();
        this.alias = account.getAlias();
        this.name = account.getName();
    }
}
