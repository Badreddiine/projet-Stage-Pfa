# Microservice de Notification

## Description

Ce microservice de notification développé en Java 17 avec Spring Boot offre une solution complète pour la gestion des notifications multi-canaux (Email, SMS, Push, MQTT). Il implémente un système de préférences utilisateur, de sessions MQTT et de traitement asynchrone des messages via un système de file d'attente.

## Architecture

Le système est basé sur le diagramme UML fourni et comprend les entités suivantes :

- **PreferenceUtilisateur** : Gestion des préférences de notification par utilisateur
- **CanalNotification** : Configuration des canaux de communication
- **SessionMQTT** : Gestion des sessions de communication MQTT
- **FileMessage** : Système de file d'attente pour le traitement asynchrone
- **Notification** : Entité centrale représentant une notification

## Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- MySQL 8.0+ (optionnel, H2 par défaut)
- Broker MQTT (Eclipse Mosquitto recommandé)
- Redis (pour le cache et rate limiting)

## Installation et Démarrage

### 1. Clone du Projet

```bash
git clone <repository-url>
cd notification-microservice
```

### 2. Compilation

```bash
mvn clean compile
```

### 3. Tests

```bash
mvn test
```

### 4. Packaging

```bash
mvn clean package
```

### 5. Démarrage

#### Avec H2 (développement)

```bash
java -jar target/notification-microservice-1.0.0.jar
```

#### Avec MySQL (production)

```bash
java -jar target/notification-microservice-1.0.0.jar --spring.profiles.active=prod
```

### 6. Accès aux Interfaces

- **API REST** : http://localhost:8080/notification-service/api
- **Console H2** : http://localhost:8080/notification-service/h2-console
- **Actuator Health** : http://localhost:8080/notification-service/actuator/health
- **Métriques Prometheus** : http://localhost:8080/notification-service/actuator/prometheus

## Configuration

### Variables d'Environnement

```bash
# Base de données
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=notification_db
export DB_USERNAME=notification_user
export DB_PASSWORD=notification_password

# MQTT
export MQTT_BROKER_URL=tcp://localhost:1883
export MQTT_USERNAME=mqtt_user
export MQTT_PASSWORD=mqtt_password

# Eureka
export EUREKA_URL=http://localhost:8761/eureka/
```

### Profils Spring

- **dev** : Développement avec H2
- **prod** : Production avec MySQL

## API Endpoints

### Notifications

- `POST /api/notifications` - Créer une notification
- `GET /api/notifications/utilisateur/{id}` - Notifications d'un utilisateur
- `GET /api/notifications/utilisateur/{id}/non-lues` - Notifications non lues
- `PUT /api/notifications/{id}/marquer-lue` - Marquer comme lue
- `DELETE /api/notifications/{id}` - Supprimer une notification

### MQTT

- `POST /api/mqtt/sessions` - Créer une session MQTT
- `DELETE /api/mqtt/sessions/{id}` - Fermer une session
- `GET /api/mqtt/sessions/actives` - Sessions actives
- `POST /api/mqtt/topics/{topic}/subscribe` - S'abonner à un topic
- `DELETE /api/mqtt/topics/{topic}/unsubscribe` - Se désabonner

## Exemples d'Utilisation

### Créer une Notification

```bash
curl -X POST http://localhost:8080/notification-service/api/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Test Notification",
    "message": "Ceci est un test",
    "type": "INFO",
    "canal": "EMAIL",
    "destinataireId": "1001",
    "typeDestinataire": "USER",
    "priorite": "NORMALE"
  }'
```

### Récupérer les Notifications

```bash
curl http://localhost:8080/notification-service/api/notifications/utilisateur/1001
```

## Déploiement avec Docker

### Build de l'Image

```bash
docker build -t notification-service:1.0.0 .
```

### Démarrage avec Docker Compose

```bash
docker-compose up -d
```

## Intégration avec API Gateway

Consultez le document [INTEGRATION_GATEWAY.md](INTEGRATION_GATEWAY.md) pour les détails complets sur l'intégration avec une API Gateway.

## Monitoring

Le service expose plusieurs endpoints de monitoring :

- `/actuator/health` - État de santé
- `/actuator/metrics` - Métriques applicatives
- `/actuator/prometheus` - Métriques Prometheus
- `/actuator/info` - Informations sur l'application

## Développement

### Structure du Projet

```
src/main/java/com/notification/
├── entity/          # Entités JPA
├── repository/      # Repositories Spring Data
├── service/         # Services métier
├── controller/      # Contrôleurs REST
├── config/          # Configuration Spring
└── dto/             # Objets de transfert de données
```

### Tests

```bash
# Tests unitaires
mvn test

# Tests d'intégration
mvn verify -Dspring.profiles.active=integration
```

## Contribution

1. Fork du projet
2. Création d'une branche feature
3. Commit des modifications
4. Push vers la branche
5. Création d'une Pull Request

## Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## Support

Pour toute question ou problème, veuillez créer une issue sur le repository GitHub.

