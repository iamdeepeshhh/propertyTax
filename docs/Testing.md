# Testing Document

This document outlines testing strategies for the application, including functional tests, integration tests, and backup/restore verification.

## Scope
- API endpoints under `/3g` (admin/master) and `/3gSurvey` (survey)
- Public views under `/`
- Batch processing for assessments and numbering
- File upload/download
- Database migrations and data integrity
- Backup and restore tests

## Test Types
- Unit Tests: Services, helpers, utilities (mock repositories). Execute with `mvn test`.
- Integration Tests: Controller + Repository + DB (use Testcontainers/PostgreSQL or a dedicated test DB; config in `application-test.properties`).
- API Contract Tests: Validate request/response schemas for key endpoints.
- End-to-End Smoke: Minimal UI/view checks for Thymeleaf routes.
- Performance: Batch processing execution time for typical ward sizes; large file uploads within limits.
- Security: Session/role enforcement for admin endpoints; authentication scenarios.

## Critical Scenarios
- User Management: signup, authenticate, list users (role ITA), update, delete.
- Master Data CRUD: create/list/update/delete for representative masters (zones, building types, usage types).
- Survey Flow: new registration, details retrieval, edit, delete, attachment uploads.
- Assessment: real-time calculation details; batch process by ward; report generation endpoints return content.
- File Uploads: valid and invalid MIME types; oversize file rejection (>50MB); multi-part handling.
- Migrations: Flyway applies cleanly on empty DB and on existing data; PL/pgSQL functions execute.

## Data Integrity Checks
- Unique constraints honored (e.g., property new number, master codes).
- FK constraints prevent orphaned references.
- Final property/notice numbers padded correctly per ward (per V2/V3 logic).

## Backup/Restore Tests
1. Seed a staging/test DB with representative data.
2. Take a logical backup (`pg_dump -Fc`).
3. Drop and recreate the database.
4. Restore via `pg_restore` and run:
   - Row count comparisons on key tables.
   - API smoke tests (GET lookups, a calculation details call).
   - Batch dry-run test (on a non-production ward).
5. Document duration and any discrepancies.

## Tooling
- JUnit/Jupiter (via spring-boot-starter-test)
- Testcontainers PostgreSQL (recommended)
- Postman/Insomnia collection for manual API testing
- Maven Surefire/Failsafe plugins (defaults from Spring Boot)

## CI Suggestions
- On PR: build + unit tests + integration tests (with ephemeral DB)
- On main: above + package; optionally build Docker image
- Nightly: run backup/restore verification against staging

