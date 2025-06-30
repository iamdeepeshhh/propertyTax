package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_ProposedRValues {
    private Integer id;
    private String propertyNo;
    private String finalPropertyNo;
    private Double rvResidential;
    private Double rvMobile;
    private Double rvElectric;
    private Double rvCommercial;
    private Double rvGovernment;
    private Double rvEducation;
    private Double rvReligious;
    private Double rvCommercialOpen;
    private Double rvResidentialOpen;
    private Double rvIndustrial;
    private Double finalRatableValue;
    private String finalYearDate;
    private Double areaLetOut;
    private Double areaNotLetOut;
    private String financialYear;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double alvLetOut;
    private Double alvNotLetOut;
    private Double rvReligiousOpen;
    private Double rvGovernmentOpen;
    private Double rvEducationOpen;
    private Double rvIndustrialOpen;
    private Integer numberOfCollectedValues;
}
