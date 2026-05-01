# ================================
# ETAPA 1: Build
# ================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar dependencias primero
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copiar código fuente y compilar
COPY src ./src
RUN mvn package -DskipTests -q

# ================================
# ETAPA 2: Runtime
# ================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Usuario no-root por seguridad
RUN addgroup -S campusevents && adduser -S campusevents -G campusevents

# Copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Carpeta de logs con permisos correctos
RUN mkdir -p logs && chown -R campusevents:campusevents /app

USER campusevents

# Variables de entorno — los valores reales vienen del .env
ARG SPRING_SECURITY_USER_NAME
ARG SPRING_SECURITY_USER_PASSWORD
ENV SPRING_SECURITY_USER_NAME=${SPRING_SECURITY_USER_NAME:-admin} \
    SPRING_SECURITY_USER_PASSWORD=${SPRING_SECURITY_USER_PASSWORD:-admin}

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]