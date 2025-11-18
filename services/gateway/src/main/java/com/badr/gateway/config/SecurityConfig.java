package com.badr.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // ENDPOINTS PUBLICS - SWAGGER ET DOCUMENTATION
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/swagger-config/**",
                                "/equipment/swagger-ui/**",
                                "/equipment/v3/api-docs/**",
                                "/equipment/swagger-ui.html",
                                "/*/swagger-ui/**",
                                "/*/v3/api-docs/**",
                                "/*/swagger-ui.html",
                                "/notification-service/v3/api-docs/**",
                                "/incident-service/v3/api-docs/**",
                                "/analysis-service/v3/api-docs/**",
                                "/mqtt-service/v3/api-docs/**",
                                "/config-service/v3/api-docs/**"
                        ).permitAll()

                        // ENDPOINTS ACTUATOR ET MONITORING
                        .pathMatchers(
                                "/actuator/**",
                                "/equipment/actuator/**",
                                "/*/actuator/**"
                        ).permitAll()

                        // OPTIONS requests pour CORS
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()

                        // ENDPOINTS EUREKA ET INFRASTRUCTURE
                        .pathMatchers(
                                "/eureka/**",
                                "/",
                                "/health/**",
                                "/info/**",
                                "/favicon.ico"
                        ).permitAll()

                        // ENDPOINTS PUBLICS D'AUTHENTIFICATION
                        .pathMatchers(
                                "/api/auth/**",
                                "/api/public/**",
                                "/realms/**",
                                "/login/**",
                                "/register/**"
                        ).permitAll()

                        // USER-SERVICE - ENDPOINTS PUBLICS
                        .pathMatchers(
                                "/user-service/api/users/all",
                                "/user-service/api/users/me/**",
                                "/api/users/all",
                                "/api/users/me/**"
                        ).permitAll()

                        // EQUIPMENT-SERVICE - ENDPOINTS PUBLICS SPÉCIFIQUES
                        .pathMatchers(
                                "/equipment-service/api/alertes/**",
                                "/api/alertes/**",
                                "/equipment-service/api/donnees-capteur/**",
                                "/api/donnees-capteur/**",
                                "/equipment-service/api/regles-seuil/**",
                                "/api/regles-seuil/**"
                        ).permitAll()

                        // =================================================================
                        // ENDPOINTS PROTÉGÉS - L'ORDRE EST IMPORTANT (du plus spécifique au plus général)
                        // =================================================================

                        // GESTION DES UTILISATEURS - PROTÉGÉ
                        .pathMatchers("/api/users/admin/**").hasRole("ADMIN")
                        .pathMatchers("/api/users/profile/**").hasAnyRole("USER", "ADMIN", "INGENIEUR", "TECHNICIEN", "OPERATEUR")
                        .pathMatchers("/api/projects/**").hasAnyRole("ADMIN", "INGENIEUR", "TECHNICIEN")

                        // EQUIPMENT SERVICE ENDPOINTS - PROTÉGÉS
                        .pathMatchers("/api/equipments/**").hasAnyRole("ADMIN", "INGENIEUR", "TECHNICIEN", "OPERATEUR")
                        .pathMatchers("/equipment-service/api/equipments/**").hasAnyRole("ADMIN", "INGENIEUR", "TECHNICIEN", "OPERATEUR")

                        // Règles de seuil - PROTÉGÉES (création/modification limitée)
                        .pathMatchers(HttpMethod.POST, "/api/regles-seuil/**").hasAnyRole("ADMIN", "INGENIEUR", "TECHNICIEN")
                        .pathMatchers(HttpMethod.PUT, "/api/regles-seuil/**").hasAnyRole("ADMIN", "INGENIEUR", "TECHNICIEN")
                        .pathMatchers(HttpMethod.DELETE, "/api/regles-seuil/**").hasAnyRole("ADMIN", "INGENIEUR")

                        // AUTRES SERVICES - PROTÉGÉS
                        .pathMatchers("/api/notifications/**").authenticated()
                        .pathMatchers("/api/incidents/**").authenticated()
                        .pathMatchers("/api/analysis/**").authenticated()
                        .pathMatchers("/api/mqtt/**").authenticated()

                        // PAR DÉFAUT - AUTHENTIFICATION REQUISE
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(grantedAuthoritiesConverter())
                        )
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                )
                .build();
    }

    @Bean
    public ServerAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (exchange, ex) -> {
            String path = exchange.getRequest().getPath().value();

            // Patterns d'endpoints publics
            String[] publicPatterns = {
                    "/actuator", "/swagger-ui", "/v3/api-docs", "/webjars",
                    "/equipment/swagger-ui", "/equipment/v3/api-docs",
                    "/eureka", "/health", "/info", "/swagger-resources",
                    "/favicon.ico", "/swagger-config",
                    "/api/auth", "/api/public", "/realms", "/login", "/register",
                    // Endpoints publics spécifiques
                    "/api/alertes", "/api/donnees-capteur"
            };

            // Vérifier si c'est un endpoint public
            for (String pattern : publicPatterns) {
                if (path.contains(pattern)) {
                    return Mono.empty();
                }
            }

            // Log pour debug
            System.err.println("Accès refusé pour le chemin: " + path);
            System.err.println("Exception: " + ex.getMessage());

            // Définir le statut 401 pour les endpoints protégés
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

    static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            try {
                // Debug logging
                System.out.println("=== JWT Token Debug ===");
                System.out.println("Subject: " + jwt.getSubject());

                final Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
                final Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");

                System.out.println("Realm Access: " + realmAccess);
                System.out.println("Resource Access: " + resourceAccess);

                Stream<String> realmRoles = Stream.empty();
                if (realmAccess != null && realmAccess.containsKey("roles")) {
                    realmRoles = ((List<String>) realmAccess.get("roles")).stream();
                }

                Stream<String> clientRoles = Stream.empty();
                if (resourceAccess != null && resourceAccess.containsKey("frontend-client")) {
                    final Map<String, Object> clientResource = (Map<String, Object>) resourceAccess.get("frontend-client");
                    if (clientResource != null && clientResource.containsKey("roles")) {
                        clientRoles = ((List<String>) clientResource.get("roles")).stream();
                    }
                }

                Collection<GrantedAuthority> authorities = Stream.concat(realmRoles, clientRoles)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        .collect(Collectors.toList());

                System.out.println("Authorities extraites: " + authorities);
                return authorities;

            } catch (Exception e) {
                System.err.println("Erreur lors de l'extraction des rôles JWT: " + e.getMessage());
                e.printStackTrace();
                return List.of();
            }
        }
    }
}