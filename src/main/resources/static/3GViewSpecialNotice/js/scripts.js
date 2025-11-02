// Show council details
$(document).ready(function () {
  $.ajax({
    url: '/3g/getCouncilDetails',
    type: 'GET',
    success: function (data) {
      if (data && data.length > 0) {
        const council = data[0];

        // Council Texts
        $('.councilLocalName').text(council.localName || '');
        $('.standardDistrictNameVC').text(council.standardDistrictNameVC || '');
        $('.localDistrictNameVC').text(council.localDistrictNameVC || '');

        // 🖼️ Top Left (always shown)
        if (council.imageBase64 && council.imageBase64.trim() !== '') {
          $('.councilLogoTopLeft').attr('src', 'data:image/png;base64,' + council.imageBase64).show();
          $('.councilLogoBottomLeft').attr('src', 'data:image/png;base64,' + council.imageBase64).show();
        }

        // 🖼️ Top Right (optional)
        if (council.image2Base64 && council.image2Base64.trim() !== '') {
          $('.councilLogoTopRight').attr('src', 'data:image/png;base64,' + council.image2Base64).show();
          $('.councilLogoBottomRight').attr('src', 'data:image/png;base64,' + council.image2Base64).show();
        } else {
          $('.councilLogoTopRight').hide();
        }

        // ✅ Center title if both right-side logos are missing
        $('.logo-title').each(function () {
          const rightLogoVisible = $(this).find('.councilLogoTopRight:visible, .councilLogoBottomRight:visible').length > 0;
          if (!rightLogoVisible) $(this).css('justify-content', 'flex-start');
          else $(this).css('justify-content', 'space-between');
        });
      }
    },
    error: function (err) {
      console.error('Error fetching council details:', err);
      $('.councilLogo').hide();
    }
  });
});


// Show year range
let globalYearRange = '';
document.addEventListener('DOMContentLoaded', () => {
  fetch('/3g/getAllAssessmentDates', {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' }
  })
    .then(response => {
      if (!response.ok) throw new Error('Network error');
      return response.json();
    })
    .then(data => {
      const currentAssessmentDate = data[0].currentassessmentdate;
      globalYearRange = formatYearRange(currentAssessmentDate);
      $('.yearRange').text(globalYearRange);
    })
    .catch(error => console.error('Error:', error));
});

function formatYearRange(date) {
  const year = parseInt(date.split('-')[0]);
  const startYearL = year + 1;
  const endYearL = year + 4;
  return `${convertToDevanagari(year.toString())}-${convertToDevanagari(startYearL.toString())} ते ${convertToDevanagari(year + 3)}-${convertToDevanagari(endYearL)}`;
}

function convertToDevanagari(numberString) {
  if (numberString == null) return '';
  const digits = ['०', '१', '२', '३', '४', '५', '६', '७', '८', '९'];
  return numberString.toString().replace(/\d/g, d => digits[d]);
}

