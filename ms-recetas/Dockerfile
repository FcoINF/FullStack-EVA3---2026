FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY ms-consultas/pom.xml ms-consultas/pom.xml
COPY ms-farmacia/pom.xml ms-farmacia/pom.xml
COPY ms-pacientes/pom.xml ms-pacientes/pom.xml
COPY ms-profesionales/pom.xml ms-profesionales/pom.xml
COPY ms-programas/pom.xml ms-programas/pom.xml
COPY ms-recetas/pom.xml ms-recetas/pom.xml
COPY .mvn .mvn
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd

RUN mvn dependency:go-offline -pl ms-recetas -am -q

COPY ms-recetas/src ms-recetas/src

RUN mvn clean package -DskipTests -pl ms-recetas -am -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/ms-recetas/target/ms-recetas-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
