function removeSpacesAndCheck(input) {
input.value = input.value.replace(/\s+/g, '');
checkSurveyPropNo();
}

const sectionToken = new URLSearchParams(window.location.search).get('sectionToken');

function checkPodRef() {
document.getElementById('newOwnerName').disabled = document.getElementById('podRef').value.trim() === '';
}
document.getElementById('podRef').addEventListener('input', checkPodRef);
window.onload = checkPodRef;

document.getElementById('sewerage').addEventListener('change', function() {
document.getElementById('sewerageType').disabled = this.value !== 'Drain';
});
function openTab(tabName) {

var tabContents = document.getElementsByClassName("tab-content");
for (var i = 0; i < tabContents.length; i++) {
tabContents[i].style.display = "none";
}
document.getElementById(tabName + "TabContent").style.display = "block";

}

function Next(tabName) {
openTab(tabName);
}

function Back(tabName) {
openTab(tabName);
}

document.addEventListener('DOMContentLoaded', function() {
var selectedProperty = localStorage.getItem('selectedProperty');
selectedProperty = selectedProperty ? JSON.parse(selectedProperty) : null;
populateYearDropdown('constYear');
fetchAndPopulateAssessmentDates();
fetchAllTypesOfAPI('/3gSurvey/getAllOldWards', 'oldWard', selectedProperty);
initializePropertyTypesAndCaptureSelection('propertyType', '/3gSurvey/propertytypes');
initializeTypesAndCaptureSelection('propertyType', 'propertySubType', '/3gSurvey/propertySubtypes');
initializeTypesAndCaptureSelection('propertySubType', 'propertyusageType', '/3gSurvey/usageTypes');
initializeTypesAndCaptureSelection('propertyusageType', 'propertyusageSubType', '/3gSurvey/usageSubtypes');
initializeTypesAndCaptureSelection('propertyType', 'buildingType', '/3gSurvey/getBuildingTypesByPropertyClassification');
initializeTypesAndCaptureSelection('buildingType', 'buildingSubType', '/3gSurvey/buildingSubtypes');

if (selectedProperty) {
console.log(selectedProperty);
document.getElementById('podRef').value = selectedProperty.podRefNoVc || '';
document.getElementById('oldWard').value = selectedProperty.podWardI || '';
document.getElementById('oldPropertyNo').value = selectedProperty.podOldPropNoVc || '';
document.getElementById('ownerName').value = selectedProperty.podOwnerNameVc || '';
document.getElementById('address').value = selectedProperty.podPropertyAddressVc || '';
document.getElementById('occupierName').value = selectedProperty.podOccupierNameVc || '';
localStorage.removeItem('selectedProperty');
}

fetch('/3g/constructionAgeFactor')
.then(response => response.json())
.then(data => {
constructionAgeFactors = data;
})
.catch(error => console.error('Error fetching construction age factors:', error));

document.querySelectorAll('[id^="classOfProperty"]').forEach(dropdown => {
disableFieldsIfClassOfPropertyOp(dropdown);
});
});

function disableFieldsIfClassOfPropertyOp(dropdown) {
console.log("disablefields");
const unitId = dropdown.id.replace('classOfProperty', '');
const isOpSelected = dropdown.value === 'op';

// Fetch the data-name attribute of the selected option
const selectedOption = dropdown.selectedOptions[0];
const dataName = selectedOption.getAttribute('data-name') || '';
console.log(`Selected data-name: ${dataName}`);

// Disable fields if "op" is selected or dataName contains "o.p"
const shouldDisable = isOpSelected || dataName.toLowerCase().includes('o.p') || dataName.toLowerCase().includes('op') || dataName.trim().toLowerCase().includes('openplot');

document.getElementById(`constructionYear${unitId}`).disabled = shouldDisable;
document.getElementById(`constructionAge${unitId}`).disabled = shouldDisable;
document.getElementById(`constAgeFactor${unitId}`).disabled = shouldDisable;

}
fetchAllTypesOfAPI('/3gSurvey/getAllZones', 'zone');
fetchAllTypesOfAPI('/3gSurvey/getAllWards', 'ward');
fetchAllTypesOfAPI('/3gSurvey/getOwnerType', 'ownerType');
fetchAllTypesOfAPI('/3gSurvey/buildstatuses', 'buildingStatus');
fetchAllTypesOfAPI('/3gSurvey/ownerCategories', 'category');
fetchAllTypesOfAPI('/3gSurvey/getSewerageTypes', 'sewerageType');
fetchAllTypesOfAPI('/3gSurvey/waterConnections', 'waterOptions');

async function fetchAllTypesOfAPI(apiUrl, selectElementId, selectedProperty = null) {
fetch(apiUrl)
.then(response => {
if (!response.ok) {
    throw new Error(`Network response was not ok for URL: ${apiUrl}`);
}
return response.json();
})
.then(data => {
const selectElement = document.getElementById(selectElementId);


const defaultOption = new Option("Select", "");
selectElement.add(defaultOption);


data.forEach(item => {
    const option = new Option(item.name, item.value);
    option.setAttribute('data-name', item.name); 
    option.setAttribute('data-deduction', item.deduction);
    selectElement.add(option);
});


if (selectedProperty) {
    Array.from(selectElement.options).forEach(option => {
    if (option.text === selectedProperty) { // Compare the name (display text)
        option.selected = true; // Mark this option as selected
    }
});
    // selectElement.value = selectedProperty;
} else {
    selectElement.value = ""; 
}


selectElement.addEventListener('change', () => logDeductionPercentage(selectElementId.replace('classOfProperty', '')));
})
.catch(error => {
console.error('Error fetching data:', error);
});
}

function openPopup(unitId) {
var popupId = 'popup-' + unitId;
var popup = document.getElementById(popupId);

if (!popup) {
popup = document.createElement('div');
popup.id = popupId;
popup.className = 'popup-container';
popup.innerHTML = getPopupContent(unitId);
document.body.appendChild(popup);
}

popup.style.display = 'block';
}


function getPopupContent(unitId) {
return `
<span class="close-btn" onclick="closePopup('${unitId}')">&times;</span>
<h2 style="text-align: center;">Flat Details</h2>
<div>
<form>
<div id="tableContainer" style="overflow-x: auto; overflow-y: auto; max-height: 400px; padding: 20px; background-color: #fff; border: 1px solid #ccc; box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);">
    <table id="table-${unitId}" style="max-width: auto; height: 100px; overflow: auto;">
    <thead>
        <tr>
            <th>Room</th>
            <th>Use</th>
            <th>Inner/Outer</th>
            <th>Deduction %</th>
            <th>Length</th>
            <th>Breadth</th>
            <th>Area Before Deduction</th>
            <th>Carpet Area</th>
            <th>Plot Area</th>
            <th>Exemption</th>
            <th>Exemption Length</th>
            <th>Exemption Breadth</th>
            <th>Exemption Area</th>
            <th>Assumption Area</th>
            <th>Legal/Illegal</th>
            <th>Legal Assmt Area</th>
            <th>Illegal Assmt Area</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <!-- Rows will be dynamically added here -->
    </tbody>
    <tr id="total-${unitId}" style="background-color: whitesmoke">
            <td colspan="4"><center>Total Area</center></td>
            <td><td><td><input type="number" id="TotalAreaBeforeDeduction-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
            <td><input type="number" id="TotalCarpetArea-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
            <td><input type="number" id="TotalPlotArea-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
            <td></td><td></td><td></td>
            <td><input type="number" id="ExemptionArea1-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
            <td><input type="number" id="AssArea1-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
            <td></td>
            <td><input type="number" id="LegalAssmtArea1-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
            <td><input type="number" id="IllegalAssmtArea1-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
            <td></td>
        </tr>
</table>
</div>
</form>
</div>
<button type="button" onclick="saveAndClosePopup('${unitId}')">Save</button>
<button onclick="addRow('${unitId}')">Add Area</button>
`;
}
//here i am searching survypropno if it exists show error
let isSurveyPropNoValid = false;

async function checkSurveyPropNo() {
if (sectionToken === '124') {
    // Backend will generate; bypass front-end uniqueness check
    const messageElement = document.getElementById('surveyPropNoMessage');
    if (messageElement) messageElement.style.display = 'none';
    isSurveyPropNoValid = true;
    return;
}
console.log("Checking Survey Property Number");
const surveyPropNo = document.getElementById('surveyPropNo').value;
const ward = document.getElementById('ward').value;
const messageElement = document.getElementById('surveyPropNoMessage');

if (surveyPropNo && ward) {
try {
    const response = await fetch(`/3gSurvey/checkSurveyPropNo?surveyPropNo=${surveyPropNo}&ward=${ward}`);
    const exists = await response.json();

    if (exists) {
        messageElement.style.display = 'block';
        isSurveyPropNoValid = false;
    } else {
        messageElement.style.display = 'none';
        isSurveyPropNoValid = true;
    }
} catch (error) {
    console.error('Error checking survey property number:', error);
    messageElement.style.display = 'none';
    isSurveyPropNoValid = false;
}
} else {
messageElement.style.display = 'none';
isSurveyPropNoValid = false;
}
}
//here done

//here we are populating picklist values so code is written below for it+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//all code will be written here

