package com.example.activities_server.controllers;

import com.example.activities_server.DTOs.ActivityDTO;
import com.example.activities_server.exceptions.ResourceNotFoundException;
import com.example.activities_server.services.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/activities/user/{userId}/latest")
    public ResponseEntity<List<ActivityDTO>> getLatestActivitiesByUserId(@PathVariable Long userId) {
        try {
            List<ActivityDTO> activities = activityService.getLatestActivitiesByUserId(userId);
            return new ResponseEntity<>(activities, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
