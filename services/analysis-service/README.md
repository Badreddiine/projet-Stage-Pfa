# Analysis Service - Service d'Analyse

## Vue d'ensemble

Le **Analysis Service** est un microservice Spring Boot conçu pour gérer les analyses, recommandations de maintenance, métriques KPI, modèles prédictifs et analyses de tendance dans une architecture microservices sécurisée avec Keycloak. Il fournit des capacités d'analyse avancées pour optimiser la maintenance et les performances des équipements.

## Fonctionnalités principales

### 🔧 Recommandations de Maintenance
- Génération automatique de recommandations basées sur l'analyse des données
- Priorisation intelligente (Faible, Moyenne, Haute, Critique)
- Workflow d'approbation et de suivi
- Estimation des coûts et scores de confiance
- Métadonnées personnalisables

### 📊 Métriques KPI
- Suivi des indicateurs de performance clés
- Calcul automatique des tendances
- Alertes basées sur des seuils configurables
- Historique des valeurs et statistiques
- Comparaison avec les objectifs cibles

### 📈 Rapports d'Analyse
- Génération de rapports personnalisés
- Support de multiples formats (PDF, Excel, JSON)
- Planification et automatisation
- Données agrégées et visualisations
- Export et partage sécurisés

### 🤖 Modèles Prédictifs
- Gestion des modèles d'apprentissage automatique
- Support de multiples algorithmes
- Versioning et déploiement des modèles
- Métriques de performance et précision
- Prédictions en temps réel

### 📉 Analyses de Tendance
- Détection automatique des tendances
- Analyse de corrélation
- Identification des patterns cycliques
- Prédictions de tendances futures
- Interprétation intelligente des données

## Architecture

### Entités principales

```
RecommandationMaintenance
├── Équipement associé
├── Type et priorité
├── Workflow d'approbation
└── Métadonnées personnalisées

MetriqueKPI
├── Valeurs actuelles/cibles
├── Historique des mesures
├── Alertes et seuils
└── Calculs de tendance

RapportAnalyse
├── Configuration du rapport
├── Données et contenu
├── Statut de génération
└── Métadonnées de sortie

ModelePredictif
├── Algorithme et paramètres
├── Données d'entraînement
├── Métriques de performance
└── Versioning

AnalyseTendance
├── Type d'analyse
├── Corrélations et pentes
├── Interprétations
└── Données statistiques
```

### Flux de données

1. **Collecte** : Réception des données depuis les autres microservices
2. **Analyse** : Traitement et calcul des métriques
3. **Prédiction** : Application des modèles prédictifs
4. **Recommandation** : Génération de recommandations intelligentes
5. **Rapport** : Création de rapports et visualisations
6. **Notification** : Alertes et notifications automatiques

## Installation et Configuration

### Prérequis

- Java 17+
- PostgreSQL 15+
- Docker et Docker Compose
- Keycloak configuré
- Services existants (Discovery, Config Server, User Service)

### Configuration de la base de données

1. **Ajoutez le script d'initialisation** à votre dossier `postgres-init` :

```sql
-- Contenu du fichier postgres-init-analysis-db.sql
CREATE DATABASE analysis_db;
CREATE USER analysis_user WITH ENCRYPTED PASSWORD 'analysis_password';
GRANT ALL PRIVILEGES ON DATABASE analysis_db TO analysis_user;
```

2. **Mise à jour du docker-compose principal** :

Ajoutez le service analysis-service à votre fichier `docker-compose.yml` existant :

```yaml
  analysis-service:
    build:
      context: ./services/analysis-service
    container_name: analysis_service_container
    hostname: analysis-service
    ports:
      - "8085:8085"
      - "9004:9004"
    depends_on:
      postgres_db:
        condition: service_healthy
      config-server:
        condition: service_healthy
      discovery-service:
        condition: service_healthy
      user-service:
        condition: service_healthy
    networks:
      - sdsgpi_net
    environment:
      # Configuration complète dans docker-compose-analysis-service.yml
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_APPLICATION_NAME=analysis-service
      - SERVER_PORT=8085
      # ... autres variables d'environnement
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:9004/actuator/health"]
      interval: 15s
      timeout: 10s
      start_period: 120s
      retries: 10
    restart: unless-stopped
```

