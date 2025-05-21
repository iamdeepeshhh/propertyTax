package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.UnitDetails_Service;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity.PropertyDetails_Entity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitDetails_Entity.UnitDetailsId;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitDetails_Entity.UnitDetails_Entity;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.UnitBuiltUp_Repository;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.UnitDetails_Repository;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyNumberGenerator_Service.UniqueIdGenerator;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.UnitBuiltUp_Service.UnitBuiltUp_Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitDetails_ServiceImpl implements UnitDetails_Service {
    private final UnitDetails_Repository unitDetails_repository;
    private final UnitBuiltUp_Repository unitBuiltUp_repository;
    private final UnitBuiltUp_Service unitBuiltUp_service;
    private final UniqueIdGenerator uniqueIdGenerator;

    @Autowired
    public UnitDetails_ServiceImpl(UnitDetails_Repository unitDetails_repository, UnitBuiltUp_Repository unitBuiltUp_repository, UnitBuiltUp_Service unitBuiltUp_service, UniqueIdGenerator uniqueIdGenerator) {
        this.unitDetails_repository = unitDetails_repository;
        this.unitBuiltUp_repository = unitBuiltUp_repository;
        this.unitBuiltUp_service = unitBuiltUp_service;
        this.uniqueIdGenerator = uniqueIdGenerator;
    }

    @Override
    @Transactional
    public UnitDetails_Dto createUnit(UnitDetails_Dto unitDetailsDto) {
        UnitDetails_Entity unitDetails = convertToEntity(unitDetailsDto);
        Long unitId = uniqueIdGenerator.generateUniqueIdForUnitDetails();
        unitDetails.setId(unitId);
        UnitDetails_Entity savedUnitDetails = unitDetails_repository.save(unitDetails);
        return convertToDto(savedUnitDetails);
    }
    @Override
    @Transactional
    public void deleteUnit(String pdNewpropertynoVc) {
        unitDetails_repository.deleteByPdNewpropertynoVc(pdNewpropertynoVc);
    }
    @Override
    @Transactional(readOnly = true)
    public List<UnitDetails_Dto> getAllUnitsByProperty(String pdNewpropertynoVc) {
        List<UnitDetails_Entity> unitDetailsList = unitDetails_repository.findAllByPdNewpropertynoVc(pdNewpropertynoVc); // Simplify: adjust to find by propertyId
        return unitDetailsList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UnitDetails_Dto updateOrCreateUnit(UnitDetails_Dto unitDetailsDto, PropertyDetails_Dto property) {
        UnitDetails_Entity unitEntity = unitDetails_repository.findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(
                unitDetailsDto.getPdNewpropertynoVc(),
                unitDetailsDto.getUdFloorNoVc(),
                unitDetailsDto.getUdUnitNoVc()
        ).orElseGet(() -> {
            UnitDetails_Entity newUnit = convertToEntity(unitDetailsDto);
            Long unitId = uniqueIdGenerator.generateUniqueIdForUnitDetails();
            newUnit.setPdNewpropertynoVc(property.getPdNewpropertynoVc());
            newUnit.setId(unitId);
            return newUnit;
        });
        updateUnitDetailsEntityWithDto(unitEntity, unitDetailsDto);
        UnitDetails_Entity savedUnit = unitDetails_repository.save(unitEntity);

        return convertToDto(savedUnit);
    }



    // Conversion and helper methods
    private UnitDetails_Entity convertToEntity(UnitDetails_Dto dto) {
        UnitDetails_Entity entity = new UnitDetails_Entity();
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
        entity.setId(dto.getId());
        entity.setUdTenantnoI(dto.getUdTenantNoI());
        entity.setUdAgefactVc(dto.getUdAgeFactVc());
        entity.setUdAssVc(dto.getUdAssVc());
        entity.setUdPlotAreaFl(dto.getUdPlotAreaFl());
        entity.setUdAreaBefDedF(dto.getUdAreaBefDedF());
        return entity;
    }

    private UnitDetails_Dto convertToDto(UnitDetails_Entity entity) {
        UnitDetails_Dto dto = new UnitDetails_Dto();

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
        dto.setId(entity.getId());
        dto.setUdTenantNoI(entity.getUdTenantnoI());
        dto.setUdAgeFactVc(entity.getUdAgefactVc());
        dto.setUdAssVc(entity.getUdAssVc());
        dto.setUdAreaBefDedF(entity.getUdAreaBefDedF());
        dto.setUdPlotAreaFl(entity.getUdPlotAreaFl());
        return dto;
    }


    //this method is used to update unit details
    private void updateUnitDetailsEntityWithDto(UnitDetails_Entity entity, UnitDetails_Dto dto) {
        if (dto.getPdNewpropertynoVc() != null) {
            entity.setPdNewpropertynoVc(dto.getPdNewpropertynoVc());
        }
        if (dto.getUdFloorNoVc() != null) {
            entity.setUdFloornoVc(dto.getUdFloorNoVc());
        }
        if (dto.getUdUnitNoVc() != null) {
            entity.setUdUnitnoVc(dto.getUdUnitNoVc());
        }
        if (dto.getUdOccupantStatusI() != null) {
            entity.setUdOccupantstatusI(dto.getUdOccupantStatusI());
        }
        if (dto.getUdRentalNoVc() != null) {
            entity.setUdRentalnoVc(dto.getUdRentalNoVc());
        }
        if (dto.getUdOccupierNameVc() != null) {
            entity.setUdOccupiernameVc(dto.getUdOccupierNameVc());
        }
        if (dto.getUdAgreementCopyVc() != null) {
            entity.setUdAgreementcopyVc(dto.getUdAgreementCopyVc());
        }
        if (dto.getUdMobileNoVc() != null) {
            entity.setUdMobilenoVc(dto.getUdMobileNoVc());
        }
        if (dto.getUdEmailIdVc() != null) {
            entity.setUdEmailidVc(dto.getUdEmailIdVc());
        }
        if (dto.getUdUsageTypeI() != null) {
            entity.setUdUsagetypeI(dto.getUdUsageTypeI());
        }
        if (dto.getUdUsageSubtypeI() != null) {
            entity.setUdUsagesubtypeI(dto.getUdUsageSubtypeI());
        }
        if (dto.getUdConstAgeI() != null) {
            entity.setUdConstAgeI(dto.getUdConstAgeI());
        }
        if (dto.getUdConstructionClassI() != null) {
            entity.setUdConstructionclassI(dto.getUdConstructionClassI());
        }
        if (dto.getUdAgeFactorI() != null) {
            entity.setUdAgefactorI(dto.getUdAgeFactorI());
        }
        if (dto.getUdCarpetAreaF() != null) {
            entity.setUdCarpetareaF(dto.getUdCarpetAreaF());
        }
        if (dto.getUdExemptedAreaF() != null) {
            entity.setUdExemptedAreaF(dto.getUdExemptedAreaF());
        }
        if (dto.getUdAssessmentAreaF() != null) {
            entity.setUdAssessmentareaF(dto.getUdAssessmentAreaF());
        }
        if (dto.getUdSignatureVc() != null) {
            entity.setUdSignatureVc(dto.getUdSignatureVc());
        }
        if (dto.getUdTotLegAreaF() != null) {
            entity.setUdTotlegAreaF(dto.getUdTotLegAreaF());
        }
        if (dto.getUdTotIllegAreaF() != null) {
            entity.setUdTotillegareaF(dto.getUdTotIllegAreaF());
        }
        if (dto.getUdUnitRemarkVc() != null) {
            entity.setUdUnitremarkVc(dto.getUdUnitRemarkVc());
        }
        if (dto.getUdConstYearDt() != null) {
            entity.setUdConstyearDt(dto.getUdConstYearDt());
        }
        if (dto.getUdEstablishmentNameVc() != null) {
            entity.setUdEstablishmentNameVc(dto.getUdEstablishmentNameVc());
        }
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getUdTenantNoI() != null) {
            entity.setUdTenantnoI(dto.getUdTenantNoI());
        }
        if (dto.getUdAgeFactVc() != null) {
            entity.setUdAgefactVc(dto.getUdAgeFactVc());
        }
        if (dto.getUdAssVc() != null) {
            entity.setUdAssVc(dto.getUdAssVc());
        }
        if (dto.getUdPlotAreaFl() != null) {
            entity.setUdPlotAreaFl(dto.getUdPlotAreaFl());
        }
        if (dto.getUdAreaBefDedF() != null) {
            entity.setUdAreaBefDedF(dto.getUdAreaBefDedF());
        }
    }

}