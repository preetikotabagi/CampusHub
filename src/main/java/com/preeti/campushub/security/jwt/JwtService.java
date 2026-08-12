package com.preeti.campushub.security.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // 256-bit Base64 encoded secret, sourced from JWT_SECRET (see .env / application.properties).
    // Previously this was hardcoded here and JWT_SECRET in .env was unused/dead config -
    // anyone with the source (e.g. in git history) could forge tokens. Now the source is
    // the single source of truth.
    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_MS = 1000L * 60 * 60 * 24; // 24 hours

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith((SecretKey) getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // NOTE: parseSignedClaims (called via extractClaims) already throws
    // io.jsonwebtoken.ExpiredJwtException for an expired token, so an explicit
    // "isExpired" check here would never be reached for a real expired token.
    // Expiry, tampering, and malformed tokens are all surfaced as exceptions
    // and handled by the caller (see JwtAuthenticationFilter).
    public boolean isTokenValid(String token, String email) {
        return extractEmail(token).equals(email);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}