//on page load we use ajax to get the council details on the report's top of the page
//then we are getting the ward number with the function getWardNoFromURL(); this is also called on start
//we are having another ajax to fetch the details with parameters such as wardno
//to sort the data coming from the server we have groupByPropertyNumber which is sorting the data according there finalpropertynumber
const getUnitUsages = new Map();
let globalYearRange = '';
document.addEventListener('DOMContentLoaded', () => {
    fetch('/3g/getAllUnitUsageTypes', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        data.forEach(item => {
            getUnitUsages.set(item.id, item.localName);
        });
    })
    .catch(error => console.error('Error fetching unit usage types:', error));

    fetch('/3g/getAllAssessmentDates', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {

        const currentAssessmentDate = data[0].currentassessmentdate;
        globalYearRange = formatYearRange(currentAssessmentDate);
        $('.yearRange').text(globalYearRange);
    })
    .catch(error => console.error('Error fetching additional data:', error));
});

function formatYearRange(date) {
    const year = parseInt(date.split('-')[0]);  // Extract the year from the date
    const startYear = year;
    const endYear = year + 3;
    const startYearL = startYear + 1;
    const endYearL = endYear + 1;
    const startYearDev = convertToDevanagari(startYear.toString());
    const endYearDev = convertToDevanagari(endYear.toString());
    
    return `${startYearDev}-${convertToDevanagari(startYearL.toString())} ते ${endYearDev}-${convertToDevanagari(endYearL.toString())}`;
}

