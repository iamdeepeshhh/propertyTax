package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.BuildingSubTypes_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.BuildingTypes_MasterDto.BuildingSubType_MasterDto;

import java.util.List;
import java.util.Optional;

public interface BuildingSubType_MasterService {
    BuildingSubType_MasterDto saveBuildingSubTypeMaster(BuildingSubType_MasterDto dto);
    List<BuildingSubType_MasterDto> getAllBuildingSubTypes();
    BuildingSubType_MasterDto getBuildingSubTypeById(Integer id);
    BuildingSubType_MasterDto updateBuildingSubTypeMaster(BuildingSubType_MasterDto dto, Integer id);
    void deleteBuildingSubTypeMaster(Integer id);
    public List<BuildingSubType_MasterDto> getBuildingSubTypesByBuildingTypeId(Integer buildingTypeId);
    public Optional<BuildingSubType_MasterDto> findById(Integer id);
}
