
document.addEventListener('DOMContentLoaded', function() {
    // Fetch council details from the backend
    fetch('/3g/getCouncilDetails')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data && data.length > 0 && data[0].standardName) {
                document.getElementById('councilLabel').textContent = data[0].standardName;
            } else {
                console.error('Council details not found or response is invalid.');
                document.getElementById('councilLabel').textContent = '3G Associates';
            }
        })
        .catch(error => {
            console.error('Error fetching council details:', error);
            // Set a fallback value if there's an error
            document.getElementById('councilLabel').textContent = '3G Associates';
        });
});

const images = document.querySelectorAll('.image-slideshow img');
images[0].style.display = 'block';
let currentIndex = 0;

function nextImage() {
    images[currentIndex].style.opacity = '0';
    setTimeout(() => {
        images[currentIndex].style.display = 'none';

        currentIndex = (currentIndex + 1) % images.length;

        images[currentIndex].style.display = 'block';
        setTimeout(() => images[currentIndex].style.opacity = '1', 1);
    }, 100);
}

setInterval(nextImage, 2000);