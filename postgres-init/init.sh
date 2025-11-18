#!/bin/bash
# Ce script garantit que tous les utilisateurs et bases de données sont créés
# de manière idempotente (il ne recrée pas ce qui existe déjà).
set -e

echo "🚀 Démarrage de l'initialisation de la base de données..."

# Étape 1: Création des utilisateurs et des bases de données
# ---------------------------------------------------------
# Connexion à la base de données 'postgres' par défaut pour exécuter ces commandes.
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL

    --- Création pour Keycloak ---
    DO \$\$ BEGIN
        IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'keycloak_user') THEN
            CREATE USER keycloak_user WITH ENCRYPTED PASSWORD 'password';
            RAISE NOTICE 'Utilisateur "keycloak_user" créé.';
        END IF;
    END \$\$;
    SELECT 'CREATE DATABASE keycloak_db OWNER keycloak_user' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'keycloak_db')\gexec
    GRANT ALL PRIVILEGES ON DATABASE keycloak_db TO keycloak_user;

    --- Création pour Equipment Service ---
    DO \$\$ BEGIN
        IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'equipment_user') THEN
            CREATE USER equipment_user WITH ENCRYPTED PASSWORD 'equipment_password';
            RAISE NOTICE 'Utilisateur "equipment_user" créé.';
        END IF;
    END \$\$;
    SELECT 'CREATE DATABASE equipment_db OWNER equipment_user' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'equipment_db')\gexec
    GRANT ALL PRIVILEGES ON DATABASE equipment_db TO equipment_user;

    --- Création pour User Service ---
    DO \$\$ BEGIN
        IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'user_user') THEN
            CREATE USER user_user WITH ENCRYPTED PASSWORD 'user_password';
            RAISE NOTICE 'Utilisateur "user_user" créé.';
        END IF;
    END \$\$;
    SELECT 'CREATE DATABASE user_db OWNER user_user' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'user_db')\gexec
    GRANT ALL PRIVILEGES ON DATABASE user_db TO user_user;

    --- Création pour Notification Service ---
    DO \$\$ BEGIN
        IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'notification_user') THEN
            CREATE USER notification_user WITH ENCRYPTED PASSWORD 'notification_password';
            RAISE NOTICE 'Utilisateur "notification_user" créé.';
        END IF;
    END \$\$;
    SELECT 'CREATE DATABASE notification_db OWNER notification_user' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'notification_db')\gexec
    GRANT ALL PRIVILEGES ON DATABASE notification_db TO notification_user;

    --- Création pour Incident Service ---
    DO \$\$ BEGIN
        IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'incident_user') THEN
            CREATE USER incident_user WITH ENCRYPTED PASSWORD 'incident_password';
            RAISE NOTICE 'Utilisateur "incident_user" créé.';
        END IF;
    END \$\$;
    SELECT 'CREATE DATABASE incident_db OWNER incident_user' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'incident_db')\gexec
    GRANT ALL PRIVILEGES ON DATABASE incident_db TO incident_user;

    --- Création pour Analysis Service ---
    DO \$\$ BEGIN
        IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'analysis_user') THEN
            CREATE USER analysis_user WITH ENCRYPTED PASSWORD 'analysis_password';
            RAISE NOTICE 'Utilisateur "analysis_user" créé.';
        END IF;
    END \$\$;
    SELECT 'CREATE DATABASE analysis_db OWNER analysis_user' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'analysis_db')\gexec
    GRANT ALL PRIVILEGES ON DATABASE analysis_db TO analysis_user;

    --- Création pour MQTT Service ---
    DO \$\$ BEGIN
        IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'mqtt_user') THEN
            CREATE USER mqtt_user WITH ENCRYPTED PASSWORD 'mqtt_password';
            RAISE NOTICE 'Utilisateur "mqtt_user" créé.';
        END IF;
    END \$\$;
    SELECT 'CREATE DATABASE mqtt_db OWNER mqtt_user' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'mqtt_db')\gexec
    GRANT ALL PRIVILEGES ON DATABASE mqtt_db TO mqtt_user;

    --- Création pour Config Service (AJOUTÉ) ---
    DO \$\$ BEGIN
        IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'config_user') THEN
            CREATE USER config_user WITH ENCRYPTED PASSWORD 'config_password';
            RAISE NOTICE 'Utilisateur "config_user" créé.';
        END IF;
    END \$\$;
    SELECT 'CREATE DATABASE config_db OWNER config_user' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'config_db')\gexec
    GRANT ALL PRIVILEGES ON DATABASE config_db TO config_user;

EOSQL

echo "🔧 Configuration des privilèges sur les schémas..."

# Étape 2: Octroi des privilèges sur le schéma public de chaque base de données
# -----------------------------------------------------------------------------
# Ces commandes doivent être exécutées en se connectant à chaque base de données respective.

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "notification_db" <<-EOSQL
    GRANT ALL ON SCHEMA public TO notification_user;
    DO \$\$ BEGIN RAISE NOTICE 'Privilèges sur le schéma public accordés pour notification_db.'; END \$\$;
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "incident_db" <<-EOSQL
    GRANT ALL ON SCHEMA public TO incident_user;
    DO \$\$ BEGIN RAISE NOTICE 'Privilèges sur le schéma public accordés pour incident_db.'; END \$\$;
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "analysis_db" <<-EOSQL
    GRANT ALL ON SCHEMA public TO analysis_user;
    DO \$\$ BEGIN RAISE NOTICE 'Privilèges sur le schéma public accordés pour analysis_db.'; END \$\$;
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "mqtt_db" <<-EOSQL
    GRANT ALL ON SCHEMA public TO mqtt_user;
    DO \$\$ BEGIN RAISE NOTICE 'Privilèges sur le schéma public accordés pour mqtt_db.'; END \$\$;
EOSQL

# --- Privilèges pour Config Service (AJOUTÉ) ---
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "config_db" <<-EOSQL
    GRANT ALL ON SCHEMA public TO config_user;
    DO \$\$ BEGIN RAISE NOTICE 'Privilèges sur le schéma public accordés pour config_db.'; END \$\$;
EOSQL

# Ajoutez ici d'autres blocs psql pour les autres services (equipment_db, user_db) si leurs entités
# nécessitent des droits spécifiques sur le schéma public.

echo "✅ Initialisation complète de la base de données terminée avec succès."
