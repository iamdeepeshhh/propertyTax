package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RVTypes_MasterDto {
    private Long ryTypeId;
    private String typeNameVc;
    private String rateFl;
    private String appliedTaxesVc;
    private String descriptionVc;
    private String descriptionUpVc;
    private Long categoryId;

    private Set<Long> taxKeysL;

    public RVTypes_MasterDto(Long ryTypeId, String typeNameVc, String rateFl,
                             String appliedTaxesVc, String descriptionVc,
                             String descriptionUpVc, Long categoryId) {
        this.ryTypeId = ryTypeId;
        this.typeNameVc = typeNameVc;
        this.rateFl = rateFl;
        this.appliedTaxesVc = appliedTaxesVc;
        this.descriptionVc = descriptionVc;
        this.descriptionUpVc = descriptionUpVc;
        this.categoryId = categoryId;
    }
}
