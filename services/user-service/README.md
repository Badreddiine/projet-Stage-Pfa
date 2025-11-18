# User Service - Service de Gestion des Utilisateurs

## Vue d'ensemble

Le **User Service** est un microservice Spring Boot conçu pour gérer les utilisateurs dans une architecture microservices sécurisée avec Keycloak. Il fournit une couche d'abstraction pour stocker et gérer les informations utilisateur étendues, tout en s'intégrant parfaitement avec votre écosystème existant.

## Fonctionnalités principales

### 🔐 Intégration Keycloak
- Synchronisation automatique avec Keycloak
- Extraction des informations utilisateur depuis les tokens JWT
- Support des rôles et permissions Keycloak

### 👥 Gestion des utilisateurs
- CRUD complet des utilisateurs
- Profils utilisateur étendus (département, poste, téléphone, etc.)
- Recherche et filtrage des utilisateurs
- Activation/désactivation des comptes

### 🛡️ Système de permissions
- Gestion granulaire des permissions
- Rôles métier personnalisables
- Contrôle d'accès basé sur les rôles (RBAC)
- Permissions héritées des rôles

### 🔄 Synchronisation
- Synchronisation en temps réel avec Keycloak
- Création automatique des utilisateurs manquants
- Mise à jour des informations lors de la connexion

## Architecture

### Entités principales

```
User (Utilisateur)
├── UserRole (Rôles utilisateur)
├── UserPermission (Permissions directes)
└── Informations étendues (département, poste, etc.)

Role (Rôle)
├── RolePermission (Permissions du rôle)
└── UserRole (Utilisateurs ayant ce rôle)

Permission (Permission)
├── UserPermission (Permissions directes)
└── RolePermission (Permissions via rôles)
```

### Flux de données

1. **Authentification** : L'utilisateur s'authentifie via Keycloak
2. **Token JWT** : Keycloak émet un token JWT avec les informations de base
3. **Gateway** : L'API Gateway extrait les informations et les transmet via header
4. **User Service** : Enrichit les informations avec les données métier
5. **Microservices** : Reçoivent les informations complètes de l'utilisateur

## Installation et Configuration

### Prérequis

- Java 17+
- PostgreSQL 15+
- Docker et Docker Compose
- Keycloak configuré
- Services existants (Discovery, Config Server)

### Configuration de la base de données

1. **Ajoutez le script d'initialisation** à votre dossier `postgres-init` :

```sql
-- Contenu du fichier postgres-init-user-db.sql
CREATE DATABASE user_db;
CREATE USER user_user WITH ENCRYPTED PASSWORD 'user_password';
GRANT ALL PRIVILEGES ON DATABASE user_db TO user_user;
```

2. **Mise à jour du docker-compose principal** :

Ajoutez le service user-service à votre fichier `docker-compose.yml` existant :

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
      # Configuration complète dans docker-compose-user-service.yml
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_APPLICATION_NAME=user-service
      - SERVER_PORT=8084
      # ... autres variables d'environnement
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:9003/actuator/health"]
      interval: 15s
      timeout: 10s
      start_period: 120s
      retries: 10
    restart: unless-stopped
```

### Déploiement

1. **Clonez le service** dans votre structure de projet :
```bash
# Placez le dossier user-service dans services/
cp -r user-service /path/to/your/project/services/
```

2. **Construisez et démarrez** :
```bash
docker-compose up --build user-service
```

3. **Vérifiez le déploiement** :
```bash
# Vérifier la santé du service
curl http://localhost:9003/actuator/health

# Vérifier l'enregistrement Eureka
curl http://localhost:8761/eureka/apps/USER-SERVICE
```

## Utilisation

### API Endpoints

#### Informations utilisateur courantes
```http
GET /api/users/me
Headers: X-User-Id: {keycloak-user-id}
```

#### Gestion des utilisateurs (Admin)
```http
# Lister tous les utilisateurs
GET /api/users

# Récupérer un utilisateur spécifique
GET /api/users/{keycloakId}