async function initializePropertyTypesAndCaptureSelection(selectId, apiUrl) {
const selectElement = document.getElementById(selectId);

// Fetch the property types from the API and populate the select element.
try {
const response = await fetch(apiUrl);
if (!response.ok) throw new Error('Failed to fetch data');
const propertyTypes = await response.json();

let optionsHtml = '<option value="">Select</option>';
propertyTypes.forEach(type => {
    optionsHtml += `<option value="${type.value}" data-standard-name="${type.Standardname.replace(/\s+/g, '').toLowerCase()}">${type.name}</option>`;
});
selectElement.innerHTML = optionsHtml;
} catch (error) {
console.error(`Error initializing property types for select #${selectId}:`, error);
}

// Hardcoded parameters for field deactivation
const conditionValue = 'openland'; // Ensure this is in lowercase and without spaces
const fieldsToDeactivate = ['constYear', 'constAgeProperty', 'ageFactorProperty', 'currentDateProperty','builtUpArea'];

// Listen for changes to the select element to handle field deactivation.
selectElement.addEventListener('change', function() {
const selectedOption = this.options[this.selectedIndex];
const standardName = selectedOption.getAttribute('data-standard-name');
handleFieldDeactivation(standardName, conditionValue, fieldsToDeactivate);
});
}


function handleFieldDeactivation(standardName, conditionValue, fieldsToDeactivate) {
if (standardName === conditionValue) {
fieldsToDeactivate.forEach(fieldId => {
    const field = document.getElementById(fieldId);
    if (field) {
        field.disabled = true;
        field.value = ''; // Optionally clear the field's value
    }
});
} else {
fieldsToDeactivate.forEach(fieldId => {
    const field = document.getElementById(fieldId);
    if (field) {
        field.disabled = false;
    }
});
}
}

async function initializeTypesAndCaptureSelection(propertyTypeId, subtypeSelectId, apiUrl) {
const propertyTypeSelectElement = document.getElementById(propertyTypeId);
const subtypeSelectElement = document.getElementById(subtypeSelectId);

// Listen for changes in the property type select element to fetch and populate subtypes.
propertyTypeSelectElement.addEventListener('change', async function() {
const selectedPropertyTypeId = this.value;
subtypeSelectElement.innerHTML = '<option value="">Select</option>';

if (!selectedPropertyTypeId) {
    return;
}

try {
    // Fetch the property subtypes based on the selected property type ID.
    const response = await fetch(`${apiUrl}/${selectedPropertyTypeId}`);
    if (!response.ok) throw new Error('Failed to fetch property subtypes');
    const propertySubtypes = await response.json();

    // Populate the subtype select element with options based on the fetched data.
    let optionsHtml = '<option value="">Select</option>';
    propertySubtypes.forEach(subtype => {
        optionsHtml += `<option value="${subtype.value}">${subtype.name}</option>`;
    });
    subtypeSelectElement.innerHTML = optionsHtml;

    // Reapply the saved subtype selection from localStorage if available.
    // const savedSubtypeSelectionId = localStorage.getItem(`${subtypeSelectId}-id`);
    // if (savedSubtypeSelectionId) {
    //     subtypeSelectElement.value = savedSubtypeSelectionId;
    // }

} catch (error) {
    console.error(`Error fetching property subtypes for ${subtypeSelectId}:`, error);
}
});

// Listen for changes to the subtype select element to save the selection to localStorage.
// // subtypeSelectElement.addEventListener('change', function() {
// //     const selectedOption = this.options[this.selectedIndex];
// //     const selectedId = selectedOption.value;
// //     const selectedName = selectedOption.text;
// //     console.log(`Selected Property Subtype ID: ${selectedId}, Name: ${selectedName}`);

// //     // Save selection ID and name to localStorage.
// //     localStorage.setItem(`${subtypeSelectId}-id`, selectedId);
// //     localStorage.setItem(`${subtypeSelectId}-name`, selectedName);
// });
}

async function initializeUnitUsageTypesAndCaptureSelection(propertyTypeId, subtypeSelectId, apiUrl) {
const subtypeSelectElement = document.getElementById(subtypeSelectId);

// Directly use propertyTypeId (numeric value) to fetch and populate subtypes without listening for changes on a select element
async function fetchAndPopulateSubtypes() {
subtypeSelectElement.innerHTML = '<option value="">Select</option>';

if (!propertyTypeId) {
    return;
}

try {
    // Fetch the property subtypes based on the propertyTypeId.
    const response = await fetch(`${apiUrl}/${propertyTypeId}`);
    if (!response.ok) throw new Error('Failed to fetch property subtypes');
    const propertySubtypes = await response.json();

    // Populate the subtype select element with options based on the fetched data.
    let optionsHtml = '<option value="">Select</option>';
    propertySubtypes.forEach(subtype => {
        optionsHtml += `<option value="${subtype.value}">${subtype.name}</option>`;
    });
    subtypeSelectElement.innerHTML = optionsHtml;

} catch (error) {
    console.error(`Error fetching property subtypes for ${propertyTypeId}:`, error);
}
}

// Immediately fetch and populate subtypes upon function call
fetchAndPopulateSubtypes();
}

//this is closure for populatin picklist code++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
function saveAndClosePopup(unitId) {
var totalCarpetArea = document.getElementById(`TotalCarpetArea-${unitId}`).value;
var totalplotarea = document.getElementById(`TotalPlotArea-${unitId}`).value;
var totalExemptedArea = document.getElementById(`ExemptionArea1-${unitId}`).value;
var totaAssessableArea = document.getElementById(`AssArea1-${unitId}`).value;
var totallegalarea = document.getElementById(`LegalAssmtArea1-${unitId}`).value;
var totalillegalarea = document.getElementById(`IllegalAssmtArea1-${unitId}`).value;
var totalareabefded = document.getElementById(`TotalAreaBeforeDeduction-${unitId}`).value;

if (!totalCarpetArea || isNaN(totalCarpetArea)) {
alert("Please enter a valid carpet area.");
return;
}

document.getElementById(`totalcarpetarea${unitId}`).value = totalCarpetArea;
document.getElementById(`totalexemptedarea${unitId}`).value = totalExemptedArea;
document.getElementById(`totalplotarea${unitId}`).value = totalplotarea;
document.getElementById(`assessablearea${unitId}`).value = totaAssessableArea;
document.getElementById(`totallegalarea${unitId}`).value = totallegalarea;
document.getElementById(`totaillegalarea${unitId}`).value = totalillegalarea;
document.getElementById(`totalareabeforeded${unitId}`).value = totalareabefded
updateAreas(unitId);
closePopup(unitId);
}

function closePopup(unitId) {
var popup = document.getElementById('popup-' + unitId);
if (popup) {
popup.style.display = 'none';
}
// document.getElementById('overlay').style.display = 'none';
}

function addRow(unitId) {
let tableBody = document.querySelector(`#table-${unitId} tbody`);
let newRow = tableBody.insertRow();
let rowId = `row-${unitId}-${tableBody.rows.length}`;

fetchAllTypesOfAPI('/3gSurvey/roomTypes', `use-${rowId}`, `room-${rowId}`);

let cellHtmlContent = [
`<input type="checkbox" id="room-${rowId}"  style="margin-right: 10px;" disabled>`,
`<select id="use-${rowId}" style="width: 120px; background-color: white; border-color:black;" onchange="enableDisableFields('${rowId}'); updateAreas('${unitId}'); checkUseValue('${rowId}','${unitId}');"></select>`,
`<select id="measureType-${rowId}" style="width: 90px; background-color: white; border-color:black;" onchange="updateCarpetArea('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}');"><option value="">Select</option><option value="inner">Inner</option><option value="outer">Outer</option></select>`,
`<input id="deduction-${rowId}" type="number" step="0.01" placeholder="Deduction Percentage" disabled />`,
`<input id="length-${rowId}" type="number" step="0.01" placeholder="Length" oninput="updateCarpetArea('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" />`,
`<input id="breadth-${rowId}" type="number" step="0.01" placeholder="Breadth" oninput="updateCarpetArea('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" />`,
`<input id="areabeforededuction-${rowId}" type="number" step="0.01" placeholder="Area Before Deduction" readonly />`,
`<input id="carpetArea-${rowId}" type="number" step="0.01" placeholder="Carpet Area" oninput="updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" />`,
`<input id="plotArea-${rowId}" type="number" step="0.01" placeholder="Plot Area" oninput="updateAssumptionArea('${rowId}'); updateAreas('${unitId}');" disabled />`,
`<select id="exemption-${rowId}" style="width: 90px; background-color: white; border-color:black;" onchange="enableDisableFields('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}');"><option value="no">No</option><option value="yes">Yes</option></select>`,
`<input id="exemptionLength-${rowId}" type="number" step="0.01" placeholder="Exemption Length" oninput="updateExemptionArea('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" disabled />`,
`<input id="exemptionBreadth-${rowId}" type="number" step="0.01" placeholder="Exemption Breadth" oninput="updateExemptionArea('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" disabled />`,
`<input id="exemptionArea-${rowId}" type="number" step="0.01" placeholder="Exemption Area" oninput="updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" />`,
`<input id="assumptionArea-${rowId}" type="number" step="0.01" placeholder="Assumption Area" readonly />`,
`<select id="legalIllegal-${rowId}" style="width: 90px; background-color: white; border-color:black;" onchange="updateLegalIllegalAreas('${rowId}'); updateAreas('${unitId}');"><option value="">Select</option><option value="legal">Legal</option><option value="illegal">Illegal</option></select>`,
`<input id="legalAssmtArea-${rowId}" type="number" step="0.01" placeholder="Legal Assmt Area" disabled />`,
`<input id="illegalAssmtArea-${rowId}" type="number" step="0.01" placeholder="Illegal Assmt Area" oninput="updateAreas('${unitId}'); updateLegalIllegalAreas('${rowId}');" disabled />`,
`<button type="button" onclick="removeRow(this, '${unitId}')">&#x1F5D1;</button>`
];

cellHtmlContent.forEach((html, index) => {
let cell = newRow.insertCell(index);
cell.innerHTML = html;
});

newRow.id = rowId;

// Call enableDisableFields to initialize the state of the new row
enableDisableFields(rowId);
// Call updateAreas to initialize the state of the new row
updateAreas(unitId);
// Call checkUseValue to initialize the state of the checkbox
checkUseValue(rowId, unitId);
}

