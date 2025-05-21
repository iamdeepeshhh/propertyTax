package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxDepreciation_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxDepreciation_MasterDto.TaxDepreciation_MasterDto;

import java.util.List;
import java.util.Map;

public interface TaxDepreciation_MasterService {
    public List<TaxDepreciation_MasterDto> addDepreciationRates(Map<String, Object> payload);
    List<TaxDepreciation_MasterDto> getAllDepreciationRates();
    void deleteDepreciationRate(Long id);
}