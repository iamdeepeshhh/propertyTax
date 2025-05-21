package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.SewerageTypes_MasterServices.SewerageType_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.SewerageTypes_MasterDto.Sewerage_MasterDto;

import java.util.List;

public interface Sewerage_MasterService {
    Sewerage_MasterDto saveSewerageType(Sewerage_MasterDto dto);
    List<Sewerage_MasterDto> findAll();
}
