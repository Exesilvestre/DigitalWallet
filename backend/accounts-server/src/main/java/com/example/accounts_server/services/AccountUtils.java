package com.example.accounts_server.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class AccountUtils {

    @Value("C:\\Users\\Usuario-\\Desktop\\exe\\ProyectoFinalDH\\backend\\accounts-server\\src\\main\\java\\com\\example\\accounts_server\\services\\wordsAlias.txt")
    private String wordsAliasPath;

    private final Random random = new Random();

    public String generateRandomCVU() {
        String cvu = String.format("%022d", random.nextLong() & ((1L << 22 * 4) - 1));
        System.out.println("Generated CVU: " + cvu);
        return cvu;
    }

    public String generateAlias() {
        List<String> words = loadWordsFromFile();
        if (words.size() < 3) {
            throw new RuntimeException("Not enough words in the file to generate an alias");
        }

        return randomWords(words, 3).stream().collect(Collectors.joining("."));
    }

    private List<String> loadWordsFromFile() {
        try {
            return Files.readAllLines(Paths.get(wordsAliasPath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading words file", e);
        }
    }

    private List<String> randomWords(List<String> words, int count) {
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