
 document.addEventListener('DOMContentLoaded', function() {

    initializePropertyTypesAndCaptureSelection('podPropertyTypeI', '/3gSurvey/propertytypes');
    initializeTypesAndCaptureSelection('podPropertyTypeI', 'podPropertySubTypeI', '/3gSurvey/propertySubtypes')
    initializeTypesAndCaptureSelection('podPropertySubTypeI', 'podUsageTypeI', '/3gSurvey/usageTypes');
    //initializeDropdown('podPropertySubTypeI', '/3gSurvey/propertySubtypes', details.podPropertySubTypeI, true, details.podPropertyTypeI);
    });

    let selectedSubtype;

    document.getElementById('podPropertySubTypeI').addEventListener('change', function() {
        selectedSubtype = this.value;
        console.log("Selected subtype:", selectedSubtype);
    });

    $('#searchButton').click(function () {
        var oldPropertyNo = $('#oldPropertyNumberInput').val().trim();
        var ownerName = $('#ownerNameInput').val().trim();
        var ward = $('#wardInput').val();
        var searchParams = new URLSearchParams();

        if (oldPropertyNo) searchParams.set("oldPropertyNo", oldPropertyNo);
        if (ownerName) searchParams.set("ownerName", ownerName);
        if (ward) searchParams.set("wardNo", ward);

        var baseUrl = "/3gSurvey/searchProperties";
        var fullUrl = baseUrl + "?" + searchParams.toString();

        document.getElementById('propertyForm').style.display = 'none';
        document.getElementById('searchResultsContainer').style.display = 'block';

        $.ajax({
            url: fullUrl,
            type: 'GET',
            success: function (data) {
                if (data && data.length > 0) {
                    var content = '<table class="table table-hover"><thead class="thead-light"><tr><th>#</th><th>Ward</th><th>Old Property No.</th><th>Owner Name</th></tr></thead><tbody>';
                    data.forEach(function (item, index) {
                        content += `<tr class="clickable-row" data-index="${index}">
                                        <td>${index + 1}</td>
                                        <td>${item.podWardI}</td>
                                        <td>${item.podOldPropNoVc}</td>
                                        <td>${item.podOwnerNameVc}</td>
                                    </tr>`;
                    });
                    content += '</tbody></table>';
                    $('#searchResults').html(content);
                    attachRowClickHandlers(data);
                } else {
                    $('#searchResults').html('<div class="alert alert-warning">No results found.</div>');
                }
            },
            error: function () {
                $('#searchResults').html('<div class="alert alert-danger">Error processing your request.</div>');
            }
        });
    });

    function attachRowClickHandlers(data) {
        $('.clickable-row').on('click', function() {
            var itemIndex = $(this).data('index'); // Get the index of the clicked row
            var selectedItem = data[itemIndex]; // Get the selected item data
            var podRefNo = selectedItem.podRefNoVc; // Retrieve the podRefNo from the selected item

            if (podRefNo) {
                // Call the searchOldProperty function with podRefNo
                searchOldProperty(podRefNo);
                document.getElementById('searchResultsContainer').style.display = 'none';
                document.getElementById('propertyForm').style.display = 'block';
            } else {
                alert("Error: Reference number (podRefNo) not found for the selected property.");
            }
        });
    }
    async function searchOldProperty(podRefNo) {
        if (!podRefNo) {
            showAlertMessage('Reference number (podRefNo) is required.');
            return;
        }
    
        const url = `/3gSurvey/OldPropertyByRefNo?podRefNo=${encodeURIComponent(podRefNo)}`;

    try {
        const response = await fetch(url, {
            headers: {
                'Accept': 'application/json',
            }
        });

        if (!response.ok) {
            throw new Error('Property not found or an error occurred.');
        }

        const details = await response.json();

        document.getElementById('podZoneI').value = details.podZoneI || '';
        document.getElementById('podWardI').value = details.podWardI || '';
        document.getElementById('podOldPropNoVc').value = details.podOldPropNoVc || '';
        document.getElementById('podNewPropertyNoVc').value = details.podNewPropertyNoVc || '';
        document.getElementById('podOwnerNameVc').value = details.podOwnerNameVc || '';
        document.getElementById('podOccupierNameVc').value = details.podOccupierNameVc || '';
        document.getElementById('podPropertyAddressVc').value = details.podPropertyAddressVc || '';
        document.getElementById('podTotalTaxFl').value = details.podTotalTaxFl || '';
        document.getElementById('podConstClassVc').value = details.podConstClassVc || '';
        document.getElementById('podPropertyTypeI').value = details.podPropertyTypeI || '';
        document.getElementById('podNoofrooms').value = details.podNoofrooms || '';
        document.getElementById('pdConstructionYearD').value = details.pdConstructionYearD ? details.pdConstructionYearD.split('T')[0] : ''; // Assuming the date is in ISO format
        document.getElementById('podPlotvalue').value = details.podPlotvalue || '';
        document.getElementById('podTotalPropertyvalue').value = details.podTotalPropertyvalue || '';
        document.getElementById('podBuildingvalue').value = details.podBuildingvalue || '';
        document.getElementById('podBuiltUpAreaFl').value = details.podBuiltUpAreaFl || '';
        document.getElementById('podPropertyTaxFl').value = details.podPropertyTaxFl || '';
        document.getElementById('podEduCessFl').value = details.podEduCessFl || '';
        document.getElementById('podEgcFl').value = details.podEgcFl || '';
        document.getElementById('podTreeTaxFl').value = details.podTreeTaxFl || '';
        document.getElementById('podEnvTaxFl').value = details.podEnvTaxFl || '';
        document.getElementById('podFireTaxFl').value = details.podFireTaxFl || '';
        document.getElementById('podTotalAssessmentArea').value = details.podTotalAssessmentArea || '';
        document.getElementById('podTotalAssessmentAreaFt').value = details.podTotalAssessmentAreaFt || '';
        document.getElementById('podTotalRatableValue').value = details.podTotalRatableValue || '';
        initializeDropdown('podPropertySubTypeI', '/3gSurvey/propertySubtypes', details.podPropertySubTypeI, true, details.podPropertyTypeI);
        initializeDropdown('podUsageTypeI', '/3gSurvey/usageTypes', details.podUsageTypeI, true, details.podPropertySubTypeI);
        document.getElementById('updateFlag').value = '1';
        document.getElementById('propertyForm').style.display = 'block';
        document.getElementById('podRefNoVc').value = details.podRefNoVc || '';
        document.getElementById('id').value = details.id || '';
        showAlertMessage('');

        if (details.unitDetails && details.unitDetails.length > 0) {
        const unitsTableBody = document.getElementById('unitsTable').querySelector('tbody');
        unitsTableBody.innerHTML = ''; // Clear previous rows
        details.unitDetails.forEach((unit, index) => {
            let row = unitsTableBody.insertRow();
            row.insertCell(0).innerHTML = `<select id='unitOccupancy-${index}' disabled></select>`;
            row.insertCell(1).innerHTML = `<select id='unitFloorNumber-${index}' disabled></select>`;
            row.insertCell(2).innerHTML = `<select id='unitOldPropertyClass-${index}' disabled></select>`;
            row.insertCell(3).innerHTML = `<input type="text" value="${unit.podConstructionYear}" disabled>`;
            row.insertCell(4).innerHTML = `<select id='unitUsageType-${index}' disabled></select>`;
            row.insertCell(5).innerHTML = `<select id='unitSubUsageType-${index}' disabled></select>`;
            row.insertCell(6).innerHTML = `<input type="text" name="podAssessmentArea" value="${unit.podAssessmentArea}" disabled>
                                           <input type="hidden" name="unitId-${index}" value="${unit.id}">`;
            row.cells[6].querySelector('input').addEventListener('input', updateTotalAssessmentArea);
            // row.insertCell(7).innerHTML = `<input type="hidden" value="${unit.id}">`;

            fetchAllTypesOfAPI('/3gSurvey/getUnitFloorNos', document.getElementById(`unitFloorNumber-${index}`), unit.podFloorNumber, 'name');
            fetchAllTypesOfAPI('/3gSurvey/constructionClassMasters', document.getElementById(`unitOldPropertyClass-${index}`), unit.podOldPropertyClass, 'name');
            initializeDropdown(`unitOccupancy-${index}`, '/3gSurvey/occupancyMasters', unit.podOccupancy);
            initializeDropdown(`unitUsageType-${index}`, `/3gSurvey/getUnitUsageByPropUsageId/${details.podUsageTypeI}`, unit.podUnitUsageType);

            // Attach event listener to update sub usage types when usage types are changed
            document.getElementById(`unitUsageType-${index}`).addEventListener('change', function() {
                updateSubUsageType(index);
            });

            initializeDropdown(`unitSubUsageType-${index}`, '/3gSurvey/getUnitUsageSub', unit.podUnitSubUsageType, true, unit.podUnitUsageType);
        });
    } else {
        document.getElementById('unitsTable').querySelector('tbody').innerHTML = '<tr><td colspan="7">No units available</td></tr>';
    }
} catch (error) {
    showAlertMessage('Record with this old property number does not exists');
    console.error('Error fetching property details:', error);
     document.getElementById('propertyForm').style.display = 'none';
}
}

