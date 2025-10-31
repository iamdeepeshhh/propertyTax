package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.TaxBills_MasterDto;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ProposedRatableValueDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxBills_MasterDto {

    private String pdBillnoVc;            // Bill No.
    private String pdWardI;                 // Ward No.
    private String pdZoneI;                 // Zone No.
    private String billDateDt;              // Bill Date
    private String pdFinalpropnoVc;       // New Property No.
    private String pdOldpropnoVc;           // Old Property No.
    private String pdSurypropnoVc;          // Survey Property No.
    private String pdUsageType;
    private String pdNewpropertynoVc;
    private String pdOwnernameVc;           // Owner Name
    private String pdOccupinameF;           // Occupier Name

    private String pdPropertyaddressVc;     // Property Address
    private Double pdAssesareaF;
    private ProposedRatableValueDetailsDto proposedRatableValueDetailsDto;
    private String financialYear;  // Current bill year
    private Map<String, Map<Long, Double>> arrearsYearWiseMap;  // 🔹 Arrears: Year → (TaxKey → Value)
    private Map<Long, Double> currentTaxMap;                    // 🔹 Current Year
    private Map<Long, Double> totalTaxMap;
    private String arrearsRangeVc;  // e.g. "2015-2016 ते 2018-2019"

}