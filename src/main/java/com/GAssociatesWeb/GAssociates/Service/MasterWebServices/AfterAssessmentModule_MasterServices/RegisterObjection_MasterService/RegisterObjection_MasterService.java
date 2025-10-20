package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.RegisterObjection_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.RegisterObjection_Dto.RegisterObjection_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.RegisterObjection_Entity.RegisterObjection_Entity;

import java.util.List;

public interface RegisterObjection_MasterService {


    public boolean existsObjection(String newPropertyNo);
    public void saveObjection(RegisterObjection_Dto dto);

    public RegisterObjection_Dto getObjection(String applicationNo);

    public List<RegisterObjection_Dto> getList();
}
