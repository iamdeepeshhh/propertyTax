package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertySubClassification_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropSubClassification_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropSubclassification_MasterEntity;

import java.util.List;
import java.util.Optional;

public interface PropSubClassification_MasterService {
    List<PropSubClassification_MasterDto> findAll();

    List<PropSubClassification_MasterDto> getSubclassificationsByClassificationId(Integer classificationId);
    PropSubClassification_MasterDto save(PropSubClassification_MasterDto dto);
    public Optional<PropSubClassification_MasterDto> findByPsmSubclassidI(Integer id);
}
