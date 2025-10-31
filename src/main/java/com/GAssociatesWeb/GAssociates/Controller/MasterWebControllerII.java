package com.GAssociatesWeb.GAssociates.Controller;


import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearingCompleteProperty_Dto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.PropertyTaxDetailArrears_MasterDto.PropertyTaxDetailArrears_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.TaxBills_MasterDto.TaxBills_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingPropertyManagement_MasterService;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyManagement_Service;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingUnitDetails_Service.AfterHearingUnitDetails_Service;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingUnitBuiltupDetails_Service.AfterHearingUnitBuiltupDetails_Service;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingPropertyTaxDetails_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingProposedRvalues_MasterRepository;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_PropertyTaxDetailsEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_ProposedRValuesEntity;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.CompleteProperty_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitBuiltUp_Dto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_PropertyTaxDetailsDto;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.PropertyTaxDetailArrears_MasterService.PropertyTaxDetailArrears_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.RegisterObjection_MasterService.RegisterObjection_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.SecondaryBatchAssessmentReport.SecondaryBatchAssessmentReport_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.TaxBills.TaxBills_MasterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingPropertyDetails_Service.AfterHearingPropertyDetails_Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/3g")
@AllArgsConstructor
public class MasterWebControllerII {

    private final AfterHearingPropertyManagement_MasterService afterHearingService;
    private final PropertyManagement_Service propertyManagementService;
    private final TaxBills_MasterService taxBills_masterService;
    private final PropertyTaxDetailArrears_MasterService propertyTaxDetailArrears_masterService;
    private final RegisterObjection_MasterService registerObjection_masterService;
    private SecondaryBatchAssessmentReport_MasterService secondaryBatchAssessmentReportService;
    private final AfterHearingPropertyDetails_Service afterHearingPropertyDetailsService;
    private final AfterHearingUnitDetails_Service afterHearingUnitDetailsService;
    private final AfterHearingUnitBuiltupDetails_Service afterHearingUnitBuiltupDetailsService;
    private final AfterHearingPropertyTaxDetails_MasterRepository afterHearingPropertyTaxRepo;
    private final AfterHearingProposedRvalues_MasterRepository afterHearingProposedRvRepo;
    

    @GetMapping("/getCompletePropertyAfterHearing")
    public ResponseEntity<?> getCompletePropertyByNewPropertyNo(@RequestParam String newPropertyNo) {
        return ResponseEntity.ok(afterHearingService.getCompletePropertyByNewPropertyNo(newPropertyNo));
    }

