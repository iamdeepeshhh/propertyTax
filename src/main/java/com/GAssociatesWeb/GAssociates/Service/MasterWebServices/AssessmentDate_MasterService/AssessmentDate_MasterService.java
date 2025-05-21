package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentDate_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentDate_MasterDto.AssessmentDate_MasterDto;

import java.util.List;

public interface AssessmentDate_MasterService {
    AssessmentDate_MasterDto saveAssessmentDate(AssessmentDate_MasterDto assessmentDate);
    List<AssessmentDate_MasterDto> getAllAssessmentDates();
    AssessmentDate_MasterDto getAssessmentDateById(Integer id);
    void deleteAssessmentDate(Integer id);
}
