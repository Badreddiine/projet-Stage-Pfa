# Migration du Microservice de Notification vers PostgreSQL

## Modifications apportées

### 1. Dépendances Maven (pom.xml)
- **Supprimé** : Dépendances H2 et MySQL
- **Ajouté** : Dépendance PostgreSQL
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. Configuration de l'application (application.yml)
- **Profil par défaut** : Configuration PostgreSQL locale
- **Profil de production** : Configuration PostgreSQL avec variables d'environnement
- **Profil Docker** : Nouveau profil spécifique pour l'environnement Docker

### 3. Nouveaux fichiers créés

#### Dockerfile
- Image basée sur OpenJDK 17
- Installation de curl pour les health checks
- Construction Maven intégrée
- Exposition des ports 8080 et 9002

#### docker-compose.yml
- Service notification-service configuré pour PostgreSQL
- Variables d'environnement pour la configuration
- Health checks configurés
- Réseau externe sdsgpi_net

#### postgres-init/init-notification-db.sql
- Script d'initialisation de la base de données
- Création de la base `notification_db`
- Création de l'utilisateur `notification_user`
- Attribution des privilèges nécessaires

#### application-docker.yml
- Configuration spécifique pour l'environnement Docker
- Variables d'environnement externalisées
- Configuration Eureka et MQTT adaptée

## Configuration de la base de données

### Informations de connexion
- **Host** : postgres_db (nom du service Docker)
- **Port** : 5432
- **Base de données** : notification_db
- **Utilisateur** : notification_user
- **Mot de passe** : notification_password

### Variables d'environnement Docker
```yaml
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres_db:5432/notification_db
SPRING_DATASOURCE_USERNAME=notification_user
SPRING_DATASOURCE_PASSWORD=notification_password
```

## Intégration avec le docker-compose principal

Pour intégrer ce service dans votre docker-compose principal, ajoutez la section suivante :

```yaml
notification-service:
  build:
    context: ./services/notification-microservice
  container_name: notification_service_container
  hostname: notification-service
  ports:
    - "8083:8080"
    - "9002:9002"
  depends_on:
    discovery-service:
      condition: service_healthy
    config-server:
      condition: service_healthy
    postgres_db:
      condition: service_healthy
  networks:
    - sdsgpi_net
  restart: unless-stopped
  environment:
    - SPRING_APPLICATION_NAME=notification-service
    - SERVER_PORT=8080
    - MANAGEMENT_SERVER_PORT=9002
    - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres_db:5432/notification_db
    - SPRING_DATASOURCE_USERNAME=notification_user
    - SPRING_DATASOURCE_PASSWORD=notification_password
    - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    - SPRING_JPA_SHOW_SQL=false
    - MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,metrics,prometheus
    - MANAGEMENT_ENDPOINT_HEALTH_SHOW_DETAILS=always
    - MANAGEMENT_ENDPOINTS_WEB_BASE_PATH=/actuator
    - SPRING_PROFILES_ACTIVE=docker
    - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://admin:admin123@discovery-service:8761/eureka/
    - EUREKA_CLIENT_REGISTER_WITH_EUREKA=true
    - EUREKA_CLIENT_FETCH_REGISTRY=true
    - EUREKA_CLIENT_ENABLED=true
    - EUREKA_INSTANCE_PREFER_IP_ADDRESS=false
    - EUREKA_INSTANCE_HOSTNAME=notification-service
    - EUREKA_INSTANCE_INSTANCE_ID=notification-service:8080
    - EUREKA_INSTANCE_LEASE_RENEWAL_INTERVAL_IN_SECONDS=30
    - EUREKA_INSTANCE_LEASE_EXPIRATION_DURATION_IN_SECONDS=90
    - EUREKA_INSTANCE_HEALTH_CHECK_URL_PATH=/actuator/health
    - EUREKA_INSTANCE_STATUS_PAGE_URL_PATH=/actuator/info
  healthcheck:
    test: ["CMD", "curl", "-f", "http://localhost:8080/notification-service/actuator/health"]
    interval: 15s
    timeout: 10s
    start_period: 120s
    retries: 10
```

## Mise à jour du script d'initialisation PostgreSQL

Ajoutez le contenu du fichier `postgres-init/init-notification-db.sql` à votre script d'initialisation PostgreSQL existant ou copiez le fichier dans le répertoire `./postgres-init/` de votre docker-compose principal.

## Commandes de déploiement

```bash
# Construction et démarrage
docker-compose up --build -d

# Vérification des logs
docker-compose logs -f notification-service

# Vérification de la santé du service
curl http://localhost:8083/notification-service/actuator/health
```

## Points d'attention

1. **Port** : Le service utilise le port 8083 pour éviter les conflits
2. **Health Check** : Configuré avec un délai de démarrage de 120 secondes
3. **Logs** : Stockés dans `/var/log/notification-service/application.log`
4. **Profils** : Utilise le profil `docker` par défaut dans l'environnement conteneurisé
5. **Base de données** : Les tables seront créées automatiquement avec `ddl-auto: update`

## Vérification de la migration

Après le démarrage, vérifiez que :
1. Le service se connecte correctement à PostgreSQL
2. Les tables sont créées dans la base `notification_db`
3. Le service s'enregistre correctement auprès d'Eureka
4. Les endpoints Actuator sont accessibles

