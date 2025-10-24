package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingUnitBuiltupDetails_Service;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_UnitBuiltupDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitBuiltUp_Dto;

import java.util.List;

public interface AfterHearingUnitBuiltupDetails_Service {
    public UnitBuiltUp_Dto createBuiltUp(UnitBuiltUp_Dto builtUpDto);
    public UnitBuiltUp_Dto updateOrCreateUnitBuiltUp(UnitBuiltUp_Dto dto);
    public void deleteBuiltUp(String pdNewpropertynoVc);
    public List<UnitBuiltUp_Dto> findAllBuiltUpsByUnit(String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc);
}
