//package com.badr.equipement_service.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
//import io.swagger.v3.oas.models.servers.Server;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@Configuration
//public class EquipmentSwaggerConfig {
//
//    @Value("${server.port:8082}")
//    private String serverPort;
//
//    @Bean
//    public OpenAPI equipmentServiceOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Equipment Service API")
//                        .description("API pour la gestion des équipements industriels, alertes, données de capteurs et règles de seuil")
//                        .version("1.0.0")
//                        .contact(new Contact()
//                                .name("Équipe SDSGPI")
//                                .email("support@sdsgpi.com")
//                                .url("https://sdsgpi.com" ))
//                        .license(new License()
//                                .name("MIT License")
//                                .url("https://opensource.org/licenses/MIT" )))
//                .servers(List.of(
//                        new Server()
//                                .url("http://localhost:" + serverPort )
//                                .description("Serveur de développement local"),
//                        new Server()
//                                .url("http://equipment-service:" + serverPort )
//                                .description("Serveur Docker interne"),
//                        new Server()
//                                .url("http://localhost:8080" )
//                                .description("Via Gateway")
//                ))
//                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
//                .components(new io.swagger.v3.oas.models.Components()
//                        .addSecuritySchemes("bearerAuth",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")
//                                        .description("JWT token obtenu via Keycloak")));
//    }
//
//    @Bean
//    public GroupedOpenApi equipmentsApi() {
//        return GroupedOpenApi.builder()
//                .group("equipments")
//                .displayName("Équipements API")
//                .pathsToMatch("/api/equipments/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi alertesApi() {
//        return GroupedOpenApi.builder()
//                .group("alertes")
//                .displayName("Alertes API")
//                .pathsToMatch("/api/alertes/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi donneesCapteursApi() {
//        // MODIFICATION : Utilisation d'un nom de groupe cohérent et sans tirets
//        return GroupedOpenApi.builder()
//                .group("capteurs")
//                .displayName("Données Capteurs API")
//                .pathsToMatch("/api/donnees-capteur/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi reglesSeuilApi() {
//        // MODIFICATION : Utilisation d'un nom de groupe cohérent et sans tirets
//        return GroupedOpenApi.builder()
//                .group("seuils")
//                .displayName("Règles de Seuil API")
//                .pathsToMatch("/api/regles-seuil/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi actuatorApi() {
//        return GroupedOpenApi.builder()
//                .group("actuator")
//                .displayName("Monitoring & Management")
//                .pathsToMatch("/actuator/**")
//                .build();
//    }
//}
