package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ConstructionClass_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.ConstructionClass_MasterDto.ConstructionClass_MasterDto;

import java.util.List;

public interface ConstClass_MasterService {
    ConstructionClass_MasterDto findById(Integer id);

    List<ConstructionClass_MasterDto> findAll();

    ConstructionClass_MasterDto save(ConstructionClass_MasterDto dto);

    void deleteById(Integer id);
}