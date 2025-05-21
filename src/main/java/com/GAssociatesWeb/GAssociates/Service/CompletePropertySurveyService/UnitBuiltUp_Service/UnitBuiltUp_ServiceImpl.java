package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.UnitBuiltUp_Service;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitBuiltUp_Dto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitBuiltUp_Entity.UnitBuiltUp_Entity;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.UnitBuiltUp_Repository;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.UnitDetails_Repository;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyNumberGenerator_Service.UniqueIdGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UnitBuiltUp_ServiceImpl implements UnitBuiltUp_Service{

    private final UnitBuiltUp_Repository unitBuiltUpRepository;
    private final UnitDetails_Repository unitDetails_repository;
    private final UniqueIdGenerator uniqueIdGenerator;

    @Autowired
    public UnitBuiltUp_ServiceImpl(UnitBuiltUp_Repository unitBuiltUpRepository, UnitDetails_Repository unitDetails_Repository, UniqueIdGenerator uniqueIdGenerator) {
        this.unitBuiltUpRepository = unitBuiltUpRepository;
        this.unitDetails_repository = unitDetails_Repository;
        this.uniqueIdGenerator = uniqueIdGenerator;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UnitBuiltUp_Dto createBuiltUp(UnitBuiltUp_Dto builtUpDto) {
        try {
            int existingBuiltUpCount = unitBuiltUpRepository.countByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(
                    builtUpDto.getPdNewpropertynoVc(), builtUpDto.getUdFloorNoVc(), builtUpDto.getUdUnitNoVc());
            builtUpDto.setUbIdsI(existingBuiltUpCount + 1);
            UnitBuiltUp_Entity unitBuiltUp = convertToEntity(builtUpDto);
            Long builtUpId = uniqueIdGenerator.generateUniqueIdForUnitBuiltUp();
            unitBuiltUp.setId(builtUpId);
            UnitBuiltUp_Entity savedUnitBuiltUp = unitBuiltUpRepository.save(unitBuiltUp);
            return convertToDto(savedUnitBuiltUp);
        }
        catch (EntityNotFoundException e) {
            throw e;
        }catch (Exception e) {
            throw new RuntimeException("Failed to create UnitBuiltUp due to an unexpected error.", e);
        }
    }

    @Override
    @Transactional
    public UnitBuiltUp_Dto updateOrCreateUnitBuiltUp(UnitBuiltUp_Dto dto) {
        // Fetch the existing built-up details based on the composite key
        UnitBuiltUp_Entity builtUpEntity = unitBuiltUpRepository.findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVcAndUbIdsI(
                dto.getPdNewpropertynoVc(),
                dto.getUdFloorNoVc(),
                dto.getUdUnitNoVc(),
                dto.getUbIdsI()
        ).orElseGet(() -> {
            // Create a new UnitBuiltUp entity if it doesn't exist
            UnitBuiltUp_Entity newBuiltUp = convertToEntity(dto);
            int existingBuiltUpCount = unitBuiltUpRepository.countByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(
                    newBuiltUp.getPdNewpropertynoVc(), newBuiltUp.getUdFloornoVc(), newBuiltUp.getUdUnitnoVc());
            newBuiltUp.setUbIdsI(existingBuiltUpCount + 1);
            Long builtUpId = uniqueIdGenerator.generateUniqueIdForUnitBuiltUp();
            newBuiltUp.setId(builtUpId);
            return newBuiltUp;
        });

        // Update the entity with values from the DTO
        updateUnitBuiltUpEntityWithDto(builtUpEntity, dto);

        // Save the entity (either updated or newly created)
        UnitBuiltUp_Entity savedBuiltUp = unitBuiltUpRepository.save(builtUpEntity);

        // Convert the saved entity back to a DTO and return
        return convertToDto(savedBuiltUp);
    }

    @Override
    @Transactional
    public void deleteBuiltUp(String pdNewpropertynoVc) {
        try {
            unitBuiltUpRepository.deleteByPdNewpropertynoVc(pdNewpropertynoVc);
        }catch (Exception e){
            e.printStackTrace(); // Consider using a logger to log this properly
        }
    }
    @Transactional(readOnly = true)
    public List<UnitBuiltUp_Dto> findAllBuiltUpsByUnit(String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc) {
        List<UnitBuiltUp_Entity> builtUps = unitBuiltUpRepository.findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(pdNewpropertynoVc,udFloornoVc,udUnitnoVc);
        return builtUps.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



//    @Transactional
//    @Override
//    public UnitBuiltUp_Dto updateBuiltUp(UnitBuiltUp_Dto builtUpDto) {
//        UnitBuiltUp_Entity existingBuiltUp = unitBuiltUpRepository.findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(builtUpDto.getPdNewpropertynoVc(), builtUpDto.getUdFloorNoVc(),builtUpDto.getUdUnitNoVc());
//        updateEntityWithDto(existingBuiltUp, builtUpDto);
//        UnitBuiltUp_Entity updatedEntity = unitBuiltUpRepository.save(existingBuiltUp);
//        return convertToDto(updatedEntity);
//    }

    @Override
    public List<UnitBuiltUp_Dto> getBuiltUpsByUnitDetails(String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc) {
        List<UnitBuiltUp_Entity> builtUps = unitBuiltUpRepository.findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(
                pdNewpropertynoVc, udFloornoVc, udUnitnoVc);
        return builtUps.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private UnitBuiltUp_Entity convertToEntity(UnitBuiltUp_Dto dto) {
        UnitBuiltUp_Entity entity = new UnitBuiltUp_Entity();

        entity.setPdNewpropertynoVc(dto.getPdNewpropertynoVc());
        entity.setUdFloornoVc(dto.getUdFloorNoVc());
        entity.setUdUnitnoVc(dto.getUdUnitNoVc());
        entity.setUbIdsI(dto.getUbIdsI()); // Assuming dto.getId() maps to ubIdsI
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
//        Random random = new Random();
//        Integer r = random.nextInt();
//        entity.setUbIdsI(r);//need to change this with the inputs from team
        return entity;
    }

    private UnitBuiltUp_Dto convertToDto(UnitBuiltUp_Entity entity) {
        UnitBuiltUp_Dto dto = new UnitBuiltUp_Dto();

        dto.setPdNewpropertynoVc(entity.getPdNewpropertynoVc());
        dto.setUdFloorNoVc(entity.getUdFloornoVc());
        dto.setUdUnitNoVc(entity.getUdUnitnoVc());
        dto.setId(entity.getId()); // Assuming entity.getUbIdsI() maps to ID
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

    //this is used to update builtup
    private void updateUnitBuiltUpEntityWithDto(UnitBuiltUp_Entity entity, UnitBuiltUp_Dto dto) {
        if (dto.getPdNewpropertynoVc() != null) {
            entity.setPdNewpropertynoVc(dto.getPdNewpropertynoVc());
        }
        if (dto.getUdFloorNoVc() != null) {
            entity.setUdFloornoVc(dto.getUdFloorNoVc());
        }
        if (dto.getUdUnitNoVc() != null) {
            entity.setUdUnitnoVc(dto.getUdUnitNoVc());
        }
        if (dto.getUbIdsI() != null) {
            entity.setUbIdsI(dto.getUbIdsI());
        }
        if (dto.getUbRoomTypeVc() != null) {
            entity.setUbRoomtypeVc(dto.getUbRoomTypeVc());
        }
        if (dto.getUbLengthFl() != null) {
            entity.setUbLengthFl(dto.getUbLengthFl());
        }
        if (dto.getUbBreadthFl() != null) {
            entity.setUbBreadthFl(dto.getUbBreadthFl());
        }
        if (dto.getUbExemptionsVc() != null) {
            entity.setUbExemptionstVc(dto.getUbExemptionsVc());
        }
        if (dto.getUbExemLengthFl() != null) {
            entity.setUbExemlengthFl(dto.getUbExemLengthFl());
        }
        if (dto.getUbExemBreadthFl() != null) {
            entity.setUbExembreadthFl(dto.getUbExemBreadthFl());
        }
        if (dto.getUbExemAreaFl() != null) {
            entity.setUbExemareaFl(dto.getUbExemAreaFl());
        }
        if (dto.getUbCarpetAreaFl() != null) {
            entity.setUbCarpetareaFl(dto.getUbCarpetAreaFl());
        }
        if (dto.getUbAssesAreaFl() != null) {
            entity.setUbAssesareaFl(dto.getUbAssesAreaFl());
        }
        if (dto.getUdTimestampDt() != null) {
            entity.setUdTimestampDt(dto.getUdTimestampDt());
        }
        if (dto.getUbLegalStVc() != null) {
            entity.setUbLegalstVc(dto.getUbLegalStVc());
        }
        if (dto.getUbLegalAreaFl() != null) {
            entity.setUbLegalareaFl(dto.getUbLegalAreaFl());
        }
        if (dto.getUbIllegalAreaFl() != null) {
            entity.setUbIllegalareaFl(dto.getUbIllegalAreaFl());
        }
        if (dto.getUdUnitRemarkVc() != null) {
            entity.setUdUnitremarkVc(dto.getUdUnitRemarkVc());
        }
        if (dto.getUbMeasureTypeVc() != null) {
            entity.setUbMeasuretypeVc(dto.getUbMeasureTypeVc());
        }
        if (dto.getUbareabefdedFl() != null) {
            entity.setUbareabefdedFl(dto.getUbareabefdedFl());
        }
        if (dto.getUbDedpercentI() != null) {
            entity.setUbDedpercentI(dto.getUbDedpercentI());
        }
        if (dto.getUbplotareaFl() != null) {
            entity.setPlotareaFl(dto.getUbplotareaFl());
        }
    }

}
