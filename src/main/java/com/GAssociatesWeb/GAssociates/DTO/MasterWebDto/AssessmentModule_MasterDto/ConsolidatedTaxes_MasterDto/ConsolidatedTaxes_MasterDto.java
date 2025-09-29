package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.ConsolidatedTaxes_MasterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsolidatedTaxes_MasterDto {
    private Long id;
    private String taxNameVc;
    private String taxRateFl;
    private String applicableonVc;

    private Boolean isActiveBl;
//    private String taxNameLocalVc;//for Local language
//    private String taxNameStandardVc;//for Standard generic language
    private String taxType;//is it (cess or tax)
    private String descriptionVc;//optional field for giving tax description
    private Long taxKeyL;
    private Integer positionI;
}