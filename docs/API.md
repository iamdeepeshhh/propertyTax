# API Reference

Base paths:
- Admin/Master: `/3g`
- Survey: `/3gSurvey`
- Public/Views: `/`

Content types:
- JSON: `application/json`
- File upload: `multipart/form-data`
- Views: Thymeleaf templates (HTML) returned by controller as String view names

Note: This document lists endpoints discovered in controllers with brief descriptions inferred from method names. For fields and validation, consult DTO classes under `src/main/java/com/GAssociatesWeb/GAssociates/DTO`.

## Public/View Endpoints (`/`)
- GET `/` → view `3GCommon`
- GET `/specialNotice/{wardNo}` → view `3GViewSpecialNotice` (model: `wardNo`)
- GET `/orderSheet` → view `3GViewOrderSheet`
- GET `/citizenLogin` → view `3GCitizenPage`
- GET `/hearingNotice` → view `3GViewHearingNotice`
- GET `/objectionReciept` → view `3GViewObjectionReciept`
- GET `/taxBill/{wardNo}` → view `3GViewTaxBill` (model: `wardNo`)
- GET `/rtcc/{pdNewpropertyno}` → view `3GRealtimeCC` (model: `newPropertyNumber`)

## Admin/Master Endpoints (`/3g`)
Authentication and user management:
- GET `/3g/MasterWebLogin` → view `3GMasterWebLogin`
- GET `/3g/MasterWebSignup` → view `3GMasterWebSignUp`
- GET `/3g/profiles` → `["ITA","ITL","IT","ACTL","ACL","DETL","DEL"]`
- POST `/3g/MasterWebUserSignup` → create user from form (DTO: `UserAccess_Dto`)
- POST `/3g/authenticate` → authenticate user (form params `username`, `password`); redirects/sets session
- GET `/3g/userManagement` → view `3GUserManagement`
- GET `/3g/getAllUsers` → JSON list of users (requires session role `ITA`)
- POST `/3g/updateUser` (JSON `UserAccess_Dto`) → update user
- DELETE `/3g/deleteUser/{username}` → delete by username

Master data: wards/zones/owner/unit/building/etc.
- POST `/3g/addWards` (JSON `{ wardNo: number }`) → create wards
- GET `/3g/getAllWards` → list wards
- POST `/3g/addZones` (JSON `Zone_MasterDto`) → add zone
- GET `/3g/getAllZones` → list zones
- POST `/3g/addBuildStatus` (JSON `BuildStatus_MasterDto`) → create build status
- GET `/3g/buildstatuses` → list build statuses
- POST `/3g/addOwnerType` (JSON `OwnerType_MasterDto`) → create owner type
- GET `/3g/getOwnerType` → list owner types
- POST `/3g/addUnitNo` (JSON `UnitNo_MasterDto`) → create unit numbers
- GET `/3g/unitNumbers` → list unit numbers
- POST `/3g/addUnitFloorNo` (JSON `UnitFloorNo_MasterDto`) → create unit floor numbers
- GET `/3g/getUnitFloorNos` → list unit floor numbers

Property classification/usage:
- POST `/3g/addPropertyType` (JSON `PropClassification_MasterDto`)
- GET `/3g/propertytypes` → list classifications
- POST `/3g/addPropertySubtypes` (JSON `PropSubClassification_MasterDto`)
- GET `/3g/propertysubtypes` → list subtypes
- GET `/3g/propertySubtypes/{classificationId}` → subtypes by classification
- POST `/3g/addUsageTypes` (JSON `PropUsageType_MasterDto`)
- GET `/3g/propertyusagetypes` → list usage types
- GET `/3g/usageTypes/{id}` → usage types by subtype id
- POST `/3g/addPropertySubUsageTypes` (JSON `PropSubUsageType_MasterDto`)
- GET `/3g/getSubUsageTypes` → list usage subtypes

Building types:
- POST `/3g/addBuildingTypes` (JSON `BuildingType_MasterDto`)
- GET `/3g/getBuildingTypes` → list building types
- GET `/3g/getBuildingTypesByPropertyClassification/{propertyClassificationId}` → list by classification
- PUT `/3g/updateBuildingTypes/{id}` (JSON `BuildingType_MasterDto`) → update
- DELETE `/3g/buildingtypes/{id}` → delete
- POST `/3g/addBuildingSubTypes` (JSON `BuildingSubType_MasterDto`)
- GET `/3g/getAllBuildingSubTypes` → list
- GET `/3g/getBuildingSubtypes/{id}` → list by building type
- PUT `/3g/{id}` / DELETE `/3g/{id}` → used in building sub-type module (see controller)

