package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxDepreciation_MasterDto;

import lombok.Data;

@Data
public class TaxDepreciation_MasterDto {
    private Long id;
    private String constructionClassVc;
    private String minAgeI;
    private String maxAgeI;
    private String depreciationPercentageI;//datatype change required

    // Getters and Setters
}