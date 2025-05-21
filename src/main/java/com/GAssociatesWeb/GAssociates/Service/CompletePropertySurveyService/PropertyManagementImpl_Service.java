package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.CompleteProperty_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitBuiltUp_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity.PropertyDetails_Entity;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.PropertyDetails_Repository;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyDetails_Service.PropertyDetails_Service;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyNumberGenerator_Service.UniqueIdGenerator;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.UnitBuiltUp_Service.UnitBuiltUp_Service;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.UnitDetails_Service.UnitDetails_Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyManagementImpl_Service implements PropertyManagement_Service{
    private final PropertyDetails_Service propertyDetails_Service;
    private final UnitDetails_Service unitDetails_Service;
    private final UnitBuiltUp_Service unitBuiltUp_Service;
    private final PropertyDetails_Repository propertyDetails_repository;
    private final UniqueIdGenerator uniqueIdGenerator;

    @Autowired
    public PropertyManagementImpl_Service(PropertyDetails_Service propertyDetailsService, UnitDetails_Service unitDetailsService, UnitBuiltUp_Service unitBuiltUpService, PropertyDetails_Repository propertyDetailsRepository, UniqueIdGenerator uniqueIdGenerator) {
        this.propertyDetails_Service = propertyDetailsService;
        this.unitDetails_Service = unitDetailsService;
        this.unitBuiltUp_Service = unitBuiltUpService;
        this.propertyDetails_repository = propertyDetailsRepository;
        this.uniqueIdGenerator = uniqueIdGenerator;
    }

    @Transactional
    public CompleteProperty_Dto createCompleteProperty(CompleteProperty_Dto completePropertyDto) {
        try {
            PropertyDetails_Dto propertyDetails_dto = completePropertyDto.getPropertyDetails();
            String wardNo = propertyDetails_dto.getPdWardI();
            String zoneNo = propertyDetails_dto.getPdZoneI();
            String newPropertyNo = uniqueIdGenerator.generateUniquePropertyNo(wardNo, zoneNo);
            propertyDetails_dto.setPdNewpropertynoVc(newPropertyNo);
            PropertyDetails_Dto propertyDetailsDTO = propertyDetails_Service.createProperty(propertyDetails_dto);
            if (propertyDetailsDTO.getUnitDetails() == null || propertyDetailsDTO.getUnitDetails().isEmpty()) {
                propertyDetailsDTO.setUnitDetails(propertyDetails_dto.getUnitDetails());
            }
            List<UnitDetails_Dto> createdUnitDetailsDTOs = propertyDetailsDTO.getUnitDetails().stream()
                    .map(unitDetailsDTO -> {
                        unitDetailsDTO.setPdNewpropertynoVc(propertyDetails_dto.getPdNewpropertynoVc());
                        UnitDetails_Dto createdUnitDetailsDTO = unitDetails_Service.createUnit(unitDetailsDTO);
                        List<UnitBuiltUp_Dto> createdBuiltUpDTOs = unitDetailsDTO.getUnitBuiltupUps().stream()
                                .map(builtUpDTO -> {
                                    // Set the pdNewpropertynoVc to builtUpDTO before creating it
                                    builtUpDTO.setPdNewpropertynoVc(unitDetailsDTO.getPdNewpropertynoVc());
                                    builtUpDTO.setUdFloorNoVc(unitDetailsDTO.getUdFloorNoVc());
                                    builtUpDTO.setUdUnitNoVc(unitDetailsDTO.getUdUnitNoVc());
                                    builtUpDTO.setUdUnitRemarkVc(unitDetailsDTO.getUdUnitRemarkVc());
                                    return unitBuiltUp_Service.createBuiltUp(builtUpDTO);
                                })
                                .collect(Collectors.toList());
                        createdUnitDetailsDTO.setUnitBuiltupUps(createdBuiltUpDTOs);
                        return createdUnitDetailsDTO;
                    })
                    .collect(Collectors.toList());

            completePropertyDto.setPropertyDetails(propertyDetailsDTO);
            completePropertyDto.setUnitDetails(createdUnitDetailsDTOs);

            return completePropertyDto;

        } catch (Exception e) {
            // Log the exception or handle it as necessary
            throw new RuntimeException("Failed to create the complete property.", e);
        }
    }

    @Transactional
    public void deleteCompleteProperty(String pdNewpropertynoVc) {
        CompleteProperty_Dto cd = new CompleteProperty_Dto();
        unitBuiltUp_Service.deleteBuiltUp(pdNewpropertynoVc);
        unitDetails_Service.deleteUnit(pdNewpropertynoVc);
        propertyDetails_Service.deleteProperty(pdNewpropertynoVc);
    }

    @Transactional(readOnly = true)
    public List<PropertyDetails_Dto> searchNewProperties(String surveyPropertyNo, String ownerName, Integer wardNo) {
        List<PropertyDetails_Entity> properties;

         if (surveyPropertyNo != null && wardNo != null){
            Optional<PropertyDetails_Entity> optionalProperty = propertyDetails_repository.findByPdSurypropnoVcAndPdWardI(surveyPropertyNo, wardNo);
            properties = optionalProperty.map(Collections::singletonList).orElse(Collections.emptyList());
        } else if (ownerName != null && !ownerName.trim().isEmpty() && wardNo != null) {
            properties = propertyDetails_repository.findByPdOwnernameVcContainingIgnoreCaseAndPdWardI(ownerName, wardNo);
        } else if (ownerName != null && !ownerName.trim().isEmpty()) {
            properties = propertyDetails_repository.findByPdOwnernameVcContainingIgnoreCase(ownerName);
        } else if (wardNo != null) {
            properties = propertyDetails_repository.findByPdWardI(wardNo);
        } else if (surveyPropertyNo != null && !surveyPropertyNo.trim().isEmpty()) {
            properties = propertyDetails_repository.findByPdSurypropnoVcContainingIgnoreCase(surveyPropertyNo);
        } else {
            properties = List.of(); // Return an empty list if no criteria are provided
        }

        return properties.stream()
                .map(this::convertPropertyDetailsToDto)
                .collect(Collectors.toList());
    }

    private PropertyDetails_Dto convertPropertyDetailsToDto(PropertyDetails_Entity entity) {
        PropertyDetails_Dto dto = new PropertyDetails_Dto();
        dto.setPdNewpropertynoVc(entity.getPdNewpropertynoVc());
        dto.setPdSurypropnoVc(entity.getPdSurypropnoVc());
        dto.setPdFinalpropnoVc(entity.getPdFinalpropnoVc());
        dto.setPdOwnernameVc(entity.getPdOwnernameVc());
        dto.setPdWardI(entity.getPdWardI().toString());
        dto.setUser_id(entity.getUserId());
        dto.setPdFinalpropnoVc((entity.getPdFinalpropnoVc()));
        dto.setPdPropertyaddressVc(entity.getPdPropertyaddressVc());
        dto.setPdZoneI(entity.getPdZoneI().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss, dd-MM-yyyy");
        String formattedDate = entity.getCreatedAt().format(formatter);
        dto.setCreateddateVc(formattedDate);
        return dto;
    }


    @Transactional(readOnly = true)
    public CompleteProperty_Dto getCompletePropertyBySurveyNumber(String pdSuryPropNo) {
        PropertyDetails_Dto propertyDetailsDto = propertyDetails_Service.getPropertyByPdSurypropnoVc(pdSuryPropNo);
        if (propertyDetailsDto == null) {
            throw new EntityNotFoundException("Property not found with survey number: " + pdSuryPropNo);
        }
        List<UnitDetails_Dto> unitDetailsDtos = unitDetails_Service.getAllUnitsByProperty(propertyDetailsDto.getPdNewpropertynoVc());
        unitDetailsDtos.forEach(unitDetailsDto -> {
            List<UnitBuiltUp_Dto> builtUpDtos = unitBuiltUp_Service.getBuiltUpsByUnitDetails(
                    unitDetailsDto.getPdNewpropertynoVc(), unitDetailsDto.getUdFloorNoVc(), unitDetailsDto.getUdUnitNoVc());
            unitDetailsDto.setUnitBuiltupUps(builtUpDtos);
        });
        CompleteProperty_Dto completePropertyDto = new CompleteProperty_Dto();
        completePropertyDto.setPropertyDetails(propertyDetailsDto);
        completePropertyDto.setUnitDetails(unitDetailsDtos);
        return completePropertyDto;
    }

    @Transactional(readOnly = true)
    public CompleteProperty_Dto getCompletePropertyByNewPropertyNo(String newPropertyNo) {
        PropertyDetails_Dto propertyDetailsDto = propertyDetails_Service.findPropertyDetailsByNewPropertyNo(newPropertyNo);
        List<UnitDetails_Dto> unitDetailsDtos = unitDetails_Service.getAllUnitsByProperty(propertyDetailsDto.getPdNewpropertynoVc());

        unitDetailsDtos.forEach(unitDetailsDto -> {
            List<UnitBuiltUp_Dto> builtUpDtos = unitBuiltUp_Service.getBuiltUpsByUnitDetails(
                    unitDetailsDto.getPdNewpropertynoVc(),
                    unitDetailsDto.getUdFloorNoVc(),
                    unitDetailsDto.getUdUnitNoVc());
            unitDetailsDto.setUnitBuiltupUps(builtUpDtos);
        });

        CompleteProperty_Dto completePropertyDto = new CompleteProperty_Dto();
        completePropertyDto.setPropertyDetails(propertyDetailsDto);
        completePropertyDto.setUnitDetails(unitDetailsDtos);

        return completePropertyDto;
    }

    @Override
    public CompleteProperty_Dto updateCompleteProperty(CompleteProperty_Dto completePropertyDto) {
        // Update Property Details
        PropertyDetails_Dto propertyDetailsDto = completePropertyDto.getPropertyDetails();
        propertyDetails_Service.updateProperty(propertyDetailsDto);

        // Update Units and Built-Up Details
        List<UnitDetails_Dto> unitDetailsDtos = completePropertyDto.getUnitDetails();
        for (UnitDetails_Dto unitDto : unitDetailsDtos) {
            // Update Unit Details
            unitDetails_Service.updateOrCreateUnit(unitDto, propertyDetailsDto);

            // Update Built-Up Details
            for (UnitBuiltUp_Dto builtUpDto : unitDto.getUnitBuiltupUps()) {
                builtUpDto.setUdUnitNoVc(unitDto.getUdUnitNoVc());
                builtUpDto.setUdFloorNoVc(unitDto.getUdFloorNoVc());
                builtUpDto.setPdNewpropertynoVc(unitDto.getPdNewpropertynoVc());
                unitBuiltUp_Service.updateOrCreateUnitBuiltUp(builtUpDto);
            }
        }

        return completePropertyDto;
    }


//    @Transactional
//    public void updateCompleteProperty(String pdNewpropertynoVc, PropertyDetails_Dto updatedPropertyDto) {
//        // Step 1: Update Property Details
//        propertyDetails_Service.updatePropertyDetails(pdNewpropertynoVc, updatedPropertyDto);
//
//        // Retrieve current units for the property to manage updates and deletions
//        List<UnitDetails_Dto> currentUnits = unitDetails_Service.findAllByPdNewpropertynoVc(pdNewpropertynoVc);
//
//        // Step 2: Update Units
//        for (UnitDetails_Dto unitDto : updatedPropertyDto.getUnits()) {
//            if (unitExists(unitDto)) {
//                // Update existing unit
//                unitDetails_Service.updateUnit(unitDto);
//            } else {
//                // Add new unit, setting pdNewpropertynoVc
//                unitDto.setPdNewpropertynoVc(pdNewpropertynoVc);
//                unitDetails_Service.createUnit(unitDto);
//            }
//        }
//    }

    private String generateUniquePropertyNo() {
//        return UUID.randomUUID().toString();
        return  "alright";
    }

    @Override
    public void uploadPropertyImage(String propertyId, MultipartFile propertyImage) {
        propertyDetails_Service.uploadPropertyImage(propertyId, propertyImage);
    }
    @Override
    public void uploadCadImage(String propertyId, MultipartFile cadImage) {
        propertyDetails_Service.uploadCadImage(propertyId, cadImage);
    }
}
