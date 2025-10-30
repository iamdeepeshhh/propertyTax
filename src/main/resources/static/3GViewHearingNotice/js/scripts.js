
// For the Council
$(document).ready(function () {
  $.ajax({
    url: '/3g/getCouncilDetails',
    type: 'GET',
    success: function(data) {
      if (data && data.length > 0) {
        councilDetails = data[0];
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

// For the year

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

     return `${startYearDev}-${convertToDevanagari(startYearL.toString())} à¤¤à¥‡ ${endYearDev}-${convertToDevanagari(endYearL.toString())}`;
 }

 function convertToDevanagari(numberString) {
     const devanagariDigits = ['à¥¦', 'à¥§', 'à¥¨', 'à¥©', 'à¥ª', 'à¥«', 'à¥¬', 'à¥­', 'à¥®', 'à¥¯'];
     return numberString.replace(/[0-9]/g, (digit) => devanagariDigits[digit]);
 }
// Fetch hearing notices from objection register and render like Special Notice
let hnDataList = [];
let hnCurrentChunk = 0;
const hnChunkSize = 100;
let $hnOriginalPage = null;

document.addEventListener('DOMContentLoaded', () => {
  const qp = new URLSearchParams(location.search);
  const wardNo = qp.get('wardNo');
  const newPropertyNo = qp.get('newPropertyNo');

  if (newPropertyNo) {
    fetch(`/3g/hearingNotices?newPropertyNo=${encodeURIComponent(newPropertyNo)}`)
      .then(r => r.json())
      .then(list => {
        if (!list || list.length === 0) return;
        $hnOriginalPage = $('.container').first().clone(false);
        renderHearingNoticeSingle(list[0]);
      })
      .catch(console.error);
  } else if (wardNo) {
    addHearingPrintButton(wardNo);
  }
});

function addHearingPrintButton(wardNo){
  $('body').prepend('<button id="startHearingPrint">Print Hearing Notices</button>');
  $('#startHearingPrint').on('click', function(){
    $('#startHearingPrint').hide();
    fetch(`/3g/hearingNotices?wardNo=${encodeURIComponent(wardNo)}`)
      .then(r => r.json())
      .then(data => {
        if (!data || data.length === 0) { alert('No data found'); $('#startHearingPrint').show(); return; }
        hnDataList = data; hnCurrentChunk = 0; $hnOriginalPage = $('.container').first().detach();
        renderAndPrintHearingChunk();
      })
      .catch(e => { console.error(e); $('#startHearingPrint').show(); });
  });
}

function renderHearingNoticeSingle(dto){
  const $page = $('.container').first();
  $page.find('.pdFinalpropnoVc').text(dto.finalPropertyNo || dto.pdFinalpropnoVc || '');
  $page.find('.pdOwnernameVc').text(dto.ownerName || dto.pdOwnernameVc || '');
  $page.find('.pdOldpropnoVc').text(dto.oldPropertyNo || dto.pdOldpropnoVc || '');
  $page.find('.pdWardI').text(dto.wardNo || dto.pdWardI || '');
  $page.find('.pdZoneI').text(dto.zoneNo || dto.pdZoneI || '');
  $page.find('.pdNoticenoVc').text(dto.noticeNo || dto.pdNoticenoVc || '');
  $page.find('.hearingDate').text(dto.hearingDate || '');
  $page.find('.hearingTime').text(dto.hearingTime || '');
}

function renderAndPrintHearingChunk(){
  const chunk = hnDataList.slice(hnCurrentChunk*hnChunkSize, (hnCurrentChunk+1)*hnChunkSize);
  if (chunk.length === 0) { alert('All chunks printed'); $('#startHearingPrint').show(); return; }

  const $wrap = $('<div id="hn-report"></div>');
  chunk.forEach(dto => {
    const $p = $hnOriginalPage.clone(false);
    $p.find('.pdFinalpropnoVc').text(dto.finalPropertyNo || '');
    $p.find('.pdOwnernameVc').text(dto.ownerName || '');
    $p.find('.pdOldpropnoVc').text(dto.oldPropertyNo || '');
    $p.find('.pdWardI').text(dto.wardNo || '');
    $p.find('.pdZoneI').text(dto.zoneNo || '');
    $p.find('.pdNoticenoVc').text(dto.noticeNo || '');
    $p.find('.hearingDate').text(dto.hearingDate || '');
    $p.find('.hearingTime').text(dto.hearingTime || '');
    $wrap.append($p);
  });

  $('body').append($wrap);
  setTimeout(() => {
    window.print();
    $wrap.remove();
    hnCurrentChunk++;
    renderAndPrintHearingChunk();
  }, 500);
}

