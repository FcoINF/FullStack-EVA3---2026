FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml pom.xml
COPY ms-consultas ms-consultas
COPY .mvn .mvn
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
RUN mvn clean package -DskipTests -pl ms-consultas -am

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/ms-consultas/target/ms-consultas-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
