package com.example.accounts_server.services;

import com.example.accounts_server.DTOs.UserDTO;
import com.example.accounts_server.entities.Account;
import com.example.accounts_server.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final Random random = new Random();

    @Value("C:\\Users\\Usuario-\\Desktop\\exe\\ProyectoFinalDH\\backend\\auth-server\\src\\main\\java\\com\\example\\auth_server\\services\\wordsAlias.txt")
    private String wordsAliasPath;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(UserDTO userDTO) {
        System.out.println("Creating account for user: " + userDTO);
        String cvu = generateRandomCVU();
        String alias = generateAlias();

        Account account = new Account(userDTO, cvu, alias);

        System.out.println("Generated CVU: " + cvu);
        System.out.println("Generated alias: " + alias);

        return accountRepository.save(account);
    }

    private String generateRandomCVU() {
        String cvu = String.format("%022d", random.nextLong() & ((1L << 22 * 4) - 1));
        System.out.println("Generated CVU: " + cvu);
        return cvu;
    }

    private String generateAlias() {
        List<String> words = loadWordsFromFile();
        if (words.size() < 3) {
            throw new RuntimeException("Not enough words in the file to generate an alias");
        }

        String alias = randomWords(words, 3).stream().collect(Collectors.joining("."));
        System.out.println("Generated alias: " + alias);
        return alias;
    }

    private List<String> loadWordsFromFile() {
        System.out.println("Loading words from file: " + wordsAliasPath);
        try {
            List<String> lines = Files.readAllLines(Paths.get(wordsAliasPath));
            System.out.println("Loaded lines: " + lines);
            return lines;
        } catch (IOException e) {
            System.err.println("Error loading words file: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error loading words file", e);
        }
    }

    private List<String> randomWords(List<String> words, int count) {
        // Ensure count does not exceed the number of available words
        if (count > words.size()) {
            throw new IllegalArgumentException("Count exceeds number of available words");
        }

        return random.ints(0, words.size())
                .distinct()
                .limit(count)
                .mapToObj(words::get)
                .collect(Collectors.toList());
    }
}
