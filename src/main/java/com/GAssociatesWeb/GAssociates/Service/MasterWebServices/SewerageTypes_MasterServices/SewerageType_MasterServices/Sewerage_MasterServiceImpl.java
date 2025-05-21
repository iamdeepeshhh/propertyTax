package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.SewerageTypes_MasterServices.SewerageType_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.SewerageTypes_MasterDto.Sewerage_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.SewerageTypes_MasterEntity.Sewerage_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.SewerageTypes_MasterRepository.Sewerage_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Sewerage_MasterServiceImpl implements Sewerage_MasterService {

    private final Sewerage_MasterRepository sewerage_masterRepository;
    private final SequenceService sequenceService;

    @Autowired
    public Sewerage_MasterServiceImpl(Sewerage_MasterRepository sewerage_masterRepository, SequenceService sequenceService) {
        this.sewerage_masterRepository = sewerage_masterRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    public Sewerage_MasterDto saveSewerageType(Sewerage_MasterDto dto) {
        Sewerage_MasterEntity entity = new Sewerage_MasterEntity();
        entity.setId(dto.getId());
        entity.setSewerage(dto.getSewerage());
        sequenceService.resetSequenceIfTableIsEmpty("sewarage_master", "sewarage_master_id_seq");
        entity = sewerage_masterRepository.save(entity);
        return convertToDto(entity);
    }

    @Override
    public List<Sewerage_MasterDto> findAll() {
        return sewerage_masterRepository.findAll().stream()
                .map(entity -> new Sewerage_MasterDto(entity.getId(), entity.getSewerage()))
                .collect(Collectors.toList());
    }

    // Utility method to convert a DTO to an entity
    private Sewerage_MasterEntity convertToEntity(Sewerage_MasterDto dto) {
        Sewerage_MasterEntity entity = new Sewerage_MasterEntity();
        entity.setId(dto.getId());
        entity.setSewerage(dto.getSewerage());
        // Add other fields here as necessary
        return entity;
    }

    // Utility method to convert an entity to a DTO
    private Sewerage_MasterDto convertToDto(Sewerage_MasterEntity entity) {
        return new Sewerage_MasterDto(entity.getId(), entity.getSewerage());
        // Add other fields here as necessary
    }
}