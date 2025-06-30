package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "afterhearing_property_rvalues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_PropertyRValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "prv_propertyno_vc")
    private String propertyNo;

    @Column(name = "prv_finalpropno_vc")
    private String finalPropertyNo;

    @Column(name = "prv_unitno_vc")
    private String unitNo;

    @Column(name = "prv_rate_f")
    private Double rate;

    @Column(name = "prv_lentingvalue_f")
    private Double lettingValue;

    @Column(name = "prv_depper_i")
    private Integer depreciationPercentage;

    @Column(name = "prv_depamount_f")
    private Double depreciationAmount;

    @Column(name = "prv_alv_f")
    private Double annualLettingValue;

    @Column(name = "prv_mainval_f")
    private Double mainValue;

    @Column(name = "prv_taxvalue_f")
    private Double taxValue;

    @Column(name = "prv_tenantname_vc")
    private String tenantName;

    @Column(name = "prv_rent_fl")
    private Double rent;

    @Column(name = "prv_yearlyrent_fl")
    private Double yearlyRent;

    @Column(name = "prv_temainval_fl")
    private Double tenantMainValue;

    @Column(name = "prv_tentantval_f")
    private Double tenantValue;

    @Column(name = "prv_ratablevalue_f")
    private Double ratableValue;

    @Column(name = "prv_finyear_vc")
    private String financialYear;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "statusdummy")
    private String statusDummy;

}
