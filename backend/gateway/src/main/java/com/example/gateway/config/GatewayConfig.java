package com.example.gateway.config;

import com.example.gateway.security.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

@Configuration
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    private static final String SECRET_TOKEN = "from-gateway"; // Define your secret token here

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Ruta para el auth-server
                .route("auth-server-logout", r -> r.path("/auth-server/api/logout")
                        .filters(f -> f
                                .filter(filter)
                                .addRequestHeader("X-Secret-Token", SECRET_TOKEN)
                                .rewritePath("/auth-server/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8081"))
                .route("auth-server-login", r -> r.path("/auth-server/api/login")
                        .filters(f -> f
                                .addRequestHeader("X-Secret-Token", SECRET_TOKEN)
                                .rewritePath("/auth-server/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8081"))
                // Ruta para el users-server
                .route("users-server-register", r -> r.path("/users-server/api/register")
                        .filters(f -> f
                                .addRequestHeader("X-Secret-Token", SECRET_TOKEN)
                                .rewritePath("/users-server/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8083"))
                .route("users-server", r -> r.path("/users-server/api/user/{id}")
                        .filters(f -> f
                                .addRequestHeader("X-Secret-Token", SECRET_TOKEN)
                                .rewritePath("/users-server/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8083"))
                .route("users-server", r -> r.path("/users-server/api/**")
                        .filters(f -> f
                                .filter(filter)
                                .addRequestHeader("X-Secret-Token", SECRET_TOKEN)
                                .rewritePath("/users-server/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8083"))
                // Ruta para el accounts-server
                .route("accounts-server", r -> r.path("/accounts-server/api/**")
                        .filters(f -> f
                                .filter(filter)
                                .addRequestHeader("X-Secret-Token", SECRET_TOKEN)
                                .rewritePath("/accounts-server/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8085"))
                // Ruta para el cards-server
                .route("cards-server", r -> r.path("/cards-server/api/**")
                        .filters(f -> f
                                .filter(filter)
                                .addRequestHeader("X-Secret-Token", SECRET_TOKEN)
                                .rewritePath("/cards-server/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8087"))
                // Ruta para el activities-server
                .route("activities-server", r -> r.path("/activities-server/api/**")
                        .filters(f -> f
                                .filter(filter)
                                .addRequestHeader("X-Secret-Token", SECRET_TOKEN)
                                .rewritePath("/activities-server/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8086"))
                .build();
    }

    @Bean
    public WebFilter corsWebFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            if (CorsUtils.isCorsRequest(exchange.getRequest())) {
                exchange.getResponse().getHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponse().getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
                exchange.getResponse().getHeaders().add("Access-Control-Allow-Headers", "Authorization, Content-Type");
                if (exchange.getRequest().getMethod().name().equals("OPTIONS")) {
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.OK);
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange);
        };
    }

}