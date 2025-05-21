package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertySubClassification_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropSubClassification_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropClassification_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropSubclassification_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropUsageSubType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository.PropClassification_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository.PropSubclassification_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropSubClass_MasterServiceImpl implements PropSubClassification_MasterService {

    private final PropSubclassification_MasterRepository propSubclassification_masterRepository;
    private final PropClassification_MasterRepository propClassification_masterRepository;
    private final SequenceService sequenceService;

    @Autowired
    public PropSubClass_MasterServiceImpl(PropSubclassification_MasterRepository subclassificationMasterRepository,
                                          PropClassification_MasterRepository classificationMasterRepository, SequenceService sequenceService) {
        this.propSubclassification_masterRepository = subclassificationMasterRepository;
        this.propClassification_masterRepository = classificationMasterRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    public List<PropSubClassification_MasterDto> findAll() {
        return propSubclassification_masterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PropSubClassification_MasterDto> getSubclassificationsByClassificationId(Integer classificationId) {
        return propSubclassification_masterRepository.findByPropertyClassificationMaster_PcmClassidI(classificationId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PropSubClassification_MasterDto save(PropSubClassification_MasterDto dto) {
        PropClassification_MasterEntity classificationMaster = propClassification_masterRepository.findById(dto.getPropertyTypeId())
                .orElseThrow(() -> new RuntimeException("Property Classification not found"));

        PropSubclassification_MasterEntity psm = new PropSubclassification_MasterEntity();
        psm.setPsmSubclassengVc(dto.getPropertySubtypeName());
        psm.setPsmSubclassllVc(dto.getLocalPropertySubtypeName());
        psm.setPropertyClassificationMaster(classificationMaster);
        sequenceService.adjustSequence(PropSubclassification_MasterEntity.class, "propsubclassification_master_psm_subclassidi_seq");
//        sequenceService.resetSequenceIfTableIsEmpty("propsubclassification_master", "propsubclassification_master_psm_subclassidi_seq");
//        Integer nextId = sequenceService.getNextSequenceValue("propsubclassification_master_psm_subclassidi_seq");
//        psm.setPsmSubclassidI(nextId);
        PropSubclassification_MasterEntity savedEntity = propSubclassification_masterRepository.save(psm);
        return convertToDto(savedEntity);
    }

    @Override
    public Optional<PropSubClassification_MasterDto> findByPsmSubclassidI(Integer id) {
        return propSubclassification_masterRepository.findById(id).map(this::convertToDto);
    }
    private PropSubClassification_MasterDto convertToDto(PropSubclassification_MasterEntity entity) {
        PropSubClassification_MasterDto dto = new PropSubClassification_MasterDto();
        dto.setPropertySubtypeName(entity.getPsmSubclassengVc());
        dto.setLocalPropertySubtypeName(entity.getPsmSubclassllVc());
        dto.setPropertyTypeId(entity.getPropertyClassificationMaster().getPcmClassidI());
        dto.setPropertySubClassificationId(entity.getPsmSubclassidI());
        // Add other fields to map as necessary
        return dto;
    }
}