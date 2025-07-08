$(document).ready(function() {
    // Retrieve the property number from the URL
    var propertyId = window.location.pathname.split('/').pop();

    if (!propertyId) {
        alert('No property number found.');
        return; // Exit if no property number is found
    }

    // Construct the URL for the API call
    var apiUrl = '/3g/realtimeCalculatedDetails/' + propertyId;

    $.ajax({
        url: apiUrl,
        type: 'GET',
        success: function(data) {
            // Check if data is available
            if (data) {
                console.log(data);

                // Populating table data
                $('#pdZoneI').text(data.pdZoneI || 'N/A');
                $('#pdWardI').text(data.pdWardI || 'N/A');
                $('#pdOldpropnoVc').text(data.pdOldpropnoVc || 'N/A');
                $('#pdSurypropnoVc').text(data.pdSurypropnoVc || 'N/A');
                $('#pdLayoutnameVc').text(data.pdLayoutnameVc || 'N/A');
                $('#pdLayoutnoVc').text(data.pdLayoutnoVc || 'N/A');
                $('#pdKhasranoVc').text(data.pdKhasranoVc || 'N/A');
                $('#pdPlotnoVc').text(data.pdPlotnoVc || 'N/A');
                $('#pdGrididVc').text(data.pdGrididVc || 'N/A');
                $('#pdRoadidVc').text(data.pdRoadidVc || 'N/A');
                $('#pdParcelidVc').text(data.pdParcelidVc || 'N/A');
                $('#pdNewpropertynoVc').text(data.pdNewpropertynoVc || 'N/A');
                $('#pdOwnernameVc').text(data.pdOwnernameVc || 'N/A');
                $('#pdContactnoVc').text(data.pdContactnoVc || 'N/A');
                $('#pdPropertynameVc').text(data.pdPropertynameVc || 'N/A');
                $('#pdPropertyaddressVc').text(data.pdPropertyaddressVc || 'N/A');
                $('#pdPincodeVc').text(data.pdPincodeVc || 'N/A');
                $('#pdCategoryI').text(data.pdCategoryI || 'N/A');
                fetchNameById('/3gSurvey/propertyTypes', data.pdPropertytypeI, function(name) {
                    $('#pdPropertytypeI').text(name);
                });
                fetchNameById('/3gSurvey/propertySubtype',data.pdPropertysubtypeI, function(name) {
                    $('#pdPropertysubtypeI').text(name);
                });
                fetchNameById('/3gSurvey/PropertyUsage', data.pdUsagetypeI, function(name) {
                    $('#pdUsagetypeI').text(name);
                });
                fetchNameById('/3gSurvey/PropertySubUsage', data.pdUsagesubtypeI, function(name) {
                    $('#pdUsagesubtypeI').text(name);
                });
                fetchNameById('/3gSurvey/buildingSubType', data.pdBuildingsubtypeI, function(name) {
                    $('#pdBuildingsubtypeI').text(name);
                });
                fetchNameById('/3gSurvey/buildingType', data.pdBuildingtypeI, function(name) {
                    $('#pdBuildingtypeI').text(name);
                });
                $('#pdConstAgeI').text(data.pdConstAgeI || 'N/A');
                $('#pdConstYearVc').text(data.pdConstYearVc || 'N/A');
                $('#pdPermissionstatusVc').text(data.pdPermissionstatusVc || 'N/A');
                $('#pdPermissionnoVc').text(data.pdPermissionnoVc || 'N/A');
                $('#pdPermissiondateDt').text(data.pdPermissiondateDt || 'N/A');
                $('#pdNooffloorsI').text(data.pdNooffloorsI || 'N/A');
                $('#pdNoofroomsI').text(data.pdNoofroomsI || 'N/A');
                $('#pdStairVc').text(data.pdStairVc || 'N/A');
                $('#pdLiftVc').text(data.pdLiftVc || 'N/A');
                $('#pdRoadwidthF').text(data.pdRoadwidthF || 'N/A');
                $('#pdToiletstatusVc').text(data.pdToiletstatusVc || 'N/A');
                $('#pdToiletsI').text(data.pdToiletsI || 'N/A');
                $('#pdSeweragesVc').text(data.pdSeweragesVc || 'N/A');
                $('#pdSeweragesType').text(data.pdSeweragesType || 'N/A');
                $('#pdWaterconnstatusVc').text(data.pdWaterconnstatusVc || 'N/A');
                $('#pdWaterconntypeVc').text(data.pdWaterconntypeVc || 'N/A');
                $('#pdMcconnectionVc').text(data.pdMcconnectionVc || 'N/A');
                $('#pdMeternoVc').text(data.pdMeternoVc || 'N/A');
                $('#pdConsumernoVc').text(data.pdConsumernoVc || 'N/A');
                $('#pdConnectiondateDt').text(data.pdConnectiondateDt || 'N/A');
                $('#pdConnectiondateDt_vc').text(data.pdConnectiondateDt_vc || 'N/A');
                $('#pdPipesizeF').text(data.pdPipesizeF || 'N/A');
                $('#pdElectricityconnectionVc').text(data.pdElectricityconnectionVc || 'N/A');
                $('#pdElemeternoVc').text(data.pdElemeternoVc || 'N/A');
                $('#pdEleconsumernoVc').text(data.pdEleconsumernoVc || 'N/A');
                $('#pdRainwaterhaverstVc').text(data.pdRainwaterhaverstVc || 'N/A');
                $('#pdSolarunitVc').text(data.pdSolarunitVc || 'N/A');
                $('#pdPlotareaF').text(data.pdPlotareaF || 'N/A');
                $('#pdTotbuiltupareaF').text(data.pdTotbuiltupareaF || 'N/A');
                $('#pdTotcarpetareaF').text(data.pdTotcarpetareaF || 'N/A');
                $('#pdTotalexempareaF').text(data.pdTotalexempareaF || 'N/A');
                $('#pdAssesareaF').text(data.pdAssesareaF || 'N/A');
                $('#pdArealetoutF').text(data.pdArealetoutF || 'N/A');
                $('#pdAreanotletoutF').text(data.pdAreanotletoutF || 'N/A');
                $('#pdCurrassesdateDt').text(data.pdCurrassesdateDt || 'N/A');
                $('#pdOldrvFl').text(data.pdOldrvFl || 'N/A');
                $('#user_id').text(data.user_id || 'N/A');
                $('#pdPolytypeVc').text(data.pdPolytypeVc || 'N/A');
                $('#pdStatusbuildingVc').text(data.pdStatusbuildingVc || 'N/A');
                $('#pdLastassesdateDt').text(data.pdLastassesdateDt || 'N/A');
                $('#pdFirstassesdateDt').text(data.pdFirstassesdateDt || 'N/A');
                $('#pdNoticenoVc').text(data.pdNoticenoVc || 'N/A');
                $('#pdIndexnoVc').text(data.pdIndexnoVc || 'N/A');
                $('#pdNewfinalpropnoVc').text(data.pdNewfinalpropnoVc || 'N/A');
                $('#pdTaxstatusVc').text(data.pdTaxstatusVc || 'N/A');
                $('#pdChangedVc').text(data.pdChangedVc || 'N/A');
                $('#pdNadetailsVc').text(data.pdNadetailsVc || 'N/A');
                $('#pdNanumberI').text(data.pdNanumberI || 'N/A');
                $('#pdNadateDt').text(data.pdNadateDt || 'N/A');
                $('#pdTddetailsVc').text(data.pdTddetailsVc || 'N/A');
                $('#pdTdordernumF').text(data.pdTdordernumF || 'N/A');
                $('#pdTddateDt').text(data.pdTddateDt || 'N/A');
                $('#pdSaledeedI').text(data.pdSaledeedI || 'N/A');
                $('#pdSaledateDt').text(data.pdSaledateDt || 'N/A');
                $('#pdCitysurveynoF').text(data.pdCitysurveynoF || 'N/A');
                $('#pdOwnertypeVc').text(data.pdOwnertypeVc || 'N/A');
                $('#pdBuildingvalueI').text(data.pdBuildingvalueI || 'N/A');
                $('#pdPlotvalueF').text(data.pdPlotvalueF || 'N/A');
                $('#pdTpdateDt').text(data.pdTpdateDt || 'N/A');
                $('#pdTpdetailsVc').text(data.pdTpdetailsVc || 'N/A');
                $('#pdTpordernumF').text(data.pdTpordernumF || 'N/A');
                $('#pdOccupinameF').text(data.pdOccupinameF || 'N/A');
                $('#pdPropimageT').text(data.pdPropimageT || 'N/A');
                $('#pdHouseplanT').text(data.pdHouseplanT || 'N/A');
                $('#pdPropimage2T').text(data.pdPropimage2T || 'N/A');
                $('#pdHouseplan2T').text(data.pdHouseplan2T || 'N/A');
                $('#pdSignT').text(data.pdSignT || 'N/A');
                $('#pdFinalpropnoVc').text(data.pdFinalpropnoVc || 'N/A');
                $('#propRefno').text(data.propRefno || 'N/A');
                $('#pdImgstatusVc').text(data.pdImgstatusVc || 'N/A');
                $('#pdSuryprop2Vc').text(data.pdSuryprop2Vc || 'N/A');
                $('#pdSuryprop1Vc').text(data.pdSuryprop1Vc || 'N/A');
                $('#createdAt').text(data.createdAt || 'N/A');
                $('#createddateVc').text(data.createddateVc || 'N/A');
                $('#updatedAt').text(data.updatedAt || 'N/A');
                $('#id').text(data.id || 'N/A');
                $('#pdApprovedByDesk1Vc').text(data.pdApprovedByDesk1Vc || 'N/A');
                $('#pdApprovedByDesk2Vc').text(data.pdApprovedByDesk2Vc || 'N/A');
                $('#pdApprovedByDesk3Vc').text(data.pdApprovedByDesk3Vc || 'N/A');
                $('#pdCompletedByVc').text(data.pdCompletedByVc || 'N/A');
                $('#pdOldwardNoVc').text(data.pdOldwardNoVc || 'N/A');
                $('#pdOldTaxVc').text(data.pdOldTaxVc || 'N/A');
                $('#oldWardNoVc').text(data.oldWardNoVc || 'N/A');
                $('#oldZoneNoVc').text(data.oldZoneNoVc || 'N/A');
                $('#calculatedAnnualRentalValueFl').text(data.annualRentalValueFl || 'N/A');
                $('#calculatedAnnualUnRentalValueFl').text(data.annualUnRentalValueFl || 'N/A');
                $('#calculatedTaxableValueToConsiderFl').text(data.finalValueToConsiderFl || 'N/A');
                $('#calculatedMaintenanceRepairAmountFl').text(data.maintainenceRepairAmountConsideredFl || 'N/A');
                $('#calculatedTaxableValueRented').text(data.taxableValueRentedFl || 'N/A');
                $('#calculatedTaxableValueUnRented').text(data.taxableValueUnRentedFl || 'N/A');
                fetchNameById('/3gSurvey/propertyTypes', data.oldPropertyTypeVc, function(name) {
                    $('#oldPropertyTypeVc').text(name);
                });
                fetchNameById('/3gSurvey/propertySubtype',data.oldPropertySubTypeVc, function(name) {
                    $('#oldPropertySubTypeVc').text(name);
                });
                fetchNameById('/3gSurvey/PropertyUsage', data.oldUsageTypeVc, function(name) {
                    $('#oldUsageTypeVc').text(name);
                });
                $('#oldConstructionTypeVc').text(data.oldConstructionTypeVc || 'N/A');
                $('#oldAssessmentAreaFl').text(data.oldAssessmentAreaFl || 'N/A');
                $('#previousAssessmentDateDt').text(data.previousAssessmentDateDt || 'N/A');
                $('#currentAssessmentDateDt').text(data.currentAssessmentDateDt || 'N/A');
                $('#propertyTax').text(data.consolidatedTaxes.propertyTaxFl || 'N/A');
                $('#educationTaxResidential').text(data.consolidatedTaxes.educationTaxResidFl || '0');
                $('#educationTaxNonResidential').text(data.consolidatedTaxes.educationTaxCommFl || '0');
                $('#educationTaxTotal').text(data.consolidatedTaxes.educationTaxTotalFl || '0');
                $('#egcTax').text(data.consolidatedTaxes.egcFl || '0');
                $('#treeTax').text(data.consolidatedTaxes.treeTaxFl || '0');
                $('#environmentTax').text(data.consolidatedTaxes.environmentalTaxFl || '0');
                $('#cleannessTax').text(data.consolidatedTaxes.cleannessTaxFl || '0');
                $('#lightTax').text(data.consolidatedTaxes.lightTaxFl || '0');
                $('#fireTax').text(data.consolidatedTaxes.fireTaxFl || '0');
                $('#userChargesTax').text(data.consolidatedTaxes.userFeesFl || '0');
                $('#totalTax').text(data.consolidatedTaxes.totalTaxFl || '0');
                $('#proposedResidentFl').text(data.proposedRatableValues.residentialFl || '0');
                $('#proposedCommercialFl').text(data.proposedRatableValues.commercialFl || '0');
                $('#proposedReligiousFl').text(data.proposedRatableValues.religiousFl || '0');
                $('#proposedEducationFl').text(data.proposedRatableValues.educationalInstituteFl || '0');
                $('#proposedGovernmentFl').text(data.proposedRatableValues.governmentFl || '0');
                $('#proposedOpenPlotResidentialFl').text(data.proposedRatableValues.residentialOpenPlotFl || '0');
                $('#proposedOpenPlotCommercialFl').text(data.proposedRatableValues.commercialOpenPlotFl || '0');
                $('#proposedOpenPlotReligiousFl').text(data.proposedRatableValues.religiousOpenPlotFl || '0');
                $('#proposedOpenPlotEducationFl').text(data.proposedRatableValues.educationAndLegalInstituteOpenPlotFl || '0');
                $('#proposedOpenPlotGovernmentFl').text(data.proposedRatableValues.governmentOpenPlotFl || '0');
                $('#proposedOpenPlotIndustrialFl').text(data.proposedRatableValues.industrialOpenPlotFl || '0');
                $('#proposedIndustrialFl').text(data.proposedRatableValues.industrialFl || '0');
                $('#proposedMobileTowerFl').text(data.proposedRatableValues.mobileTowerFl || '0');
                $('#proposedElectricSubstationFl').text(data.proposedRatableValues.electricSubstationFl || '0');
                $('#proposedTotalFl').text(data.proposedRatableValues.aggregateFl || '0');
                
                $('#proposedResidentFl1').text(data.proposedRatableValues.residentialFl || '0');
                $('#proposedCommercialFl1').text(data.proposedRatableValues.commercialFl || '0');
                $('#proposedReligiousFl1').text(data.proposedRatableValues.religiousFl || '0');
                $('#proposedEducationFl1').text(data.proposedRatableValues.educationalInstituteFl || '0');
                $('#proposedGovernmentFl1').text(data.proposedRatableValues.governmentFl || '0');
                $('#proposedOpenPlotResidentialFl1').text(data.proposedRatableValues.residentialOpenPlotFl || '0');
                $('#proposedOpenPlotCommercialFl1').text(data.proposedRatableValues.commercialOpenPlotFl || '0');
                $('#proposedOpenPlotReligiousFl1').text(data.proposedRatableValues.religiousOpenPlotFl || '0');
                $('#proposedOpenPlotEducationFl1').text(data.proposedRatableValues.educationAndLegalInstituteOpenPlotFl || '0');
                $('#proposedOpenPlotGovernmentFl1').text(data.proposedRatableValues.governmentOpenPlotFl || '0');
                $('#proposedOpenPlotIndustrialFl1').text(data.proposedRatableValues.industrialOpenPlotFl || '0');
                $('#proposedIndustrialFl1').text(data.proposedRatableValues.industrialFl || '0');
                $('#proposedMobileTowerFl1').text(data.proposedRatableValues.mobileTowerFl || '0');
                $('#proposedElectricSubstationFl1').text(data.proposedRatableValues.electricSubstationFl || '0');
                $('#proposedTotalFl1').text(data.proposedRatableValues.aggregateFl || '0');
                populateUnitDetails(data.unitDetails);
                // Handle more fields as needed
            } else {
                alert('No data returned for this property.');
            }
        },
        error: function(xhr, status, error) {
            alert('Error fetching property details: ' + error);
        }
    });
});

