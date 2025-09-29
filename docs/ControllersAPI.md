# Controllers API (Success Responses)

This document lists REST endpoints from MasterWebController and PropertySurveyController, with the typical success HTTP status and example JSON response shapes. View-returning routes are omitted; see templates for those.

Note: Many list endpoints return arrays of objects with `value` (id/code) and `name` (label). DTO fields may include more attributes; this summarizes successful shapes observed in code.

## MasterWebController (`/3g`)

- GET `/3g/getAllUsers`
  - 200 OK
  - Body: `[UserAccess_Dto, ...]`

- POST `/3g/updateUser`
  - 200 OK
  - Body: `UserAccess_Dto` (updated)

- DELETE `/3g/deleteUser/{username}`
  - 200 OK
  - Body: `"User deleted successfully."`

- POST `/3g/addWards`
  - 200 OK
  - Body: `"Ward added successfully"`

- GET `/3g/getAllWards`
  - 200 OK
  - Body: `[{ value: <wardId>, name: <wardName> }, ...]`

- POST `/3g/addZones`
  - 200 OK
  - Body: `"Zone added successfully"`

- GET `/3g/getAllZones`
  - 200 OK
  - Body: `[{ value: <zoneId>, name: <zoneName> }, ...]`

- POST `/3g/addBuildStatus`
  - 201 Created
  - Body: `BuildStatus_MasterDto`

- GET `/3g/buildstatuses`
  - 200 OK
  - Body: `[{ value: <id>, name: <statusName> }, ...]`

- POST `/3g/addOwnerType`
  - 201 Created
  - Body: `OwnerType_MasterDto`

- GET `/3g/getOwnerType`
  - 200 OK
  - Body: `[{ value: <id>, name: <ownerType> }, ...]`

- POST `/3g/addUnitNo`
  - 201 Created
  - Body: empty

- GET `/3g/unitNumbers`
  - 200 OK
  - Body: `[{ value: <id>, name: <unitNo> }, ...]`

- POST `/3g/addUnitFloorNo`
  - 201 Created
  - Body: empty

- GET `/3g/getUnitFloorNos`
  - 200 OK
  - Body: `[{ value: <id>, name: <floorNo> }, ...]`

- POST `/3g/addPropertyType`
  - 201 Created
  - Body: `PropClassification_MasterDto`

- GET `/3g/propertytypes`
  - 200 OK
  - Body: `[{ value: <classificationId>, name: <classificationName> }, ...]`

- POST `/3g/addPropertySubtypes`
  - 201 Created
  - Body: `PropSubClassification_MasterDto`

- GET `/3g/propertysubtypes`
  - 200 OK
  - Body: `[{ value: <subtypeId>, name: <subtypeName> }, ...]`

- GET `/3g/propertySubtypes/{classificationId}`
  - 200 OK
  - Body: `[{ value: <subtypeId>, name: <subtypeName> }, ...]`

- POST `/3g/addUsageTypes`
  - 201 Created
  - Body: `PropUsageType_MasterDto`

- GET `/3g/propertyusagetypes`
  - 200 OK
  - Body: `[{ value: <usageTypeId>, name: <usageTypeName> }, ...]`

- GET `/3g/usageTypes/{id}`
  - 200 OK
  - Body: `[{ value: <usageTypeId>, name: <usageTypeName> }, ...]`

- POST `/3g/addPropertySubUsageTypes`
  - 201 Created
  - Body: `PropSubUsageType_MasterDto`

- GET `/3g/getSubUsageTypes`
  - 200 OK
  - Body: `[{ value: <subUsageTypeId>, name: <subUsageTypeName> }, ...]`

- POST `/3g/addBuildingTypes`
  - 201 Created
  - Body: `BuildingType_MasterDto`

- GET `/3g/getBuildingTypes`
  - 200 OK
  - Body: `[{ value: <buildingTypeId>, name: <buildingTypeName> }, ...]`

- GET `/3g/getBuildingTypesByPropertyClassification/{propertyClassificationId}`
  - 200 OK
  - Body: `[{ value: <buildingTypeId>, name: <buildingTypeName> }, ...]`

- PUT `/3g/updateBuildingTypes/{id}`
  - 200 OK
  - Body: `BuildingType_MasterDto` (updated)

- DELETE `/3g/buildingtypes/{id}`
  - 200 OK (empty body)

- POST `/3g/addBuildingSubTypes`
  - 201 Created
  - Body: `BuildingSubType_MasterDto`

