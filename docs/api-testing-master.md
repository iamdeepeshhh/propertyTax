API Testing – Master Endpoints (v1)

Purpose: Master-data endpoints feed dropdowns and lookups used across the survey and assessment flows. This document lists the endpoints, a short “why” description, sample JSON (from DTOs/entities), and expected HTTP statuses. Note: all property identifiers (e.g., pdNewpropertynoVc) are strings containing numeric-only values.

Base
- Base path: `/3g` (MasterWebController)
- Auth: Most master endpoints are public; session not strictly required. Use `/3g/profiles` to retrieve allowed signup profiles.

Profiles
- Endpoint: GET `/3g/profiles`
- Why: Returns the predeclared allowed user profiles for signup/security checks.
- Sample JSON:
  ["ITA", "ITL", "IT", "ACTL", "ACL", "DETL", "DEL"]
- Status: 200 OK

Zones & Wards
- Endpoint: GET `/3g/getAllZones`
- Why: Lists zones for property addressing.
- Sample JSON (Zone_MasterDto):
  [ { "zoneNo": 1, "startZoneNo": 1 } ]
- Status: 200 OK

- Endpoint: GET `/3g/getAllOldWards`
- Why: Lists legacy/old wards for cross-reference.
- Sample JSON (OldWard_MasterDto):
  [ { "oldwardno": 1, "countNo": "001" } ]
- Status: 200 OK

- Endpoint: GET `/3g/getAllWards`
- Why: Lists current wards.
- Sample JSON (Ward_MasterEntity):
  [ { "wardNo": 10, "countNo": "010" } ]
- Status: 200 OK

Owner & Build Status
- Endpoint: GET `/3g/getOwnerType`
- Why: Owner types master for property ownership metadata.
- Sample JSON (OwnerType_MasterDto):
  [ { "ownertypeId": 1, "ownerType": "OWNER", "ownerTypeMarathi": "मालक" } ]
- Status: 200 OK

- Endpoint: GET `/3g/ownerCategories`
- Why: Owner category master (e.g., General, SC/ST, etc.).
- Sample JSON (OwnerCategory_MasterDto):
  [ { "id": 1, "ownerCategory": "GENERAL", "ownerCategoryMarathi": "सामान्य" } ]
- Status: 200 OK

- Endpoint: GET `/3g/buildstatuses`
- Why: Building construction status master.
- Sample JSON (BuildStatus_MasterDto):
  [ { "id": 1, "buildStatus": "COMPLETED" } ]
- Status: 200 OK

Property Types & Usage
- Endpoint: GET `/3g/propertytypes`
- Why: Property classification (high level types).
- Sample JSON (PropClassification_MasterDto):
  [ { "pcmId": 1, "propertyTypeName": "Residential", "localPropertyTypeName": "निवासी" } ]
- Status: 200 OK

- Endpoint: GET `/3g/propertySubtypes/{classificationId}`
- Why: Sub-classifications by classification id.
- Sample JSON (PropSubClassification_MasterDto):
  [ { "propertySubClassificationId": 101, "propertyTypeId": 1, "propertySubtypeName": "Apartment", "localPropertySubtypeName": "अपार्टमेंट" } ]
- Status: 200 OK

- Endpoint: GET `/3g/propertyusagetypes`
- Why: Usage types under a property subtype.
- Sample JSON (PropUsageType_MasterDto):
  [ { "propertyUsageTypeId": 1, "propertySubTypeId": 101, "usageTypeName": "Self-Occupied", "localUsagetypeName": "स्व-वापर" } ]
- Status: 200 OK

- Endpoint: GET `/3g/usageTypes/{id}`
- Why: Returns usage types for a given subtype id.
- Sample JSON: same shape as PropUsageType_MasterDto.
- Status: 200 OK

- Endpoint: GET `/3g/getSubUsageTypes`
- Why: Lists all property usage subtypes; combine with `/3g/usageTypes/{id}` for filtered usage types.
- Sample JSON (PropSubUsageType_MasterDto):
  [ { "id": 11, "usageId": 1, "usageTypeEng": "Commercial-Shop", "usageTypeLocal": "व्यावसायिक-शॉप", "taxType": "TAX_A" } ]
- Status: 200 OK

Building Types
- Endpoint: GET `/3g/getBuildingTypesByPropertyClassification/{propertyClassificationId}`
- Why: Building types allowed for a classification.
- Sample JSON (BuildingType_MasterDto):
  [ { "btBuildingTypeId": 1, "btBuildingtypeengVc": "RCC", "btBuildingtypellVc": "आरसीसी" } ]
- Status: 200 OK

- Endpoint: GET `/3g/getBuildingSubtypes/{id}`
- Why: Sub-types under a building type.
- Sample JSON (BuildingSubType_MasterDto):
  [ { "buildingsubtypeid": 10, "buildingtypeid": 1, "bstBuildingsubtypeengVc": "RCC-G+1" } ]
