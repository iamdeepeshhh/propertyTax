$(document).ready(function () {
  // ──────────────────────────────
  // 1️⃣  COUNCIL DETAILS
  // ──────────────────────────────
  $.ajax({
    url: '/3g/getCouncilDetails',
    type: 'GET',
    success: function (data) {
      if (data && data.length > 0) {
        const council = data[0];
        window.councilDetails = council; // cache globally

        $('.councilName').text(
          (council.localName || '') + ' / ' + (council.standardName || '')
        );

        if (council.imageBase64)
          $('.councilLogoLeft').attr(
            'src',
            'data:image/png;base64,' + council.imageBase64
          );

        if (council.image2Base64 && council.image2Base64.trim() !== '')
          $('.councilLogoRight')
            .attr('src', 'data:image/png;base64,' + council.image2Base64)
            .show();
        else $('.councilLogoRight').hide();

        $('.localDistrictNameVC').text(council.localDistrictNameVC || '');
      }
    },
  });

  // ──────────────────────────────
  // 2️⃣  DETECT MODE (ward vs property)
  // ──────────────────────────────
  const qp = new URLSearchParams(window.location.search);
  const newPropertyNo = qp.get('newPropertyNo');
  const wardNo = qp.get('wardNo');

  if (wardNo && !newPropertyNo) {
    // Ward mode → multiple pages
    fetch(`/3g/orderSheet/byWard?wardNo=${wardNo}`)
      .then((r) => r.json())
      .then((list) => renderMultipleOrderSheets(list))
      .catch((err) => console.error('Error loading ward order sheets:', err));
  } else if (newPropertyNo) {
    // Single property mode
    fetch(`/3g/orderSheet?newPropertyNo=${newPropertyNo}`)
      .then((r) => r.json())
      .then((dto) => {
        const $root = $('.container').first();
        buildReportTaxTableInRootAsync($root, '#orderSheetBeforeTaxTable', 'ORDER_SHEET')
          .then(() => fillOrderSheet($root, dto));
      })
      .catch((err) => console.error('Error loading single order sheet:', err));
  }
});


// ──────────────────────────────
// 3️⃣  MULTI-PAGE (WARD) MODE
// ──────────────────────────────
async function renderMultipleOrderSheets(list) {
  if (!list || list.length === 0) return;
  const $template = $('.container').first();

  for (let idx = 0; idx < list.length; idx++) {
    const dto = list[idx];
    const $root = idx === 0 ? $template : $template.clone(true);
    await buildReportTaxTableInRootAsync($root, '#orderSheetBeforeTaxTable', 'ORDER_SHEET');
    fillOrderSheet($root, dto);

    if (idx > 0) {
      $root.css('page-break-before', 'always');
      $('body').append($root);
    }
  }
}


// ──────────────────────────────
// 4️⃣  FILL ALL DETAILS
// ──────────────────────────────
function fillOrderSheet($root, dto) {

  console.log(dto);

  if (!dto) return;

  // Fill first header table (7 columns)
  const $hdr = $root.find('table').eq(0).find('tr').eq(1).find('td');
  if ($hdr.length >= 7) {
    $hdr.eq(0).text(dto.noticeNo || '');
    $hdr.eq(1).text(dto.wardNo || '');
    $hdr.eq(2).text(dto.zoneNo || '');
    $hdr.eq(3).text(dto.finalPropertyNo || '');
    $hdr.eq(4).text(dto.surveyNo || '');
    $hdr.eq(5).text(dto.oldPropertyNo || '');
    $hdr.eq(6).text(formatDate(dto.hearingDate));
  }

  // Fill hearing details (example placeholders)
  $root.find('.ownerName').text(dto.ownerName || '');
  $root.find('.applicantName').text(dto.applicantName || '');
  $root.find('.applicationNo').text(dto.applicationNo || '');
  // $root.find('.hearingDate').text(formatDate(dto.hearingDate));
  $root.find('.hearingStatus').text(dto.hearingStatus || '');
  $root.find('.objectionDate').text(formatDate(dto.objectionDate)||'--------');
  $root.find('.hearingDate').text(formatDate(dto.hearingDate)||'--------');
    const reasonsText = dto.reasons || '';
    if (reasonsText.trim() !== '') {
        const reasonsArray = reasonsText.split(',').map(r => r.trim()).filter(r => r !== '');
        const formattedReasons = reasonsArray
            .map((r, i) => `${i + 1}. ${r}`)
            .join('  '); // two spaces between items or use '\n' for new lines
        $root.find('.reasons').text(formattedReasons);
    } else {
        $root.find('.reasons').text('');
    }



  // Render dynamic tax and R-values
  renderTaxes(dto, $root);
  renderProposedRValues(dto, $root);
}


