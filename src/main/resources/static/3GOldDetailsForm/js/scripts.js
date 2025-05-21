document.addEventListener('DOMContentLoaded', function() {
    initializePropertyTypesAndCaptureSelection('podPropertyTypeI', '/3gSurvey/propertytypes');
    initializeTypesAndCaptureSelection('podPropertyTypeI', 'podPropertySubTypeI', '/3gSurvey/propertySubtypes')
    initializeTypesAndCaptureSelection('podPropertySubTypeI', 'podUsageTypeI', '/3gSurvey/usageTypes');
    });
document.getElementById('propertyOldDetailsForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    const form = event.target;
    const formData = new FormData(form);
    const jsonData = {
        podZoneI: formData.get('podZoneI'),
        podWardI: formData.get('podWardI'),
        podOldPropNoVc: formData.get('podOldPropNoVc'),
        podNewPropertyNoVc: formData.get('podNewPropertyNoVc'),
        podOwnerNameVc: formData.get('podOwnerNameVc'),
        podOccupierNameVc: formData.get('podOccupierNameVc'),
        podPropertyAddressVc: formData.get('podPropertyAddressVc'),
        podConstClassVc: formData.get('podConstClassVc'),
        podPropertyTypeI: formData.get('podPropertyTypeI'),
        podPropertySubTypeI: formData.get('podPropertySubTypeI'),
        podUsageTypeI: formData.get('podUsageTypeI'),
        podNoofrooms: formData.get('podNoofrooms'),
        pdConstructionYearD: formData.get('pdConstructionYearD'),
        podPlotvalue: formData.get('podPlotvalue'),
        podTotalPropertyvalue: formData.get('podTotalPropertyvalue'),
        podBuildingvalue: formData.get('podBuildingvalue'),
        podBuiltUpAreaFl: formData.get('podBuiltUpAreaFl'),
        podPropertyTaxFl: formData.get('podPropertyTaxFl'),
        podEduCessFl: formData.get('podEduCessFl'),
        podEgcFl: formData.get('podEgcFl'),
        podTreeTaxFl: formData.get('podTreeTaxFl'),
        podEnvTaxFl: formData.get('podEnvTaxFl'),
        podFireTaxFl: formData.get('podFireTaxFl'),
        podTotalTaxFl: formData.get('podTotalTaxFl'),
        podTotalAssessmentArea: formData.get('podTotalAssessmentArea'),
        podTotalAssessmentAreaFt: formData.get('podTotalAssessmentAreaFt'),
        podTotalRatableValue: formData.get('podTotalRatableValue'),
        unitDetails: []
    };

    // Process unit details
    const unitCount = document.querySelectorAll('#unitsTable tbody tr').length;
    for (let i = 0; i < unitCount; i++) {
        jsonData.unitDetails.push({
            podOccupancy: formData.get(`unitDetails[${i}].occupancy`),
            podFloorNumber: formData.get(`unitDetails[${i}].floorNumber`),
            podOldPropertyClass: formData.get(`unitDetails[${i}].oldPropertyClass.selectedText`),
            podConstructionYear: formData.get(`unitDetails[${i}].constructionYear`),
            podUnitUsageType: formData.get(`unitDetails[${i}].unitUsageType`),
            podUnitSubUsageType: formData.get(`unitDetails[${i}].unitSubUsageType`),
            podAssessmentArea: formData.get(`unitDetails[${i}].assessmentArea`)
        });
    }
        console.log("hii data is here: "+jsonData);
    fetch(form.action, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(jsonData)
    }).then(response => {
        if (response.ok) {
            alert("Form submitted successfully!");
            window.location.href = "/3gSurvey/oldpropertydetails"; // Redirect on success
        } else {
            throw new Error('Network response was not ok.');
        }
    }).catch(error => {
        showAlertMessage('An Record with this old property number already exists: ' + podOldPropNoVc.value);
    });
});

