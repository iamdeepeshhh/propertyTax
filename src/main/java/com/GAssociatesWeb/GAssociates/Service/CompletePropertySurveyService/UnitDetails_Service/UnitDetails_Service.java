package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.UnitDetails_Service;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;

import java.util.List;

public interface UnitDetails_Service {
    UnitDetails_Dto createUnit(UnitDetails_Dto unitDetailsDto);
    List<UnitDetails_Dto> getAllUnitsByProperty(String pdNewpropertynoVc);
    public UnitDetails_Dto updateOrCreateUnit(UnitDetails_Dto dto, PropertyDetails_Dto property);
    void deleteUnit(String unitId);
}
