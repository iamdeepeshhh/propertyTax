window.addEventListener("load", function() {
  $.ajax({
    url: '/3g/getCouncilDetails',
    type: 'GET',
    success: function (data) {
      if (data && data.length > 0) {
        const councilDetails = data[0];
        $('.councilLocalName').text(councilDetails.localName);
        $('.councilLocalNameHeader').text(councilDetails.localName);
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
   $('.taxBillTable').each(function () {
     buildTaxBillTable(this, 'TAX_BILL');
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

function buildTaxBillTable(tableElement, templateName = "TAX_BILL") {
  $.get('/3g/reportTaxConfigs?template=' + templateName, function(configs) {
    if (!configs || configs.length === 0) return;

    const $table = $(tableElement);
    const $tbody = $table.find('tbody');
    $tbody.empty();

    configs.sort((a,b) => a.sequenceI - b.sequenceI);

    configs.forEach(cfg => {
      const row = `
        <tr>
          <td>${cfg.localNameVc}</td>
          <td id="taxArrear-${cfg.taxKeyL}" style="text-align:center;">0</td>
          <td id="taxCurrent-${cfg.taxKeyL}" style="text-align:center;">0</td>
          <td id="taxTotal-${cfg.taxKeyL}" style="text-align:center;">0</td>
        </tr>`;
      $tbody.append(row);
    });

    $tbody.append(`
      <tr class="pinklight">
        <td><b>एकूण कर</b></td>
        <td id="totalArrear" style="text-align:center;"><b>0</b></td>
        <td id="totalCurrent" style="text-align:center;"><b>0</b></td>
        <td id="totalTax" style="text-align:center;"><b>0</b></td>
      </tr>
    `);
  });
}


