Backend For Frontend (BFF) - Proyecto DonatonEste repositorio contiene el Backend For Frontend (BFF) de la plataforma Donaton, desarrollado con Spring Boot 3.x. Su función principal es actuar como un orquestador que centraliza la seguridad y optimiza la comunicación entre el frontend y los distintos microservicios de backend. 

🛠️ Tecnologías y ArquetiposJava 17: Lenguaje base para asegurar compatibilidad con las últimas librerías de Spring.  Spring Boot 3.x: Framework principal para el desarrollo de microservicios ágiles y eficientes.  Spring Security & JWT: Implementación de seguridad robusta para la autenticación y autorización basada en tokens.  Maven: Utilizado para la gestión de dependencias y como arquetipo base para asegurar la coherencia técnica.  

🔐 Seguridad y Patrones Arquitectónicos Siguiendo las pautas de la Evaluación Parcial N°2, el BFF implementa los siguientes patrones:  BFF Pattern: Proporciona un punto de entrada único para el frontend en Next.js, filtrando y agregando datos de los microservicios (ms-donaciones, ms-logistica, ms-necesidades) para mejorar el rendimiento.  Security Filter Chain: Implementación de un JwtFilter para validar los tokens en cada petición, garantizando acceso protegido.  Repository Pattern: Desacoplamiento de la lógica de negocio del acceso a datos, asegurando un código limpio. 

📂 Estructura de Controladores El BFF expone endpoints estratégicos para el consumo del frontend:  AuthController: Gestión de inicio de sesión y generación de tokens JWT.  DonacionController: Punto de enlace hacia el microservicio de donaciones.  EnvioController: Orquestación de peticiones hacia el microservicio de logística.  NecesidadController: Interfaz para la gestión de requerimientos críticos.  

🚀 Instalación y EjecuciónRequisitos: Java 17 instalado.  Maven 3.8+.  
Configuración: Configura las URLs en src/main/resources/application.properties:
server.port= 8080

api.donaciones.url= http://localhost:8081

api.logistica.url= http://localhost:8082

api.necesidades.url= http://localhost:8083

Ejecución:
mvn spring-boot:run

🧪 Pruebas y Calidad

Pruebas Unitarias: Incluye tests unitarios para validar la seguridad y la redirección de controladores.  Clean Code: Código organizado bajo estándares de la industria para garantizar escalabilidad.
