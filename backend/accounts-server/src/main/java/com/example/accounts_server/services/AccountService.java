package com.example.accounts_server.services;

import com.example.accounts_server.DTOs.AccountDTO;
import com.example.accounts_server.DTOs.UpdateDTO;
import com.example.accounts_server.DTOs.UserDTO;
import com.example.accounts_server.entities.Account;
import com.example.accounts_server.exceptions.ResourceNotFoundException;
import com.example.accounts_server.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountUtils accountUtils;
    private final Random random = new Random();

    @Value("C:\\Users\\Usuario-\\Desktop\\exe\\ProyectoFinalDH\\backend\\accounts-server\\src\\main\\java\\com\\example\\accounts_server\\services\\wordsAlias.txt")
    private String wordsAliasPath;

    public AccountService(AccountRepository accountRepository, AccountUtils accountUtils) {
        this.accountRepository = accountRepository;
        this.accountUtils = accountUtils;
    }

    public AccountDTO getAccountByUserId(Long userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for userId " + userId));
        return new AccountDTO(account);
    }

    public AccountDTO updateAccountByUserId(Long userId, UpdateDTO updateDTO) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for userId " + userId));

        account.update(updateDTO);

        Account updatedAccount = accountRepository.save(account);

        return new AccountDTO(updatedAccount);
    }

    public Account createAccount(UserDTO userDTO) {
        String cvu = accountUtils.generateRandomCVU();
        String alias = accountUtils.generateAlias();

        Account account = new Account(userDTO, cvu, alias);


        return accountRepository.save(account);
    }

    public Account updateAccountBalance(Long userId, Double amount) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for userId " + userId));

        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
    }


}
