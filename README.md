# Backend For Frontend (BFF) - Proyecto Donaton

Este repositorio contiene el **Backend For Frontend (BFF)** de la plataforma Donaton, desarrollado con **Spring Boot 3.x**. Su función principal es actuar como un orquestador centralizado que gestiona la seguridad y optimiza la comunicación entre el frontend y los microservicios de backend.

## 🏗️ Arquitectura y Patrones de Diseño
Siguiendo las pautas de la **Evaluación Parcial N°2**, este componente implementa patrones arquitectónicos clave para asegurar una solución escalable:

*   **🌐 BFF Pattern**: Proporciona un punto de entrada único para el frontend en Next.js, encargándose de filtrar y agregar los datos provenientes de los microservicios (`ms-donaciones`, `ms-logistica`, `ms-necesidades`).
*   **🔐 Security Filter Chain**: Implementación de seguridad centralizada mediante un `JwtFilter` que valida los tokens de acceso en cada petición.
*   **📂 Repository Pattern**: Desacoplamiento de la lógica de negocio del acceso a datos, garantizando que los componentes sean eficientes y mantenibles.

## 🛠️ Tecnologías Utilizadas
*   **☕ Java 17**: Lenguaje base para asegurar compatibilidad con las últimas librerías de Spring.
*   **🍃 Spring Boot 3.x**: Framework principal para el desarrollo de servicios ágiles y de alto rendimiento.
*   **🔑 Spring Security & JWT**: Implementación de seguridad robusta para la autenticación y autorización basada en tokens.
*   **🔨 Maven**: Utilizado para la gestión de dependencias y como arquetipo base para asegurar la coherencia del proyecto.

## 📂 Estructura de Controladores
El BFF expone endpoints estratégicos para facilitar el consumo de datos desde la interfaz de usuario:
*   `AuthController`: Gestión de inicio de sesión y generación de tokens JWT.
*   `DonacionController`: Punto de enlace orquestado hacia el microservicio de donaciones.
*   `EnvioController`: Gestión de peticiones hacia el microservicio de logística.
*   `NecesidadController`: Interfaz centralizada para la gestión de requerimientos críticos.

## 🚀 Instalación y Ejecución

1.  **📋 Requisitos**:
    *   Java 17 instalado.
    *   Maven 3.8+.

2.  **⚙️ Configuración**:
    Revisa el archivo `src/main/resources/application.properties` para configurar las URLs de los microservicios y el puerto del servidor:
    
```properties
    server.port=8080
    api.donaciones.url=http://localhost:8081
    api.logistica.url=http://localhost:8082
    api.necesidades.url=http://localhost:8083
    ```

3.  **▶️ Ejecución**:
    ```bash
    mvn spring-boot:run
    ```

## 🧪 Pruebas y Calidad
*   **✅ Pruebas Unitarias**: El proyecto incluye validaciones unitarias para asegurar la correcta lógica de seguridad y la redirección de los controladores.
*   **🧹 Clean Code**: Se aplica una estructura limpia y ordenada, demostrando un alto nivel de calidad en la construcción del backend.

