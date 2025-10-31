
// =============================== ??? Council Details & Year Range ===============================
$(document).on('click', '#printBtn', function () {

  window.print();
});

let usageTypeMap = {};
const usageTypePromise = fetch('/3g/propertyusagetypes')
  .then(res => res.json())
  .then(arr => {
    arr.forEach(it => {
      const id = String(it.id);
      usageTypeMap[id] = it.localName || ''; // ✅ store only local name
    });
    console.log("✅ Loaded Local Usage Types:", usageTypeMap);
  })
  .catch(err => console.error("❌ Failed to load usage types:", err));

$(document).ready(function () {

  // ?? Council Details
  $.ajax({
    url: '/3g/getCouncilDetails',
    type: 'GET',
    success: function (data) {
      if (data && data.length > 0) {
        const c = data[0];
        $('.councilLocalName, .councilLocalNameHeader').text(c.localName);
        if (c.imageBase64) {
          $('.councilLogo').attr('src', 'data:image/png;base64,' + c.imageBase64);
          $('.standardSiteNameVC').text(c.standardSiteNameVC);
          $('.localSiteNameVC').text(c.localSiteNameVC);
          $('.standardDistrictNameVC').text(c.standardDistrictNameVC);
          $('.localDistrictNameVC').text(c.localDistrictNameVC);
          // Chief officer signature (Base64 -> data URL)
          try {
            const b64 = String(c.chiefOfficerSignBase64 || '').trim();
            if (b64) {
              const isData = /^data:image\/(png|jpeg);base64,/i.test(b64);
              const isPng = b64.startsWith('iVBORw0K');
              const isJpeg = b64.startsWith('/9j/');
              window.chiefOfficerSignUrl = isData ? b64 : `data:${isPng ? 'image/png' : (isJpeg ? 'image/jpeg' : 'image/png')};base64,${b64}`;
              $('.officerSignature').attr('src', window.chiefOfficerSignUrl);
            }
          } catch (e) { console.warn('chiefOfficerSign parsing failed', e); }
        }
      }
    },
    error: function () { console.error("? Failed to fetch council details."); }
  });

  // ?? Assessment Year Range
  fetch('/3g/getAllAssessmentDates')
    .then(res => res.json())
    .then(data => {
      const currentAssessmentDate = data[0].currentassessmentdate;
      const year = parseInt(currentAssessmentDate.split('-')[0]);
      const startYearL = year + 1;
      const endYearL = year + 4;
      const rangeText = `${convertToDevanagari(year)}-${convertToDevanagari(startYearL)} ?? ${convertToDevanagari(year + 3)}-${convertToDevanagari(endYearL)}`;
      $('.yearRange').text(rangeText);
    })
    .catch(err => console.error('Error fetching assessment dates:', err));

  // If URL has ?newPropertyNo, render single tax bill and skip ward rendering
  try {
    const qp = new URLSearchParams(window.location.search);
    const np = qp.get('newPropertyNo');
    if (np) {
      fetchSingleTaxBill(np);
      return; // stop further ward-based flow
    }
  } catch (e) { console.warn('query param parse failed', e); }

  // ?? Ward number from URL
  const path = window.location.pathname; // e.g. /taxBill/7
  const wardMatch = path.match(/\/taxBill\/(\d+)/);
  const wardNo = wardMatch ? wardMatch[1] : null;

  if (wardNo) {
    fetchWardTaxBills(wardNo);
  } else {
    alert("?? No ward number found in URL.");
  }
});

// =============================== ?? Convert Digits ===============================
function convertToDevanagari(num) {
  const digits = ['?','?','?','?','?','?','?','?','?','?'];
  return num.toString().replace(/\d/g, d => digits[d]);
}
function convertToDevanagariYear(yearStr) {
  if (!yearStr) return '';
  const parts = yearStr.split('-');
  if (parts.length === 2) {
     return `वर्ष ${parts[0]} ते ${parts[1]}`;
  }
  return `वर्ष ${yearStr}`;
}


// =============================== ??? Fetch Ward Tax Bills ===============================
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

function fetchSingleTaxBill(newPropertyNo) {
  $.ajax({
    url: `/3g/taxBills/single?newPropertyNo=${encodeURIComponent(newPropertyNo)}`,
    type: 'GET',
    success: function (dto) {
      if (!dto) {
        alert("No tax bill found for property " + newPropertyNo);
        return;
      }
      renderBatchPreview([dto]);
    },
    error: function () {
      alert("Error fetching tax bill for property " + newPropertyNo);
    }
  });
}

