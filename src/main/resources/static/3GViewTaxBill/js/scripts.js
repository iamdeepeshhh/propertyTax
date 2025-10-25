// =============== 🏛️ Council Details & Year Range =====================\
$(document).on('click', '#printBtn', function () {
  window.print();
});
$(document).ready(function () {
  buildTaxBillTable(".taxBillTable", "TAX_BILL");

  // 🏛 Council Details
  $.ajax({
    url: '/3g/getCouncilDetails',
    type: 'GET',
    success: function (data) {
      if (data && data.length > 0) {
        const c = data[0];
        $('.councilLocalName').text(c.localName);
        $('.councilLocalNameHeader').text(c.localName);
        if (c.imageBase64) {
          $('.councilLogo').attr('src', 'data:image/png;base64,' + c.imageBase64);
          $('.standardSiteNameVC').text(c.standardSiteNameVC);
          $('.localSiteNameVC').text(c.localSiteNameVC);
          $('.standardDistrictNameVC').text(c.standardDistrictNameVC);
          $('.localDistrictNameVC').text(c.localDistrictNameVC);
        }
      }
    },
    error: function () { console.error("❌ Failed to fetch council details."); }
  });

  // 🗓 Assessment Year
  fetch('/3g/getAllAssessmentDates')
    .then(res => res.json())
    .then(data => {
      const currentAssessmentDate = data[0].currentassessmentdate;
      const year = parseInt(currentAssessmentDate.split('-')[0]);
      const startYearL = year + 1;
      const endYearL = year + 4;
      const rangeText = `${convertToDevanagari(year)}-${convertToDevanagari(startYearL)} ते ${convertToDevanagari(year + 3)}-${convertToDevanagari(endYearL)}`;
      $('.yearRange').text(rangeText);
    })
    .catch(err => console.error('Error fetching assessment dates:', err));

  // 🧾 Load Batch Data (Ward)
  const path = window.location.pathname; // e.g. /taxBill/7
  const wardMatch = path.match(/\/taxBill\/(\d+)/);
  const wardNo = wardMatch ? wardMatch[1] : null;

  if (wardNo) {
    fetchWardTaxBills(wardNo);
  } else {
    alert("⚠️ No ward number found in URL.");
  }
});

function convertToDevanagari(num) {
  const digits = ['०', '१', '२', '३', '४', '५', '६', '७', '८', '९'];
  return num.toString().replace(/\d/g, d => digits[d]);
}

// =============== 🧾 Dynamic Tax Table Builder =====================
function buildTaxBillTable(selector, template = "TAX_BILL") {
  $.get('/3g/reportTaxConfigs?template=' + template, function (configs) {
    if (!configs || configs.length === 0) return;
    const $table = $(selector);
    const $tbody = $table.find('tbody');
    $tbody.empty();
    configs.sort((a, b) => a.sequenceI - b.sequenceI);
    configs.forEach(cfg => {
      const row = `
        <tr>
          <td>${cfg.localNameVc}</td>
          <td id="taxArrear-${cfg.taxKeyL}" class="t-c">0</td>
          <td id="taxCurrent-${cfg.taxKeyL}" class="t-c">0</td>
          <td id="taxTotal-${cfg.taxKeyL}" class="t-c">0</td>
        </tr>`;
      $tbody.append(row);
    });
    $tbody.append(`
      <tr class="pinklight">
        <td><b>एकूण कर</b></td>
        <td id="totalArrear" class="t-c"><b>0</b></td>
        <td id="totalCurrent" class="t-c"><b>0</b></td>
        <td id="totalTax" class="t-c"><b>0</b></td>
      </tr>`);
  });
}

// =============== 🏘️ Ward-wise Batch Data Fetch =====================
function fetchWardTaxBills(wardNo) {
  $.ajax({
    url: `/3g/taxBills?wardNo=${wardNo}`,
    type: 'GET',
    success: function (data) {
      if (!data || data.length === 0) {
        alert("No tax bills found for ward " + wardNo);
        return;
      }
      renderBatchPreview(data);
    },
    error: function () {
      alert("Error fetching tax bills.");
    }
  });
}

// =============== 🧾 Render Batch Tax Bill Preview =====================
function renderBatchPreview(dataList) {
  const $container = $('#main-report-section');
  $container.empty();

  dataList.forEach(dto => {
    const $page = $('.page-container.template').clone().removeClass('template').show();

    // 🏠 Property info
    $page.find('.pdOwnernameVc').text(dto.pdOwnernameVc || '');
    $page.find('.pdWardI').text(dto.pdWardI || '');
    $page.find('.pdZoneI').text(dto.pdZoneI || '');
    $page.find('.pdSurypropnoVc').text(dto.pdSurypropnoVc || '');
    $page.find('.pdNewpropertynoVc').text(dto.pdNewpropertynoVc || '');
    $page.find('.pdOldpropnoVc').text(dto.pdOldpropnoVc || '');
    $page.find('.pdFinalpropnoVc').text(dto.pdFinalpropnoVc || '');
    $page.find('.pdPropertyaddressVc').text(dto.pdPropertyaddressVc || '');
    $page.find('.pdAssesareaF').text(dto.pdAssesareaF || '');

    // 💰 Proposed Ratable Values
    const rv = dto.proposedRatableValues || {};
    $page.find('.residentialFl').text(rv.residentialFl || '');
    $page.find('.commercialFl').text(rv.commercialFl || '');
    $page.find('.religiousFl').text(rv.religiousFl || '');
    $page.find('.industrialFl').text(rv.industrialFl || '');
    $page.find('.governmentFl').text(rv.governmentFl || '');
    $page.find('.mobileTowerFl').text(rv.mobileTowerFl || '');
    $page.find('.electricSubstationFl').text(rv.electricSubstationFl || '');
    $page.find('.residentialOpenPlotFl').text(rv.residentialOpenPlotFl || '');
    $page.find('.commercialOpenPlotFl').text(rv.commercialOpenPlotFl || '');
    $page.find('.religiousOpenPlotFl').text(rv.religiousOpenPlotFl || '');
    $page.find('.educationalInstituteOpenPlotFl').text(rv.educationalInstituteOpenPlotFl || '');
    $page.find('.governmentOpenPlotFl').text(rv.governmentOpenPlotFl || '');
    $page.find('.industrialOpenPlotFl').text(rv.industrialOpenPlotFl || '');
    $page.find('.educationAndLegalInstituteOpenPlotFl').text(rv.educationAndLegalInstituteOpenPlotFl || '');
    $page.find('.aggregateFl').text(rv.aggregateFl || '');


    // 🧾 Tax Key Values
    if (dto.taxKeyValueMap) {
      for (let key in dto.taxKeyValueMap) {
        $page.find(`#taxCurrent-${key}`).text(dto.taxKeyValueMap[key] != null ? dto.taxKeyValueMap[key] : 0);
        $page.find(`#taxTotal-${key}`).text(dto.taxKeyValueMap[key] != null ? dto.taxKeyValueMap[key] : 0);
      }
    }
    if (dto.consolidatedTaxes) {
      $page.find('#totalCurrent').text(dto.consolidatedTaxes.totalTaxFl || '0');
      $page.find('#totalTax').text(dto.consolidatedTaxes.totalTaxFl || '0');
    }

    $container.append($page);
  });

  console.log(`✅ Loaded ${dataList.length} tax bills for preview`);
}
