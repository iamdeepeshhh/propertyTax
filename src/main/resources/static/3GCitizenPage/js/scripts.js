document.addEventListener('DOMContentLoaded', function() {
    // Function to fetch council details
    fetch('/3g/getCouncilDetails')
        .then(response => response.json())
        .then(data => {

            // Check if data is an array and has at least one element
            if (Array.isArray(data) && data.length > 0 && data[0].standardName) {
                // Set the council name in the navbar if available
                document.getElementById('councilName').textContent = data[0].standardName;
            } else {
                console.error('Council details not found or response is invalid.');
                // Set a default name if there's an issue with fetching
                document.getElementById('councilName').textContent = '3G Associates';
            }
        })
        .catch(error => {
            console.error('Error fetching council details:', error);
            // Set a default name in case of an error
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

// Listen for changes to the select element, log the selected option's ID and name, and save them to localStorage.
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
                    console.log(data);
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
            <button class="btn btn-primary mr-2" onclick="viewObjectionSection('${item.pdNewpropertynoVc}')">Register Objection</button>

        `;
    } else if (actionType === 'upload') {
        return `
            <button class="btn btn-primary mr-2" onclick="viewSurveyReport('${item.pdNewpropertynoVc}')">Survey Report</button>
            <button class="btn btn-success" onclick="uploadFile('${item.pdNewpropertynoVc}', 'cad')">Upload Cad</button>
            <button class="btn btn-success" onclick="uploadFile('${item.pdNewpropertynoVc}', 'propertyImage')">Upload Prop Image</button>
        `;
    }
    return '';
}

// performSearch to get the searching parameters from the sections such as surveyreports and uploadfiles
function performSearch(sectionId) {
    const finalProperty = document.querySelector(`#${sectionId} #finalPropertyNo`).value.trim();
    const wardNumber = document.querySelector(`#${sectionId} #wardNumberInput`).value.trim();

    const searchParams = {};
    if (finalProperty) searchParams.finalPropertyNo = finalProperty;
    if (wardNumber) searchParams.wardNo = wardNumber;

    const tableBodyId = (sectionId === 'objections') ? 'surveyReports' : 'searchResultsUpload';
    const actionType = (sectionId === 'objections') ? 'survey' : 'upload';
    fetchPropertyRecords('/3g/searchByFinalPropertyNoAndWardNo', searchParams, tableBodyId, actionType, [  'pdFinalpropnoVc', 'pdOwnernameVc','pdPropertyaddressVc','pdOldpropnoVc', 'pdZoneI', 'pdSurypropnoVc']);
}

//Search Function
document.getElementById('searchButtonSurvey').addEventListener('click', function() {
    const finalProperty = document.getElementById('finalPropertyNo').value.trim();
    const wardNumber = document.getElementById('wardNumberInput').value.trim();

    if (!finalProperty || !wardNumber) {
        alert('No Records Can Be Found.');
        return;
    }
    performSearch('objections');
    // Proceed with search logic here
    console.log('Search triggered with:', finalProperty, wardNumber);
});

function viewObjectionSection(pdNewpropertynoVc) {
    // Show the objection section
    document.getElementById("objections").style.display = "none";
    document.getElementById("objectionSection").style.display = "block";
    const apiUrl = '/3gSurvey/detailsComplete/' + pdNewpropertynoVc;
    fetch(apiUrl)
    .then(response => response.json())
    .then(data => {
    document.getElementById('objectionWardNo').value = data.propertyDetails.pdWardI;
    document.getElementById('objectionFinalPropertyNo').value = data.propertyDetails.pdFinalpropnoVc;
    document.getElementById('objectionOwnerName').value = data.propertyDetails.pdOwnernameVc;})
    .catch(err => console.error(err));

}

function cancelObjection() {
    document.getElementById("objectionSection").style.display = "none";
    document.getElementById("objections").style.display = "block";
//    document.getElementById("objectionForm").reset(); // Optional reset
}

function submitObjection(){
      window.location.replace('/objectionReciept');
 }