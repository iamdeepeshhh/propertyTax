# UI Documentation

This document indexes the HTML templates and JavaScript assets, and maps known routes to views. It helps new developers navigate the front-end structure and connect pages to backend endpoints.

## Structure
- Templates (Thymeleaf/HTML): `src/main/resources/templates`
- Static assets (CSS/JS): `src/main/resources/static/<PageName>/{css,js}`

## Route → View (known mappings)
From `MainController`:
- `/` → `3GCommon`
- `/specialNotice/{wardNo}` → `3GViewSpecialNotice`
- `/orderSheet` → `3GViewOrderSheet`
- `/citizenLogin` → `3GCitizenPage`
- `/hearingNotice` → `3GViewHearingNotice`
- `/objectionReciept` → `3GViewObjectionReciept`
- `/taxBill/{wardNo}` → `3GViewTaxBill`
- `/rtcc/{pdNewpropertyno}` → `3GRealtimeCC`

From `MasterWebController` and `PropertySurveyController` (examples):
- `/3g/MasterWebLogin` → `3GMasterWebLogin`
- `/3g/MasterWebSignup` → `3GMasterWebSignUp`
- `/3g/userManagement` → `3GUserManagement`
- `/3g/masterSheet` → `3GMasterSheet`
- `/3gSurvey/surveyLogin` → `3GSurveyLogin`
- `/3gSurvey/signup` → `3GSignUp`
- `/3gSurvey/form` → `3GSurveyForm`
- `/3gSurvey/afterlogin` → `3GAfterLogin`
- Additional templates returned by controllers include: `3GMasterWeb`, `3GRealtimeCC`, `3GSearchSurveyForm`, `3GViewCalculationSheet`, and other `3GView*` pages.

Refer to the controllers for the full list and any conditional routing.

## Templates Index
- 3GAfterLogin.html
- 3GBatchAssessmentReport.html
- 3GBatchCalculationReport.html
- 3GCitizenPage.html
- 3GCommon.html
- 3GDesktopEditForm.html
- 3GEditSurveyForm.html
- 3GMasterSheet.html
- 3GMasterWeb.html
- 3GMasterWebLogin.html
- 3GMasterWebSignUp.html
- 3gmasterwebv1.html
- 3GNewOrOldRegisteration.html
- 3GOldDetailsEditForm.html
- 3GOldDetailsForm.html
- 3GRealtimeCC.html
- 3GSearchSurveyForm.html
- 3GSignUp.html
- 3GSurveyForm.html
- 3GSurveyLogin.html
- 3gsurveyv1.html
- 3GUserManagement.html
- 3GViewCalculationSheet.html
- 3GViewHearingNotice.html
- 3GViewObjectionReciept.html
- 3GViewOrderSheet.html
- 3GViewSpecialNotice.html
- 3GViewSurveyFrom.html
- 3GViewTaxBill.html
- popup.html

## JavaScript Assets Index
- 3GAfterLogin/js/scripts.js
- 3GBatchAssessmentReport/js/scripts.js
- 3GBatchCalculationReport/js/scripts.js
- 3GCitizenPage/js/scripts.js
- 3GCommon/js/scripts.js
- 3GDesktopEditForm/js/scripts.js
- 3GEditForm/js/scripts.js
- 3GMasterWeb/js/scripts.js
- 3GMasterWebLogin/js/scripts.js
- 3GMasterWebSignUp/js/scripts.js
- 3GNewOrOldRegistration/js/scripts.js
- 3GOldDetailsEditForm/js/scripts.js
- 3GOldDetailsForm/js/scripts.js
- 3GRealtimeCC/js/scripts.js
- 3GSearchSurveyForm/js/scripts.js
- 3GSignup/js/scripts.js
- 3GSurveyForm/js/scripts.js
- 3GSurveyLogin/js/scripts.js
- 3GUserManagement/js/scripts.js
- 3GViewCalculationSheet/js/scripts.js
- 3GViewHearingNotice/js/scripts.js
- 3GViewObjectionReciept/js/scripts.js
- 3GViewOrderSheet/js/scripts.js
- 3GViewSpecialNotice/js/scripts.js
- 3GViewSurveyForm/js/scripts.js
- 3GViewTaxBill/js/scripts.js

## How to Document Each Page (pattern)
For each template, capture:
- Purpose: What the page is for.
- Route(s): URL(s) that render this template.
- Assets: Linked JS/CSS.
- Inputs: Key forms/fields and validations.
- API calls: Endpoints hit by its JS (see docs/API.md).
- Notes: Security, performance, or UX considerations.

Example stub (fill incrementally):
- Page: `3GUserManagement`
  - Routes: `/3g/userManagement`
  - Assets: `static/3GUserManagement/js/scripts.js`, `.../css/styles.css`
  - API calls: `/3g/getAllUsers`, `/3g/updateUser`, `/3g/deleteUser/{username}`
  - Notes: Requires session role `ITA` to list users.

If you want, I can auto-extract script tags per template and scan JS for `fetch`/`$.ajax`/`axios` endpoints to prefill API calls per page.

## Per-Page Summary (auto-detected)
The following sections summarize each template’s linked JS and detected API calls from those scripts.

