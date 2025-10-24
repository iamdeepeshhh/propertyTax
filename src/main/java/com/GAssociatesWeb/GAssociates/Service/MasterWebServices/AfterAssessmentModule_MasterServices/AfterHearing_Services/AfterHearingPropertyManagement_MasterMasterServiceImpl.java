package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.*;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitBuiltUp_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_PropertyDetailsEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_PropertyRValuesEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_PropertyTaxDetailsEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_ProposedRValuesEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module.Property_RValues;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module.Property_TaxDetails;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module.Proposed_RValues;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingPropertyDetails_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingPropertyRvalues_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingPropertyTaxDetails_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingProposedRvalues_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository.Property_RValuesRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository.Property_TaxDetailsRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository.Proposed_RValuesRepository;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyDetails_Service.PropertyDetails_Service;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyNumberGenerator_Service.UniqueIdGenerator;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.UnitBuiltUp_Service.UnitBuiltUp_Service;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.UnitDetails_Service.UnitDetails_Service;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingPropertyDetails_Service.AfterHearingPropertyDetails_Service;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingUnitBuiltupDetails_Service.AfterHearingUnitBuiltupDetails_Service;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingUnitDetails_Service.AfterHearingUnitDetails_Service;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AfterHearingPropertyManagement_MasterMasterServiceImpl implements AfterHearingPropertyManagement_MasterService {
    private final PropertyDetails_Service propertyDetails_Service;
    private final UnitDetails_Service unitDetails_Service;
    private final UnitBuiltUp_Service unitBuiltUp_Service;
    private final AfterHearingPropertyDetails_Service afterPropertyDetailsService;
    private final AfterHearingUnitDetails_Service afterUnitDetailsService;
    private final AfterHearingUnitBuiltupDetails_Service afterUnitBuiltupService;
    private final AfterHearingPropertyDetails_MasterRepository propertyDetailsRepository;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final Proposed_RValuesRepository proposed_rValuesRepository;
    private final Property_TaxDetailsRepository property_taxDetailsRepository;
    private final Property_RValuesRepository property_rValuesRepository;
    private final AfterHearingPropertyRvalues_MasterRepository afterHearingPropertyRvalues_masterRepository;
    private final AfterHearingPropertyTaxDetails_MasterRepository afterHearingPropertyTaxDetails_masterRepository;
    private final AfterHearingProposedRvalues_MasterRepository afterHearingProposedRvalues_masterRepository;


    // ----------------------------------------------------------------------
    // CREATE COMPLETE PROPERTY
    // ----------------------------------------------------------------------
    @Transactional
    public AfterHearingCompleteProperty_Dto createCompleteProperty(AfterHearingCompleteProperty_Dto dto) {
        try {
            PropertyDetails_Dto propertyDetailsDto = dto.getPropertyDetails();

            if (propertyDetailsDto == null || propertyDetailsDto.getPdNewpropertynoVc() == null) {
                throw new IllegalArgumentException("Property details or property number cannot be null");
            }

            String newPropertyNo = propertyDetailsDto.getPdNewpropertynoVc();
            System.out.println("🟢 Starting After Hearing record creation for property: " + newPropertyNo);

            // -------------------------------------------------------------
            // 1️⃣ Fallback: If no data updated, load from main property table
            // -------------------------------------------------------------
            if (isPropertyDetailsEmpty(propertyDetailsDto)) {
                System.out.println("⚠️ No updated property data provided — loading existing property from main repository");
                propertyDetailsDto = propertyDetails_Service.findPropertyDetailsByNewPropertyNo(newPropertyNo);
                dto.setPropertyDetails(propertyDetailsDto);
            }

            // -------------------------------------------------------------
            // 2️⃣ Create After Hearing Property record
            // -------------------------------------------------------------
            PropertyDetails_Dto savedProperty = afterPropertyDetailsService.createAfterHearingProperty(propertyDetailsDto);

            // -------------------------------------------------------------
            // 3️⃣ Handle Unit Details (fallback to main repo if empty)
            // -------------------------------------------------------------
            List<UnitDetails_Dto> unitDetails = dto.getUnitDetails();
            if (unitDetails == null || unitDetails.isEmpty()) {
                System.out.println("⚠️ No updated unit details found — loading units from main repository");
                unitDetails = unitDetails_Service.getAllUnitsByProperty(newPropertyNo);
            }

            List<UnitDetails_Dto> createdUnits = new ArrayList<>();
            for (UnitDetails_Dto unitDto : unitDetails) {
                unitDto.setPdNewpropertynoVc(newPropertyNo);
                UnitDetails_Dto createdUnit = afterUnitDetailsService.createUnit(unitDto);

                // ---- Handle Built-Ups ----
                List<UnitBuiltUp_Dto> builtUps = unitDto.getUnitBuiltupUps();
                if (builtUps == null || builtUps.isEmpty()) {
                    builtUps = unitBuiltUp_Service.getBuiltUpsByUnitDetails(
                            newPropertyNo,
                            unitDto.getUdFloorNoVc(),
                            unitDto.getUdUnitNoVc()
                    );
                }

                List<UnitBuiltUp_Dto> createdBuiltUps = new ArrayList<>();
                for (UnitBuiltUp_Dto b : builtUps) {
                    b.setPdNewpropertynoVc(newPropertyNo);
                    b.setUdFloorNoVc(createdUnit.getUdFloorNoVc());
                    b.setUdUnitNoVc(createdUnit.getUdUnitNoVc());
                    createdBuiltUps.add(afterUnitBuiltupService.createBuiltUp(b));
                }

                createdUnit.setUnitBuiltupUps(createdBuiltUps);
                createdUnits.add(createdUnit);
            }

            dto.setPropertyDetails(savedProperty);
            dto.setUnitDetails(createdUnits);

            // -------------------------------------------------------------
            // 4️⃣ Copy Old Property RValues from Main Table to After Hearing
            // -------------------------------------------------------------
            List<Property_RValues> oldRValues = property_rValuesRepository.findAllByPrvPropertyNoVc(newPropertyNo);

            if (oldRValues != null && !oldRValues.isEmpty()) {
                List<AfterHearing_PropertyRValuesEntity> copiedRValues = oldRValues.stream()
                        .map(this::mapRValueToAfterHearing)
                        .collect(Collectors.toList());

                afterHearingPropertyRvalues_masterRepository.saveAll(copiedRValues);
                System.out.println("✅ Copied " + copiedRValues.size() + " RValue records to After Hearing");
            } else {
                System.out.println("⚠️ No RValues found for property: " + newPropertyNo);
            }

            // -------------------------------------------------------------
// 5️⃣ Save Proposed Ratable Values (if provided)
// -------------------------------------------------------------
            if (dto.getProposedRValues() != null && !dto.getProposedRValues().isEmpty()) {
                dto.getProposedRValues().forEach(rv -> {
                    rv.setPrNewPropertyNoVc(newPropertyNo);   // new property no for After Hearing
                    rv.setPrFinalPropNoVc(dto.getPropertyDetails().getPdFinalpropnoVc()); // also set final property number
                });

                afterHearingProposedRvalues_masterRepository.saveAll(convertProposedRValues(dto.getProposedRValues()));
                System.out.println("✅ Proposed Ratable Values saved.");
            }

// -------------------------------------------------------------
// 6️⃣ Save Property Tax Details (if provided)
// -------------------------------------------------------------
            if (dto.getPropertyTaxDetails() != null && !dto.getPropertyTaxDetails().isEmpty()) {
                dto.getPropertyTaxDetails().forEach(tax -> {
                    tax.setPtNewPropertyNoVc(newPropertyNo);   // new property no for After Hearing
                    tax.setPtFinalPropertyNoVc(dto.getPropertyDetails().getPdFinalpropnoVc()); // also set final property number
                });

                afterHearingPropertyTaxDetails_masterRepository.saveAll(convertPropertyTaxDetails(dto.getPropertyTaxDetails()));
                System.out.println("✅ Property Tax Details saved.");
            }

            System.out.println("🎯 After Hearing record creation completed successfully for property: " + newPropertyNo);
            return dto;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to create After Hearing complete property record: " + e.getMessage(), e);
        }
    }



    // ----------------------------------------------------------------------
    // DELETE COMPLETE PROPERTY
    // ----------------------------------------------------------------------

    @Transactional
    public void deleteCompleteProperty(String pdNewpropertynoVc) {
        afterUnitBuiltupService.deleteBuiltUp(pdNewpropertynoVc);
        afterUnitDetailsService.deleteUnit(pdNewpropertynoVc);
        afterPropertyDetailsService.deleteAfterHearingProperty(pdNewpropertynoVc);
    }

    // ----------------------------------------------------------------------
    // SEARCH PROPERTIES
    // ----------------------------------------------------------------------

    @Transactional
    public List<AfterHearing_PropertyDetailsDto> searchAfterHearingProperties(
            String surveyPropertyNo,
            String ownerName,
            Integer wardNo,
            String finalPropertyNo
    ) {
        List<AfterHearing_PropertyDetailsEntity> properties;

        if (finalPropertyNo != null && !finalPropertyNo.isEmpty() && wardNo != null) {
            properties = propertyDetailsRepository.findByPdFinalpropnoVcAndPdWardI(finalPropertyNo, wardNo);

        } else if (finalPropertyNo != null && !finalPropertyNo.isEmpty()) {
            properties = propertyDetailsRepository.findByPdFinalpropnoVcContainingIgnoreCase(finalPropertyNo);

        } else if (surveyPropertyNo != null && !surveyPropertyNo.isEmpty() && wardNo != null) {
            properties = propertyDetailsRepository.findByPdSurypropnoVcAndPdWardI(surveyPropertyNo, wardNo)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());

        } else if (ownerName != null && !ownerName.isEmpty() && wardNo != null) {
            properties = propertyDetailsRepository.findByPdOwnernameVcContainingIgnoreCaseAndPdWardI(ownerName, wardNo);

        } else if (ownerName != null && !ownerName.isEmpty()) {
            properties = propertyDetailsRepository.findByPdOwnernameVcContainingIgnoreCase(ownerName);

        } else if (wardNo != null) {
            properties = propertyDetailsRepository.findByPdWardI(wardNo);

        } else if (surveyPropertyNo != null && !surveyPropertyNo.isEmpty()) {
            properties = propertyDetailsRepository.findByPdSurypropnoVcContainingIgnoreCase(surveyPropertyNo);

        } else {
            properties = Collections.emptyList();
        }

        return properties.stream()
                .map(this::convertPropertyDetailsToDto)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------------
    // FETCH COMPLETE PROPERTY BY SURVEY NO
    // ----------------------------------------------------------------------

//    @Transactional
//    public AfterHearingCompleteProperty_Dto getCompletePropertyBySurveyNumber(String surveyNo) {
//        AfterHearing_PropertyDetailsDto property = propertyDetailsService.getPropertyBySurveyNumber(surveyNo);
//        if (property == null) {
//            throw new EntityNotFoundException("After Hearing Property not found for survey number: " + surveyNo);
//        }
//
//        List<AfterHearing_UnitDetailsDto> unitDetails =
//                unitDetailsService.getAllUnitsByProperty(property.getNewPropertyNo());
//        unitDetails.forEach(unit -> {
//            List<AfterHearing_UnitBuiltupDto> builtUps =
//                    unitBuiltupService.getBuiltUpsByUnitDetails(
//                            unit.getPdNewpropertynoVc(), unit.getUdFloornoVc(), unit.getUdUnitnoVc());
//            unit.setUnitBuiltups(builtUps);
//        });
//
//        AfterHearing_CompletePropertyDto complete = new AfterHearing_CompletePropertyDto();
//        complete.setPropertyDetails(property);
//        complete.setUnitDetails(unitDetails);
//        return complete;
//    }

    // ----------------------------------------------------------------------
    // FETCH COMPLETE PROPERTY BY NEW PROPERTY NO
    // ----------------------------------------------------------------------

    @Transactional
    public AfterHearingCompleteProperty_Dto getCompletePropertyByNewPropertyNo(String newPropertyNo) {
        try {
            // 1️⃣ Fetch Property Details using the main service method
            PropertyDetails_Dto property =
                    propertyDetails_Service.findPropertyDetailsByNewPropertyNo(newPropertyNo);

            // 2️⃣ Fetch Unit Details (reusing your existing method)
            List<UnitDetails_Dto> unitDetails =
                    unitDetails_Service.getAllUnitsByProperty(property.getPdNewpropertynoVc());

            // 3️⃣ For each Unit, fetch Built-up details (reuse your existing method)
            unitDetails.forEach(unit -> {
                List<UnitBuiltUp_Dto> builtUps = unitBuiltUp_Service.getBuiltUpsByUnitDetails(
                        unit.getPdNewpropertynoVc(),
                        unit.getUdFloorNoVc(),
                        unit.getUdUnitNoVc()
                );
                unit.setUnitBuiltupUps(builtUps);
            });

            // 4️⃣ Fetch Property Tax Details (same as your original)
            List<Property_TaxDetails> taxEntities =
                    property_taxDetailsRepository.findAllByPtFinalPropertyNoVc(property.getPdFinalpropnoVc());

            List<AfterHearing_PropertyTaxDetailsDto> taxDetails = taxEntities.stream()
                    .map(this::convertTaxEntityToDto)
                    .collect(Collectors.toList());

            // 5️⃣ Fetch Proposed Ratable Values (use New Property No — NOT Final Property No)
            List<Proposed_RValues> rvEntities =
                    proposed_rValuesRepository.findByPrNewPropertyNoVc(property.getPdNewpropertynoVc());

            List<AfterHearing_ProposedRValuesDto> proposedRvalues = rvEntities.stream()
                    .map(this::convertToProposedRvDto)
                    .collect(Collectors.toList());

            // 6️⃣ Build Complete DTO
            AfterHearingCompleteProperty_Dto complete = new AfterHearingCompleteProperty_Dto();
            complete.setPropertyDetails(property);
            complete.setUnitDetails(unitDetails);
            complete.setPropertyTaxDetails(taxDetails);
            complete.setProposedRValues(proposedRvalues);

            return complete;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching After Hearing property for newPropertyNo: " + newPropertyNo, e);
        }
    }

    // ----------------------------------------------------------------------
    // UPDATE COMPLETE PROPERTY
    // ----------------------------------------------------------------------
//    @Override
//    @Transactional
//    public AfterHearing_CompletePropertyDto updateCompleteProperty(AfterHearing_CompletePropertyDto dto) {
//        AfterHearing_PropertyDetailsDto property = dto.getPropertyDetails();
//        propertyDetailsService.updateAfterHearingProperty(property);
//
//        for (AfterHearing_UnitDetailsDto unitDto : dto.getUnitDetails()) {
//            unitDetailsService.updateOrCreateUnit(unitDto, property);
//            for (AfterHearing_UnitBuiltupDto builtupDto : unitDto.getUnitBuiltups()) {
//                builtupDto.setPdNewpropertynoVc(unitDto.getPdNewpropertynoVc());
//                builtupDto.setUdFloornoVc(unitDto.getUdFloornoVc());
//                builtupDto.setUdUnitnoVc(unitDto.getUdUnitnoVc());
//                unitBuiltupService.updateOrCreateUnitBuiltUp(builtupDto);
//            }
//        }
//        return dto;
//    }

    // ----------------------------------------------------------------------
    // UPLOAD PROPERTY IMAGES
    // ----------------------------------------------------------------------
//    @Override
//    public void uploadPropertyImage(String newPropertyNo, MultipartFile image) {
//        propertyDetailsService.uploadAfterHearingPropertyImage(newPropertyNo, image);
//    }
//
//    @Override
//    public void uploadCadImage(String newPropertyNo, MultipartFile cadImage) {
//        propertyDetailsService.uploadAfterHearingCadImage(newPropertyNo, cadImage);
//    }

    // ----------------------------------------------------------------------
    // UTILITY MAPPER
    // ----------------------------------------------------------------------
    private AfterHearing_PropertyDetailsDto convertPropertyDetailsToDto(AfterHearing_PropertyDetailsEntity entity) {
        AfterHearing_PropertyDetailsDto dto = new AfterHearing_PropertyDetailsDto();
        dto.setPdNewpropertynoVc(entity.getPdNewpropertynoVc());
        dto.setPdSurypropnoVc(entity.getPdSurypropnoVc());
        dto.setPdOwnernameVc(entity.getPdOwnernameVc());
        dto.setPdZoneI(String.valueOf(entity.getPdZoneI()));
        dto.setPdWardI(String.valueOf(entity.getPdWardI()));
        dto.setPdFinalpropnoVc(entity.getPdFinalpropnoVc());
        dto.setPdOldpropnoVc(entity.getPdOldpropnoVc());
        dto.setPdLayoutnameVc(entity.getPdLayoutnameVc());
        dto.setPdLayoutnoVc(entity.getPdLayoutnoVc());
        dto.setPdPropertyaddressVc(entity.getPdPropertyaddressVc());

        if (entity.getCreatedAt() != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH-mm-ss, dd-MM-yyyy");
            dto.setCreateddateVc(entity.getCreatedAt().format(fmt));
        }

        return dto;
    }

    // ✅ Convert Entity → DTO
    // ✅ Converts Proposed_RValues (main assessment entity) → AfterHearing_ProposedRValuesDto
    private AfterHearing_ProposedRValuesDto convertToProposedRvDto(Proposed_RValues entity) {
        AfterHearing_ProposedRValuesDto dto = new AfterHearing_ProposedRValuesDto();

        dto.setPrNewPropertyNoVc(entity.getPrNewPropertyNoVc());
        dto.setPrFinalPropNoVc(entity.getPrFinalPropNoVc());
        dto.setPrResidentialFl(entity.getPrResidentialFl());
        dto.setPrCommercialFl(entity.getPrCommercialFl());
        dto.setPrIndustrialFl(entity.getPrIndustrialFl());
        dto.setPrReligiousFl(entity.getPrReligiousFl());
        dto.setPrEducationalFl(entity.getPrEducationalFl());
        dto.setPrMobileTowerFl(entity.getPrMobileTowerFl());
        dto.setPrElectricSubstationFl(entity.getPrElectricSubstationFl());
        dto.setPrGovernmentFl(entity.getPrGovernmentFl());
        dto.setPrResidentialOpenPlotFl(entity.getPrResidentialOpenPlotFl());
        dto.setPrCommercialOpenPlotFl(entity.getPrCommercialOpenPlotFl());
        dto.setPrIndustrialOpenPlotFl(entity.getPrIndustrialOpenPlotFl());
        dto.setPrReligiousOpenPlotFl(entity.getPrReligiousOpenPlotFl());
        dto.setPrEducationAndLegalInstituteOpenPlotFl(entity.getPrEducationAndLegalInstituteOpenPlotFl());
        dto.setPrGovernmentOpenPlotFl(entity.getPrGovernmentOpenPlotFl());
        dto.setPrTotalRatableValueFl(entity.getPrTotalRatableValueFl());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    // ✅ Convert DTO → Entity
    private AfterHearing_ProposedRValuesEntity convertToProposedRvEntity(AfterHearing_ProposedRValuesDto dto) {
        AfterHearing_ProposedRValuesEntity entity = new AfterHearing_ProposedRValuesEntity();

        entity.setPrNewPropertyNoVc(dto.getPrNewPropertyNoVc());
        entity.setPrFinalPropNoVc(dto.getPrFinalPropNoVc());
        entity.setPrResidentialFl(dto.getPrResidentialFl());
        entity.setPrCommercialFl(dto.getPrCommercialFl());
        entity.setPrIndustrialFl(dto.getPrIndustrialFl());
        entity.setPrReligiousFl(dto.getPrReligiousFl());
        entity.setPrEducationalFl(dto.getPrEducationalFl());
        entity.setPrMobileTowerFl(dto.getPrMobileTowerFl());
        entity.setPrElectricSubstationFl(dto.getPrElectricSubstationFl());
        entity.setPrGovernmentFl(dto.getPrGovernmentFl());
        entity.setPrResidentialOpenPlotFl(dto.getPrResidentialOpenPlotFl());
        entity.setPrCommercialOpenPlotFl(dto.getPrCommercialOpenPlotFl());
        entity.setPrIndustrialOpenPlotFl(dto.getPrIndustrialOpenPlotFl());
        entity.setPrReligiousOpenPlotFl(dto.getPrReligiousOpenPlotFl());
        entity.setPrEducationAndLegalInstituteOpenPlotFl(dto.getPrEducationAndLegalInstituteOpenPlotFl());
        entity.setPrGovernmentOpenPlotFl(dto.getPrGovernmentOpenPlotFl());
        entity.setPrTotalRatableValueFl(dto.getPrTotalRatableValueFl());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        return entity;
    }

    // ✅ Converts Property_TaxDetails (main assessment entity) → AfterHearing_PropertyTaxDetailsDto
    // ✅ Converts Property_TaxDetails (main entity) → AfterHearing_PropertyTaxDetailsDto
    private AfterHearing_PropertyTaxDetailsDto convertTaxEntityToDto(Property_TaxDetails entity) {
        AfterHearing_PropertyTaxDetailsDto dto = new AfterHearing_PropertyTaxDetailsDto();

        dto.setPtNewPropertyNoVc(entity.getPtNewPropertyNoVc());
        dto.setPtFinalPropertyNoVc(entity.getPtFinalPropertyNoVc());
        dto.setPtPropertyTaxFl(entity.getPtPropertyTaxFl());
        dto.setPtEgcTaxFl(entity.getPtEgcTaxFl());
        dto.setPtTreeTaxFl(entity.getPtTreeTaxFl());
        dto.setPtCleanTaxFl(entity.getPtCleanTaxFl());
        dto.setPtFireTaxFl(entity.getPtFireTaxFl());
        dto.setPtLightTaxFl(entity.getPtLightTaxFl());
        dto.setPtUserChargesFl(entity.getPtUserChargesFl());
        dto.setPtEnvironmentTaxFl(entity.getPtEnvironmentTaxFl());
        dto.setPtEduResTaxFl(entity.getPtEduResTaxFl());
        dto.setPtEduNonResTaxFl(entity.getPtEduNonResTaxFl());
        dto.setPtEduTaxFl(entity.getPtEduTaxFl());
        dto.setPtWaterTaxFl(entity.getPtWaterTaxFl());
        dto.setPtSewerageTaxFl(entity.getPtSewerageTaxFl());
        dto.setPtSewerageBenefitTaxFl(entity.getPtSewerageBenefitTaxFl());
        dto.setPtWaterBenefitTaxFl(entity.getPtWaterBenefitTaxFl());
        dto.setPtStreetTaxFl(entity.getPtStreetTaxFl());
        dto.setPtSpecialConservancyTaxFl(entity.getPtSpecialConservancyTaxFl());
        dto.setPtMunicipalEduTaxFl(entity.getPtMunicipalEduTaxFl());
        dto.setPtSpecialEduTaxFl(entity.getPtSpecialEduTaxFl());
        dto.setPtServiceChargesFl(entity.getPtServiceChargesFl());
        dto.setPtMiscellaneousChargesFl(entity.getPtMiscellaneousChargesFl());

        // Reserved taxes 1–25
        dto.setPtTax1Fl(entity.getPtTax1Fl());
        dto.setPtTax2Fl(entity.getPtTax2Fl());
        dto.setPtTax3Fl(entity.getPtTax3Fl());
        dto.setPtTax4Fl(entity.getPtTax4Fl());
        dto.setPtTax5Fl(entity.getPtTax5Fl());
        dto.setPtTax6Fl(entity.getPtTax6Fl());
        dto.setPtTax7Fl(entity.getPtTax7Fl());
        dto.setPtTax8Fl(entity.getPtTax8Fl());
        dto.setPtTax9Fl(entity.getPtTax9Fl());
        dto.setPtTax10Fl(entity.getPtTax10Fl());
        dto.setPtTax11Fl(entity.getPtTax11Fl());
        dto.setPtTax12Fl(entity.getPtTax12Fl());
        dto.setPtTax13Fl(entity.getPtTax13Fl());
        dto.setPtTax14Fl(entity.getPtTax14Fl());
        dto.setPtTax15Fl(entity.getPtTax15Fl());
        dto.setPtTax16Fl(entity.getPtTax16Fl());
        dto.setPtTax17Fl(entity.getPtTax17Fl());
        dto.setPtTax18Fl(entity.getPtTax18Fl());
        dto.setPtTax19Fl(entity.getPtTax19Fl());
        dto.setPtTax20Fl(entity.getPtTax20Fl());
        dto.setPtTax21Fl(entity.getPtTax21Fl());
        dto.setPtTax22Fl(entity.getPtTax22Fl());
        dto.setPtTax23Fl(entity.getPtTax23Fl());
        dto.setPtTax24Fl(entity.getPtTax24Fl());
        dto.setPtTax25Fl(entity.getPtTax25Fl());

        dto.setPtFinalTaxFl(entity.getPtFinalTaxFl());
        dto.setPtFinalYearVc(entity.getPtFinalYearVc());
        dto.setPtFinalRvFl(entity.getPtFinalRvFl());
        dto.setPtDummyVc(entity.getPtDummyVc());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }


    private AfterHearing_PropertyTaxDetailsEntity convertToTaxEntity(AfterHearing_PropertyTaxDetailsDto dto) {
        AfterHearing_PropertyTaxDetailsEntity entity = new AfterHearing_PropertyTaxDetailsEntity();

        entity.setPtNewPropertyNoVc(dto.getPtNewPropertyNoVc());
        entity.setPtFinalPropertyNoVc(dto.getPtFinalPropertyNoVc());
        entity.setPtPropertyTaxFl(dto.getPtPropertyTaxFl());
        entity.setPtEgcTaxFl(dto.getPtEgcTaxFl());
        entity.setPtTreeTaxFl(dto.getPtTreeTaxFl());
        entity.setPtCleanTaxFl(dto.getPtCleanTaxFl());
        entity.setPtFireTaxFl(dto.getPtFireTaxFl());
        entity.setPtLightTaxFl(dto.getPtLightTaxFl());
        entity.setPtUserChargesFl(dto.getPtUserChargesFl());
        entity.setPtEnvironmentTaxFl(dto.getPtEnvironmentTaxFl());
        entity.setPtEduResTaxFl(dto.getPtEduResTaxFl());
        entity.setPtEduNonResTaxFl(dto.getPtEduNonResTaxFl());
        entity.setPtEduTaxFl(dto.getPtEduTaxFl());
        entity.setPtWaterTaxFl(dto.getPtWaterTaxFl());
        entity.setPtSewerageTaxFl(dto.getPtSewerageTaxFl());
        entity.setPtSewerageBenefitTaxFl(dto.getPtSewerageBenefitTaxFl());
        entity.setPtWaterBenefitTaxFl(dto.getPtWaterBenefitTaxFl());
        entity.setPtStreetTaxFl(dto.getPtStreetTaxFl());
        entity.setPtSpecialConservancyTaxFl(dto.getPtSpecialConservancyTaxFl());
        entity.setPtMunicipalEduTaxFl(dto.getPtMunicipalEduTaxFl());
        entity.setPtSpecialEduTaxFl(dto.getPtSpecialEduTaxFl());
        entity.setPtServiceChargesFl(dto.getPtServiceChargesFl());
        entity.setPtMiscellaneousChargesFl(dto.getPtMiscellaneousChargesFl());
        entity.setPtTax1Fl(dto.getPtTax1Fl());
        entity.setPtTax2Fl(dto.getPtTax2Fl());
        entity.setPtTax3Fl(dto.getPtTax3Fl());
        entity.setPtTax4Fl(dto.getPtTax4Fl());
        entity.setPtTax5Fl(dto.getPtTax5Fl());
        entity.setPtTax6Fl(dto.getPtTax6Fl());
        entity.setPtTax7Fl(dto.getPtTax7Fl());
        entity.setPtTax8Fl(dto.getPtTax8Fl());
        entity.setPtTax9Fl(dto.getPtTax9Fl());
        entity.setPtTax10Fl(dto.getPtTax10Fl());
        entity.setPtTax11Fl(dto.getPtTax11Fl());
        entity.setPtTax12Fl(dto.getPtTax12Fl());
        entity.setPtTax13Fl(dto.getPtTax13Fl());
        entity.setPtTax14Fl(dto.getPtTax14Fl());
        entity.setPtTax15Fl(dto.getPtTax15Fl());
        entity.setPtTax16Fl(dto.getPtTax16Fl());
        entity.setPtTax17Fl(dto.getPtTax17Fl());
        entity.setPtTax18Fl(dto.getPtTax18Fl());
        entity.setPtTax19Fl(dto.getPtTax19Fl());
        entity.setPtTax20Fl(dto.getPtTax20Fl());
        entity.setPtTax21Fl(dto.getPtTax21Fl());
        entity.setPtTax22Fl(dto.getPtTax22Fl());
        entity.setPtTax23Fl(dto.getPtTax23Fl());
        entity.setPtTax24Fl(dto.getPtTax24Fl());
        entity.setPtTax25Fl(dto.getPtTax25Fl());
        entity.setPtFinalTaxFl(dto.getPtFinalTaxFl());
        entity.setPtFinalYearVc(dto.getPtFinalYearVc());
        entity.setPtFinalRvFl(dto.getPtFinalRvFl());
        entity.setPtDummyVc(dto.getPtDummyVc());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        return entity;
    }
    private boolean isPropertyDetailsEmpty(PropertyDetails_Dto dto) {
        return dto.getPdZoneI() == null &&
                dto.getPdWardI() == null &&
                (dto.getPdOwnernameVc() == null || dto.getPdOwnernameVc().isEmpty()) &&
                (dto.getPdLayoutnameVc() == null || dto.getPdLayoutnameVc().isEmpty());
    }
    private AfterHearing_PropertyRValuesEntity mapRValueToAfterHearing(Property_RValues r) {
        AfterHearing_PropertyRValuesEntity ar = new AfterHearing_PropertyRValuesEntity();
        ar.setPrvPropertyNoVc(r.getPrvPropertyNoVc());
        ar.setPrvUnitNoVc(r.getPrvUnitNoVc());
        ar.setPrvFinalPropNoVc(r.getPrvFinalPropNoVc());
        ar.setPrvRatePerSqMFl(r.getPrvRatePerSqMFl());
        ar.setPrvRentalValAsPerRateFl(r.getPrvRentalValAsPerRateFl());
        ar.setPrvDepreciationRateFl(r.getPrvDepreciationRateFl());
        ar.setPrvDepreciationAmountFl(r.getPrvDepreciationAmountFl());
        ar.setPrvAmountAfterDepreciationFl(r.getPrvAmountAfterDepreciationFl());
        ar.setPrvMaintenanceRepairsFl(r.getPrvMaintenanceRepairsFl());
        ar.setPrvTaxableValueByRateFl(r.getPrvTaxableValueByRateFl());
        ar.setPrvTenantNameVc(r.getPrvTenantNameVc());
        ar.setPrvActualMonthlyRentFl(r.getPrvActualMonthlyRentFl());
        ar.setPrvActualAnnualRentFl(r.getPrvActualAnnualRentFl());
        ar.setPrvMaintenanceRepairsRentFl(r.getPrvMaintenanceRepairsRentFl());
        ar.setPrvTaxableValueByRentFl(r.getPrvTaxableValueByRentFl());
        ar.setPrvTaxableValueConsideredFl(r.getPrvTaxableValueConsideredFl());
        ar.setPrvFinancialYearVc(r.getPrvFinancialYearVc());
        ar.setPrvAgeFactorVc(r.getPrvAgeFactorVc());
        ar.setCreatedAt(LocalDateTime.now());
        ar.setUpdatedAt(LocalDateTime.now());
        return ar;
    }

    private List<AfterHearing_ProposedRValuesEntity> convertProposedRValues(List<AfterHearing_ProposedRValuesDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos.stream().map(dto -> {
            AfterHearing_ProposedRValuesEntity entity = new AfterHearing_ProposedRValuesEntity();

            entity.setPrNewPropertyNoVc(dto.getPrNewPropertyNoVc());
            entity.setPrFinalPropNoVc(dto.getPrFinalPropNoVc());
            entity.setPrResidentialFl(dto.getPrResidentialFl());
            entity.setPrCommercialFl(dto.getPrCommercialFl());
            entity.setPrIndustrialFl(dto.getPrIndustrialFl());
            entity.setPrReligiousFl(dto.getPrReligiousFl());
            entity.setPrEducationalFl(dto.getPrEducationalFl());
            entity.setPrMobileTowerFl(dto.getPrMobileTowerFl());
            entity.setPrElectricSubstationFl(dto.getPrElectricSubstationFl());
            entity.setPrGovernmentFl(dto.getPrGovernmentFl());
            entity.setPrResidentialOpenPlotFl(dto.getPrResidentialOpenPlotFl());
            entity.setPrCommercialOpenPlotFl(dto.getPrCommercialOpenPlotFl());
            entity.setPrIndustrialOpenPlotFl(dto.getPrIndustrialOpenPlotFl());
            entity.setPrReligiousOpenPlotFl(dto.getPrReligiousOpenPlotFl());
            entity.setPrEducationAndLegalInstituteOpenPlotFl(dto.getPrEducationAndLegalInstituteOpenPlotFl());
            entity.setPrGovernmentOpenPlotFl(dto.getPrGovernmentOpenPlotFl());
            entity.setPrTotalRatableValueFl(dto.getPrTotalRatableValueFl());
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());

            return entity;
        }).collect(Collectors.toList());
    }

    private List<AfterHearing_PropertyTaxDetailsEntity> convertPropertyTaxDetails(List<AfterHearing_PropertyTaxDetailsDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos.stream().map(dto -> {
            AfterHearing_PropertyTaxDetailsEntity entity = new AfterHearing_PropertyTaxDetailsEntity();

            entity.setPtNewPropertyNoVc(dto.getPtNewPropertyNoVc());
            entity.setPtFinalPropertyNoVc(dto.getPtFinalPropertyNoVc());
            entity.setPtPropertyTaxFl(dto.getPtPropertyTaxFl());
            entity.setPtEgcTaxFl(dto.getPtEgcTaxFl());
            entity.setPtTreeTaxFl(dto.getPtTreeTaxFl());
            entity.setPtCleanTaxFl(dto.getPtCleanTaxFl());
            entity.setPtFireTaxFl(dto.getPtFireTaxFl());
            entity.setPtLightTaxFl(dto.getPtLightTaxFl());
            entity.setPtUserChargesFl(dto.getPtUserChargesFl());
            entity.setPtEnvironmentTaxFl(dto.getPtEnvironmentTaxFl());
            entity.setPtEduResTaxFl(dto.getPtEduResTaxFl());
            entity.setPtEduNonResTaxFl(dto.getPtEduNonResTaxFl());
            entity.setPtEduTaxFl(dto.getPtEduTaxFl());
            entity.setPtWaterTaxFl(dto.getPtWaterTaxFl());
            entity.setPtSewerageTaxFl(dto.getPtSewerageTaxFl());
            entity.setPtSewerageBenefitTaxFl(dto.getPtSewerageBenefitTaxFl());
            entity.setPtWaterBenefitTaxFl(dto.getPtWaterBenefitTaxFl());
            entity.setPtStreetTaxFl(dto.getPtStreetTaxFl());
            entity.setPtSpecialConservancyTaxFl(dto.getPtSpecialConservancyTaxFl());
            entity.setPtMunicipalEduTaxFl(dto.getPtMunicipalEduTaxFl());
            entity.setPtSpecialEduTaxFl(dto.getPtSpecialEduTaxFl());
            entity.setPtServiceChargesFl(dto.getPtServiceChargesFl());
            entity.setPtMiscellaneousChargesFl(dto.getPtMiscellaneousChargesFl());

            // Reserved fields (1–25)
            entity.setPtTax1Fl(dto.getPtTax1Fl());
            entity.setPtTax2Fl(dto.getPtTax2Fl());
            entity.setPtTax3Fl(dto.getPtTax3Fl());
            entity.setPtTax4Fl(dto.getPtTax4Fl());
            entity.setPtTax5Fl(dto.getPtTax5Fl());
            entity.setPtTax6Fl(dto.getPtTax6Fl());
            entity.setPtTax7Fl(dto.getPtTax7Fl());
            entity.setPtTax8Fl(dto.getPtTax8Fl());
            entity.setPtTax9Fl(dto.getPtTax9Fl());
            entity.setPtTax10Fl(dto.getPtTax10Fl());
            entity.setPtTax11Fl(dto.getPtTax11Fl());
            entity.setPtTax12Fl(dto.getPtTax12Fl());
            entity.setPtTax13Fl(dto.getPtTax13Fl());
            entity.setPtTax14Fl(dto.getPtTax14Fl());
            entity.setPtTax15Fl(dto.getPtTax15Fl());
            entity.setPtTax16Fl(dto.getPtTax16Fl());
            entity.setPtTax17Fl(dto.getPtTax17Fl());
            entity.setPtTax18Fl(dto.getPtTax18Fl());
            entity.setPtTax19Fl(dto.getPtTax19Fl());
            entity.setPtTax20Fl(dto.getPtTax20Fl());
            entity.setPtTax21Fl(dto.getPtTax21Fl());
            entity.setPtTax22Fl(dto.getPtTax22Fl());
            entity.setPtTax23Fl(dto.getPtTax23Fl());
            entity.setPtTax24Fl(dto.getPtTax24Fl());
            entity.setPtTax25Fl(dto.getPtTax25Fl());

            entity.setPtFinalTaxFl(dto.getPtFinalTaxFl());
            entity.setPtFinalYearVc(dto.getPtFinalYearVc());
            entity.setPtFinalRvFl(dto.getPtFinalRvFl());
            entity.setPtDummyVc(dto.getPtDummyVc());
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());

            return entity;
        }).collect(Collectors.toList());
    }


}