function initializeDropdown(elementId, apiUrl, selectedValue, isSubtype = false, usageTypeId = null, callback = null) {
const select = document.getElementById(elementId);
if (!select) {
    console.error(`Element not found: ${elementId}`);
    return;
}

let finalUrl = apiUrl;
if (isSubtype && usageTypeId) {
    finalUrl = `${apiUrl}/${usageTypeId}`;
}

fetch(finalUrl)
.then(response => {
    if (!response.ok) throw new Error('Failed to fetch data');
    return response.json();
})
.then(data => {
    let optionsHtml = '<option value="">Select</option>';
    data.forEach(item => {
        // Make sure to check for null or undefined before calling toString
        const isSelected = selectedValue !== null && selectedValue !== undefined && item.value.toString() === selectedValue.toString() ? ' selected' : '';
        optionsHtml += `<option value="${item.value}"${isSelected}>${item.name}</option>`;
    });
    select.innerHTML = optionsHtml;
    if (callback) callback();
})
.catch(error => {
    console.error(`Error loading data for '${elementId}':`, error);
    select.innerHTML = '<option value="">Error loading data</option>';
});
}

async function fetchAllTypesOfAPI(apiUrl, selectElement, selectedIdentifier, identifierType = 'value') {
    try {
        const response = await fetch(apiUrl);
        if (!response.ok) throw new Error(`Failed to fetch data from ${apiUrl}`);
        const items = await response.json();

        // Clear existing options in the select element
        selectElement.innerHTML = '';

        // Populate the select element and set the selected option based on either value or name
        items.forEach(item => {
            let option = new Option(item.name);
            if ((identifierType === 'name' && item.name === selectedIdentifier)) {
                option.selected = true;
            }
            selectElement.appendChild(option);
        });
    } catch (error) {
        console.error(`Error fetching data from ${apiUrl}:`, error);
        selectElement.innerHTML = '<option value="">Failed to load data</option>';
    }
}

