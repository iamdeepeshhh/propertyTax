package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.PropertyUnitDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearingCompleteProperty_Dto {
    private PropertyDetails_Dto propertyDetails;
    private List<UnitDetails_Dto> unitDetails;


    private List<AfterHearing_PropertyTaxDetailsDto> propertyTaxDetails;
    private Map<Long, Double> taxKeyValueMapAfterAssess = new HashMap<>();
    private Map<Long, Double> taxKeyValueMapAfterHearing = new HashMap<>();

    //for assessment register
    private List<PropertyUnitDetailsDto> propertyUnitDetails;
    private List<AfterHearing_PropertyRValuesDto> propertyRValues;
    private List<AfterHearing_ProposedRValuesDto> proposedRValues;
}