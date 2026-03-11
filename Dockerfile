FROM maven:3.9.6-eclipse-temurin-17-alpine
LABEL authors="summon10"
WORKDIR /app
EXPOSE 8080
COPY target/wallet-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]