function checkUseValue(rowId, unitId) {
const selectElement = document.getElementById(`use-${rowId}`);
const checkboxElement = document.getElementById(`room-${rowId}`);
const deductionElement = document.getElementById(`deduction-${rowId}`);
const measureTypeElement = document.getElementById(`measureType-${rowId}`);

if (selectElement.value == 1) {
checkboxElement.checked = true;
} else {
checkboxElement.checked = false;
}

if (measureTypeElement.value === 'outer' && selectElement.value !== 'open plot') {
deductionElement.disabled = false;
} else {
deductionElement.disabled = true;
}
updateAreas(unitId);
}
function updateoccupancyfields(unitId){
// Enable/disable fields based on occupancy selection
const occupancy = document.getElementById(`occupancy${unitId}`);
const occupierName = document.getElementById(`occupiername${unitId}`);
const tenantNo = document.getElementById(`tenantno${unitId}`);
const monthlyRent = document.getElementById(`monthlyrent${unitId}`);
const rentalDocument = document.getElementById(`rentalDocument${unitId}`);
const selectedOption = occupancy.options[occupancy.selectedIndex];
if (selectedOption.text.toLowerCase() === 'tenant') {
    occupierName.disabled = false;
    tenantNo.disabled = false;
    monthlyRent.disabled = false;
    rentalDocument.disabled = false;
} else {
    occupierName.disabled = true;
    tenantNo.disabled = true;
    monthlyRent.disabled = true;
    rentalDocument.disabled = true;
}
}

function logDeductionPercentage(unitId) {
const classOfPropertyElement = document.querySelector(`#classOfProperty${unitId}`);
if (classOfPropertyElement) {
const selectedOption = classOfPropertyElement.options[classOfPropertyElement.selectedIndex];
const deductionPercentage = parseFloat(selectedOption.getAttribute('data-deduction')) || 0; // Value is the percentage of deduction
console.log("Deduction Percentage:", deductionPercentage); // Log the deduction percentage
const deductionPercentageField = document.querySelector(`#deductionPercentageField${unitId}`);
if (deductionPercentageField) {
    deductionPercentageField.value = deductionPercentage + '%'; // Display the percentage
}
}
}
// Update the updateAreas function to log the deduction percentage during calculation
function getSelectedOptionText(selectElement) {
const selectedOption = selectElement.options[selectElement.selectedIndex];
return selectedOption.getAttribute('data-name') || selectedOption.text;
}


function updateCarpetArea(rowId) {
let useElement = document.getElementById(`use-${rowId}`);
const useText = useElement.options[useElement.selectedIndex].text.toLowerCase();
let length = parseFloat(document.getElementById(`length-${rowId}`).value) || 0;
let breadth = parseFloat(document.getElementById(`breadth-${rowId}`).value) || 0;
let measureType = document.getElementById(`measureType-${rowId}`).value;
let unitCount = rowId.split('-')[1];
let deductionPercentageField = parseFloat(document.getElementById(`deductionPercentageField${unitCount}`).value) || 0;
let deductionElement = document.getElementById(`deduction-${rowId}`);
let carpetAreaElement = document.getElementById(`carpetArea-${rowId}`);
let areaBeforeDeductionElement = document.getElementById(`areabeforededuction-${rowId}`);
let plotAreaElement = document.getElementById(`plotArea-${rowId}`);

let areaBeforeDeduction = length * breadth;

if (useText === 'open plot') {
plotAreaElement.value = areaBeforeDeduction ? areaBeforeDeduction.toFixed(2) : '';
carpetAreaElement.value = '';
areaBeforeDeductionElement.value = '';
} else {
if (measureType === "outer") {
deductionElement.value = deductionPercentageField;
let carpetArea = areaBeforeDeduction - (areaBeforeDeduction * deductionPercentageField / 100);
carpetAreaElement.value = carpetArea ? carpetArea.toFixed(2) : '';
areaBeforeDeductionElement.value = areaBeforeDeduction ? areaBeforeDeduction.toFixed(2) : '';
} else {
deductionElement.value = '';
carpetAreaElement.value = areaBeforeDeduction ? areaBeforeDeduction.toFixed(2) : '';
areaBeforeDeductionElement.value = '';
}
}

updateAssumptionArea(rowId);
updatePropertySectionTotals();
}

function updateExemptionArea(rowId) {
let exemptionLength = parseFloat(document.getElementById(`exemptionLength-${rowId}`).value) || 0;
let exemptionBreadth = parseFloat(document.getElementById(`exemptionBreadth-${rowId}`).value) || 0;
let exemptionAreaElement = document.getElementById(`exemptionArea-${rowId}`);

let exemptionArea = exemptionLength * exemptionBreadth;
exemptionAreaElement.value = exemptionArea ? exemptionArea.toFixed(2) : '';
}

function updateAssumptionArea(rowId) {
let carpetArea = parseFloat(document.getElementById(`carpetArea-${rowId}`).value) || 0;
let plotArea = parseFloat(document.getElementById(`plotArea-${rowId}`).value) || 0;
let exemptionArea = parseFloat(document.getElementById(`exemptionArea-${rowId}`).value) || 0;
let assumptionAreaElement = document.getElementById(`assumptionArea-${rowId}`);
let legalIllegalElement = document.getElementById(`legalIllegal-${rowId}`);
let legalAssmtAreaElement = document.getElementById(`legalAssmtArea-${rowId}`);
let illegalAssmtAreaElement = document.getElementById(`illegalAssmtArea-${rowId}`);

let area = plotArea || carpetArea; // Use plot area if available, otherwise use carpet area
let assumptionArea = area - exemptionArea;

// Ensure assumption area is 0 if area equals exemption area
if (area === exemptionArea) {
assumptionArea = 0;
}

assumptionAreaElement.value = assumptionArea ? assumptionArea.toFixed(2) : '0';

// Handle legal/illegal area
if (legalIllegalElement.value === 'illegal') {
let illegalArea = parseFloat(illegalAssmtAreaElement.value) || 0;
legalAssmtAreaElement.disabled = true;
illegalAssmtAreaElement.disabled = false;
legalAssmtAreaElement.value = (assumptionArea - illegalArea) ? (assumptionArea - illegalArea).toFixed(2) : '0';
} else {
legalAssmtAreaElement.disabled = false;
illegalAssmtAreaElement.disabled = true;
legalAssmtAreaElement.value = assumptionArea ? assumptionArea.toFixed(2) : '0';
illegalAssmtAreaElement.value = '';
}

updateAreas(rowId.split('-')[1]); // Update totals for the unit
updatePropertySectionTotals();
}

function updateLegalIllegalAreas(rowId) {
let assumptionArea = parseFloat(document.getElementById(`assumptionArea-${rowId}`).value) || 0;
let legalIllegalElement = document.getElementById(`legalIllegal-${rowId}`);
let legalAssmtAreaElement = document.getElementById(`legalAssmtArea-${rowId}`);
let illegalAssmtAreaElement = document.getElementById(`illegalAssmtArea-${rowId}`);

if (legalIllegalElement.value === 'illegal') {
let illegalArea = parseFloat(illegalAssmtAreaElement.value) || 0;
legalAssmtAreaElement.disabled = true;
illegalAssmtAreaElement.disabled = false;
legalAssmtAreaElement.value = (assumptionArea - illegalArea) ? (assumptionArea - illegalArea).toFixed(2) : '0';
} else {
legalAssmtAreaElement.disabled = false;
illegalAssmtAreaElement.disabled = true;
legalAssmtAreaElement.value = assumptionArea ? assumptionArea.toFixed(2) : '0';
illegalAssmtAreaElement.value = '';
}

updateAreas(rowId.split('-')[1]); // Update totals for the unit
updatePropertySectionTotals();
}

function enableDisableFields(rowId) {
let useElement = document.getElementById(`use-${rowId}`);
const useText = useElement.options[useElement.selectedIndex].text.toLowerCase();
let plotAreaElement = document.getElementById(`plotArea-${rowId}`);
let carpetAreaElement = document.getElementById(`carpetArea-${rowId}`);
let measureTypeElement = document.getElementById(`measureType-${rowId}`);
let exemptionElement = document.getElementById(`exemption-${rowId}`);
let exemptionLengthElement = document.getElementById(`exemptionLength-${rowId}`);
let exemptionBreadthElement = document.getElementById(`exemptionBreadth-${rowId}`);
let exemptionAreaElement = document.getElementById(`exemptionArea-${rowId}`);
let deductionElement = document.getElementById(`deduction-${rowId}`);
let areaBeforeDeductionElement = document.getElementById(`areabeforededuction-${rowId}`);

if (measureTypeElement.value === 'outer') {
areaBeforeDeductionElement.disabled = false;
deductionElement.disabled = false;
} else {
areaBeforeDeductionElement.disabled = true;
deductionElement.disabled = true;
}

if (useText === 'open plot' || useText == 'openplot' || useText == 'op' || useText == 'o.p') {
plotAreaElement.disabled = false;
carpetAreaElement.disabled = true;
measureTypeElement.disabled = true; // Disable inner/outer options
areaBeforeDeductionElement.disabled = true;
} else {
plotAreaElement.disabled = true;
carpetAreaElement.disabled = false;
measureTypeElement.disabled = false; // Enable inner/outer options for other uses
}

if (exemptionElement && exemptionElement.value === 'yes') {
exemptionLengthElement.disabled = useText === 'open plot';
exemptionBreadthElement.disabled = useText === 'open plot';
exemptionAreaElement.disabled = false;
} else {
exemptionLengthElement.disabled = true;
exemptionBreadthElement.disabled = true;
exemptionAreaElement.disabled = true;
}
}

