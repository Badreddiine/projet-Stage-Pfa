@echo off
REM Ce script est l'équivalent Windows du script .sh.
REM Il est conçu pour être exécuté dans un environnement où 'psql' est accessible.
REM Cependant, le conteneur PostgreSQL est basé sur Linux et exécutera un script .sh.
REM Le script .sh précédent est donc celui qui doit être utilisé par Docker.

REM Ce fichier .cmd est fourni à titre d'information pour montrer à quoi ressemblerait
REM la commande sur Windows, mais il ne sera PAS utilisé par Docker Compose.
REM Vous devez conserver le fichier .sh pour que le conteneur PostgreSQL fonctionne.

echo "Création de l'utilisateur et de la base de données pour equipment-service..."

REM La commande ci-dessous ne fonctionnera que si vous avez installé psql localement
REM et que le conteneur postgres_db a son port 5432 exposé.
psql -v ON_ERROR_STOP=1 --host=localhost --port=5432 --username=%POSTGRES_USER% --dbname=%POSTGRES_DB% -c "CREATE USER equipment_user WITH PASSWORD 'equipment_password';"
psql -v ON_ERROR_STOP=1 --host=localhost --port=5432 --username=%POSTGRES_USER% --dbname=%POSTGRES_DB% -c "CREATE DATABASE equipment_db;"
psql -v ON_ERROR_STOP=1 --host=localhost --port=5432 --username=%POSTGRES_USER% --dbname=%POSTGRES_DB% -c "GRANT ALL PRIVILEGES ON DATABASE equipment_db TO equipment_user;"

echo "Terminé."