let globalYearRange = '';
document.addEventListener('DOMContentLoaded', () => {

    fetch('/3g/getAllAssessmentDates', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {

        const currentAssessmentDate = data[0].currentassessmentdate;
        globalYearRange = formatYearRange(currentAssessmentDate);
        $('.yearRange').text(globalYearRange);
    })
    .catch(error => console.error('Error fetching additional data:', error));
});



function formatYearRange(date) {
    const year = parseInt(date.split('-')[0]);  // Extract the year from the date
    const startYear = year;
    const endYear = year + 3;
    const startYearL = startYear + 1;
    const endYearL = endYear + 1;
    const startYearDev = convertToDevanagari(startYear.toString());
    const endYearDev = convertToDevanagari(endYear.toString());

    return `${startYearDev}-${convertToDevanagari(startYearL.toString())} ते ${endYearDev}-${convertToDevanagari(endYearL.toString())}`;
}


// ──────────────────────────────
// 5️⃣  TAX TABLE GENERATOR
// ──────────────────────────────
function buildReportTaxTableInRoot($root, tableSelector, templateName = 'ORDER_SHEET') {
  $.get(`/3g/reportTaxConfigs?template=${encodeURIComponent(templateName)}`, function (configs) {
    if (!configs || configs.length === 0) return;
    const $table = $root.find(tableSelector);
    if ($table.length === 0) return;
    $table.empty();

    const $row1 = $('<tr class="t-a-c"></tr>');
    const $row2 = $('<tr class="t-a-c"></tr>');
    const $valRow = $('<tr class="t-a-c"></tr>');
    // Pleasant, readable header colors (align with Special Notice)
    $row1.css('background-color', '#CEF6CE');   // light green
    $row2.css('background-color', '#F8E0F7');   // light lavender
    $table.append($row1).append($row2).append($valRow);

    const parentMap = {};
    configs.forEach((c) => {
      if (!c.parentTaxKeyL) parentMap[c.taxKeyL] = { parent: c, children: [] };
    });
    configs.forEach((c) => {
      if (c.parentTaxKeyL)
        (parentMap[c.parentTaxKeyL] ||= { parent: null, children: [] }).children.push(c);
    });

    configs.sort((a, b) => (a.sequenceI || 0) - (b.sequenceI || 0));

    configs.forEach((cfg) => {
      if (!cfg.parentTaxKeyL) {
        const group = parentMap[cfg.taxKeyL];
        if (group && group.children && group.children.length > 0) {
          $row1.append(
            `<th colspan="${group.children.length + (cfg.showTotalBl ? 1 : 0)}">${cfg.localNameVc || ''}</th>`
          );
          group.children.forEach((child) => {
            $row2.append(`<th>${child.localNameVc || ''}</th>`);
            $valRow.append(`<td id="tax-${child.taxKeyL}">0</td>`);
          });
          if (cfg.showTotalBl) {
            $row2.append('<th>एकूण</th>');
            $valRow.append(`<td id="tax-${cfg.taxKeyL}">0</td>`);
          }
        } else {
          $row1.append(`<th rowspan="2">${cfg.localNameVc || ''}</th>`);
          $valRow.append(`<td id="tax-${cfg.taxKeyL}">0</td>`);
        }
      }
    });

    $row1.append('<th rowspan="2">एकूण</th>');
    $valRow.append('<td id="totalTax">0</td>');
  });
}

