# 📚 PATRIC.IA — Microservicio de eventos

---

### 🛠️ Stack Tecnológico

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-47A248?style=for-the-badge&logo=mongodb&logoColor=white)

### ☁️ Infraestructura & Calidad

![Docker](https://img.shields.io/badge/Docker-Container-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI/CD-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-Quality-4E9BCD?style=for-the-badge&logo=sonarqube&logoColor=white)

### 🏗️ Arquitectura

![Hexagonal](https://img.shields.io/badge/Architecture-Hexagonal-blueviolet?style=for-the-badge)
![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-blue?style=for-the-badge)
![REST API](https://img.shields.io/badge/REST-API-009688?style=for-the-badge)

</div>
---

## 📑 Tabla de Contenidos

1. [👤 Integrantes](#1--integrantes)
2. [🎯 Objetivo del Microservicio](#2--objetivo-del-microservicio)
3. [⚡ Funcionalidades Principales](#3--funcionalidades-principales)
4. [📋 Estrategia de Versionamiento y Branches](#4--estrategia-de-versionamiento-y-branches)
5. [⚙️ Tecnologías Utilizadas](#5--tecnologías-utilizadas)
6. [🧩 Funcionalidad y Endpoints](#6--funcionalidad-y-endpoints)
7. [🏛️ Arquitectura, Patrones y Módulos](#7-️-arquitectura-patrones-y-módulos)
8. [📊 Diagramas](#8--diagramas)
9. [⚠️ Manejo de Errores](#9--manejo-de-errores)
10. [🧪 Evidencia de Pruebas y Cobertura](#10--evidencia-de-pruebas-y-cobertura)
11. [🗂️ Organización del Código](#11-️-organización-del-código)
12. [🔗 Conexiones con Servicios Externos](#12--conexiones-con-servicios-externos)
13. [🚀 Ejecución del Proyecto](#13--ejecución-del-proyecto)
14. [🐳 Dockerización](#14--dockerización)
15. [⚙️ Pipelines CI/CD](#15-️-pipelines-cicd)
16. [☁️ Despliegue en Azure](#16-️-despliegue-en-azure)
---

## 1. 👤 Integrantes

- Tomas Espitia Quiroga
- Sebastian Gonzalez Aranguren
- Camilo Cristancho
- Andres Pineda
---

## 2. 🎯 Objetivo del microservicio

El microservicio de **Eventos Universitarios** gestiona el ciclo de vida completo de los eventos académicos, culturales, deportivos y de bienestar de la Escuela Colombiana de Ingeniería Julio Garavito. Permite crear y consultar eventos con distintos tipos de acceso (abiertos o con cupo limitado), gestionar inscripciones (RSVP) y controlar la disponibilidad de cupos en tiempo real. Implementa los requisitos funcionales de **Eventos** del proyecto PATRICI.A.
 
---

## 3. ⚡ Funcionalidades principales

| Funcionalidad | Descripción |
|---|---|
| **Gestión de Eventos** | Crea, consulta y cancela eventos con nombre, lugar, categoría, tipo (OPEN/WITH_CAPACITY), cupo y fecha. |
| **Sistema de RSVP** | Confirma asistencia de estudiantes. Controla el cupo disponible en tiempo real y valida que el evento esté activo. |
| **Cancelación de Eventos** | El organizador cancela un evento activo; el estado cambia a CANCELLED. |
| **Cancelación de RSVP** | El estudiante cancela su inscripción, liberando un cupo en el evento. |
| **Consulta con Filtros** | Lista todos los eventos o busca uno por su ID. |
 
---

## 4. 📋 Estrategia de Versionamiento y Branches

### Estrategia de Ramas (Git Flow)

#### `main`
- Rama **estable** lista para producción. Solo recibe merges desde `develop`.
- Rama **protegida**: PR obligatorio, CI en verde antes de mergear.
#### `develop`
- Integración continua. Recibe merges desde `feature/*`.
#### `feature/*`
- Desarrollo de una funcionalidad específica. **Base:** `develop`. **Cierre:** PR hacia `develop`.
### 4.1 Convenciones para commits

```
feat: agregar endpoint de cancelación de RSVP
fix: corregir validación de cupo disponible
docs: actualizar README con endpoints
test: agregar pruebas de CancelEventUseCaseTest
```
 
---

## 5. ⚙️ Tecnologías Utilizadas

| **Tecnología** | **Uso en el proyecto** |
|---|---|
| **Java 21** | Lenguaje base con mejoras modernas. |
| **Spring Boot 3.3.0** | Framework principal para el microservicio REST. |
| **Spring Web** | Endpoints REST bajo `/api/events` y `/api/rsvp`. |
| **Spring Security** | Seguridad stateless; integración JWT con auth-service. |
| **Spring Data MongoDB** | Integración con MongoDB mediante el patrón Repository. |
| **MongoDB 7.0** | Base de datos NoSQL con colecciones `events` y `event_rsvp`. |
| **Flapdoodle Embed MongoDB** | MongoDB embebido exclusivo para tests. |
| **Maven** | Gestión de dependencias y builds. |
| **Lombok** | `@Getter`, `@Builder`, `@Data`, `@RequiredArgsConstructor`. |
| **jjwt 0.12.3** | Validación de tokens JWT con HMAC-SHA256. |
| **JUnit 5** | Framework de pruebas unitarias. |
| **Mockito** | Simulación de dependencias en pruebas. |
| **JaCoCo 0.8.13** | Reporte de cobertura de código. |
| **SonarQube** | Análisis estático de calidad. |
| **SpringDoc 2.6.0** | Swagger en `/swagger-ui.html`. |
| **Docker** | Contenerización multi-stage. |
| **Docker Compose** | Orquestación local app + MongoDB. |
| **GitHub Actions** | Pipeline CI/CD automatizado. |
 
---

## 6. 🧩 Funcionalidad y Endpoints
 
---

### 1️⃣ Crear Evento — `POST /api/events`

#### 📦 Request

| Campo | Tipo | Restricción | Descripción |
|---|---|:---:|---|
| name | String | Obligatorio | Nombre del evento |
| description | String | Opcional | Descripción |
| dateTime | LocalDateTime | Obligatorio | `yyyy-MM-ddTHH:mm:ss` |
| location | String | Obligatorio | Lugar |
| category | Enum | Obligatorio | ACADEMIC, CULTURAL, SPORTS, WELLNESS |
| type | Enum | Obligatorio | OPEN o WITH_CAPACITY |
| maxCapacity | Integer | Solo WITH_CAPACITY | Cupo máximo |
| organizerId | String | Obligatorio | ID del organizador |

#### 📤 Response (201 CREATED)

```json
{
  "id": "abc123-def456",
  "name": "Hackathon ECI 2026",
  "category": "ACADEMIC",
  "type": "WITH_CAPACITY",
  "maxCapacity": 50,
  "availableSpots": 50,
  "organizerId": "org-001",
  "status": "ACTIVE",
  "createdAt": "2026-05-10T08:00:00"
}
```

| HTTP | Escenario | Mensaje |
|:---:|:---|:---|
| 409 | Evento ya existe | `"El evento ya existe"` |
| 400 | Datos inválidos | `"Validation failed"` |
 
---

### 2️⃣ Consultar Todos los Eventos — `GET /api/events`

**Response (200 OK):** lista de `EventResponse`. Sin eventos retorna `[]`.
 
---

### 3️⃣ Consultar Evento por ID — `GET /api/events/{id}`

| HTTP | Escenario | Mensaje |
|:---:|:---|:---|
| 200 | Éxito | Objeto `EventResponse` completo |
| 404 | No existe | `"Evento no encontrado por el ID: {id}"` |
 
---

### 4️⃣ Cancelar Evento — `PATCH /api/events/{id}/cancel`

| HTTP | Escenario | Mensaje |
|:---:|:---|:---|
| 200 | Cancelado | `EventResponse` con `status: CANCELLED` |
| 404 | No existe | `"Evento no encontrado por el ID: {id}"` |
| 409 | Ya cancelado | `"El evento ya fue cancelado"` |
 
---

### 5️⃣ Crear RSVP — `POST /api/rsvp`

#### 📦 Request

| Campo | Tipo | Restricción | Descripción |
|---|---|:---:|---|
| eventId | String | Obligatorio | ID del evento |
| studentId | String | Obligatorio | ID del estudiante |
| confirmedAt | LocalDateTime | Obligatorio | Fecha de confirmación |
| status | Enum | Obligatorio | CONFIRMED |

#### 📤 Response (201 CREATED)

```json
{
  "id": "rsvp-001",
  "eventId": "abc123-def456",
  "studentId": "student-789",
  "confirmedAt": "2026-05-10T10:00:00",
  "status": "CONFIRMED"
}
```

| HTTP | Escenario | Mensaje |
|:---:|:---|:---|
| 400 | Sin cupo o cancelado | `"El evento con ID X no está disponible"` |
| 404 | Evento no existe | `"Evento no encontrado por el ID: X"` |
 
---

### 6️⃣ Cancelar RSVP — `PATCH /api/rsvp/{id}/cancel`

| HTTP | Escenario | Mensaje |
|:---:|:---|:---|
| 200 | Cancelado | `EventResponseRsvp` con `status: CANCELLED` |
| 404 | RSVP no existe | `"RSVP no encontrado"` |
 
---

## 7. 🏛️ Arquitectura, Patrones y Módulos

### Estilo Arquitectónico: Arquitectura Hexagonal (Ports & Adapters)

El dominio permanece completamente aislado de frameworks, bases de datos y dependencias externas. Las dependencias siempre apuntan hacia adentro.



### Patrones de Diseño Aplicados

| Patrón | Dónde se aplica | Propósito |
|---|---|---|
| **Ports & Adapters** | Toda la arquitectura | Desacopla el dominio de la infraestructura. Los puertos son interfaces; los adapters son implementaciones concretas. |
| **Repository** | `EventMongoRepository`, `EventRsvpMongoRepository` | Abstrae el acceso a datos MongoDB detrás de una interfaz definida en el dominio. |
| **Use Case / Interactor** | `CreateEventUseCase`, `CancelEventUseCase`, `CreateRsvpUseCase`, etc. | Encapsula cada caso de uso de negocio en una clase dedicada con responsabilidad única. |
| **DTO (Data Transfer Object)** | `EventRequest`, `EventResponse`, `EventRequestRsvp`, `EventResponseRsvp` | Separa los datos de la API del modelo de dominio. Nunca expone entidades de dominio directamente. |
| **Mapper** | `EventMapper`, `EventRsvpMapper`, `EventPersistenceMapper`, `EventRsvpPersistenceMapper` | Transforma entre capas (domain ↔ DTO, domain ↔ entity) sin contaminar ninguna capa con responsabilidades de otra. |
| **Value Object** | `EventId`, `RsvpId` | Encapsula identidad con semántica de dominio. Son inmutables y se comparan por valor, no por referencia. |
| **Builder** | Todos los modelos y value objects | Construcción fluida y legible de objetos complejos vía `@Builder` de Lombok. |
| **Exception Handler centralizado** | `GlobalExceptionHandler` (`@RestControllerAdvice`) | Captura todas las excepciones de dominio y las transforma en respuestas HTTP uniformes sin contaminar los controllers. |
| **Dependency Injection** | `@Component`, `@Service`, `@RequiredArgsConstructor` | Inversión de control gestionada por Spring IoC container. |

### Módulos que interactúan con Eventos

| Módulo | Dirección |
|---|-|
| **snorlax-energy-auth-service** |Auth → valida token en Eventos |
| **squirtle-squad-notification-service** | Eventos → notifica al crear/cancelar evento y RSVP |
| **charizard-compiled-gamification-service** | Eventos → otorga XP al confirmar RSVP |
| **charizard-compiled-hangout-service** | Parches referencian eventos al vincularse |
 
---

## 8. 📊 Diagramas

### Diagrama de Componentes - Vista Específica

///FOTO////

### Diagrama de Clases del Dominio

///FOTO////
### Diagrama de Base de Datos (MongoDB)

///FOTO////
### Diagrama de Despliegue

///FOTO////

---

## 9. ⚠️ Manejo de Errores

El `GlobalExceptionHandler` (`@RestControllerAdvice`) centraliza todas las excepciones y retorna respuestas JSON uniformes:

### Formato estándar de error

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Evento no encontrado por el ID: abc123"
}
```

### Excepciones manejadas

| ⚠️ Excepción | 🔢 HTTP | 💬 Escenario |
|:---|:---:|:---|
| `EventNotFoundException` | 404 | El evento no existe en MongoDB |
| `EventAlreadyExistsException` | 409 | Se intenta crear un evento duplicado |
| `EventNotAvailableException` | 400 | Evento cancelado o sin cupo disponible |
| `EventAlreadyCancelledException` | 409 | Se intenta cancelar un evento ya cancelado |
| `InvalidEventException` | 400 | Datos del evento inválidos |
| `RsvpNotFoundException` | 404 | El RSVP no existe |
| `Exception` | 500 | Error inesperado del servidor |

### Beneficios

| 🎯 Beneficio | 📋 Descripción |
|:---|:---|
| **Uniformidad** | Todas las respuestas de error tienen el mismo formato |
| **Mantenibilidad** | Agregar nuevas excepciones no requiere modificar cada controller |
| **Seguridad** | Oculta detalles internos del servidor |
| **Trazabilidad** | Cada error incluye el código HTTP y descripción |
 
---

## 10. 🧪 Evidencia de Pruebas y Cobertura

### Tipos de pruebas implementadas

| 🧪 Tipo                            | 📋 Descripción                                                                                                             | 🛠️ Herramientas  |
|:-----------------------------------|:---------------------------------------------------------------------------------------------------------------------------|:------------------|
| **Pruebas Unitarias**              | Validan el funcionamiento aislado de casos de uso, controladores y lógica de dominio con mocks                             | JUnit 5 + Mockito |
| **DataLoader**                     | Prueban carga inicial y que no duplica en reinicios                                                                        | JUnit 5 + Mockito |
| **Contexto Spring**           | Verifica que la app levanta correctamente con H2                                                                           | @SpringBootTest   |
| **Cobertura de Codigo**          | Para medir el porcentaje de codigo que cubren las pruebas                                                                  | JaCoCo            |
| **Controller (MockMvc)**           | Pruebas de endpoints REST                                                                                                  | Spring MockMvc    |
| **Contexto Spring**                | Verifica que la app levanta correctamente                                                                                  | @SpringBootTest   |
| **Cobertura de código**            | Mide el porcentaje cubierto por las pruebas                                                                                | JaCoCo            |

### Suites de prueba — 102 casos totales

```
src/test/java/edu/eci/patricia/DOWS_patricia/
│
├── application/
│   ├── mapper/
│   │   ├── EventMapperTest.java                     (7 casos)
│   │   └── EventRsvpMapperTest.java                 (7 casos)
│   └── usecase/
│       ├── CreateEventUseCaseTest.java              (3 casos)
│       ├── CancelEventUseCaseTest.java              (4 casos)
│       ├── GetEventByIdUseCaseTest.java             (3 casos)
│       ├── GetEventsUseCaseTest.java                (3 casos)
│       ├── CreateRsvpUseCaseTest.java               (6 casos)
│       └── CancelRsvpUseCaseTest.java               (6 casos)
│
├── domain/
│   ├── model/
│   │   ├── EventTest.java                           (9 casos)
│   │   └── EventRsvpTest.java                       (6 casos)
│   └── valueobjects/
│       ├── EventIdTest.java                         (6 casos)
│       ├── RsvpIdTest.java                          (6 casos)
│       ├── OrganizerIdTest.java                     (6 casos)
│       └── StudentIdTest.java                       (6 casos)
│
├── entrypoints/rest/controller/
│   ├── EventControllerTest.java                     (5 casos)
│   └── EventRsvpControllerTest.java                 (2 casos)
│
├── infrastructure/adapters/
│   ├── adapter/
│   │   ├── EventRepositoryAdapterTest.java          (7 casos)
│   │   └── EventRsvpRepositoryAdapterTest.java      (3 casos)
│   └── persistence/
│       ├── entity/
│       │   ├── EventEntityTest.java                 (4 casos)
│       │   └── EventRsvpEntityTest.java             (3 casos)
│       └── mapper/
│           ├── EventPersistenceMapperTest.java      (3 casos)
│           └── EventRsvpPersistenceMapperTest.java  (3 casos)
│
└── EeveevoidingResponsibilitiesCampusEventsApplicationTests.java (1 caso)
```

### Cómo ejecutar las pruebas

```bash
# Ejecutar todas las pruebas
mvn test
 
# Suite específica
mvn test -Dtest=CreateEventUseCaseTest
 
# Reporte de cobertura JaCoCo
mvn clean verify
 
# Ver reporte HTML
open target/site/jacoco/index.html
```

### Evidencia de ejecución

**Reporte JaCoCo con cobertura de código:**

///FOTO////

---

## 11. 🗂️ Organización del Código

```
eeveevoiding-responsibilities-campus-events/
│
├── .github/workflows/
│   ├── ci.yml                                        # Pipeline CI (build + tests)
│   └── cd.yml                                        # Pipeline CD (deploy Azure)
│
├── src/
│   ├── main/
│   │   ├── java/edu/eci/patricia/DOWS_patricia/
│   │   │   ├── application/                          # 🔵 CAPA DE APLICACIÓN
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   │   ├── EventRequest.java
│   │   │   │   │   │   └── EventRequestRsvp.java
│   │   │   │   │   └── response/
│   │   │   │   │       ├── EventResponse.java
│   │   │   │   │       └── EventResponseRsvp.java
│   │   │   │   ├── mapper/
│   │   │   │   │   ├── EventMapper.java
│   │   │   │   │   └── EventRsvpMapper.java
│   │   │   │   └── usecase/
│   │   │   │       ├── CreateEventUseCase.java
│   │   │   │       ├── CancelEventUseCase.java
│   │   │   │       ├── GetEventByIdUseCase.java
│   │   │   │       ├── GetEventsUseCase.java
│   │   │   │       ├── CreateRsvpUseCase.java
│   │   │   │       └── CancelRsvpUseCase.java
│   │   │   │
│   │   │   ├── domain/                               # 🟢 CAPA DE DOMINIO
│   │   │   │   ├── exceptions/
│   │   │   │   │   ├── EventNotFoundException.java
│   │   │   │   │   ├── EventAlreadyExistsException.java
│   │   │   │   │   ├── EventAlreadyCancelledException.java
│   │   │   │   │   ├── EventNotAvailableException.java
│   │   │   │   │   ├── InvalidEventException.java
│   │   │   │   │   └── RsvpNotFoundException.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── Event.java
│   │   │   │   │   ├── EventRsvp.java
│   │   │   │   │   └── enums/
│   │   │   │   │       ├── EventCategory.java
│   │   │   │   │       ├── EventStatus.java
│   │   │   │   │       ├── EventType.java
│   │   │   │   │       └── RsvpStatus.java
│   │   │   │   ├── ports/in/
│   │   │   │   │   ├── CreateEventPort.java
│   │   │   │   │   ├── CancelEventPort.java
│   │   │   │   │   ├── GetEventByIdPort.java
│   │   │   │   │   ├── GetEventsPort.java
│   │   │   │   │   ├── CreateRsvpPort.java
│   │   │   │   │   └── CancelRsvpPort.java
│   │   │   │   └── valueobjects/
│   │   │   │       ├── EventId.java
│   │   │   │       └── RsvpId.java
│   │   │   │
│   │   │   ├── entrypoints/                          # 🟠 ENTRADA (DRIVING ADAPTERS)
│   │   │   │   ├── advice/GlobalExceptionHandler.java
│   │   │   │   └── rest/controller/
│   │   │   │       ├── EventController.java
│   │   │   │       └── EventRsvpController.java
│   │   │   │
│   │   │   └── infrastructure/                       # 🟠 INFRAESTRUCTURA
│   │   │       ├── adapters/
│   │   │       │   ├── adapter/
│   │   │       │   │   ├── EventRepositoryAdapter.java
│   │   │       │   │   └── EventRsvpRepositoryAdapter.java
│   │   │       │   └── persistence/
│   │   │       │       ├── entity/
│   │   │       │       │   ├── EventEntity.java
│   │   │       │       │   └── EventRsvpEntity.java
│   │   │       │       ├── mapper/
│   │   │       │       │   ├── EventPersistenceMapper.java
│   │   │       │       │   └── EventRsvpPersistenceMapper.java
│   │   │       │       └── repository/
│   │   │       │           ├── EventMongoRepository.java
│   │   │       │           └── EventRsvpMongoRepository.java
│   │   │       └── config/SecurityConfig.java
│   │   │
│   │   └── resources/application.properties
│   │
│   └── test/
│       └── (ver sección 10)
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```
 
---

## 12. 🔗 Conexiones con Servicios Externos

| Servicio | Tipo | Variable | Descripción |
|---|---|---|---|
| **snorlax-energy-auth-service** | JWT compartido (HMAC-SHA256) | `JWT_SECRET` | Valida el token en cada request. Sin esta el microservicio rechaza todo. |
| **squirtle-squad-notification-service** | HTTP REST | `NOTIFICATION_SERVICE_URL` | Notifica al crear/cancelar eventos y al confirmar/cancelar RSVPs. |
| **charizard-compiled-gamification-service** | HTTP REST | `GAMIFICATION_SERVICE_URL` | Otorga XP al estudiante cuando confirma asistencia (RSVP). |
| **MongoDB Atlas** | Driver Mongo | `SPRING_DATA_MONGODB_URI` | Persistencia principal. Colecciones `events` y `event_rsvp`. |

> Las integraciones con **Notification** y **Gamification** están identificadas y pendientes de implementación REST. El `JWT_SECRET` ya está activo.
 
---

## 13. 🚀 Ejecución del Proyecto

### 📋 Prerrequisitos

- **Java 21**, **Maven 3.9+**, **Docker & Docker Compose**
### 🛠️ Opción 1: Local con Maven

```bash
# 1. Levantar MongoDB
docker run --name campus-events-db \
  -e MONGO_INITDB_DATABASE=campus-events \
  -p 27017:27017 -d mongo:7
 
# 2. Ejecutar la aplicación
mvn spring-boot:run
```

📍 **URL Local:** `http://localhost:8080`
📚 **Swagger UI:** `http://localhost:8080/swagger-ui.html`

### 🐳 Opción 2: Docker Compose

```bash
docker compose up --build
```

### ⚙️ Variables de entorno

| Variable | Valor por defecto | Descripción |
|:---|:---|:---|
| `SPRING_DATA_MONGODB_URI` | `mongodb://localhost:27017/campus-events` | URI de MongoDB |
| `WEBSITES_PORT` | `8080` | Puerto del servidor |
| `JWT_SECRET` | *(requerida)* | Clave HMAC-SHA256 del auth-service |
 
---

## 14. 🐳 Dockerización

El microservicio usa un **Dockerfile multi-stage** que separa la fase de build de la de ejecución:

```dockerfile
# Stage 1: Build con Maven completo
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn package -DskipTests -q
 
# Stage 2: Runtime mínimo con JRE Alpine
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S campusevents && adduser -S campusevents -G campusevents
COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p logs && chown -R campusevents:campusevents /app
USER campusevents
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

| ✅ Ventaja | Descripción |
|:---|:---|
| **Multi-stage** | La imagen final no incluye Maven ni código fuente — solo el JAR |
| **Alpine JRE** | Imagen base mínima, reduce el tamaño significativamente |
| **Non-root user** | El proceso corre como `campusevents`, no como `root` |
 
---

## 15. ⚙️ Pipelines CI/CD

### Pipeline de Desarrollo — `ci.yml`

Se ejecuta en cada **push o PR** a `main` o `develop`:

```yaml
name: CI - Build & Unit Tests
 
on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main, develop]
 
jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
      - uses: actions/cache@v4
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
      - name: Build and run tests with coverage
        run: mvn clean verify
      - name: Upload test reports
        uses: actions/upload-artifact@v4
        with:
          name: test-reports
          path: target/surefire-reports/
      - name: Upload JaCoCo coverage report
        uses: actions/upload-artifact@v4
        with:
          name: coverage-report
          path: target/site/jacoco/
 
  docker:
    needs: build-and-test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build Docker image
        run: docker build -t campus-events:${{ github.sha }} .
```

**Flujo CI:**
```
Push/PR → Checkout → Setup Java 21 → Cache Maven
        → mvn clean verify (build + tests + JaCoCo)
        → Upload artefactos (test-reports, coverage-report)
        → Build Docker image (si tests pasan)
```

### Pipeline de Producción — `cd.yml`

Se ejecuta **solo cuando `ci.yml` pasa en `main`**:

```yaml
name: CD - Deploy to Azure
 
on:
  workflow_run:
    workflows: ["CI - Build & Unit Tests"]
    types: [completed]
    branches: [main]
 
jobs:
  deploy:
    if: ${{ github.event.workflow_run.conclusion == 'success' }}
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Build JAR
        run: mvn package -DskipTests
      - name: Login to Azure
        uses: azure/login@v1
        with:
          creds: ${{ secrets.AZURE_CREDENTIALS }}
      - name: Deploy to Azure App Service
        uses: azure/webapps-deploy@v2
        with:
          app-name: ${{ secrets.AZURE_APP_SERVICE_NAME }}
          package: target/*.jar
```

**Flujo completo CD:**
```
Merge a main
    │
    ├─ ci.yml (~3-5 min)   Build + Tests + JaCoCo + Docker build
    │
    └─ cd.yml (~1-2 min)   [solo si ci.yml pasa]
        Build JAR → Azure Login → Deploy App Service → 🟢 Live
```

### Secrets de GitHub requeridos

| Secret | Descripción |
|---|---|
| `AZURE_CREDENTIALS` | JSON del Service Principal (pedir a Sebastian) |
| `AZURE_APP_SERVICE_NAME` | `app-patricia-campus-events` |
| `JWT_SECRET_TEST` | String ≥ 32 chars para CI |
| `SONAR_TOKEN` | `d453a7a0e951ce1dc893b735caaa7e44be3b1e99` |
| `SONAR_ORGANIZATION` | `patrici-a` |
| `SONAR_HOST_URL` | `https://sonarcloud.io` |

### Evidencia del despliegue CI/CD

////////////IMAGEN///////////////

---

## 16. ☁️ Despliegue en Azure

El microservicio está desplegado en **Azure App Service** dentro del Resource Group `patricia-prod`.

| Recurso | Valor |
|---|---|
| **App Service** | `app-patricia-campus-events` |
| **Resource Group** | `patricia-prod` |
| **Región** | Canada Central |
| **Runtime** | Java 21, Linux |
| **Plan** | `ASP-LIBREBIAgroup-942d` |

### 🌐 URLs públicas

| Recurso | URL |
|---|---|
| **API Base** | `https://app-patricia-campus-events.azurewebsites.net/api` |
| **Swagger UI** | `https://app-patricia-campus-events.azurewebsites.net/swagger-ui.html` |

### Variables de entorno en Azure App Service

| Nombre | Descripción |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `dev` |
| `WEBSITES_PORT` | `8080` |
| `SPRING_DATA_MONGODB_URI` | URI completa de MongoDB Atlas |
| `JWT_SECRET` | Clave compartida con auth-service |
 
---

### 🏆 Equipo **eeveevoiding-responsibilities**

![Course](https://img.shields.io/badge/Course-DOSW-orange?style=for-the-badge)
![Year](https://img.shields.io/badge/Year-2026--1-blue?style=for-the-badge)

> 💡 **M09 — Eventos Universitarios** gestiona el ciclo de vida completo de los eventos institucionales de la ECI, conectando organizadores y estudiantes de forma fluida y confiable.

**🎓 Escuela Colombiana de Ingeniería Julio Garavito**
