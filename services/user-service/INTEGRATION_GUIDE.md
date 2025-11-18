# Guide d'Intégration - User Service

## Vue d'ensemble de l'intégration

Ce guide détaille comment intégrer le User Service dans votre architecture microservices existante pour gérer les informations utilisateur de manière centralisée et sécurisée.

## Architecture d'intégration

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│    Keycloak     │    │   API Gateway   │    │  User Service   │
│  (Auth Server)  │────│  (Entry Point)  │────│ (User Manager)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                        │
                                │                        │
                       ┌────────▼────────┐              │
                       │  Microservices  │◄─────────────┘
                       │   (Equipment,   │
                       │  Notification)  │
                       └─────────────────┘
```

## Étape 1 : Mise à jour de votre docker-compose.yml

### 1.1 Ajout du script d'initialisation de la base de données

Ajoutez le contenu suivant à votre fichier `postgres-init/init-user-db.sql` :

```sql
-- Création de la base de données pour le user-service
CREATE DATABASE user_db;
CREATE USER user_user WITH ENCRYPTED PASSWORD 'user_password';
GRANT ALL PRIVILEGES ON DATABASE user_db TO user_user;

\c user_db;
GRANT ALL ON SCHEMA public TO user_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO user_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO user_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO user_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO user_user;
```

### 1.2 Ajout du service user-service

Ajoutez cette section à votre `docker-compose.yml` existant :

```yaml
  user-service:
    build:
      context: ./services/user-service
    container_name: user_service_container
    hostname: user-service
    ports:
      - "8084:8084"
      - "9003:9003"
    depends_on:
      postgres_db:
        condition: service_healthy
      config-server:
        condition: service_healthy
      discovery-service:
        condition: service_healthy
    networks:
      - sdsgpi_net
    environment:
      # Spring Configuration
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_APPLICATION_NAME=user-service
      - SERVER_PORT=8084
      
      # Config Server
      - SPRING_CONFIG_IMPORT=optional:configserver:http://config-server:8888
      - SPRING_CLOUD_CONFIG_USERNAME=admin
      - SPRING_CLOUD_CONFIG_PASSWORD=admin
      
      # Eureka Discovery
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://admin:admin123@discovery-service:8761/eureka/
      - EUREKA_CLIENT_REGISTER_WITH_EUREKA=true
      - EUREKA_CLIENT_FETCH_REGISTRY=true
      - EUREKA_CLIENT_ENABLED=true
      - EUREKA_INSTANCE_HOSTNAME=user-service
      - EUREKA_INSTANCE_INSTANCE_ID=user-service:8084
      
      # Database Configuration
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres_db:5432/user_db
      - SPRING_DATASOURCE_USERNAME=user_user
      - SPRING_DATASOURCE_PASSWORD=user_password
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
      
      # Security Configuration
      - SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://keycloak:8080/realms/auth-service-realm
      - SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI=http://keycloak:8080/realms/auth-service-realm/protocol/openid_connect/certs
      
      # Management Endpoints
      - MANAGEMENT_SERVER_PORT=9003
      - MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,metrics,prometheus,eureka
      - MANAGEMENT_ENDPOINT_HEALTH_SHOW_DETAILS=always
      
      # User Service Specific
      - USER_SERVICE_KEYCLOAK_SYNC_ENABLED=true
      - USER_SERVICE_KEYCLOAK_SYNC_ON_LOGIN=true
      - USER_SERVICE_DEFAULT_ROLE=USER
      
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:9003/actuator/health"]
      interval: 15s
      timeout: 10s
      start_period: 120s
      retries: 10
    restart: unless-stopped