### Déploiement

1. **Clonez le service** dans votre structure de projet :
```bash
# Placez le dossier analysis-service dans services/
cp -r analysis-service /path/to/your/project/services/
```

2. **Construisez et démarrez** :
```bash
docker-compose up --build analysis-service
```

3. **Vérifiez le déploiement** :
```bash
# Vérifier la santé du service
curl http://localhost:9004/actuator/health

# Vérifier l'enregistrement Eureka
curl http://localhost:8761/eureka/apps/ANALYSIS-SERVICE
```

## Utilisation

### API Endpoints

#### Recommandations de Maintenance
```http
# Créer une recommandation
POST /api/recommandations
Content-Type: application/json
{
  "equipmentId": 1,
  "typeRecommandation": "MAINTENANCE_PREVENTIVE",
  "titre": "Remplacement des filtres",
  "description": "Les filtres montrent des signes d'usure",
  "priorite": "HAUTE",
  "coutEstime": 150.0,
  "scoreConfiance": 0.85
}

# Lister toutes les recommandations
GET /api/recommandations

# Recommandations urgentes
GET /api/recommandations/urgent

# Approuver une recommandation
PUT /api/recommandations/{id}/approve?approvedBy=john.doe

# Rechercher des recommandations
GET /api/recommandations/search?searchTerm=filtre
```

#### Métriques KPI
```http
# Créer une métrique
POST /api/metriques
Content-Type: application/json
{
  "nomMetrique": "Taux de disponibilité",
  "categorie": "PERFORMANCE",
  "valeurActuelle": 98.5,
  "valeurCible": 99.0,
  "unite": "%",
  "periodeCalcul": "MENSUEL",
  "seuilAlerte": 95.0
}

# Lister les métriques actives
GET /api/metriques

# Métriques avec alerte
GET /api/metriques/alerts

# Mettre à jour une valeur
PUT /api/metriques/{id}/value?nouvelleValeur=97.8

# Statistiques d'une métrique
GET /api/metriques/{id}/stats
```

#### Rapports d'Analyse
```http
# Créer un rapport
POST /api/rapports
Content-Type: application/json
{
  "nomRapport": "Rapport mensuel de maintenance",
  "typeRapport": "MAINTENANCE",
  "description": "Analyse des activités de maintenance du mois",
  "dateDebut": "2024-01-01T00:00:00",
  "finPeriode": "2024-01-31T23:59:59",
  "format": "PDF"
}

# Lister les rapports
GET /api/rapports

# Rapports terminés
GET /api/rapports/completed

# Télécharger un rapport
GET /api/rapports/{id}/download
```

### Intégration avec les autres microservices

#### 1. Client Feign pour Analysis Service

Dans vos microservices existants, ajoutez :

```java
@FeignClient(name = "analysis-service", path = "/api")
public interface AnalysisServiceClient {

    @PostMapping("/recommandations")
    RecommandationMaintenanceDto createRecommandation(@RequestBody CreateRecommandationRequest request);
    
    @GetMapping("/recommandations/equipment/{equipmentId}")
    List<RecommandationMaintenanceDto> getRecommandationsByEquipment(@PathVariable Long equipmentId);
    
    @PostMapping("/metriques")
    MetriqueKPIDto createMetrique(@RequestBody CreateMetriqueRequest request);
    
    @PutMapping("/metriques/{id}/value")
    MetriqueKPIDto updateMetriqueValue(@PathVariable Long id, @RequestParam Double nouvelleValeur);
}
```

#### 2. Utilisation dans vos services

