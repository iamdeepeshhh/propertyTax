package com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompleteProperty_Dto {
    private PropertyDetails_Dto propertyDetails;
    private List<UnitDetails_Dto> unitDetails;
    private List<AdditionalInfo_Dto> additionalInfo;
    private String Error;
}
