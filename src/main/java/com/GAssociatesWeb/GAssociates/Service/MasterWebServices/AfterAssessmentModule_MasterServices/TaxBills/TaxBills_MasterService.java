package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.TaxBills;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;

import java.util.List;

public interface TaxBills_MasterService {
    public List<AssessmentResultsDto> getTaxBillsByWard(int wardNo);
    public List<AssessmentResultsDto> getTaxBillsByNewPropertyNo(String newPropertyNo);
}