- Status: 200 OK

Utilities: Water, Sewerage, Unit
- Endpoint: GET `/3g/getSewerageTypes`
- Why: Sewerage connection master list.
- Sample JSON (Sewerage_MasterDto):
  [ { "id": 1, "sewerage": "YES" } ]
- Status: 200 OK

- Endpoint: GET `/3g/waterConnections`
- Why: Water connection types master list.
- Sample JSON (WaterConnection_MasterDto):
  [ { "waterConnection": "Municipal", "waterConnectionMarathi": "महापालिका" } ]
- Status: 200 OK

- Endpoint: GET `/3g/getUnitFloorNos`
- Why: Unit floor number master.
- Sample JSON (UnitFloorNo_MasterDto):
  [ { "id": 1, "unitfloorno": "G" } ]
- Status: 200 OK

- Endpoint: GET `/3g/unitNumbers`
- Why: Unit number master.
- Sample JSON (UnitNo_MasterDto):
  [ { "id": 1, "unitno": "1" } ]
- Status: 200 OK

- Endpoint: GET `/3g/getUnitUsageByPropUsageId/{propUsageId}`
- Why: Unit usage types by property usage id.
- Sample JSON (UnitUsageType_MasterDto):
  [ { "uum_usageid_i": 1, "uum_usagetypeeng_vc": "RESIDENTIAL", "uum_usagetypell_vc": "निवासी", "pum_usageid_i": 1 } ]
- Status: 200 OK

- Endpoint: GET `/3g/getUnitUsageSub/{unitUsageId}`
- Why: Unit usage sub-types by unit usage id.
- Sample JSON (UnitUsageSubType_MasterDto):
  [ { "usm_usagesubid_i": 101, "uum_usageclassid_i": 1, "usm_usagetypeeng_vc": "SHOP", "usm_usagetypell_vc": "दुकान" } ]
- Status: 200 OK

Other Masters
- Endpoint: GET `/3g/constructionClassMasters`
- Why: Construction class master (used for depreciation/assessment).
- Sample JSON (ConstructionClass_MasterDto):
  [ { "id": 1, "constructionClass": "A" } ]
- Status: 200 OK

- Endpoint: GET `/3g/occupancyMasters`
- Why: Occupancy master (owner/tenant/other).
- Sample JSON (Occupancy_MasterDto):
  [ { "occupancy_id": 1, "occupancy": "OWNER", "occupancy_marathi": "मालक" } ]
- Status: 200 OK

- Endpoint: GET `/3g/getAllAssessmentDates`
- Why: Assessment dates used in calculations and reports.
- Sample JSON (AssessmentDate_MasterDto):
  [ { "assessmentId": 1, "firstAssessmentDate": "2010-04-01", "lastAssessmentDate": "2015-03-31", "currentAssessmentDate": "2020-04-01" } ]
- Status: 200 OK

- Endpoint: GET `/3g/roomTypes`
- Why: Room type master (used in unit built-up details).
- Sample JSON (RoomType_MasterDto):
  [ { "id": 1, "roomType": "ROOM", "roomTypeMarathi": "खोली", "roomSelected": 0 } ]
- Status: 200 OK

- Endpoint: GET `/3g/getAllRemarks`
- Why: Predefined remarks used during survey/validation.
- Sample JSON (Remarks_MasterDto):
  [ { "id": 1, "remark": "Verified" } ]
- Status: 200 OK

Notes
- Property numbers are numeric-only strings (e.g., "100123"). Do not include letters when testing ids in string fields.
- Usage values are provided in English and Marathi across DTOs (e.g., `usageTypeName` and `localUsagetypeName`, or `uum_usagetypeeng_vc` and `uum_usagetypell_vc`). Include both when asserting payloads.
- Many endpoints return arrays of maps in the controller; shapes above mirror the corresponding DTO/entity fields used in code.

Create/Update/Delete (Masters)
- POST `/3g/addWards`: { "wardNo": 10 } → 200 OK | 500 on errors
- POST `/3g/addZones`: Zone_MasterDto
  { "zoneNo": 1, "startZoneNo": 1 } → 200 OK | 500
- POST `/3g/addBuildStatus`: BuildStatus_MasterDto
  { "id": 1, "buildStatus": "COMPLETED" } → 201 Created
- POST `/3g/addOwnerType`: OwnerType_MasterDto
  { "ownertypeId": 1, "ownerType": "OWNER", "ownerTypeMarathi": "मालक" } → 201 Created
- POST `/3g/addUnitNo`: UnitNo_MasterDto
  { "id": 1, "unitno": "1" } → 201 Created
- POST `/3g/addUnitFloorNo`: UnitFloorNo_MasterDto
  { "id": 1, "unitfloorno": "G" } → 201 Created
- POST `/3g/addPropertyType`: PropClassification_MasterDto
  { "pcmId": 1, "propertyTypeName": "Residential", "localPropertyTypeName": "निवासी", "language": "EN" } → 201 Created
