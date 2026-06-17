package com.jastigi.silentcampaignmanager.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.LoginRequest;
import com.jastigi.silentcampaignmanager.dto.LoginResponse;
import com.jastigi.silentcampaignmanager.exception.InvalidCredentialsException;
import com.jastigi.silentcampaignmanager.security.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        if (!"admin".equals(request.getUsername())
                || !"admin123".equals(request.getPassword())) {

            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(
                request.getUsername());

        return new LoginResponse(token);
    }

}
