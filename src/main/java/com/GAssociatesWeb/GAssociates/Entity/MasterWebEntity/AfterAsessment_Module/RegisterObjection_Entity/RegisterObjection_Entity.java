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

    @Column(name = "rg_wardno_i")
    private Integer wardNo;

    @Column(name = "rg_zoneno_i")
    private Integer zoneNo;

    @Column(name = "rg_surveyno_vc")
    private String surveyNo;

    @Column(name = "rg_finalpropno_vc")
    private String finalPropertyNo;

    @Column(name = "rg_newpropertyno_vc")
    private String newPropertyNo;

    @Column(name = "rg_ownername_vc")
    private String ownerName;

    @Column(name = "rg_reasons_vc" , columnDefinition = "TEXT")
    private String reasons;

    @Column(name = "rg_others_vc")
    private String others;

    @Column(name = "rg_respondent_vc")
    private String respondent;

    @Column(name = "rg_objectiondate_dt")
    private String objectionDate;

    @Column(name = "rg_oldpropno_vc")
    private String oldPropertyNo;

    @Column(name = "rg_userdate_vc")
    private String userDate;

    @Column(name = "rg_usertime_vc")
    private String userTime;

    @Column(name = "notice_no")
    private Integer noticeNo;

    @Column(name = "rg_hearingdate_vc")
    private String hearingDate;

    @Column(name = "rg_hearingtime_vc")
    private String hearingTime;

    @Column(name = "rg_hearingstatus_vc")
    private String hearingStatus;

    @Column(name = "rg_applicationno_vc")
    private String applicationNo;

    @Column(name = "rg_phoneno_vc")
    private String phoneNo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "rg_applicationreceiveddate_dt")
    private String applicationReceivedDate;

    @Column(name = "rg_changedvalue_vc")
    private String changedValue;

}

