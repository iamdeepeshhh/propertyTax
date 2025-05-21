package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.WaterConnection_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.WaterConnection_MasterDto.WaterConnection_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity.WaterConnection_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyDetails_MasterRepository.WaterConnection_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaterConnection_MasterServiceImpl implements WaterConnection_MasterService {

    @Autowired
    private WaterConnection_MasterRepository waterConnection_masterRepository;
    @Autowired
    SequenceService sequenceService;

    @Override
    @Transactional
    public WaterConnection_MasterDto addWaterConnection(WaterConnection_MasterDto dto) {
        WaterConnection_MasterEntity entity = convertToEntity(dto);
        sequenceService.resetSequenceIfTableIsEmpty("waterconnection_master", "waterconnection_master_waterconnection_id_seq");
        entity = waterConnection_masterRepository.save(entity);
        return convertToDto(entity);
    }

    @Override
    public List<WaterConnection_MasterDto> getAllWaterConnections() {
        return waterConnection_masterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Manual conversion methods
    private WaterConnection_MasterEntity convertToEntity(WaterConnection_MasterDto dto) {
        WaterConnection_MasterEntity entity = new WaterConnection_MasterEntity();
        entity.setWaterConnection(dto.getWaterConnection());
        entity.setWaterConnectionMarathi(dto.getWaterConnectionMarathi());
        // Set other fields as necessary
        return entity;
    }

    private WaterConnection_MasterDto convertToDto(WaterConnection_MasterEntity entity) {
        WaterConnection_MasterDto dto = new WaterConnection_MasterDto();
        dto.setWaterConnection(entity.getWaterConnection());
        dto.setWaterConnectionMarathi(entity.getWaterConnectionMarathi());
        // Set other fields as necessary
        return dto;
    }
}
