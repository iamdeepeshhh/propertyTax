package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.UnitBuiltUp_Service;


import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitBuiltUp_Dto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitDetails_Entity.UnitDetails_Entity;

import java.util.List;

public interface UnitBuiltUp_Service {
    UnitBuiltUp_Dto createBuiltUp(UnitBuiltUp_Dto builtUpDto);
    //List<UnitBuiltUp_Dto> findAllBuiltUpsByUnit(String pdNewpropertynoVc, String udUnitNoVc);
    public UnitBuiltUp_Dto updateOrCreateUnitBuiltUp(UnitBuiltUp_Dto dto);
    void deleteBuiltUp(String pdNewpropertynoVc);
    public List<UnitBuiltUp_Dto> getBuiltUpsByUnitDetails(String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc);
}
