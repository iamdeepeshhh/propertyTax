package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.RegisterObjection_Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "register_objection")
@Data                    // Generates getters, setters, equals, hashCode, toString
@NoArgsConstructor       // Generates no-argument constructor
@AllArgsConstructor      // Generates all-arguments constructor
public class RegisterObjection_Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer rg_wardno_i;
    private Integer rg_zoneno_i;
    private String rg_surveyno_vc;
    private String rg_finalpropno_vc;
    private String rg_newpropertyno_vc;
    private String rg_ownername_vc;
    private String rg_reasons_vc;
    private String rg_others_vc;
    private String rg_respondent_vc;
    private String rg_objectiondate_dt;
    private String rg_oldpropno_vc;
    private String rg_userdate_vc;
    private String rg_usertime_vc;
    private Integer notice_no;
    private String rg_hearingdate_vc;
    private String rg_hearingtime_vc;
    private String rg_hearingstatus_vc;
    private String rg_applicationno_vc;
    private String rg_phoneno_vc;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String rg_applicationreceiveddate_dt;
    private String rg_changedvalue_vc;

}

