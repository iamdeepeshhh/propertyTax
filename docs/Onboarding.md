# Developer Onboarding

## Repository Tour
- `src/main/java/com/GAssociatesWeb/GAssociates`:
  - `Application.java`: Spring Boot entry point and `ModelMapper` bean (STRICT).
  - `Controller/`: `MainController`, `MasterWebController`, `PropertySurveyController` (views + REST endpoints).
  - `Service/`: business logic segmented by domains (master data, survey, assessment/reporting, batch).
  - `Repository/`: Spring Data repositories.
  - `DTO/` and `Entity/`: data transfer objects and JPA entities grouped by domain.
- `src/main/resources`:
  - `application.properties`: env-based DB config, Flyway, session, logging.
  - `db/migrations`: Flyway SQL migrations (Spring Batch tables, property/notice assignment functions).
  - `templates/` (expected): Thymeleaf views referenced by controllers.
- `Dockerfile`, `docker-compose.yml`: containerization.

## First Run
1) Install JDK 17 and Maven.
2) Set `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` for a local or remote PostgreSQL instance.
3) `mvn clean package && mvn spring-boot:run`
4) Browse `http://localhost:8080`.

## Coding Conventions
- Java 17, Spring Boot 3.2.x.
- Prefer constructor injection for services.
- Keep controllers thin; place logic in services.
- Use DTOs for API payloads; map with `ModelMapper`.
- Add Flyway migrations for any schema change (never change schema without a migration).
- Pagination and filtering for list endpoints are encouraged when adding new APIs.

## Adding a New Master Entity
1) Create JPA Entity and table migration (Flyway `V{n}__*.sql`).
2) Create DTO and Repository.
3) Implement Service with validation and mapping.
4) Expose Controller endpoints for CRUD.
5) Add unit/integration tests where possible.

## Working with Files and Reports
- File uploads are handled via multipart endpoints; coordinate storage with `ImageUtils`.
- Reports (PDF/Excel) generated via OpenPDF/PDFBox/POI; reuse/report generator services.

## Security
- Session-based checks exist; plan migration to Spring Security for robust authN/Z.
- Use BCrypt for password storage; protect state-changing endpoints.

## Local Docker
- Build jar then `docker compose up --build arvi-app` (or another service).
- Each service uses different DB names and host ports; avoid pointing to production databases.

## Troubleshooting
- Flyway errors: verify DB permissions and clean up failed migrations.
- Missing templates: ensure Thymeleaf templates exist for referenced view names.
- Large uploads: adjust `spring.servlet.multipart.max-*` in properties.

## Useful Commands
- Build: `mvn clean package`
- Run: `mvn spring-boot:run`
- Test: `mvn test`
- Format (IDE): enable Lombok annotation processing in your IDE.

