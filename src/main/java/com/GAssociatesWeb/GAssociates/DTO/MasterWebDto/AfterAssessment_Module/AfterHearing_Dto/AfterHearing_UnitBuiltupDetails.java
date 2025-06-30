package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_UnitBuiltupDetails {
    private Integer id;
    private String newPropertyNo;
    private String floorNo;
    private Integer unitNo;
    private Integer builtupId;
    private String roomType;
    private String length;
    private String breadth;
    private String exemptionStatus;
    private String exemptionLength;
    private String exemptionBreadth;
    private String exemptionArea;
    private String carpetArea;
    private String assessableArea;
    private LocalDate timestamp;
    private String legalStatus;
    private String legalArea;
    private String illegalArea;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String unitRemark;
    private String measureType;
    private String originalAssessArea;
    private String deductionPercent;
    private String areaBeforeDeduction;
    private String plotArea;
}
