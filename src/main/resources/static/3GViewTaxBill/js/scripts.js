window.addEventListener("load", function() {
  $.ajax({
    url: '/3g/getCouncilDetails',
    type: 'GET',
    success: function (data) {
      if (data && data.length > 0) {
        const councilDetails = data[0];
        $('.councilLocalName').text(councilDetails.localName);
        if (councilDetails.imageBase64) {
          $('.councilLogo').attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
          $('.standardSiteNameVC').text(councilDetails.standardSiteNameVC);
          $('.localSiteNameVC').text(councilDetails.localSiteNameVC);
          $('.standardDistrictNameVC').text(councilDetails.standardDistrictNameVC);
          $('.localDistrictNameVC').text(councilDetails.localDistrictNameVC);
        }
      }
    },
    error: function () {
      console.error("❌ Failed to fetch council details.");
    }
  });
});

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


