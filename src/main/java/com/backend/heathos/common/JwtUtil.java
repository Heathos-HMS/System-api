package com.backend.heathos.common;


import com.backend.heathos.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;  // Reads the secret from application.properties

    @Value("${jwt.expiration-ms}")
    private long expirationMs;  // Reads 86400000 from application.properties

    // Converts the secret string into a proper cryptographic key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

   //CREATES TOKEN AFTER SUCCESSFUL LOGIN FOR USER
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())  // who this token is for (userId)
                .claim("email", user.getEmail())       // extra info stored inside the token
                .claim("role", user.getRole().name())  // the role stored as "DOCTOR" etc.
                .setIssuedAt(new Date())               // when this token was created
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs)) // when it expires
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // sign it with our secret
                .compact(); // convert to the final token string
    }

    // READ all claims from a token
    // Claims = all the data stored inside the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // GET the userId from a token
    public UUID getUserIdFromToken(String token) {
        String subject = extractAllClaims(token).getSubject();
        return UUID.fromString(subject);
    }

    // GET the role from a token
    public String getRoleFromToken(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    //CHECK if a token is still valid (not expired)
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token); // if this throws, token is invalid or expired
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
