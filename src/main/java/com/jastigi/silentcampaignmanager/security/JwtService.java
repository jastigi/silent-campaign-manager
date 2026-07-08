package com.jastigi.silentcampaignmanager.security;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username) {

        SecretKey key = new SecretKeySpec(
                secretKey.getBytes(),
                Jwts.SIG.HS256.key().build().getAlgorithm());

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 86400000))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {

        SecretKey key = new SecretKeySpec(
                secretKey.getBytes(),
                Jwts.SIG.HS256.key().build().getAlgorithm());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(
            String token,
            String username) {

        return username.equals(
                extractUsername(token));
    }

}
