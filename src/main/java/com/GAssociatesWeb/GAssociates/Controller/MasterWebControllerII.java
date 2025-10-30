package com.GAssociatesWeb.GAssociates.Controller;


import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearingCompleteProperty_Dto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.PropertyTaxDetailArrears_MasterDto.PropertyTaxDetailArrears_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.TaxBills_MasterDto.TaxBills_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingPropertyManagement_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.PropertyTaxDetailArrears_MasterService.PropertyTaxDetailArrears_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.RegisterObjection_MasterService.RegisterObjection_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.SecondaryBatchAssessmentReport.SecondaryBatchAssessmentReport_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.TaxBills.TaxBills_MasterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/3g")
@AllArgsConstructor
public class MasterWebControllerII {

    private final AfterHearingPropertyManagement_MasterService afterHearingService;
    private final TaxBills_MasterService taxBills_masterService;
    private final PropertyTaxDetailArrears_MasterService propertyTaxDetailArrears_masterService;
    private final RegisterObjection_MasterService registerObjection_masterService;
    private SecondaryBatchAssessmentReport_MasterService secondaryBatchAssessmentReportService;

    @GetMapping("/getCompletePropertyAfterHearing")
    public ResponseEntity<?> getCompletePropertyByNewPropertyNo(@RequestParam String newPropertyNo) {
        return ResponseEntity.ok(afterHearingService.getCompletePropertyByNewPropertyNo(newPropertyNo));
    }

    @PostMapping(value = "/createCompletePropertyAfterHearing", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createCompleteProperty(
            @RequestPart("updatedFields") String updatedFieldsJson,
            @RequestPart(value = "propertyImage", required = false) MultipartFile propertyImage,
            @RequestPart(value = "propertyImage2", required = false) MultipartFile propertyImage2,
            @RequestPart(value = "housePlan1", required = false) MultipartFile housePlan1,
            @RequestPart(value = "housePlan2", required = false) MultipartFile housePlan2) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        AfterHearingCompleteProperty_Dto dto = mapper.readValue(updatedFieldsJson, AfterHearingCompleteProperty_Dto.class);

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

    @PostMapping("/afterHearing/markStatus")
    public ResponseEntity<?> markAfterHearingStatus(@RequestBody Map<String, String> payload) {
        String newPropertyNo = payload.get("newPropertyNo");
        String status = payload.get("status");        // RETAINED / ABSENT / CHANGED
        String changedValue = payload.get("changeType"); // RV / ASSESSMENT (optional)

        System.out.println("payload+"+payload);

        boolean updated = registerObjection_masterService.updateHearingStatus(newPropertyNo, status, changedValue);

        if (updated) {
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