function populateUnitDetails(units) {
    var $tbody = $('#unitDetailsTable tbody');
    $tbody.empty();

    units.forEach(function(unit) {
        var unitRow = `
            <tr class="t-a-c h-25">
                <td>${unit.unitNoVc || 'N/A'}</td>
                <td>${unit.floorNoVc || 'N/A'}</td>
                <td id="occupantStatus-${unit.unitNoVc}">${unit.occupantStatusI || 'N/A'}</td>
                <td id="usageType-${unit.unitNoVc}">${unit.usageTypeVc || 'N/A'}</td>
                <td id="usageSubType-${unit.unitNoVc}">${unit.usageSubTypeVc || 'N/A'}</td>
                <td>${unit.constructionTypeVc || 'N/A'}</td>
                <td>${unit.constructionYearVc || 'N/A'}</td>
                <td>${unit.constructionAgeVc || 'N/A'}</td>
                <td>${unit.ageFactorVc || 'N/A'}</td>
                <td>${unit.carpetAreaFl || '0'}</td>
                <td>${unit.plotAreaFl || '0'}</td>
                <td>${unit.exemptedAreaFl || '0'}</td>
                <td>${unit.exemptedAreaFl || '0'}</td>
                <td>${unit.taxableAreaFl || '0'}</td>
                <td>${unit.ratePerSqMFl || '0'}</td>
                <td>${unit.rentalValAsPerRateFl || '0'}</td>
                <td>${unit.depreciationRateFl || '0'}</td>
                <td>${unit.depreciationAmountFl || '0'}</td>
                <td>${unit.amountAfterDepreciationFl || '0'}</td>
                <td>${unit.maintenanceRepairsFl || '0'}</td>
                <td>${unit.taxableValueByRateFl || '0'}</td>
                <td>${unit.tenantNameVc || 'N/A'}</td>
                <td>${unit.actualMonthlyRentFl || '0'}</td>
                <td>${unit.actualAnnualRentFl || '0'}</td>
                <td>${unit.maintenanceRepairsRentFl || '0'}</td>
                <td>${unit.taxableValueByRentFl || '0'}</td>
                <td>${unit.taxableValueConsideredFl || '0'}</td>
            </tr>`;

        $tbody.append(unitRow);
        fetchNameById('/3gSurvey/unitUsageType', unit.usageTypeVc, function(name) {
            $('#usageType-' + unit.unitNoVc).text(name);
        });
        fetchNameById('/3gSurvey/unitSubUsageType', unit.usageSubTypeVc, function(name) {
            $('#usageSubType-' + unit.unitNoVc).text(name);
        });
        fetchNameById('/3gSurvey/occupantStatus', unit.occupantStatusI, function(name) {
            $('#occupantStatus-' + unit.unitNoVc).text(name);
        });
    });
}
function fetchNameById(url, id, callback) {
    $.ajax({
        url: url + '/' + id,
        type: 'GET',
        success: function(response) {
            if (response && response.name) {
                callback(response.name);
            } else {
                callback('N/A');
            }
        },
        error: function(xhr, status, error) {
            console.error('Error fetching name:', error);
            callback('N/A');
        }
    });
    }