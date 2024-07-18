package com.example.auth_server.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;


import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final JwtAuthConverterProperties properties;


    public JwtAuthConverter(JwtAuthConverterProperties properties) {
        this.properties = properties;
    }


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        System.out.println("JWT: " + jwt.getTokenValue());
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt,authorities,getprincipalClaimName(jwt));
    }


    // para extraer los roles del REINO

/*    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                    Stream.concat(extractResourceRoles(jwt).stream(),extractRealmRoles(jwt).stream()))
                .collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt,authorities,getprincipalClaimName(jwt));
    }*/

    // this is to get the prinicpal name of the user from the preferred username
    private String getprincipalClaimName(Jwt jwt){
        String claimName = JwtClaimNames.SUB;
        if (properties.getPrincipalAttribute() != null){
            claimName = properties.getPrincipalAttribute();
        }
        return  jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource = null;
        Collection<String> resourceRoles;

        if (resourceAccess == null
                || (resource = (Map<String, Object>) resourceAccess.get(properties.getResourceId())) == null
                || (resourceRoles = (Collection<String>) resource.get("roles")) == null) {
            return Set.of();
        }

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }


    // para extraer los roles del REINO
/*  private Collection<? extends GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("realm_access");
        Collection<String> resourceRoles;

        if (resourceAccess == null
                || (resourceRoles = (Collection<String>) resourceAccess.get("roles")
                ) == null) {
            return Set.of();
        }

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }*/

}
