# CompuStore - Users Service

Microservicio de autenticación y emisión de JWT (ADMIN/CLIENT).
- Spring Boot 3.3.2, Java 17
- JJWT 0.12.5
- MySQL (compustore_users)
- Swagger (Bearer)

## Endpoints
- POST `/api/users/register`
- POST `/api/users/login`
- GET  `/api/users/profile` (JWT requerido)

## Run
1. Configurar `application.properties`
2. `mvn clean package`
3. `java -jar target/users-*.jar`
