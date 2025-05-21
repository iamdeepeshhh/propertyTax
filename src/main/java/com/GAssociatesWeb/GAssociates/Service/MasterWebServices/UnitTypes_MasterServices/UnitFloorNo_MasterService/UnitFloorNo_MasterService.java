package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitTypes_MasterServices.UnitFloorNo_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitTypes_MasterDto.UnitFloorNo_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitTypes_MasterEntity.UnitFloorNo_MasterEntity;

import java.util.List;

public interface UnitFloorNo_MasterService {
    UnitFloorNo_MasterEntity saveUnitFloorNoMaster(UnitFloorNo_MasterDto unitFloorNo_masterDto) throws Exception;
    // Add other service methods as needed
    List<UnitFloorNo_MasterDto> getAllUnitFloorNos();
}

