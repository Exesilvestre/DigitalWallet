package com.example.activities_server.clients;

import com.example.activities_server.DTOs.CardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cards-server")
public interface CardClient {
    @GetMapping("api/accounts/{userId}/cards/by-number/{cardNumber}")
    CardDTO getCardByLastFourNumbers(@PathVariable("userId") Long userId, @PathVariable("cardNumber") String cardNumber);
}