# Créer un utilisateur
POST /api/users
Content-Type: application/json
{
  "keycloakId": "user-uuid",
  "username": "john.doe",
  "email": "john.doe@company.com",
  "firstName": "John",
  "lastName": "Doe",
  "department": "IT",
  "position": "Développeur"
}

# Mettre à jour un utilisateur
PUT /api/users/{keycloakId}
Content-Type: application/json
{
  "department": "DevOps",
  "position": "Ingénieur DevOps"
}

# Rechercher des utilisateurs
GET /api/users/search?searchTerm=john

# Désactiver un utilisateur
DELETE /api/users/{keycloakId}
```

#### Synchronisation Keycloak
```http
POST /api/users/sync
Content-Type: application/json
{
  "keycloakId": "user-uuid",
  "username": "john.doe",
  "email": "john.doe@company.com",
  "firstName": "John",
  "lastName": "Doe",
  "roles": ["USER", "MANAGER"]
}
```

### Intégration avec les autres microservices

#### 1. Configuration de la Gateway

Ajoutez ce filtre global à votre API Gateway :

```java
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
                List<String> roles = jwt.getClaimAsStringList("realm_access.roles");

                // Construire l'objet JSON
                String userInfoJson = String.format(
                    "{\"userId\":\"%s\", \"username\":\"%s\", \"email\":\"%s\", \"roles\":[%s]}",
                    userId, username, email,
                    roles.stream().map(r -> "\"" + r + "\"").collect(Collectors.joining(","))
                );

                // Encoder en Base64
                String userInfoHeader = Base64.getEncoder().encodeToString(userInfoJson.getBytes());

                // Ajouter le header
                ServerHttpRequest requestWithHeader = exchange.getRequest().mutate()
                    .header("X-User-Info", userInfoHeader)
                    .build();
                
                ServerWebExchange exchangeWithHeader = exchange.mutate().request(requestWithHeader).build();
                
                return chain.filter(exchangeWithHeader);
            })
            .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
```

#### 2. Configuration des microservices

Dans chaque microservice, ajoutez ces classes :

**UserInfo.java** :
```java
public class UserInfo {
    private String userId;
    private String username;
    private String email;
    private String fullName;
    private String department;
    private String position;
    private List<String> roles;
    private List<String> permissions;
    
    // Getters, setters, méthodes utilitaires
    public boolean hasRole(String roleName) {
        return roles != null && roles.contains(roleName);
    }
    
    public boolean hasPermission(String permissionName) {
        return permissions != null && permissions.contains(permissionName);
    }
}
```

**UserContextHolder.java** :
```java
public class UserContextHolder {
    private static final ThreadLocal<UserInfo> userContext = new ThreadLocal<>();

    public static void setUserInfo(UserInfo userInfo) {
        userContext.set(userInfo);
    }

    public static UserInfo getUserInfo() {
        return userContext.get();
    }

    public static void clear() {
        userContext.remove();
    }
}
```

**UserInfoInterceptor.java** :
```java
@Component
public class UserInfoInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userInfoHeader = request.getHeader("X-User-Info");

        if (userInfoHeader != null && !userInfoHeader.isEmpty()) {
            byte[] decodedBytes = Base64.getDecoder().decode(userInfoHeader);
            String userInfoJson = new String(decodedBytes);
            UserInfo userInfo = objectMapper.readValue(userInfoJson, UserInfo.class);
            UserContextHolder.setUserInfo(userInfo);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContextHolder.clear();
    }
}
```

#### 3. Utilisation dans vos contrôleurs

```java
@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {

    @GetMapping("/my-equipments")
    public List<Equipment> getMyEquipments() {
        UserInfo currentUser = UserContextHolder.getUserInfo();
        
        if (currentUser != null) {
            // Filtrer les équipements selon l'utilisateur
            if (currentUser.hasRole("ADMIN")) {
                return equipmentService.getAllEquipments();
            } else {
                return equipmentService.getEquipmentsByUser(currentUser.getUserId());
            }
        }
        
        return Collections.emptyList();
    }
}
```

## Sécurité

### Authentification
- Basée sur les tokens JWT de Keycloak
- Validation automatique des tokens
- Extraction des rôles et permissions

### Autorisation
- Contrôle d'accès basé sur les rôles (RBAC)
- Permissions granulaires par ressource et action
- Annotations Spring Security (`@PreAuthorize`)

### Endpoints protégés
```java
@PreAuthorize("hasRole('ADMIN') or hasPermission('USER', 'READ')")
@GetMapping("/{keycloakId}")
public ResponseEntity<UserDto> getUserById(@PathVariable String keycloakId) {
    // ...
}
```

## Monitoring et Observabilité

### Endpoints Actuator
- **Health** : `http://localhost:9003/actuator/health`
- **Info** : `http://localhost:9003/actuator/info`
- **Metrics** : `http://localhost:9003/actuator/metrics`
- **Prometheus** : `http://localhost:9003/actuator/prometheus`

