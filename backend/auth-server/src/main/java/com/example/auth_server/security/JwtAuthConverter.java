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

// implementa la clase que tiene como INPUT un JWT y como output un AbstractAuthenticationToken
// lo que hace esta clase es tomar la informacion del JWT emitido por el IAM y va a sacar de ese JWT informacion
// que nos sirve en spring para tener info necesario para tener autentticidad y autorizacion


@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final JwtAuthConverterProperties properties;


    public JwtAuthConverter(JwtAuthConverterProperties properties) {
        this.properties = properties;
    }


    @Override
    //Toma la informacion del JWT emitido por el IAM y saca del JWT cierta info que nos sirve como spring dentro de nuestra app
    // en el contexto la info necesaria para tener acceso dentro de la app
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

    //CON ESTE GET vamos a obtener el nombre del user, recordemos que lo definimos en properties con "preferred_username"
    private String getprincipalClaimName(Jwt jwt){
        String claimName = JwtClaimNames.SUB;
        if (properties.getPrincipalAttribute() != null){
            claimName = properties.getPrincipalAttribute();
        }
        return  jwt.getClaim(claimName);
    }

    // Este metodo extrae los roles del JWT
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