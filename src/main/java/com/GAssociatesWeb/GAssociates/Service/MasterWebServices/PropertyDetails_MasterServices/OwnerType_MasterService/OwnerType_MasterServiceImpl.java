package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.OwnerType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.OwnerType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity.OwnerType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyDetails_MasterRepository.OwnerType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceConstants;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerType_MasterServiceImpl implements OwnerType_MasterService {

    @Autowired
    private OwnerType_MasterRepository ownerTypeMasterRepository;
    @Autowired
    private final SequenceService sequenceService;

    public OwnerType_MasterServiceImpl(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Override
    public OwnerType_MasterDto addOwnerType(OwnerType_MasterDto ownerTypeMasterDto) throws Exception {
        OwnerType_MasterEntity ownerTypeMasterEntity = new OwnerType_MasterEntity();
        ownerTypeMasterEntity.setOwnerType(ownerTypeMasterDto.getOwnerType());
        ownerTypeMasterEntity.setOwnerTypeMarathi(ownerTypeMasterDto.getOwnerTypeMarathi());

        // Sequence handling
        sequenceService.resetSequenceIfTableIsEmpty("ownertype_master", "ownertype_master_ownertype_id_seq");
        if (ownerTypeMasterEntity.getOwnertypeId() == null) {
            long minvalue = SequenceConstants.MIN_VALUE;
            sequenceService.ensureSequenceExists("ownertype_master_ownertype_id_seq", "ownertype_master.ownertypeId", minvalue);
            Integer nextId = sequenceService.getNextSequenceValue("ownertype_master_ownertype_id_seq");
            ownerTypeMasterEntity.setOwnertypeId(nextId);
        }

        OwnerType_MasterEntity savedEntity = ownerTypeMasterRepository.save(ownerTypeMasterEntity);
        OwnerType_MasterDto savedDto = new OwnerType_MasterDto();
        savedDto.setOwnertypeId(savedEntity.getOwnertypeId());
        savedDto.setOwnerType(savedEntity.getOwnerType());
        savedDto.setOwnerTypeMarathi(savedEntity.getOwnerTypeMarathi());
        return savedDto;
    }

    @Override
    public List<OwnerType_MasterDto> findAllOwnerTypes() {
        List<OwnerType_MasterEntity> entities = ownerTypeMasterRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public OwnerType_MasterDto convertToDto(OwnerType_MasterEntity entity) {
        OwnerType_MasterDto dto = new OwnerType_MasterDto();
        dto.setOwnertypeId(entity.getOwnertypeId());
        dto.setOwnerType(entity.getOwnerType());
        dto.setOwnerTypeMarathi(entity.getOwnerTypeMarathi());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
