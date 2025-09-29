API Testing – Property CRUD (v1)

Purpose: Endpoints to create, read, update, and delete complete property records used in survey flow. Includes sample JSON shaped to DTOs and field suffix conventions. Note: all property numbers are strings with numeric-only content.

Base
- Base path: `/3gSurvey`
- Auth: Requires session established via `POST /3gSurvey/authenticate` (form fields: `username`, `password`). Use a session-aware client to persist cookies.

Field Conventions
- `I`: integer stored as string (e.g., "1")
- `Vc`: varchar string
- `Fl`: float (numeric)
- Images: sent as files; server converts to Base64 internally

Create – New Property
- Endpoint: POST `/3gSurvey/submitNewPropertyDetails`
- Why: Creates a complete property (details + units + built-up). Optional images supported.
- Content-Type: `multipart/form-data`
- Parts:
  - `jsonData`: JSON of `CompleteProperty_Dto` (see sample below)
  - Optional files: `previewPropertyImage`, `previewPropertyImage2`, `previewHousePlan1`, `previewHousePlan2`
- Sample `jsonData` (CompleteProperty_Dto):
```
{
  "propertyDetails": {
    "pdNewpropertynoVc": "100123",
    "pdZoneI": "1",
    "pdWardI": "10",
    "pdOldpropnoVc": "555001",
    "pdSurypropnoVc": "200456",
    "pdLayoutnameVc": "Green Park",
    "pdLayoutnoVc": "A-12",
    "pdKhasranoVc": "KH123",
    "pdPlotnoVc": "45",
    "pdGrididVc": "G12",
    "pdRoadidVc": "R9",
    "pdParcelidVc": "P100",
    "pdOwnernameVc": "John Doe",
    "pdAddnewownernameVc": "Jane Doe",
    "pdPannoVc": "ABCDE1234F",
    "pdAadharnoVc": "123412341234",
    "pdContactnoVc": "9999999999",
    "pdPropertynameVc": "Doe Residency",
    "pdPropertyaddressVc": "123 Main Street, Ward 10",
    "pdPincodeVc": "444001",
    "pdCategoryI": "1",
    "pdPropertytypeI": "1",
    "pdPropertysubtypeI": "101",
    "pdUsagetypeI": "1",
    "pdUsagesubtypeI": "11",
    "pdBuildingtypeI": "1",
    "pdBuildingsubtypeI": "10",
    "pdConstAgeI": "5",
    "pdConstYearVc": "2019",
    "pdPermissionstatusVc": "APPROVED",
    "pdPermissionnoVc": "PRM-2020-001",
    "pdPermissiondateDt": "2020-01-10",
    "pdNooffloorsI": "2",
    "pdNoofroomsI": "5",
    "pdStairVc": "YES",
    "pdLiftVc": "NO",
    "pdRoadwidthF": 30.0,
    "pdToiletstatusVc": "ATTACHED",
    "pdToiletsI": "2",
    "pdSeweragesVc": "YES",
    "pdSeweragesType": "UNDERGROUND",
    "pdWaterconnstatusVc": "YES",
    "pdWaterconntypeVc": "Municipal",
    "pdMcconnectionVc": "YES",
    "pdMeternoVc": "MTR1001",
    "pdConsumernoVc": "CNS2002",
    "pdConnectiondateDt": "2021-06-01",
    "pdConnectiondateDt_vc": "2021-06-01",
    "pdPipesizeF": 1.0,
    "pdElectricityconnectionVc": "YES",
    "pdElemeternoVc": "EM1001",
    "pdEleconsumernoVc": "EC2002",
    "pdRainwaterhaverstVc": "NO",
    "pdSolarunitVc": "NO",
    "pdPlotareaF": 120.0,
    "pdTotbuiltupareaF": 90.0,
    "pdTotcarpetareaF": 80.0,
    "pdTotalexempareaF": 0.0,
    "pdAssesareaF": 90.0,
    "pdArealetoutF": 0.0,
    "pdAreanotletoutF": 90.0,
    "pdCurrassesdateDt": "2024-04-01",
    "pdOldrvFl": 0.0,
    "user_id": "operator1",
    "pdPolytypeVc": "RCC",
    "pdStatusbuildingVc": "COMPLETED",
    "pdLastassesdateDt": "2023-04-01",
    "pdFirstassesdateDt": "2020-04-01",
    "pdNoticenoVc": "NTC-2024-001",
    "pdIndexnoVc": "IDX-100123",
    "pdNewfinalpropnoVc": "100123",
    "pdTaxstatusVc": "1",
    "pdChangedVc": "0",
    "pdNadetailsVc": "",
    "pdNanumberI": "",
    "pdNadateDt": "",
    "pdTddetailsVc": "",
    "pdTdordernumF": 0.0,
    "pdTddateDt": "",
    "pdSaledeedI": "",
    "pdSaledateDt": "",
    "pdCitysurveynoF": 0.0,
    "pdOwnertypeVc": "OWNER",
    "pdBuildingvalueI": "0",
    "pdPlotvalueF": 0.0,
    "pdTpdateDt": "",
    "pdTpdetailsVc": "",
    "pdTpordernumF": 0.0,
    "pdOccupinameF": "John Doe",
    "pdPropimageT": null,
    "pdHouseplanT": null,
    "pdPropimage2T": null,
    "pdHouseplan2T": null,
    "pdSignT": null,
    "pdFinalpropnoVc": "100123",
    "propRefno": "300789",
    "pdImgstatusVc": "PRESENT",
    "pdSuryprop2Vc": "0",
    "pdSuryprop1Vc": "200456",
    "createddateVc": "2024-06-01",
    "pdOldwardNoVc": "05",
    "pdOldTaxVc": "0",
    "unitDetails": [
      {
        "pdNewpropertynoVc": "100123",
        "udFloorNoVc": "G",
        "udUnitNoVc": 1,
        "udOccupantStatusI": "1",
        "udRentalNoVc": "",
        "udAreaBefDedF": 45.0,
        "udOccupierNameVc": "John Doe",
        "udAgreementCopyVc": "",
        "udMobileNoVc": "9999999999",
        "udEmailIdVc": "john@example.com",
        "udUsageTypeI": "1",
        "udUsageSubtypeI": "11",
        "udConstAgeI": "5",
        "udConstructionClassI": "A",
        "udAgeFactorI": "1.00",
        "udCarpetAreaF": 45.0,
        "udExemptedAreaF": 0.0,
        "udAssessmentAreaF": 45.0,
        "udSignatureVc": "",
        "udTotLegAreaF": 45.0,
        "udTotIllegAreaF": 0.0,
        "udUnitRemarkVc": "",
        "udConstYearDt": "2019",
        "udEstablishmentNameVc": "",
        "udTenantNoI": "",
        "udAgeFactVc": "1.00",
        "udPlotAreaFl": 120.0,
        "udAssVc": "OK",
        "unitBuiltupUps": [
          {
            "pdNewpropertynoVc": "100123",
            "udFloorNoVc": "G",
            "udUnitNoVc": 1,
            "ubIdsI": "1",
            "ubDedpercentI": "0",
            "ubareabefdedFl": 45.0,
            "ubplotareaFl": 120.0,
            "ubRoomTypeVc": "ROOM",
            "ubLengthFl": 5.0,
            "ubBreadthFl": 9.0,
            "ubExemptionsVc": "NONE",
            "ubExemLengthFl": 0.0,
            "ubExemBreadthFl": 0.0,
            "ubExemAreaFl": 0.0,
            "ubCarpetAreaFl": 45.0,
            "ubAssesAreaFl": 45.0,
            "ubLegalStVc": "LEGAL",
            "ubLegalAreaFl": 45.0,
            "ubIllegalAreaFl": 0.0,
            "udUnitRemarkVc": "",
            "ubMeasureTypeVc": "FT"
          }
        ]
      }
    ]
  }
}
```
- Status:
  - 200 OK with `{ "message": "Property details submitted successfully." }` on success
  - 400 Bad Request on invalid `jsonData`
  - 401 Unauthorized if no session
  - 500 Internal Server Error on processing error

