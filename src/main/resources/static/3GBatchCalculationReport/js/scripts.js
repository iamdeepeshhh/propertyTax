$(document).ready(function () {
 $.ajax({
        url: '/3g/getCouncilDetails',
        type: 'GET',
        success: function(data) {
            if (data && data.length > 0) {
                const councilDetails = data[0];
                $('#councilName').text(councilDetails.localName + ' / ' + councilDetails.standardName || 'नगर परिषद');
                $('#councilName1').text(councilDetails.localName + ' / ' + councilDetails.standardName || 'नगर परिषद');
                if (councilDetails.imageBase64) {
                    $('#councilLogo').attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
                    $('#councilLogo1').attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
                } else {
                    $('#councilLogo').attr('src', 'https://example.com/fallback-image.png');
                    $('#councilLogo1').attr('src', 'https://example.com/fallback-image.png');
                }
            } else {
                $('#councilLogo').attr('src', 'https://example.com/no-data-image.png');
            }
        },
        error: function(xhr, status, error) {
            console.error('Error fetching council name:', error);
            $('#councilName').text('-');
        }
    });
    // Fetch the report data from the backend
    var wardNo = window.location.pathname.split('/').pop();
    var apiUrl = '/3g/propertyCalculationSheetReport?wardNo=' + wardNo;

    fetch(apiUrl) // Replace with dynamic wardNo if necessary
        .then(response => response.json())
            .then(data => {
                const groupedProperties = groupByPropertyNumber(data);
                groupedProperties.forEach((property, index) => {
                let clonedDiv = $('#maindiv').clone().removeAttr('id').attr('id', 'maindiv-' + index).show();
                populateReportData(clonedDiv, property);
                $('body').append(clonedDiv);
            });
            $('#maindiv').hide(); // Hide the original template
        })
        .catch(error => console.error('Error fetching property report:', error));
    });

    function populateReportData(div, property) {
    
        console.log(property.proposedRatableValues.residentialFl+"rv1");
        // Set the text values for the fields within the cloned div using property data
        div.find('#financialYear').text(property.financialYear || '-');
        div.find('#assessmentDate').text(property.currentAssessmentDateDt || '-');
        div.find('#assessmentTime').text(property.assessmentTime || '-');
        div.find('#pageNo').text(property.pageNo || '-');
        div.find('#pdFinalpropnoVc').text(property.pdFinalpropnoVc || '-');
        div.find('#pdSurypropnoVc').text(property.pdSurypropnoVc || '-');
        div.find('#pdIndexnoVc').text(property.pdIndexnoVc || '-');

        div.find('#pdWardI').text(property.pdWardI || '-');
        div.find('#pdZoneI').text(property.pdZoneI || '-');
        div.find('#pdOldpropnoVc').text(property.pdOldpropnoVc || '-');
        div.find('#pdOwnernameVc').text(property.pdOwnernameVc || '-');
        div.find('#pdOccupinameF').text(property.pdOccupinameF || '-');
        div.find('#pdPropertyaddressVc').text(property.pdPropertyaddressVc || '-');
        div.find('#pdPropertynameVc').text(property.pdPropertynameVc || '-');
        div.find('#pdLayoutnameVc').text(property.pdLayoutnameVc || '-');
        div.find('#pdKhasranoVc').text(property.pdKhasranoVc || '-');
        div.find('#pdPlotnoVc').text(property.pdPlotnoVc || '-');
        div.find('#pdGrididVc').text(property.pdGrididVc || '-');
        div.find('#pdParcelidVc').text(property.pdParcelidVc || '-');
        div.find('#pdRoadidVc').text(property.pdRoadidVc || '-');
        div.find('#pdToiletsI').text(property.pdToiletsI || '-');
        div.find('#pdSeweragesVc').text(property.pdSeweragesVc || '-');
        div.find('#pdSeweragesType').text(property.pdSeweragesType || '-');
        div.find('#pdWaterconntypeVc').text(property.pdWaterconntypeVc || '-');
        div.find('#pdMcconnectionVc').text(property.pdMcconnectionVc || '-');
        div.find('#pdMeternoVc').text(property.pdMeternoVc || '-');
        div.find('#pdConsumernoVc').text(property.pdConsumernoVc || '-');
        div.find('#pdConnectiondateDt').text(property.pdConnectiondateDt || '-');
        div.find('#pdPipesizeF').text(property.pdPipesizeF || '-');
        div.find('#pdPermissionstatusVc').text(property.pdPermissionstatusVc || '-');
        div.find('#pdPermissionnoVc').text(property.pdPermissionnoVc || '-');
        div.find('#pdPermissiondateDt').text(property.pdPermissiondateDt || '-');
        div.find('#pdRainwaterhaverstVc').text(property.pdRainwaterhaverstVc || '-');
        div.find('#pdSolarunitVc').text(property.pdSolarunitVc || '-');
        div.find('#pdStairVc').text(property.pdStairVc || '-');
        div.find('#pdLiftVc').text(property.pdLiftVc || '-');
        div.find('#pdContactnoVc').text(property.pdContactnoVc || '-');
        div.find('#oldWardNoVc').text(property.oldWardNoVc || '-');
        div.find('#oldZoneNoVc').text(property.oldZoneNoVc || '-');
        div.find('#oldPropertyTypeVc').text(property.oldPropertyTypeVc || '-');
        div.find('#oldPropertySubTypeVc').text(property.oldPropertySubTypeVc || '-');
        div.find('#oldUsageTypeVc').text(property.oldUsageTypeVc || '-');
        div.find('#oldConstructionTypeVc').text(property.oldConstructionTypeVc || '-');
        div.find('#pdCategoryI').text(property.pdCategoryI || '-');
        div.find('#oldAssessmentAreaFl').text(property.oldAssessmentAreaFl || '-');
        div.find('#previousAssessmentDateDt').text(property.previousAssessmentDateDt || '-');
        div.find('#currentAssessmentDateDt').text(property.currentAssessmentDateDt || '-');

        // Populate other relevant property details
        div.find('#pdPropertytypeI').text(property.pdPropertytypeI || '-');
        div.find('#pdPropertysubtypeI').text(property.pdPropertysubtypeI || '-');
        div.find('#pdUsagetypeI').text(property.pdUsagetypeI || '-');
        div.find('#pdUsagesubtypeI').text(property.pdUsagesubtypeI || '-');
        div.find('#pdBuildingtypeI').text(property.pdBuildingtypeI || '-');
        div.find('#pdBuildingsubtypeI').text(property.pdBuildingsubtypeI || '-');
        div.find('#pdStatusbuildingVc').text(property.pdStatusbuildingVc || '-');
        div.find('#pdOwnertypeVc').text(property.pdOwnertypeVc || '-');
        div.find('#pdCategoryI').text(property.pdCategoryI || '-');
        div.find('#pdConstAgeI').text(property.pdConstAgeI || '-');
        div.find('#pdPlotareaF').text(property.pdPlotareaF || '-');
        div.find('#pdTotbuiltupareaF').text(property.pdTotbuiltupareaF || '-');
        div.find('#pdTotcarpetareaF').text(property.pdTotcarpetareaF || '-');
        div.find('#pdTotalexempareaF').text(property.pdTotalexempareaF || '-');
        div.find('#pdAssesareaF').text(property.pdAssesareaF || '-');
        div.find('#pdArealetoutF').text(property.pdArealetoutF || '-');
        div.find('#pdAreanotletoutF').text(property.pdAreanotletoutF || '-');
        div.find('#pdNoofroomsI').text(property.pdNoofroomsI || '-');
        div.find('#pdNooffloorsI').text(property.pdNooffloorsI || '-');
        div.find('#calculatedAnnualRentalValueFl').text(property.calculatedAnnualRentalValueFl || '-');
        div.find('#calculatedAnnualUnRentalValueFl').text(property.calculatedAnnualUnRentalValueFl || '-');
        div.find('#calculatedTaxableValueToConsiderFl').text(property.calculatedTaxableValueToConsiderFl || '-');
        div.find('#calculatedMaintenanceRepairAmountFl').text(property.calculatedMaintenanceRepairAmountFl || '-');
        div.find('#calculatedTaxableValueRented').text(property.calculatedTaxableValueRented || '-');
        div.find('#calculatedTaxableValueUnRented').text(property.calculatedTaxableValueUnRented || '-');
        
        
        div.find('#proposedResidentFl').text(property.proposedRatableValues.residentialFl || '0');
        div.find('#proposedCommercialFl').text(property.proposedRatableValues.commercialFl || '0');
        div.find('#proposedReligiousFl').text(property.proposedRatableValues.religiousFl || '0');
        div.find('#proposedOpenPlotResidentialFl').text(property.proposedRatableValues.residentialOpenPlotFl || '0');
        div.find('#proposedOpenPlotCommercialFl').text(property.proposedRatableValues.commercialOpenPlotFl || '0');
        div.find('#proposedOpenPlotReligiousFl').text(property.proposedRatableValues.religiousOpenPlotFl || '0');
        div.find('#proposedOpenPlotEducationFl').text(property.proposedRatableValues.educationAndLegalInstituteOpenPlotFl || '0');
        div.find('#proposedOpenPlotGovernmentFl').text(property.proposedRatableValues.governmentOpenPlotFl || '0');
        div.find('#proposedOpenPlotIndustrialFl').text(property.proposedRatableValues.industrialOpenPlotFl || '0');
        div.find('#proposedEducationFl').text(property.proposedRatableValues.educationalInstituteFl || '0');
        div.find('#proposedGovernmentFl').text(property.proposedRatableValues.governmentFl || '0');
        div.find('#proposedIndustrialFl').text(property.proposedRatableValues.industrialFl || '0');
        div.find('#proposedMobileTowerFl').text(property.proposedRatableValues.mobileTowerFl || '0');
        div.find('#proposedElectricSubstationFl').text(property.proposedRatableValues.electricSubstationFl || '0');
        div.find('#proposedTotalFl').text(property.proposedRatableValues.aggregateFl || '0');
    
        div.find('#propertyTax').text(property.consolidatedTaxes.propertyTaxFl || '0');
        div.find('#educationTaxResidential').text(property.consolidatedTaxes.educationTaxResidFl || '0');
        div.find('#educationTaxNonResidential').text(property.consolidatedTaxes.educationTaxCommFl || '0');
        div.find('#educationTaxTotal').text(property.consolidatedTaxes.educationTaxTotalFl || '0');
        div.find('#egcTax').text(property.consolidatedTaxes.egcFl || '0');
        div.find('#treeTax').text(property.consolidatedTaxes.treeTaxFl || '0');
        div.find('#environmentTax').text(property.consolidatedTaxes.environmentalTaxFl || '0');
        div.find('#cleannessTax').text(property.consolidatedTaxes.cleannessTaxFl || '0');
        div.find('#lightTax').text(property.consolidatedTaxes.lightTaxFl || '0');
        div.find('#fireTax').text(property.consolidatedTaxes.fireTaxFl || '0');
        div.find('#userChargesTax').text(property.consolidatedTaxes.userChargesFl || '0');
        div.find('#totalTax').text(property.consolidatedTaxes.totalTaxFl || '0');
    
        // Populate unit details (only call this once for each property)
        populateUnitDetails(div, property.unitDetails);
    }

    function populateUnitDetails(div, units) {
        // Get the unit details table body within the cloned div
        let unitDetailsTableBody = div.find('#unitDetailsTable tbody');

        // Clear any existing rows (if needed)
        unitDetailsTableBody.empty();

        // Check if units array is present and has data
        if (units && units.length > 0) {
            // Iterate through each unit in the units array
            units.forEach((unit) => {
                // Create a new row for the unit
                let unitRow = `
                    <tr class="t-a-c h-25">
                        <td>${unit.unitNoVc}</td>
                        <td>${unit.floorNoVc}</td>
                        <td>${unit.occupantStatusI === "1" ? "Owner" : "Tenant"}</td>
                        <td>${unit.usageTypeVc}</td>
                        <td>${unit.usageSubTypeVc}</td>
                        <td>${unit.constructionTypeVc}</td>
                        <td>${unit.constructionYearVc}</td>
                        <td>${unit.constructionAgeVc}</td>
                        <td>${unit.ageFactorVc}</td>
                        <td>-</td>
                        <td>${unit.carpetAreaFl}</td>
                        <td>${unit.taxableAreaFl}</td>
                        <td>${unit.exemptedAreaFl}</td>
                        <td>${unit.taxableAreaFl}</td>
                        <td>${unit.ratePerSqMFl}</td>
                        <td>${unit.rentalValAsPerRateFl}</td>
                        <td>${unit.depreciationRateFl}</td>
                        <td>${unit.depreciationAmountFl}</td>
                        <td>${unit.amountAfterDepreciationFl}</td>
                        <td>${unit.maintenanceRepairsFl}</td>
                        <td>${unit.taxableValueByRateFl}</td>
                        <td>${unit.tenantNameVc || ''}</td>
                        <td>${unit.actualMonthlyRentFl || 0}</td>
                        <td>${unit.actualAnnualRentFl || 0}</td>
                        <td>${unit.maintenanceRepairsRentFl || 0}</td>
                        <td>${unit.taxableValueByRentFl || 0}</td>
                        <td>${unit.taxableValueConsideredFl}</td>
                    </tr>
                `;
                // Append the unit row to the table body
                unitDetailsTableBody.append(unitRow);
            });
        } else {
            console.warn('No units available for this property');
        }
    }

    // This will help group properties according to there final property number so that we will get units in one calculationsheet
    function groupByPropertyNumber(properties) {
        const grouped = {};
            properties.forEach(property => {
                const key = property.pdFinalpropnoVc;
                if (!grouped[key]) {
                    grouped[key] = {...property, unitDetails: []};
                }
                // Append unit details to the property entry in the grouped object
                grouped[key].unitDetails.push(...property.unitDetails);
            });
            Object.keys(grouped).forEach(key=> { 
                grouped[key].unitDetails.sort((a, b) => a.unitNoVc - b.unitNoVc);
            });
        return Object.values(grouped); // Convert grouped object into an array
    }