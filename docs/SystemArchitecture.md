# System Architecture

## High-Level View
- Client/UI: Browser-based UI using Thymeleaf templates served by Spring MVC.
- API Layer: Spring MVC controllers expose REST endpoints under `/3g` (admin/master) and `/3gSurvey` (survey flows) and public view routes under `/`.
- Service Layer: Business logic per domain (master data, survey, assessment, batch, reporting).
- Data Access: Spring Data JPA repositories with PostgreSQL.
- Batch/Jobs: Spring Batch for ward-based processing and reporting.
- Documents: OpenPDF/PDFBox for PDF, Apache POI for Excel import/export.

## Component Diagram (textual)
- Web Controllers
  - `MainController` → serves public views (special notices, hearing notice, tax bill, RTCC, etc.).
  - `MasterWebController` → admin/master CRUD APIs, assessment, reporting, uploads.
  - `PropertySurveyController` → survey login, property create/edit/search, old property flows, form lookups.
- Services
  - Master services (zones, wards, owner types, building types, etc.).
  - Survey services (property management, old details, number generation).
  - Assessment services (real-time, batch, report generators, result logs).
  - File/utility services (image utils, Excel helper/service).
- Persistence
  - Repositories per entity (e.g., PropertyDetails_Repository, PropertyDeletionLog_Repository).
  - Entities grouped in `Entity.MasterWebEntity.*` and `Entity.PropertySurveyEntity.*`.
- Integration
  - Flyway migrations for schema and PL/pgSQL functions.
  - Spring Batch tables/state.

## Request Flow (example)
1. Client sends POST `/3g/addPropertyType` with JSON.
2. `MasterWebController` validates and delegates to `PropClassification_MasterService`.
3. Service maps DTO → Entity (via `ModelMapper`), persists via repository.
4. Returns DTO response; controller shapes HTTP status (201 created).

## Batch Processing Flow
1. Admin triggers `/3g/processBatch/{wardNo}`.
2. Batch service selects properties for the ward ordered by survey number.
3. PL/pgSQL function assigns `pd_finalpropno_vc` and `pd_noticeno_vc`.
4. Assessment batch reports generated and exposed at `/3g/batchAssessmentReport/{wardNo}` and `/3g/batchCalculationReport/{wardNo}`.

## Deployment Topology
- Single Spring Boot service listening on `server.port` (default 8080).
- External PostgreSQL database configured by env vars.
- Containerized via Dockerfile; multiple environment services in `docker-compose.yml` for different DBs/ports.

## Cross-Cutting Concerns
- Security: Session-based checks exist; recommended to migrate to Spring Security with stateless tokens for APIs and role-based method security.
- Logging: SLF4J + Log4j core; add structured logging in production.
- Configuration: Externalize DB settings via env vars; consider Spring Profiles (`application-{env}.properties`).
- Validation: Use `spring-boot-starter-validation` annotations on DTOs/entities and centralized exception handling.
- Performance: Batch writes via Hibernate batching; add indexes (see DatabaseDesign.md) and cache hot lookups if needed.

## Error Handling
- Standard `ResponseEntity<?>` used; recommend `@ControllerAdvice` to unify error structure (code, message, details).

## Extensibility
- Adding a master: add Entity/DTO/Repository/Service/Controller + Flyway migration.
- Adding assessment rules: extend services and report generators under `AssessmentModule_*` packages.
- Adding storage: abstract `ImageUtils` to support S3/minio and configure via env vars.

## End-to-End Flow and Services

This section details the application flow with the specific services that participate at each step, matching your intended business flow from user setup → master data → property registration → updates/deletes → rate configuration → assessment (realtime/batch) → reporting.

1) User and Roles
- Controller: `/3g` MasterWebController (login/signup pages), `/3gSurvey` PropertySurveyController (survey login)
- Services:
  - `UserAccessService` (Service.UserAccess_MasterService) — signup, authenticate, manage users.

2) Master Data Setup (prerequisites for property registration and assessment)
- Property Details masters:
  - `Zone_MasterService`
  - `OldWard_MasterService`
  - `Ward_MasterService` (used via MasterWebController addWards/getAllWards)
  - `OwnerType_MasterService`, `OwnerCategory_MasterService`
  - `BuildStatus_MasterService`
  - `WaterConnection_MasterService`, `Sewerage_MasterService`
- Property Types and Usage:
  - `PropClassification_MasterService` (Property Types)
  - `PropSubClassification_MasterService` (Property Subtypes)
  - `PropertyUsageType_MasterService` (Usage Types)
  - `PropertySubUsageType_MasterService` (Usage Subtypes)
  - `BuildingType_MasterService`, `BuildingSubType_MasterService`
  - `RoomType_MasterService`, `Remarks_MasterService`
- Assessment Dates and Age Factor:
  - `AssessmentDate_MasterService` (current/first/last assessment dates)
  - `AgeFactor_MasterService` (construction age factor bands)
- Construction / Occupancy:
  - `ConstClass_MasterService` (Construction class)
  - `Occupancy_MasterService`

