package com.GAssociatesWeb.GAssociates.Controller;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentDate_MasterDto.AssessmentDate_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.BuildingTypes_MasterDto.BuildingSubType_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.BuildingTypes_MasterDto.BuildingType_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.ConstructionClass_MasterDto.ConstructionClass_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.Occupancy_MasterDto.Occupancy_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.OldWardTypes_MasterDto.OldWard_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.BuildStatus_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.OwnerCategory_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.OwnerType_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.Zone_MasterDto;
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
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.CompleteProperty_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyOldDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.UserAccessDto.UserAccess_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.WardTypes_MasterEntity.Ward_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDeletionLog_Entity.PropertyDeletionLog;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.PropertyDeletionLog_Repository;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.PropertyDetails_Repository;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyManagement_Service;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyOldDetails_Service.PropertyOldDetails_Service;
import com.GAssociatesWeb.GAssociates.Service.ImageUtils;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentDate_MasterService.AssessmentDate_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.BuildingSubTypes_MasterServices.BuildingSubType_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.BuildingTypes_MasterServices.BuildingType_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ConstructionClass_MasterServices.ConstClass_MasterService;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.el.PropertyNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import com.GAssociatesWeb.GAssociates.Service.PropertySurveyServices.SurveyDataService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = "/3gSurvey")
public class PropertySurveyController {


//    @Autowired
//    private SurveyDataService surveydataservice;

    Logger logger = LoggerFactory.getLogger(getClass());

    private final UserAccessService userAccessService;
    private final PropertyManagement_Service propertyManagement_service;
    private final PropertyOldDetails_Service propertyOldDetails_service;
    private final PropClassification_MasterService propClassification_masterService;
    private final PropSubClassification_MasterService propSubClassification_masterService;
    private final PropertyUsageType_MasterService propertyUsageType_masterService;
    private final Ward_MasterService ward_MasterService;
    private final Zone_MasterService zone_MasterService;
    private final OldWard_MasterService oldWard_MasterService;
    private final Occupancy_MasterService occupancy_MasterService;
    private final OwnerType_MasterService ownerType_MasterService;
    private final BuildStatus_MasterService buildStatus_MasterService;
    private final BuildingType_MasterService buildingType_MasterService;
    private final Sewerage_MasterService sewerage_MasterService;
    private final WaterConnection_MasterService waterConnection_MasterService;
    private final UnitNo_MasterService unitNo_MasterService;
    private final UnitFloorNo_MasterService unitFloorNo_MasterService;
    private final UnitUsageType_MasterService unitUsageType_MasterService;
    private final UnitUsageSubType_MasterService unitUsageSubType_MasterService;
    private final ConstClass_MasterService constClass_MasterService;
    private final PropertySubUsageType_MasterService propertySubUsageType_MasterService;
    private final BuildingSubType_MasterService buildingSubType_MasterService;
    private final OwnerCategory_MasterService ownerCategory_masterService;
    private final Remarks_MasterService remarks_masterService;
    private final RoomType_MasterService roomType_masterService;
    private final AssessmentDate_MasterService assessmentDate_masterService;
    private final PropertyDetails_Repository propertyDetails_repository;
    private final PropertyDeletionLog_Repository propertyDeletionLog_repository;