function updateAreas(unitId) {
console.log("Updating totals for unit: " + unitId);

let totalCarpetArea = 0, totalExemptionArea = 0, totalAssumptionArea = 0;
let totalPlotArea = 0, totalAreaBeforeDeduction = 0; // Added totalAreaBeforeDeduction
let unitRoomCount = 0;
let totalLegalArea = 0, totalIllegalArea = 0;
let tableBody = document.querySelector(`#table-${unitId} tbody`);
let rows = tableBody.querySelectorAll('tr:not(#total-' + unitId + ')');

rows.forEach(row => {
let rowId = row.id;
let useElement = document.getElementById(`use-${rowId}`);
const useText = useElement.options[useElement.selectedIndex].text.toLowerCase();

let carpetArea = parseFloat(document.getElementById(`carpetArea-${rowId}`).value) || 0;
let plotArea = parseFloat(document.getElementById(`plotArea-${rowId}`).value) || 0;
let exemptionArea = parseFloat(document.getElementById(`exemptionArea-${rowId}`).value) || 0;
let assumptionArea = parseFloat(document.getElementById(`assumptionArea-${rowId}`).value) || 0;
let legalAssmtArea = parseFloat(document.getElementById(`legalAssmtArea-${rowId}`).value) || 0;
let illegalAssmtArea = parseFloat(document.getElementById(`illegalAssmtArea-${rowId}`).value) || 0;
let areaBeforeDeduction = parseFloat(document.getElementById(`areabeforededuction-${rowId}`).value) || 0; // Added line

if (useText !== 'open plot') {
totalCarpetArea += carpetArea;
}
totalPlotArea += plotArea;
totalExemptionArea += exemptionArea;
totalAssumptionArea += assumptionArea;
totalLegalArea += legalAssmtArea;
totalIllegalArea += illegalAssmtArea;
totalAreaBeforeDeduction += areaBeforeDeduction; // Added line

const checkbox = row.querySelector(`input[type="checkbox"]`);
if (checkbox && checkbox.checked) {
unitRoomCount++; // Increment the unit room count only if the checkbox is checked
}
});

document.getElementById(`TotalCarpetArea-${unitId}`).value = totalCarpetArea ? totalCarpetArea.toFixed(2) : '0';
document.getElementById(`TotalPlotArea-${unitId}`).value = totalPlotArea ? totalPlotArea.toFixed(2) : '0';
document.getElementById(`ExemptionArea1-${unitId}`).value = totalExemptionArea ? totalExemptionArea.toFixed(2) : '0';
document.getElementById(`AssArea1-${unitId}`).value = totalAssumptionArea ? totalAssumptionArea.toFixed(2) : '0';
document.getElementById(`LegalAssmtArea1-${unitId}`).value = totalLegalArea ? totalLegalArea.toFixed(2) : '0';
document.getElementById(`IllegalAssmtArea1-${unitId}`).value = totalIllegalArea ? totalIllegalArea.toFixed(2) : '0';
document.getElementById(`TotalAreaBeforeDeduction-${unitId}`).value = totalAreaBeforeDeduction ? totalAreaBeforeDeduction.toFixed(2) : '0'; // Added line
document.querySelector(`#unitrooms${unitId}`).value = unitRoomCount;
updatePropertySectionTotals();
}

function updatePropertySectionTotals() {
let totalCarpetArea = 0, totalExemptionArea = 0, totalAssumptionArea = 0;
let totalPlotArea = 0;
let totalLetOutArea = 0, totalNotLetOutArea = 0;
let floors = new Set();  // Set to keep track of unique floors
let totalRooms = 0;      // Variable to count the total number of rooms

// Get all unit totals and sum them up
document.querySelectorAll('[id^="TotalCarpetArea-"]').forEach(input => {
const unitId = input.id.split('-')[1]; // Extract unitId from the input id
const occupancyElement = document.querySelector(`#occupancy${unitId}`);
const floorElement = document.querySelector(`#floorNo${unitId}`); // Assuming you have an element with id 'floor-unitId'
const useElement = document.querySelector(`#use-${unitId}`);

let carpetArea = parseFloat(input.value) || 0;
let plotArea = parseFloat(document.querySelector(`#TotalPlotArea-${unitId}`).value) || 0;
let isUnitFloorOpen = floorElement && floorElement.value.toLowerCase() === 'open';

if (!isUnitFloorOpen) {
totalCarpetArea += carpetArea;
totalPlotArea += plotArea;

// Check if the occupancy element exists and then use its value
if (occupancyElement) {
    const selectedOption = occupancyElement.options[occupancyElement.selectedIndex];
    const occupancyName = selectedOption.textContent;

    if (occupancyName.toLowerCase() === 'tenant') {
        totalLetOutArea += carpetArea;
    } else {
        totalNotLetOutArea += carpetArea;
    }
}
}

// Add floor number to the set
if (!isUnitFloorOpen && floorElement && floorElement.value) {
floors.add(floorElement.value);
}

// Count the number of rooms based on checkbox selection
const tableBody = document.querySelector(`#table-${unitId} tbody`);
if (tableBody) {
tableBody.querySelectorAll('tr').forEach(row => {
    const checkbox = row.querySelector(`input[type="checkbox"]`);
    if (checkbox && checkbox.checked) {
        totalRooms++; // Increment the total rooms only if the checkbox is checked
    }
});
}
});

document.querySelectorAll('[id^="ExemptionArea1-"]').forEach(input => {
totalExemptionArea += parseFloat(input.value) || 0;
});

document.querySelectorAll('[id^="AssArea1-"]').forEach(input => {
totalAssumptionArea += parseFloat(input.value) || 0;
});

// Update plot area only if it is greater than zero
if (totalPlotArea > 0) {
document.getElementById('plotArea').value = totalPlotArea.toFixed(2);
} else {
// If no total plot area from built-up, keep the manually entered plot area
let manualPlotArea = parseFloat(document.getElementById('plotArea').value) || 0;
totalPlotArea = manualPlotArea;
}
// Update main property section inputs
document.getElementById('carpetArea').value = totalCarpetArea.toFixed(2);
document.getElementById('exemptedArea').value = totalExemptionArea.toFixed(2);
document.getElementById('assessableArea').value = totalAssumptionArea.toFixed(2);
document.getElementById('areaLetout').value = totalLetOutArea.toFixed(2);
document.getElementById('areaNotLetout').value = totalNotLetOutArea.toFixed(2);
document.getElementById('nooffloors').value = floors.size;  // Update the number of floors
document.getElementById('noofrooms').value = totalRooms;  // Update the total number of rooms
}
//function updateManualPlotArea() {
//let manualPlotArea = parseFloat(document.getElementById('plotArea').value) || 0;
//if (manualPlotArea > 0) {
//document.getElementById('plotArea').value = manualPlotArea.toFixed(2);
//}
//}document.getElementById('plotArea').addEventListener('input', updateManualPlotArea);

function createNumberInput(name) {
var input = document.createElement("input");
input.type = "number";
input.name = name;
input.style = "width: 78px; padding: 0px; background-color: white; border-color:white;";
return input;
}

function removeRow(button, unitId) {
let row = button.closest('tr');
row.parentNode.removeChild(row);
updateAreas(unitId);
}

function cancelUpload(inputId) {
var fileInput = document.getElementById(inputId);
if (fileInput) {
fileInput.value = '';
}

// Optionally, clear the image preview if one exists
// This assumes you have a corresponding image preview element for each input
var previewId = 'preview' + inputId.charAt(0).toUpperCase() + inputId.slice(1);
var previewImage = document.getElementById(previewId);
if (previewImage) {
previewImage.src = '';
}

// Optionally, you might want to log or alert if the input or preview elements are not found
if (!fileInput) {
console.error('Failed to find file input:', inputId);
}
if (!previewImage) {
console.error('Failed to find preview image:', previewId);
}
}

function togglePopup(selectId, popupId) {
var selectBox = document.getElementById(selectId);
var selectedValue = selectBox.options[selectBox.selectedIndex].value;
var popup = document.getElementById(popupId);

if (selectedValue == "yes") {
popup.style.display = "block";
} else {
popup.style.display = "none";
}
}

// <!--    function showSignaturePopup() {-->
// <!--        var signaturePopup = document.getElementById("signaturePopup");-->
// <!--        signaturePopup.style.display = "block";-->
// <!--    }-->

// <!--    function hideSignaturePopup() {-->
// <!--        var signaturePopup = document.getElementById("signaturePopup");-->
// <!--        signaturePopup.style.display = "none";-->
// <!--    }-->


// <!--    function initCanvas() {-->
// <!--        var canvas = document.getElementById("signatureCanvas");-->
// <!--        var ctx = canvas.getContext("2d");-->
// <!--        var isDrawing = false;-->
// <!--        var lastX = 0;-->
// <!--        var lastY = 0;-->

// <!--        function draw(e) {-->
// <!--            if (!isDrawing) return;-->

// <!--            e.preventDefault();-->

// <!--            var posX = e.clientX || e.touches[0].clientX;-->
// <!--            var posY = e.clientY || e.touches[0].clientY;-->
// <!--            var rect = canvas.getBoundingClientRect();-->

// <!--            posX = (posX - rect.left) / (rect.right - rect.left) * canvas.width;-->
// <!--            posY = (posY - rect.top) / (rect.bottom - rect.top) * canvas.height;-->

// <!--            ctx.beginPath();-->
// <!--            ctx.moveTo(lastX, lastY);-->
// <!--            ctx.lineTo(posX, posY);-->
// <!--            ctx.strokeStyle = 'black';-->
// <!--            ctx.lineWidth = 2;-->
// <!--            ctx.stroke();-->
// <!--            lastX = posX;-->
// <!--            lastY = posY;-->
// <!--        }-->

