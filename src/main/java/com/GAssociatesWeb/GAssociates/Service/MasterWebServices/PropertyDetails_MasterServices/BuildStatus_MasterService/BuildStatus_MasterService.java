package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.BuildStatus_MasterService;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.BuildStatus_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.OwnerType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity.BuildStatus_MasterEntity;

import java.util.List;

public interface BuildStatus_MasterService {
    public BuildStatus_MasterDto saveBuildStatus(BuildStatus_MasterDto buildStatusDto) throws Exception;
    // Additional service methods can be defined as per requirements
    List<BuildStatus_MasterDto> findAllBuildStatuses();
}
