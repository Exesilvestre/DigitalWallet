package com.example.auth_server.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtAuthConverterProperties properties;

    public JwtAuthConverter(JwtAuthConverterProperties properties) {
        this.properties = properties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // Extraer roles del JWT
        Collection<? extends GrantedAuthority> authorities = extractRoles(jwt);

        // Convertir a Collection<GrantedAuthority>
        Collection<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(auth -> (GrantedAuthority) auth)
                .collect(Collectors.toSet());

        // Crear el token de autenticación con el JWT y las autoridades extraídas
        return new JwtAuthenticationToken(jwt, grantedAuthorities, getPrincipalClaimName(jwt));
    }

    // Obtener el nombre del principal (usuario) del JWT
    private String getPrincipalClaimName(Jwt jwt) {
        return jwt.getClaim("email"); // Asumiendo que usas "email" como claim del principal
    }

    // Extraer roles del JWT
    private Collection<? extends GrantedAuthority> extractRoles(Jwt jwt) {
        // Obtener los roles del JWT
        Collection<String> roles = (Collection<String>) jwt.getClaims().getOrDefault("roles", Set.of());

        // Convertir roles a authorities
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}