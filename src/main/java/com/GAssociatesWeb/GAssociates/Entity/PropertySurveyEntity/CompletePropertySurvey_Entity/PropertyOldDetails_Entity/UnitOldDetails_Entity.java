package com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyOldDetails_Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "unit_olddetails", schema = "public")
@ToString(exclude = {"propertyOldDetails"})
public class UnitOldDetails_Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_olddetails_id_seq")
    @SequenceGenerator(name = "unit_olddetails_id_seq", sequenceName = "unit_olddetails_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "pod_ward_i", nullable = false)
    private String podWardI;

    @Column(name = "pod_oldpropno_vc", nullable = false)
    private String podOldPropNoVc;

    @Column(name = "occupancy")
    private String occupancy;

    @Column(name = "floor_number")
    private String floorNumber;

    @Column(name = "old_property_class")
    private String oldPropertyClass;

    @Column(name = "construction_year")
    private String constructionYear;

    @Column(name = "unit_usage_type")
    private String unitUsageType;

    @Column(name = "unit_sub_usage_type")
    private String unitSubUsageType;

    @Column(name = "assessment_area")
    private String assessmentArea;

    // Many-to-one relationship to PropertyOldDetails_Entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_no", referencedColumnName = "pod_refno_vc")
    private PropertyOldDetails_Entity propertyOldDetails;

}