### Logs
- Logs structurés avec Logback
- Niveaux configurables par package
- Corrélation des requêtes

### Métriques
- Métriques Spring Boot Actuator
- Métriques personnalisées pour les opérations métier
- Export Prometheus pour Grafana

## Documentation API

### Swagger UI
Accédez à la documentation interactive à l'adresse :
`http://localhost:8084/swagger-ui.html`

### OpenAPI 3.0
Spécification disponible à :
`http://localhost:8084/v3/api-docs`

## Développement

### Structure du projet
```
user-service/
├── src/main/java/com/sdsgpi/userservice/
│   ├── config/          # Configurations Spring
│   ├── controller/      # Contrôleurs REST
│   ├── dto/            # Objets de transfert de données
│   ├── entity/         # Entités JPA
│   ├── exception/      # Exceptions personnalisées
│   ├── repository/     # Repositories JPA
│   ├── security/       # Configuration sécurité
│   └── service/        # Services métier
├── src/main/resources/
│   └── application.properties
├── src/test/           # Tests unitaires et d'intégration
├── Dockerfile
├── docker-compose-user-service.yml
└── README.md
```

### Tests
```bash
# Tests unitaires
./mvnw test

# Tests d'intégration
./mvnw integration-test

# Couverture de code
./mvnw jacoco:report
```

### Build local
```bash
# Compilation
./mvnw clean compile

# Package
./mvnw clean package

# Exécution locale
./mvnw spring-boot:run
```

## Dépannage

### Problèmes courants

#### 1. Erreur de connexion à la base de données
```
Caused by: org.postgresql.util.PSQLException: Connection refused
```
**Solution** : Vérifiez que PostgreSQL est démarré et que la base `user_db` existe.

#### 2. Erreur d'authentification Keycloak
```
Invalid token signature
```
**Solution** : Vérifiez la configuration `spring.security.oauth2.resourceserver.jwt.issuer-uri`.

#### 3. Service non enregistré dans Eureka
```
Cannot execute request on any known server
```
**Solution** : Vérifiez la configuration Eureka et que le Discovery Service est accessible.

### Logs utiles
```bash
# Logs du service
docker logs user_service_container

# Logs de la base de données
docker logs postgres_db_container

# Logs Eureka
docker logs discovery_service_container
```

## Contribution

### Standards de code
- Java 17+ avec Spring Boot 3.2.5
- Lombok pour réduire le boilerplate
- Validation JSR-303
- Documentation Javadoc

### Workflow Git
1. Fork du repository
2. Branche feature (`git checkout -b feature/nouvelle-fonctionnalite`)
3. Commit (`git commit -am 'Ajout nouvelle fonctionnalité'`)
4. Push (`git push origin feature/nouvelle-fonctionnalite`)
5. Pull Request

## Support

Pour toute question ou problème :
1. Consultez cette documentation
2. Vérifiez les logs du service
3. Consultez la documentation Swagger
4. Contactez l'équipe de développement

---

**Version** : 1.0.0  
**Dernière mise à jour** : Janvier 2025  
**Auteur** : Équipe SDSGPI

