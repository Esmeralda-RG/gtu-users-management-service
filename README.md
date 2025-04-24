# Microservicio de Usuarios

## Descripción
Este repositorio contiene el código fuente del microservicio de usuarios, desarrollado en Spring Boot.

## Estructura de Ramas y Flujo de Trabajo
Para mantener un flujo de desarrollo organizado, se siguen las siguientes reglas:

### Ramas Principales
- **main** → Solo debe contener versiones estables y en producción.
- **release** → Rama utilizada para QA y pruebas antes de pasar a `main`.
- **develop** → Rama principal de desarrollo, donde se integran las funcionalidades en curso.

## ¿Qué se hizo en este proyecto?

### 1. **API REST**
- Se implementaron controladores para manejar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) de usuarios.
- Se generó documentación automática de la API utilizando **Swagger**, accesible en `/api-docs`.

### 2. **Autenticación y Autorización**
- Se implementa autenticación basada en **JWT**.
- Se configuraron roles y permisos para controlar el acceso a las funcionalidades:
  - **SuperAdmin**: Acceso completo a todas las operaciones.
  - **Admin**: Acceso a todas las operaciones para el conductor.
  - **Usuario**: Acceso limitado a sus propios datos.

### 3. **Swagger y Documentación**
- Se integró **Swagger UI** para generar documentación interactiva de la API.
- La documentación está disponible en el endpoint `/api-docs`.

### 4. **Estructura del Proyecto**
El proyecto sigue la estructura estándar de Spring Boot:
- **`src/main/java`**: Código fuente principal.
  - **Controladores**: Manejan las solicitudes HTTP.
  - **Servicios**: Contienen la lógica de negocio.
  - **Repositorios**: Interactúan con la base de datos.
  - **Modelos**: Representan las entidades de la base de datos.
- **`src/main/resources`**: Archivos de configuración.
  - `application.properties`: Configuración de la aplicación.
