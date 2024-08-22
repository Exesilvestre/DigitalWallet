package com.example.auth_server.services;

import com.example.auth_server.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final long accessValidityInMilliseconds = 180000; // 3 minutos
    private final long refreshValidityInMilliseconds = 604800000; // 7 días

    // Genera el access token
    public String generateAccessToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessValidityInMilliseconds);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getEmail());
        claims.put("iat", now.getTime() / 1000); // Tiempo en segundos
        claims.put("exp", validity.getTime() / 1000); // Tiempo en segundos

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Genera el refresh token
    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Extrae los claims del token JWT.
     *
     * @param token El token JWT del cual se extraen los claims.
     * @return Los claims del token.
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Valida el token JWT comprobando la firma y la expiración.
     *
     * @param token El token JWT a validar.
     * @return Verdadero si el token es válido, falso en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);

            // Verificar la expiración del token
            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            // Si no hay excepciones y la expiración es válida, el token es válido
            return true;
        } catch (Exception e) {
            return false; // En caso de cualquier excepción, el token es inválido
        }
    }
}
