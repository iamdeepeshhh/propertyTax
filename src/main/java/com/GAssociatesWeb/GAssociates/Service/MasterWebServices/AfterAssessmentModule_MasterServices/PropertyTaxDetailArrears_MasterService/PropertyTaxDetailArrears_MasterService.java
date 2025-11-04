package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.PropertyTaxDetailArrears_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.PropertyTaxDetailArrears_MasterDto.PropertyTaxDetailArrears_MasterDto;

import java.util.List;

public interface PropertyTaxDetailArrears_MasterService {
    PropertyTaxDetailArrears_MasterDto saveArrears(PropertyTaxDetailArrears_MasterDto dto);
    List<PropertyTaxDetailArrears_MasterDto> getAll();
    PropertyTaxDetailArrears_MasterDto getSingleArrearsTaxDetails(String newPropertyNo);
    PropertyTaxDetailArrears_MasterDto getArrearsByPropertyAndYear(String newPropertyNo, String financialYear);
}
