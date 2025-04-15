package com.microservices.security;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;

import java.util.Date;

import javax.crypto.SecretKey;


@Service
public class JwtService {
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;
    private final SecretKey key = Jwts.SIG.HS256.key().build();


    public String generateToken(String userId, String role) {
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("role", role)
                .signWith(key)
                .compact();
    }
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token); // já valida assinatura
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    
    

}
