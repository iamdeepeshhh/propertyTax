package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitUsageType_MasterServices.UnitUsageType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitUsageTypes_MasterDto.UnitUsageType_MasterDto;

import java.util.List;
import java.util.Optional;

public interface UnitUsageType_MasterService {
    List<UnitUsageType_MasterDto> getAllUnitUsages();
    UnitUsageType_MasterDto saveUnitUsageMaster(UnitUsageType_MasterDto dto);
    List<UnitUsageType_MasterDto> findAllByPropUsageId(Integer propUsageId);
    UnitUsageType_MasterDto updateUnitUsageMaster(UnitUsageType_MasterDto dto, Integer id);
    void deleteUnitUsageMaster(Integer id);
    UnitUsageType_MasterDto getUnitUsageMasterById(Integer id);
    public Optional<UnitUsageType_MasterDto> findById(Integer id);
}
