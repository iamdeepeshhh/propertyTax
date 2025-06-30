package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_Entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "prorv_propertyno_vc")
    private String propertyNo;

    @Column(name = "prorv_finalpropno_vc")
    private String finalPropertyNo;

    @Column(name = "prorv_rv_res")
    private Double rvResidential;

    @Column(name = "prorv_rv_mob")
    private Double rvMobile;

    @Column(name = "prorv_rv_ele")
    private Double rvElectric;

    @Column(name = "prorv_rv_com")
    private Double rvCommercial;

    @Column(name = "prorv_rv_gov")
    private Double rvGovernment;

    @Column(name = "prorv_rv_edu")
    private Double rvEducation;

    @Column(name = "prorv_rv_reg")
    private Double rvReligious;

    @Column(name = "prorv_rv_comop")
    private Double rvCommercialOpen;

    @Column(name = "prorv_rv_resop")
    private Double rvResidentialOpen;

    @Column(name = "prorv_rv_indus")
    private Double rvIndustrial;

    @Column(name = "prorv_final_rv")
    private Double finalRatableValue;

    @Column(name = "prorv_finalyear_dt")
    private String finalYearDate;

    @Column(name = "propletout")
    private Double areaLetOut;

    @Column(name = "propnotletout")
    private Double areaNotLetOut;

    @Column(name = "prorv_finyear_vc")
    private String financialYear;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "alvletout")
    private Double alvLetOut;

    @Column(name = "alvnotletout")
    private Double alvNotLetOut;

    @Column(name = "prorv_rv_regop")
    private Double rvReligiousOpen;

    @Column(name = "prorv_rv_govop")
    private Double rvGovernmentOpen;

    @Column(name = "prorv_rv_eduop")
    private Double rvEducationOpen;

    @Column(name = "prorv_rv_indusop")
    private Double rvIndustrialOpen;

    @Column(name = "noofcolvalues")
    private Integer numberOfCollectedValues;
}