3) Property Registration (New and Old)
- Controller: `/3gSurvey` PropertySurveyController
- Services (CompletePropertySurveyService package):
  - `PropertyManagement_Service` (or Impl) — orchestrates creation and updates of complete property
  - `PropertyDetails_Service`
  - `UnitDetails_Service`
  - `UnitBuiltUp_Service`
  - `AdditionalInfo_Service`
  - `PropertyOldDetails_Service` (for old records)
  - `UniqueIdGenerator` (for new property numbers)
- Flows:
  - New property: POST `/3gSurvey/submitNewPropertyDetails` (multipart: JSON + images)
  - View details: GET `/3gSurvey/detailsComplete/{pdNewPropNo}` or `/3gSurvey/details/{pdSuryPropNo}`
  - Update property: PATCH `/3gSurvey/submitUpdatedPropertyDetails` (multipart)
  - Delete property: POST `/3gSurvey/deleteNewProperty`
  - Old property flows: submit/update/delete and join with new via reference

4) Rate Configuration (after council shares rates)
- MasterWebServices/AssessmentModule:
  - `PropertyRates_MasterService`
  - `ConsolidatedTaxes_MasterService`
  - `EduCessAndEmpCess_MasterService`
  - `RVTypes_MasterService`, `RVTypeCategory_MasterService`
  - `TaxDepreciation_MasterService`
- Supporting default/seeding:
  - `DefaultTypesAndTaxes` (helper for defaults)
- Persist rates and consolidated taxes before assessments.

5) Realtime Assessment
- Services:
  - `TaxAssessment_MasterService` (TaxAssessmentRealtime) — compute per-unit/property values, ratable values, consolidated taxes
  - `PreLoadCache`, `ResultLogsCache` — caching
  - Reporting generators (optional on-demand PDF):
    - `AssessmentReportPdfGenerator`
    - `CalculationSheetGenerator_MasterService`
- Controller endpoints (MasterWebController):
  - GET `/3g/realtimeCalculatedDetails/{newPropertyNumber}` — returns computed details (DTO: AssessmentResultsDto)
  - GET `/3g/calculationSheet/{newPropertyNumber}` — renders calculation sheet (PDF/HTML)

6) Batch Assessment
- Spring Batch configuration/services:
  - `JobConfiguration`, `PropertyDetailsReader`, `TaxAssessmentBatchProcessor`, `PropertyDetailsWriter`
  - `BatchCompletionListener`, `PropertyDetailsSkipPolicy`, `CustomSkipListener`
  - `BatchReportGenerator_MasterService` (Batch reports), `CalculationSheetGenerator_MasterService`
- Controller endpoints:
  - POST `/3g/processBatch/{wardNo}` — triggers batch
  - GET `/3g/batchAssessmentReport/{wardNo}` — batch assessment report
  - GET `/3g/batchCalculationReport/{wardNo}` — batch calculation sheets

7) Dynamic Consolidated Taxes (Display)
- Persisted masters:
  - `consolidated_taxes_master` (Entity + `TaxTypeEnum`) — master list with taxKeyL, names, activity
- Recommended display config (for dynamic, ordered, aggregate rows):
  - `report_tax_config` (label/localLabel, sequence, visible, contributesToTotal, rounding, optional filters, effective dates)
  - Single-entity approach with `componentTaxKeys` (JSON list of taxKeyL) to aggregate multiple components into one visible line (e.g., Property Tax = sum of building+land; Education Tax = sum of residential+commercial).
- Services to integrate:
  - `TaxBreakdownService` — map computed columns (pt_*) → taxKeyL, produce Map<Long, BigDecimal>
  - `ReportTaxConfigService` — load active configs for property context
  - `ReportTaxAggregator` — aggregate per config row (single taxKeyL or sum of componentTaxKeys), round, order by sequence, compute grand total
- Integration points:
  - Realtime: after compute, attach dynamic tax lines + total to `AssessmentResultsDto`
  - Reports (single + batch): build breakdown from native query row; aggregate via config; iterate lines in sequence when rendering (replace hard-coded rows)

8) After Assessment & Notices
- Services:
  - `SpecialNotice_MasterService`, `RegisterObjection_MasterService`
- Views (MainController): special notices, hearing notices, order sheet, objection receipt, tax bill, RTCC.

## End-to-End Flow Summary
- Users & Roles → set up via `UserAccessService`
- Masters → create via MasterWebServices (zones/wards/owners/types/usage/building/room/sewerage/water/assessment dates/age factor/etc.)
- Property Registration → new/old via PropertySurveyController and CompletePropertySurvey services
- Edit/Delete → both new and old properties editable/deletable
- Rates & Taxes → configure council rates and consolidated taxes (PropertyRates, ConsolidatedTaxes, Edu/Emp Cess, Depreciation)
- Assessment → realtime via `TaxAssessment_MasterService`; batch via Spring Batch pipeline
- Reports → dynamic consolidated taxes rendered via `report_tax_config` and aggregator; single/batch calculation sheets and results PDFs
