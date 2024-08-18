package com.example.gateway.config;

import com.example.gateway.security.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("users-server", r -> r.path("/api/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://users-service"))
                .route("auth-server", r -> r.path("/api/**")
                        .uri("lb://auth-server"))
                .build();
    }

}