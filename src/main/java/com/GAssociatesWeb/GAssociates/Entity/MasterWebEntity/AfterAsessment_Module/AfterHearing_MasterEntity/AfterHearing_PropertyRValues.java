package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module.Property_RValuesId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "afterhearing_property_rvalues")
@IdClass(Property_RValuesId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_PropertyRValues {

    @Id
    @Column(name = "prv_propertyno_vc", nullable = false)
    private String prvPropertyNoVc;

    @Id
    @Column(name = "prv_unitno_vc", nullable = false)
    private String prvUnitNoVc;

    @Column(name = "prv_finalpropno_vc", length = 50)
    private String prvFinalPropNoVc;

    @Column(name = "prv_rate_f")
    private Double prvRatePerSqMFl;

    @Column(name = "prv_lentingvalue_f")
    private Double prvRentalValAsPerRateFl;

    @Column(name = "prv_depper_i")
    private Integer prvDepreciationRateFl;

    @Column(name = "prv_depamount_f")
    private Double prvDepreciationAmountFl;

    @Column(name = "prv_alv_f")
    private Double prvAmountAfterDepreciationFl;

    @Column(name = "prv_mainval_f")
    private Double prvMaintenanceRepairsFl;

    @Column(name = "prv_taxvalue_f")
    private Double prvTaxableValueByRateFl;

    @Column(name = "prv_tenantname_vc")
    private String prvTenantNameVc;

    @Column(name = "prv_rent_fl")
    private Double prvActualMonthlyRentFl;

    @Column(name = "prv_yearlyrent_fl")
    private Double prvActualAnnualRentFl;

    @Column(name = "prv_temainval_fl")
    private Double prvMaintenanceRepairsRentFl;

    @Column(name = "prv_tentantval_f")
    private Double prvTaxableValueByRentFl;

    @Column(name = "prv_ratablevalue_f")
    private Double prvTaxableValueConsideredFl;

    @Column(name = "prv_finyear_vc", length = 20)
    private String prvFinancialYearVc;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //added age factor field so that we dont get issues further
    @Column(name = "prv_agefactor_vc")
    private String prvAgeFactorVc;
}
