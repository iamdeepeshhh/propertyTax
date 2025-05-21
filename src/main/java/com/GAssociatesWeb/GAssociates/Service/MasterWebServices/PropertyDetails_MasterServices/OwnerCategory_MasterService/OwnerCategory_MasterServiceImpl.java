package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.OwnerCategory_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.OwnerCategory_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity.OwnerCategory_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyDetails_MasterRepository.OwnerCategory_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OwnerCategory_MasterServiceImpl implements OwnerCategory_MasterService {

    @Autowired
    private OwnerCategory_MasterRepository ownerCategory_masterRepository;

    @Override
    public List<OwnerCategory_MasterDto> findAll() {
        return ownerCategory_masterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OwnerCategory_MasterDto findById(Integer id) {
        Optional<OwnerCategory_MasterEntity> entity = ownerCategory_masterRepository.findById(id);
        if (entity.isPresent()) {
            return convertToDto(entity.get());
        }
        return null; // Or throw an exception/return an optional depending on your error handling strategy
    }

    @Override
    public OwnerCategory_MasterDto addOwnerCategory(OwnerCategory_MasterDto ownerCategoryMasterDto) {
        OwnerCategory_MasterEntity ownerCategory_masterEntity = convertToEntity(ownerCategoryMasterDto);
        ownerCategory_masterEntity = ownerCategory_masterRepository.save(ownerCategory_masterEntity);
        return convertToDto(ownerCategory_masterEntity);
    }

    @Override
    public void deleteById(Integer id) {
        ownerCategory_masterRepository.deleteById(id);
    }

    // Conversion methods
    private OwnerCategory_MasterEntity convertToEntity(OwnerCategory_MasterDto dto) {
        OwnerCategory_MasterEntity entity = new OwnerCategory_MasterEntity();
        entity.setOwnerCategory(dto.getOwnerCategory());
        entity.setOwnerCategoryMarathi(dto.getOwnerCategoryMarathi());
        // Set other fields as necessary
        return entity;
    }

    private OwnerCategory_MasterDto convertToDto(OwnerCategory_MasterEntity entity) {
        OwnerCategory_MasterDto dto = new OwnerCategory_MasterDto();
        dto.setOwnerCategory(entity.getOwnerCategory());
        dto.setOwnerCategoryMarathi(entity.getOwnerCategoryMarathi());
        // Set other fields as necessary
        return dto;
    }
}