- GET `/3g/getAllBuildingSubTypes`
  - 200 OK
  - Body: `[{ value: <subtypeId>, name: <subtypeName> }, ...]`

- GET `/3g/getBuildingSubtypes/{id}`
  - 200 OK
  - Body: `BuildingSubType_MasterDto`

- PUT `/3g/{id}` (building subtype update)
  - 200 OK
  - Body: `BuildingSubType_MasterDto` (updated)

- DELETE `/3g/{id}` (building subtype delete)
  - 200 OK (empty body)

- POST `/3g/addUnitUsageType`
  - 201 Created
  - Body: `UnitUsageType_MasterDto`

- GET `/3g/{id}` (unit usage by id)
  - 200 OK
  - Body: `UnitUsageType_MasterDto`

- GET `/3g/getUnitUsageByPropUsageId/{propUsageId}`
  - 200 OK
  - Body: `[{ value: <id>, name: <unitUsageType> }, ...]`

- GET `/3g/getAllUnitUsageTypes`
  - 200 OK
  - Body: `[{ value: <id>, name: <unitUsageType> }, ...]`

- POST `/3g/updateUnitUsageById/{id}`
  - 200 OK
  - Body: `UnitUsageType_MasterDto` (updated)

- POST `/3g/deleteUnitUsageById`
  - 200 OK (empty body)

- POST `/3g/addUnitUsageSub`
  - 201 Created
  - Body: `UnitUsageSubType_MasterDto`

- GET `/3g/getUnitUsageSub/{unitUsageId}`
  - 200 OK
  - Body: `[{ value: <subtypeId>, name: <subtypeName> }, ...]`

- GET `/3g/getAllUnitUsageSubTypes`
  - 200 OK
  - Body: `[{ value: <subtypeId>, name: <subtypeName> }, ...]`

- POST `/3g/updateUnitUsagesSub/{id}`
  - 200 OK
  - Body: `UnitUsageSubType_MasterDto` (updated)

- POST `/3g/deleteUnitUsagesSub`
  - 200 OK (empty body)

- POST `/3g/addOwnerCategory`
  - 201 Created
  - Body: `OwnerCategory_MasterDto`

- GET `/3g/ownerCategories`
  - 200 OK
  - Body: `[{ value: <id>, name: <ownerCategory> }, ...]`

- POST `/3g/addWaterConnection`
  - 201 Created
  - Body: `WaterConnection_MasterDto`

- GET `/3g/waterConnections`
  - 200 OK
  - Body: `[{ value: <id>, name: <connectionType> }, ...]`

- GET `/3g/getSewerageTypes`
  - 200 OK
  - Body: `[{ value: <id>, name: <sewerageType> }, ...]`

- POST `/3g/addSewerage`
  - 201 Created
  - Body: `Sewerage_MasterDto`

- POST `/3g/addOldWards`
  - 200 OK
  - Body: `"Successfully added N old wards."`

- GET `/3g/getAllOldWards`
  - 200 OK
  - Body: `[OldWard_MasterDto, ...]`

- POST `/3g/constructionClassMasters`
  - 201 Created
  - Body: `ConstructionClass_MasterDto`

- GET `/3g/constructionClassMasters/{id}`
  - 200 OK
  - Body: `ConstructionClass_MasterDto`

- GET `/3g/constructionClassMasters`
  - 200 OK
  - Body: `[{ value: <id>, name: <className> }, ...]`

- PUT `/3g/constructionClassMasters/{id}`
  - 200 OK
  - Body: `ConstructionClass_MasterDto` (updated)

- POST `/3g/deleteConstructionClassMastersById`
  - 200 OK (empty body)

- POST `/3g/occupancyMasters`
  - 201 Created
  - Body: `Occupancy_MasterDto`

- GET `/3g/occupancyMasters/{id}`
  - 200 OK or 404 Not Found
  - Body: `Occupancy_MasterDto`

- GET `/3g/occupancyMasters`
  - 200 OK
  - Body: `[{ value: <id>, name: <occupancy> }, ...]`

- PUT `/3g/occupancyMasters/{id}`
  - 200 OK
  - Body: `Occupancy_MasterDto` (updated)

- DELETE `/3g/occupancyMasters/{id}`
  - 200 OK (empty body)

- Other assessment endpoints (rates, cess, consolidated taxes, reports)
  - Typically 200 OK (GET) or 201 Created (POST create/update). Bodies are DTOs or arrays similar to above.