    public PropertySurveyController(UserAccessService userAccessService, PropertyManagement_Service propertyManagement_service,
                                    PropertyOldDetails_Service propertyOldDetails_service, PropClassification_MasterService propClassification_MasterService,
                                    PropSubClassification_MasterService propSubClassification_MasterService, PropertyUsageType_MasterService propertyUsageType_MasterService,
                                    Ward_MasterService ward_MasterService, Zone_MasterService zone_MasterService, OldWard_MasterService oldWard_MasterService,
                                    Occupancy_MasterService occupancy_MasterService, OwnerType_MasterService ownerType_MasterService, BuildStatus_MasterService buildStatus_MasterService,
                                    BuildingType_MasterService buildingType_MasterService, Sewerage_MasterService sewerage_MasterService, WaterConnection_MasterService waterConnection_MasterService,
                                    UnitNo_MasterService unitNo_MasterService, UnitFloorNo_MasterService unitFloorNo_MasterService, UnitUsageType_MasterService unitUsageType_MasterService,
                                    UnitUsageSubType_MasterService unitUsageSubType_MasterService, ConstClass_MasterService constClass_MasterService, PropertySubUsageType_MasterService propertySubUsageType_MasterService,
                                    BuildingSubType_MasterService buildingSubType_MasterService, OwnerCategory_MasterService ownerCategoryMasterService, Remarks_MasterService remarks_MasterService, RoomType_MasterService roomType_MasterService,
                                    AssessmentDate_MasterService assessmentDate_masterService, PropertyDetails_Repository propertyDetailsRepository, PropertyDeletionLog_Repository propertyDeletionLogRepository) {

        this.userAccessService = userAccessService;
        this.propertyManagement_service = propertyManagement_service;
        this.propertyOldDetails_service = propertyOldDetails_service;
        this.propClassification_masterService = propClassification_MasterService;
        this.propSubClassification_masterService = propSubClassification_MasterService;
        this.propertyUsageType_masterService = propertyUsageType_MasterService;
        this.ward_MasterService = ward_MasterService;
        this.zone_MasterService = zone_MasterService;
        this.oldWard_MasterService = oldWard_MasterService;
        this.occupancy_MasterService = occupancy_MasterService;
        this.ownerType_MasterService = ownerType_MasterService;
        this.buildStatus_MasterService = buildStatus_MasterService;
        this.buildingType_MasterService = buildingType_MasterService;
        this.sewerage_MasterService = sewerage_MasterService;
        this.waterConnection_MasterService = waterConnection_MasterService;
        this.unitNo_MasterService = unitNo_MasterService;
        this.unitFloorNo_MasterService = unitFloorNo_MasterService;
        this.unitUsageType_MasterService = unitUsageType_MasterService;
        this.unitUsageSubType_MasterService = unitUsageSubType_MasterService;
        this.constClass_MasterService = constClass_MasterService;
        this.propertySubUsageType_MasterService = propertySubUsageType_MasterService;
        this.buildingSubType_MasterService = buildingSubType_MasterService;
        this.ownerCategory_masterService = ownerCategoryMasterService;
        this.remarks_masterService = remarks_MasterService;
        this.roomType_masterService = roomType_MasterService;
        this.assessmentDate_masterService = assessmentDate_masterService;
        this.propertyDetails_repository = propertyDetailsRepository;
        this.propertyDeletionLog_repository = propertyDeletionLogRepository;
    }

    @GetMapping(value = "/surveyLogin")
    public String ShowLoginPage() {

        return "3GSurveyLogin";
    }

    @GetMapping(value = "/signup")
    public String ShowSignUpPage() {
        return "3GSignUp";
    }

    //for security rasons we have defined profiles of users here
    @GetMapping("/profiles")
    @ResponseBody
    public List<String> getProfileOptions() {
        return Arrays.asList("ITA", "ITL", "IT", "ACTL", "ACL", "DETL", "DEL");
    }

    @PostMapping("/UserSignup")
    public ModelAndView signupUser(@ModelAttribute UserAccess_Dto userAccessDto,RedirectAttributes redirectAttributes) {
        userAccessService.signup(userAccessDto);
        return new ModelAndView("redirect:/3gSurvey/afterlogin");
    }

    //we are using this to authenticate the users
    @PostMapping(value = "/authenticate")
    public String authenticateUser(HttpServletRequest request, RedirectAttributes redirectAttributes, String username, String password) {
        if (userAccessService.authenticate(username.replace(" ", ""), password.replace(" ", ""))) {
            // Authentication successful, store username in session and redirect to survey form
            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            UserAccess_Dto userProfile = userAccessService.getUserByUsername(username);
            session.setAttribute("userProfile", userProfile);
            return "redirect:/3gSurvey/afterlogin";
        } else {
            // Authentication failed, redirect back to login page
            redirectAttributes.addFlashAttribute("loginError", "Username or password is incorrect.");
            return "redirect:/3gSurvey/surveyLogin"; // You can add a query parameter to indicate authentication failure
        }
    }

    @GetMapping("/getUserProfileFromSession")
    @ResponseBody
    public ResponseEntity<UserAccess_Dto> getUserProfileFromSession(HttpSession session) {
        UserAccess_Dto userProfile = (UserAccess_Dto) session.getAttribute("userProfile");
        if (userProfile == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(userProfile);
    }

    //after successful authentication url will be redirected to below method
    @GetMapping(value = "/afterlogin")
    public String showAfterLogin(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            return "redirect:/3gSurvey/surveyLogin"; // Redirect to login page if not authenticated
        }
        return "3GAfterLogin";
    }

    //if someone is going for new registeration below method will be initialized
    @GetMapping(value = "/form")
    public String showSurveyForm(HttpServletRequest request) {
        // Check if user is authenticated
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            return "redirect:/3gSurvey/surveyLogin"; // Redirect to login page if not authenticated
        }
        return "3GNewOrOldRegisteration";
    }
