FROM openjdk:17-jdk-slim

# Installer curl pour healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Définir le dossier de travail
WORKDIR /app

# Copier Maven Wrapper et fichiers nécessaires pour télécharger les dépendances
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Rendre le wrapper exécutable et télécharger les dépendances en avance
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler et packager sans exécuter les tests
RUN ./mvnw clean package -DskipTests

# Trouver le JAR généré et le copier comme app.jar
RUN find target -name "*.jar" -not -name "*sources*" -not -name "*javadoc*" \
    | head -1 | xargs -I {} cp {} app.jar

# Exposer le port du service (à changer selon le microservice)
EXPOSE 8085

# Lancer l'application
CMD ["java", "-jar", "app.jar"]
