package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.EduCessAndEmpCess_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.EduCessAndEmpCess_MasterDto.EduCessAndEmpCess_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.EduCessAndEmpCess_MasterEntity.EduCessAndEmpCess_MasterEntity;

import java.util.List;

public interface EduCessAndEmpCess_MasterService {
    List<EduCessAndEmpCess_MasterDto> getAllCessRates();
    EduCessAndEmpCess_MasterDto getCessRateById(Long id);
    EduCessAndEmpCess_MasterDto createCessRate(EduCessAndEmpCess_MasterDto cessRateDto);
    EduCessAndEmpCess_MasterDto updateCessRate(Long id, EduCessAndEmpCess_MasterDto cessRateDetails);
    void deleteCessRate(Long id);
}
