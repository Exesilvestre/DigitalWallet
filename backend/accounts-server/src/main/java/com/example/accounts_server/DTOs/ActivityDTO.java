package com.example.accounts_server.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

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

}
