package com.GAssociatesWeb.GAssociates.Controller;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AgeFactor_MasterDto.AgeFactor_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentDate_MasterDto.AssessmentDate_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.ConsolidatedTaxes_MasterDto.ConsolidatedTaxes_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.EduCessAndEmpCess_MasterDto.EduCessAndEmpCess_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.PropertyRates_MasterDto.PropertyRates_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto.RVTypeCategory_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto.RVTypes_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxDepreciation_MasterDto.TaxDepreciation_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.BuildingTypes_MasterDto.BuildingSubType_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.BuildingTypes_MasterDto.BuildingType_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.ConstructionClass_MasterDto.ConstructionClass_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.CouncilDetails_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.Occupancy_MasterDto.Occupancy_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.OldWardTypes_MasterDto.OldWard_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.*;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropClassification_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropSubClassification_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropSubUsageType_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto.PropUsageType_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.Remarks_MasterDto.Remarks_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.RoomType_MasterDto.RoomType_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.SewerageTypes_MasterDto.Sewerage_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitTypes_MasterDto.UnitFloorNo_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitTypes_MasterDto.UnitNo_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitUsageTypes_MasterDto.UnitUsageSubType_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitUsageTypes_MasterDto.UnitUsageType_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.WaterConnection_MasterDto.WaterConnection_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.UserAccessDto.UserAccess_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.WardTypes_MasterEntity.Ward_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDeletionLog_Entity.PropertyDeletionLog;
import com.GAssociatesWeb.GAssociates.Helper.ExcelHelper;
import com.GAssociatesWeb.GAssociates.Helper.ExcelService;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.PropertyDeletionLog_Repository;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyManagement_Service;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyNumberGenerator_Service.UniqueIdGenerator;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyOldDetails_Service.PropertyOldDetails_Service;
import com.GAssociatesWeb.GAssociates.Service.ImageUtils;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AgeFactor_MasterService.AgeFactor_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentDate_MasterService.AssessmentDate_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.ConolidatedTaxes_MasterService.ConsolidatedTaxes_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.EduCessAndEmpCess_MasterService.EduCessAndEmpCess_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.PropertyRates_MasterService.PropertyRates_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.RVTypes_MasterService.RVTypeCategory_MasterService.RVTypeCategory_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.RVTypes_MasterService.RVTypes_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.PreLoadCache;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.ReportGenerator.BatchReportGenerator_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.ReportGenerator.CalculationSheet.CalculationSheetGenerator_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentRealtime.TaxAssessment_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxDepreciation_MasterService.TaxDepreciation_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.BuildingSubTypes_MasterServices.BuildingSubType_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.BuildingTypes_MasterServices.BuildingType_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ConstructionClass_MasterServices.ConstClass_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.CouncilDetails_MasterService.CouncilDetails_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.Occupancy_MasterServices.Occupancy_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.OldWardTypes_MasterServices.OldWard_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.BuildStatus_MasterService.BuildStatus_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.OwnerCategory_MasterService.OwnerCategory_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.OwnerType_MasterService.OwnerType_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.WaterConnection_MasterService.WaterConnection_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.Zone_MasterService.Zone_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertyClassification_MasterService.PropClassification_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertySubClassification_MasterService.PropSubClassification_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertyUsageSubType_MasterService.PropertySubUsageType_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyTypes_MasterServices.PropertyUsageType_MasterService.PropertyUsageType_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.Remarks_MasterService.Remarks_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.RoomType_MasterService.RoomType_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.SewerageTypes_MasterServices.SewerageType_MasterServices.Sewerage_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitTypes_MasterServices.UnitFloorNo_MasterService.UnitFloorNo_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitTypes_MasterServices.UnitNo_MasterService.UnitNo_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitUsageType_MasterServices.UnitUsageSubType_MasterService.UnitUsageSubType_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.UnitUsageType_MasterServices.UnitUsageType_MasterService.UnitUsageType_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.WardTypes_MasterServices.Ward_MasterService;
import com.GAssociatesWeb.GAssociates.Service.UserAccess_MasterService.UserAccessService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


    //Phase 1 Development for 3G Associates
    //Data Entry Provision and MasterWeb
    //from 01-02-2024 to 07-05-2024
@Controller
@RequestMapping(value = "/3g")
public class MasterWebController {