## PropertySurveyController (`/3gSurvey`)

- GET `/3gSurvey/getUserProfileFromSession`
  - 200 OK or 401 Unauthorized
  - Body: `UserAccess_Dto` or null

- GET `/3gSurvey/searchOldProperties`
  - 200 OK
  - Body: `[PropertyOldDetails_Dto, ...]`

- GET `/3gSurvey/searchProperties`
  - 200 OK
  - Body: `[PropertyDetails_Dto, ...]`

- POST `/3gSurvey/submitNewPropertyDetails` (multipart)
  - 200 OK
  - Body: `{"message":"Property details submitted successfully."}`

- PATCH `/3gSurvey/submitUpdatedPropertyDetails` (multipart)
  - 200 OK
  - Body: `{ "message": "Property details updated successfully.", "updatedProperty": CompleteProperty_Dto }`

- POST `/3gSurvey/deleteNewProperty`
  - 200 OK
  - Body: `{ success: true, message: "Property deleted successfully." }`

- GET `/3gSurvey/details/{pdSuryPropNo}`
  - 200 OK
  - Body: `CompleteProperty_Dto`

- GET `/3gSurvey/detailsComplete/{pdNewPropNo}`
  - 200 OK
  - Body: `CompleteProperty_Dto`

- POST `/3gSurvey/submitPropertyOldDetails`
  - 200 OK (empty body)

- PUT `/3gSurvey/updateOldProperty`
  - 200 OK
  - Body: `PropertyOldDetails_Dto` (updated)

- GET `/3gSurvey/OldProperty`
  - 200 OK
  - Body: `PropertyOldDetails_Dto`

- GET `/3gSurvey/OldPropertyByRefNo`
  - 200 OK
  - Body: `PropertyOldDetails_Dto`

- DELETE `/3gSurvey/delete/{oldPropertyNo}/{wardNo}`
  - 200 OK
  - Body: `"Property and related units deleted successfully."`

- Lookups for form population (zones, wards, owner types, statuses, property types/subtypes/usages, building types/subtypes, sewerage/water, unit floors/numbers, unit usages/sub-usages, construction class, occupancy, owner categories, assessment dates, room types)
  - 200 OK
  - Body: arrays of `{ value, name }` or specific DTO maps

- GET `/3gSurvey/checkSurveyPropNo?surveyPropNo=...&ward=...`
  - 200 OK
  - Body: `true` or `false`

- GET `/3gSurvey/getPropertiesCount`
  - 200 OK
  - Body: `{ "<ward>": "<count>", ... }` (string map)

Notes
- Error paths typically return 4xx/5xx with a short error message string or `{ message: ..., error: ... }` objects.
- Exact DTO field sets are defined under `DTO.*`; this document focuses on the high-level success shapes and statuses.

## Endpoint-by-Endpoint (method, path, success status, body)

Below, each endpoint lists method, path, success status, and the JSON body on success. For DTO bodies, see the Full Example Payloads section for complete field sets.

### PropertySurveyController (`/3gSurvey`)

- GET /3gSurvey/getUserProfileFromSession
  - 200 OK (or 401 if no session)
  - JSON: UserAccess_Dto

- GET /3gSurvey/searchOldProperties?ownerName=...&oldPropertyNo=...&ward=...
  - 200 OK
  - JSON: [PropertyOldDetails_Dto, ...]

- GET /3gSurvey/searchProperties?ownerName=...&newPropertyNo=...&ward=...
  - 200 OK
  - JSON: [PropertyDetails_Dto, ...]

- POST /3gSurvey/submitNewPropertyDetails (multipart)
  - Request (multipart/form-data):
    - jsonData: CompleteProperty_Dto as JSON string
    - previewPropertyImage, previewPropertyImage2, previewHousePlan1, previewHousePlan2 (optional files)
  - 200 OK
  - JSON: { "message": "Property details submitted successfully." }

- PATCH /3gSurvey/submitUpdatedPropertyDetails (multipart)
  - Request (multipart/form-data):
    - updatedFields: CompleteProperty_Dto (partial) as JSON string
    - propertyImage, propertyImage2, housePlan1, housePlan2 (optional files)
  - 200 OK
  - JSON: { "message": "Property details updated successfully.", "updatedProperty": CompleteProperty_Dto }

- POST /3gSurvey/deleteNewProperty
  - Request (application/json): { pdNewpropertynoVc, ward, surveyPropNo, ownerName, createdBy, remarks }
  - 200 OK
  - JSON: { success: true, message: "Property deleted successfully." }

