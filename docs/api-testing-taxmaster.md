API Testing – Tax Master (v1)

Purpose: Endpoints to manage tax-related masters used in assessment: depreciation rates, property rates, RV types, consolidated taxes, and cess rates. Includes minimal sample payloads aligned to DTOs (I=string integer, Vc=varchar string, Fl=float numeric).

Base
- Base path: `/3g`
- Auth: Typically open in controller, but use session when needed.

Depreciation Rates
- POST `/3g/depreciationRates`
  - Why: Bulk create/update depreciation rates.
  - Body: Map payload accepted (service expects a structured map). Example shape:
    { "rates": [ { "id": 1, "constructionClassVc": "A", "minAgeI": "0", "maxAgeI": "5", "depreciationPercentageI": "5" } ] }
  - Status: 200 OK
- GET `/3g/getDepreciationRates`
  - Why: List all depreciation rates.
  - Status: 200 OK
- POST `/3g/deleteDepreciationRate?id=1`
  - Why: Delete a depreciation rate by id.
  - Status: 204 No Content

Property Rates
- GET `/3g/getAllPropertyRates`
  - Why: List per-construction/zone property rates.
  - Status: 200 OK
- GET `/3g/getPropertyRateById/{id}`
  - Why: Fetch a property rate by id.
  - Status: 200 OK | 404
- POST `/3g/createPropertyRate`
  - Why: Create one or more property rates (Map payload handled in service).
  - Sample (PropertyRates_MasterDto): { "constructionTypeVc": "RCC", "taxRateZoneI": "1", "rateI": "12" }
  - Status: 200 OK (returns list)
- POST `/3g/updatePropertyRate/{id}`
  - Why: Update a property rate.
  - Body: PropertyRates_MasterDto
  - Status: 200 OK
- POST `/3g/deletePropertyRate?id=1`
  - Why: Delete a property rate by id.
  - Status: 204 No Content

RV Types (Ratable Value Types)
- GET `/3g/getRVTypeCategories`
  - Why: List RV type categories used to group RV types.
  - Response JSON (RVTypeCategory_MasterDto):
    [ { "categoryId": 10, "categoryNameVc": "RESIDENTIAL", "categoryNameLocalVc": "निवासी", "categoryDescriptionVc": "Residential category" } ]
  - Status: 200 OK | 204 No Content
- Notes (Applied Taxes mapping):
  - Applied taxes for an RV type are linked through `appliedTaxesVc` (comma-separated) and `taxKeysL` (array of tax keys) on RVTypes_MasterDto.
  - No separate controller endpoints were found for RvTypesAppliedTaxes; mapping is managed via the RV type create/update payloads.
- POST `/3g/addRVType`
  - Why: Create an RV type.
  - Body (RVTypes_MasterDto):
    { "ryTypeId": 1, "typeNameVc": "RESIDENTIAL", "rateFl": "1.00", "appliedTaxesVc": "TAX_A,TAX_B", "descriptionVc": "Base RV", "descriptionUpVc": "Base RV upper", "categoryId": 10, "taxKeysL": [1,2,3] }
  - Status: 200 OK
- GET `/3g/getAllRVTypes`
  - Why: List RV types with applied taxes.
  - Status: 200 OK
- GET `/3g/getRVTypeById/{id}`
  - Why: Fetch RV type by id.
  - Status: 200 OK
- POST `/3g/updateRVType/{id}`
  - Why: Update RV type.
  - Body: RVTypes_MasterDto
  - Status: 200 OK
- POST `/3g/deleteRVType?id=1`
  - Why: Delete RV type.
  - Status: 204 No Content

Consolidated Taxes
- POST `/3g/addConsolidatedTax`
  - Why: Create a consolidated tax/charge.
  - Body (ConsolidatedTaxes_MasterDto):
    { "id": 1, "taxNameVc": "PropertyTax", "taxRateFl": "10.0", "applicableonVc": "RV", "isActiveBl": true, "taxType": "TAX", "descriptionVc": "Primary tax", "taxKeyL": 100, "positionI": 1 }
  - Status: 200 OK
- GET `/3g/getAllConsolidatedTaxes`
  - Why: List all consolidated taxes.
  - Status: 200 OK
- GET `/3g/getConsolidatedTaxById/{id}`
  - Why: Fetch a consolidated tax by id.
  - Status: 200 OK
- POST `/3g/updateConsolidatedTax/{id}`
  - Why: Update a consolidated tax entry.
  - Body: ConsolidatedTaxes_MasterDto
  - Status: 200 OK
- POST `/3g/deleteConsolidatedTax?id=1`
  - Why: Delete a consolidated tax.
  - Status: 204 No Content

Cess Rates (Education/Employment/EGC)
- GET `/3g/getAllCessRates`
  - Why: List cess rates.
  - Status: 200 OK
- GET `/3g/getCessRateById/{id}`
  - Why: Fetch cess rate by id.
  - Status: 200 OK
- POST `/3g/createCessRate`
  - Why: Create cess rate.
  - Body (EduCessAndEmpCess_MasterDto):
    { "id": 1, "minTaxableValueFl": "0", "maxTaxableValueFl": "10000", "residentialRateFl": "1.00", "commercialRateFl": "2.00", "egcRateFl": "0.50" }
  - Status: 200 OK
- POST `/3g/updateCessRate/{id}`
  - Why: Update cess rate.
  - Body: EduCessAndEmpCess_MasterDto
  - Status: 200 OK
- POST `/3g/deleteCessRate?id=1`
  - Why: Delete cess rate.
  - Status: 204 No Content

Notes
- Use numeric-only strings where fields end with `I` and free-form strings for `Vc`. Floats (`Fl`) are numeric.
- These masters are referenced by the assessment and calculation endpoints.
