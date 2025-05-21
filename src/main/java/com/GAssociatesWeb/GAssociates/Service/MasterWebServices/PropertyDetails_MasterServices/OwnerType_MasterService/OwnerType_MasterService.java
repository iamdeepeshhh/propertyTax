package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.OwnerType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.OwnerType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity.OwnerType_MasterEntity;

import java.util.List;

public interface OwnerType_MasterService {
    public OwnerType_MasterDto addOwnerType(OwnerType_MasterDto ownerTypeMasterDto) throws Exception;
    List<OwnerType_MasterDto> findAllOwnerTypes();
}
