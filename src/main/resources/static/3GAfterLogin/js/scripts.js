document.addEventListener('DOMContentLoaded', function() {
    // Function to fetch council details
    fetch('/3g/getCouncilDetails')
        .then(response => response.json())
        .then(data => {
            // Check if data is an array and has at least one element
            if (Array.isArray(data) && data.length > 0 && data[0].standardName) {
                // Set the council name in the navbar if available
                document.getElementById('councilName').textContent = data[0].standardName;
            } else {
                console.error('Council details not found or response is invalid.');
                // Set a default name if there's an issue with fetching
                document.getElementById('councilName').textContent = '3G Associates';
            }
        })
        .catch(error => {
            console.error('Error fetching council details:', error);
            // Set a default name in case of an error
            document.getElementById('councilName').textContent = '3G Associates';
        });
});
var sessionTimeout = 1 * 60 * 1000;
setTimeout(function() {
    window.location.reload();
}, sessionTimeout);