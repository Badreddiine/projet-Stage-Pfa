# Intégration du Microservice de Notification avec une API Gateway

## Table des Matières

1. [Introduction](#introduction)
2. [Architecture du Microservice](#architecture-du-microservice)
3. [Configuration de l'API Gateway](#configuration-de-lapi-gateway)
4. [Routage et Load Balancing](#routage-et-load-balancing)
5. [Sécurité et Authentification](#sécurité-et-authentification)
6. [Monitoring et Observabilité](#monitoring-et-observabilité)
7. [Gestion des Erreurs](#gestion-des-erreurs)
8. [Déploiement et Mise en Production](#déploiement-et-mise-en-production)

## Introduction

Ce document détaille l'intégration du microservice de notification développé en Java 17 avec Spring Boot dans une architecture de microservices utilisant une API Gateway. L'API Gateway sert de point d'entrée unique pour tous les clients, gérant le routage, la sécurité, le monitoring et la résilience des communications avec les microservices back-end.

Le microservice de notification implémente un système complet de gestion des notifications multi-canaux (Email, SMS, Push, MQTT) avec support des préférences utilisateur, des sessions MQTT et d'un système de file d'attente pour le traitement asynchrone des messages.

## Architecture du Microservice

### Vue d'ensemble

Le microservice de notification suit une architecture en couches avec les composants suivants :

- **Couche de Présentation** : Contrôleurs REST exposant les APIs
- **Couche Métier** : Services gérant la logique applicative
- **Couche de Persistance** : Repositories JPA pour l'accès aux données
- **Couche d'Intégration** : Services MQTT et de gestion des files de messages

### Entités Principales

Le système est basé sur cinq entités principales correspondant au diagramme UML fourni :

1. **PreferenceUtilisateur** : Gère les préférences de notification par utilisateur
2. **CanalNotification** : Définit les canaux de communication disponibles
3. **SessionMQTT** : Gère les sessions de communication MQTT
4. **FileMessage** : Système de file d'attente pour le traitement asynchrone
5. **Notification** : Entité centrale représentant une notification

### Points d'Exposition API

Le microservice expose plusieurs endpoints REST :

- `POST /api/notifications` : Création d'une nouvelle notification
- `GET /api/notifications/utilisateur/{id}` : Récupération des notifications d'un utilisateur
- `GET /api/notifications/utilisateur/{id}/non-lues` : Notifications non lues
- `PUT /api/notifications/{id}/marquer-lue` : Marquer une notification comme lue
- `POST /api/mqtt/sessions` : Création d'une session MQTT
- `GET /api/mqtt/sessions/actives` : Sessions MQTT actives




## Configuration de l'API Gateway

### Spring Cloud Gateway

Pour intégrer le microservice de notification avec Spring Cloud Gateway, plusieurs configurations sont nécessaires. Spring Cloud Gateway offre un routage dynamique, des filtres personnalisés et une intégration native avec les outils de découverte de services comme Eureka.

#### Configuration du Gateway (application.yml)

```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        - id: notification-service
          uri: lb://notification-service
          predicates:
            - Path=/api/notifications/**
          filters:
            - StripPrefix=0
            - AddRequestHeader=X-Gateway-Source, api-gateway
            - AddResponseHeader=X-Gateway-Response, notification-service
        - id: notification-mqtt
          uri: lb://notification-service
          predicates:
            - Path=/api/mqtt/**
          filters:
            - StripPrefix=0
            - AddRequestHeader=X-MQTT-Gateway, true
      default-filters:
        - DedupeResponseHeader=Access-Control-Allow-Credentials Access-Control-Allow-Origin
        - AddResponseHeader=X-Response-Time, #{T(System).currentTimeMillis()}
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
              - OPTIONS
            allowedHeaders: "*"
            allowCredentials: false

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
```

#### Configuration Avancée avec Filtres Personnalisés

Pour une intégration plus poussée, il est possible de créer des filtres personnalisés pour gérer l'authentification, la limitation de débit et la transformation des requêtes :

```java
@Component
public class NotificationGatewayFilterFactory extends AbstractGatewayFilterFactory<NotificationGatewayFilterFactory.Config> {
    
    public NotificationGatewayFilterFactory() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Ajouter des headers spécifiques pour le service de notification
            ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-Service-Type", "notification")
                .header("X-Request-ID", UUID.randomUUID().toString())
                .build();
            
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }
    
    public static class Config {
        // Configuration du filtre
    }
}
```

### Netflix Zuul (Alternative)

Pour les architectures utilisant Netflix Zuul, la configuration serait la suivante :

```yaml
zuul:
  routes:
    notification-service:
      path: /api/notifications/**
      serviceId: notification-service
      stripPrefix: false
    notification-mqtt:
      path: /api/mqtt/**
      serviceId: notification-service
      stripPrefix: false
  host:
    connect-timeout-millis: 10000
    socket-timeout-millis: 60000
  ribbon:
    eager-load:
      enabled: true
```

### Kong API Gateway

Kong peut également être utilisé comme API Gateway avec la configuration suivante :

```bash
# Création du service
curl -i -X POST http://localhost:8001/services/ \
  --data "name=notification-service" \
  --data "url=http://notification-service:8080"

# Création de la route
curl -i -X POST http://localhost:8001/services/notification-service/routes \
  --data "paths[]=/api/notifications" \
  --data "paths[]=/api/mqtt"

# Ajout du plugin de limitation de débit
curl -i -X POST http://localhost:8001/services/notification-service/plugins \
  --data "name=rate-limiting" \
  --data "config.minute=100" \
  --data "config.hour=1000"
```

## Routage et Load Balancing

### Découverte de Services avec Eureka

Le microservice de notification s'enregistre automatiquement auprès d'Eureka Server grâce à la configuration suivante dans `application.yml` :

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 30
    lease-expiration-duration-in-seconds: 90
    metadata-map:
      version: 1.0.0
      description: "Service de notification multi-canaux"
      health-check-url: http://localhost:8080/notification-service/actuator/health
```

### Stratégies de Load Balancing

L'API Gateway peut implémenter différentes stratégies de répartition de charge :

#### Round Robin (Par défaut)
```yaml
notification-service:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RoundRobinRule
```

#### Weighted Response Time
```yaml
notification-service:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.WeightedResponseTimeRule
```

#### Availability Filtering
```yaml
notification-service:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.AvailabilityFilteringRule
```

### Configuration des Health Checks

Pour assurer une haute disponibilité, des health checks sont configurés :

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
      probes:
        enabled: true
  health:
    livenessstate:
      enabled: true
    readinessstate:
      enabled: true
```

Le service expose les endpoints suivants pour le monitoring :
- `/actuator/health` : État général du service
- `/actuator/health/liveness` : Vérification de la vivacité
- `/actuator/health/readiness` : Vérification de la disponibilité
- `/actuator/metrics` : Métriques de performance


## Sécurité et Authentification

### JWT Token Validation

L'API Gateway peut valider les tokens JWT avant de router les requêtes vers le microservice de notification :

```java
@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    
    private final JwtTokenProvider tokenProvider;
    
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = extractToken(exchange.getRequest());
        
        if (token != null && tokenProvider.validateToken(token)) {
            String userId = tokenProvider.getUserIdFromToken(token);
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header("X-User-ID", userId)
                .header("X-Authenticated", "true")
                .build();
            
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }
        
        return unauthorized(exchange);
    }
    
    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
```

### OAuth2 Integration

Pour une intégration OAuth2 complète :

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://auth-server.example.com
          jwk-set-uri: https://auth-server.example.com/.well-known/jwks.json
  cloud:
    gateway:
      routes:
        - id: notification-service-secured
          uri: lb://notification-service
          predicates:
            - Path=/api/notifications/**
          filters:
            - TokenRelay=
            - RemoveRequestHeader=Cookie
```

### Rate Limiting et Throttling

Configuration de la limitation de débit par utilisateur :

```java
@Bean
public RedisRateLimiter redisRateLimiter() {
    return new RedisRateLimiter(10, 20, 1);
}

@Bean
public KeyResolver userKeyResolver() {
    return exchange -> exchange.getRequest().getHeaders()
        .getFirst("X-User-ID") != null ? 
        Mono.just(exchange.getRequest().getHeaders().getFirst("X-User-ID")) :
        Mono.just("anonymous");
}
```

Configuration dans `application.yml` :

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: notification-service-rate-limited
          uri: lb://notification-service
          predicates:
            - Path=/api/notifications/**
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
                redis-rate-limiter.requestedTokens: 1
                key-resolver: "#{@userKeyResolver}"
```

## Monitoring et Observabilité

### Métriques et Monitoring

Le microservice expose des métriques Prometheus via Actuator :

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      service: notification-service
      version: 1.0.0
```

### Distributed Tracing avec Zipkin

Configuration pour le tracing distribué :

```yaml
spring:
  zipkin:
    base-url: http://zipkin-server:9411
  sleuth:
    sampler:
      probability: 1.0
    zipkin:
      enabled: true
```

Ajout de la dépendance dans `pom.xml` :

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

### Logging Centralisé

Configuration pour l'envoi des logs vers un système centralisé :

```yaml
logging:
  level:
    com.notification: DEBUG
    org.springframework.web: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%X{traceId:-},%X{spanId:-}] %-5level %logger{36} - %msg%n"
  file:
    name: logs/notification-service.log
```

### Custom Metrics

Ajout de métriques personnalisées dans les services :

```java
@Service
public class NotificationService {
    
    private final MeterRegistry meterRegistry;
    private final Counter notificationCreatedCounter;
    private final Timer notificationProcessingTimer;
    
    public NotificationService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.notificationCreatedCounter = Counter.builder("notifications.created")
            .description("Number of notifications created")
            .register(meterRegistry);
        this.notificationProcessingTimer = Timer.builder("notifications.processing.time")
            .description("Time taken to process notifications")
            .register(meterRegistry);
    }
    
    public Notification creerNotification(Notification notification) {
        return Timer.Sample.start(meterRegistry)
            .stop(notificationProcessingTimer, () -> {
                notificationCreatedCounter.increment();
                // Logique de création
                return processNotification(notification);
            });
    }
}
```

## Gestion des Erreurs

### Circuit Breaker Pattern

Implémentation du pattern Circuit Breaker avec Resilience4j :

```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot2</artifactId>
</dependency>
```

Configuration :

```yaml
resilience4j:
  circuitbreaker:
    instances:
      notification-service:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 5s
        failureRateThreshold: 50
        eventConsumerBufferSize: 10
```

Utilisation dans le service :

```java
@Component
public class NotificationServiceClient {
    
    @CircuitBreaker(name = "notification-service", fallbackMethod = "fallbackNotification")
    public Notification createNotification(Notification notification) {
        // Appel au service de notification
        return restTemplate.postForObject("/api/notifications", notification, Notification.class);
    }
    
    public Notification fallbackNotification(Notification notification, Exception ex) {
        // Méthode de fallback en cas d'échec
        return new Notification("Service temporairement indisponible");
    }
}
```

### Retry Pattern

Configuration des tentatives de retry :

```yaml
resilience4j:
  retry:
    instances:
      notification-service:
        maxAttempts: 3
        waitDuration: 1s
        enableExponentialBackoff: true
        exponentialBackoffMultiplier: 2
```

### Global Exception Handler

Gestionnaire global d'exceptions dans l'API Gateway :

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponse> handleConnectionException(ConnectException ex) {
        ErrorResponse error = new ErrorResponse(
            "SERVICE_UNAVAILABLE",
            "Le service de notification est temporairement indisponible",
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
    
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeoutException(TimeoutException ex) {
        ErrorResponse error = new ErrorResponse(
            "REQUEST_TIMEOUT",
            "La requête a expiré. Veuillez réessayer.",
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(error);
    }
}
```

### Validation des Données

Validation côté Gateway avant routage :

```java
@Component
public class RequestValidationFilter implements GatewayFilter {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        if (HttpMethod.POST.equals(request.getMethod()) && 
            request.getPath().value().contains("/notifications")) {
            
            return exchange.getRequest().getBody()
                .collectList()
                .flatMap(dataBuffers -> {
                    String body = dataBuffers.stream()
                        .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            return new String(bytes, StandardCharsets.UTF_8);
                        })
                        .collect(Collectors.joining());
                    
                    if (isValidNotificationRequest(body)) {
                        return chain.filter(exchange);
                    } else {
                        return badRequest(exchange);
                    }
                });
        }
        
        return chain.filter(exchange);
    }
    
    private boolean isValidNotificationRequest(String body) {
        // Logique de validation
        return body.contains("titre") && body.contains("message");
    }
}
```


## Déploiement et Mise en Production

### Containerisation avec Docker

#### Dockerfile pour le Microservice

```dockerfile
FROM openjdk:17-jdk-slim

LABEL maintainer="notification-team@example.com"
LABEL version="1.0.0"
LABEL description="Microservice de notification multi-canaux"

# Création de l'utilisateur non-root
RUN addgroup --system notification && adduser --system --group notification

# Répertoire de travail
WORKDIR /app

# Copie du JAR
COPY target/notification-microservice-1.0.0.jar app.jar

# Changement de propriétaire
RUN chown -R notification:notification /app

# Basculement vers l'utilisateur non-root
USER notification

# Variables d'environnement
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=prod

# Port d'exposition
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/notification-service/actuator/health || exit 1

# Point d'entrée
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

#### Docker Compose pour l'Environnement Complet

```yaml
version: '3.8'

services:
  # Service de découverte Eureka
  eureka-server:
    image: steeltoeoss/eureka-server:latest
    container_name: eureka-server
    ports:
      - "8761:8761"
    environment:
      - EUREKA_CLIENT_REGISTER_WITH_EUREKA=false
      - EUREKA_CLIENT_FETCH_REGISTRY=false
    networks:
      - microservices-network

  # Base de données MySQL
  mysql-db:
    image: mysql:8.0
    container_name: notification-mysql
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: notification_db
      MYSQL_USER: notification_user
      MYSQL_PASSWORD: notification_password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - microservices-network

  # Broker MQTT
  mosquitto:
    image: eclipse-mosquitto:latest
    container_name: mqtt-broker
    ports:
      - "1883:1883"
      - "9001:9001"
    volumes:
      - ./config/mosquitto.conf:/mosquitto/config/mosquitto.conf
    networks:
      - microservices-network

  # Redis pour le cache et rate limiting
  redis:
    image: redis:7-alpine
    container_name: redis-cache
    ports:
      - "6379:6379"
    networks:
      - microservices-network

  # Microservice de notification
  notification-service:
    build: .
    container_name: notification-service
    depends_on:
      - eureka-server
      - mysql-db
      - mosquitto
      - redis
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - EUREKA_URL=http://eureka-server:8761/eureka/
      - DB_HOST=mysql-db
      - DB_PORT=3306
      - DB_NAME=notification_db
      - DB_USERNAME=notification_user
      - DB_PASSWORD=notification_password
      - MQTT_BROKER_URL=tcp://mosquitto:1883
      - REDIS_HOST=redis
      - REDIS_PORT=6379
    ports:
      - "8081:8080"
    networks:
      - microservices-network
    restart: unless-stopped

  # API Gateway
  api-gateway:
    image: notification/api-gateway:latest
    container_name: api-gateway
    depends_on:
      - eureka-server
      - notification-service
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - EUREKA_URL=http://eureka-server:8761/eureka/
    ports:
      - "8080:8080"
    networks:
      - microservices-network
    restart: unless-stopped

volumes:
  mysql_data:

networks:
  microservices-network:
    driver: bridge
```

### Déploiement Kubernetes

#### Deployment YAML

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: notification-service
  labels:
    app: notification-service
    version: v1
spec:
  replicas: 3
  selector:
    matchLabels:
      app: notification-service
  template:
    metadata:
      labels:
        app: notification-service
        version: v1
    spec:
      containers:
      - name: notification-service
        image: notification/notification-service:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: DB_HOST
          value: "mysql-service"
        - name: MQTT_BROKER_URL
          value: "tcp://mqtt-service:1883"
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /notification-service/actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /notification-service/actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
---
apiVersion: v1
kind: Service
metadata:
  name: notification-service
spec:
  selector:
    app: notification-service
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: ClusterIP
```

#### ConfigMap pour la Configuration

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: notification-config
data:
  application.yml: |
    server:
      port: 8080
    spring:
      application:
        name: notification-service
      datasource:
        url: jdbc:mysql://mysql-service:3306/notification_db
        username: notification_user
        password: notification_password
    mqtt:
      broker:
        url: tcp://mqtt-service:1883
    eureka:
      client:
        service-url:
          defaultZone: http://eureka-service:8761/eureka/
```

### Pipeline CI/CD avec Jenkins

```groovy
pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'your-registry.com'
        IMAGE_NAME = 'notification-service'
        KUBECONFIG = credentials('kubeconfig')
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-org/notification-service.git'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
            post {
                always {
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Docker Build') {
            steps {
                script {
                    def image = docker.build("${DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}")
                    docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-registry-credentials') {
                        image.push()
                        image.push('latest')
                    }
                }
            }
        }
        
        stage('Deploy to Staging') {
            steps {
                sh """
                    kubectl set image deployment/notification-service \
                    notification-service=${DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} \
                    --namespace=staging
                """
            }
        }
        
        stage('Integration Tests') {
            steps {
                sh 'mvn verify -Dspring.profiles.active=integration'
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                input message: 'Deploy to production?', ok: 'Deploy'
                sh """
                    kubectl set image deployment/notification-service \
                    notification-service=${DOCKER_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} \
                    --namespace=production
                """
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        failure {
            emailext (
                subject: "Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                body: "Build failed. Check console output at ${env.BUILD_URL}",
                to: "dev-team@example.com"
            )
        }
    }
}
```

## Exemples d'Utilisation

### Création d'une Notification via l'API Gateway

```bash
# Création d'une notification simple
curl -X POST http://api-gateway:8080/api/notifications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-jwt-token" \
  -d '{
    "titre": "Bienvenue",
    "message": "Bienvenue sur notre plateforme",
    "type": "INFO",
    "canal": "EMAIL",
    "destinataireId": "1001",
    "typeDestinataire": "USER",
    "priorite": "NORMALE",
    "metadonnees": {
      "template": "welcome_template",
      "language": "fr"
    }
  }'
```

### Récupération des Notifications Non Lues

```bash
curl -X GET http://api-gateway:8080/api/notifications/utilisateur/1001/non-lues \
  -H "Authorization: Bearer your-jwt-token"
```

### Gestion des Sessions MQTT

```bash
# Création d'une session MQTT
curl -X POST http://api-gateway:8080/api/mqtt/sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-jwt-token" \
  -d '{
    "utilisateurId": "1001",
    "roleUtilisateur": "USER"
  }'

# Souscription à un topic
curl -X POST http://api-gateway:8080/api/mqtt/topics/alerts%2Fuser%2F1001/subscribe \
  -H "Authorization: Bearer your-jwt-token"
```

### Monitoring et Métriques

```bash
# Vérification de la santé du service
curl http://api-gateway:8080/api/notifications/actuator/health

# Récupération des métriques Prometheus
curl http://api-gateway:8080/api/notifications/actuator/prometheus
```

## Conclusion

L'intégration du microservice de notification avec une API Gateway offre une architecture robuste et scalable pour la gestion des notifications multi-canaux. Cette approche permet de centraliser la gestion de la sécurité, du routage et du monitoring tout en maintenant la flexibilité et l'indépendance des microservices.

Les points clés de cette intégration incluent :

- **Routage intelligent** avec découverte de services automatique
- **Sécurité centralisée** avec validation JWT et OAuth2
- **Résilience** grâce aux patterns Circuit Breaker et Retry
- **Observabilité complète** avec métriques, tracing et logging
- **Scalabilité horizontale** avec load balancing automatique
- **Déploiement simplifié** avec containerisation et orchestration Kubernetes

Cette architecture permet de répondre aux exigences de performance, de fiabilité et de maintenabilité des systèmes de notification modernes tout en offrant une expérience développeur optimale.

---

*Document rédigé par Manus AI - Version 1.0 - Date de création : $(date)*

