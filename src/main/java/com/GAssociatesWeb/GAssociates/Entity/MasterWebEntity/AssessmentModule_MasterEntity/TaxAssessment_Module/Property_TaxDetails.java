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
