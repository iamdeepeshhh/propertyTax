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
        // Enforce a single-record policy: upsert into the first/only record.
        List<AssessmentDate_MasterEntity> all = assessmentDate_masterRepository.findAll();

        AssessmentDate_MasterEntity entity;
        if (!all.isEmpty()) {
            // Update the existing record (use the first one)
            entity = all.get(0);
        } else {
            // Create a new record
            entity = new AssessmentDate_MasterEntity();
        }

        // Map incoming values
        entity.setFirstAssessmentDate(assessmentDateDTO.getFirstAssessmentDate());
        entity.setLastAssessmentDate(assessmentDateDTO.getLastAssessmentDate());
        entity.setCurrentAssessmentDate(assessmentDateDTO.getCurrentAssessmentDate());

        // Save the single authoritative record
        AssessmentDate_MasterEntity saved = assessmentDate_masterRepository.save(entity);

        // If more than one record exists, prune extras
        if (all.size() > 1) {
            for (int i = 1; i < all.size(); i++) {
                AssessmentDate_MasterEntity extra = all.get(i);
                try { assessmentDate_masterRepository.deleteById(extra.getAssessmentId()); } catch (Exception ignored) {}
            }
        }

        return mapToDTO(saved);
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
