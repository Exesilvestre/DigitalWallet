package com.example.cards_server;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String SECRET_HEADER_NAME = "X-Secret-Token";
    private static final String SECRET_TOKEN = "from-gateway";

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new SecretTokenValidationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public static class SecretTokenValidationFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

            String token = request.getHeader(SECRET_HEADER_NAME);
            System.out.println("Received Secret Token: " + token);

            if (SECRET_TOKEN.equals(token)) {
                System.out.println("Secret token is valid, proceeding with request.");

                // Configura el contexto de seguridad si el token es válido
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        "user", // Principal
                        null, // Credentials
                        Collections.emptyList() // Authorities
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);
            } else {
                System.out.println("Secret token is invalid, denying request.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }
}
