# Database Design

## Overview
The application uses PostgreSQL with JPA/Hibernate for ORM and Flyway for database migrations. Domain data is grouped into Master data (lookups), Property Survey (new/old property capture), and Assessment/Reporting. Batch processing uses standard Spring Batch tables.

Key characteristics:
- Naming: Entities in `Entity.*` map to tables, DTOs in `DTO.*` map to API payloads.
- Migrations: `src/main/resources/db/migrations` (V1–V3 provided).
- Performance: `hibernate.jdbc.batch_size=1000` for bulk writes.

## Core Domains and Entities (conceptual)
- Property Survey
  - PropertyDetails (property_details): core record; includes ward, survey property numbers, final property number, notice number, status, timestamps.
  - PropertyOldDetails: legacy property data linked by old property number and ward.
  - UnitDetails / UnitBuiltupDetails / UnitOldDetails: unit-level attributes per property.
  - AdditionalInfo: optional property metadata.
- Master Data
  - Wards, Zones, Old Wards
  - Property Classification, Sub-classification
  - Property Usage Type, Sub-usage Type
  - Building Type, Building SubType
  - Owner Type, Owner Category
  - Unit No, Unit Floor No
  - Occupancy, Construction Class, Age Factor
  - Sewerage Type, Water Connection Type
  - Room Type, Remarks
  - Assessment Date, Council Details
- Assessment
  - PropertyRates, ConsolidatedTaxes, Edu/Emp Cess, TaxDepreciation, RV Types, RV Type Categories
  - Assessment Results and logs
- System/Logs
  - PropertyDeletionLog

Note: Exact table/column names derive from each `Entity.*` class; add Flyway DDL to reflect any new/changed entities.

## Relationships (high-level)
- PropertyDetails 1—N UnitDetails (a property contains many units)
- PropertyDetails 1—N UnitBuiltupDetails
- PropertyDetails 1—N UnitOldDetails
- PropertyDetails N—1 Ward, Zone, Old Ward
- PropertyDetails N—1 OwnerType, OwnerCategory
- PropertyDetails N—1 SewerageType, WaterConnectionType
- PropertyDetails N—1 BuildingType, BuildingSubType, Occupancy, ConstructionClass
- PropertyDetails N—1 Classification/SubClassification, Usage/SubUsage, RoomType
- Assessment entities reference PropertyDetails and master-tax tables for calculation inputs.

## Keys, Indexing, and Constraints (recommendations)
- Primary Keys: surrogate `BIGSERIAL`/`UUID` or natural keys as per entity.
- Unique Keys: enforce uniqueness for master lookups (e.g., codes/names) and for `property_details.pd_newpropertyno_vc`.
- Foreign Keys: from property to all referenced master tables; ON UPDATE RESTRICT, ON DELETE RESTRICT/SET NULL depending on semantics.
- Indexes:
  - `property_details(pd_ward_i, pd_finalpropno_vc)` to accelerate search by ward and property number.
  - `property_details(pd_surypropno_vc)` for survey lookup.
  - FKs commonly used in filters (e.g., classification, usage, building type).

## Flyway Migrations Present
- V1__Create_Spring_Batch_Tables.sql: standard Spring Batch tables and sequences.
- V2__Assign_FinalPropertyNumber.sql: function `assign_final_property_number(ward_number integer)` updates property final numbers and sub-property components.
- V3__Assign_FinalPropertyNumberAndNoticeNumber.sql: extends V2 to also set `pd_noticeno_vc` like `WA{ward}_{seq}`.

Usage: Batch/job or admin triggers iterate properties within a ward in survey-number order, assign final property and notice numbers, and update timestamps.

## Data Lifecycle
- Create: Survey endpoints create new `property_details` and related unit rows.
- Update: Edits permitted prior to finalization; logs captured in `PropertyDeletionLog` on deletes.
- Finalize: Batch or function assigns `pd_finalpropno_vc` and `pd_noticeno_vc` per ward sequence.
- Archive/Retention: Define archival policy for old properties and deletion logs.

## Sample DDL Patterns (illustrative)
```sql
-- PropertyDetails (illustrative, align with Entity fields)
CREATE TABLE property_details (
  id BIGSERIAL PRIMARY KEY,
  pd_newpropertyno_vc VARCHAR(64) UNIQUE NOT NULL,
  pd_ward_i INTEGER NOT NULL,
  pd_surypropno_vc VARCHAR(64),
  pd_suryprop1_vc VARCHAR(64),
  pd_suryprop2_vc INTEGER,
  pd_finalpropno_vc VARCHAR(32),
  pd_noticeno_vc VARCHAR(64),
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  updated_at TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX idx_prop_ward_final ON property_details(pd_ward_i, pd_finalpropno_vc);
```

## Data Validation (high-level)
- Enforce non-null on mandatory master references.
- Use numeric ranges for wards and sequences; pad in application or database function as in V2/V3.
- Validate file uploads (MIME, size limit 50MB, virus scanning in production).

## Backups and Recovery
See `docs/Backup.md` for PostgreSQL backup/restore strategies and verification procedures.

