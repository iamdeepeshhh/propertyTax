package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "afterhearing_unit_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_ProposedRValues {
    @Id
    @Column(name = "pr_newpropertyno_vc",nullable = false)
    private String PrNewPropertyNoVc;

    @Column(name = "pr_finalpropno_vc")
    private String PrFinalPropNoVc;

    @Column(name = "pr_residential_fl")
    private Double PrResidentialFl;

    @Column(name = "pr_commercial_fl")
    private Double PrCommercialFl;

    @Column(name = "pr_industrial_fl")
    private Double PrIndustrialFl;

    @Column(name = "pr_religious_fl")
    private Double PrReligiousFl;

    @Column(name = "pr_educational_fl")
    private Double PrEducationalFl;

    @Column(name = "pr_mobiletower_fl")
    private Double PrMobileTowerFl;

    @Column(name = "pr_electricsubstation_fl")
    private Double PrElectricSubstationFl;

    @Column(name = "pr_government_fl")
    private Double PrGovernmentFl;

    @Column(name = "pr_residentialopenplot_fl")
    private Double PrResidentialOpenPlotFl;

    @Column(name = "pr_commercialopenplot_fl")
    private Double PrCommercialOpenPlotFl;

    @Column(name = "pr_industrialopenplot_fl")
    private Double PrIndustrialOpenPlotFl;

    @Column(name = "pr_religiousopenplot_fl")
    private Double PrReligiousOpenPlotFl;

    @Column(name = "pr_educationlegalopenplot_fl")
    private Double PrEducationAndLegalInstituteOpenPlotFl;

    @Column(name = "pr_governmentopenplot_fl")
    private Double PrGovernmentOpenPlotFl;

    @Column(name = "pr_totalrv_fl")
    private Double PrTotalRatableValueFl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
