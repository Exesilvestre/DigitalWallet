package com.example.auth_server.services;

import com.example.auth_server.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.token-validity}")
    private long accessValidityInMilliseconds;
    private final Set<String> invalidatedTokens = new HashSet<>();

    // Genera el access token
    public String generateAccessToken(User user) {
        LocalDateTime issuedAt = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime expiresAt = issuedAt.plusSeconds(accessValidityInMilliseconds / 1000);
        String userId = user.getId().toString();

        String token = Jwts.builder()
                .setSubject(userId)
                .claim("email", user.getEmail())
                .setIssuedAt(Date.from(issuedAt.toInstant(ZoneOffset.UTC)))
                .setExpiration(Date.from(expiresAt.toInstant(ZoneOffset.UTC)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();


        return token;
    }

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
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

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public long getExpirationDuration(String token) {
        Claims claims = getClaims(token);
        Date expirationDate = claims.getExpiration();
        return (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
    }
}
