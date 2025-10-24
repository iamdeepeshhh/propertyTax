package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingPropertyDetails_Service;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_PropertyDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_PropertyDetailsEntity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity.PropertyDetails_Entity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyOldDetails_Entity.PropertyOldDetails_Entity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingPropertyDetails_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.PropertyOldDetails_Repository;
import com.GAssociatesWeb.GAssociates.Service.ImageUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional; // ✅ Use this one
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AfterHearingPropertyDetails_ServiceImpl implements AfterHearingPropertyDetails_Service {

    private static final Logger logger = LoggerFactory.getLogger(AfterHearingPropertyDetails_ServiceImpl.class);

    private final AfterHearingPropertyDetails_MasterRepository afterHearingPropertyDetailsRepository;
    private final PropertyOldDetails_Repository propertyOldDetailsRepository;

    @Autowired
    public AfterHearingPropertyDetails_ServiceImpl(
            AfterHearingPropertyDetails_MasterRepository afterHearingPropertyDetailsRepository,
            PropertyOldDetails_Repository propertyOldDetailsRepository
    ) {
        this.afterHearingPropertyDetailsRepository = afterHearingPropertyDetailsRepository;
        this.propertyOldDetailsRepository = propertyOldDetailsRepository;
    }

    // ----------------------------------------------------------------------
    // CREATE
    // ----------------------------------------------------------------------

    @Transactional
    public PropertyDetails_Dto createAfterHearingProperty(PropertyDetails_Dto dto) {
        AfterHearing_PropertyDetailsEntity entity = convertToEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        AfterHearing_PropertyDetailsEntity saved = afterHearingPropertyDetailsRepository.save(entity);
        return convertToDto(saved);
    }

    // ----------------------------------------------------------------------
    // READ
    // ----------------------------------------------------------------------

    @Transactional(readOnly = true)
    public PropertyDetails_Dto getPropertyByNewPropertyNo(String newPropertyNo) {
        try {
            AfterHearing_PropertyDetailsEntity entity = afterHearingPropertyDetailsRepository
                    .findByPdNewpropertynoVc(newPropertyNo)
                    .orElseThrow(() -> new EntityNotFoundException("After Hearing Property not found with number: " + newPropertyNo));

            PropertyDetails_Dto dto = convertToDto(entity);

            // enrich with old details if exists
            if (entity.getPropRefno() != null && !entity.getPropRefno().isEmpty()) {
                enrichDtoWithOldDetails(dto, entity);
            }

            return dto;
        } catch (Exception e) {
            logger.error("Error fetching After Hearing property: {}", e.getMessage());
            throw e;
        }
    }

    // ----------------------------------------------------------------------
    // UPDATE
    // ----------------------------------------------------------------------

    @Transactional
    public PropertyDetails_Dto updateAfterHearingProperty(PropertyDetails_Dto dto) {
        return afterHearingPropertyDetailsRepository.findByPdNewpropertynoVc(dto.getPdNewpropertynoVc())
                .map(existing -> {
                    updateEntityWithDto(existing, dto);
//                    existing.setUpdatedAt(Date.now());
                    AfterHearing_PropertyDetailsEntity updated = afterHearingPropertyDetailsRepository.save(existing);
                    return convertToDto(updated);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        "After Hearing Property not found with number: " + dto.getPdNewpropertynoVc()));
    }

    // ----------------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------------

    @Transactional
    public void deleteAfterHearingProperty(String newPropertyNo) {
        Optional<AfterHearing_PropertyDetailsEntity> entityOpt =
                afterHearingPropertyDetailsRepository.findByPdNewpropertynoVc(newPropertyNo);

        if (entityOpt.isPresent()) {
            afterHearingPropertyDetailsRepository.delete(entityOpt.get());
            logger.info("Deleted After Hearing Property successfully: {}", newPropertyNo);
        } else {
            throw new EntityNotFoundException("After Hearing Property not found: " + newPropertyNo);
        }
    }

    // ----------------------------------------------------------------------
    // IMAGE UPLOADS
    // ----------------------------------------------------------------------

    public void uploadCadImage(String newPropertyNo, MultipartFile cadImage) {
        AfterHearing_PropertyDetailsEntity entity = afterHearingPropertyDetailsRepository.findById(newPropertyNo)
                .orElseThrow(() -> new RuntimeException("After Hearing Property not found"));
        String base64Cad = ImageUtils.convertToBase64(cadImage);
        entity.setPdHouseplan2T(base64Cad);
        afterHearingPropertyDetailsRepository.save(entity);
    }


    public void uploadPropertyImage(String newPropertyNo, MultipartFile propertyImage) {
        AfterHearing_PropertyDetailsEntity entity = afterHearingPropertyDetailsRepository.findById(newPropertyNo)
                .orElseThrow(() -> new RuntimeException("After Hearing Property not found"));
        String base64Property = ImageUtils.convertToBase64(propertyImage);
        entity.setPdPropimageT(base64Property);
        afterHearingPropertyDetailsRepository.save(entity);
    }

    // Conversion and helper methods
    private AfterHearing_PropertyDetailsEntity convertToEntity(PropertyDetails_Dto dto) {
        AfterHearing_PropertyDetailsEntity entity = new AfterHearing_PropertyDetailsEntity();
        entity.setPdZoneI(Integer.valueOf(dto.getPdZoneI()));
        entity.setPdWardI(Integer.valueOf(dto.getPdWardI()));
        entity.setPdOldpropnoVc(dto.getPdOldpropnoVc());
        entity.setPdSurypropnoVc(dto.getPdSurypropnoVc().replace(" ", ""));
        entity.setPdLayoutnameVc(dto.getPdLayoutnameVc());
        entity.setPdLayoutnoVc(dto.getPdLayoutnoVc());
        entity.setPdKhasranoVc(dto.getPdKhasranoVc());
        entity.setPdPlotnoVc(dto.getPdPlotnoVc());
        entity.setPdGrididVc(dto.getPdGrididVc());
        entity.setPdRoadidVc(dto.getPdRoadidVc());
        entity.setPdParcelidVc(dto.getPdParcelidVc());
        entity.setPdNewpropertynoVc(dto.getPdNewpropertynoVc());
        entity.setPdOwnernameVc(dto.getPdOwnernameVc());
        entity.setPdAddnewownernameVc(dto.getPdAddnewownernameVc());
        entity.setPdPannoVc(dto.getPdPannoVc());
        entity.setPdAadharnoVc(dto.getPdAadharnoVc());
        entity.setPdContactnoVc(dto.getPdContactnoVc());
        entity.setPdPropertynameVc(dto.getPdPropertynameVc());
        entity.setPdPropertyaddressVc(dto.getPdPropertyaddressVc());
        entity.setPdPincodeVc(dto.getPdPincodeVc());
        entity.setPdCategoryI(dto.getPdCategoryI());
        entity.setPdPropertytypeI(dto.getPdPropertytypeI());
        entity.setPdPropertysubtypeI(dto.getPdPropertysubtypeI());
        entity.setPdUsagetypeI(dto.getPdUsagetypeI());
        entity.setPdUsagesubtypeI(dto.getPdUsagesubtypeI());
        entity.setPdBuildingtypeI(dto.getPdBuildingtypeI());
        entity.setPdBuildingsubtypeI(dto.getPdBuildingsubtypeI());
        entity.setPdConstAgeI(dto.getPdConstAgeI());
        entity.setPdConstYearVc(dto.getPdConstYearVc());
        entity.setPdPermissionstatusVc(dto.getPdPermissionstatusVc());
        entity.setPdPermissionnoVc(dto.getPdPermissionnoVc());
        entity.setPdPermissiondateDt(dto.getPdPermissiondateDt());
        entity.setPdNooffloorsI(dto.getPdNooffloorsI());
        entity.setPdNoofroomsI(dto.getPdNoofroomsI());
        entity.setPdStairVc(dto.getPdStairVc());
        entity.setPdLiftVc(dto.getPdLiftVc());
        entity.setPdRoadwidthF(dto.getPdRoadwidthF());
        entity.setPdToiletstatusVc(dto.getPdToiletstatusVc());
        entity.setPdToiletsI(dto.getPdToiletsI());
        entity.setPdSeweragesVc(dto.getPdSeweragesVc());
        entity.setPdSeweragesType(dto.getPdSeweragesType());
        entity.setPdWaterconnstatusVc(dto.getPdWaterconnstatusVc());
        entity.setPdWaterconntypeVc(dto.getPdWaterconntypeVc());
        entity.setPdMcconnectionVc(dto.getPdMcconnectionVc());
        entity.setPdMeternoVc(dto.getPdMeternoVc());
        entity.setPdConsumernoVc(dto.getPdConsumernoVc());
        entity.setPdConnectiondateDt(dto.getPdConnectiondateDt());
        entity.setPdPipesizeF(dto.getPdPipesizeF());
        entity.setPdElectricityconnectionVc(dto.getPdElectricityconnectionVc());
        entity.setPdElemeternoVc(dto.getPdElemeternoVc());
        entity.setPdEleconsumernoVc(dto.getPdEleconsumernoVc());
        entity.setPdRainwaterhaverstVc(dto.getPdRainwaterhaverstVc());
        entity.setPdSolarunitVc(dto.getPdSolarunitVc());
        entity.setPdPlotareaF(dto.getPdPlotareaF());
        entity.setPdTotbuiltupareaF(dto.getPdTotbuiltupareaF());
        entity.setPdTotcarpetareaF(dto.getPdTotcarpetareaF());
        entity.setPdTotalexempareaF(dto.getPdTotalexempareaF());
        entity.setPdAssesareaF(dto.getPdAssesareaF());
        entity.setPdArealetoutF(dto.getPdArealetoutF());
        entity.setPdAreanotletoutF(dto.getPdAreanotletoutF());
        entity.setPdCurrassesdateDt(dto.getPdCurrassesdateDt());
        entity.setUserId(dto.getUser_id());
        entity.setPdPolytypeVc(dto.getPdPolytypeVc());
        entity.setPdStatusbuildingVc(dto.getPdStatusbuildingVc());
        entity.setPdLastassesdateDt(dto.getPdLastassesdateDt());
        entity.setPdNoticenoVc(dto.getPdNoticenoVc());
        entity.setPdIndexnoVc(dto.getPdIndexnoVc());
        entity.setPdNewfinalpropnoVc(dto.getPdNewfinalpropnoVc());
        entity.setPdTddetailsVc(dto.getPdTddetailsVc());
        entity.setPdTaxstatusVc(dto.getPdTaxstatusVc());
        entity.setPdChangedVc(dto.getPdChangedVc());
        entity.setPdNadetailsVc(dto.getPdNadetailsVc());
        entity.setPdNanumberI(dto.getPdNanumberI());
        entity.setPdNadateDt(dto.getPdNadateDt());
        entity.setPdSaledeedI(dto.getPdSaledeedI());
        entity.setPdSaledateDt(dto.getPdSaledateDt());
        entity.setPdCitysurveynoF(dto.getPdCitysurveynoF());
        entity.setPdOwnertypeVc(dto.getPdOwnertypeVc());
        entity.setPdBuildingvalueI(dto.getPdBuildingvalueI());
        entity.setPdPlotvalueF(dto.getPdPlotvalueF());
        entity.setPdTpdateDt(dto.getPdTpdateDt());
        entity.setPdTpdetailsVc(dto.getPdTpdetailsVc());
        entity.setPdTpordernumF(dto.getPdTpordernumF());
        entity.setPdOccupinameF(dto.getPdOccupinameF());
        entity.setPdOldrvFl(dto.getPdOldrvFl());
        entity.setPdPropimageT(dto.getPdPropimageT());
        entity.setPdHouseplanT(dto.getPdHouseplanT());
        entity.setPdPropimage2T(dto.getPdPropimage2T());
        entity.setPdHouseplan2T(dto.getPdHouseplan2T());
        entity.setPdSignT(dto.getPdSignT());
        entity.setPropRefno(dto.getPropRefno());
        entity.setPdFinalpropnoVc(dto.getPdFinalpropnoVc());
        entity.setPdImgstatusVc(dto.getPdImgstatusVc());
        entity.setPdSuryprop2Vc(dto.getPdSuryprop2Vc());
        entity.setPdSuryprop1Vc(dto.getPdSuryprop1Vc());
        entity.setPdTdordernumF(dto.getPdTdordernumF());
        entity.setPdTddateDt(dto.getPdTddateDt());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setPdFirstassesdateDt(dto.getPdFirstassesdateDt());
        entity.setId(1);
        return entity;
    }


    private PropertyDetails_Dto convertToDto(AfterHearing_PropertyDetailsEntity entity) {
        PropertyDetails_Dto dto = new PropertyDetails_Dto();

        dto.setPdZoneI(entity.getPdZoneI().toString());
        dto.setPdWardI(entity.getPdWardI().toString());
        dto.setPdOldpropnoVc(entity.getPdOldpropnoVc());
        dto.setPdSurypropnoVc(entity.getPdSurypropnoVc());
        dto.setPdLayoutnameVc(entity.getPdLayoutnameVc());
        dto.setPdLayoutnoVc(entity.getPdLayoutnoVc());
        dto.setPdKhasranoVc(entity.getPdKhasranoVc());
        dto.setPdPlotnoVc(entity.getPdPlotnoVc());
        dto.setPdGrididVc(entity.getPdGrididVc());
        dto.setPdRoadidVc(entity.getPdRoadidVc());
        dto.setPdParcelidVc(entity.getPdParcelidVc());
        dto.setPdNewpropertynoVc(entity.getPdNewpropertynoVc());
        dto.setPdOwnernameVc(entity.getPdOwnernameVc());
        dto.setPdAddnewownernameVc(entity.getPdAddnewownernameVc());
        dto.setPdPannoVc(entity.getPdPannoVc());
        dto.setPdAadharnoVc(entity.getPdAadharnoVc());
        dto.setPdContactnoVc(entity.getPdContactnoVc());
        dto.setPdPropertynameVc(entity.getPdPropertynameVc());
        dto.setPdPropertyaddressVc(entity.getPdPropertyaddressVc());
        dto.setPdPincodeVc(entity.getPdPincodeVc());
        dto.setPdCategoryI(entity.getPdCategoryI());
        dto.setPdPropertytypeI(entity.getPdPropertytypeI());
        dto.setPdPropertysubtypeI(entity.getPdPropertysubtypeI());
        dto.setPdUsagetypeI(entity.getPdUsagetypeI());
        dto.setPdUsagesubtypeI(entity.getPdUsagesubtypeI());
        dto.setPdBuildingtypeI(entity.getPdBuildingtypeI());
        dto.setPdBuildingsubtypeI(entity.getPdBuildingsubtypeI());
        dto.setPdConstAgeI(entity.getPdConstAgeI());
        dto.setPdConstYearVc(entity.getPdConstYearVc());
        dto.setPdPermissionstatusVc(entity.getPdPermissionstatusVc());
        dto.setPdPermissionnoVc(entity.getPdPermissionnoVc());
        dto.setPdPermissiondateDt(entity.getPdPermissiondateDt());
        dto.setPdNooffloorsI(entity.getPdNooffloorsI());
        dto.setPdNoofroomsI(entity.getPdNoofroomsI());
        dto.setPdStairVc(entity.getPdStairVc());
        dto.setPdLiftVc(entity.getPdLiftVc());
        dto.setPdRoadwidthF(entity.getPdRoadwidthF());
        dto.setPdToiletstatusVc(entity.getPdToiletstatusVc());
        dto.setPdToiletsI(entity.getPdToiletsI());
        dto.setPdSeweragesVc(entity.getPdSeweragesVc());
        dto.setPdSeweragesType(entity.getPdSeweragesType());
        dto.setPdWaterconnstatusVc(entity.getPdWaterconnstatusVc());
        dto.setPdWaterconntypeVc(entity.getPdWaterconntypeVc());
        dto.setPdMcconnectionVc(entity.getPdMcconnectionVc());
        dto.setPdMeternoVc(entity.getPdMeternoVc());
        dto.setPdConsumernoVc(entity.getPdConsumernoVc());
        dto.setPdConnectiondateDt(entity.getPdConnectiondateDt());
        dto.setPdConnectiondateDt_vc(String.valueOf(entity.getPdConnectiondateDt()));
        dto.setPdPipesizeF(entity.getPdPipesizeF());
        dto.setPdElectricityconnectionVc(entity.getPdElectricityconnectionVc());
        dto.setPdElemeternoVc(entity.getPdElemeternoVc());
        dto.setPdEleconsumernoVc(entity.getPdEleconsumernoVc());
        dto.setPdRainwaterhaverstVc(entity.getPdRainwaterhaverstVc());
        dto.setPdSolarunitVc(entity.getPdSolarunitVc());
        dto.setPdPlotareaF(entity.getPdPlotareaF());
        dto.setPdTotbuiltupareaF(entity.getPdTotbuiltupareaF());
        dto.setPdTotcarpetareaF(entity.getPdTotcarpetareaF());
        dto.setPdTotalexempareaF(entity.getPdTotalexempareaF());
        dto.setPdAssesareaF(entity.getPdAssesareaF());
        dto.setPdArealetoutF(entity.getPdArealetoutF());
        dto.setPdAreanotletoutF(entity.getPdAreanotletoutF());
        dto.setPdCurrassesdateDt(entity.getPdCurrassesdateDt());
        dto.setPdOldrvFl(entity.getPdOldrvFl());
        dto.setUser_id(entity.getUserId()); // Note: Adjust according to your DTO's needs
        dto.setPdPolytypeVc(entity.getPdPolytypeVc());
        dto.setPdStatusbuildingVc(entity.getPdStatusbuildingVc());
        dto.setPdLastassesdateDt(entity.getPdLastassesdateDt());
        dto.setPdNoticenoVc(entity.getPdNoticenoVc());
        dto.setPdIndexnoVc(entity.getPdIndexnoVc());
        dto.setPdNewfinalpropnoVc(entity.getPdNewfinalpropnoVc());
        dto.setPdFinalpropnoVc(entity.getPdFinalpropnoVc());
        dto.setPdTaxstatusVc(entity.getPdTaxstatusVc());
        dto.setPdChangedVc(entity.getPdChangedVc());
        dto.setPdNadetailsVc(entity.getPdNadetailsVc());
        dto.setPdNanumberI(entity.getPdNanumberI());
        dto.setPdNadateDt(entity.getPdNadateDt());
        dto.setPdTddetailsVc(entity.getPdTddetailsVc());
        dto.setPdTdordernumF(entity.getPdTdordernumF());
        dto.setPdTddateDt(entity.getPdTddateDt());
        dto.setPdSaledeedI(entity.getPdSaledeedI());
        dto.setPdSaledateDt(entity.getPdSaledateDt());
        dto.setPdCitysurveynoF(entity.getPdCitysurveynoF());
        dto.setPdOwnertypeVc(entity.getPdOwnertypeVc());
        dto.setPdBuildingvalueI(entity.getPdBuildingvalueI());
        dto.setPdPlotvalueF(entity.getPdPlotvalueF());
        dto.setPdTpdateDt(entity.getPdTpdateDt());
        dto.setPdTpdetailsVc(entity.getPdTpdetailsVc());
        dto.setPdTpordernumF(entity.getPdTpordernumF());
        dto.setPdOccupinameF(entity.getPdOccupinameF());
        dto.setPdPropimageT(entity.getPdPropimageT());
        dto.setPdHouseplanT(entity.getPdHouseplanT());
        dto.setPdPropimage2T(entity.getPdPropimage2T());
        dto.setPdHouseplan2T(entity.getPdHouseplan2T());
        dto.setPdSignT(entity.getPdSignT());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss, dd-MM-yyyy");
        String formattedDate = entity.getCreatedAt().format(formatter);
        dto.setCreateddateVc(formattedDate);
        dto.setPdFirstassesdateDt(entity.getPdFirstassesdateDt());
        return dto;
    }

    private void enrichDtoWithOldDetails(PropertyDetails_Dto dto, AfterHearing_PropertyDetailsEntity entity) {
        // Check if the entity has a valid reference number to old property details
        if (entity.getPropRefno() != null && !entity.getPropRefno().isEmpty()) {
            try {
                // Attempt to fetch old property details using the reference number
                Optional<PropertyOldDetails_Entity> oldDetailsOpt = propertyOldDetailsRepository.findByPodRefNoVc(Integer.parseInt(entity.getPropRefno()));
                if (oldDetailsOpt.isPresent()) {
                    PropertyOldDetails_Entity oldDetails = oldDetailsOpt.get();
                    // Set old property details into the DTO
                    dto.setPdOldpropnoVc(oldDetails.getPodOldPropNoVc());
                    dto.setPdOldwardNoVc(String.valueOf(oldDetails.getPodWardI())); // Assuming ward is an integer that needs conversion
                    dto.setPdOldTaxVc(oldDetails.getPodTotalTaxFl());
                } else {
                    // Optionally handle the case where old details are not found
                    logger.warn("No old property details found for reference number: {}", entity.getPropRefno());
                }
            } catch (NumberFormatException e) {
                // Handle the case where the reference number is not a valid integer
                logger.error("Invalid reference number format: {}", entity.getPropRefno(), e);
            } catch (Exception e) {
                // General exception handling, for unexpected errors
                logger.error("Error fetching old property details for reference number: {}", entity.getPropRefno(), e);
            }
        }
    }

    //this method is created for updating property details entity
    private void updateEntityWithDto(AfterHearing_PropertyDetailsEntity entity, PropertyDetails_Dto dto) {
        if (dto.getPdApprovedByDesk1Vc() != null) {
            entity.setPdApprovedByDesk1Vc(dto.getPdApprovedByDesk1Vc());
        }
        if (dto.getPdZoneI() != null) {
            entity.setPdZoneI(Integer.parseInt(dto.getPdZoneI()));
        }
        if (dto.getPdWardI() != null) {
            entity.setPdWardI(Integer.parseInt(dto.getPdWardI()));
        }
        if (dto.getPdOldpropnoVc() != null) {
            entity.setPdOldpropnoVc(dto.getPdOldpropnoVc());
        }
        if (dto.getPdSurypropnoVc() != null) {
            entity.setPdSurypropnoVc(dto.getPdSurypropnoVc().replace(" ", ""));
        }
        if (dto.getPdLayoutnameVc() != null) {
            entity.setPdLayoutnameVc(dto.getPdLayoutnameVc());
        }
        if (dto.getPdLayoutnoVc() != null) {
            entity.setPdLayoutnoVc(dto.getPdLayoutnoVc());
        }
        if (dto.getPdKhasranoVc() != null) {
            entity.setPdKhasranoVc(dto.getPdKhasranoVc());
        }
        if (dto.getPdPlotnoVc() != null) {
            entity.setPdPlotnoVc(dto.getPdPlotnoVc());
        }
        if (dto.getPdGrididVc() != null) {
            entity.setPdGrididVc(dto.getPdGrididVc());
        }
        if (dto.getPdRoadidVc() != null) {
            entity.setPdRoadidVc(dto.getPdRoadidVc());
        }
        if (dto.getPdParcelidVc() != null) {
            entity.setPdParcelidVc(dto.getPdParcelidVc());
        }
        if (dto.getPdNewpropertynoVc() != null) {
            entity.setPdNewpropertynoVc(dto.getPdNewpropertynoVc());
        }
        if (dto.getPdOwnernameVc() != null) {
            entity.setPdOwnernameVc(dto.getPdOwnernameVc());
        }
        if (dto.getPdAddnewownernameVc() != null) {
            entity.setPdAddnewownernameVc(dto.getPdAddnewownernameVc());
        }
        if (dto.getPdPannoVc() != null) {
            entity.setPdPannoVc(dto.getPdPannoVc());
        }
        if (dto.getPdAadharnoVc() != null) {
            entity.setPdAadharnoVc(dto.getPdAadharnoVc());
        }
        if (dto.getPdContactnoVc() != null) {
            entity.setPdContactnoVc(dto.getPdContactnoVc());
        }
        if (dto.getPdPropertynameVc() != null) {
            entity.setPdPropertynameVc(dto.getPdPropertynameVc());
        }
        if (dto.getPdPropertyaddressVc() != null) {
            entity.setPdPropertyaddressVc(dto.getPdPropertyaddressVc());
        }
        if (dto.getPdPincodeVc() != null) {
            entity.setPdPincodeVc(dto.getPdPincodeVc());
        }
        if (dto.getPdCategoryI() != null) {
            entity.setPdCategoryI(dto.getPdCategoryI());
        }
        if (dto.getPdPropertytypeI() != null) {
            entity.setPdPropertytypeI(dto.getPdPropertytypeI());
        }
        if (dto.getPdPropertysubtypeI() != null) {
            entity.setPdPropertysubtypeI(dto.getPdPropertysubtypeI());
        }
        if (dto.getPdUsagetypeI() != null) {
            entity.setPdUsagetypeI(dto.getPdUsagetypeI());
        }
        if (dto.getPdUsagesubtypeI() != null) {
            entity.setPdUsagesubtypeI(dto.getPdUsagesubtypeI());
        }
        if (dto.getPdBuildingtypeI() != null) {
            entity.setPdBuildingtypeI(dto.getPdBuildingtypeI());
        }
        if (dto.getPdBuildingsubtypeI() != null) {
            entity.setPdBuildingsubtypeI(dto.getPdBuildingsubtypeI());
        }
        if (dto.getPdConstAgeI() != null) {
            entity.setPdConstAgeI(dto.getPdConstAgeI());
        }
        if (dto.getPdConstYearVc() != null) {
            entity.setPdConstYearVc(dto.getPdConstYearVc());
        }
        if (dto.getPdPermissionstatusVc() != null) {
            entity.setPdPermissionstatusVc(dto.getPdPermissionstatusVc());
        }
        if (dto.getPdPermissionnoVc() != null) {
            entity.setPdPermissionnoVc(dto.getPdPermissionnoVc());
        }
        if (dto.getPdPermissiondateDt() != null) {
            entity.setPdPermissiondateDt(dto.getPdPermissiondateDt());
        }
        if (dto.getPdNooffloorsI() != null) {
            entity.setPdNooffloorsI(dto.getPdNooffloorsI());
        }
        if (dto.getPdNoofroomsI() != null) {
            entity.setPdNoofroomsI(dto.getPdNoofroomsI());
        }
        if (dto.getPdStairVc() != null) {
            entity.setPdStairVc(dto.getPdStairVc());
        }
        if (dto.getPdLiftVc() != null) {
            entity.setPdLiftVc(dto.getPdLiftVc());
        }
        if (dto.getPdRoadwidthF() != null) {
            entity.setPdRoadwidthF(dto.getPdRoadwidthF());
        }
        if (dto.getPdToiletstatusVc() != null) {
            entity.setPdToiletstatusVc(dto.getPdToiletstatusVc());
        }
        if (dto.getPdToiletsI() != null) {
            entity.setPdToiletsI(dto.getPdToiletsI());
        }
        if (dto.getPdSeweragesVc() != null) {
            entity.setPdSeweragesVc(dto.getPdSeweragesVc());
        }
        if (dto.getPdSeweragesType() != null) {
            entity.setPdSeweragesType(dto.getPdSeweragesType());
        }
        if (dto.getPdWaterconnstatusVc() != null) {
            entity.setPdWaterconnstatusVc(dto.getPdWaterconnstatusVc());
        }
        if (dto.getPdWaterconntypeVc() != null) {
            entity.setPdWaterconntypeVc(dto.getPdWaterconntypeVc());
        }
        if (dto.getPdMcconnectionVc() != null) {
            entity.setPdMcconnectionVc(dto.getPdMcconnectionVc());
        }
        if (dto.getPdMeternoVc() != null) {
            entity.setPdMeternoVc(dto.getPdMeternoVc());
        }
        if (dto.getPdConsumernoVc() != null) {
            entity.setPdConsumernoVc(dto.getPdConsumernoVc());
        }
        if (dto.getPdConnectiondateDt() != null) {
            entity.setPdConnectiondateDt(dto.getPdConnectiondateDt());
        }
        if (dto.getPdPipesizeF() != null) {
            entity.setPdPipesizeF(dto.getPdPipesizeF());
        }
        if (dto.getPdElectricityconnectionVc() != null) {
            entity.setPdElectricityconnectionVc(dto.getPdElectricityconnectionVc());
        }
        if (dto.getPdElemeternoVc() != null) {
            entity.setPdElemeternoVc(dto.getPdElemeternoVc());
        }
        if (dto.getPdEleconsumernoVc() != null) {
            entity.setPdEleconsumernoVc(dto.getPdEleconsumernoVc());
        }
        if (dto.getPdRainwaterhaverstVc() != null) {
            entity.setPdRainwaterhaverstVc(dto.getPdRainwaterhaverstVc());
        }
        if (dto.getPdSolarunitVc() != null) {
            entity.setPdSolarunitVc(dto.getPdSolarunitVc());
        }
        if (dto.getPdPlotareaF() != null) {
            entity.setPdPlotareaF(dto.getPdPlotareaF());
        }
        if (dto.getPdTotbuiltupareaF() != null) {
            entity.setPdTotbuiltupareaF(dto.getPdTotbuiltupareaF());
        }
        if (dto.getPdTotcarpetareaF() != null) {
            entity.setPdTotcarpetareaF(dto.getPdTotcarpetareaF());
        }
        if (dto.getPdTotalexempareaF() != null) {
            entity.setPdTotalexempareaF(dto.getPdTotalexempareaF());
        }
        if (dto.getPdAssesareaF() != null) {
            entity.setPdAssesareaF(dto.getPdAssesareaF());
        }
        if (dto.getPdArealetoutF() != null) {
            entity.setPdArealetoutF(dto.getPdArealetoutF());
        }
        if (dto.getPdAreanotletoutF() != null) {
            entity.setPdAreanotletoutF(dto.getPdAreanotletoutF());
        }
        if (dto.getPdCurrassesdateDt() != null) {
            entity.setPdCurrassesdateDt(dto.getPdCurrassesdateDt());
        }
        if (dto.getPdOldrvFl() != null) {
            entity.setPdOldrvFl(dto.getPdOldrvFl());
        }
        if (dto.getPdNoticenoVc() != null) {
            entity.setPdNoticenoVc(dto.getPdNoticenoVc());
        }
        if (dto.getPdIndexnoVc() != null) {
            entity.setPdIndexnoVc(dto.getPdIndexnoVc());
        }
        if (dto.getPdFinalpropnoVc() != null) {
            entity.setPdFinalpropnoVc(dto.getPdFinalpropnoVc());
        }
        if (dto.getPdImgstatusVc() != null) {
            entity.setPdImgstatusVc(dto.getPdImgstatusVc());
        }
        if (dto.getPdPolytypeVc() != null) {
            entity.setPdPolytypeVc(dto.getPdPolytypeVc());
        }
        if (dto.getPdStatusbuildingVc() != null) {
            entity.setPdStatusbuildingVc(dto.getPdStatusbuildingVc());
        }
        if (dto.getPdTaxstatusVc() != null) {
            entity.setPdTaxstatusVc(dto.getPdTaxstatusVc());
        }
        if (dto.getPdChangedVc() != null) {
            entity.setPdChangedVc(dto.getPdChangedVc());
        }
        if (dto.getPdTpdetailsVc() != null) {
            entity.setPdTpdetailsVc(dto.getPdTpdetailsVc());
        }
        if (dto.getPdTpordernumF() != null) {
            entity.setPdTpordernumF(dto.getPdTpordernumF());
        }
        if (dto.getPdTpdateDt() != null) {
            entity.setPdTpdateDt(dto.getPdTpdateDt());
        }
        if (dto.getPdOccupinameF() != null) {
            entity.setPdOccupinameF(dto.getPdOccupinameF());
        }
        if (dto.getPdFirstassesdateDt() != null) {
            entity.setPdFirstassesdateDt(dto.getPdFirstassesdateDt());
        }
        if (dto.getPdLastassesdateDt() != null) {
            entity.setPdLastassesdateDt(dto.getPdLastassesdateDt());
        }
        if (dto.getPdPropimageT() != null) {
            entity.setPdPropimageT(dto.getPdPropimageT());
        }
        if (dto.getPdHouseplanT() != null) {
            entity.setPdHouseplanT(dto.getPdHouseplanT());
        }
        if (dto.getPdPropimage2T() != null) {
            entity.setPdPropimage2T(dto.getPdPropimage2T());
        }
        if (dto.getPdHouseplan2T() != null) {
            entity.setPdHouseplan2T(dto.getPdHouseplan2T());
        }
        if (dto.getPdSignT() != null) {
            entity.setPdSignT(dto.getPdSignT());
        }
        if (dto.getPdTddetailsVc() != null) {
            entity.setPdTddetailsVc(dto.getPdTddetailsVc());
        }
        if (dto.getPdTdordernumF() != null) {
            entity.setPdTdordernumF(dto.getPdTdordernumF());
        }
        if (dto.getPdTddateDt() != null) {
            entity.setPdTddateDt(dto.getPdTddateDt());
        }
        if (dto.getPdSaledeedI() != null) {
            entity.setPdSaledeedI(dto.getPdSaledeedI());
        }
        if (dto.getPdSaledateDt() != null) {
            entity.setPdSaledateDt(dto.getPdSaledateDt());
        }
        if (dto.getPdCitysurveynoF() != null) {
            entity.setPdCitysurveynoF(dto.getPdCitysurveynoF());
        }
        if (dto.getPdOwnertypeVc() != null) {
            entity.setPdOwnertypeVc(dto.getPdOwnertypeVc());
        }
        if (dto.getPdBuildingvalueI() != null) {
            entity.setPdBuildingvalueI(dto.getPdBuildingvalueI());
        }
        if (dto.getPdPlotvalueF() != null) {
            entity.setPdPlotvalueF(dto.getPdPlotvalueF());
        }
        if (dto.getPdNadetailsVc() != null) {
            entity.setPdNadetailsVc(dto.getPdNadetailsVc());
        }
        if (dto.getPdNanumberI() != null) {
            entity.setPdNanumberI(dto.getPdNanumberI());
        }
        if (dto.getPdNadateDt() != null) {
            entity.setPdNadateDt(dto.getPdNadateDt());
        }
    }

    //    adding methods for uploading images of CAD and Property
//    @Override
//    public void uploadCadImage(String newPropertyNo, MultipartFile cadImage) {
//        PropertyDetails_Entity propertyDetails_entity = AfterHearing.findById(newPropertyNo)
//                .orElseThrow(() -> new RuntimeException("Property not found"));
//        String base64Cad = ImageUtils.convertToBase64(cadImage);
//        propertyDetails_entity.setPdHouseplan2T(base64Cad);
//        propertyDetails_repository.save(propertyDetails_entity);
//    }
//    @Override
//    public void uploadPropertyImage(String newPropertyNo, MultipartFile propertImage) {
//        PropertyDetails_Entity propertyDetails_entity = propertyDetails_repository.findById(newPropertyNo)
//                .orElseThrow(() -> new RuntimeException("Property not found"));
//        String base64Property = ImageUtils.convertToBase64(propertImage);
//        propertyDetails_entity.setPdPropimageT(base64Property);
//        propertyDetails_repository.save(propertyDetails_entity);
//    }

}