package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.OldWardTypes_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.OldWardTypes_MasterDto.OldWard_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.OldWard_MasterEntity.OldWard_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.OldWardTypes_MasterRepository.OldWard_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OldWard_MasterServiceImpl implements OldWard_MasterService{
    private final OldWard_MasterRepository oldWardMasterRepository;
    private final SequenceService sequenceService;
    @Autowired
    public OldWard_MasterServiceImpl(OldWard_MasterRepository oldWardMasterRepository, SequenceService sequenceService) {
        this.oldWardMasterRepository = oldWardMasterRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    public OldWard_MasterDto saveOldWardMaster(OldWard_MasterDto oldWardMasterDTO) {
        OldWard_MasterEntity entity = convertToEntity(oldWardMasterDTO);
        sequenceService.resetSequenceIfTableIsEmpty("oldward_master", "oldward_master_oldwardno_seq");
        entity = oldWardMasterRepository.save(entity);
        return convertToDTO(entity);
    }

    @Override
    public OldWard_MasterDto findOldWardMasterByOldwardno(Integer oldwardno) {
        OldWard_MasterEntity entity = oldWardMasterRepository.findById(oldwardno.toString())
                .orElseThrow(() -> new RuntimeException("Entity not found with oldwardno: " + oldwardno));
        return convertToDTO(entity);
    }

    @Override
    public List<OldWard_MasterDto> findAllOldWardMasters() {
        return oldWardMasterRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOldWardMasterByOldwardno(Integer oldwardno) {
        oldWardMasterRepository.deleteById(oldwardno.toString());
    }

    private OldWard_MasterDto convertToDTO(OldWard_MasterEntity entity) {
        OldWard_MasterDto dto = new OldWard_MasterDto();
        dto.setOldwardno(entity.getOldwardno());
        dto.setCountNo(entity.getCountNo());
        return dto;
    }

    private OldWard_MasterEntity convertToEntity(OldWard_MasterDto dto) {
        OldWard_MasterEntity entity = new OldWard_MasterEntity();
        entity.setOldwardno(dto.getOldwardno());
        entity.setCountNo(dto.getCountNo());
        return entity;
    }
}
