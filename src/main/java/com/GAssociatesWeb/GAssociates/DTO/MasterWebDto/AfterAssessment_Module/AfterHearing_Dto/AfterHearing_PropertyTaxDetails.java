package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_PropertyTaxDetails {

    private Integer id;
    private String newPropertyNo;
    private String finalPropertyNo;
    private Double propertyTax;
    private Double egcTax;
    private Double treeTax;
    private Double environmentTax;
    private Double lightTax;
    private Double cleanTax;
    private Double fireTax;
    private Double userCharges;
    private Double educationResidentialTax;
    private Double educationNonResidentialTax;
    private Double totalEducationTax;
    private Double finalTax;
    private String finalYear;
    private Double finalRatableValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime updated_at;

}
