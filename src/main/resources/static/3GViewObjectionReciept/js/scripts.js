
function cancelsearch() {
    window.location.replace('/citizenLogin');
}

function printOut(print) {  var printOutContent = document.querySelector('.print').innerHTML;
var originalContent = document.body.innerHTML;
document.body.innerHTML = printOutContent;
window.print();
document.body.innerHTML = originalContent;
}
