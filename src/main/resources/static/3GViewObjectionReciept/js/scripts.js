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

                // ✅ Table population
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

                // ✅ Replace dynamic fields based on your JSON
                const ownerName = data.ownerName || '';
                const noticeNo = data.noticeNo || '';
                const date = data.applicationReceivedDate || '';
                const reasons = data.reasons || '';
                const time = data.userTime || '';
                const appNo = data.applicationNo || '';

                // Applicant name replacements
                $('span[style*="border-bottom"][style*="font-weight:bold"]').eq(0).text(ownerName);
                $('span[style*="border-bottom"]').eq(1).text(noticeNo);
                $('span[style*="border-bottom"]').eq(2).text(date);
                
                // Replace reasons text
                $('#usercomments').text(reasons);

                // Replace footer details
                $('#lastDiv td:contains("दिनांक")').next().text(date || '');
                $('#lastDiv td:contains("वेळ")').next().text(time || '');
                $('#lastDiv td:contains("अर्ज क्र.")').next().text(appNo || '');

            } else {
                console.warn('No objection data received');
            }
        },
        error: function (xhr, status, error) {
            console.error('Error fetching objection details:', error);
        }
    });
}
