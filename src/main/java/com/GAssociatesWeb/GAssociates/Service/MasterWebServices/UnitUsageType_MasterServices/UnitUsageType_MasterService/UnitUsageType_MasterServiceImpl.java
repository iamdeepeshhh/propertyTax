package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitUsageType_MasterServices.UnitUsageType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitUsageTypes_MasterDto.UnitUsageType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropClassification_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropUsageType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity.UnitUsageType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository.PropUsageType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitUsageTypes_MasterRepository.UnitUsageType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UnitUsageType_MasterServiceImpl implements UnitUsageType_MasterService{

    @Autowired
    private UnitUsageType_MasterRepository unitUsageType_masterRepository;

    @Autowired
    private PropUsageType_MasterRepository propUsageType_masterRepository;
    @Autowired
    private SequenceService sequenceService;


    @Override
    public UnitUsageType_MasterDto saveUnitUsageMaster(UnitUsageType_MasterDto dto) {

        PropUsageType_MasterEntity propUsageType = propUsageType_masterRepository.findById(dto.getPum_usageid_i())
                .orElseThrow(() -> new IllegalArgumentException("PropUsageType with ID " + dto.getPum_usageid_i() + " does not exist"));
        UnitUsageType_MasterEntity entity = new UnitUsageType_MasterEntity();
        entity.setPropUsageTypeMaster(propUsageType);
        entity.setUum_usagetypeeng_vc(dto.getUum_usagetypeeng_vc());
        entity.setUum_usagetypell_vc(dto.getUum_usagetypell_vc());
        entity.setUum_created_dt(dto.getUum_created_dt());
        entity.setUum_updated_dt(dto.getUum_updated_dt());
        entity.setUum_abbr_vc(dto.getUum_abbr_vc());
        entity.setUum_type_vc(dto.getUum_type_vc());
        entity.setUum_rvtype_vc(dto.getUum_rvtype_vc());
        sequenceService.adjustSequence(UnitUsageType_MasterEntity.class, "unitusage_master_uum_usageid_i_seq");
        entity = unitUsageType_masterRepository.save(entity);

        // Convert the saved entity back to DTO to return
        return convertToDto(entity);
    }

    @Override
    public List<UnitUsageType_MasterDto> findAllByPropUsageId(Integer propUsageId) {
        List<UnitUsageType_MasterEntity> entities = unitUsageType_masterRepository.findByPropUsageTypeMaster_Id(propUsageId);
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UnitUsageType_MasterDto> findById(Integer id) {
        return unitUsageType_masterRepository.findById(id).map(this::convertToDto);
    }
    @Override
    public List<UnitUsageType_MasterDto> getAllUnitUsages() {
        return unitUsageType_masterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public UnitUsageType_MasterDto updateUnitUsageMaster(UnitUsageType_MasterDto dto, Integer id) {
        UnitUsageType_MasterEntity entity = unitUsageType_masterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UnitUsageMaster not found with id: " + id));
        updateEntityFromDto(entity, dto);
        UnitUsageType_MasterEntity updatedEntity = unitUsageType_masterRepository.save(entity);
        return convertToDto(updatedEntity);
    }

    @Override
    public void deleteUnitUsageMaster(Integer id) {
        unitUsageType_masterRepository.deleteById(id);
    }

    @Override
    public UnitUsageType_MasterDto getUnitUsageMasterById(Integer id) {
        UnitUsageType_MasterEntity entity = unitUsageType_masterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UnitUsageMaster not found for ID: " + id));
        return convertToDto(entity);
    }

    private UnitUsageType_MasterDto convertToDto(UnitUsageType_MasterEntity entity) {
        if (entity == null) {
            return null;
        }

        UnitUsageType_MasterDto dto = new UnitUsageType_MasterDto();
        dto.setUum_usageid_i(entity.getUum_usageid_i());
        dto.setUum_usagetypeeng_vc(entity.getUum_usagetypeeng_vc());
        dto.setUum_usagetypell_vc(entity.getUum_usagetypell_vc());
        // Handling the relationship, ensure to check for null to avoid NullPointerException
        if (entity.getPropUsageTypeMaster() != null) {
            dto.setPum_usageid_i(entity.getPropUsageTypeMaster().getId());
        }
        dto.setUum_created_dt(entity.getUum_created_dt());
        dto.setUum_updated_dt(entity.getUum_updated_dt());
        dto.setUum_abbr_vc(entity.getUum_abbr_vc());
        dto.setUum_type_vc(entity.getUum_type_vc());
        dto.setUum_rvtype_vc(entity.getUum_rvtype_vc());

        return dto;
    }
    private void updateEntityFromDto(UnitUsageType_MasterEntity entity, UnitUsageType_MasterDto dto) {
        if (dto.getUum_usagetypeeng_vc() != null) {
            entity.setUum_usagetypeeng_vc(dto.getUum_usagetypeeng_vc());
        }
        if (dto.getUum_usagetypell_vc() != null) {
            entity.setUum_usagetypell_vc(dto.getUum_usagetypell_vc());
        }
        if (dto.getUum_abbr_vc() != null) {
            entity.setUum_abbr_vc(dto.getUum_abbr_vc());
        }
        if (dto.getUum_type_vc() != null) {
            entity.setUum_type_vc(dto.getUum_type_vc());
        }
        if (dto.getUum_rvtype_vc() != null) {
            entity.setUum_rvtype_vc(dto.getUum_rvtype_vc());
        }
        if (dto.getUum_created_dt() != null) {
            entity.setUum_created_dt(dto.getUum_created_dt());
        }
        if (dto.getUum_updated_dt() != null) {
            entity.setUum_updated_dt(dto.getUum_updated_dt());
        }
        if (dto.getPum_usageid_i() != null) {
            PropUsageType_MasterEntity propUsageTypeMaster = propUsageType_masterRepository.findById(dto.getPum_usageid_i())
                    .orElseThrow(() -> new EntityNotFoundException("PropUsageTypeMaster not found for ID: " + dto.getPum_usageid_i()));
            entity.setPropUsageTypeMaster(propUsageTypeMaster);
        }
    }
}
