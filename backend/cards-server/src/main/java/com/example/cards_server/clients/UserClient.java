package com.example.cards_server.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "users-server")
public interface UserClient {

    @GetMapping("api/users/exists/{id}")
    boolean userExists(@PathVariable Long id);
}
