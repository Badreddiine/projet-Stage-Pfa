# Étape 1 : Compilation
FROM eclipse-temurin:17-jdk-jammy AS build

# Crée un répertoire de travail
WORKDIR /app

# Copie ton code source dans l'image
COPY src /app/src

# Compile ton programme
RUN javac -d /app/bin /app/src/ma/fsts/badr/Main.java

# Étape 2 : Exécution
FROM eclipse-temurin:17-jre-jammy

# Répertoire de travail
WORKDIR /app

# Copie le code compilé depuis l'étape de build
COPY --from=build /app/bin /app/bin

# Expose le port (si ton appli en avait besoin)
EXPOSE 8080

# Exécution du programme
ENTRYPOINT ["java", "-cp", "/app/bin", "ma.fsts.badr.Main"]



