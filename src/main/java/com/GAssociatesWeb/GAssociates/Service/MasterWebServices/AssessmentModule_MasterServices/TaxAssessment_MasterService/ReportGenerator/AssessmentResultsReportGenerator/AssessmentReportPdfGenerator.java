package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.ReportGenerator.AssessmentResultsReportGenerator;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;

import java.util.List;

public interface AssessmentReportPdfGenerator {
    public byte[] generateCombinedWarningsPdf(String wardNo, List<AssessmentResultsDto> assessmentResultsDtos);
}
