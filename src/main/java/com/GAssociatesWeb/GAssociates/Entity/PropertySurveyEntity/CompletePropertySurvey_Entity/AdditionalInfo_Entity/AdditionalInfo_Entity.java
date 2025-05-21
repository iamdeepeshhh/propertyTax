package com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.AdditionalInfo_Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "property_additionalinfo")
public class AdditionalInfo_Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pa_propertyid_vc", nullable = false, length = 15)
    private String paPropertyidVc;

    @Column(name = "pa_name_vc", length = 100)
    private String paNameVc;

    @Column(name = "pa_gender_vc", length = 10)
    private String paGenderVc;

    @Column(name = "pa_age_in")
    private String paAgeIn;

    @Column(name = "pa_relation_vc", length = 50)
    private String paRelationVc;

    @Column(name = "pa_qualification_vc", length = 50)
    private String paQualificationVc;

    @Column(name = "pa_occupation_vc", length = 50)
    private String paOccupationVc;

    @Column(name = "pa_citysurveyno_vc", length = 20)
    private String paCitysurveynoVc;

    @Column(name = "pa_adultno_in")
    private String paAdultnoIn;

    @Column(name = "pa_childrenno_in")
    private String paChildrennoIn;

    @Column(name = "pa_ctaxstatus_vc", length = 50)
    private String paCtaxstatusVc;

    @Column(name = "pa_ctaxamount_in")
    private String paCtaxamountIn;

    @Column(name = "pa_nroadname_vc", length = 50)
    private String paNroadnameVc;

    @Column(name = "pa_roadtype_vc", length = 50)
    private String paRoadtypeVc;

    @Column(name = "pa_noofpersons_vc")
    private String paNoofpersonsVc;

    @Column(name = "pa_prelation1_vc")
    private String paPrelation1Vc;

    @Column(name = "pa_pname1_vc")
    private String paPname1Vc;

    @Column(name = "pa_pgender1_vc")
    private String paPgender1Vc;

    @Column(name = "pa_poccupation1_vc")
    private String paPoccupation1Vc;

    @Column(name = "pa_page1_vc")
    private String paPage1Vc;

    @Column(name = "pa_pqualification1_vc")
    private String paPqualification1Vc;

    @Column(name = "pa_prelation2_vc")
    private String paPrelation2Vc;

    @Column(name = "pa_pname2_vc")
    private String paPname2Vc;

    @Column(name = "pa_pgender2_vc")
    private String paPgender2Vc;

    @Column(name = "pa_poccupation2_vc")
    private String paPoccupation2Vc;

    @Column(name = "pa_page2_vc")
    private String paPage2Vc;

    @Column(name = "pa_pqualification2_vc")
    private String paPqualification2Vc;

    @Column(name = "pa_prelation3_vc")
    private String paPrelation3Vc;

    @Column(name = "pa_pname3_vc")
    private String paPname3Vc;

    @Column(name = "pa_pgender3_vc")
    private String paPgender3Vc;

    @Column(name = "pa_poccupation3_vc")
    private String paPoccupation3Vc;

    @Column(name = "pa_page3_vc")
    private String paPage3Vc;

    @Column(name = "pa_pqualification3_vc")
    private String paPqualification3Vc;

    @Column(name = "pa_prelation4_vc")
    private String paPrelation4Vc;

    @Column(name = "pa_pname4_vc")
    private String paPname4Vc;

    @Column(name = "pa_pgender4_vc")
    private String paPgender4Vc;

    @Column(name = "pa_poccupation4_vc")
    private String paPoccupation4Vc;

    @Column(name = "pa_page4_vc")
    private String paPage4Vc;

    @Column(name = "pa_pqualification4_vc")
    private String paPqualification4Vc;

    @Column(name = "pa_prelation5_vc")
    private String paPrelation5Vc;

    @Column(name = "pa_pname5_vc")
    private String paPname5Vc;

    @Column(name = "pa_pgender5_vc")
    private String paPgender5Vc;

    @Column(name = "pa_poccupation5_vc")
    private String paPoccupation5Vc;

    @Column(name = "pa_page5_vc")
    private String paPage5Vc;

    @Column(name = "pa_pqualification5_vc")
    private String paPqualification5Vc;

    @Column(name = "pa_prelation6_vc")
    private String paPrelation6Vc;

    @Column(name = "pa_pname6_vc")
    private String paPname6Vc;

    @Column(name = "pa_pgender6_vc")
    private String paPgender6Vc;

    @Column(name = "pa_poccupation6_vc")
    private String paPoccupation6Vc;

    @Column(name = "pa_page6_vc")
    private String paPage6Vc;

    @Column(name = "pa_pqualification6_vc")
    private String paPqualification6Vc;

    @Column(name = "pa_prelation7_vc")
    private String paPrelation7Vc;

    @Column(name = "pa_pname7_vc")
    private String paPname7Vc;

    @Column(name = "pa_pgender7_vc")
    private String paPgender7Vc;

    @Column(name = "pa_poccupation7_vc")
    private String paPoccupation7Vc;

    @Column(name = "pa_page7_vc")
    private String paPage7Vc;

    @Column(name = "pa_pqualification7_vc")
    private String paPqualification7Vc;

    @Column(name = "pa_prelation8_vc")
    private String paPrelation8Vc;

    @Column(name = "pa_pname8_vc")
    private String paPname8Vc;

    @Column(name = "pa_pgender8_vc")
    private String paPgender8Vc;

    @Column(name = "pa_poccupation8_vc")
    private String paPoccupation8Vc;

    @Column(name = "pa_page8_vc")
    private String paPage8Vc;

    @Column(name = "pa_pqualification8_vc")
    private String paPqualification8Vc;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}