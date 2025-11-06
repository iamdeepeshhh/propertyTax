// ==========================================
// 🏛️ Fetch & Render Council Details
// ==========================================
$(document).ready(function () {
  $.ajax({
    url: '/3g/getCouncilDetails',
    type: 'GET',
    success: function (data) {
      if (data && data.length > 0) {
        const council = data[0];
        $('.councilName').text(council.localName);
        $('.localDistrictNameVC').text(council.localDistrictNameVC);
        if (council.imageBase64) {
          $('.councilLogo').attr('src', 'data:image/png;base64,' + council.imageBase64);
        }
      }
    },
  });
});

// ==========================================
// 📅 Fetch Assessment Year Range
// ==========================================
let globalYearRange = '';
document.addEventListener('DOMContentLoaded', () => {
  fetch('/3g/getAllAssessmentDates')
    .then((r) => r.json())
    .then((data) => {
      if (!data || data.length === 0) return;
      const date = data[0].currentassessmentdate;
      globalYearRange = formatYearRange(date);
      $('.yearRange').text(globalYearRange);
    })
    .catch(console.error);
});

function formatYearRange(date) {
  const year = parseInt(date.split('-')[0]); // e.g., 2024
  const startNext = year + 1;                // e.g., 2025
  const endYear = year + 3;                  // e.g., 2027
  const endNext = year + 4;                  // e.g., 2028

  // ✅ Always pass strings to convertToDevanagari
  const startPart = `${convertToDevanagari(year.toString())}-${convertToDevanagari(startNext.toString().slice(-2))}`;
  const endPart = `${convertToDevanagari(endYear.toString())}-${convertToDevanagari(endNext.toString().slice(-2))}`;

  return `${startPart} ते ${endPart}`;
}

function convertToDevanagari(numStr) {
  if (numStr == null) return ''; // null/undefined safety
  numStr = numStr.toString();    // ensure it's a string
  const digits = ['०', '१', '२', '३', '४', '५', '६', '७', '८', '९'];
  return numStr.replace(/[0-9]/g, (d) => digits[d]);
}
// ==========================================
// 📜 Fetch Hearing Notices and Print
// ==========================================
let hnDataList = [];
let hnCurrentChunk = 0;
const hnChunkSize = 100;
let $hnOriginalPage = null;

document.addEventListener('DOMContentLoaded', () => {
  const qp = new URLSearchParams(location.search);
  const wardNo = qp.get('wardNo');
  const newPropertyNo = qp.get('newPropertyNo');

  if (newPropertyNo) {
    // 🧾 Single Notice Mode
    fetch(`/3g/hearingNotices?newPropertyNo=${encodeURIComponent(newPropertyNo)}`)
      .then((r) => r.json())
      .then((list) => {
        if (!list || list.length === 0) return;
        $hnOriginalPage = $('.container').first().clone(false);
        renderHearingNoticeSingle(list[0]);
      })
      .catch(console.error);
  } else if (wardNo) {
    // 📑 Multi Notice (Ward Mode)
    addHearingPrintButton(wardNo);
  }
});

// ==========================================
// 🖨️ Add Print Button and Start Printing
// ==========================================
function addHearingPrintButton(wardNo) {
  $('body').prepend(
    '<button id="startHearingPrint" style="margin:10px;padding:8px 16px;background:#007bff;color:#fff;border:none;border-radius:4px;cursor:pointer;">Print Hearing Notices</button>'
  );

  $('#startHearingPrint').on('click', function () {
    $('#startHearingPrint').hide();
    fetch(`/3g/hearingNotices?wardNo=${encodeURIComponent(wardNo)}`)
      .then((r) => r.json())
      .then((data) => {
        if (!data || data.length === 0) {
          alert('⚠️ No Hearing Notices found for this ward.');
          $('#startHearingPrint').show();
          return;
        }
        hnDataList = data;
        hnCurrentChunk = 0;
        $hnOriginalPage = $('.container').first().detach();
        renderAndPrintHearingChunk();
      })
      .catch((e) => {
        console.error(e);
        $('#startHearingPrint').show();
      });
  });

  // Auto-trigger preview loading without manual click
  setTimeout(function(){
    var btn = document.getElementById('startHearingPrint');
    if (btn) { btn.click(); }
  }, 0);
}

