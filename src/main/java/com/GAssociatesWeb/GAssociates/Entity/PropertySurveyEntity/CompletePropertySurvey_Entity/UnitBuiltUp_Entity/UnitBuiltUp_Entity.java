package com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitBuiltUp_Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "unit_builtup")
@IdClass(UnitBuiltUpId.class)
public class UnitBuiltUp_Entity {

    @Id
    @Column(name = "pd_newpropertyno_vc", nullable = false, length = 50)
    private String pdNewpropertynoVc;

    @Id
    @Column(name = "ud_floorno_vc", nullable = false, length = 50)
    private String udFloornoVc;

    @Id
    @Column(name = "ud_unitno_vc", nullable = false)
    private Integer udUnitnoVc;

    @Id
    @Column(name = "ub_ids_i", nullable = false)
    private Integer ubIdsI;

    @Column(name = "ub_roomtype_vc", length = 50)
    private String ubRoomtypeVc;

    @Column(name = "ub_length_fl")
    private String ubLengthFl;

    @Column(name = "ub_breadth_fl")
    private String ubBreadthFl;

    @Column(name = "ub_exemptionst_vc", length = 50)
    private String ubExemptionstVc;

    @Column(name = "ub_exemlength_fl")
    private String ubExemlengthFl;

    @Column(name = "ub_exembreadth_fl")
    private String ubExembreadthFl;

    @Column(name = "ub_exemarea_fl")
    private String ubExemareaFl;

    @Column(name = "ub_carpetarea_fl")
    private String ubCarpetareaFl;

    @Column(name = "ub_assesarea_fl")
    private String ubAssesareaFl;

    @Column(name = "ud_timestamp_dt")
    private Date udTimestampDt;

    @Column(name = "ub_legalst_vc", length = 50)
    private String ubLegalstVc;

    @Column(name = "ub_dedpercent_i")
    private String ubDedpercentI;

    @Column(name = "ub_areabeforededuction_fl")
    private String ubareabefdedFl;

    @Column(name = "ub_legalarea_fl")
    private String ubLegalareaFl;

    @Column(name = "ub_illegalarea_fl")
    private String ubIllegalareaFl;

    @Column(name = "ud_unitremark_vc")
    private String udUnitremarkVc;

    @Column(name = "ub_measuretype_vc", length = 50)
    private String ubMeasuretypeVc;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name="plotarea")
    private String plotareaFl;

    @Column(name = "id", nullable = false)
    private Long id;

}
