package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitTypes_MasterServices.UnitFloorNo_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitTypes_MasterDto.UnitFloorNo_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitTypes_MasterEntity.UnitFloorNo_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitTypes_MasterRepository.UnitFloorNo_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceConstants;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitFloorNo_MasterServiceImpl implements UnitFloorNo_MasterService {

    @Autowired
    private final UnitFloorNo_MasterRepository unitFloorNo_masterRepository;

    @Autowired
    private final SequenceService sequenceService;
    @Autowired
    public UnitFloorNo_MasterServiceImpl(UnitFloorNo_MasterRepository unitFloorNoMasterRepository, SequenceService sequenceService) {
        unitFloorNo_masterRepository = unitFloorNoMasterRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    public UnitFloorNo_MasterEntity saveUnitFloorNoMaster(UnitFloorNo_MasterDto unitFloorNo_masterDto) throws Exception {
        UnitFloorNo_MasterEntity unitFloorNo_masterEntity = new UnitFloorNo_MasterEntity();
        unitFloorNo_masterEntity.setUnitfloorno(unitFloorNo_masterDto.getUnitfloorno());
        sequenceService.resetSequenceIfTableIsEmpty("unitfloorno_master", "unitfloorno_master_id_seq");
        if (unitFloorNo_masterEntity.getId() == null) {
            sequenceService.adjustSequence(UnitFloorNo_MasterEntity.class, "unitfloorno_master_id_seq");
            Integer nextId = sequenceService.getNextSequenceValue("unitfloorno_master_id_seq");
            unitFloorNo_masterEntity.setId(nextId);
        }
        return unitFloorNo_masterRepository.save(unitFloorNo_masterEntity);
    }

    @Override
    public List<UnitFloorNo_MasterDto> getAllUnitFloorNos() {
        return unitFloorNo_masterRepository.findAll().stream()
                .map(entity -> {
                    UnitFloorNo_MasterDto dto = new UnitFloorNo_MasterDto();
                    dto.setUnitfloorno(entity.getUnitfloorno());
                    dto.setId(entity.getId());
                    // Directly include any additional mapping you need here
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
