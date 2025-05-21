package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.Zone_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.Zone_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity.Zone_MasterEntity;

import java.util.List;

public interface Zone_MasterService {
    Zone_MasterEntity addZones(Zone_MasterDto zoneRequestDTO) throws Exception;
    List<Zone_MasterDto> getAllZones();
}