- POST `/3g/addPropertySubtypes`: PropSubClassification_MasterDto
  { "propertySubClassificationId": 101, "propertyTypeId": 1, "propertySubtypeName": "Apartment", "localPropertySubtypeName": "अपार्टमेंट", "language": "EN" } → 201 Created
- POST `/3g/addUsageTypes`: PropUsageType_MasterDto
  { "propertyUsageTypeId": 1, "propertySubTypeId": 101, "usageTypeName": "Self-Occupied", "localUsagetypeName": "स्व-वापर" } → 201 Created
- POST `/3g/addPropertySubUsageTypes`: PropSubUsageType_MasterDto
  { "id": 11, "usageId": 1, "usageTypeEng": "Commercial-Shop", "usageTypeLocal": "व्यावसायिक-शॉप", "taxType": "TAX_A" } → 200 OK
- POST `/3g/addBuildingTypes`: BuildingType_MasterDto
  { "btBuildingTypeId": 1, "pcmClassidI": 1, "btBuildingtypeengVc": "RCC", "btBuildingtypellVc": "आरसीसी" } → 200 OK
- PUT `/3g/updateBuildingTypes/{id}`: BuildingType_MasterDto → 200 OK | 404
- DELETE `/3g/buildingtypes/{id}` → 204 No Content | 404
- POST `/3g/addBuildingSubTypes`: BuildingSubType_MasterDto
  { "buildingsubtypeid": 10, "buildingtypeid": 1, "bstBuildingsubtypeengVc": "RCC-G+1", "bstBuildingsubtypellVc": "RCC-G+1 (MR)" } → 200 OK
- (Use GET `/3g/getBuildingSubtypes/{id}` as documented above)
- POST `/3g/addUnitUsageType`: UnitUsageType_MasterDto
  { "uum_usageid_i": 1, "uum_usagetypeeng_vc": "RESIDENTIAL", "uum_usagetypell_vc": "निवासी", "pum_usageid_i": 1 } → 200 OK
- POST `/3g/updateUnitUsageById/{id}`: UnitUsageType_MasterDto → 200 OK
- POST `/3g/deleteUnitUsageById?id=1` → 204 No Content
- POST `/3g/addUnitUsageSub`: UnitUsageSubType_MasterDto
  { "usm_usagesubid_i": 101, "uum_usageclassid_i": 1, "usm_usagetypeeng_vc": "SHOP", "usm_usagetypell_vc": "दुकान" } → 200 OK
- POST `/3g/updateUnitUsagesSub/{id}`: UnitUsageSubType_MasterDto → 200 OK
- POST `/3g/deleteUnitUsagesSub?id=101` → 204 No Content
- POST `/3g/addOwnerCategory`: OwnerCategory_MasterDto
  { "id": 1, "ownerCategory": "GENERAL", "ownerCategoryMarathi": "सामान्य" } → 200 OK
- POST `/3g/addWaterConnection`: WaterConnection_MasterDto
  { "waterConnection": "Municipal", "waterConnectionMarathi": "महापालिका" } → 200 OK
- POST `/3g/addSewerage`: Sewerage_MasterDto
  { "id": 1, "sewerage": "YES" } → 200 OK
- POST `/3g/addOldWards`: { "wardCount": 10 } → 200 OK
- POST `/3g/constructionClassMasters`: ConstructionClass_MasterDto
  { "ccm_conclassid_i": 1, "ccm_classnameeng_vc": "A", "ccm_classnamell_vc": "A (MR)", "ccm_percentageofdeduction_i": 10 } → 200 OK
- PUT `/3g/constructionClassMasters/{id}`: ConstructionClass_MasterDto → 200 OK
- POST `/3g/deleteConstructionClassMastersById?id=1` → 204 No Content
- POST `/3g/occupancyMasters`: Occupancy_MasterDto
  { "occupancy_id": 1, "occupancy": "OWNER", "occupancy_marathi": "मालक" } → 200 OK
- POST `/3g/createAssessmentDates`: AssessmentDate_MasterDto
  { "assessmentId": 1, "firstAssessmentDate": "2010-04-01", "lastAssessmentDate": "2015-03-31", "currentAssessmentDate": "2020-04-01" } → 201 Created
- POST `/3g/deleteAssessmentById?id=1` → 204 No Content
- POST `/3g/roomTypes`: RoomType_MasterDto
  { "id": 1, "roomType": "ROOM", "roomTypeMarathi": "खोली", "roomSelected": 0 } → 201 Created
- PUT `/3g/roomTypes/{id}`: RoomType_MasterDto → 200 OK | 404
- POST `/3g/DeleteRoomTypes?id=1` → 204 No Content
- POST `/3g/addRemarks`: Remarks_MasterDto
  { "id": 1, "remark": "Verified" } → 200 OK
- POST `/3g/uploadExcel` (multipart) with `file=<excel>` → 200 OK | 400 | 417