- GET /3gSurvey/details/{pdSuryPropNo}
  - 200 OK
  - JSON: CompleteProperty_Dto

- GET /3gSurvey/detailsComplete/{pdNewPropNo}
  - 200 OK
  - JSON: CompleteProperty_Dto

- POST /3gSurvey/submitPropertyOldDetails
  - Request: PropertyOldDetails_Dto
  - 200 OK (empty)

- PUT /3gSurvey/updateOldProperty
  - Request: PropertyOldDetails_Dto
  - 200 OK
  - JSON: PropertyOldDetails_Dto

- GET /3gSurvey/OldProperty?oldPropertyNo=...&wardNo=...
  - 200 OK
  - JSON: PropertyOldDetails_Dto

- GET /3gSurvey/OldPropertyByRefNo?podRefNo=...
  - 200 OK
  - JSON: PropertyOldDetails_Dto

- DELETE /3gSurvey/delete/{oldPropertyNo}/{wardNo}
  - 200 OK
  - JSON: "Property and related units deleted successfully."

- GET /3gSurvey/getAllZones
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/getAllOldWards
  - 200 OK
  - JSON: [ { value, name }, ... ] or [OldWard_MasterDto]

- GET /3gSurvey/getAllWards
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/getOwnerType
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/buildstatuses
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/propertytypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/propertyusagetypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/propertySubtypes/{classificationId}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/usageTypes/{id}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/usageSubtypes/{usageTypeId}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/getBuildingTypesByPropertyClassification/{propertyClassificationId}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/buildingSubtypes/{buildingTypeId}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/getSewerageTypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/waterConnections
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/getUnitFloorNos
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/unitNumbers
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/getUnitUsageByPropUsageId/{propUsageId}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/getUnitUsageSub/{unitUsageId}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/constructionClassMasters
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/occupancyMasters
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/ownerCategories
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/getAllAssessmentDates
  - 200 OK
  - JSON: [AssessmentDate_MasterDto, ...]

- GET /3gSurvey/roomTypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3gSurvey/propertyTypes/{id} (and other /{id} lookups)
  - 200 OK
  - JSON: { ...mapped fields for the id... }

- GET /3gSurvey/checkSurveyPropNo?surveyPropNo=...&ward=...
  - 200 OK
  - JSON: true | false

- GET /3gSurvey/getPropertiesCount
  - 200 OK
  - JSON: { "<ward>": "<count>", ... }

### MasterWebController (`/3g`)

- GET /3g/getAllUsers
  - 200 OK
  - JSON: [UserAccess_Dto, ...]

- POST /3g/updateUser
  - Request: UserAccess_Dto
  - 200 OK
  - JSON: UserAccess_Dto

- DELETE /3g/deleteUser/{username}
  - 200 OK
  - JSON: "User deleted successfully."

- POST /3g/addWards
  - Request: { wardNo: number }
  - 200 OK
  - JSON: "Ward added successfully"

- GET /3g/getAllWards
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addZones
  - Request: Zone_MasterDto
  - 200 OK
  - JSON: "Zone added successfully"

- GET /3g/getAllZones
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addBuildStatus
  - Request: BuildStatus_MasterDto
  - 201 Created
  - JSON: BuildStatus_MasterDto

- GET /3g/buildstatuses
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addOwnerType
  - Request: OwnerType_MasterDto
  - 201 Created
  - JSON: OwnerType_MasterDto

- GET /3g/getOwnerType
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addUnitNo
  - Request: UnitNo_MasterDto
  - 201 Created
  - JSON: (empty)

- GET /3g/unitNumbers
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addUnitFloorNo
  - Request: UnitFloorNo_MasterDto
  - 201 Created
  - JSON: (empty)

- GET /3g/getUnitFloorNos
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addPropertyType
  - Request: PropClassification_MasterDto
  - 201 Created
  - JSON: PropClassification_MasterDto

- GET /3g/propertytypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addPropertySubtypes
  - Request: PropSubClassification_MasterDto
  - 201 Created
  - JSON: PropSubClassification_MasterDto

- GET /3g/propertysubtypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3g/propertySubtypes/{classificationId}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addUsageTypes
  - Request: PropUsageType_MasterDto
  - 201 Created
  - JSON: PropUsageType_MasterDto

- GET /3g/propertyusagetypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3g/usageTypes/{id}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addPropertySubUsageTypes
  - Request: PropSubUsageType_MasterDto
  - 201 Created
  - JSON: PropSubUsageType_MasterDto