// <!--        function startPosition(e) {-->
// <!--            isDrawing = true;-->
// <!--            draw(e);-->
// <!--        }-->

// <!--        function finishedPosition() {-->
// <!--            isDrawing = false;-->
// <!--            ctx.beginPath();-->
// <!--        }-->

// <!--        canvas.addEventListener('mousedown', startPosition);-->
// <!--        canvas.addEventListener('mousemove', draw);-->
// <!--        canvas.addEventListener('mouseup', finishedPosition);-->
// <!--        canvas.addEventListener('mouseout', finishedPosition);-->

// <!--        canvas.addEventListener('touchstart', startPosition, { passive: false });-->
// <!--        canvas.addEventListener('touchmove', draw, { passive: false });-->
// <!--        canvas.addEventListener('touchend', finishedPosition, { passive: false });-->
// <!--    }-->
// <!--    function resizeCanvas() {-->
// <!--        var canvas = document.getElementById("signatureCanvas");-->
// <!--        var signaturePopup = document.getElementById("signaturePopup");-->
// <!--        var popupContent = document.getElementById("popupContent");-->
// <!--        canvas.width = popupContent.offsetWidth;-->
// <!--        canvas.height = popupContent.offsetHeight;-->
// <!--    }-->
// <!--    window.addEventListener('resize', resizeCanvas);-->
// <!--    window.onload = function () {-->
// <!--        initCanvas();-->
// <!--        resizeCanvas();-->
// <!--    };-->

// <!--    function clearSignature(event) {-->
// <!--        event.preventDefault();-->
// <!--        var canvas = document.getElementById("signatureCanvas");-->
// <!--        var ctx = canvas.getContext("2d");-->
// <!--        ctx.clearRect(0, 0, canvas.width, canvas.height);-->
// <!--    }-->

// <!--    function saveSignature(event) {-->
// <!--        event.preventDefault();-->
// <!--        var canvas = document.getElementById("signatureCanvas");-->
// <!--        var signature = canvas.toDataURL();-->
// <!--        document.getElementById("signature").value = signature;-->
// <!--        alert("Record saved");-->
// <!--        hideSignaturePopup();-->
// <!--    }-->

var activeFormId = null;

function toggleForm(formId) {
console.log("Toggling form:", formId);
var form = document.getElementById(formId);

var allForms = document.getElementsByClassName("formContainer");
for (var i = 0; i < allForms.length; i++) {
if (allForms[i].id !== formId) {
    allForms[i].style.display = "none";
}
}

// Toggle the display of the clicked form
if(form){
if (form.style.display === "block") {
form.style.display = "none";
activeFormId = null;
}
else {
// Hide the previously active form, if any
if (activeFormId && activeFormId !== formId) {
    var activeForm = document.getElementById(activeFormId);
    if (activeForm) {
        activeForm.style.display = "none";
    }
}
// Show the clicked form
form.style.display = "block";
activeFormId = formId;
}
}else {
console.error("Form with ID " + formId + " not found.");
}
}

document.getElementById('propertyusageType').addEventListener('change', function() {
const propertyTypeId = this.value; // Get the selected property type ID
updateFormsBasedOnPropertyType(propertyTypeId);
});

var unitCount = 1;
function addUnit() {
var existingUnitButtons = document.querySelectorAll('.unitbutton').length; //Added to make unit no dynamic and uniform
unitCount = existingUnitButtons + 1;
var unitButtons = document.getElementById("unitButtons");
var formContainers = document.getElementById("formContainers");
var existingUnitButtons = document.querySelectorAll('.unitbutton').length;
unitCount = existingUnitButtons + 1;
var formId = "myForm" + unitCount;

var button = document.createElement("button");
button.textContent = "Unit " + unitCount;
button.className = "unitbutton";
button.id = "unitButton" + unitCount;
button.type = "button";
button.onclick = function() { toggleForm(formId); };
unitButtons.appendChild(button);

var formContainer = document.createElement("div");
formContainer.id = formId;
formContainer.className = "formContainer";
formContainer.style.display = "none";

var form = document.createElement("form");
form.innerHTML = getFormInnerHTML(unitCount);
formContainer.appendChild(form);

document.getElementById("formContainers").appendChild(formContainer); //added for testing purpose

fetchAllTypesOfAPI('/3gSurvey/getUnitFloorNos', `floorNo${unitCount}`);
fetchAllTypesOfAPI('/3gSurvey/constructionClassMasters', `classOfProperty${unitCount}`);
fetchAllTypesOfAPI('/3gSurvey/occupancyMasters', `occupancy${unitCount}`);
fetchAllTypesOfAPI('/3gSurvey/getAllRemarks', `remark${unitCount}`);
// Only update forms for the newly added unit
const currentPropertyTypeId = document.getElementById('propertyusageType').value;
if (currentPropertyTypeId) {
    initializeUnitUsageTypesAndCaptureSelection(currentPropertyTypeId, `unitusageType${unitCount}`, '/3gSurvey/getUnitUsageByPropUsageId');
    initializeTypesAndCaptureSelection(`unitusageType${unitCount}`, `unitusageSubType${unitCount}`, '/3gSurvey/getUnitUsageSub');
}

var deleteButton = document.createElement("button");
deleteButton.textContent = "Delete Unit " + unitCount;
deleteButton.className = "deletebtn";
deleteButton.id = "deleteButton" + unitCount; // Unique delete button ID
deleteButton.type = "button";

deleteButton.onclick = function () {
    removeUnit(formId);
}

form.appendChild(deleteButton);

formContainers.appendChild(formContainer);

formContainer.appendChild(form);
var allForms = formContainers.getElementsByClassName("formContainer");
for (var i = 0; i < allForms.length; i++) {
    allForms[i].style.display = "none";
}

formContainer.style.display = "block";
unitCount++;

 // Add event listener for the new "Class of Property" dropdown
document.getElementById(`classOfProperty${unitCount}`).addEventListener('change', function() {
    disableFieldsIfClassOfPropertyOp(this);
});

// Initial call to handle preselected value
disableFieldsIfClassOfPropertyOp(document.getElementById(`classOfProperty${unitCount}`));
updatePropertySectionTotals();
}

function updateFormsBasedOnPropertyType(propertyTypeId) {
for (let i = 1; i <= unitCount; i++) {
const subtypeSelectId = `unitusageType${i}`;
const apiUrl = '/3gSurvey/getUnitUsageByPropUsageId';
// Update forms only if they are newly added or empty
if (!document.getElementById(subtypeSelectId).value) {
initializeUnitUsageTypesAndCaptureSelection(propertyTypeId, subtypeSelectId, apiUrl);
}
}
}

function populateYearDropdown(dropdownId) {
const yearDropdown = document.getElementById(dropdownId);
const currentYear = new Date().getFullYear();
let yearOptions = '<option value="">Select Year</option>';
for (let year = currentYear; year >= 1800; year--) {
yearOptions += `<option value="${year}">${year}</option>`;
}
yearDropdown.innerHTML = yearOptions;
}

function fetchAndPopulateAssessmentDates() {
fetch('/3gSurvey/getAllAssessmentDates')
.then(response => response.json())
.then(data => {
    if (data.length > 0) {  const { currentAssessmentDate, lastAssessmentDate, firstAssessmentDate } = data[0];
    document.getElementById('currentAssmtDate').value = currentAssessmentDate;
    document.getElementById('lastAssmtDate').value = lastAssessmentDate;
    document.getElementById('firstAssmtDate').value = firstAssessmentDate;
    }
})
.catch(error => console.error('Error fetching assessment dates:', error));
}

