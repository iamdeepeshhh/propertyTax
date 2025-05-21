package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.WaterConnection_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.WaterConnection_MasterDto.WaterConnection_MasterDto;

import java.util.List;

public interface WaterConnection_MasterService {
    WaterConnection_MasterDto addWaterConnection(WaterConnection_MasterDto dto);
    List<WaterConnection_MasterDto> getAllWaterConnections();
}
