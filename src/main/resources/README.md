# Campus Events — Guía Docker


 
---

## Configuración inicial

### 1. Crear el archivo `.env`

Copia el archivo de ejemplo y completa los valores:

```bash
cp .env.example .env
```

El `.env` debe tener esto:

```env
SPRING_SECURITY_USER_NAME=admin
SPRING_SECURITY_USER_PASSWORD=admin
```
 
---

## Comandos principales

### 2. Construir la imagen

```bash
docker build -t campus-events .
```

> Ejecuta este comando cada vez que hagas cambios en el código.

### 3. Correr el contenedor

```bash
docker run -p 8080:8080 --env-file .env campus-events
```

### 4. Verificar que funciona

Abre el navegador y entra a:

```
http://localhost:8080/swagger-ui/index.html
```

Si ves la interfaz de Swagger, todo está funcionando correctamente ✅
 
---
