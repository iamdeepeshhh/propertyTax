package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertyClassification_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropClassification_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropClassification_MasterEntity;

import java.util.List;
import java.util.Optional;

public interface PropClassification_MasterService {
    PropClassification_MasterDto save(PropClassification_MasterDto dto);
    List<PropClassification_MasterDto> findAll();
    Optional<PropClassification_MasterDto> findById(Integer id);

    public Optional<PropClassification_MasterDto> findByPcmClassidI(Integer pcmClassidI);
}
