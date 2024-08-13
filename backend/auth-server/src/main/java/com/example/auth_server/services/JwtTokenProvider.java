package com.example.auth_server.services;

import com.example.auth_server.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey; // La clave secreta utilizada para firmar el token

    private final long validityInMilliseconds = 180000; // 3 minutos de validez

    /**
     * Genera un token JWT para el usuario especificado.
     *
     * @param user El usuario para el que se genera el token.
     * @return El token JWT generado.
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

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
