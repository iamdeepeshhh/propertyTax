package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class AfterHearing_PropertyDetailsDto {

    private String pdZoneI;
    private String pdWardI;
    private String pdOldpropnoVc;
    private String pdSurypropnoVc;
    private String pdLayoutnameVc;
    private String pdLayoutnoVc;
    private String pdKhasranoVc;
    private String pdPlotnoVc;
    private String pdGrididVc;
    private String pdRoadidVc;
    private String pdParcelidVc;
    private String pdNewpropertynoVc;
    private String pdOwnernameVc;
    private String pdAddnewownernameVc;
    private String pdPannoVc;
    private String pdAadharnoVc;
    private String pdContactnoVc;
    private String pdPropertynameVc;
    private String pdPropertyaddressVc;
    private String pdPincodeVc;
    private String pdCategoryI;
    private String pdPropertytypeI;
    private String pdPropertysubtypeI;
    private String pdUsagetypeI;
    private String pdUsagesubtypeI;
    private String pdBuildingtypeI;
    private String pdBuildingsubtypeI;
    private Integer pdConstAgeI;
    private String pdConstYearVc;
    private String pdPermissionstatusVc;
    private String pdPermissionnoVc;
    private java.util.Date pdPermissiondateDt;
    private Integer pdNooffloorsI;
    private Integer pdNoofroomsI;
    private String pdStairVc;
    private String pdLiftVc;
    private String pdRoadwidthF;
    private String pdToiletstatusVc;
    private Integer pdToiletsI;
    private String pdSeweragesVc;
    private String pdSeweragesType;
    private String pdWaterconnstatusVc;
    private String pdWaterconntypeVc;
    private String pdMcconnectionVc;
    private String pdMeternoVc;
    private String pdConsumernoVc;
    private java.util.Date pdConnectiondateDt;
    private String pdConnectiondateDt_vc;
    private Double pdPipesizeF;
    private String pdElectricityconnectionVc;
    private String pdElemeternoVc;
    private String pdEleconsumernoVc;
    private String pdRainwaterhaverstVc;
    private String pdSolarunitVc;
    private String pdPlotareaF;
    private String pdTotbuiltupareaF;
    private String pdTotcarpetareaF;
    private String pdTotalexempareaF;
    private String pdAssesareaF;
    private String pdArealetoutF;
    private String pdAreanotletoutF;
    private String pdCurrassesdateDt;
    private String pdOldrvFl;
    private String user_id;
    private String pdPolytypeVc;
    private String pdStatusbuildingVc;
    private String pdLastassesdateDt;
    private String pdFirstassesdateDt;
    private String pdNoticenoVc;
    private String pdIndexnoVc;
    private String pdNewfinalpropnoVc;
    private Integer pdTaxstatusVc;
    private Integer pdChangedVc;
    private String pdNadetailsVc;
    private String pdNanumberI;
    private String pdNadateDt;
    private String pdTddetailsVc;
    private String pdTdordernumF;
    private String pdTddateDt;
    private String pdSaledeedI;
    private String pdSaledateDt;
    private String pdCitysurveynoF;
    private String pdOwnertypeVc;
    private String pdBuildingvalueI;
    private String pdPlotvalueF;
    private String pdTpdateDt;
    private String pdTpdetailsVc;
    private String pdTpordernumF;
    private String pdOccupinameF;
    private String pdPropimageT;
    private String pdHouseplanT;
    private String pdPropimage2T;
    private String pdHouseplan2T;
    private String pdSignT;
    private String pdFinalpropnoVc;
    private String propRefno;
    private String pdImgstatusVc;
    private Integer pdSuryprop2Vc;
    private String pdSuryprop1Vc;
    private LocalDateTime createdAt;
    private String createddateVc;
    private Date updatedAt;
    private Long id;
    private String pdApprovedByDesk1Vc;
    private String pdApprovedByDesk2Vc;
    private String pdApprovedByDesk3Vc;
    private String pdCompletedByVc;

    private String pdOldwardNoVc;
    private String pdOldTaxVc;

    private List<AfterHearing_UnitDetailsDto> unitDetails = new ArrayList<>();

    public AfterHearing_PropertyDetailsDto() {
    }

    public AfterHearing_PropertyDetailsDto(String pdNewpropertynoVc, String pdZoneI, String pdWardI, String pdSurypropnoVc,
                               String pdOwnernameVc, String pdOccupinameF, String pdFinalpropnoVc, String pdPropertyaddressVc) {
        this.pdNewpropertynoVc = pdNewpropertynoVc;
        this.pdZoneI = pdZoneI;
        this.pdWardI = pdWardI;
        this.pdSurypropnoVc = pdSurypropnoVc;
        this.pdOwnernameVc = pdOwnernameVc;
        this.pdOccupinameF = pdOccupinameF;
        this.pdFinalpropnoVc = pdFinalpropnoVc;
        this.pdPropertyaddressVc = pdPropertyaddressVc;
    }

}
