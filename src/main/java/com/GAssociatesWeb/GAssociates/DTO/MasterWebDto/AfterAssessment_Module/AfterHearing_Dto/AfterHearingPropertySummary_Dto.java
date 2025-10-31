package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto;

import lombok.Data;

@Data
public class AfterHearingPropertySummary_Dto {
    private String newPropertyNoVc;
    private String finalPropertyNoVc;
    private String ownerNameVc;
    private String wardNoVc;
    private String zoneNoVc;

    // 🔹 Aggregated from all units
    private Double totalCarpetAreaFl;
    private Double totalAssessmentAreaFl;
    private Double totalYearlyRentFl;            // भाडे (वार्षिक)
    private Double totalAlvFl;                   // वार्षिक मूल्य (ALV)
    private Double totalCombinedAlvRentFl;       // एकूण (भाडे + अल्व)
    private Double totalDepreciationPercentFl;   // दरकपात %
    private Double totalDepreciationAmountFl;    // दरकपात रक्कम
    private Double totalRatableValueFl;          // करयोग्य मूल्य
    private Double consideredRvFl;               // दरयोग्य मूल्य (RV)


    // 🔹 Aggregated taxes (if available)
    private Double totalBeforeTaxFl;
    private Double totalAfterTaxFl;

    // Final RV/Tax snapshots (before/after hearing)
    private Double beforeFinalRvFl;
    private Double beforeFinalTaxFl;
    private Double afterFinalRvFl;
    private Double afterFinalTaxFl;
}
