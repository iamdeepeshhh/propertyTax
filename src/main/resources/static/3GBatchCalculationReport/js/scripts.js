// Preload master data at script load so helpers can use it
let masters = {};
let mastersReady = Promise.resolve();
try {
  mastersReady = preloadMasters().then(m => { masters = m; }).catch(() => { masters = {}; });
} catch (e) { masters = {}; mastersReady = Promise.resolve(); }

$(document).ready(function () {
 // (reverted) remove dynamic tax header builder
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
                // Also set dynamic site/district names if present in template
                $('.siteNameStd, .standardSiteNameVC').text(councilDetails.standardSiteNameVC || councilDetails.standardName || '');
                $('.siteNameLocal, .localSiteNameVC').text(councilDetails.localSiteNameVC || councilDetails.localName || '');
                $('.districtNameStd').text(councilDetails.standardDistrictNameVC || '');
                $('.districtNameLocal').text(councilDetails.localDistrictNameVC || '');
            } else {
                $('#councilLogo').attr('src', 'https://example.com/no-data-image.png');
            }
        },
        error: function(xhr, status, error) {
            console.error('Error fetching council name:', error);
            $('#councilName').text('-');
        }
    });
    // Fetch and show assessment year range (for F.Y. field)
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
    buildReportTaxTable("#calculationSheetTaxTable", "CALCULATION_SHEET");

