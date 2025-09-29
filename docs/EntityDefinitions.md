# Entity Definitions and Usage

This document explains the purpose of the main entities in the system, how they are used across controllers/services, and how they relate to each other during property registration, assessment (realtime/batch), and reporting.

Note: Table names mirror the domain; actual physical names may vary slightly depending on JPA mappings.

## Core Property Domain

- property_details
  - Purpose: Root record for a property (address/owner/infra/areas/keys to masters). Holds survey and final property numbers, connection details, and key totals (plot/built‑up/carpet/assessable/exemptions).
  - Created/Updated by: PropertySurveyController via PropertyManagement_Service (and PropertyDetails_Service); UniqueIdGenerator assigns pd_newpropertyno_vc.
  - Used by: Realtime/Batch assessment as the input property; referenced by unit_details, property_rvalues, proposed_rvalues, property_taxdetails. Rendered in calculation sheets and assessment reports.

- unit_details
  - Purpose: Per‑unit slice of a property: unit number, floor, occupancy, usage type/subtype, per‑unit areas, remarks, contacts (tenant/phone/email), and age/class details.
  - Created/Updated by: PropertyManagement_Service and UnitDetails_Service.
  - Used by: Assessment engine to compute per‑unit RValues; UI tabs for unit editing; reporting (unit breakdown on calc sheet).

- unit_builtup_details
  - Purpose: Granular room/area rows for each unit; holds dimensions, inner/outer measure type, deduction %, carpet/assessable area, exemptions, legal/illegal splits.
  - Created/Updated by: UnitBuiltUp_Service; driven from the Unit Area popup on the edit form.
  - Used by: Unit‑level area totals and assessment inputs.

## Assessment (Computed) Entities

- property_rvalues
  - Purpose: Computed values at property+unit level: yearly rent, ALV, depreciation %/amount, rate per sqm, lenting/considered values, per‑unit assessment results.
  - Produced by: TaxAssessment_MasterService (Realtime and Batch processor) from unit_details + unit_builtup_details + master rates/age factors.
  - Used by: Reporting (per‑unit rows in calculation sheets) and to derive proposed/properties totals.

- proposed_rvalues
  - Purpose: Per‑property aggregate ratable values, including splits (residential vs non‑residential and sub‑categories) used in reports.
  - Produced by: TaxAssessment_MasterService during assessment, summing per‑unit/property results.
  - Used by: Calculation sheets and assessment summaries.

- property_taxdetails
  - Purpose: Consolidated taxes by component (columns): property tax, education cess (residential/commercial/total), user charges, EGC, fire, light, tree, clean, final totals.
  - Produced by: TaxAssessment_MasterService using master tax catalogs/rates.
  - Used by: Reporting (tax sections in calculation sheet and assessment results PDFs). Also the source for dynamic, sequenced rendering via the ReportTaxConfig aggregator.

## After‑Assessment/Audit

- property_deletion_log
  - Purpose: Audit log capturing details and snapshots (base64 images/house plans) when a property record is deleted.
  - Created by: PropertySurveyController on delete.
  - Used by: Admin for traceability and recovery.

- special_notice / register_objection (module DTOs/entities)
  - Purpose: Notices and objections after assessment.
  - Managed by: SpecialNotice_MasterService, RegisterObjection_MasterService.
  - Used by: MainController views (special/hearing/objection receipt) and master web flows.

## Master Catalogs (Complete)

- ward_master, zone_master, oldward_master
  - Purpose: Geography. Wards/zones used for numbering, filtering, scope in batch; old wards preserve historical mapping.
  - Services: Ward_MasterService, Zone_MasterService, OldWard_MasterService.

- owner_type_master, owner_category_master
  - Purpose: Ownership metadata used in property profiles and may drive tax applicability.
  - Services: OwnerType_MasterService, OwnerCategory_MasterService.

- build_status_master
  - Purpose: Construction/building status (e.g., completed, under construction) impacting assessment.
  - Service: BuildStatus_MasterService.

- water_connection_master, sewerage_master
  - Purpose: Infrastructure connections for property (water types, sewerage types/status).
  - Services: WaterConnection_MasterService, Sewerage_MasterService.

