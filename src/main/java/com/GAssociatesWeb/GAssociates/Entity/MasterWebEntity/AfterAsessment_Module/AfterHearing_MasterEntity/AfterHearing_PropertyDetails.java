package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "afterhearing_property_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_PropertyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pd_zone_i")
    private Integer zone;

    @Column(name = "pd_ward_i")
    private Integer ward;

    @Column(name = "pd_oldpropno_vc")
    private String oldPropertyNo;

    @Column(name = "pd_surypropno_vc")
    private String surveyPropertyNo;

    @Column(name = "pd_layoutname_vc")
    private String layoutName;

    @Column(name = "pd_layoutno_vc")
    private String layoutNo;

    @Column(name = "pd_khasrano_vc")
    private String khasraNo;

    @Column(name = "pd_plotno_vc")
    private String plotNo;

    @Column(name = "pd_gridid_vc")
    private String gridId;

    @Column(name = "pd_roadid_vc")
    private String roadId;

    @Column(name = "pd_parcelid_vc")
    private String parcelId;

    @Column(name = "pd_newpropertyno_vc")
    private String newPropertyNo;

    @Column(name = "pd_ownername_vc")
    private String ownerName;

    @Column(name = "pd_addnewownername_vc")
    private String additionalOwnerName;

    @Column(name = "pd_panno_vc")
    private String panNo;

    @Column(name = "pd_aadharno_vc")
    private String aadharNo;

    @Column(name = "pd_contactno_vc")
    private String contactNo;

    @Column(name = "pd_propertyname_vc")
    private String propertyName;

    @Column(name = "pd_propertyaddress_vc")
    private String propertyAddress;

    @Column(name = "pd_pincode_vc")
    private String pincode;

    @Column(name = "pd_category_i")
    private String category;

    @Column(name = "pd_propertytype_i")
    private String propertyType;

    @Column(name = "pd_propertysubtype_i")
    private String propertySubType;

    @Column(name = "pd_usagetype_i")
    private String usageType;

    @Column(name = "pd_usagesubtype_i")
    private String usageSubType;

    @Column(name = "pd_buildingtype_i")
    private String buildingType;

    @Column(name = "pd_buildingsubtype_i")
    private String buildingSubType;

    @Column(name = "pd_const_age_i")
    private Integer constructionAge;

    @Column(name = "pd_permissionstatus_vc")
    private String permissionStatus;

    @Column(name = "pd_permissionno_vc")
    private String permissionNo;

    @Column(name = "pd_permissiondate_dt")
    private LocalDate permissionDate;

    @Column(name = "pd_nooffloors_i")
    private Integer numberOfFloors;

    @Column(name = "pd_noofrooms_i")
    private Integer numberOfRooms;

    @Column(name = "pd_stair_vc")
    private String stair;

    @Column(name = "pd_lift_vc")
    private String lift;

    @Column(name = "pd_roadwidth_f")
    private String roadWidth;

    @Column(name = "pd_toiletstatus_vc")
    private String toiletStatus;

    @Column(name = "pd_toilets_i")
    private Integer toilets;

    @Column(name = "pd_sewerages_vc")
    private String sewerage;

    @Column(name = "pd_sewerages_type")
    private String sewerageType;

    @Column(name = "pd_waterconnstatus_vc")
    private String waterConnStatus;

    @Column(name = "pd_waterconntype_vc")
    private String waterConnType;

    @Column(name = "pd_mcconnection_vc")
    private String mcConnection;

    @Column(name = "pd_meterno_vc")
    private String meterNo;

    @Column(name = "pd_consumerno_vc")
    private String consumerNo;

    @Column(name = "pd_connectiondate_dt")
    private LocalDate connectionDate;

    @Column(name = "pd_pipesize_f")
    private Double pipeSize;

    @Column(name = "pd_electricityconnection_vc")
    private String electricityConnection;

    @Column(name = "pd_elemeterno_vc")
    private String eleMeterNo;

    @Column(name = "pd_eleconsumerno_vc")
    private String eleConsumerNo;

    @Column(name = "pd_rainwaterhaverst_vc")
    private String rainwaterHarvesting;

    @Column(name = "pd_solarunit_vc")
    private String solarUnit;

    @Column(name = "pd_plotarea_f")
    private String plotArea;

    @Column(name = "pd_totbuiltuparea_f")
    private String totalBuiltupArea;

    @Column(name = "pd_totcarpetarea_f")
    private String totalCarpetArea;

    @Column(name = "pd_totalexemparea_f")
    private String totalExemptedArea;

    @Column(name = "pd_assesarea_f")
    private String assessableArea;

    @Column(name = "pd_arealetout_f")
    private String areaLetOut;

    @Column(name = "pd_areanotletout_f")
    private String areaNotLetOut;

    @Column(name = "pd_currassesdate_dt")
    private LocalDate currentAssessmentDate;

    @Column(name = "pd_oldrv_fl")
    private String oldRatableValue;

    @Column(name = "pd_propimage_t")
    private String propertyImage;

    @Column(name = "pd_houseplan_t")
    private String housePlan;

    @Column(name = "pd_propimage2_t")
    private String propertyImage2;

    @Column(name = "pd_houseplan2_t")
    private String housePlan2;

    @Column(name = "pd_sign_t")
    private String signature;

    @Column(name = "pd_timpestamps_dt")
    private LocalDate timestamp;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "pd_polytype_vc")
    private String polygonType;

    @Column(name = "pd_statusbuilding_vc")
    private String statusOfBuilding;

    @Column(name = "pd_lastassesdate_dt")
    private String lastAssessmentDate;

    @Column(name = "prop_refno")
    private String propRefNo;

    @Column(name = "pd_finalpropno_vc")
    private String finalPropertyNo;

    @Column(name = "pd_imgstatus_vc")
    private String imageStatus;

    @Column(name = "pd_suryprop2_vc")
    private Integer surveyProperty2;

    @Column(name = "pd_suryprop1_vc")
    private String surveyProperty1;

    @Column(name = "pd_noticeno_vc")
    private String noticeNo;

    @Column(name = "pd_indexno_vc")
    private String indexNo;

    @Column(name = "pd_newfinalpropno_vc")
    private String newFinalPropertyNo;

    @Column(name = "pd_taxstatus_vc")
    private Integer taxStatus;

    @Column(name = "pd_changed_vc")
    private Integer changed;

    @Column(name = "pd_nadetails_vc")
    private String naDetails;

    @Column(name = "pd_nanumber_i")
    private String naNumber;

    @Column(name = "pd_nadate_dt")
    private String naDate;

    @Column(name = "pd_tddetails_vc")
    private String tdDetails;

    @Column(name = "pd_tdordernum_f")
    private String tdOrderNo;

    @Column(name = "pd_tddate_dt")
    private String tdDate;

    @Column(name = "pd_saledeed_i")
    private String saleDeed;

    @Column(name = "pd_saledate_dt")
    private String saleDate;

    @Column(name = "pd_citysurveyno_f")
    private String citySurveyNo;

    @Column(name = "pd_ownertype_vc")
    private String ownerType;

    @Column(name = "pd_buildingvalue_i")
    private String buildingValue;

    @Column(name = "pd_plotvalue_f")
    private String plotValue;

    @Column(name = "pd_tpdate_dt")
    private String tpDate;

    @Column(name = "pd_tpdetails_vc")
    private String tpDetails;

    @Column(name = "pd_tpordernum_f")
    private String tpOrderNo;

    @Column(name = "pd_occupiname_f")
    private String occupierName;

    @Column(name = "pd_suryprop3_vc")
    private Integer surveyProperty3;
}
