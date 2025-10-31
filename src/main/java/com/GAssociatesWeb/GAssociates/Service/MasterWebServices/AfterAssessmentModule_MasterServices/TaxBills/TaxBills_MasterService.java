package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.TaxBills;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.TaxBills_MasterDto.TaxBills_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;

import java.util.List;

public interface TaxBills_MasterService {
    public List<TaxBills_MasterDto> getTaxBillsByWard(int wardNo);
    public List<TaxBills_MasterDto> getTaxBillsByNewPropertyNo(String newPropertyNo);
    public TaxBills_MasterDto getSingleTaxBillByNewPropertyNo(String newPropertyNo);
}
