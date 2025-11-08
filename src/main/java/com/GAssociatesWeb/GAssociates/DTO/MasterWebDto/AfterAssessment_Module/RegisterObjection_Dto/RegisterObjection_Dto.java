package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.RegisterObjection_Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data                    // Generates getters, setters, equals, hashCode, toString
@NoArgsConstructor       // Generates no-argument constructor
@AllArgsConstructor      // Generates all-arguments constructor
public class RegisterObjection_Dto {

    private Integer id;

    private Integer wardNo;
    private Integer zoneNo;
    private String surveyNo;
    private String finalPropertyNo;
    private String newPropertyNo;
    private String ownerName;
    private String reasons;
    private String others;
    private String respondent;
    private String objectionDate;
    private String oldPropertyNo;
    private String userDate;
    private String userTime;
    private String noticeNo;
    private String hearingDate;
    private String hearingTime;
    private String hearingStatus;
    private String applicationNo;
    private String phoneNo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String applicationReceivedDate;
    private String changedValue;
    private Double totalTax;
    private Double totalRValue;

}

