package com.example.activities_server.controllers;

import com.example.activities_server.DTOs.AccountSummaryDTO;
import com.example.activities_server.DTOs.ActivityDTO;
import com.example.activities_server.DTOs.LoadMoneyRequest;
import com.example.activities_server.DTOs.TransferRequest;
import com.example.activities_server.exceptions.ResourceNotFoundException;
import com.example.activities_server.services.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/{userId}/transferences")
    public ResponseEntity<List<ActivityDTO>> getLatestActivitiesByUserId(@PathVariable Long userId) {
        try {
            List<ActivityDTO> activities = activityService.getLatestActivitiesByUserId(userId);
            return new ResponseEntity<>(activities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/activty")
    public ResponseEntity<List<ActivityDTO>> getAllActivitiesByUserId(@PathVariable Long userId) {
        try {
            System.out.println("hola");
            List<ActivityDTO> activities = activityService.getAllActivitiesByUserId(userId);
            return ResponseEntity.ok(activities);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}/activity/{transactionId}")
    public ResponseEntity<ActivityDTO> getActivityDetail(@PathVariable Long userId, @PathVariable Long transactionId) {
        try {
            ActivityDTO activity = activityService.getActivityByUserIdAndTransactionId(userId, transactionId);
            return ResponseEntity.ok(activity);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/accounts/{userId}/transferences")
    public ResponseEntity<ActivityDTO> loadMoneyByCard(@PathVariable Long userId, @RequestBody LoadMoneyRequest loadMoneyRequest) {
        try {
            ActivityDTO activity = activityService.loadMoneyByCard(userId, loadMoneyRequest);
            return new ResponseEntity<>(activity, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/accounts/{userId}/transfer")
    public ResponseEntity<ActivityDTO> makeTransfer(@PathVariable Long userId, @RequestBody TransferRequest transferRequest) {
        try {
            ActivityDTO activity = activityService.makeTransfer(userId, transferRequest);
            return new ResponseEntity<>(activity, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.GONE);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
