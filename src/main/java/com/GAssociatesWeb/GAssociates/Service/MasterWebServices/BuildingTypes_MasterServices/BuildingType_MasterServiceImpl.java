package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.BuildingTypes_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.BuildingTypes_MasterDto.BuildingType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.BuildingTypes_MasterEntity.BuildingType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropClassification_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.BuildingTypes_MasterRepository.BuildingType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository.PropClassification_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BuildingType_MasterServiceImpl implements BuildingType_MasterService {
    @Autowired
    private BuildingType_MasterRepository buildingType_masterRepository;
    @Autowired
    SequenceService sequenceService;
    @Autowired
    private PropClassification_MasterRepository propClassification_masterRepository;

    @Override
    @Transactional
    public BuildingType_MasterDto saveBuildingTypeMaster(BuildingType_MasterDto buildingTypeMasterDTO) {
        PropClassification_MasterEntity classificationMaster = propClassification_masterRepository.findById(buildingTypeMasterDTO.getPcmClassidI())
                .orElseThrow(() -> new RuntimeException("Property Classification not found for ID: " + buildingTypeMasterDTO.getPcmClassidI()));

        BuildingType_MasterEntity buildingType = new BuildingType_MasterEntity();
        // Assuming direct reference set via a setter method (adjust if using a different field name)
        buildingType.setPropertyClassificationMaster(classificationMaster);
        updateEntityFromDto(buildingType, buildingTypeMasterDTO);
        Integer nextId = sequenceService.getNextSequenceValue("buildingtype_master_buildingtypeid_seq");
        buildingType.setBuildingtypeid(nextId);
        sequenceService.resetSequenceIfTableIsEmpty("buildingtype_master", "buildingtype_master_buildingtypeid_seq");
        BuildingType_MasterEntity savedEntity = buildingType_masterRepository.save(buildingType);
        return convertToDTO(savedEntity);
    }

    @Override
    public List<BuildingType_MasterDto> getAllBuildingTypes() {
        List<BuildingType_MasterEntity> entities = buildingType_masterRepository.findAll();
        return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<BuildingType_MasterDto> findById(Integer id) {
        return buildingType_masterRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<BuildingType_MasterDto> getBuildingTypesByPropertyClassificationId(Integer pcmClassidI) {
        return buildingType_masterRepository.findByPropertyClassificationMaster_PcmClassidI(pcmClassidI).stream()
                .map(this::convertToDTO) // Ensure this method name matches your actual conversion method
                .collect(Collectors.toList());
    }

    @Override
    public BuildingType_MasterDto updateBuildingType(BuildingType_MasterDto buildingTypeMasterDTO, Integer id) {
        BuildingType_MasterEntity entity = buildingType_masterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Building Type not found"));
        // Update entity fields here
        entity = buildingType_masterRepository.save(entity);
        return convertToDTO(entity);
    }

    @Override
    public void deleteBuildingType(Integer id) {
        buildingType_masterRepository.deleteById(id);
    }

    private void updateEntityFromDto(BuildingType_MasterEntity entity, BuildingType_MasterDto dto) {
        entity.setBtBuildingtypeengVc(dto.getBtBuildingtypeengVc());
        // No need to set pcmClassidI directly if it's managed via a relationship
        entity.setPcmProptypellVc(dto.getPcmProptypellVc());
        entity.setBtBuildingtypellVc(dto.getBtBuildingtypellVc());
        entity.setBtCreatedDt(dto.getBtCreatedDt());
        entity.setBtUpdatedDt(dto.getBtUpdatedDt());
        entity.setBtRemarksVc(dto.getBtRemarksVc());
    }
    private BuildingType_MasterDto convertToDTO(BuildingType_MasterEntity entity) {
        BuildingType_MasterDto dto = new BuildingType_MasterDto();
        // Add this line if your DTO has a field for ID
        dto.setBtBuildingtypeengVc(entity.getBtBuildingtypeengVc());
        // Ensure there's a null check for relationships
        if (entity.getPropertyClassificationMaster() != null) {
            dto.setPcmClassidI(entity.getPropertyClassificationMaster().getPcmClassidI());
        }
        dto.setPcmProptypellVc(entity.getPcmProptypellVc());
        dto.setBtBuildingtypellVc(entity.getBtBuildingtypellVc());
        dto.setBtCreatedDt(entity.getBtCreatedDt());
        dto.setBtUpdatedDt(entity.getBtUpdatedDt());
        dto.setBtRemarksVc(entity.getBtRemarksVc());
        dto.setBtBuildingTypeId(entity.getBuildingtypeid());
        return dto;
    }

}
