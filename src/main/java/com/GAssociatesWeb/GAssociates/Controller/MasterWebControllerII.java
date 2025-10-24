package com.GAssociatesWeb.GAssociates.Controller;


import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearingCompleteProperty_Dto;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearingPropertyManagement_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.SecondaryBatchAssessmentReport.SecondaryBatchAssessmentReport_MasterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/3g")
@AllArgsConstructor
public class MasterWebControllerII {

    private final AfterHearingPropertyManagement_MasterService afterHearingService;

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

        // Convert JSON string to DTO
        ObjectMapper mapper = new ObjectMapper();
        AfterHearingCompleteProperty_Dto dto = mapper.readValue(updatedFieldsJson, AfterHearingCompleteProperty_Dto.class);

        System.out.println("✅ Received AfterHearing Property: " + dto);
        return ResponseEntity.ok(afterHearingService.createCompleteProperty(dto));
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
}
