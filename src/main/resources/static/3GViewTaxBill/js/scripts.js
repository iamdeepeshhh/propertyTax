
    function cancelsearch() {
        document.taxreceipt.action = "https://gadchandurmunicipalcouncil.com/searchtaxcollectiondetails";
        document.taxreceipt.submit();
      }

      function printOut(divId) {
      var printOutContent = document.getElementById(divId).innerHTML;
      var originalContent = document.body.innerHTML;
      document.body.innerHTML = printOutContent;
      window.print();
      document.body.innerHTML = originalContent;
    }


    function cancelsearch() {
      document.taxreceipt.action = "https://gadchandurmunicipalcouncil.com/searchtaxcollectiondetails";
      document.taxreceipt.submit();
}

function printOut(divId) {
var printOutContent = document.getElementById(divId).innerHTML;
var originalContent = document.body.innerHTML;
document.body.innerHTML = printOutContent;
window.print();
document.body.innerHTML = originalContent;
}


