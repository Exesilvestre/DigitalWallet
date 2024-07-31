package com.example.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.server.SecurityWebFilterChain;

public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/userKeycloak/**", "/userKeycloak/create").permitAll()
                        .pathMatchers("/user/**").permitAll()
                        .pathMatchers("/accounts/**").permitAll()
                        .pathMatchers("/doc/**").permitAll()
                        .anyExchange().permitAll()
                );

        return http.build();
    }

    @Bean
    public StrictHttpFirewall allowDoubleSlash() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true); // Permite "//" en la URL
        return firewall;
    }
}