```

## Étape 2 : Configuration de l'API Gateway

### 2.1 Ajout du filtre global UserInfoFilter

Créez le fichier `UserInfoFilter.java` dans votre Gateway :

```java
package com.sdsgpi.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserInfoFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
            .filter(context -> context.getAuthentication() instanceof JwtAuthenticationToken)
            .map(context -> (JwtAuthenticationToken) context.getAuthentication())
            .flatMap(authentication -> {
                Jwt jwt = authentication.getToken();

                // Extraire les informations du token JWT
                String userId = jwt.getSubject();
                String username = jwt.getClaimAsString("preferred_username");
                String email = jwt.getClaimAsString("email");
                String firstName = jwt.getClaimAsString("given_name");
                String lastName = jwt.getClaimAsString("family_name");
                
                // Récupérer les rôles du realm
                List<String> roles = extractRoles(jwt);

                // Construire un objet JSON avec les informations
                String userInfoJson = String.format(
                    "{\"userId\":\"%s\", \"username\":\"%s\", \"email\":\"%s\", \"firstName\":\"%s\", \"lastName\":\"%s\", \"roles\":[%s]}",
                    userId,
                    username != null ? username : "",
                    email != null ? email : "",
                    firstName != null ? firstName : "",
                    lastName != null ? lastName : "",
                    roles.stream().map(r -> "\"" + r + "\"").collect(Collectors.joining(","))
                );

                // Encoder en Base64 pour un transport sécurisé
                String userInfoHeader = Base64.getEncoder().encodeToString(userInfoJson.getBytes());

                // Ajouter les headers à la requête
                ServerHttpRequest requestWithHeaders = exchange.getRequest().mutate()
                    .header("X-User-Info", userInfoHeader)
                    .header("X-User-Id", userId)
                    .build();
                
                ServerWebExchange exchangeWithHeaders = exchange.mutate().request(requestWithHeaders).build();
                
                return chain.filter(exchangeWithHeaders);
            })
            .switchIfEmpty(chain.filter(exchange));
    }

    private List<String> extractRoles(Jwt jwt) {
        try {
            // Extraire les rôles du realm
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                return (List<String>) realmAccess.get("roles");
            }
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer la requête
        }
        return List.of();
    }

    @Override
    public int getOrder() {
        return -1; // Exécuter après l'authentification
    }
}
```

### 2.2 Mise à jour de la configuration de routage

Ajoutez cette route à votre configuration Gateway :

```yaml
spring:
  cloud:
    gateway:
      routes:
        # ... vos routes existantes ...
        
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=0
```

## Étape 3 : Intégration dans vos microservices existants

### 3.1 Ajout des classes utilitaires

Dans chaque microservice (equipment-service, notification-service, etc.), ajoutez ces classes :

#### UserInfo.java
```java
package com.sdsgpi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String department;
    private String position;
    private Boolean isActive;
    private String preferredLanguage;
    private List<String> roles;
    private List<String> permissions;

    public boolean hasRole(String roleName) {
        return roles != null && roles.contains(roleName);
    }

    public boolean hasPermission(String permissionName) {
        return permissions != null && permissions.contains(permissionName);
    }

    public boolean hasAnyRole(List<String> roleNames) {
        if (roles == null || roleNames == null) return false;
        return roleNames.stream().anyMatch(roles::contains);
    }

    public String getFullName() {
        if (fullName != null && !fullName.isEmpty()) {
            return fullName;
        }
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return username;
    }
}
```

#### UserContextHolder.java
```java
package com.sdsgpi.common.security;

import com.sdsgpi.common.dto.UserInfo;

public class UserContextHolder {
    private static final ThreadLocal<UserInfo> userContext = new ThreadLocal<>();

    public static void setUserInfo(UserInfo userInfo) {
        userContext.set(userInfo);
    }

    public static UserInfo getUserInfo() {
        return userContext.get();
    }

