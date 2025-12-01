FROM eclipse-temurin:17-jdk-alpine

LABEL authors="jrr"

WORKDIR /app

COPY api-rest/target/api-rest-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]