// Function to update the unit sub usage types based on the unit usage type selected
function updateSubUsageType(unitIndex) {
    const usageTypeSelect = document.getElementById(`unitUsageType-${unitIndex}`);
    const usageTypeId = usageTypeSelect.value;
    const subUsageTypeSelect = document.getElementById(`unitSubUsageType-${unitIndex}`);
    initializeDropdown(`unitSubUsageType-${unitIndex}`, '/3gSurvey/getUnitUsageSub', null, true, usageTypeId);
}

// Function to update unit usage types based on property usage type selected
function updateUnitUsageTypes(propertyUsageTypeId) {
    const unitUsageTypeSelects = document.querySelectorAll("[id^='unitUsageType-']");
    unitUsageTypeSelects.forEach((select, index) => {
        initializeDropdown(select.id, '/3gSurvey/getUnitUsageByPropertyUsage', null, true, propertyUsageTypeId, () => {
            // Reset sub usage type when main usage type is updated
            updateSubUsageType(index);
        });
    });
}

function updateTotalAssessmentArea() {
    const rows = document.querySelectorAll('#unitsTable tbody tr');
    let totalAssessmentArea = 0;
    rows.forEach(row => {
        const assessmentAreaInput = row.querySelector('input[name="podAssessmentArea"]');
        const assessmentArea = parseFloat(assessmentAreaInput.value) || 0;
        totalAssessmentArea += assessmentArea;
    });
    document.getElementById('podTotalAssessmentArea').value = totalAssessmentArea.toFixed(2); // Update the total field, formatted to 2 decimal places
}