- GET /3g/getSubUsageTypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addBuildingTypes
  - Request: BuildingType_MasterDto
  - 201 Created
  - JSON: BuildingType_MasterDto

- GET /3g/getBuildingTypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3g/getBuildingTypesByPropertyClassification/{propertyClassificationId}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- PUT /3g/updateBuildingTypes/{id}
  - Request: BuildingType_MasterDto
  - 200 OK
  - JSON: BuildingType_MasterDto

- DELETE /3g/buildingtypes/{id}
  - 200 OK (empty)

- POST /3g/addBuildingSubTypes
  - Request: BuildingSubType_MasterDto
  - 201 Created
  - JSON: BuildingSubType_MasterDto

- GET /3g/getAllBuildingSubTypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3g/getBuildingSubtypes/{id}
  - 200 OK
  - JSON: BuildingSubType_MasterDto

- PUT /3g/{id} (building subtype update)
  - Request: BuildingSubType_MasterDto
  - 200 OK
  - JSON: BuildingSubType_MasterDto

- DELETE /3g/{id} (building subtype delete)
  - 200 OK (empty)

- POST /3g/addUnitUsageType
  - Request: UnitUsageType_MasterDto
  - 201 Created
  - JSON: UnitUsageType_MasterDto

- GET /3g/{id} (unit usage by id)
  - 200 OK
  - JSON: UnitUsageType_MasterDto

- GET /3g/getUnitUsageByPropUsageId/{propUsageId}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3g/getAllUnitUsageTypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/updateUnitUsageById/{id}
  - Request: UnitUsageType_MasterDto
  - 200 OK
  - JSON: UnitUsageType_MasterDto

- POST /3g/deleteUnitUsageById
  - Request: { id: number }
  - 200 OK (empty)

- POST /3g/addUnitUsageSub
  - Request: UnitUsageSubType_MasterDto
  - 201 Created
  - JSON: UnitUsageSubType_MasterDto

- GET /3g/getUnitUsageSub/{unitUsageId}
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3g/getAllUnitUsageSubTypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/updateUnitUsagesSub/{id}
  - Request: UnitUsageSubType_MasterDto
  - 200 OK
  - JSON: UnitUsageSubType_MasterDto

- POST /3g/deleteUnitUsagesSub
  - Request: { id: number }
  - 200 OK (empty)

- POST /3g/addOwnerCategory
  - Request: OwnerCategory_MasterDto
  - 201 Created
  - JSON: OwnerCategory_MasterDto

- GET /3g/ownerCategories
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addWaterConnection
  - Request: WaterConnection_MasterDto
  - 201 Created
  - JSON: WaterConnection_MasterDto

- GET /3g/waterConnections
  - 200 OK
  - JSON: [ { value, name }, ... ]

- GET /3g/getSewerageTypes
  - 200 OK
  - JSON: [ { value, name }, ... ]

- POST /3g/addSewerage
  - Request: Sewerage_MasterDto
  - 201 Created
  - JSON: Sewerage_MasterDto

- POST /3g/addOldWards
  - Request: { wardNo: number }
  - 200 OK
  - JSON: "Successfully added N old wards."

- GET /3g/getAllOldWards
  - 200 OK
  - JSON: [OldWard_MasterDto, ...]

- POST /3g/constructionClassMasters
  - Request: ConstructionClass_MasterDto
  - 201 Created
  - JSON: ConstructionClass_MasterDto

- GET /3g/constructionClassMasters/{id}
  - 200 OK
  - JSON: ConstructionClass_MasterDto

- GET /3g/constructionClassMasters
  - 200 OK
  - JSON: [ { value, name }, ... ]

- PUT /3g/constructionClassMasters/{id}
  - Request: ConstructionClass_MasterDto
  - 200 OK
  - JSON: ConstructionClass_MasterDto

- POST /3g/deleteConstructionClassMastersById
  - Request: { id: number }
  - 200 OK (empty)

- POST /3g/occupancyMasters
  - Request: Occupancy_MasterDto
  - 201 Created
  - JSON: Occupancy_MasterDto

- GET /3g/occupancyMasters/{id}
  - 200 OK (or 404)
  - JSON: Occupancy_MasterDto

- GET /3g/occupancyMasters
  - 200 OK
  - JSON: [ { value, name }, ... ]