- Page: 3GAfterLogin.html
  - Scripts: static/3GAfterLogin/js/scripts.js
  - API calls: /3g/getCouncilDetails, /3gSurvey/getPropertiesCount

- Page: 3GBatchAssessmentReport.html
  - Scripts: static/3GBatchAssessmentReport/js/scripts.js
  - API calls: /3g/getAllUnitUsageTypes, /3g/getAllAssessmentDates, /3g/getCouncilDetails, /3g/propertyBatchReport

- Page: 3GBatchCalculationReport.html
  - Scripts: static/3GBatchCalculationReport/js/scripts.js
  - API calls: /3g/getCouncilDetails

- Page: 3GCitizenPage.html
  - Scripts: static/3GCitizenPage/js/scripts.js
  - API calls: /3g/submitObjection, /3g/getCouncilDetails

- Page: 3GCommon.html
  - Scripts: (none detected)
  - API calls: (none detected)

- Page: 3GDesktopEditForm.html
  - Scripts: (none detected)
  - API calls: (none detected)

- Page: 3GEditSurveyForm.html
  - Scripts: static/3GEditForm/js/scripts.js
  - API calls: /3g/constructionAgeFactor, /3gSurvey/getAllAssessmentDates, /3gSurvey/submitUpdatedPropertyDetails

- Page: 3GMasterSheet.html
  - Scripts: (none detected)
  - API calls: (none detected)

- Page: 3GMasterWeb.html
  - Scripts: static/3GMasterWeb/js/scripts.js
  - API calls: /3g/getCouncilDetails, /3g/getAllRVTypes, /3g/uploadExcel, /3g/getAllConsolidatedTaxes, /3g/saveCouncilDetails, /3gSurvey/getPropertiesCount, /3gSurvey/deleteNewProperty

- Page: 3GMasterWebLogin.html
  - Scripts: static/3GMasterWebLogin/js/scripts.js
  - API calls: /3g/getCouncilDetails

- Page: 3GMasterWebSignUp.html
  - Scripts: static/3GMasterWebSignUp/js/scripts.js
  - API calls: /3g/profiles

- Page: 3gmasterwebv1.html
  - Scripts: (none detected)
  - API calls: (none detected)

- Page: 3GNewOrOldRegisteration.html
  - Scripts: static/3GNewOrOldRegistration/js/scripts.js
  - API calls: (none detected)

- Page: 3GOldDetailsEditForm.html
  - Scripts: static/3GOldDetailsEditForm/js/scripts.js
  - API calls: /3gSurvey/updateOldProperty

- Page: 3GOldDetailsForm.html
  - Scripts: static/3GOldDetailsForm/js/scripts.js
  - API calls: (none detected)

- Page: 3GRealtimeCC.html
  - Scripts: static/3GRealtimeCC/js/scripts.js
  - API calls: /3g/getAllConsolidatedTaxes

- Page: 3GSearchSurveyForm.html
  - Scripts: static/3GSearchSurveyForm/js/scripts.js
  - API calls: /3gSurvey/getUserProfileFromSession, /3gSurvey/deleteNewProperty

- Page: 3GSignUp.html
  - Scripts: static/3GSignup/js/scripts.js
  - API calls: /3gSurvey/profiles

- Page: 3GSurveyForm.html
  - Scripts: static/3GSurveyForm/js/scripts.js
  - API calls: /3g/constructionAgeFactor, /3gSurvey/getAllAssessmentDates, /3gSurvey/submitNewPropertyDetails

- Page: 3GSurveyLogin.html
  - Scripts: static/3GSurveyLogin/js/scripts.js
  - API calls: /3g/getCouncilDetails

- Page: 3gsurveyv1.html
  - Scripts: (none detected)
  - API calls: (none detected)

- Page: 3GUserManagement.html
  - Scripts: static/3GUserManagement/js/scripts.js
  - API calls: /3g/downloadUsersExcel, /3g/getCouncilDetails, /3g/getAllUsers, /3g/updateUser

- Page: 3GViewCalculationSheet.html
  - Scripts: static/3GViewCalculationSheet/js/scripts.js
  - API calls: /3g/getAllAssessmentDates, /3g/getCouncilDetails

- Page: 3GViewHearingNotice.html
  - Scripts: static/3GViewHearingNotice/js/scripts.js
  - API calls: /3g/getAllAssessmentDates, /3g/getCouncilDetails

- Page: 3GViewObjectionReciept.html
  - Scripts: static/3GViewObjectionReciept/js/scripts.js
  - API calls: (none detected)

- Page: 3GViewOrderSheet.html
  - Scripts: static/3GViewOrderSheet/js/scripts.js
  - API calls: /3g/getAllAssessmentDates, /3g/getCouncilDetails

- Page: 3GViewSpecialNotice.html
  - Scripts: static/3GViewSpecialNotice/js/scripts.js
  - API calls: /3g/getAllAssessmentDates, /3g/getCouncilDetails

- Page: 3GViewSurveyFrom.html
  - Scripts: static/3GViewSurveyForm/js/scripts.js
  - API calls: /3g/getCouncilDetails

- Page: 3GViewTaxBill.html
  - Scripts: static/3GViewTaxBill/js/scripts.js
  - API calls: /3g/getCouncilDetails

- Page: popup.html
  - Scripts: (none detected)
  - API calls: (none detected)
