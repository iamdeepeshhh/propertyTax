package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertyUsageType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropUsageType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropUsageType_MasterEntity;

import java.util.List;
import java.util.Optional;

public interface PropertyUsageType_MasterService {
    PropUsageType_MasterDto saveUsageType(PropUsageType_MasterDto propUsageType_masterDto);
    List<PropUsageType_MasterDto> findAll();
    List<PropUsageType_MasterDto> findUsageTypesBySubtypeId(Integer subclassificationId);
    public Optional<PropUsageType_MasterDto> findById(Integer id);
}
