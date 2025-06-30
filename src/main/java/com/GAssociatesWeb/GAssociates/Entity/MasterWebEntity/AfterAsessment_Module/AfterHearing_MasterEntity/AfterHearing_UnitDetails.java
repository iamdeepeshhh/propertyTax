package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "afterhearing_unit_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_UnitDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pd_newpropertyno_vc")
    private String newPropertyNo;

    @Column(name = "ud_floorno_vc")
    private String floorNo;

    @Column(name = "ud_unitno_vc")
    private Integer unitNo;

    @Column(name = "ud_occupantstatus_i")
    private Integer occupantStatus;

    @Column(name = "ud_rentalno_vc")
    private String rentalNo;

    @Column(name = "ud_occupiername_vc")
    private String occupierName;

    @Column(name = "ud_agreementcopy_vc")
    private String agreementCopy;

    @Column(name = "ud_mobileno_vc")
    private String mobileNo;

    @Column(name = "ud_emailid_vc")
    private String emailId;

    @Column(name = "ud_usagetype_i")
    private Integer usageType;

    @Column(name = "ud_usagesubtype_i")
    private Integer usageSubType;

    @Column(name = "ud_const_age_i")
    private Integer constructionAge;

    @Column(name = "ud_constructionclass_i")
    private String constructionClass;

    @Column(name = "ud_agefactor_i")
    private String ageFactor;

    @Column(name = "ud_carpetarea_f")
    private String carpetArea;

    @Column(name = "ud_exempted_area_f")
    private String exemptedArea;

    @Column(name = "ud_assessmentarea_f")
    private String assessmentArea;

    @Column(name = "ud_signature_vc")
    private String signature;

    @Column(name = "ud_timestamp_dt")
    private LocalDate timestamp;

    @Column(name = "ud_totleg_area_f")
    private String totalLegalArea;

    @Column(name = "ud_totillegarea_f")
    private String totalIllegalArea;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "ud_unitremark_vc")
    private String unitRemark;

    @Column(name = "ud_constyear_dt")
    private String constructionYear;

    @Column(name = "ud_establishyear_i")
    private String establishmentYear;

    @Column(name = "ud_orgagefactor_vc")
    private String originalAgeFactor;

    @Column(name = "ud_areabefded_fl")
    private String areaBeforeDeduction;
}

