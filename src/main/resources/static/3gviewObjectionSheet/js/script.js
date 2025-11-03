$(document).ready(function () {

    // 🔹 Fetch Council Details for the header (optional)
    $.ajax({
        url: '/3g/getCouncilDetails',
        type: 'GET',
        success: function (data) {
            if (data && data.length > 0) {
                const council = data[0];
                $('.councilName').text((council.localName || '') + ' / ' + (council.standardName || ''));
                if (council.imageBase64) {
                    $('.councilLogoLeft').attr('src', 'data:image/png;base64,' + council.imageBase64);
                }
                if (council.image2Base64 && council.image2Base64.trim() !== '') {
                    $('.councilLogoRight').attr('src', 'data:image/png;base64,' + council.image2Base64).show();
                } else {
                    $('.councilLogoRight').hide();
                }
            }
        }
    });

    // 🔹 Fetch all objections from controller
    $.ajax({
        url: '/3g/getObjectionList',  // ✅ Adjust path if necessary
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log("Fetched objections:", data);
            if (data && data.length > 0) {
                populateObjectionTable(data);
            } else {
                alert("No objection data available.");
            }
        },
        error: function (err) {
            console.error("Error fetching objection list:", err);
            alert("Failed to fetch objection data.");
        }
    });

    // 🔹 Function to populate table
    function populateObjectionTable(data) {
        const tbody = $('table[frame="border"] tbody'); // main data table
        tbody.find('tr:gt(1)').remove(); // remove old rows except header

        data.forEach((item, index) => {
            const row = `
                <tr>
                    <td>${index + 1}</td>
                    <td>
                        ${item.objectionNo || '-'}
                        <hr style="margin:0px !important;">${formatDate(item.objectionDate)}
                        <hr style="margin:0px !important;">${item.applicationNo || '-'}
                    </td>
                    <td>${item.wardNo || '-'}</td>
                    <td>
                        ${item.newPropertyNo || '-'}
                        <hr style="margin:0px !important;">${item.oldPropertyNo || '-'}
                        <hr style="margin:0px !important;">${item.surveyPropertyNo || '-'}
                    </td>
                    <td>
                        ${item.ownerName || '-'}
                        <hr style="margin:0px !important;">${item.applicantName || '-'}
                    </td>
                    <td align="justify" style="width:40%;padding:5px;">${item.objectionReason || '-'}</td>
                </tr>
            `;
            tbody.append(row);
        });
    }

    // 🔹 Helper to format date (YYYY-MM-DD → DD-MM-YYYY)
    function formatDate(dateString) {
        if (!dateString) return '-';
        const d = new Date(dateString);
        const day = String(d.getDate()).padStart(2, '0');
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const year = d.getFullYear();
        return `${day}-${month}-${year}`;
    }

    // 🔹 PRINT functionality
    $('#btnPrint').on('click', function () {
        window.print();
    });

    // 🔹 SAVE TO PDF functionality (requires html2pdf.js)
    $('#btnSavePDF').on('click', function () {
        const element = document.body; // or specific div: document.getElementById('report-section')
        const opt = {
            margin: 0.2,
            filename: 'Objection_Register.pdf',
            image: { type: 'jpeg', quality: 0.98 },
            html2canvas: { scale: 2 },
            jsPDF: { unit: 'in', format: 'a4', orientation: 'portrait' }
        };
        html2pdf().set(opt).from(element).save();
    });

});
