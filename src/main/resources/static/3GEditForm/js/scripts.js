    //We have adjusted the script for edit record feature here mentioning the methods which are either created or in which changes are done
    //the changes are done in following methods:
    //1. displayBase64Image : this is created to fetch and show the image which is stored in db
    //2. populateDependentDropdown : this is created for fetching depending dropdowns
    //3. fetchAllTypesOfAPI : this is modified callback parameter is added in this basically we can add one more parameter as a function to call it after this method has been executed
    document.addEventListener('DOMContentLoaded', function () {
        // Initialize property types and setup cascading dropdowns
        initializeDropdown('propertyType', '/3gSurvey/propertytypes', null, false, null);
        // Once the parent dropdown is initialized, set up the dependent dropdowns
        populateDependentDropdown('propertyType', 'propertySubType', '/3gSurvey/propertySubtypes');
        populateDependentDropdown('propertySubType', 'propertyusageType', '/3gSurvey/usageTypes');
        populateDependentDropdown('propertyusageType', 'propertyusageSubType', '/3gSurvey/usageSubtypes');

        populateDependentDropdown('propertyType', 'buildingType', '/3gSurvey/getBuildingTypesByPropertyClassification');
        populateDependentDropdown('buildingType', 'buildingSubType', '/3gSurvey/buildingSubtypes');

        //This eventlistener helps to check whether anything is getting changed in propertyusagetype or not
        document.getElementById('propertyusageType').addEventListener('change', function () { const propertyUsageValue = this.value; updateUnitUsages(propertyUsageValue);
        });
    });
    let cachedPropertyData = {};
    $(document).ready(function() {
        fetchAllTypesOfAPI('/3gSurvey/getAllOldWards', 'oldWard');
        fetchAllTypesOfAPI('/3gSurvey/getAllZones', 'zone');
        fetchAllTypesOfAPI('/3gSurvey/getAllWards', 'ward');
        fetchAllTypesOfAPI('/3gSurvey/getOwnerType', 'ownerType');
        fetchAllTypesOfAPI('/3gSurvey/buildstatuses', 'buildingStatus');
        fetchAllTypesOfAPI('/3gSurvey/ownerCategories', 'category');
        fetchAllTypesOfAPI('/3gSurvey/getSewerageTypes', 'sewerageType');
        fetchAllTypesOfAPI('/3gSurvey/waterConnections', 'waterOptions');
        
        // Fetch dropdown data from the backend
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const propertyId = urlParams.get('newpropertyno');
        console.log('Property ID:', propertyId);
        
        const apiUrl = `/3gSurvey/detailsComplete/${propertyId}`;
        $('#editPropertyForm').prepend('<div id="loading" class="alert alert-info">Loading property details...</div>');

        $.ajax({
            url: apiUrl,
            type: 'GET',
            success: function(data) {
                $('#loading').remove();
                if (data && data.propertyDetails) {
                    console.log('unit details:', data.unitDetails);
                    console.log('Property details:', data);
                    cachedPropertyData = JSON.parse(JSON.stringify(data)); // Deep copy to cachedPropertyData
                    $('#podRef').val(data.propertyDetails.propRefno || ''); // Reference Number
                    $('#zone').val(data.propertyDetails.pdZoneI || ''); // Zone
                    $('#oldWard').val(data.propertyDetails.pdOldwardNoVc || ''); // Old Ward
                    $('#ward').val(data.propertyDetails.pdWardI || ''); // Ward
                    $('#citySurveyNo').val(data.propertyDetails.pdCitysurveynoF || ''); // City Survey No
                    $('#oldPropertyNo').val(data.propertyDetails.pdOldpropnoVc || ''); // Old Property No
                    $('#surveyPropNo').val(data.propertyDetails.pdSurypropnoVc || ''); // Survey Prop No
                    $('#sequenceNo').val(data.propertyDetails.pdIndexnoVc || ''); // Sequence No
                    $('#layoutName').val(data.propertyDetails.pdLayoutnameVc || ''); // Layout Name
                    $('#layoutNo').val(data.propertyDetails.pdLayoutnoVc || ''); // Layout No
                    $('#khasraNo').val(data.propertyDetails.pdKhasranoVc || ''); // Khasra No
                    $('#plotNo').val(data.propertyDetails.pdPlotnoVc || ''); // Plot No
                    $('#ownerName').val(data.propertyDetails.pdOwnernameVc || ''); // Owner Name
                    $('#newOwnerName').val(data.propertyDetails.pdAddnewownernameVc || ''); // New Owner Name
                    $('#occupierName').val(data.propertyDetails.pdOccupinameF || ''); // Occupier Name
                    $('#address').val(data.propertyDetails.pdPropertyaddressVc || ''); // Address
                    $('#ownerType').val(data.propertyDetails.pdOwnertypeVc || ''); // Owner Type
                    $('#buildingStatus').val(data.propertyDetails.pdStatusbuildingVc || ''); // Building Status
                    $('#panNo').val(data.propertyDetails.pdPannoVc || ''); // PAN No
                    $('#aadharNo').val(data.propertyDetails.pdAadharnoVc || ''); // Aadhar No
                    $('#ownermobileNo').val(data.propertyDetails.pdContactnoVc || ''); // Mobile No
                    $('#category').val(data.propertyDetails.pdCategoryI || ''); // Category
                    $('#propertyName').val(data.propertyDetails.pdPropertynameVc || ''); // Property Name
                    $('#buildingValue').val(data.propertyDetails.pdBuildingvalueI || ''); // Building Value
                    $('#plotValue').val(data.propertyDetails.pdPlotvalueF || ''); // Plot Value
                    $('#propertyType').val(data.propertyDetails.pdPropertytypeI || ''); // Property Type
                    initializeDropdown('propertySubType', '/3gSurvey/propertySubtypes', data.propertyDetails.pdPropertysubtypeI, true, data.propertyDetails.pdPropertytypeI);
                    initializeDropdown('propertyusageType', '/3gSurvey/usageTypes', data.propertyDetails.pdUsagetypeI, true, data.propertyDetails.pdPropertysubtypeI);
                    initializeDropdown('propertyusageSubType', '/3gSurvey/usageSubtypes', data.propertyDetails.pdUsagesubtypeI, true, data.propertyDetails.pdUsagetypeI);
                    initializeDropdown('buildingType', '/3gSurvey/getBuildingTypesByPropertyClassification', data.propertyDetails.pdBuildingtypeI, true, data.propertyDetails.pdPropertytypeI);
                    initializeDropdown('buildingSubType', '/3gSurvey/buildingSubtypes', data.propertyDetails.pdBuildingsubtypeI, true, data.propertyDetails.pdBuildingtypeI);
                    $('#constYear').val(splitDatesAndSet(data.propertyDetails.pdConstYearVc) || '');
                    updatePropertyAgeFactor(splitDatesAndSet(data.propertyDetails.pdConstYearVc), data.propertyDetails.createddateVc) 
                    $('#constAgeProperty').val(data.propertyDetails.pdConstAgeI || ''); // Construction Age
                    // $('#ageFactorProperty').val(data.propertyDetails.pdAgeFactorVc || ''); // Age Factor
                    $('#permissionSelection').val(data.propertyDetails.pdPermissionstatusVc || ''); // Permission
                    togglePopup('permissionSelection', 'permissionPopup');
                    $('#permissionNumber').val(data.propertyDetails.pdPermissionnoVc || '');
                    
                    $('#permissionDate').val(extractOrFormatDate(null,data.propertyDetails.pdPermissiondateDt) || '');
                    $('#lift').val(data.propertyDetails.pdLiftVc || '');
                    $('#stair').val(data.propertyDetails.pdStairVc || '');
                    $('#roadWidth').val(data.propertyDetails.pdRoadwidthF || ''); // Road Width

                    $('#toiletSelection').val(data.propertyDetails.pdToiletstatusVc || '');
                    togglePopup('toiletSelection', 'toiletPopup');
                    $('#numberOfToilets').val(data.propertyDetails.pdToiletsI || '');

                    
                    $('#sewerage').val(data.propertyDetails.pdSeweragesVc || '');

                    $('#sewerageType').val(data.propertyDetails.pdSeweragesType || '');
                    $('#waterSelection').val(data.propertyDetails.pdWaterconnstatusVc || '');
                    togglePopup('waterSelection', 'waterPopup');
                    $('#waterOptions').val(data.propertyDetails.pdWaterconntypeVc || '');
                    
                    $('#mcConnection').val(data.propertyDetails.pdMcconnectionVc || '');
                    $('#meterNumber').val(data.propertyDetails.pdMeternoVc || '');
                    $('#consumerNumber').val(data.propertyDetails.pdConsumernoVc || '');
                    $('#connectionDate').val(extractOrFormatDate(data.propertyDetails.pdConnectiondateDt_vc,null) || '');
                    $('#pipeSize').val(data.propertyDetails.pdPipesizeF || '');
                    $('#solar').val(data.propertyDetails.pdSolarunitVc || '');
                    $('#electricitySelection').val(data.propertyDetails.pdElectricityconnectionVc || '');
                    togglePopup('electricitySelection', 'electricityPopup')
                    $('#emeterNumber').val(data.propertyDetails.pdElemeternoVc || '');
                    $('#econsumerNumber').val(data.propertyDetails.pdEleconsumernoVc || '');
                    $('#rainWaterHarvesting').val(data.propertyDetails.pdRainwaterhaverstVc || '');

                    // Additional fields as needed
                    $('#plotArea').val(data.propertyDetails.pdPlotareaF || ''); // Plot Area
                    $('#builtUpArea').val(data.propertyDetails.pdTotbuiltupareaF || ''); // Built-up Area
                    $('#carpetArea').val(data.propertyDetails.pdTotcarpetareaF || ''); // Carpet Area
                    $('#exemptedArea').val(data.propertyDetails.pdTotalexempareaF || ''); // Exempted Area
                    $('#assessableArea').val(data.propertyDetails.pdAssesareaF || ''); // Assessable Area
                    $('#areaLetout').val(data.propertyDetails.pdArealetoutF || ''); // Area Letout
                    $('#areaNotLetout').val(data.propertyDetails.pdAreanotletoutF || ''); // Area Not Letout
                    $('#noofrooms').val(data.propertyDetails.pdNoofroomsI || ''); // Number of Rooms
                    $('#nooffloors').val(data.propertyDetails.pdNooffloorsI || ''); // Number of Floors
                    $('#lastAssmtDate').val(data.propertyDetails.pdLastassesdateDt || ''); // Last Assessment Date

                    $('#naSelection').val(data.propertyDetails.pdNadetailsVc || '');
                    togglePopup('naSelection', 'naPopup');
                    $('#naOrder').val(data.propertyDetails.pdNanumberI || '');
                    $('#naDate').val(data.propertyDetails.pdNadateDt || '');
                    $('#tdSelection').val(data.propertyDetails.pdTddetailsVc || '');
                    togglePopup('tdSelection', 'tdPopup');
                    $('#tdOrder').val(data.propertyDetails.pdTdordernumF || '');
                    $('#tdDate').val(data.propertyDetails.pdTddateDt || '');

                    $('#tpSelection').val(data.propertyDetails.pdTpdetailsVc || '');
                    togglePopup('tpSelection', 'tpPopup');
                    $('#tpOrder').val(data.propertyDetails.pdTpordernumF || '');
                    $('#tpDate').val(data.propertyDetails.pdTpdateDt || '');

                    $('#saledeedSelection').val(data.propertyDetails.pdSaledeedI || '');
                    togglePopup('saledeedSelection', 'saledeedPopup');
                    $('#saledeedDate').val(data.propertyDetails.pdSaledateDt || '');

                    $('#oldRV').val(data.propertyDetails.pdOldrvFl || '');      
                    displayBase64Image(data.propertyDetails.pdPropimageT, 'previewPropertyImage');
                    displayBase64Image(data.propertyDetails.pdPropimage2T, 'previewPropertyImage2');
                    displayBase64Image(data.propertyDetails.pdHouseplanT, 'previewHousePlan1');
                    displayBase64Image(data.propertyDetails.pdHouseplan2T, 'previewHousePlan2');
                    if (data && data.unitDetails) {
                        data.unitDetails.forEach((unit, index) => {
                            addUnit(unit); // Add a new unit
                            const unitIndex = index + 1; // Unit numbering starts from 1
                            initializeDropdown(`unitusageType${unitIndex}`, '/3gSurvey/getUnitUsageByPropUsageId', unit.udUsageTypeI, true, data.propertyDetails.pdUsagetypeI);
                            initializeDropdown(`unitusageSubType${unitIndex}`, '/3gSurvey/getUnitUsageSub', unit.udUsageSubtypeI , true, unit.udUsageTypeI);
                            document.getElementById(`totalcarpetarea${unitIndex}`).value = unit.udCarpetAreaF || '0'; // Carpet Area
                            document.getElementById(`assessablearea${unitIndex}`).value = unit.udAssessmentAreaF || ''; // Built-up Area
                            document.getElementById(`unitNo${unitIndex}`).value = unit.udUnitNoVc || unitIndex; // Unit No
                            document.getElementById(`monthlyrent${unitIndex}`).value = unit.udRentalNoVc || ''; // Monthly Rent (Placeholder)
                            document.getElementById(`tenantno${unitIndex}`).value = unit.udTenantNoI || ''; // Tenant Number
                            document.getElementById(`occupiername${unitIndex}`).value = unit.udOccupierNameVc || ''; // Tenant Name
                            // document.getElementById(`constructionYear${unitIndex}`).value = unit.udConstYearDt || ''; // Construction Year
                            updateUnitAgeFactor(splitDatesAndSet(unit.udConstYearDt), unitIndex, data.propertyDetails.createddateVc);
                            // document.getElementById(`constructionAge${unitIndex}`).value = unit.udConstAgeI || ''; // Construction Age
                            // document.getElementById(`constAgeFactor${unitIndex}`).value = unit.udAgeFactorI || ''; // Construction Age Factor
                            document.getElementById(`remark${unitIndex}`).value = unit.udUnitRemarkVc || ''; // Remark
                            document.getElementById(`umobileNo${unitIndex}`).value = unit.udMobileNoVc || ''; // Mobile No
                            document.getElementById(`email${unitIndex}`).value = unit.udEmailIdVc || ''; // Email
                            document.getElementById(`establishmentName${unitIndex}`).value = unit.udEstablishmentNameVc || '';
                                if(unit && unit.unitBuiltupUps){
                                    unit.unitBuiltupUps = unit.unitBuiltupUps.filter((builtup) => {
                                        const isRowEmpty = Object.values(builtup).every(
                                            (value) => value === null || value === undefined || value === ""
                                        );
                                        return !isRowEmpty;
                                    });
                                    openPopup(unit.udUnitNoVc)
                                    unit.unitBuiltupUps.forEach((builtup) => {
                                        addRow(unit.udUnitNoVc, {
                                            use: builtup.ubRoomTypeVc || '',
                                            measureType: builtup.ubMeasureTypeVc || '',
                                            deduction: builtup.ubDedpercentI || '',
                                            length: builtup.ubLengthFl || '',
                                            breadth: builtup.ubBreadthFl || '',
                                            areaBeforeDeduction: builtup.ubareabefdedFl || '',
                                            carpetArea: builtup.ubCarpetAreaFl || '',
                                            plotArea: builtup.ubplotareaFl || '',
                                            exemption: builtup.ubExemptionsVc || 'no',
                                            exemptionLength: builtup.ubExemLengthFl || '',
                                            exemptionBreadth: builtup.ubExemBreadthFl || '',
                                            exemptionArea: builtup.ubExemAreaFl || '',
                                            assumptionArea: builtup.ubAssesAreaFl || '',
                                            legalIllegal: builtup.ubLegalStVc || '',
                                            legalAssmtArea: builtup.ubLegalAreaFl || '',
                                            illegalAssmtArea: builtup.ubIllegalAreaFl || '',
                                            ubIdsI: builtup.ubIdsI || '',
                                            pdNewpropertynoVc: builtup.pdNewpropertynoVc || '',
                                            udUnitNoVc: builtup.udUnitNoVc || '',
                                            udFloorNoVc: builtup.udFloorNoVc || '',
                                        });
                                    });
                                    saveAndClosePopup(unit.udUnitNoVc);
                                }
                        });
                    }
                } else {
                    $('#editPropertyForm').prepend('<div class="alert alert-warning">No property details found.</div>');
                }
            },
            error: function(xhr, status, error) {
                $('#loading').remove();
                console.error('Error:', error);
                $('#editPropertyForm').prepend('<div class="alert alert-danger">Unable to fetch property details. Please try again later.</div>');
            }
        });

        function appendImage(container, imageData) {
            if (imageData) {
                const imgElement = $('<img>').attr('src', `data:image/png;base64,${imageData}`)
                    .css({ width: '100%', height: '100%' });
                $(container).empty().append(imgElement);
            }
        }
    });
    function openTab(tabId) {
        let tabs = document.getElementsByClassName("tab-content");
        for (let i = 0; i < tabs.length; i++) {
            tabs[i].style.display = "none";
        }
        document.getElementById(tabId).style.display = "block";
    }

    function extractOrFormatDate(datetime = null, isoTimeStamp = null) {
        if (datetime != null) {
            return datetime.split(' ')[0];
        } else if (isoTimeStamp != null) {
            return new Date(isoTimeStamp).toISOString().split('T')[0];
        } else {
            return null;
        }
    }

    function displayBase64Image(base64String, previewId, mimeType = 'image/jpeg'){
        if(base64String){
            const base64Image = `data:${mimeType};base64,${base64String}`;
            document.getElementById(previewId).src = base64Image;
        } else {
            console.warn(`No image data available for ${previewId}`);
        }
    }
    function Next(tabId) {
        openTab(tabId);
    }

    function Back(tabId) {
        openTab(tabId);
    }

    function initializeDropdown(elementId, apiUrl, selectedValue = null, isSubtype = false, usageTypeId = null, callback = null) {
        const select = document.getElementById(elementId);
        if (!select) {
            console.error(`Element not found: ${elementId}`);
            return;
        }

        let finalUrl = apiUrl;
        if (isSubtype && usageTypeId) {
            finalUrl = `${apiUrl}/${usageTypeId}`;
        }

        fetch(finalUrl)
            .then(response => {
                if (!response.ok) throw new Error('Failed to fetch data');
                return response.json();
            })
            .then(data => {
                let optionsHtml = '<option value="">Select</option>';
                data.forEach(item => {
                    const isSelected = selectedValue !== null && selectedValue !== undefined && item.value.toString() === selectedValue.toString() ? ' selected' : '';
                    optionsHtml += `<option value="${item.value}"${isSelected}>${item.name}</option>`;
                });
                select.innerHTML = optionsHtml;

                if (callback) callback();
            })
            .catch(error => {
                console.error(`Error loading data for '${elementId}':`, error);
                select.innerHTML = '<option value="">Error loading data</option>';
            });
    }
    async function populateDependentDropdown(parentId, childId, apiUrl, initialValue = null, callback = null) {
    const parentElement = document.getElementById(parentId);
    const childElement = document.getElementById(childId);

    if (!parentElement) {
        console.error(`Parent element with ID "${parentId}" not found.`);
        return;
    }

    if (!childElement) {
        console.error(`Child element with ID "${childId}" not found.`);
        return;
    }

    // Helper function to fetch and populate the child dropdown
    async function fetchAndPopulate(parentValue, selectedValue = null) {
            childElement.innerHTML = '<option value="">Select</option>';
            if (!parentValue) return;

            try {
                const response = await fetch(`${apiUrl}/${parentValue}`);
                if (!response.ok) throw new Error(`Failed to fetch data for ${childId}`);
                const data = await response.json();

                let optionsHtml = '<option value="">Select</option>';
                data.forEach(item => {
                    const isSelected = item.value === selectedValue ? ' selected' : '';
                    optionsHtml += `<option value="${item.value}"${isSelected}>${item.name}</option>`;
                });
                childElement.innerHTML = optionsHtml;
            } catch (error) {
                console.error(`Error fetching data for ${childId}:`, error);
                childElement.innerHTML = '<option value="">Error loading data</option>';
            }
        }

        // Populate immediately if an initial value is provided
        if (initialValue) {
            fetchAndPopulate(parentElement.value, initialValue);
        }

        // Add change event listener to the parent dropdown
        parentElement.addEventListener('change', function () {
            fetchAndPopulate(this.value);
        });

    }

    function removeSpacesAndCheck(input) {
    input.value = input.value.replace(/\s+/g, '');
    checkSurveyPropNo();
    }
    
    function checkPodRef() {
    document.getElementById('newOwnerName').disabled = document.getElementById('podRef').value.trim() === '';
    }
    document.getElementById('podRef').addEventListener('input', checkPodRef);
    window.onload = checkPodRef;
    
    document.getElementById('sewerage').addEventListener('change', function() {
    document.getElementById('sewerageType').disabled = this.value !== 'Drain';
    });
    function openTab(tabName) {
    
    var tabContents = document.getElementsByClassName("tab-content");
    for (var i = 0; i < tabContents.length; i++) {
    tabContents[i].style.display = "none";
    }
    document.getElementById(tabName + "TabContent").style.display = "block";
    
    }
    
    function Next(tabName) {
    openTab(tabName);
    }
    
    function Back(tabName) {
    openTab(tabName);
    }
    
    document.addEventListener('DOMContentLoaded', function() {
    var selectedProperty = localStorage.getItem('selectedProperty');
    selectedProperty = selectedProperty ? JSON.parse(selectedProperty) : null;
    populateYearDropdown('constYear');
    fetchAndPopulateAssessmentDates();
    
    
    if (selectedProperty) {
    console.log(selectedProperty);
    document.getElementById('podRef').value = selectedProperty.podRefNoVc || '';
    document.getElementById('oldWard').value = selectedProperty.podWardI || '';
    document.getElementById('oldPropertyNo').value = selectedProperty.podOldPropNoVc || '';
    document.getElementById('ownerName').value = selectedProperty.podOwnerNameVc || '';
    document.getElementById('address').value = selectedProperty.podPropertyAddressVc || '';
    document.getElementById('occupierName').value = selectedProperty.podOccupierNameVc || '';
    localStorage.removeItem('selectedProperty');
    }
    
    fetch('/3g/constructionAgeFactor')
    .then(response => response.json())
    .then(data => {
    constructionAgeFactors = data;
    })
    .catch(error => console.error('Error fetching construction age factors:', error));
    
    document.querySelectorAll('[id^="classOfProperty"]').forEach(dropdown => {
    disableFieldsIfClassOfPropertyOp(dropdown);
    });
    });
    
    function disableFieldsIfClassOfPropertyOp(dropdown) {
    console.log("disablefields");
    const unitId = dropdown.id.replace('classOfProperty', '');
    const isOpSelected = dropdown.value === 'op';
    
   
    const selectedOption = dropdown.selectedOptions[0];
    const dataName = selectedOption.getAttribute('data-name') || '';
    console.log(`Selected data-name: ${dataName}`);
    
    
    const shouldDisable = isOpSelected || dataName.toLowerCase().includes('o.p');
    
    document.getElementById(`constructionYear${unitId}`).disabled = shouldDisable;
    document.getElementById(`constructionAge${unitId}`).disabled = shouldDisable;
    document.getElementById(`constAgeFactor${unitId}`).disabled = shouldDisable;
    
    }
    // fetchAllTypesOfAPI('/3gSurvey/getAllZones', 'zone');
    // fetchAllTypesOfAPI('/3gSurvey/getAllWards', 'ward');
    // fetchAllTypesOfAPI('/3gSurvey/getOwnerType', 'ownerType');
    // fetchAllTypesOfAPI('/3gSurvey/buildstatuses', 'buildingStatus');
    // fetchAllTypesOfAPI('/3gSurvey/ownerCategories', 'category');
    // fetchAllTypesOfAPI('/3gSurvey/getSewerageTypes', 'sewerageType');
    // fetchAllTypesOfAPI('/3gSurvey/waterConnections', 'waterOptions');
    
   
   
    //this is used for splitting the date of construction year
    function splitDatesAndSet(dateString) {
        if (!dateString) return null;
    
        const date = new Date(dateString);
        if (isNaN(date)) {
            console.error(`Invalid date string: ${dateString}`);
            return null; // Return null for invalid dates
        }
    
        return date.getFullYear(); // Extract and return the year
    }
    async function fetchAllTypesOfAPI(apiUrl, selectElementId, selectedProperty = null, callback) {
    fetch(apiUrl)
    .then(response => {
    if (!response.ok) {
        throw new Error(`Network response was not ok for URL: ${apiUrl}`);
    }
    return response.json();
    })
    .then(data => {
    const selectElement = document.getElementById(selectElementId);
    const classOfProperty = selectElementId.replace('classOfProperty', '');
    const defaultOption = new Option("Select", "");
    selectElement.add(defaultOption);
    
   
    data.forEach(item => {
        const option = new Option(item.name, item.value);
        option.setAttribute('data-name', item.name); 
        option.setAttribute('data-deduction', item.deduction);
        selectElement.add(option);
    });
    
   
    if (selectedProperty) {
        let isOptionFound = false;

        Array.from(selectElement.options).forEach(option => {
            if (
                String(option.value) === String(selectedProperty) || // Match by value
                option.text === selectedProperty || // Match by text
                option.getAttribute('data-name') === selectedProperty // Match by custom attribute
            ) {
                option.selected = true;
                isOptionFound = true;
                logDeductionPercentage(classOfProperty);
            }
        });

        if (!isOptionFound) {
            console.warn(`Selected property "${selectedProperty}" not found in options.`);
        }
    } else {
        selectElement.value = "";
    }
    
    selectElement.addEventListener('change', () => logDeductionPercentage(selectElementId.replace('classOfProperty', '')));
    
    if (callback) {
        callback();
    }

    })
    .catch(error => {
    console.error('Error fetching data:', error);
    });
    
    }
    function openPopup(unitId) {
    var popupId = 'popup-' + unitId;
    var popup = document.getElementById(popupId);
    
    if (!popup) {
    popup = document.createElement('div');
    popup.id = popupId;
    popup.className = 'popup-container';
    popup.innerHTML = getPopupContent(unitId);
    document.body.appendChild(popup);
    }
    
    popup.style.display = 'block';
    }
    
    
    function getPopupContent(unitId) {
    return `
    <span class="close-btn" onclick="closePopup('${unitId}')">&times;</span>
    <h2 style="text-align: center;">Flat Details</h2>
    <div>
    <form>
    <div id="tableContainer" style="overflow-x: auto; overflow-y: auto; max-height: 400px; padding: 20px; background-color: #fff; border: 1px solid #ccc; box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);">
        <table id="table-${unitId}" style="max-width: auto; height: 100px; overflow: auto;">
        <thead>
            <tr>
                <th>Room</th>
                <th>Use</th>
                <th>Inner/Outer</th>
                <th>Deduction %</th>
                <th>Length</th>
                <th>Breadth</th>
                <th>Area Before Deduction</th>
                <th>Carpet Area</th>
                <th>Plot Area</th>
                <th>Exemption</th>
                <th>Exemption Length</th>
                <th>Exemption Breadth</th>
                <th>Exemption Area</th>
                <th>Assumption Area</th>
                <th>Legal/Illegal</th>
                <th>Legal Assmt Area</th>
                <th>Illegal Assmt Area</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <!-- Rows will be dynamically added here -->
        </tbody>
        <tr id="total-${unitId}" style="background-color: whitesmoke">
                <td colspan="4"><center>Total Area</center></td>
                <td><td><td><input type="number" id="TotalAreaBeforeDeduction-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
                <td><input type="number" id="TotalCarpetArea-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
                <td><input type="number" id="TotalPlotArea-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
                <td></td><td></td><td></td>
                <td><input type="number" id="ExemptionArea1-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
                <td><input type="number" id="AssArea1-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
                <td></td>
                <td><input type="number" id="LegalAssmtArea1-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
                <td><input type="number" id="IllegalAssmtArea1-${unitId}" style="width: 78px; padding: 0px; background-color: white; border-color:white;"></td>
                <td></td>
            </tr>
    </table>
    </div>
    </form>
    </div>
    <button type="button" onclick="saveAndClosePopup('${unitId}')">Save</button>
    <button onclick="addRow('${unitId}')">Add Area</button>
    `;
    }
    //here i am searching survypropno if it exists show error
    let isSurveyPropNoValid = false;
    
    async function checkSurveyPropNo() {
    console.log("Checking Survey Property Number");
    const surveyPropNo = document.getElementById('surveyPropNo').value;
    const ward = document.getElementById('ward').value;
    const messageElement = document.getElementById('surveyPropNoMessage');
    
    if (surveyPropNo && ward) {
    try {
        const response = await fetch(`/3gSurvey/checkSurveyPropNo?surveyPropNo=${surveyPropNo}&ward=${ward}`);
        const exists = await response.json();
    
        if (exists) {
            messageElement.style.display = 'block';
            isSurveyPropNoValid = false;
        } else {
            messageElement.style.display = 'none';
            isSurveyPropNoValid = true;
        }
    } catch (error) {
        console.error('Error checking survey property number:', error);
        messageElement.style.display = 'none';
        isSurveyPropNoValid = false;
    }
    } else {
    messageElement.style.display = 'none';
    isSurveyPropNoValid = false;
    }
    }
    //here done
    
    //here we are populating picklist values so code is written below for it+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    //all code will be written here
    
    async function initializePropertyTypesAndCaptureSelection(selectId, apiUrl) {
    const selectElement = document.getElementById(selectId);
    
    // Fetch the property types from the API and populate the select element.
    try {
    const response = await fetch(apiUrl);
    if (!response.ok) throw new Error('Failed to fetch data');
    const propertyTypes = await response.json();
    
    let optionsHtml = '<option value="">Select</option>';
    propertyTypes.forEach(type => {
        optionsHtml += `<option value="${type.value}" data-standard-name="${type.Standardname.replace(/\s+/g, '').toLowerCase()}">${type.name}</option>`;
    });
    selectElement.innerHTML = optionsHtml;
    } catch (error) {
    console.error(`Error initializing property types for select #${selectId}:`, error);
    }
    
    const conditionValue = 'openland'; // Ensure this is in lowercase and without spaces
    const fieldsToDeactivate = ['constYear', 'constAgeProperty', 'ageFactorProperty', 'currentDateProperty','builtUpArea'];
    
    // Listen for changes to the select element to handle field deactivation.
    selectElement.addEventListener('change', function() {
    const selectedOption = this.options[this.selectedIndex];
    const standardName = selectedOption.getAttribute('data-standard-name');
    handleFieldDeactivation(standardName, conditionValue, fieldsToDeactivate);
    });
    }
    
    
    function handleFieldDeactivation(standardName, conditionValue, fieldsToDeactivate) {
    if (standardName === conditionValue) {
    fieldsToDeactivate.forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (field) {
            field.disabled = true;
            field.value = ''; // Optionally clear the field's value
        }
    });
    } else {
    fieldsToDeactivate.forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (field) {
            field.disabled = false;
        }
    });
    }
    }
    
    async function initializeUnitUsageTypesAndCaptureSelection(propertyTypeId, subtypeSelectId, apiUrl) {
    const subtypeSelectElement = document.getElementById(subtypeSelectId);
    
    // Directly use propertyTypeId (numeric value) to fetch and populate subtypes without listening for changes on a select element
    async function fetchAndPopulateSubtypes() {
    subtypeSelectElement.innerHTML = '<option value="">Select</option>';
    
    if (!propertyTypeId) {
        return;
    }
    
    try {
        // Fetch the property subtypes based on the propertyTypeId.
        const response = await fetch(`${apiUrl}/${propertyTypeId}`);
        if (!response.ok) throw new Error('Failed to fetch property subtypes');
        const propertySubtypes = await response.json();
    
        // Populate the subtype select element with options based on the fetched data.
        let optionsHtml = '<option value="">Select</option>';
        propertySubtypes.forEach(subtype => {
            optionsHtml += `<option value="${subtype.value}">${subtype.name}</option>`;
        });
        subtypeSelectElement.innerHTML = optionsHtml;
    
    } catch (error) {
        console.error(`Error fetching property subtypes for ${propertyTypeId}:`, error);
    }
    }
    
    // Immediately fetch and populate subtypes upon function call
    fetchAndPopulateSubtypes();
    }
    
    //This method is written so that unit usage can be changed whenever propertyusage is changed even on same unit
    function updateUnitUsages(propertyUsageValue) {
        const unitUsageElements = document.querySelectorAll('[id^="unitusageType"]');
        unitUsageElements.forEach((unitUsageElement) => {
            const unitId = unitUsageElement.id.replace('unitusageType', '');
            initializeUnitUsageTypesAndCaptureSelection(propertyUsageValue, `unitusageType${unitId}`, '/3gSurvey/getUnitUsageByPropUsageId');
            populateDependentDropdown(`unitusageType${unitId}`, `unitusageSubType${unitId}`, '/3gSurvey/getUnitUsageSub');
        });
    }

    //this is closure for populatin picklist code++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    function saveAndClosePopup(unitId) {
    var totalCarpetArea = document.getElementById(`TotalCarpetArea-${unitId}`).value;
    var totalplotarea = document.getElementById(`TotalPlotArea-${unitId}`).value;
    var totalExemptedArea = document.getElementById(`ExemptionArea1-${unitId}`).value;
    var totaAssessableArea = document.getElementById(`AssArea1-${unitId}`).value;
    var totallegalarea = document.getElementById(`LegalAssmtArea1-${unitId}`).value;
    var totalillegalarea = document.getElementById(`IllegalAssmtArea1-${unitId}`).value;
    var totalareabefded = document.getElementById(`TotalAreaBeforeDeduction-${unitId}`).value;
    
    if (!totalCarpetArea || isNaN(totalCarpetArea)) {
    alert("Please enter a valid carpet area.");
    return;
    }
    
    document.getElementById(`totalcarpetarea${unitId}`).value = totalCarpetArea;
    document.getElementById(`totalexemptedarea${unitId}`).value = totalExemptedArea;
    document.getElementById(`totalplotarea${unitId}`).value = totalplotarea;
    document.getElementById(`assessablearea${unitId}`).value = totaAssessableArea;
    document.getElementById(`totallegalarea${unitId}`).value = totallegalarea;
    document.getElementById(`totaillegalarea${unitId}`).value = totalillegalarea;
    document.getElementById(`totalareabeforeded${unitId}`).value = totalareabefded
    updateAreas(unitId);
    closePopup(unitId);
    }
    
    function closePopup(unitId) {
    var popup = document.getElementById('popup-' + unitId);
    if (popup) {
    popup.style.display = 'none';
    }
    // document.getElementById('overlay').style.display = 'none';
    }
    

    
    function addRow(unitId, rowData = {}) {
    let tableBody = document.querySelector(`#table-${unitId} tbody`);
    let newRow = tableBody.insertRow();
    let rowId = `row-${unitId}-${tableBody.rows.length}`;
    //basically this is a condition if there is nothing it will set the measuretype as "select" if there is "inner" then dropdown will be selected as "Inner" and if there is "outer" with "Outer"
    const measureType = rowData.measureType === 'inner' ? 'inner' : rowData.measureType === 'outer' ? 'outer' : '';
    
    let cellHtmlContent = [
        `<input type="checkbox" id="room-${rowId}" style="margin-right: 10px;" ${rowData.checked ? 'checked' : ''} disabled>`,
        `<select id="use-${rowId}" style="width: 120px; background-color: white; border-color:black;" onchange="enableDisableFields('${rowId}'); updateAreas('${unitId}'); updateCarpetArea('${rowId}'); checkUseValue('${rowId}','${unitId}');">
            <option></option>
        </select>`,
       `<select id="measureType-${rowId}" style="width: 90px; background-color: white; border-color:black;" onchange="updateCarpetArea('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}');">
            <option value="" ${!measureType ? 'selected' : ''}>Select</option>
            <option value="inner" ${measureType === 'inner' ? 'selected' : ''}>Inner</option>
            <option value="outer" ${measureType === 'outer' ? 'selected' : ''}>Outer</option>
        </select>`,
        `<input id="deduction-${rowId}" type="number" step="0.01" placeholder="Deduction Percentage" value="${rowData.deduction || ''}" disabled />`,
        `<input id="length-${rowId}" type="number" step="0.01" placeholder="Length" value="${rowData.length || ''}" oninput="updateCarpetArea('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" />`,
        `<input id="breadth-${rowId}" type="number" step="0.01" placeholder="Breadth" value="${rowData.breadth || ''}" oninput="updateCarpetArea('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" />`,
        `<input id="areabeforededuction-${rowId}" type="number" step="0.01" placeholder="Area Before Deduction" value="${rowData.areaBeforeDeduction || ''}" readonly />`,
        `<input id="carpetArea-${rowId}" type="number" step="0.01" placeholder="Carpet Area" value="${rowData.carpetArea || ''}" oninput="updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" />`,
        `<input id="plotArea-${rowId}" type="number" step="0.01" placeholder="Plot Area" value="${rowData.plotArea || ''}" oninput="updateAssumptionArea('${rowId}'); updateAreas('${unitId}');" disabled />`,
        `<select id="exemption-${rowId}" style="width: 90px; background-color: white; border-color:black;" onchange="enableDisableFields('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}');">
            <option value="no" ${rowData.exemption === 'no' ? 'selected' : ''}>No</option>
            <option value="yes" ${rowData.exemption === 'yes' ? 'selected' : ''}>Yes</option>
         </select>`,
        `<input id="exemptionLength-${rowId}" type="number" step="0.01" placeholder="Exemption Length" value="${rowData.exemptionLength || ''}" oninput="updateExemptionArea('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" disabled />`,
        `<input id="exemptionBreadth-${rowId}" type="number" step="0.01" placeholder="Exemption Breadth" value="${rowData.exemptionBreadth || ''}" oninput="updateExemptionArea('${rowId}'); updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" disabled />`,
        `<input id="exemptionArea-${rowId}" type="number" step="0.01" placeholder="Exemption Area" value="${rowData.exemptionArea || ''}" oninput="updateAssumptionArea('${rowId}'); updateAreas('${unitId}')" />`,
        `<input id="assumptionArea-${rowId}" type="number" step="0.01" placeholder="Assumption Area" value="${rowData.assumptionArea || ''}" readonly />`,
        `<select id="legalIllegal-${rowId}" style="width: 90px; background-color: white; border-color:black;" onchange="updateLegalIllegalAreas('${rowId}'); updateAreas('${unitId}');">
            <option value="legal" ${rowData.legalIllegal === 'legal' ? 'selected' : ''}>Legal</option>
            <option value="illegal" ${rowData.legalIllegal === 'illegal' ? 'selected' : ''}>Illegal</option>
         </select>`,
        `<input id="legalAssmtArea-${rowId}" type="number" step="0.01" placeholder="Legal Assmt Area" value="${rowData.legalAssmtArea || ''}" disabled />`,
        `<input id="illegalAssmtArea-${rowId}" type="number" step="0.01" placeholder="Illegal Assmt Area" value="${rowData.illegalAssmtArea || ''}" oninput="updateAreas('${unitId}'); updateLegalIllegalAreas('${rowId}');" disabled />`,
        `<input id="pdNewpropertynoVc-${rowId}" type="hidden" value="${rowData.pdNewpropertynoVc || 'null'}">`,
        `<input id="ubIdsI-${rowId}" type="hidden" value="${rowData.ubIdsI || 'null'}">`,
        `<input id="udUnitNoVc-${rowId}" type="hidden" value="${rowData.udUnitNoVc || 'null'}">`,
        `<input id="udFloorNoVc-${rowId}" type="hidden" value="${rowData.udFloorNoVc || 'null'}">`,
        `<button type="button" onclick="removeRow(this, '${unitId}')">&#x1F5D1;</button>`,
    ];

    fetchAllTypesOfAPI('/3gSurvey/roomTypes', `use-${rowId}`, rowData.use, () => {
        checkUseValue(rowId, unitId);
    });

    cellHtmlContent.forEach((html, index) => {
    let cell = newRow.insertCell(index);
    cell.innerHTML = html;
    });
    
    newRow.id = rowId;
    
    enableDisableFields(rowId);
    updateAreas(unitId);
    checkUseValue(rowId, unitId);
    }

    function checkUseValue(rowId, unitId) {
    const selectElement = document.getElementById(`use-${rowId}`);
    const checkboxElement = document.getElementById(`room-${rowId}`);
    const deductionElement = document.getElementById(`deduction-${rowId}`);
    const measureTypeElement = document.getElementById(`measureType-${rowId}`);
    
    if (selectElement.value == 1) {
    checkboxElement.checked = true;
    } else {
    checkboxElement.checked = false;
    }
    
    if (measureTypeElement.value === 'outer' && selectElement.value !== 'open plot') {
    deductionElement.disabled = false;
    } else {
    deductionElement.disabled = true;
    }
    updateAreas(unitId);
    }
    function updateoccupancyfields(unitId){
    // Enable/disable fields based on occupancy selection
    const occupancy = document.getElementById(`occupancy${unitId}`);
    const occupierName = document.getElementById(`occupiername${unitId}`);
    const tenantNo = document.getElementById(`tenantno${unitId}`);
    const monthlyRent = document.getElementById(`monthlyrent${unitId}`);
    const rentalDocument = document.getElementById(`rentalDocument${unitId}`);
    const selectedOption = occupancy.options[occupancy.selectedIndex];
    if (selectedOption.text.toLowerCase() === 'tenant') {
        occupierName.disabled = false;
        tenantNo.disabled = false;
        monthlyRent.disabled = false;
        // rentalDocument.disabled = false;
    } else {
        occupierName.disabled = true;
        tenantNo.disabled = true;
        monthlyRent.disabled = true;
        // rentalDocument.disabled = true;
    }
    updateAreas(unitId)
    }
    
    function logDeductionPercentage(unitId) {
    const classOfPropertyElement = document.querySelector(`#classOfProperty${unitId}`);
    if (classOfPropertyElement) {
    const selectedOption = classOfPropertyElement.options[classOfPropertyElement.selectedIndex];
    const deductionPercentage = parseFloat(selectedOption.getAttribute('data-deduction')) || 0; // Value is the percentage of deduction
    const deductionPercentageField = document.querySelector(`#deductionPercentageField${unitId}`);
    if (deductionPercentageField) {
        deductionPercentageField.value = deductionPercentage + '%'; // Display the percentage
    }
    }
    }
    // Update the updateAreas function to log the deduction percentage during calculation
    function getSelectedOptionText(selectElement) {
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    return selectedOption.getAttribute('data-name') || selectedOption.text;
    }
    
    
    function updateCarpetArea(rowId) {
    let useElement = document.getElementById(`use-${rowId}`);
    const useText = useElement.options[useElement.selectedIndex].text.toLowerCase();
    let length = parseFloat(document.getElementById(`length-${rowId}`).value) || 0;
    let breadth = parseFloat(document.getElementById(`breadth-${rowId}`).value) || 0;
    let measureType = document.getElementById(`measureType-${rowId}`).value;
    let unitCount = rowId.split('-')[1];
    let deductionPercentageField = parseFloat(document.getElementById(`deductionPercentageField${unitCount}`).value) || 0;
    let deductionElement = document.getElementById(`deduction-${rowId}`);
    let carpetAreaElement = document.getElementById(`carpetArea-${rowId}`);
    let areaBeforeDeductionElement = document.getElementById(`areabeforededuction-${rowId}`);
    let plotAreaElement = document.getElementById(`plotArea-${rowId}`);
    
    let areaBeforeDeduction = length * breadth;
    
    if (useText === 'open plot' || useText === 'openplot' || useText === 'openlawn' || useText === 'open lawn') {
    plotAreaElement.value = areaBeforeDeduction ? areaBeforeDeduction.toFixed(2) : '';
    carpetAreaElement.value = '';
    areaBeforeDeductionElement.value = '';
    }else {
        plotAreaElement.value = ''; // Clear plot area
        carpetAreaElement.value = ''; // Reset other related fields if needed
        areaBeforeDeductionElement.value = ''; // Reset other related fields if needed

    if (measureType === "outer") {
    deductionElement.value = deductionPercentageField;
    let carpetArea = areaBeforeDeduction - (areaBeforeDeduction * deductionPercentageField / 100);
    carpetAreaElement.value = carpetArea ? carpetArea.toFixed(2) : '';
    areaBeforeDeductionElement.value = areaBeforeDeduction ? areaBeforeDeduction.toFixed(2) : '';
    } else {
    deductionElement.value = '';
    carpetAreaElement.value = areaBeforeDeduction ? areaBeforeDeduction.toFixed(2) : '';
    areaBeforeDeductionElement.value = '';
    }
    }
    
    updateAssumptionArea(rowId);
    updatePropertySectionTotals();
    }
    
    function updateExemptionArea(rowId) {
    let exemptionLength = parseFloat(document.getElementById(`exemptionLength-${rowId}`).value) || 0;
    let exemptionBreadth = parseFloat(document.getElementById(`exemptionBreadth-${rowId}`).value) || 0;
    let exemptionAreaElement = document.getElementById(`exemptionArea-${rowId}`);
    
    let exemptionArea = exemptionLength * exemptionBreadth;
    exemptionAreaElement.value = exemptionArea ? exemptionArea.toFixed(2) : '';
    }
    
    function updateAssumptionArea(rowId) {
    let carpetArea = parseFloat(document.getElementById(`carpetArea-${rowId}`).value) || 0;
    let plotArea = parseFloat(document.getElementById(`plotArea-${rowId}`).value) || 0;
    let exemptionArea = parseFloat(document.getElementById(`exemptionArea-${rowId}`).value) || 0;
    let assumptionAreaElement = document.getElementById(`assumptionArea-${rowId}`);
    let legalIllegalElement = document.getElementById(`legalIllegal-${rowId}`);
    let legalAssmtAreaElement = document.getElementById(`legalAssmtArea-${rowId}`);
    let illegalAssmtAreaElement = document.getElementById(`illegalAssmtArea-${rowId}`);
    
    let area = plotArea || carpetArea; // Use plot area if available, otherwise use carpet area
    let assumptionArea = area - exemptionArea;
    
    // Ensure assumption area is 0 if area equals exemption area
    if (area === exemptionArea) {
    assumptionArea = 0;
    }
    
    assumptionAreaElement.value = assumptionArea ? assumptionArea.toFixed(2) : '0';
    
    // Handle legal/illegal area
    if (legalIllegalElement.value === 'illegal') {
    let illegalArea = parseFloat(illegalAssmtAreaElement.value) || 0;
    legalAssmtAreaElement.disabled = true;
    illegalAssmtAreaElement.disabled = false;
    legalAssmtAreaElement.value = (assumptionArea - illegalArea) ? (assumptionArea - illegalArea).toFixed(2) : '0';
    } else {
    legalAssmtAreaElement.disabled = false;
    illegalAssmtAreaElement.disabled = true;
    legalAssmtAreaElement.value = assumptionArea ? assumptionArea.toFixed(2) : '0';
    illegalAssmtAreaElement.value = '';
    }
    
    updateAreas(rowId.split('-')[1]); // Update totals for the unit
    updatePropertySectionTotals();
    }
    
    function updateLegalIllegalAreas(rowId) {
    let assumptionArea = parseFloat(document.getElementById(`assumptionArea-${rowId}`).value) || 0;
    let legalIllegalElement = document.getElementById(`legalIllegal-${rowId}`);
    let legalAssmtAreaElement = document.getElementById(`legalAssmtArea-${rowId}`);
    let illegalAssmtAreaElement = document.getElementById(`illegalAssmtArea-${rowId}`);
    
    if (legalIllegalElement.value === 'illegal') {
    let illegalArea = parseFloat(illegalAssmtAreaElement.value) || 0;
    legalAssmtAreaElement.disabled = true;
    illegalAssmtAreaElement.disabled = false;
    legalAssmtAreaElement.value = (assumptionArea - illegalArea) ? (assumptionArea - illegalArea).toFixed(2) : '0';
    } else {
    legalAssmtAreaElement.disabled = false;
    illegalAssmtAreaElement.disabled = true;
    legalAssmtAreaElement.value = assumptionArea ? assumptionArea.toFixed(2) : '0';
    illegalAssmtAreaElement.value = '';
    }
    
    updateAreas(rowId.split('-')[1]); // Update totals for the unit
    updatePropertySectionTotals();
    }
    
    //This is getting used to disable fields such as carpetarea whenever i change the use type in builtup area row then this function is getting used
    function enableDisableFields(rowId) {
    let useElement = document.getElementById(`use-${rowId}`);
    const useText = useElement.options[useElement.selectedIndex].text.toLowerCase();
    let plotAreaElement = document.getElementById(`plotArea-${rowId}`);
    let carpetAreaElement = document.getElementById(`carpetArea-${rowId}`);
    let measureTypeElement = document.getElementById(`measureType-${rowId}`);
    let exemptionElement = document.getElementById(`exemption-${rowId}`);
    let exemptionLengthElement = document.getElementById(`exemptionLength-${rowId}`);
    let exemptionBreadthElement = document.getElementById(`exemptionBreadth-${rowId}`);
    let exemptionAreaElement = document.getElementById(`exemptionArea-${rowId}`);
    let deductionElement = document.getElementById(`deduction-${rowId}`);
    let areaBeforeDeductionElement = document.getElementById(`areabeforededuction-${rowId}`);
    
    if (measureTypeElement.value === 'outer') {
    areaBeforeDeductionElement.disabled = false;
    deductionElement.disabled = false;
    } else {
    areaBeforeDeductionElement.disabled = true;
    deductionElement.disabled = true;
    }
    if (useText === 'open plot' || useText == 'openplot' || useText == 'op' || useText == 'o.p') {
    plotAreaElement.disabled = false;
    carpetAreaElement.disabled = true;
    measureTypeElement.disabled = true; // Disable inner/outer options
    areaBeforeDeductionElement.disabled = true;
    } else {
    plotAreaElement.disabled = true;
    carpetAreaElement.disabled = false;
    measureTypeElement.disabled = false; // Enable inner/outer options for other uses
    }
    
    if (exemptionElement && exemptionElement.value === 'yes') {
    exemptionLengthElement.disabled = useText === 'open plot';
    exemptionBreadthElement.disabled = useText === 'open plot';
    exemptionAreaElement.disabled = false;
    } else {
    exemptionLengthElement.disabled = true;
    exemptionBreadthElement.disabled = true;
    exemptionAreaElement.disabled = true;
    }
    }
    
    function updateAreas(unitId) {
    let totalCarpetArea = 0, totalExemptionArea = 0, totalAssumptionArea = 0;
    let totalPlotArea = 0, totalAreaBeforeDeduction = 0; // Added totalAreaBeforeDeduction
    let unitRoomCount = 0;
    let totalLegalArea = 0, totalIllegalArea = 0;
    let tableBody = document.querySelector(`#table-${unitId} tbody`);
    let rows = tableBody.querySelectorAll('tr:not(#total-' + unitId + ')');
    
    rows.forEach(row => {
    let rowId = row.id;
    let useElement = document.getElementById(`use-${rowId}`);
    const useText = useElement.options[useElement.selectedIndex].text.toLowerCase();
    
    let carpetArea = parseFloat(document.getElementById(`carpetArea-${rowId}`).value) || 0;
    let plotArea = parseFloat(document.getElementById(`plotArea-${rowId}`).value) || 0;
    let exemptionArea = parseFloat(document.getElementById(`exemptionArea-${rowId}`).value) || 0;
    let assumptionArea = parseFloat(document.getElementById(`assumptionArea-${rowId}`).value) || 0;
    let legalAssmtArea = parseFloat(document.getElementById(`legalAssmtArea-${rowId}`).value) || 0;
    let illegalAssmtArea = parseFloat(document.getElementById(`illegalAssmtArea-${rowId}`).value) || 0;
    let areaBeforeDeduction = parseFloat(document.getElementById(`areabeforededuction-${rowId}`).value) || 0; // Added line
    
    if (useText !== 'open plot') {
        totalCarpetArea += carpetArea;
    }
    totalPlotArea += plotArea;
    totalExemptionArea += exemptionArea;
    totalAssumptionArea += assumptionArea;
    totalLegalArea += legalAssmtArea;
    totalIllegalArea += illegalAssmtArea;
    totalAreaBeforeDeduction += areaBeforeDeduction; // Added line
    
    const checkbox = row.querySelector(`input[type="checkbox"]`);
    if (checkbox && checkbox.checked) {
    unitRoomCount++; // Increment the unit room count only if the checkbox is checked
    }
    });
    
    document.getElementById(`TotalCarpetArea-${unitId}`).value = totalCarpetArea ? totalCarpetArea.toFixed(2) : '0';
    document.getElementById(`TotalPlotArea-${unitId}`).value = totalPlotArea ? totalPlotArea.toFixed(2) : '0';
    document.getElementById(`ExemptionArea1-${unitId}`).value = totalExemptionArea ? totalExemptionArea.toFixed(2) : '0';
    document.getElementById(`AssArea1-${unitId}`).value = totalAssumptionArea ? totalAssumptionArea.toFixed(2) : '0';
    document.getElementById(`LegalAssmtArea1-${unitId}`).value = totalLegalArea ? totalLegalArea.toFixed(2) : '0';
    document.getElementById(`IllegalAssmtArea1-${unitId}`).value = totalIllegalArea ? totalIllegalArea.toFixed(2) : '0';
    document.getElementById(`TotalAreaBeforeDeduction-${unitId}`).value = totalAreaBeforeDeduction ? totalAreaBeforeDeduction.toFixed(2) : '0'; // Added line
    document.querySelector(`#unitrooms${unitId}`).value = unitRoomCount;
    updatePropertySectionTotals();
    }
    
    function updatePropertySectionTotals() {
    let totalCarpetArea = 0, totalExemptionArea = 0, totalAssumptionArea = 0;
    let totalPlotArea = 0;
    let totalLetOutArea = 0, totalNotLetOutArea = 0;
    let floors = new Set();  // Set to keep track of unique floors
    let totalRooms = 0;      // Variable to count the total number of rooms
    
    // Get all unit totals and sum them up
    document.querySelectorAll('[id^="TotalCarpetArea-"]').forEach(input => {
    const unitId = input.id.split('-')[1]; // Extract unitId from the input id
    const occupancyElement = document.querySelector(`#occupancy${unitId}`);
    const floorElement = document.querySelector(`#floorNo${unitId}`);
    const useElement = document.querySelector(`#use-${unitId}`);
    
    let carpetArea = parseFloat(input.value) || 0;
    let plotArea = parseFloat(document.querySelector(`#TotalPlotArea-${unitId}`).value) || 0;
    let isUnitFloorOpen = floorElement && floorElement.value.toLowerCase() === 'open';
    
    if (!isUnitFloorOpen) {
    totalCarpetArea += carpetArea;
    totalPlotArea += plotArea;
    
    if (occupancyElement) {
        const selectedIndex = occupancyElement.selectedIndex;
    
        const selectedOption = selectedIndex >= 0 ? occupancyElement.options[selectedIndex] : null;
    
        if (selectedOption) {
            const occupancyName = selectedOption.textContent || '';
            if (occupancyName.toLowerCase() === 'tenant') {
                totalLetOutArea += carpetArea;
            } else {
                totalNotLetOutArea += carpetArea;
            }
        } 
        // else {
        //     console.warn(`No valid option selected for occupancyElement with unitId: ${unitId}`);
        // }
    } else {
        console.warn(`Occupancy element not found for unitId: ${unitId}`);
    }
    }
    
    if (!isUnitFloorOpen && floorElement && floorElement.value) {
    floors.add(floorElement.value);
    }
    
    const tableBody = document.querySelector(`#table-${unitId} tbody`);
    if (tableBody) {
    tableBody.querySelectorAll('tr').forEach(row => {
        const checkbox = row.querySelector(`input[type="checkbox"]`);
        if (checkbox && checkbox.checked) {
            totalRooms++; // Increment the total rooms only if the checkbox is checked
        }
    });
    }
    });
    
    document.querySelectorAll('[id^="ExemptionArea1-"]').forEach(input => {
    totalExemptionArea += parseFloat(input.value) || 0;
    });
    
    document.querySelectorAll('[id^="AssArea1-"]').forEach(input => {
    totalAssumptionArea += parseFloat(input.value) || 0;
    });
    
    // Update plot area only if it is greater than zero
    if (totalPlotArea > 0) {
    document.getElementById('plotArea').value = totalPlotArea.toFixed(2);
    } else {
    // If no total plot area from built-up, keep the manually entered plot area
    let manualPlotArea = parseFloat(document.getElementById('plotArea').value) || 0;
    totalPlotArea = manualPlotArea;
    }
    // Update main property section inputs
    document.getElementById('carpetArea').value = totalCarpetArea.toFixed(2);
    document.getElementById('exemptedArea').value = totalExemptionArea.toFixed(2);
    document.getElementById('assessableArea').value = totalAssumptionArea.toFixed(2);
    document.getElementById('areaLetout').value = totalLetOutArea.toFixed(2);
    document.getElementById('areaNotLetout').value = totalNotLetOutArea.toFixed(2);
    document.getElementById('nooffloors').value = floors.size;  // Update the number of floors
    document.getElementById('noofrooms').value = totalRooms;  // Update the total number of rooms
    }
    function createNumberInput(name) {
    var input = document.createElement("input");
    input.type = "number";
    input.name = name;
    input.style = "width: 78px; padding: 0px; background-color: white; border-color:white;";
    return input;
    }
    
    function removeRow(button, unitId) {
    let row = button.closest('tr');
    row.parentNode.removeChild(row);
    updateAreas(unitId);
    }
    
    //this is getting used for removing uploaded image from given id's the first parameter is imageplacement field's id and second is for conditionvalue if its zero then image wont update.
    function cancelUpload(inputId, conditionId) {
    const conditionField = document.getElementById(conditionId);
    conditionField.value = "0";
    
    var fileInput = document.getElementById(inputId);
    if (fileInput) {
    fileInput.value = '';
    }
    
    // Optionally, clear the image preview if one exists
    // This assumes you have a corresponding image preview element for each input
    var previewId = 'preview' + inputId.charAt(0).toUpperCase() + inputId.slice(1);
    var previewImage = document.getElementById(previewId);
    if (previewImage) {
    previewImage.src = '';
    }
    
    // Optionally, you might want to log or alert if the input or preview elements are not found
    if (!fileInput) {
    console.error('Failed to find file input:', inputId);
    }
    if (!previewImage) {
    console.error('Failed to find preview image:', previewId);
    }
    }
    
    function togglePopup(selectId, popupId) {
    var selectBox = document.getElementById(selectId);
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;
    var popup = document.getElementById(popupId);
    
    if (selectedValue == "yes") {
    popup.style.display = "block";
    } else {
    popup.style.display = "none";
    }
    }
    
    // <!--    function showSignaturePopup() {-->
    // <!--        var signaturePopup = document.getElementById("signaturePopup");-->
    // <!--        signaturePopup.style.display = "block";-->
    // <!--    }-->
    
    // <!--    function hideSignaturePopup() {-->
    // <!--        var signaturePopup = document.getElementById("signaturePopup");-->
    // <!--        signaturePopup.style.display = "none";-->
    // <!--    }-->
    
    
    // <!--    function initCanvas() {-->
    // <!--        var canvas = document.getElementById("signatureCanvas");-->
    // <!--        var ctx = canvas.getContext("2d");-->
    // <!--        var isDrawing = false;-->
    // <!--        var lastX = 0;-->
    // <!--        var lastY = 0;-->
    
    // <!--        function draw(e) {-->
    // <!--            if (!isDrawing) return;-->
    
    // <!--            e.preventDefault();-->
    
    // <!--            var posX = e.clientX || e.touches[0].clientX;-->
    // <!--            var posY = e.clientY || e.touches[0].clientY;-->
    // <!--            var rect = canvas.getBoundingClientRect();-->
    
    // <!--            posX = (posX - rect.left) / (rect.right - rect.left) * canvas.width;-->
    // <!--            posY = (posY - rect.top) / (rect.bottom - rect.top) * canvas.height;-->
    
    // <!--            ctx.beginPath();-->
    // <!--            ctx.moveTo(lastX, lastY);-->
    // <!--            ctx.lineTo(posX, posY);-->
    // <!--            ctx.strokeStyle = 'black';-->
    // <!--            ctx.lineWidth = 2;-->
    // <!--            ctx.stroke();-->
    // <!--            lastX = posX;-->
    // <!--            lastY = posY;-->
    // <!--        }-->
    
    // <!--        function startPosition(e) {-->
    // <!--            isDrawing = true;-->
    // <!--            draw(e);-->
    // <!--        }-->
    
    // <!--        function finishedPosition() {-->
    // <!--            isDrawing = false;-->
    // <!--            ctx.beginPath();-->
    // <!--        }-->
    
    // <!--        canvas.addEventListener('mousedown', startPosition);-->
    // <!--        canvas.addEventListener('mousemove', draw);-->
    // <!--        canvas.addEventListener('mouseup', finishedPosition);-->
    // <!--        canvas.addEventListener('mouseout', finishedPosition);-->
    
    // <!--        canvas.addEventListener('touchstart', startPosition, { passive: false });-->
    // <!--        canvas.addEventListener('touchmove', draw, { passive: false });-->
    // <!--        canvas.addEventListener('touchend', finishedPosition, { passive: false });-->
    // <!--    }-->
    // <!--    function resizeCanvas() {-->
    // <!--        var canvas = document.getElementById("signatureCanvas");-->
    // <!--        var signaturePopup = document.getElementById("signaturePopup");-->
    // <!--        var popupContent = document.getElementById("popupContent");-->
    // <!--        canvas.width = popupContent.offsetWidth;-->
    // <!--        canvas.height = popupContent.offsetHeight;-->
    // <!--    }-->
    // <!--    window.addEventListener('resize', resizeCanvas);-->
    // <!--    window.onload = function () {-->
    // <!--        initCanvas();-->
    // <!--        resizeCanvas();-->
    // <!--    };-->
    
    // <!--    function clearSignature(event) {-->
    // <!--        event.preventDefault();-->
    // <!--        var canvas = document.getElementById("signatureCanvas");-->
    // <!--        var ctx = canvas.getContext("2d");-->
    // <!--        ctx.clearRect(0, 0, canvas.width, canvas.height);-->
    // <!--    }-->
    
    // <!--    function saveSignature(event) {-->
    // <!--        event.preventDefault();-->
    // <!--        var canvas = document.getElementById("signatureCanvas");-->
    // <!--        var signature = canvas.toDataURL();-->
    // <!--        document.getElementById("signature").value = signature;-->
    // <!--        alert("Record saved");-->
    // <!--        hideSignaturePopup();-->
    // <!--    }-->
    
    var activeFormId = null;
    
    function toggleForm(formId) {
    console.log("Toggling form:", formId);
    var form = document.getElementById(formId);
    
    var allForms = document.getElementsByClassName("formContainer");
    for (var i = 0; i < allForms.length; i++) {
    if (allForms[i].id !== formId) {
        allForms[i].style.display = "none";
    }
    }
    
    if(form){
    if (form.style.display === "block") {
    form.style.display = "none";
    activeFormId = null;
    }
    else {
    if (activeFormId && activeFormId !== formId) {
        var activeForm = document.getElementById(activeFormId);
        if (activeForm) {
            activeForm.style.display = "none";
        }
    }
    form.style.display = "block";
    activeFormId = formId;
    }
    }else {
    console.error("Form with ID " + formId + " not found.");
    }
    }
    
    document.getElementById('propertyusageType').addEventListener('change', function() {
    const propertyTypeId = this.value; // Get the selected property type ID
    updateFormsBasedOnPropertyType(propertyTypeId);
    });
    
    var unitCount = 1;
    function addUnit(unitDetails = {}) {
    var existingUnitButtons = document.querySelectorAll('.unitbutton').length; //Added to make unit no dynamic and uniform
    unitCount = existingUnitButtons + 1;
    var unitButtons = document.getElementById("unitButtons");
    var formContainers = document.getElementById("formContainers");
    var existingUnitButtons = document.querySelectorAll('.unitbutton').length;
    unitCount = existingUnitButtons + 1;
    var formId = "myForm" + unitCount;
    
    var button = document.createElement("button");
    button.textContent = "Unit " + unitCount;
    button.className = "unitbutton";
    button.id = "unitButton" + unitCount;
    button.type = "button";
    button.onclick = function() { toggleForm(formId); };
    unitButtons.appendChild(button);
    
    var formContainer = document.createElement("div");
    formContainer.id = formId;
    formContainer.className = "formContainer";
    formContainer.style.display = "none";
    
    var form = document.createElement("form");
    form.innerHTML = getFormInnerHTML(unitCount,unitDetails);
    formContainer.appendChild(form);
    
    document.getElementById("formContainers").appendChild(formContainer); //added for testing purpose
    
    fetchAllTypesOfAPI('/3gSurvey/getUnitFloorNos', `floorNo${unitCount}`, unitDetails.udFloorNoVc || null);
    fetchAllTypesOfAPI('/3gSurvey/constructionClassMasters', `classOfProperty${unitCount}`, unitDetails.udConstructionClassI || null);
    fetchAllTypesOfAPI('/3gSurvey/occupancyMasters', `occupancy${unitCount}`, unitDetails.udOccupantStatusI || null);
    fetchAllTypesOfAPI('/3gSurvey/getAllRemarks', `remark${unitCount}`, unitDetails.udUnitRemarkVc || null);
    // Only update forms for the newly added unit
    const currentPropertyTypeId = document.getElementById('propertyusageType').value;
    if (currentPropertyTypeId) {
        initializeUnitUsageTypesAndCaptureSelection(currentPropertyTypeId, `unitusageType${unitCount}`, '/3gSurvey/getUnitUsageByPropUsageId');
        populateDependentDropdown(`unitusageType${unitCount}`, `unitusageSubType${unitCount}`, '/3gSurvey/getUnitUsageSub');
    }
    
    var deleteButton = document.createElement("button");
    deleteButton.textContent = "Delete Unit " + unitCount;
    deleteButton.className = "deletebtn";
    deleteButton.id = "deleteButton" + unitCount; // Unique delete button ID
    deleteButton.type = "button";
    
    deleteButton.onclick = function () {
        removeUnit(formId);
    }
    
    form.appendChild(deleteButton);
    
    formContainers.appendChild(formContainer);
    
    formContainer.appendChild(form);
    var allForms = formContainers.getElementsByClassName("formContainer");
    for (var i = 0; i < allForms.length; i++) {
        allForms[i].style.display = "none";
    }
    
    formContainer.style.display = "block";
    unitCount++;
    
    document.getElementById('formContainers').addEventListener('change', function (event) {
        if (event.target && event.target.id === `classOfProperty${unitCount}`) {
            disableFieldsIfClassOfPropertyOp(event.target);
        }
    });
    
    
    // Initial call to handle preselected value
    // disableFieldsIfClassOfPropertyOp(document.getElementById(`classOfProperty${unitCount}`));
    updatePropertySectionTotals();
    }
    
    function updateFormsBasedOnPropertyType(propertyTypeId) {
        for (let i = 1; i <= unitCount; i++) {
            const subtypeSelectId = `unitusageType${i}`;
            const apiUrl = '/3gSurvey/getUnitUsageByPropUsageId';
            // Update forms only if they are newly added or empty
        if (!document.getElementById(subtypeSelectId).value) {
            initializeUnitUsageTypesAndCaptureSelection(propertyTypeId, subtypeSelectId, apiUrl);
        }
        }
    }
    
    function populateYearDropdown(dropdownId) {
    const yearDropdown = document.getElementById(dropdownId);
    const currentYear = new Date().getFullYear();
    let yearOptions = '<option value="">Select Year</option>';
    for (let year = currentYear; year >= 1800; year--) {
    yearOptions += `<option value="${year}">${year}</option>`;
    }
    yearDropdown.innerHTML = yearOptions;
    }
    
    function fetchAndPopulateAssessmentDates() {
    fetch('/3gSurvey/getAllAssessmentDates')
    .then(response => response.json())
    .then(data => {
        if (data.length > 0) {  const { currentAssessmentDate, lastAssessmentDate, firstAssessmentDate } = data[0];
        document.getElementById('currentAssmtDate').value = currentAssessmentDate;
        document.getElementById('lastAssmtDate').value = lastAssessmentDate;
        document.getElementById('firstAssmtDate').value = firstAssessmentDate;
        }
    })
    .catch(error => console.error('Error fetching assessment dates:', error));
    }
    
    function getFormInnerHTML(unitCount, unitDetails = {}) {
    // Generate year options dynamically
    const currentYear = new Date().getFullYear();
    let yearOptions = '<option value="">Select Year</option>';
    const constructionYear = splitDatesAndSet(unitDetails.udConstYearDt) || ''; // Get the construction year
    for (let year = currentYear; year >= 1900; year--) {
        const isSelected = String(constructionYear) === String(year) ? 'selected' : '';
        yearOptions += `<option value="${year}" ${isSelected}>${year}</option>`;
    }
    return '<label for="floorNo' + unitCount + '" class="mandatory">Floor No:</label>' +
    '<select id="floorNo' + unitCount + '" name="floorNo">' +
    '</select><br>' +
    '<label for="occupancy' + unitCount + '" class="mandatory">Occupancy:</label>' +
    '<select id="occupancy' + unitCount + '" name="occupancy" onchange="updateoccupancyfields(' + unitCount + ')">' +
    '</select><br>' +
    '<label for="monthlyrent' + unitCount + '">Monthly Rent:</label>' +
    '<input type="number" style="width: 90%;" id="monthlyrent' + unitCount + '" name="monthlyrent" disabled><br>' +
    '<label for="tenantno' + unitCount + '" class="mandatory">Tenant Number:</label>' +
    '<input type="text" id="tenantno' + unitCount + '" name="tenantno" disabled><br>' +
    '<label for="occupiername' + unitCount + '">Tenant Name:</label>' +
    '<input type="text" id="occupiername' + unitCount + '" name="occupiername" disabled><br>' +
    // '<label for="rentalDocument' + unitCount + '">Rental Document:</label>' +
    // '<input type="file" id="rentalDocument' + unitCount + '" name="rentalDocument" disabled><br><br>' +
    '<label for="unitNo' + unitCount + '" class="mandatory">Unit No.:</label>' +
    '<input type="text" id="unitNo' + unitCount + '" name="unitNo" value="' + unitCount + '" readonly class="disabled-field"><br>' +
    '<label id="umobileNo" for="umobileNo' + unitCount + '">Mobile No:</label>' +
    '<input type="text" id="umobileNo' + unitCount + '" name="umobileNo"><br>' +
    '<label for="email' + unitCount + '">Email:</label>' +
    '<input type="text" id="email' + unitCount + '" name="email"><br>' +
    '<label for="unitusageType' + unitCount + '" class="mandatory">Usage Type:</label>' +
    '<select id="unitusageType' + unitCount + '" name="unitusageType">' +
    '</select><br>' +
    '<label for="unitusageSubType' + unitCount + '" class="mandatory">Usage Sub Type:</label>' +
    '<select id="unitusageSubType' + unitCount + '" name="unitusageSubType">' +
    '</select><br>' +
    '<label for="classOfProperty' + unitCount + '" class="mandatory">Class Of Property:</label>' +
    '<select id="classOfProperty' + unitCount + '" name="classOfProperty" onchange="disableFieldsIfClassOfPropertyOp(this)">' +
    '</select><br>' +
    '<label for="deductionPercentageField' + unitCount + '">% of Deduction:</label>' +
    '<input type="text" id="deductionPercentageField' + unitCount + '" name="deductionPercentageField" disabled><br>' +
    '<label for="establishmentName' + unitCount + '">Establishment Name:</label>' +
    '<input type="text" id="establishmentName' + unitCount + '" name="establishmentName"><br>' +
    '<label for="constructionYear' + unitCount + '">Construction Year:</label>' +
    '<select id="constructionYear' + unitCount + '" name="constructionYear" onchange="updateUnitAgeFactor(this.value, ' + unitCount + ')">' +
    yearOptions +
    '</select><br>' +
    '<label for="constructionAge' + unitCount + '">Construction Age:</label>' +
    '<input type="text" id="constructionAge' + unitCount + '" name="constructionAge" readonly><br>' +
    '<label for="constAgeFactor' + unitCount + '">Const. Age Factor:</label>' +
    '<input type="text" id="constAgeFactor' + unitCount + '" readonly class="disabled-field" name="constAgeFactor"><br>' +
    '<input type="hidden" id="agefactorunithid' + unitCount + '">'+
    '<input type="text" id="currentDateUnit' + unitCount + '" name="currentDateUnit">'+
    '<label id="unitarea" for="unitArea' + unitCount + '">Unit Area:</label>' +
    '<input type="text" id="unitArea' + unitCount + '" name="unitArea" onclick="openPopup(' + unitCount + ')"><br>' +
    '<label id="unitrooms" for="unitrooms' + unitCount + '">Rooms:</label>' +
    '<input type="text" id="unitrooms' + unitCount + '" name="unitrooms" ><br>' +
    '<label id="totalplotarea" for="totalplotarea' + unitCount + '">Total Plot Area:</label>' +
    '<input type="text" id="totalplotarea' + unitCount + '" name="totalplotarea"><br>' +
    '<label id="totalareabeforeded" for="totalareabeforeded' + unitCount + '">Total Area before Deduction:</label>' +
    '<input type="text" id="totalareabeforeded' + unitCount + '" name="totalareabeforeded"><br>' +
    '<label id="totalcarpetarea" for="totalcarpetarea' + unitCount + '">Total Carpet Area:</label>' +
    '<input type="text" id="totalcarpetarea' + unitCount + '" name="totalcarpetarea"><br>' +
    '<label id="totalexemptedarea" for="totalexemptedarea' + unitCount + '">Total Exempted Area:</label>' +
    '<input type="text" id="totalexemptedarea' + unitCount + '" name="totalexemptedarea"><br>' +
    '<label id="assessablearea" for="assessablearea' + unitCount + '">Total Assessable Area:</label>' +
    '<input type="text" id="assessablearea' + unitCount + '" name="assessablearea"><br>' +
    '<label id="totallegalarea" for="totallegalarea' + unitCount + '">Total Legal Area:</label>' +
    '<input type="text" id="totallegalarea' + unitCount + '" name="totallegalarea"><br>' +
    '<label id="totaillegalarea" for="totaillegalarea' + unitCount + '">Total Illegal Area:</label>' +
    '<input type="text" id="totaillegalarea' + unitCount + '" name="totaillegalarea"><br>' +
    '<label for="remark' + unitCount + '">Remark:</label>' +
    '<select id="remark' + unitCount + '" name="remark">' +
    '</select><br><br>' +
    '<div class="button-container">';
    }
    
    function calculateAgeAndUpdateFactor(selectedYear, ageFieldId, factorFieldId, hiddenFieldId, currentDateFieldId, createddateVc = null) {
        if (!selectedYear) return;
    
        let currentDate, currentYear, currentMonth, currentDay, constructionYear, age;
    
        if (!createddateVc) {
            // Use current date if createddateVc is null or undefined
            currentDate = new Date();
            currentYear = currentDate.getFullYear();
            currentMonth = String(currentDate.getMonth() + 1).padStart(2, '0'); // Adding 1 since months are 0-based
            currentDay = String(currentDate.getDate()).padStart(2, '0');
        } else {
            try {
                // Split createddateVc and extract date components
                const [time, date] = createddateVc.split(', ');
                const [day, month, year] = date.split('-');
                currentDate = new Date(`${year}-${month}-${day}`);
                currentYear = currentDate.getFullYear();
                currentMonth = String(currentDate.getMonth() + 1).padStart(2, '0');
                currentDay = String(currentDate.getDate()).padStart(2, '0');
            } catch (error) {
                console.error('Invalid createddateVc format:', createddateVc);
                return; // Exit the function if the format is invalid
            }
        }
    
        // Calculate construction year and age
        constructionYear = parseInt(selectedYear, 10); // Ensure selected year is treated as a number
        age = Math.max(0, currentYear - constructionYear);
    
        // Ensure age is never negative
        // if (age < 0) {
        //     age = 0;
        // }
        const ageField = document.getElementById(ageFieldId);
        if (ageField) {
            ageField.value = age;
        }
    
        const { factor, ageFactorId } = getAgeFactorFromDB(age);
        // Set the Construction Age Factor in a text field
        const factorField = document.getElementById(factorFieldId);
        if (factorField) {
            factorField.value = factor;
        }
    
        // Update the hidden input field with the age factor ID
        const hiddenField = document.getElementById(hiddenFieldId);
        if (hiddenField) {
            hiddenField.value = ageFactorId;
        }
    
        // Populate the current date field with selected year and current month and day
        const formattedCurrentDate = `${selectedYear}-${currentMonth}-${currentDay}`;
        const currentDateField = document.getElementById(currentDateFieldId);
        if (currentDateField) {
            currentDateField.value = formattedCurrentDate;
        }
    }
    
    function getAgeFactorFromDB(age) {
    let factor = 'Unknown';
    let ageFactorId = null;
    
    for (const factorEntry of constructionAgeFactors) {
    const minAge = parseInt(factorEntry.afm_ageminage_vc, 10);
    const maxAge = parseInt(factorEntry.afm_agemaxage_vc, 10);
    if (age >= minAge && age <= maxAge) {
        factor = factorEntry.afm_agefactornamell_vc;
        ageFactorId = factorEntry.afm_agefactorid_i; // Assuming afm_agefactorid_i exists in factorEntry
        break;
    }
    }
    
    return { factor, ageFactorId };
    }
    // Usage Example for Units
    function updateUnitAgeFactor(selectedYear, unitCount, createddateVc) {
    calculateAgeAndUpdateFactor(selectedYear, `constructionAge${unitCount}`, `constAgeFactor${unitCount}`, `agefactorunithid${unitCount}`, `currentDateUnit${unitCount}`,createddateVc);
    }
    
    // Usage Example for Property
    function updatePropertyAgeFactor(selectedYear, createddateVc) {
    calculateAgeAndUpdateFactor(selectedYear, 'constAgeProperty', 'ageFactorProperty', 'agefactorpropertyhid', 'currentDateProperty', createddateVc);
    }

    function removeUnit(formId) {
    var formContainer = document.getElementById(formId);
    if (formContainer) {
    formContainer.remove();
    }
    
    var popupId = 'popup-' + formId.replace("myForm", "");
    var popup = document.getElementById(popupId);
    if (popup) {
    popup.remove();
    }
    
    var buttonId = "unitButton" + formId.replace("myForm", "");
    var unitButton = document.getElementById(buttonId);
    if (unitButton) {
    unitButton.remove();
    }
    
    if (typeof activeFormId !== 'undefined' && activeFormId === formId) {
    activeFormId = null;
    }
    
    var existingUnitButtons = document.querySelectorAll('.unitbutton');
    unitCount = existingUnitButtons.length;
    
    var currentUnitIndex = parseInt(formId.replace("myForm", ""));
    existingUnitButtons.forEach(function(button, index) {
    var expectedIndex = index + 1;
    if (expectedIndex >= currentUnitIndex) {
        button.textContent = "Unit " + expectedIndex;
        button.id = "unitButton" + expectedIndex;
        button.onclick = function() { toggleForm("myForm" + expectedIndex); };
    
        var newFormContainer = document.getElementById("myForm" + (index + 2));
        if (newFormContainer) {
            newFormContainer.id = "myForm" + expectedIndex;
            var deleteButton = newFormContainer.querySelector(".deletebtn");
            var unitNoInput = newFormContainer.querySelector('input[name="unitNo"]');
            if (deleteButton) {
                deleteButton.textContent = "Delete Unit " + expectedIndex;
                deleteButton.id = "deleteButton" + expectedIndex;
                deleteButton.onclick = function () {
                    removeUnit("myForm" + expectedIndex);
                }
            }
            if (unitNoInput) {
                unitNoInput.value = expectedIndex;
            }
        }
    }
    });
    updatePropertySectionTotals();
    }
    
    var compressedImages = {};
    
    function compressImage(file, previewId, conditionId) {
    if (!file) return;  // Exit if no file is selected
    const conditionField = document.getElementById(conditionId);
    conditionField.value = "1";

    const maxWidth = 525;  // Maximum width of the compressed image
    const maxHeight = 700; // Maximum height of the compressed image
    const quality = 9;   // Quality of the compressed image
    
    const reader = new FileReader();
    reader.onload = (event) => {
    const img = new Image();
    img.src = event.target.result;
    
    img.onload = () => {
        const canvas = document.createElement('canvas');
        let width = img.width;
        let height = img.height;
    
        // Adjust the dimensions while maintaining the aspect ratio
        if (width > height) {
            if (width > maxWidth) {
                height *= maxWidth / width;
                width = maxWidth;
            }
        } else {
            if (height > maxHeight) {
                width *= maxHeight / height;
                height = maxHeight;
            }
        }
    
        canvas.width = width;
        canvas.height = height;
        const ctx = canvas.getContext('2d');
        ctx.drawImage(img, 0, 0, width, height);
    
        canvas.toBlob((blob) => {
            compressedImages[previewId] = blob;
            const imageUrl = URL.createObjectURL(blob);
            document.getElementById(previewId).src = imageUrl;
        }, 'image/jpeg', quality);
    };
    };
    
    reader.readAsDataURL(file);
    }
    
    //this is used for getting the content from dropdown like in roomtypes whatever roomtype is selected it will get the name from the dropdown
    function getSelectedOptionText(selectId) {
    if (!selectId) {
    console.error('getSelectedOptionText: selectElement is null');
    return '';
    }
    const selectElement = document.getElementById(selectId);
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    return selectedOption.getAttribute('data-name') || selectedOption.text;
    }
    

    function collectFormData(cachedData) {
        const updatedFields = {
            propertyDetails: {},
            unitDetails: [],
        };
    
        const imageFields = new FormData();

        console.log('newproperty number: '+cachedData.propertyDetails.pdNewpropertynoVc);
        let formData = {
            propertyDetails: {
                pdNewpropertynoVc: cachedData.propertyDetails.pdNewpropertynoVc,
                propRefno: document.getElementById('podRef').value,
                pdZoneI: document.getElementById('zone').value,
                pdWardI: document.getElementById('ward').value,
                pdCitysurveynoF: document.getElementById('citySurveyNo').value,
                pdOldpropnoVc: document.getElementById('oldPropertyNo').value,
                pdSurypropnoVc: document.getElementById('surveyPropNo').value,
                pdLayoutnameVc: document.getElementById('layoutName').value,
                pdLayoutnoVc: document.getElementById('layoutNo').value,
                pdKhasranoVc: document.getElementById('khasraNo').value,
                pdPlotnoVc: document.getElementById('plotNo').value,
                pdOwnernameVc: document.getElementById('ownerName').value,
                pdAddnewownernameVc: document.getElementById('newOwnerName').value,
                pdOccupinameF: document.getElementById('occupierName').value,
                pdPropertyaddressVc: document.getElementById('address').value,
                pdOwnertypeVc: document.getElementById('ownerType').value,
                pdStatusbuildingVc: document.getElementById('buildingStatus').value,
                //property
                //(a)
                pdPannoVc: document.getElementById('panNo').value,
                pdAadharnoVc: document.getElementById('aadharNo').value,
                pdContactnoVc: document.getElementById('ownermobileNo').value,
                pdCategoryI: document.getElementById('category').value,
                pdPropertynameVc: document.getElementById('propertyName').value,
                pdBuildingvalueI: document.getElementById('buildingValue').value,
                pdPlotvalueF: document.getElementById('plotValue').value,
                //(b)
                pdPropertytypeI: document.getElementById('propertyType').value,
                pdPropertysubtypeI: document.getElementById('propertySubType').value,
                pdUsagetypeI: document.getElementById('propertyusageType').value,
                pdUsagesubtypeI: document.getElementById('propertyusageSubType').value,
                pdBuildingtypeI: document.getElementById('buildingType').value,
                pdBuildingsubtypeI: document.getElementById('buildingSubType').value,
                pdConstYearVc: document.getElementById('currentDateProperty').value,
                pdConstAgeI: document.getElementById('constAgeProperty').value,
                //pdagefactor
                pdPermissionstatusVc: document.getElementById('permissionSelection').value,
                pdPermissionnoVc: document.getElementById('permissionNumber').value,
                pdPermissiondateDt: document.getElementById('permissionDate').value,
                pdStairVc: document.getElementById('stair').value,
                pdLiftVc: document.getElementById('lift').value,
                pdRoadwidthF: document.getElementById('roadWidth').value,
                //(c)
                pdToiletstatusVc: document.getElementById('toiletSelection').value,
                pdToiletsI: document.getElementById('numberOfToilets').value,
                pdSeweragesVc: document.getElementById('sewerage').value,
                pdSeweragesType: document.getElementById('sewerageType').value,
                pdWaterconnstatusVc: document.getElementById('waterSelection').value,
                pdWaterconntypeVc: document.getElementById('waterOptions').value,
                pdMcconnectionVc: document.getElementById('mcConnection').value,
                pdMeternoVc: document.getElementById('meterNumber').value,
                pdConsumernoVc: document.getElementById('consumerNumber').value,
                pdConnectiondateDt: document.getElementById('connectionDate').value,
                pdPipesizeF: document.getElementById('pipeSize').value,
                pdSolarunitVc: document.getElementById('solar').value,
                pdElectricityconnectionVc: document.getElementById('electricitySelection').value,
                pdElemeternoVc: document.getElementById('emeterNumber').value,
                pdEleconsumernoVc: document.getElementById('econsumerNumber').value,
                pdRainwaterhaverstVc: document.getElementById('rainWaterHarvesting').value,
                //(d)
                pdPlotareaF: document.getElementById('plotArea').value,
                pdTotbuiltupareaF: document.getElementById('builtUpArea').value,
                pdTotcarpetareaF: document.getElementById('carpetArea').value,
                pdTotalexempareaF: document.getElementById('exemptedArea').value,
                pdAssesareaF: document.getElementById('assessableArea').value,
                pdArealetoutF: document.getElementById('areaLetout').value,
                pdAreanotletoutF: document.getElementById('areaNotLetout').value,
                pdNooffloorsI: document.getElementById('nooffloors').value,
                pdNoofroomsI: document.getElementById('noofrooms').value,
                pdCurrassesdateDt: document.getElementById('currentAssmtDate').value,
                pdLastassesdateDt: document.getElementById('lastAssmtDate').value,
                pdFirstassesdateDt: document.getElementById('firstAssmtDate').value,
                pdNadetailsVc: document.getElementById('naSelection').value,
                pdNanumberI: document.getElementById('naOrder').value,
                pdNadateDt: document.getElementById('naDate').value,
                pdTddetailsVc: document.getElementById('tdSelection').value,
                pdTdordernumF: document.getElementById('tdOrder').value,
                pdTddateDt: document.getElementById('tdDate').value,
                pdTpdetailsVc: document.getElementById('tpSelection').value,
                pdTpordernumF: document.getElementById('tpOrder').value,
                pdTpdateDt: document.getElementById('tpDate').value,
                pdSaledeedI: document.getElementById('saledeedSelection').value,
                pdSaledateDt: document.getElementById('saledeedDate').value,
                pdOldrvFl: document.getElementById('oldRV').value,
                unitDetails: []
            }
        };

        updatedFields.propertyDetails.pdNewpropertynoVc = cachedData.propertyDetails.pdNewpropertynoVc;

        // Compare propertyDetails
        for (const key in formData.propertyDetails) {
            if (key !== 'unitDetails' && formData.propertyDetails[key] !== cachedData.propertyDetails[key]) {
                updatedFields.propertyDetails[key] = formData.propertyDetails[key];
            }
        }
    
        // Collect and compare unitDetails
        document.querySelectorAll('.formContainer').forEach((unitContainer, index) => {
            let unitIndex = index + 1;
            let currentUnit = {
                udOccupantStatusI: unitContainer.querySelector(`#occupancy${unitIndex}`).value, // Assuming this field exists
                udRentalNoVc: unitContainer.querySelector(`#monthlyrent${unitIndex}`).value,
                udTenantNoI: unitContainer.querySelector(`#tenantno${unitIndex}`).value,
                udOccupierNameVc: unitContainer.querySelector(`#occupiername${unitIndex}`).value,
                udEstablishmentNameVc: unitContainer.querySelector(`#establishmentName${unitIndex}`).value,
                udMobileNoVc: unitContainer.querySelector(`#umobileNo${unitIndex}`).value,
                udEmailIdVc: unitContainer.querySelector(`#email${unitIndex}`).value,
                udUsageTypeI: parseInt(unitContainer.querySelector(`#unitusageType${unitIndex}`).value, 10),
                udUsageSubtypeI: parseInt(unitContainer.querySelector(`#unitusageSubType${unitIndex}`).value, 10),
                udConstructionClassI: getSelectedOptionText(`classOfProperty${unitIndex}`),
                udConstYearDt: unitContainer.querySelector(`#currentDateUnit${unitIndex}`).value,
                udConstAgeI: parseInt(unitContainer.querySelector(`#constructionAge${unitIndex}`).value, 10),
                udAgeFactorI: unitContainer.querySelector(`#agefactorunithid${unitIndex}`).value,
                udPlotAreaFl: parseFloat(unitContainer.querySelector(`#totalplotarea${unitIndex}`).value),
                udCarpetAreaF: parseFloat(unitContainer.querySelector(`#totalcarpetarea${unitIndex}`).value),
                udExemptedAreaF: parseFloat(unitContainer.querySelector(`#totalexemptedarea${unitIndex}`).value),
                udAssessmentAreaF: parseFloat(unitContainer.querySelector(`#assessablearea${unitIndex}`).value),
                udTotLegAreaF: parseFloat(unitContainer.querySelector(`#totallegalarea${unitIndex}`).value),
                udTotIllegAreaF: parseFloat(unitContainer.querySelector(`#totaillegalarea${unitIndex}`).value),
                udUnitRemarkVc: unitContainer.querySelector(`#remark${unitIndex}`).value,
                udAreaBefDedF: parseFloat(unitContainer.querySelector(`#totalareabeforeded${unitIndex}`).value),
                // these are primary key details of unit
                pdNewpropertynoVc: cachedData.propertyDetails.pdNewpropertynoVc,
                udFloorNoVc: unitContainer.querySelector(`#floorNo${unitIndex}`).value,
                udUnitNoVc: parseInt(unitContainer.querySelector(`#unitNo${unitIndex}`).value, 10),
                // Add more fields as required
                unitBuiltupUps: []
            };
    
            const cachedUnit = cachedData.unitDetails?.[index] || {};
            const unitUpdates = {
                pdNewpropertynoVc: currentUnit.pdNewpropertynoVc,
                udUnitNoVc: currentUnit.udUnitNoVc,
                udFloorNoVc: currentUnit.udFloorNoVc
            };
    
            // Compare unit-level fields
            for (const key in currentUnit) {
                if (key !== 'unitBuiltupUps' && currentUnit[key] !== cachedUnit[key]) {
                    unitUpdates[key] = currentUnit[key];
                }
            }
            const builtUpUpdates = [];
            let popupId = `popup-${unitIndex}`;
            let popup = document.getElementById(popupId);
    
            if (popup) {
                const rows = popup.querySelectorAll('table tbody tr');
                rows.forEach((row, rowIndex) => {
                    let rowId = `row-${unitIndex}-${rowIndex + 1}`;
                    let roomTypeElement = row.querySelector(`select[id="use-${rowId}"]`);
                    let currentBuiltUp = {
                        ubRoomTypeVc: roomTypeElement ? getSelectedOptionText(roomTypeElement.id) : '',
                        ubDedpercentI: row.querySelector(`input[id="deduction-${rowId}"]`)?.value || null,
                        ubareabefdedFl: row.querySelector(`input[id="areabeforededuction-${rowId}"]`)?.value || null,
                        ubLengthFl: row.querySelector(`input[id="length-${rowId}"]`)?.value || '',
                        ubBreadthFl: row.querySelector(`input[id="breadth-${rowId}"]`)?.value || '',
                        ubExemptionsVc: row.querySelector(`select[id="exemption-${rowId}"]`)?.value || '',
                        ubExemLengthFl: row.querySelector(`input[id="exemptionLength-${rowId}"]`)?.value || '',
                        ubExemBreadthFl: row.querySelector(`input[id="exemptionBreadth-${rowId}"]`)?.value || '',
                        ubCarpetAreaFl: row.querySelector(`input[id="carpetArea-${rowId}"]`)?.value || '',
                        ubLegalStVc: row.querySelector(`select[id="legalIllegal-${rowId}"]`)?.value || '',
                        ubExemAreaFl: row.querySelector(`input[id="exemptionArea-${rowId}"]`)?.value || '',
                        ubAssesAreaFl: row.querySelector(`input[id="assumptionArea-${rowId}"]`)?.value || '',
                        ubLegalAreaFl: row.querySelector(`input[id="legalAssmtArea-${rowId}"]`)?.value || '',
                        ubIllegalAreaFl: row.querySelector(`input[id="illegalAssmtArea-${rowId}"]`)?.value || '',
                        ubMeasureTypeVc: row.querySelector(`select[id="measureType-${rowId}"]`)?.value || '',
                        ubplotareaFl: row.querySelector(`input[id="plotArea-${rowId}"]`)?.value || '',
                        // these are primary key details of builtup

                        pdNewpropertynoVc: row.querySelector(`input[id="pdNewpropertynoVc-${rowId}"]`)?.value || '',
                        udUnitNoVc: row.querySelector(`input[id="udUnitNoVc-${rowId}"]`)?.value || '',
                        udFloorNoVc: row.querySelector(`input[id="udFloorNoVc-${rowId}"]`)?.value || '',
                        ubIdsI: row.querySelector(`input[id="ubIdsI-${rowId}"]`)?.value || '',
                        };

                    const isRowEmpty = Object.values(currentBuiltUp).every(
                        (value) => value === null || value === '' || value === undefined
                    );
            
                    if (!isRowEmpty) {
                        const cachedBuiltUp = cachedData.unitBuiltupUps?.[rowIndex] || {};
                        const builtUpDiff = {};
            
                        for (const key in currentBuiltUp) {
                            if (currentBuiltUp[key] !== cachedBuiltUp[key]) {
                                builtUpDiff[key] = currentBuiltUp[key];
                            }
                        }
            
                        if (Object.keys(builtUpDiff).length > 0) {
                            builtUpUpdates.push(builtUpDiff);
                        }
                    }
                });
            }
    
            if (Object.keys(unitUpdates).length > 0 || builtUpUpdates.length > 0) {
                unitUpdates.unitBuiltupUps = builtUpUpdates;
                updatedFields.unitDetails.push(unitUpdates);
            }
    
            formData.propertyDetails.unitDetails.push(currentUnit);
        });
    
        let jsonData = JSON.stringify(formData);
    
        let formDataToSend = new FormData();
        formDataToSend.append('jsonData', jsonData); // Append JSON string directly
    
        const imageInputs = [
            { inputId: "propertyImage", conditionId: "includePropertyImage" },
            { inputId: "propertyImage2", conditionId: "includePropertyImage2" },
            { inputId: "housePlan1", conditionId: "includeHousePlan1" },
            { inputId: "housePlan2", conditionId: "includeHousePlan2" },
        ];

        imageInputs.forEach(field => {
            const condition = document.getElementById(field.conditionId)?.value;
            const fileInput = document.getElementById(field.inputId);

            if (condition === "1" && fileInput?.files[0]) {
                imageFields.append(field.inputId, fileInput.files[0]);
            }
        });
    
    console.log('Updated Fields:', updatedFields);
    console.log('Image Fields:', [...imageFields.entries()]); // Debugging

    return { updatedFields, imageFields };
    }

    var memberCount = 1;
    
    function addMember() {
    memberCount++;
    
    var formContainer = document.getElementById("formContainer");
    
    const newForm = `
    <div id="memberForm${memberCount}">
    <h3>Member ${memberCount} Details:</h3>
    <label for="name${memberCount}">Name:</label>
    <input type="text" id="name${memberCount}" name="name${memberCount}"><br>
    
    <label for="qualification${memberCount}">Qualification:</label>
    <select id="qualification${memberCount}" name="qualification${memberCount}">
        <option value="undergraduate">Undergraduate</option>
        <option value="postgraduate">Postgraduate</option>
        <option value="doctorate">Doctorate</option>
    </select><br>
    
    <label for="occupation${memberCount}">Occupation:</label>
    <input type="text" id="occupation${memberCount}" name="occupation${memberCount}"><br>
    
    <label for="noOfAdults${memberCount}">No. Of Adults:</label>
    <input type="text" id="noOfAdults${memberCount}" name="noOfAdults${memberCount}"><br>
    
    <label for="noOfChildren${memberCount}">No. of Children:</label>
    <input type="text" id="noOfChildren${memberCount}" name="noOfChildren${memberCount}"><br>
    
    <label for="currentTaxDetails${memberCount}">Current Tax Details:</label>
    <select id="currentTaxDetails${memberCount}" name="currentTaxDetails${memberCount}">
        <option value="taxed">Taxed</option>
        <option value="nontaxed">Non-taxed</option>
    </select><br>
    
    <label for="taxAmount${memberCount}">Tax Amount:</label>
    <input type="text" id="taxAmount${memberCount}" name="taxAmount${memberCount}"><br>
    
    <label for="roadType${memberCount}">Road Type:</label>
    <select id="roadType${memberCount}" name="roadType${memberCount}">
        <option value="residential">Residential</option>
        <option value="commercial">Commercial</option>
    </select><br>
    
    <label for="roadName${memberCount}">Road Name:</label>
    <input type="text" id="roadName${memberCount}" name="roadName${memberCount}"><br>
    
    <button type="button" onclick="addMember()">Add Another Member</button>
    <button type="button" onclick="removeMember(${memberCount})">Remove Member</button>
    <hr>
    </div>
    `;
    document.getElementById("memberForm1").insertAdjacentHTML('beforeend', newForm);
    
    }
    
    function removeMember(formId) {
    var formToRemove = document.getElementById(`memberForm${formId}`);
    formToRemove.remove();
    }
    
    async function validateForm() {
    let isValid = true;
    let missingFields = [];
    document.querySelectorAll('.mandatory').forEach((field) => {
    let input = document.getElementById(field.htmlFor);
    if (input && !input.disabled && !input.value.trim()) {
        isValid = false;
        missingFields.push(field.textContent);
    }
    });
    
    if (!isValid) {
    alert("Please fill in the following mandatory fields: " + missingFields.join(', '));
    return false;
    }
    
    // Check the survey property number validity
    await checkSurveyPropNo();
    
    if (!isSurveyPropNoValid) {
    alert("The Survey Property Number already exists or is invalid.");
    return false;
    }
    
    return true;
    }
    
    async function submitFormData() {
    // if (!await validateForm()) {
    // return;
    // }
    const { updatedFields, imageFields } = collectFormData(cachedPropertyData);
    
    document.getElementById('loadingSpinner').style.display = 'flex';
    const formData = new FormData();

    formData.append('updatedFields', JSON.stringify(updatedFields));
    for (const [key, blob] of imageFields.entries()) {
        if (blob) {
            formData.append(key, blob, `${key}.jpg`);
        } else {
            console.log(`No file for key: ${key}`);
        }
    }
    try {
    const response = await fetch('/3gSurvey/submitUpdatedPropertyDetails', {
        method: 'PATCH',
        body: formData,
    });
    
    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status} ${response.statusText}`);
    }
    
    const result = await response.json();
    
    alert(result.message || "Form updated successfully!");
    
    window.removeEventListener('beforeunload', handleBeforeUnload);
    
    // window.location.href = "/3gSurvey/newRegistration";
    } catch (error) {
    console.error('Submission failed:', error);
    alert('Form could not be submitted');
    } finally {
    
    document.getElementById('loadingSpinner').style.display = 'none';
    }
    }
    
    function handleBeforeUnload(event) {
    event.preventDefault();
    event.returnValue = 'Are you sure you want to leave? Your changes may not be saved.';
    }
    
    window.addEventListener('beforeunload', handleBeforeUnload);
