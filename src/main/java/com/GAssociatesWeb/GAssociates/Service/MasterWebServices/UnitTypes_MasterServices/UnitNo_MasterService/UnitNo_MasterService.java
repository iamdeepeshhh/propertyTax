package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitTypes_MasterServices.UnitNo_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitTypes_MasterDto.UnitNo_MasterDto;

import java.util.List;

public interface UnitNo_MasterService {
    void saveUnitNoMaster(UnitNo_MasterDto unitNoMasterDto) throws Exception;
    List<UnitNo_MasterDto> getAllUnitNoMasters();
}
