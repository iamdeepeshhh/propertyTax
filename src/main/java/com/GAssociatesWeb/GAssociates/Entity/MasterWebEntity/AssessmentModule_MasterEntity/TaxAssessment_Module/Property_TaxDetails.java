package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "property_Taxdetails")
public class Property_TaxDetails {
    @Id
    @Column(name = "pt_newpropertyno_vc", nullable = false)
    private String ptNewPropertyNoVc;

    @Column(name = "pt_finalpropno_vc")
    private String ptFinalPropertyNoVc;

    @Column(name = "pt_propertytax_fl")
    private Double ptPropertyTaxFl;

    @Column(name = "pt_egctax_fl")
    private Double ptEgcTaxFl;

    @Column(name = "pt_treetax_fl")
    private Double ptTreeTaxFl;

    @Column(name = "pt_cleantax_fl")
    private Double ptCleanTaxFl;

    @Column(name = "pt_firetax_fl")
    private Double ptFireTaxFl;

    @Column(name = "pt_lighttax_fl")
    private Double ptLightTaxFl;

    @Column(name = "pt_usercharges_fl")
    private Double ptUserChargesFl;

    @Column(name = "pt_environmenttax_fl")
    private Double ptEnvironmentTaxFl;

    @Column(name = "pt_edurestax_fl")
    private Double ptEduResTaxFl;

    @Column(name = "pt_edunrestax_fl")
    private Double ptEduNonResTaxFl;

    @Column(name = "pt_edutax_fl")
    private Double ptEduTaxFl;

    // ✅ New Taxes
    @Column(name = "pt_watertax_fl")
    private Double ptWaterTaxFl;

    @Column(name = "pt_seweragetax_fl")
    private Double ptSewerageTaxFl;

    @Column(name = "pt_seweragebenefittax_fl")
    private Double ptSewerageBenefitTaxFl;

    @Column(name = "pt_waterbenefittax_fl")
    private Double ptWaterBenefitTaxFl;

    @Column(name = "pt_streettax_fl")
    private Double ptStreetTaxFl;

    @Column(name = "pt_specialconservancytax_fl")
    private Double ptSpecialConservancyTaxFl;

    @Column(name = "pt_municipaledutax_fl")
    private Double ptMunicipalEduTaxFl;

    @Column(name = "pt_specialedutax_fl")
    private Double ptSpecialEduTaxFl;

    @Column(name = "pt_servicecharges_fl")
    private Double ptServiceChargesFl;

    @Column(name = "pt_miscellaneouscharges_fl")
    private Double ptMiscellaneousChargesFl;

    // ✅ Reserve fields for future taxes (tax1 to tax25)
    @Column(name = "pt_tax1_fl") private Double ptTax1Fl;
    @Column(name = "pt_tax2_fl") private Double ptTax2Fl;
    @Column(name = "pt_tax3_fl") private Double ptTax3Fl;
    @Column(name = "pt_tax4_fl") private Double ptTax4Fl;
    @Column(name = "pt_tax5_fl") private Double ptTax5Fl;
    @Column(name = "pt_tax6_fl") private Double ptTax6Fl;
    @Column(name = "pt_tax7_fl") private Double ptTax7Fl;
    @Column(name = "pt_tax8_fl") private Double ptTax8Fl;
    @Column(name = "pt_tax9_fl") private Double ptTax9Fl;
    @Column(name = "pt_tax10_fl") private Double ptTax10Fl;
    @Column(name = "pt_tax11_fl") private Double ptTax11Fl;
    @Column(name = "pt_tax12_fl") private Double ptTax12Fl;
    @Column(name = "pt_tax13_fl") private Double ptTax13Fl;
    @Column(name = "pt_tax14_fl") private Double ptTax14Fl;
    @Column(name = "pt_tax15_fl") private Double ptTax15Fl;
    @Column(name = "pt_tax16_fl") private Double ptTax16Fl;
    @Column(name = "pt_tax17_fl") private Double ptTax17Fl;
    @Column(name = "pt_tax18_fl") private Double ptTax18Fl;
    @Column(name = "pt_tax19_fl") private Double ptTax19Fl;
    @Column(name = "pt_tax20_fl") private Double ptTax20Fl;
    @Column(name = "pt_tax21_fl") private Double ptTax21Fl;
    @Column(name = "pt_tax22_fl") private Double ptTax22Fl;
    @Column(name = "pt_tax23_fl") private Double ptTax23Fl;
    @Column(name = "pt_tax24_fl") private Double ptTax24Fl;
    @Column(name = "pt_tax25_fl") private Double ptTax25Fl;

    @Column(name = "pt_final_tax_fl")
    private Double ptFinalTaxFl;

    @Column(name = "pt_finalyear_vc")
    private String ptFinalYearVc;

    @Column(name = "pt_finalrv_fl")
    private Double ptFinalRvFl;

    @Column(name = "pt_statusDummy_vc")
    private String ptDummyVc;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
