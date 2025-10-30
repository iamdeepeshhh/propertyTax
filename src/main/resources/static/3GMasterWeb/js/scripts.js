// JavaScript function to show the content of the selected tab
document.addEventListener("DOMContentLoaded", function() {
    showTab('home');
});
document.getElementById('usmApplyDifferentRateVc').addEventListener('change', function() {
    const isCheckboxChecked = this.checked;
    const rateTypeSelect = document.getElementById('usm_rvtype_vc');
    rateTypeSelect.disabled = !isCheckboxChecked;
});
document.getElementById('uum_rvtype_vc').addEventListener('change', () => {
    getRateTypeDescription({
        selectElementId: 'uum_rvtype_vc',
        descriptionFieldId: 'rate-description-usage',
        dataSource: rvTypes
    });
});
document.getElementById('usm_rvtype_vc').addEventListener('change', () => {
    getRateTypeDescription({
        selectElementId: 'usm_rvtype_vc',
        descriptionFieldId: 'rate-description-usagesub',
        dataSource: rvTypes
    });
});
document.getElementById('rvTypeSelect').addEventListener('change', () => {
    getRateTypeDescription({
        selectElementId: 'rvTypeSelect',
        descriptionFieldId: 'descriptionUpVc',
        dataSource: rvTypes
    });
});
document.addEventListener('DOMContentLoaded', function() {
    // Function to fetch council details
    fetch('/3g/getCouncilDetails')
        .then(response => response.json())
        .then(data => {
            if (Array.isArray(data) && data.length > 0 && data[0].standardName) {
                // Set the council name in the navbar if available
                document.getElementById('councilName').textContent = data[0].standardName;
            } else {
                console.error('Council details not found or response is invalid.');
                document.getElementById('councilName').textContent = '3G Associates';
            }
        })
        .catch(error => {
            console.error('Error fetching council details:', error);
            document.getElementById('councilName').textContent = '3G Associates';
        });
});
function showTab(tabId) {
    // Hide all sections
    document.querySelectorAll('section').forEach(section => {
        section.style.display = 'none';
    });
    // Show the selected section
    const selectedSection = document.getElementById(tabId);
    if (selectedSection) {
        selectedSection.style.display = 'block';
    }
}

// Show a specific master child section by id (used by new Master menu)
function showMaster(childId) {
    // Reuse showTab behavior: hide all and show only requested child
    document.querySelectorAll('section').forEach(section => {
        section.style.display = 'none';
    });
    const target = document.getElementById(childId);
    if (target) target.style.display = 'block';
}

function populateDropdown(selectId, endpointUrl, displayProperty, idProperty = null) {
    // selectID is the name of the dropdown element
    // endpointUrl is the URL to fetch data from backend(controller)
    // displayProperty is the property of the data object that will be displayed in the dropdown
    // idProperty is the property of the data object that will be used as the value in dropdown to send to backend
      fetch(endpointUrl)
        .then(response => response.json())
        .then(data => {
            if (idProperty) {
                data.sort((a, b) => (a[idProperty] > b[idProperty] ? 1 : -1));
            }

            const selectElement = document.getElementById(selectId);
            let optionsHtml = `<option value="">Select an option</option>`;
            data.forEach(item => {
                if (idProperty) {
                    const value = item[idProperty];
                    const text = item[displayProperty];
                    optionsHtml += `<option value="${value}">${text}</option>`;
                } else {
                    const text = item[displayProperty];
                    optionsHtml += `<option value="${text}">${text}</option>`;
                }
            });
            selectElement.innerHTML = optionsHtml;
        })
        .catch(error => console.error(`Error fetching data from ${endpointUrl}:`, error));
}

let rvTypes = {}; // map of rvTypeId -> RV type object (should include taxKeysL)

async function fetchAllRateTypes() {
try {
const response = await fetch('/3g/getAllRVTypes');
if (response.ok) {
    const rateTypesData = await response.json();
    rateTypesData.forEach(rateType => {
    const key = rateType.id ?? rateType.ryTypeId ?? rateType.rvTypeId;
        if (key != null) {
            rvTypes[key] = rateType;
        }
    });
    console.log('Loaded RV Types:', rvTypes);
} else {
    console.error('Failed to fetch rate types');
}
} catch (error) {
console.error('Error fetching rate types:', error);
}
}

function getRateTypeDescription(params) {
    // below are the params  
    // selectElementId: this is denoting the dropdown Id name
    // descriptionFieldId: this is the field in which desxription is shown
    // dataSource: this is the array which is declared on the start in which all data related to rvtypes is stored
    const { selectElementId, descriptionFieldId, dataSource } = params;
    const rateTypeSelect = document.getElementById(selectElementId);
    const descriptionField = document.getElementById(descriptionFieldId);
    const selectedValue = rateTypeSelect.value;
    const selectedRateType = dataSource[selectedValue];
    // Clear any previous tax selections; will re-check if this RV type has assignments
    try { preSelectTaxes([]); } catch (e) {}
    if (descriptionField instanceof HTMLTextAreaElement) {
        descriptionField.value = selectedRateType ? selectedRateType.descriptionVc : '';
        console.log("Description updated to:", descriptionField.value);
    } else {
        descriptionField.textContent = selectedRateType ? selectedRateType.descriptionVc : '';
    }

    // âœ… Also preselect taxes
    if (selectedRateType) { if (selectedRateType.taxKeysL && selectedRateType.taxKeysL.length) { preSelectTaxes(selectedRateType.taxKeysL); } else if (selectedRateType.appliedTaxesVc) { const names = selectedRateType.appliedTaxesVc.split(",").map(n => n.trim()).filter(Boolean); } }
}

//as we want to set the selected checkboxes whenever we select any ratetype so we are using the preselct checkboxes function
function preSelectTaxes(selectedIds) {
    document.querySelectorAll('#rvCheckboxesContainer .form-check-input').forEach(cb => {
        cb.checked = false;
    });

    selectedIds.forEach(val => {
        let checkbox = null;
        const sval = String(val);
        if (/^\d+$/.test(sval)) {
            checkbox = document.querySelector(`#rvCheckboxesContainer input[data-tax-key-l='${sval}']`);
        }
        if (!checkbox) {
            checkbox = Array.from(document.querySelectorAll('#rvCheckboxesContainer .form-check-input'))
                .find(cb => cb.value && cb.value.trim() === sval.trim());
        }
        if (checkbox) checkbox.checked = true;
    })

     // Optionally update "Select All"
    const allCheckboxes = document.querySelectorAll('#rvCheckboxesContainer .form-check-input:not(#selectAll)');
    const allChecked = [...allCheckboxes].every(cb => cb.checked);
    document.getElementById('selectAll').checked = allChecked;
}
function updateHiddenField(selectId, hiddenInputId) {
    const selectElement = document.getElementById(selectId);
    const selectedOptionText = selectElement.options[selectElement.selectedIndex].text;
    document.getElementById(hiddenInputId).value = selectedOptionText;
}



