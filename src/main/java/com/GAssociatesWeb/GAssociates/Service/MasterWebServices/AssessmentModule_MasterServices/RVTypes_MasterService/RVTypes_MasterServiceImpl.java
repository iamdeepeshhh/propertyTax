package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.RVTypes_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto.RVTypes_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RVTypes_MasterEntity.RVTypeCategory_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RVTypes_MasterEntity.RVTypes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.RVTypes_MasterRepository.RVTypeCategory_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.RVTypes_MasterRepository.RVTypes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class RVTypes_MasterServiceImpl implements RVTypes_MasterService {

    @Autowired
    private RVTypes_MasterRepository rvTypes_MasterRepository;
    @Autowired
    private RVTypeCategory_MasterRepository rvTypeCategory_masterRepository;
    @Autowired
    private SequenceService sequenceService;

    @Override
    public RVTypes_MasterDto saveRVType(RVTypes_MasterDto rvTypeDto) {
        rvTypeDto.setTypeNameVc(generateNextRateTypeName());
        RVTypes_MasterEntity rvType = convertToEntity(rvTypeDto);
        sequenceService.resetSequenceIfTableIsEmpty("rvtypes_master","rvtypes_master_rv_type_id_seq");
        RVTypes_MasterEntity savedRVType = rvTypes_MasterRepository.save(rvType);
        return convertToDTO(savedRVType);
    }

    @Override
    public List<RVTypes_MasterDto> getAllRVTypes() {
        return rvTypes_MasterRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RVTypes_MasterDto getRVTypeById(Long id) {
        return rvTypes_MasterRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public RVTypes_MasterDto updateRVType(Long id, RVTypes_MasterDto rvTypeDto) {
        return rvTypes_MasterRepository.findById(id)
                .map(existingRVType -> {
                    existingRVType.setTypeNameVc(rvTypeDto.getTypeNameVc());
                    existingRVType.setRateFl(rvTypeDto.getRateFl());
                    existingRVType.setAppliedTaxesVc(rvTypeDto.getAppliedTaxesVc());
                    existingRVType.setDescriptionVc(rvTypeDto.getDescriptionUpVc());
                    RVTypeCategory_MasterEntity category = rvTypeCategory_masterRepository.findById(rvTypeDto.getCategoryId())
                            .orElseThrow(() -> new NoSuchElementException("Category not found with id " + rvTypeDto.getCategoryId()));
                    existingRVType.setCategory(category);

                    RVTypes_MasterEntity updatedRVType = rvTypes_MasterRepository.save(existingRVType);
                    return convertToDTO(updatedRVType);
                })
                .orElseThrow(() -> new NoSuchElementException("RVType not found with id " + id));
    }

    private String generateNextRateTypeName() {
        Long maxId = rvTypes_MasterRepository.findMaxId();
        if (maxId == null) {
            maxId = 0L; // Start from 0 if there are no records
        }
        return "Rate Type " + (maxId + 1);
    }

    @Override
    public void deleteRVTypeById(Long id) {
        rvTypes_MasterRepository.deleteById(id);
    }

    private RVTypes_MasterDto convertToDTO(RVTypes_MasterEntity rvTypesMaster) {
        RVTypes_MasterDto rvTypesMasterDTO = new RVTypes_MasterDto();
        rvTypesMasterDTO.setRyTypeId(rvTypesMaster.getRvTypeId());
        rvTypesMasterDTO.setTypeNameVc(rvTypesMaster.getTypeNameVc());
        rvTypesMasterDTO.setRateFl(rvTypesMaster.getRateFl());
        rvTypesMasterDTO.setAppliedTaxesVc(rvTypesMaster.getAppliedTaxesVc());
        rvTypesMasterDTO.setDescriptionVc(rvTypesMaster.getDescriptionVc());
        rvTypesMasterDTO.setCategoryId(rvTypesMaster.getCategory().getCategoryId());
        return rvTypesMasterDTO;
    }

    private RVTypes_MasterEntity convertToEntity(RVTypes_MasterDto rvTypesMasterDTO) {
        RVTypes_MasterEntity rvTypesMaster = new RVTypes_MasterEntity();
        rvTypesMaster.setRvTypeId(rvTypesMasterDTO.getRyTypeId());
        rvTypesMaster.setTypeNameVc(rvTypesMasterDTO.getTypeNameVc());
        rvTypesMaster.setRateFl(rvTypesMasterDTO.getRateFl());
        rvTypesMaster.setAppliedTaxesVc(rvTypesMasterDTO.getAppliedTaxesVc());
        rvTypesMaster.setDescriptionVc(rvTypesMasterDTO.getDescriptionVc());
        if (rvTypesMasterDTO.getCategoryId() != null) {
            rvTypeCategory_masterRepository.findById(rvTypesMasterDTO.getCategoryId()).ifPresent(rvTypesMaster::setCategory);
        } else {
            rvTypesMaster.setCategory(null);
        }
        return rvTypesMaster;
    }
}