// Promise-based variant to ensure the table is ready before filling values
function buildReportTaxTableInRootAsync($root, tableSelector, templateName = 'ORDER_SHEET') {
  return new Promise((resolve, reject) => {
    $.get(`/3g/reportTaxConfigs?template=${encodeURIComponent(templateName)}`, function (configs) {
      // Even if no configs, resolve to avoid blocking
      if (!configs || configs.length === 0) { resolve(); return; }

      const $table = $root.find(tableSelector);
      if ($table.length === 0) { resolve(); return; }
      $table.empty();
      const $rowMain =$('<tr><th colspan="15" style="text-align: left;background-color: rgb(161, 255, 255)">सुतावणी आधीचे कर विवरण</th></tr>');
      const $row1 = $('<tr class="t-a-c"></tr>');
      const $row2 = $('<tr class="t-a-c"></tr>');
      const $valRow = $('<tr class="t-a-c"></tr>');
      // Pleasant, readable header colors (align with Special Notice)
      $row1.css('background-color', '#CEF6CE');
      $row2.css('background-color', '#F8E0F7');
      $table.append($rowMain).append($row1).append($row2).append($valRow);

      const parentMap = {};
      configs.forEach((c) => { if (!c.parentTaxKeyL) parentMap[c.taxKeyL] = { parent: c, children: [] }; });
      configs.forEach((c) => { if (c.parentTaxKeyL) (parentMap[c.parentTaxKeyL] ||= { parent: null, children: [] }).children.push(c); });

      configs.sort((a, b) => (a.sequenceI || 0) - (b.sequenceI || 0));

      configs.forEach((cfg) => {
        if (!cfg.parentTaxKeyL) {
          const group = parentMap[cfg.taxKeyL];
          if (group && group.children && group.children.length > 0) {
            $row1.append(`<th colspan="${group.children.length + (cfg.showTotalBl ? 1 : 0)}">${cfg.localNameVc || ''}</th>`);
            group.children.forEach((child) => {
              $row2.append(`<th>${child.localNameVc || ''}</th>`);
              $valRow.append(`<td id="tax-${child.taxKeyL}">0</td>`);
            });
            if (cfg.showTotalBl) {
              $row2.append('<th>एकूण</th>');
              $valRow.append(`<td id="tax-${cfg.taxKeyL}">0</td>`);
            }
          } else {
            $row1.append(`<th rowspan="2">${cfg.localNameVc || ''}</th>`);
            $valRow.append(`<td id="tax-${cfg.taxKeyL}">0</td>`);
          }
        }
      });

      $row1.append('<th rowspan="2">एकूण</th>');
      $valRow.append('<td id="totalTax">0</td>');
      resolve();
    }).fail(reject);
  });
}


// ──────────────────────────────
// 6️⃣  TAX VALUES FILL
// ──────────────────────────────
async function renderTaxes(dto, $root) {
  try {
    let res = await fetch('/3g/reportTaxConfigs?template=ORDER_SHEET');
    let configs = await res.json();
    if (!configs || configs.length === 0) {
      const res2 = await fetch('/3g/reportTaxConfigs?template=ASSESSMENT_REGISTER');
      configs = await res2.json();
    }

    const map = dto.taxKeyValueMap || {};
    let total = 0;

    configs.forEach((cfg) => {
      const key = cfg.taxKeyL;
      const amt = Number(map[key] || 0);
      $root.find('#tax-' + key).text(amt.toFixed(2));
      total += amt;
    });

    $root.find('#totalTax').text(total.toFixed(2));
  } catch (e) {
    console.warn('renderTaxes failed', e);
  }
}