```java
@Service
@RequiredArgsConstructor
public class EquipmentMaintenanceService {

    private final AnalysisServiceClient analysisClient;

    public void processMaintenanceData(Equipment equipment, MaintenanceData data) {
        // Créer une recommandation basée sur les données
        if (data.requiresMaintenance()) {
            CreateRecommandationRequest request = CreateRecommandationRequest.builder()
                .equipmentId(equipment.getId())
                .typeRecommandation("MAINTENANCE_CORRECTIVE")
                .titre("Maintenance requise pour " + equipment.getName())
                .priorite(determinePriority(data))
                .coutEstime(estimateCost(data))
                .build();
                
            analysisClient.createRecommandation(request);
        }
        
        // Mettre à jour les métriques
        updatePerformanceMetrics(equipment, data);
    }
    
    private void updatePerformanceMetrics(Equipment equipment, MaintenanceData data) {
        // Mettre à jour le taux de disponibilité
        analysisClient.updateMetriqueValue(1L, data.getAvailabilityRate());
        
        // Mettre à jour l'efficacité
        analysisClient.updateMetriqueValue(2L, data.getEfficiencyRate());
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
@PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
@PostMapping("/recommandations")
public ResponseEntity<RecommandationMaintenanceDto> createRecommandation(...) {
    // ...
}

@PreAuthorize("hasRole('MANAGER') or hasPermission('READ_REPORTS')")
@GetMapping("/rapports")
public ResponseEntity<List<RapportAnalyseDto>> getAllReports() {
    // ...
}
```

## Monitoring et Observabilité

### Endpoints Actuator
- **Health** : `http://localhost:9004/actuator/health`
- **Info** : `http://localhost:9004/actuator/info`
- **Metrics** : `http://localhost:9004/actuator/metrics`
- **Prometheus** : `http://localhost:9004/actuator/prometheus`

### Logs
- Logs structurés avec Logback
- Niveaux configurables par package
- Corrélation des requêtes
- Logs spécialisés pour les analyses

### Métriques personnalisées
- Nombre de recommandations générées
- Temps de traitement des analyses
- Précision des modèles prédictifs
- Taux d'approbation des recommandations

## Documentation API

### Swagger UI
Accédez à la documentation interactive à l'adresse :
`http://localhost:8085/swagger-ui.html`

### OpenAPI 3.0
Spécification disponible à :
`http://localhost:8085/v3/api-docs`

## Développement

### Structure du projet
```
analysis-service/
├── src/main/java/com/sdsgpi/analysisservice/
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
├── docker-compose-analysis-service.yml
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

## Algorithmes et Modèles

### Recommandations de Maintenance
- **Analyse prédictive** : Basée sur l'historique des pannes
- **Scoring de priorité** : Algorithme multi-critères
- **Estimation des coûts** : Modèles de régression
- **Confiance** : Calcul bayésien

### Métriques KPI
- **Calcul de tendances** : Régression linéaire
- **Détection d'anomalies** : Écart-type et percentiles
- **Prédictions** : Moyennes mobiles et lissage exponentiel
- **Alertes** : Seuils adaptatifs

### Analyses de Tendance
- **Corrélation** : Coefficient de Pearson
- **Détection de cycles** : Transformée de Fourier
- **Classification** : Algorithmes de clustering
- **Prédiction** : Séries temporelles (ARIMA)

## Dépannage

### Problèmes courants

#### 1. Erreur de connexion à la base de données
```
Caused by: org.postgresql.util.PSQLException: Connection refused
```
**Solution** : Vérifiez que PostgreSQL est démarré et que la base `analysis_db` existe.

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

#### 4. Erreur de calcul des métriques
```
NullPointerException in metric calculation
```
**Solution** : Vérifiez que les données d'entrée sont complètes et valides.

### Logs utiles
```bash
# Logs du service
docker logs analysis_service_container

# Logs de la base de données
docker logs postgres_db_container

# Logs Eureka
docker logs discovery_service_container
```

## Performance et Optimisation

### Configuration recommandée
```properties
# Taille des lots pour le traitement
analysis.service.batch.size=100

# TTL du cache
analysis.service.cache.ttl=3600

# Activation des prédictions
analysis.service.prediction.enabled=true

# Taille maximale des rapports
analysis.service.reports.max-size=10MB
```

### Optimisations base de données
- Index sur les colonnes fréquemment utilisées
- Partitioning des tables d'historique
- Archivage automatique des anciennes données
- Requêtes optimisées avec pagination

### Cache et performance
- Cache Redis pour les métriques fréquentes
- Cache local pour les modèles prédictifs
- Traitement asynchrone des rapports
- Pool de connexions optimisé

## Contribution

### Standards de code
- Java 17+ avec Spring Boot 3.2.5
- Lombok pour réduire le boilerplate
- Validation JSR-303
- Documentation Javadoc
- Tests unitaires obligatoires

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

