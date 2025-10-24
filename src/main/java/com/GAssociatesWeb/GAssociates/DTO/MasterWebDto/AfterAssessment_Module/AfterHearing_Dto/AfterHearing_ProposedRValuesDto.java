package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_ProposedRValuesDto {
    private String prNewPropertyNoVc;                     // New Property Number
    private String prFinalPropNoVc;                       // Final Property Number

    private Double prResidentialFl;                       // Residential
    private Double prCommercialFl;                        // Commercial
    private Double prIndustrialFl;                        // Industrial
    private Double prReligiousFl;                         // Religious
    private Double prEducationalFl;                       // Educational
    private Double prMobileTowerFl;                       // Mobile Tower
    private Double prElectricSubstationFl;                // Electric Substation
    private Double prGovernmentFl;                        // Government

    // ✅ Open Plot Ratable Values
    private Double prResidentialOpenPlotFl;               // Residential Open Plot
    private Double prCommercialOpenPlotFl;                // Commercial Open Plot
    private Double prIndustrialOpenPlotFl;                // Industrial Open Plot
    private Double prReligiousOpenPlotFl;                 // Religious Open Plot
    private Double prEducationAndLegalInstituteOpenPlotFl;// Education/Legal Open Plot
    private Double prGovernmentOpenPlotFl;                // Government Open Plot

    private Double prTotalRatableValueFl;                 // Total Ratable Value

    private LocalDateTime createdAt;                      // Created Timestamp
    private LocalDateTime updatedAt;                      // Updated Timestamp

    // -----------------------------------------
    // 🔹 Additional Analytical / Report Fields
    // -----------------------------------------
    private Integer id;                     // For internal tracking if needed
    private String finalPropertyNo;         // Alternate naming for reports
    private Double finalRatableValue;       // Calculated total RV for reports
    private String finalYearDate;           // Year/date range for RV
    private String financialYear;           // Financial year (e.g. 2024-25)
    private Integer numberOfCollectedValues;// Total categories contributing to total RV

    // ---- Area and ALV fields ----
    private Double areaLetOut;
    private Double areaNotLetOut;
    private Double alvLetOut;
    private Double alvNotLetOut;
}
