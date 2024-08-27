package com.example.accounts_server.clients;
import com.example.accounts_server.DTOs.ActivityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "activities-service")
public interface ActivitiesClient {
    @GetMapping("/activities/user/{userId}/latest")
    List<ActivityDTO> getLatestActivitiesByUserId(@PathVariable("userId") Long userId);
}
