package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "afterhearing_property_taxdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_PropertyTaxDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pt_newpropertyno_vc")
    private String newPropertyNo;

    @Column(name = "pt_finalpropno_vc")
    private String finalPropertyNo;

    @Column(name = "pt_propertytax_fl")
    private Double propertyTax;

    @Column(name = "pt_egctax_fl")
    private Double egcTax;

    @Column(name = "pt_treetax_fl")
    private Double treeTax;

    @Column(name = "pt_envtax_fl")
    private Double environmentTax;

    @Column(name = "pt_lighttax_fl")
    private Double lightTax;

    @Column(name = "pt_cleantax_fl")
    private Double cleanTax;

    @Column(name = "pt_firetax_fl")
    private Double fireTax;

    @Column(name = "pt_usercharges_fl")
    private Double userCharges;

    @Column(name = "pt_edurestax_fl")
    private Double educationResidentialTax;

    @Column(name = "pt_edunrestax_fl")
    private Double educationNonResidentialTax;

    @Column(name = "pt_edutax_fl")
    private Double totalEducationTax;

    @Column(name = "pt_final_tax_fl")
    private Double finalTax;

    @Column(name = "pt_finalyear_vc")
    private String finalYear;

    @Column(name = "pt_finalrv_fl")
    private Double finalRatableValue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}