package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto;

import lombok.Data;

@Data
public class ConsolidatedTaxDetailsDto {
    private Double propertyTaxFl;
    private Double educationTaxResidFl;
    private Double educationTaxCommFl;
    private Double educationTaxTotalFl;
    private Double egcFl; // Employment Guarantee Cess
    private Double treeTaxFl;
    private Double environmentalTaxFl;
    private Double cleannessTaxFl;
    private Double lightTaxFl;
    private Double fireTaxFl;
    private Double userChargesFl;
    private Double totalTaxFl;
    private String finalPropertyNo;
}