Unit usage:
- POST `/3g/addUnitUsageType` (JSON `UnitUsageType_MasterDto`)
- GET `/3g/{id}` → fetch by id (unit usage context)
- GET `/3g/getUnitUsageByPropUsageId/{propUsageId}`
- GET `/3g/getAllUnitUsageTypes`
- POST `/3g/updateUnitUsageById/{id}`
- POST `/3g/deleteUnitUsageById`
- POST `/3g/addUnitUsageSub` (JSON `UnitUsageSubType_MasterDto`)
- GET `/3g/getUnitUsageSub/{unitUsageId}`
- GET `/3g/getAllUnitUsageSubTypes`
- POST `/3g/updateUnitUsagesSub/{id}`
- POST `/3g/deleteUnitUsagesSub`

Other master data:
- POST `/3g/addOwnerCategory` (JSON `OwnerCategory_MasterDto`); GET `/3g/ownerCategories`
- POST `/3g/addWaterConnection` (JSON `WaterConnection_MasterDto`); GET `/3g/waterConnections`
- GET `/3g/getSewerageTypes`; POST `/3g/addSewerage` (JSON `Sewerage_MasterDto`)
- POST `/3g/addOldWards`; GET `/3g/getAllOldWards`

Construction class and occupancy:
- POST `/3g/constructionClassMasters`
- GET `/3g/constructionClassMasters/{id}`; GET `/3g/constructionClassMasters`
- PUT `/3g/constructionClassMasters/{id}`; POST `/3g/deleteConstructionClassMastersById`
- POST `/3g/occupancyMasters`
- GET `/3g/occupancyMasters/{id}`; GET `/3g/occupancyMasters`
- PUT `/3g/occupancyMasters/{id}`; DELETE `/3g/occupancyMasters/{id}`

Assessment dates and factors:
- POST `/3g/createAssessmentDates`; GET `/3g/getAllAssessmentDates`; GET `/3g/getAssessmentById/{id}`; POST `/3g/deleteAssessmentById`
- GET `/3g/constructionAgeFactor`; GET `/3g/constructionAgeFactor/{id}`; POST `/3g/addConstructionAgeFactor`; DELETE `/3g/deleteConstructionAgeFactor/{id}`

Rooms/Remarks:
- GET `/3g/roomTypes`; GET `/3g/roomTypes/{id}`; POST `/3g/roomTypes`; PUT `/3g/roomTypes/{id}`; POST `/3g/DeleteRoomTypes`
- POST `/3g/addRemarks`; GET `/3g/getAllRemarks`

Files and Excel:
- POST `/3g/uploadExcel` (multipart) → import
- GET `/3g/download-template` → Excel template
- GET `/3g/getDeletionLogs` → deletion logs

Assessment and reports:
- GET `/3g/calculationSheet/{newPropertyNumber}` → calculation sheet (view/PDF)
- GET `/3g/calculatedDetails/{newPropertyNumber}` → JSON details
- GET `/3g/realtimeCalculatedDetails/{newPropertyNumber}` → JSON details
- GET `/3g/showsurvey/{id}` → survey view/details
- POST `/3g/processBatch/{wardNo}` → trigger batch processing
- GET `/3g/batchAssessmentReport/{wardNo}`; GET `/3g/batchCalculationReport/{wardNo}` → reports
- GET `/3g/propertyBatchReport`; GET `/3g/propertyCalculationSheetReport`

Council details:
- POST `/3g/saveCouncilDetails` (multipart)
- GET `/3g/getCouncilDetails`; POST `/3g/deleteCouncilDetails`

Images:
- POST `/3g/uploadCadImage` (multipart)
- POST `/3g/uploadPropertyImage` (multipart)

Objections and notices:
- GET `/3g/searchByFinalPropertyNoAndWardNo`
- POST `/3g/submitObjection` (JSON `RegisterObjection_Dto`)
- GET `/3g/specialNotices/{wardNo}`
- GET `/3g/resultLogs/{wardNo}`

