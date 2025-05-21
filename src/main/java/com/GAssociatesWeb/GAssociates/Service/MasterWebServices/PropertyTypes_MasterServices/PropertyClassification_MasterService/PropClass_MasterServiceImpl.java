package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertyClassification_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropClassification_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropClassification_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropUsageType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository.PropClassification_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropClass_MasterServiceImpl implements PropClassification_MasterService {

    private final PropClassification_MasterRepository propClassification_masterRepository;

    @Autowired
    private final SequenceService sequenceService;
    @Autowired
    public PropClass_MasterServiceImpl(PropClassification_MasterRepository repository, SequenceService sequenceService) {
        this.propClassification_masterRepository = repository;
        this.sequenceService = sequenceService;
    }

    @Override
    public PropClassification_MasterDto save(PropClassification_MasterDto dto) {
        PropClassification_MasterEntity entity = new PropClassification_MasterEntity();
        // Manually map fields from DTO to Entity
        entity.setPcmProptypeengVc(dto.getPropertyTypeName());
        entity.setPcmProptypellVc(dto.getLocalPropertyTypeName());
        entity.setPcmBuildingVc(dto.getBuildingInp());
        sequenceService.adjustSequence(PropClassification_MasterEntity.class, "propertyclassification_master_pcm_classidi_seq");

        PropClassification_MasterEntity savedEntity = propClassification_masterRepository.save(entity);

        // Convert the saved entity back to DTO
        return convertToDto(savedEntity);
    }

    @Override
    public List<PropClassification_MasterDto> findAll() {
        return propClassification_masterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PropClassification_MasterDto> findById(Integer id) {
        return Optional.empty();
    }

    public Optional<PropClassification_MasterDto> findByPcmClassidI(Integer pcmClassidI) {
        Optional<PropClassification_MasterEntity> entity = propClassification_masterRepository.findById(pcmClassidI);
        return entity.map(this::convertToDto);
    }

    // Helper method to convert Entity to DTO
    private PropClassification_MasterDto convertToDto(PropClassification_MasterEntity entity) {
        PropClassification_MasterDto dto = new PropClassification_MasterDto();
        dto.setPropertyTypeName(entity.getPcmProptypeengVc());
        dto.setLocalPropertyTypeName(entity.getPcmProptypellVc());
        dto.setBuildingInp(entity.getPcmBuildingVc());
        dto.setPcmId(entity.getPcmClassidI());
        // Map other fields as necessary
        return dto;
    }
}
