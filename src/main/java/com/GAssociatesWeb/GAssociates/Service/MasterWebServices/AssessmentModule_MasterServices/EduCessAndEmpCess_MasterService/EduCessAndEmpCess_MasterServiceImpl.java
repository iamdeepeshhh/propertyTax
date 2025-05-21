package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.EduCessAndEmpCess_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.EduCessAndEmpCess_MasterDto.EduCessAndEmpCess_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.EduCessAndEmpCess_MasterEntity.EduCessAndEmpCess_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.EduCessAndEmpCess_MasterRepository.EduCessAndEmpCess_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EduCessAndEmpCess_MasterServiceImpl implements EduCessAndEmpCess_MasterService{
    @Autowired
    private EduCessAndEmpCess_MasterRepository cessRateRepository;

    @Override
    public List<EduCessAndEmpCess_MasterDto> getAllCessRates() {
        List<EduCessAndEmpCess_MasterEntity> cessRates = cessRateRepository.findAll();
        return cessRates.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public EduCessAndEmpCess_MasterDto getCessRateById(Long id) {
        EduCessAndEmpCess_MasterEntity cessRate = cessRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CessRate not found with id :" + id));
        return convertToDto(cessRate);
    }

    @Override
    public EduCessAndEmpCess_MasterDto createCessRate(EduCessAndEmpCess_MasterDto cessRateDto) {
        EduCessAndEmpCess_MasterEntity cessRate = convertToEntity(cessRateDto);
        EduCessAndEmpCess_MasterEntity savedCessRate = cessRateRepository.save(cessRate);
        return convertToDto(savedCessRate);
    }

    @Override
    public EduCessAndEmpCess_MasterDto updateCessRate(Long id, EduCessAndEmpCess_MasterDto cessRateDetails) {
        EduCessAndEmpCess_MasterEntity cessRate = cessRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CessRate not found with id :" + id));

        cessRate.setMinTaxableValueFl(cessRateDetails.getMinTaxableValueFl());
        cessRate.setMaxTaxableValueFl(cessRateDetails.getMaxTaxableValueFl());
        cessRate.setResidentialRateFl(cessRateDetails.getResidentialRateFl());
        cessRate.setCommercialRateFl(cessRateDetails.getCommercialRateFl());
        cessRate.setEgcRateFl(cessRateDetails.getEgcRateFl());

        EduCessAndEmpCess_MasterEntity updatedCessRate = cessRateRepository.save(cessRate);
        return convertToDto(updatedCessRate);
    }

    @Override
    public void deleteCessRate(Long id) {
        EduCessAndEmpCess_MasterEntity cessRate = cessRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CessRate not found with id :" + id));
        cessRateRepository.delete(cessRate);
    }

    private EduCessAndEmpCess_MasterDto convertToDto(EduCessAndEmpCess_MasterEntity entity) {
        EduCessAndEmpCess_MasterDto dto = new EduCessAndEmpCess_MasterDto();
        dto.setId(entity.getId());
        dto.setMinTaxableValueFl(entity.getMinTaxableValueFl());
        dto.setMaxTaxableValueFl(entity.getMaxTaxableValueFl());
        dto.setResidentialRateFl(entity.getResidentialRateFl());
        dto.setCommercialRateFl(entity.getCommercialRateFl());
        dto.setEgcRateFl(entity.getEgcRateFl());
        return dto;
    }

    private EduCessAndEmpCess_MasterEntity convertToEntity(EduCessAndEmpCess_MasterDto dto) {
        EduCessAndEmpCess_MasterEntity entity = new EduCessAndEmpCess_MasterEntity();
        entity.setId(dto.getId());
        entity.setMinTaxableValueFl(dto.getMinTaxableValueFl());
        entity.setMaxTaxableValueFl(dto.getMaxTaxableValueFl());
        entity.setResidentialRateFl(dto.getResidentialRateFl());
        entity.setCommercialRateFl(dto.getCommercialRateFl());
        entity.setEgcRateFl(dto.getEgcRateFl());
        return entity;
    }
}
