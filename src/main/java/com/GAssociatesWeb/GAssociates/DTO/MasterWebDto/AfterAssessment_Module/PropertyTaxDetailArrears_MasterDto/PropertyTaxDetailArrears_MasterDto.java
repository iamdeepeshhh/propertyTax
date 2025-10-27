package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.PropertyTaxDetailArrears_MasterDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyTaxDetailArrears_MasterDto {
    // 🧱 Identification
    private Integer arrearsId;
    private Integer ward;
    private Integer zone;
    private String surveyPropertyNo;
    private String finalPropertyNo;
    private String oldPropertyNo;
    private String newPropertyNo;
    private String arrFinalPropertyNo;

    // 👤 Owner Details
    private String ownerName;
    private String addNewOwnerName;
    private String occupierName;

    // 📅 Year and Status
    private String financialYear;
    private String ptFinalYear;
    private String ptStatus;

    // 🕒 Audit Info
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 💰 Arrears Tax Components
    private Double propertyTax;
    private Double educationTax;
    private Double treeTax;
    private Double environmentTax;
    private Double fireTax;
    private Double electricityTax;
    private Double userCharges;
    private Double lightTax;
    private Double cleanTax;
    private Double interest;
    private Double waterTax;
    private Double egcTax;
    private Double totalTax;
    private Double noticeFee;
    private Double miscellaneousTax;
    private Double otherTax;
    private Double penalty;
    private Double discount;

    // 💸 After Hearing Specific Taxes
    private Double ptPropertyTax;
    private Double ptEduTax;
    private Double ptEgcTax;
    private Double ptTreeTax;
    private Double ptLightTax;
    private Double ptFireTax;
    private Double ptCleanTax;
    private Double ptEnvTax;
    private Double ptWaterTax;
    private Double ptUserCharges;
    private Double ptMiscellaneousCharges;
    private Double ptServiceCharges;
    private Double ptMunicipalEduTax;
    private Double ptSpecialConservancyTax;
    private Double ptSpecialEduTax;
    private Double ptStreetTax;
    private Double ptSewerageTax;
    private Double ptSewerageBenefitTax;
    private Double ptWaterBenefitTax;

    // 📊 Summary Fields
    private Double ptFinalRatableValue;
    private Double ptFinalTax;

    // 🔢 Dynamic Tax Fields (pt_tax1_fl → pt_tax25_fl)
    private Double ptTax1;
    private Double ptTax2;
    private Double ptTax3;
    private Double ptTax4;
    private Double ptTax5;
    private Double ptTax6;
    private Double ptTax7;
    private Double ptTax8;
    private Double ptTax9;
    private Double ptTax10;
    private Double ptTax11;
    private Double ptTax12;
    private Double ptTax13;
    private Double ptTax14;
    private Double ptTax15;
    private Double ptTax16;
    private Double ptTax17;
    private Double ptTax18;
    private Double ptTax19;
    private Double ptTax20;
    private Double ptTax21;
    private Double ptTax22;
    private Double ptTax23;
    private Double ptTax24;
    private Double ptTax25;
}
