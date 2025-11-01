package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.HearingNotice_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.RegisterObjection_Dto.RegisterObjection_Dto;

import java.util.List;

public interface HearingNotice_MasterService {

    public List<RegisterObjection_Dto> getHearingNoticesByWard(Integer wardNo);

    }
