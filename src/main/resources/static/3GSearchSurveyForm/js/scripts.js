$(document).ready(function() {
var isDeProfile = false;
var deletePropNo, deleteSurveyPropNo, deleteOwnerName, deleteCreatedBy, deleteWard;

// Fetch and log user profile on page load
function fetchAndLogUserProfile() {
    return $.ajax({
        url: '/3gSurvey/getUserProfileFromSession',
        type: 'GET',
        success: function(profileData) {
            console.log("User Profile:", profileData);
            isDeProfile = (profileData && profileData.profile === 'DEL');
            updateTableHead();
        },
        error: function() {
            console.error('Error fetching user profile.');
        }
    });
}

// Update table head based on user profile
function updateTableHead() {
    var tableHeadHtml = `
        <tr>
            <th>#</th>
            <th>SPN</th>
            <th>Ward</th>
            <th>Owner Name</th>
            <th>Address</th>
            <th>Property Number</th>
            <th>Zone</th>
            <th>Created By</th>`;
    if (!isDeProfile) {
        tableHeadHtml += `<th>Actions</th>`;
    }
    tableHeadHtml += `</tr>`;
    $('#tableHead').html(tableHeadHtml);
}

// Call the function to fetch and log user profile
fetchAndLogUserProfile();

$('#searchButton').click(function() {
    var surveyNumber = $('#surveyNumberInput').val().trim();
    var ownerName = $('#ownerNameInput').val().trim();
    var wardNumber = $('#wardNumberInput').val();
    var searchParams = new URLSearchParams();

    if (surveyNumber) {
        searchParams.set("surveyPropertyNo", surveyNumber);
    }
    if (ownerName) {
        searchParams.set("ownerName", ownerName);
    }
    if (wardNumber) {
        searchParams.set("wardNo", wardNumber);
    }

    var baseUrl = "/3gSurvey/searchNewProperties";
    var fullUrl = baseUrl + "?" + searchParams.toString();

    // Ensure user profile is fetched and used before updating search results
    fetchAndLogUserProfile().then(function() {
        $.ajax({
            url: fullUrl,
            type: 'GET',
            success: function(data) {
                var tbody = $('#searchResults');
                tbody.empty();
                if (data && data.length > 0) {
                    $('.no-results').hide();
                    data.sort((a, b) => {
                        function parseSurveyPropNo(propNo) {
                            const parts = propNo.split('/').map(Number);
                            return { prefix: parts[0] || 0, suffix: parts[1] || 0 };
                        }
                    
                        const surveyA = parseSurveyPropNo(a.pdSurypropnoVc);
                        const surveyB = parseSurveyPropNo(b.pdSurypropnoVc);
                    
                        if (surveyA.prefix !== surveyB.prefix) {
                            return surveyA.prefix - surveyB.prefix;
                        }
                        return surveyA.suffix - surveyB.suffix;
                    });

                    data.forEach(function(item, index) {
                    var deleteButton = isDeProfile ? '' : `<button class="btn btn-danger delete-btn" 
                        data-propno="${item.pdNewpropertynoVc}" 
                        data-surveypropno="${item.pdSurypropnoVc}" 
                        data-ownername="${item.pdOwnernameVc}" 
                        data-createdby="${item.user_id}" 
                        data-ward="${item.pdWardI}" 
                        data-toggle="modal" 
                        data-target="#deleteModal">Delete</button>`;
                    
                    var editButton = isDeProfile ? '' : `<button class="btn btn-primary edit-btn" 
                        data-propno="${item.pdNewpropertynoVc}" 
                        data-createdby="${item.user_id}" 
                        data-toggle="modal" 
                        data-target="#editModal">Edit</button>`;
                    
                    var row = `<tr class="search-result-row" data-propno="${item.pdNewpropertynoVc}">
                        <td>${index + 1}</td>
                        <td>${item.pdSurypropnoVc}</td>
                        <td>${item.pdWardI}</td>
                        <td>${item.pdOwnernameVc}</td>
                        <td>${item.pdPropertyaddressVc}</td>
                        <td>${item.pdFinalpropnoVc}</td>
                        <td>${item.pdZoneI}</td>
                        <td>${item.user_id}</td>`;
                    
                    if (!isDeProfile) {
                        row += `<td>${deleteButton}${editButton}</td>`;
                    }
                    
                    row += `</tr>`;
                    tbody.append(row);
                });

                    // Attach click event to each row
                    $('.search-result-row').on('click', function() {
                        var propNo = $(this).data('propno');
                        window.location.href = '/3gSurvey/showsurvey/' + propNo; // Navigate to the next page with the survey ID
                    });

                    // Attach click event to delete buttons and stop propagation
                    $('.delete-btn').on('click', function(event) {
                        event.stopPropagation(); // Prevent row click event
                        deletePropNo = $(this).data('propno');
                        deleteSurveyPropNo = $(this).data('surveypropno');
                        deleteOwnerName = $(this).data('ownername');
                        deleteCreatedBy = $(this).data('createdby');
                        deleteWard = $(this).data('ward');
                        // Show the modal manually
                        $('#deleteModal').modal('show');
                    });
                    $('.edit-btn').on('click', function(event) {
                       event.stopPropagation(); // Prevent the row click event
                       var propNo = $(this).data('propno'); // Get the property number
                    window.location.href = `/3gSurvey/editSurveyForm?newpropertyno=${propNo}&mode=survey`;
                    });
                } else {
                    $('.no-results').show();
                }
            },
            error: function() {
                $('#searchResults').html('<tr><td colspan="9">Error in processing your request.</td></tr>');
                $('.no-results').hide();
            }
        });
    });
});

// Confirm deletion button in modal
$('#confirmDeleteButton').on('click', function() {
    console.log("Deleting...");
    var remarks = $('#deletionRemarks').val();
    if (!remarks.trim()) {
        alert('Please provide remarks for deletion.');
        return;
    }

    console.log("Confirm delete clicked with remarks:", remarks);

    $.ajax({
        url: '/3gSurvey/deleteNewProperty',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            pdNewpropertynoVc: deletePropNo,
            surveyPropNo: deleteSurveyPropNo,
            ward: deleteWard,
            ownerName: deleteOwnerName,
            createdBy: deleteCreatedBy,
            remarks: remarks
        }),
        success: function(response) {
            console.log("Deletion response:", response);
            if (response.success) {
                $('tr[data-propno="' + deletePropNo + '"]').remove();
                $('#deleteModal').modal('hide');
                alert(response.message);
            } else {
                alert(response.message);
            }
        },
        error: function(xhr) {
            console.error("Deletion error:", xhr);
            var errorMessage = xhr.responseJSON && xhr.responseJSON.message ? xhr.responseJSON.message : 'Error deleting record.';
            alert(errorMessage);
        }
    });
});
});