document.addEventListener('DOMContentLoaded', function() {
const propertyUsageSelect = document.getElementById('podUsageTypeI');
propertyUsageSelect.addEventListener('change', function() {
    updateUnitUsageTypesBasedOnPropertyUsage(propertyUsageSelect.value);

});
});

async function updateUnitUsageTypesBasedOnPropertyUsage(propertyUsageId) {
const apiUrl = `/3gSurvey/usageTypes/${propertyUsageId}`; // Adjust the URL to your actual API that fetches usage types based on property usage
const unitUsageSelects = document.querySelectorAll('[name*="unitDetails[].unitUsageType"]');

for (let select of unitUsageSelects) {
    await populateSelectWithOptionsFromAPI(select, apiUrl);
}
}

async function populateSelectWithOptionsFromAPI(select, apiUrl) {
try {
    const response = await fetch(apiUrl);
    if (!response.ok) throw new Error('Failed to fetch data');
    const usageTypes = await response.json();

    console.log("API Response:", usageTypes);
    select.innerHTML = '<option value="">Select</option>'; // Clear existing options and show a default
    usageTypes.forEach(type => {
        select.options.add(new Option(type.name, type.value));
    });
} catch (error) {
    console.error(`Error fetching data from ${apiUrl}:`, error);
    // Optionally, inform the user of a failed load if the select is visible or critical
    select.innerHTML = '<option value="">Select</option>';
}
}

async function addUnit() {
var table = document.getElementById('unitsTable').getElementsByTagName('tbody')[0];
var rowIndex = table.rows.length; // Get the current number of rows to index new rows
var row = table.insertRow();

// Create cells with placeholders for dynamic content
var fields = [
    { name: `unitDetails[${rowIndex}].occupancy`, type: 'select', apiUrl: '/3gSurvey/occupancyMasters' },
    { name: `unitDetails[${rowIndex}].floorNumber`, type: 'select', apiUrl: '/3gSurvey/getUnitFloorNos' },
    { name: `unitDetails[${rowIndex}].oldPropertyClass`, type: 'select', apiUrl: '/3gSurvey/constructionClassMasters' },
    { name: `unitDetails[${rowIndex}].constructionYear`, type: 'date' },
    { name: `unitDetails[${rowIndex}].unitUsageType`, type: 'select', apiUrl: `/3gSurvey/getUnitUsageByPropUsageId/${document.getElementById('podUsageTypeI').value}` },
    { name: `unitDetails[${rowIndex}].unitSubUsageType`, type: 'select', apiUrl: '/3gSurvey/getUnitUsageSub' }, // Initially empty, filled based on unit usage type
    { name: `unitDetails[${rowIndex}].assessmentArea`, type: 'number', step: 'any' }
];

// Add the inputs or selects to each cell
for (let field of fields) {
    const cell = row.insertCell();
    let element;

    if (field.type === 'select') {
        element = document.createElement('select');
        element.name = field.name;
        element.addEventListener('change', function() {
            const hiddenInput = document.querySelector(`input[name="${field.name}.selectedText"]`);
            hiddenInput.value = element.options[element.selectedIndex].text;
        });

        if (field.apiUrl) {
            await populateSelectWithOptionsFromAPI(element, field.apiUrl);
            if (field.name.includes('unitUsageType')) {
                element.addEventListener('change', () => {
                    const subtypeSelect = row.querySelector(`[name="${field.name.replace('unitUsageType', 'unitSubUsageType')}"]`);
                    const apiUrl = `/3gSurvey/getUnitUsageSub/${element.value}`; // Adjust the URL to your endpoint
                    populateSelectWithOptionsFromAPI(subtypeSelect, apiUrl);
                });
            }
        } else {
            element.innerHTML = '<option value="">Select usage type first</option>';
        }
    } else {
        element = document.createElement('input');
        element.type = field.type;
        element.name = field.name;
        if (field.type === 'number') element.step = 'any';
    }

    cell.appendChild(element);

    // Add a hidden input field for storing the selected text
    if (field.type === 'select') {
        const hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = `${field.name}.selectedText`;
        cell.appendChild(hiddenInput);
    }
}

// Add a cell with a remove button
var actionCell = row.insertCell();
actionCell.innerHTML = `<button type="button" onclick="removeUnit(this)">Remove</button>`;
}


