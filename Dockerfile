FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S campusevents && adduser -S campusevents -G campusevents
COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p logs && chown -R campusevents:campusevents /app
USER campusevents
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]