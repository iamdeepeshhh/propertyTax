package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.PropertyRates_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.PropertyRates_MasterDto.PropertyRates_MasterDto;

import java.util.List;
import java.util.Map;

public interface PropertyRates_MasterService {
    List<PropertyRates_MasterDto> getAllPropertyRates();
    PropertyRates_MasterDto getPropertyRateById(Long id);
    List<PropertyRates_MasterDto> addPropertyRate(Map<String, Object> payload);
    PropertyRates_MasterDto updatePropertyRate(Long id, PropertyRates_MasterDto propertyRateDetails);
    void deletePropertyRate(Long id);
}
