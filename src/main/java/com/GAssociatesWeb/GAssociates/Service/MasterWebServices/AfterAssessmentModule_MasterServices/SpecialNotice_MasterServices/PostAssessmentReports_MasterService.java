package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.SpecialNotice_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;

import java.util.List;

public interface PostAssessmentReports_MasterService {
   // List<AssessmentResultsDto> getAllResults();

    List<AssessmentResultsDto> getSpecialNoticesByWard(int wardNo);
    public long getSpecialNoticeCount(Integer wardNo, String newPropertyNo);
    public List<AssessmentResultsDto> getSpecialNoticesByNewPropertyNo(String newPropertyNo);
    public List<AssessmentResultsDto> getObjectionReceiptByNewPropertyNo();

}
