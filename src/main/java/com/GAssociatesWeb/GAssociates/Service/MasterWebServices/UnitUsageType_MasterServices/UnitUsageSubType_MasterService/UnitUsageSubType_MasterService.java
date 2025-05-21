package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitUsageType_MasterServices.UnitUsageSubType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitUsageTypes_MasterDto.UnitUsageSubType_MasterDto;

import java.util.List;
import java.util.Optional;

public interface UnitUsageSubType_MasterService {
    UnitUsageSubType_MasterDto saveUnitUsageSubTypeMaster(UnitUsageSubType_MasterDto dto);
    List<UnitUsageSubType_MasterDto> findAllUnitUsageSubTypeMasters();
    List<UnitUsageSubType_MasterDto> findByUnitUsageId(Integer id);
    UnitUsageSubType_MasterDto updateUnitUsageSubTypeMaster(Integer usm_usagesubid_i, UnitUsageSubType_MasterDto dto);
    void deleteUnitUsageSubTypeMaster(Integer id);
    public Optional<UnitUsageSubType_MasterDto> findById(Integer id);
}
