FROM ubuntu:latest
LABEL authors="adamp"

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
