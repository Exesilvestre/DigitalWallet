package com.example.auth_server.services;


import com.example.auth_server.entities.Account;
import com.example.auth_server.entities.User;
import com.example.auth_server.repositories.AccountRepository;
import com.example.auth_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    @Value("${alias.words.file:path/to/wordsAlias.txt}")
    private String wordsAliasPath;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account createAccount(User user) {
        String cvu = generateRandomCVU();
        String alias = generateAlias();

        Account account = new Account(user, cvu, alias);

        return accountRepository.save(account);
    }

    private String generateRandomCVU() {
        return String.format("%022d", random.nextLong() & ((1L << 22 * 4) - 1));
    }

    private String generateAlias() {
        List<String> words = loadWordsFromFile();
        if (words.size() < 3) {
            throw new RuntimeException("Not enough words in the file to generate an alias");
        }

        return randomWords(words, 3).stream().collect(Collectors.joining("."));
    }

    private List<String> loadWordsFromFile() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(wordsAliasPath)))) {
            return reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error loading words file", e);
        }
    }

    private List<String> randomWords(List<String> words, int count) {
        return random.ints(0, words.size())
                .distinct()
                .limit(count)
                .mapToObj(words::get)
                .collect(Collectors.toList());
    }
}
