package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertyUsageType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropUsageType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropSubclassification_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropUsageSubType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropUsageType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository.PropSubclassification_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository.PropUsageType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyUsageType_MasterServiceImpl implements PropertyUsageType_MasterService {
    @Autowired
    private PropSubclassification_MasterRepository propSubclassification_masterRepository;

    @Autowired
    private PropUsageType_MasterRepository propUsageType_masterRepository;
    @Autowired
    private SequenceService sequenceService;

    @Override
    @Transactional
    public PropUsageType_MasterDto saveUsageType(PropUsageType_MasterDto dto) {
        PropSubclassification_MasterEntity subclassification = propSubclassification_masterRepository.findById(dto.getPropertySubTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid propertySubTypeId: " + dto.getPropertySubTypeId()));

        PropUsageType_MasterEntity entity = new PropUsageType_MasterEntity();
        entity.setPropertySubclassificationMaster(subclassification);
        entity.setUsageTypeEng(dto.getUsageTypeName());
        entity.setUsageTypeLl(dto.getLocalUsagetypeName());
        sequenceService.adjustSequence(PropUsageType_MasterEntity.class, "propertyusage_master_pum_usageid_i_seq");
        PropUsageType_MasterEntity savedEntity = propUsageType_masterRepository.save(entity);
        return convertToDto(savedEntity);
    }

    @Override
    public Optional<PropUsageType_MasterDto> findById(Integer id) {
        return propUsageType_masterRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<PropUsageType_MasterDto> findAll() {
        return propUsageType_masterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PropUsageType_MasterDto> findUsageTypesBySubtypeId(Integer subtypeId) {
        return propUsageType_masterRepository.findByPropertySubclassificationMaster_psmSubclassidI(subtypeId).stream()
                .map(this::convertToDto) // Assuming you implement this conversion method
                .collect(Collectors.toList());
    }


    private PropUsageType_MasterDto convertToDto(PropUsageType_MasterEntity entity) {
        PropUsageType_MasterDto dto = new PropUsageType_MasterDto();
        dto.setPropertyUsageTypeId(entity.getId());
        dto.setPropertySubTypeId(entity.getPropertySubclassificationMaster().getPsmSubclassidI());
        dto.setUsageTypeName(entity.getUsageTypeEng());
        dto.setLocalUsagetypeName(entity.getUsageTypeLl());
        // Add other fields mapping as necessary
        return dto;
    }


}
