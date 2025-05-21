package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.BuildingTypes_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.BuildingTypes_MasterDto.BuildingSubType_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.BuildingTypes_MasterDto.BuildingType_MasterDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BuildingType_MasterService {
    BuildingType_MasterDto saveBuildingTypeMaster(BuildingType_MasterDto buildingTypeMasterDTO);
    List<BuildingType_MasterDto> getAllBuildingTypes();
    List<BuildingType_MasterDto> getBuildingTypesByPropertyClassificationId(Integer pcmClassidI);
    BuildingType_MasterDto updateBuildingType(BuildingType_MasterDto buildingTypeMasterDTO, Integer id);
    void deleteBuildingType(Integer id);
    public Optional<BuildingType_MasterDto> findById(Integer id);

}
