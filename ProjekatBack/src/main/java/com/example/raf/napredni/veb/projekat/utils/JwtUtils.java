package com.example.raf.napredni.veb.projekat.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class JwtUtils {
    private final String key = "Tajni Kljuc!";

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String generateJwtToken(String email, Set<String> permissions) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("permissions", permissions);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, key).compact();
    }

    public boolean validateToken(String token, MyUserDetails user) {
        return (user.getEmail().equals(extractEmail(token)) && !isTokenExpired(token));
    }

    public boolean canReadUsers(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("can_read_users", Boolean.class);
    }

    public boolean canCreateUsers(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("can_create_users", Boolean.class);
    }

    public boolean canUpdateUsers(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("can_update_users", Boolean.class);
    }

    public boolean canDeleteUsers(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("can_delete_users", Boolean.class);
    }
}
