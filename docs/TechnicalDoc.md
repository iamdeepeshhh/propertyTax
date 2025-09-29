# Technical Documentation

## Architecture Overview
- Layered Spring Boot application:
  - Controllers: Handle REST + view endpoints (`/3g`, `/3gSurvey`, `/`).
  - Services: Business logic for master data, survey, assessments, reporting.
  - Repositories: Spring Data JPA to PostgreSQL.
  - DTOs/Entities: DTOs in `DTO.*` map to entities in `Entity.*` via `ModelMapper` (STRICT strategy).
  - Views: Thymeleaf templates returned by controllers (e.g., `3GMasterWebLogin`, `3GUserManagement`, notice pages).

Key entry point: `com.GAssociatesWeb.GAssociates.Application` with `@SpringBootApplication` and `@EnableWebMvc`.

## Data and Persistence
- Database: PostgreSQL.
- ORM: JPA/Hibernate. Batch size tuned via `spring.jpa.properties.hibernate.jdbc.batch_size=1000`.
- Migrations: Flyway (`src/main/resources/db/migrations`):
  - `V1__Create_Spring_Batch_Tables.sql`: Spring Batch tables and sequences.
  - `V2__Assign_FinalPropertyNumber.sql`: PL/pgSQL function assigning final property numbers by ward.
  - `V3__Assign_FinalPropertyNumberAndNoticeNumber.sql`: Extends V2 to also assign notice numbers.
- Notable table: `property_details` referenced in PL/pgSQL; entities span master and survey domains.

## Batch Processing
- Spring Batch configured; controllers trigger batch/report actions per ward (e.g., `/3g/processBatch/{wardNo}`).
- Batch state persisted in standard `BATCH_*` tables.

## Reporting and Documents
- PDF generation: OpenPDF and PDFBox.
- Excel import/export: Apache POI; an upload endpoint and a template download endpoint exist.
- Assessment and calculation sheet generators are under `Service.MasterWebServices.AssessmentModule_*` packages.

## File Handling
- Multipart uploads supported for CAD and property images (`/3g/uploadCadImage`, `/3g/uploadPropertyImage`).
- Utility: `Service.ImageUtils` (implementation storage medium determined there; typically DB or filesystem).
- Limits: `spring.servlet.multipart.max-file-size` and `max-request-size` set to 50MB.

## Security
- Session-based checks used in some endpoints (e.g., `getAllUsers` requires session role `ITA`).
- `authenticateUser` comment warns to improve for security; consider migrating to Spring Security with password hashing (BCrypt), CSRF protection, and role annotations.

## Logging and Observability
- SLF4J used in controllers/services.
- Default Spring Boot logging; `log4j-core` is included explicitly.
- Consider adding `spring-boot-starter-actuator` for health checks and metrics.

## Design Patterns and Practices
- DTO pattern for decoupling API payloads from JPA entities.
- Service/Repository layering for separation of concerns.
- Strategy-like usage of services across master modules.
- Model Mapping centralized via a configured `ModelMapper` bean (STRICT matching).
- Flyway for migration/versioned schema management.

## Package Structure (high-level)
- `Controller`: `MainController`, `MasterWebController`, `PropertySurveyController`.
- `Service`: master modules (e.g., BuildingType, PropertyRates), survey services, assessment/reporting, batch utilities.
- `Repository`: Spring Data repositories per domain object.
- `DTO`/`Entity`: modularized per domain (MasterWeb, PropertySurvey, etc.).

## Configuration
- `application.properties`: DB config via env vars (`DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`), Flyway locations, servlet session timeout.
- Dockerfile: minimal JRE base (OpenJDK 17 Alpine), copies built jar and runs.
- docker-compose: multiple services mapping to different DB names and exposed ports.

## Building Blocks and Extensibility
- Add a master entity: create DTO + Entity + Repository + Service + Controller endpoints; add Flyway migration for schema.
- Add a survey field: update DTO/Entity + mappers + service logic; ensure relevant lookups are exposed.
- Add an assessment rule: extend the `AssessmentModule_*` services and recalculation/report generators.

## Known Gaps / TODOs
- Harden authentication/authorization via Spring Security.
- Centralize exception handling via `@ControllerAdvice` with consistent error responses.
- Add integration tests for core flows; add contract tests for API endpoints.
- Consider moving file storage to object storage (S3/minio) and persisting URLs in DB.

