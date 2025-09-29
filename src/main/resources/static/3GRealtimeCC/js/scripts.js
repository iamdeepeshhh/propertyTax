
    $(document).ready(function() {
        // Normalize header structure to include dynamic council + year ranges
        try {
            var $firstDiv = $('#firstDiv');
            var $row = $firstDiv.find('tr').first();
            var $cells = $row.find('td');
            if ($cells.length >= 2) {
                var $left = $cells.eq(0);
                var $center = $cells.eq(1);

                // Ensure logo element exists and is identifiable
                var $logo = $left.find('img').first();
                if ($logo.length === 0) {
                    $logo = $('<img>');
                    $left.empty().append($logo);
                }
                $logo.attr({ id: 'councilLogo', alt: 'Council Logo' }).css({ height: '80px', width: '80px' });
                if (!$logo.attr('src')) {
                    $logo.attr('src', 'https://example.com/default-image.png');
                }

                // Replace center cell content with dynamic-friendly markup
                $center.html(
                    '<span id="councilName" style="color: black; font-size: 25px;">Municipal Council</span><br>' +
                    '(नमुना ४३ नियम ७४ पहा)/(Form No. 43 See Rule No. 74)<br>' +
                    'सन <span class="yearRangeDev">year range</span> या वर्षाकरिता कर आकारणीस पात्र असलेल्या इमारती व जमीन यांची आकारणी सूची/Assessment List of Buildings and Lands for the Year of <span class="yearRangeEng">year range</span>'
                );
            }
        } catch (e) {
            console.warn('Header normalization skipped:', e);
        }
        // Inject council details (name/logo) similar to 3GViewCalculationSheet
        $.ajax({
            url: '/3g/getCouncilDetails',
            type: 'GET',
            success: function (data) {
                if (data && data.length > 0) {
                    const councilDetails = data[0];
                    // Find existing header elements and tag them for reuse
                    var $logo = $('#firstDiv img').first();
                    var $name = $('#firstDiv td').eq(1).find('span').first();
                    if ($logo.length) { $logo.attr('id', 'councilLogo'); }
                    if ($name.length) { $name.attr('id', 'councilName'); }

                    // Set name and logo
                    if ($name.length) {
                        $name.text((councilDetails.localName || 'नगर परिषद') + ' / ' + (councilDetails.standardName || 'Municipal Council'));
                    }
                    if ($logo.length) {
                        if (councilDetails.imageBase64) {
                            $logo.attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
                        } else {
                            $logo.attr('src', 'https://example.com/fallback-image.png');
                        }
                    }

                    // Update second page header (thirdDiv) if present
                    var $third = $('#thirdDiv');
                    if ($third.length) {
                        var $row2 = $third.find('tr').first();
                        var $cells2 = $row2.find('td');
                        if ($cells2.length >= 2) {
                            var $logo2 = $cells2.eq(0).find('img').first();
                            var $center2 = $cells2.eq(1);
                            if ($logo2.length) {
                                if (councilDetails.imageBase64) {
                                    $logo2.attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
                                } else {
                                    $logo2.attr('src', 'https://example.com/fallback-image.png');
                                }
                                $logo2.attr({ id: 'councilLogo1', alt: 'Council Logo' });
                            }
                            $center2.html(
                                '<span id="councilName1" style="color: black; font-size: 25px;"></span><br>' +
                                '(नमुना ४३ नियम ७४ पहा)/(Form No. 43 See Rule No. 74)<br>' +
                                'सन <span class="yearRangeDev">year range</span> या वर्षाकरिता कर आकारणीस पात्र असलेल्या इमारती व जमीन यांची आकारणी सूची/Assessment List of Buildings and Lands for the Year of <span class="yearRangeEng">year range</span>'
                            );
                            $('#councilName1').text((councilDetails.localName || 'नगर परिषद') + ' / ' + (councilDetails.standardName || 'Municipal Council'));
                        }
                    }

                    // Replace SiteName placeholders in signatory sections
                    var siteNameStd = councilDetails.standardSiteNameVC || councilDetails.standardName || '';
                    var siteNameLocal = councilDetails.localSiteNameVC || councilDetails.localName || '';
                    // Signatories around consolidated taxes
                    $("table.f-w-b.b-n.m-t-50 td, table.b-n td").each(function(){
                        var $el = $(this);
                        var text = $el.text();
                        if (text.indexOf('SiteName') >= 0) {
                            var hasDevanagari = /[\u0900-\u097F]/.test(text);
                            var repl = hasDevanagari ? siteNameLocal : siteNameStd;
                            $el.text(text.replace(/SiteName/g, repl));
                        }
                    });
                }
            },
            error: function () {
                // Silent fail; keep defaults
                console.warn('Failed to load council details');
            }
        });

        // Prepare second-page header to mirror first with simple spans
        ensureSecondPageHooks();

        // Fetch assessment dates to populate year ranges (FY header)
        $.ajax({
            url: '/3g/getAllAssessmentDates',
            type: 'GET',
            success: function (data) {
                if (data && data.length > 0) {
                    const current = data[0].currentassessmentdate || '-';
                    const fyEng = formatYearRange(current, false);
                    const fyDev = formatYearRange(current, true);
                    $('.financialYear, #financialYear').text(fyEng);
                    $('.yearRangeDev').text(fyDev);
                    $('.yearRangeEng').text(fyEng);
                }
            },
            error: function () {
                console.warn('Failed to load assessment dates');
            }
        });

        buildReportTaxTable("#calculationSheetTaxTable", "CALCULATION_SHEET");



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

                    // Populating table data (ids and common classes for multi-page)
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
                    // Populate images: property photo and floor plan
                    setImageFromData($('#pdPropimageT'), data.pdPropimageT);
                    setImageFromData($('#pdHouseplan2T'), data.pdHouseplan2T || data.pdHouseplanT);
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
                    // $('#propertyTax').text(data.consolidatedTaxes.propertyTaxFl || 'N/A');
                    // $('#educationTaxResidential').text(data.consolidatedTaxes.educationTaxResidFl || '0');
                    // $('#educationTaxNonResidential').text(data.consolidatedTaxes.educationTaxCommFl || '0');
                    // $('#educationTaxTotal').text(data.consolidatedTaxes.educationTaxTotalFl || '0');
                    // $('#egcTax').text(data.consolidatedTaxes.egcFl || '0');
                    // $('#treeTax').text(data.consolidatedTaxes.treeTaxFl || '0');
                    // $('#environmentTax').text(data.consolidatedTaxes.environmentalTaxFl || '0');
                    // $('#cleannessTax').text(data.consolidatedTaxes.cleannessTaxFl || '0');
                    // $('#lightTax').text(data.consolidatedTaxes.lightTaxFl || '0');
                    // $('#fireTax').text(data.consolidatedTaxes.fireTaxFl || '0');
                    // $('#userChargesTax').text(data.consolidatedTaxes.userFeesFl || '0');
                    // $('#totalTax').text(data.consolidatedTaxes.totalTaxFl || '0');
                    if (data && data.taxKeyValueMap) {
                        const taxKeyValueMap = data.taxKeyValueMap;
                        for (let key in taxKeyValueMap) {
                            $(`#tax-${key}`).text(taxKeyValueMap[key]);
                        }
                    }
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

    // Helpers ported from 3GViewCalculationSheet
    function formatYearRange(date, useDevanagari = true) {
          if (!date || date === '-') return '-';
          const year = parseInt(date.split('-')[0]);
          const startYear = year;
          const endYear = year + 3;
          const startYearL = startYear + 1;
          const endYearL = endYear + 1;
          const toText = useDevanagari ? convertToDevanagari : (n) => n;
          const separator = useDevanagari ? 'ते' : 'to';
          return `${toText(startYear.toString())}-${toText(startYearL.toString())} ${separator} ${toText(endYear.toString())}-${toText(endYearL.toString())}`;
      }
    function convertToDevanagari(numberString) {
        var map = ['०','१','२','३','४','५','६','७','८','९'];
        return String(numberString).replace(/\d/g, function(d){ return map[parseInt(d)]; });
    }

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

    // Best-effort image setter handling base64 or URL
    function setImageFromData($img, value) {
        if (!$img || $img.length === 0) return;
        if (!value || value === 'N/A' || value === '-') {
            $img.attr('src', '');
            return;
        }
        var v = String(value).trim();
        if (v.startsWith('data:image')) {
            $img.attr('src', v);
            return;
        }
        if (/^https?:\/\//i.test(v)) {
            $img.attr('src', v);
            return;
        }
        // Heuristics for base64 blob without mime prefix
        var mime = 'image/jpeg';
        if (v.startsWith('iVBOR')) mime = 'image/png';
        if (v.startsWith('/9j/')) mime = 'image/jpeg';
        $img.attr('src', 'data:' + mime + ';base64,' + v);
    }

    // Backward-compat: defined as no-op to avoid ReferenceError
    function ensureSecondPageHooks() {}
