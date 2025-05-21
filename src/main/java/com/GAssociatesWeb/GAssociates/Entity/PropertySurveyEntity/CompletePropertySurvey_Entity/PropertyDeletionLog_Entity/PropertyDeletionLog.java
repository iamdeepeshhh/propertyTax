package com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDeletionLog_Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PropertyDeletionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ward_no")
    private String wardnoVc;

    @Column(name = "survey_prop_no")
    private String surveyPropNo;

    @Column(name = "pd_newpropertyno_vc")
    private String pdNewpropertynoVc;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "record_owner")
    private String recordOwner;

    @Column(name = "username")
    private String username;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "deletion_time")
    private String deletionTime;

    @Column(name = "base64image1", columnDefinition = "TEXT")
    private String base64Image1;

    @Column(name = "base64image2", columnDefinition = "TEXT")
    private String base64Image2;

    @Column(name = "base64house_plan1", columnDefinition = "TEXT")
    private String base64HousePlan1;

    @Column(name = "base64house_plan2", columnDefinition = "TEXT")
    private String base64HousePlan2;

}