document.addEventListener('DOMContentLoaded', () => {
    if (details && details.propertyTypeId) {
        initializeDropdown('podPropertyTypeI', '/3gSurvey/propertyTypes', details.propertyTypeId, false, null, () => {
            if (details.propertySubTypeId) {
                initializeDropdown('podPropertySubTypeI', '/3gSurvey/propertySubtypes', details.propertySubTypeId, true, details.propertyTypeId, () => {
                    if (details.usageTypeId) {
                        initializeDropdown('podUsageTypeI', '/3gSurvey/usageTypes', details.usageTypeId, true, details.propertySubTypeId);
                    }
                });
            }
        });
    }
});

function collectUnitDetails() {
    const unitDetails = [];
    const rows = document.querySelectorAll('#unitsTable tbody tr');
    rows.forEach(row => {
        const selects = row.querySelectorAll('select');
        const inputs = row.querySelectorAll('input[type="text"]'); // Select only text input elements
        const hiddenInput = row.querySelector('input[type="hidden"]'); // Select the hidden input for unit ID

        const unitDetail = {
            podOccupancy: selects[0] ? selects[0].value : '',                  // Occupancy (select)
            podFloorNumber: selects[1] ? selects[1].value : '',               // Floor Number (select)
            podOldPropertyClass: selects[2] ? selects[2].value : '',          // Old Property Class (select)
            podConstructionYear: inputs[0] ? inputs[0].value : '',            // Construction Year (text)
            podUnitUsageType: selects[3] ? selects[3].value : '',             // Usage Type (select)
            podUnitSubUsageType: selects[4] ? selects[4].value : '',          // Sub Usage Type (select)
            podAssessmentArea: inputs[1] ? inputs[1].value : '',              // Assessment Area (text)
            id: hiddenInput ? hiddenInput.value : ''                          // ID (hidden)
        };

        unitDetails.push(unitDetail);
    });
    return unitDetails;
}

function submitForm() {
event.preventDefault();
const elements = document.getElementById('propertyForm').elements;
for (let i = 0; i < elements.length; i++) {
    elements[i].disabled = false;
}

const formData = {};
for (let i = 0; i < elements.length; i++) {
    const item = elements[i];
    formData[item.name] = item.value;
}

formData.unitDetails = collectUnitDetails(); // Ensure this collects data in the format expected by the backend
formData.updateFlag = 1;

const jsonData = JSON.stringify(formData);

fetch('/3gSurvey/updateOldProperty', {
    method: 'PUT', // Use PUT for updates
    headers: {
        'Content-Type': 'application/json',
    },
    body: jsonData,
})
.then(response => {
        if (response.ok) {
            alert("Form Updated successfully!");
            window.location.href = "/3gSurvey/getOldPropertyDetails"; // Redirect on success
        } else {
            throw new Error('Network response was not ok.');
        }
    })
.catch(error => {
    console.error('There has been a problem with your fetch operation:', error);
});
}


