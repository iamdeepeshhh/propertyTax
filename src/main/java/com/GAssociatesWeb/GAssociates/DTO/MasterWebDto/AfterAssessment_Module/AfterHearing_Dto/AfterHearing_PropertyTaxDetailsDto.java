package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_PropertyTaxDetailsDto {
    private String ptNewPropertyNoVc;
    private String ptFinalPropertyNoVc;

    private Double ptPropertyTaxFl;
    private Double ptEgcTaxFl;
    private Double ptTreeTaxFl;
    private Double ptCleanTaxFl;
    private Double ptFireTaxFl;
    private Double ptLightTaxFl;
    private Double ptUserChargesFl;
    private Double ptEnvironmentTaxFl;
    private Double ptEduResTaxFl;
    private Double ptEduNonResTaxFl;
    private Double ptEduTaxFl;

    // ✅ Newly Added Taxes
    private Double ptWaterTaxFl;
    private Double ptSewerageTaxFl;
    private Double ptSewerageBenefitTaxFl;
    private Double ptWaterBenefitTaxFl;
    private Double ptStreetTaxFl;
    private Double ptSpecialConservancyTaxFl;
    private Double ptMunicipalEduTaxFl;
    private Double ptSpecialEduTaxFl;
    private Double ptServiceChargesFl;
    private Double ptMiscellaneousChargesFl;

    // ✅ Reserve fields for future taxes
    private Double ptTax1Fl;
    private Double ptTax2Fl;
    private Double ptTax3Fl;
    private Double ptTax4Fl;
    private Double ptTax5Fl;
    private Double ptTax6Fl;
    private Double ptTax7Fl;
    private Double ptTax8Fl;
    private Double ptTax9Fl;
    private Double ptTax10Fl;
    private Double ptTax11Fl;
    private Double ptTax12Fl;
    private Double ptTax13Fl;
    private Double ptTax14Fl;
    private Double ptTax15Fl;
    private Double ptTax16Fl;
    private Double ptTax17Fl;
    private Double ptTax18Fl;
    private Double ptTax19Fl;
    private Double ptTax20Fl;
    private Double ptTax21Fl;
    private Double ptTax22Fl;
    private Double ptTax23Fl;
    private Double ptTax24Fl;
    private Double ptTax25Fl;

    private Double ptFinalTaxFl;
    private String ptFinalYearVc;
    private Double ptFinalRvFl;
    private String ptDummyVc;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
