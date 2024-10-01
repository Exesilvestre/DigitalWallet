package com.example.activities_server.services;

import com.example.activities_server.DTOs.AccountDTO;
import com.example.activities_server.DTOs.ActivityDTO;
import com.example.activities_server.DTOs.CardDTO;
import com.example.activities_server.DTOs.LoadMoneyRequest;
import com.example.activities_server.clients.AccountClient;
import com.example.activities_server.clients.CardClient;
import com.example.activities_server.entities.Activity;
import com.example.activities_server.exceptions.ResourceNotFoundException;
import com.example.activities_server.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final CardClient cardClient;
    private final AccountClient accountClient;

    public ActivityService(ActivityRepository activityRepository, CardClient cardClient, AccountClient accountClient) {
        this.activityRepository = activityRepository;
        this.cardClient = cardClient;
        this.accountClient = accountClient;
    }

    public List<ActivityDTO> getLatestActivitiesByUserId(Long userId) {
        List<Activity> activities = activityRepository.findTop5ByUserIdOrderByDateDesc(userId);
        return activities.stream()
                .map(ActivityDTO::new)
                .collect(Collectors.toList());
    }

    public List<ActivityDTO> getAllActivitiesByUserId(Long userId) {
        List<Activity> activities = activityRepository.findAllByUserIdOrderByDateDesc(userId);
        return activities.stream()
                .map(ActivityDTO::new)
                .collect(Collectors.toList());
    }

    public ActivityDTO getActivityByUserIdAndTransactionId(Long userId, Long transactionId) {
        Activity activity = activityRepository.findByUserIdAndId(userId, transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with ID " + transactionId + " not found for user " + userId));
        return new ActivityDTO(activity);
    }

    public ActivityDTO loadMoneyByCard(Long userId, LoadMoneyRequest loadMoneyRequest) {
        double amountToLoad = loadMoneyRequest.getAmount();

        if (amountToLoad <= 0) {
            throw new IllegalArgumentException("The amount to load must be greater than zero.");
        }

        CardDTO card = cardClient.getCardByLastFourNumbers(userId, loadMoneyRequest.getCardNumber());
        if (card == null) {
            throw new ResourceNotFoundException("Card not found for userId " + userId + " with card number " + loadMoneyRequest.getCardNumber());
        }

        // Actualiza el saldo y obtiene la cuenta actualizada
        AccountDTO updatedAccount = accountClient.updateAccountBalance(userId, amountToLoad);

        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setAmount(amountToLoad);
        activity.setOrigin(updatedAccount.getAlias()); // Usa el alias de la cuenta actualizada
        activity.setDestination(updatedAccount.getAlias()); // Usa el mismo alias
        activity.setCardId(card.getId());
        activity.setType("LOAD MONEY FROM CARD");
        activity.setDate(String.valueOf(new Date()));
        activity.setDetail("desde tarjeta terminada en " + loadMoneyRequest.getCardNumber());

        Activity savedActivity = activityRepository.save(activity);

        return new ActivityDTO(savedActivity);
    }

}
