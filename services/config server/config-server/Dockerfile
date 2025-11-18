# Dockerfile pour le config-server
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# D'abord, copier le dossier de configuration DANS l'image
COPY configurations/ ./configurations/

# Ensuite, copier le JAR de l'application
COPY target/config-server-*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
