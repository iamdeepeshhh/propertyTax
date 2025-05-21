package com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitBuiltUp_Entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UnitBuiltUpId {

    private String pdNewpropertynoVc;
    private String udFloornoVc;
    private Integer udUnitnoVc;
    private Integer ubIdsI;

}