    private final Ward_MasterService ward_masterService;
    private final PropClassification_MasterService propClassification_masterService;
    private final Zone_MasterService zone_masterService;
    private final BuildStatus_MasterService buildStatus_masterService;
    private final OwnerType_MasterService ownerType_masterService;
    private final PropSubClassification_MasterService propSubClassification_masterService;
    private final UnitFloorNo_MasterService unitFloorNo_masterService;
    private final UnitNo_MasterService unitNo_masterService;
    private final PropertyUsageType_MasterService propertyUsageType_masterService;
    private final OwnerCategory_MasterService ownerCategory_masterService;
    private final WaterConnection_MasterService waterConnection_masterService;
    private final Sewerage_MasterService sewerage_masterService;
    private final PropertySubUsageType_MasterService propertySubUsageType_masterService;
    private final BuildingType_MasterService buildingType_masterService;
    private final BuildingSubType_MasterService buildingSubType_masterService;
    private final UnitUsageType_MasterService unitUsageType_masterService;
    private final UnitUsageSubType_MasterService unitUsageSubType_masterService;
    private final UserAccessService userAccessService;
    private final OldWard_MasterService oldWardMasterService;
    private final ConstClass_MasterService constClass_MasterService;
    private final Occupancy_MasterService occupancy_masterService;
    private final AssessmentDate_MasterService assessmentDate_masterService;
    private final RoomType_MasterService roomType_masterService;
    private final AgeFactor_MasterService ageFactor_masterService;
    private final Remarks_MasterService remarksMasterService;
    private final PropertyOldDetails_Service propertyOldDetails_service;
    private final ExcelService excelService;
    private final PropertyManagement_Service propertyManagement_service;
    private final PropertyDeletionLog_Repository propertyDeletionLog_repository;
    private final TaxDepreciation_MasterService taxDepreciation_masterService;
    private final RVTypes_MasterService rvTypes_masterService;
    private final ConsolidatedTaxes_MasterService consolidatedTaxes_masterService;
    private final EduCessAndEmpCess_MasterService eduCessAndEmpCess_masterService;
    private final PropertyRates_MasterService propertyRates_masterService;
    private final TaxAssessment_MasterService taxAssessment_masterService;
    private final BatchReportGenerator_MasterService batchReportGenerator_masterService;
    private final CalculationSheetGenerator_MasterService calculationSheetGenerator_masterService;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final PreLoadCache preLoadCache;
    private final JobLauncher jobLauncher;
    private final Job processPropertyDetailsJob;
    private final CouncilDetails_MasterService councilDetails_masterService;
    private final RVTypeCategory_MasterService rvTypeCategory_masterService;
    private static final Logger logger = Logger.getLogger(MasterWebController.class.getName());
    public MasterWebController(Ward_MasterService ward_masterService, PropClassification_MasterService propClassification_masterService,
                               PropSubClassification_MasterService propSubClassification_masterService,
                               Zone_MasterService zone_masterService, BuildStatus_MasterService buildStatus_masterService,
                               OwnerType_MasterService ownerType_masterService, UnitFloorNo_MasterService unitFloorNo_MasterService,
                               UnitNo_MasterService unitNo_MasterService, PropertyUsageType_MasterService propertyUsageType_MasterService,
                               OwnerCategory_MasterService ownerCategory_masterService, WaterConnection_MasterService waterConnection_MasterService,
                               Sewerage_MasterService sewerage_MasterService, PropertySubUsageType_MasterService propertySubUsageType_MasterService,
                               BuildingType_MasterService buildingType_MasterService, BuildingSubType_MasterService buildingSubType_MasterService,
                               UnitUsageType_MasterService unitUsageType_MasterService, UnitUsageSubType_MasterService unitUsageSubType_MasterService,
                               UserAccessService userAccessService, OldWard_MasterService oldWard_MasterService, ConstClass_MasterService constClass_MasterService,
                               Occupancy_MasterService occupancy_masterService, AssessmentDate_MasterService assessmentDate_masterService, RoomType_MasterService roomType_MasterService,
                               AgeFactor_MasterService ageFactor_MasterService, Remarks_MasterService remarks_MasterService, PropertyOldDetails_Service propertyOldDetailsService, ExcelService excelService,
                               PropertyManagement_Service property_ManagementService, PropertyDeletionLog_Repository propertyDeletionLogRepository, TaxDepreciation_MasterService taxDepreciation_MasterService,
                               RVTypes_MasterService rvTypes_MasterService, ConsolidatedTaxes_MasterService consolidatedTaxes_MasterService, EduCessAndEmpCess_MasterService eduCessAndEmpCess_MasterService, PropertyRates_MasterService propertyRates_MasterService,
                               TaxAssessment_MasterService taxAssessment_MasterService, BatchReportGenerator_MasterService batchReportGenerator_MasterService, CalculationSheetGenerator_MasterService calculationSheetGenerator_MasterService, UniqueIdGenerator uniqueIdGenerator, PreLoadCache preLoadCache, JobLauncher jobLauncher, Job processPropertyDetailsJob,
                               CouncilDetails_MasterService councilDetails_MasterService, RVTypeCategory_MasterService rvTypeCategory_MasterService) {
        this.ward_masterService = ward_masterService;
        this.propClassification_masterService = propClassification_masterService;
        this.propSubClassification_masterService = propSubClassification_masterService;
        this.zone_masterService = zone_masterService;
        this.buildStatus_masterService = buildStatus_masterService;
        this.ownerType_masterService = ownerType_masterService;
        this.unitFloorNo_masterService = unitFloorNo_MasterService;
        this.unitNo_masterService = unitNo_MasterService;
        this.propertyUsageType_masterService = propertyUsageType_MasterService;
        this.ownerCategory_masterService = ownerCategory_masterService;
        this.waterConnection_masterService = waterConnection_MasterService;
        this.sewerage_masterService = sewerage_MasterService;
        this.propertySubUsageType_masterService = propertySubUsageType_MasterService;
        this.buildingType_masterService = buildingType_MasterService;
        this.buildingSubType_masterService = buildingSubType_MasterService;
        this.unitUsageType_masterService = unitUsageType_MasterService;
        this.unitUsageSubType_masterService = unitUsageSubType_MasterService;
        this.userAccessService = userAccessService;
        this.oldWardMasterService = oldWard_MasterService;
        this.constClass_MasterService = constClass_MasterService;
        this.occupancy_masterService = occupancy_masterService;
        this.assessmentDate_masterService = assessmentDate_masterService;
        this.roomType_masterService = roomType_MasterService;
        this.ageFactor_masterService = ageFactor_MasterService;
        this.remarksMasterService = remarks_MasterService;
        this.propertyOldDetails_service = propertyOldDetailsService;
        this.excelService = excelService;
        this.propertyManagement_service = property_ManagementService;
        this.propertyDeletionLog_repository = propertyDeletionLogRepository;

        this.taxDepreciation_masterService = taxDepreciation_MasterService;
        this.rvTypes_masterService = rvTypes_MasterService;
        this.consolidatedTaxes_masterService = consolidatedTaxes_MasterService;
        this.eduCessAndEmpCess_masterService = eduCessAndEmpCess_MasterService;
        this.propertyRates_masterService = propertyRates_MasterService;
        this.taxAssessment_masterService = taxAssessment_MasterService;
        this.batchReportGenerator_masterService = batchReportGenerator_MasterService;
        this.calculationSheetGenerator_masterService = calculationSheetGenerator_MasterService;
        this.uniqueIdGenerator = uniqueIdGenerator;
        this.preLoadCache = preLoadCache;
        this.jobLauncher = jobLauncher;
        this.processPropertyDetailsJob = processPropertyDetailsJob;
        this.councilDetails_masterService = councilDetails_MasterService;
        this.rvTypeCategory_masterService = rvTypeCategory_MasterService;
    }

    @GetMapping(value = "/MasterWebLogin")
    public String getMasterWebLogin() {
        return "3GMasterWebLogin";
    }

    @GetMapping(value = "/MasterWebSignup")
    public String ShowSignUpPage() {
        return "3GMasterWebSignUp";
    }

    @GetMapping("/profiles")
    @ResponseBody
    public List<String> getProfileOptions() {
        return Arrays.asList("ITA", "ITL", "IT", "ACTL", "ACL", "DETL", "DEL");
    }

    @PostMapping("/MasterWebUserSignup")
    public ModelAndView signupUser(@ModelAttribute UserAccess_Dto userAccessDto,RedirectAttributes redirectAttributes) {
        userAccessService.signup(userAccessDto);
        // Redirecting or returning a view name after successful signup
        return new ModelAndView("redirect:/3g/masterSheet");
        // Replace "redirect:/signupSuccess" with the path or view you want users to see after signup
    }

    @PostMapping(value = "/authenticate")
    public String authenticateUser(HttpServletRequest request, RedirectAttributes redirectAttributes, String username, String password) {
        if (userAccessService.authenticateMaster(username, password)) {//this method needs to be changed for security purposes
            // Authentication successful, store username in session and redirect to survey form
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            return "redirect:/3g/masterSheet";
        } else {
            // Authentication failed, redirect back to login page
            redirectAttributes.addFlashAttribute("loginError", "Username or password is incorrect.");
            return "redirect:/3g/MasterWebLogin"; // You can add a query parameter to indicate authentication failure
        }
    }

    @GetMapping("/userManagement")
    public String userManagement() {
        return "3GUserManagement"; // Assuming your HTML file is named userManagement.html
    }

    @GetMapping(value = "/getAllUsers")
    public ResponseEntity<List<UserAccess_Dto>> getAllUsers() {
        List<UserAccess_Dto> users = userAccessService.getAllUsers();
        return ResponseEntity.ok(users);
    }

//    @GetMapping("/downloadUsersExcel")
//    public void downloadUsersExcel(HttpServletResponse response) throws IOException {
//        List<UserAccess_Dto> users = userAccessService.getAllUsers();
//
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Users");
//
//        // Create header row
//        Row headerRow = sheet.createRow(0);
//        headerRow.createCell(0).setCellValue("Name");
//        headerRow.createCell(1).setCellValue("Username");
//        headerRow.createCell(2).setCellValue("Password");
//
//
//        // Create data rows
//        int rowNum = 1;
//        for (UserAccess_Dto user : users) {
//            Row row = sheet.createRow(rowNum++);
//            row.createCell(0).setCellValue(user.getFirstname()+" "+user.getLastname());
//            row.createCell(1).setCellValue(user.getUsername());
//            row.createCell(2).setCellValue(user.getPassword());
//        }
//
//        // Set the response headers and content type
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.xlsx");
//
//        // Write the workbook to the response output stream
//        workbook.write(response.getOutputStream());
//        workbook.close();
//    }

