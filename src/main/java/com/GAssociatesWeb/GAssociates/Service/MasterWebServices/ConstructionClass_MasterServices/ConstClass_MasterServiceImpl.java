package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ConstructionClass_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.ConstructionClass_MasterDto.ConstructionClass_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.ConstructionClass_MasterEntity.ConstructionClass_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.ConstructionClass_MasterRepository.ConstructionClass_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConstClass_MasterServiceImpl implements ConstClass_MasterService {
    @Autowired
    private final ConstructionClass_MasterRepository constructionClass_MasterRepository;
    @Autowired
    private final SequenceService sequenceService;

    @Autowired
    public ConstClass_MasterServiceImpl(ConstructionClass_MasterRepository repository, SequenceService sequenceService) {
        this.constructionClass_MasterRepository = repository;
        this.sequenceService = sequenceService;
    }

    @Override
    public ConstructionClass_MasterDto findById(Integer id) {
        ConstructionClass_MasterEntity entity = constructionClass_MasterRepository.findById(id).orElse(null);
        return convertToDto(entity);
    }

    @Override
    public List<ConstructionClass_MasterDto> findAll() {
        List<ConstructionClass_MasterEntity> entities = constructionClass_MasterRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConstructionClass_MasterDto save(ConstructionClass_MasterDto dto) {
        ConstructionClass_MasterEntity entity = convertToEntity(dto);
        sequenceService.resetSequenceIfTableIsEmpty("constructionclass_master", "constructionclass_master_ccm_conclassid_i_seq");
        Integer nextId = sequenceService.getNextSequenceValue("constructionclass_master_ccm_conclassid_i_seq");
        entity.setCcm_conclassid_i(nextId);
        ConstructionClass_MasterEntity savedEntity = constructionClass_MasterRepository.save(entity);
        return convertToDto(savedEntity);
    }

    @Override
    public void deleteById(Integer id) {
        constructionClass_MasterRepository.deleteById(id);
    }

    private ConstructionClass_MasterDto convertToDto(ConstructionClass_MasterEntity entity) {
        if (entity == null) return null;
        ConstructionClass_MasterDto dto = new ConstructionClass_MasterDto();
        dto.setCcm_conclassid_i(entity.getCcm_conclassid_i());
        dto.setCcm_classnameeng_vc(entity.getCcm_classnameeng_vc());
        dto.setCcm_classnamell_vc(entity.getCcm_classnamell_vc());
        dto.setCcm_percentageofdeduction_i(entity.getCcm_percentageofdeduction_i());
        dto.setCcm_created_dt(entity.getCcm_created_dt());
        dto.setCcm_updated_dt(entity.getCcm_updated_dt());
        dto.setCcm_remarks_vc(entity.getCcm_remarks_vc());
       // dto.setId(entity.getId());
        return dto;
    }

    private ConstructionClass_MasterEntity convertToEntity(ConstructionClass_MasterDto dto) {
        if (dto == null) return null;
        ConstructionClass_MasterEntity entity = new ConstructionClass_MasterEntity();
        entity.setCcm_conclassid_i(dto.getCcm_conclassid_i());
        entity.setCcm_classnameeng_vc(dto.getCcm_classnameeng_vc());
        entity.setCcm_percentageofdeduction_i(dto.getCcm_percentageofdeduction_i());
        entity.setCcm_classnamell_vc(dto.getCcm_classnamell_vc());
        entity.setCcm_created_dt(dto.getCcm_created_dt());
        entity.setCcm_updated_dt(dto.getCcm_updated_dt());
        entity.setCcm_remarks_vc(dto.getCcm_remarks_vc());
      //  entity.setId(dto.getId());
        return entity;
    }
}