// Fetch tax config for this template (build dynamic headers like 3GRealtimeCC)
//    $.get('/3g/reportTaxConfigs?template=CALCULATION_SHEET', function(configs) {
//        if (!configs || configs.length === 0) {
//            console.warn("No tax configs received");
//            return;
//        }
//
//        // Ensure table scaffold exists and remove any old/static rows except the title row
//        const table = $('#rightTable');
//        const rows = table.find('tr');
//        if (rows.length > 1) {
//            rows.slice(1).remove();
//        }
//
//        // Create target rows inside the template (these will clone with #maindiv)
//        const headerRow1 = $('<tr id="taxHeaderRow" height="5" class="t-a-c"></tr>').css('background-color', '#CEF6CE');
//        const headerRow2 = $('<tr id="taxChildHeaderRow" class="t-a-c"></tr>').css('background-color', '#F8E0F7');
//        const valueRow   = $('<tr id="taxValueRow" class="h-25 t-a-c"></tr>');
//        table.append(headerRow1).append(headerRow2).append(valueRow);
//
//        // Build parent-child structure
//        const parentMap = {};
//        configs.forEach(cfg => {
//            if (!cfg.parentTaxKeyL) {
//                parentMap[cfg.taxKeyL] = { parent: cfg, children: [] };
//            }
//        });
//        configs.forEach(cfg => {
//            if (cfg.parentTaxKeyL) {
//                if (!parentMap[cfg.parentTaxKeyL]) {
//                    parentMap[cfg.parentTaxKeyL] = { parent: null, children: [] };
//                }
//                parentMap[cfg.parentTaxKeyL].children.push(cfg);
//            }
//        });
//
//        // Sort by sequence
//        configs.sort((a, b) => a.sequenceI - b.sequenceI);
//
//        // Render headers
//        configs.forEach(cfg => {
//            if (!cfg.parentTaxKeyL) {
//                const group = parentMap[cfg.taxKeyL];
//                if (group && group.children.length > 0) {
//                    // Parent header spans children (+ total if needed)
//                    headerRow1.append(
//                        `<th colspan="${group.children.length + (cfg.showTotalBl ? 1 : 0)}">${cfg.localNameVc}</th>`
//                    );
//
//                    // Add children in row2
//                    group.children.forEach(child => {
//                        headerRow2.append(`<th>${child.localNameVc}</th>`);
//                        valueRow.append(`<td id="tax-${child.taxKeyL}"><b>0</b></td>`);
//                    });
//
//                    // If parent has total column
//                   if (cfg.showTotalBl) {
//                       headerRow2.append(`<th>Total</th>`);
//                       valueRow.append(`<td id="tax-${cfg.taxKeyL}"><b>0</b></td>`);
//                   }
//                } else {
//                    // Standalone tax
//                    headerRow1.append(`<th rowspan="2">${cfg.localNameVc}</th>`);
//                    valueRow.append(`<td id="tax-${cfg.taxKeyL}"><b>0</b></td>`);
//                }
//            }
//        });
//
//        // Always append grand total at end
//        headerRow1.append(`<th rowspan="2">एकूण</th>`);
//        valueRow.append(`<td id="totalTax"><b>0</b></td>`);

    
    // Fetch the report data from the backend in pages of 100
    const wardNo = window.location.pathname.split('/').pop();
    const size = 100;
    let page = 0;
    let total = null;
    let rendered = 0;

    // Simple progress indicator
    const $progress = $('<div id="batch-progress" style="position:fixed;top:8px;right:12px;background:#222;color:#fff;padding:6px 10px;border-radius:4px;z-index:9999;font:12px/1.4 sans-serif;">Loading...</div>');
    $('body').append($progress);
    // Hide per-chunk toolbar label and button ("Chunk X - Y reports") per request
    (function hideChunkToolbar() {
      try {
        const id = 'hide-chunk-toolbar-style';
        if (!document.getElementById(id)) {
          const style = document.createElement('style');
          style.id = id;
          style.type = 'text/css';
          style.appendChild(document.createTextNode('.chunk-toolbar{display:none!important;}'));
          document.head.appendChild(style);
        }
      } catch (e) { /* no-op */ }
    })();

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
    function updateProgress() {
        if (total !== null) {
            $progress.text(`Rendered ${rendered} of ${total}`);
        } else {
            $progress.text(`Rendered ${rendered}...`);
        }
    }

    function renderBatch(data) {
        const groupedProperties = groupByPropertyNumber(data);
        const baseIndex = rendered;

        // Create a container for this batch to support per-chunk printing
        const $chunk = $('<div class="batch-chunk"></div>');

        // Add a small toolbar for printing this chunk
        const chunkLabel = `Chunk ${Math.floor(baseIndex / size) + 1}`; /*
        `const $toolbar = $(`
          <div class="chunk-toolbar" style="position:relative;margin:8px 0;padding:6px 10px;background:#f7f7f7;border:1px solid #ddd;font:12px/1.4 sans-serif;">
            <span>${chunkLabel} — ${groupedProperties.length} reports</span>
            <button type="button" class="print-chunk-btn" style="float:right;padding:4px 8px;">Print this ${groupedProperties.length}</button>
          </div>
        `);`*/
        const $toolbar = $(
          `<div class="chunk-toolbar" style="position:relative;margin:8px 0;padding:6px 10px;background:#f7f7f7;border:1px solid #ddd;font:12px/1.4 sans-serif;">
            <span>${chunkLabel} - ${groupedProperties.length} reports</span>
            <button type="button" class="print-chunk-btn" style="float:right;padding:4px 8px;">Print this ${groupedProperties.length}</button>
          </div>`
        );
        $toolbar.find('.print-chunk-btn').on('click', () => {
          try {
            // Inject minimal CSS for printing only this chunk
            ensurePrintCss();
            $chunk.addClass('print-chunk');
            window.print();
          } finally {
            $chunk.removeClass('print-chunk');
          }
        });
        $chunk.append($toolbar);

        groupedProperties.forEach((property, idx) => {
            const index = baseIndex + idx;

            let clonedDiv = $('#maindiv')
              .clone()
              .removeAttr('id')
              .attr('id', 'maindiv-' + index)
              .show();

            populateReportData(clonedDiv, property);

            // ✅ Wrap in a container so printing can chunk properly
            const wrapper = $('<div class="report-wrapper" style="page-break-after: always;"></div>');
            wrapper.append(clonedDiv);

            $chunk.append(wrapper);
        });

        $('body').append($chunk);

        rendered += groupedProperties.length;
        updateProgress();
    }

    async function fetchCount() {
        try {
            const res = await fetch(`/3g/propertyCalculationSheetReport/count?wardNo=${wardNo}`);
            if (res.ok) {
                const n = await res.text();
                const parsed = parseInt(n, 10);
                if (!isNaN(parsed)) total = parsed;
            }
        } catch (e) { console.warn('Count fetch failed', e); }
    }

    async function fetchPage(p) {
        const url = `/3g/propertyCalculationSheetReport?wardNo=${wardNo}&page=${p}&size=${size}`;
        const res = await fetch(url);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
    }

    async function loadAllBatches() {
        try { await mastersReady; } catch (e) { /* ignore, fall back to ids */ }
        await fetchCount();
        updateProgress();
        while (true) {
            try {
                const batch = await fetchPage(page);
                if (!Array.isArray(batch) || batch.length === 0) break;
                renderBatch(batch);
                page += 1;
                // Yield to UI thread between batches
                await new Promise(r => setTimeout(r, 0));
            } catch (err) {
                console.error('Error fetching property report batch:', err);
                break;
            }
        }
        $('#maindiv').hide(); // Hide the original template
        $progress.text(`Rendered ${rendered}${total !== null ? ' of ' + total : ''}`);
    }

    loadAllBatches();
    });

    function populateReportData(div, property) {

        console.log(property);
        // Set the text values for the fields within the cloned div using property data
        // F.Y., Date, Time appear on both pages; set by id and class
        setTextTargets(div, ['#financialYear', '.financialYear'], property.financialYear || $('#financialYear').text() || '-');
        setTextTargets(div, ['#assessmentDate', '.assessmentDate'], property.currentAssessmentDateDt || '-');
        setTextTargets(div, ['#assessmentTime', '.assessmentTime'], property.assessmentTime || '-');
        div.find('#pageNo').text(property.pageNo || '-');
        setTextTargets(div, ['#pdFinalpropnoVc', '.pdFinalpropnoVc'], property.pdFinalpropnoVc || '-');
        div.find('#pdSurypropnoVc').text(property.pdSurypropnoVc || '-');
        div.find('#pdIndexnoVc').text(property.pdIndexnoVc || '-');
        // Populate images
        if (property.pdPropimageT) {
          const img = div.find('#pdPropimageT');
          if (img && img.length) img.attr('src', 'data:image/jpeg;base64,' + property.pdPropimageT);
        }
        if (property.pdHouseplan2T) {
          const cad = div.find('#pdHouseplan2T');
          if (cad && cad.length) cad.attr('src', 'data:image/jpeg;base64,' + property.pdHouseplan2T);
        }
        setTextTargets(div, ['#pdWardI', '.pdWardI'], property.pdWardI || '-');
        setTextTargets(div, ['#pdZoneI', '.pdZoneI'], property.pdZoneI || '-');
        setTextTargets(div, ['#pdOldpropnoVc', '.pdOldpropnoVc'], property.pdOldpropnoVc || '-');
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
        div.find('#pdPermissionstatusVc')
           .text(translate("yesNo", property.pdPermissionstatusVc));
        div.find('#pdPermissionnoVc').text(property.pdPermissionnoVc || '-');
        div.find('#pdPermissiondateDt').text(property.pdPermissiondateDt || '-');
        div.find('#pdRainwaterhaverstVc')
           .text(translate("yesNo", property.pdRainwaterhaverstVc));

        div.find('#pdSolarunitVc')
           .text(translate("yesNo", property.pdSolarunitVc));

        div.find('#pdStairVc')
           .text(translate("yesNo", property.pdStairVc));

        div.find('#pdLiftVc')
           .text(translate("yesNo", property.pdLiftVc));
        div.find('#pdContactnoVc').text(property.pdContactnoVc || '-');
        div.find('#oldWardNoVc').text(property.oldWardNoVc || '-');
        div.find('#oldZoneNoVc').text(property.oldZoneNoVc || '-');
        // Old property details: resolve names via preloaded maps
        div.find('#oldPropertyTypeVc').text(resolveName(masters.propertyTypes, property.oldPropertyTypeVc));
        div.find('#oldPropertySubTypeVc').text(resolveName(masters.propertySubtypes, property.oldPropertySubTypeVc));
        div.find('#oldUsageTypeVc').text(resolveName(masters.propertyUsageTypes, property.oldUsageTypeVc));
        div.find('#oldConstructionTypeVc').text(property.oldConstructionTypeVc || '-');
        div.find('#pdCategoryI')
           .text(translate("category", property.pdCategoryI));
        div.find('#oldAssessmentAreaFl').text(property.oldAssessmentAreaFl || '-');
        div.find('#previousAssessmentDateDt').text(property.previousAssessmentDateDt || '-');
        div.find('#currentAssessmentDateDt').text(property.currentAssessmentDateDt || '-');

        // Populate other relevant property details (IDs -> Local names) using preloaded maps
        div.find('#pdPropertytypeI').text(resolveName(masters.propertyTypes, property.pdPropertytypeI));
        div.find('#pdPropertysubtypeI').text(resolveName(masters.propertySubtypes, property.pdPropertysubtypeI));
        div.find('#pdUsagetypeI').text(resolveName(masters.propertyUsageTypes, property.pdUsagetypeI));
        div.find('#pdUsagesubtypeI').text(resolveName(masters.propertySubUsageTypes, property.pdUsagesubtypeI));
        div.find('#pdBuildingtypeI').text(resolveName(masters.buildingTypes, property.pdBuildingtypeI));
        div.find('#pdBuildingsubtypeI').text(resolveName(masters.buildingSubTypes, property.pdBuildingsubtypeI));
        div.find('#pdStatusbuildingVc')
           .text(translate("buildingStatus", property.pdStatusbuildingVc));
        div.find('#pdOwnertypeVc')
           .text(translate("ownerType", property.pdOwnertypeVc));
        div.find('#pdCategoryI')
           .text(translate("category", property.pdCategoryI));
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
        // Map calculated/totals consistently with 3GRealtimeCC and AssessmentResultsDto
        // AssessmentResultsDto fields: annualRentalValueFl, annualUnRentalValueFl,
        // finalValueToConsiderFl, maintainenceRepairAmountConsideredFl,
        // taxableValueRentedFl, taxableValueUnRentedFl
        div.find('#calculatedAnnualRentalValueFl').text(property.annualRentalValueFl || '-');
        div.find('#calculatedAnnualUnRentalValueFl').text(property.annualUnRentalValueFl || '-');
        div.find('#calculatedTaxableValueToConsiderFl').text(property.finalValueToConsiderFl || '-');
        div.find('#calculatedMaintenanceRepairAmountFl').text(property.maintainenceRepairAmountConsideredFl || '-');
        div.find('#calculatedTaxableValueRented').text(property.taxableValueRentedFl || '-');
        div.find('#calculatedTaxableValueUnRented').text(property.taxableValueUnRentedFl || '-');
        // Proposed ratable values on both pages (use ids and classes)
        setTextTargets(div, ['#proposedResidentFl', '#proposedResidentFl1', '.proposedResidentFl'], property.proposedRatableValues.residentialFl || '0');
        setTextTargets(div, ['#proposedCommercialFl', '#proposedCommercialFl1', '.proposedCommercialFl'], property.proposedRatableValues.commercialFl || '0');
        setTextTargets(div, ['#proposedReligiousFl', '#proposedReligiousFl1', '.proposedReligiousFl'], property.proposedRatableValues.religiousFl || '0');
        setTextTargets(div, ['#proposedOpenPlotResidentialFl', '#proposedOpenPlotResidentialFl1', '.proposedOpenPlotResidentialFl'], property.proposedRatableValues.residentialOpenPlotFl || '0');
        setTextTargets(div, ['#proposedOpenPlotCommercialFl', '#proposedOpenPlotCommercialFl1', '.proposedOpenPlotCommercialFl'], property.proposedRatableValues.commercialOpenPlotFl || '0');
        setTextTargets(div, ['#proposedOpenPlotReligiousFl', '#proposedOpenPlotReligiousFl1', '.proposedOpenPlotReligiousFl'], property.proposedRatableValues.religiousOpenPlotFl || '0');
        setTextTargets(div, ['#proposedOpenPlotEducationFl', '#proposedOpenPlotEducationFl1', '.proposedOpenPlotEducationFl'], property.proposedRatableValues.educationAndLegalInstituteOpenPlotFl || '0');
        setTextTargets(div, ['#proposedOpenPlotGovernmentFl', '#proposedOpenPlotGovernmentFl1', '.proposedOpenPlotGovernmentFl'], property.proposedRatableValues.governmentOpenPlotFl || '0');
        setTextTargets(div, ['#proposedOpenPlotIndustrialFl', '#proposedOpenPlotIndustrialFl1', '.proposedOpenPlotIndustrialFl'], property.proposedRatableValues.industrialOpenPlotFl || '0');
        setTextTargets(div, ['#proposedEducationFl', '#proposedEducationFl1', '.proposedEducationFl'], property.proposedRatableValues.educationalInstituteFl || '0');
        setTextTargets(div, ['#proposedGovernmentFl', '#proposedGovernmentFl1', '.proposedGovernmentFl'], property.proposedRatableValues.governmentFl || '0');
        setTextTargets(div, ['#proposedIndustrialFl', '#proposedIndustrialFl1', '.proposedIndustrialFl'], property.proposedRatableValues.industrialFl || '0');
        setTextTargets(div, ['#proposedMobileTowerFl', '#proposedMobileTowerFl1', '.proposedMobileTowerFl'], property.proposedRatableValues.mobileTowerFl || '0');
        setTextTargets(div, ['#proposedElectricSubstationFl', '#proposedElectricSubstationFl1', '.proposedElectricSubstationFl'], property.proposedRatableValues.electricSubstationFl || '0');
        setTextTargets(div, ['#proposedTotalFl', '#proposedTotalFl1', '.proposedTotalFl'], property.proposedRatableValues.aggregateFl || '0');

        // Dynamic tax key rendering like 3GRealtimeCC
        if (property && property.taxKeyValueMap) {
            const taxKeyValueMap = property.taxKeyValueMap;
            for (let key in taxKeyValueMap) {
                div.find(`#tax-${key}`).text(taxKeyValueMap[key] != null ? taxKeyValueMap[key] : 0);
            }
        }
        if (property && property.consolidatedTaxes) {
            div.find('#totalTax').text(property.consolidatedTaxes.totalTaxFl || '0');
        }

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
                        <td id="occupantStatus-${unit.unitNoVc}">
                          ${resolveName(masters.occupantStatus, unit.occupantStatusI)}
                        </td>
                        <td id="usageType-${unit.unitNoVc}">${resolveName(masters.unitUsageTypes, getFirstDefined(unit.usageTypeI, unit.usageTypeVc, unit.usageTypeId))}</td>
                        <td id="usageSubType-${unit.unitNoVc}">${resolveName(masters.unitSubUsageTypes, getFirstDefined(unit.usageSubTypeI, unit.usageSubTypeVc, unit.usageSubTypeId))}</td>
                        <td>${unit.constructionTypeVc}</td>
                        <td>${unit.constructionYearVc}</td>
                        <td>${unit.constructionAgeVc}</td>
                        <td>${unit.ageFactorVc}</td>
                        <td>${unit.areaBefDedFl}</td>
                        <td>${unit.carpetAreaFl}</td>
                        <td>${unit.plotAreaFl}</td>
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
                // Resolve local names for IDs using preloaded maps / local resolver
                div.find('#usageType-' + unit.unitNoVc).text(resolveName(masters.unitUsageTypes, unit.usageTypeVc));
                div.find('#usageSubType-' + unit.unitNoVc).text(resolveName(masters.unitSubUsageTypes, unit.usageSubTypeVc));
                div.find('#occupantStatus-' + unit.unitNoVc).text(resolveName(masters.occupantStatus, unit.occupantStatusI));
            });
        } else {
            console.warn('No units available for this property');
        }
    }

    // This will help group properties according to there final property number so that we will get units in one calculationsheet
    // Helper to extract a numeric key from final property no for true numeric ordering
    function _finalPropNumericKey(val) {
        const s = (val || '').toString();
        // Prefer leading number (e.g., "56/12" -> 56); fallback to first numeric group
        const lead = s.match(/^\d+/);
        if (lead) return parseInt(lead[0], 10);
        const any = s.match(/\d+/);
        return any ? parseInt(any[0], 10) : Number.POSITIVE_INFINITY;
    }

    function groupByPropertyNumber(properties) {
        const grouped = {};
        properties.forEach(property => {
            const key = property.pdFinalpropnoVc;
            if (!grouped[key]) {
                grouped[key] = { ...property, unitDetails: [] };
            }
            // Append unit details to the property entry in the grouped object
            grouped[key].unitDetails.push(...(property.unitDetails || []));
        });
        // Sort units numerically within each property
        Object.keys(grouped).forEach(key => {
            grouped[key].unitDetails.sort((a, b) => (parseInt(a.unitNoVc, 10) || 0) - (parseInt(b.unitNoVc, 10) || 0));
        });
        // Convert to array and sort properties by numeric value of final property number
        const arr = Object.values(grouped);
        arr.sort((a, b) => {
            const ak = _finalPropNumericKey(a.pdFinalpropnoVc);
            const bk = _finalPropNumericKey(b.pdFinalpropnoVc);
            if (ak !== bk) return ak - bk;
            // Secondary: localeCompare with numeric to stabilize equal numeric keys
            return (a.pdFinalpropnoVc || '').toString()
                .localeCompare((b.pdFinalpropnoVc || '').toString(), undefined, { numeric: true, sensitivity: 'base' });
        });
        return arr;
    }

    // ----------- Sorting of already-rendered reports -----------
    function _finalPropKeyFromText(text) {
        const s = (text || '').toString();
        const digits = s.replace(/\D/g, '');
        return digits ? parseInt(digits, 10) : Number.POSITIVE_INFINITY;
    }

    // ----------- Chunked Printing Script -----------
    // Add a button to trigger chunked printing
    $(document).ready(function () {
      $('body').prepend(`<button id="printAllReports">🖨 Print Reports</button>`);

      $('#printAllReports').on('click', function () {
        printReportsInChunks(100); // 100 reports per print job
      });
    });

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
    });
    function printReportsInChunks(chunkSize = 200) {
      const reports = document.querySelectorAll('.report-wrapper');
      const totalReports = reports.length;
      let currentIndex = 0;
      let batchNo = 1;

       function printNextChunk() {
          if (currentIndex >= totalReports) {
            alert("✅ All reports printed.");
            return;
          }

          const nextIndex = Math.min(currentIndex + chunkSize, totalReports);
          let htmlContent = '';

          for (let i = currentIndex; i < nextIndex; i++) {
            htmlContent += `
              <div style="page-break-after: always;">
                ${reports[i].outerHTML}
              </div>`;
          }

          const win = window.open('', '_blank');
          win.document.write(`
            <html>
              <head>
                <title>Print Batch ${batchNo}</title>
                <link rel="stylesheet" href="/3GBatchCalculationReport/CSS/styles.css">
                <style>@page { size: A3 landscape; margin: 10mm; }</style>
              </head>
              <body>${htmlContent}</body>
            </html>
          `);
          win.document.close();

          win.onload = () => {
            win.focus();
            win.print();

            let advanced = false;
            const advance = () => {
              if (advanced) return;
              advanced = true;
              try { win.close(); } catch {}
              currentIndex = nextIndex;
              batchNo++;
              printNextChunk(); // 🔁 immediately go to next batch
            };

            win.onafterprint = advance;

            // last fallback if onafterprint never fires (PDF save case)
            setTimeout(advance, 10000);
          };
       }

        printNextChunk();
    }


  function setTextTargets(scope, selectors, value) {
    (selectors || []).forEach(sel => {
      const $els = scope.find(sel);
      if ($els && $els.length) { $els.text(value); }
    });
  }
  function getFirstDefined() {
    for (let i = 0; i < arguments.length; i++) {
      const v = arguments[i];
      if (v !== undefined && v !== null && v !== '') return v;
    }
    return undefined;
  }

  // ------- Master data helpers (bulk preload + resolvers) -------
  function resolveOccupantStatus(val) {
    return (val === '1' || val === 1) ? 'Owner' : (val ? 'Tenant' : '-');
  }

  function resolveName(map, id) {
    if (!map || id === null || id === undefined || id === '') return '-';
    // Try numeric id first, then string keys
    const idNum = Number(id);
    if (!Number.isNaN(idNum) && map.has(idNum)) return map.get(idNum) || '-';
    if (map.has(String(id))) return map.get(String(id)) || '-';
    if (map.has(id)) return map.get(id) || '-';
    return '-';
  }

  function preloadMasters() {
    // Endpoints inspired by MasterWebController
    const endpoints = {
      propertyTypes: '/3g/propertytypes',
      propertySubtypes: '/3g/propertysubtypes',
      propertyUsageTypes: '/3g/propertyusagetypes',
      propertySubUsageTypes: '/3g/getSubUsageTypes',
      buildingTypes: '/3g/getBuildingTypes',
      buildingSubTypes: '/3g/getAllBuildingSubTypes',
      unitUsageTypes: '/3g/getAllUnitUsageTypes',
      unitSubUsageTypes: '/3g/getAllUnitUsageSubTypes',
      occupantStatus: '/3g/occupancyMasters'
    };

    const fetchJson = (url) => fetch(url).then(r => r.ok ? r.json() : []).catch(() => []);
    const toMap = (list) => {
      const map = new Map();
      const idKeys = ['id','idI','typeId','usageTypeId','propertyTypeId','classificationId','value'];
      const nameKeys = [
        // Common shapes used across endpoints
        'localName','localNameVC','localNameVc','name','typeName','usageTypeName','unitTypeName','standardName',
        // Keys returned by unit usage subtype endpoints
        'usm_usagetypell_vc','usm_usagetypeeng_vc','uum_usagetypeeng_vc'
      ];
      (list || []).forEach(item => {
        if (!item || typeof item !== 'object') return;
        let idVal;
        for (const k of idKeys) { if (k in item) { idVal = item[k]; break; } }
        if (idVal === undefined) return;
        let nameVal;
        for (const k of nameKeys) { if (k in item) { nameVal = item[k]; break; } }
        if (!nameVal && 'localname' in item) nameVal = item['localname'];
        map.set(idVal, nameVal || '-');
      });
      return map;
    };

   return Promise.all([
     fetchJson(endpoints.propertyTypes),
     fetchJson(endpoints.propertySubtypes),
     fetchJson(endpoints.propertyUsageTypes),
     fetchJson(endpoints.propertySubUsageTypes),
     fetchJson(endpoints.buildingTypes),
     fetchJson(endpoints.buildingSubTypes),
     fetchJson(endpoints.unitUsageTypes),
     fetchJson(endpoints.unitSubUsageTypes),
     fetchJson(endpoints.occupantStatus)
   ]).then(([pt, pst, put, psut, bt, bst, uut, usut, occ]) => ({
     propertyTypes: toMap(pt),
     propertySubtypes: toMap(pst),
     propertyUsageTypes: toMap(put),
     propertySubUsageTypes: toMap(psut),
     buildingTypes: toMap(bt),
     buildingSubTypes: toMap(bst),
     unitUsageTypes: toMap(uut),
     unitSubUsageTypes: toMap(usut),
     occupantStatus: toMap(occ)   // ✅ preload map
   }));
  }

  // Ensure print CSS exists to print only a given chunk
  function ensurePrintCss() {
    if (document.getElementById('chunk-print-style')) return;
    const css = `@media print {\n  body * { visibility: hidden !important; }\n  .print-chunk, .print-chunk * { visibility: visible !important; }\n}`;
    const style = document.createElement('style');
    style.id = 'chunk-print-style';
    style.type = 'text/css';
    style.appendChild(document.createTextNode(css));
    document.head.appendChild(style);
  }
  function formatYearRange(date, useDevanagari = true) {
        if (!date || date === '-') return '-';
        const year = parseInt(date.split('-')[0]);
        const startYear = year;
        const endYear = year + 3;
        const startYearL = startYear + 1;
        const endYearL = endYear + 1;
        const toText = useDevanagari ? convertToDevanagari : (n) => n;
        const separator = useDevanagari ? 'ते' : 'To';
        return `${toText(startYear.toString())}-${toText(startYearL.toString())} ${separator} ${toText(endYear.toString())}-${toText(endYearL.toString())}`;
  }

    const _nameCache = new Map();
    function fetchNameById(url, id, callback) {
      if (id === null || id === undefined || id === '') { callback('-'); return; }
      const key = url + '::' + id;
      if (_nameCache.has(key)) { callback(_nameCache.get(key)); return; }
      $.ajax({
        url: url + '/' + id,
        type: 'GET',
        success: function(response) {
          if (response && (response.name || response.localName || response.localNameVC)) {
            const val = response.name || response.localName || response.localNameVC;
            _nameCache.set(key, val);
            callback(val);
          } else {
            callback('-');
          }
        },
        error: function() { callback('-'); }
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

    function convertToDevanagari(numberString) {
        const devanagariDigits = ['०', '१', '२', '३', '४', '५', '६', '७', '८', '९'];
        return numberString.replace(/[0-9]/g, (digit) => devanagariDigits[digit]);
    }
