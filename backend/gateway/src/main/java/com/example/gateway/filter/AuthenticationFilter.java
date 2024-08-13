package com.example.gateway.filter;

import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import com.example.gateway.repository.IAuthServiceClient;

@Component
public class AuthenticationFilter implements WebFilter {

    @Autowired
    private IAuthServiceClient authServiceClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return authServiceClient.validateToken(token)
                    .flatMap(isValid -> {
                        if (isValid) {
                            return chain.filter(exchange);
                        } else {
                            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                        }
                    });
        } else {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        }
    }
}