Read – Get Property
- Endpoint: GET `/3gSurvey/details/{pdSuryPropNo}`
- Why: Fetch complete property by survey property number (string numeric-only).
- Response: `CompleteProperty_Dto`
- Status: 200 OK; 404 Not Found; 500 Internal Server Error

- Endpoint: GET `/3gSurvey/detailsComplete/{pdNewPropNo}`
- Why: Fetch complete property by new property number (string numeric-only).
- Response: `CompleteProperty_Dto`
- Status: 200 OK; 404 Not Found; 500 Internal Server Error

Update – Existing Property
- Endpoint: PATCH `/3gSurvey/submitUpdatedPropertyDetails`
- Why: Updates an existing complete property. Same structure as create but only changed fields are required.
- Content-Type: `multipart/form-data`
- Parts:
  - `updatedFields`: JSON of `CompleteProperty_Dto` (partial allowed)
  - Optional files: `propertyImage`, `propertyImage2`, `housePlan1`, `housePlan2`
- Minimal sample `updatedFields` JSON:
```
{
  "propertyDetails": {
    "pdNewpropertynoVc": "100123",
    "pdOwnernameVc": "John Q. Doe",
    "pdPropertyaddressVc": "456 Updated Street, Ward 10"
  }
}
```
- Status:
  - 200 OK with `{ message, updatedProperty }`
  - 401 Unauthorized if no session
  - 404 Not Found if target not found
  - 500 Internal Server Error on errors

