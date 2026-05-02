package com.donaton.bff.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {

    private final Key key = Keys.hmacShaKeyFor(
            "donaton-super-secret-key-1234567890".getBytes()
    );

    // 🔍 Obtener usuario
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 🔐 Obtener roles (seguro)
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object roles = claims.get("roles");

        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .map(Object::toString)
                    .toList();
        }

        return List.of();
    }

    // 🔥 Generar token con roles
    public String generarToken(String usuario, List<String> roles) {
        return Jwts.builder()
                .setSubject(usuario)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(key)
                .compact();
    }

    // 🔍 Obtener claims
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 🔐 Parsear token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ⏱️ Validar expiración (opcional pero PRO)
    public boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
}