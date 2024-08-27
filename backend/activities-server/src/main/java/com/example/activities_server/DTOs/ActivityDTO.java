package com.example.activities_server.DTOs;

import com.example.activities_server.entities.Activity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ActivityDTO {
    private Long id;
    private String name;
    private Double amount;
    private String dated;
    private String origin;
    private String destination;
    private String type;

    public ActivityDTO(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getName();
        this.amount = activity.getAmount();
        this.dated = activity.getDated();
        this.origin = activity.getOrigin();
        this.destination = activity.getDestination();
        this.type = activity.getType();
    }
}
