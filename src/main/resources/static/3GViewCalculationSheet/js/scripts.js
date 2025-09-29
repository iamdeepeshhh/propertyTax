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
    buildReportTaxTable("#calculationSheetTaxTable", "CALCULATION_SHEET");

    // Build dynamic tax headers from report tax configs (same logic as RealtimeCC/Batch)
//    $.get('/3g/reportTaxConfigs?template=CALCULATION_SHEET', function(configs) {
//        if (!configs || configs.length === 0) {
//            console.warn('No tax configs received');
//            return;
//        }
//        const table = $('#rightTable');
//        const rows = table.find('tr');
//        if (rows.length > 1) rows.slice(1).remove();
//        const headerRow1 = $('<tr id="taxHeaderRow" height="5" class="t-a-c"></tr>').css('background-color', '#CEF6CE');
//        const headerRow2 = $('<tr id="taxChildHeaderRow" class="t-a-c"></tr>').css('background-color', '#F8E0F7');
//        const valueRow   = $('<tr id="taxValueRow" class="h-25 t-a-c"></tr>');
//        table.append(headerRow1).append(headerRow2).append(valueRow);
//        const parentMap = {};
//        configs.forEach(cfg => { if (!cfg.parentTaxKeyL) parentMap[cfg.taxKeyL] = { parent: cfg, children: [] }; });
//        configs.forEach(cfg => { if (cfg.parentTaxKeyL) { (parentMap[cfg.parentTaxKeyL] ||= { parent: null, children: [] }).children.push(cfg); } });
//        configs.sort((a,b) => a.sequenceI - b.sequenceI);
//        configs.forEach(cfg => {
//            if (!cfg.parentTaxKeyL) {
//                const group = parentMap[cfg.taxKeyL];
//                if (group && group.children.length > 0) {
//                    headerRow1.append(`<th colspan="${group.children.length + (cfg.showTotalBl ? 1 : 0)}">${cfg.localNameVc}</th>`);
//                    group.children.forEach(child => {
//                        headerRow2.append(`<th>${child.localNameVc}</th>`);
//                        valueRow.append(`<td id="tax-${child.taxKeyL}"><b>0</b></td>`);
//                    });
//                    if (cfg.showTotalBl) {
//                        headerRow2.append('<th>एकूण</th>');
//                        valueRow.append(`<td id="tax-${cfg.taxKeyL}"><b>0</b></td>`);
//                    }
//                } else {
//                    headerRow1.append(`<th rowspan="2">${cfg.localNameVc}</th>`);
//                    valueRow.append(`<td id="tax-${cfg.taxKeyL}"><b>0</b></td>`);
//                }
//            }
//        });
//        headerRow1.append('<th rowspan="2">Total</th>');
//        valueRow.append('<td id="totalTax"><b>0</b></td>');
//    });

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
                $('#pdCategoryI').text(translate("category", data.pdCategoryI));
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
                $('#pdPermissionstatusVc').text(translate("yesNo", data.pdPermissionstatusVc));
                $('#pdPermissionnoVc').text(data.pdPermissionnoVc || '-');
                $('#pdPermissiondateDt').text(data.pdPermissiondateDt || '-');
                $('#pdNooffloorsI').text(data.pdNooffloorsI || '-');
                $('#pdNoofroomsI').text(data.pdNoofroomsI || '-');
                $('#pdStairVc').text(translate("yesNo", data.pdStairVc));
                $('#pdLiftVc').text(translate("yesNo", data.pdLiftVc));
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
                $('#pdRainwaterhaverstVc').text(translate("yesNo", data.pdRainwaterhaverstVc));
                $('#pdSolarunitVc').text(translate("yesNo", data.pdSolarunitVc));
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
                $('#pdStatusbuildingVc').text(translate("buildingStatus", data.pdStatusbuildingVc));
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
                $('#pdOwnertypeVc').text(translate("ownerType", data.pdOwnertypeVc));
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
                // Fill dynamic taxes by keys
                if (data.taxKeyValueMap) {
                    const m = data.taxKeyValueMap;
                    for (let key in m) {
                        $(`#tax-${key}`).text(m[key] != null ? m[key] : 0);
                    }
                }
                if (data.consolidatedTaxes) {
                    $('#totalTax').text(data.consolidatedTaxes.totalTaxFl || '0');
                }
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

