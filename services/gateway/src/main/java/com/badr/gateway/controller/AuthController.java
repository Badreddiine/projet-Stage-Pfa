package com.badr.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${spring.security.oauth2.client.provider.keycloak.authorization-uri}")
    private String authorizationUri;

    @Value("${keycloak.logout-uri}")
    private String keycloakLogoutUri;

    @Value("${frontend.redirect-uri}")
    private String frontendRedirectUri;

    @GetMapping("/login-url")
    public Mono<ResponseEntity<Map<String, String>>> getLoginUrl() {
        Map<String, String> response = new HashMap<>();
        response.put("loginUrl", authorizationUri +
                "?client_id=frontend-client" +
                "&redirect_uri=" + frontendRedirectUri +
                "&response_type=code" +
                "&scope=openid");
        return Mono.just(ResponseEntity.ok(response));
    }

    @GetMapping("/register-url")
    public Mono<ResponseEntity<Map<String, String>>> getRegisterUrl() {
        Map<String, String> response = new HashMap<>();
        response.put("registerUrl", authorizationUri +
                "?client_id=frontend-client" +
                "&redirect_uri=" + frontendRedirectUri +
                "&response_type=code" +
                "&scope=openid" +
                "&kc_action=register");
        return Mono.just(ResponseEntity.ok(response));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Map<String, String>>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("logoutUrl", keycloakLogoutUri +
                "?redirect_uri=" + frontendRedirectUri);
        return Mono.just(ResponseEntity.ok(response));
    }
}