function getFormInnerHTML(unitCount) {
// Generate year options dynamically
const currentYear = new Date().getFullYear();
let yearOptions = '<option value="">Select Year</option>';
for (let year = currentYear; year >= 1800; year--) {
yearOptions += `<option value="${year}">${year}</option>`;
}

return '<label for="floorNo' + unitCount + '" class="mandatory">Floor No:</label>' +
'<select id="floorNo' + unitCount + '" name="floorNo">' +
'</select><br>' +
'<label for="occupancy' + unitCount + '" class="mandatory">Occupancy:</label>' +
'<select id="occupancy' + unitCount + '" name="occupancy" onchange="updateoccupancyfields(' + unitCount + ')">' +
'</select><br>' +
'<label for="monthlyrent' + unitCount + '">Monthly Rent:</label>' +
'<input type="number" style="width: 90%;" id="monthlyrent' + unitCount + '" name="monthlyrent" disabled><br>' +
'<label for="tenantno' + unitCount + '" class="mandatory">Tenant Number:</label>' +
'<input type="text" id="tenantno' + unitCount + '" name="tenantno" disabled><br>' +
'<label for="occupiername' + unitCount + '">Tenant Name:</label>' +
'<input type="text" id="occupiername' + unitCount + '" name="occupiername" disabled><br>' +
// '<label for="rentalDocument' + unitCount + '">Rental Document:</label>' +
// '<input type="file" id="rentalDocument' + unitCount + '" name="rentalDocument" disabled><br><br>' +
'<label for="unitNo' + unitCount + '" class="mandatory">Unit No.:</label>' +
'<input type="text" id="unitNo' + unitCount + '" name="unitNo" value="' + unitCount + '" readonly class="disabled-field"><br>' +
'<label id="existingElement" for="umobileNo' + unitCount + '">Mobile No:</label>' +
'<input type="text" id="umobileNo' + unitCount + '" name="umobileNo"><br>' +
'<label for="email' + unitCount + '">Email:</label>' +
'<input type="text" id="email' + unitCount + '" name="email"><br>' +
'<label for="unitusageType' + unitCount + '" class="mandatory">Usage Type:</label>' +
'<select id="unitusageType' + unitCount + '" name="unitusageType">' +
'</select><br>' +
'<label for="unitusageSubType' + unitCount + '" class="mandatory">Usage Sub Type:</label>' +
'<select id="unitusageSubType' + unitCount + '" name="unitusageSubType">' +
'</select><br>' +
'<label for="classOfProperty' + unitCount + '" class="mandatory">Class Of Property:</label>' +
'<select id="classOfProperty' + unitCount + '" name="classOfProperty" onchange="disableFieldsIfClassOfPropertyOp(this)">' +
'</select><br>' +
'<label for="deductionPercentageField' + unitCount + '">% of Deduction:</label>' +
'<input type="text" id="deductionPercentageField' + unitCount + '" name="deductionPercentageField" disabled><br>' +
'<label for="establishmentName' + unitCount + '">Establishment Name:</label>' +
'<input type="text" id="establishmentName' + unitCount + '" name="establishmentName"><br>' +
'<label for="constructionYear' + unitCount + '">Construction Year:</label>' +
'<select id="constructionYear' + unitCount + '" name="constructionYear" onchange="updateUnitAgeFactor(this.value, ' + unitCount + ')">' +
yearOptions +
'</select><br>' +
'<label for="constructionAge' + unitCount + '">Construction Age:</label>' +
'<input type="text" id="constructionAge' + unitCount + '" name="constructionAge" readonly><br>' +
'<label for="constAgeFactor' + unitCount + '">Const. Age Factor:</label>' +
'<input type="text" id="constAgeFactor' + unitCount + '" readonly class="disabled-field" name="constAgeFactor"><br>' +
'<input type="hidden" id="agefactorunithid' + unitCount + '">'+
'<input type="hidden" id="currentDateUnit' + unitCount + '" name="currentDateUnit">'+
'<label id="unitareain" for="unitArea' + unitCount + '">Unit Area:</label>' +
'<input type="text" id="unitArea' + unitCount + '" name="unitArea" onclick="openPopup(' + unitCount + ')"><br>' +
'<label id="unitrooms" for="unitrooms' + unitCount + '">Rooms:</label>' +
'<input type="text" id="unitrooms' + unitCount + '" name="unitrooms" ><br>' +
'<label id="totalplotarea" for="totalplotarea' + unitCount + '">Total Plot Area:</label>' +
'<input type="text" id="totalplotarea' + unitCount + '" name="totalplotarea"><br>' +
'<label id="totalareabeforeded" for="totalareabeforeded' + unitCount + '">Total Area before Deduction:</label>' +
'<input type="text" id="totalareabeforeded' + unitCount + '" name="totalareabeforeded"><br>' +
'<label id="totalcarpetarea" for="totalcarpetarea' + unitCount + '">Total Carpet Area:</label>' +
'<input type="text" id="totalcarpetarea' + unitCount + '" name="totalcarpetarea"><br>' +
'<label id="totalexemptedarea" for="totalexemptedarea' + unitCount + '">Total Exempted Area:</label>' +
'<input type="text" id="totalexemptedarea' + unitCount + '" name="totalexemptedarea"><br>' +
'<label id="assessablearea" for="assessablearea' + unitCount + '">Total Assessable Area:</label>' +
'<input type="text" id="assessablearea' + unitCount + '" name="assessablearea"><br>' +
'<label id="totallegalarea" for="totallegalarea' + unitCount + '">Total Legal Area:</label>' +
'<input type="text" id="totallegalarea' + unitCount + '" name="totallegalarea"><br>' +
'<label id="totaillegalarea" for="totaillegalarea' + unitCount + '">Total Illegal Area:</label>' +
'<input type="text" id="totaillegalarea' + unitCount + '" name="totaillegalarea"><br>' +
'<label for="remark' + unitCount + '">Remark:</label>' +
'<select id="remark' + unitCount + '" name="remark">' +
'</select><br><br>' +
'<div class="button-container">';
}

function calculateAgeAndUpdateFactor(selectedYear, ageFieldId, factorFieldId, hiddenFieldId, currentDateFieldId) {
if (!selectedYear) return;

const currentDate = new Date();
const currentYear = currentDate.getFullYear();
const currentMonth = String(currentDate.getMonth() + 1).padStart(2, '0'); // Adding 1 since months are 0-based
const currentDay = String(currentDate.getDate()).padStart(2, '0');

const constructionYear = new Date(selectedYear).getFullYear();
const age = currentYear - constructionYear;

console.log(`Selected Year: ${selectedYear}, Age: ${age}`);

// Populate the Construction Age field
const ageField = document.getElementById(ageFieldId);
if (ageField) {
ageField.value = age;
console.log(`Updated Age Field (${ageFieldId}): ${age}`);
}

// Determine the Construction Age Factor based on the age
const { factor, ageFactorId } = getAgeFactorFromDB(age);
console.log(`Age Factor for Age ${age}: ${factor}, ID: ${ageFactorId}`);

// Set the Construction Age Factor in a text field
const factorField = document.getElementById(factorFieldId);
if (factorField) {
factorField.value = factor;
}

// Update the hidden input field with the age factor ID
const hiddenField = document.getElementById(hiddenFieldId);
if (hiddenField) {
hiddenField.value = ageFactorId;
}

// Populate the current date field with selected year and current month and day
const formattedCurrentDate = `${selectedYear}-${currentMonth}-${currentDay}`;
const currentDateField = document.getElementById(currentDateFieldId);
if (currentDateField) {
currentDateField.value = formattedCurrentDate;
console.log(`Updated Current Date Field (${currentDateFieldId}): ${formattedCurrentDate}`);
}
}
function getAgeFactorFromDB(age) {
let factor = 'Unknown';
let ageFactorId = null;

for (const factorEntry of constructionAgeFactors) {
const minAge = parseInt(factorEntry.afm_ageminage_vc, 10);
const maxAge = parseInt(factorEntry.afm_agemaxage_vc, 10);
console.log(`Checking age range: ${minAge} - ${maxAge}`);

if (age >= minAge && age <= maxAge) {
    factor = factorEntry.afm_agefactornamell_vc;
    ageFactorId = factorEntry.afm_agefactorid_i; // Assuming afm_agefactorid_i exists in factorEntry
    break;
}
}

return { factor, ageFactorId };
}
// Usage Example for Units
function updateUnitAgeFactor(selectedYear, unitCount) {
calculateAgeAndUpdateFactor(selectedYear, `constructionAge${unitCount}`, `constAgeFactor${unitCount}`, `agefactorunithid${unitCount}`, `currentDateUnit${unitCount}`);
}

// Usage Example for Property
function updatePropertyAgeFactor(selectedYear) {
calculateAgeAndUpdateFactor(selectedYear, 'constAgeProperty', 'ageFactorProperty', 'agefactorpropertyhid', 'currentDateProperty');
}
function removeUnit(formId) {
var formContainer = document.getElementById(formId);
if (formContainer) {
formContainer.remove(); // Remove the form container
}

var popupId = 'popup-' + formId.replace("myForm", "");
var popup = document.getElementById(popupId);
if (popup) {
popup.remove(); // Remove the popup for the unit
}

var buttonId = "unitButton" + formId.replace("myForm", "");
var unitButton = document.getElementById(buttonId);
if (unitButton) {
unitButton.remove(); // Remove the unit button
}

if (typeof activeFormId !== 'undefined' && activeFormId === formId) {
activeFormId = null;
}

// Recalculate unit counts and update remaining units
var existingUnitButtons = document.querySelectorAll('.unitbutton');
unitCount = existingUnitButtons.length;  // Update global unit count

var currentUnitIndex = parseInt(formId.replace("myForm", ""));
existingUnitButtons.forEach(function(button, index) {
var expectedIndex = index + 1;
if (expectedIndex >= currentUnitIndex) {
    button.textContent = "Unit " + expectedIndex;
    button.id = "unitButton" + expectedIndex;
    button.onclick = function() { toggleForm("myForm" + expectedIndex); };

    var newFormContainer = document.getElementById("myForm" + (index + 2));
    if (newFormContainer) {
        newFormContainer.id = "myForm" + expectedIndex;
        var deleteButton = newFormContainer.querySelector(".deletebtn");
        var unitNoInput = newFormContainer.querySelector('input[name="unitNo"]');
        if (deleteButton) {
            deleteButton.textContent = "Delete Unit " + expectedIndex;
            deleteButton.id = "deleteButton" + expectedIndex;
            deleteButton.onclick = function () {
                removeUnit("myForm" + expectedIndex);
            }
        }
        if (unitNoInput) {
            unitNoInput.value = expectedIndex; // Update the Unit No. input field
        }
    }
}
});
updatePropertySectionTotals();
}

var compressedImages = {};

function compressImage(file, previewId) {
if (!file) return;  // Exit if no file is selected

const maxWidth = 525;  // Maximum width of the compressed image
const maxHeight = 700; // Maximum height of the compressed image
const quality = 9;     // Quality of the compressed image

const reader = new FileReader();
reader.onload = (event) => {
const img = new Image();
img.src = event.target.result;

img.onload = () => {
    const canvas = document.createElement('canvas');
    let width = img.width;
    let height = img.height;

    // Adjust the dimensions while maintaining the aspect ratio
    if (width > height) {
        if (width > maxWidth) {
            height *= maxWidth / width;
            width = maxWidth;
        }
    } else {
        if (height > maxHeight) {
            width *= maxHeight / height;
            height = maxHeight;
        }
    }

    canvas.width = width;
    canvas.height = height;
    const ctx = canvas.getContext('2d');
    ctx.drawImage(img, 0, 0, width, height);

    // Convert the canvas to a blob and update the image preview
    canvas.toBlob((blob) => {
        compressedImages[previewId] = blob; // Store the blob using the previewId as the key
        const imageUrl = URL.createObjectURL(blob);
        document.getElementById(previewId).src = imageUrl;
    }, 'image/jpeg', quality);
};
};

reader.readAsDataURL(file);
}