    public static String getCurrentUserId() {
        UserInfo userInfo = userContext.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    public static String getCurrentUsername() {
        UserInfo userInfo = userContext.get();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    public static boolean hasRole(String roleName) {
        UserInfo userInfo = userContext.get();
        return userInfo != null && userInfo.hasRole(roleName);
    }

    public static boolean hasPermission(String permissionName) {
        UserInfo userInfo = userContext.get();
        return userInfo != null && userInfo.hasPermission(permissionName);
    }

    public static void clear() {
        userContext.remove();
    }
}
```

#### UserInfoInterceptor.java
```java
package com.sdsgpi.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdsgpi.common.dto.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String userInfoHeader = request.getHeader("X-User-Info");
            
            if (userInfoHeader != null && !userInfoHeader.isEmpty()) {
                // Décoder le header Base64
                byte[] decodedBytes = Base64.getDecoder().decode(userInfoHeader);
                String userInfoJson = new String(decodedBytes);
                
                // Convertir en objet UserInfo
                UserInfo userInfo = objectMapper.readValue(userInfoJson, UserInfo.class);
                
                // Stocker dans le ThreadLocal
                UserContextHolder.setUserInfo(userInfo);
                
                log.debug("Informations utilisateur extraites: {}", userInfo.getUsername());
            }
        } catch (Exception e) {
            log.warn("Erreur lors de l'extraction des informations utilisateur: {}", e.getMessage());
            // Ne pas bloquer la requête
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Nettoyer le ThreadLocal pour éviter les fuites de mémoire
        UserContextHolder.clear();
    }
}
```

### 3.2 Configuration Web

Ajoutez cette configuration dans chaque microservice :

```java
package com.sdsgpi.equipmentservice.config;

