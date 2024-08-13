package com.example.gateway.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

@FeignClient(name = "auth-service", url = "http://localhost:8081")
public interface IAuthServiceClient {

    @GetMapping("/api/auth/validate")
    Mono<Boolean> validateToken(@RequestHeader("Authorization") String token);
}
