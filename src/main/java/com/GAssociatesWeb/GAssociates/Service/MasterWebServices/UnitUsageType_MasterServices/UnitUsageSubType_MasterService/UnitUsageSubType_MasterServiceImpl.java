package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitUsageType_MasterServices.UnitUsageSubType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitUsageTypes_MasterDto.UnitUsageSubType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity.UnitUsageSubType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity.UnitUsageType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitUsageTypes_MasterRepository.UnitUsageSubType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitUsageTypes_MasterRepository.UnitUsageType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UnitUsageSubType_MasterServiceImpl implements UnitUsageSubType_MasterService {

    @Autowired
    private UnitUsageSubType_MasterRepository unitUsageSubType_masterRepository;

    @Autowired
    private UnitUsageType_MasterRepository unitUsageType_masterRepository;
    @Autowired
    private SequenceService sequenceService;

    @Override
    public UnitUsageSubType_MasterDto saveUnitUsageSubTypeMaster(UnitUsageSubType_MasterDto dto) {
        UnitUsageType_MasterEntity unitUsageType = unitUsageType_masterRepository.findById(dto.getUum_usageclassid_i())
                .orElseThrow(() -> new IllegalArgumentException("UnitUsageType with ID " + dto.getUum_usageclassid_i() + " does not exist"));

        UnitUsageSubType_MasterEntity entity = new UnitUsageSubType_MasterEntity();
        entity.setUnitUsageMaster(unitUsageType);
        entity.setUsm_usagetypeeng_vc(dto.getUsm_usagetypeeng_vc());
        entity.setUsm_usagetypell_vc(dto.getUsm_usagetypell_vc());
//        entity.setUsm_taxtype_i(dto.getUsm_taxtype_i());
        entity.setUsm_usercharges_i(dto.getUsm_usercharges_i());
        entity.setUsmApplyDifferentRateVc(dto.getUsmApplyDifferentRateVc());
        if (dto.getUsmApplyDifferentRateVc()){
            entity.setUsm_rvtype_vc(dto.getUsm_rvtype_vc());
        } else {
            Optional<UnitUsageType_MasterEntity> unitUsageTypeMasterEntity = unitUsageType_masterRepository.findById(dto.getUum_usageclassid_i());
            entity.setUsm_rvtype_vc(unitUsageTypeMasterEntity.get().getUum_rvtype_vc());
        }
//        entity.setUsm_taxamt_i(dto.getUsm_taxamt_i());
        entity.setUsm_created_dt(LocalDateTime.now());
        // usm_updated_dt is not set here since it's a creation operation

        sequenceService.adjustSequence(UnitUsageSubType_MasterEntity.class, "unitusagesub_master_usm_usagesubid_i_seq");
        entity = unitUsageSubType_masterRepository.save(entity);
        return convertToDto(entity);
    }


    @Override
    public List<UnitUsageSubType_MasterDto> findAllUnitUsageSubTypeMasters() {
        List<UnitUsageSubType_MasterEntity> entities = unitUsageSubType_masterRepository.findAll();
        return entities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<UnitUsageSubType_MasterDto> findByUnitUsageId(Integer unitUsageId) {
        List<UnitUsageSubType_MasterEntity> entities = unitUsageSubType_masterRepository.findByUnitUsageMaster_Uum_usageid_i(unitUsageId);
        return entities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<UnitUsageSubType_MasterDto> findById(Integer id) {
        return unitUsageSubType_masterRepository.findById(id).map(this::convertToDto);
    }
    private UnitUsageSubType_MasterDto convertToDto(UnitUsageSubType_MasterEntity entity) {
        UnitUsageSubType_MasterDto dto = new UnitUsageSubType_MasterDto();
        dto.setUsm_usagesubid_i(entity.getUsm_usagesubid_i());
        dto.setUum_usageclassid_i(entity.getUnitUsageMaster().getUum_usageid_i());
        dto.setUum_usagetypeeng_vc(entity.getUnitUsageMaster().getUum_usagetypeeng_vc());
        dto.setUum_usagetypell_vc(entity.getUnitUsageMaster().getUum_usagetypell_vc());
        dto.setUsm_usagetypeeng_vc(entity.getUsm_usagetypeeng_vc());
        dto.setUsm_usagetypell_vc(entity.getUsm_usagetypell_vc());
        dto.setUsm_usercharges_i(entity.getUsm_usercharges_i());
//        dto.setUsm_taxtype_i(entity.getUsm_taxtype_i());
        dto.setUsmApplyDifferentRateVc(entity.getUsmApplyDifferentRateVc());
        dto.setUsm_rvtype_vc(entity.getUsm_rvtype_vc());
//        dto.setUsm_taxamt_i(entity.getUsm_taxamt_i());
        return dto;
    }

    @Override
    public UnitUsageSubType_MasterDto updateUnitUsageSubTypeMaster(Integer usm_usagesubid_i, UnitUsageSubType_MasterDto dto) {
        UnitUsageSubType_MasterEntity entity = unitUsageSubType_masterRepository.findById(usm_usagesubid_i)
                .orElseThrow(() -> new EntityNotFoundException("UnitUsageSubTypeMaster not found with id: " + usm_usagesubid_i));

        // Assuming the DTO contains an ID for the UnitUsageType_Master and other updated fields
        UnitUsageType_MasterEntity unitUsageTypeMaster = unitUsageType_masterRepository.findById(entity.getUnitUsageMaster().getUum_usageid_i())
                .orElseThrow(() -> new EntityNotFoundException("UnitUsageTypeMaster not found with ID: " + dto.getUum_usageclassid_i()));

        // Update entity fields from dto
        entity.setUnitUsageMaster(unitUsageTypeMaster);
        entity.setUsm_usagetypeeng_vc(dto.getUsm_usagetypeeng_vc());
        entity.setUsm_usagetypell_vc(dto.getUsm_usagetypell_vc());
        entity.setUsm_updated_dt(dto.getUsm_updated_dt() == null ? LocalDateTime.now() : dto.getUsm_updated_dt().toLocalDateTime()); // Auto-update the update timestamp if not provided
        entity.setUsm_taxtype_i(dto.getUsm_taxtype_i());
        entity.setUsmApplyDifferentRateVc(dto.getUsmApplyDifferentRateVc());
        entity.setUsm_usercharges_i(dto.getUsm_usercharges_i());
        entity.setUsm_rvtype_vc(dto.getUsm_rvtype_vc());
//        entity.setUsm_taxamt_i(dto.getUsm_taxamt_i());

        UnitUsageSubType_MasterEntity updatedEntity = unitUsageSubType_masterRepository.save(entity);

        return convertToDto(updatedEntity);
    }

    @Override
    public void deleteUnitUsageSubTypeMaster(Integer id) {
        unitUsageSubType_masterRepository.deleteById(id);
    }
}