// ==========================================
// 🧾 Render Single Hearing Notice
// ==========================================
function renderHearingNoticeSingle(dto) {
  console.log(dto);
  
  const $page = $('.container').first();

  // --- Fill both tables ---
  $page.find('table').each(function() {
    const $table = $(this);
    $table.find('tbody tr td').eq(0).text(dto.applicationNo || '');
    $table.find('tbody tr td').eq(1).text(dto.zoneNo || '');
    $table.find('tbody tr td').eq(2).text(dto.wardNo || '');
    $table.find('tbody tr td').eq(3).text(dto.finalPropertyNo || '');
    $table.find('tbody tr td').eq(4).text(dto.oldPropertyNo || '');
  });

  // --- Dynamic text binding for both content blocks ---
  $page.find('.ownerName, .pdOwnernameVc').text(dto.ownerName || '');
  $page.find('.hearingDate').text(dto.hearingDate || '');
  $page.find('.hearingTime').text(formatTo12Hour(dto.hearingTime) || '');
  $page.find('.noticeNo, .pdNoticenoVc').text(dto.noticeNo || '');
  $page.find('.applicationReceivedDate').text(dto.applicationReceivedDate || '');
}


function formatTo12Hour(time24) {
  if (!time24) return '';
  
  // Split "HH:mm"
  const [hours, minutes] = time24.split(':').map(Number);
  
  // Determine AM/PM
  const period = hours >= 12 ? 'PM' : 'AM';
  
  // Convert to 12-hour format (0 → 12, 13 → 1, etc.)
  const hours12 = hours % 12 || 12;
  
  // Return formatted string
  return `${hours12}:${minutes.toString().padStart(2, '0')} ${period}`;
}

// ==========================================
// 🧾 Render & Print Multiple Hearing Notices
// ==========================================
function renderAndPrintHearingChunk() {
  const chunk = hnDataList.slice(hnCurrentChunk * hnChunkSize, (hnCurrentChunk + 1) * hnChunkSize);
  if (chunk.length === 0) {
  // ✅ All chunks rendered – show Print All button
  if (!$('#hn-final-print').length) {
    $('body').append(
      '<button id="hn-final-print" style="margin:10px;padding:8px 16px;background:#28a745;color:#fff;border:none;border-radius:4px;cursor:pointer;">🖨️ Print All</button>'
    );
    $('#hn-final-print').on('click', () => window.print());
  }
  return;
}

  let $wrap = $('#hn-report');
  if (!$wrap.length) {
    $wrap = $('<div id="hn-report"></div>');
    $('body').append($wrap);
  }
  chunk.forEach((dto) => {
    const $p = $hnOriginalPage.clone(false);

    // Fill Table
    $p.find('tbody tr td').eq(0).text(dto.applicationNo || '-');
    $p.find('tbody tr td').eq(1).text(dto.zoneNo || '-');
    $p.find('tbody tr td').eq(2).text(dto.wardNo || '-');
    $p.find('tbody tr td').eq(3).text(dto.newPropertyNo || '-');
    $p.find('tbody tr td').eq(4).text(dto.oldPropertyNo || '-');

    // Dynamic Text Fill (support both legacy and new selectors)
    $p.find('.noticeNo, .pdNoticenoVc').text(dto.noticeNo || '-');
    $p.find('.ownerName, .pdOwnernameVc').text(dto.ownerName || '-');
    $p.find('.hearingDate').text(dto.hearingDate || '-');
    $p.find('.hearingTime').text(dto.hearingTime || '-');

    $wrap.append($p);
  });

  // wrapper already ensured above
  if (!$('#hn-manual-print').length) {
    $('body').prepend('<button id="hn-manual-print" style="margin:10px;padding:8px 16px;background:#28a745;color:#fff;border:none;border-radius:4px;cursor:pointer;">Print All</button>');
    $('#hn-manual-print').on('click', function () { window.print(); });
  }

  // Preview mode: render next chunk without auto-print
  hnCurrentChunk++;
  renderAndPrintHearingChunk();
}


// ==========================================
// 🖨️ Add Global Print Button (works for single & multi)
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
  const btn = document.createElement('button');
  btn.textContent = '🖨️ Print Page';
  btn.id = 'globalPrintBtn';
  btn.style.cssText =
    'position:fixed;top:10px;right:20px;padding:8px 16px;background:#007bff;color:#fff;border:none;border-radius:4px;cursor:pointer;z-index:9999;';
  document.body.appendChild(btn);
  btn.addEventListener('click', () => window.print());
});



