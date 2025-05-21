package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertyUsageSubType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropSubUsageType_MasterDto;

import java.util.List;
import java.util.Optional;

public interface PropertySubUsageType_MasterService {
    PropSubUsageType_MasterDto saveUsageSubType(PropSubUsageType_MasterDto dto);
    List<PropSubUsageType_MasterDto> findAll();
    Optional<PropSubUsageType_MasterDto> findById(Integer id);
    public List<PropSubUsageType_MasterDto> findUsageSubTypesByUsageId(Integer usageId);
}
