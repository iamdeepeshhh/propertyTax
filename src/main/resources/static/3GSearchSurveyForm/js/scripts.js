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

// Keep state for client-side paging
var surveySearchState = { page: 0, size: 200, base: {} };

function renderSurveyRows(data, append) {
    var tbody = $('#searchResults');
    if (!append) tbody.empty();
    if (data && data.length > 0) {
        $('.no-results').hide();
        data.sort((a, b) => {
                function parseSurveyPropNo(propNo) {
                    // Handle + sign: only take the first part before +
                    const mainPart = String(propNo || '').split('+')[0].trim();
                    
                    // Split on '/' if present (e.g., "123/4" → [123,4])
                    const parts = mainPart.split('/').map(Number);
                    return { prefix: parts[0] || 0, suffix: parts[1] || 0 };
                }

                const surveyA = parseSurveyPropNo(a.pdSurypropnoVc);
                const surveyB = parseSurveyPropNo(b.pdSurypropnoVc);

                // Compare prefix first
                if (surveyA.prefix !== surveyB.prefix) return surveyA.prefix - surveyB.prefix;
                // Then suffix
                return surveyA.suffix - surveyB.suffix;
                });

        const existingCount = append ? tbody.find('tr').length : 0;
        data.forEach(function(item, idx) {
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
                <td>${existingCount + idx + 1}</td>
                <td>${item.pdSurypropnoVc || ''}</td>
                <td>${item.pdWardI || ''}</td>
                <td>${item.pdOwnernameVc || ''}</td>
                <td>${item.pdPropertyaddressVc || ''}</td>
                <td>${item.pdFinalpropnoVc || ''}</td>
                <td>${item.pdZoneI || ''}</td>
                <td>${item.user_id || ''}</td>`;
            if (!isDeProfile) {
                row += `<td>${deleteButton}${editButton}</td>`;
            }
            row += `</tr>`;
            tbody.append(row);
        });
        // wire events
        $('.search-result-row').off('click').on('click', function() {
            var propNo = $(this).data('propno');
            window.location.href = '/3gSurvey/showsurvey/' + propNo;
        });
        $('.delete-btn').off('click').on('click', function(event) {
            event.stopPropagation();
            deletePropNo = $(this).data('propno');
            deleteSurveyPropNo = $(this).data('surveypropno');
            deleteOwnerName = $(this).data('ownername');
            deleteCreatedBy = $(this).data('createdby');
            deleteWard = $(this).data('ward');
            $('#deleteModal').modal('show');
        });
        $('.edit-btn').off('click').on('click', function(event) {
            event.stopPropagation();
            var propNo = $(this).data('propno');
            window.location.href = `/3gSurvey/editSurveyForm?newpropertyno=${propNo}&mode=survey`;
        });
    } else if (!append) {
        $('.no-results').show();
    }
}

function updateSurveyPager(hasMore) {
    const pagerId = 'searchResults-pager';
    let pager = document.getElementById(pagerId);
    const tableEl = document.getElementById('searchResults')?.closest('table');
    if (tableEl) {
        if (!pager) {
            pager = document.createElement('div');
            pager.id = pagerId;
            pager.style.textAlign = 'center';
            pager.style.margin = '8px 0 16px 0';
            tableEl.parentNode.insertBefore(pager, tableEl.nextSibling);
        }
        pager.innerHTML = hasMore ? `<button class="btn btn-secondary" id="loadMoreBtn">Load More</button>` : '';
        if (hasMore) {
            document.getElementById('loadMoreBtn').onclick = function() { loadMoreSurvey(); };
        }
    }
}

function fetchSurveyPage(append=false) {
    var params = new URLSearchParams();
    Object.keys(surveySearchState.base || {}).forEach(k => {
        if (surveySearchState.base[k] !== null && surveySearchState.base[k] !== '') params.set(k, surveySearchState.base[k]);
    });
    params.set('page', surveySearchState.page);
    params.set('size', surveySearchState.size);
    var url = '/3gSurvey/searchNewProperties?' + params.toString();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            renderSurveyRows(data, append);
            const hasMore = Array.isArray(data) && data.length >= surveySearchState.size;
            updateSurveyPager(hasMore);
        },
        error: function() {
            if (!append) $('#searchResults').html('<tr><td colspan="9">Error in processing your request.</td></tr>');
            $('.no-results').hide();
        }
    });
}

function loadMoreSurvey() {
    surveySearchState.page += 1;
    fetchSurveyPage(true);
}

$('#searchButton').click(function() {
    var surveyNumber = $('#surveyNumberInput').val().trim();
    var ownerName = $('#ownerNameInput').val().trim();
    var wardNumber = $('#wardNumberInput').val();
    // set base filters
    surveySearchState.base = {
        surveyPropertyNo: surveyNumber || '',
        ownerName: ownerName || '',
        wardNo: wardNumber || ''
    };
    surveySearchState.page = 0; // reset page
    // Ensure user profile loaded, then fetch first page
    fetchAndLogUserProfile().then(function() {
        fetchSurveyPage(false);
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
