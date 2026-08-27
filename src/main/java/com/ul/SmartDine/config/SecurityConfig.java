package com.ul.SmartDine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] PUBLIC_PATHS = {
            "/api/auth/signup",
            "/api/auth/login",
            "/api/auth/login/google",
            "/api/auth/login/github",
            "/api/auth/otp/verify",
            "/api/auth/password/forgot",
            "/api/auth/password/reset",
            "/actuator/health",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf->csrf.disable()).sessionManagement(sm ->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth-> auth.requestMatchers(PUBLIC_PATHS).permitAll().anyRequest().authenticated()).oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt-> jwt.jwtAuthenticationConverter()))
    }

    private JwtAuthenticationConverter keycloakJwtAuthenticationConverter(){
    }
}
