package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.*;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.PropertyUnitDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ProposedRatableValueDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitBuiltUp_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.*;
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
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearing_TaxComputationalService.AfterHearing_TaxCalculationService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentRealtime.TaxAssessment_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final TaxAssessment_MasterService taxAssessment_masterService;
    private final AfterHearing_TaxCalculationService afterHearingTaxCalculationService;

    private static final Logger log =
            LoggerFactory.getLogger(AfterHearingPropertyManagement_MasterMasterServiceImpl.class);
    // ----------------------------------------------------------------------
    // CREATE COMPLETE PROPERTY
    // ----------------------------------------------------------------------
    @Transactional
    public AfterHearingCompleteProperty_Dto createCompleteProperty(
            String newPropertyNo,
            AfterHearingCompleteProperty_Dto dto,
            boolean byRv,
            boolean byAssessment) {

        try {
            PropertyDetails_Dto propertyDetailsDto = dto.getPropertyDetails();

            if (propertyDetailsDto == null || propertyDetailsDto.getPdNewpropertynoVc() == null) {
                throw new IllegalArgumentException("Property details or property number cannot be null");
            }

            if (byAssessment) {
                log.info("[AFTER_HEARING][FULL_ASSESSMENT] Starting reassessment for property: " + newPropertyNo);
                handleFullAssessment(newPropertyNo, dto, propertyDetailsDto);
            }
            else if (byRv) {
                log.info("[AFTER_HEARING][RV_CHANGE] Recalculating taxes by new RV for property: " + newPropertyNo);
                handleRvChange(newPropertyNo, dto, propertyDetailsDto);
            }
            else {
                log.info("[AFTER_HEARING][RETAINED] Copying data as-is for property: " + newPropertyNo);
                handleRetained(newPropertyNo, dto, propertyDetailsDto);
            }

            log.info("[AFTER_HEARING] ✅ Completed processing for property: " + newPropertyNo);
            return dto;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to process After Hearing property: " + e.getMessage(), e);
        }
    }

    private void handleFullAssessment(
            String newPropertyNo,
            AfterHearingCompleteProperty_Dto dto,
            PropertyDetails_Dto propertyDetailsDto) {

        String finalPropertyNo = dto.getPropertyDetails().getPdFinalpropnoVc();
        // 🔹 Step 1: Save Property into after-hearing
        PropertyDetails_Dto savedProperty = afterPropertyDetailsService.createAfterHearingProperty(propertyDetailsDto);

        // 🔹 Step 2: Handle Units + BuiltUps
        List<UnitDetails_Dto> unitDetails = dto.getUnitDetails();
        if (unitDetails == null || unitDetails.isEmpty()) {
            log.info("⚠️ No updated unit details found — loading from main property");
            unitDetails = unitDetails_Service.getAllUnitsByProperty(newPropertyNo);
        }

        List<UnitDetails_Dto> createdUnits = new ArrayList<>();
        for (UnitDetails_Dto unit : unitDetails) {
            unit.setPdNewpropertynoVc(newPropertyNo);
            UnitDetails_Dto createdUnit = afterUnitDetailsService.createUnit(unit);

            List<UnitBuiltUp_Dto> builtUps = unit.getUnitBuiltupUps();
            if (builtUps == null || builtUps.isEmpty()) {
                builtUps = unitBuiltUp_Service.getBuiltUpsByUnitDetails(
                        newPropertyNo,
                        unit.getUdFloorNoVc(),
                        unit.getUdUnitNoVc()
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

        dto.setUnitDetails(createdUnits);

        // Copy existing unit-wise RValues into after-hearing (RV-only change retains RValues)
        List<Property_RValues> oldRValues = property_rValuesRepository.findAllByPrvPropertyNoVc(newPropertyNo);
        if (oldRValues != null && !oldRValues.isEmpty()) {
            afterHearingPropertyRvalues_masterRepository.saveAll(
                    oldRValues.stream().map(this::mapRValueToAfterHearing).collect(Collectors.toList())
            );
        }

        // 🔹 Step 3: Run assessment
        AssessmentResultsDto result = taxAssessment_masterService.performAssessment(newPropertyNo, true);

        System.out.println("Perform assessment result \n"+result);

        // 🔹 Step 4: Save RValues, Proposed RVs, Tax Details
        if (result.getProposedRatableValues() != null){
            AfterHearing_ProposedRValuesEntity proposedRValuesEntity = convertAssessmentResultsOfProposedRvaluesToEntity(result.getProposedRatableValues(),newPropertyNo, finalPropertyNo);
            afterHearingProposedRvalues_masterRepository.save(proposedRValuesEntity);
        }
        if (result.getTaxKeyValueMap() != null){
            AfterHearing_PropertyTaxDetailsEntity propertyTaxDetailsEntity = convertAssessmentResultsOfTaxToEntity(result.getTaxKeyValueMap(),newPropertyNo,dto.getPropertyDetails().getPdFinalpropnoVc());
            afterHearingPropertyTaxDetails_masterRepository.save(propertyTaxDetailsEntity);
        }
        if (result.getUnitDetails() != null){
            List<AfterHearing_PropertyRValuesEntity> PropertyRvalues = convertAssessmentResultsOfPropertyRvaluesToEntity(result.getUnitDetails(),newPropertyNo,dto.getPropertyDetails().getPdFinalpropnoVc());
            afterHearingPropertyRvalues_masterRepository.saveAll(PropertyRvalues);
        }

        dto.setPropertyDetails(savedProperty);
        dto.setByAssessment(true);
        dto.setHearingStatus("CHANGED");
        dto.setChangeType("ASSESSMENT");

        System.out.println("method ended successfully"+dto);

        log.info("[AFTER_HEARING][FULL_ASSESSMENT] ✅ Completed reassessment for " + newPropertyNo);
    }

    private void  handleRvChange(
            String newPropertyNo,
            AfterHearingCompleteProperty_Dto dto,
            PropertyDetails_Dto propertyDetailsDto) {

        // 🔹 Step 1: Copy Property & Units
        PropertyDetails_Dto savedProperty = afterPropertyDetailsService.createAfterHearingProperty(propertyDetailsDto);
        dto.setPropertyDetails(savedProperty);

        List<UnitDetails_Dto> unitDetails = unitDetails_Service.getAllUnitsByProperty(newPropertyNo);
        List<UnitDetails_Dto> createdUnits = new ArrayList<>();

        for (UnitDetails_Dto unit : unitDetails) {
            unit.setPdNewpropertynoVc(newPropertyNo);
            UnitDetails_Dto createdUnit = afterUnitDetailsService.createUnit(unit);

            List<UnitBuiltUp_Dto> builtUps = unitBuiltUp_Service.getBuiltUpsByUnitDetails(
                    newPropertyNo, unit.getUdFloorNoVc(), unit.getUdUnitNoVc()
            );

            List<UnitBuiltUp_Dto> createdBuiltUps = builtUps.stream()
                    .map(b -> {
                        b.setPdNewpropertynoVc(newPropertyNo);
                        b.setUdFloorNoVc(createdUnit.getUdFloorNoVc());
                        b.setUdUnitNoVc(createdUnit.getUdUnitNoVc());
                        return afterUnitBuiltupService.createBuiltUp(b);
                    })
                    .collect(Collectors.toList());

            createdUnit.setUnitBuiltupUps(createdBuiltUps);
            createdUnits.add(createdUnit);
        }

        dto.setUnitDetails(createdUnits);

        // 🔹 Step 2: Recalculate taxes using the new RV
        if (dto.getProposedRValues() == null || dto.getProposedRValues().isEmpty()) {
            throw new IllegalArgumentException("❌ Missing ProposedRValues list in DTO");
        }

        ProposedRatableValueDetailsDto proposed =
                mapToProposedRatableValueDetailsDto(dto.getProposedRValues().get(0));

        System.out.println("proposedrvalues::"+dto.getProposedRValues());
        if (proposed == null)
            throw new IllegalArgumentException("Missing ProposedRatableValueDetailsDto for RV change");

        double propertyTax = afterHearingTaxCalculationService.calculatePropertyTax(proposed);
        double eduCess = afterHearingTaxCalculationService.calculateEducationCess(proposed);
        double egc = afterHearingTaxCalculationService.calculateEgc(proposed);

        // 🧾 Consolidated taxes (Tree, Fire, Light, etc.)
        Map<Long, Double> consolidatedTaxes =
                afterHearingTaxCalculationService.calculateConsolidatedTaxes(proposed.getAggregateFl(), propertyTax);

        // 🧮 Merge all taxes into one map (to be persisted)
        Map<Long, Double> allTaxes = new LinkedHashMap<>();
        allTaxes.put(ReportTaxKeys.PT1, propertyTax);
        allTaxes.put(ReportTaxKeys.EDUC_RES, eduCess); // Education (res+comm) combined
        allTaxes.put(ReportTaxKeys.EGC, egc);
        allTaxes.putAll(consolidatedTaxes);

        double totalTax = allTaxes.values().stream().mapToDouble(Double::doubleValue).sum();

        List<Property_RValues> oldRValues = property_rValuesRepository.findAllByPrvPropertyNoVc(newPropertyNo);
        if (!oldRValues.isEmpty()) {
            afterHearingPropertyRvalues_masterRepository.saveAll(
                    oldRValues.stream().map(this::mapRValueToAfterHearing).collect(Collectors.toList())
            );
        }

        // 🔹 Step 3: Save new Proposed RV
        AfterHearing_ProposedRValuesDto rvDto = mapToAfterHearingProposedRvDto(proposed,newPropertyNo);
        afterHearingProposedRvalues_masterRepository.saveAll(
                convertProposedRValues(Collections.singletonList(rvDto))
        );

        // 🔹 Step 4: Save all Tax Details
        AfterHearing_PropertyTaxDetailsEntity taxEntity =
                convertAssessmentResultsOfTaxToEntity(allTaxes, newPropertyNo, propertyDetailsDto.getPdFinalpropnoVc());
        afterHearingPropertyTaxDetails_masterRepository.save(taxEntity);

        // ✅ Update DTO meta flags
        dto.setByRv(true);
        dto.setByAssessment(false);
        dto.setHearingStatus("CHANGED");
        dto.setChangeType("RV");

        log.info("[AFTER_HEARING][RV_CHANGE] ♻️ Units copied and full tax recalculated for " + newPropertyNo);
    }

    private void handleRetained(
            String newPropertyNo,
            AfterHearingCompleteProperty_Dto dto,
            PropertyDetails_Dto propertyDetailsDto) {

        // 🔹 Step 1: Copy property
        PropertyDetails_Dto savedProperty = afterPropertyDetailsService.createAfterHearingProperty(propertyDetailsDto);
        dto.setPropertyDetails(savedProperty);

        // 🔹 Step 2: Copy units and built-ups
        List<UnitDetails_Dto> unitDetails = unitDetails_Service.getAllUnitsByProperty(newPropertyNo);
        List<UnitDetails_Dto> createdUnits = new ArrayList<>();

        for (UnitDetails_Dto unit : unitDetails) {
            unit.setPdNewpropertynoVc(newPropertyNo);
            UnitDetails_Dto createdUnit = afterUnitDetailsService.createUnit(unit);

            List<UnitBuiltUp_Dto> builtUps = unitBuiltUp_Service.getBuiltUpsByUnitDetails(
                    newPropertyNo,
                    unit.getUdFloorNoVc(),
                    unit.getUdUnitNoVc()
            );

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

        dto.setUnitDetails(createdUnits);

        // 🔹 Step 3: Copy existing RValues, RVs, Tax Details
        List<Property_RValues> oldRValues = property_rValuesRepository.findAllByPrvPropertyNoVc(newPropertyNo);
        if (!oldRValues.isEmpty()) {
            afterHearingPropertyRvalues_masterRepository.saveAll(
                    oldRValues.stream().map(this::mapRValueToAfterHearing).collect(Collectors.toList())
            );
        }

        List<Proposed_RValues> rvList = proposed_rValuesRepository.findByPrNewPropertyNoVc(newPropertyNo);
        if (!rvList.isEmpty()) {
            afterHearingProposedRvalues_masterRepository.saveAll(
                    convertProposedRValues(rvList.stream().map(this::convertToProposedRvDto).collect(Collectors.toList()))
            );
        }

        List<Property_TaxDetails> taxList = property_taxDetailsRepository.findAllByPtFinalPropertyNoVc(
                propertyDetailsDto.getPdFinalpropnoVc()
        );
        if (!taxList.isEmpty()) {
            afterHearingPropertyTaxDetails_masterRepository.saveAll(
                    convertPropertyTaxDetails(taxList.stream().map(this::convertTaxEntityToDto).collect(Collectors.toList()))
            );
        }

        dto.setByAssessment(false);
        dto.setByRv(false);
        dto.setHearingStatus("RETAINED");
        dto.setChangeType("NONE");

        log.info("[AFTER_HEARING][RETAINED] ✅ Property retained with all units and built-ups copied for " + newPropertyNo);
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


    private AfterHearing_PropertyTaxDetailsEntity convertAssessmentResultsOfTaxToEntity(
            Map<Long, Double> taxMap,
            String newPropertyNo,
            String finalPropertyNo
    ) {
        AfterHearing_PropertyTaxDetailsEntity tax = new AfterHearing_PropertyTaxDetailsEntity();

        tax.setPtNewPropertyNoVc(newPropertyNo);
        tax.setPtFinalPropertyNoVc(finalPropertyNo);

        // 🏛️ PROPERTY TAX COMPOSITION

        tax.setPtPropertyTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.PT_PARENT, 0.0)));
        // 📚 EDUCATION + EGC
        tax.setPtEduTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.EDUC_PARENT, 0.0)));
        tax.setPtEduResTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.EDUC_RES, 0.0)));
        tax.setPtEduNonResTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.EDUC_COMM, 0.0)));
        tax.setPtEgcTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.EGC, 0.0)));

        // 🌳 ENVIRONMENT + CITY SERVICES
        tax.setPtTreeTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TREE_TAX, 0.0)));
        tax.setPtEnvironmentTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.ENV_TAX, 0.0)));
        tax.setPtCleanTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.CLEAN_TAX, 0.0)));
        tax.setPtLightTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.LIGHT_TAX, 0.0)));
        tax.setPtFireTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.FIRE_TAX, 0.0)));

        // 💧 WATER & SEWERAGE
        tax.setPtWaterTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.WATER_TAX, 0.0)));
        tax.setPtSewerageTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.SEWERAGE_TAX, 0.0)));
        tax.setPtSewerageBenefitTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.SEWERAGE_BEN, 0.0)));
        tax.setPtWaterBenefitTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.WATER_BEN, 0.0)));

        // 🛣️ STREET + CONSERVANCY
        tax.setPtStreetTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.STREET_TAX, 0.0)));
        tax.setPtSpecialConservancyTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.SPEC_CONS, 0.0)));

        // 🎓 EDUCATIONAL CESS TYPES
        tax.setPtMunicipalEduTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.MUNICIPAL_EDU, 0.0)));
        tax.setPtSpecialEduTaxFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.SPECIAL_EDU, 0.0)));

        // ⚙️ SERVICE & USER CHARGES
        tax.setPtServiceChargesFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.SERVICE_CHG, 0.0)));
        tax.setPtMiscellaneousChargesFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.MISC_CHG, 0.0)));
        tax.setPtUserChargesFl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.USER_CHG, 0.0)));

        // 🔢 FLEXIBLE / RESERVED TAXES
        tax.setPtTax1Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX1, 0.0)));
        tax.setPtTax2Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX2, 0.0)));
        tax.setPtTax3Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX3, 0.0)));
        tax.setPtTax4Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX4, 0.0)));
        tax.setPtTax5Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX5, 0.0)));
        tax.setPtTax6Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX6, 0.0)));
        tax.setPtTax7Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX7, 0.0)));
        tax.setPtTax8Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX8, 0.0)));
        tax.setPtTax9Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX9, 0.0)));
        tax.setPtTax10Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX10, 0.0)));
        tax.setPtTax11Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX11, 0.0)));
        tax.setPtTax12Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX12, 0.0)));
        tax.setPtTax13Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX13, 0.0)));
        tax.setPtTax14Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX14, 0.0)));
        tax.setPtTax15Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX15, 0.0)));
        tax.setPtTax16Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX16, 0.0)));
        tax.setPtTax17Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX17, 0.0)));
        tax.setPtTax18Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX18, 0.0)));
        tax.setPtTax19Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX19, 0.0)));
        tax.setPtTax20Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX20, 0.0)));
        tax.setPtTax21Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX21, 0.0)));
        tax.setPtTax22Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX22, 0.0)));
        tax.setPtTax23Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX23, 0.0)));
        tax.setPtTax24Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX24, 0.0)));
        tax.setPtTax25Fl(nullToZero(taxMap.getOrDefault(ReportTaxKeys.TAX25, 0.0)));

        // 🧮 Total Tax
        double totalTax = taxMap.containsKey(ReportTaxKeys.TOTAL_TAX)
                ? taxMap.get(ReportTaxKeys.TOTAL_TAX)
                : taxMap.values().stream().mapToDouble(Double::doubleValue).sum();
        tax.setPtFinalTaxFl(nullToZero(totalTax));

        tax.setCreatedAt(LocalDateTime.now());
        tax.setUpdatedAt(LocalDateTime.now());
        return tax;
    }

    private AfterHearing_ProposedRValuesEntity convertAssessmentResultsOfProposedRvaluesToEntity(
            ProposedRatableValueDetailsDto dto,String newPropertyNoVc, String finalPropertyNo
    ) {
        AfterHearing_ProposedRValuesEntity prv = new AfterHearing_ProposedRValuesEntity();

        prv.setPrNewPropertyNoVc(newPropertyNoVc);
        prv.setPrFinalPropNoVc(finalPropertyNo);

        // 🧮 Ratable Value Fields
        prv.setPrResidentialFl(nullToZero(dto.getResidentialFl()));
        prv.setPrCommercialFl(nullToZero(dto.getCommercialFl()));
        prv.setPrIndustrialFl(nullToZero(dto.getIndustrialFl()));
        prv.setPrReligiousFl(nullToZero(dto.getReligiousFl()));
        prv.setPrEducationalFl(nullToZero(dto.getEducationalInstituteFl()));
        prv.setPrGovernmentFl(nullToZero(dto.getGovernmentFl()));
        prv.setPrMobileTowerFl(nullToZero(dto.getMobileTowerFl()));
        prv.setPrElectricSubstationFl(nullToZero(dto.getElectricSubstationFl()));

        // 🏗️ Open Plot Categories
        prv.setPrResidentialOpenPlotFl(nullToZero(dto.getResidentialOpenPlotFl()));
        prv.setPrCommercialOpenPlotFl(nullToZero(dto.getCommercialOpenPlotFl()));
        prv.setPrIndustrialOpenPlotFl(nullToZero(dto.getIndustrialOpenPlotFl()));
        prv.setPrReligiousOpenPlotFl(nullToZero(dto.getReligiousOpenPlotFl()));
        prv.setPrEducationAndLegalInstituteOpenPlotFl(nullToZero(dto.getEducationAndLegalInstituteOpenPlotFl()));
        prv.setPrGovernmentOpenPlotFl(nullToZero(dto.getGovernmentOpenPlotFl()));

        // 🧾 Total Ratable Value
        prv.setPrTotalRatableValueFl(nullToZero(dto.getAggregateFl()));

        // 🕒 Metadata
        prv.setCreatedAt(LocalDateTime.now());
        prv.setUpdatedAt(LocalDateTime.now());

        return prv;
    }

    private List<AfterHearing_PropertyRValuesEntity> convertAssessmentResultsOfPropertyRvaluesToEntity(
            List<PropertyUnitDetailsDto> unitDtos,
            String newPropertyNoVc,
            String finalPropertyNo
    ) {
        if (unitDtos == null || unitDtos.isEmpty()) {
            return Collections.emptyList();
        }

        return unitDtos.stream().map(u -> {
            AfterHearing_PropertyRValuesEntity ar = new AfterHearing_PropertyRValuesEntity();

            ar.setPrvPropertyNoVc(newPropertyNoVc);
            ar.setPrvFinalPropNoVc(finalPropertyNo);
            ar.setPrvUnitNoVc(u.getUnitNoVc());

            // 🏠 Core Values
            ar.setPrvRatePerSqMFl(nullToZero(u.getRatePerSqMFl()));
            ar.setPrvRentalValAsPerRateFl(nullToZero(u.getRentalValAsPerRateFl()));
            ar.setPrvDepreciationRateFl(u.getDepreciationRateFl() != null ? u.getDepreciationRateFl().intValue() : 0);
            ar.setPrvDepreciationAmountFl(nullToZero(u.getDepreciationAmountFl()));
            ar.setPrvAmountAfterDepreciationFl(nullToZero(u.getValueAfterDepreciationFl()));
            ar.setPrvMaintenanceRepairsFl(nullToZero(u.getMaintenanceRepairsFl()));
            ar.setPrvTaxableValueByRateFl(nullToZero(u.getTaxableValueByRateFl()));

            // 🧾 Rent-Based Fields
            ar.setPrvTenantNameVc(u.getTenantNameVc());
            ar.setPrvActualMonthlyRentFl(nullToZero(u.getActualMonthlyRentFl()));
            ar.setPrvActualAnnualRentFl(nullToZero(u.getActualAnnualRentFl()));
            ar.setPrvMaintenanceRepairsRentFl(nullToZero(u.getMaintenanceRepairsRentFl()));
            ar.setPrvTaxableValueByRentFl(nullToZero(u.getTaxableValueByRentFl()));
            ar.setPrvTaxableValueConsideredFl(nullToZero(u.getTaxableValueConsideredFl()));

            // 🧮 Additional Fields
            ar.setPrvAgeFactorVc(u.getAgeFactorVc());

            // 🕒 Metadata
            ar.setCreatedAt(LocalDateTime.now());
            ar.setUpdatedAt(LocalDateTime.now());

            return ar;
        }).collect(Collectors.toList());
    }

    private ProposedRatableValueDetailsDto mapToProposedRatableValueDetailsDto(AfterHearing_ProposedRValuesDto rvDto) {
        ProposedRatableValueDetailsDto dto = new ProposedRatableValueDetailsDto();

        dto.setPdNewPropertynovc(rvDto.getPrNewPropertyNoVc());
        dto.setFinalPropertyNoVc(rvDto.getPrFinalPropNoVc());

        dto.setResidentialFl(rvDto.getPrResidentialFl());
        dto.setCommercialFl(rvDto.getPrCommercialFl());
        dto.setIndustrialFl(rvDto.getPrIndustrialFl());
        dto.setReligiousFl(rvDto.getPrReligiousFl());
        dto.setEducationalInstituteFl(rvDto.getPrEducationalFl());
        dto.setGovernmentFl(rvDto.getPrGovernmentFl());
        dto.setMobileTowerFl(rvDto.getPrMobileTowerFl());
        dto.setElectricSubstationFl(rvDto.getPrElectricSubstationFl());

        dto.setResidentialOpenPlotFl(rvDto.getPrResidentialOpenPlotFl());
        dto.setCommercialOpenPlotFl(rvDto.getPrCommercialOpenPlotFl());
        dto.setIndustrialOpenPlotFl(rvDto.getPrIndustrialOpenPlotFl());
        dto.setReligiousOpenPlotFl(rvDto.getPrReligiousOpenPlotFl());
        dto.setEducationAndLegalInstituteOpenPlotFl(rvDto.getPrEducationAndLegalInstituteOpenPlotFl());
        dto.setGovernmentOpenPlotFl(rvDto.getPrGovernmentOpenPlotFl());

        dto.setAggregateFl(rvDto.getPrTotalRatableValueFl());

        return dto;
    }

    private AfterHearing_ProposedRValuesDto mapToAfterHearingProposedRvDto(ProposedRatableValueDetailsDto src, String newPropertyNoVc) {
        AfterHearing_ProposedRValuesDto dto = new AfterHearing_ProposedRValuesDto();

        dto.setPrNewPropertyNoVc(newPropertyNoVc);
        dto.setPrFinalPropNoVc(src.getFinalPropertyNoVc());

        // 🏠 Ratable Value Components
        dto.setPrResidentialFl(nullToZero(src.getResidentialFl()));
        dto.setPrCommercialFl(nullToZero(src.getCommercialFl()));
        dto.setPrIndustrialFl(nullToZero(src.getIndustrialFl()));
        dto.setPrReligiousFl(nullToZero(src.getReligiousFl()));
        dto.setPrEducationalFl(nullToZero(src.getEducationalInstituteFl()));
        dto.setPrGovernmentFl(nullToZero(src.getGovernmentFl()));
        dto.setPrMobileTowerFl(nullToZero(src.getMobileTowerFl()));
        dto.setPrElectricSubstationFl(nullToZero(src.getElectricSubstationFl()));

        // 🏗️ Open Plot Components
        dto.setPrResidentialOpenPlotFl(nullToZero(src.getResidentialOpenPlotFl()));
        dto.setPrCommercialOpenPlotFl(nullToZero(src.getCommercialOpenPlotFl()));
        dto.setPrIndustrialOpenPlotFl(nullToZero(src.getIndustrialOpenPlotFl()));
        dto.setPrReligiousOpenPlotFl(nullToZero(src.getReligiousOpenPlotFl()));
        dto.setPrEducationAndLegalInstituteOpenPlotFl(nullToZero(src.getEducationAndLegalInstituteOpenPlotFl()));
        dto.setPrGovernmentOpenPlotFl(nullToZero(src.getGovernmentOpenPlotFl()));

        // 🧾 Total Ratable Value
        dto.setPrTotalRatableValueFl(nullToZero(src.getAggregateFl()));

        // 🕒 Metadata
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        return dto;
    }

    private Double nullToZero(Double val) {
        return val == null ? 0.0 : val;
    }
}
