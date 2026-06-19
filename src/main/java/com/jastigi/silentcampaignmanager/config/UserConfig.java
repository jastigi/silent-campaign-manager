package com.jastigi.silentcampaignmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableMethodSecurity
public class UserConfig {

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.withUsername("admin")
                .password("$2a$10$NaEngq3poo3uqaI6OfcmCufL3b7PckwJ4y2CV8Gu0Ice.cCZFv.oS")
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("user")
                .password("$2a$10$GbQreTc7X5NRh0u0YWfiC.3EpgwRKXBUMssJ73NW2Gd2vJUFy1PWO")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

}
