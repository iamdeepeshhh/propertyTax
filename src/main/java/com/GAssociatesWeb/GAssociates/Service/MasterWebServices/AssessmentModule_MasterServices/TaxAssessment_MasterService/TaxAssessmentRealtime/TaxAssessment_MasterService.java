package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentRealtime;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;

import java.util.Map;

public interface TaxAssessment_MasterService {
    public AssessmentResultsDto  performAssessment(String newPropertyNumber, boolean fromAfterHearing);
    //public AssessmentResultsDto dtoConversion(Map<String, Object> assessmentData);
}
