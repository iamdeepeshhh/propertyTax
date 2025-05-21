package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.Occupancy_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.Occupancy_MasterDto.Occupancy_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.Occupancy_MasterEntity.Occupancy_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.Occupancy_MasterRepository.Occupancy_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Occupancy_MasterServiceImpl implements Occupancy_MasterService{
    private final Occupancy_MasterRepository occupancyMasterRepository;

    @Autowired
    public Occupancy_MasterServiceImpl(Occupancy_MasterRepository occupancyMasterRepository) {
        this.occupancyMasterRepository = occupancyMasterRepository;
    }

    @Override
    public Occupancy_MasterDto saveOccupancy_Master(Occupancy_MasterDto dto) {
        Occupancy_MasterEntity entity = convertToEntity(dto);
        entity = occupancyMasterRepository.save(entity);
        return convertToDto(entity);
    }

    @Override
    public Occupancy_MasterDto findOccupancy_MasterById(Integer id) {
        return null;
    }

    @Override
    public Optional<Occupancy_MasterDto> findById(Integer id) {
        return occupancyMasterRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<Occupancy_MasterDto> findAllOccupancy_Masters() {
        return occupancyMasterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOccupancy_MasterById(Integer id) {
        occupancyMasterRepository.deleteById(id);
    }

    private Occupancy_MasterDto convertToDto(Occupancy_MasterEntity entity) {
        Occupancy_MasterDto dto = new Occupancy_MasterDto();
        dto.setOccupancy_id(entity.getOccupancy_id());
        dto.setOccupancy(entity.getOccupancy());
        dto.setOccupancy_marathi(entity.getOccupancy_marathi());
        dto.setCreated_at(entity.getCreated_at());
        dto.setUpdated_at(entity.getUpdated_at());
        return dto;
    }

    private Occupancy_MasterEntity convertToEntity(Occupancy_MasterDto dto) {
        Occupancy_MasterEntity entity = new Occupancy_MasterEntity();
        entity.setOccupancy_id(dto.getOccupancy_id());
        entity.setOccupancy(dto.getOccupancy());
        entity.setOccupancy_marathi(dto.getOccupancy_marathi());
        entity.setCreated_at(dto.getCreated_at());
        entity.setUpdated_at(dto.getUpdated_at());
        return entity;
    }
}
