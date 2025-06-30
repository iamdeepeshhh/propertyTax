package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_PropertyRValues {

    private Integer id;
    private String propertyNo;
    private String finalPropertyNo;
    private String unitNo;
    private Double rate;
    private Double lettingValue;
    private Integer depreciationPercentage;
    private Double depreciationAmount;
    private Double annualLettingValue;
    private Double mainValue;
    private Double taxValue;
    private String tenantName;
    private Double rent;
    private Double yearlyRent;
    private Double tenantMainValue;
    private Double tenantValue;
    private Double ratableValue;
    private String financialYear;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String statusDummy;

}
