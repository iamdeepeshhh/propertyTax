# Setup Guide

## Prerequisites
- Java Development Kit (JDK) 17+
- Maven 3.9+
- PostgreSQL 13+ (local or remote)
- Docker (optional)

## Environment Configuration
The app uses env vars to configure the database (defaults in parentheses):
- `DB_HOST` (localhost)
- `DB_NAME` (trialarvi)
- `DB_USER` (postgres)
- `DB_PASSWORD` (root)

Optionally adjust:
- `server.port` (default 8080) in `application.properties`

Windows PowerShell example:
```
$env:DB_HOST = 'localhost'
$env:DB_NAME = 'trialarvi'
$env:DB_USER = 'postgres'
$env:DB_PASSWORD = 'root'
```

## Database Preparation
1) Create database and user with required privileges.
2) Ensure network access between app and DB host.
3) Flyway runs automatically at startup and applies migrations from `classpath:db/migrations`.

## Build
- Clean and package:
```
mvn clean package
```

## Run (Local)
- Using Spring Boot Maven plugin:
```
mvn spring-boot:run
```
- Or run the built jar:
```
java -jar target/3GAssociates-0.0.1-SNAPSHOT.jar
```

App will be available at `http://localhost:8080`.

## Run (Docker)
1) Build jar: `mvn clean package`
2) Use docker-compose to run one of the provided services:
```
docker compose up --build arvi-app
```
Other services map to different DB names and host ports (see `docker-compose.yml`).

## Testing
- Unit/integration tests (if present) can be executed via:
```
mvn test
```
- If tests are not configured, build will still succeed without tests:
```
mvn clean package -DskipTests
```

## Common Issues
- Authentication: `authenticateUser` currently uses a simple check; consider setting up secured credentials.
- DB migrations: Ensure the DB user has permissions to create functions and tables.
- File uploads: Requests capped at 50MB (adjust `spring.servlet.multipart.*`).

