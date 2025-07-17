# Stage 1: Build (opcional si ja tens el JAR creat)
# FROM maven:3.9.6-eclipse-temurin-21 AS build
# WORKDIR /app
# COPY . .
# RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
