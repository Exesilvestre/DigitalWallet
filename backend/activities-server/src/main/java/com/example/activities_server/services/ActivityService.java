package com.example.activities_server.services;

import com.example.activities_server.DTOs.ActivityDTO;
import com.example.activities_server.entities.Activity;
import com.example.activities_server.exceptions.ResourceNotFoundException;
import com.example.activities_server.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<ActivityDTO> getLatestActivitiesByUserId(Long userId) {
        List<Activity> activities = activityRepository.findTop5ByUserIdOrderByDatedDesc(userId);
        if (activities.isEmpty()) {
            throw new ResourceNotFoundException("No activities found for user with id " + userId);
        }
        // Usar el constructor de ActivityDTO que acepta Activity
        return activities.stream()
                .map(ActivityDTO::new) // Llamar al constructor que recibe Activity
                .collect(Collectors.toList());
    }
}
