package com.jastigi.silentcampaignmanager.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey",
                "mySuperSecretKeyForSilentCampaignManager2026!!SCM");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);
    }

    @Test
    void shouldGenerateToken() {

        String token = jwtService.generateToken("admin");

        assertNotNull(token);
        assertFalse(token.isBlank());

        System.out.println(token);
    }

    @Test
    void shouldExtractUsernameFromToken() {

        String token = jwtService.generateToken("admin");

        String username = jwtService.extractUsername(token);

        assertEquals("admin", username);
    }

}
