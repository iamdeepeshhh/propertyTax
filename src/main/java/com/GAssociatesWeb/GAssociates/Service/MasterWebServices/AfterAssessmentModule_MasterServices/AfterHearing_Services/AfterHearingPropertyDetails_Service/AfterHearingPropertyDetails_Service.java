package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingPropertyDetails_Service;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_PropertyDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;

public interface AfterHearingPropertyDetails_Service {
    public PropertyDetails_Dto createAfterHearingProperty(PropertyDetails_Dto dto);
    public PropertyDetails_Dto getPropertyByNewPropertyNo(String newPropertyNo);
    public PropertyDetails_Dto updateAfterHearingProperty(PropertyDetails_Dto dto);
    public void deleteAfterHearingProperty(String newPropertyNo);
}
