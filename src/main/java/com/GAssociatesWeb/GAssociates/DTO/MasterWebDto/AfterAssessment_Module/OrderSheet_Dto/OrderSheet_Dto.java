package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.OrderSheet_Dto;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_PropertyTaxDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_ProposedRValuesDto;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Data
public class OrderSheet_Dto {
    private Integer wardNo;
    private Integer zoneNo;
    private String surveyNo;
    private String finalPropertyNo;
    private String newPropertyNo;
    private String oldPropertyNo;

    private String ownerName;
    private String applicantName; // alias of respondent if present

    private String noticeNo;
    private String applicationNo;
    private String objectionDate;
    private String applicationReceivedDate;
    private String hearingDate;
    private String hearingTime;
    private String hearingStatus;
    private String reasons;
    private String others;
    private String respondent;

    // Enriched blocks required on Order Sheet
    private List<AfterHearing_PropertyTaxDetailsDto> propertyTaxDetails;
    private List<AfterHearing_ProposedRValuesDto> proposedRValues;

    // Simple key -> value map for taxes (after-hearing) keyed by configured taxKeyL
    private Map<Long, Double> taxKeyValueMap = new HashMap<>();
}
