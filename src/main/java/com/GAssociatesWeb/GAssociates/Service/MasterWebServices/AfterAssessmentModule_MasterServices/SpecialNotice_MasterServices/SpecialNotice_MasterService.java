package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.SpecialNotice_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;

import java.util.List;

public interface SpecialNoticeService {
   // List<AssessmentResultsDto> getAllResults();

    List<AssessmentResultsDto> getSpecialNoticesByWard(int wardNo);
}
