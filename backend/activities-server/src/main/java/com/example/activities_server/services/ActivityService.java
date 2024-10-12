package com.example.activities_server.services;

import com.example.activities_server.DTOs.*;
import com.example.activities_server.clients.AccountClient;
import com.example.activities_server.clients.CardClient;
import com.example.activities_server.entities.Activity;
import com.example.activities_server.exceptions.ResourceNotFoundException;
import com.example.activities_server.repositories.ActivityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        activity.setOrigin(updatedAccount.getAlias());
        activity.setDestination(updatedAccount.getAlias());
        activity.setCardId(card.getId());
        activity.setType("LOAD MONEY FROM CARD");
        activity.setDate(String.valueOf(new Date()));
        activity.setDetail("Ingresaste desde tarjeta terminada en " + loadMoneyRequest.getCardNumber());

        Activity savedActivity = activityRepository.save(activity);

        return new ActivityDTO(savedActivity);
    }

    public List<ActivityDTO> getLast5TransfersByUserAlias(Long userId) {
        // Obtener el alias del usuario desde el microservicio de cuentas
        String alias = accountClient.getAccountByUserId(userId).getAlias();

        // Filtrar las actividades donde el 'from' sea el alias del usuario y obtener las 5 más recientes
        List<Activity> activities = activityRepository.findTop5ByOriginOrderByDateDesc(alias);

        return activities.stream()
                .map(ActivityDTO::new)
                .collect(Collectors.toList());
    }

    public ActivityDTO makeTransfer(Long userId, TransferRequest transferRequest) {
        // Obtener la cuenta del usuario de origen
        AccountDTO originAccount = accountClient.getAccountByUserId(userId);
        if (originAccount == null) {
            throw new ResourceNotFoundException("Cuenta no encontrada para userId " + userId);
        }
        AccountSearchRequest searchRequest = new AccountSearchRequest();
        String destination = transferRequest.getDestination();

        // Identificar si el destino es CVU o alias
        if (destination.matches("\\d+")) { // Si es solo dígitos, asumimos que es CVU
            searchRequest.setCvu(destination);
        } else {
            searchRequest.setAlias(destination);
        }

        // Obtener la cuenta del destinatario usando CVU o alias
        ResponseEntity<AccountDTO> responseEntity = accountClient.getAccountByCvuOrAlias(searchRequest);
        AccountDTO destinationAccount = responseEntity.getBody();
        if (destinationAccount == null) {
            throw new ResourceNotFoundException("Cuenta no encontrada para el alias o CVU proporcionado.");
        }

        // Validar fondos suficientes
        if (originAccount.getBalance() < transferRequest.getAmount()) {
            throw new IllegalArgumentException("Fondos insuficientes");
        }

        // Actualizar el balance de la cuenta de origen (restar)
        AccountDTO updatedOriginAccount = accountClient.updateAccountBalance(userId, transferRequest.getAmount());

        // Actualizar el balance de la cuenta de destino (sumar)
        AccountDTO updatedDestinationAccount = accountClient.updateAccountBalance(Long.valueOf(destinationAccount.getUserId()), Math.abs(transferRequest.getAmount()));

        // Guardar la actividad para el **usuario de origen**
        Activity originActivity = new Activity();
        originActivity.setUserId(userId);
        originActivity.setAmount(transferRequest.getAmount());
        originActivity.setOrigin(updatedOriginAccount.getAlias());
        originActivity.setDestination(updatedDestinationAccount.getAlias());
        originActivity.setType("Transfer");
        originActivity.setDate(new Date().toString());
        originActivity.setDetail("Transferencia a " + transferRequest.getDestination());

        Activity savedOriginActivity = activityRepository.save(originActivity);

        // Guardar la actividad para el **usuario de destino**
        Activity destinationActivity = new Activity();
        destinationActivity.setUserId(Long.valueOf(destinationAccount.getUserId()));
        destinationActivity.setAmount(Math.abs(transferRequest.getAmount()));
        destinationActivity.setOrigin(updatedOriginAccount.getAlias());
        destinationActivity.setDestination(updatedDestinationAccount.getAlias());
        destinationActivity.setType("Transfer");
        destinationActivity.setDate(new Date().toString());
        destinationActivity.setDetail("Recibiste transferencia desde " + originAccount.getAlias());

        activityRepository.save(destinationActivity);

        // Retornar la actividad del usuario origen (puedes ajustar esto según lo que desees retornar)
        return new ActivityDTO(savedOriginActivity);
    }

}
