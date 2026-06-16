FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# 1. Copy all pom.xml files to satisfy multi-module resolution
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

# 2. Download dependencies (cached layer, solo se repite si cambian los pom)
RUN mvn dependency:go-offline -pl ms-consultas -am -q

# 3. Copy source code of only the module we build
COPY ms-consultas/src ms-consultas/src

# 4. Build
RUN mvn clean package -DskipTests -pl ms-consultas -am -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/ms-consultas/target/ms-consultas-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
