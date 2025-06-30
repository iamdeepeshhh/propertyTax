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
public class AfterHearing_UnitDetails {

    private Integer id;
    private String newPropertyNo;
    private String floorNo;
    private Integer unitNo;
    private Integer occupantStatus;
    private String rentalNo;
    private String occupierName;
    private String agreementCopy;
    private String mobileNo;
    private String emailId;
    private Integer usageType;
    private Integer usageSubType;
    private Integer constructionAge;
    private String constructionClass;
    private String ageFactor;
    private String carpetArea;
    private String exemptedArea;
    private String assessmentArea;
    private String signature;
    private LocalDate timestamp;
    private String totalLegalArea;
    private String totalIllegalArea;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String unitRemark;
    private String constructionYear;
    private String establishmentYear;
    private String originalAgeFactor;
    private String areaBeforeDeduction;

}

