package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.SecondaryBatchAssessmentReport;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearingCompleteProperty_Dto;

import java.util.List;

public interface SecondaryBatchAssessmentReport_MasterService {
    public List<AfterHearingCompleteProperty_Dto> generateCombinedAfterHearingReport(Integer wardNo);
    }
