package com.jastigi.silentcampaignmanager.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void shouldGenerateToken() {

        String token = jwtService.generateToken("admin");

        assertNotNull(token);
        assertFalse(token.isBlank());

        System.out.println(token);
    }

}
