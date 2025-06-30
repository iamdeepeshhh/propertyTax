package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_Entity;

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

    private Integer pd_zone_i;
    private Integer pd_ward_i;
    private String pd_oldpropno_vc;
    private String pd_surypropno_vc;
    private String pd_layoutname_vc;
    private String pd_layoutno_vc;
    private String pd_khasrano_vc;
    private String pd_plotno_vc;
    private String pd_gridid_vc;
    private String pd_roadid_vc;
    private String pd_parcelid_vc;
    private String pd_newpropertyno_vc; // Also used as Primary Key
    private String pd_ownername_vc;
    private String pd_addnewownername_vc;
    private String pd_panno_vc;
    private String pd_aadharno_vc;
    private String pd_contactno_vc;
    private String pd_propertyname_vc;
    private String pd_propertyaddress_vc;
    private String pd_pincode_vc;
    private String pd_category_i;
    private String pd_propertytype_i;
    private String pd_propertysubtype_i;
    private String pd_usagetype_i;
    private String pd_usagesubtype_i;
    private String pd_buildingtype_i;
    private String pd_buildingsubtype_i;
    private Integer pd_const_age_i;
    private String pd_permissionstatus_vc;
    private String pd_permissionno_vc;
    private LocalDate pd_permissiondate_dt;
    private Integer pd_nooffloors_i;
    private Integer pd_noofrooms_i;
    private String pd_stair_vc;
    private String pd_lift_vc;
    private String pd_roadwidth_f;
    private String pd_toiletstatus_vc;
    private Integer pd_toilets_i;
    private String pd_sewerages_vc;
    private String pd_sewerages_type;
    private String pd_waterconnstatus_vc;
    private String pd_waterconntype_vc;
    private String pd_mcconnection_vc;
    private String pd_meterno_vc;
    private String pd_consumerno_vc;
    private LocalDate pd_connectiondate_dt;
    private Double pd_pipesize_f;
    private String pd_electricityconnection_vc;
    private String pd_elemeterno_vc;
    private String pd_eleconsumerno_vc;
    private String pd_rainwaterhaverst_vc;
    private String pd_solarunit_vc;
    private String pd_plotarea_f;
    private String pd_totbuiltuparea_f;
    private String pd_totcarpetarea_f;
    private String pd_totalexemparea_f;
    private String pd_assesarea_f;
    private String pd_arealetout_f;
    private String pd_areanotletout_f;
    private LocalDate pd_currassesdate_dt;
    private String pd_oldrv_fl;
    private String pd_propimage_t;
    private String pd_houseplan_t;
    private String pd_propimage2_t;
    private String pd_houseplan2_t;
    private String pd_sign_t;
    private LocalDate pd_timpestamps_dt;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String user_id;
    private String pd_polytype_vc;
    private String pd_statusbuilding_vc;
    private String pd_lastassesdate_dt;
    private String prop_refno;
    private String pd_finalpropno_vc;
    private String pd_imgstatus_vc;
    private Integer pd_suryprop2_vc;
    private String pd_suryprop1_vc;
    private String pd_noticeno_vc;
    private String pd_indexno_vc;
    private String pd_newfinalpropno_vc;
    private Integer pd_taxstatus_vc;
    private Integer pd_changed_vc;
    private String pd_nadetails_vc;
    private String pd_nanumber_i;
    private String pd_nadate_dt;
    private String pd_tddetails_vc;
    private String pd_tdordernum_f;
    private String pd_tddate_dt;
    private String pd_saledeed_i;
    private String pd_saledate_dt;
    private String pd_citysurveyno_f;
    private String pd_ownertype_vc;
    private String pd_buildingvalue_i;
    private String pd_plotvalue_f;
    private String pd_tpdate_dt;
    private String pd_tpdetails_vc;
    private String pd_tpordernum_f;
    private String pd_occupiname_f;
    private Integer pd_suryprop3_vc;
}