## Survey Endpoints (`/3gSurvey`)
Auth and views:
- GET `/3gSurvey/surveyLogin` → view
- GET `/3gSurvey/signup` → view
- GET `/3gSurvey/profiles` → roles
- POST `/3gSurvey/UserSignup` → sign up (DTO `UserAccess_Dto`)
- POST `/3gSurvey/authenticate` → authenticate
- GET `/3gSurvey/getUserProfileFromSession` → profile from session
- GET `/3gSurvey/afterlogin` → view
- GET `/3gSurvey/form` → survey form view

Search and details:
- GET `/3gSurvey/searchAssessment`
- GET `/3gSurvey/searchProperties`
- GET `/3gSurvey/searchNewProperties`
- GET `/3gSurvey/newRegistration` → view
- GET `/3gSurvey/details/{pdSuryPropNo}` → details by survey prop no
- GET `/3gSurvey/detailsComplete/{pdNewPropNo}` → full details by new prop no

Create/Update/Delete new property:
- POST `/3gSurvey/submitNewPropertyDetails` (multipart) → create
- POST `/3gSurvey/deleteNewProperty` → delete by identifiers in body

Old property details:
- GET `/3gSurvey/oldpropertydetails`
- POST `/3gSurvey/submitPropertyOldDetails` (JSON) → create
- PUT `/3gSurvey/updateOldProperty` (JSON)
- GET `/3gSurvey/getOldPropertyDetails`
- GET `/3gSurvey/OldProperty` (JSON)
- GET `/3gSurvey/OldPropertyByRefNo` (JSON)
- DELETE `/3gSurvey/delete/{oldPropertyNo}/{wardNo}`

Views and helpers:
- GET `/3gSurvey/success`
- GET `/3gSurvey/showsurvey/{id}`

Master lookups for forms:
- GET `/3gSurvey/getAllZones`, `/getAllOldWards`, `/getAllWards`
- GET `/3gSurvey/getOwnerType`, `/buildstatuses`, `/propertytypes`, `/propertyusagetypes`
- GET `/3gSurvey/propertySubtypes/{classificationId}`
- GET `/3gSurvey/usageTypes/{id}`, `/usageSubtypes/{usageTypeId}`
- GET `/3gSurvey/getBuildingTypesByPropertyClassification/{propertyClassificationId}`
- GET `/3gSurvey/buildingSubtypes/{buildingTypeId}`
- GET `/3gSurvey/getSewerageTypes`, `/waterConnections`
- GET `/3gSurvey/getUnitFloorNos`, `/unitNumbers`
- GET `/3gSurvey/getUnitUsageByPropUsageId/{propUsageId}`
- GET `/3gSurvey/getUnitUsageSub/{unitUsageId}`
- GET `/3gSurvey/constructionClassMasters`, `/occupancyMasters`, `/ownerCategories`
- GET `/3gSurvey/getAllAssessmentDates`, `/roomTypes`
- GET `/3gSurvey/propertyTypes/{id}`, `/propertySubtype/{id}`
- GET `/3gSurvey/PropertyUsage/{id}`, `/PropertySubUsage/{id}`
- GET `/3gSurvey/buildingType/{id}`, `/buildingSubType/{id}`
- GET `/3gSurvey/unitUsageType/{id}`, `/unitSubUsageType/{id}`
- GET `/3gSurvey/occupantStatus/{id}`
- GET `/3gSurvey/getAllRemarks`
- GET `/3gSurvey/checkSurveyPropNo` → validate uniqueness/format
- GET `/3gSurvey/editSurveyForm`
- GET `/3gSurvey/getPropertiesCount`

## Request/Response Examples
Create Property Type
- POST `/3g/addPropertyType`
Request (application/json):
```
{
  "classificationId": 1,
  "classificationName": "Residential",
  "description": "..."
}
```
Response (201 Created):
```
{
  "classificationId": 1,
  "classificationName": "Residential",
  "...": "..."
}
```

Upload Property Image
- POST `/3g/uploadPropertyImage` (multipart/form-data)
Fields: `file`, optional identifiers (e.g., property number) per UI integration.
Response: 200 OK on success.

Calculation Details
- GET `/3g/realtimeCalculatedDetails/{newPropertyNumber}`
Response (application/json): calculation breakdown fields for the property.

Note: For detailed payload schemas, refer to DTO classes under `DTO.MasterWebDto` and `DTO.PropertySurveyDto` packages.

