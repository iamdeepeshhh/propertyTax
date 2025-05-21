package com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "property_details", schema = "public")
public class PropertyDetails_Entity {


    @Id
    @Column(name = "pd_newpropertyno_vc", nullable = false, length = 50)
    private String pdNewpropertynoVc;

    @Column(name = "pd_zone_i")
    private Integer pdZoneI;

    @Column(name = "pd_ward_i")
    private Integer pdWardI;

    @Column(name = "pd_oldpropno_vc", length = 50)
    private String pdOldpropnoVc;

    @Column(name = "pd_surypropno_vc", nullable = false, length = 50)
    private String pdSurypropnoVc;

    @Column(name = "pd_layoutname_vc", length = 50)
    private String pdLayoutnameVc;

    @Column(name = "pd_layoutno_vc", length = 50)
    private String pdLayoutnoVc;

    @Column(name = "pd_khasrano_vc", length = 50)
    private String pdKhasranoVc;

    @Column(name = "pd_plotno_vc", length = 50)
    private String pdPlotnoVc;

    @Column(name = "pd_gridid_vc", length = 50)
    private String pdGrididVc;

    @Column(name = "pd_roadid_vc", length = 50)
    private String pdRoadidVc;

    @Column(name = "pd_parcelid_vc")
    private String pdParcelidVc;

    @Column(name = "pd_ownername_vc" ,length = 3000)
    private String pdOwnernameVc;

    @Column(name = "pd_addnewownername_vc" ,length = 3000)
    private String pdAddnewownernameVc;

    @Column(name = "pd_panno_vc", length = 50)
    private String pdPannoVc;

    @Column(name = "pd_aadharno_vc", length = 50)
    private String pdAadharnoVc;

    @Column(name = "pd_contactno_vc", length = 50)
    private String pdContactnoVc;

    @Column(name = "pd_propertyname_vc", length = 50)
    private String pdPropertynameVc;

    @Column(name = "pd_propertyaddress_vc", length = 200)
    private String pdPropertyaddressVc;

    @Column(name = "pd_pincode_vc", length = 50)
    private String pdPincodeVc;

    @Column(name = "pd_category_i")
    private String pdCategoryI;

    @Column(name = "pd_propertytype_i")
    private String pdPropertytypeI;

    @Column(name = "pd_propertysubtype_i")
    private String pdPropertysubtypeI;

    @Column(name = "pd_usagetype_i")
    private String pdUsagetypeI;

    @Column(name = "pd_usagesubtype_i")
    private String pdUsagesubtypeI;

    @Column(name = "pd_buildingtype_i")
    private String pdBuildingtypeI;

    @Column(name = "pd_buildingsubtype_i")
    private String pdBuildingsubtypeI;

    @Column(name = "pd_const_age_i")
    private Integer pdConstAgeI;

    @Column(name = "pd_year_i")
    private String pdConstYearVc;

    @Column(name = "pd_permissionstatus_vc", length = 50)
    private String pdPermissionstatusVc;

    @Column(name = "pd_permissionno_vc", length = 50)
    private String pdPermissionnoVc;

    @Column(name = "pd_permissiondate_dt")
    private Date pdPermissiondateDt;

    @Column(name = "pd_nooffloors_i")
    private Integer pdNooffloorsI;

    @Column(name = "pd_noofrooms_i")
    private Integer pdNoofroomsI;

    @Column(name = "pd_stair_vc", length = 10)
    private String pdStairVc;

    @Column(name = "pd_lift_vc", length = 10)
    private String pdLiftVc;

    @Column(name = "pd_roadwidth_f")
    private String pdRoadwidthF;

    @Column(name = "pd_toiletstatus_vc", length = 50)
    private String pdToiletstatusVc;

    @Column(name = "pd_toilets_i")
    private Integer pdToiletsI;

    @Column(name = "pd_sewerages_vc", length = 30)
    private String pdSeweragesVc;

    @Column(name = "pd_sewerages_type", length = 30)
    private String pdSeweragesType;

    @Column(name = "pd_waterconnstatus_vc", length = 50)
    private String pdWaterconnstatusVc;

    @Column(name = "pd_waterconntype_vc", length = 50)
    private String pdWaterconntypeVc;

    @Column(name = "pd_mcconnection_vc", length = 50)
    private String pdMcconnectionVc;

    @Column(name = "pd_meterno_vc", length = 50)
    private String pdMeternoVc;

    @Column(name = "pd_consumerno_vc", length = 50)
    private String pdConsumernoVc;

    @Column(name = "pd_connectiondate_dt")
    private Date pdConnectiondateDt;

    @Column(name = "pd_pipesize_f")
    private Double pdPipesizeF;

    @Column(name = "pd_electricityconnection_vc", length = 50)
    private String pdElectricityconnectionVc;

    @Column(name = "pd_elemeterno_vc", length = 50)
    private String pdElemeternoVc;