function convertToDevanagari(numberString) {
    const devanagariDigits = ['०', '१', '२', '३', '४', '५', '६', '७', '८', '९'];
    return numberString.replace(/[0-9]/g, (digit) => devanagariDigits[digit]);
}
let councilDetails = {};
$(document).ready(function () {
 $.ajax({
   url: '/3g/getCouncilDetails',
   type: 'GET',
   success: function(data) {
     if (data && data.length > 0) {
       councilDetails = data[0]; // ✅ Save council data globally

       // This part below updates the main page (optional)
       $('.councilName').text(councilDetails.localName + ' / ' + councilDetails.standardName);
       if (councilDetails.imageBase64) {
           $('.councilLogo').attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
       }
       $('.standardSiteNameVC').text(councilDetails.standardSiteNameVC);
       $('.localSiteNameVC').text(councilDetails.localSiteNameVC);
       $('.standardDistrictNameVC').text(councilDetails.standardDistrictNameVC);
       $('.localDistrictNameVC').text(councilDetails.localDistrictNameVC);

     }
   }
 });
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
        url: '/3g/propertyBatchReport',  // Adjust the URL to match your backend route
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
        Object.keys(grouped).forEach(key => {
            grouped[key].unitDetails.sort((a, b) => a.unitNoVc - b.unitNoVc);
        });
        
        // Convert map to array and sort by final property number (natural order)
        const arr = Object.values(grouped);
        arr.sort((a, b) => {
            const A = (a.pdFinalpropnoVc || '').toString();
            const B = (b.pdFinalpropnoVc || '').toString();
            return A.localeCompare(B, undefined, { numeric: true, sensitivity: 'base' });
        });
        return arr;
    }

    // Function to populate the table with the grouped data
    function populateTable(groupedData) {
        const batchSize = 10;
        const reportRoot = $('#report-root');
        reportRoot.empty();

        let totalTaxableValueByRate = 0;
        let totalMaintenanceRepairs = 0;
        let totalTaxableValueConsidered = 0;
        let totalAggregateFl = 0;
        let totalResidentialFl = 0;
        let totalNonResidentialFl = 0;
        let totalTaxFl = 0;

         for (let i = 0; i < groupedData.length; i += batchSize) {
                const batch = groupedData.slice(i, i + batchSize);
                const template = document.getElementById('report-chunk-template');
                const chunk = template.content.cloneNode(true);
                reportRoot.append(chunk);

                const currentChunk = reportRoot.children().last(); // Get this added chunk

                 // ✅ Now update council name/logo inside this chunk
                $(currentChunk).find('.councilName').text(councilDetails.localName + ' / ' + councilDetails.standardName);
                if (councilDetails.imageBase64) {
                    $(currentChunk).find('.councilLogo').attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
                }
                $(currentChunk).find('.yearRange').text(globalYearRange);

                // Proceed with tbody filling
                const tbody = $(currentChunk).find('tbody');


        // Iterate through the grouped data and create table rows
         batch.forEach((item, batchIndex) => {
                    let index = i + batchIndex;
                    // Extract and format unit details exactly like your current logic
                    let combinedFloor = [], combinedRent = [], combinedUsage = [], combinedConstruction = [],
                        combinedAgeFactor = [], combinedCarpetArea = [], combinedTaxableArea = [],
                        combinedRatePerSqM = [], combinedRentalValueAsPerRate = [], combinedDepreciationRate = [],
                        combinedDepreciationAmount = [], combinedAmountAfterDepreciation = [],
                        combinedTaxableValueByRate = [], combinedTaxableValueConsidered = [],
                        combinedMaintenanceRepairs = [];

            // Combine each unit's details into respective columns
            item.unitDetails.forEach(unit => {
                combinedFloor.push(!unit.floorNoVc?.trim() ? '-': isNaN(Number(unit.floorNoVc.trim())) ? unit.floorNoVc.trim().charAt(0).toUpperCase(): unit.floorNoVc.trim());
                combinedRent.push(unit.actualAnnualRentFl || '-');
                combinedUsage.push(unit.usageTypeVc || '-');
                combinedConstruction.push(unit.constructionTypeVc || '-');
                combinedAgeFactor.push(unit.ageFactorVc || '-');
                combinedCarpetArea.push(unit.carpetAreaFl || '-');
                combinedTaxableArea.push(unit.taxableAreaFl || '0');
                combinedRatePerSqM.push(unit.ratePerSqMFl || '0');
                combinedRentalValueAsPerRate.push(unit.rentalValAsPerRateFl || '-');
                combinedDepreciationRate.push(unit.depreciationRateFl || '0');
                combinedDepreciationAmount.push(unit.depreciationAmountFl || '0');
                combinedAmountAfterDepreciation.push(unit.amountAfterDepreciationFl || '0');
                combinedTaxableValueByRate.push(unit.taxableValueByRateFl || '0');
                combinedTaxableValueConsidered.push(unit.taxableValueConsideredFl || '0');
                combinedMaintenanceRepairs.push(unit.maintenanceRepairsFl || '0');
                totalTaxableValueByRate += parseFloat(unit.taxableValueByRateFl || 0);
                totalMaintenanceRepairs += parseFloat(unit.maintenanceRepairsFl || 0);
                totalTaxableValueConsidered += parseFloat(unit.taxableValueConsideredFl || 0);
            });

    totalAggregateFl += parseFloat(item.proposedRatableValues.aggregateFl || 0);
    totalResidentialFl += parseFloat(item.proposedRatableValues.residentialFl || 0);
    totalNonResidentialFl += parseFloat(item.proposedRatableValues.nonResidentialFl || 0);
    totalTaxFl += parseFloat(item.consolidatedTaxes.totalTaxFl || 0);

            // Function to join array elements with continuous borders
            const joinContinuous = (arr, isUsageType = false) => {
                return `<div style="display: flex; flex-direction: column; width: 100%; height: 100%;">
                    ${arr.map((item, i) => {
                        const id = Number(item);
                        const description = isUsageType ? getUnitUsages.get(id) || '-' : item;
                        return `<div style="border-top: ${i !== 0 ? '1px solid #000' : 'none'}; padding: 5px;">${description}</div>`;
                    }).join('')}
                </div>`;
            };


            // Create row for the main property details with increased font size
            const propertyRow = `
                <tr style="font-size: 16px;">
                    <td>${index + 1}</td>
                    <td>
                        <div style="display: flex; flex-direction: column; width: 100%;">
                            <div style="padding: 6px;">${item.pdWardI || '-'}</div>
                            <div style="padding: 5px; border-top: 1px solid #000; font-size: 12px; color: gray;">${item.pdOldWardI || '-'}</div>
                        </div>
                    </td>
                    <td>
                        <div style="display: flex; flex-direction: column; width: 100%;">
                            <div style="padding: 5px;">${item.pdZoneI || '-'}</div>
                            <div style="padding: 5px; border-top: 1px solid #000; font-size: 12px; color: gray;">${item.oldZoneNoVc || '-'}</div>
                        </div>
                    </td>
                    <td>
                        <div style="display: flex; flex-direction: column; width: 100%;">
                            <div style="padding: 5px;">${item.pdFinalpropnoVc || '-'}</div>
                            <div style="padding: 5px; border-top: 1px solid #000;">${item.pdOldpropnoVc || '-'}</div>
                            <div style="padding: 5px; border-top: 1px solid #000;">${item.pdSurypropnoVc || '-'}</div>
                        </div>
                    </td>
                    <td>${item.pdOwnernameVc || '-'}</td>
                    <td>${item.pdOccupinameF || '-'}</td>

                    <!-- These columns will contain the combined details of all units with continuous borders -->
                    <td>${joinContinuous(combinedFloor)}</td>
                    <td>${joinContinuous(combinedRent)}</td>
                    <td>${joinContinuous(combinedUsage, true)}</td>
                    <td>${joinContinuous(combinedConstruction)}</td>
                    <td>${joinContinuous(combinedAgeFactor)}</td>
                    <td>${joinContinuous(combinedCarpetArea)}</td>
                    <td>${joinContinuous(combinedTaxableArea)}</td>
                    <td>${joinContinuous(combinedRatePerSqM)}</td>

                    <td>${joinContinuous(combinedRentalValueAsPerRate)}</td>
                    <td>${joinContinuous(combinedDepreciationRate)}</td>
                    <td>${joinContinuous(combinedDepreciationAmount)}</td>
                    <td>${joinContinuous(combinedAmountAfterDepreciation)}</td>
                    <td>${joinContinuous(combinedTaxableValueByRate)}</td>
                    <td>${joinContinuous(combinedMaintenanceRepairs)}</td>
                    <td>${joinContinuous(combinedTaxableValueConsidered)}</td>
                    <td>${item.proposedRatableValues.aggregateFl}</td>
                    <td>${item.proposedRatableValues.residentialFl}</td>
                    <td>${item.proposedRatableValues.nonResidentialFl}</td>
                    <td>${item.consolidatedTaxes.totalTaxFl ? item.consolidatedTaxes.totalTaxFl : '0'}</td>
                    <td>${item.pdOldrvFl ? item.pdOldrvFl : '0'}</td>
                    <td>${item.pdOldTaxVc ? item.pdOldTaxVc : '0'}</td>
                    <td>-</td>
                </tr>
            `;

            tbody.append(propertyRow);  // Append the row to the table body
    });
        console.log("Appending totals row");

    // Append the fully filled chunk to the page
        reportRoot.append(chunk);
    }

        // Append a final row for totals after the forEach loop
       const lastTbody = reportRoot.find('tbody').last();
        const totalsRow = `
            <tr class="totals-row">
                <td>एकूण:</td>
                <td colspan="17"></td>  // Ensure this spans the correct number of columns
                <td>${totalTaxableValueByRate.toFixed(2)}</td>
                <td>${totalMaintenanceRepairs.toFixed(2)}</td>
                <td>${totalTaxableValueConsidered.toFixed(2)}</td>
                <td>${totalAggregateFl.toFixed(2)}</td>
                <td>${totalResidentialFl.toFixed(2)}</td>
                <td>${totalNonResidentialFl.toFixed(2)}</td>
                <td>${totalTaxFl.toFixed(2)}</td>
                <td></td>  // Additional columns if necessary
                <td></td>
                <td></td>
            </tr>
        `;
        lastTbody.append(totalsRow);

        $('#signature-container').show();
    }
});
