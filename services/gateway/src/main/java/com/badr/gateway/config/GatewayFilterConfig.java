package com.badr.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayFilterConfig {

    /**
     * Filtre global pour surveiller et journaliser les en-têtes volumineux
     */
    @Bean
    public GlobalFilter headerSizeLoggingFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = request.getHeaders();

            // Calculer la taille totale des en-têtes
            int totalHeaderSize = calculateHeaderSize(headers);

            // Journaliser les en-têtes volumineux
            if (totalHeaderSize > 32768) { // Seuil de 32KB
                System.out.println("ATTENTION: En-têtes volumineux détectés - Taille totale: " + totalHeaderSize + " octets");
                System.out.println("Chemin de la requête: " + request.getPath().value());

                // Journaliser les en-têtes individuels volumineux
                headers.forEach((name, values) -> {
                    values.forEach(value -> {
                        if (value.length() > 8192) { // Seuil de 8KB pour les en-têtes individuels
                            System.out.println("En-tête volumineux: " + name + " = " + value.length() + " octets");
                        }
                    });
                });
            }

            return chain.filter(exchange);
        };
    }

    /**
     * Filtre pour optimiser les en-têtes et réduire leur taille
     */
    @Bean
    public GlobalFilter headerOptimizationFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().value();

            // Optimiser les en-têtes pour certains endpoints
            if (isInternalEndpoint(path)) {
                ServerHttpRequest modifiedRequest = request.mutate()
                        .headers(headers -> optimizeHeaders(headers))
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }

            return chain.filter(exchange);
        };
    }

    /**
     * Filtre pour ajouter des en-têtes de réponse personnalisés
     */
    @Bean
    public GlobalFilter responseHeaderFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().add("X-Gateway-Version", "1.0.0");
                response.getHeaders().add("X-Processed-By", "SDSGPI-Gateway");
            }));
        };
    }

    private int calculateHeaderSize(HttpHeaders headers) {
        int totalSize = 0;
        for (String name : headers.keySet()) {
            for (String value : headers.get(name)) {
                totalSize += name.length() + value.length() + 4; // +4 pour ": " et "\r\n"
            }
        }
        return totalSize;
    }

    private boolean isInternalEndpoint(String path) {
        return path.startsWith("/actuator/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs/") ||
                path.startsWith("/webjars/");
    }

    private void optimizeHeaders(HttpHeaders headers) {
        // Supprimer les en-têtes non nécessaires pour les endpoints internes
        headers.remove("Cookie");
        headers.remove("X-Forwarded-For");
        headers.remove("X-Forwarded-Proto");
        headers.remove("X-Real-IP");

        // Conserver seulement les en-têtes essentiels
        String userAgent = headers.getFirst("User-Agent");
        String accept = headers.getFirst("Accept");
        String host = headers.getFirst("Host");
        String authorization = headers.getFirst("Authorization");

        headers.clear();

        if (userAgent != null) headers.add("User-Agent", userAgent);
        if (accept != null) headers.add("Accept", accept);
        if (host != null) headers.add("Host", host);
        if (authorization != null) headers.add("Authorization", authorization);
    }
}