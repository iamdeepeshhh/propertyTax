package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.ConolidatedTaxes_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.ConsolidatedTaxes_MasterDto.ConsolidatedTaxes_MasterDto;

import java.util.List;

public interface ConsolidatedTaxes_MasterService {
    List<ConsolidatedTaxes_MasterDto> getAllTaxes();
    ConsolidatedTaxes_MasterDto getTaxById(Long id);
    ConsolidatedTaxes_MasterDto createTax(ConsolidatedTaxes_MasterDto taxDto);
    ConsolidatedTaxes_MasterDto updateTax(Long id, ConsolidatedTaxes_MasterDto taxDto);
    void deleteTax(Long id);
}
