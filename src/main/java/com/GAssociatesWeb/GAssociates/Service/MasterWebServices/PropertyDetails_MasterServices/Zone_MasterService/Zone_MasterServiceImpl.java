package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.Zone_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.Zone_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity.Zone_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyDetails_MasterRepository.Zone_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Zone_MasterServiceImpl implements Zone_MasterService {
    @Autowired
    private final Zone_MasterRepository zone_masterRepository;

    @Autowired
    private final SequenceService sequenceService;

    @Autowired
    public Zone_MasterServiceImpl(Zone_MasterRepository zoneMasterRepository, SequenceService sequenceService) {
        this.zone_masterRepository = zoneMasterRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    public Zone_MasterEntity addZones(Zone_MasterDto zone_masterDto) throws Exception {
        Zone_MasterEntity zone_masterEntity = convertToEntity(zone_masterDto);
        sequenceService.resetSequenceIfTableIsEmpty("zone_master", "zone_master_id_seq");
        return zone_masterRepository.save(zone_masterEntity);
    }


    @Override
    public List<Zone_MasterDto> getAllZones() {
        return zone_masterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private Zone_MasterDto convertToDto(Zone_MasterEntity entity) {
        Zone_MasterDto dto = new Zone_MasterDto();
        dto.setZoneNo(entity.getZoneno());
        // Set other properties if needed
        return dto;
    }

    private Zone_MasterEntity convertToEntity(Zone_MasterDto zone_masterDto) {
        Zone_MasterEntity zone_masterEntity = new Zone_MasterEntity();
        zone_masterEntity.setZoneno(zone_masterDto.getZoneNo());
        return zone_masterEntity;
    }
}