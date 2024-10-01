package com.example.activities_server.DTOs;

import com.example.activities_server.entities.Activity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActivityDTO {
    private Long id;
    private Double amount;
    private String date;
    private String origin;
    private String destination;
    private String type;
    private String detail;
    private Long cardId;

    public ActivityDTO(Activity activity) {
        this.id = activity.getId();
        this.amount = activity.getAmount();
        this.date = activity.getDate();  // Cambiando 'dated' a 'date'
        this.origin = activity.getOrigin();
        this.destination = activity.getDestination();
        this.type = activity.getType();
        this.detail = activity.getDetail();  // Agregando 'detail'
        this.cardId = activity.getCardId();  // Agregando 'cardId'
    }
}