function buildReportTaxTable(tableSelector, templateName = "CALCULATION_SHEET") {
    $.get('/3g/reportTaxConfigs?template=' + templateName, function(configs) {
        if (!configs || configs.length === 0) {
            console.warn('No tax configs received');
            return;
        }

        const table = $(tableSelector);
        const rows = table.find('tr');
        if (rows.length > 1) rows.slice(1).remove();

        const headerRow1 = $('<tr class="t-a-c"></tr>').css('background-color', '#CEF6CE');
        const headerRow2 = $('<tr class="t-a-c"></tr>').css('background-color', '#F8E0F7');
        const valueRow   = $('<tr class="t-a-c"></tr>');

        table.append(headerRow1).append(headerRow2).append(valueRow);

        const parentMap = {};
        configs.forEach(cfg => {
            if (!cfg.parentTaxKeyL) parentMap[cfg.taxKeyL] = { parent: cfg, children: [] };
        });
        configs.forEach(cfg => {
            if (cfg.parentTaxKeyL) {
                (parentMap[cfg.parentTaxKeyL] ||= { parent: null, children: [] }).children.push(cfg);
            }
        });

        configs.sort((a,b) => a.sequenceI - b.sequenceI);

        configs.forEach(cfg => {
            if (!cfg.parentTaxKeyL) {
                const group = parentMap[cfg.taxKeyL];
                if (group && group.children.length > 0) {
                    headerRow1.append(`<th colspan="${group.children.length + (cfg.showTotalBl ? 1 : 0)}">${cfg.localNameVc}</th>`);
                    group.children.forEach(child => {
                        headerRow2.append(`<th>${child.localNameVc}</th>`);
                        valueRow.append(`<td id="tax-${child.taxKeyL}"><b>0</b></td>`);
                    });
                    if (cfg.showTotalBl) {
                        headerRow2.append('<th>एकूण</th>');
                        valueRow.append(`<td id="tax-${cfg.taxKeyL}"><b>0</b></td>`);
                    }
                } else {
                    headerRow1.append(`<th rowspan="2">${cfg.localNameVc}</th>`);
                    valueRow.append(`<td id="tax-${cfg.taxKeyL}"><b>0</b></td>`);
                }
            }
        });

        headerRow1.append('<th rowspan="2">एकूण</th>');
        valueRow.append('<td id="totalTax"><b>0</b></td>');
    });
}

function formatYearRange(date, useDevanagari = true) {
    if (!date || date === '-') return '-';
    const year = parseInt(date.split('-')[0]);
    const startYear = year;
    const endYear = year + 3;
    const startYearL = startYear + 1;
    const endYearL = endYear + 1;
    const toText = useDevanagari ? convertToDevanagari : (n) => n;
    return `${toText(startYear.toString())}-${toText(startYearL.toString())} - ${toText(endYear.toString())}-${toText(endYearL.toString())}`;
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
                <td id="occupantStatus-${unit.unitNoVc}">${translate("occupantStatus", unit.occupantStatusI)}</td>
                <td id="usageType-${unit.unitNoVc}">${unit.usageTypeVc || '-'}</td>
                <td id="usageSubType-${unit.unitNoVc}">${unit.usageSubTypeVc || '-'}</td>
                <td>${unit.constructionTypeVc || '-'}</td>
                <td>${unit.constructionYearVc || '-'}</td>
                <td>${unit.constructionAgeVc || '-'}</td>
                <td>${unit.ageFactorVc || '-'}</td>
                <td>${unit.areaBefDedFl || '0'}</td>
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
            $('#occupantStatus-' + unit.unitNoVc).text(translate("occupantStatus", name));
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

    const translationsMap = {
      ownerType: {
        "Private": "खाजगी",
        "Co-operative Housing Society": "गृहनिर्माण सहकारी संस्था",
        "Government": "शासन",
        "Municipality": "नगर पंचायत",
        "Public": "सार्वजनिक",
        "Other": "इतर"
      },
      occupantStatus: {
        "Owner": "मालक",
        "Tenant": "भाडेकरी",
        "Occupier": "भोगवटदार",
        "Encroacher": "अतिक्रमणकारी",
        "Owner/Tenant": "मालक/भाडेकरी"
      },
      category: {
        "Owner": "मालक",
        "Tenant": "भाडेकरू",
        "Owner/Tenant": "मालक/भाडेकरू",
        "Occupier": "भोगवटदार",
        "Encroacher": "अतिक्रमणकारी"
      },
      buildingStatus: {
        "Normal": "साधारण",
        "Demolished": "पाडलेले",
        "normal": "साधारण",
        "demolished": "पाडलेले"
      },
      yesNo: {
        "yes": "होय",
        "no": "नाही"
      }
    };


    function translate(field, value) {
      if (!value || value === '-') return '-';

      // normalize Yes/No case-insensitively
      if (field === "yesNo") {
        const key = value.toString().toLowerCase();
        return translationsMap.yesNo[key] || value;
      }

      const map = translationsMap[field] || {};
      return map[value] || value;
    }
