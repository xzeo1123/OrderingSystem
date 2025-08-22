package com.orderingsystem.orderingsystem.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    private SecretKey accessKey()  { return Keys.hmacShaKeyFor(jwtProperties.getAccessSecret().getBytes()); }
    private SecretKey refreshKey() { return Keys.hmacShaKeyFor(jwtProperties.getRefreshSecret().getBytes()); }

    public String generateAccessToken(String username, String role) {
        Date now = new Date();
        Date exp  = new Date(now.getTime() + jwtProperties.getAccessTtl());

        return Jwts.builder()
            .setSubject(username)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(accessKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtProperties.getRefreshTtl());

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(refreshKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(accessKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public String getRole(String token) {
        Object r = Jwts.parserBuilder()
            .setSigningKey(accessKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role");
        return r != null ? r.toString() : null;
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(accessKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(refreshKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromRefresh(String refreshToken) {
        return Jwts.parserBuilder()
            .setSigningKey(refreshKey())
            .build()
            .parseClaimsJws(refreshToken)
            .getBody()
            .getSubject();
    }
}
