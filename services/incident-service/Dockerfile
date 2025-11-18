FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests

# Solution universelle : copie le premier JAR trouvé vers app.jar
RUN find target -name "*.jar" -not -name "*sources*" -not -name "*javadoc*" | head -1 | xargs -I {} cp {} target/app.jar

# Adaptez le port selon le service
EXPOSE 8084

# Adaptez le nom du service
RUN mkdir -p /var/log/equipement-service

CMD ["java", "-jar", "target/app.jar"]
