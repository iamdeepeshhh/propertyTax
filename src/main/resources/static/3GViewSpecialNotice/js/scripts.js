// Show council details
$(document).ready(function () {
  $.ajax({
    url: '/3g/getCouncilDetails',
    type: 'GET',
    success: function (data) {
      if (data && data.length > 0) {
        const councilDetails = data[0];
        $('.councilName').text(councilDetails.localName);
        if (councilDetails.imageBase64) {
          $('.councilLogo').attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
          $('.standardSiteNameVC').text(councilDetails.standardSiteNameVC);
          $('.localSiteNameVC').text(councilDetails.localSiteNameVC);
          $('.standardDistrictNameVC').text(councilDetails.standardDistrictNameVC);
          $('.localDistrictNameVC').text(councilDetails.localDistrictNameVC);
        }
      }
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

let dataList = [];
let currentChunk = 0;
const chunkSize = 100;
let $originalPage = null;

// 2️⃣ Setup button and AJAX call
$(document).ready(function () {
  $('body').prepend(`<button id="startPrint">🖨 Print Special Notice</button>`);

  $('#startPrint').on('click', function () {
    $('#startPrint').hide();

    const pathSegments = window.location.pathname.split('/');
    const wardNo = pathSegments.find(segment => /^\d+$/.test(segment));

    if (!wardNo) {
      alert("Ward number not found in URL.");
      $('#startPrint').show();
      return;
    }

    $.ajax({
      url: `/3g/specialNotices/${wardNo}`,
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

        renderAndPrintChunk();  // ✅ Called here
      },
      error: function () {
        alert("Error fetching data.");
        $('#startPrint').show();
      }
    });
  });
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
    $page.find('.pdWardI').text(dto.pdWardI || '');
    $page.find('.pdZoneI').text(dto.pdZoneI || '');
    $page.find('.pdSurypropnoVc').text(dto.pdSurypropnoVc || '');
    $page.find('.pdNewpropertynoVc').text(dto.pdNewpropertynoVc || '');
    $page.find('.pdOldpropnoVc').text(dto.pdOldpropnoVc || '');
    $page.find('.pdPropertytypeAndSubtype').text((dto.pdPropertytypeI || '') + ' → ' + (dto.pdPropertysubtypeI || ''));
    $page.find('.pdPlotvalueF').text(dto.pdPlotvalueF || '');
    $page.find('.fireTaxFl').text(dto.consolidatedTaxes?.fireTaxFl || '');
    $page.find('.currentAssessmentDateDt').text(dto.currentAssessmentDateDt || '');

    // 🏘️ Property Details Table
    $page.find('.pdFinalpropnoVc').text(dto.pdFinalpropnoVc || '');
    $page.find('.pdUsagetypeAndSubtype').text((dto.pdUsagetypeI || '') + ' → ' + (dto.pdUsagesubtypeI || ''));
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
    $page.find('.pdBuildingvalueI').text(dto.pdBuildingvalueI || '');
    $page.find('.mobileTowerFl').text(dto.mobileTowerFl || '');
    $page.find('.electricSubstationFl').text(dto.electricSubstationFl || '');

    // 💰 Consolidated Tax Table
    const tax = dto.consolidatedTaxes || {};
    $page.find('.propertyTaxFl').text(tax.propertyTaxFl || '');
    $page.find('.educationTaxResid').text(dto.educationTaxResid || '');
    $page.find('.educationTaxComm').text(dto.educationTaxComm || '');
    $page.find('.educationTaxTotalFl').text(tax.educationTaxTotalFl || '');
    $page.find('.egcFl').text(tax.egcFl || '');
    $page.find('.treeTaxFl').text(tax.treeTaxFl || '');
    $page.find('.cleannessTaxFl').text(tax.cleannessTaxFl || '');
    $page.find('.lightTaxFl').text(tax.lightTaxFl || '');
    $page.find('.fireTaxFl').text(tax.fireTaxFl || '');

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


//Function using the four AIP by Himanshu

// 1. Property Types
getIdValueList('/propertytypes', 'englishname').then(data => {
    console.log("Property Types:", data);
});

// 2. Property Subtypes
getIdValueList('/propertysubtypes', 'marathiname').then(data => {
    console.log("Property Subtypes:", data);
});

// 3. Property Usage Types
getIdValueList('/propertyusagetypes', 'englishname').then(data => {
    console.log("Usage Types:", data);
});

// 4. Property Sub Usage Types
getIdValueList('/getSubUsageTypes', 'marathiname').then(data => {
    console.log("Sub Usage Types:", data);
});


async function getIdValueList(apiUrl, nameKey = 'englishname') {
    try {
        const response = await fetch(apiUrl); // 🔗 Makes connection here
        if (!response.ok) throw new Error(`Failed to fetch from ${apiUrl}`);

        const data = await response.json(); // 🔗 Parses API response

        return data.map(item => ({
            id: Number(item.value),
            value: item[nameKey]
        }));
    } catch (error) {
        console.error(`Error fetching from ${apiUrl}:`, error);
        return [];
    }
}


