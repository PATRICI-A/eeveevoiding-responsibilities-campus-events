# Campus Events — Guía Docker

---


## Configuración inicial

### 1. Crear el archivo `.env`

Renombra el archivo `.env.example` a `.env` en la raíz del proyecto:

```bash
cp .env.example .env
```

El archivo ya tiene los valores por defecto listos para desarrollo. No necesitas cambiar nada para correrlo localmente.

---

## Levantar el proyecto

### 2. Construir y correr los contenedores

```bash
docker compose up --build
```

Este comando se encarga de todo:
- Compila la aplicación con Maven
- Descarga la imagen de MongoDB
- Espera a que MongoDB esté listo antes de iniciar Spring Boot
- Levanta ambos contenedores

> La primera vez tarda más porque descarga las imágenes base. Las siguientes veces es más rápido.

### 3. (Opcional) Correr en segundo plano

Si quieres recuperar la terminal:

```bash
docker compose up --build -d
```

Para ver los logs después:

```bash
docker compose logs -f
```

---

## Verificar que funciona

### 4. Comprobar el estado de los contenedores

```bash
docker compose ps
```

Deberías ver dos contenedores con estado `running`:
- `campus-events-mongo`
- `campus-events-app`


## Detener el proyecto

```bash
docker compose down
```

Si además quieres borrar los datos de MongoDB:

```bash
docker compose down -v
```