package com.example.activities_server.clients;

import com.example.activities_server.DTOs.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "accounts-server", url = "http://localhost:8085/api")
public interface AccountClient {

    @PutMapping("/accounts/{userId}/balance") // Updated path
    AccountDTO updateAccountBalance(@PathVariable("userId") Long userId, @RequestBody Double amount);

}
