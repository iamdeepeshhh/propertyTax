$(document).ready(function() {
    $.ajax({
        url: '/3g/getCouncilDetails',
        type: 'GET',
        success: function(data) {
            if (data && data.length > 0) {
                const councilDetails = data[0];
                $('.councilName').text(councilDetails.localName || 'नगर परिषद');
//                $('#councilName1').text(councilDetails.localName || 'नगर परिषद');
                if (councilDetails.imageBase64) {
                    $('#councilLogo').attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
                    $('#councilLogo1').attr('src', 'data:image/png;base64,' + councilDetails.imageBase64);
                } else {
                    $('#councilLogo').attr('src', 'https://example.com/fallback-image.png');
                    $('#councilLogo1').attr('src', 'https://example.com/fallback-image.png');
                }
            } else {
                $('#councilLogo').attr('src', 'https://example.com/no-data-image.png');
            }
        },
        error: function(xhr, status, error) {
            console.error('Error fetching council name:', error);
            $('.councilName').text('-');
        }
    });
// Retrieve the property number from local storage
var propertyId = window.location.pathname.split('/').pop();
console.log(propertyId);
if (!propertyId) {
    alert('No property number found.');
    return; // Exit if no property number is found
}

// Construct the URL for the API call
var apiUrl = '/3gSurvey/detailsComplete/' + propertyId;

$.ajax({
    url: apiUrl,
    type: 'GET',
    success: function(data) {
        // Check if data is available
        if(data) {
            console.log(data.propertyDetails)
            // Populating table data
            $('#wardNo').text(data.propertyDetails.pdWardI || '-');
            $('#zoneNo').text(data.propertyDetails.pdZoneI || '-');
            $('#spn').text(data.propertyDetails.pdSurypropnoVc || '-');
            $('#wardNo1').text(data.propertyDetails.pdWardI || '-');
            $('#zoneNo1').text(data.propertyDetails.pdZoneI || '-');
            $('#spn1').text(data.propertyDetails.pdSurypropnoVc || '-');
            $('#wardNo2').text(data.propertyDetails.pdWardI || '-');
            $('#zoneNo2').text(data.propertyDetails.pdZoneI || '-');
            $('#spn2').text(data.propertyDetails.pdSurypropnoVc || '-');
            $('#ownername').html("<b>" + (data.propertyDetails.pdOwnernameVc || '-') + "</b>");
            $('#finalPropNo').text(data.propertyDetails.finalPropNo || '-');
            $('#oldUniquePropNo').text(data.propertyDetails.oldUniquePropNo || '-');
            $('#ownerMobileNo').text(data.propertyDetails.pdContactnoVc || '-');
            $('#oldpropno').text(data.propertyDetails.pdOldpropnoVc || '-');
            $('#oldWard').text(data.propertyDetails.pdOldwardNoVc || '-');
            $('#ownerMobileNo1').text(data.propertyDetails.pdContactnoVc || '-');
            $('#oldpropno1').text(data.propertyDetails.pdOldpropnoVc || '-');
            $('#oldWard1').text(data.propertyDetails.pdOldwardNoVc || '-');
            $('#oldpropno2').text(data.propertyDetails.pdOldpropnoVc || '-');
            $('#oldWard2').text(data.propertyDetails.pdOldwardNoVc || '-');
            $('#ownerMobileNo2').text(data.propertyDetails.pdContactnoVc || '-');
            $('#currentassmtdate').text(data.propertyDetails.pdCurrassesdateDt || '-');
            $('#firstassmtdate').text(data.propertyDetails.pdFirstassesdateDt || '-');
            $('#recentlastassmtdate').text(data.propertyDetails.pdLastassesdateDt || '-');
            $('#constructionyear').text(data.propertyDetails.pdConstYearVc || '-');
            $('#oldtax').text(data.propertyDetails.pdOldTaxVc || '-');
            $('#layoutno').text(data.propertyDetails.pdLayoutnoVc || '-');
            $('#citysurveyno').text(data.propertyDetails.pdCitysurveynoF || '-');
            $('#khasrano').text(data.propertyDetails.pdKhasranoVc || '-');
            $('#plotno').text(data.propertyDetails.pdPlotnoVc || '-');
            $('#ownertype').text(data.propertyDetails.pdOwnertypeVc || '-');
            $('#ownercategory').text(data.propertyDetails.pdCategoryI || '-');
            fetchNameById('/3gSurvey/propertyTypes', data.propertyDetails.pdPropertytypeI, function(name) {
                $('#propertytype').text(name);
            });
            fetchNameById('/3gSurvey/propertySubtype', data.propertyDetails.pdPropertysubtypeI, function(name) {
                $('#propertysubtype').text(name);
            });
            fetchNameById('/3gSurvey/PropertyUsage', data.propertyDetails.pdUsagetypeI, function(name) {
                $('#usagetype').text(name);
            });
            fetchNameById('/3gSurvey/PropertySubUsage', data.propertyDetails.pdUsagesubtypeI, function(name) {
                $('#usagesubtype').text(name);
            });
            fetchNameById('/3gSurvey/buildingType', data.propertyDetails.pdBuildingtypeI, function(name) {
                $('#buildingtype').text(name);
            });
            fetchNameById('/3gSurvey/buildingSubType', data.propertyDetails.pdBuildingsubtypeI, function(name) {
                $('#buildingsubtype').text(name);
            });
            $('#buildstatus').text(data.propertyDetails.pdStatusbuildingVc || '-');
            $('#rainhar').text(data.propertyDetails.pdRainwaterhaverstVc || '-');
            $('#solar').text(data.propertyDetails.pdSolarunitVc || '-');
            $('#email').text(data.propertyDetails.ooo || '-');
            $('#occupiername').text(data.propertyDetails.pdOccupinameF || '-');
            $('#owneroccupation').text(data.propertyDetails.ooo || '-');
            $('#propertyname').text(data.propertyDetails.pdPropertynameVc || '-');
            $('#panno').text(data.propertyDetails.pdPannoVc || '-');
            $('#currenttaxamount').text(data.propertyDetails.ooo || '-');
            $('#seweragetype').text(data.propertyDetails.pdSeweragesType || '-');
            $('#totalplotarea').text(data.propertyDetails.pdPlotareaF + ' चौ.मी' || '-');
            $('#aadharno').text(data.propertyDetails.pdAadharnoVc  || '-');
            $('#builtuparea').text(data.propertyDetails.pdTotbuiltupareaF + ' चौ.मी' || '-');
            $('#arealetout').text(data.propertyDetails.pdArealetoutF + ' चौ.मी' || '-');
            $('#areanotletout').text(data.propertyDetails.pdAreanotletoutF + ' चौ.मी' || '-');
            $('#totassesarea').text(data.propertyDetails.pdAssesareaF + ' चौ.मी' || '-');
            $('#roadwidth').text(data.propertyDetails.pdRoadwidthF + ' मी' || '-');
            $('#roadtype').text(data.propertyDetails.ooo || '-');
            $('#currenttaxpaid').text(data.propertyDetails.ooo  || '-');
            $('#layoutname').text(data.propertyDetails.pdLayoutnameVc  || '-');
            $('#naland').text(data.propertyDetails.pdNadetailsVc || '');
            $('#nadate').text(data.propertyDetails.pdNadateDt  || '-');
            $('#naorder').text(data.propertyDetails.pdNanumberI || '-');
            $('#tpdetails').text(data.propertyDetails.pdTpdetailsVc || '');
            $('#tpdate').text(data.propertyDetails.pdTpdateDt || '-');
            $('#tporder').text(data.propertyDetails.pdTpordernumF || '-');
            $('#tddetails').text(data.propertyDetails.pdTddetailsVc || '-');
            $('#tddate').text(data.propertyDetails.pdTddateDt || '-');
            $('#tdorder').text(data.propertyDetails.pdTdordernumF || '-');
            $('#watercontype').text(data.propertyDetails.pdWaterconntypeVc || '-');
            $('#waterconauth').text(data.propertyDetails.pdWaterconnstatusVc || '-');
            $('#electricconnection').text(data.propertyDetails.pdElectricityconnectionVc || '-');
            $('#electricmeterno').text(data.propertyDetails.pdElemeternoVc || '-');
            $('#electricconsumerNumber').text(data.propertyDetails.pdEleconsumernoVc || '-');
            $('#napameternumber').text(data.propertyDetails.pdMeternoVc || '-');
            $('#napaconsumerno').text(data.propertyDetails.pdConsumernoVc || '-');
            $('#napacondate').text(data.propertyDetails.pdConnectiondateDt_vc || '-');
            $('#napapipesize').text(data.propertyDetails.pdPipesizeF + ' इंच' || '-');
            $('#sewerage').text(data.propertyDetails.pdSeweragesVc || '-');
            $('#lift').text(data.propertyDetails.pdLiftVc || '-');
            $('#stairs').text(data.propertyDetails.pdStairVc || '-');
            $('#toilet').text(data.propertyDetails.pdToiletstatusVc || '-');
            $('#nooftoilets').text(data.propertyDetails.pdToiletsI || '-');
            $('#totalnooffloors').text(data.propertyDetails.pdNooffloorsI || '-');
            $('#totalnoofrooms').text(data.propertyDetails.pdNoofroomsI || '-');
            $('#permission').text(data.propertyDetails.pdPermissionstatusVc || '-');
            $('#permissionno').text(data.propertyDetails.pdPermissionnoVc || '-');
            $('#owneraddress').text(data.propertyDetails.pdPropertyaddressVc || '-');
            $('#newownername').text(data.propertyDetails.pdAddnewownernameVc || '-');

                if(data.propertyDetails.createddateVc) {
                    console.log(data.propertyDetails.createddateVc);
                    var dateTimeParts = data.propertyDetails.createddateVc.split(', ');
                    var timePart = dateTimeParts[0];
                    var datePart = dateTimeParts[1];
                    $('#creationdate').text(datePart || '-');
                    $('#creationtime').text(timePart || '-');
                } else {
                    $('#creationdate').text('-');
                    $('#creationtime').text('-');
                }
                
                if (data.propertyDetails.pdHouseplanT) {
                    var imgElement1 = $('<img>').attr('src', 'data:image/png;base64,' + data.propertyDetails.pdHouseplanT)
                                                .css({width: '100%', height: '100%'});
                    $('#pdHouseplanT').append(imgElement1);
                }
                if (data.propertyDetails.pdHouseplan2T) {
                    var imgElement2 = $('<img>').attr('src', 'data:image/png;base64,' + data.propertyDetails.pdHouseplan2T)
                                                .css({width: '100%', height: '100%'});
                    $('#pdHouseplan2T').append(imgElement2);
                }
                if (data.propertyDetails.pdPropimageT) {
                    var imgElement3 = $('<img>').attr('src', 'data:image/png;base64,' + data.propertyDetails.pdPropimageT)
                                                .css({width: '100%', height: '100%'});
                    $('#pdPropimageT').append(imgElement3);
                }
                if (data.propertyDetails.pdPropimage2T) {
                    var imgElement4 = $('<img>').attr('src', 'data:image/png;base64,' + data.propertyDetails.pdPropimage2T)
                                                .css({width: '100%', height: '100%'});
                    $('#pdPropimage2T').append(imgElement4);
                }


            console.log('Data fetched successfully:', data);
            if (data.unitDetails && data.unitDetails.length > 0) {
                populateUnitsTable(data.unitDetails);
                populateBuiltups(data.unitDetails);
            } else {
                console.log('No units data available.');
            }
        } else {
            alert('No data returned for this property.');
        }
    },
    error: function(xhr, status, error) {
        alert('Error fetching property details: ' + error);
    }
});
});


