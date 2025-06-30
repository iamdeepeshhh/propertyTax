package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "afterhearing_unit_builtup")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_UnitBuiltupDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pd_newpropertyno_vc")
    private String newPropertyNo;

    @Column(name = "ud_floorno_vc")
    private String floorNo;

    @Column(name = "ud_unitno_vc")
    private Integer unitNo;

    @Column(name = "ub_ids_i")
    private Integer builtupId;

    @Column(name = "ub_roomtype_vc")
    private String roomType;

    @Column(name = "ub_length_fl")
    private String length;

    @Column(name = "ub_breadth_fl")
    private String breadth;

    @Column(name = "ub_exemptionst_vc")
    private String exemptionStatus;

    @Column(name = "ub_exemlength_fl")
    private String exemptionLength;

    @Column(name = "ub_exembreadth_fl")
    private String exemptionBreadth;

    @Column(name = "ub_exemarea_fl")
    private String exemptionArea;

    @Column(name = "ub_carpetarea_fl")
    private String carpetArea;

    @Column(name = "ub_assesarea_fl")
    private String assessableArea;

    @Column(name = "ud_timestamp_dt")
    private LocalDate timestamp;

    @Column(name = "ub_legalst_vc")
    private String legalStatus;

    @Column(name = "ub_legalarea_fl")
    private String legalArea;

    @Column(name = "ub_illegalarea_fl")
    private String illegalArea;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "ud_unitremark_vc")
    private String unitRemark;

    @Column(name = "ub_measuretype_vc")
    private String measureType;

    @Column(name = "ub_orgassessarea_vc")
    private String originalAssessArea;

    @Column(name = "ub_dedpercent_i")
    private String deductionPercent;

    @Column(name = "ub_areabeforededuction_fl")
    private String areaBeforeDeduction;

    @Column(name = "plotarea")
    private String plotArea;
}