function removeUnit(button) {
    var row = button.parentNode.parentNode;
    row.parentNode.removeChild(row);
    // Update indices of remaining rows to keep form submission data accurate
    updateIndices();
}

function updateIndices() {
    var rows = document.querySelectorAll('#unitsTable tbody tr');
    rows.forEach((row, index) => {
        row.querySelectorAll('input').forEach(input => {
            var name = input.name.match(/^(unitDetails\[)\d+(\].+)$/);
            if (name) {
                input.name = name[1] + index + name[2];
            }
        });
    });
}
   function updateTotalAssessmentArea() {
        var assessmentAreas = [];
        var table = document.getElementById('unitsTable').getElementsByTagName('tbody')[0];
        var rows = table.getElementsByTagName('tr');

        for (var i = 0; i < rows.length; i++) {
            var assessmentAreaInput = rows[i].querySelector('input[name*="assessmentArea"]');
            if (assessmentAreaInput && assessmentAreaInput.value) {
                var value = parseFloat(assessmentAreaInput.value);
                if (!isNaN(value)) { // Ensure the parsed value is a number
                    assessmentAreas.push(value);
                }
            }
        }

        var totalAssessmentArea = assessmentAreas.reduce(function(acc, value) {
            return acc + value;
        }, 0);

        var totalAreaInput = document.getElementById('podTotalAssessmentArea');
        if (totalAreaInput) { // Check if the element exists
            totalAreaInput.value = totalAssessmentArea.toFixed(2);
        }
    }

    // Example of binding this function to input events
    document.getElementById('unitsTable').addEventListener('input', function(event) {
        if (event.target.name.includes('assessmentArea')) {
            updateTotalAssessmentArea();
        }
    });

    async function fetchAllTypesOfAPI(apiUrl, selectElement) {
        try {
            const response = await fetch(apiUrl);
            if (!response.ok) throw new Error(`Failed to fetch data from ${apiUrl}`);
            const items = await response.json();

            // Populate the select element
            for (let item of items) {
                let option = new Option(item.name, item.value);
                selectElement.appendChild(option);
            }
        } catch (error) {
            console.error(`Error fetching data from ${apiUrl}:`, error);
        }
    }
async function initializePropertyTypesAndCaptureSelection(selectId, apiUrl) {
    const selectElement = document.getElementById(selectId);

    // Fetch the property types from the API and populate the select element.
    try {
        const response = await fetch(apiUrl);
        if (!response.ok) throw new Error('Failed to fetch data');
        const propertyTypes = await response.json();

        let optionsHtml = '<option value="">Select</option>';
        propertyTypes.forEach(type => {
            // Apply specific format for "Building Type" dropdown or others as needed
            if (selectId === 'bt-property-type-select') {
                // For "Building Type", concatenate the value and name
                optionsHtml += `<option value="${type.value},${type.name}">${type.name}</option>`;
            } else {
                // For other dropdowns, just use the ID or another appropriate attribute
                optionsHtml += `<option value="${type.value}">${type.name}</option>`;
            }
        });
        selectElement.innerHTML = optionsHtml;

    }catch (error) {
        console.error(`Error initializing property types for select #${selectId}:`, error);
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

        } catch (error) {
            console.error(`Error fetching property subtypes for ${subtypeSelectId}:`, error);
        }
    });
}

function showAlertMessage(message) {
    var alertPlaceholder = document.getElementById('alertPlaceholder');
    var messageElement = document.createElement('p');
    messageElement.style.color = 'red'; // Set the color to red
    messageElement.innerText = message; // Set the alert message text
    alertPlaceholder.innerHTML = '';
    alertPlaceholder.appendChild(messageElement);
    alertPlaceholder.style.display = 'block';
}