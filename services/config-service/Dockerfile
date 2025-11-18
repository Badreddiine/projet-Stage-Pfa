# Étape 1 : Build Maven
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app

# Copier seulement le pom.xml pour mettre en cache les dépendances
COPY pom.xml .

# Télécharger les dépendances Maven
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler et packager sans tests
RUN mvn clean package -DskipTests

# Étape 2 : Image finale
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# --- MODIFICATION CI-DESSOUS ---
# Copier n'importe quel fichier .jar depuis le répertoire target
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8088
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