function getSelectedOptionText(selectId) {
if (!selectId) {
console.error('getSelectedOptionText: selectElement is null');
return '';
}
const selectElement = document.getElementById(selectId);
const selectedOption = selectElement.options[selectElement.selectedIndex];
return selectedOption.getAttribute('data-name') || selectedOption.text;
}

function collectFormData() {
let formData = {
propertyDetails: {
//main
propRefno: document.getElementById('podRef').value,
pdZoneI: document.getElementById('zone').value,
pdWardI: document.getElementById('ward').value,
pdCitysurveynoF: document.getElementById('citySurveyNo').value,
pdOldpropnoVc: document.getElementById('oldPropertyNo').value,
pdSurypropnoVc: document.getElementById('surveyPropNo').value,
pdLayoutnameVc: document.getElementById('layoutName').value,
pdLayoutnoVc: document.getElementById('layoutNo').value,
pdKhasranoVc: document.getElementById('khasraNo').value,
pdPlotnoVc: document.getElementById('plotNo').value,
pdOwnernameVc: document.getElementById('ownerName').value,
pdAddnewownernameVc: document.getElementById('newOwnerName').value,
pdOccupinameF: document.getElementById('occupierName').value,
pdPropertyaddressVc: document.getElementById('address').value,
pdOwnertypeVc: document.getElementById('ownerType').value,
pdStatusbuildingVc: document.getElementById('buildingStatus').value,
//property
//(a)
pdPannoVc: document.getElementById('panNo').value,
pdAadharnoVc: document.getElementById('aadharNo').value,
pdContactnoVc: document.getElementById('ownermobileNo').value,
pdCategoryI: document.getElementById('category').value,
pdPropertynameVc: document.getElementById('propertyName').value,
pdBuildingvalueI: document.getElementById('buildingValue').value,
pdPlotvalueF: document.getElementById('plotValue').value,
//(b)
pdPropertytypeI: document.getElementById('propertyType').value,
pdPropertysubtypeI: document.getElementById('propertySubType').value,
pdUsagetypeI: document.getElementById('propertyusageType').value,
pdUsagesubtypeI: document.getElementById('propertyusageSubType').value,
pdBuildingtypeI: document.getElementById('buildingType').value,
pdBuildingsubtypeI: document.getElementById('buildingSubType').value,
pdConstYearVc: document.getElementById('currentDateProperty').value,
pdConstAgeI: document.getElementById('constAgeProperty').value,
//pdagefactor
pdPermissionstatusVc: document.getElementById('permissionSelection').value,
pdPermissionnoVc: document.getElementById('permissionNumber').value,
pdPermissiondateDt: document.getElementById('permissionDate').value,
pdStairVc: document.getElementById('stair').value,
pdLiftVc: document.getElementById('lift').value,
pdRoadwidthF: document.getElementById('roadWidth').value,
//(c)
pdToiletstatusVc: document.getElementById('toiletSelection').value,
pdToiletsI: document.getElementById('numberOfToilets').value,
pdSeweragesVc: document.getElementById('sewerage').value,
pdSeweragesType: document.getElementById('sewerageType').value,
pdWaterconnstatusVc: document.getElementById('waterSelection').value,
pdWaterconntypeVc: document.getElementById('waterOptions').value,
pdMcconnectionVc: document.getElementById('mcConnection').value,
pdMeternoVc: document.getElementById('meterNumber').value,
pdConsumernoVc: document.getElementById('consumerNumber').value,
pdConnectiondateDt: document.getElementById('connectionDate').value,
pdPipesizeF: document.getElementById('pipeSize').value,
pdSolarunitVc: document.getElementById('solar').value,
pdElectricityconnectionVc: document.getElementById('electricitySelection').value,
pdElemeternoVc: document.getElementById('emeterNumber').value,
pdEleconsumernoVc: document.getElementById('econsumerNumber').value,
pdRainwaterhaverstVc: document.getElementById('rainWaterHarvesting').value,
//(d)
pdPlotareaF: document.getElementById('plotArea').value,
pdTotbuiltupareaF: document.getElementById('builtUpArea').value,
pdTotcarpetareaF: document.getElementById('carpetArea').value,
pdTotalexempareaF: document.getElementById('exemptedArea').value,
pdAssesareaF: document.getElementById('assessableArea').value,
pdArealetoutF: document.getElementById('areaLetout').value,
pdAreanotletoutF: document.getElementById('areaNotLetout').value,
pdNooffloorsI: document.getElementById('nooffloors').value,
pdNoofroomsI: document.getElementById('noofrooms').value,
pdCurrassesdateDt: document.getElementById('currentAssmtDate').value,
pdLastassesdateDt: document.getElementById('lastAssmtDate').value,
pdFirstassesdateDt: document.getElementById('firstAssmtDate').value,
pdNadetailsVc: document.getElementById('naSelection').value,
pdNanumberI: document.getElementById('naOrder').value,
pdNadateDt: document.getElementById('naDate').value,
pdTddetailsVc: document.getElementById('tdSelection').value,
pdTdordernumF: document.getElementById('tdOrder').value,
pdTddateDt: document.getElementById('tdDate').value,
pdTpdetailsVc: document.getElementById('tpSelection').value,
pdTpordernumF: document.getElementById('tpOrder').value,
pdTpdateDt: document.getElementById('tpDate').value,
pdSaledeedI: document.getElementById('saledeedSelection').value,
pdSaledateDt: document.getElementById('saledeedDate').value,
pdOldrvFl: document.getElementById('oldRV').value,
unitDetails: []
// additionalInfo: {
//     paNameVc: document.getElementById('additionalName').value,
//     paQualificationVc: document.getElementById('qualification').value,
//     paOccupationVc: document.getElementById('occupation').value,
//     paAdultnoIn: document.getElementById('noOfAdults').value,
//     paChildrennoIn: document.getElementById('noOfChildren').value,
//     paCtaxstatusVc: document.getElementById('currentTaxDetails').value,
//     paCtaxamountIn: document.getElementById('taxAmount').value,
//     paNroadnameVc: document.getElementById('roadName').value,
//     paRoadtypeVc: document.getElementById('roadType').value,
// }
}
};

document.querySelectorAll('.formContainer').forEach((unitContainer, index) => {
let unitIndex = index + 1;
let unitDetails = {
udFloorNoVc: unitContainer.querySelector(`#floorNo${unitIndex}`).value,
udUnitNoVc: parseInt(unitContainer.querySelector(`#unitNo${unitIndex}`).value, 10),
udOccupantStatusI: unitContainer.querySelector(`#occupancy${unitIndex}`).value, // Assuming this field exists
udRentalNoVc: unitContainer.querySelector(`#monthlyrent${unitIndex}`).value,
udTenantNoI: unitContainer.querySelector(`#tenantno${unitIndex}`).value,
udOccupierNameVc: unitContainer.querySelector(`#occupiername${unitIndex}`).value,
udEstablishmentNameVc: unitContainer.querySelector(`#establishmentName${unitIndex}`).value,
udMobileNoVc: unitContainer.querySelector(`#umobileNo${unitIndex}`).value,
udEmailIdVc: unitContainer.querySelector(`#email${unitIndex}`).value,
udUsageTypeI: parseInt(unitContainer.querySelector(`#unitusageType${unitIndex}`).value, 10),
udUsageSubtypeI: parseInt(unitContainer.querySelector(`#unitusageSubType${unitIndex}`).value, 10),
udConstructionClassI: getSelectedOptionText(`classOfProperty${unitIndex}`),
udConstYearDt: unitContainer.querySelector(`#currentDateUnit${unitIndex}`).value,
udConstAgeI: parseInt(unitContainer.querySelector(`#constructionAge${unitIndex}`).value, 10),
udAgeFactorI: unitContainer.querySelector(`#agefactorunithid${unitIndex}`).value,
udPlotAreaFl: parseFloat(unitContainer.querySelector(`#totalplotarea${unitIndex}`).value),
udCarpetAreaF: parseFloat(unitContainer.querySelector(`#totalcarpetarea${unitIndex}`).value),
udExemptedAreaF: parseFloat(unitContainer.querySelector(`#totalexemptedarea${unitIndex}`).value),
udAssessmentAreaF: parseFloat(unitContainer.querySelector(`#assessablearea${unitIndex}`).value),
udTotLegAreaF: parseFloat(unitContainer.querySelector(`#totallegalarea${unitIndex}`).value),
udTotIllegAreaF: parseFloat(unitContainer.querySelector(`#totaillegalarea${unitIndex}`).value),
udUnitRemarkVc: unitContainer.querySelector(`#remark${unitIndex}`).value,
udAreaBefDedF: parseFloat(unitContainer.querySelector(`#totalareabeforeded${unitIndex}`).value),
//  // Assuming this field exists
//  // Assuming this field exists
// udAgreementCopyVc: unitContainer.querySelector(`#agreementCopy${unitIndex}`).value, // Handling file inputs may need adjustments





//udCarpetAreaF: parseFloat(unitContainer.querySelector(`#carpetArea${unitIndex}`).value),
// udExemptedAreaF: parseFloat(unitContainer.querySelector(`#exemptedArea${unitIndex}`).value),
// udAssessmentAreaF: parseFloat(unitContainer.querySelector(`#assessmentArea${unitIndex}`).value),
//udSignatureVc: unitContainer.querySelector(`#signature${unitIndex}`).value,
// udTotLegAreaF: parseFloat(unitContainer.querySelector(`#totLegArea${unitIndex}`).value),
// udTotIllegAreaF: parseFloat(unitContainer.querySelector(`#totIllegArea${unitIndex}`).value),



//udAgeFactVc: unitContainer.querySelector(`#ageFact${unitIndex}`).value,
//udAssVc: unitContainer.querySelector(`#ass${unitIndex}`).value,
unitBuiltupUps: []
};

let popupId = `popup-${unitIndex}`;
let popup = document.getElementById(popupId);
console.log("here is popup:"+popup)
if (popup) {
// Select all rows without excluding the last one
const rows = popup.querySelectorAll('table tbody tr');
for (let rowIndex = 0; rowIndex < rows.length - 1; rowIndex++) {
let row = rows[rowIndex];
let rowId = `row-${unitIndex}-${rowIndex+1}`;

let roomTypeElement = row.querySelector(`select[id="use-${rowId}"]`);
let deduction = row.querySelector(`input[id="deduction-${rowId}"]`);
let lengthElement = row.querySelector(`input[id="length-${rowId}"]`);
let breadthElement = row.querySelector(`input[id="breadth-${rowId}"]`);
let areabeforededuction = row.querySelector(`input[id="areabeforededuction-${rowId}"]`);
let exemptionsElement = row.querySelector(`select[id="exemption-${rowId}"]`);
let exemLengthElement = row.querySelector(`input[id="exemptionLength-${rowId}"]`);
let exemBreadthElement = row.querySelector(`input[id="exemptionBreadth-${rowId}"]`);
let carpetAreaElement = row.querySelector(`input[id="carpetArea-${rowId}"]`);
let legalStatusElement = row.querySelector(`select[id="legalIllegal-${rowId}"]`);
// Assuming calculated fields are shown in input fields or spans with IDs following a similar naming convention
let exemAreaElement = document.getElementById(`exemptionArea-${rowId}`);
let assesAreaElement = document.getElementById(`assumptionArea-${rowId}`);
let legalAreaElement = document.getElementById(`legalAssmtArea-${rowId}`);
let illegalAreaElement = document.getElementById(`illegalAssmtArea-${rowId}`);
let measureTypeElement = row.querySelector(`select[id="measureType-${rowId}"]`);
let plotAreaelement = document.getElementById(`plotArea-${rowId}`);


//if (useElement && innerOuterElement && lengthElement && breadthElement && carpetAreaElement) {
    let builtUpAreas = {
        ubRoomTypeVc: roomTypeElement ? getSelectedOptionText(roomTypeElement.id) : '',
        ubDedpercentI: deduction ? parseFloat(deduction.value) : 0,
        ubareabefdedFl: areabeforededuction ? parseFloat(areabeforededuction.value) : 0,
        ubLengthFl: lengthElement ? lengthElement.value : '',
        ubBreadthFl: breadthElement ? breadthElement.value : '',
        ubExemptionsVc: exemptionsElement ? exemptionsElement.value : '',
        ubExemLengthFl: exemLengthElement ? exemLengthElement.value : '',
        ubExemBreadthFl: exemBreadthElement ? exemBreadthElement.value : '',
        ubCarpetAreaFl: carpetAreaElement ? carpetAreaElement.value : '',
        ubLegalStVc: legalStatusElement ? legalStatusElement.value : '',
        ubExemAreaFl: exemAreaElement ? exemAreaElement.value : '', // Capture real-time calculated values
        ubAssesAreaFl: assesAreaElement ? assesAreaElement.value : '',
        ubLegalAreaFl: legalAreaElement ? legalAreaElement.value : '',
        ubIllegalAreaFl: illegalAreaElement ? illegalAreaElement.value : '',
        ubMeasureTypeVc: measureTypeElement ? measureTypeElement.value : '',
        ubplotareaFl: plotAreaelement ?  plotAreaelement.value : ''
    };
    unitDetails.unitBuiltupUps.push(builtUpAreas);

}
}
formData.propertyDetails.unitDetails.push(unitDetails);
});

let jsonData = JSON.stringify(formData);

// Create a new FormData object
let formDataToSend = new FormData();
formDataToSend.append('jsonData', jsonData); // Append JSON string directly

// Append image files to FormData
const imageFields = ['previewPropertyImage', 'previewPropertyImage2', 'previewHousePlan1', 'previewHousePlan2'];
imageFields.forEach(field => {
let blob = compressedImages[field]; // Access the blob stored after compression
if (blob) {
formDataToSend.append(field, blob, field + ".jpg"); // Append blob with a filename
}
});

// Debug: Log each FormData key-value pair
for (let pair of formDataToSend.entries()) {
console.log(`${pair[0]}:`, pair[1]);
}

return formDataToSend;
}

