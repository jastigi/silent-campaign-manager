package com.jastigi.silentcampaignmanager.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {

        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

        } catch (Exception ex) {

            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(
                request.getUsername());

        return new LoginResponse(token);
    }

}