    @GetMapping("/afterHearing/compareProperty")
    public ResponseEntity<?> compareBeforeAfter(
            @RequestParam(value = "newPropertyNo", required = false) String newPropertyNo,
            @RequestParam(value = "finalPropertyNo", required = false) String finalPropertyNo) {
        try {
            String np = (newPropertyNo != null && !newPropertyNo.trim().isEmpty()) ? newPropertyNo.trim() : null;
            if ((np == null || np.isEmpty()) && finalPropertyNo != null && !finalPropertyNo.trim().isEmpty()) {
                // Resolve new property no from final property number
                List<PropertyDetails_Dto> matches = propertyManagementService.searchNewProperties(null, null, null, finalPropertyNo.trim());
                if (matches != null && !matches.isEmpty()) {
                    np = matches.get(0).getPdNewpropertynoVc();
                }
            }

            if (np == null || np.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(java.util.Collections.singletonMap("error", "Provide newPropertyNo or finalPropertyNo"));
            }

            // BEFORE: live property snapshot
            CompleteProperty_Dto before = propertyManagementService.getCompletePropertyByNewPropertyNo(np);

            // AFTER: assemble from after-hearing tables
            AfterHearingCompleteProperty_Dto after = new AfterHearingCompleteProperty_Dto();
            try {
                PropertyDetails_Dto afterProp = afterHearingPropertyDetailsService.getPropertyByNewPropertyNo(np);
                after.setPropertyDetails(afterProp);
            } catch (Exception nf) {
                // if no after-hearing record exists, keep 'after' minimal instead of failing
                after.setPropertyDetails(null);
            }

            // units + builtups
            List<UnitDetails_Dto> ahUnits = afterHearingUnitDetailsService.getAllUnitsByProperty(newPropertyNo);
            if (ahUnits != null) {
                for (UnitDetails_Dto u : ahUnits) {
                    List<UnitBuiltUp_Dto> builtUps = afterHearingUnitBuiltupDetailsService.findAllBuiltUpsByUnit(
                            u.getPdNewpropertynoVc(), u.getUdFloorNoVc(), u.getUdUnitNoVc());
                    u.setUnitBuiltupUps(builtUps);
                }
            }
            after.setUnitDetails(ahUnits);

            // taxes (after-hearing)
            List<AfterHearing_PropertyTaxDetailsEntity> taxEntities = afterHearingPropertyTaxRepo.findByPtNewPropertyNoVc(np);
            if (taxEntities != null && !taxEntities.isEmpty()) {
                List<AfterHearing_PropertyTaxDetailsDto> taxDtos = taxEntities.stream()
                        .map(this::convertAfterTaxEntityToDto)
                        .collect(java.util.stream.Collectors.toList());
                after.setPropertyTaxDetails(taxDtos);
            }

            // proposed RVs (after-hearing)
            List<AfterHearing_ProposedRValuesEntity> prvs = afterHearingProposedRvRepo.findByPrNewPropertyNoVc(np);
            if (prvs != null && !prvs.isEmpty()) {
                java.util.List<com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_ProposedRValuesDto> prvDtos = prvs.stream().map(e -> {
                    com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_ProposedRValuesDto d = new com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_ProposedRValuesDto();
                    d.setPrNewPropertyNoVc(e.getPrNewPropertyNoVc());
                    d.setPrFinalPropNoVc(e.getPrFinalPropNoVc());
                    d.setPrResidentialFl(e.getPrResidentialFl());
                    d.setPrCommercialFl(e.getPrCommercialFl());
                    d.setPrIndustrialFl(e.getPrIndustrialFl());
                    d.setPrReligiousFl(e.getPrReligiousFl());
                    d.setPrEducationalFl(e.getPrEducationalFl());
                    d.setPrMobileTowerFl(e.getPrMobileTowerFl());
                    d.setPrElectricSubstationFl(e.getPrElectricSubstationFl());
                    d.setPrGovernmentFl(e.getPrGovernmentFl());
                    d.setPrResidentialOpenPlotFl(e.getPrResidentialOpenPlotFl());
                    d.setPrCommercialOpenPlotFl(e.getPrCommercialOpenPlotFl());
                    d.setPrIndustrialOpenPlotFl(e.getPrIndustrialOpenPlotFl());
                    d.setPrReligiousOpenPlotFl(e.getPrReligiousOpenPlotFl());
                    d.setPrEducationAndLegalInstituteOpenPlotFl(e.getPrEducationAndLegalInstituteOpenPlotFl());
                    d.setPrGovernmentOpenPlotFl(e.getPrGovernmentOpenPlotFl());
                    d.setPrTotalRatableValueFl(e.getPrTotalRatableValueFl());
                    d.setCreatedAt(e.getCreatedAt());
                    d.setUpdatedAt(e.getUpdatedAt());
                    return d;
                }).collect(java.util.stream.Collectors.toList());
                after.setProposedRValues(prvDtos);
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("before", before);
            payload.put("after", after);
            return ResponseEntity.ok(payload);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    private AfterHearing_PropertyTaxDetailsDto convertAfterTaxEntityToDto(AfterHearing_PropertyTaxDetailsEntity entity) {
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

    @PostMapping(value = "/createCompletePropertyAfterHearing", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createCompleteProperty(
            @RequestPart("updatedFields") String updatedFieldsJson,
            @RequestPart(value = "propertyImage", required = false) MultipartFile propertyImage,
            @RequestPart(value = "propertyImage2", required = false) MultipartFile propertyImage2,
            @RequestPart(value = "housePlan1", required = false) MultipartFile housePlan1,
            @RequestPart(value = "housePlan2", required = false) MultipartFile housePlan2) throws JsonProcessingException {


        ObjectMapper mapper = new ObjectMapper();
        String utf8Json = ensureUtf8(updatedFieldsJson);
        AfterHearingCompleteProperty_Dto dto = mapper.readValue(utf8Json, AfterHearingCompleteProperty_Dto.class);
        System.out.println(dto);
        String newPropertyNo = dto.getPropertyDetails().getPdNewpropertynoVc();
        boolean byRv = dto.isByRv();
        boolean byAssessment = dto.isByAssessment();

        System.out.println("✅ Received After Hearing Property for: " + newPropertyNo);
        System.out.println("🧾 Flags → byRv: " + byRv + ", byAssessment: " + byAssessment);
        System.out.println("🧾 Type: " + dto.getChangeType() + " | Status: " + dto.getHearingStatus());

        try {
            // 🟢 Pass booleans explicitly
            AfterHearingCompleteProperty_Dto saved = afterHearingService.createCompleteProperty(newPropertyNo,dto, byRv, byAssessment);

            if (saved == null || saved.getPropertyDetails() == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Property could not be saved"));

            // 🟡 Update status only if changed
            if ("CHANGED".equalsIgnoreCase(dto.getHearingStatus())) {
                boolean updated = registerObjection_masterService.updateHearingStatus(
                        newPropertyNo, dto.getHearingStatus(), dto.getChangeType());
                System.out.println(updated
                        ? "🟩 Hearing status updated successfully"
                        : "⚠️ Hearing status update failed");
            }

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private String ensureUtf8(String input) {
        return new String(input.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @GetMapping("/secondaryBatchAssessmentReport")
    public ResponseEntity<?> getSecondaryBatchAssessmentReport(@RequestParam("wardNo") Integer wardNo) {
        try {
            List<AfterHearingCompleteProperty_Dto> results =
                    secondaryBatchAssessmentReportService.generateCombinedAfterHearingReport(wardNo);

            if (results == null || results.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No records found for this ward.");
            }
            return ResponseEntity.ok(results);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating Secondary Batch Assessment Report: " + e.getMessage());
        }
    }

    @GetMapping("/taxBills")
    public ResponseEntity<List<TaxBills_MasterDto>> getTaxBills(
            @RequestParam(value = "wardNo", required = false) Integer wardNo,
            @RequestParam(value = "newPropertyNo", required = false) String newPropertyNo) {

        try {
            List<TaxBills_MasterDto> results;
            if (newPropertyNo != null) {
                results = taxBills_masterService.getTaxBillsByNewPropertyNo(newPropertyNo);
            } else if (wardNo != null) {
                results = taxBills_masterService.getTaxBillsByWard(wardNo);
            } else {
                return ResponseEntity.badRequest().build();
            }

            if (results == null || results.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(results);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/taxBills/single")
    public ResponseEntity<TaxBills_MasterDto> getSingleTaxBill(
            @RequestParam("newPropertyNo") String newPropertyNo) {
        try {
            TaxBills_MasterDto dto = taxBills_masterService.getSingleTaxBillByNewPropertyNo(newPropertyNo);
            if (dto == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/afterHearing/markStatus")
    public ResponseEntity<?> markAfterHearingStatus(@RequestBody Map<String, String> payload) {
        String newPropertyNo = payload.get("newPropertyNo");
        String status = payload.get("status");        // RETAINED / ABSENT / CHANGED
        String changedValue = payload.get("changeType"); // RV / ASSESSMENT (optional)

        System.out.println("payload+"+payload);

        boolean updated = registerObjection_masterService.updateHearingStatus(newPropertyNo, status, changedValue);

        if (updated) {
            try {
                // For RETAINED/ABSENT, persist a snapshot from BEFORE-HEARING (live) tables
                if ("RETAINED".equalsIgnoreCase(status) || "ABSENT".equalsIgnoreCase(status)) {
                    boolean alreadySnapshotted = false;
                    try { alreadySnapshotted = afterHearingPropertyDetailsService.getPropertyByNewPropertyNo(newPropertyNo) != null; } catch (Exception ignore) {}

                    if (!alreadySnapshotted) {
                        // Fetch live complete property (survey/property tables)
                        com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.CompleteProperty_Dto live =
                                propertyManagementService.getCompletePropertyByNewPropertyNo(newPropertyNo);
                        if (live != null && live.getPropertyDetails() != null) {
                            AfterHearingCompleteProperty_Dto snapshot = new AfterHearingCompleteProperty_Dto();
                            snapshot.setPropertyDetails(live.getPropertyDetails());
                            snapshot.setUnitDetails(live.getUnitDetails());
                            snapshot.setHearingStatus(status);
                            snapshot.setChangeType("NONE");
                            // Persist to after-hearing without re-assessment or RV change
                            afterHearingService.createCompleteProperty(newPropertyNo, snapshot, false, false);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // proceed to return success for status update even if snapshot fails
            }
            return ResponseEntity.ok(Map.of(
                    "message", "Hearing status updated successfully",
                    "property", newPropertyNo,
                    "status", status,
                    "changeType", changedValue == null ? "" : changedValue
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No objection found for property " + newPropertyNo));
        }
    }

    // 🧾 Add Arrears Record
    @PostMapping("/addPropertyArrearsTax")
    public ResponseEntity<PropertyTaxDetailArrears_MasterDto> saveArrearsTax(
            @RequestBody PropertyTaxDetailArrears_MasterDto dto) {
        System.out.println(dto);
        if (dto == null) {
            return ResponseEntity.badRequest().build();
        }

        PropertyTaxDetailArrears_MasterDto savedDto = propertyTaxDetailArrears_masterService.saveArrears(dto);
        return ResponseEntity.ok(savedDto);
    }

    // 📋 Get All Arrears Records
    @GetMapping("/getAllPropertyArrearsTaxDetails")
    public ResponseEntity<List<PropertyTaxDetailArrears_MasterDto>> getAllArrearsTaxDetails() {
        List<PropertyTaxDetailArrears_MasterDto> allArrears = propertyTaxDetailArrears_masterService.getAll();

        if (allArrears == null || allArrears.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(allArrears);
    }

    // 🔍 Get Single Arrears Record by Property Number
    @GetMapping("/getPropertyArrears/{newPropertyNo}")
    public ResponseEntity<PropertyTaxDetailArrears_MasterDto> getSingleArrearsTaxDetails(
            @PathVariable("newPropertyNo") String newPropertyNo) {

        if (newPropertyNo == null || newPropertyNo.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        PropertyTaxDetailArrears_MasterDto arrearsDetails =
                propertyTaxDetailArrears_masterService.getSingleArrearsTaxDetails(newPropertyNo);

        if (arrearsDetails == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(arrearsDetails);
    }

    // Fetch arrears by unique key: newPropertyNo + financialYear
    @GetMapping("/getPropertyArrearsByYear")
    public ResponseEntity<PropertyTaxDetailArrears_MasterDto> getPropertyArrearsByYear(
            @RequestParam("newPropertyNo") String newPropertyNo,
            @RequestParam("financialYear") String financialYear) {

        if (newPropertyNo == null || newPropertyNo.isBlank() || financialYear == null || financialYear.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        PropertyTaxDetailArrears_MasterDto dto =
                propertyTaxDetailArrears_masterService.getArrearsByPropertyAndYear(newPropertyNo, financialYear);

        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }
}
