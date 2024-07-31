package com.example.auth_server.security;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")

// esto es una clase de configuracion, tomara las configuraciones que tengan el prefijo  "jwt.auth.converter"
// tiene como objetivo ser el DTO de esta informacion que saca de jwt.auth.converter
public class JwtAuthConverterProperties {

    private String resourceId;
    private String principalAttribute;

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public void setPrincipalAttribute(String principalAttribute) {
        this.principalAttribute = principalAttribute;
    }







}