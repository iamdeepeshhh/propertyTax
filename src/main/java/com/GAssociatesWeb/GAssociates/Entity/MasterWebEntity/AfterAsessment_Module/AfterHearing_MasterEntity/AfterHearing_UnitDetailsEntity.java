package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity;

import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity.PropertyDetails_Entity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitDetails_Entity.UnitDetailsId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "afterhearing_unit_details")
@Data
@NoArgsConstructor
@IdClass(UnitDetailsId.class)
@AllArgsConstructor
public class AfterHearing_UnitDetailsEntity {

    @Id
    @Column(name = "pd_newpropertyno_vc", nullable = false, length = 50)
    private String pdNewpropertynoVc;

    @Id
    @Column(name = "ud_floorno_vc", nullable = false, length = 50)
    private String udFloornoVc;

    @Id
    @Column(name = "ud_unitno_vc", nullable = false)
    private Integer udUnitnoVc;

    @Column(name = "ud_occupantstatus_i")
    private Integer udOccupantstatusI;

    @Column(name = "ud_rentalno_vc", length = 50)
    private String udRentalnoVc;

    @Column(name = "ud_occupiername_vc", length = 100)
    private String udOccupiernameVc;

    @Column(name = "ud_agreementcopy_vc", columnDefinition = "TEXT")
    private String udAgreementcopyVc;

    @Column(name = "ud_mobileno_vc", length = 50)
    private String udMobilenoVc;

    @Column(name = "ud_emailid_vc", length = 40)
    private String udEmailidVc;

    @Column(name = "ud_usagetype_i")
    private Integer udUsagetypeI;

    @Column(name = "ud_usagesubtype_i")
    private Integer udUsagesubtypeI;

    @Column(name = "ud_const_age_i")
    private Integer udConstAgeI;

    @Column(name = "ud_constructionclass_i", length = 50)
    private String udConstructionclassI;

    @Column(name = "ud_agefactor_i", length = 30)
    private String udAgefactorI;

    @Column(name = "ud_carpetarea_f")
    private String udCarpetareaF;

    @Column(name = "ud_exempted_area_f")
    private String udExemptedAreaF;

    @Column(name = "ud_assessmentarea_f")
    private String udAssessmentareaF;

    @Column(name = "ud_signature_vc", columnDefinition = "TEXT")
    private String udSignatureVc;

    @Column(name = "ud_timestamp_dt")
    private Date udTimestampDt;

    @Column(name = "ud_totleg_area_f")
    private String udTotlegAreaF;

    @Column(name = "ud_totillegarea_f")
    private String udTotillegareaF;

    @Column(name = "ud_unitremark_vc")
    private String udUnitremarkVc;

    @Column(name = "ud_constyear_dt")
    private String udConstyearDt;

    @Column(name = "ud_establishname_vc")
    private String udEstablishmentNameVc;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ud_tenantno_i")
    private String udTenantnoI;

    @Column(name = "ud_agefact_vc")
    private String udAgefactVc;

    @Column(name = "ud_plotarea_fl")
    private String udPlotAreaFl;

    @Column(name = "ud_ass_vc")
    private String udAssVc;

    @Column(name = "ud_areabefded_fl")
    private String udAreaBefDedF;

    @ManyToOne
    @JoinColumn(name = "pd_newpropertyno_vc", referencedColumnName = "pd_newpropertyno_vc", insertable = false, updatable = false)
    private PropertyDetails_Entity propertyDetails;
}