function submitForm(formId, inputIds, endpointUrl, dynamicRowInputNames = [], dynamicTableId = '') {
var form = document.getElementById(formId);
var isValid = true;
var formData = {};

if (!input) {
    console.warn(`âš ï¸ Input with ID '${inputId}' not found in the form '${formId}'.`);
    return;
}

// Collect static form fields
inputIds.forEach(function(inputId) {
    var input = document.getElementById(inputId);
    if (input.type === 'checkbox') {
        formData[input.name] = input.checked;
    } else {
        if (!input.checkValidity()) {
            input.reportValidity();
            isValid = false;
        }
        formData[input.name] = input.value;
    }
});

// this is modification i did for making the record of (depreciation percentages) and (tax rates) section
// Collect dynamic row data if dynamicTableId is provided
if (dynamicTableId) {
    const tableBody = document.getElementById(dynamicTableId);
    const rows = tableBody.querySelectorAll('tr');
    const rowData = [];

    rows.forEach(row => {
        const rowObject = {};
        dynamicRowInputNames.forEach(name => {
            const input = row.querySelector(`input[name="${name}"], select[name="${name}"]`);
            if (input) {
                rowObject[name] = input.value;
            }
        });
        rowData.push(rowObject);
    });

    formData['dynamicRows'] = rowData;
}

console.log('Form Data to submit:', JSON.stringify(formData, null, 2));
if (isValid) {
    // Attach selected tax keys when creating/updating RV Types
    if (typeof endpointUrl === 'string' && endpointUrl.indexOf('RVType') !== -1) {
        const selectedTaxKeys = [];
        document.querySelectorAll('#rvCheckboxesContainer .form-check-input:checked').forEach(cb => {
            if (cb.id !== 'selectAll') {
                const key = parseInt(cb.dataset.taxKeyL);
                if (!isNaN(key)) selectedTaxKeys.push(key);
            }
        });
        formData.taxKeysL = selectedTaxKeys;
    }

    fetch(endpointUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (response.ok) {
            console.log(formData);
            alert('Form submitted successfully!');
            form.reset();  // Reset the form after successful submission
            // Reset hidden input fields if any
            document.getElementById('roomSelectedHidden') && (document.getElementById('roomSelectedHidden').value = "0");

            if (dynamicTableId) {
                const tableBody = document.getElementById(dynamicTableId);
                tableBody.innerHTML = ''; // Clear all rows
            }
            
            // Refresh the corresponding table
            const tableIdMap = {
                'zoneForm': 'existingZonesTableBody',
                'buildingStatusForm': 'existingBuildStatusesTableBody',
                'ownerTypeForm': 'existingOwnerTypesTableBody',
                'ownerCategoryForm': 'existingOwnerCategoriesTableBody',
                'waterConnectionForm': 'existingWaterConnectionsTableBody',
                'propertyTypeForm': 'existingPropertyTypesTableBody',
                'propertySubTypeForm': 'existingPropertySubtypes',
                'propertyUsageTypeForm': 'existingPropertyUsageTypes',
                'propertyUsageSubTypeForm': 'existingPropertyUsageSubtypesTableBody',
                'buildingTypeForm': 'existingBuildingTypesTableBody',
                'buildingSubtypeForm': 'existingBuildingSubtypesTableBody',
                'sewerageForm': 'existingSewerageTypesTableBody',
                'unitNoForm': 'existingUnitNumbersTableBody',
                'unitFloorNoForm': 'existingUnitFloorNumbersTableBody',
                'unitUsageTypeForm': 'existingUnitUsageTypesTableBody',
                'unitUsageSubTypeForm': 'existingUnitUsageSubtypesTableBody',
                'constructionClassForm': 'existingConstructionClassesTableBody',
                'assessmentDatesForm': 'existingAssessmentDatesTableBody',
                'occupancyForm': 'existingOccupanciesTableBody',
                'roomTypesForm': 'existingRoomTypesTableBody',
                'wardForm': 'existingWardsTableBody',
                'oldWardsForm': 'existingOldWardsTableBody',
                'constructionAgeFactorForm': 'existingConstructionAgeFactorsTableBody',
                'remarksForm': 'existingRemarksTableBody',
                'propertyRatesForm': 'existingPropertyRatesTableBody',
                'addConsolidatedTaxForm': 'existingConsolidatedTaxesTableBody',
                'depreciationForm': 'existingDepreciationRatesTableBody',
                'educationEmploymentCessForm' : 'existingCessRatesTableBody',
                'rvTypeForm' : 'existingRVTypesTableBody'
            };

            const tableBodyId = tableIdMap[formId];
            if (tableBodyId) {
                const endpointMap = {
                    'zoneForm': '/3g/getAllZones',
                    'buildingStatusForm': '/3g/buildstatuses',
                    'ownerTypeForm': '/3g/getOwnerType',
                    'ownerCategoryForm': '/3g/ownerCategories',
                    'waterConnectionForm': '/3g/waterConnections',
                    'propertyTypeForm': '/3g/propertytypes',
                    'propertySubTypeForm': '/3g/propertysubtypes',
                    'propertyUsageTypeForm': '/3g/propertyusagetypes',
                    'propertyUsageSubTypeForm': '/3g/getSubUsageTypes',
                    'buildingTypeForm': '/3g/getBuildingTypes',
                    'buildingSubtypeForm': '/3g/getAllBuildingSubTypes',
                    'sewerageForm': '/3g/getSewerageTypes',
                    'unitNoForm': '/3g/unitNumbers',
                    'unitFloorNoForm': '/3g/getUnitFloorNos',
                    'unitUsageTypeForm': '/3g/getAllUnitUsageTypes',
                    'unitUsageSubTypeForm': '/3g/getAllUnitUsageSubTypes',
                    'constructionClassForm': '/3g/constructionClassMasters',
                    'assessmentDatesForm': '/3g/getAllAssessmentDates',
                    'occupancyForm': '/3g/occupancyMasters',
                    'roomTypesForm': '/3g/roomTypes',
                    'wardForm': '/3g/getAllWards',
                    'oldWardsForm': '/3g/getAllOldWards',
                    'constructionAgeFactorForm': '/3g/constructionAgeFactor',
                    'remarksForm': '/3g/getAllRemarks',
                    'propertyRatesForm': '/3g/getAllPropertyRates',
                    'addConsolidatedTaxForm' : '/3g/getAllConsolidatedTaxes',
                    'depreciationForm': '/3g/getDepreciationRates',
                    'educationEmploymentCessForm' : '/3g/getAllCessRates',
                    'rvTypeForm' : '/3g/getAllRVTypes'
                };

                const endpoint = endpointMap[formId];
                // Use correct columns based on the form being submitted
                const columnMap = {
                    'zoneForm': ['name'],
                    'buildingStatusForm': ['name'],
                    'ownerTypeForm': ['ownerType', 'ownerTypeMarathi'],
                    'ownerCategoryForm': ['englishname', 'marathiname'],
                    'waterConnectionForm': ['waterConnection', 'waterConnectionMarathi'],
                    'propertyTypeForm': ['standardName', 'localName'],
                    'propertySubTypeForm': ['id', 'standardName', 'localName'],
                    'propertyUsageTypeForm': ['id', 'standardName', 'localName'],
                    'propertyUsageSubTypeForm': ['id', 'standardName', 'localName'],
                    'buildingTypeForm': ['id', 'standardName', 'localName'],
                    'buildingSubtypeForm': ['id', 'standardName', 'localName'],
                    'sewerageForm': ['englishname'],
                    'unitNoForm': ['englishname'],
                    'unitFloorNoForm': ['englishname'],
                    'unitUsageTypeForm': ['standardName', 'localName','uum_rvtype_vc'],
                    'unitUsageSubTypeForm': ['uum_usagetypeeng_vc','usm_usagetypeeng_vc', 'usm_usagetypell_vc','usm_usercharges_i','usmApplyDifferentRateVc','usm_rvtype_vc'],
                    'constructionClassForm': ['englishname', 'marathiname', 'Deduction'],
                    'assessmentDatesForm': ['firstassessmentdate', 'currentassessmentdate', 'lastassessmentdate'],
                    'occupancyForm': ['englishname', 'marathiname', 'value'],
                    'roomTypesForm': ['englishname', 'room'],
                    'wardForm': ['wardNo'],
                    'oldWardsForm': ['countNo'],
                    'constructionAgeFactorForm': ['afm_agefactorid_i','afm_agefactornameeng_vc', 'afm_agefactornamell_vc', 'afm_ageminage_vc', 'afm_agemaxage_vc', 'afm_remarks_vc'],
                    'remarksForm': ['remark'],
                    'propertyRatesForm' : ['constructionTypeVc','taxRateZoneI','rateI','applicableonVc'],
                    'addConsolidatedTaxForm' : ['taxNameVc','taxRateFl','applicableonVc'],
                    'depreciationForm': ['constructionClassVc','minAgeI','maxAgeI','depreciationPercentageI'],
                    'educationEmploymentCessForm' : ['minTaxableValueFl','maxTaxableValueFl','residentialRateFl','commercialRateFl','egcRateFl'],
                    'rvTypeForm' : ['typeNameVc','rateFl','applicableonVc','descriptionVc']
                };

                const columns = columnMap[formId];
                fetchAndPopulateTable(endpoint, tableBodyId, columns);
            }

        } else {
            alert('Failed to submit form. Please try again later.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    });
}
}


document.addEventListener('DOMContentLoaded', function() {
fetchConsolidatedTaxesCheckbox();
populateDropdown('constructionClassVc', '/3g/constructionClassMasters', 'marathiname');
populateDropdown('taxSelect', '/3g/getAllConsolidatedTaxes', 'taxNameVc','id');
populateDropdown('rvTypeSelect', '/3g/getAllRVTypes', 'typeNameVc','id');
populateDropdown('uum_rvtype_vc', '/3g/getAllRVTypes', 'typeNameVc','id');
populateDropdown('usm_rvtype_vc', '/3g/getAllRVTypes', 'typeNameVc','id');
populateDropdown('categoryIdUp','/3g/getRVTypeCategories', 'categoryNameLocalVc','categoryId');
populateDropdown('categoryId','/3g/getRVTypeCategories', 'categoryNameLocalVc','categoryId');
populateDropdown('constructionTypeVc', '/3g/constructionClassMasters', 'marathiname');
// populateDropdown('batchReportWard', '/3g/getAllWards', 'wardNo');


// populateDropdown('taxRateZoneI', '/3g/getAllZones', 'name');
fetchAndPopulateTable('/3g/propertytypes', 'existingPropertyTypesTableBody', ['id','standardName', 'localName']);
fetchAndPopulateTable('/3g/propertysubtypes', 'existingPropertySubtypes', ['id','standardName', 'localName']);
fetchAndPopulateTable('/3g/propertyusagetypes', 'existingPropertyUsageTypes', ['id','standardName', 'localName']);
fetchAndPopulateTable('/3g/getSubUsageTypes', 'existingPropertyUsageSubtypesTableBody', ['id', 'standardName', 'localName']);
fetchAndPopulateTable('/3g/getBuildingTypes', 'existingBuildingTypesTableBody', ['id', 'standardName', 'localName']);
fetchAndPopulateTable('/3g/getAllBuildingSubTypes', 'existingBuildingSubtypesTableBody', ['id', 'standardName', 'localName']);
fetchAndPopulateTable('/3g/getSewerageTypes', 'existingSewerageTypesTableBody', ['englishname']);
fetchAndPopulateTable('/3g/unitNumbers', 'existingUnitNumbersTableBody', ['englishname']);
fetchAndPopulateTable('/3g/getUnitFloorNos', 'existingUnitFloorNumbersTableBody', ['englishname']);
fetchAndPopulateTable('/3g/getAllUnitUsageTypes', 'existingUnitUsageTypesTableBody', ['standardName', 'localName','uum_rvtype_vc'],'/3g/deleteUnitUsageById','/3g/updateUnitUsageById');
fetchAndPopulateTable('/3g/getAllUnitUsageSubTypes', 'existingUnitUsageSubtypesTableBody', ['uum_usagetypeeng_vc','usm_usagetypeeng_vc', 'usm_usagetypell_vc','usm_usercharges_i','usmApplyDifferentRateVc','usm_rvtype_vc'],'/3g/deleteUnitUsagesSub','/3g/updateUnitUsagesSub');
fetchAndPopulateTable('/3g/constructionClassMasters', 'existingConstructionClassesTableBody', ['englishname', 'marathiname', 'Deduction'],'deleteConstructionClassMastersById');
fetchAndPopulateTable('/3g/getAllAssessmentDates', 'existingAssessmentDatesTableBody', ['firstassessmentdate', 'currentassessmentdate', 'lastassessmentdate'], '/3g/deleteAssessmentById');
fetchAndPopulateTable('/3g/occupancyMasters', 'existingOccupanciesTableBody', ['standardName', 'localName', 'id']);
fetchAndPopulateTable('/3g/roomTypes', 'existingRoomTypesTableBody', ['englishname', 'room']);
fetchAndPopulateTable('/3g/getAllZones', 'existingZonesTableBody', ['name']);
fetchAndPopulateTable('/3g/getAllRemarks', 'existingRemarksTableBody', ['remark']);
fetchAndPopulateTable('/3g/constructionAgeFactor', 'existingConstructionAgeFactorsTableBody', ['afm_agefactorid_i','afm_agefactornameeng_vc', 'afm_agefactornamell_vc', 'afm_ageminage_vc', 'afm_agemaxage_vc', 'afm_remarks_vc']);
initializePropertyTypesAndCaptureSelection('uusc-property-type-select', 'propertytypes');
initializePropertyTypesAndCaptureSelection('uuc-property-type-select', 'propertytypes');
initializePropertyTypesAndCaptureSelection('property-type-select', '/3g/propertytypes');
initializeBuildingTypesAndCaptureSelection('bt-property-type-select', '/3g/getBuildingTypes');
initializePropertySubtypesBasedOnType('puc-property-type-select', 'puc-property-subtype-select', 'propertySubtypes');
initializePropertySubtypesBasedOnType('pusc-property-type-select', 'pusc-property-subtype-select', 'propertySubtypes');
initializePropertySubtypesBasedOnType('uuc-property-type-select', 'uuc-property-subtype-select', 'propertySubtypes');
initializePropertySubtypesBasedOnType('uusc-property-type-select', 'uusc-property-subtype-select', 'propertySubtypes')
initializePropertyUsageTypesBasedOnSubtype('pusc-property-subtype-select','pusc-property-usage-type-select','usageTypes');
initializePropertyUsageTypesBasedOnSubtype('uusc-property-subtype-select','uusc-property-usage-type-select','usageTypes');
initializeUnitUsageTypesBasedOnPropertyUsage('uuc-property-subtype-select','uuc-property-usage-type-select','usageTypes');
initializeUnitUsageTypesBasedOnPropertyUsage('uusc-property-usage-type-select', 'uusc-unit-usage-type-select', 'getUnitUsageByPropUsageId');
initializeBuildingTypesAndCaptureSelection('building-type-select', 'getBuildingTypes');
initializePropertyTypesAndCaptureSelection('bt-property-type-select', 'propertytypes');
fetchAndPopulateTable('/3g/getOwnerType', 'existingOwnerTypesTableBody', ['ownerType', 'ownerTypeMarathi']);
fetchAndPopulateTable('/3g/buildstatuses', 'existingBuildStatusesTableBody', ['name']);
fetchAndPopulateTable('/3g/ownerCategories', 'existingOwnerCategoriesTableBody', ['englishname', 'marathiname']);
fetchAndPopulateTable('/3g/waterConnections', 'existingWaterConnectionsTableBody', ['waterConnection', 'waterConnectionMarathi']);
fetchAndPopulateTable('/3g/getAllWards', 'existingWardsTableBody', ['wardNo']);
fetchAndPopulateTable('/3g/getDeletionLogs', 'existingDeletionLogsTableBody', ['wardnoVc','surveyPropNo', 'ownerName', 'recordOwner', 'remarks', 'deletionTime', 'username']);
fetchAndPopulateTable('/3g/getAllOldWards', 'existingOldWardsTableBody', ['oldwardno']);
fetchAndPopulateTable('/3g/getAllRVTypes', 'existingRVTypesTableBody', ['typeNameVc','rateFl','appliedTaxesVc','descriptionVc']);
fetchAndPopulateTable('/3g/getAllConsolidatedTaxes', 'existingConsolidatedTaxesTableBody', ['taxNameVc','taxRateFl','applicableonVc','isActiveBl']);
fetchAndPopulateTable('/3g/getAllPropertyRates', 'existingPropertyRatesTableBody', ['constructionTypeVc','taxRateZoneI','rateI'],'/3g/deletePropertyRate');
fetchAndPopulateTable('/3g/getAllCessRates', 'existingCessRatesTableBody', ['minTaxableValueFl','maxTaxableValueFl','residentialRateFl','commercialRateFl','egcRateFl'],'/3g/deleteCessRate');
fetchAndPopulateTable('/3g/getDepreciationRates', 'existingDepreciationRatesTableBody', ['constructionClassVc','minAgeI','maxAgeI','depreciationPercentageI'],'/3g/deleteDepreciationRate');
fetchAndPopulateTable('/3g/getCouncilDetails','councilDetailsTableBody', ['standardName', 'localName'], '/3g/deleteCouncilDetails');
fetchAndPopulateTable('/3g/reportTaxConfigs/all','rtc-table-body', ['template', 'sequence', 'englishname', 'marathiname', 'visible', 'showTotal']);

fetchAllRateTypes();
});

async function initializePropertyTypesAndCaptureSelection(selectId, apiUrl) {
const selectElement = document.getElementById(selectId);

// Fetch the property types from the API and populate the select element.
try {
    const response = await fetch(apiUrl);
    if (!response.ok) throw new Error('Failed to fetch data');
    const propertyTypes = await response.json();

    let optionsHtml = '<option value="">Select a property type</option>';
    propertyTypes.forEach(type => {
        if (selectId === 'bt-property-type-select') {
            optionsHtml += `<option value="${type.id},${type.localName}">${type.localName}</option>`;
        } else {
            optionsHtml += `<option value="${type.id}">${type.localName}</option>`;
        }
    });
    selectElement.innerHTML = optionsHtml;

}catch (error) {
    console.error(`Error initializing property types for select #${selectId}:`, error);
}

// Attempt to reapply saved selection from localStorage
const savedValue = localStorage.getItem(`${selectId}-value`);
if (savedValue) {
    selectElement.value = savedValue;
}

// Listen for changes to the select element, log the selected option's ID and name, and save them to localStorage.
selectElement.addEventListener('change', function() {
  const selectedOption = this.options[this.selectedIndex];
    const value = selectedOption.value;
    let selectedId, selectedName;

    // Special handling for the "Building Type" section
    if (selectId === 'bt-property-type-select') {
        [selectedId, selectedName] = selectedOption.value.split(',');
        } else {
        // For other select elements, handle the value as just an ID
        selectedId = selectedOption.value;
        selectedName = selectedOption.text; // Using .text to get the visible name
    }

    console.log(`Selected ID: ${selectedId}, Name: ${selectedName}`);

    // Save the selection ID (and name if applicable) to localStorage
    localStorage.setItem(`${selectId}-id`, selectedId);
    localStorage.setItem(`${selectId}-name`, selectedName);
    localStorage.setItem(`${selectId}-value`, value);

});
}


async function initializePropertySubtypesBasedOnType(propertyTypeId, subtypeSelectId, apiUrl) {
const propertyTypeSelectElement = document.getElementById(propertyTypeId);
const subtypeSelectElement = document.getElementById(subtypeSelectId);

// Listen for changes in the property type select element to fetch and populate subtypes.
propertyTypeSelectElement.addEventListener('change', async function() {
    const selectedPropertyTypeId = this.value;

    if (!selectedPropertyTypeId) {
        subtypeSelectElement.innerHTML = '<option value="">Select a property subtype</option>';
        return;
    }

    try {
        // Fetch the property subtypes based on the selected property type ID.
        const response = await fetch(`${apiUrl}/${selectedPropertyTypeId}`);
        if (!response.ok) throw new Error('Failed to fetch property subtypes');
        const propertySubtypes = await response.json();

        // Populate the subtype select element with options based on the fetched data.
        let optionsHtml = '<option value="">Select a property subtype</option>';
        propertySubtypes.forEach(subtype => {
            optionsHtml += `<option value="${subtype.id}">${subtype.localName}</option>`;
        });
        subtypeSelectElement.innerHTML = optionsHtml;

        // Reapply the saved subtype selection from localStorage if available.
        const savedSubtypeSelectionId = localStorage.getItem(`${subtypeSelectId}-id`);
        if (savedSubtypeSelectionId) {
            subtypeSelectElement.value = savedSubtypeSelectionId;
        }

    } catch (error) {
        console.error(`Error fetching property subtypes for ${subtypeSelectId}:`, error);
    }
});

// Listen for changes to the subtype select element to save the selection to localStorage.
subtypeSelectElement.addEventListener('change', function() {
    const selectedOption = this.options[this.selectedIndex];
    const selectedId = selectedOption.value;
    const selectedName = selectedOption.text;
    console.log(`Selected Property Subtype ID: ${selectedId}, Name: ${selectedName}`);

    // Save selection ID and name to localStorage.
    localStorage.setItem(`${subtypeSelectId}-id`, selectedId);
    localStorage.setItem(`${subtypeSelectId}-name`, selectedName);
});
}


async function initializePropertyUsageTypesBasedOnSubtype(propertySubtypeId, usageTypeSelectId, apiUrl) {
const propertySubtypeSelectElement = document.getElementById(propertySubtypeId);
const usageTypeSelectElement = document.getElementById(usageTypeSelectId);

// Listen for changes in the property subtype select element to fetch and populate usage types.
propertySubtypeSelectElement.addEventListener('change', async function() {
    const selectedPropertySubtypeId = this.value;

    if (!selectedPropertySubtypeId) {
        usageTypeSelectElement.innerHTML = '<option value="">Select a property usage type</option>';
        return;
    }

    try {
        // Fetch the property usage types based on the selected property subtype ID.
        const response = await fetch(`${apiUrl}/${selectedPropertySubtypeId}`);
        console.log(response);
        if (!response.ok) throw new Error('Failed to fetch property usage types');
        const propertyUsageTypes = await response.json();

        // Populate the usage type select element with options based on the fetched data.
        let optionsHtml = '<option value="">Select a property usage type</option>';
        propertyUsageTypes.forEach(usageType => {
            optionsHtml += `<option value="${usageType.id}">${usageType.localName}</option>`;
        });
        usageTypeSelectElement.innerHTML = optionsHtml;

        // Reapply the saved usage type selection from localStorage if available.
        const savedUsageTypeSelectionId = localStorage.getItem(`${usageTypeSelectId}-id`);
        if (savedUsageTypeSelectionId) {
            usageTypeSelectElement.value = savedUsageTypeSelectionId;
        }

    } catch (error) {
        console.error(`Error fetching property usage types for ${usageTypeSelectId}:`, error);
    }
});

// Listen for changes to the usage type select element to save the selection to localStorage.
usageTypeSelectElement.addEventListener('change', function() {
    const selectedOption = this.options[this.selectedIndex];
    const selectedId = selectedOption.value;
    const selectedName = selectedOption.text;
    console.log(`Selected Property Usage Type ID: ${selectedId}, Name: ${selectedName}`);

    // Save selection ID and name to localStorage.
    localStorage.setItem(`${usageTypeSelectId}-id`, selectedId);
    localStorage.setItem(`${usageTypeSelectId}-name`, selectedName);
});
}


async function initializeUnitUsageTypesBasedOnPropertyUsage(propertyUsageId, unitUsageTypeSelectId, apiUrl) {
const propertyUsageSelectElement = document.getElementById(propertyUsageId);
const unitUsageTypeSelectElement = document.getElementById(unitUsageTypeSelectId);
// Listen for changes in the property usage type select element to fetch and populate unit usage types.
propertyUsageSelectElement.addEventListener('change', async function() {
    const selectedPropertyUsageId = this.value;

    if (!selectedPropertyUsageId) {
        unitUsageTypeSelectElement.innerHTML = '<option value="">Select a unit usage type</option>';
        return;
    }

    try {
        // Fetch the unit usage types based on the selected property usage type ID.
        const response = await fetch(`${apiUrl}/${selectedPropertyUsageId}`);
        if (!response.ok) throw new Error('Failed to fetch unit usage types');
        const unitUsageTypes = await response.json();

        // Populate the unit usage type select element with options based on the fetched data.
        let optionsHtml = '<option value="">Select a unit usage type</option>';
        unitUsageTypes.forEach(usageType => {
            optionsHtml += `<option value="${usageType.id}">${usageType.localName}</option>`;
        });
        unitUsageTypeSelectElement.innerHTML = optionsHtml;

        // Reapply the saved unit usage type selection from localStorage if available.
        const savedUnitUsageTypeSelectionId = localStorage.getItem(`${unitUsageTypeSelectId}-id`);
        if (savedUnitUsageTypeSelectionId) {
            unitUsageTypeSelectElement.value = savedUnitUsageTypeSelectionId;
        }

    } catch (error) {
        console.error(`Error fetching unit usage types for ${unitUsageTypeSelectId}:`, error);
    }
});

// Listen for changes to the unit usage type select element to save the selection to localStorage.
unitUsageTypeSelectElement.addEventListener('change', function() {
    const selectedOption = this.options[this.selectedIndex];
    const selectedId = selectedOption.value;
    const selectedName = selectedOption.text;
    console.log(`Selected Unit Usage Type ID: ${selectedId}, Name: ${selectedName}`);

    // Save selection ID and name to localStorage.
    localStorage.setItem(`${unitUsageTypeSelectId}-id`, selectedId);
    localStorage.setItem(`${unitUsageTypeSelectId}-name`, selectedName);
});
}


async function initializeBuildingTypesAndCaptureSelection(selectId, apiUrl) {
const selectElement = document.getElementById(selectId);

try {
    const response = await fetch(apiUrl);
    if (!response.ok) throw new Error('Failed to fetch data');
    const buildingTypes = await response.json();

    // Populate options
    let optionsHtml = '<option value="">Select a building type</option>';
    buildingTypes.forEach(type => {
        // Use type.value as the identifier, concatenated with type.name for the option's value
        optionsHtml += `<option value="${type.id},${type.standardName}">${type.localName}</option>`;
    });
    selectElement.innerHTML = optionsHtml;

    // Attempt to reapply saved selection based on both value and name
    const savedSelectionValue = localStorage.getItem(`${selectId}-value`);
    if (savedSelectionValue) {
        // Iterate over options to find a match for reapplication
        for (let option of selectElement.options) {
            if (option.value === savedSelectionValue) {
                selectElement.value = option.value;
                break;
            }
        }
    }

    // Listen for changes and save new selection
    selectElement.addEventListener('change', function() {
        const selectedOption = this.options[this.selectedIndex];
        // Assuming value is in the format "value,name"
        const selectedValue = selectedOption.value;
        console.log(`Selected Building Type Value: ${selectedValue}`);
        // Save the combined value (id,name) to localStorage to preserve both pieces of information
        localStorage.setItem(`${selectId}-value`, selectedValue);
    });
} catch (error) {
    console.error(`Error initializing building types for select #${selectId}:`, error);
}
}


async function fetchAndPopulateTable(endpointUrl, tableBodyId, columns, deleteEndpointUrl = null, updateEndpointUrl = null) {
    try {
        const response = await fetch(endpointUrl);
        if (!response.ok) throw new Error('Failed to fetch data');
        const data = await response.json();
        data.sort((a, b) => (a.value > b.value ? 1 : -1));

        const tableBody = document.getElementById(tableBodyId);
        tableBody.innerHTML = ''; // Clear existing content

        data.forEach((item, index) => {
            const row = document.createElement('tr');
            if (index % 2 === 1) {
                row.classList.add('table-secondary'); // Bootstrap class for alternate row coloring
            }
            // Add the index column
            const indexCell = document.createElement('td');
            indexCell.textContent = index + 1;
            indexCell.style.textAlign = 'center'; // Center align
            row.appendChild(indexCell);

            columns.forEach(column => {
                const cell = document.createElement('td');
                if (typeof item[column] === "boolean") {
                    cell.textContent = item[column] ? 'True' : 'False';
                } else {
                    cell.textContent = item[column] || '0'; // Display '0' for falsy values that are not boolean
                }
                cell.style.textAlign = 'center'; // Center align
                row.appendChild(cell);
            });

            // Add action buttons
            const actionsCell = document.createElement('td');

            // Add delete button if deleteEndpointUrl is provided
            if (deleteEndpointUrl) {
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'Delete';
                deleteButton.classList.add('btn', 'btn-danger', 'mr-2');
                deleteButton.onclick = function() {
                    deleteRecord(item.id, deleteEndpointUrl, () => {// here we would require id to be changed to value and all should be uniform like value in usm psm n all
                        // Refresh the table after successful deletion
                        fetchAndPopulateTable(endpointUrl, tableBodyId, columns, deleteEndpointUrl, updateEndpointUrl);
                    });
                };
                actionsCell.appendChild(deleteButton);
            }

            // Add edit button if updateEndpointUrl is provided
            if (updateEndpointUrl) {
                const editButton = document.createElement('button');
                editButton.textContent = 'Edit';
                editButton.classList.add('btn', 'btn-warning');
                editButton.onclick = function() {
                    editRecord(row, item, columns, updateEndpointUrl, () => {
                        // Refresh the table after successful update
                        fetchAndPopulateTable(endpointUrl, tableBodyId, columns, deleteEndpointUrl, updateEndpointUrl);
                    });
                };
                actionsCell.appendChild(editButton);
            }

            actionsCell.style.textAlign = 'center'; // Center align
            row.appendChild(actionsCell);

            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error(`Error fetching data from ${endpointUrl}:`, error);
    }
}


function editRecord(row, item, columns, updateEndpointUrl, onSuccess) {
    const originalCells = [];
    const cells = row.querySelectorAll('td');

    // Save the original content of the cells and create input elements for editable cells
    cells.forEach((cell, index) => {
        originalCells.push(cell.innerHTML);
        if (index !== 0 && index !== cells.length - 1) {  // Skip the first and last columns for editing
            cell.innerHTML = '';
            const column = columns[index - 1];  // Adjust index to match columns array
            if (typeof item[column] === "boolean") {
                const select = document.createElement('select');
                select.className = 'form-control';
                select.innerHTML = `
                    <option value="true" ${item[column] ? 'selected' : ''}>True</option>
                    <option value="false" ${!item[column] ? 'selected' : ''}>False</option>
                `;
                cell.appendChild(select);
            } else {
                const input = document.createElement('input');
                input.type = 'text';
                input.className = 'form-control';
                input.value = item[column];
                cell.appendChild(input);
            }
        }
    });

    const actionsCell = cells[cells.length - 1];
    const originalActions = actionsCell.innerHTML; // Save the original actions cell content
    actionsCell.innerHTML = '';

    const saveButton = document.createElement('button');
    saveButton.textContent = 'Save';
    saveButton.classList.add('btn', 'btn-success', 'mr-2');
    saveButton.onclick = function() {
        const updatedData = { id: item.id }; // Include the ID in the updatedData object

        //added to check true or false and save it
        row.querySelectorAll('input, select').forEach((element, index) => {
            const column = columns[index];  // Directly map element to column
            if (element.tagName.toLowerCase() === 'select') {
                updatedData[column] = element.value === 'true';  // Convert to boolean
            } else {
                updatedData[column] = element.value;
            }
        });
        // If updating RV Type, also include selected tax keys from checkboxes
        if (typeof updateEndpointUrl === 'string' && updateEndpointUrl.indexOf('/3g/updateRVType') !== -1) {
            const selectedTaxKeys = [];
            document.querySelectorAll('#rvCheckboxesContainer .form-check-input:checked').forEach(cb => {
                if (cb.id !== 'selectAll') {
                    const key = parseInt(cb.dataset.taxKeyL);
                    if (!isNaN(key)) selectedTaxKeys.push(key);
                }
            });
            updatedData.taxKeysL = selectedTaxKeys;
        }

        //added to check true or false 
        updateRecord(updatedData, updateEndpointUrl, () => {
            // Revert the row back to non-editable mode
            cells.forEach((cell, index) => {
                if (index === 0 || index === cells.length - 1) { // Skip index and actions columns
                    cell.innerHTML = originalCells[index];
                } else {
                    cell.innerHTML = originalCells[index];
                }
            });

            actionsCell.innerHTML = originalActions; // Restore the original actions cell content

            // Restore event listeners for edit and delete buttons
            const editButton = actionsCell.querySelector('.btn-warning');
            editButton.onclick = function() {
                editRecord(row, item, columns, updateEndpointUrl, onSuccess);
            };

            const deleteButton = actionsCell.querySelector('.btn-danger');
            deleteButton.onclick = function() {
                deleteRecord(item.value, deleteEndpointUrl, () => {
                    fetchAndPopulateTable(endpointUrl, tableBodyId, columns, deleteEndpointUrl, updateEndpointUrl);
                });
            };

            onSuccess(); // Call the onSuccess callback
        });
    };

    const cancelButton = document.createElement('button');
    cancelButton.textContent = 'Cancel';
    cancelButton.classList.add('btn', 'btn-secondary');
    cancelButton.onclick = function() {
        cancelEdit(row, originalCells, originalActions, updateEndpointUrl, onSuccess);
    };

    actionsCell.appendChild(saveButton);
    actionsCell.appendChild(cancelButton);
}


function updateTaxRate() {
    const formData = new FormData(document.getElementById('updateTaxRateForm'));
    const dataObject = {};
    formData.forEach((value, key) => {
        dataObject[key] = value;
        console.log(`Form Data - Key: ${key}, Value: ${value}`);
    });
    updateRecord(dataObject, '/3g/updateConsolidatedTax', () => location.reload());
}


function updateRvRate() {
    const formData = new FormData(document.getElementById('updateRateForm'));
    const dataObject = {};
    formData.forEach((value, key) => {
        dataObject[key] = value;
        console.log(`Form Data - Key: ${key}, Value: ${value}`);
    });

   const selectedTaxNames = [];
   const selectedTaxKeys = [];

    document.querySelectorAll('#rvCheckboxesContainer .form-check-input:checked').forEach(checkbox => {
        if (checkbox.checked && checkbox.id !== 'selectAll') { // Exclude 'Select All'
            selectedTaxNames.push(checkbox.value);
            selectedTaxKeys.push(parseInt(checkbox.dataset.taxKeyL));
            console.log('keys of checkboxes'+selectedTaxKeys);
        }
    });

//    const educationCessChecked = document.getElementById('educationCessOption').checked;
//    const selectedSubCessOption = document.querySelector('input[name="subCessOption"]:checked');
//    if (educationCessChecked && selectedSubCessOption) {
//        selectedTaxNames.push(`Education Tax(${selectedSubCessOption.value})`);
//    }
//
//    // Handling EGC option
//    const egcChecked = document.getElementById('egcOption').checked;
//    if (egcChecked) {
//        selectedTaxNames.push('Employment Guarantee Cess (EGC)');
//    }

    dataObject['appliedTaxesVc'] = selectedTaxNames.join(', ');
    dataObject['taxKeysL'] = selectedTaxKeys;
    console.log('Data to send:', dataObject);

    updateRecord(dataObject, '/3g/updateRVType', () => fetchAndPopulateTable('/3g/getAllRVTypes', 'existingRVTypesTableBody', ['typeNameVc','rateFl','appliedTaxesVc','descriptionVc']))
}


function updateRecord(data, updateEndpointUrl, onSuccess) {
    const id = data.id || data[Object.keys(data)[0]]; // Assuming the first key is the ID

    fetch(`${updateEndpointUrl}/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.ok) {
            alert('Record updated successfully!');
            onSuccess();
        } else {
            alert('Failed to update record. Please try again.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    });
}


function cancelEdit(row, originalCells, originalActions, updateEndpointUrl, onSuccess) {
    row.querySelectorAll('td:not(:last-child)').forEach((cell, index) => {
        cell.innerHTML = originalCells[index];
    });

    const actionsCell = row.querySelector('td:last-child');
    actionsCell.innerHTML = originalActions; // Restore the original actions cell content

    // Restore event listeners for edit and delete buttons
    const editButton = actionsCell.querySelector('.btn-warning');
    editButton.onclick = function() {
        editRecord(row, item, columns, updateEndpointUrl, onSuccess);
    };

    const deleteButton = actionsCell.querySelector('.btn-danger');
    deleteButton.onclick = function() {
        deleteRecord(item.id, deleteEndpointUrl, () => {
            fetchAndPopulateTable(endpointUrl, tableBodyId, columns, deleteEndpointUrl, updateEndpointUrl);
        });
    };
}


async function deleteRecord(id, deleteEndpointUrl, onSuccess) {
if (confirm('Are you sure you want to delete this record?')) {
    try {
        const response = await fetch(deleteEndpointUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({ id: id })
        });
        if (response.ok) {
        console.log(id+" this is id");
            alert('Record deleted successfully!');
           // onSuccess();
        } else {
            alert('Failed to delete record. Please try again.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An unexpected error occurred. Please try again later.');
    }
}
}


//document.getElementById('uploadForm').addEventListener('submit', function(event) {
//event.preventDefault();
//const form = document.getElementById('uploadForm');
//const formData = new FormData(form);
//
//fetch('/3g/uploadExcel', {
//    method: 'POST',
//    body: formData
//})
//.then(response => response.text())
//.then(data => {
//    alert(data);
//    form.reset();  // Reset the form after successful submission
//})
//.catch(error => {
//    console.error('Error uploading file:', error);
//    alert('Error uploading file. Please try again.');
//});
//});


// fetchPropertyRecords is the method getting used for searching the properties and setting them into the table
function fetchPropertyRecords(endpointUrl, queryParams, tableBodyId, actionType, columns) {
    const url = new URL(endpointUrl, window.location.origin);
    Object.keys(queryParams).forEach(key => url.searchParams.append(key, queryParams[key]));
    
    fetch(url)
    .then(response => {
        if (!response.ok) {
            if (response.status === 204) {
                return [];
            } else {
                document.getElementById(tableBodyId).innerHTML = '<tr><td colspan="9">No Records Found.</td></tr>';
                throw new Error('Failed to fetch data');
            }
        }
        return response.json();
    })
    .then(data => {
        const tableBody = document.getElementById(tableBodyId);
        tableBody.innerHTML = '';


        if (data && data.length > 0) {
            data.sort((a, b) => {
                const valA = a['pdSurypropnoVc'] ? a['pdSurypropnoVc'].toString().toLowerCase() : '';
                const valB = b['pdSurypropnoVc'] ? b['pdSurypropnoVc'].toString().toLowerCase() : '';
                return valA.localeCompare(valB); // ascending order
            });

            data.forEach((item, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `<td>${index + 1}</td>` +
                    columns.map(col => `<td>${item[col] || ''}</td>`).join('') +
                    `<td>` + getActionButtons(actionType, item) + `</td>`;
                tableBody.appendChild(row);
            });
        } else {
            tableBody.innerHTML = '<tr><td colspan="' + (columns.length + 2) + '">No results found.</td></tr>';
        }
    })
    .catch(error => console.error('Error fetching property records:', error));
}
// getActionButtons is getting used to segregate the buttons for uploading cad images and property images
// and viewing survey report,calculationsheet also deleting reports
function getActionButtons(actionType, item) {
    if (actionType === 'survey') {
        return `
            <button class="btn btn-primary mr-2" onclick="viewSurveyReport('${item.pdNewpropertynoVc}')">Survey Report</button>
            <button class="btn btn-primary mr-2" onclick="viewCalculationSheet('${item.pdNewpropertynoVc}')">Calculation Sheet</button>
            <button class="btn btn-danger" onclick="showDeleteModal('${item.pdNewpropertynoVc}', '${item.pdSurypropnoVc}', '${item.pdOwnernameVc}', '${item.user_id}', '${item.pdWardI}')">Delete</button>
        `;
    }

    else if (actionType === 'uploadFiles') {
        return `
            <button class="btn btn-primary mr-2" onclick="viewSurveyReport('${item.pdNewpropertynoVc}')">Survey Report</button>
            <button class="btn btn-success mr-2" onclick="uploadFile('${item.pdNewpropertynoVc}', 'cad')">Upload CAD</button>
            <button class="btn btn-success" onclick="uploadFile('${item.pdNewpropertynoVc}', 'propertyImage')">Upload Property Image</button>
        `;
    }

    else if (actionType === 'notices') {
        return `
            <button class="btn btn-warning mr-2" onclick="viewSpecialNotice(null, '${item.pdNewpropertynoVc}')">Special Notice</button>
        `;
    }

    else if (actionType === 'objection') {
       return `
           <button class="btn btn-primary" onclick="openAfterHearingProperty('${item.newPropertyNo}')">
               After Hearing Decision
           </button>
       `;
    }
    else if (actionType === 'arrears') {
        return `
            <button class="btn btn-primary"
                onclick="editArrears(
                    '${item.pdNewpropertynoVc}',
                    '${item.pdFinalpropnoVc || ''}',
                    '${item.pdOwnernameVc || ''}',
                    '${item.pdWardI || ''}'
                )">
                Edit Taxes
            </button>
        `;
    }


    return '';
}

function editArrears(newPropertyNo, finalPropertyNo, ownerName, ward) {
  document.getElementById("arrears").style.display = "none";
  document.getElementById("arrears-tax").style.display = "block";

  document.getElementById("newPropertyNo").value = newPropertyNo || '';
  document.getElementById("finalPropertyNo").value = finalPropertyNo || '';

  document.getElementById("finalPropertyNo").value = finalPropertyNo || '';
  document.getElementById("ownerName").value = ownerName || '';
  document.getElementById("finalPropertyNo").readOnly = true;
  document.getElementById("ownerName").readOnly = true;

  if (document.getElementById("ward")) {
    document.getElementById("ward").value = ward || '';
  }

  document.getElementById("arrears-tax").scrollIntoView({ behavior: "smooth" });
}

// performSearch to get the searching parameters from the sections such as surveyreports and uploadfiles
function performSearch(sectionId, columns) {
    const spn = document.querySelector(`#${sectionId} #${sectionId}-spnInput`).value.trim();
    const ownerName = document.querySelector(`#${sectionId} #${sectionId}-ownerNameInput`).value.trim();
    const wardNumber = document.querySelector(`#${sectionId} #${sectionId}-wardNumberInput`).value.trim();
    const finalPropertyNo = document.querySelector(`#${sectionId} #${sectionId}-finalPropertyNoInput`).value.trim();
    const searchParams = {};

    if (spn) searchParams.surveyPropertyNo = spn;
    if (ownerName) searchParams.ownerName = ownerName;
    if (wardNumber) searchParams.wardNo = wardNumber;
    if (finalPropertyNo) searchParams.finalPropertyNo = finalPropertyNo;

    let tableBodyId, actionType;
    if (sectionId === 'surveyReports') {
        tableBodyId = 'searchResultsSurvey';
        actionType = 'survey';
    } else if (sectionId === 'uploadFiles') {
        tableBodyId = 'searchResultsUpload';
        actionType = 'uploadFiles';
    } else if (sectionId === 'notices') {
        tableBodyId = 'notices-searchResults';
        actionType = 'notices';
    }

    fetchPropertyRecords('/3g/searchNewProperties', searchParams, tableBodyId, actionType, columns);
}


document.getElementById('searchButtonSurvey').addEventListener('click', function() {
    performSearch('surveyReports',  ['pdSurypropnoVc', 'pdOwnernameVc', 'user_id', 'createddateVc', 'pdPropertyaddressVc', 'pdWardI', 'pdZoneI']);
});
// For Upload Files Search
document.getElementById('searchButtonUpload').addEventListener('click', function() {
    performSearch('uploadFiles',  ['pdSurypropnoVc', 'pdOwnernameVc', 'user_id', 'createddateVc', 'pdPropertyaddressVc', 'pdWardI', 'pdZoneI']);
});
// For Notices Search
document.getElementById('notices-searchButton').addEventListener('click', function() {
    performSearch('notices',  ['pdFinalpropnoVc', 'pdOwnernameVc', 'pdPropertyaddressVc', 'pdWardI', 'pdZoneI']);
});


function viewSurveyReport(pdNewpropertynoVc) {
console.log('View property:', pdNewpropertynoVc);
window.open('/3g/showsurvey/' + pdNewpropertynoVc, '_blank', 'noopener,noreferrer');
}


function viewCalculationSheet(pdNewpropertynoVc) {
console.log('View property:', pdNewpropertynoVc);
window.open('/3g/calculationSheet/' + pdNewpropertynoVc, '_blank', 'noopener,noreferrer');
}

function viewCalculationSheetRt(pdNewpropertynoVc) {
console.log('View property:', pdNewpropertynoVc);
window.open('/rtcc/' + pdNewpropertynoVc, '_blank', 'noopener,noreferrer');
}


function viewBatchAssessmentReport() {
 const wardNo = document.getElementById('batchReportWard').value;
    if (!wardNo) {
        alert("Please select a ward before viewing the report.");
        return;
    }
    const url = `/3g/batchAssessmentReport/${wardNo}`;
    window.open(url, '_blank', 'noopener,noreferrer');
}


function showDeleteModal(pdNewpropertynoVc,surveyPropNo,ownerName,createdBy,ward) {
console.log('Show delete modal for property:', pdNewpropertynoVc);
deletePropNo = pdNewpropertynoVc;
deleteSurveyPropNo = surveyPropNo;
deleteOwnerName = ownerName;
deleteCreatedBy = createdBy;
deleteWard = ward;
// Show the delete modal
$('#deleteModal').modal('show');
}

function uploadFile(newPropertyNo, fileType) {
    const fileInput = Object.assign(document.createElement('input'), { type: 'file', accept: 'image/*', style: 'display:none' });

    fileInput.onchange = () => {
        const file = fileInput.files[0];
        if (!file) return alert('No file selected.');

        const [maxWidth, maxHeight] = fileType === 'cad' ? [1600, 1280] : [525, 700];

        compressImage(file, maxWidth, maxHeight, null, (blob) => 
            sendCompressedBlob(newPropertyNo, blob, file.name, fileType)
        );
    };

    document.body.append(fileInput);
    fileInput.click();
    document.body.removeChild(fileInput);
}


//so after we upload file the image will be passed into the compressimage function then-
//-callback function which we will use is sendCompressedBlob() given below
function sendCompressedBlob(propertyNo, blob, fileName, uploadType) {
    const formData = new FormData();
    formData.append('newPropertyNo', propertyNo);
    formData.append(uploadType === 'cad' ? 'cadImage' : 'propertyImage', blob, fileName);

    fetch(`/3g/${uploadType === 'cad' ? 'uploadCadImage' : 'uploadPropertyImage'}`, {
        method: 'POST',
        body: formData
    })
    .then(res => res.ok ? alert(`${uploadType} uploaded!`) : alert(`Failed to upload ${uploadType}`))
    .catch(err => {
        console.error(err);
        alert('Unexpected error.');
    });
}

// Confirm deletion button in modal
$('#confirmDeleteButton').on('click', function() {
    console.log("Deleting...");
    var remarks = $('#deletionRemarks').val();
    if (!remarks.trim()) {
        alert('Please provide remarks for deletion.');
        return;
    }

    console.log("Confirm delete clicked with remarks:", remarks);

    $.ajax({
        url: '/3gSurvey/deleteNewProperty',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            pdNewpropertynoVc: deletePropNo,
            surveyPropNo: deleteSurveyPropNo,
            ownerName: deleteOwnerName,
            createdBy: deleteCreatedBy,
            remarks: remarks,
            ward: deleteWard
        }),
        success: function(response) {
            console.log("Deletion response:", response);
            if (response.success) {
                $('#deleteModal').modal('hide');
                alert(response.message);
                const spn = document.getElementById('spnInput').value.trim();
                const ownerName = document.getElementById('ownerNameInput').value.trim();
                const wardNumber = document.getElementById('wardNumberInput').value.trim();

                const searchParams = {};
                if (spn) searchParams.surveyPropertyNo = spn;
                if (ownerName) searchParams.ownerName = ownerName;
                if (wardNumber) searchParams.wardNo = wardNumber;

                fetchPropertyRecords('/3g/searchNewProperties', searchParams, 'searchResults', ['pdSurypropnoVc', 'pdOwnernameVc', 'user_id', 'createddateVc', 'pdPropertyaddressVc', 'pdWardI', 'pdZoneI']);
            } else {
                alert(response.message);
            }
        },
        error: function(xhr) {
            console.error("Deletion error:", xhr);
            var errorMessage = xhr.responseJSON && xhr.responseJSON.message ? xhr.responseJSON.message : 'Error deleting record.';
            alert(errorMessage);
        }
    });
});
function addRow(tableId, inputNames, dropdownConfig = []) {
    const table = document.getElementById(tableId);
    const row = document.createElement('tr');

    inputNames.forEach(name => {
        const cell = document.createElement('td');
        
        if (dropdownConfig.some(config => config.name === name)) {
            const config = dropdownConfig.find(config => config.name === name);
            const select = document.createElement('select');
            select.className = 'form-control';
            select.name = name;
            select.id = name + '-' + table.rows.length; // Ensure unique ID for the dropdown

            // Placeholder option
            select.innerHTML = `<option value="">Select an option</option>`;
            cell.appendChild(select);
            
            // Call populateDropdown with null handling for displayProperty and idProperty
            populateDropdown(select.id, config.endpointUrl, config.displayProperty || null, config.idProperty || null);
        } else {
            const input = document.createElement('input');
            input.type = 'text';
            input.className = 'form-control';
            input.name = name;
            input.placeholder = name.replace(/([A-Z])/g, ' $1');
            cell.appendChild(input);
        }

        row.appendChild(cell);
    });

    const actionsCell = document.createElement('td');
    actionsCell.innerHTML = `
        <button type="button" class="btn btn-success btn-sm" onclick="saveRow(this)">Save</button>
        <button type="button" class="btn btn-secondary btn-sm" onclick="removeRow(this)">Cancel</button>
    `;
    row.appendChild(actionsCell);
    table.appendChild(row);
}

function saveRow(button) {
    const row = button.closest('tr');
    row.querySelectorAll('input').forEach(input => {
        input.setAttribute('readonly', true);
    });
    button.closest('td').innerHTML = `
        <button type="button" class="btn btn-warning btn-sm" onclick="editRow(this)">Edit</button>
        <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Delete</button>
    `;
}
function editRow(button) {
    const row = button.closest('tr');
    row.querySelectorAll('input').forEach(input => {
        input.removeAttribute('readonly');
    });
    button.closest('td').innerHTML = `
        <button type="button" class="btn btn-success btn-sm" onclick="saveRow(this)">Save</button>
        <button type="button" class="btn btn-secondary btn-sm" onclick="removeRow(this)">Cancel</button>
    `;
}
function removeRow(button) {
    const row = button.closest('tr');
    row.remove();
}
//var sessionTimeout = 10 * 60 * 1000;
//setTimeout(function() {
//    window.location.reload();
//}, sessionTimeout);

//phase 2
function fetchConsolidatedTaxesCheckbox() {
  // Fetch consolidated taxes to create checkboxes
  fetch('/3g/getAllConsolidatedTaxes')
    .then(response => response.json())
    .then(taxes => {
      console.log("Fetched taxes:", taxes);
      const container = document.getElementById('rvCheckboxesContainer');
      container.innerHTML = ''; // Clear previous entries if any

      // Create Select All checkbox
      const selectAllDiv = document.createElement('div');
      selectAllDiv.className = 'form-check';

      const selectAllCheckbox = document.createElement('input');
      selectAllCheckbox.type = 'checkbox';
      selectAllCheckbox.className = 'form-check-input';
      selectAllCheckbox.id = 'selectAll';
      selectAllCheckbox.addEventListener('change', () => {
        const allCheckboxes = document.querySelectorAll('#rvCheckboxesContainer .form-check-input');
        allCheckboxes.forEach(checkbox => checkbox.checked = selectAllCheckbox.checked);
      });

      const selectAllLabel = document.createElement('label');
      selectAllLabel.className = 'form-check-label';
      selectAllLabel.htmlFor = 'selectAll';
      selectAllLabel.textContent = 'Select All';

      selectAllDiv.appendChild(selectAllCheckbox);
      selectAllDiv.appendChild(selectAllLabel);
      container.appendChild(selectAllDiv);

      // Create checkboxes for each tax
      taxes.forEach(tax => {
        const div = document.createElement('div');
        div.className = 'form-check';

        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.className = 'form-check-input';
        checkbox.id = `tax-${tax.id}`;
        checkbox.value = tax.taxNameVc;
        checkbox.name = 'applicableonVc';
        checkbox.dataset.taxKeyL = tax.taxKeyL;

        const label = document.createElement('label');
        label.className = 'form-check-label';
        label.htmlFor = `tax-${tax.id}`;
        label.textContent = tax.taxNameVc;

        div.appendChild(checkbox);
        div.appendChild(label);
        container.appendChild(div);
      });
    })
    .catch(error => console.error('Error fetching consolidated taxes:', error));
}


//function preSelectTaxes(taxKeysArray) {
//  taxKeysArray.forEach(key => {
//    const checkbox = document.getElementById(`tax-${key}`);
//    if (checkbox) checkbox.checked = true;
//  });
//}

async function fetchAndPopulateWardsForBatch(endpointUrl, tableBodyId) {
    try {
        const response = await fetch(endpointUrl);
        if (!response.ok) throw new Error('Failed to fetch ward data');
        const data = await response.json();

        const tableBody = document.getElementById(tableBodyId);
        tableBody.innerHTML = ''; // Clear existing content

        data.forEach((ward, index) => {
            const row = document.createElement('tr');

            // Alternate row coloring for better readability
            if (index % 2 === 1) {
                row.classList.add('table-secondary');
            }

            // Add the index column
            const indexCell = document.createElement('td');
            indexCell.className = "text-center";
            indexCell.textContent = index + 1;
            row.appendChild(indexCell);

            // Add ward information (e.g., ward number)
            const wardCell = document.createElement('td');
            wardCell.className = "text-center";
            wardCell.textContent = ward.wardNo;
            row.appendChild(wardCell);

            // Add actions
            const actionsCell = document.createElement('td');
            actionsCell.className = "text-center";
            // "Process Ward" button (shown only if permitted by server)
            const allowProcEl = document.getElementById('perm-controls');
            const allowProcess = allowProcEl && allowProcEl.dataset && allowProcEl.dataset.allowProcess === 'true';
            if (allowProcess) {
                const processButton = document.createElement('button');
                processButton.textContent = 'Process Ward';
                processButton.classList.add('btn', 'btn-success', 'mr-2');
                processButton.onclick = function() {
                    processBatchForWard(ward.wardNo, row);
                };
                actionsCell.appendChild(processButton);
            }

            // "View Batch Report" button
            const viewBatchReportButton = document.createElement('button');
            viewBatchReportButton.textContent = 'Batch Assessment Report';
            viewBatchReportButton.classList.add('btn', 'btn-primary', 'mr-2');
            viewBatchReportButton.onclick = function() {
                viewBatchAssessmentReport(ward.wardNo);
            };
            actionsCell.appendChild(viewBatchReportButton);

            // "View Calculation Sheet" button
            const viewCalculationSheetButton = document.createElement('button');
            viewCalculationSheetButton.textContent = 'Batch Calculation Report';
            viewCalculationSheetButton.classList.add('btn', 'btn-primary', 'mr-2');
            viewCalculationSheetButton.onclick = function() {
                viewBatchCalculationSheet(ward.wardNo);
            };
            actionsCell.appendChild(viewCalculationSheetButton);

            // "View Special Notices" button
            const viewSpecialNoticeButton = document.createElement('button');
            viewSpecialNoticeButton.textContent = 'Batch Special Notices';
            viewSpecialNoticeButton.classList.add('btn', 'btn-primary','mr-2');
            viewSpecialNoticeButton.onclick = function() {
                viewSpecialNotice(ward.wardNo);
            };
            actionsCell.appendChild(viewSpecialNoticeButton);

            // "View Hearing Notices" button
            const viewHearingNoticeButton = document.createElement('button');
            viewHearingNoticeButton.textContent = 'Batch Hearing Notices';
            viewHearingNoticeButton.classList.add('btn', 'btn-primary','mr-2');
            viewHearingNoticeButton.onclick = function() {
                viewHearingNotice(ward.wardNo);
            };
            actionsCell.appendChild(viewHearingNoticeButton);


            // "View OrderSheets" Button
            const viewOrderSheetButton = document.createElement('button');
            viewOrderSheetButton.textContent = 'Batch OrderSheet';
            viewOrderSheetButton.classList.add('btn', 'btn-primary','mr-2');
            viewOrderSheetButton.onclick = function() {
                viewOrderSheet(ward.wardNo);
            };

            actionsCell.appendChild(viewOrderSheetButton);

            // "View Hearing Notices" button
            const viewAssessmentRegisterButton = document.createElement('button');
            viewAssessmentRegisterButton.textContent = 'Batch Assessment Register';
            viewAssessmentRegisterButton.classList.add('btn', 'btn-primary','mr-2');
            viewAssessmentRegisterButton.onclick = function() {
                viewSecondaryBatchAssessmentReport(ward.wardNo);
            };
            actionsCell.appendChild(viewAssessmentRegisterButton);

            // "View Tax Bills" Button
            const viewTaxBillsButton = document.createElement('button');
            viewTaxBillsButton.textContent = 'Tax Bills';
            viewTaxBillsButton.classList.add('btn', 'btn-primary','mr-2');
            viewTaxBillsButton.onclick = function() {
                viewTaxBills(ward.wardNo);
            };

            actionsCell.appendChild(viewTaxBillsButton);

            row.appendChild(actionsCell);
            tableBody.appendChild(row);

            // Check if the ward is already processed and update button visibility

        });
    } catch (error) {
        console.error('Error fetching ward data:', error);
    }
}

function processBatchForWard(wardNo) {
    alert(`Batch job has been Started for Ward No: ${wardNo}`);
    const url = `/3g/processBatch/${wardNo}`;

    const spinnerOverlay = document.createElement('div');
    spinnerOverlay.setAttribute('id', 'loadingSpinner');
    spinnerOverlay.style.cssText = 'position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.5); z-index: 9999; display: flex; justify-content: center; align-items: center;';
    const spinner = document.createElement('div');
    spinner.className = 'spinner';
    spinnerOverlay.appendChild(spinner);
    document.body.appendChild(spinnerOverlay);

    fetch(url, {
        method: 'POST',
    })
    .then(response => {
        document.body.removeChild(spinnerOverlay);
        if (response.ok) {
            alert(`Batch job has been completed for Ward No: ${wardNo}`);
//             downloadResultLogsPdf(wardNo);
        } else {
            response.text().then(text => {
                alert(`Batch job has been failed: ${text}`);
            });
//            downloadResultLogsPdf(wardNo);
        }
    })
    .catch(error => {
        console.error('Error processing batch:', error);
        alert('An unexpected error occurred while starting the batch. Please try again later.');
    });
}

//function downloadResultLogsPdf(wardNo) {
//    fetch(`/3g/resultLogs/${wardNo}`)
//        .then(res => res.blob())
//        .then(blob => {
//            const url = URL.createObjectURL(blob);
//            const a = Object.assign(document.createElement('a'), {
//                href: url,
//                download: `Ward_${wardNo}_ResultLogs.pdf`
//            });
//            document.body.appendChild(a);
//            a.click();
//            a.remove();
//            URL.revokeObjectURL(url);
//        })
//        .catch(err => {
//            console.error('Download error:', err);
//            alert('Result Logs PDF download failed.');
//        });
//}

// Function to view the batch report for a ward
function viewBatchAssessmentReport(wardNo) {
    console.log('Viewing batch report for ward:', wardNo);
    const url = `/3g/batchAssessmentReport/${wardNo}`;
    window.open(url, '_blank', 'noopener,noreferrer');
}
function viewBatchCalculationSheet(wardNo) {
    console.log('Viewing calculation sheet for ward:', wardNo);
    const url = `/3g/batchCalculationReport/${wardNo}`;
    window.open(url, '_blank', 'noopener,noreferrer');
}
function viewCalculationSheet(wardNo) {
    console.log('Viewing calculation sheet for ward:', wardNo);
    const url = `/3g/calculationSheet/${wardNo}`;
    window.open(url, '_blank', 'noopener,noreferrer');
}
function viewSpecialNotice(wardNo, newPropertyNo) {
    let url = `/specialNotice/${wardNo}`;
    if (newPropertyNo) {
        url += `?newPropertyNo=${newPropertyNo}`;
    }
    window.open(url, '_blank', 'noopener,noreferrer');
}
function viewHearingNotice(wardNo) {
    const url = `/hearingNotice`;
    window.open(url, '_blank', 'noopener,noreferrer');
}
function viewSecondaryBatchAssessmentReport(wardNo) {
    const url = `/secondaryBatchAssessmentReport`;
    window.open(url, '_blank', 'noopener,noreferrer');
}
function viewOrderSheet(wardNo) {

    const url = `/orderSheet`;
    window.open(url, '_blank', 'noopener,noreferrer');
}
function viewTaxBills(wardNo) {
    const url = `/taxBill/${wardNo}`;
    window.open(url, '_blank', 'noopener,noreferrer');
}

// Call this function on page load to populate the table
document.addEventListener('DOMContentLoaded', function() {
    fetchAndPopulateWardsForBatch('/3g/getAllWards', 'wardsTableBody');
});

function showSubOptions() {
    const subOptionsContainer = document.getElementById('subOptionsContainer');
    const educationCessChecked = document.getElementById('educationCessOption').checked;
    subOptionsContainer.innerHTML = ''; // Clear previous options

    if (educationCessChecked) {
        subOptionsContainer.innerHTML = `
            <p><b>Note: Select the rates for Education Cess as per the Rate Types</b></p>
            <div class="form-check">
                <input class="form-check-input" type="radio" name="subCessOption" id="residentialOption" value="Residential">
                <label class="form-check-label" for="residentialOption">Residential</label>
            </div>
            <div class="form-check">
                <input class="form-check-input" type="radio" name="subCessOption" id="commercialOption" value="Commercial">
                <label class="form-check-label" for="commercialOption">Commercial</label>
            </div>
        `;
    }
}
function submitCouncilDetailsForm() {
    // Get form inputs
    const standardNameInput = document.getElementById('standardName');
    const localNameInput = document.getElementById('localName');
    const imageInput = document.getElementById('councilImage'); // File input for logo
    const chiefSignInput = document.getElementById('chiefOfficerSign'); // File input for chief officer sign
    const companySignInput = document.getElementById('companySign'); // File input for company sign

    // Field created by Himanshu for standardization and localization of site and district
    const standardSiteNameInput = document.getElementById('standardSiteName');
    const localSiteNameInput = document.getElementById('localSiteName');
    const standardDistrictName = document.getElementById('standardDistrictName');
    const localDistrictName = document.getElementById('localDistrictName');

    // Helper to build and submit regardless of logo presence
    const buildAndSubmit = (logoBlob) => {
        const formData = new FormData();
        formData.append('standardName', standardNameInput.value);
        formData.append('localName', localNameInput.value);
        formData.append('standardSiteNameVc' , standardSiteNameInput.value);
        formData.append('localSiteNameVc', localSiteNameInput.value);
        formData.append('standardDistrictNameVc' , standardDistrictName.value);
        formData.append('localDistrictNameVc' , localDistrictName.value);
        if (logoBlob) {
            const fileRef = imageInput.files && imageInput.files[0];
            formData.append('councilImage', logoBlob, (fileRef && fileRef.name) ? fileRef.name : 'council.png');
        }
        // Append signatures if provided (no compression for simplicity)
        const chiefFile = chiefSignInput && chiefSignInput.files ? chiefSignInput.files[0] : null;
        if (chiefFile) formData.append('chiefOfficerSign', chiefFile, chiefFile.name);
        const companyFile = companySignInput && companySignInput.files ? companySignInput.files[0] : null;
        if (companyFile) formData.append('companySign', companyFile, companyFile.name);
        fetch('/3g/updateCouncilDetails', { method: 'POST', body: formData })
            .then((response) => {
                if (response.ok) {
                    alert('Council details submitted successfully!');
                    document.getElementById('councilDetailsForm').reset();
                } else {
                    alert('Failed to submit council details. Please try again.');
                }
            })
            .catch((error) => {
                console.error('Error occurred while submitting council details:', error);
                alert('An unexpected error occurred. Please try again later.');
            });
    };
    const file = imageInput.files && imageInput.files[0];
    if (file) {
        // Compress the image and then submit
        compressImage(file, 525, 700, null, (compressedBlob) => {
            buildAndSubmit(compressedBlob);
        });
    } else {
        // No logo selected; still submit other fields and signatures
        buildAndSubmit(null);
    }
}
function compressImage(file,maxWidth,maxHeight, previewId, callback) {
    if (!file) return;  // Exit if no file is selected

   
    const quality = 0.7;   // Quality of the compressed image (0 to 1)

    const reader = new FileReader();
    reader.onload = (event) => {
        const img = new Image();
        img.src = event.target.result;

        img.onload = () => {
            const canvas = document.createElement('canvas');
            let width = img.width;
            let height = img.height;

            // Adjust dimensions while maintaining the aspect ratio
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

            // Convert the canvas to a blob
            canvas.toBlob(
                (blob) => {
                    if (blob) {
                        callback(blob); // Pass the blob to the callback
                    }
                },
                'image/jpeg',
                quality
            );
        };

        img.onerror = () => {
            console.error('Error loading image for compression.');
        };
    };

    reader.onerror = () => {
        console.error('Error reading file with FileReader.');
    };

    reader.readAsDataURL(file);
}

document.addEventListener('DOMContentLoaded', () => {
  fetch('/3gSurvey/getPropertiesCount')
    .then(res => res.json())
    .then(data => {
      const container = document.getElementById('wardTilesContainer');
      let total = 0;

      Object.entries(data).forEach(([key, value]) => {
        if (key.toLowerCase() !== 'total') {
          const tile = document.createElement('div');
          tile.className = 'ward-tile';
          tile.innerHTML = `
            <div class="ward-name">${key}</div>
            <div class="ward-count">${value}</div>
          `;
          container.appendChild(tile);
        } else {
          total = value;
        }
      });

      // Optional: show total as a separate tile
      const totalTile = document.createElement('div');
      totalTile.className = 'ward-tile';
      totalTile.style.backgroundColor = '#d4edda';
      totalTile.innerHTML = `
        <div class="ward-name">Total</div>
        <div class="ward-count">${total}</div>
      `;
      container.appendChild(totalTile);
    })
    .catch(err => console.error('Failed to load ward data:', err));
});

function loadObjectionTakenProperties() {
    const queryParams = {
        spn: document.getElementById('objection-spnInput').value,
        finalPropertyNo: document.getElementById('objection-finalPropertyNoInput').value,
        ownerName: document.getElementById('objection-ownerNameInput').value,
        ward: document.getElementById('objection-wardNumberInput').value
    };

    fetchPropertyRecords(
        '/3g/getObjectionTakenProperties',
        queryParams,
        'objection-searchResults',
        'objection',
        ['finalPropertyNo', 'ownerName', 'wardNo', 'reasons', 'hearingStatus', 'objectionDate']
    );
}

function loadArrearsProperties() {
    const queryParams = {
        surveyPropertyNo: document.getElementById('arrears-spnInput').value,
        finalPropertyNo: document.getElementById('arrears-finalPropertyNoInput').value,
        ownerName: document.getElementById('arrears-ownerNameInput').value,
        wardNo: document.getElementById('arrears-wardNumberInput').value
    };
    fetchPropertyRecords(
        '/3g/searchNewProperties',
        queryParams,
        'arrears-searchResults',
        'arrears',
        ['pdFinalpropnoVc', 'pdOwnernameVc', 'pdPropertyaddressVc', 'pdWardI', 'pdZoneI']
    );
}

function openAfterHearingProperty(newPropertyNo) {
  window.selectedPropertyNo = newPropertyNo;
  console.log("thisisnew " + newPropertyNo);

  document.getElementById("decisionSelect").value = "";

  const decisionModal = new bootstrap.Modal(document.getElementById("hearingDecisionModal"));
  decisionModal.show();

  document.getElementById("confirmDecisionBtn").onclick = function () {
    const decision = document.getElementById("decisionSelect").value;
    if (!decision) {
      alert("Please select a decision before proceeding.");
      return;
    }

    decisionModal.hide();

    if (decision === "retained" || decision === "absent") {
      // Immediate backend update (no data changes)
      markObjectionStatus(newPropertyNo, decision.toUpperCase());
    } else if (decision === "changed") {
      // If changed â€” open 2nd modal
      openChangeTypeModal(newPropertyNo);
    }
  };
}

// =======================
// ðŸ”¹ Step 2: Select Change Type (RV / Assessment)
// =======================
function openChangeTypeModal(newPropertyNo) {
  document.getElementById("changeTypeSelect").value = "";

  const changeModal = new bootstrap.Modal(document.getElementById("changeTypeModal"));
  changeModal.show();

  document.getElementById("confirmChangeTypeBtn").onclick = function () {
    const changeType = document.getElementById("changeTypeSelect").value;
    if (!changeType) {
      alert("Please select a change type before proceeding.");
      return;
    }

    changeModal.hide();

    // âœ… Simple boolean logic â€” no need toUpperCase()
    const byRv = (changeType === "RV");
    const byAssessment = (changeType === "ASSESSMENT");


    localStorage.setItem("afterHearingDecision", "CHANGED");
    localStorage.setItem("afterHearingChangeType", changeType);
    localStorage.setItem("afterHearingByRv", byRv);
    localStorage.setItem("afterHearingByAssessment", byAssessment);
    localStorage.setItem("afterHearingProperty", newPropertyNo);

    // ðŸŸ¢ Open edit form
    editAssessment(newPropertyNo);
  };
}


// =======================
// ðŸ”¹ Mark Retained / Absent (immediate update)
// =======================
function markObjectionStatus(newPropertyNo, status) {
  fetch(`/3g/afterHearing/markStatus`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      newPropertyNo: newPropertyNo,
      status: status
    })
  })
    .then(response => {
      if (!response.ok) throw new Error("Failed to update status");
      return response.json();
    })
    .then(data => {
      alert(`Property marked as ${status}.`);
      window.location.reload();
    })
    .catch(err => alert(err.message));
}

