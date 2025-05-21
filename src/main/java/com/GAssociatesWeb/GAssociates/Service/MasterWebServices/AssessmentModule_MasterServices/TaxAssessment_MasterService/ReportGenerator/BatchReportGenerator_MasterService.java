package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.ReportGenerator;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;

import java.io.IOException;
import java.util.List;

public interface BatchReportGenerator_MasterService {
    public List<AssessmentResultsDto> generatePropertyReport(Integer wardNo);
}
