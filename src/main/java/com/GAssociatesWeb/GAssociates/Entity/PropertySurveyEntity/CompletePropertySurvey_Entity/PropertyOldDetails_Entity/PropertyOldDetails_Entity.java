package com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyOldDetails_Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "property_olddetails", schema = "public")
@ToString(exclude = {"unitDetails"})
public class PropertyOldDetails_Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_olddetails_id_seq")
    @SequenceGenerator(name = "property_olddetails_id_seq", sequenceName = "property_olddetails_id_seq", allocationSize = 1)
    private Integer id;//sequence is there on this and another value as well this sequence will get generate automatically

    @Column(name = "pod_zone_i")
    private String podZoneI;

    @Column(name = "pod_ward_i", nullable = false)
    private String podWardI;

    @Column(name = "pod_oldpropno_vc", nullable = false)
    private String podOldPropNoVc;

    @Column(name = "pod_newpropertyno_vc")
    private String podNewPropertyNoVc;

    @Column(name = "pod_ownername_vc", length = 3000)
    private String podOwnerNameVc;

    @Column(name = "pod_occupiername_vc", length = 3000)
    private String podOccupierNameVc;

    @Column(name = "pod_propertyaddress_vc")
    private String podPropertyAddressVc;

    @Column(name = "pod_constclass_vc", length = 50)
    private String podConstClassVc;

    @Column(name = "pod_propertytype_i", length = 35)
    private String podPropertyTypeI;

    @Column(name = "pod_propertysubtype_i", length = 35)
    private String podPropertySubTypeI;

    @Column(name = "pod_usagetype_i", length = 35)
    private String podUsageTypeI;

    @Column(name = "pod_noofrooms_i", length = 35)
    private String podNoofrooms;

    @Column(name = "pod_plotvalue_f")
    private String podPlotvalue;

    @Column(name = "pod_totpropertyvalue_f")
    private String podTotalPropertyvalue;

    @Column(name = "pod_buildingvalue_vc")
    private String podBuildingvalue;

    @Column(name = "pod_builtuparea_fl")
    private String podBuiltUpAreaFl;

    @Column(name = "pod_lastassdt_dt")
    private String podLastAssDtDt;

    @Column(name = "pod_currentass_dt")
    private String podCurrentAssDt;

    @Column(name = "pod_totaltax_fl")
    private String podTotalTaxFl;

    @Column(name = "pod_propertytax_fl")
    private String podPropertyTaxFl;

    @Column(name = "pod_educess_fl")
    private String podEduCessFl;

    @Column(name = "pod_egc_fl")
    private String podEgcFl;

    @Column(name = "pod_treetax_fl")
    private String podTreeTaxFl;

    @Column(name = "pod_envtax_fl")
    private String podEnvTaxFl;

    @Column(name = "pod_firetax_fl")
    private String podFireTaxFl;

    @Column(name = "pod_created_dt")
    private LocalDateTime podCreatedDt;

    @Column(name = "pod_updated_dt")
    private LocalDateTime podUpdatedDt;

    @Column(name = "pod_refno_vc",unique = true,nullable = false)
    private Integer podRefNoVc;//for this will be generating sequence interanally in my web application itself only

    @Column(name = "pd_newpropertyno_vc")
    private String pdNewPropertyNoVc;

    @Column(name = "pd_constructionyear_d")
    private String pdConstructionYearD;

    @Column(name = "pod_totalratablevalue_i")
    private String podTotalRatableValue;

    @Column(name = "pod_totalassessmentarea_fl")
    private String podTotalAssessmentArea;

    @Column(name = "pod_totalassessmentareaft_fl")
    private String podTotalAssessmentAreaFt; //field added for sqft

    @Column(name = "userid")
    private String userid;

    @OneToMany(mappedBy = "propertyOldDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnitOldDetails_Entity> unitDetails;
}
