<div align="center">

# PATRIC.IA — Microservicio de Eventos Universitarios (M09)

### *"Gestionando momentos, conectando la comunidad."*

---

### Stack Tecnológico

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-47A248?style=for-the-badge&logo=mongodb&logoColor=white)

### Infraestructura & Calidad

![Docker](https://img.shields.io/badge/Docker-Container-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Azure](https://img.shields.io/badge/Azure-Cloud-0078D4?style=for-the-badge&logo=microsoft-azure&logoColor=white)

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
| **Spring Boot 3.3.0** | Framework principal para APIs REST. |
| **Spring Web** | Exposición de endpoints REST (controladores HTTP). |
| **Spring Security + JWT** | Protección de endpoints mediante autenticación basada en tokens. |
| **Spring Data MongoDB** | Integración con base de datos NoSQL mediante patrón Repository. |
| **MongoDB (Atlas / Local)** | Base de datos NoSQL principal. |
| **Apache Maven** | Gestión de dependencias y automatización de builds. |
| **Lombok 1.18.38** | Reducción de boilerplate (`@Getter`, `@Builder`, etc.). |
| **MapStruct 1.5.5** | Mapeo automático entre entidades y DTOs. |
| **JUnit 5 & Mockito** | Framework de pruebas unitarias y simulación de dependencias. |
| **JaCoCo 0.8.13** | Análisis de cobertura de pruebas integrado al pipeline. |
| **SpringDoc OpenAPI 2.6.0** | Swagger UI para documentación de API. |
| **Docker** | Contenedorización para despliegues consistentes. |

---

## 3. Descripción del Microservicio

El microservicio de **Eventos Universitarios (M09)** tiene como objetivo gestionar el ciclo de vida completo de los eventos académicos, culturales, deportivos y de bienestar que se realizan en el campus. Adicionalmente, permite la gestión de RSVP (Inscripciones), control de aforos y confirmación de asistencias para los estudiantes.

Puerto: `8080` (por defecto). Integrado con **MongoDB** como base de datos principal.

---

## 4. Cómo Funciona

### Arquitectura Hexagonal (Ports & Adapters)

El servicio sigue los principios de la Arquitectura Limpia, dividiendo la aplicación en tres capas principales:

- **Dominio:** Contiene las entidades principales (`Event`, `EventRsvp`), los `ValueObjects` y las interfaces (`Ports`).
- **Aplicación:** Casos de uso (`CreateEventUseCase`, `GetEventsUseCase`, `CreateRsvpUseCase`, etc.) y sus correspondientes `Mappers`.
- **Infraestructura & Entrypoints:** Implementaciones de adaptadores a base de datos (MongoDB) y los controladores REST.

### Patrones de Diseño

| Patrón | Descripción |
|---|---|
| **Ports & Adapters** | Múltiples puertos de entrada (Casos de uso) y puertos de salida (Repositorios). |
| **Repository** | Acceso a MongoDB abstraído a través de interfaces de dominio. |
| **DTO (Data Transfer Object)** | Separación entre la representación del dato y la solicitud REST. |
| **Mapper** | Conversión entre Entidad, Dominio y DTO gestionado por MapStruct. |
| **Global Exception Handler** | `@RestControllerAdvice` para capturar y estandarizar los errores HTTP. |

---

## 5. Diagrama de Datos

> 📷 **[Insert Image: Diagrama_Base_Datos.png]**

El modelo de datos está orientado a documentos en MongoDB e incluye colecciones para Eventos e Inscripciones (RSVP).

---

## 6. Diagrama de Clases

> 📷 **[Insert Image: Diagrama_Clases.png]**

Resumen del dominio:
- **`Event`**: Contiene `name`, `description`, `dateTime`, `location`, `category`, `type`, `maxCapacity`, etc.
- **`EventRsvp`**: Vincula un estudiante (`studentId`) con un evento (`eventId`), registrando fecha de confirmación.

---

## 7. Diagrama de Componentes

> 📷 **[Insert Image: Diagrama_Componentes_General.png]**

> 📷 **[Insert Image: Diagrama_Componentes_Especifico.png]**

| Componente | Tipo | Interfaz |
|---|---|---|
| `EventController` | REST Controller | Múltiples endpoints en `/api/events` |
| `EventRsvpController` | REST Controller | Múltiples endpoints en `/api/rsvp` |
| Casos de Uso | Application Service | Implementan los puertos de entrada |
| Adaptadores Mongo | Infrastructure | Implementan los puertos de repositorios |

---

## 8. Funcionalidades Principales

<div align="center">

| ID | Funcionalidad | Descripción |
|---|---|---|
| F01 | **Gestión de eventos** | Crea y visualiza eventos con fechas, capacidad, y tipo. |
| F02 | **Cancelar eventos** | Un administrador puede cancelar un evento pasándolo al estado `CANCELLED`. |
| F03 | **Asistencia a un evento (RSVP)** | Estudiantes apartan cupo (si es limitado) y confirman asistencia. |
| F04 | **Manejo de Errores Globales** | Respuestas HTTP consistentes para excepciones de negocio. |

</div>

---

## 9. Endpoints

### Resumen

| Método | Endpoint | Funcionalidad |
|---|---|---|
| `GET` | `/api/events` | Consultar todos los eventos |
| `GET` | `/api/events/{id}` | Consultar evento por ID |
| `POST` | `/api/events` | Crear un nuevo evento |
| `PATCH` | `/api/events/{id}/cancel` | Cancelar un evento |
| `POST` | `/api/rsvp` | Crear RSVP (Inscripción a evento) |
| `PATCH` | `/api/rsvp/{id}/cancel` | Cancelar inscripción |

*(Los detalles de petición y respuesta, como códigos HTTP (200, 201, 400, 404, 409) son manejados mediante el GlobalExceptionHandler).*

---

## 10. Colas de Mensajería

Actualmente, este servicio funciona de forma síncrona mediante REST para el ciclo principal de eventos. Integraciones futuras pueden emplear mensajería para notificaciones de asistencia.

---

## 11. Evidencia de Pruebas

### Clases de prueba implementadas

El proyecto cuenta con un conjunto amplio de 102 casos de prueba unitarios para la lógica de los casos de uso, repositorios, y mapeos:

```
src/test/java/edu/eci/patricia/DOWS_patricia/
├── application/usecase/ (Ej: CreateEventUseCaseTest, CreateRsvpUseCaseTest)
├── domain/model/ (Ej: EventTest, EventRsvpTest)
├── entrypoints/rest/controller/
└── infrastructure/adapters/
```

### Cómo ejecutar las pruebas

```bash
# Ejecutar pruebas unitarias
./mvnw test

# Todas las pruebas + reporte JaCoCo
./mvnw verify
```

> 📷 **[Insert Image: Ejecucion_Pruebas_Exitosa.png]**

---

## 12. Evidencia de Cobertura

Se utiliza JaCoCo para la generación de reportes con un mínimo de cobertura requerido en el pipeline.

> 📷 **[Insert Image: Reporte_Cobertura_JaCoCo.png]**

---

## 13. Cómo Ejecutar

### Prerrequisitos

- Java 21
- Maven 3.9+
- Docker & Docker Compose

### Opción 1: Ejecución Local

```bash
# 1. Levantar MongoDB con Docker
docker run --name campus-events-db -e MONGO_INITDB_DATABASE=campus-events -p 27017:27017 -d mongo:7

# 2. Ejecutar sin Docker (Maven)
./mvnw spring-boot:run
```

**URL:** `http://localhost:8080`
**Swagger UI:** `http://localhost:8080/swagger-ui.html`

### Opción 2: Docker Compose

```bash
docker compose up --build
```

### Variables de Entorno

| Variable | Descripción |
|---|---|
| `SPRING_DATA_MONGODB_URI` | `mongodb://localhost:27017/campus-events` |
| `SERVER_PORT` | `8080` |
| `JWT_SECRET` | Clave HMAC-SHA256 para validación de tokens |

---

## 14. Evidencia CI/CD

El repositorio implementa estrategias de Integración Continua a través de GitHub Actions para el empaquetado, pruebas, generación de imágenes en Docker y despliegue hacia servicios de nube.

> 📷 **[Insert Image: Pipeline_CI.png]**

> 📷 **[Insert Image: Pipeline_CD.png]**

> 📷 **[Insert Image: Diagrama_Despliegue.png]**

---

## 15. Link Swagger

| Ambiente | URL |
|---|---|
| Local (Maven) | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

---

## 16. Estructura del Código

```
src/main/java/edu/eci/patricia/DOWS_patricia/
├── application/               # CAPA DE APLICACIÓN
│   ├── dto/                   # Requests / Responses
│   ├── mapper/                # MapStruct Interfaces
│   └── usecase/               # Implementación lógica de negocio
├── domain/                    # CAPA DE DOMINIO
│   ├── exceptions/            # Excepciones de negocio personalizadas
│   ├── model/                 # Entidades del Dominio (Event, EventRsvp)
│   ├── ports/                 # Puertos In/Out
│   └── valueobjects/          # Value Objects
├── entrypoints/               # DRIVING ADAPTERS
│   ├── advice/                # GlobalExceptionHandler
│   └── rest/controller/       # Endpoints REST
└── infrastructure/            # DRIVEN ADAPTERS
    ├── adapters/              # Conexión MongoDB / Repositorios
    └── config/                # Spring Security
```

---

## 17. Código Documentado

La lógica de negocio implementada en las diferentes capas se encuentra probada y modularizada para fácil comprensión, y los endpoints están debidamente documentados para su exportación a través de OpenAPI (Swagger).

---

## 18. Conexiones Externas

| Módulo | Tipo | Dirección | Detalle |
|---|---|---|---|
| **M01 — Autenticación** | JWT | Cliente → M09 | Verificación del JWT en cada petición restringida por roles (Spring Security). |

---

## 19. Pipeline de Desarrollo

Para el desarrollo se maneja una estrategia Git Flow donde todo código fluye a través de Pull Requests hacia la rama `develop`, asegurando su verificación a través del pipeline.

---

## 20. Pipeline de Producción

Los merges hacia la rama `main` despliegan automáticamente nuevas versiones hacia los entornos administrados mediante Docker y contenedores persistentes en la nube (ej. Azure).

---

## 21. Dockerizado

El proyecto incluye un `Dockerfile` y un `docker-compose.yml` que empaqueta la aplicación Java junto con sus dependencias base, orquestando de manera sencilla la inicialización tanto del servicio REST como del gestor documental MongoDB.

---

## 22. Versionamiento

Mantenemos un versionamiento semántico utilizando ramas de funcionalidad (`feature/*`) para nuevas integraciones. Todos los aportes pasan por CI/CD.
