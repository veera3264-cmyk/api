package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret:mySuperSecretKeyForDashboardApplication123456789123456}")
    private String secretKey;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpiration;

    public String generateToken(String email, String role) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }
        if (jwtExpiration <= 0) {
            throw new IllegalStateException("JWT expiration must be positive");
        }

        Date issuedAt = new Date();
        String normalizedRole = role == null || role.isBlank()
                ? "ROLE_USER"
                : role.startsWith("ROLE_") ? role : "ROLE_" + role;

        return Jwts.builder()
                .claim("role", normalizedRole)
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(issuedAt.getTime() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public boolean isTokenValid(String token, String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        return email.equals(claims.getSubject())
                && expiration != null
                && expiration.after(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}