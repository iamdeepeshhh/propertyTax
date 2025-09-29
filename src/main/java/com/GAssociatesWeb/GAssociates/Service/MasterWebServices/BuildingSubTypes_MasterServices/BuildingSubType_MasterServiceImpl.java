package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.BuildingSubTypes_MasterServices;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.BuildingTypes_MasterDto.BuildingSubType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.BuildingTypes_MasterEntity.BuildingSubType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.BuildingTypes_MasterEntity.BuildingType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.BuildingTypes_MasterRepository.BuildingSubType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.BuildingTypes_MasterRepository.BuildingType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BuildingSubType_MasterServiceImpl implements BuildingSubType_MasterService {

    @Autowired
    private BuildingSubType_MasterRepository buildingSubTypeMasterRepository;

    @Autowired
    private BuildingType_MasterRepository buildingTypeMasterRepository;
    @Autowired
    private SequenceService sequenceService;

    @Override
    @Transactional
    public BuildingSubType_MasterDto saveBuildingSubTypeMaster(BuildingSubType_MasterDto dto) {
        BuildingSubType_MasterEntity entity = new BuildingSubType_MasterEntity();

        // Setting properties from DTO to Entity
        Optional<BuildingType_MasterEntity> buildingType = buildingTypeMasterRepository.findById(dto.getBuildingtypeid());
        if (!buildingType.isPresent()) {
            throw new RuntimeException("Building Type not found for ID: " + dto.getBuildingtypeid());
        }

        entity.setBuildingTypeMaster(buildingType.get());
        entity.setBstBuildingsubtypeengVc(dto.getBstBuildingsubtypeengVc());
        entity.setBstBuildingsubtypellVc(dto.getBstBuildingsubtypellVc());
        entity.setBstCreatedDt(dto.getBstCreatedDt());
        entity.setBstUpdatedDt(dto.getBstUpdatedDt());
        entity.setBstRemarksVc(dto.getBstRemarksVc());
        entity.setBtBuildingtypellVc(dto.getBtBuildingtypellVc());
        sequenceService.adjustSequence(BuildingSubType_MasterEntity.class, "buildingsubtype_master_buildingsubtypeid_seq");
        BuildingSubType_MasterEntity savedEntity = buildingSubTypeMasterRepository.save(entity);
        return convertToDTO(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuildingSubType_MasterDto> getAllBuildingSubTypes() {
        return buildingSubTypeMasterRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BuildingSubType_MasterDto getBuildingSubTypeById(Integer id) {
        return null;
    }

    @Override
    public Optional<BuildingSubType_MasterDto> findById(Integer id) {
        return buildingSubTypeMasterRepository.findById(id).map(this::convertToDTO);
    }
    @Override
    public List<BuildingSubType_MasterDto> getBuildingSubTypesByBuildingTypeId(Integer buildingTypeId) {
        return buildingSubTypeMasterRepository.findByBuildingTypeMaster_buildingtypeid(buildingTypeId).stream()
                .map(this::convertToDTO) // Convert each entity to a DTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BuildingSubType_MasterDto updateBuildingSubTypeMaster(BuildingSubType_MasterDto dto, Integer id) {
        BuildingSubType_MasterEntity entity = buildingSubTypeMasterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Building SubType not found with ID: " + id));

        // Similar to save, but check if any field needs to be updated based on DTO
        // Assume update logic here...

        BuildingSubType_MasterEntity updatedEntity = buildingSubTypeMasterRepository.save(entity);
        return convertToDTO(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteBuildingSubTypeMaster(Integer id) {
        buildingSubTypeMasterRepository.deleteById(id);
    }

    private BuildingSubType_MasterDto convertToDTO(BuildingSubType_MasterEntity entity) {
        BuildingSubType_MasterDto dto = new BuildingSubType_MasterDto();
        dto.setBuildingsubtypeid(entity.getBuildingsubtypeid());
        dto.setBstBuildingsubtypeengVc(entity.getBstBuildingsubtypeengVc());
        dto.setBstBuildingsubtypellVc(entity.getBstBuildingsubtypellVc());
        dto.setBuildingtypeid(entity.getBuildingTypeMaster().getBuildingtypeid());
        dto.setBstCreatedDt(entity.getBstCreatedDt());
        dto.setBstUpdatedDt(entity.getBstUpdatedDt());
        dto.setBstRemarksVc(entity.getBstRemarksVc());
        return dto;
    }

}
