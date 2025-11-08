function cancelsearch() {
    window.location.replace('/citizenLogin');
}

function printOut(print) {
    var printOutContent = document.querySelector('.print').innerHTML;
    var originalContent = document.body.innerHTML;
    document.body.innerHTML = printOutContent;
    window.print();
    document.body.innerHTML = originalContent;
}

// Fetch council details (works fine)
$(document).ready(function () {
    $.ajax({
        url: '/3g/getCouncilDetails',
        type: 'GET',
        success: function (data) {
            if (data && data.length > 0) {
                const councilDetails = data[0];
                $('.councilLocalName').text((councilDetails.localName || '') + ',');
                if (councilDetails.imageBase64) {
                    $('.councilLogo').attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
                }
                $('.standardSiteNameVC').text(councilDetails.standardSiteNameVC || '');
                $('.localSiteNameVC').text(councilDetails.localSiteNameVC || '');
                $('.standardDistrictNameVC').text(councilDetails.standardDistrictNameVC || '');
                $('.localDistrictNameVC').text(councilDetails.localDistrictNameVC || '');
            }
        },
        error: function (xhr, status, error) {
            console.error("Error fetching council details:", error);
        }
    });
});

$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const newPropertyNo = urlParams.get('newPropertyNo');
    console.log("URL param newPropertyNo:", newPropertyNo);

    if (newPropertyNo) {
        fetchObjectionDetails(newPropertyNo);
    } else {
        console.warn('No newPropertyNo in URL');
    }
});


function fetchObjectionDetails(newPropertyNo) {
    $.ajax({
        url: '/3g/getObjectionDetails/' + newPropertyNo,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            if (data) {
                console.log("Fetched objection details:", data);

                // ✅ Table population (Looks fine)
                const tableRow = `
                    <tr class="h-25 t-c">
                        <td>${data.wardNo || ''}</td>
                        <td>${data.zoneNo || ''}</td>
                        <td>${data.surveyNo || ''}</td>
                        <td>${data.newPropertyNo || ''}</td>
                        <td>${data.oldWardNo || ''}</td>
                        <td>${data.oldZoneNo || ''}</td>
                        <td>${data.oldPropertyNo || ''}</td>
                        <td>${data.finalPropertyNo || ''}</td>
                    </tr>
                `;
                $('#fifthDiv table').find('tr:gt(0)').remove();
                $('#fifthDiv table').append(tableRow);
                
                // 🛑 FIXED: Added dot for class selector
                $('.yearRange').text(formatYearRange(data.hearingDate));


                // ✅ Replace dynamic fields based on your JSON
                const ownerName = data.ownerName || '';
                const noticeNo = data.noticeNo || '';
                const applicationDate = data.applicationReceivedDate ? formatDate(data.applicationReceivedDate) : ''; // Use formatDate
                const hearingDateFormatted = data.hearingDate ? formatDate(data.hearingDate) : ''; // New variable for hearing date
                const reasons = data.reasons || '';
                const time = data.userTime || '';
                const appNo = data.applicationNo || '';

                // Applicant name replacements
                // 🛑 FIXED: Removed undefined $page and used standard selector
                $('.ownerName').text(ownerName); 
                $('.noticeNo').text(noticeNo);
                $('.applicationReceivedDate').text(applicationDate);
                $('.totalRValue').text(data.totalRValue);
                $('.totalTax').text(data.totalTax);
                
                
                
                const reasonsArray = reasons.split(',').map(reason => reason.trim()); 

            
                const numberedReasonsArray = reasonsArray.map((reason, index) => {
                    return `${index + 1}. ${reason}`;
                });

                const formattedReasonsHTML = numberedReasonsArray.join('<br>'); // Simplified join string

                $('.reasons').html(formattedReasonsHTML);

                const formattedReasonsHTML2 = numberedReasonsArray.join(' ');

                $('.reasons2').text(formattedReasonsHTML2);

                // Replace footer details
                // 🛑 FIXED: Use two different selectors to avoid overwriting (or adjust your HTML)
                $('.hearingDate').text(hearingDateFormatted || ''); // Assuming a new class .hearingDate
                $('.hearingTime').text(time || ''); 
                $('.applicationNo').text(appNo || '');

            } else {
                console.warn('No objection data received');
            }
        },
        error: function (xhr, status, error) {
            console.error('Error fetching objection details:', error);
        }
    });
}

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
