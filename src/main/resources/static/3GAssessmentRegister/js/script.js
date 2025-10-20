// ✅ Reusable function to build report tax table dynamically
function buildReportTaxTable(tableSelector, templateName = "ASSESSMENT_REGISTER") {
    $.get('/3g/reportTaxConfigs?template=' + templateName, function(configs) {
        if (!configs || configs.length === 0) {
            console.warn('No tax configs received');
            return;
        }

        const table = $(tableSelector);
        const rows = table.find('tr');
        if (rows.lXength > 1) rows.slice(1).remove();

        const headerRow1 = $('<tr class="t-a-c"></tr>').css('background-color', '#CEF6CE');
        const headerRow2 = $('<tr class="t-a-c"></tr>').css('background-color', '#F8E0F7');
        const valueRow   = $('<tr class="t-a-c"></tr>');

        table.append(headerRow1).append(headerRow2).append(valueRow);

        const parentMap = {};
        configs.forEach(cfg => {
            if (!cfg.parentTaxKeyL) parentMap[cfg.taxKeyL] = { parent: cfg, children: [] };
        });
        configs.forEach(cfg => {
            if (cfg.parentTaxKeyL) {
                (parentMap[cfg.parentTaxKeyL] ||= { parent: null, children: [] }).children.push(cfg);
            }
        });

        configs.sort((a,b) => a.sequenceI - b.sequenceI);

        configs.forEach(cfg => {
            if (!cfg.parentTaxKeyL) {
                const group = parentMap[cfg.taxKeyL];
                if (group && group.children.length > 0) {
                    headerRow1.append(`<th colspan="${group.children.length + (cfg.showTotalBl ? 1 : 0)}">${cfg.localNameVc}</th>`);
                    group.children.forEach(child => {
                        headerRow2.append(`<th>${child.localNameVc}</th>`);
                        valueRow.append(`<td id="tax-${child.taxKeyL}"><b>0</b></td>`);
                    });
                    if (cfg.showTotalBl) {
                        headerRow2.append('<th>एकूण</th>');
                        valueRow.append(`<td id="tax-${cfg.taxKeyL}"><b>0</b></td>`);
                    }
                } else {
                    headerRow1.append(`<th rowspan="2">${cfg.localNameVc}</th>`);
                    valueRow.append(`<td id="tax-${cfg.taxKeyL}"><b>0</b></td>`);
                }
            }
        });

        headerRow1.append('<th rowspan="2">एकूण</th>');
        valueRow.append('<td id="totalTax"><b>0</b></td>');
    });
}