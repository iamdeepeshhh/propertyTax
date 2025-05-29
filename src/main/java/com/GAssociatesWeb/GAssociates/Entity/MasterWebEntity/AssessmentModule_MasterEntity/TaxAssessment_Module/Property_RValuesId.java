package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor // Generates an all-argument constructor
@NoArgsConstructor   // Generates a no-argument constructor
public class Property_RValuesId implements Serializable {
    private String prvPropertyNoVc;  // Property number
    private String prvUnitNoVc;      // Unit number


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property_RValuesId that = (Property_RValuesId) o;
        return Objects.equals(prvPropertyNoVc, that.prvPropertyNoVc) &&
                Objects.equals(prvUnitNoVc, that.prvUnitNoVc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prvPropertyNoVc, prvUnitNoVc);
    }
}
