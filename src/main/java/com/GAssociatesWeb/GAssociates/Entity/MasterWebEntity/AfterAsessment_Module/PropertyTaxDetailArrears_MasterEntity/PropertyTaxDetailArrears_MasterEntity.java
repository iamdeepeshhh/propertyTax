package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.PropertyTaxDetailArrears_MasterEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "arrears_property_taxdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyTaxDetailArrears_MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "arrears_id_i")
    private Integer arrearsId;

    @Column(name = "arrears_ward_i")
    private Integer ward;

    @Column(name = "arrears_surypropno_vc")
    private String surveyPropertyNo;

    @Column(name = "arrears_finalpropno_vc")
    private String finalPropertyNo;

    @Column(name = "arrears_ownername_vc")
    private String ownerName;

    @Column(name = "arrears_addnewownername_vc")
    private String addNewOwnerName;

    // ───── Core Taxes (From Arrears)
    @Column(name = "arrears_propertytax_dp")
    private Double propertyTax;

    @Column(name = "arrears_edutax_dp")
    private Double educationTax;

    @Column(name = "arrears_treetax_dp")
    private Double treeTax;

    @Column(name = "arrears_envtax_dp")
    private Double environmentTax;

    @Column(name = "arrears_firetax_dp")
    private Double fireTax;

    @Column(name = "arrears_electricity_dp")
    private Double electricityTax;

    @Column(name = "arrears_usercharges_dp")
    private Double userCharges;

    @Column(name = "arrears_lighttax_dp")
    private Double lightTax;

    @Column(name = "arrears_cleantax_dp")
    private Double cleanTax;

    @Column(name = "arrears_interest_dp")
    private Double interest;

    @Column(name = "arrears_water_dp")
    private Double waterTax;

    @Column(name = "arrears_egctax_dp")
    private Double egcTax;

    @Column(name = "arrears_totaltax_dp")
    private Double totalTax;

    @Column(name = "arrears_oldpropno_i")
    private String oldPropertyNo;

    @Column(name = "arrears_zone_i")
    private Integer zone;

    @Column(name = "arrears_occupiname_vc")
    private String occupierName;

    @Column(name = "arrears_newpropertyno_vc")
    private String newPropertyNo;

    @Column(name = "arrears_noticefee_dp")
    private Double noticeFee;

    @Column(name = "arrears_miscell_dp")
    private Double miscellaneousTax;

    @Column(name = "arrears_othertax_dp")
    private Double otherTax;

    @Column(name = "arrears_penalty_dp")
    private Double penalty;

    @Column(name = "arrears_discount_dp")
    private Double discount;

    @Column(name = "arrears_financialyear_dt")
    private String financialYear;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "arr_finalpropno_vc")
    private String arrFinalPropertyNo;

    // ───── Additional Fields (From After Hearing)
    @Column(name = "pt_finalyear_vc")
    private String ptFinalYear;

    @Column(name = "pt_finalrv_fl")
    private Double ptFinalRatableValue;

    @Column(name = "pt_final_tax_fl")
    private Double ptFinalTax;

    @Column(name = "pt_status_dummy_vc")
    private String ptStatus;

    // ───── After Hearing Taxes ─────
    @Column(name = "pt_propertytax_fl")
    private Double ptPropertyTax;

    @Column(name = "pt_edutax_fl")
    private Double ptEduTax;

    @Column(name = "pt_egctax_fl")
    private Double ptEgcTax;

    @Column(name = "pt_treetax_fl")
    private Double ptTreeTax;

    @Column(name = "pt_lighttax_fl")
    private Double ptLightTax;

    @Column(name = "pt_firetax_fl")
    private Double ptFireTax;

    @Column(name = "pt_cleantax_fl")
    private Double ptCleanTax;

    @Column(name = "pt_envtax_fl")
    private Double ptEnvTax;

    @Column(name = "pt_watertax_fl")
    private Double ptWaterTax;

    @Column(name = "pt_usercharges_fl")
    private Double ptUserCharges;

    @Column(name = "pt_miscellaneouscharges_fl")
    private Double ptMiscellaneousCharges;

    @Column(name = "pt_servicecharges_fl")
    private Double ptServiceCharges;

    @Column(name = "pt_municipaledutax_fl")
    private Double ptMunicipalEduTax;

    @Column(name = "pt_specialconservancytax_fl")
    private Double ptSpecialConservancyTax;

    @Column(name = "pt_specialedutax_fl")
    private Double ptSpecialEduTax;

    @Column(name = "pt_streettax_fl")
    private Double ptStreetTax;

    @Column(name = "pt_seweragetax_fl")
    private Double ptSewerageTax;

    @Column(name = "pt_seweragebenefittax_fl")
    private Double ptSewerageBenefitTax;

    @Column(name = "pt_waterbenefittax_fl")
    private Double ptWaterBenefitTax;

    // ───── Generic tax placeholders (pt_tax1_fl ... pt_tax25_fl) ─────
    @Column(name = "pt_tax1_fl") private Double ptTax1;
    @Column(name = "pt_tax2_fl") private Double ptTax2;
    @Column(name = "pt_tax3_fl") private Double ptTax3;
    @Column(name = "pt_tax4_fl") private Double ptTax4;
    @Column(name = "pt_tax5_fl") private Double ptTax5;
    @Column(name = "pt_tax6_fl") private Double ptTax6;
    @Column(name = "pt_tax7_fl") private Double ptTax7;
    @Column(name = "pt_tax8_fl") private Double ptTax8;
    @Column(name = "pt_tax9_fl") private Double ptTax9;
    @Column(name = "pt_tax10_fl") private Double ptTax10;
    @Column(name = "pt_tax11_fl") private Double ptTax11;
    @Column(name = "pt_tax12_fl") private Double ptTax12;
    @Column(name = "pt_tax13_fl") private Double ptTax13;
    @Column(name = "pt_tax14_fl") private Double ptTax14;
    @Column(name = "pt_tax15_fl") private Double ptTax15;
    @Column(name = "pt_tax16_fl") private Double ptTax16;
    @Column(name = "pt_tax17_fl") private Double ptTax17;
    @Column(name = "pt_tax18_fl") private Double ptTax18;
    @Column(name = "pt_tax19_fl") private Double ptTax19;
    @Column(name = "pt_tax20_fl") private Double ptTax20;
    @Column(name = "pt_tax21_fl") private Double ptTax21;
    @Column(name = "pt_tax22_fl") private Double ptTax22;
    @Column(name = "pt_tax23_fl") private Double ptTax23;
    @Column(name = "pt_tax24_fl") private Double ptTax24;
    @Column(name = "pt_tax25_fl") private Double ptTax25;
}
