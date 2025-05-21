package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitTypes_MasterServices.UnitNo_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitTypes_MasterDto.UnitNo_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitTypes_MasterEntity.UnitFloorNo_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitTypes_MasterEntity.UnitNo_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitTypes_MasterRepository.UnitNo_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitNo_MasterServiceImpl implements UnitNo_MasterService{
    @Autowired
    private UnitNo_MasterRepository unitNo_masterRepository;
    @Autowired
    private SequenceService sequenceService;


    @Override
    public void saveUnitNoMaster(UnitNo_MasterDto unitNoMasterDto) throws Exception {
        int count = Integer.parseInt(unitNoMasterDto.getUnitno());
        for (int i = 1; i <= count; i++) {
            UnitNo_MasterEntity unitNo_masterEntity = new UnitNo_MasterEntity();
             // Create a new entity for each iteration.
            String unitNo =  String.valueOf(i);
            unitNo_masterEntity.setUnitno(unitNo);
            sequenceService.resetSequenceIfTableIsEmpty("unitno_master", "unitno_master_id_seq");
            unitNo_masterRepository.save(unitNo_masterEntity); // Save each new entity.
        }

        // Method now returns void, indicating it's purely for side effects (creating entities).
    }
    @Override
    public List<UnitNo_MasterDto> getAllUnitNoMasters() {
        return unitNo_masterRepository.findAll().stream()
                .map(entity -> {
                    UnitNo_MasterDto dto = new UnitNo_MasterDto();
                    dto.setId(entity.getId());
                    dto.setUnitno(entity.getUnitno());
                    // Directly include any additional mapping you need here
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