    @Column(name = "pd_eleconsumerno_vc", length = 50)
    private String pdEleconsumernoVc;

    @Column(name = "pd_rainwaterhaverst_vc", length = 50)
    private String pdRainwaterhaverstVc;

    @Column(name = "pd_solarunit_vc", length = 50)
    private String pdSolarunitVc;

    @Column(name = "pd_plotarea_f")
    private String pdPlotareaF;

    @Column(name = "pd_totbuiltuparea_f")
    private String pdTotbuiltupareaF;

    @Column(name = "pd_totcarpetarea_f")
    private String pdTotcarpetareaF;

    @Column(name = "pd_totalexemparea_f")
    private String pdTotalexempareaF;

    @Column(name = "pd_assesarea_f")
    private String pdAssesareaF;

    @Column(name = "pd_arealetout_f")
    private String pdArealetoutF;

    @Column(name = "pd_areanotletout_f")
    private String pdAreanotletoutF;

    @Column(name = "pd_currassesdate_dt")
    private String pdCurrassesdateDt;

    @Column(name = "pd_oldrv_fl")
    private String pdOldrvFl;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "pd_polytype_vc")
    private String pdPolytypeVc;

    @Column(name = "pd_statusbuilding_vc")
    private String pdStatusbuildingVc;

    @Column(name = "pd_lastassesdate_dt")
    private String pdLastassesdateDt;

    @Column(name = "pd_noticeno_vc")
    private String pdNoticenoVc;

    @Column(name = "pd_indexno_vc")
    private String pdIndexnoVc;

    @Column(name = "pd_newfinalpropno_vc")
    private String pdNewfinalpropnoVc;

    @Column(name = "pd_taxstatus_vc")
    private Integer pdTaxstatusVc;

    @Column(name = "pd_changed_vc")
    private Integer pdChangedVc;

    @Column(name = "pd_nadetails_vc", length = 35)
    private String pdNadetailsVc;

    @Column(name = "pd_nanumber_i", length = 35)
    private String pdNanumberI;

    @Column(name = "pd_nadate_dt", length = 35)
    private String pdNadateDt;

    @Column(name = "pd_tddetails_vc")
    private String pdTddetailsVc;

    @Column(name = "pd_tdordernum_f")
    private String pdTdordernumF;

    @Column(name = "pd_tddate_dt")
    private String pdTddateDt;

    @Column(name = "pd_saledeed_i", length = 35)
    private String pdSaledeedI;

    @Column(name = "pd_saledate_dt", length = 35)
    private String pdSaledateDt;

    @Column(name = "pd_citysurveyno_f")
    private String pdCitysurveynoF;

    @Column(name = "pd_ownertype_vc", length = 35)
    private String pdOwnertypeVc;

    @Column(name = "pd_buildingvalue_i", length = 35)
    private String pdBuildingvalueI;

    @Column(name = "pd_plotvalue_f", length = 35)
    private String pdPlotvalueF;

    @Column(name = "pd_tpdate_dt", length = 35)
    private String pdTpdateDt;

    @Column(name = "pd_tpdetails_vc", length = 35)
    private String pdTpdetailsVc;

    @Column(name = "pd_tpordernum_f", length = 35)
    private String pdTpordernumF;

    @Column(name = "pd_occupiname_f", length = 3000)
    private String pdOccupinameF;

    @Column(name = "pd_propimage_t", columnDefinition = "TEXT")
    private String pdPropimageT;

    @Column(name = "pd_houseplan_t", columnDefinition = "TEXT")
    private String pdHouseplanT;

    @Column(name = "pd_propimage2_t", columnDefinition = "TEXT")
    private String pdPropimage2T;

    @Column(name = "pd_houseplan2_t", columnDefinition = "TEXT")
    private String pdHouseplan2T;

    @Column(name = "pd_sign_t", columnDefinition = "TEXT")
    private String pdSignT;

    @Column(name = "prop_refno")
    private String propRefno;

    @Column(name = "pd_finalpropno_vc")
    private String pdFinalpropnoVc;

    @Column(name = "pd_imgstatus_vc")
    private String pdImgstatusVc;

    @Column(name = "pd_suryprop2_vc")
    private Integer pdSuryprop2Vc;

    @Column(name = "pd_suryprop1_vc")
    private String pdSuryprop1Vc;

    @Column(name = "pd_timpestamps_dt")
    private Date pdTimpestampsDt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "approvedbydesk1", columnDefinition = "TEXT")
    private String pdApprovedByDesk1Vc;

    @Column(name = "approvedbydesk2", columnDefinition = "TEXT")
    private String pdApprovedByDesk2Vc;

    @Column(name = "approvedbydesk3", columnDefinition = "TEXT")
    private String pdApprovedByDesk3Vc;

    @Column(name = "completedby", columnDefinition = "TEXT")
    private String pdCompletedByVc;

    @Column(name = "pd_firstassesdate_dt")
    private String pdFirstassesdateDt;

}