    @PostMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserAccess_Dto userDto) {
        try {
            UserAccess_Dto updatedUser = userAccessService.updateUser(userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }
    @DeleteMapping("/deleteUser/{username}")
    public ResponseEntity<?> deleteUserByUsername(@PathVariable String username) {
        try {
            boolean isDeleted = userAccessService.deleteUserByUsername(username);
            if (isDeleted) {
                return ResponseEntity.ok().body("User deleted successfully.");
            } else {
               return ResponseEntity.badRequest().body("User could not be deleted.");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping(value = "/masterSheet")
    public String getMasterSheet(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            return "redirect:/3g/MasterWebLogin"; // Redirect to login page if not authenticated
        }
        return "3GMasterWeb";
    }


    //for adding wards in database we are using below method
    @PostMapping("/addWards")
    public ResponseEntity<String> addWards(@RequestBody Map<String, Integer> requestBody) {
        int wardCount = requestBody.get("wardNo");
        try {
            ward_masterService.addWards(wardCount);
            return ResponseEntity.ok("Ward added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding ward");
        }
    }

    @GetMapping("/getAllWards")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAllWards() {
        List<Ward_MasterEntity> wards = ward_masterService.getAllWards();
        List<Map<String, Object>> responseData = wards.stream().map(ward -> {
            Map<String, Object> item = new HashMap<>();
            item.put("wardNo", ward.getWardNo()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //  for adding zones in database we are using below method
    @PostMapping("/addZones")
    public ResponseEntity<String> addZones(@RequestBody Zone_MasterDto zone_masterDto) {
        try {
            zone_masterService.addZones(zone_masterDto);
            return ResponseEntity.ok("Zone added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding zone");
        }
    }
    @GetMapping("/getAllZones")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAllZones() {
        List<Zone_MasterDto> zones = zone_masterService.getAllZones();
        List<Map<String, Object>> responseData = zones.stream().map(zone -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", zone.getZoneNo()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }
    //for adding buildingStatus
    @PostMapping("/addBuildStatus")
    public ResponseEntity<BuildStatus_MasterDto> createBuildStatus(@RequestBody BuildStatus_MasterDto dto) throws Exception {
        BuildStatus_MasterDto savedDto = buildStatus_masterService.saveBuildStatus(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
    @GetMapping("/buildstatuses")
    public ResponseEntity<List<Map<String, Object>>> getBuildStatuses() {
        List<BuildStatus_MasterDto> buildStatuses = buildStatus_masterService.findAllBuildStatuses();
        List<Map<String, Object>> responseData = buildStatuses.stream().map(buildstatus -> {
            Map<String, Object> item = new HashMap<>();
            item.put("value", buildstatus.getId());
            item.put("name", buildstatus.getBuildStatus()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //for adding ownerType
    @PostMapping("/addOwnerType")
    public ResponseEntity<OwnerType_MasterDto> addOwnerType(@RequestBody OwnerType_MasterDto ownerTypeMasterDto) throws Exception {
        OwnerType_MasterDto savedDto = ownerType_masterService.addOwnerType(ownerTypeMasterDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
    @GetMapping("/getOwnerType")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAllOwnerTypes() {
        List<OwnerType_MasterDto> ownerTypes = ownerType_masterService.findAllOwnerTypes();
        List<Map<String, Object>> responseData = ownerTypes.stream().map(ownerType -> {
            Map<String, Object> item = new HashMap<>();
            item.put("ownerType", ownerType.getOwnerType());
            item.put("ownerTypeMarathi", ownerType.getOwnerTypeMarathi());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //for adding unitNo.
    @PostMapping("/addUnitNo")
    public ResponseEntity<Void> createUnitNos(@RequestBody UnitNo_MasterDto unitNoMasterDto) throws Exception {
        unitNo_masterService.saveUnitNoMaster(unitNoMasterDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/unitNumbers")
    public ResponseEntity<List<Map<String, Object>>> getAllUnitNos() {
        List<UnitNo_MasterDto> unitNos = unitNo_masterService.getAllUnitNoMasters();
        List<Map<String, Object>> responseData = unitNos.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("englishname", type.getUnitno());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/addUnitFloorNo")
    public ResponseEntity<Void> saveUnitFloorNoMaster(@RequestBody UnitFloorNo_MasterDto unitFloorNo_masterDto) throws Exception {
        unitFloorNo_masterService.saveUnitFloorNoMaster(unitFloorNo_masterDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getUnitFloorNos")
    public ResponseEntity<List<Map<String, Object>>> listUnitFloorNoMasters() {
        List<UnitFloorNo_MasterDto> unitFloorNos = unitFloorNo_masterService.getAllUnitFloorNos();
        List<Map<String, Object>> responseData = unitFloorNos.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("englishname", type.getUnitfloorno());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }


    //property types
    //for adding property type in database and getting them from db we are using below method
    @PostMapping("/addPropertyType")
    public ResponseEntity<PropClassification_MasterDto> addPropertyType(@RequestBody PropClassification_MasterDto propClassificationMasterDto) {
        PropClassification_MasterDto savedDto = propClassification_masterService.save(propClassificationMasterDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    //for getting picklist values of propertytypes in every section which is related to property type
    @GetMapping("/propertytypes")
    public ResponseEntity<List<Map<String, Object>>> getClassifications() {
     // Fetch all property classifications
            List<PropClassification_MasterDto> classifications = propClassification_masterService.findAll();
            List<Map<String, Object>> responseData = classifications.stream().map(type -> {
                Map<String, Object> item = new HashMap<>();
                item.put("name", type.getLocalPropertyTypeName());
                item.put("englishname", type.getPropertyTypeName());// Assuming PropertyType has getName()
                item.put("value", type.getPcmId()); // Assuming PropertyType has getId()
                return item;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(responseData);
    }

    //for adding property subtypes in property subtype section
    @PostMapping("/addPropertySubtypes")
    public ResponseEntity<PropSubClassification_MasterDto> addPropertySubType(@RequestBody PropSubClassification_MasterDto propSubClassificationMasterDto) {
        PropSubClassification_MasterDto savedDto = propSubClassification_masterService.save(propSubClassificationMasterDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @GetMapping("/propertysubtypes")
    public ResponseEntity<List<Map<String, Object>>> getAllPropertySubTypes() {
        List<PropSubClassification_MasterDto> propertySubTypes = propSubClassification_masterService.findAll();
        List<Map<String, Object>> responseData = propertySubTypes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("marathiname", type.getLocalPropertySubtypeName());
            item.put("englishname", type.getPropertySubtypeName());
            item.put("value", type.getPropertySubClassificationId());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }
    //for getting property subtypes after selecting property type in property usage type section
    @GetMapping("/propertySubtypes/{classificationId}")
    public ResponseEntity<List<Map<String, Object>>> getSubclassificationsByClassificationId(@PathVariable Integer classificationId) {
        List<PropSubClassification_MasterDto> subclassifications = propSubClassification_masterService.getSubclassificationsByClassificationId(classificationId);
        List<Map<String, Object>> responseData = subclassifications.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getLocalPropertySubtypeName()); // Assuming PropertyType has getName()
            item.put("value", type.getPropertySubClassificationId()); // Assuming PropertyType has getId()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);

    }

    //for adding property subtypes after selecting property subtype in property usagetype section
    @PostMapping("/addUsageTypes")
    public ResponseEntity<PropUsageType_MasterDto> createPropUsageType(@RequestBody PropUsageType_MasterDto dto) {
        PropUsageType_MasterDto savedDto = propertyUsageType_masterService.saveUsageType(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @GetMapping("/propertyusagetypes")
    public ResponseEntity<List<Map<String, Object>>> getPropertyUsageTypes() {
        List<PropUsageType_MasterDto> usageTypes = propertyUsageType_masterService.findAll();
        List<Map<String, Object>> responseData = usageTypes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("marathiname", type.getLocalUsagetypeName());
            item.put("englishname", type.getUsageTypeName());
            item.put("value", type.getPropertyUsageTypeId());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }
    //for getting property usagetypes in property usagesubtype section so that we can select one of them
    @GetMapping("/usageTypes/{id}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getUsageTypesBySubtypeId(@PathVariable("id") Integer subtypeId) {
        List<PropUsageType_MasterDto> dtos = propertyUsageType_masterService.findUsageTypesBySubtypeId(subtypeId);
        List<Map<String, Object>> responseData = dtos.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getLocalUsagetypeName()); // Assuming PropertyType has getName()
            item.put("value", type.getPropertyUsageTypeId()); // Assuming PropertyType has getId()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }
    //and then for adding property usage id and sub usage name
    @PostMapping("/addPropertySubUsageTypes")
    public ResponseEntity<PropSubUsageType_MasterDto> createPropSubUsageType(@RequestBody PropSubUsageType_MasterDto dto) {
        PropSubUsageType_MasterDto savedDto = propertySubUsageType_masterService.saveUsageSubType(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
    //for getting all subusage types
    @GetMapping("/getSubUsageTypes")
    @ResponseBody
    public List<Map<String, Object>> getAllPropUsageSubTypes() {
        List<PropSubUsageType_MasterDto> subUsageTypes = propertySubUsageType_masterService.findAll();
        List<Map<String, Object>> responseData = subUsageTypes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("marathiname", type.getUsageTypeLocal());
            item.put("englishname", type.getUsageTypeEng());
            item.put("value", type.getId());
            return item;
        }).collect(Collectors.toList());
        return responseData;
    }
    //propertytypes


    //Building types
    //for adding BuildingTypes
    @PostMapping("/addBuildingTypes")
    public ResponseEntity<BuildingType_MasterDto> addBuildingType(@RequestBody BuildingType_MasterDto buildingTypeMasterDTO) {
        String temcustom = buildingTypeMasterDTO.getTemcustom();
        String[] parts = temcustom.split(",", 2);
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        buildingTypeMasterDTO.setPcmClassidI(id);
        buildingTypeMasterDTO.setPcmProptypellVc(name);
        BuildingType_MasterDto savedDto = buildingType_masterService.saveBuildingTypeMaster(buildingTypeMasterDTO);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @GetMapping("/getBuildingTypes")
    public ResponseEntity<List<Map<String, Object>>> getAllBuildingTypes() {
        List<BuildingType_MasterDto> buildingTypes = buildingType_masterService.getAllBuildingTypes();
        List<Map<String, Object>> responseData = buildingTypes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getBtBuildingtypellVc());
            item.put("englishname", type.getBtBuildingtypeengVc());
            item.put("value", type.getBtBuildingTypeId());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }
    //for getting building types by there id
    @GetMapping("/getBuildingTypesByPropertyClassification/{propertyClassificationId}")
    public ResponseEntity<List<Map<String, Object>>> getBuildingTypesByPropertyClassificationId(@PathVariable Integer propertyClassificationId) {
        List<BuildingType_MasterDto> buildingTypeMasterDTOs = buildingType_masterService.getBuildingTypesByPropertyClassificationId(propertyClassificationId);
        List<Map<String, Object>> responseData = buildingTypeMasterDTOs.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getBtBuildingtypellVc()); // Assuming PropertyType has getName()
            item.put("value", type.getBtBuildingTypeId()); // Assuming PropertyType has getId()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }
    //for updating buildingTypes with id
    @PutMapping("/updateBuildingTypes/{id}")
    public ResponseEntity<BuildingType_MasterDto> updateBuildingType(@PathVariable Integer id, @RequestBody BuildingType_MasterDto buildingTypeMasterDTO) {
        BuildingType_MasterDto updatedDto = buildingType_masterService.updateBuildingType(buildingTypeMasterDTO, id);
        return ResponseEntity.ok(updatedDto);
    }
    //for Deleting buildingTypes
    @DeleteMapping("/buildingtypes/{id}")
    public ResponseEntity<?> deleteBuildingType(@PathVariable Integer id) {
        buildingType_masterService.deleteBuildingType(id);
        return ResponseEntity.ok().build();
    }
    //BuildingTypes



    //BuildingSubTypes
    //adding building subtypes
    @PostMapping("/addBuildingSubTypes")
    public ResponseEntity<BuildingSubType_MasterDto> addBuildingSubType(@RequestBody BuildingSubType_MasterDto buildingSubTypeMasterDto) {
        String temcustom = buildingSubTypeMasterDto.getTemcustomb();
        String[] parts = temcustom.split(",", 2);
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        buildingSubTypeMasterDto.setBuildingtypeid(id);
        buildingSubTypeMasterDto.setBtBuildingtypellVc(name);
        BuildingSubType_MasterDto savedDto = buildingSubType_masterService.saveBuildingSubTypeMaster(buildingSubTypeMasterDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @GetMapping("/getAllBuildingSubTypes")
    public ResponseEntity<List<Map<String, Object>>> getAllBuildingSubTypes() {
        List<BuildingSubType_MasterDto> buildingSubTypes = buildingSubType_masterService.getAllBuildingSubTypes();
        List<Map<String, Object>> responseData = buildingSubTypes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("marathiname", type.getBstBuildingsubtypellVc());
            item.put("englishname", type.getBstBuildingsubtypeengVc());
            item.put("value", type.getBuildingsubtypeid());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/getBuildingSubtypes/{id}")
    public ResponseEntity<BuildingSubType_MasterDto> getBuildingSubTypeById(@PathVariable Integer id) {
        BuildingSubType_MasterDto buildingSubTypeMasterDto = buildingSubType_masterService.getBuildingSubTypeById(id);
        return ResponseEntity.ok(buildingSubTypeMasterDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildingSubType_MasterDto> updateBuildingSubType(@PathVariable Integer id, @RequestBody BuildingSubType_MasterDto buildingSubTypeMasterDto) {
        BuildingSubType_MasterDto updatedDto = buildingSubType_masterService.updateBuildingSubTypeMaster(buildingSubTypeMasterDto, id);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuildingSubType(@PathVariable Integer id) {
        buildingSubType_masterService.deleteBuildingSubTypeMaster(id);
        return ResponseEntity.ok().build();
    }
    //BuildingSubTypes

    //Unit Usage Types
    @PostMapping("/addUnitUsageType")
    public ResponseEntity<UnitUsageType_MasterDto> createUnitUsageMaster(@RequestBody UnitUsageType_MasterDto dto) {
        UnitUsageType_MasterDto savedDto = unitUsageType_masterService.saveUnitUsageMaster(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitUsageType_MasterDto> getUnitUsageMasterById(@PathVariable Integer id) {
        UnitUsageType_MasterDto dto = unitUsageType_masterService.getUnitUsageMasterById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getUnitUsageByPropUsageId/{propUsageId}")
    public ResponseEntity<List<Map<String, Object>>> findAllByPropUsageId(@PathVariable Integer propUsageId) {
        List<UnitUsageType_MasterDto> dtos = unitUsageType_masterService.findAllByPropUsageId(propUsageId);
        List<Map<String, Object>> responseData = dtos.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getUum_usagetypell_vc()); // Assuming PropertyType has getName()
            item.put("value", type.getUum_usageid_i()); // Assuming PropertyType has getId()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/getAllUnitUsageTypes")
    public ResponseEntity<List<Map<String, Object>>> getAllUnitUsages() {
        List<UnitUsageType_MasterDto> unitUsageTypes = unitUsageType_masterService.getAllUnitUsages();
        List<Map<String, Object>> responseData = unitUsageTypes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("uum_usagetypell_vc", type.getUum_usagetypell_vc());
            item.put("uum_usagetypeeng_vc", type.getUum_usagetypeeng_vc());
            item.put("uum_rvtype_vc", type.getUum_rvtype_vc());
            item.put("value", type.getUum_usageid_i());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/updateUnitUsageById/{id}")
    public ResponseEntity<UnitUsageType_MasterDto> updateUnitUsageMaster(@PathVariable Integer id, @RequestBody UnitUsageType_MasterDto dto) {
        UnitUsageType_MasterDto updatedDto = unitUsageType_masterService.updateUnitUsageMaster(dto, id);
        return ResponseEntity.ok(updatedDto);
    }

    @PostMapping("/deleteUnitUsageById")
    public ResponseEntity<Void> deleteUnitUsageMaster(@RequestParam Integer id) {
        unitUsageType_masterService.deleteUnitUsageMaster(id);
        return ResponseEntity.ok().build();
    }

    //Unit Usage Types


    //Unit Usage Sub Types
    @PostMapping("/addUnitUsageSub")
    public ResponseEntity<UnitUsageSubType_MasterDto> createUnitUsageSubType(@RequestBody UnitUsageSubType_MasterDto unitUsageSubTypeDto) {
        UnitUsageSubType_MasterDto savedDto = unitUsageSubType_masterService.saveUnitUsageSubTypeMaster(unitUsageSubTypeDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }


    @GetMapping("/getUnitUsageSub/{unitUsageId}")
    public ResponseEntity<List<Map<String, Object>>> getUnitUsageSubTypesByUsageId(@PathVariable Integer unitUsageId) {
        List<UnitUsageSubType_MasterDto> unitUsageSubTypeMasterDtos = unitUsageSubType_masterService.findByUnitUsageId(unitUsageId);
        List<Map<String, Object>> responseData = unitUsageSubTypeMasterDtos.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getUsm_usagetypell_vc()); // Assuming PropertyType has getName()
            item.put("value", type.getUsm_usagesubid_i()); // Assuming PropertyType has getId()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/getAllUnitUsageSubTypes")
    public ResponseEntity<List<Map<String, Object>>> getAllUnitUsageSubTypes() {
        List<UnitUsageSubType_MasterDto> unitUsageSubTypes = unitUsageSubType_masterService.findAllUnitUsageSubTypeMasters();
        List<Map<String, Object>> responseData = unitUsageSubTypes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("usm_usagetypell_vc", type.getUsm_usagetypell_vc());
            item.put("usm_usagetypeeng_vc", type.getUsm_usagetypeeng_vc());
            item.put("usm_usercharges_i",type.getUsm_usercharges_i());
            item.put("value", type.getUsm_usagesubid_i());
            item.put("uum_usagetypeeng_vc", type.getUum_usagetypeeng_vc());
            item.put("usm_rvtype_vc", type.getUsm_rvtype_vc());
            item.put("usmApplyDifferentRateVc", type.getUsmApplyDifferentRateVc());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    // to update unit usageSubtypes we are using the below function
    @PostMapping("/updateUnitUsagesSub/{id}")
    public ResponseEntity<UnitUsageSubType_MasterDto> updateUnitUsageSubType(@PathVariable Integer id, @RequestBody UnitUsageSubType_MasterDto unitUsageSubTypeDto) {
        return ResponseEntity.ok(unitUsageSubType_masterService.updateUnitUsageSubTypeMaster(id, unitUsageSubTypeDto));
    }

    @PostMapping("/deleteUnitUsagesSub")
    public ResponseEntity<Void> deleteUnitUsageSubMaster(@RequestParam Integer id) {
        unitUsageSubType_masterService.deleteUnitUsageSubTypeMaster(id);
        return ResponseEntity.noContent().build();
    }
    //Unit Usage Sub Types

    //for adding owner category
    @PostMapping("/addOwnerCategory")
    public ResponseEntity<OwnerCategory_MasterDto> addOwnerCategory(@RequestBody OwnerCategory_MasterDto ownerCategoryMasterDto) {
        OwnerCategory_MasterDto savedDto = ownerCategory_masterService.addOwnerCategory(ownerCategoryMasterDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
    //for getting owner category
    @GetMapping("/ownerCategories")
    public ResponseEntity<List<Map<String, Object>>> getOwnerCategories() {
        List<OwnerCategory_MasterDto> ownerCategoryMasterDtos = ownerCategory_masterService.findAll();
        List<Map<String, Object>> responseData = ownerCategoryMasterDtos.stream().map(ownertype -> {
            Map<String, Object> item = new HashMap<>();
            item.put("marathiname", ownertype.getOwnerCategoryMarathi());
            item.put("englishname", ownertype.getOwnerCategory());// Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }
    //for adding water-connections
    @PostMapping("/addWaterConnection")
    public ResponseEntity<WaterConnection_MasterDto> addWaterConnection(@RequestBody WaterConnection_MasterDto waterConnectionMasterDto) {
        WaterConnection_MasterDto savedDto = waterConnection_masterService.addWaterConnection(waterConnectionMasterDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
    //for getting water-connections
    @GetMapping("/waterConnections")
    public ResponseEntity<List<Map<String, Object>>> listWaterConnections() {
        List<WaterConnection_MasterDto> waterConnections = waterConnection_masterService.getAllWaterConnections();
        List<Map<String, Object>> responseData = waterConnections.stream().map(watercon -> {
            Map<String, Object> item = new HashMap<>();
            item.put("waterConnection", watercon.getWaterConnection());
            item.put("waterConnectionMarathi", watercon.getWaterConnectionMarathi()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }
    //for getting sewerage types
    @GetMapping("/getSewerageTypes")
    public ResponseEntity<List<Map<String, Object>>> listSewerageTypes() {
        List<Sewerage_MasterDto> existingSewerageTypes = sewerage_masterService.findAll();
        List<Map<String, Object>> responseData = existingSewerageTypes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("englishname", type.getSewerage());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }
    //for adding sewerage types
    @PostMapping("/addSewerage")
    public ResponseEntity<Sewerage_MasterDto> addSewerageType(@RequestBody Sewerage_MasterDto sewerageMasterDto) {
        Sewerage_MasterDto savedDto = sewerage_masterService.saveSewerageType(sewerageMasterDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    //for oldwards,agefactor,...etc

    //methods for adding oldwards and getting them
    @PostMapping("/addOldWards")
    public ResponseEntity<String> addOldWards(@RequestBody Map<String, Integer> request) {
        int wardCount = request.get("wardCount");
        for (int i = 1; i <= wardCount; i++) {
            OldWard_MasterDto oldWardMasterDTO = new OldWard_MasterDto();
            oldWardMasterDTO.setCountNo(String.valueOf(i));
            // Here, you might set other properties based on your application's needs
            oldWardMasterService.saveOldWardMaster(oldWardMasterDTO);
        }
        return ResponseEntity.ok("Successfully added " + wardCount + " old wards.");
    }

    @GetMapping("/getAllOldWards")
    public ResponseEntity<List<OldWard_MasterDto>> getAllOldWards() {
        List<OldWard_MasterDto> oldWards = oldWardMasterService.findAllOldWardMasters();
        if (oldWards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(oldWards);
    }

    //methods for construction classes
    @PostMapping("/constructionClassMasters")
    public ResponseEntity<ConstructionClass_MasterDto> createConstructionClassMaster(@RequestBody ConstructionClass_MasterDto dto) {
        ConstructionClass_MasterDto savedDto = constClass_MasterService.save(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @GetMapping("/constructionClassMasters/{id}")
    public ResponseEntity<ConstructionClass_MasterDto> getConstructionClassMasterById(@PathVariable Integer id) {
        ConstructionClass_MasterDto dto = constClass_MasterService.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/constructionClassMasters")
    public ResponseEntity<List<Map<String, Object>>> getAllConstructionClassMasters() {
        List<ConstructionClass_MasterDto> constructionClasses = constClass_MasterService.findAll();
        List<Map<String, Object>> responseData = constructionClasses.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("marathiname", type.getCcm_classnamell_vc());
            item.put("englishname", type.getCcm_classnameeng_vc());
            item.put("Deduction", type.getCcm_percentageofdeduction_i());
            item.put("value",type.getCcm_conclassid_i());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/constructionClassMasters/{id}")
    public ResponseEntity<ConstructionClass_MasterDto> updateConstructionClassMaster(@PathVariable String id, @RequestBody ConstructionClass_MasterDto dto) {
        dto.setCcm_conclassid_i(dto.getCcm_conclassid_i()); // Ensure the DTO has the correct ID for update
        ConstructionClass_MasterDto updatedDto = constClass_MasterService.save(dto);
        return ResponseEntity.ok(updatedDto);
    }

    @PostMapping("/deleteConstructionClassMastersById")
    public ResponseEntity<Void> deleteConstructionClassMaster(@RequestParam Integer id) {
        constClass_MasterService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    //methods for occupancies
    @PostMapping("/occupancyMasters")
    public ResponseEntity<Occupancy_MasterDto> createOccupancyMaster(@RequestBody Occupancy_MasterDto dto) {
        Occupancy_MasterDto savedDto = occupancy_masterService.saveOccupancy_Master(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @GetMapping("/occupancyMasters/{id}")
    public ResponseEntity<Occupancy_MasterDto> getOccupancyMasterById(@PathVariable Integer id) {
        Occupancy_MasterDto dto = occupancy_masterService.findOccupancy_MasterById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/occupancyMasters")
    public ResponseEntity<List<Map<String, Object>>> getAllOccupancyMasters() {
        List<Occupancy_MasterDto> occupancyMasters = occupancy_masterService.findAllOccupancy_Masters();
        List<Map<String, Object>> responseData = occupancyMasters.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("marathiname", type.getOccupancy_marathi());
            item.put("englishname", type.getOccupancy());
            item.put("value", type.getOccupancy_id());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/occupancyMasters/{id}")
    public ResponseEntity<Occupancy_MasterDto> updateOccupancyMaster(@PathVariable Integer id, @RequestBody Occupancy_MasterDto dto) {
        dto.setOccupancy_id(id);
        Occupancy_MasterDto updatedDto = occupancy_masterService.saveOccupancy_Master(dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/occupancyMasters/{id}")
    public ResponseEntity<Void> deleteOccupancyMaster(@PathVariable Integer id) {
        occupancy_masterService.deleteOccupancy_MasterById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createAssessmentDates")
    public ResponseEntity<AssessmentDate_MasterDto> createAssessmentDate(@RequestBody AssessmentDate_MasterDto assessmentDateDTO) {
        AssessmentDate_MasterDto createdAssessmentDate = assessmentDate_masterService.saveAssessmentDate(assessmentDateDTO);
        return new ResponseEntity<>(createdAssessmentDate, HttpStatus.CREATED);
    }

    @GetMapping("/getAllAssessmentDates")
    public ResponseEntity<List<Map<String, Object>>> getAllAssessmentDates() {
        List<AssessmentDate_MasterDto> assessmentDates = assessmentDate_masterService.getAllAssessmentDates();
        List<Map<String, Object>> responseData = assessmentDates.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("value", type.getAssessmentId());
            item.put("firstassessmentdate", type.getFirstAssessmentDate()); // Assuming there is a name field
            item.put("currentassessmentdate", type.getCurrentAssessmentDate());
            item.put("lastassessmentdate", type.getLastAssessmentDate());// Assuming there is an ID field
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/getAssessmentById/{id}")
    public ResponseEntity<AssessmentDate_MasterDto> getAssessmentDateById(@PathVariable Integer id) {
        AssessmentDate_MasterDto assessmentDate = assessmentDate_masterService.getAssessmentDateById(id);
        if (assessmentDate != null) {
            return new ResponseEntity<>(assessmentDate, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/deleteAssessmentById")
    public ResponseEntity<Void> deleteAssessmentDate(@RequestParam Integer id) {
        assessmentDate_masterService.deleteAssessmentDate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Adding roomtypes for master
    @GetMapping("/roomTypes")
    public ResponseEntity<List<Map<String, Object>>> getAllRoomTypes() {
        List<RoomType_MasterDto> roomTypes = roomType_masterService.findAll();
        List<Map<String, Object>> responseData = roomTypes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("englishname", type.getRoomType());
            item.put("room", type.getRoomSelected());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/roomTypes/{id}")
    public ResponseEntity<RoomType_MasterDto> getRoomTypeById(@PathVariable Integer id) {
        RoomType_MasterDto roomTypeMasterDto = roomType_masterService.findById(id);
        if (roomTypeMasterDto != null) {
            return ResponseEntity.ok(roomTypeMasterDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/roomTypes")
    public ResponseEntity<RoomType_MasterDto> createRoomType(@Valid @RequestBody RoomType_MasterDto roomTypeMasterDto) {
        RoomType_MasterDto createdRoomType = roomType_masterService.addRoomType(roomTypeMasterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoomType);
    }

    @PutMapping("/roomTypes/{id}")
    public ResponseEntity<RoomType_MasterDto> updateRoomType(@PathVariable Integer id, @Valid @RequestBody RoomType_MasterDto roomTypeMasterDto) {
        RoomType_MasterDto existingRoomType = roomType_masterService.findById(id);
        if (existingRoomType != null) {
            roomTypeMasterDto.setId(id);
            RoomType_MasterDto updatedRoomType = roomType_masterService.addRoomType(roomTypeMasterDto);
            return ResponseEntity.ok(updatedRoomType);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/DeleteRoomTypes")
    public ResponseEntity<Void> deleteRoomType(@RequestParam Integer id) {
        RoomType_MasterDto existingRoomType = roomType_masterService.findById(id);
        if (existingRoomType != null) {
            roomType_masterService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/constructionAgeFactor")
    public ResponseEntity<List<Map<String, Object>>> getAllAgeFactors() {
        List<AgeFactor_MasterDto> ageFactors = ageFactor_masterService.getAllAgeFactors();
        List<Map<String, Object>> responseData = ageFactors.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("afm_agefactornameeng_vc", type.getAfm_agefactornameeng_vc());
            item.put("afm_agefactornamell_vc", type.getAfm_agefactornamell_vc());
            item.put("afm_ageminage_vc", type.getAfm_ageminage_vc());
            item.put("afm_agemaxage_vc", type.getAfm_agemaxage_vc());
            item.put("afm_remarks_vc", type.getAfm_remarks_vc());
            item.put("afm_agefactorid_i", type.getAfm_agefactorid_i());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/constructionAgeFactor/{id}")
    public ResponseEntity<AgeFactor_MasterDto> getAgeFactorById(@PathVariable Integer id) {
        return ResponseEntity.ok(ageFactor_masterService.getAgeFactorById(id));
    }

    @PostMapping("/addConstructionAgeFactor")
    public ResponseEntity<AgeFactor_MasterDto> createAgeFactor(@RequestBody AgeFactor_MasterDto ageFactorMasterDto) {
        return ResponseEntity.ok(ageFactor_masterService.createAgeFactor(ageFactorMasterDto));
    }

    @DeleteMapping("/deleteConstructionAgeFactor/{id}")
    public ResponseEntity<Void> deleteAgeFactor(@PathVariable Integer id) {
        ageFactor_masterService.deleteAgeFactor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/addRemarks")
    public ResponseEntity<Remarks_MasterDto> addRemark(@RequestBody Remarks_MasterDto remarksMasterDto) {
        return ResponseEntity.ok(remarksMasterService.createRemark(remarksMasterDto));
    }

    @GetMapping("/getAllRemarks")
    public ResponseEntity<List<Remarks_MasterDto>> getAllRemarks() {
        return ResponseEntity.ok(remarksMasterService.getAllRemarks());
    }

    @PostMapping("/uploadExcel")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                propertyOldDetails_service.saveExcel(file);
                return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully: " + file.getOriginalFilename());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Fail to upload file: " + file.getOriginalFilename());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload an excel file!");
    }

    @GetMapping("/download-template")
    public ResponseEntity<ByteArrayResource> downloadTemplate() throws IOException {
        byte[] data = excelService.generateExcelTemplate();
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=OldPropertyDetailsTemplate.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(data.length)
                .body(resource);
    }

    @GetMapping("/getDeletionLogs")
    public ResponseEntity<List<PropertyDeletionLog>> getDeletionLogs() {
        List<PropertyDeletionLog> deletionLogs = propertyDeletionLog_repository.findAll();
        return ResponseEntity.ok(deletionLogs);
    }

    @GetMapping("/searchNewProperties")
    public ResponseEntity<List<PropertyDetails_Dto>> searchProperties(
            @RequestParam(required = false) String surveyPropertyNo,
            @RequestParam(required = false) String ownerName,
            @RequestParam(required = false) Integer wardNo) {
        List<PropertyDetails_Dto> results = propertyManagement_service.searchNewProperties(surveyPropertyNo, ownerName, wardNo);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    //Phase 2 Development for 3G Associates
    //Tax Assessment Modules
    //15-05-2024



    //***endpoints for tax depreciation***
    @PostMapping("/depreciationRates")
    public ResponseEntity<List<TaxDepreciation_MasterDto>> addDepreciationRates(@RequestBody Map<String, Object> payload) {
        List<TaxDepreciation_MasterDto> savedDtos = taxDepreciation_masterService.addDepreciationRates(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDtos);
    }
    @GetMapping("/getDepreciationRates")
    public ResponseEntity<List<Map<String, Object>>> getAllDepreciationRates() {
        List<TaxDepreciation_MasterDto> rates = taxDepreciation_masterService.getAllDepreciationRates();
        List<Map<String, Object>> responseData = rates.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("constructionClassVc", type.getConstructionClassVc());
            item.put("minAgeI", type.getMinAgeI());
            item.put("maxAgeI", type.getMaxAgeI());
            item.put("depreciationPercentageI", type.getDepreciationPercentageI());
            item.put("value", type.getId());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/deleteDepreciationRate")
    public ResponseEntity<Void> deleteDepreciationRate(@RequestParam Long id) {
        taxDepreciation_masterService.deleteDepreciationRate(id);
        return ResponseEntity.noContent().build();
    }






    //***endpoints for propertyrates***
    @GetMapping("/getAllPropertyRates")
    public ResponseEntity<List<Map<String, Object>>> getAllPropertyRates() {
        List<PropertyRates_MasterDto> propertyRates = propertyRates_masterService.getAllPropertyRates();
        List<Map<String, Object>> responseData = propertyRates.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("constructionTypeVc", type.getConstructionTypeVc());
            item.put("taxRateZoneI", type.getTaxRateZoneI());
            item.put("rateI", type.getRateI());
            item.put("value", type.getId());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/getPropertyRateById/{id}")
    public ResponseEntity<PropertyRates_MasterDto> getPropertyRateById(@PathVariable Long id) {
        PropertyRates_MasterDto propertyRate = propertyRates_masterService.getPropertyRateById(id);
        return propertyRate != null ? ResponseEntity.ok(propertyRate) : ResponseEntity.notFound().build();
    }

    @PostMapping("/createPropertyRate")
    public ResponseEntity<List<PropertyRates_MasterDto>> createPropertyRate(@RequestBody Map<String, Object> payload) {
        List<PropertyRates_MasterDto> newPropertyRate = propertyRates_masterService.addPropertyRate(payload);
        return ResponseEntity.ok(newPropertyRate);
    }

    @PostMapping("/updatePropertyRate/{id}")
    public ResponseEntity<PropertyRates_MasterDto> updatePropertyRate(@PathVariable Long id, @RequestBody PropertyRates_MasterDto propertyRateDetails) {
        PropertyRates_MasterDto updatedPropertyRate = propertyRates_masterService.updatePropertyRate(id, propertyRateDetails);
        return ResponseEntity.ok(updatedPropertyRate);
    }

    @PostMapping("/deletePropertyRate")
    public ResponseEntity<Void> deletePropertyRate(@RequestParam Long id) {
        propertyRates_masterService.deletePropertyRate(id);
        return ResponseEntity.noContent().build();
    }





    //***endpoints for RVtypes***
    @PostMapping("/addRVType")
    public ResponseEntity<RVTypes_MasterDto> addRVType(@RequestBody RVTypes_MasterDto rvTypeDto) {
        RVTypes_MasterDto savedRVType = rvTypes_masterService.saveRVType(rvTypeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRVType);
    }

    @GetMapping("/getAllRVTypes")
    public ResponseEntity<List<Map<String, Object>>> getAllRVTypes() {
        List<RVTypes_MasterDto> rvTypes = rvTypes_masterService.getAllRVTypes();
        List<Map<String, Object>> responseData = rvTypes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("typeNameVc", type.getTypeNameVc());
            item.put("rateFl", type.getRateFl());
            item.put("appliedTaxesVc", type.getAppliedTaxesVc());
            item.put("descriptionVc", type.getDescriptionVc());
            item.put("value", type.getRyTypeId());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/deleteRVType")
    public ResponseEntity<Void> deleteRVType(@RequestParam Long id) {
        rvTypes_masterService.deleteRVTypeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getRVTypeById/{id}")
    public ResponseEntity<RVTypes_MasterDto> getRVTypeById(@PathVariable Long id) {
        RVTypes_MasterDto rvType = rvTypes_masterService.getRVTypeById(id);
        return rvType != null ? ResponseEntity.ok(rvType) : ResponseEntity.notFound().build();
    }

    @PostMapping("/updateRVType/{id}")
    public ResponseEntity<RVTypes_MasterDto> updateRVType(@PathVariable Long id, @RequestBody RVTypes_MasterDto rvTypeDetails) {
        System.out.println(rvTypeDetails);
        RVTypes_MasterDto updatedRVType = rvTypes_masterService.updateRVType(id, rvTypeDetails);
        return ResponseEntity.ok(updatedRVType);
    }




    //***endpoints for consolidated taxes***
    @PostMapping("/addConsolidatedTax")
    public ResponseEntity<ConsolidatedTaxes_MasterDto> addConsolidatedTax(@RequestBody ConsolidatedTaxes_MasterDto taxDto) {
        ConsolidatedTaxes_MasterDto savedTax = consolidatedTaxes_masterService.createTax(taxDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTax);
    }

    // Get all consolidated taxes
    @GetMapping("/getAllConsolidatedTaxes")
    public ResponseEntity<List<Map<String, Object>>> getAllConsolidatedTaxes() {
        List<ConsolidatedTaxes_MasterDto> taxes = consolidatedTaxes_masterService.getAllTaxes();
        List<Map<String, Object>> responseData = taxes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("taxNameVc", type.getTaxNameVc());
            item.put("taxRateFl", type.getTaxRateFl());
            item.put("applicableonVc", type.getApplicableonVc());
            item.put("value", type.getId());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    // Delete a consolidated tax
    @PostMapping("/deleteConsolidatedTax")
    public ResponseEntity<Void> deleteConsolidatedTax(@RequestParam Long id) {
        consolidatedTaxes_masterService.deleteTax(id);
        return ResponseEntity.noContent().build();
    }

    // Get a specific consolidated tax by ID
    @GetMapping("/getConsolidatedTaxById/{id}")
    public ResponseEntity<ConsolidatedTaxes_MasterDto> getConsolidatedTaxById(@PathVariable Long id) {
        ConsolidatedTaxes_MasterDto taxDto = consolidatedTaxes_masterService.getTaxById(id);
        return taxDto != null ? ResponseEntity.ok(taxDto) : ResponseEntity.notFound().build();
    }
    //update consolidated tax
    @PostMapping("/updateConsolidatedTax/{id}")
    public ResponseEntity<ConsolidatedTaxes_MasterDto> updateConsolidatedTax(@PathVariable Long id, @RequestBody ConsolidatedTaxes_MasterDto taxDetails) {
        ConsolidatedTaxes_MasterDto updatedTax = consolidatedTaxes_masterService.updateTax(id, taxDetails);
        return ResponseEntity.ok(updatedTax);
    }





    //***endpoints for education cess and employment cess
    @GetMapping("/getAllCessRates")
    public ResponseEntity<List<Map<String, Object>>> getAllCessRates() {
        List<EduCessAndEmpCess_MasterDto> cessRates = eduCessAndEmpCess_masterService.getAllCessRates();
        List<Map<String, Object>> responseData = cessRates.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("minTaxableValueFl", type.getMinTaxableValueFl());
            item.put("maxTaxableValueFl", type.getMaxTaxableValueFl());
            item.put("residentialRateFl", type.getResidentialRateFl());
            item.put("commercialRateFl", type.getCommercialRateFl());
            item.put("egcRateFl", type.getEgcRateFl());
            item.put("value", type.getId());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/getCessRateById/{id}")
    public ResponseEntity<EduCessAndEmpCess_MasterDto> getCessRateById(@PathVariable Long id) {
        EduCessAndEmpCess_MasterDto cessRate = eduCessAndEmpCess_masterService.getCessRateById(id);
        return cessRate != null ? ResponseEntity.ok(cessRate) : ResponseEntity.notFound().build();
    }

    @PostMapping("/createCessRate")
    public ResponseEntity<EduCessAndEmpCess_MasterDto> createCessRate(@RequestBody EduCessAndEmpCess_MasterDto cessRateDto) {
        EduCessAndEmpCess_MasterDto newCessRate = eduCessAndEmpCess_masterService.createCessRate(cessRateDto);
        return ResponseEntity.ok(newCessRate);
    }

    @PostMapping("/updateCessRate/{id}")
    public ResponseEntity<EduCessAndEmpCess_MasterDto> updateCessRate(@PathVariable Long id, @RequestBody EduCessAndEmpCess_MasterDto cessRateDetails) {
        EduCessAndEmpCess_MasterDto updatedCessRate = eduCessAndEmpCess_masterService.updateCessRate(id, cessRateDetails);
        return ResponseEntity.ok(updatedCessRate);
    }

    @PostMapping("/deleteCessRate")
    public ResponseEntity<Void> deleteCessRate(@RequestParam Long id) {
        eduCessAndEmpCess_masterService.deleteCessRate(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/calculationSheet/{newPropertyNumber}")
    public String showCalculationSheet(@PathVariable("newPropertyNumber") String newPropertyNumber, Model model) {
        model.addAttribute("newPropertyNumber", newPropertyNumber);
        return "3GViewCalculationSheet"; // Thymeleaf template or just HTML
    }

    @GetMapping("/calculatedDetails/{newPropertyNumber}")
    public ResponseEntity<List<AssessmentResultsDto>> getCalculatedDetails(@PathVariable("newPropertyNumber") String newPropertyNumber) {
       List<AssessmentResultsDto> assessmentResultsDtos = calculationSheetGenerator_masterService.generateSinglePropertyReport(newPropertyNumber);
//        AssessmentResultsDto assessmentResultsDto = taxAssessment_masterService.performAssessment(newPropertyNumber);
        if (assessmentResultsDtos != null) {
            return ResponseEntity.ok(assessmentResultsDtos);
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    @GetMapping("/showsurvey/{id}")
    public String showSurvey(@PathVariable("id") String propertyId, Model model) {
        model.addAttribute("propertyId", propertyId);
        return "3GViewSurveyFrom"; // Ensure this matches the name of your HTML file (excluding the .html extension)
    }

    @PostMapping("/processBatch/{wardNo}")
    public ResponseEntity<String> processBatch(@PathVariable("wardNo") String wardNo) {
        try {
            logger.info(wardNo);
            preLoadCache.preloadAllCaches();
            uniqueIdGenerator.assignFinalPropertyNumbers(wardNo);
            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addString("wardNo", wardNo);
            jobParametersBuilder.addLong("runTimestamp", System.currentTimeMillis()); // Add timestamp to make it unique
            jobLauncher.run(processPropertyDetailsJob, jobParametersBuilder.toJobParameters());
            return ResponseEntity.ok("Batch job has been started for Ward No: " + wardNo);
        } catch (Exception e) {
            logger.info("Failed to start batch job for Ward No: {}:"+wardNo+e);
            return ResponseEntity.status(500).body("Failed to start batch job: " + e.getMessage());
        }
    }

    @GetMapping("/batchAssessmentReport/{wardNo}")
    public String showBatchAssessmentReport(@PathVariable("wardNo") Integer wardNo, Model model) {
        // Return the name of the view that will render the batch assessment report
        return "3GBatchAssessmentReport"; // This should be the name of your HTML/Thymeleaf template
    }

    @GetMapping("/batchCalculationReport/{wardNo}")
    public String showBatchCalculationReport(@PathVariable("wardNo") Integer wardNo, Model model) {
        // Return the name of the view that will render the batch assessment report
        return "3GBatchCalculationReport"; // This should be the name of your HTML/Thymeleaf template
    }

    @GetMapping("/propertyBatchReport")
    public ResponseEntity<List<AssessmentResultsDto>> getPropertyBatchReport(@RequestParam("wardNo") Integer wardNo) {
        try {
            // Call service method to get report data
            List<AssessmentResultsDto> reportData = batchReportGenerator_masterService.generatePropertyReport(wardNo);
            // Return the data as a JSON response
            return ResponseEntity.ok(reportData);
        } catch (Exception e) {
            // Handle exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/propertyCalculationSheetReport")
    public ResponseEntity<List<AssessmentResultsDto>> getPropertyCalculationSheetReport(@RequestParam("wardNo") Integer wardNo) {
        try {
            // Call service method to get report data
            List<AssessmentResultsDto> reportData = calculationSheetGenerator_masterService.generatePropertyCalculationReport(wardNo);
            // Return the data as a JSON response
            return ResponseEntity.ok(reportData);
        } catch (Exception e) {
            // Handle exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //This is Additional feature to get council names and images so that application can become standard
    @PostMapping(value = "/saveCouncilDetails", consumes = "multipart/form-data")
    public ResponseEntity<String> saveCouncilDetails(@RequestParam("standardName") String standardName,
                                                     @RequestParam("localName") String localName,
                                                     @RequestParam("standardSiteNameVc") String standardSiteNameVc,
                                                     @RequestParam("localSiteNameVc") String localSiteNameVc,
                                                     @RequestParam("standardDistrictNameVc") String standardDistrictNameVc,
                                                     @RequestParam("localDistrictNameVc") String localDistrictNameVc,
                                                     @RequestParam(name = "councilImage", required = false) MultipartFile councilImage){
        try{
            CouncilDetails_MasterDto councilDetails_masterDto = new CouncilDetails_MasterDto();
            councilDetails_masterDto.setStandardName(standardName);
            councilDetails_masterDto.setLocalName(localName);

            System.out.println(standardSiteNameVc);
            councilDetails_masterDto.setStandardDistrictNameVC(standardDistrictNameVc);
            councilDetails_masterDto.setLocalDistrictNameVC(localDistrictNameVc);
            councilDetails_masterDto.setStandardSiteNameVC(standardSiteNameVc);
            councilDetails_masterDto.setLocalSiteNameVC(localSiteNameVc);
            if (councilImage != null && !councilImage.isEmpty()) {
                String base64Image = ImageUtils.convertToBase64(councilImage);
                councilDetails_masterDto.setImageBase64(base64Image);
            }
            councilDetails_masterService.saveCouncilDetails(councilDetails_masterDto);
            return ResponseEntity.ok("Success");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving council details.");
        }
    }

    @GetMapping("/getCouncilDetails")
    public ResponseEntity<List<Map<String, Object>>> getCouncilDetails() {
        try {
            CouncilDetails_MasterDto councilDetailsDto = councilDetails_masterService.getSingleCouncilDetails();
            if (councilDetailsDto != null) {
                // Transform the CouncilDetails_MasterDto into a map structure
                Map<String, Object> item = new HashMap<>();
                item.put("value", councilDetailsDto.getId()); // Include the ID
                item.put("standardName", councilDetailsDto.getStandardName());
                item.put("localName", councilDetailsDto.getLocalName());
                item.put("imageBase64", councilDetailsDto.getImageBase64()); // Add the Base64 image data
                item.put("standardSiteNameVC", councilDetailsDto.getStandardSiteNameVC());
                item.put("localSiteNameVC", councilDetailsDto.getLocalSiteNameVC());
                item.put("standardDistrictNameVC", councilDetailsDto.getStandardDistrictNameVC());
                item.put("localDistrictNameVC", councilDetailsDto.getLocalDistrictNameVC());
                // Return the map as a list containing the single object
                List<Map<String, Object>> councilDetailsList = Collections.singletonList(item);
                return ResponseEntity.ok(councilDetailsList);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PostMapping("/deleteCouncilDetails")
    public ResponseEntity<Void> deleteCouncilDetails(@RequestParam Long id) {
        try {

            boolean isDeleted = councilDetails_masterService.deleteCouncilDetailById(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            e.printStackTrace();

            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/getRVTypeCategories")
    public ResponseEntity<List<RVTypeCategory_MasterDto>> getAllRVTypeCategories() {
        List<RVTypeCategory_MasterDto> categories = rvTypeCategory_masterService.getAllRVTypeCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

//for uploading cad images and property images we will be using seperate api endpoints-
//-so below are endpoints
    @PostMapping(value = "/uploadCadImage", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadCadImage(@RequestParam("newPropertyNo") String newPropertyNo,
                                                 @RequestParam("cadImage") MultipartFile cadImage){
        try {
            if (cadImage == null || cadImage.isEmpty()) {
                return ResponseEntity.badRequest().body("No property image uploaded.");
            }

            propertyManagement_service.uploadCadImage(newPropertyNo, cadImage);
            return ResponseEntity.ok("File received successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading property image.");
        }
    }

    @PostMapping(value = "/uploadPropertyImage", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadPropertyImage(@RequestParam("newPropertyNo") String newPropertyNo,
                                                      @RequestParam("propertyImage") MultipartFile propertyImage){
        try {
            if (propertyImage == null || propertyImage.isEmpty()) {
                return ResponseEntity.badRequest().body("No property image uploaded.");
            }

            propertyManagement_service.uploadPropertyImage(newPropertyNo, propertyImage);
            return ResponseEntity.ok("File received successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading property image.");
        }
    }


}