function toggleEdit() {
// Select all input and select elements except those explicitly kept disabled
const formElements = document.querySelectorAll(
    '#propertyForm input:not([type="submit"]):not([type="button"]), #propertyForm select, #unitsTable input'
);

// Determine if any non-permanently-disabled elements are disabled
const isAnyDisabled = Array.from(formElements).some(element => element.disabled);

// Toggle the disabled property for elements not permanently disabled
formElements.forEach(element => {
    element.disabled = !isAnyDisabled;
});

// Update the button's text and toggle the submit button's visibility
const editButton = document.querySelector('.edit-btn');
const submitButton = document.querySelector('#propertyForm input[type="submit"]');

if (isAnyDisabled) {
    editButton.value = 'Cancel Edit';
    submitButton.style.display = 'block'; // Show the submit button when editing
} else {
    editButton.value = 'Edit';
    submitButton.style.display = 'none'; // Hide the submit button when not editing
}
}

function deleteProperty() {
const oldPropertyNo = document.getElementById('podOldPropNoVc').value;
const wardNo = document.getElementById('podWardI').value;

if (!oldPropertyNo || !wardNo) {
    showAlertMessage('Old Property Number and Ward Number are required.');
    return;
}

if (!confirm('Are you sure you want to delete this property? This action cannot be undone.')) {
    return; // Stop if the user cancels.
}

const url = `/3gSurvey/delete/${oldPropertyNo}/${wardNo}`;

fetch(url, {
    method: 'DELETE'
})
.then(response => {
    if (response.ok) {
        showAlertMessage('Property deleted successfully.');
        // Optionally, redirect to another page or refresh the list
        window.location.href = "/3gSurvey/getOldPropertyDetails";
    } else {
        response.text().then(text => { throw new Error(text); });
    }
})
.catch(error => {
    console.error('Error during deletion:', error);
    showAlertMessage('Failed to delete property: ' + error.message);
});
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

async function initializeTypesAndCaptureSelection(propertyTypeId, subtypeSelectId, apiUrl, initialPropertyTypeId) {
const propertyTypeSelectElement = document.getElementById(propertyTypeId);
const subtypeSelectElement = document.getElementById(subtypeSelectId);

console.log(propertyTypeSelectElement + " this is id capture");

// Function to fetch and populate subtypes
async function fetchAndPopulateSubtypes(propertyTypeId, selectedId) {
    subtypeSelectElement.innerHTML = '<option value="">Select</option>';
    if (!propertyTypeId) return;

    try {
        const response = await fetch(`${apiUrl}/${propertyTypeId}`);
        if (!response.ok) throw new Error('Failed to fetch data');
        const propertySubtypes = await response.json();

        let optionsHtml = '<option value="">Select</option>';
        propertySubtypes.forEach(subtype => {
            const isSelected = subtype.value === selectedId ? ' selected' : '';
            optionsHtml += `<option value="${subtype.value}"${isSelected}>${subtype.name}</option>`;
        });
        subtypeSelectElement.innerHTML = optionsHtml;
    } catch (error) {
        console.error(`Error fetching subtypes for ${subtypeSelectId}:`, error);
    }
}
// Populate subtypes immediately if initialPropertyTypeId is provided
if (initialPropertyTypeId) {
    fetchAndPopulateSubtypes(initialPropertyTypeId);
}

// Set up listener for changes
propertyTypeSelectElement.addEventListener('change', function() {
    fetchAndPopulateSubtypes(this.value);
});
}

//code to try
//code to
function showAlertMessage(message) {
    var alertPlaceholder = document.getElementById('alertPlaceholder');
    if (message === '') {
        alertPlaceholder.style.display = 'none'; // Hide the alert message
    } else {
        var messageElement = document.createElement('p');
        messageElement.style.color = 'red'; // Set the color to red
        messageElement.innerText = message; // Set the alert message text
        alertPlaceholder.innerHTML = '';
        alertPlaceholder.appendChild(messageElement);
        alertPlaceholder.style.display = 'block';
    }
}