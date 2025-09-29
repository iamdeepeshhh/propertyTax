package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.ReportGenerator.CalculationSheet;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;

import java.util.List;

public interface CalculationSheetGenerator_MasterService {
    public List<AssessmentResultsDto> generatePropertyCalculationReport(Integer wardNo);
    public List<AssessmentResultsDto> generatePropertyCalculationReport(Integer wardNo, int page, int size);
    public long countPropertiesForCalculationReport(Integer wardNo);
    public List<AssessmentResultsDto> generateSinglePropertyReport(String newPropertyNo);
}
