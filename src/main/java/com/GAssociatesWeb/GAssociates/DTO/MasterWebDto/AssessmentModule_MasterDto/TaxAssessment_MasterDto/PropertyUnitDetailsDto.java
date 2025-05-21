package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto;

import lombok.Data;

@Data
public class PropertyUnitDetailsDto {

    private String newPropertyNo;
    private String finalPropertyNo;
    private String occupantStatusI;
    private String constAgeI;
    private String unitNoVc;
    private String floorNoVc;
    private String usageTypeVc;
    private String usageSubTypeVc;
    private String constructionTypeVc;
    private String constructionYearVc;
    private String constructionAgeVc;
    private String ageFactorVc;
    private String plotAreaFl;
    private String exemptedAreaFl;
    private String tenantNameVc;
    private String carpetAreaFl;
    private String taxableAreaFl;
    private Double rentValueByRateFl;
    private Double depreciationRateFl;
    private Double depreciationAmountFl;
    private Double valueAfterDepreciationFl;
    private Double maintenanceRepairsFl;
    private Double maintenanceRepairsRentFl;
    private Double taxableValueByRateFl;

    private Double actualMonthlyRentFl;
    private Double actualAnnualRentFl;
    private Double taxableValueByRentFl;
    private Double taxableValueConsideredFl;
    private Double ratePerSqMFl;
    private Double rentalValAsPerRateFl;
    private Double amountAfterDepreciationFl;

}
