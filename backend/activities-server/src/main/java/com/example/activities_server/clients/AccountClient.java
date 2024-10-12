package com.example.activities_server.clients;

import com.example.activities_server.DTOs.AccountDTO;
import com.example.activities_server.DTOs.AccountSearchRequest;
import com.example.activities_server.DTOs.TransferRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "accounts-server", url = "http://localhost:8085/api")
public interface AccountClient {

    @PutMapping("/accounts/{userId}/balance") // Updated path
    AccountDTO updateAccountBalance(@PathVariable("userId") Long userId, @RequestBody Double amount);

    @GetMapping("/accounts/{userId}")
    AccountDTO getAccountByUserId(@PathVariable("userId") Long userId);

    @PostMapping("/accounts/transfer")
    ResponseEntity<String> transfer(@RequestBody TransferRequest transferRequest);

    @PostMapping("/accounts/search") // Cambiado a POST
    ResponseEntity<AccountDTO> getAccountByCvuOrAlias(@RequestBody AccountSearchRequest request);
}