//    @GetMapping(value = "/editform")
//    public String editForm(HttpServletRequest request){
//        HttpSession session = request.getSession();
//        if (session.getAttribute("username") == null) {
//            return "redirect:/3gSurvey/surveyLogin"; // Redirect to login page if not authenticated
//        }
//        return "3GSearchSurveyForm";
//    }
//    //after successful registration we are sending it to database

    //this method is getting used for searching assessment
    @GetMapping(value = "/searchAssessment")
    public String searchAssessment(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            return "redirect:/3gSurvey/surveyLogin"; // Redirect to login page if not authenticated
        }
        return "3GSearchSurveyForm";
    }
    //search suggestions of old properties
    @GetMapping("/searchProperties")
    public ResponseEntity<List<PropertyOldDetails_Dto>> searchProperties(
            @RequestParam(required = false) String oldPropertyNo,
            @RequestParam(required = false) String ownerName,
            @RequestParam(required = false) String wardNo) {

        List<PropertyOldDetails_Dto> properties = propertyOldDetails_service.searchOldProperties(oldPropertyNo, ownerName, wardNo);

        if (properties.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(properties);
        }
    }

    //search suggestions of new property
    @GetMapping("/searchNewProperties")
    public ResponseEntity<List<PropertyDetails_Dto>> searchProperties(
            @RequestParam(required = false) String surveyPropertyNo,
            @RequestParam(required = false) String ownerName,
            @RequestParam(required = false) Integer wardNo) {

        logger.info(surveyPropertyNo+wardNo+ownerName);
        List<PropertyDetails_Dto> results = propertyManagement_service.searchNewProperties(surveyPropertyNo, ownerName, wardNo);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    //this method is getting used for opening new registeration form
    @GetMapping(value = "/newRegistration")
    public String newRegisteration(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            return "redirect:/3gSurvey/surveyLogin";
        }
        return "3GSurveyForm";
    }

    //this method is for creating new property
    @PostMapping(value = "/submitNewPropertyDetails", consumes = "multipart/form-data")
    public ResponseEntity<?> submitProperty(
            @RequestParam("jsonData") String jsonData,
            @RequestParam(name = "previewPropertyImage", required = false) MultipartFile pdPropimageT,
            @RequestParam(name = "previewPropertyImage2", required = false) MultipartFile pdPropimage2T,
            @RequestParam(name = "previewHousePlan1", required = false) MultipartFile pdHouseplanT,
            @RequestParam(name = "previewHousePlan2", required = false) MultipartFile pdHouseplan2T,
            HttpServletRequest request) throws IOException {


        ObjectMapper objectMapper = new ObjectMapper();

        CompleteProperty_Dto completeProperty_dto;
        try {
            completeProperty_dto = objectMapper.readValue(jsonData, CompleteProperty_Dto.class);

        } catch (IOException e) {
            logger.error("Error parsing JSON data", e);
            return ResponseEntity.badRequest().body("{\"error\":\"Invalid JSON data.\"}");
        }

        // Handle image uploads
        if (pdPropimageT != null && !pdPropimageT.isEmpty()) {
            completeProperty_dto.getPropertyDetails().setPdPropimageT(ImageUtils.convertToBase64(pdPropimageT));
        }

        if (pdHouseplanT != null && !pdHouseplanT.isEmpty()) {
            completeProperty_dto.getPropertyDetails().setPdHouseplanT(ImageUtils.convertToBase64(pdHouseplanT));
        }

        if (pdPropimage2T != null && !pdPropimage2T.isEmpty()) {
            completeProperty_dto.getPropertyDetails().setPdPropimage2T(ImageUtils.convertToBase64(pdPropimage2T));
        }

        if (pdHouseplan2T != null && !pdHouseplan2T.isEmpty()) {
            completeProperty_dto.getPropertyDetails().setPdHouseplan2T(ImageUtils.convertToBase64(pdHouseplan2T));
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.error("No session found for the user.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\":\"User session not found.\"}");
        }

        String username = (String) session.getAttribute("username");
        if (username == null || username.trim().isEmpty()) {
            logger.error("Username not found in session.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\":\"User not logged in.\"}");
        }

        completeProperty_dto.getPropertyDetails().setUser_id(username);
        CompleteProperty_Dto resultDto = propertyManagement_service.createCompleteProperty(completeProperty_dto);

        if (resultDto != null && resultDto.getPropertyDetails() != null && !resultDto.getPropertyDetails().getUnitDetails().isEmpty()) {
            logger.info("Property submission successful for user: {}", username);
            return ResponseEntity.ok("{\"message\":\"Property details submitted successfully.\"}");
        } else {
            logger.error("Property submission failed for user: {}", username);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Failed to submit property details.\"}");
        }
    }

    //this is used to convert the image in base64 string