var memberCount = 1;

function addMember() {
memberCount++;

var formContainer = document.getElementById("formContainer");

const newForm = `
<div id="memberForm${memberCount}">
<h3>Member ${memberCount} Details:</h3>
<label for="name${memberCount}">Name:</label>
<input type="text" id="name${memberCount}" name="name${memberCount}"><br>

<label for="qualification${memberCount}">Qualification:</label>
<select id="qualification${memberCount}" name="qualification${memberCount}">
    <option value="undergraduate">Undergraduate</option>
    <option value="postgraduate">Postgraduate</option>
    <option value="doctorate">Doctorate</option>
</select><br>

<label for="occupation${memberCount}">Occupation:</label>
<input type="text" id="occupation${memberCount}" name="occupation${memberCount}"><br>

<label for="noOfAdults${memberCount}">No. Of Adults:</label>
<input type="text" id="noOfAdults${memberCount}" name="noOfAdults${memberCount}"><br>

<label for="noOfChildren${memberCount}">No. of Children:</label>
<input type="text" id="noOfChildren${memberCount}" name="noOfChildren${memberCount}"><br>

<label for="currentTaxDetails${memberCount}">Current Tax Details:</label>
<select id="currentTaxDetails${memberCount}" name="currentTaxDetails${memberCount}">
    <option value="taxed">Taxed</option>
    <option value="nontaxed">Non-taxed</option>
</select><br>

<label for="taxAmount${memberCount}">Tax Amount:</label>
<input type="text" id="taxAmount${memberCount}" name="taxAmount${memberCount}"><br>

<label for="roadType${memberCount}">Road Type:</label>
<select id="roadType${memberCount}" name="roadType${memberCount}">
    <option value="residential">Residential</option>
    <option value="commercial">Commercial</option>
</select><br>

<label for="roadName${memberCount}">Road Name:</label>
<input type="text" id="roadName${memberCount}" name="roadName${memberCount}"><br>

<button type="button" onclick="addMember()">Add Another Member</button>
<button type="button" onclick="removeMember(${memberCount})">Remove Member</button>
<hr>
</div>
`;
document.getElementById("memberForm1").insertAdjacentHTML('beforeend', newForm);

}

function removeMember(formId) {
var formToRemove = document.getElementById(`memberForm${formId}`);
formToRemove.remove();
}

async function validateForm() {
let isValid = true;
let missingFields = [];
document.querySelectorAll('.mandatory').forEach((field) => {
let input = document.getElementById(field.htmlFor);
if (sectionToken === '124' && field.htmlFor === 'surveyPropNo') {
    return; // SPN will be generated server-side for section 124
}
if (input && !input.disabled && !input.value.trim()) {
    isValid = false;
    missingFields.push(field.textContent);
}
});

if (!isValid) {
alert("Please fill in the following mandatory fields: " + missingFields.join(', '));
return false;
}

// Check the survey property number validity
if (sectionToken !== '124') {
    await checkSurveyPropNo();

    if (!isSurveyPropNoValid) {
        alert("The Survey Property Number already exists or is invalid.");
        return false;
    }
} else {
    // Section 124 auto-generates SPN on server; treat as valid
    isSurveyPropNoValid = true;
}

return true;
}

async function submitFormData() {
if (!await validateForm()) {
return;
}
const formDataToSend = collectFormData();
const endpoint = sectionToken === '124'
    ? '/3g/sections/124/submitSurvey'
    : '/3gSurvey/submitNewPropertyDetails';
if (sectionToken) {
    formDataToSend.append('sectionToken', sectionToken);
}

document.getElementById('loadingSpinner').style.display = 'flex';
try {
const response = await fetch(endpoint, {
    method: 'POST',
    body: formDataToSend, // Send the FormData object directly
    // Do not set Content-Type header; let the browser handle it
});

if (!response.ok) {
    throw new Error(`HTTP error! Status: ${response.status} ${response.statusText}`);
}

const result = await response.json();
console.log('Submission successful:', result);

const messages = [];
if (result.message) {
    messages.push(result.message);
}
if (result.finalPropertyNo) {
    messages.push(`Final Property No: ${result.finalPropertyNo}`);
} else if (result.pdFinalpropnoVc) {
    messages.push(`Final Property No: ${result.pdFinalpropnoVc}`);
}
if (result.newPropertyNo) {
    messages.push(`New Property No: ${result.newPropertyNo}`);
}
if (sectionToken === '124') {
    if (result.assessmentDone === false) {
        messages.push('Assessment could not be completed automatically.');
    } else if (result.assessmentDone) {
        messages.push('Assessment completed.');
    }
}
if (result.assessmentError) {
    messages.push(`Assessment note: ${result.assessmentError}`);
}

alert(messages.length ? messages.join('\n') : "Form submitted successfully!");

window.removeEventListener('beforeunload', handleBeforeUnload);

const redirectUrl = sectionToken === '124'
    ? "/3gSurvey/newRegistration?sectionToken=124"
    : "/3gSurvey/newRegistration";
window.location.href = redirectUrl;
} catch (error) {
console.error('Submission failed:', error);
alert('Form could not be submitted');
} finally {
// Hide the loading spinner
document.getElementById('loadingSpinner').style.display = 'none';
}
}

function handleBeforeUnload(event) {
event.preventDefault();
event.returnValue = 'Are you sure you want to leave? Your changes may not be saved.';
}

window.addEventListener('beforeunload', handleBeforeUnload);