function groupByUnitNumber(unitsData) {
    const sortedUnits = unitsData.sort((a, b) => {
        const unitA = parseInt(a.udUnitNoVc) || 0;   const unitB = parseInt(b.udUnitNoVc) || 0;
        return unitA - unitB;
    });

    // Group sorted units by `udUnitNoVc`
    const groupedData = new Map();
    sortedUnits.forEach(unit => {
        const unitNumber = unit.udUnitNoVc || '-';
        if (!groupedData.has(unitNumber)) {
            groupedData.set(unitNumber, []);
        }
        groupedData.get(unitNumber).push(unit);
    });

    return groupedData;
}



function populateUnitsTable(unitsData) {
var $table = $('#unitsTable tbody');
$table.find('tr:gt(0)').remove(); // Clear all rows except the header

var totalCarpetArea = 0;
var totalPlotArea = 0;
var totalExemptedArea = 0;
var totalAssessmentArea = 0;

const groupedData = groupByUnitNumber(unitsData);

unitsData.forEach(function(unit, index) {
    console.log(unit.unitBuiltupUps);
    var $row = $('<tr>').attr('height', '30');
    $row.append($('<td>').text(index + 1).attr('align', 'center'));
    $row.append($('<td>').text(unit.udFloorNoVc || '-').attr('align', 'center'));
    $row.append($('<td>').text(unit.udConstYearDt || '-').attr('align', 'center'));
    $row.append($('<td>').text(unit.udConstAgeI || '-').attr('align', 'center'));
    $row.append($('<td>').text(unit.udConstructionClassI || '-').attr('align', 'center'));
    var $usageTypeCell = $('<td>').attr('align', 'center');
    var $usageSubtypeCell = $('<td>').attr('align', 'center');
    var $occupantStatusCell = $('<td>').attr('align', 'center');
    fetchNameById('/3gSurvey/unitUsageType', unit.udUsageTypeI, function(name) {
        $usageTypeCell.text(name);
    });
    fetchNameById('/3gSurvey/unitSubUsageType', unit.udUsageSubtypeI, function(name) {
        $usageSubtypeCell.text(name);
    });
    fetchNameById('/3gSurvey/occupantStatus', unit.udOccupantStatusI, function(name) {
        $occupantStatusCell.text(name);
    });
    $row.append($usageTypeCell);
    $row.append($usageSubtypeCell);
    $row.append($('<td>').text(unit.udAreaBefDedF || '-').attr('align', 'center'));
    $row.append($('<td>').text(unit.udCarpetAreaF || '-').attr('align', 'center'));
    $row.append($('<td>').text(unit.udPlotAreaFl || '-').attr('align', 'center'));
    $row.append($('<td>').text(unit.udExemptedAreaF || '-').attr('align', 'center'));
    $row.append($('<td>').text(unit.udAssessmentAreaF || '-').attr('align', 'center'));
    $row.append($occupantStatusCell);
    $row.append($('<td>').text(unit.udOccupierNameVc || '-').attr('align', 'center'));
    $row.append($('<td>').text(unit.udRentalNoVc || '-').attr('align', 'center'));
    $row.append($('<td>').text(unit.udSignatureVc || '-').attr('align', 'center'));
    $row.append($('<td>').text(unit.udEstablishmentNameVc || '-').attr('align', 'center'));
    $table.append($row);

    // Update totals
    totalCarpetArea += parseFloat(unit.udCarpetAreaF) || 0;
    totalPlotArea += parseFloat(unit.udPlotAreaFl) || 0;
    totalExemptedArea += parseFloat(unit.udExemptedAreaF) || 0;
    totalAssessmentArea += parseFloat(unit.udAssessmentAreaF) || 0;

});

// Append the totals row
var $totalsRow = $('<tr>').attr('height', '30').css('background-color', '#EDEDED');
$totalsRow.append($('<th>')); // Empty cell for the index column
$totalsRow.append($('<td>').attr('colspan', '7').attr('align', 'left').text('एकूण क्षेत्रफळ'));
$totalsRow.append($('<td>').attr('align', 'center').text(totalCarpetArea.toFixed(2) + ' चौ.मी'));
$totalsRow.append($('<td>').attr('align', 'center').text(totalPlotArea.toFixed(2) + ' चौ.मी'));
$totalsRow.append($('<td>').attr('align', 'center').text(totalExemptedArea.toFixed(2) + ' चौ.मी'));
$totalsRow.append($('<td>').attr('align', 'center').text(totalAssessmentArea.toFixed(2) + ' चौ.मी'));
$totalsRow.append($('<td colspan="6">')); // Remaining columns empty
$table.append($totalsRow);
}

