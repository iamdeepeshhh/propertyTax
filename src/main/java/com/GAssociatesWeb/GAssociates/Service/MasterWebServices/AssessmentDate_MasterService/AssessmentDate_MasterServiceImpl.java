package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentDate_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentDate_MasterDto.AssessmentDate_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentDate_MasterEntity.AssessmentDate_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentDate_MasterRepository.AssessmentDate_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssessmentDate_MasterServiceImpl implements AssessmentDate_MasterService{

    private final AssessmentDate_MasterRepository assessmentDate_masterRepository;

    @Autowired
    public AssessmentDate_MasterServiceImpl(AssessmentDate_MasterRepository assessmentDateRepository) {
        this.assessmentDate_masterRepository = assessmentDateRepository;
    }

    @Override
    public AssessmentDate_MasterDto saveAssessmentDate(AssessmentDate_MasterDto assessmentDateDTO) {
        System.out.println(assessmentDateDTO);
        AssessmentDate_MasterEntity assessmentDateMasterEntity = mapToEntity(assessmentDateDTO);
        assessmentDateMasterEntity = assessmentDate_masterRepository.save(assessmentDateMasterEntity);
        return mapToDTO(assessmentDateMasterEntity);
    }

    @Override
    public List<AssessmentDate_MasterDto> getAllAssessmentDates() {
        return assessmentDate_masterRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AssessmentDate_MasterDto getAssessmentDateById(Integer id) {
        return assessmentDate_masterRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    public void deleteAssessmentDate(Integer id) {
        assessmentDate_masterRepository.deleteById(id);
    }

    private AssessmentDate_MasterDto mapToDTO(AssessmentDate_MasterEntity assessmentDate) {
        AssessmentDate_MasterDto dto = new AssessmentDate_MasterDto();
        // Map entity fields to DTO fields
        dto.setAssessmentId(assessmentDate.getAssessmentId());
        dto.setFirstAssessmentDate(assessmentDate.getFirstAssessmentDate());
        dto.setLastAssessmentDate(assessmentDate.getLastAssessmentDate());
        dto.setCurrentAssessmentDate(assessmentDate.getCurrentAssessmentDate());
        return dto;
    }

    private AssessmentDate_MasterEntity mapToEntity(AssessmentDate_MasterDto dto) {
        AssessmentDate_MasterEntity assessmentDate = new AssessmentDate_MasterEntity();
        // Map DTO fields to entity fields
        assessmentDate.setAssessmentId(dto.getAssessmentId());
        assessmentDate.setFirstAssessmentDate(dto.getFirstAssessmentDate());
        assessmentDate.setLastAssessmentDate(dto.getLastAssessmentDate());
        assessmentDate.setCurrentAssessmentDate(dto.getCurrentAssessmentDate());
        return assessmentDate;
    }
}
