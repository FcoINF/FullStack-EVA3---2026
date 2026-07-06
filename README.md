# DSY1103-RedSaludPatag-nica
Proyecto Semestral EP3 — DSY1103 — RedSaludPatagónica

# RedSaludPatagónica — DSY1103 Desarrollo FullStack 1

## Descripción
RedSaludPatagónica es una organización privada sin fines de lucro que gestiona una red de postas
rurales y centros de salud familiar (CESFAM) en territorios aislados de las provincias de Chiloé y
Palena. Opera 8 postas rurales en localidades de difícil acceso como Melinka, Raúl Marín
Balmaceda, Caleta Tortel y Puerto Gala, más un CESFAM en Chaitén y un hospital de baja
complejidad en Futaleufú.
Las postas son atendidas por 1 o 2 TENS (Técnicos en Enfermería) que dependen médicamente de
médicos remotos en Chaitén. El sistema de telemedicina está siendo implementado, y requiere una
plataforma que coordine las consultas remotas, el stock de medicamentos en cada posta, el traslado
de pacientes cuando es necesario (en avioneta o lancha) y el registro de las fichas clínicas de una
población rural dispersa.

El proyecto busca resolver la dificultad de acceso a la atención médica en comunidades rurales dispersas,
especialmente para adultos mayores y personas sin RUT, mediante un sistema de **microservicios en Spring Boot** que permite:

- Registro alternativo de pacientes (nombre, dirección, fecha de nacimiento).
- Gestión de consultas médicas presenciales y remotas.
- Administración de recetas y stock de medicamentos.
- Organización de programas comunitarios de salud.
- Comunicación entre servicios mediante **API Gateway**.
- Centralización de rutas con **API Gateway** y documentación con **Swagger/OpenAPI**.


## Equipo
| Nombre | GitHub |
|--------|--------|
| Raúl Ferrini | @Potasio230 |
| Francisco Molina | @FcoINF |


##  Microservicios Implementados
| # | Microservicio    | Puerto | Descripción |
|---|------------------|--------|-------------|
| 1 | ms-profesionales | 8081   | Gestión de médicos y TENS. |
| 2 | ms-pacientes     | 8082   | Registro de pacientes, incluso sin RUT. |
| 3 | ms-recetas       | 8083   | Emisión de recetas médicas derivadas de consultas. |
| 4 | ms-consultas     | 8084   | Registro de consultas médicas presenciales y remotas. |
| 5 | ms-programas     | 8085   | Administración de programas comunitarios de salud. |
| 6 | ms-farmacia      | 8086   | Control de stock y vencimiento de medicamentos. |

## 🌐 API Gateway
El **API Gateway** funciona como punto único de entrada al sistema. A continuación se muestran
las rutas configuradas:

| Microservicio   | Ruta Gateway                        | Puerto |
|-----------------|-------------------------------------|--------|
| Profesionales   | `/redsalud/v1/profesionales/**`     | 8081   |
| Pacientes       | `/redsalud/v1/pacientes/**`         | 8082   |
| Recetas         | `/redsalud/v1/recetas/**`           | 8083   |
| Consultas       | `/redsalud/v1/consultas/**`         | 8084   |
| Programas       | `/redsalud/v1/programas/**`         | 8085   |
| Farmacia        | `/redsalud/v1/farmacia/**`          | 8086   |

---

## 📑 Documentación Swagger
Cada microservicio expone su documentación en Swagger UI:
- Profesionales → `http://localhost:8081/swagger-ui.html`
- Pacientes → `http://localhost:8082/swagger-ui.html`
- Recetas → `http://localhost:8083/swagger-ui.html`
- Consultas → `http://localhost:8084/swagger-ui.html`
- Programas → `http://localhost:8085/swagger-ui.html`
- Farmacia → `http://localhost:8086/swagger-ui.html`



##  Tecnologías Utilizadas
- **Java 17 / Spring Boot 3.x**
- **Spring Data JPA + Hibernate**
- **PostgreSQL / H2**
- **API Gateway** para enrutamiento centralizado
- **Swagger/OpenAPI** para documentación de APIs
- **Docker** para contenerización y despliegue local
- **Render** para despliegue remoto
- **GitHub + ClickUp** para control de versiones y gestión de tareas
- **Postman** para pruebas de endpoints

## Cómo Ejecutar el Proyecto
1. Clonar el repositorio: `git clone [URL]`
2. Configurar la base de datos en `application.properties`
3. Ejecutar cada microservicio: `./mvnw spring-boot:run`
4. Acceder a la documentación Swagger en: `http://localhost:{puerto}/swagger-ui.html`

## Estado del Proyecto
🔄 Finalizado — EP3 2025

