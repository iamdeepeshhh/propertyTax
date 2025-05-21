$(document).ready(function() {

$.ajax({
    url: '/3g/getAllAssessmentDates',
    type: 'GET',
    success: function (data) {
        if (data && data.length > 0) {
            const current = data[0].currentassessmentdate || '-';
            const previous = data[0].previousassessmentdate || '-';

            $('.currentAssessmentDateDt').text(formatYearRange(current, false));

            // Optional: Display formatted year range in Devanagari
            $('.yearRangeDev').text(formatYearRange(current, true));
            $('.yearRangeEng').text(formatYearRange(current, false));
            $('.financialYear').text(formatYearRange(current, false));
        }
    },
    error: function () {
        console.error('Error fetching assessment dates');
        $('#currentAssessmentDate, #previousAssessmentDate, #yearRange').text('-');
    }
});

$.ajax({
        url: '/3g/getCouncilDetails',
        type: 'GET',
        success: function(data) {
            if (data && data.length > 0) {
                const councilDetails = data[0];
                console.log(councilDetails);
                $('#councilName').text(councilDetails.localName + ' / ' + councilDetails.standardName || 'नगर परिषद');
                $('#councilName1').text(councilDetails.localName + ' / ' + councilDetails.standardName || 'नगर परिषद');
                $('.siteNameStd').text(councilDetails.standardSiteNameVC);
                $('.siteNameLocal').text(councilDetails.localSiteNameVC);
                $('.districtNameLocal').text(councilDetails.localDistrictNameVC);
                $('.districtNameStd').text(councilDetails.standardDistrictNameVC);
                
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
    // Retrieve the property number from the URL
    var propertyId = window.location.pathname.split('/').pop();

    if (!propertyId) {
        alert('No property number found.');
        return; // Exit if no property number is found
    }

    // Construct the URL for the API call
    var apiUrl = '/3g/calculatedDetails/' + propertyId;

    $.ajax({
        url: apiUrl,
        type: 'GET',
        success: function(data) {
            // Check if data is available
            if (data) {
                data = data[0];
                console.log(data[0]);
                // Populating table data
                $('.pdZoneI').text(data.pdZoneI || '-');
                $('.pdWardI').text(data.pdWardI || '-');
                $('.pdOldpropnoVc').text(data.pdOldpropnoVc || '-');
                $('#pdSurypropnoVc').text(data.pdSurypropnoVc || '-');
                $('#pdLayoutnameVc').text(data.pdLayoutnameVc || '-');
                $('#pdLayoutnoVc').text(data.pdLayoutnoVc || '-');
                $('#pdKhasranoVc').text(data.pdKhasranoVc || '-');
                $('#pdPlotnoVc').text(data.pdPlotnoVc || '-');
                $('#pdGrididVc').text(data.pdGrididVc || '-');
                $('#pdRoadidVc').text(data.pdRoadidVc || '-');
                $('#pdParcelidVc').text(data.pdParcelidVc || '-');
                $('#pdNewpropertynoVc').text(data.pdNewpropertynoVc || '-');
                $('#pdOwnernameVc').text(data.pdOwnernameVc || '-');
                $('#pdContactnoVc').text(data.pdContactnoVc || '-');
                $('#pdPropertynameVc').text(data.pdPropertynameVc || '-');
                $('#pdPropertyaddressVc').text(data.pdPropertyaddressVc || '-');
                $('#pdPincodeVc').text(data.pdPincodeVc || '-');
                $('#pdCategoryI').text(data.pdCategoryI || '-');
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
                $('#pdConstAgeI').text(data.pdConstAgeI || '-');
                $('#pdConstYearVc').text(data.pdConstYearVc || '-');
                $('#pdPermissionstatusVc').text(data.pdPermissionstatusVc || '-');
                $('#pdPermissionnoVc').text(data.pdPermissionnoVc || '-');
                $('#pdPermissiondateDt').text(data.pdPermissiondateDt || '-');
                $('#pdNooffloorsI').text(data.pdNooffloorsI || '-');
                $('#pdNoofroomsI').text(data.pdNoofroomsI || '-');
                $('#pdStairVc').text(data.pdStairVc || '-');
                $('#pdLiftVc').text(data.pdLiftVc || '-');
                $('#pdRoadwidthF').text(data.pdRoadwidthF || '-');
                $('#pdToiletstatusVc').text(data.pdToiletstatusVc || '-');
                $('#pdToiletsI').text(data.pdToiletsI || '-');
                $('#pdSeweragesVc').text(data.pdSeweragesVc || '-');
                $('#pdSeweragesType').text(data.pdSeweragesType || '-');
                $('#pdWaterconnstatusVc').text(data.pdWaterconnstatusVc || '-');
                $('#pdWaterconntypeVc').text(data.pdWaterconntypeVc || '-');
                $('#pdMcconnectionVc').text(data.pdMcconnectionVc || '-');
                $('#pdMeternoVc').text(data.pdMeternoVc || '-');
                $('#pdConsumernoVc').text(data.pdConsumernoVc || '-');
                $('#pdConnectiondateDt').text(data.pdConnectiondateDt || '-');
                $('#pdConnectiondateDt_vc').text(data.pdConnectiondateDt_vc || '-');
                $('#pdPipesizeF').text(data.pdPipesizeF || '-');
                $('#pdElectricityconnectionVc').text(data.pdElectricityconnectionVc || '-');
                $('#pdElemeternoVc').text(data.pdElemeternoVc || '-');
                $('#pdEleconsumernoVc').text(data.pdEleconsumernoVc || '-');
                $('#pdRainwaterhaverstVc').text(data.pdRainwaterhaverstVc || '-');
                $('#pdSolarunitVc').text(data.pdSolarunitVc || '-');
                $('#pdPlotareaF').text(data.pdPlotareaF || '-');
                $('#pdTotbuiltupareaF').text(data.pdTotbuiltupareaF || '-');
                $('#pdTotcarpetareaF').text(data.pdTotcarpetareaF || '-');
                $('#pdTotalexempareaF').text(data.pdTotalexempareaF || '-');
                $('#pdAssesareaF').text(data.pdAssesareaF || '-');
                $('#pdArealetoutF').text(data.pdArealetoutF || '-');
                $('#pdAreanotletoutF').text(data.pdAreanotletoutF || '-');
                $('#pdCurrassesdateDt').text(data.pdCurrassesdateDt || '-');
                $('#pdOldrvFl').text(data.pdOldrvFl || '-');
                $('#user_id').text(data.user_id || '-');
                $('#pdPolytypeVc').text(data.pdPolytypeVc || '-');
                $('#pdStatusbuildingVc').text(data.pdStatusbuildingVc || '-');
                $('#pdLastassesdateDt').text(data.pdLastassesdateDt || '-');
                $('#pdFirstassesdateDt').text(data.pdFirstassesdateDt || '-');
                $('#pdNoticenoVc').text(data.pdNoticenoVc || '-');
                $('#pdIndexnoVc').text(data.pdIndexnoVc || '-');
                $('#pdNewfinalpropnoVc').text(data.pdNewfinalpropnoVc || '-');
                $('#pdTaxstatusVc').text(data.pdTaxstatusVc || '-');
                $('#pdChangedVc').text(data.pdChangedVc || '-');
                $('#pdNadetailsVc').text(data.pdNadetailsVc || '-');
                $('#pdNanumberI').text(data.pdNanumberI || '-');
                $('#pdNadateDt').text(data.pdNadateDt || '-');
                $('#pdTddetailsVc').text(data.pdTddetailsVc || '-');
                $('#pdTdordernumF').text(data.pdTdordernumF || '-');
                $('#pdTddateDt').text(data.pdTddateDt || '-');
                $('#pdSaledeedI').text(data.pdSaledeedI || '-');
                $('#pdSaledateDt').text(data.pdSaledateDt || '-');
                $('#pdCitysurveynoF').text(data.pdCitysurveynoF || '-');
                $('#pdOwnertypeVc').text(data.pdOwnertypeVc || '-');
                $('#pdBuildingvalueI').text(data.pdBuildingvalueI || '-');
                $('#pdPlotvalueF').text(data.pdPlotvalueF || '-');
                $('#pdTpdateDt').text(data.pdTpdateDt || '-');
                $('#pdTpdetailsVc').text(data.pdTpdetailsVc || '-');
                $('#pdTpordernumF').text(data.pdTpordernumF || '-');
                $('#pdOccupinameF').text(data.pdOccupinameF || '-');
                $('#pdPropimageT').text(data.pdPropimageT || '-');
                $('#pdHouseplanT').text(data.pdHouseplanT || '-');
                $('#pdPropimage2T').text(data.pdPropimage2T || '-');
                $('#pdHouseplan2T').text(data.pdHouseplan2T || '-');
                $('#pdSignT').text(data.pdSignT || '-');
                $('.pdFinalpropnoVc').text(data.pdFinalpropnoVc || '-');
                $('#propRefno').text(data.propRefno || '-');
                $('#pdImgstatusVc').text(data.pdImgstatusVc || '-');
                $('#pdSuryprop2Vc').text(data.pdSuryprop2Vc || '-');
                $('#pdSuryprop1Vc').text(data.pdSuryprop1Vc || '-');
                $('#createdAt').text(data.createdAt || '-');
                $('#createddateVc').text(data.createddateVc || '-');
                $('#updatedAt').text(data.updatedAt || '-');
                $('#id').text(data.id || '-');
                $('#pdApprovedByDesk1Vc').text(data.pdApprovedByDesk1Vc || '-');
                $('#pdApprovedByDesk2Vc').text(data.pdApprovedByDesk2Vc || '-');
                $('#pdApprovedByDesk3Vc').text(data.pdApprovedByDesk3Vc || '-');
                $('#pdCompletedByVc').text(data.pdCompletedByVc || '-');
                $('#pdOldwardNoVc').text(data.pdOldwardNoVc || '-');
                $('#pdOldTaxVc').text(data.pdOldTaxVc || '-');
                $('#oldWardNoVc').text(data.oldWardNoVc || '-');
                $('#oldZoneNoVc').text(data.oldZoneNoVc || '-');
                $('#calculatedAnnualRentalValueFl').text(data.annualRentalValueFl || '-');
                $('#calculatedAnnualUnRentalValueFl').text(data.annualUnRentalValueFl || '-');
                $('#calculatedTaxableValueToConsiderFl').text(data.finalValueToConsiderFl || '-');
                $('#calculatedMaintenanceRepairAmountFl').text(data.maintainenceRepairAmountConsideredFl || '-');
                $('#calculatedTaxableValueRented').text(data.taxableValueRentedFl || '-');
                $('#calculatedTaxableValueUnRented').text(data.taxableValueUnRentedFl || '-');
                fetchNameById('/3gSurvey/propertyTypes', data.oldPropertyTypeVc, function(name) {
                    $('#oldPropertyTypeVc').text(name);
                });
                fetchNameById('/3gSurvey/propertySubtype',data.oldPropertySubTypeVc, function(name) {
                    $('#oldPropertySubTypeVc').text(name);
                });
                fetchNameById('/3gSurvey/PropertyUsage', data.oldUsageTypeVc, function(name) {
                    $('#oldUsageTypeVc').text(name);
                });
                $('#oldConstructionTypeVc').text(data.oldConstructionTypeVc || '-');
                $('#oldAssessmentAreaFl').text(data.oldAssessmentAreaFl || '-');
                $('#previousAssessmentDateDt').text(data.previousAssessmentDateDt || '-');
                // $('#currentAssessmentDateDt').text(data.currentAssessmentDateDt || '-');
                $('#propertyTax').text(data.consolidatedTaxes.propertyTaxFl || '0');
                $('#educationTaxResidential').text(data.consolidatedTaxes.educationTaxResidFl || '0');
                $('#educationTaxNonResidential').text(data.consolidatedTaxes.educationTaxCommFl || '0');
                $('#educationTaxTotal').text(data.consolidatedTaxes.educationTaxTotalFl || '0');
                $('#egcTax').text(data.consolidatedTaxes.egcFl || '0');
                $('#treeTax').text(data.consolidatedTaxes.treeTaxFl || '0');
                $('#environmentTax').text(data.consolidatedTaxes.environmentalTaxFl || '0');
                $('#cleannessTax').text(data.consolidatedTaxes.cleannessTaxFl || '0');
                $('#lightTax').text(data.consolidatedTaxes.lightTaxFl || '0');
                $('#fireTax').text(data.consolidatedTaxes.fireTaxFl || '0');
                $('#userChargesTax').text(data.consolidatedTaxes.userChargesFl || '0');
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
                 if (data.pdPropimageT) {
                    document.getElementById('pdPropimageT').src = "data:image/jpeg;base64," + data.pdPropimageT;
                 }

                // Show CAD image
                if (data.pdHouseplan2T) {
                    document.getElementById('pdHouseplan2T').src = "data:image/jpeg;base64," + data.pdHouseplan2T;
                }
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

function formatYearRange(date, useDevanagari = true) {
    if (!date || date === '-') return '-';
    const year = parseInt(date.split('-')[0]);
    const startYear = year;
    const endYear = year + 3;
    const startYearL = startYear + 1;
    const endYearL = endYear + 1;
    const toText = useDevanagari ? convertToDevanagari : (n) => n;
    return `${toText(startYear.toString())}-${toText(startYearL.toString())} ते ${toText(endYear.toString())}-${toText(endYearL.toString())}`;
}
function convertToDevanagari(numberString) {
    const devanagariDigits = ['०', '१', '२', '३', '४', '५', '६', '७', '८', '९'];
    return numberString.replace(/[0-9]/g, (digit) => devanagariDigits[digit]);
}

function populateUnitDetails(units) {
    var $tbody = $('#unitDetailsTable tbody');
    $tbody.empty();

    units.sort((a,b) => {
        const numA = parseInt(a.unitNoVc);
        const numB = parseInt(b.unitNoVc);
        
        if(isNaN(numA) || isNaN(numB)){
            return (a.unitNoVc || '').localeCompare(b.unitNoVc || '');
        }
        return numA - numB;
    });

    units.forEach(function(unit) {
        var unitRow = `
            <tr class="t-a-c h-25">
                <td>${unit.unitNoVc || '-'}</td>
                <td>${unit.floorNoVc || '-'}</td>
                <td id="occupantStatus-${unit.unitNoVc}">${unit.occupantStatusI || '-'}</td>
                <td id="usageType-${unit.unitNoVc}">${unit.usageTypeVc || '-'}</td>
                <td id="usageSubType-${unit.unitNoVc}">${unit.usageSubTypeVc || '-'}</td>
                <td>${unit.constructionTypeVc || '-'}</td>
                <td>${unit.constructionYearVc || '-'}</td>
                <td>${unit.constructionAgeVc || '-'}</td>
                <td>${unit.ageFactorVc || '-'}</td>
                <td>${unit.exemptedAreaFl || '0'}</td>
                <td>${unit.carpetAreaFl || '0'}</td>
                <td>${unit.plotAreaFl || '0'}</td>
                <td>${unit.exemptedAreaFl || '0'}</td>
                <td>${unit.taxableAreaFl || '0'}</td>
                //property r_values table
                <td>${unit.ratePerSqMFl || '0'}</td>
                <td>${unit.rentalValAsPerRateFl || '0'}</td>
                <td>${unit.depreciationRateFl || '0'}</td>
                <td>${unit.depreciationAmountFl || '0'}</td>
                <td>${unit.amountAfterDepreciationFl || '0'}</td>
                <td>${unit.maintenanceRepairsFl || '0'}</td>
                <td>${unit.taxableValueByRateFl || '0'}</td>
                <td>${unit.tenantNameVc || '-'}</td>
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
                callback('-');
            }
        },
        error: function(xhr, status, error) {
            console.error('Error fetching name:', error);
            callback('-');
        }
    });
    }