- prop_classification_master (Property Types), prop_subclassification_master (Property Subtypes)
  - Purpose: High-level classification of properties and fine-grained subtypes.
  - Services: PropClassification_MasterService, PropSubClassification_MasterService.

- prop_usage_type_master (Usage Types), prop_sub_usage_type_master (Usage Subtypes)
  - Purpose: Usage catalog (residential/commercial/etc.) and sub-usage.
  - Services: PropertyUsageType_MasterService, PropertySubUsageType_MasterService.

- building_type_master, building_subtype_master
  - Purpose: Building structure catalog per classification.
  - Services: BuildingType_MasterService, BuildingSubType_MasterService.

- unit_no_master, unit_floor_no_master
  - Purpose: Allowed unit numbers and floor numbers for standardized entry.
  - Services: UnitNo_MasterService, UnitFloorNo_MasterService.

- unit_usage_type_master, unit_usage_subtype_master
  - Purpose: Allowed unit-level usage types and subtypes aligned with property usage.
  - Services: UnitUsageType_MasterService, UnitUsageSubType_MasterService.

- construction_class_master, occupancy_master
  - Purpose: Classes (A/B/C...) and occupancy statuses that influence rate selection.
  - Services: ConstClass_MasterService, Occupancy_MasterService.

- room_type_master, remarks_master
  - Purpose: Room/space types used in built-up rows; standard remarks.
  - Services: RoomType_MasterService, Remarks_MasterService.

- assessmentdate_master
  - Purpose: Current/last/first assessment dates used to stamp and control assessment cycles.
  - Service: AssessmentDate_MasterService.

- age_factor_master
  - Purpose: Age factor bands (min/max age → factor, labels) used to compute construction age factor.
  - Service: AgeFactor_MasterService.

- council_details
  - Purpose: Council branding/headers, signatures, seals used in notices/reports.
  - Service: CouncilDetails_MasterService.

## Tax/Rates Catalogs (Master Values for Assessment)

- consolidated_taxes_master
  - Purpose: Canonical list of taxes (property tax, edu cess residential/commercial, user charges, EGC, etc.) with `TaxTypeEnum` and stable `taxKeyL` used to link computed values to display.
  - Service: ConsolidatedTaxes_MasterService.
  - Usage: Assessment to decide which components to compute; reporting to label/dynamically display consolidated taxes.

- property_rates_master
  - Purpose: Base rates and tariff structures used in rate/area computations.
  - Service: PropertyRates_MasterService.

- edu_cess_emp_cess_master
  - Purpose: Education cess and employee cess configurations.
  - Service: EduCessAndEmpCess_MasterService.

- rv_types, rv_type_category
  - Purpose: Ratable value type definition and categories.
  - Services: RVTypes_MasterService, RVTypeCategory_MasterService.

- rv_types_applied_taxes
  - Purpose: Mapping table defining which consolidated taxes apply to which RV types/categories.
  - Service: RvTypesAppliedTaxes_MasterService.

- tax_depreciation_master
  - Purpose: Depreciation tables used in assessment math.
  - Service: TaxDepreciation_MasterService.

- council_details
  - Purpose: Council metadata and branding/artifacts (used in report headers/footers and templates).
  - Service: CouncilDetails_MasterService.

## Batch Metadata (Spring Batch)

- BATCH_JOB_* and BATCH_STEP_* tables
  - Purpose: Standard Spring Batch schema for job/step instance, execution, parameters, and contexts.
  - Usage: Persist batch runs of assessment and batch report generation.

## How Entities Work Together (Lifecycle)

1) Masters are defined first via MasterWebController endpoints → MasterWebServices persist lookups and tax/rate catalogs.
2) Property registration creates `property_details` → adds `unit_details` → adds `unit_builtup_details`. Old property records maintained in parallel and can be attached by reference.
3) When rates/taxes are ready, assessment services compute `property_rvalues` (per unit), aggregate to `proposed_rvalues` (per property), and populate `property_taxdetails` (consolidated taxes per property).
4) Reports (calculation sheet and assessment result PDFs) query these computed entities together with property/unit data. Dynamic tax display uses `consolidated_taxes_master` + a display config (optional `report_tax_config`) to render taxes by `taxKeyL` in the desired sequence.
5) Batch assessment uses Spring Batch tables for process lineage; results land in the same assessment entities as realtime.
