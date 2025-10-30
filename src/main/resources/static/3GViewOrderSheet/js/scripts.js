
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
