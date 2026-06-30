Silent Campaign Manager

REST API desarrollada con Spring Boot para la gestión de campañas submarinas, submarinos, patrullas y contactos navales.

Objetivo

Este proyecto tiene como finalidad demostrar conocimientos de desarrollo backend moderno utilizando Java y Spring Boot mediante la implementación de una API REST siguiendo buenas prácticas de arquitectura y desarrollo profesional.

Tecnologías
Java 21
Spring Boot 3.5
Spring Data JPA
PostgreSQL
Maven
Lombok
OpenAPI / Swagger
Docker (próximamente)
Spring Security + JWT (próximamente)

Ejecución local
Para iniciar la base de datos PostgreSQL y ejecutar la aplicación localmente:

  1. Ejecuta `docker compose up -d`.
  2. Verifica que el contenedor `scm-postgres` esté en marcha.
  3. Inicia la aplicación.

La aplicación usa estas variables si no se proporcionan otras:

  - `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/silent_campaign_db`
  - `SPRING_DATASOURCE_USERNAME=admin`
  - `SPRING_DATASOURCE_PASSWORD=admin`

Funcionalidades previstas
Gestión de campañas
Crear campaña
Consultar campañas
Actualizar campaña
Eliminar campaña
Gestión de submarinos
Registro de submarinos
Asociación a campañas
Gestión de patrullas
Registro de patrullas
Historial de operaciones
Gestión de contactos
Registro de contactos detectados
Clasificación de objetivos
Arquitectura

El proyecto sigue una arquitectura por capas:

Controller
Service
Repository
Entity
DTO
Mapper
Estado del proyecto

Fase inicial de desarrollo.

Autor

Jorge Martínez Juan
