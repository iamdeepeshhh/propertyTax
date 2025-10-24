package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingUnitDetails_Service;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_PropertyDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_UnitDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_UnitDetailsEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingUnitDetails_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyNumberGenerator_Service.UniqueIdGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AfterHearingUnitDetails_ServiceImpl implements AfterHearingUnitDetails_Service{

    private final AfterHearingUnitDetails_MasterRepository unitDetailsRepository;
    private final UniqueIdGenerator uniqueIdGenerator;

    @Autowired
    public AfterHearingUnitDetails_ServiceImpl(
            AfterHearingUnitDetails_MasterRepository unitDetailsRepository,
            UniqueIdGenerator uniqueIdGenerator
    ) {
        this.unitDetailsRepository = unitDetailsRepository;
        this.uniqueIdGenerator = uniqueIdGenerator;
    }

    /**
     * Create a new After Hearing Unit record.
     */
    @Override
    @Transactional
    public UnitDetails_Dto createUnit(UnitDetails_Dto unitDetailsDto) {
        try {
            AfterHearing_UnitDetailsEntity entity = convertToEntity(unitDetailsDto);
            Long id = uniqueIdGenerator.generateUniqueIdForUnitDetails();
            entity.setId(id);
            AfterHearing_UnitDetailsEntity savedEntity = unitDetailsRepository.save(entity);
            return convertToDto(savedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create After Hearing Unit record.", e);
        }
    }

    /**
     * Update or create an After Hearing Unit record.
     * Composite key fields (pdNewpropertynoVc, udFloornoVc, udUnitnoVc) remain unchanged.
     */

    @Transactional
    public UnitDetails_Dto updateOrCreateUnit(UnitDetails_Dto dto, PropertyDetails_Dto property) {
        AfterHearing_UnitDetailsEntity entity = unitDetailsRepository
                .findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(
                        dto.getPdNewpropertynoVc(),
                        dto.getUdFloorNoVc(),
                        dto.getUdUnitNoVc()
                )
                .orElseGet(() -> {
                    AfterHearing_UnitDetailsEntity newEntity = convertToEntityFromBase(dto);
                    Long id = uniqueIdGenerator.generateUniqueIdForUnitDetails();
                    newEntity.setId(id);
                    newEntity.setPdNewpropertynoVc(property.getPdNewpropertynoVc());
                    return newEntity;
                });

        updateUnitDetailsEntityWithDto(entity, dto);
        AfterHearing_UnitDetailsEntity savedEntity = unitDetailsRepository.save(entity);
        return convertToDto(savedEntity);
    }

    /**
     * Delete all units belonging to a property number.
     */
    @Override
    @Transactional
    public void deleteUnit(String pdNewpropertynoVc) {
        try {
            unitDetailsRepository.deleteByPdNewpropertynoVc(pdNewpropertynoVc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve all unit details by property number.
     */
    @Transactional
    public List<UnitDetails_Dto> getAllUnitsByProperty(String pdNewpropertynoVc) {
        List<AfterHearing_UnitDetailsEntity> entities =
                unitDetailsRepository.findAllByPdNewpropertynoVc(pdNewpropertynoVc);
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------
    // Conversion Methods
    // -------------------------------------------------------

    /**
     * Converts AfterHearing DTO → Entity (used for new records)
     */
    private AfterHearing_UnitDetailsEntity convertToEntity(UnitDetails_Dto dto) {
        AfterHearing_UnitDetailsEntity entity = new AfterHearing_UnitDetailsEntity();

        entity.setPdNewpropertynoVc(dto.getPdNewpropertynoVc());
        entity.setUdFloornoVc(dto.getUdFloorNoVc());
        entity.setUdUnitnoVc(dto.getUdUnitNoVc());
        entity.setUdOccupantstatusI(dto.getUdOccupantStatusI());
        entity.setUdRentalnoVc(dto.getUdRentalNoVc());
        entity.setUdOccupiernameVc(dto.getUdOccupierNameVc());
        entity.setUdAgreementcopyVc(dto.getUdAgreementCopyVc());
        entity.setUdMobilenoVc(dto.getUdMobileNoVc());
        entity.setUdEmailidVc(dto.getUdEmailIdVc());
        entity.setUdUsagetypeI(dto.getUdUsageTypeI());
        entity.setUdUsagesubtypeI(dto.getUdUsageSubtypeI());
        entity.setUdConstAgeI(dto.getUdConstAgeI());
        entity.setUdConstructionclassI(dto.getUdConstructionClassI());
        entity.setUdAgefactorI(dto.getUdAgeFactorI());
        entity.setUdCarpetareaF(dto.getUdCarpetAreaF());
        entity.setUdExemptedAreaF(dto.getUdExemptedAreaF());
        entity.setUdAssessmentareaF(dto.getUdAssessmentAreaF());
        entity.setUdSignatureVc(dto.getUdSignatureVc());
        entity.setUdTotlegAreaF(dto.getUdTotLegAreaF());
        entity.setUdTotillegareaF(dto.getUdTotIllegAreaF());
        entity.setUdUnitremarkVc(dto.getUdUnitRemarkVc());
        entity.setUdConstyearDt(dto.getUdConstYearDt());
        entity.setUdEstablishmentNameVc(dto.getUdEstablishmentNameVc());
        entity.setUdTenantnoI(dto.getUdTenantNoI());
        entity.setUdAgefactVc(dto.getUdAgeFactVc());
        entity.setUdAssVc(dto.getUdAssVc());
        entity.setUdPlotAreaFl(dto.getUdPlotAreaFl());
        entity.setUdAreaBefDedF(dto.getUdAreaBefDedF());
        return entity;
    }

    /**
     * Converts Base Unit DTO → Entity (used for cloning existing base record into after hearing)
     */
    private AfterHearing_UnitDetailsEntity convertToEntityFromBase(UnitDetails_Dto dto) {
        AfterHearing_UnitDetailsEntity entity = new AfterHearing_UnitDetailsEntity();

        entity.setPdNewpropertynoVc(dto.getPdNewpropertynoVc());
        entity.setUdFloornoVc(dto.getUdFloorNoVc());
        entity.setUdUnitnoVc(dto.getUdUnitNoVc());
        entity.setUdOccupantstatusI(dto.getUdOccupantStatusI());
        entity.setUdRentalnoVc(dto.getUdRentalNoVc());
        entity.setUdOccupiernameVc(dto.getUdOccupierNameVc());
        entity.setUdAgreementcopyVc(dto.getUdAgreementCopyVc());
        entity.setUdMobilenoVc(dto.getUdMobileNoVc());
        entity.setUdEmailidVc(dto.getUdEmailIdVc());
        entity.setUdUsagetypeI(dto.getUdUsageTypeI());
        entity.setUdUsagesubtypeI(dto.getUdUsageSubtypeI());
        entity.setUdConstAgeI(dto.getUdConstAgeI());
        entity.setUdConstructionclassI(dto.getUdConstructionClassI());
        entity.setUdAgefactorI(dto.getUdAgeFactorI());
        entity.setUdCarpetareaF(dto.getUdCarpetAreaF());
        entity.setUdExemptedAreaF(dto.getUdExemptedAreaF());
        entity.setUdAssessmentareaF(dto.getUdAssessmentAreaF());
        entity.setUdSignatureVc(dto.getUdSignatureVc());
        entity.setUdTotlegAreaF(dto.getUdTotLegAreaF());
        entity.setUdTotillegareaF(dto.getUdTotIllegAreaF());
        entity.setUdUnitremarkVc(dto.getUdUnitRemarkVc());
        entity.setUdConstyearDt(dto.getUdConstYearDt());
        entity.setUdEstablishmentNameVc(dto.getUdEstablishmentNameVc());
        entity.setUdTenantnoI(dto.getUdTenantNoI());
        entity.setUdAgefactVc(dto.getUdAgeFactVc());
        entity.setUdAssVc(dto.getUdAssVc());
        entity.setUdPlotAreaFl(dto.getUdPlotAreaFl());
        entity.setUdAreaBefDedF(dto.getUdAreaBefDedF());
        return entity;
    }

    /**
     * Converts Entity → DTO
     */
    private UnitDetails_Dto convertToDto(AfterHearing_UnitDetailsEntity entity) {
        UnitDetails_Dto dto = new UnitDetails_Dto();

        dto.setId(entity.getId());
        dto.setPdNewpropertynoVc(entity.getPdNewpropertynoVc());
        dto.setUdFloorNoVc(entity.getUdFloornoVc());
        dto.setUdUnitNoVc(entity.getUdUnitnoVc());
        dto.setUdOccupantStatusI(entity.getUdOccupantstatusI());
        dto.setUdRentalNoVc(entity.getUdRentalnoVc());
        dto.setUdOccupierNameVc(entity.getUdOccupiernameVc());
        dto.setUdAgreementCopyVc(entity.getUdAgreementcopyVc());
        dto.setUdMobileNoVc(entity.getUdMobilenoVc());
        dto.setUdEmailIdVc(entity.getUdEmailidVc());
        dto.setUdUsageTypeI(entity.getUdUsagetypeI());
        dto.setUdUsageSubtypeI(entity.getUdUsagesubtypeI());
        dto.setUdConstAgeI(entity.getUdConstAgeI());
        dto.setUdConstructionClassI(entity.getUdConstructionclassI());
        dto.setUdAgeFactorI(entity.getUdAgefactorI());
        dto.setUdCarpetAreaF(entity.getUdCarpetareaF());
        dto.setUdExemptedAreaF(entity.getUdExemptedAreaF());
        dto.setUdAssessmentAreaF(entity.getUdAssessmentareaF());
        dto.setUdSignatureVc(entity.getUdSignatureVc());
        dto.setUdTotLegAreaF(entity.getUdTotlegAreaF());
        dto.setUdTotIllegAreaF(entity.getUdTotillegareaF());
        dto.setUdUnitRemarkVc(entity.getUdUnitremarkVc());
        dto.setUdConstYearDt(entity.getUdConstyearDt());
        dto.setUdEstablishmentNameVc(entity.getUdEstablishmentNameVc());
        dto.setUdTenantNoI(entity.getUdTenantnoI());
        dto.setUdAgeFactVc(entity.getUdAgefactVc());
        dto.setUdAssVc(entity.getUdAssVc());
        dto.setUdPlotAreaFl(entity.getUdPlotAreaFl());
        dto.setUdAreaBefDedF(entity.getUdAreaBefDedF());
        return dto;
    }

    // -------------------------------------------------------
    // Update Helper (Immutable Composite Keys)
    // -------------------------------------------------------

    private void updateUnitDetailsEntityWithDto(AfterHearing_UnitDetailsEntity entity, UnitDetails_Dto dto) {
        // 🚫 Skip composite key updates
        if (dto.getUdOccupantStatusI() != null) entity.setUdOccupantstatusI(dto.getUdOccupantStatusI());
        if (dto.getUdRentalNoVc() != null) entity.setUdRentalnoVc(dto.getUdRentalNoVc());
        if (dto.getUdOccupierNameVc() != null) entity.setUdOccupiernameVc(dto.getUdOccupierNameVc());
        if (dto.getUdAgreementCopyVc() != null) entity.setUdAgreementcopyVc(dto.getUdAgreementCopyVc());
        if (dto.getUdMobileNoVc() != null) entity.setUdMobilenoVc(dto.getUdMobileNoVc());
        if (dto.getUdEmailIdVc() != null) entity.setUdEmailidVc(dto.getUdEmailIdVc());
        if (dto.getUdUsageTypeI() != null) entity.setUdUsagetypeI(dto.getUdUsageTypeI());
        if (dto.getUdUsageSubtypeI() != null) entity.setUdUsagesubtypeI(dto.getUdUsageSubtypeI());
        if (dto.getUdConstAgeI() != null) entity.setUdConstAgeI(dto.getUdConstAgeI());
        if (dto.getUdConstructionClassI() != null) entity.setUdConstructionclassI(dto.getUdConstructionClassI());
        if (dto.getUdAgeFactorI() != null) entity.setUdAgefactorI(dto.getUdAgeFactorI());
        if (dto.getUdCarpetAreaF() != null) entity.setUdCarpetareaF(dto.getUdCarpetAreaF());
        if (dto.getUdExemptedAreaF() != null) entity.setUdExemptedAreaF(dto.getUdExemptedAreaF());
        if (dto.getUdAssessmentAreaF() != null) entity.setUdAssessmentareaF(dto.getUdAssessmentAreaF());
        if (dto.getUdSignatureVc() != null) entity.setUdSignatureVc(dto.getUdSignatureVc());
        if (dto.getUdTotLegAreaF() != null) entity.setUdTotlegAreaF(dto.getUdTotLegAreaF());
        if (dto.getUdTotIllegAreaF() != null) entity.setUdTotillegareaF(dto.getUdTotIllegAreaF());
        if (dto.getUdUnitRemarkVc() != null) entity.setUdUnitremarkVc(dto.getUdUnitRemarkVc());
        if (dto.getUdConstYearDt() != null) entity.setUdConstyearDt(dto.getUdConstYearDt());
        if (dto.getUdEstablishmentNameVc() != null) entity.setUdEstablishmentNameVc(dto.getUdEstablishmentNameVc());
        if (dto.getUdTenantNoI() != null) entity.setUdTenantnoI(dto.getUdTenantNoI());
        if (dto.getUdAgeFactVc() != null) entity.setUdAgefactVc(dto.getUdAgeFactVc());
        if (dto.getUdAssVc() != null) entity.setUdAssVc(dto.getUdAssVc());
        if (dto.getUdPlotAreaFl() != null) entity.setUdPlotAreaFl(dto.getUdPlotAreaFl());
        if (dto.getUdAreaBefDedF() != null) entity.setUdAreaBefDedF(dto.getUdAreaBefDedF());
    }
}