// =======================
// ðŸ”¹ Open Edit Form
// =======================
function editAssessment(newPropertyNo) {
  const url = `/3gSurvey/editSurveyForm?newpropertyno=${newPropertyNo}&mode=assessment`;
  window.open(url, "_blank");
}

// =======================
// ðŸ”¹ Called AFTER officer saves modified data
// =======================
async function finalizeAfterHearingStatus(newPropertyNo) {
  try {
    if (!window.afterHearingDecision || !window.afterHearingChangeType) {
      console.warn("âš ï¸ No hearing decision stored for property " + newPropertyNo);
      return;
    }

    const payload = {
      newPropertyNo: newPropertyNo,
      status: window.afterHearingDecision,
      changeType: window.afterHearingChangeType
    };

    const res = await fetch(`/3g/afterHearing/markStatus`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!res.ok) throw new Error("Server error updating status");

    const data = await res.json();
    console.log("âœ… Hearing status finalized:", data);

    alert("After Hearing status marked as CHANGED successfully!");

  } catch (error) {
    console.error("âŒ Failed to finalize hearing status:", error);
    alert("Error marking After Hearing status. Please check console.");
  }
}

async function saveArrearsTax() {
  try {
    const form = document.getElementById("arrearsTaxForm");
    if (!form) {
      alert("âš ï¸ Form not found!");
      return;
    }

    const formData = new FormData(form);
    const jsonData = {};

    formData.forEach((value, key) => {
      // ðŸ”’ Do not convert IDs or codes to numbers
      const keepAsString = [
        "newPropertyNo",
        "finalPropertyNo",
        "financialYear",
        "ownerName"
      ];

      if (keepAsString.includes(key)) {
        jsonData[key] = value.trim(); // preserve leading zeros
      } else if (!isNaN(value) && value.trim() !== "") {
        jsonData[key] = parseFloat(value);
      } else {
        jsonData[key] = value;
      }
    });

    console.log("Submitting arrears tax data:", jsonData);

    const response = await fetch("/3g/addPropertyArrearsTax", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(jsonData),
    });

    if (!response.ok) throw new Error(`Server error: ${response.status}`);

    const result = await response.json();
    alert("âœ… Arrears Tax Saved Successfully!");

    form.reset();
    document.getElementById("arrears-tax").style.display = "none";
    document.getElementById("arrears").style.display = "block";

  } catch (error) {
    console.error("âŒ Error while saving arrears tax:", error);
    alert("âš ï¸ Failed to save arrears tax. Please check console for details.");
  }
}