async function renderBatchPreview(dataList) {
  const $container = $('#main-report-section');
  $container.empty();

  for (const dto of dataList) {
    const $page = $('.page-container.template').clone().removeClass('template').show();
    if (window.chiefOfficerSignUrl) {
      $page.find('.officerSignature').attr('src', window.chiefOfficerSignUrl);
    }

    // ?? Property info
    $page.find('.pdOwnernameVc').text(dto.pdOwnernameVc || '');
    $page.find('.pdWardI').text(dto.pdWardI || '');
    $page.find('.pdZoneI').text(dto.pdZoneI || '');
    $page.find('.pdSurypropnoVc').text(dto.pdSurypropnoVc || '');
    $page.find('.pdNewpropertynoVc').text(dto.pdNewpropertynoVc || '');
    $page.find('.pdOldpropnoVc').text(dto.pdOldpropnoVc || '');
    $page.find('.pdFinalpropnoVc').text(dto.pdFinalpropnoVc || '');
    $page.find('.pdPropertyaddressVc').text(dto.pdPropertyaddressVc || '');
    $page.find('.pdAssesareaF').text(dto.pdAssesareaF || '');
    (function setUsage() {
      const id = dto.pdUsageType != null ? String(dto.pdUsageType) : '';
      if (!id) return;

      const setLocalName = (map) => {
        const name = map[id];
        return name || id; // fallback to id if not found
      };

      if (usageTypeMap && usageTypeMap[id]) {
        $page.find('.pdUsageTypeName').text(setLocalName(usageTypeMap));
      } else {
        usageTypePromise.then(() => {
          $page.find('.pdUsageTypeName').text(setLocalName(usageTypeMap));
        });
      }
    })();

    // ?? Proposed Ratable Values
    const rv = dto.proposedRatableValueDetailsDto || {};
    Object.entries(rv).forEach(([key, val]) => {
      $page.find(`.${key}`).text(val || '0');
    });

    // ?? Build arrears + current tax table
    buildYearWiseTaxTable($page, dto);

    $container.append($page);
  }

  console.log(`? Rendered ${dataList.length} tax bills`);
}

// =============================== ?? Build Year-wise Tax Table ===============================
function buildYearWiseTaxTable($page, dto) {
  const arrearsMap = dto.arrearsYearWiseMap || {};
  const currentTaxMap = dto.currentTaxMap || {};
  const totalTaxMap = dto.totalTaxMap || {};
  const arrearsYears = Object.keys(arrearsMap);

  // If there are no arrears ? show only current & total columns
  const showArrears = arrearsYears.length > 0;

  $.get('/3g/reportTaxConfigs?template=TAX_BILL', function (configs) {
    if (!configs || configs.length === 0) return;

    configs.sort((a, b) => a.sequenceI - b.sequenceI);

    let html = `
      <table class="table table-bordered" style="width:100%;font-size:12px;margin-top:10px;border-collapse:collapse;">
        <thead style="font-weight:600;">
          <tr>
            <th style="background:#f4cccc;">कराचे तपशील</th>`;

    if (showArrears) {
      arrearsYears.forEach(y => {
        html += `
          <th style="padding:0;">
            <div style="background:#f4cccc; font-weight:600; padding:2px 4px;">थकबाकी रक्कम</div>
            <div style="background:#c9daf8; padding:2px 4px;">${convertToDevanagariYear(y)}</div>
          </th>`;
      });
    }

    html += `
        <th style="background:#f4cccc;">चालू मागणी</th>
        <th style="background:#f4cccc;">एकूण रक्कम</th>
      </tr>
    </thead>
    <tbody>`;
    // Totals
    const totalByYear = {};
    let totalCurrent = 0;
    let totalOverall = 0;

    // === Rows for each tax ===
    configs.forEach(cfg => {
      const key = cfg.taxKeyL;
      const name = cfg.localNameVc;
      html += `<tr><td>${name}</td>`;

      let arrearTotal = 0;

      if (showArrears) {
        arrearsYears.forEach(y => {
          const val = parseFloat(arrearsMap[y]?.[key] || 0);
          html += `<td class="t-c">${val.toFixed(2)}</td>`;
          arrearTotal += val;
          totalByYear[y] = (totalByYear[y] || 0) + val;
        });
      }

      const current = parseFloat(currentTaxMap[key] || 0);
      const overall = parseFloat(totalTaxMap[key] || arrearTotal + current);

      html += `<td class="t-c">${current.toFixed(2)}</td>`;
      html += `<td class="t-c"><b>${overall.toFixed(2)}</b></td></tr>`;

      totalCurrent += current;
      totalOverall += overall;
    });

    // === Total Row ===
    html += `<tr style="background:#fcd2d2;font-weight:700;"><td>एकूण कर</td>`;
    if (showArrears) {
      arrearsYears.forEach(y => {
        html += `<td class="t-c">${(totalByYear[y] || 0).toFixed(2)}</td>`;
      });
    }
    html += `<td class="t-c">${totalCurrent.toFixed(2)}</td>`;
    html += `<td class="t-c">${totalOverall.toFixed(2)}</td></tr></tbody></table>`;

    // Inject below the main tax section
    $page.find('.taxBillTable').after(html);
  });
}


