package com.example.quiz4retake.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    private final byte[] JWT_SECRET = "123456789123456789123456789123456".getBytes(StandardCharsets.UTF_8);
    private final SecretKey JWT_SECRET_KEY = Keys.hmacShaKeyFor(JWT_SECRET);

    public String generateToken(String username, HashMap<String, String> payload) {
        return Jwts.builder()
                .subject(username)
                .claims(payload)
                .issuedAt(new Date())
                .signWith(JWT_SECRET_KEY)
                .expiration(new Date(System.currentTimeMillis() + (1000L * 60L * 60L * 24L))) // 1 day
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(JWT_SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    public String getRole(String token) {
        return parseToken(token).get("role", String.class);
    }
}
