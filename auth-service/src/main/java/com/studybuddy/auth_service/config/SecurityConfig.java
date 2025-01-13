package com.studybuddy.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF protection
                .authorizeRequests()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/member/**").permitAll()
                .anyRequest().permitAll() // Allow all requests without authentication
                .and()
                .formLogin().disable() // Disable form login
                .httpBasic().disable() // Disable HTTP basic authentication
                .headers()
                .frameOptions().disable(); // Disable frame options for H2 Console

        return http.build();
    }
}