function populateBuiltups(unitsData) {
var $table = $('#builtupsTable tbody');
$table.empty(); // Clear the table first

var grandTotalCarpetArea = 0;
var grandTotalExemptedArea = 0;
var grandTotalPlotArea = 0;
var grandTotalAssessableArea = 0;
var grandTotalAuthorizedArea = 0;
var grandTotalUnauthorizedArea = 0;

unitsData.forEach((unit, unitIndex) => {
    var totalCarpetArea = 0;
    var totalExemptedArea = 0;
    var totalPlotArea = 0;
    var totalAssessableArea = 0;
    var totalAuthorizedArea = 0;
    var totalUnauthorizedArea = 0;

    unit.unitBuiltupUps.forEach((builtup, index) => {
        var $row = $('<tr>');

        // Add unit-specific information only for the first built-up
        if (index === 0) {
            $row.append($('<td>').attr('rowspan', unit.unitBuiltupUps.length).text(unit.udUnitNoVc));
            $row.append($('<td>').attr('rowspan', unit.unitBuiltupUps.length).text(unit.udFloorNoVc));
        }

        $row.append($('<td>').text(builtup.ubIdsI || "-"));
        // Add built-up specific information
        var $usageTypeCell = $('<td>').attr('align', 'center');
        var $usageSubtypeCell = $('<td>').attr('align', 'center');
        $row.append($('<td>').text(builtup.ubRoomTypeVc));
            fetchNameById('/3gSurvey/unitUsageType', unit.udUsageTypeI, function(name) {
                $usageTypeCell.text(name);
        });
            fetchNameById('/3gSurvey/unitSubUsageType', unit.udUsageSubtypeI, function(name) {
                $usageSubtypeCell.text(name);
        });
        $row.append($usageTypeCell);
        $row.append($usageSubtypeCell);
        $row.append($('<td>').text(builtup.ubLengthFl +" x "+ builtup.ubBreadthFl).attr('align', 'center'));
        $row.append($('<td>').text(builtup.ubareabefdedFl || "-"));
        $row.append($('<td>').text((builtup.ubDedpercentI || '0')+ " %"));
        $row.append($('<td>').text(builtup.ubCarpetAreaFl || "-"));
        $row.append($('<td>').text(builtup.ubplotareaFl || "-"));
        $row.append($('<td>').text(builtup.ubExemAreaFl || "-"));
        $row.append($('<td>').text(builtup.ubAssesAreaFl));
        $row.append($('<td>').text(builtup.ubLegalAreaFl));
        $row.append($('<td>').text(builtup.ubIllegalAreaFl || "-"));
        $row.append($('<td>').text(unit.udConstructionClassI));
        $row.append($('<td>').text(unit.udConstYearDt || "-"));
        $row.append($('<td>').text(unit.udOccupierNameVc || "-"));
        $row.append($('<td>').text(unit.udRentalNoVc || "-"));
        $row.append($('<td>').text(builtup.udUnitRemarkVc || "-"));
        $row.append($('<td>').text(unit.udEstablishmentNameVc || "-"));
        $table.append($row);

        // Update unit totals
        totalCarpetArea += parseFloat(builtup.ubCarpetAreaFl) || 0;
        totalPlotArea += parseFloat(builtup.ubplotareaFl) || 0;
        totalExemptedArea += parseFloat(builtup.ubExemAreaFl) || 0;
        totalAssessableArea += parseFloat(builtup.ubAssesAreaFl) || 0;
        totalAuthorizedArea += parseFloat(builtup.ubLegalAreaFl) || 0;
        totalUnauthorizedArea += parseFloat(builtup.ubIllegalAreaFl) || 0;
    });

    // Append totals row for this unit
    var $unitTotalRow = $('<tr>').css('font-weight', 'bold');
    $unitTotalRow.append($('<td colspan="9">').text('Total of Unit No ' + (unitIndex + 1)));
    $unitTotalRow.append($('<td>').text(totalCarpetArea.toFixed(2) + ' चौ.मी'));
    $unitTotalRow.append($('<td>').text(totalPlotArea.toFixed(2) + ' चौ.मी'));
    $unitTotalRow.append($('<td>').text(totalExemptedArea.toFixed(2) + ' चौ.मी'));
    $unitTotalRow.append($('<td>').text(totalAssessableArea.toFixed(2) + ' चौ.मी'));
    $unitTotalRow.append($('<td>').text(totalAuthorizedArea.toFixed(2) + ' चौ.मी'));
    $unitTotalRow.append($('<td>').text(totalUnauthorizedArea.toFixed(2) + ' चौ.मी'));
    $unitTotalRow.append($('<td colspan="8">')); // Remaining columns empty
    $table.append($unitTotalRow);

    // Update grand totals
    grandTotalCarpetArea += totalCarpetArea;
    grandTotalPlotArea += totalPlotArea;
    grandTotalExemptedArea += totalExemptedArea;
    grandTotalAssessableArea += totalAssessableArea;
    grandTotalAuthorizedArea += totalAuthorizedArea;
    grandTotalUnauthorizedArea += totalUnauthorizedArea;
});

// Append grand totals row
var $grandTotalRow = $('<tr>').css('font-weight', 'bold', 'background-color', '#EDEDED');
$grandTotalRow.append($('<td colspan="9">').text('Grand Total'));
$grandTotalRow.append($('<td>').text(grandTotalCarpetArea.toFixed(2) + ' चौ.मी'));
$grandTotalRow.append($('<td>').text(grandTotalPlotArea.toFixed(2) + ' चौ.मी'));
$grandTotalRow.append($('<td>').text(grandTotalExemptedArea.toFixed(2) + ' चौ.मी'));
$grandTotalRow.append($('<td>').text(grandTotalAssessableArea.toFixed(2) + ' चौ.मी'));
$grandTotalRow.append($('<td>').text(grandTotalAuthorizedArea.toFixed(2) + ' चौ.मी'));
$grandTotalRow.append($('<td>').text(grandTotalUnauthorizedArea.toFixed(2) + ' चौ.मी'));
$grandTotalRow.append($('<td colspan="8">')); // Remaining columns empty
$table.append($grandTotalRow);
}

function fetchNameById(url, id, callback) {
$.ajax({
    url: url + '/' + id,
    type: 'GET',
    success: function(response) {
        if (response && response.name) {
            callback(response.name);
        } else {
            callback('-');
        }
    },
    error: function(xhr, status, error) {
        console.error('Error fetching name:', error);
        callback('-');
    }
});
}