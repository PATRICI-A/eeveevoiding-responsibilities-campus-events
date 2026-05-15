<div align="center">

# 📚 PATRIC.IA — Microservicio de eventos

### *"SLOGAN"*

---

### 🛠️ Stack Tecnológico

![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-47A248?style=for-the-badge&logo=mongodb&logoColor=white)

### ☁️ Infraestructura & Calidad

![Azure](https://img.shields.io/badge/Azure-Cloud-0078D4?style=for-the-badge&logo=microsoft-azure&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Container-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

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
4. [📋 Estrategia de Versionamiento y Branches](#4--manejo-de-estrategia-de-versionamiento-y-branches)
    - [4.1 Convenciones para crear ramas](#41-convenciones-para-crear-ramas)
5. [⚙️ Tecnologías Utilizadas](#5--tecnologias-utilizadas)
6. [🧩 Funcionalidad](#6--funcionalidad)
7. [📊 Diagramas](#7--diagramas)
8. [⚠️ Manejo de Errores](#8--manejo-de-errores)
9. [🧪 Evidencia de Pruebas y Ejecución](#9--evidencia-de-las-pruebas-y-como-ejecutarlas)
10. [🗂️ Organización del Código](#10--codigo-de-la-implementacion-organizado-en-las-respectivas-carpetas)
11. [🚀 Ejecución del Proyecto](#11--ejecucion-del-proyecto)
12. [☁️ CI/CD y Despliegue en Azure](#12--evidencia-de-cicd-y-despliegue-en-azure)
13. [🤝 Contribuciones](#13--contribuciones)

---

## 1. 👤 Integrantes:

- Tomas Espitia Quiroga
- Sebastian Gonzalez Aranguren
- Camilo Cristancho
- Andres Pineda

## 2. 🎯 Objetivo del microservicio

---

El microservicio de Eventos Universitarios tiene como objetivo gestionar el ciclo de 
vida completo de los eventos académicos, culturales, deportivos y de bienestar que se realizan en la 
Escuela Colombiana de Ingeniería Julio Garavito. Además, incorpora la posibilidad de cancelar eventos 
e inscripciones garantizando una experiencia confiable y organizada para toda la comunidad estudiantil.

## 3. ⚡ Funcionalidades principales

---

<div align="center">

<table>
  <thead>
    <tr>
      <th>💡 Funcionalidad</th>
      <th>Descripción</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>Gestion de eventos</strong></td>
      <td>Crea eventos almacenando información como la fecha, capacidad, descripción y tipo de evento</td>
    </tr>
    <tr>
      <td><strong>Cancelar eventos</strong></td>
      <td>El administrador puede cancelar un evento cambiando el estado del mismo de ACTIVE a CANCELLED, o al tipo FINISHED automáticamente cuando el evento se considera terminado</td>
    </tr>
    <tr>
      <td><strong>Asistencia a un evento</strong></td>
      <td>Si un evento tiene capacidad limitada, un estudiante puede apartar el cupo a este evento, de la misma manera cancelar el cupo previamente solicitado.</td>
    </tr>
  </tbody>
</table>

</div>


## 4. 📋 Manejo de Estrategia de versionamiento y branches

### Estrategia de Ramas (Git Flow)

#### `main`
- Rama **estable** con la versión final lista para demo/producción.
- Solo recibe merges desde `develop`.
- Rama **protegida**: PR obligatorio, aprobaciones requeridas, CI en verde.
#### `develop`
- Base de integración continua para nuevas funcionalidades.
- Recibe merges desde `feature/*`.
#### `feature/*`
- Desarrollo de una funcionalidad o refactor específico.
- **Base:** `develop`. **Cierre:** PR hacia `develop`.
### 4.1 Convenciones para commits

```
[tipo]: [descripción específica]
 
feat: agregar endpoint de cancelación de eventos
fix: corregir validación de cupo en CreateRsvpUseCase
docs: actualizar README con instrucciones de ejecución
test: agregar pruebas de EventControllerTest con MockMvc
```
---

## 5. ⚙️ Tecnologías Utilizadas

---

| **Tecnología / Herramienta** | **Uso principal en el proyecto** |
|------------------------------|----------------------------------|
| **Java OpenJDK** | Lenguaje de programación base de los microservicios backend, orientado a objetos y multiplataforma. |
| **Spring Boot** | Framework principal para construir microservicios independientes, exponiendo APIs REST y gestionando configuración e inyección de dependencias. |
| **Spring Web** | Exposición de endpoints REST en cada microservicio (controladores HTTP) dentro de la arquitectura hexagonal. |
| **Spring Security** | Configuración de autenticación y autorización mediante roles, asegurando el acceso a los endpoints de los microservicios. |
| **Spring Data MongoDB** | Integración de cada microservicio con su base de datos NoSQL en MongoDB usando el patrón Repository y puertos/adaptadores. |
| **MongoDB Atlas** | Base de datos NoSQL en la nube, con colecciones independientes por dominio (pagos, billetera, recibos, promociones, usuarios, etc.). |
| **Apache Maven** | Gestión de dependencias, empaquetado de cada microservicio y automatización de builds en los pipelines CI/CD. |
| **Lombok** | Reducción de código repetitivo en los microservicios con anotaciones como `@Getter`, `@Setter`, `@Builder` y `@AllArgsConstructor`. |
| **JUnit 5** | Framework de pruebas unitarias para validar la lógica de dominio y casos de uso en cada microservicio. |
| **Mockito** | Simulación de dependencias (puertos, repositorios, clientes externos) en pruebas unitarias sin acceder a infraestructura real. |
| **JaCoCo** | Generación de reportes de cobertura de código de los microservicios para evaluar la efectividad de las pruebas. |
| **SonarQube** | Análisis estático del código y control de calidad, identificando vulnerabilidades, code smells y problemas de mantenibilidad. |
| **Swagger (OpenAPI 3)** | Generación automática de documentación y prueba interactiva de los endpoints REST de cada microservicio. |
| **Postman** | Entorno de pruebas de la API para validar manualmente las peticiones y respuestas JSON de los distintos microservicios (`POST`, `GET`, `PATCH`, `DELETE`). |
| **Docker** | Contenerización de cada microservicio para garantizar despliegues aislados y consistentes entre entornos. |
| **Azure App Service** | Entorno de ejecución en la nube donde se despliegan los contenedores Docker de los microservicios. |
| **Azure Container Registry (ACR)** | Almacenamiento y versionado de las imágenes Docker generadas en los pipelines de CI/CD. |
| **GitHub Actions** | Pipelines de integración y despliegue continuo (CI/CD) para compilar, probar, analizar y desplegar cada microservicio. |
| **AWS API Gateway** | Punto de entrada único al backend que enruta las solicitudes del cliente al microservicio correspondiente (Pagos, Wallet, Recibos, QR, etc.). |
| **AWS Lambda (Node.js)** | Funciones ligeras que actúan como lógica intermedia cuando se requiere preprocesar o adaptar solicitudes antes de enviarlas a los microservicios. |
| **SSL / HTTPS** | Cifrado de la comunicación entre cliente, API Gateway y microservicios para garantizar la seguridad de la información. |

## 6. 🧩 Funcionalidades

---

### 1️⃣ Crear Evento

Permite al organizador crear un nuevo evento institucional.

**Endpoint:** `POST /api/events`
 
---

#### 📦 Estructura de la solicitud

| 🏷️ Campo | 🗃️ Tipo | ⚠️ Restricciones | 📝 Descripción |
|---|---|:---:|---|
| name | String | Obligatorio | Nombre del evento |
| description | String | Opcional | Descripción del evento |
| dateTime | LocalDateTime | Obligatorio | Fecha y hora del evento (yyyy-MM-ddTHH:mm:ss) |
| location | String | Obligatorio | Lugar de realización |
| category | Enum | Obligatorio | ACADEMIC, CULTURAL, SPORTS, WELLNESS |
| type | Enum | Obligatorio | OPEN o WITH_CAPACITY |
| maxCapacity | Integer | Opcional | Cupo máximo (solo para WITH_CAPACITY) |
| organizerId | String | Obligatorio | ID del organizador del evento |
 
---

#### 📦 Estructura de la respuesta

| 🏷️ Campo | 🗃️ Tipo | 📝 Descripción |
|---|---|---|
| id | String | Identificador único del evento (UUID) |
| name | String | Nombre del evento |
| description | String | Descripción del evento |
| dateTime | LocalDateTime | Fecha y hora del evento |
| location | String | Lugar de realización |
| category | Enum | Categoría del evento |
| type | Enum | Tipo: OPEN o WITH_CAPACITY |
| maxCapacity | Integer | Cupo máximo |
| availableSpots | Integer | Cupos disponibles actuales |
| organizerId | String | ID del organizador |
| status | Enum | Estado actual: ACTIVE, CANCELLED, FULL |
| createdAt | LocalDateTime | Fecha de creación |
 
---

#### ✅ Happy Path

**Request:**
POST /api/events
```json
 
{
  "name": "Hackathon ECI 2026",
  "description": "Maratón de programación abierta a todos los estudiantes",
  "dateTime": "2026-06-15T09:00:00",
  "location": "Auditorio principal",
  "category": "ACADEMIC",
  "type": "WITH_CAPACITY",
  "maxCapacity": 50,
  "organizerId": "org-001"
}
```

**Response (201 CREATED):**
```json
{
  "id": "abc123-def456",
  "name": "Hackathon ECI 2026",
  "description": "Maratón de programación abierta a todos los estudiantes",
  "dateTime": "2026-06-15T09:00:00",
  "location": "Auditorio principal",
  "category": "ACADEMIC",
  "type": "WITH_CAPACITY",
  "maxCapacity": 50,
  "availableSpots": 50,
  "organizerId": "org-001",
  "status": "ACTIVE",
  "createdAt": "2026-05-10T08:00:00"
}
```
 
---

#### 📊 Errores manejados

| 🔢 HTTP | ⚠️ Escenario | 💬 Mensaje |
|:---:|:---|:---|
| ![409](https://img.shields.io/badge/409-Conflict-orange?style=flat) | Evento ya existe | `"El evento ya existe"` |
| ![400](https://img.shields.io/badge/400-Bad_Request-red?style=flat) | Datos inválidos | `"Validation failed"` |
 
---

### 2️⃣ Consultar Todos los Eventos

Retorna la lista completa de eventos registrados en el sistema.

**Endpoint:** `GET /api/events`
 
---

#### ✅ Happy Path

**Request:**
```
GET /api/events
```

**Response (200 OK):**
```json
[
  {
    "id": "abc123-def456",
    "name": "Hackathon ECI 2026",
    "category": "ACADEMIC",
    "type": "WITH_CAPACITY",
    "status": "ACTIVE",
    "availableSpots": 48,
    "dateTime": "2026-06-15T09:00:00"
  }
]
```
 
---

#### 📊 Errores manejados

| 🔢 HTTP | ⚠️ Escenario | 💬 Mensaje |
|:---:|:---|:---|
| ![200](https://img.shields.io/badge/200-OK-success?style=flat) | Sin eventos | Lista vacía `[]` |
 
---

### 3️⃣ Consultar Evento por ID

Permite recuperar la información completa de un evento específico.

**Endpoint:** `GET /api/events/{id}`
 
---

#### ✅ Happy Path

**Request:**
```
GET /api/events/abc123-def456
```

**Response (200 OK):**
```json
{
  "id": "abc123-def456",
  "name": "Hackathon ECI 2026",
  "status": "ACTIVE",
  "availableSpots": 48,
  "maxCapacity": 50
}
```
 
---

#### 📊 Errores manejados

| 🔢 HTTP | ⚠️ Escenario | 💬 Mensaje |
|:---:|:---|:---|
| ![404](https://img.shields.io/badge/404-Not_Found-orange?style=flat) | Evento no existe | `"Evento no encontrado por el ID: abc123"` |
 
---

### 4️⃣ Cancelar Evento

Permite al organizador cancelar un evento activo, cambiando su estado a CANCELLED.

**Endpoint:** `PATCH /api/events/{id}/cancel`
 
---

#### ✅ Happy Path

**Request:**
```
PATCH /api/events/abc123-def456/cancel
```

**Response (200 OK):**
```json
{
  "id": "abc123-def456",
  "name": "Hackathon ECI 2026",
  "status": "CANCELLED"
}
```
 
---

#### 📊 Errores manejados

| 🔢 HTTP | ⚠️ Escenario | 💬 Mensaje |
|:---:|:---|:---|
| ![404](https://img.shields.io/badge/404-Not_Found-orange?style=flat) | Evento no existe | `"Evento no encontrado por el ID: abc123"` |
| ![409](https://img.shields.io/badge/409-Conflict-orange?style=flat) | Ya está cancelado | `"El evento ya fue cancelado"` |
 
---

### 5️⃣ Crear RSVP (Inscripción)

Permite a un estudiante confirmar su asistencia a un evento activo con cupo disponible.

**Endpoint:** `POST /api/rsvp`
 
---

#### 📦 Estructura de la solicitud

| 🏷️ Campo | 🗃️ Tipo | ⚠️ Restricciones | 📝 Descripción |
|---|---|:---:|---|
| eventId | String | Obligatorio | ID del evento al que se inscribe |
| studentId | String | Obligatorio | ID del estudiante |
| confirmedAt | LocalDateTime | Obligatorio | Fecha y hora de confirmación |
| status | Enum | Obligatorio | CONFIRMED |
 
---

#### 📦 Estructura de la respuesta

| 🏷️ Campo | 🗃️ Tipo | 📝 Descripción |
|---|---|---|
| id | String | Identificador único del RSVP |
| eventId | String | ID del evento |
| studentId | String | ID del estudiante |
| confirmedAt | LocalDateTime | Fecha y hora de confirmación |
| status | Enum | Estado: CONFIRMED o CANCELLED |
 
---

#### ✅ Happy Path

**Request:**
POST /api/rsvp
```json
 
{
  "eventId": "abc123-def456",
  "studentId": "student-789",
  "confirmedAt": "2026-05-10T10:00:00",
  "status": "CONFIRMED"
}
```

**Response (201 CREATED):**
```json
{
  "id": "rsvp-001",
  "eventId": "abc123-def456",
  "studentId": "student-789",
  "confirmedAt": "2026-05-10T10:00:00",
  "status": "CONFIRMED"
}
```
 
---

#### 📊 Errores manejados

| 🔢 HTTP | ⚠️ Escenario | 💬 Mensaje |
|:---:|:---|:---|
| ![400](https://img.shields.io/badge/400-Bad_Request-red?style=flat) | Sin cupo o cancelado | `"El evento con ID X no está disponible"` |
| ![404](https://img.shields.io/badge/404-Not_Found-orange?style=flat) | Evento no existe | `"Evento no encontrado por el ID: X"` |
 
---

### 6️⃣ Cancelar RSVP

Permite a un estudiante cancelar su inscripción a un evento, liberando un cupo.

**Endpoint:** `PATCH /api/rsvp/{id}/cancel`
 
---

#### ✅ Happy Path

**Request:**
```
PATCH /api/rsvp/rsvp-001/cancel
```

**Response (200 OK):**
```json
{
  "id": "rsvp-001",
  "eventId": "abc123-def456",
  "studentId": "student-789",
  "status": "CANCELLED"
}
```
 
---

#### 📊 Errores manejados

| 🔢 HTTP | ⚠️ Escenario | 💬 Mensaje |
|:---:|:---|:---|
| ![404](https://img.shields.io/badge/404-Not_Found-orange?style=flat) | RSVP no existe | `"RSVP no encontrado"` |
 
---

## 7. 📊 Diagramas

### Diagrama de componentes - Vista General



### Diagrama de componentes - Vista Especifica



### Diagrama de base de datos



### Diagrama de Clases del Dominio 



### Diagrama de Despliegue

---

## 8. ⚠️ Manejo de Errores

El microservicio implementa un `GlobalExceptionHandler` con `@RestControllerAdvice` que centraliza
todas las excepciones y retorna respuestas estandarizadas.

### Global Exception Handler

Se encarga de capturar y manejar todas las excepciones del sistema de forma centralizada.

- ✅ **Centraliza** la captura de excepciones desde todos los controladores
- ✅ **Retorna mensajes consistentes** con el código HTTP apropiado
- ✅ **Asigna códigos HTTP** según la naturaleza del error (400, 404, 409, 500)
- ✅ **Define mensajes descriptivos** que ayudan al desarrollador y al usuario
### Excepciones manejadas

| ⚠️ Excepción | 🔢 HTTP | 💬 Escenario                                   |
|:---|:---:|:-----------------------------------------------|
| `EventNotFoundException` | 404 | El evento solicitado no existe                 |
| `EventAlreadyExistsException` | 409 | Se intenta crear un evento que ya existe       |
| `EventNotAvailableException` | 400 | El evento está cancelado o sin cupo disponible |
| `EventAlreadyCancelledException` | 409 | Se intenta cancelar un evento ya cancelado     |
| `RsvpNotFoundException` | 404 | El RSVP solicitado no existe                   |
| `Exception` | 500 | Error inesperado del servidor                  |

### Beneficios

| 🎯 **Beneficio** | 📋 **Descripción** |
|:---|:---|
| **🎯 Uniformidad** | Todas las respuestas de error tienen el mismo formato |
| **🔧 Mantenibilidad** | Agregar nuevas excepciones no requiere modificar cada controlador |
| **🔒 Seguridad** | Oculta los detalles internos del servidor |
| **📍 Trazabilidad** | Cada error incluye el código HTTP y descripción del fallo |
| **🤝 Integración fluida** | Facilita la comunicación con frontend y herramientas como Postman/Swagger |
 
---

## 9. 🧪 Evidencia de las pruebas y cómo ejecutarlas

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

### Suites de prueba

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

**Total: 102 casos de prueba**

### Cómo ejecutar las pruebas

```bash
# Ejecutar todas las pruebas
mvn test
 
# Ejecutar una suite específica
mvn test -Dtest=CreateEventUseCaseTest
 
# Generar reporte de cobertura JaCoCo
mvn clean verify
 
# Ver reporte HTML
target/site/jacoco/index.html
```

### Evidencia de ejecución

1. **Consola mostrando pruebas ejecutadas exitosamente**
   ///////////////FOTO//////////////////

2. **Reporte JaCoCo con cobertura de código**
   ////////////////FOTO////////////////////


---

## 10. 🗂️ Código de la implementación organizado en las respectivas carpetas


```
eeveevoiding-responsibilities-campus-events/
│
├── src/
│   ├── main/
│   │   ├── java/edu/eci/patricia/DOWS_patricia/
│   │   │   │
│   │   │   ├── application/                              # 🔵 CAPA DE APLICACIÓN
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
│   │   │   ├── domain/                                   # 🟢 CAPA DE DOMINIO
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
│   │   │   │   ├── ports/
│   │   │   │   │   └── in/
│   │   │   │   │       ├── CreateEventPort.java
│   │   │   │   │       ├── CancelEventPort.java
│   │   │   │   │       ├── GetEventByIdPort.java
│   │   │   │   │       ├── GetEventsPort.java
│   │   │   │   │       ├── CreateRsvpPort.java
│   │   │   │   │       └── CancelRsvpPort.java
│   │   │   │   └── valueobjects/
│   │   │   │       ├── EventId.java
│   │   │   │       └── RsvpId.java
│   │   │   │
│   │   │   ├── entrypoints/                              # 🟠 ENTRADA (DRIVING ADAPTERS)
│   │   │   │   ├── advice/
│   │   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │   └── rest/controller/
│   │   │   │       ├── EventController.java
│   │   │   │       └── EventRsvpController.java
│   │   │   │
│   │   │   └── infrastructure/                           # 🟠 INFRAESTRUCTURA
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
│   │   │       └── config/
│   │   │           └── SecurityConfig.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/edu/eci/patricia/DOWS_patricia/
│           ├── application/mapper/
│           ├── application/usecase/
│           ├── domain/model/
│           ├── domain/valueobjects/
│           ├── entrypoints/rest/controller/
│           └── infrastructure/adapters/
│
├── pom.xml
└── README.md
```

---

## 11. 🚀 Ejecución del Proyecto
### 📋 Prerrequisitos

- **Java 21**
- **Maven 3.9+**
- **Docker & Docker Compose** (para MongoDB local)
### 🛠️ Opción 1: Ejecución Local

```bash
# 1. Levantar MongoDB con Docker
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

### ⚙️ Variables de Entorno

| Variable | Valor por defecto | Descripción |
|:---|:---|:---|
| `SPRING_DATA_MONGODB_URI` | `mongodb://localhost:27017/campus-events` | URI de conexión MongoDB |
| `SERVER_PORT` | `8080` | Puerto del servidor |
| `JWT_SECRET` | *(requerida al activar JWT)* | Clave HMAC-SHA256 compartida con auth-service |
 
---

### 🏆 Equipo **eeveevoiding-responsibilities**

![Course](https://img.shields.io/badge/Course-DOSW-orange?style=for-the-badge)
![Year](https://img.shields.io/badge/Year-2026--1-blue?style=for-the-badge)

> 💡 **M09 — Eventos Universitarios** es el microservicio encargado de gestionar el ciclo de vida completo de los eventos institucionales de la ECI, conectando organizadores y estudiantes en una experiencia fluida y confiable.

**🎓 Escuela Colombiana de Ingeniería Julio Garavito**

---

## 13. 🤝 Contribuciones y Metodología