// ✅ Reusable function to build report tax table dynamically
function buildReportTaxTable(tableSelector, templateName = "SPECIAL_NOTICE") {
    $.get('/3g/reportTaxConfigs?template=' + templateName, function(configs) {
        if (!configs || configs.length === 0) {
            console.warn('No tax configs received');
            return;
        }

        const table = $(tableSelector);
        const rows = table.find('tr');
        if (rows.lXength > 1) rows.slice(1).remove();

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

let dataList = [];
let currentChunk = 0;
const chunkSize = 100;
let $originalPage = null;

// 2️⃣ Setup button and AJAX call
$(document).ready(function () {
  const path = window.location.pathname; // e.g. /specialNotice/1
  const wardMatch = path.match(/\/specialNotice\/(\d+)/);
  const wardNo = wardMatch ? wardMatch[1] : null;
  const newPropertyNo = new URLSearchParams(window.location.search).get("newPropertyNo");

  if (newPropertyNo) {
    // 👉 Directly render single property special notice
     $('body').prepend(`
        <button id="printBtn"
          style="
            position: fixed;
            top: 20px;
            left: 30px;
            padding: 8px 14px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: 600;
            z-index: 1000;
          ">
          🖨️ Print
        </button>
      `);

      // ✅ Attach event
      $('#printBtn').on('click', function () {
        $(this).hide();           // Hide before print
        window.print();           // Trigger print
        setTimeout(() => $(this).show(), 1000); // Re-show after print
      });

  } else {
    // 👉 Ward case: Add button for printing
    $('body').prepend(`<button id="startPrint">🖨 Print Special Notice</button>`);

    $('#startPrint').on('click', function () {
      $('#startPrint').hide();

      $.ajax({
        url: `/3g/specialNotices?wardNo=${wardNo}`,
        method: 'GET',
        success: function (data) {
          if (!data || data.length === 0) {
            alert("No data found.");
            $('#startPrint').show();
            return;
          }

          dataList = data;
          currentChunk = 0;
          $originalPage = $('.page-container').detach();

          renderAndPrintChunk();  // ✅ Ward-wise printing
        },
        error: function () {
          alert("Error fetching data.");
          $('#startPrint').show();
        }
      });
    });
  }
});

// ✅ Add print button event
function renderAndPrintChunk() {
  const chunkData = dataList.slice(currentChunk * chunkSize, (currentChunk + 1) * chunkSize);
  if (chunkData.length === 0) {
    alert("✅ All chunks printed.");
    $('#startPrint').show();
    return;
  }

  const $reportContainer = $('<div id="main-report-section"></div>');

  chunkData.forEach((dto) => {
    const $page = $originalPage.clone(false, false);

    // 🖼️ Images
    if (dto.pdHouseplan2T) {
      $page.find('#pdHouseplan2T').attr('src', 'data:image/jpeg;base64,' + dto.pdHouseplan2T);
    }
    if (dto.pdPropimageT) {
      $page.find('#pdPropimageT').attr('src', 'data:image/jpeg;base64,' + dto.pdPropimageT);
    }


    // 📋 Summary Table
    $page.find('.pdNoticenoVc').text(dto.pdNoticenoVc || '');
    $page.find('.pdOwnernameVc').text(dto.pdOwnernameVc || '');
    $page.find('.pdOccupinameF').text(dto.pdOccupinameF || '');
    $page.find('.pdWardI').text(dto.pdWardI || '');
    $page.find('.pdZoneI').text(dto.pdZoneI || '');
    $page.find('.pdSurypropnoVc').text(dto.pdSurypropnoVc || '');
    $page.find('.pdNewpropertynoVc').text(dto.pdNewpropertynoVc || '');
    $page.find('.pdOldpropnoVc').text(dto.pdOldpropnoVc || '');
    $page.find('.pdPropertytypeAndSubtype').text((propertyTypeMap[dto.pdPropertytypeI] || '') + ' → ' + (propertySubtypeMap[dto.pdPropertysubtypeI] || ''));
    $page.find('.pdPlotvalueF').text(dto.pdPlotvalueF || '');
    $page.find('.fireTaxFl').text(dto.consolidatedTaxes?.fireTaxFl || '');
    $page.find('.currentAssessmentDateDt').text(dto.currentAssessmentDateDt || '');               //change by anand                             //change by anand
                           //Change by anand


    // 🏘️ Property Details Table
    $page.find('.pdFinalpropnoVc').text(dto.pdFinalpropnoVc || '');
    $page.find('.pdUsagetypeAndSubtype').text( (usageTypeMap[dto.pdUsagetypeI] || '') + ' → ' + (subUsageTypeMap[dto.pdUsagesubtypeI] || ''));
    $page.find('.pdAssesareaF').text(dto.pdAssesareaF || '');



    // 🏷️ Ratable Value Table
    const ratable = dto.proposedRatableValues || {};
    $page.find('.residentialFl').text(ratable.residentialFl || '');
    $page.find('.commercialFl').text(ratable.commercialFl || '');
    $page.find('.religiousFl').text(dto.religiousFl || '');
    $page.find('.residentialOpenPlotFl').text(dto.residentialOpenPlotFl || '');
    $page.find('.commercialOpenPlotFl').text(dto.commercialOpenPlotFl || '');
    $page.find('.religiousOpenPlotFl').text(dto.religiousOpenPlotFl || '');
    $page.find('.educationalInstituteOpenPlotFl').text(dto.educationalInstituteOpenPlotFl || '');
    $page.find('.governmentOpenPlotFl').text(dto.governmentOpenPlotFl || '');
    $page.find('.industrialOpenPlotFl').text(dto.industrialOpenPlotFl || '');
    $page.find('.educationAndLegalInstituteOpenPlotFl').text(dto.educationAndLegalInstituteOpenPlotFl || '');
    $page.find('.governmentFl').text(dto.governmentFl || '');
    $page.find('.industrialFl').text(dto.industrialFl || '');
    $page.find('.mobileTowerFl').text(dto.mobileTowerFl || '');
    $page.find('.electricSubstationFl').text(dto.electricSubstationFl || '');
    $page.find('.aggregateFl').text(ratable.aggregateFl || '');

    // 💰 Consolidated Tax Table
    if (dto.taxKeyValueMap) {
        const taxKeyValueMap = dto.taxKeyValueMap;
        for (let key in taxKeyValueMap) {
            $page.find(`#tax-${key}`).text(taxKeyValueMap[key] != null ? taxKeyValueMap[key] : 0);
        }
    }
    if (dto.consolidatedTaxes) {
        $page.find('#totalTax').text(dto.consolidatedTaxes.totalTaxFl || '0');
    }
    $reportContainer.append($page);
  });

  $('body').append($reportContainer);

  setTimeout(() => {
    window.print();
    setTimeout(() => {
      $('#main-report-section').remove();
      currentChunk++;
      renderAndPrintChunk();
    }, 2000);
  }, 500);
}


let propertyTypeMap = {};
let propertySubtypeMap = {};
let usageTypeMap = {};
let subUsageTypeMap = {};

// 1. Property Types
getIdValueList('/3g/propertytypes', 'localName').then(data => {
    propertyTypeMap = Object.fromEntries(data.map(item => [item.id, item.value]));
});

// 2. Property Subtypes
getIdValueList('/3g/propertysubtypes', 'localName').then(data => {
    propertySubtypeMap = Object.fromEntries(data.map(item => [item.id, item.value]));
});

getIdValueList('/3g/propertyusagetypes', 'localName').then(data => {
    usageTypeMap = Object.fromEntries(data.map(item => [item.id, item.value]));
});

getIdValueList('/3g/getSubUsageTypes', 'localName').then(data => {
    subUsageTypeMap = Object.fromEntries(data.map(item => [item.id, item.value]));
});



async function getIdValueList(apiUrl, nameKey = 'localName') {
    try {
        const response = await fetch(apiUrl);
        if (!response.ok) throw new Error(`Failed to fetch from ${apiUrl}`);

        const data = await response.json();

        return data.map(item => ({
            id: Number(item.id),
            value: item[nameKey] // default is 'localName', e.g. "मालमत्ता प्रकार १"
        }));
    } catch (error) {
        console.error(`Error fetching from ${apiUrl}:`, error);
        return [];
    }
}

async function renderSingleSpecialNotice(propertyNo) {
  try {
    const response = await fetch(`/3g/specialNotices?newPropertyNo=${propertyNo}`);
    if (!response.ok) throw new Error("Failed to fetch special notice");
    const data = await response.json();

    if (!data || data.length === 0) {
      alert("No special notice found for property: " + propertyNo);
      return;
    }

    const dto = data[0]; // only one expected
    if (!$originalPage) {
      $originalPage = $('.page-container').detach();
    }
    const $page = $originalPage.clone(false, false);
    $('body').addClass('print-preview');
    console.log(dto);
    // images
    if (dto.pdHouseplan2T) $page.find('#pdHouseplan2T').attr('src', 'data:image/jpeg;base64,' + dto.pdHouseplan2T);
    if (dto.pdPropimageT) $page.find('#pdPropimageT').attr('src', 'data:image/jpeg;base64,' + dto.pdPropimageT);

    // summary
    $page.find('.pdNoticenoVc').text(dto.pdNoticenoVc || '');
    $page.find('.pdOwnernameVc').text(dto.pdOwnernameVc || '');
    $page.find('.pdOccupinameF').text(dto.pdOccupinameF || '');
    $page.find('.pdWardI').text(dto.pdWardI || '');
    $page.find('.pdZoneI').text(dto.pdZoneI || '');
    $page.find('.pdSurypropnoVc').text(dto.pdSurypropnoVc || '');
    $page.find('.pdNewpropertynoVc').text(dto.pdNewpropertynoVc || '');
    $page.find('.pdOldpropnoVc').text(dto.pdOldpropnoVc || '');
    $page.find('.pdPropertytypeAndSubtype').text((propertyTypeMap[dto.pdPropertytypeI] || '') + ' → ' + (propertySubtypeMap[dto.pdPropertysubtypeI] || ''));

    // usage
    $page.find('.pdFinalpropnoVc').text(dto.pdFinalpropnoVc || '');
    $page.find('.pdUsagetypeAndSubtype').text((usageTypeMap[dto.pdUsagetypeI] || '') + ' → ' + (subUsageTypeMap[dto.pdUsagesubtypeI] || ''));
    $page.find('.pdAssesareaF').text(dto.pdAssesareaF || '');

    // ratable values
    const ratable = dto.proposedRatableValues || {};
    $page.find('.residentialFl').text(ratable.residentialFl || '');
    $page.find('.commercialFl').text(ratable.commercialFl || '');
    $page.find('.aggregateFl').text(ratable.aggregateFl || '');

    // consolidated taxes
    if (dto.taxKeyValueMap) {
      for (let key in dto.taxKeyValueMap) {
        $page.find(`#tax-${key}`).text(dto.taxKeyValueMap[key] != null ? dto.taxKeyValueMap[key] : 0);
      }
    }
    if (dto.consolidatedTaxes) {
      $page.find('#totalTax').text(dto.consolidatedTaxes.totalTaxFl || '0');
    }

    $('body').append($('<div class="single-report"></div>').append($page));

  } catch (e) {
    console.error(e);
    alert("Error fetching single special notice");
  }
}