Delete – New Property
- Endpoint: POST `/3gSurvey/deleteNewProperty`
- Why: Deletes a complete property and stores a deletion log with images.
- Content-Type: `application/json`
- Request JSON:
```
{
  "pdNewpropertynoVc": "100123",
  "ward": "10",
  "surveyPropNo": "200456",
  "ownerName": "John Doe",
  "createdBy": "operator1",
  "remarks": "Duplicate record"
}
```
- Response JSON:
```
{ "success": true, "message": "Property deleted successfully." }
```
- Status:
  - 200 OK on success
  - 500 Internal Server Error with message on failure

Notes
- Property numbers (new, survey, final) are strings with numeric-only content (e.g., "100123").
- Follow I/Vc/Fl suffix meanings when constructing payloads.

Old Property – Register/Read/Update/Delete

Register (Create)
- Endpoint: POST `/3gSurvey/submitPropertyOldDetails`
- Why: Creates or upserts an old property record with optional unit breakdown.
- Content-Type: `application/json`
- Body (PropertyOldDetails_Dto):
```
{
  "podBuiltUpAreaFl": 90.0,
  "podZoneI": "1",
  "podWardI": "10",
  "podOldPropNoVc": "555001",
  "podNewPropertyNoVc": "100123",
  "podOwnerNameVc": "John Doe",
  "podOccupierNameVc": "John Doe",
  "podPropertyAddressVc": "123 Main Street, Ward 10",
  "podConstClassVc": "A",
  "podPropertyTypeI": "1",
  "podPropertySubTypeI": "101",
  "podUsageTypeI": "1",
  "podNoofrooms": "5",
  "podPlotvalue": "0",
  "podTotalPropertyvalue": "0",
  "podOccupancyStatusVc": "OWNER",
  "podBuildingvalue": "0",
  "podLastAssDtDt": "2023-04-01",
  "podCurrentAssDt": "2024-04-01",
  "podTotalTaxFl": "0",
  "podPropertyTaxFl": "0",
  "podEduCessFl": "0",
  "podEgcFl": "0",
  "podTreeTaxFl": "0",
  "podEnvTaxFl": "0",
  "podFireTaxFl": "0",
  "pdNewPropertyNoVc": "100123",
  "pdConstructionYearD": "2019",
  "unitDetails": [
    {
      "podOccupancy": "OWNER",
      "podFloorNumber": "G",
      "podOldPropertyClass": "A",
      "podConstructionYear": "2019",
      "podUnitUsageType": "1",
      "podUnitSubUsageType": "11",
      "podAssessmentArea": "45.0",
      "podWardNo": "10",
      "podOldPropNo": "555001"
    }
  ]
}
```
- Status: 200 OK (controller uses flags `oldpresent`/`error` for hints; still 200)

Read
- Endpoint: GET `/3gSurvey/OldProperty?oldPropertyNo={oldPropNo}&wardNo={ward}`
  - Why: Fetch old property details by old property number and ward.
  - Status: 200 OK | 404 Not Found | 500 Internal Server Error
- Endpoint: GET `/3gSurvey/OldPropertyByRefNo?podRefNo={id}`
  - Why: Fetch by internal reference number.
  - Status: 200 OK | 404 Not Found | 500 Internal Server Error

Update
- Endpoint: PUT `/3gSurvey/updateOldProperty`
- Why: Updates an existing old property. Requires a valid `podRefNoVc` (> 0).
- Content-Type: `application/json`
- Body (partial allowed):
```
{
  "podRefNoVc": 123,
  "podOwnerNameVc": "John Q. Doe",
  "podPropertyAddressVc": "456 Updated Street, Ward 10",
  "unitDetails": [
    {
      "id": 1,
      "podAssessmentArea": "46.5"
    }
  ]
}
```
- Status: 200 OK (returns updated DTO) | 404 Not Found | 500 Internal Server Error

Delete
- Endpoint: DELETE `/3gSurvey/delete/{oldPropertyNo}/{wardNo}`
- Why: Deletes an old property and related units by composite key.
- Status: 200 OK | 500 Internal Server Error
