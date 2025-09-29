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

    // ✅ Newly Added Taxes
    private Double waterTaxFl;
    private Double sewerageTaxFl;
    private Double sewerageBenefitTaxFl;
    private Double waterBenefitTaxFl;
    private Double streetTaxFl;
    private Double specialConservancyTaxFl;
    private Double municipalEducationTaxFl;
    private Double specialEducationTaxFl;
    private Double serviceChargesFl;
    private Double miscellaneousChargesFl;

    // Optional: Add flexible slots if you support dynamic future taxes
    private Double tax1Fl;
    private Double tax2Fl;
    private Double tax3Fl;
    private Double tax4Fl;
    private Double tax5Fl;
    private Double tax6Fl;
    private Double tax7Fl;
    private Double tax8Fl;
    private Double tax9Fl;
    private Double tax10Fl;
    private Double tax11Fl;
    private Double tax12Fl;
    private Double tax13Fl;
    private Double tax14Fl;
    private Double tax15Fl;
    private Double tax16Fl;
    private Double tax17Fl;
    private Double tax18Fl;
    private Double tax19Fl;
    private Double tax20Fl;
    private Double tax21Fl;
    private Double tax22Fl;
    private Double tax23Fl;
    private Double tax24Fl;
    private Double tax25Fl;

    private Double totalTaxFl;
    private String finalPropertyNo;

    
}
