package com.example.users_server.clients;


import com.example.users_server.DTOs.AccountCreatedDTO;
import com.example.users_server.DTOs.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "accounts-server", url = "http://localhost:8083/api")
public interface AccountServiceClient {

    @PostMapping("/create-account")
    AccountCreatedDTO createAccount(@RequestBody UserDTO userDTO);
}
