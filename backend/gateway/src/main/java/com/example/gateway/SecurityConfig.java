package com.example.gateway;

import com.example.gateway.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationFilter authenticationFilter) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth-server/api/login", "/auth-server/api/register").permitAll() // Permitir acceso sin autenticación a estas rutas
                        .anyExchange().authenticated() // Requerir autenticación para cualquier otra ruta
                )
                .addFilterBefore(authenticationFilter, SecurityWebFiltersOrder.AUTHORIZATION); // Añadir el filtro de autenticación antes del filtro de autorización
        return http.build();
    }
}
