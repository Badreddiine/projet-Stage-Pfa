//package com.badr.equipement_service.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Value("${cors.allowed-origins}")
//    private String[] allowedOrigins;
//
//    @Value("${cors.allowed-methods}")
//    private String[] allowedMethods;
//
//    @Value("${cors.allowed-headers}")
//    private String allowedHeaders;
//
//    @Value("${cors.allow-credentials:true}")
//    private boolean allowCredentials;
//
//    @Value("${cors.max-age:3600}")
//    private long maxAge;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins(allowedOrigins)
//                .allowedMethods(allowedMethods)
//                .allowedHeaders(allowedHeaders.split(","))
//                .allowCredentials(allowCredentials)
//                .maxAge(maxAge);
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        // Origins autorisées
//        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
//
//        // Méthodes autorisées
//        configuration.setAllowedMethods(Arrays.asList(allowedMethods));
//
//        // Headers autorisés
//        if ("*".equals(allowedHeaders)) {
//            configuration.addAllowedHeader("*");
//        } else {
//            configuration.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
//        }
//
//        // Headers exposés (pour que le frontend puisse les lire)
//        configuration.setExposedHeaders(List.of(
//                "Authorization",
//                "Content-Type",
//                "X-Requested-With",
//                "accept",
//                "Origin",
//                "Access-Control-Request-Method",
//                "Access-Control-Request-Headers"
//        ));
//
//        configuration.setAllowCredentials(allowCredentials);
//        configuration.setMaxAge(maxAge);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }
//}