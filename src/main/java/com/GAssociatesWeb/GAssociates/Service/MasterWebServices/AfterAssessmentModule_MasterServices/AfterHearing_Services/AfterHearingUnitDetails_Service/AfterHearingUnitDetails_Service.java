package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingUnitDetails_Service;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_PropertyDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_UnitDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;

import java.util.List;

public interface AfterHearingUnitDetails_Service {
    public UnitDetails_Dto createUnit(UnitDetails_Dto unitDetailsDto);
    public void deleteUnit(String pdNewpropertynoVc);
    public List<UnitDetails_Dto> getAllUnitsByProperty(String pdNewpropertynoVc);
}
