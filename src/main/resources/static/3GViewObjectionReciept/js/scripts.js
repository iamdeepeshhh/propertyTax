
function cancelsearch() {
    window.location.replace('/citizenLogin');
}

function printOut(print) {  var printOutContent = document.querySelector('.print').innerHTML;
var originalContent = document.body.innerHTML;
document.body.innerHTML = printOutContent;
window.print();
document.body.innerHTML = originalContent;
}

$(document).ready(function () {
    $.ajax({
        url: '/3g/getCouncilDetails',
        type: 'GET',
        success: function (data) {
            if (data && data.length > 0) {
                const councilDetails = data[0];
                console.log("++"+councilDetails);
                // Set council local name
                $('.councilLocalName').text(councilDetails.localName+',' || '');

                // Set logo if present
                if (councilDetails.imageBase64) {
                    $('.councilLogo').attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
                }

                // Set other details
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