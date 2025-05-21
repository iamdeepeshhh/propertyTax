package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.RoomType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.RoomType_MasterDto.RoomType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.RoomType_MasterEntity.RoomType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.RoomType_MasterRepository.RoomType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomType_MasterServiceImpl implements RoomType_MasterService {

    @Autowired
    private RoomType_MasterRepository roomType_masterRepository;
    @Autowired
    private SequenceService sequenceService;

    @Override
    public List<RoomType_MasterDto> findAll() {
        return roomType_masterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoomType_MasterDto findById(Integer id) {
        Optional<RoomType_MasterEntity> entity = roomType_masterRepository.findById(id);
        if (entity.isPresent()) {
            return convertToDto(entity.get());
        }
        return null; // Or throw an exception/return an optional depending on your error handling strategy
    }

    @Override
    public RoomType_MasterDto addRoomType(RoomType_MasterDto roomTypeMasterDto) {
        RoomType_MasterEntity roomType_masterEntity = convertToEntity(roomTypeMasterDto);
        sequenceService.resetSequenceIfTableIsEmpty("roomtype_master", "roomtype_master_roomtype_id_seq");
        roomType_masterEntity = roomType_masterRepository.save(roomType_masterEntity);
        return convertToDto(roomType_masterEntity);
    }

    @Override
    public void deleteById(Integer id) {
        roomType_masterRepository.deleteById(id);
    }

    // Conversion methods
    private RoomType_MasterEntity convertToEntity(RoomType_MasterDto dto) {
        RoomType_MasterEntity entity = new RoomType_MasterEntity();
        entity.setRoomType(dto.getRoomType());
        entity.setRoomTypeMarathi(dto.getRoomTypeMarathi());
        entity.setRoomSelected(dto.getRoomSelected());
        // Set other fields as necessary
        return entity;
    }

    private RoomType_MasterDto convertToDto(RoomType_MasterEntity entity) {
        RoomType_MasterDto dto = new RoomType_MasterDto();
        dto.setRoomType(entity.getRoomType());
        dto.setRoomTypeMarathi(entity.getRoomTypeMarathi());
        dto.setRoomSelected(entity.getRoomSelected());
        // Set other fields as necessary
        return dto;
    }
}