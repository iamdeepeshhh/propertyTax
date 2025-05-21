package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.RVTypes_MasterService.RVTypeCategory_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto.RVTypeCategory_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RVTypes_MasterEntity.RVTypeCategory_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.RVTypes_MasterRepository.RVTypeCategory_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RVTypeCategory_MasterServiceImpl implements RVTypeCategory_MasterService{

    @Autowired
    private RVTypeCategory_MasterRepository rvTypeCategoryMasterRepository;
    @Autowired
    private SequenceService sequenceService;

    @Override
    public void saveRVTypeCategory(RVTypeCategory_MasterDto rvTypeCategoryMasterDto) {
        RVTypeCategory_MasterEntity entity = convertToEntity(rvTypeCategoryMasterDto);
        sequenceService.resetSequenceIfTableIsEmpty("rvtypescategory_master","rvtypescategory_master_category_id_seq");
        rvTypeCategoryMasterRepository.save(entity);
    }

    @Override
    public List<RVTypeCategory_MasterDto> getAllRVTypeCategories() {
        return rvTypeCategoryMasterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private RVTypeCategory_MasterDto convertToDto(RVTypeCategory_MasterEntity entity) {
        RVTypeCategory_MasterDto dto = new RVTypeCategory_MasterDto();
        dto.setCategoryId(entity.getCategoryId());
        dto.setCategoryNameVc(entity.getCategoryNameVc());
        dto.setCategoryNameLocalVc(entity.getCategoryNameLocalVc());
        dto.setCategoryDescriptionVc(entity.getCategoryDescriptionVc());
        return dto;
    }

    private RVTypeCategory_MasterEntity convertToEntity(RVTypeCategory_MasterDto dto) {
        RVTypeCategory_MasterEntity entity = new RVTypeCategory_MasterEntity();
        entity.setCategoryId(dto.getCategoryId());
        entity.setCategoryNameVc(dto.getCategoryNameVc());
        entity.setCategoryNameLocalVc(dto.getCategoryNameLocalVc());
        entity.setCategoryDescriptionVc(dto.getCategoryDescriptionVc());
        return entity;
    }

}
