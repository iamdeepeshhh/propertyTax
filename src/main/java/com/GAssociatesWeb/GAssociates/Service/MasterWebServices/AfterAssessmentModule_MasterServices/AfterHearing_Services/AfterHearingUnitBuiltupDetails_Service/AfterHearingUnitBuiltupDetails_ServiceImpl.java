package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingUnitBuiltupDetails_Service;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_UnitBuiltupDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitBuiltUp_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_UnitBuiltupDetailsEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingUnitBuiltup_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingUnitDetails_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyNumberGenerator_Service.UniqueIdGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AfterHearingUnitBuiltupDetails_ServiceImpl implements AfterHearingUnitBuiltupDetails_Service {

    private final AfterHearingUnitBuiltup_MasterRepository unitBuiltUpRepository;
    private final AfterHearingUnitDetails_MasterRepository unitDetailsRepository;
    private final UniqueIdGenerator uniqueIdGenerator;

    @Autowired
    public AfterHearingUnitBuiltupDetails_ServiceImpl(
            AfterHearingUnitBuiltup_MasterRepository unitBuiltUpRepository,
            AfterHearingUnitDetails_MasterRepository unitDetailsRepository,
            UniqueIdGenerator uniqueIdGenerator
    ) {
        this.unitBuiltUpRepository = unitBuiltUpRepository;
        this.unitDetailsRepository = unitDetailsRepository;
        this.uniqueIdGenerator = uniqueIdGenerator;
    }

    /**
     * Create new After Hearing Built-up record.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UnitBuiltUp_Dto createBuiltUp(UnitBuiltUp_Dto builtUpDto) {
        try {
            int existingBuiltUpCount = unitBuiltUpRepository.countByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(
                    builtUpDto.getPdNewpropertynoVc(),
                    builtUpDto.getUdFloorNoVc(),
                    builtUpDto.getUdUnitNoVc()
            );

            builtUpDto.setUbIdsI(existingBuiltUpCount + 1);

            AfterHearing_UnitBuiltupDetailsEntity unitBuiltUp = convertToEntity(builtUpDto);
            Long builtUpId = uniqueIdGenerator.generateUniqueIdForUnitBuiltUp();
            unitBuiltUp.setId(builtUpId);

            AfterHearing_UnitBuiltupDetailsEntity saved = unitBuiltUpRepository.save(unitBuiltUp);
            return convertToDto(saved);

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create After Hearing UnitBuiltUp due to an unexpected error.", e);
        }
    }

    /**
     * Update an existing After Hearing Built-up record (identity remains fixed).
     * If not found, create a new one.
     */
    @Transactional
    public UnitBuiltUp_Dto updateOrCreateUnitBuiltUp(UnitBuiltUp_Dto dto) {
        AfterHearing_UnitBuiltupDetailsEntity entity =
                unitBuiltUpRepository.findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVcAndUbIdsI(
                        dto.getPdNewpropertynoVc(),
                        dto.getUdFloorNoVc(),
                        dto.getUdUnitNoVc(),
                        dto.getUbIdsI()
                ).orElseGet(() -> {
                    // If not found, create new record
                    AfterHearing_UnitBuiltupDetailsEntity newEntity = convertToEntity(dto);
                    int existingCount = unitBuiltUpRepository.countByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(
                            newEntity.getPdNewpropertynoVc(),
                            newEntity.getUdFloornoVc(),
                            newEntity.getUdUnitnoVc()
                    );
                    newEntity.setUbIdsI(existingCount + 1);
                    Long id = uniqueIdGenerator.generateUniqueIdForUnitBuiltUp();
                    newEntity.setId(id);
                    return newEntity;
                });

        // Update mutable fields only (no composite key changes)
        updateUnitBuiltUpEntityWithDto(entity, dto);
        AfterHearing_UnitBuiltupDetailsEntity saved = unitBuiltUpRepository.save(entity);
        return convertToDto(saved);
    }

    /**
     * Delete all built-up entries for a property.
     */
    @Transactional
    public void deleteBuiltUp(String pdNewpropertynoVc) {
        try {
            unitBuiltUpRepository.deleteByPdNewpropertynoVc(pdNewpropertynoVc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all built-up entries for a property, floor, and unit.
     */
    @Transactional(readOnly = true)
    public List<UnitBuiltUp_Dto> findAllBuiltUpsByUnit(String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc) {
        List<AfterHearing_UnitBuiltupDetailsEntity> builtUps = unitBuiltUpRepository
                .findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(pdNewpropertynoVc, udFloornoVc, udUnitnoVc);
        return builtUps.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Get all built-up entries by specific unit details.
     */
    @Transactional(readOnly = true)
    public List<UnitBuiltUp_Dto> getBuiltUpsByUnitDetails(String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc) {
        List<AfterHearing_UnitBuiltupDetailsEntity> builtUps = unitBuiltUpRepository
                .findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(pdNewpropertynoVc, udFloornoVc, udUnitnoVc);
        return builtUps.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // ------------------------------------------------------------
    // Conversion Helpers
    // ------------------------------------------------------------

    private AfterHearing_UnitBuiltupDetailsEntity convertToEntity(UnitBuiltUp_Dto dto) {
        AfterHearing_UnitBuiltupDetailsEntity entity = new AfterHearing_UnitBuiltupDetailsEntity();

        entity.setPdNewpropertynoVc(dto.getPdNewpropertynoVc());
        entity.setUdFloornoVc(dto.getUdFloorNoVc());
        entity.setUdUnitnoVc(dto.getUdUnitNoVc());
        entity.setUbIdsI(dto.getUbIdsI());
        entity.setUbRoomtypeVc(dto.getUbRoomTypeVc());
        entity.setUbLengthFl(dto.getUbLengthFl());
        entity.setUbBreadthFl(dto.getUbBreadthFl());
        entity.setUbExemptionstVc(dto.getUbExemptionsVc());
        entity.setUbExemlengthFl(dto.getUbExemLengthFl());
        entity.setUbExembreadthFl(dto.getUbExemBreadthFl());
        entity.setUbExemareaFl(dto.getUbExemAreaFl());
        entity.setUbCarpetareaFl(dto.getUbCarpetAreaFl());
        entity.setUbAssesareaFl(dto.getUbAssesAreaFl());
        entity.setUdTimestampDt(dto.getUdTimestampDt());
        entity.setUbLegalstVc(dto.getUbLegalStVc());
        entity.setUbLegalareaFl(dto.getUbLegalAreaFl());
        entity.setUbIllegalareaFl(dto.getUbIllegalAreaFl());
        entity.setUdUnitremarkVc(dto.getUdUnitRemarkVc());
        entity.setUbMeasuretypeVc(dto.getUbMeasureTypeVc());
        entity.setUbareabefdedFl(dto.getUbareabefdedFl());
        entity.setUbDedpercentI(dto.getUbDedpercentI());
        entity.setPlotareaFl(dto.getUbplotareaFl());

        return entity;
    }

    private UnitBuiltUp_Dto convertToDto(AfterHearing_UnitBuiltupDetailsEntity entity) {
        UnitBuiltUp_Dto dto = new UnitBuiltUp_Dto();

        dto.setPdNewpropertynoVc(entity.getPdNewpropertynoVc());
        dto.setUdFloorNoVc(entity.getUdFloornoVc());
        dto.setUdUnitNoVc(entity.getUdUnitnoVc());
        dto.setId(entity.getId());
        dto.setUbIdsI(entity.getUbIdsI());
        dto.setUbRoomTypeVc(entity.getUbRoomtypeVc());
        dto.setUbLengthFl(entity.getUbLengthFl());
        dto.setUbBreadthFl(entity.getUbBreadthFl());
        dto.setUbExemptionsVc(entity.getUbExemptionstVc());
        dto.setUbExemLengthFl(entity.getUbExemlengthFl());
        dto.setUbExemBreadthFl(entity.getUbExembreadthFl());
        dto.setUbExemAreaFl(entity.getUbExemareaFl());
        dto.setUbCarpetAreaFl(entity.getUbCarpetareaFl());
        dto.setUbAssesAreaFl(entity.getUbAssesareaFl());
        dto.setUbLegalStVc(entity.getUbLegalstVc());
        dto.setUbLegalAreaFl(entity.getUbLegalareaFl());
        dto.setUbIllegalAreaFl(entity.getUbIllegalareaFl());
        dto.setUdUnitRemarkVc(entity.getUdUnitremarkVc());
        dto.setUbMeasureTypeVc(entity.getUbMeasuretypeVc());
        dto.setUbareabefdedFl(entity.getUbareabefdedFl());
        dto.setUbDedpercentI(entity.getUbDedpercentI());
        dto.setUbplotareaFl(entity.getPlotareaFl());

        return dto;
    }

    // ------------------------------------------------------------
    // Update Helper (immutable composite key)
    // ------------------------------------------------------------
    private void updateUnitBuiltUpEntityWithDto(AfterHearing_UnitBuiltupDetailsEntity entity, UnitBuiltUp_Dto dto) {
        // ❌ Do not update composite key fields
        // pdNewpropertynoVc, udFloorNoVc, udUnitNoVc, ubIdsI remain fixed

        if (dto.getUbRoomTypeVc() != null) entity.setUbRoomtypeVc(dto.getUbRoomTypeVc());
        if (dto.getUbLengthFl() != null) entity.setUbLengthFl(dto.getUbLengthFl());
        if (dto.getUbBreadthFl() != null) entity.setUbBreadthFl(dto.getUbBreadthFl());
        if (dto.getUbExemptionsVc() != null) entity.setUbExemptionstVc(dto.getUbExemptionsVc());
        if (dto.getUbExemLengthFl() != null) entity.setUbExemlengthFl(dto.getUbExemLengthFl());
        if (dto.getUbExemBreadthFl() != null) entity.setUbExembreadthFl(dto.getUbExemBreadthFl());
        if (dto.getUbExemAreaFl() != null) entity.setUbExemareaFl(dto.getUbExemAreaFl());
        if (dto.getUbCarpetAreaFl() != null) entity.setUbCarpetareaFl(dto.getUbCarpetAreaFl());
        if (dto.getUbAssesAreaFl() != null) entity.setUbAssesareaFl(dto.getUbAssesAreaFl());
        if (dto.getUdTimestampDt() != null) entity.setUdTimestampDt(dto.getUdTimestampDt());
        if (dto.getUbLegalStVc() != null) entity.setUbLegalstVc(dto.getUbLegalStVc());
        if (dto.getUbLegalAreaFl() != null) entity.setUbLegalareaFl(dto.getUbLegalAreaFl());
        if (dto.getUbIllegalAreaFl() != null) entity.setUbIllegalareaFl(dto.getUbIllegalAreaFl());
        if (dto.getUdUnitRemarkVc() != null) entity.setUdUnitremarkVc(dto.getUdUnitRemarkVc());
        if (dto.getUbMeasureTypeVc() != null) entity.setUbMeasuretypeVc(dto.getUbMeasureTypeVc());
        if (dto.getUbareabefdedFl() != null) entity.setUbareabefdedFl(dto.getUbareabefdedFl());
        if (dto.getUbDedpercentI() != null) entity.setUbDedpercentI(dto.getUbDedpercentI());
        if (dto.getUbplotareaFl() != null) entity.setPlotareaFl(dto.getUbplotareaFl());
    }
}

