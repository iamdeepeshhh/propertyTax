package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitBuiltUp_Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_UnitDetailsDto {
    private String pdNewpropertynoVc;
    private String udFloorNoVc;
    private Integer udUnitNoVc;
    private Integer udOccupantStatusI;
    private String udRentalNoVc;
    private String udAreaBefDedF;
    private String udOccupierNameVc;
    private String udAgreementCopyVc; // Text field for agreement copy
    private String udMobileNoVc;
    private String udEmailIdVc;
    private Integer udUsageTypeI;
    private Integer udUsageSubtypeI;
    private Integer udConstAgeI;
    private String udConstructionClassI;
    private String udAgeFactorI;
    private String udCarpetAreaF;
    private String udExemptedAreaF;
    private String udAssessmentAreaF;
    private String udSignatureVc; // Text field for signature
    private java.sql.Date udTimestampDt;
    private String udTotLegAreaF;
    private String udTotIllegAreaF;
    private String udUnitRemarkVc;
    private String udConstYearDt;
    private String udEstablishmentNameVc;
    // Additional fields from your structure
    private String udTenantNoI;
    private String udAgeFactVc;
    private String udPlotAreaFl;
    private String udAssVc;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
    private Long id;

    private List<AfterHearing_UnitBuiltupDetailsDto> unitBuiltupUps;          // Area Before Deduction
}