// ──────────────────────────────
// 7️⃣  PROPOSED RATABLE VALUES
// ──────────────────────────────
function renderProposedRValues(dto, $root) {
  const list = dto.proposedRValues || [];
  if (Array.isArray(list) && list.length > 0) {
    const sum = (sel) => list.reduce((s, x) => s + (Number(x[sel] || 0)), 0);
    const mapping = [
      { cls: '.residentialFl', key: 'prResidentialFl' },
      { cls: '.commercialFl', key: 'prCommercialFl' },
      { cls: '.religiousFl', key: 'prReligiousFl' },
      { cls: '.residentialOpenPlotFl', key: 'prResidentialOpenPlotFl' },
      { cls: '.commercialOpenPlotFl', key: 'prCommercialOpenPlotFl' },
      { cls: '.religiousOpenPlotFl', key: 'prReligiousOpenPlotFl' },
      { cls: '.educationalInstituteOpenPlotFl', key: 'prEducationAndLegalInstituteOpenPlotFl' },
      { cls: '.governmentOpenPlotFl', key: 'prGovernmentOpenPlotFl' },
      { cls: '.industrialOpenPlotFl', key: 'prIndustrialOpenPlotFl' },
      { cls: '.educationAndLegalInstituteOpenPlotFl', key: 'prEducationalFl' },
      { cls: '.governmentFl', key: 'prGovernmentFl' },
      { cls: '.industrialFl', key: 'prIndustrialFl' },
      { cls: '.mobileTowerFl', key: 'prMobileTowerFl' },
      { cls: '.electricSubstationFl', key: 'prElectricSubstationFl' },
      { cls: '.aggregateFl', key: 'prTotalRatableValueFl' },
    ];
    mapping.forEach(({ cls, key }) => {
      const val = sum(key);
      $root.find(cls).text(val.toFixed(2));
    });
    return;
  }

  // Fallback: flat object provided
  const obj = dto.proposedRatableValues || dto.proposedRValue || {};
  const toNum = (v) => (isNaN(Number(v)) ? 0 : Number(v));
  const set = (cls, v) => $root.find(cls).text(toNum(v).toFixed(2));

  set('.residentialFl', obj.residentialFl);
  set('.commercialFl', obj.commercialFl);
  set('.religiousFl', obj.religiousFl);
  set('.residentialOpenPlotFl', obj.residentialOpenPlotFl);
  set('.commercialOpenPlotFl', obj.commercialOpenPlotFl);
  set('.religiousOpenPlotFl', obj.religiousOpenPlotFl);
  set('.educationalInstituteOpenPlotFl', obj.educationalInstituteOpenPlotFl || obj.educationAndLegalInstituteOpenPlotFl);
  set('.governmentOpenPlotFl', obj.governmentOpenPlotFl);
  set('.industrialOpenPlotFl', obj.industrialOpenPlotFl);
  set('.educationAndLegalInstituteOpenPlotFl', obj.educationAndLegalInstituteOpenPlotFl);
  set('.governmentFl', obj.governmentFl);
  set('.industrialFl', obj.industrialFl);
  set('.mobileTowerFl', obj.mobileTowerFl);
  set('.electricSubstationFl', obj.electricSubstationFl);
  set('.aggregateFl', obj.aggregateFl);
}


// ──────────────────────────────
// 8️⃣  UTILS
// ──────────────────────────────
function formatDate(dateStr) {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  return `${d.getDate().toString().padStart(2, '0')}-${(d.getMonth() + 1)
    .toString()
    .padStart(2, '0')}-${d.getFullYear()}`;
}

function convertToDevanagari(num) {
  const map = ['०','१','२','३','४','५','६','७','८','९'];
  return num.toString().replace(/[0-9]/g, (d) => map[d]);
}


$(document).on('click', '#printBtn', function () {
  printOrderSheets();
});

function printOrderSheets() {
  // Optional: add a small delay to ensure all async data is rendered
  setTimeout(() => {
    // Remove button before printing
    $('#printBtn').hide();

    // Use print() for browser print dialog
    window.print();

    // Show button again after printing
    setTimeout(() => $('#printBtn').show(), 500);
  }, 500);
}