- PUT /3g/occupancyMasters/{id}
  - Request: Occupancy_MasterDto
  - 200 OK
  - JSON: Occupancy_MasterDto

- DELETE /3g/occupancyMasters/{id}
  - 200 OK (empty)


## Full Example Payloads (based on DTOs)

- CompleteProperty_Dto (used by /3gSurvey/details, /detailsComplete, and update responses)
```
{
  "propertyDetails": {
    "pdZoneI": "1",
    "pdWardI": "02",
    "pdOldpropnoVc": "OLD-123",
    "pdSurypropnoVc": "0000112233",
    "pdLayoutnameVc": "Alpha Layout",
    "pdLayoutnoVc": "L-45",
    "pdKhasranoVc": "KH-77",
    "pdPlotnoVc": "P-12",
    "pdGrididVc": "G-9",
    "pdRoadidVc": "R-21",
    "pdParcelidVc": "PR-5",
    "pdNewpropertynoVc": "9200910",
    "pdOwnernameVc": "John Doe",
    "pdAddnewownernameVc": "Jane Doe",
    "pdPannoVc": "ABCDE1234F",
    "pdAadharnoVc": "123412341234",
    "pdContactnoVc": "9876543210",
    "pdPropertynameVc": "Sunshine Residency",
    "pdPropertyaddressVc": "123 Main Street",
    "pdPincodeVc": "400001",
    "pdCategoryI": "1",
    "pdPropertytypeI": "10",
    "pdPropertysubtypeI": "101",
    "pdUsagetypeI": "20",
    "pdUsagesubtypeI": "201",
    "pdBuildingtypeI": "30",
    "pdBuildingsubtypeI": "301",
    "pdConstAgeI": 12,
    "pdConstYearVc": "2012",
    "pdPermissionstatusVc": "yes",
    "pdPermissionnoVc": "PRM-123",
    "pdPermissiondateDt": "2020-05-10T00:00:00Z",
    "pdNooffloorsI": 3,
    "pdNoofroomsI": 10,
    "pdStairVc": "Yes",
    "pdLiftVc": "No",
    "pdRoadwidthF": "12.0",
    "pdToiletstatusVc": "yes",
    "pdToiletsI": 2,
    "pdSeweragesVc": "Drain",
    "pdSeweragesType": "Type-A",
    "pdWaterconnstatusVc": "yes",
    "pdWaterconntypeVc": "Tap",
    "pdMcconnectionVc": "Authorized",
    "pdMeternoVc": "MTR-888",
    "pdConsumernoVc": "CN-777",
    "pdConnectiondateDt": "2021-01-01T00:00:00Z",
    "pdConnectiondateDt_vc": "2021-01-01",
    "pdPipesizeF": 1.0,
    "pdElectricityconnectionVc": "yes",
    "pdElemeternoVc": "EM-555",
    "pdEleconsumernoVc": "EC-444",
    "pdRainwaterhaverstVc": "No",
    "pdSolarunitVc": "Yes",
    "pdPlotareaF": "200.00",
    "pdTotbuiltupareaF": "150.00",
    "pdTotcarpetareaF": "120.00",
    "pdTotalexempareaF": "10.00",
    "pdAssesareaF": "110.00",
    "pdArealetoutF": "0.00",
    "pdAreanotletoutF": "110.00",
    "pdCurrassesdateDt": "2024-04-01",
    "pdOldrvFl": "5000.00",
    "user_id": "surveyor1",
    "pdPolytypeVc": "poly",
    "pdStatusbuildingVc": "Completed",
    "pdLastassesdateDt": "2023-04-01",
    "pdFirstassesdateDt": "2020-04-01",
    "pdNoticenoVc": "WA02_0001",
    "pdIndexnoVc": "SEQ-01",
    "pdNewfinalpropnoVc": "020001",
    "pdTaxstatusVc": 1,
    "pdChangedVc": 0,
    "pdNadetailsVc": "NA",
    "pdNanumberI": "NA-1",
    "pdNadateDt": "2022-01-01",
    "pdTddetailsVc": "TD",
    "pdTdordernumF": "TD-1",
    "pdTddateDt": "2022-02-01",
    "pdSaledeedI": "yes",
    "pdSaledateDt": "2022-03-01",
    "pdCitysurveynoF": "CS-99",
    "pdOwnertypeVc": "Owner",
    "pdBuildingvalueI": "1000000",
    "pdPlotvalueF": "500000",
    "pdTpdateDt": "2021-05-01",
    "pdTpdetailsVc": "TP",
    "pdTpordernumF": "TP-1",
    "pdOccupinameF": "TenantName",
    "pdPropimageT": null,
    "pdHouseplanT": null,
    "pdPropimage2T": null,
    "pdHouseplan2T": null,
    "pdSignT": null,
    "pdFinalpropnoVc": "020001",
    "propRefno": "12345",
    "pdImgstatusVc": "OK",
    "pdSuryprop2Vc": 0,
    "pdSuryprop1Vc": "0000112233",
    "createdAt": "2024-01-01T10:00:00",
    "createddateVc": "2024-01-01",
    "updatedAt": "2024-01-10T10:00:00Z",
    "id": 1,
    "pdApprovedByDesk1Vc": "ITA",
    "pdApprovedByDesk2Vc": null,
    "pdApprovedByDesk3Vc": null,
    "pdCompletedByVc": null,
    "pdOldwardNoVc": "01",
    "pdOldTaxVc": "4000.00"
  },
  "unitDetails": [
    {
      "pdNewpropertynoVc": "9200910",
      "udFloorNoVc": "G",
      "udUnitNoVc": 1,
      "udOccupantStatusI": 1,
      "udRentalNoVc": "0",
      "udAreaBefDedF": "60.00",
      "udOccupierNameVc": "Alice",
      "udAgreementCopyVc": null,
      "udMobileNoVc": "9999999999",
      "udEmailIdVc": "a@example.com",
      "udUsageTypeI": 20,
      "udUsageSubtypeI": 201,
      "udConstAgeI": 12,
      "udConstructionClassI": "A",
      "udAgeFactorI": "0.8",
      "udCarpetAreaF": "55.00",
      "udExemptedAreaF": "5.00",
      "udAssessmentAreaF": "50.00",
      "udSignatureVc": null,
      "udTimestampDt": "2024-01-01",
      "udTotLegAreaF": "50.00",
      "udTotIllegAreaF": "0.00",
      "udUnitRemarkVc": "Good",
      "udConstYearDt": "2012",
      "udEstablishmentNameVc": null,
      "udTenantNoI": "",
      "udAgeFactVc": "0.8",
      "udPlotAreaFl": "0.00",
      "udAssVc": "",
      "createdAt": "2024-01-01T10:00:00Z",
      "updatedAt": "2024-01-10T10:00:00Z",
      "id": 11,
      "unitBuiltupUps": [
        {
          "pdNewpropertynoVc": "9200910",
          "udFloorNoVc": "G",
          "udUnitNoVc": 1,
          "ubIdsI": 101,
          "ubDedpercentI": "10",
          "ubareabefdedFl": "20.00",
          "ubplotareaFl": "0.00",
          "ubRoomTypeVc": "Bedroom",
          "ubLengthFl": "5.00",
          "ubBreadthFl": "4.00",
          "ubExemptionsVc": "no",
          "ubExemLengthFl": "0.00",
          "ubExemBreadthFl": "0.00",
          "ubExemAreaFl": "0.00",
          "ubCarpetAreaFl": "18.00",
          "ubAssesAreaFl": "18.00",
          "udTimestampDt": "2024-01-01",
          "ubLegalStVc": "legal",
          "ubLegalAreaFl": "18.00",
          "ubIllegalAreaFl": "0.00",
          "udUnitRemarkVc": null,
          "ubMeasureTypeVc": "inner",
          "createdAt": "2024-01-01T10:00:00Z",
          "updatedAt": "2024-01-10T10:00:00Z",
          "id": 1001,
          "unitDetailsDto": null
        }
      ]
    }
  ],
  "additionalInfo": [],
  "Error": null
}
```