import com.sdsgpi.common.security.UserInfoInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserInfoInterceptor userInfoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInfoInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**");
    }
}
```

### 3.3 Configuration Jackson

Ajoutez cette configuration pour la sérialisation JSON :

```java
package com.sdsgpi.equipmentservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
```

## Étape 4 : Utilisation dans vos contrôleurs

### 4.1 Exemple d'utilisation basique

```java
@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/my-equipments")
    public ResponseEntity<List<Equipment>> getMyEquipments() {
        UserInfo currentUser = UserContextHolder.getUserInfo();
        
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Equipment> equipments;
        
        if (currentUser.hasRole("ADMIN")) {
            // Les admins voient tous les équipements
            equipments = equipmentService.getAllEquipments();
        } else if (currentUser.hasRole("MANAGER")) {
            // Les managers voient les équipements de leur département
            equipments = equipmentService.getEquipmentsByDepartment(currentUser.getDepartment());
        } else {
            // Les utilisateurs normaux voient seulement leurs équipements
            equipments = equipmentService.getEquipmentsByUserId(currentUser.getUserId());
        }
        
        return ResponseEntity.ok(equipments);
    }

    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody CreateEquipmentRequest request) {
        UserInfo currentUser = UserContextHolder.getUserInfo();
        
        if (currentUser == null || !currentUser.hasPermission("WRITE_EQUIPMENT")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Associer l'équipement à l'utilisateur créateur
        Equipment equipment = equipmentService.createEquipment(request, currentUser.getUserId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(equipment);
    }
}
```

### 4.2 Utilisation avec les annotations de sécurité

```java
@RestController
@RequestMapping("/api/equipments")
@PreAuthorize("hasRole('USER')")
public class EquipmentController {

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @equipmentService.isOwner(#id, authentication.name)")
    public ResponseEntity<Equipment> getEquipment(@PathVariable Long id) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(equipment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasPermission('DELETE_EQUIPMENT')")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
}
```

## Étape 5 : Service de communication avec User Service

### 5.1 Client Feign pour User Service

Ajoutez cette dépendance à vos microservices :

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

Créez le client Feign :

```java
package com.sdsgpi.equipmentservice.client;

import com.sdsgpi.common.dto.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {

    @GetMapping("/me")
    UserInfo getCurrentUserInfo(@RequestHeader("X-User-Id") String userId);
    
    @GetMapping("/{keycloakId}")
    UserInfo getUserById(@PathVariable String keycloakId);
}
```

Activez Feign dans votre application :

```java
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class EquipmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EquipmentServiceApplication.class, args);
    }
}
```

### 5.2 Utilisation du client Feign

```java
@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final UserServiceClient userServiceClient;
    private final EquipmentRepository equipmentRepository;

    public List<Equipment> getEquipmentsWithUserInfo() {
        List<Equipment> equipments = equipmentRepository.findAll();
        
        // Enrichir avec les informations utilisateur
        return equipments.stream()
            .map(equipment -> {
                try {
                    UserInfo userInfo = userServiceClient.getUserById(equipment.getOwnerId());
                    equipment.setOwnerName(userInfo.getFullName());
                    equipment.setOwnerDepartment(userInfo.getDepartment());
                } catch (Exception e) {
                    log.warn("Impossible de récupérer les infos utilisateur pour {}", equipment.getOwnerId());
                }
                return equipment;
            })
            .collect(Collectors.toList());
    }
}
```

## Étape 6 : Tests d'intégration

### 6.1 Test de l'extraction des informations utilisateur

```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserInfoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldExtractUserInfoFromHeader() {
        // Simuler un header X-User-Info
        String userInfoJson = "{\"userId\":\"test-user\",\"username\":\"testuser\",\"roles\":[\"USER\"]}";
        String encodedUserInfo = Base64.getEncoder().encodeToString(userInfoJson.getBytes());
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Info", encodedUserInfo);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            "/api/equipments/my-equipments",
            HttpMethod.GET,
            entity,
            String.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
```

### 6.2 Test du contrôle d'accès

```java
@Test
void shouldDenyAccessWithoutPermission() {
    String userInfoJson = "{\"userId\":\"test-user\",\"username\":\"testuser\",\"roles\":[\"USER\"]}";
    String encodedUserInfo = Base64.getEncoder().encodeToString(userInfoJson.getBytes());
    
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-User-Info", encodedUserInfo);
    
    HttpEntity<String> entity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange(
        "/api/equipments/admin-only",
        HttpMethod.GET,
        entity,
        String.class
    );
    
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
}
```

## Étape 7 : Déploiement et vérification

### 7.1 Déploiement complet

```bash
# 1. Arrêter les services existants
docker-compose down

# 2. Construire et démarrer tous les services
docker-compose up --build

# 3. Vérifier que tous les services sont en cours d'exécution
docker-compose ps
```

### 7.2 Vérifications post-déploiement

```bash
# Vérifier la santé du user-service
curl http://localhost:9003/actuator/health

# Vérifier l'enregistrement dans Eureka
curl http://localhost:8761/eureka/apps/USER-SERVICE

# Tester l'API du user-service
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     http://localhost:8084/api/users/me

# Vérifier la documentation Swagger
open http://localhost:8084/swagger-ui.html
```

### 7.3 Test de bout en bout

```bash
# 1. Obtenir un token depuis Keycloak
TOKEN=$(curl -X POST "http://localhost:8181/realms/auth-service-realm/protocol/openid_connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=your-client-id" \
  -d "username=testuser" \
  -d "password=testpass" | jq -r '.access_token')

# 2. Tester l'accès via la Gateway
curl -H "Authorization: Bearer $TOKEN" \
     http://localhost:8080/api/users/me

# 3. Tester l'accès à un autre microservice
curl -H "Authorization: Bearer $TOKEN" \
     http://localhost:8080/api/equipments/my-equipments
```

## Dépannage

### Problèmes courants et solutions

#### 1. Header X-User-Info non transmis
**Symptôme** : `UserContextHolder.getUserInfo()` retourne `null`
**Solution** : Vérifiez que le `UserInfoFilter` est bien configuré dans la Gateway

#### 2. Erreur de désérialisation JSON
**Symptôme** : `JsonProcessingException` lors de la lecture du header
**Solution** : Vérifiez la configuration Jackson et la structure du JSON

#### 3. Permissions non reconnues
**Symptôme** : `@PreAuthorize` ne fonctionne pas
**Solution** : Vérifiez la configuration Spring Security et l'extraction des rôles

#### 4. Fuite de mémoire ThreadLocal
**Symptôme** : Consommation mémoire croissante
**Solution** : Vérifiez que `UserContextHolder.clear()` est appelé dans `afterCompletion`

### Logs de débogage

Ajoutez ces configurations de logging pour le débogage :

```properties
# Dans application.properties de chaque microservice
logging.level.com.sdsgpi.common.security=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web.servlet.DispatcherServlet=DEBUG
```

## Conclusion

Cette intégration vous permet de :

1. **Centraliser** la gestion des utilisateurs
2. **Sécuriser** l'accès aux ressources
3. **Enrichir** les informations utilisateur
4. **Simplifier** le développement des microservices

Le User Service devient le point central pour toutes les informations utilisateur, tout en maintenant la performance grâce au cache des informations dans les headers HTTP.

Pour toute question ou problème d'intégration, consultez les logs des services et la documentation Swagger du User Service.

