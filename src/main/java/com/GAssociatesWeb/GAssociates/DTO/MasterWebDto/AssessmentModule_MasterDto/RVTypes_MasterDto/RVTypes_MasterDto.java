package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
