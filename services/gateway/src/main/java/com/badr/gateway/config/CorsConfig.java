package com.badr.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:3001" ); // Remplacez par l'URL de votre frontend Next.js
        corsConfig.addAllowedMethod("*"); // Autorise toutes les méthodes (GET, POST, PUT, DELETE, etc.)
        corsConfig.addAllowedHeader("*"); // Autorise tous les en-têtes, y compris Authorization
        corsConfig.setAllowCredentials(true); // Autorise l'envoi de cookies et d'en-têtes d'authentification
        corsConfig.setMaxAge(3600L); // Cache la pré-vérification CORS pendant 1 heure

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Applique cette configuration à toutes les routes
        return new CorsWebFilter(source);
    }
}
