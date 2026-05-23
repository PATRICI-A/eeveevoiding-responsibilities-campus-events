<div align="center">

# Campus Events Service — Microservicio de Eventos Universitarios (M09)

### *"Gestionando momentos, conectando la comunidad."*

---

### Stack Tecnológico

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-47A248?style=for-the-badge&logo=mongodb&logoColor=white)

### Infraestructura & Calidad

![Docker](https://img.shields.io/badge/Docker-Container-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![JaCoCo](https://img.shields.io/badge/JaCoCo-Coverage-8A0808?style=for-the-badge)

### Arquitectura

![Hexagonal](https://img.shields.io/badge/Architecture-Hexagonal-blueviolet?style=for-the-badge)
![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-blue?style=for-the-badge)
![REST API](https://img.shields.io/badge/REST-API-009688?style=for-the-badge)

</div>

---

## Tabla de Contenidos

1. [Integrantes](#1-integrantes)
2. [Tecnologías Utilizadas](#2-tecnologías-utilizadas)
3. [Descripción del Microservicio](#3-descripción-del-microservicio)
4. [Cómo Funciona](#4-cómo-funciona)
5. [Diagrama de Datos](#5-diagrama-de-datos)
6. [Diagrama de Clases](#6-diagrama-de-clases)
7. [Diagrama de Componentes](#7-diagrama-de-componentes)
8. [Funcionalidades Principales](#8-funcionalidades-principales)
9. [Endpoints](#9-endpoints)
10. [Colas de Mensajería](#10-colas-de-mensajería)
11. [Evidencia de Pruebas](#11-evidencia-de-pruebas)
12. [Evidencia de Cobertura](#12-evidencia-de-cobertura)
13. [Cómo Ejecutar](#13-cómo-ejecutar)
14. [Evidencia CI/CD](#14-evidencia-cicd)
15. [Link Swagger](#15-link-swagger)
16. [Estructura del Código](#16-estructura-del-código)
17. [Código Documentado](#17-código-documentado)
18. [Conexiones Externas](#18-conexiones-externas)
19. [Pipeline de Desarrollo](#19-pipeline-de-desarrollo)
20. [Pipeline de Producción](#20-pipeline-de-producción)
21. [Dockerizado](#21-dockerizado)
22. [Versionamiento](#22-versionamiento)

---

## 1. Integrantes

- Tomas Espitia Quiroga
- Sebastian Gonzalez Aranguren
- Camilo Cristancho
- Andres Pineda

---

## 2. Tecnologías Utilizadas

| **Tecnología / Herramienta** | **Uso principal en el proyecto** |
|---|---|
| **Java 21 (OpenJDK)** | Lenguaje base con soporte para Spring Boot. |
| **Spring Boot 3.3.0** | Framework principal. Agrupa Web, Security y Data. |
| **Spring Web** | Exposición de endpoints REST mediante controladores (`EventController`, `EventRsvpController`). |
| **Spring Security + JWT** | Protección de endpoints mediante token de sesión configurado con filtros y extracción de claims. |
| **Spring Data MongoDB** | Acceso a MongoDB, mapeando los documentos de Eventos y RSVP. |
| **MongoDB 7.0** | BD NoSQL principal. |
| **RabbitMQ (AMQP)** | Integración con sistema de colas para notificaciones. |
| **Apache Maven** | Gestión de dependencias y automatización de builds. |
| **Lombok 1.18.38** | Reducción de boilerplate. |
| **MapStruct 1.5.5** | Mapeo eficiente de Entidades a DTOs. |
| **JUnit 5 & Mockito** | Framework de pruebas unitarias y simulación de dependencias (Mocks). |
| **JaCoCo 0.8.13** | Análisis de cobertura de pruebas integrada en el ciclo de vida de Maven. |
| **SpringDoc OpenAPI 2.6.0** | Exposición dinámica del esquema de API y Swagger UI. |
| **Docker** | Contenedorización de la aplicación y base de datos local vía `docker-compose`. |

---

## 3. Descripción del Microservicio

El microservicio de **Eventos Universitarios** (M09), conocido en el repositorio como `eeveevoiding-responsibilities-campus-events`, tiene como objetivo gestionar el ciclo de vida completo de los eventos académicos, culturales, deportivos y de bienestar de la universidad PATRIC.IA.

Sus responsabilidades principales cubren:
1. **Gestión de Eventos:** Permite la creación, actualización, consulta y cancelación de eventos. Estos pueden ser de acceso abierto o con capacidad limitada.
2. **Sistema de RSVP (Inscripciones):** Permite que los estudiantes reserven su cupo en los eventos de capacidad limitada. Asegura el control en tiempo real de la disponibilidad.
3. **Notificación y Control:** Publicación de eventos de dominio cuando los eventos o los RSVPs cambian de estado, permitiendo notificar a los interesados.

Puerto: `8080`. Integrado con **MongoDB**.

---

## 4. Cómo Funciona

### Arquitectura Hexagonal (Ports & Adapters) y Clean Architecture

```
┌─────────────────────────────────────────────────────┐
│                  EXTERIOR                           │
│  ┌──────────────┐         ┌──────────────────────┐  │
│  │  Controllers │         │  Mongo Adapters      │  │
│  │  (REST)      │         │  (MongoDB)           │  │
│  │  Port In ──► │         │                      │  │
│  └──────┬───────┘         └────────────┬─────────┘  │
│         │          DOMINIO             │ ◄ Port Out  │
│         ▼   ┌────────────────────┐     │             │
│         └──►│  Use Cases /       │◄────┘             │
│             │  Domain Models     │                   │
│             └────────────────────┘                   │
└─────────────────────────────────────────────────────┘
```

El flujo está completamente aislado del framework:
1. Las peticiones entran por los `*Controller` en la capa *entrypoints*.
2. El controlador delega a los casos de uso definidos en *domain/ports/in*.
3. Los *Use Cases* (implementados en *application/usecase*) ejecutan lógica de negocio estricta y se comunican con los puertos de salida (*domain/ports/out*).
4. La persistencia ocurre a través de los *adapters* de repositorios en la capa *infrastructure*.

### Lógica de Dominio y Patrones de Diseño

| Patrón / Concepto | Ubicación | Descripción |
|---|---|---|
| **Ports & Adapters** | Toda la arquitectura | Interfaces claras (`In/Out`) para independizar el dominio. |
| **Value Object** | `EventId`, `RsvpId` | Abstraen las validaciones de identidad en las entidades. |
| **Casos de Uso Aislados** | `CreateEventUseCase`, `CreateRsvpUseCase` | Cada operación es una clase que aplica la regla de SRP (Single Responsibility Principle). |
| **Manejo de Cupos** | `EventDomainException` | Verificaciones estrictas en el dominio de la capacidad (`EventCapacityFullException`, `EventNotActiveException`). |

---

## 5. Diagrama de Datos

<div align="center">
![Diagrama de Datos](docs/Diagrama_Datos.png)
</div>

Contiene un modelo documental distribuido en dos colecciones principales:
- **Colección `events`**: Almacena toda la metadata del evento (nombre, descripción, categoría, estado, máxima capacidad, etc.).
- **Colección `event_rsvp`**: Almacena la relación entre el estudiante y su inscripción al evento.

---

## 6. Diagrama de Clases

<div align="center">
![Diagrama de Clases](docs/Diagrama_Clases.png)
</div>

**Resumen del diseño de dominio:**
- **`Event`**: Entidad principal que encapsula la información y reglas de negocio sobre el cupo del evento.
- **`EventRsvp`**: Representa la intención o confirmación de asistencia de un estudiante a un evento en particular.
- **Enumeradores Centrales**: `EventCategory` (ACADEMIC, CULTURAL, SPORTS, WELLNESS), `EventStatus` (ACTIVE, CANCELLED), `EventType` (OPEN, WITH_CAPACITY), `RsvpStatus` (CONFIRMED, CANCELLED).

---

## 7. Diagrama de Componentes

<div align="center">
![Diagrama de Componentes](docs/Diagrama_Componentes.png)
</div>

| Componente | Tipo | Responsabilidad |
|---|---|---|
| `EventController` | REST API | Recepción de tráfico HTTP para creación, consulta y cancelación de eventos. |
| `EventRsvpController` | REST API | Recepción de peticiones para inscribir o cancelar la asistencia de un estudiante a un evento. |
| `GlobalExceptionHandler` | Controller Advice | Centraliza el mapeo de `EventDomainException` y otras a códigos HTTP. |
| `EventChangePublisher` | Messaging | Emite mensajes a RabbitMQ cuando un evento relevante ocurre. |
| `*MongoRepository` | Spring Data | Ejecuta consultas y manipulación de documentos en MongoDB. |

---

## 8. Funcionalidades Principales

<div align="center">

| ID | Funcionalidad | Descripción |
|---|---|---|
| F01 | **Gestión de Eventos (CRUD)** | Creación y visualización detallada de los eventos del campus, ya sean libres o con límite de cupos. |
| F02 | **Gestión de Inscripciones (RSVP)** | Los estudiantes confirman su asistencia a los eventos controlados. El sistema deduce el cupo disponible dinámicamente. |
| F03 | **Cancelaciones y Disponibilidad** | Los estudiantes pueden retractar su inscripción liberando su cupo, y los organizadores pueden cancelar eventos enteros. |
| F04 | **Notificación de Cambios** | Los cambios de estado de eventos e inscripciones desencadenan avisos al microservicio de notificaciones. |

</div>

---

## 9. Endpoints

### Resumen de Rutas Principales

| Dominio | Endpoint | Método | Funcionalidad |
|---|---|---|---|
| **Eventos** | `/api/events` | `GET`, `POST` | Listar y crear eventos universitarios. |
| **Eventos** | `/api/events/{id}` | `GET` | Ver el detalle de un evento particular. |
| **Eventos** | `/api/events/{id}/cancel` | `PATCH` | Cancelar un evento. |
| **Inscripciones** | `/api/rsvp` | `POST` | Crear una inscripción confirmada (RSVP). |
| **Inscripciones** | `/api/rsvp/{id}/cancel` | `PATCH` | Cancelar una inscripción confirmada. |

> **Autenticación:** El servicio requiere JWT para validar a los usuarios. Los permisos varían (ej. solo el creador del evento puede cancelarlo).

---

## 10. Colas de Mensajería

El servicio implementa **RabbitMQ** para publicar eventos de dominio y desacoplar las notificaciones:

| Evento | Descripción | Consumer (Ejemplo) |
|---|---|---|
| `EventChangeEventDto` | Contiene metadata sobre la modificación en el estado del evento o la confirmación de RSVP. | `notification-service`, `gamification-service`. |

---

## 11. Evidencia de Pruebas

El servicio tiene una sólida estructura de pruebas usando JUnit 5 y Mockito. Posee pruebas para Casos de Uso, Entidades de Dominio, Controladores MVC y la capa de Infraestructura MongoDB.

```
src/test/java/edu/eci/patricia/DOWS_patricia/
├── application/usecase/          # Pruebas a la lógica de RSVP y creación
├── domain/model/                 # Pruebas a Entidades e invariantes de cupo
├── entrypoints/rest/controller/  # Test WebMvc para validar códigos 400, 404, 201
└── infrastructure/adapters/      # Test Mongo usando Flapdoodle Embed
```

### Comandos de Ejecución

```bash
# Ejecución general de pruebas
./mvnw test

# Pruebas + Generar reporte de JaCoCo
./mvnw clean verify
```

---

## 12. Evidencia de Cobertura

La construcción exige mantener una cobertura estricta de `>= 80%` gestionada a través de JaCoCo. La capa de configuración, mappers DTO y la clase Main se excluyen del cómputo.

*(Ejecuta el pipeline de pruebas o visualiza el archivo `target/site/jacoco/index.html` tras realizar un verify).*

---

## 13. Cómo Ejecutar

### Prerrequisitos

- Java 21
- Maven 3.9+
- Docker & Docker Compose (Recomendado para BD y RabbitMQ)

### Opción 1: Desarrollo Local (Maven + Contenedores)

```bash
# 1. Levantar MongoDB y RabbitMQ
docker run --name campus-events-db -e MONGO_INITDB_DATABASE=campus-events -p 27017:27017 -d mongo:7
# (Opcional) Levantar RabbitMQ
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

# 2. Instalar dependencias y correr en el puerto 8080
./mvnw spring-boot:run
```

### Opción 2: Docker Compose

El repositorio cuenta con la receta para levantar inmediatamente la aplicación junto a sus dependencias.

```bash
# Levantar servicios
docker compose up --build

# Bajar servicios
docker compose down -v
```

### Variables de Entorno Claves

| Variable | Descripción |
|---|---|
| `SPRING_DATA_MONGODB_URI` | URI de conexión a la BD Mongo |
| `SERVER_PORT` | Puerto de exposición de la API (Defecto: `8080`) |
| `JWT_SECRET` | Secreto HS256 para validación del token |
| `SPRING_RABBITMQ_HOST` | Host para conectividad con la cola AMQP |

---

## 14. Evidencia CI/CD

El módulo posee integración con GitHub Actions para asegurar la calidad del código mediante la validación continua.

El flujo incluye:
1. Configuración de **Java 21**.
2. **Compilación** vía Maven.
3. **Ejecución de Pruebas** con cobertura JaCoCo (`mvn clean verify`).
4. **Validación de Reglas** (como la obligación de no tener tests caídos ni cobertura por debajo del umbral).
5. Despliegue empaquetado como imagen Docker hacia el registro remoto de Azure/GitHub.

---

## 15. Link Swagger

Una vez ejecutada la aplicación, la documentación generada automáticamente por **SpringDoc OpenAPI** es accesible en:

| Entorno | URL |
|---|---|
| Interfaz Gráfica UI | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |
| Definición JSON | [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) |

*(Nota: Requiere autorizar el Bearer Token en Swagger para consumir los endpoints)*.

---

## 16. Estructura del Código

Respetando `Ports & Adapters`:

```text
src/main/java/edu/eci/patricia/DOWS_patricia/
├── application/                     # Lógica Orquestadora (Use Cases)
│   ├── dto/                         # Request / Response
│   ├── mapper/                      # MapStruct Mappers
│   └── usecase/                     # Implementación Use Cases
├── domain/                          # Lógica Pura
│   ├── exceptions/                  # Excepciones de negocio (ej. Cupo lleno)
│   ├── model/                       # Entidades y Enums
│   ├── ports/                       # Interfaces in/out
│   └── valueobjects/                # Objetos inmutables de identidad
├── entrypoints/                     # Capa de Entrada Web
│   ├── advice/                      # Global Exception Handler
│   └── rest/controller/             # Controladores
└── infrastructure/                  # Adaptadores Salientes
    ├── adapters/                    # Implementación Repositorios
    ├── config/                      # Seguridad, Swagger, RabbitMQ
    ├── messaging/                   # Publicadores RabbitMQ
    └── notification/                # Clientes externos
```

---

## 17. Código Documentado

El código base se apoya fuertemente en el auto-documentado derivado de los principios Clean Code. Los DTOs tienen estructura clara en `application/dto`. Se proveen definiciones OpenAPI directas en los Request y Response de manera limpia. 

---

## 18. Conexiones Externas

| Módulo | Tipo | Descripción |
|---|---|---|
| **snorlax-energy-auth-service** | JWT (Offline) | El filtro `JwtAuthFilter` utiliza una clave compartida para decodificar los claims del token inyectado por el servicio Auth. |
| **squirtle-squad-notification-service** | Mensajería (AMQP / HTTP) | Recibe las alertas generadas por cambios de eventos o reservaciones. |
| **charizard-compiled-gamification-service** | HTTP | Sistema de gamificación para premiar la asistencia a eventos universitarios. |

---

## 19. Pipeline de Desarrollo

1. Todo el desarrollo se deriva de la rama `develop`.
2. Ramas feature como `feature/nombre-de-la-tarea`.
3. Commits convencionales (`feat:`, `fix:`, `docs:`).
4. Pruebas locales superadas con éxito.
5. Pull Request a `develop` condicionado por la revisión automatizada del flujo en GitHub Actions.

---

## 20. Pipeline de Producción

Los merges sobre la rama `main` disparan un pipeline de Producción para el despliegue hacia Cloud (ej. Azure). Se construyen las imágenes en Docker basándose en el artefacto final sin arrastrar el código fuente.

---

## 21. Dockerizado

### Dockerfile

Basado en multistage-build para seguridad y bajo peso:

```dockerfile
# Stage 1: Compilación
FROM maven:3.9.6-eclipse-temurin-21 AS build
...
# Stage 2: Runtime Mínimo
FROM eclipse-temurin:21-jre-alpine
...
COPY --from=build /app/target/*.jar app.jar
...
USER campusevents
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 22. Versionamiento

Uso riguroso de **Git Flow**:

- `main`: Entorno de producción.
- `develop`: Entorno de staging e integración conjunta.
- `feat/*`: Para el desarrollo iterativo.
- Etiquetado de versiones garantizado durante los releases de las diferentes interacciones de PATRIC.IA.