- UserAccess_Dto (e.g., /3g/getAllUsers, /3g/updateUser)
```
{
  "id": 1,
  "username": "admin",
  "password": null,
  "profile": "ITA",
  "status": "ACTIVE",
  "firstname": "System",
  "lastname": "Admin",
  "phone": "9876543210",
  "email": "admin@example.com"
}
```

- BuildStatus_MasterDto (201 Created bodies)
```
{ "id": 1, "buildStatus": "Completed" }
```

- PropClassification_MasterDto
```
{
  "pcmId": 10,
  "propertyTypeName": "Residential",
  "language": "en",
  "localPropertyTypeName": "निवासी",
  "buildingInp": "Y"
}
```

- PropSubClassification_MasterDto
```
{
  "propertyTypeId": 10,
  "propertySubtypeName": "Apartment",
  "language": "en",
  "localPropertySubtypeName": "अपार्टमेंट",
  "propertySubClassificationId": 101
}
```

- UnitUsageType_MasterDto
```
{
  "uum_usageid_i": 20,
  "uum_usagetypeeng_vc": "Residential",
  "uum_usagetypell_vc": "निवासी",
  "pum_usageid_i": 5,
  "uum_created_dt": "2024-01-01T10:00:00",
  "uum_updated_dt": "2024-01-10T10:00:00",
  "uum_abbr_vc": "RES",
  "uum_type_vc": "Main",
  "uum_rvtype_vc": "A"
}
```

