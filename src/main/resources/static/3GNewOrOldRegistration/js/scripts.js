document.addEventListener('DOMContentLoaded', function() {
fetchAllTypesOfAPI('/3gSurvey/getAllOldWards', 'wardInput');
});
$('#searchButton').click(function() {
    var oldPropertyNo = $('#oldPropertyNumberInput').val().trim();
    var ownerName = $('#ownerNameInput').val().trim();
    var ward = $('#wardInput').val();
    var searchParams = new URLSearchParams();

    if (oldPropertyNo) {
        searchParams.set("oldPropertyNo", oldPropertyNo);
    }
    if (ownerName) {
        searchParams.set("ownerName", ownerName);
    }
    if (ward) {
        searchParams.set("wardNo", ward);
    }

    var baseUrl = "/3gSurvey/searchProperties";
    var fullUrl = baseUrl + "?" + searchParams.toString();

    $.ajax({
        url: fullUrl,
        type: 'GET',
        success: function(data) {
            if (data && data.length > 0) {
                var content = '<table class="table table-hover"><thead class="thead-light"><tr><th>#</th><th>Ref No</th><th>Ward</th><th>Old Property No.</th><th>Owner Name</th></tr></thead><tbody>';
                data.forEach(function(item, index) {
                    content += '<tr class="clickable-row" data-index="' + index + '">' +
                               '<td>' + (index + 1) + '</td>' +
                               '<td>' + item.podRefNoVc + '</td>' +
                               '<td>' + item.podWardI + '</td>' +
                               '<td>' + item.podOldPropNoVc + '</td>' +
                               '<td>' + item.podOwnerNameVc + '</td>' +
                               '</tr>';
                });
                content += '</tbody></table>';
                $('#searchResults').html(content);
                attachRowClickHandlers(data);
            } else {
                $('#searchResults').html('<div class="alert alert-warning">No results found.</div>');
            }
        },
        error: function() {
            $('#searchResults').html('<div class="alert alert-danger">Error in processing your request.</div>');
        }
    });
});

function attachRowClickHandlers(data) {
    $('.clickable-row').on('click', function() {
        var itemIndex = $(this).data('index');
        var selectedItem = data[itemIndex];
        localStorage.setItem('selectedProperty', JSON.stringify(selectedItem));
        window.location.href = '/3gSurvey/newRegistration';
    });
}

$('#newRegistrationButton').click(function() {
    window.location.href = '/3gSurvey/newRegistration'; // Navigate to the registration page
});

async function fetchAllTypesOfAPI(apiUrl, selectElementId, selectedProperty = null) {
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Network response was not ok for URL: ${apiUrl}`);
            }
            return response.json();
        })
        .then(data => {
            const selectElement = document.getElementById(selectElementId);

            // Add a 'Select' option at the top of the dropdown
            const defaultOption = new Option("Select", "");
            selectElement.add(defaultOption);

            // Populate the dropdown with options from the fetched data
            data.forEach(item => {
                const option = new Option(item.name, item.value);
                option.setAttribute('data-name', item.name); // Assuming value is the percentage of deduction
                selectElement.add(option);
            });

            // Set the dropdown value to the default 'Select' or to a specified property, if applicable
            if (selectedProperty) {
                selectElement.value = selectedProperty.podWardI;
            } else {
                selectElement.value = ""; // Ensures 'Select' is selected by default if no property is specified
            }

            // Add event listener for logging the deduction percentage
            selectElement.addEventListener('change', () => logDeductionPercentage(selectElementId.replace('classOfProperty', '')));
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
}
