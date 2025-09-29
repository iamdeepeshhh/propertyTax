package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.RVTypes_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto.RVTypes_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RvTypesAppliedTaxes_MasterDto.RvTypesAppliedTaxes_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.ConsolidatedTaxes_MasterEntity.ConsolidatedTaxes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RVTypes_MasterEntity.RVTypeCategory_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RVTypes_MasterEntity.RVTypes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RvTypesAppliedTaxes_MasterEntity.RvTypesAppliedTaxes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.ConsolidatedTaxes_MasterRepository.ConsolidatedTaxes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.RVTypes_MasterRepository.RVTypeCategory_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.RVTypes_MasterRepository.RVTypes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.RvTypesAppliedTaxes_MasterRepository.RvTypesAppliedTaxes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RVTypes_MasterServiceImpl implements RVTypes_MasterService {

    @Autowired
    private RVTypes_MasterRepository rvTypes_MasterRepository;
    @Autowired
    private RVTypeCategory_MasterRepository rvTypeCategory_masterRepository;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private ConsolidatedTaxes_MasterRepository consolidatedTaxes_masterRepository;
    @Autowired
    private RvTypesAppliedTaxes_MasterRepository rvTypesAppliedTaxes_masterRepository;

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
        return rvTypes_MasterRepository.findById(id).map(existing -> {
            // Update core fields if provided
            if (rvTypeDto.getTypeNameVc() != null)    existing.setTypeNameVc(rvTypeDto.getTypeNameVc());
            if (rvTypeDto.getRateFl() != null)        existing.setRateFl(rvTypeDto.getRateFl());
            if (rvTypeDto.getDescriptionUpVc() != null) existing.setDescriptionVc(rvTypeDto.getDescriptionUpVc());
            // Update category if changed
            if (rvTypeDto.getCategoryId() != null) {
                RVTypeCategory_MasterEntity category = rvTypeCategory_masterRepository
                        .findById(rvTypeDto.getCategoryId())
                        .orElseThrow(() -> new NoSuchElementException("Category not found with id " + rvTypeDto.getCategoryId()));
                existing.setCategory(category);
            }
            //check whether keys are there or not
            if(rvTypeDto.getTaxKeysL() != null){
                rvTypesAppliedTaxes_masterRepository.deleteByRvtypeId(id);

                for (Long taxKey : rvTypeDto.getTaxKeysL()) {
                    RvTypesAppliedTaxes_MasterEntity link = new RvTypesAppliedTaxes_MasterEntity();
                    link.setRvtypeId(id);
                    link.setTaxKeyL(taxKey);
                    link.setCreatedDt(LocalDateTime.now());
                    link.setUpdatedDt(LocalDateTime.now());
                    rvTypesAppliedTaxes_masterRepository.save(link);
                }
                String appliedNames = rvTypeDto.getTaxKeysL().stream()
                        .map(taxKey -> consolidatedTaxes_masterRepository.findByTaxKeyL(taxKey)
                                .map(ConsolidatedTaxes_MasterEntity::getTaxNameVc).orElse(""))
                        .filter(name -> !name.isEmpty())
                        .collect(Collectors.joining(", "));
                existing.setAppliedTaxesVc(appliedNames);
            }
            rvTypes_MasterRepository.save(existing);
            return convertToDTO(existing);
        }).orElseThrow(() -> new NoSuchElementException("RVType not found with id " + id));
    }



//    @Transactional
//    public RvTypesAppliedTaxes_MasterDto applyTaxToRateType(Long rvtypeId, Long taxId) {
//        // Insert mapping if not already present
//        if (!rvTypesAppliedTaxes_masterRepository.existsByRvtypeIdAndTaxId(rvtypeId, taxId)) {
//            RvTypesAppliedTaxes_MasterEntity link = new RvTypesAppliedTaxes_MasterEntity();
//            link.setRvtypeId(rvtypeId);
//            link.setTaxKeysI(taxId);
//            link.setCreatedDt(LocalDateTime.now());
//            link.setUpdatedDt(LocalDateTime.now());
//            rvTypesAppliedTaxes_masterRepository.save(link);
//        }
//        // Rebuild the appliedTaxesVc string on the parent rate type
//        RVTypes_MasterEntity rvType = rvTypes_MasterRepository.findById(rvtypeId)
//                .orElseThrow(() -> new NoSuchElementException("Rate type not found"));
//        List<RvTypesAppliedTaxes_MasterEntity> mappings =
//                rvTypesAppliedTaxes_masterRepository.findByRvtypeId(rvtypeId);
//        String appliedNames = mappings.stream()
//                .map(m -> consolidatedTaxes_masterRepository.findById(m.getTaxKeysI())
//                        .map(ConsolidatedTaxes_MasterEntity::getTaxNameVc).orElse(""))
//                .filter(name -> !name.isEmpty())
//                .collect(Collectors.joining(","));
//        rvType.setAppliedTaxesVc(appliedNames);
//        rvTypes_MasterRepository.save(rvType);
//
//        // Return a DTO for the new link if needed
//        Optional<RvTypesAppliedTaxes_MasterEntity> linkOpt =
//                rvTypesAppliedTaxes_masterRepository.findByRvtypeIdAndTaxId(rvtypeId, taxId);
//
//        RvTypesAppliedTaxes_MasterEntity link = linkOpt
//                .orElseThrow(() -> new IllegalStateException(
//                        "Mapping rvtypeId=" + rvtypeId + ", taxId=" + taxId + " not found"));
//        return convertToDto(link);
//    }

    private RvTypesAppliedTaxes_MasterDto convertToDto(RvTypesAppliedTaxes_MasterEntity entity) {
        RvTypesAppliedTaxes_MasterDto dto = new RvTypesAppliedTaxes_MasterDto();
        dto.setId(entity.getId());
        dto.setRvtypeId(entity.getRvtypeId());
        dto.setTaxKeyL(entity.getTaxKeyL());
        dto.setCreatedDt(entity.getCreatedDt());
        dto.setUpdatedDt(entity.getUpdatedDt());
        return dto;
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

        Set<Long> taxIds = rvTypesAppliedTaxes_masterRepository.findByRvtypeId(rvTypesMaster.getRvTypeId())
                .stream()
                .map(RvTypesAppliedTaxes_MasterEntity::getTaxKeyL)
                .collect(Collectors.toSet());

        rvTypesMasterDTO.setTaxKeysL(taxIds);
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