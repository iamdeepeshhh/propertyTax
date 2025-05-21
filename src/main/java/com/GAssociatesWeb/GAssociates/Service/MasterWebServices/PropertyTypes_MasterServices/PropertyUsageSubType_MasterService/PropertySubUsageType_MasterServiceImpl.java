package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertyUsageSubType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropSubUsageType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropUsageSubType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropUsageType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository.PropUsageSubType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository.PropUsageType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertySubUsageType_MasterServiceImpl implements PropertySubUsageType_MasterService {
    @Autowired
    private PropUsageSubType_MasterRepository propUsageSubType_masterRepository;

    @Autowired
    private PropUsageType_MasterRepository propUsageType_masterRepository;
    @Autowired
    SequenceService sequenceService;

    @Override
    @Transactional
    public PropSubUsageType_MasterDto saveUsageSubType(PropSubUsageType_MasterDto dto) {
        PropUsageType_MasterEntity usageType = propUsageType_masterRepository.findById(dto.getUsageId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid UsageTypeID: " + dto.getUsageId()));

        PropUsageSubType_MasterEntity entity = new PropUsageSubType_MasterEntity();
        entity.setUsageId(usageType.getId()); // Set the entire usageType entity
        entity.setUsageTypeEng(dto.getUsageTypeEng());
        entity.setUsageTypeLocal(dto.getUsageTypeLocal());
        entity.setTaxType(dto.getTaxType());
        // Populate other fields as necessary
        sequenceService.adjustSequence(PropUsageSubType_MasterEntity.class, "propertyusagesub_master_pusm_usagesubid_i_seq");
//        Integer nextId = sequenceService.getNextSequenceValue("propertyusagesub_master_pusm_usagesubid_i_seq");
//        entity.setId(nextId);
        PropUsageSubType_MasterEntity savedEntity = propUsageSubType_masterRepository.save(entity);
        return convertToDto(savedEntity);
    }
    @Override
    public List<PropSubUsageType_MasterDto> findAll() {
        return propUsageSubType_masterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PropSubUsageType_MasterDto> findById(Integer id) {
        return propUsageSubType_masterRepository.findById(id).map(this::convertToDto);
    }
    @Override
    public List<PropSubUsageType_MasterDto> findUsageSubTypesByUsageId(Integer usageId) {
        return propUsageSubType_masterRepository.findByUsageId(usageId).stream()
                .map(this::convertToDto) // Convert each entity to a DTO
                .collect(Collectors.toList());
    }

    private PropSubUsageType_MasterDto convertToDto(PropUsageSubType_MasterEntity entity) {
        PropSubUsageType_MasterDto dto = new PropSubUsageType_MasterDto();
        dto.setId(entity.getId());
        dto.setUsageTypeEng(entity.getUsageTypeEng());
        dto.setUsageTypeLocal(entity.getUsageTypeLocal());
        dto.setUsageId(entity.getUsageId());
        dto.setTaxType(entity.getTaxType());
        // Map other fields as necessary
        return dto;
    }
}
