package com.jastigi.silentcampaignmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI silentCampaignManagerOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title(
                                        "Silent Campaign Manager API")
                                .version(
                                        "1.0.0-RC1")
                                .description(
                                        "REST API for Cold War submarine campaign management and tactical simulation."))
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(
                                        SECURITY_SCHEME_NAME))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .name(
                                                        SECURITY_SCHEME_NAME)
                                                .type(
                                                        SecurityScheme.Type.HTTP)
                                                .scheme(
                                                        "bearer")
                                                .bearerFormat(
                                                        "JWT")));
    }

}