//    private Optional<String> processImageUpload(MultipartFile file) throws IOException {
//        if (file != null && !file.isEmpty()) {
//            return Optional.of(ImageUtils.convertToBase64(file));
//        }
//        return Optional.empty();
//    }


    //this method is for updating existing property record
    @PatchMapping(value = "/submitUpdatedPropertyDetails", consumes = "multipart/form-data")
    public ResponseEntity<?> updatePropertyDetails(
            @RequestParam("updatedFields") String updatedFieldsJson,
            @RequestParam(value = "propertyImage", required = false) MultipartFile propertyImage,
            @RequestParam(value = "propertyImage2", required = false) MultipartFile propertyImage2,
            @RequestParam(value = "housePlan1", required = false) MultipartFile housePlan1,
            @RequestParam(value = "housePlan2", required = false) MultipartFile housePlan2,
            HttpServletRequest request) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompleteProperty_Dto updatedPropertyDto = objectMapper.readValue(updatedFieldsJson, CompleteProperty_Dto.class);
            if (propertyImage != null) {
                updatedPropertyDto.getPropertyDetails().setPdPropimageT(
                        ImageUtils.convertToBase64(propertyImage)
                );
            }
            if (propertyImage2 != null) {
                updatedPropertyDto.getPropertyDetails().setPdPropimage2T(
                        ImageUtils.convertToBase64(propertyImage2)
                );
            }
            if (housePlan1 != null) {
                updatedPropertyDto.getPropertyDetails().setPdHouseplanT(
                        ImageUtils.convertToBase64(housePlan1)
                );
            }
            if (housePlan2 != null) {
                updatedPropertyDto.getPropertyDetails().setPdHouseplan2T(
                        ImageUtils.convertToBase64(housePlan2)
                );
            }

            HttpSession session = request.getSession(false);
            if (session == null) {
                logger.error("No session found for the user.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\":\"User session not found.\"}");
            }

            String username = (String) session.getAttribute("username");
            if (username == null || username.trim().isEmpty()) {
                logger.error("Username not found in session.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\":\"User not logged in.\"}");
            }

            updatedPropertyDto.getPropertyDetails().setPdApprovedByDesk1Vc(username);
            // Call the service method to update property details
            CompleteProperty_Dto resultDto = propertyManagement_service.updateCompleteProperty(updatedPropertyDto);

            return ResponseEntity.ok(Map.of(
                    "message", "Property details updated successfully.",
                    "updatedProperty", resultDto
            ));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Failed to update property: " + ex.getMessage()
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "An error occurred while updating property details.",
                    "error", ex.getMessage()
            ));
        }
    }


    // Deleting a complete property
    @PostMapping("/deleteNewProperty")
    public ResponseEntity<Map<String, Object>> deleteCompleteProperty(
            @RequestBody Map<String, String> requestData,
            HttpSession session) {

        String pdNewpropertynoVc = requestData.get("pdNewpropertynoVc");
        String ward = requestData.get("ward");
        String surveyPropNo = requestData.get("surveyPropNo");
        String ownerName = requestData.get("ownerName");
        String createdBy = requestData.get("createdBy");
        String remarks = requestData.get("remarks");

        Map<String, Object> response = new HashMap<>();
        try {
            // Retrieve username from session
            String username = (String) session.getAttribute("username");

            // Fetch property details to get images
            CompleteProperty_Dto propertyDetails = propertyManagement_service.getCompletePropertyByNewPropertyNo(pdNewpropertynoVc);

            // Save deletion log
            PropertyDeletionLog deletionLog = new PropertyDeletionLog();
            deletionLog.setSurveyPropNo(surveyPropNo);
            deletionLog.setPdNewpropertynoVc(pdNewpropertynoVc);
            deletionLog.setOwnerName(ownerName);
            deletionLog.setUsername(username);
            deletionLog.setRemarks(remarks);
            deletionLog.setRecordOwner(createdBy);
            deletionLog.setWardnoVc(ward);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss, dd-MM-yyyy");
            String formattedDeletionTime = LocalDateTime.now().format(formatter);
            deletionLog.setDeletionTime(formattedDeletionTime);

            // Set base64 images
            deletionLog.setBase64Image1(propertyDetails.getPropertyDetails().getPdPropimageT());
            deletionLog.setBase64Image2(propertyDetails.getPropertyDetails().getPdPropimage2T());
            deletionLog.setBase64HousePlan1(propertyDetails.getPropertyDetails().getPdHouseplanT());
            deletionLog.setBase64HousePlan2(propertyDetails.getPropertyDetails().getPdHouseplan2T());

            propertyDeletionLog_repository.save(deletionLog);

            // Delete the property detail
            propertyManagement_service.deleteCompleteProperty(pdNewpropertynoVc);

            response.put("success", true);
            response.put("message", "Property deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting property: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // Fetching complete property by survey number
    @GetMapping("/details/{pdSuryPropNo}")
    @ResponseBody // This annotation ensures the response is written directly to the HTTP response body
    public ResponseEntity<?> getCompletePropertyBySurveyNumber(@PathVariable String pdSuryPropNo) {
        try {
            CompleteProperty_Dto property = propertyManagement_service.getCompletePropertyBySurveyNumber(pdSuryPropNo);
            return ResponseEntity.ok(property); // Return the property details as JSON
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to find property with the provided survey number."); // Return 500 Internal Server Error
        }
    }
    //fetching complete property by PdNewPropNo
    @GetMapping("/detailsComplete/{pdNewPropNo}")
    @ResponseBody // This annotation ensures the response is written directly to the HTTP response body
    public ResponseEntity<?> getCompletePropertyByPdNewPropNo(@PathVariable String pdNewPropNo) {
        try {
            CompleteProperty_Dto property = propertyManagement_service.getCompletePropertyByNewPropertyNo(pdNewPropNo);
            return ResponseEntity.ok(property); // Return the property details as JSON
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to find property with the provided survey number."); // Return 500 Internal Server Error
        }
    }

    // Display form for submitting new old property details
    @GetMapping("/oldpropertydetails")
    public String showNewOldPropertyForm(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            return "redirect:/3gSurvey"; // Redirect to login page if not authenticated
        }
        return "3GOldDetailsForm"; // Thymeleaf template for the form
    }

    // Process submission of new old property details
    @PostMapping(path = "/submitPropertyOldDetails", consumes = "application/json")
    public ResponseEntity<?> submitOldPropertyDetails(@RequestBody PropertyOldDetails_Dto oldPropertyDetails_dto, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
        oldPropertyDetails_dto.setPodOldPropNoVc(oldPropertyDetails_dto.getPodOldPropNoVc().replace(" ", ""));
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        oldPropertyDetails_dto.setUser_id(username);
        propertyOldDetails_service.saveOrUpdatePropertyOldDetail(oldPropertyDetails_dto);
        if (oldPropertyDetails_dto.getOldpresent()){
            return ResponseEntity.ok().build();
        }if(oldPropertyDetails_dto.getError()){
            redirectAttributes.addFlashAttribute("Oldpropertyexists", "The Entered old property number already exists.");
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.ok().build();
            // Redirect to a success page
        }
    }

    //this method is getting used for updating old property record
    @PutMapping("/updateOldProperty")
    public ResponseEntity<?> updateOldProperty(@RequestBody PropertyOldDetails_Dto dto) {
        try {
            if (dto.getPodRefNoVc() == null || dto.getPodRefNoVc() <= 0) {
                throw new IllegalArgumentException("Reference number is required for updating.");
            }
        PropertyOldDetails_Dto updatedProperty = propertyOldDetails_service.updatePropertyOldDetails(dto);
            return ResponseEntity.ok(updatedProperty);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/getOldPropertyDetails")
    public String showRegisteredOldProperty() {
        return "3GOldDetailsEditForm";
    }
    // Retrieve and display old property details by id

    @GetMapping(value = "/OldProperty", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> showOldPropertyDetails(@RequestParam String oldPropertyNo, @RequestParam String wardNo) {
        try {
            PropertyOldDetails_Dto oldPropertyDetails = propertyOldDetails_service
                    .getPropertyOldDetailByOldPropertyNoAndWardNo(oldPropertyNo.replace(" ", ""), wardNo)
                    .orElseThrow(() -> new NoSuchElementException("Invalid old property ID: " + oldPropertyNo + " in ward: " + wardNo));

            System.out.println(oldPropertyDetails);
            return ResponseEntity.ok(oldPropertyDetails);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping(value = "/OldPropertyByRefNo", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> getOldPropertyByPodRefNo(@RequestParam Integer podRefNo) {
        try {
            PropertyOldDetails_Dto oldPropertyDetails = propertyOldDetails_service
                    .getPropertyOldDetailByPodRefNo(podRefNo)
                    .orElseThrow(() -> new NoSuchElementException("No property found with podRefNo: " + podRefNo));

            return ResponseEntity.ok(oldPropertyDetails);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Delete old property details by id
    @DeleteMapping("/delete/{oldPropertyNo}/{wardNo}")
    public ResponseEntity<?> deleteOldProperty(@PathVariable String oldPropertyNo, @PathVariable String wardNo) {
        try {
            propertyOldDetails_service.deletePropertyAndUnitsByOldPropertyNoAndWardNo(oldPropertyNo, wardNo);
            return ResponseEntity.ok("Property and related units deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting property: " + e.getMessage());
        }
    }

    // Optional: Success page after submission
    @GetMapping("/success")
    public String showSuccessPage() {
        return "oldPropertyDetailsSuccess";
    }

    // Optional: List all old property details

    @GetMapping("/showsurvey/{id}")
    public String showSurvey(@PathVariable("id") String propertyId, Model model) {
        // You can pass any necessary data to the view here
        model.addAttribute("propertyId", propertyId);
        return "3GViewSurveyFrom"; // Ensure this matches the name of your HTML file (excluding the .html extension)
    }


    //from here i will be getting all picklist field values data below are api's
    //1-for zones:
    @GetMapping("/getAllZones")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAllZones() {
        List<Zone_MasterDto> zones = zone_MasterService.getAllZones();
        List<Map<String, Object>> responseData = zones.stream().map(zone -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", zone.getZoneNo()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //2-for old wards:
    @GetMapping("/getAllOldWards")
    public ResponseEntity<List<Map<String, Object>>> getAllOldWards() {
        List<OldWard_MasterDto> oldWards = oldWard_MasterService.findAllOldWardMasters();
        List<Map<String, Object>> responseData = oldWards.stream().map(oldward -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", oldward.getOldwardno()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //3-for wards:
    @GetMapping("/getAllWards")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAllWards() {
        List<Ward_MasterEntity> wards = ward_MasterService.getAllWards();
        List<Map<String, Object>> responseData = wards.stream().map(ward -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", ward.getWardNo()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //4-for owner type:
    @GetMapping("/getOwnerType")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAllOwnerTypes() {
        List<OwnerType_MasterDto> ownerTypes = ownerType_MasterService.findAllOwnerTypes();
        List<Map<String, Object>> responseData = ownerTypes.stream().map(ownertype -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", ownertype.getOwnerType()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //5-for building status:
    @GetMapping("/buildstatuses")
    public ResponseEntity<List<Map<String, Object>>> getBuildStatuses() {
        List<BuildStatus_MasterDto> buildStatuses = buildStatus_MasterService.findAllBuildStatuses();
        List<Map<String, Object>> responseData = buildStatuses.stream().map(buildstatus -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", buildstatus.getBuildStatus()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //6-for property types:
    @GetMapping("/propertytypes")
    public ResponseEntity<List<Map<String, Object>>> getClassifications() {
        List<PropClassification_MasterDto> classifications = propClassification_masterService.findAll();
        List<Map<String, Object>> responseData = classifications.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getLocalPropertyTypeName());
            item.put("Standardname", type.getPropertyTypeName());// Assuming PropertyType has getName()
            item.put("value", type.getPcmId()); // Assuming PropertyType has getId()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/propertyusagetypes")
    public ResponseEntity<List<Map<String, Object>>> getPropertyUsageTypes() {
        // Fetch all property usage types
        List<PropUsageType_MasterDto> usageTypes = propertyUsageType_masterService.findAll();
        List<Map<String, Object>> responseData = usageTypes.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getLocalUsagetypeName()); // Assuming PropUsageType_MasterDto has getName()
            item.put("value", type.getPropertyUsageTypeId()); // Assuming PropUsageType_MasterDto has getId()
            return item;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseData);
    }

    //7-for property subtypes:
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

    //8-for usage types:
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

    //9-for usage subtypes:
    @GetMapping("/usageSubtypes/{usageTypeId}")
    public ResponseEntity<List<Map<String, Object>>> getUsageSubtypesByUsageId(@PathVariable("usageTypeId") Integer usageTypeId) {
        // Fetch all usage subtypes by usage type ID
        List<PropSubUsageType_MasterDto> dtos = propertySubUsageType_MasterService.findUsageSubTypesByUsageId(usageTypeId);
        List<Map<String, Object>> responseData = dtos.stream().map(subtype -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", subtype.getUsageTypeLocal()); // Assuming PropUsageSubType_MasterDto has getUsageTypeLocal()
            item.put("value", subtype.getId()); // Assuming PropUsageSubType_MasterDto has getId()
            // You can add more fields to the map as needed
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //10-for building type:
    @GetMapping("/getBuildingTypesByPropertyClassification/{propertyClassificationId}")
    public ResponseEntity<List<Map<String, Object>>> getBuildingTypesByPropertyClassificationId(@PathVariable Integer propertyClassificationId) {
        List<BuildingType_MasterDto> buildingTypeMasterDTOs = buildingType_MasterService.getBuildingTypesByPropertyClassificationId(propertyClassificationId);
        List<Map<String, Object>> responseData = buildingTypeMasterDTOs.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getBtBuildingtypellVc()); // Assuming PropertyType has getName()
            item.put("value", type.getBtBuildingTypeId()); // Assuming PropertyType has getId()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //11-for building subtype:
    @GetMapping("/buildingSubtypes/{buildingTypeId}")
    public ResponseEntity<List<Map<String, Object>>> getBuildingSubTypesByBuildingTypeId(@PathVariable("buildingTypeId") Integer buildingTypeId) {
        List<BuildingSubType_MasterDto> dtos = buildingSubType_MasterService.getBuildingSubTypesByBuildingTypeId(buildingTypeId);
        List<Map<String, Object>> responseData = dtos.stream().map(subtype -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", subtype.getBstBuildingsubtypellVc()); // Assuming BuildingSubType_MasterDto has getBstBuildingsubtypellVc()
            item.put("value", subtype.getBuildingsubtypeid()); // Assuming BuildingSubType_MasterDto has getBuildingsubtypeid()
            // Add other fields as needed
            return item;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseData);
    }

    //12-for sewerage types:
    @GetMapping("/getSewerageTypes")
    public ResponseEntity<List<Map<String, Object>>> listSewerageTypes() {
        List<Sewerage_MasterDto> existingSewerageTypes = sewerage_MasterService.findAll();
        List<Map<String, Object>> responseData = existingSewerageTypes.stream().map(sewerage -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", sewerage.getSewerage()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //13-for water connections:
    @GetMapping("/waterConnections")
    public ResponseEntity<List<Map<String, Object>>> listWaterConnections() {
        List<WaterConnection_MasterDto> waterConnections = waterConnection_MasterService.getAllWaterConnections();
        List<Map<String, Object>> responseData = waterConnections.stream().map(watercon -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", watercon.getWaterConnectionMarathi()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //14-for floor no:
    @GetMapping("/getUnitFloorNos")
    public ResponseEntity<List<Map<String, Object>>> listUnitFloorNoMasters() {
        List<UnitFloorNo_MasterDto> unitFloorNos = unitFloorNo_MasterService.getAllUnitFloorNos();
        List<Map<String, Object>> responseData = unitFloorNos.stream().map(unitfloorno -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", unitfloorno.getUnitfloorno()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //15-for unit no:
    @GetMapping("/unitNumbers")
    public ResponseEntity<List<Map<String, Object>>> getAllUnitNos() {
        List<UnitNo_MasterDto> unitNos = unitNo_MasterService.getAllUnitNoMasters();
        List<Map<String, Object>> responseData = unitNos.stream().map(unitNo -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", unitNo.getId()); // Assuming UnitNo_MasterDto has getId()
            item.put("unitno", unitNo.getUnitno()); // Assuming UnitNo_MasterDto has getUnitno()
            // Directly include any additional mappings you need here
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //16-for unit usage type:
    @GetMapping("/getUnitUsageByPropUsageId/{propUsageId}")
    public ResponseEntity<List<Map<String, Object>>> findAllByPropUsageId(@PathVariable Integer propUsageId) {
        List<UnitUsageType_MasterDto> dtos = unitUsageType_MasterService.findAllByPropUsageId(propUsageId);
        List<Map<String, Object>> responseData = dtos.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getUum_usagetypell_vc()); // Assuming PropertyType has getName()
            item.put("value", type.getUum_usageid_i()); // Assuming PropertyType has getId()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //17-for unit usage subtype:
    @GetMapping("/getUnitUsageSub/{unitUsageId}")
    public ResponseEntity<List<Map<String, Object>>> getUnitUsageSubTypesByUsageId(@PathVariable Integer unitUsageId) {
        List<UnitUsageSubType_MasterDto> unitUsageSubTypeMasterDtos = unitUsageSubType_MasterService.findByUnitUsageId(unitUsageId);
        List<Map<String, Object>> responseData = unitUsageSubTypeMasterDtos.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getUsm_usagetypell_vc()); // Assuming PropertyType has getName()
            item.put("value", type.getUsm_usagesubid_i()); // Assuming PropertyType has getId()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //18-for class of property:
    @GetMapping("/constructionClassMasters")
    public ResponseEntity<List<Map<String, Object>>> getAllConstructionClassMasters() {
        List<ConstructionClass_MasterDto> dtos = constClass_MasterService.findAll();
        List<Map<String, Object>> responseData = dtos.stream().map(constclass -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", constclass.getCcm_classnamell_vc()); // Assuming Zone_MasterDto has getZoneNo()
            item.put("value", constclass.getCcm_conclassid_i());
            item.put("deduction", constclass.getCcm_percentageofdeduction_i());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //19-for occupancies:
    @GetMapping("/occupancyMasters")
    public ResponseEntity<List<Map<String, Object>>> getAllOccupancyMasters() {
        List<Occupancy_MasterDto> dtos = occupancy_MasterService.findAllOccupancy_Masters();
        List<Map<String, Object>> responseData = dtos.stream().map(type -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", type.getOccupancy()); // Assuming PropertyType has getName()
            item.put("value", type.getOccupancy_id()); // Assuming PropertyType has getId()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //20-for ownerCategory
    @GetMapping("/ownerCategories")
    public ResponseEntity<List<Map<String, Object>>> getOwnerCategories() {
        List<OwnerCategory_MasterDto> ownerCategoryMasterDtos = ownerCategory_masterService.findAll();
        List<Map<String, Object>> responseData = ownerCategoryMasterDtos.stream().map(ownertype -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", ownertype.getOwnerCategory()); // Assuming Zone_MasterDto has getZoneNo()
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/getAllAssessmentDates")
    public ResponseEntity<List<AssessmentDate_MasterDto>> getAllAssessmentDates() {
        List<AssessmentDate_MasterDto> assessmentDates = assessmentDate_masterService.getAllAssessmentDates();
        return new ResponseEntity<>(assessmentDates, HttpStatus.OK);
    }

    @GetMapping("/roomTypes")
    public ResponseEntity<List<Map<String, Object>>> getAllRoomTypes() {
        List<RoomType_MasterDto> roomTypeMasterDtos = roomType_masterService.findAll();
        List<Map<String, Object>> responseData = roomTypeMasterDtos.stream().map(roomType -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", roomType.getRoomType());
            item.put("value", roomType.getRoomSelected());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    //now will create this section for getting names with id
    @GetMapping("/propertyTypes/{id}")
    public ResponseEntity<Map<String, Object>> getPropertyTypeById(@PathVariable("id") Integer id) {
        Optional<PropClassification_MasterDto> dto = propClassification_masterService.findByPcmClassidI(id);
        if (dto.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", dto.get().getPcmId());
            response.put("name", dto.get().getLocalPropertyTypeName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/propertySubtype/{id}")
    public ResponseEntity<Map<String, Object>> getPropertySubtypeById(@PathVariable("id") Integer id) {
        Optional<PropSubClassification_MasterDto> dto = propSubClassification_masterService.findByPsmSubclassidI(id);
        if (dto.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", dto.get().getPropertySubClassificationId());
            response.put("name", dto.get().getLocalPropertySubtypeName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

    }
    @GetMapping("/PropertyUsage/{id}")
    public ResponseEntity<Map<String, Object>> getPropertyUsageById(@PathVariable("id") Integer id) {
        Optional<PropUsageType_MasterDto> dto = propertyUsageType_masterService.findById(id);
        if (dto.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", dto.get().getPropertyUsageTypeId());
            response.put("name", dto.get().getLocalUsagetypeName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/PropertySubUsage/{id}")
    public ResponseEntity<Map<String, Object>> getPropertySubUsageById(@PathVariable("id") Integer id) {
        Optional<PropSubUsageType_MasterDto> dto = propertySubUsageType_MasterService.findById(id);
        if (dto.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", dto.get().getId());
            response.put("name", dto.get().getUsageTypeLocal());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buildingType/{id}")
    public ResponseEntity<Map<String, Object>> getbuildingTypeById(@PathVariable("id") Integer id) {
        Optional<BuildingType_MasterDto> dto = buildingType_MasterService.findById(id);
        if (dto.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", dto.get().getBtBuildingTypeId());
            response.put("name", dto.get().getBtBuildingtypellVc());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/buildingSubType/{id}")
    public ResponseEntity<Map<String, Object>> getbuildingSubTypeById(@PathVariable("id") Integer id) {
        Optional<BuildingSubType_MasterDto> dto = buildingSubType_MasterService.findById(id);
        if (dto.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", dto.get().getBuildingsubtypeid());
            response.put("name", dto.get().getBstBuildingsubtypellVc());
            response.put("data", dto.get()); // include all DTO data if needed
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/unitUsageType/{id}")
    public ResponseEntity<Map<String, Object>> getunitUsageTypeById(@PathVariable("id") Integer id) {
        Optional<UnitUsageType_MasterDto> dto = unitUsageType_MasterService.findById(id);
        if (dto.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", dto.get().getUum_usageid_i());
            response.put("name", dto.get().getUum_usagetypell_vc());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/unitSubUsageType/{id}")
    public ResponseEntity<Map<String, Object>> getunitSubUsageTypeById(@PathVariable("id") Integer id) {
        Optional<UnitUsageSubType_MasterDto> dto = unitUsageSubType_MasterService.findById(id);
        if (dto.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", dto.get().getUsm_usagesubid_i());
            response.put("name", dto.get().getUsm_usagetypell_vc());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/occupantStatus/{id}")
    public ResponseEntity<Map<String, Object>> getoccupantStatusById(@PathVariable("id") Integer id) {
        Optional<Occupancy_MasterDto> dto = occupancy_MasterService.findById(id);
        if (dto.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", dto.get().getOccupancy_id());
            response.put("name", dto.get().getOccupancy());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAllRemarks")
    public ResponseEntity<List<Map<String, Object>>> listAllRemarks() {
        List<Remarks_MasterDto> existingRemarks = remarks_masterService.getAllRemarks();
        List<Map<String, Object>> responseData = existingRemarks.stream().map(remark -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", remark.getRemark());
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/checkSurveyPropNo")
    public ResponseEntity<Boolean> checkSurveyPropNo(@RequestParam String surveyPropNo, @RequestParam Integer ward) {
        boolean exists = propertyDetails_repository.findByPdSurypropnoVcAndPdWardI(surveyPropNo, ward).isPresent();
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/editSurveyForm")
    public String editForm(@RequestParam("newpropertyno") String newPropertyNo, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            return "redirect:/3gSurvey/surveyLogin"; // Redirect to login page if not authenticated
        }

        // Fetch property details using the newpropertyno
        CompleteProperty_Dto property = propertyManagement_service.getCompletePropertyByNewPropertyNo(newPropertyNo);
        if (property == null) {
            throw new PropertyNotFoundException("Property with number " + newPropertyNo + " not found.");
        }

        // Pass property details to the template
        model.addAttribute("property", property);

        return "3GEditSurveyForm";
    }

}
