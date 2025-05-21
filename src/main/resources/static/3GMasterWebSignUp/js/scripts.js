function showAlert() {
    alert("User Created successfully! Please reach out to Admin to activate your user");
    return true;
}
 window.onload = function() {
    fetch('/3g/profiles')
        .then(response => response.json())
        .then(data => {
            const selectElement = document.getElementById('profile');
            data.forEach(value => {
                const option = document.createElement('option');
                option.value = value;
                option.text = value;
                selectElement.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching profiles:', error));
};
//const images = document.querySelectorAll('.image-slideshow img');
//let currentIndex = 0;
//
//function nextImage() {
//    images[currentIndex].classList.remove('active');
//    currentIndex = (currentIndex + 1) % images.length;
//    images[currentIndex].classList.add('active');
//}
//
//setInterval(nextImage, 2000);
//
//
//setInterval(nextImage, 2000);