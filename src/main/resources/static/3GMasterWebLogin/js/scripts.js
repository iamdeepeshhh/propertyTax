
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
            if (data && data.length > 0) {
                const council = data[0];
                if (council.standardName) {
                    document.getElementById('councilLabel').textContent = council.standardName;
                }
                // If a council logo is available, show it in the slideshow image
                if (council.imageBase64) {
                    const img = document.querySelector('.image-slideshow img');
                    if (img) {
                        const raw = String(council.imageBase64).trim();
                        // If server already returns a full data URL, use as-is
                        if (/^data:image\/(png|jpeg);base64,/i.test(raw)) {
                            console.log('Using council logo (prefixed data URL)');
                            img.src = raw;
                        } else {
                            // Guess mime from signature when only base64 payload is provided
                            const isPng = raw.startsWith('iVBORw0K');
                            const isJpeg = raw.startsWith('/9j/');
                            const mime = isPng ? 'image/png' : (isJpeg ? 'image/jpeg' : 'image/png');
                            console.log('Using council logo (mime:', mime, ', size:', raw.length, ')');
                            img.src = `data:${mime};base64,${raw}`;
                        }
                        img.alt = (council.localName || council.standardName || 'Council') + ' logo';
                        img.style.display = 'block';
                        img.style.opacity = '1';
                        // Fallback to default if image fails to decode
                        img.onerror = function() {
                            console.warn('Council logo failed to load, falling back to default');
                            this.src = '/3GMasterWebLogin/images/logo.jpg';
                        };
                    }
                } else {
                    console.log('No council imageBase64 provided; keeping default logo');
                }
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
