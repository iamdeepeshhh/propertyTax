$(document).ready(function () {
    // Function to get wardNo from URL path (e.g., /batchAssessmentReport/1)
    function getWardNoFromURL() {
        const pathParts = window.location.pathname.split('/');  // Split the path by '/'
        return pathParts[pathParts.length - 1];  // Get the last part which is the wardNo
    }

    const wardNo = getWardNoFromURL();
    console.log("Extracted wardNo from URL path:", wardNo);  // Debugging log

    if (!wardNo) {
        console.error("Ward number not found in URL.");
        alert("Invalid Ward Number.");
        return;  // Stop execution if wardNo is not found
    }

    // Show loading indicator (optional)
    $('#loading-indicator').show();

    // Fetch the property report data via AJAX
    $.ajax({
        url: '/3g/property-report',  // Adjust the URL to match your backend route
        type: 'GET',
        data: {
            wardNo: wardNo  // Use the extracted wardNo from the URL path
        },
        success: function (response) {
            console.log("Data received from server:", response);  // Debugging log
            $('#loading-indicator').hide();  // Hide loading indicator

            if (response && response.length > 0) {
                const groupedData = groupByPropertyNumber(response);  // Group records by property number
                populateTable(groupedData);  // Populate the table
            } else {
                alert("No data available for the selected Ward.");
            }
        },
        error: function (error) {
            console.error("Error fetching the report data:", error);
            $('#loading-indicator').hide();  // Hide loading indicator on error
            alert("Failed to fetch data. Please try again later.");
        }
    });

    // Function to group the data by property number
    function groupByPropertyNumber(data) {
        const grouped = {};

        data.forEach(item => {
            const propertyNumber = item.pdFinalpropnoVc;

            if (!grouped[propertyNumber]) {
                grouped[propertyNumber] = {
                    ...item,
                    unitDetails: [...item.unitDetails]
                };
            } else {
                // Combine unit details for the same property number
                grouped[propertyNumber].unitDetails.push(...item.unitDetails);
            }
        });

        return Object.values(grouped);  // Return the grouped data as an array
    }

    // Function to populate the table with the grouped data
      function populateTable(groupedData) {
            const tbody = $('tbody');  // Select the table body
            tbody.empty();  // Clear any existing rows

            // Iterate through the grouped data and create table rows
            groupedData.forEach((item, index) => {
                // Initialize variables for the combined unit details
                let combinedFloor = [];
                let combinedRent = [];
                let combinedUsage = [];
                let combinedConstruction = [];
                let combinedAgeFactor = [];
                let combinedCarpetArea = [];
                let combinedTaxableArea = [];
                let combinedRatePerSqM = [];

                // Combine each unit's details into respective columns
                item.unitDetails.forEach(unit => {
                    combinedFloor.push(unit.floorNoVc || 'N/A');
                    combinedRent.push(unit.actualAnnualRentFl || 'N/A');
                    combinedUsage.push(unit.usageTypeVc || 'N/A');
                    combinedConstruction.push(unit.constructionTypeVc || 'N/A');
                    combinedAgeFactor.push(unit.ageFactorVc || 'N/A');
                    combinedCarpetArea.push(unit.carpetAreaFl || 'N/A');
                    combinedTaxableArea.push(unit.taxableAreaFl || 'N/A');
                    combinedRatePerSqM.push(unit.ratePerSqMFl || 'N/A');
                });

                // Function to join array elements with continuous borders
                const joinContinuous = (arr) => {
                    return `<div style="display: flex; flex-direction: column; width: 100%; height: 100%;">
                        ${arr.map((item, i) =>
                            `<div style="border-top: ${i !== 0 ? '1px solid #000' : 'none'}; padding: 5px;">${item}</div>`
                        ).join('')}
                    </div>`;
                };

                // Create row for the main property details with increased font size
                const propertyRow = `
                    <tr style="font-size: 16px;">
                        <td>${index + 1}</td>
                        <td>
                            <div style="display: flex; flex-direction: column; width: 100%;">
                                <div style="padding: 5px;">${item.pdWardI || 'N/A'}</div>
                                <div style="padding: 5px; border-top: 1px solid #000; font-size: 12px; color: gray;">${item.pdOldWardI || '-'}</div>
                            </div>
                        </td>
                        <td>
                            <div style="display: flex; flex-direction: column; width: 100%;">
                                <div style="padding: 5px;">${item.pdZoneI || 'N/A'}</div>
                                <div style="padding: 5px; border-top: 1px solid #000; font-size: 12px; color: gray;">${item.oldZoneNoVc || '-'}</div>
                            </div>
                        </td>
                        <td>
                            <div style="display: flex; flex-direction: column; width: 100%;">
                                <div style="padding: 5px;">${item.pdFinalpropnoVc || 'N/A'}</div>
                                <div style="padding: 5px; border-top: 1px solid #000; font-size: 12px; color: gray;">${item.pdOldpropnoVc || '-'}</div>
                            </div>
                        </td>
                        <td>${item.pdOwnernameVc || 'N/A'}</td>
                        <td>${item.pdOccupinameF || 'N/A'}</td>

                        <!-- These columns will contain the combined details of all units with continuous borders -->
                        <td>${joinContinuous(combinedFloor)}</td>
                        <td>${joinContinuous(combinedRent)}</td>
                        <td>${joinContinuous(combinedUsage)}</td>
                        <td>${joinContinuous(combinedConstruction)}</td>
                        <td>${joinContinuous(combinedAgeFactor)}</td>
                        <td>${joinContinuous(combinedCarpetArea)}</td>
                        <td>${joinContinuous(combinedTaxableArea)}</td>
                        <td>${joinContinuous(combinedRatePerSqM)}</td>

                        <!-- Rest of the fields for the property -->
                        <td>${item.proposedRatableValues ? item.proposedRatableValues.aggregateFl : 'N'}</td>
                        <td>${item.proposedRatableValues ? item.proposedRatableValues.residentialFl : 'N'}</td>
                        <td>${item.proposedRatableValues ? item.proposedRatableValues.commercialFl : 'N'}</td>
                        <td>${item.consolidatedTaxes ? item.consolidatedTaxes.totalTaxFl : 'N/A'}</td>
                        <td>'N/A'</td>
                        <td>'10% maintainence'</td>
                        <td></td>
                        <td>N/A</td>
                        <td>N/A</td>
                        <td>N/A</td>
                        <td>${item.consolidatedTaxes.totalTaxFl ? item.consolidatedTaxes.totalTaxFl : 'N/A'}</td>
                        <td>${item.oldrvFl ? item.oldrvFl : 'N/A'}</td>
                        <td>${item.pdOldTaxVc ? item.pdOldTaxVc : 'N/A'}</td>
                        <td>N/A</td>
                    </tr>
                `;

                tbody.append(propertyRow);  // Append the row to the table body
            });
        }
});
