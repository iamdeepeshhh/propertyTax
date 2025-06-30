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

    private String pd_newpropertyno_vc;
    private String ud_floorno_vc;
    private Integer ud_unitno_vc;
    private Integer ud_occupantstatus_i;
    private String ud_rentalno_vc;
    private String ud_occupiername_vc;
    private String ud_agreementcopy_vc;
    private String ud_mobileno_vc;
    private String ud_emailid_vc;
    private Integer ud_usagetype_i;
    private Integer ud_usagesubtype_i;
    private Integer ud_const_age_i;
    private String ud_constructionclass_i;
    private String ud_agefactor_i;
    private String ud_carpetarea_f;
    private String ud_exempted_area_f;
    private String ud_assessmentarea_f;
    private String ud_signature_vc;
    private LocalDate ud_timestamp_dt;
    private String ud_totleg_area_f;
    private String ud_totillegarea_f;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String ud_unitremark_vc;
    private String ud_constyear_dt;
    private String ud_establishyear_i;
    private String ud_orgagefactor_vc;
    private String ud_areabefded_fl;

}

