package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.OwnerCategory_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.OwnerCategory_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity.OwnerCategory_MasterEntity;

import java.util.List;

public interface OwnerCategory_MasterService {
    List<OwnerCategory_MasterDto> findAll();
    OwnerCategory_MasterDto findById(Integer id);
    OwnerCategory_MasterDto addOwnerCategory(OwnerCategory_MasterDto dto);
    void deleteById(Integer id);
}