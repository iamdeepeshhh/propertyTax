# Functional Requirements Specification (FRS)

## 1. Product Overview
3GAssociates is a property tax and survey management platform for municipalities. It manages master data, captures new/old property details, performs tax assessments (real-time and batch), generates notices/reports, and supports user management with role-based access.

## 2. Stakeholders and Roles
- Municipal Administrators (e.g., role `ITA`): full access to master data, user management, batch processing, reporting.
- Survey Officers (field users): create/edit property survey records, upload media, view master lookups.
- Back-office Staff: review survey data, generate notices, run calculations, export/import data.
- Citizens (limited views): view notices, tax bills, RTCC via public endpoints.

Roles surfaced in the codebase (via `/profiles`): ITA, ITL, IT, ACTL, ACL, DETL, DEL. Authorization enforcement is primarily controller-level checks (e.g., session check in some methods) and should be extended as needed.

## 3. Functional Scope
### 3.1 User Access and Profiles
- Sign up and authenticate users.
- Maintain user profiles and roles.
- List/search users; update/delete users (admin-only).

### 3.2 Master Data Management
Maintain lookup/reference data used for survey and assessment flows:
- Wards, Zones, Old Wards
- Property classifications and sub-classifications
- Property usage types and sub-usage types
- Building types and subtypes (with mapping to property classification)
- Owner types and owner categories
- Unit numbers and unit floor numbers
- Occupancy, Building/Construction class, Age factor
- Sewerage types, Water connection types
- Room types, Remarks
- Council details
- Assessment dates

Operations include create, list, update, delete, and filtered retrieval.

### 3.3 Property Survey
- Create a new property record with details and uploads (images/CAD).
- Edit and delete survey property records.
- Capture old property details; view by number/reference; delete by old property and ward.
- Lookup endpoints to populate forms with master data.
- Search by parameters (e.g., ward, property numbers).

### 3.4 Assessment and Reporting
- Real-time tax assessment and calculation details for a property.
- Batch processing by ward: assignment of final property numbers and notice numbers; generate batch assessment and calculation reports.
- Generate per-property calculation sheet and assessment result PDFs.
- Export/import Excel; download templates; view deletion logs.

### 3.5 Notices and Public Views
- Render special notices, hearing notices, order sheets, objection receipts.
- Tax bill and RTCC views by property/ward identifiers.
- Submit objections and list special notices and result logs (by ward).

## 4. Business Rules and Constraints
- Final property numbers are generated sequentially per ward with zero padding (Flyway functions `V2`, `V3`).
- Survey property numbers may include merged (`+`) or sub-properties (`/`); sub-properties increment per main property.
- Some operations require role checks (e.g., `getAllUsers` enforces role `ITA`).
- File upload size limits: 50MB per request/file (see `spring.servlet.multipart.*`).
- Database schema management via Flyway; destructive operations disabled by default should be enforced in production.

## 5. Non-Functional Requirements
- Security: enforce authentication/authorization for admin and survey operations; avoid storing raw passwords (improve `authenticateUser` per comment in code).
- Performance: batch size for JPA operations is configured (`hibernate.jdbc.batch_size=1000`).
- Availability: service runs statelessly; session timeouts are 20 minutes by default.
- Observability: SLF4J logging; consider adding Actuator for health/metrics.

## 6. Success Criteria
- Authorized users can complete survey -> assessment -> notice/report workflows end-to-end.
- Master data remains consistent and traceable across CRUD operations.
- Batch processing reliably assigns final property and notice numbers per ward and produces reports.
- Public endpoints render expected notices/bills/RTCC views.

## 7. Dependencies
- PostgreSQL database availability and credentials.
- Storage for uploaded files (currently handled via `ImageUtils` and database/repo layer; ensure capacity).
- PDF/Excel libraries for reports.

## 8. Assumptions and Open Items
- Role and session management will be hardened (token-based auth or Spring Security can be added).
- Full validation rules per master entity to be finalized with domain experts.
- Exact UI templates exist under Thymeleaf resources (not included here); mapping names are present in controllers.