- WaterConnection_MasterDto
```
{
  "waterConnection": "Tap",
  "waterConnectionMarathi": "नळ"
}
```

- Zone_MasterDto
```
{ "id": 1, "zoneName": "Zone A", "localZoneName": "झोन अ" }
```

- OwnerType_MasterDto
```
{ "id": 1, "ownerType": "Owner", "localOwnerType": "मालक" }
```

- OwnerCategory_MasterDto
```
{ "id": 1, "ownerCategory": "General", "localOwnerCategory": "सामान्य" }
```

- OldWard_MasterDto
```
{ "id": 1, "oldWardName": "Old Ward 1", "localOldWardName": "जुना प्रभाग 1" }
```

- AssessmentDate_MasterDto
```
{ "currentAssessmentDate": "2024-04-01", "lastAssessmentDate": "2023-04-01", "firstAssessmentDate": "2020-04-01" }
```

- RoomType_MasterDto
```
{ "id": 1, "roomType": "Bedroom", "localRoomType": "शयनकक्ष" }
```

- Remarks_MasterDto
```
{ "id": 1, "remark": "Good", "localRemark": "चांगले" }
```

- Sewerage_MasterDto
```
{ "id": 1, "sewerageType": "Drain", "localSewerageType": "नाली" }
```

- UnitNo_MasterDto
```
{ "id": 1, "unitNo": "101" }
```

- UnitFloorNo_MasterDto
```
{ "id": 1, "floorNo": "G" }
```

- BuildingType_MasterDto
```
{ "id": 30, "buildingTypeName": "Residential", "localBuildingTypeName": "निवासी", "propertyClassificationId": 10 }
```

- BuildingSubType_MasterDto
```
{ "id": 301, "buildingSubTypeName": "Apartment", "localBuildingSubTypeName": "अपार्टमेंट", "buildingTypeId": 30 }
```

- ConstructionClass_MasterDto
```
{ "id": 1, "constructionClassName": "A", "localConstructionClassName": "अ" }
```

- Occupancy_MasterDto
```
{ "id": 1, "occupancy": "Owner Occupied", "localOccupancy": "स्वमालकी" }
```

- PropUsageType_MasterDto
```
{ "id": 20, "usageTypeName": "Residential", "localUsageTypeName": "निवासी" }
```

- PropSubUsageType_MasterDto
```
{ "id": 201, "usageSubTypeName": "Residential - House", "localUsageSubTypeName": "निवासी - घर" }
```

- ConsolidatedTaxDetailsDto (embedded in AssessmentResultsDto)
```
{
  "propertyTax": 1200.0,
  "educationResidential": 100.0,
  "educationCommercial": 50.0,
  "educationTotal": 150.0,
  "userCharges": 200.0,
  "egc": 75.0,
  "fireTax": 0.0,
  "lightTax": 0.0,
  "treeTax": 0.0,
  "cleanTax": 0.0,
  "finalTotalTax": 1625.0
}
```

- AssessmentResultsDto (selected fields and nested objects)
```
{
  "pdZoneI": "1",
  "pdWardI": "02",
  "pdNewpropertynoVc": "9200910",
  "pdFinalpropnoVc": "020001",
  "pdOwnernameVc": "John Doe",
  "pdPropertyaddressVc": "123 Main Street",
  "annualRentalValueFl": "50000",
  "annualUnRentalValueFl": "0",
  "finalValueToConsiderFl": "50000",
  "consolidatedTaxes": { ... see ConsolidatedTaxDetailsDto ... },
  "proposedRatableValues": {
    "totalratablevalue": 50000,
    "residential": 30000,
    "nonResidential": 20000
  },
  "unitDetails": [ { ... see PropertyUnitDetailsDto ... } ],
  "taxKeyValueMap": { "1001": 1200.0, "2001": 100.0, "2002": 50.0 }
}
```
