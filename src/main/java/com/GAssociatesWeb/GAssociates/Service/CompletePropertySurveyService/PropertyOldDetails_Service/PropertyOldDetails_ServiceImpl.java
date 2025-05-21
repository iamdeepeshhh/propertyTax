package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyOldDetails_Service;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyOldDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitOldDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyOldDetails_Entity.PropertyOldDetails_Entity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyOldDetails_Entity.UnitOldDetails_Entity;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.PropertyOldDetails_Repository;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.UnitOldDetails_Repository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PropertyOldDetails_ServiceImpl implements PropertyOldDetails_Service{


    private final PropertyOldDetails_Repository propertyOldDetails_repository;
    private final UnitOldDetails_Repository unitOldDetails_repository;
    private final SequenceService sequenceService;
    private static final Pattern CONTAINS_DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    @Autowired
    public PropertyOldDetails_ServiceImpl(PropertyOldDetails_Repository propertyOldDetails_repository, UnitOldDetails_Repository unitOldDetails_Repository, SequenceService sequenceService) {
        this.propertyOldDetails_repository = propertyOldDetails_repository;
        this.unitOldDetails_repository = unitOldDetails_Repository;
        this.sequenceService = sequenceService;
    }

//    @Override
//    public PropertyOldDetails_Dto savePropertyOldDetail(PropertyOldDetails_Dto dto) throws Exception {
//        System.out.println(dto.getUser_id()+"in service");
//        PropertyOldDetails_Entity entity = convertToEntity(dto);
//        sequenceService.resetSequenceIfTableIsEmpty("property_olddetails", "property_olddetails_pod_refno_vc_seq");
//            Integer nextId = sequenceService.getNextSequenceValue("property_olddetails_pod_refno_vc_seq");
//            entity.setPodRefNoVc(nextId);
//        PropertyOldDetails_Entity savedentity = propertyOldDetails_repository.save(entity);
//        return convertToDto(savedentity);
//    }

    @Override
    public Optional<PropertyOldDetails_Dto> getPropertyOldDetailByPodRefNo(Integer podRefNo) {
        System.out.println("this is refno:"+ podRefNo);
        return propertyOldDetails_repository.findByPodRefNoVc(podRefNo)
                .map(this::convertToDto); // Convert entity to DTO
    }

    @Override
    public PropertyOldDetails_Dto updatePropertyOldDetails(PropertyOldDetails_Dto dto){
        if (dto.getId() == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        // Fetch the existing entity by ID (primary key)
        Optional<PropertyOldDetails_Entity> existingEntityOptional = propertyOldDetails_repository.findById(dto.getId());
        if (!existingEntityOptional.isPresent()) {
            throw new NoSuchElementException("No property found with ID: " + dto.getId());
        }

        PropertyOldDetails_Entity existingEntity = existingEntityOptional.get();

        // Ensure podRefNoVc remains immutable
        if (!dto.getPodRefNoVc().equals(existingEntity.getPodRefNoVc())) {
            throw new IllegalArgumentException("podRefNoVc cannot be changed");
        }

        // Update fields from the DTO
        existingEntity.setPodZoneI(dto.getPodZoneI());
        existingEntity.setPodWardI(dto.getPodWardI());
        existingEntity.setPodNewPropertyNoVc(dto.getPodNewPropertyNoVc());
        existingEntity.setPodOwnerNameVc(dto.getPodOwnerNameVc());
        existingEntity.setPodOccupierNameVc(dto.getPodOccupierNameVc());
        existingEntity.setPodPropertyAddressVc(dto.getPodPropertyAddressVc());
        existingEntity.setPodConstClassVc(dto.getPodConstClassVc());
        existingEntity.setPodPropertyTypeI(dto.getPodPropertyTypeI());
        existingEntity.setPodPropertySubTypeI(dto.getPodPropertySubTypeI());
        existingEntity.setPodUsageTypeI(dto.getPodUsageTypeI());
        existingEntity.setPodNoofrooms(dto.getPodNoofrooms());
        existingEntity.setPodPlotvalue(dto.getPodPlotvalue());
        existingEntity.setPodTotalPropertyvalue(dto.getPodTotalPropertyvalue());
        existingEntity.setPodBuildingvalue(dto.getPodBuildingvalue());
        existingEntity.setPodBuiltUpAreaFl(dto.getPodBuiltUpAreaFl());
        existingEntity.setPodLastAssDtDt(dto.getPodLastAssDtDt());
        existingEntity.setPodCurrentAssDt(dto.getPodCurrentAssDt());
        existingEntity.setPodTotalTaxFl(dto.getPodTotalTaxFl());
        existingEntity.setPodPropertyTaxFl(dto.getPodPropertyTaxFl());
        existingEntity.setPodEduCessFl(dto.getPodEduCessFl());
        existingEntity.setPodEgcFl(dto.getPodEgcFl());
        existingEntity.setPodTreeTaxFl(dto.getPodTreeTaxFl());
        existingEntity.setPodEnvTaxFl(dto.getPodEnvTaxFl());
        existingEntity.setPodFireTaxFl(dto.getPodFireTaxFl());
        existingEntity.setPdNewPropertyNoVc(dto.getPdNewPropertyNoVc());
        existingEntity.setPdConstructionYearD(dto.getPdConstructionYearD());
        existingEntity.setUserid(dto.getUser_id());
        existingEntity.setPodOldPropNoVc(dto.getPodOldPropNoVc());
        existingEntity.setPodTotalAssessmentArea(dto.getPodTotalAssessmentArea());
        existingEntity.setPodTotalAssessmentAreaFt(dto.getPodTotalAssessmentAreaFt());
        existingEntity.setPodTotalRatableValue(dto.getPodTotalRatableValue());

        updateUnitDetails(dto.getUnitDetails(), existingEntity);
        // Save the updated entity
        PropertyOldDetails_Entity updatedEntity = propertyOldDetails_repository.save(existingEntity);

        // Convert the updated entity back to a DTO and return it
        return convertToDto(updatedEntity);
    }


    @Override
    @Transactional
    public PropertyOldDetails_Dto saveOrUpdatePropertyOldDetail(PropertyOldDetails_Dto dto) throws Exception {
        if (dto.getPodOldPropNoVc() == null || dto.getPodOldPropNoVc().isEmpty()) {
            throw new IllegalArgumentException("podOldPropNoVc cannot be null or empty");
        }
        PropertyOldDetails_Entity entity;

        if (dto.getUpdateFlag() == 1) {
            // Update logic
            System.out.println(dto.getUpdateFlag() + "update flag is green");
            String oldpropno = dto.getPodOldPropNoVc();
            String ward = dto.getPodWardI();
            System.out.println("dto: " + dto);
            Optional<PropertyOldDetails_Entity> existingEntity = propertyOldDetails_repository.findByPodOldPropNoVcAndPodWardI(oldpropno, ward);
            System.out.println("this is existing entity: " + existingEntity);
            if (existingEntity.isPresent()) {
                entity = existingEntity.get();
                dto.setOldpresent(true);
            } else {
                throw new Exception("Entity to update not found with old property number: " + dto.getPodOldPropNoVc());
            }
        } else {
            // Create logic
            Optional<PropertyOldDetails_Entity> existingEntity = propertyOldDetails_repository.findByPodOldPropNoVcAndPodWardI(dto.getPodOldPropNoVc(), dto.getPodWardI());
            if (existingEntity.isPresent()) {
                dto.setError(true);
                throw new Exception("An entity with this old property number already exists: " + dto.getPodOldPropNoVc());
            }
            entity = new PropertyOldDetails_Entity();

            // If your sequence logic is needed for new entries, keep it here
            if (propertyOldDetails_repository.count() == 0) {
                sequenceService.resetSequenceIfTableIsEmpty("property_olddetails", "property_olddetails_pod_refno_vc_seq");
                sequenceService.resetSequenceIfTableIsEmpty("property_olddetails", "property_olddetails_id_seq");
            }

            Integer nextId = sequenceService.getNextSequenceValue("property_olddetails_pod_refno_vc_seq");
            entity.setPodRefNoVc(nextId);
        }

        // Populate entity fields
        entity.setPodZoneI(dto.getPodZoneI());
        entity.setPodWardI(dto.getPodWardI());
        entity.setPodNewPropertyNoVc(dto.getPodNewPropertyNoVc());
        entity.setPodOwnerNameVc(dto.getPodOwnerNameVc());
        entity.setPodOccupierNameVc(dto.getPodOccupierNameVc());
        entity.setPodPropertyAddressVc(dto.getPodPropertyAddressVc());
        entity.setPodConstClassVc(dto.getPodConstClassVc());
        entity.setPodPropertyTypeI(dto.getPodPropertyTypeI());
        entity.setPodPropertySubTypeI(dto.getPodPropertySubTypeI());
        entity.setPodUsageTypeI(dto.getPodUsageTypeI());
        entity.setPodNoofrooms(dto.getPodNoofrooms());
        entity.setPodPlotvalue(dto.getPodPlotvalue());
        entity.setPodTotalPropertyvalue(dto.getPodTotalPropertyvalue());
        entity.setPodBuildingvalue(dto.getPodBuildingvalue());
        entity.setPodBuiltUpAreaFl(dto.getPodBuiltUpAreaFl());
        entity.setPodLastAssDtDt(dto.getPodLastAssDtDt());
        entity.setPodCurrentAssDt(dto.getPodCurrentAssDt());
        entity.setPodTotalTaxFl(dto.getPodTotalTaxFl());
        entity.setPodPropertyTaxFl(dto.getPodPropertyTaxFl());
        entity.setPodEduCessFl(dto.getPodEduCessFl());
        entity.setPodEgcFl(dto.getPodEgcFl());
        entity.setPodTreeTaxFl(dto.getPodTreeTaxFl());
        entity.setPodEnvTaxFl(dto.getPodEnvTaxFl());
        entity.setPodFireTaxFl(dto.getPodFireTaxFl());
        entity.setPdNewPropertyNoVc(dto.getPdNewPropertyNoVc());
        entity.setPdConstructionYearD(dto.getPdConstructionYearD());
        entity.setUserid(dto.getUser_id());
        entity.setPodOldPropNoVc(dto.getPodOldPropNoVc());
        entity.setPodTotalAssessmentArea(dto.getPodTotalAssessmentArea());
        entity.setPodTotalAssessmentAreaFt(dto.getPodTotalAssessmentAreaFt());
        entity.setPodTotalRatableValue(dto.getPodTotalRatableValue());

        updateUnitDetails(dto.getUnitDetails(), entity);
        PropertyOldDetails_Entity savedEntity = propertyOldDetails_repository.save(entity);
        return convertToDto(savedEntity);
    }


    @Override
    public void saveExcel(MultipartFile file) {
        try {
            System.out.println("Starting to process the file");
            List<PropertyOldDetails_Entity> propertyOldDetailsList = excelToPropertyOldDetails(file.getInputStream());
            System.out.println("Parsed Excel file, now saving to repository");
            propertyOldDetails_repository.saveAll(propertyOldDetailsList);
            System.out.println("Saved all records successfully");
        } catch (IOException e) {
            System.err.println("Error processing the file: " + e.getMessage());
            throw new RuntimeException("Fail to store excel data: " + e.getMessage());
        }
    }

    private void updateUnitDetails(List<UnitOldDetails_Dto> unitDtos, PropertyOldDetails_Entity propertyEntity) {
        if (unitDtos != null) {
            unitDtos.forEach(dto -> {
                UnitOldDetails_Entity unitEntity;
                if (dto.getId() != null) {
                    // Fetch existing unit
                    unitEntity = unitOldDetails_repository.findById(dto.getId())
                            .orElseThrow(() -> new RuntimeException("Unit not found with ID: " + dto.getId()));
                } else {
                    // Create new unit if ID is not provided
                    unitEntity = new UnitOldDetails_Entity();
                }

                // Update the unit entity with DTO details
                updateUnitEntityFromDto(dto, unitEntity);

                // Set foreign keys or reference to the parent entity
                unitEntity.setPropertyOldDetails(propertyEntity);
                unitEntity.setPodOldPropNoVc(propertyEntity.getPodOldPropNoVc());
                unitEntity.setPodWardI(propertyEntity.getPodWardI());

                // Save the updated or new unit
                unitOldDetails_repository.save(unitEntity);
            });
        }
    }

    private void updateUnitEntityFromDto(UnitOldDetails_Dto dto, UnitOldDetails_Entity unit) {
        // Assume the unit is already created or fetched, just update its properties
        unit.setOccupancy(dto.getPodOccupancy());
        unit.setFloorNumber(dto.getPodFloorNumber());
        unit.setOldPropertyClass(dto.getPodOldPropertyClass());
        unit.setConstructionYear(dto.getPodConstructionYear());
        unit.setUnitUsageType(dto.getPodUnitUsageType());
        unit.setUnitSubUsageType(dto.getPodUnitSubUsageType());
        unit.setAssessmentArea(dto.getPodAssessmentArea());
    }
    private UnitOldDetails_Entity convertUnitDtoToEntity(UnitOldDetails_Dto dto) {
        UnitOldDetails_Entity unit = new UnitOldDetails_Entity();
        unit.setOccupancy(dto.getPodOccupancy());
        unit.setFloorNumber(dto.getPodFloorNumber());
        unit.setOldPropertyClass(dto.getPodOldPropertyClass());
        unit.setConstructionYear(dto.getPodConstructionYear());
        unit.setUnitUsageType(dto.getPodUnitUsageType());
        unit.setUnitSubUsageType(dto.getPodUnitSubUsageType());
        unit.setAssessmentArea(dto.getPodAssessmentArea());
        return unit;
    }

    private UnitOldDetails_Dto convertUnitEntityToDto(UnitOldDetails_Entity entity) {
        UnitOldDetails_Dto dto = new UnitOldDetails_Dto();
        dto.setPodOccupancy(entity.getOccupancy());
        dto.setPodFloorNumber(entity.getFloorNumber());
        dto.setPodOldPropertyClass(entity.getOldPropertyClass());
        dto.setPodConstructionYear(entity.getConstructionYear());
        dto.setPodUnitUsageType(entity.getUnitUsageType());
        dto.setPodUnitSubUsageType(entity.getUnitSubUsageType());
        dto.setPodAssessmentArea(entity.getAssessmentArea());
        dto.setId(entity.getId());
        return dto;
    }
    @Override
    public PropertyOldDetails_Dto updatePropertyOldDetail(Integer id, PropertyOldDetails_Dto dto) {
        PropertyOldDetails_Entity entity = propertyOldDetails_repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property detail not found for id: " + id));
        // Update entity with dto values
        entity = propertyOldDetails_repository.save(entity);
        return convertToDto(entity);
    }

    @Override
    public Optional<PropertyOldDetails_Dto> getPropertyOldDetailByOldPropertyNo(String oldPropertyNo) {
        return Optional.empty();
    }

    @Override
    public Optional<PropertyOldDetails_Dto> getPropertyOldDetailByOldPropertyNoAndWardNo(String oldPropertyNo, String ward) {
        Optional<PropertyOldDetails_Entity> propertyEntity = propertyOldDetails_repository.findByPodOldPropNoVcAndPodWardI(oldPropertyNo, ward);
        if (propertyEntity.isPresent()) {
            PropertyOldDetails_Entity entity = propertyEntity.get();

            // Fetch unit details using the reference number
            List<UnitOldDetails_Entity> units = unitOldDetails_repository.findByPropertyOldDetails_PodRefNoVc(entity.getPodRefNoVc());

            // Convert the property and unit details to DTO
            PropertyOldDetails_Dto propertyDto = convertToDto(entity);
            List<UnitOldDetails_Dto> unitDtos = units.stream()
                    .map(this::convertUnitEntityToDto)
                    .collect(Collectors.toList());

            propertyDto.setUnitDetails(unitDtos);
            return Optional.of(propertyDto);
        }
        return Optional.empty();
    }

    public List<PropertyOldDetails_Dto> searchOldProperties(String oldPropertyNo, String ownerName, String wardNo) {
        // Search by old property number and ward number
        if (oldPropertyNo != null && !oldPropertyNo.trim().isEmpty() && wardNo != null && !wardNo.trim().isEmpty()) {
            Optional<PropertyOldDetails_Entity> properties = propertyOldDetails_repository.findByPodOldPropNoVcAndPodWardI(oldPropertyNo, wardNo);
            return properties.stream().map(this::convertToDto).collect(Collectors.toList());
        }
        // Search by owner name and ward number
        else if (ownerName != null && !ownerName.trim().isEmpty() && wardNo != null && !wardNo.trim().isEmpty()) {
            List<PropertyOldDetails_Entity> properties = propertyOldDetails_repository.findByPodOwnerNameVcContainingIgnoreCaseAndPodWardI(ownerName, wardNo);
            return properties.stream().map(this::convertToDto).collect(Collectors.toList());
        }
        // Search by old property number only
        else if (oldPropertyNo != null && !oldPropertyNo.trim().isEmpty()) {
            List<PropertyOldDetails_Entity> properties = propertyOldDetails_repository.findByPodOldPropNoVcContainingIgnoreCase(oldPropertyNo);
            return properties.stream().map(this::convertToDto).collect(Collectors.toList());
        }
        // Search by owner name only
        else if (ownerName != null && !ownerName.trim().isEmpty()) {
            List<PropertyOldDetails_Entity> properties = propertyOldDetails_repository.findByPodOwnerNameVcContainingIgnoreCase(ownerName);
            return properties.stream().map(this::convertToDto).collect(Collectors.toList());
        }
        // Search by ward number only
        else if (wardNo != null && !wardNo.trim().isEmpty()) {
            List<PropertyOldDetails_Entity> properties = propertyOldDetails_repository.findByPodWardI(wardNo);
            return properties.stream().map(this::convertToDto).collect(Collectors.toList());
        }
        // Return empty list if no search criteria provided
        else {
            return List.of();
        }
    }


    @Override
    public List<PropertyOldDetails_Dto> getAllPropertyOldDetails() {
        return propertyOldDetails_repository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public void deletePropertyAndUnitsByOldPropertyNoAndWardNo(String oldPropertyNo, String wardNo) {
        Optional<PropertyOldDetails_Entity> propertyOptional = propertyOldDetails_repository.findByPodOldPropNoVcAndPodWardI(oldPropertyNo, wardNo);

        if (propertyOptional.isPresent()) {
            PropertyOldDetails_Entity property = propertyOptional.get();
            Integer podRefNo = property.getPodRefNoVc();
            unitOldDetails_repository.deleteByPropertyOldDetailsRefNo(String.valueOf(podRefNo));
            propertyOldDetails_repository.delete(property); // Adjusted to use delete operation on the entity
        } else {
            // Handle the case where the property isn't found if necessary
            System.out.println("No property found with Old Property No: " + oldPropertyNo + " and Ward No: " + wardNo);
        }
    }

    public List<PropertyOldDetails_Entity> excelToPropertyOldDetails(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0); // Read the first sheet
            Iterator<Row> rows = sheet.iterator();

            List<PropertyOldDetails_Entity> propertyOldDetailsList = new ArrayList<>();
            int rowNumber = 0;

            // Iterate over all rows
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                rowNumber++; // Track the current row number

                // Skip the header row
                if (rowNumber == 1) continue;

                PropertyOldDetails_Entity propertyOldDetails = new PropertyOldDetails_Entity();
                boolean isRowValid = true;  // Flag to track if the row has invalid data

                // Process each cell in the current row
                for (int cellIdx = 0; cellIdx <= 29; cellIdx++) {
                    Cell currentCell = currentRow.getCell(cellIdx);
                    String cellValue = getCellValueAsString(currentCell).trim(); // Trim to remove extra spaces

                    // Validate the "Old Property Number" field
                    if (cellIdx == 0) {
                        if (cellValue.isEmpty()) {
                            System.err.println("Error processing row " + rowNumber + ": Old Property Number (pod_oldpropno_vc) cannot be null or empty.");
                            printRowValues(currentRow);  // Print all the values in this row
                            isRowValid = false;  // Mark this row as invalid
                            break;  // Skip further processing of this row
                        }
                        propertyOldDetails.setPodOldPropNoVc(cellValue);
                    } else {
                        // Process other fields based on cell index
                        switch (cellIdx) {
                            case 1:
                                propertyOldDetails.setPodWardI(cellValue);
                                break;
                            case 2:
                                propertyOldDetails.setPodZoneI(cellValue);
                                break;
                            case 3:
                                propertyOldDetails.setPodOwnerNameVc(cellValue);
                                break;
                            case 4:
                                propertyOldDetails.setPodOccupierNameVc(cellValue);
                                break;
                            case 5:
                                propertyOldDetails.setPodPropertyAddressVc(cellValue);
                                break;
                            case 6:
                                propertyOldDetails.setPodConstClassVc(cellValue);
                                break;
                            case 7:
                                propertyOldDetails.setPodPropertyTypeI(cellValue);
                                break;
                            case 8:
                                propertyOldDetails.setPodPropertySubTypeI(cellValue);
                                break;
                            case 9:
                                propertyOldDetails.setPodUsageTypeI(cellValue);
                                break;
                            case 10:
                                propertyOldDetails.setPodNoofrooms(cellValue);
                                break;
                            case 11:
                                propertyOldDetails.setPodPlotvalue(cellValue);
                                break;
                            case 12:
                                propertyOldDetails.setPodTotalPropertyvalue(cellValue);
                                break;
                            case 13:
                                propertyOldDetails.setPodBuildingvalue(cellValue);
                                break;
                            case 14:
                                propertyOldDetails.setPodBuiltUpAreaFl(cellValue);
                                break;
                            case 15:
                                propertyOldDetails.setPodLastAssDtDt(cellValue);
                                break;
                            case 16:
                                propertyOldDetails.setPodCurrentAssDt(cellValue);
                                break;
                            case 17:
                                propertyOldDetails.setPodTotalTaxFl(cellValue);
                                break;
                            case 18:
                                propertyOldDetails.setPodPropertyTaxFl(cellValue);
                                break;
                            case 19:
                                propertyOldDetails.setPodEduCessFl(cellValue);
                                break;
                            case 20:
                                propertyOldDetails.setPodEgcFl(cellValue);
                                break;
                            case 21:
                                propertyOldDetails.setPodTreeTaxFl(cellValue);
                                break;
                            case 22:
                                propertyOldDetails.setPodEnvTaxFl(cellValue);
                                break;
                            case 23:
                                propertyOldDetails.setPodFireTaxFl(cellValue);
                                break;
                            case 24:
                                propertyOldDetails.setPodNewPropertyNoVc(cellValue);
                                break;
                            case 25:
                                propertyOldDetails.setPdConstructionYearD(cellValue);
                                break;
                            case 26:
                                propertyOldDetails.setUserid(cellValue);
                                break;
                            case 27:
                                propertyOldDetails.setPodTotalAssessmentArea(cellValue);
                                break;
                            case 28:
                                propertyOldDetails.setPodTotalRatableValue(cellValue);
                                break;
                            case 29:
                                propertyOldDetails.setPodTotalAssessmentAreaFt(cellValue);
                                break;
                            default:
                                break;
                        }
                    }
                }

                // If row is invalid, skip adding it to the list
                if (!isRowValid) {
                    continue;
                }

                // Set creation and update dates
                propertyOldDetails.setPodCreatedDt(LocalDateTime.now());
                propertyOldDetails.setPodUpdatedDt(LocalDateTime.now());
                propertyOldDetails.setPodRefNoVc(sequenceService.getNextSequenceValue("property_olddetails_pod_refno_vc_seq"));

                // Add valid property details to the list
                propertyOldDetailsList.add(propertyOldDetails);
            }

            return propertyOldDetailsList;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }

    private void printRowValues(Row row) {
        System.out.println("Values in row " + row.getRowNum() + ": ");
        for (Cell cell : row) {
            String cellValue = getCellValueAsString(cell).trim();
            System.out.print(cellValue + " | ");  // Print the value of each cell
        }
        System.out.println();  // Move to the next line after printing all cells
    }
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            case ERROR:
            default:
                return "";
        }
    }

    private PropertyOldDetails_Dto convertToDto(PropertyOldDetails_Entity entity) {
        PropertyOldDetails_Dto dto = new PropertyOldDetails_Dto();
        dto.setId(entity.getId());
        dto.setPodZoneI(entity.getPodZoneI());
        dto.setPodWardI(entity.getPodWardI());
        dto.setPodOldPropNoVc(entity.getPodOldPropNoVc());
        dto.setPodNewPropertyNoVc(entity.getPodNewPropertyNoVc());
        dto.setPodOwnerNameVc(entity.getPodOwnerNameVc());
        dto.setPodOccupierNameVc(entity.getPodOccupierNameVc());
        dto.setPodPropertyAddressVc(entity.getPodPropertyAddressVc());
        dto.setPodConstClassVc(entity.getPodConstClassVc());
        dto.setPodPropertyTypeI(entity.getPodPropertyTypeI());
        dto.setPodPropertySubTypeI(entity.getPodPropertySubTypeI());
        dto.setPodUsageTypeI(entity.getPodUsageTypeI());
        dto.setPodNoofrooms(entity.getPodNoofrooms());
        dto.setPodPlotvalue(entity.getPodPlotvalue());
        dto.setPodTotalPropertyvalue(entity.getPodTotalPropertyvalue());
        dto.setPodBuildingvalue(entity.getPodBuildingvalue());
        dto.setPodBuiltUpAreaFl(entity.getPodBuiltUpAreaFl());
        dto.setPodLastAssDtDt(entity.getPodLastAssDtDt());
        dto.setPodCurrentAssDt(entity.getPodCurrentAssDt());
        dto.setPodTotalTaxFl(entity.getPodTotalTaxFl());
        dto.setPodPropertyTaxFl(entity.getPodPropertyTaxFl());
        dto.setPodEduCessFl(entity.getPodEduCessFl());
        dto.setPodEgcFl(entity.getPodEgcFl());
        dto.setPodTreeTaxFl(entity.getPodTreeTaxFl());
        dto.setPodEnvTaxFl(entity.getPodEnvTaxFl());
        dto.setPodFireTaxFl(entity.getPodFireTaxFl());
        dto.setPodRefNoVc(entity.getPodRefNoVc());
        dto.setPdNewPropertyNoVc(entity.getPdNewPropertyNoVc());
        dto.setPdConstructionYearD(entity.getPdConstructionYearD());
        dto.setUser_id(entity.getUserid());
        dto.setPodTotalAssessmentArea(entity.getPodTotalAssessmentArea());
        dto.setPodTotalRatableValue(entity.getPodTotalRatableValue());
        dto.setPodTotalAssessmentAreaFt(entity.getPodTotalAssessmentAreaFt());
        return dto;
    }

    private PropertyOldDetails_Entity convertToEntity(PropertyOldDetails_Dto dto) {
        PropertyOldDetails_Entity entity = new PropertyOldDetails_Entity();
        entity.setPodZoneI(dto.getPodZoneI());
        entity.setPodWardI(dto.getPodWardI());
        entity.setPodOldPropNoVc(dto.getPodOldPropNoVc());
        entity.setPodNewPropertyNoVc(dto.getPodNewPropertyNoVc());
        entity.setPodOwnerNameVc(dto.getPodOwnerNameVc());
        entity.setPodOccupierNameVc(dto.getPodOccupierNameVc());
        entity.setPodPropertyAddressVc(dto.getPodPropertyAddressVc());
        entity.setPodConstClassVc(dto.getPodConstClassVc());
        entity.setPodPropertyTypeI(dto.getPodPropertyTypeI());
        entity.setPodPropertySubTypeI(dto.getPodPropertySubTypeI());
        entity.setPodUsageTypeI(dto.getPodUsageTypeI());
        entity.setPodNoofrooms(dto.getPodNoofrooms());
        entity.setPodPlotvalue(dto.getPodPlotvalue());
        entity.setPodTotalPropertyvalue(dto.getPodTotalPropertyvalue());
        entity.setPodBuildingvalue(dto.getPodBuildingvalue());
        entity.setPodBuiltUpAreaFl(dto.getPodBuiltUpAreaFl());
        entity.setPodLastAssDtDt(dto.getPodLastAssDtDt());
        entity.setPodCurrentAssDt(dto.getPodCurrentAssDt());
        entity.setPodTotalTaxFl(dto.getPodTotalTaxFl());
        entity.setPodPropertyTaxFl(dto.getPodPropertyTaxFl());
        entity.setPodEduCessFl(dto.getPodEduCessFl());
        entity.setPodEgcFl(dto.getPodEgcFl());
        entity.setPodTreeTaxFl(dto.getPodTreeTaxFl());
        entity.setPodEnvTaxFl(dto.getPodEnvTaxFl());
        entity.setPodFireTaxFl(dto.getPodFireTaxFl());
        entity.setPodRefNoVc(dto.getPodRefNoVc());
        entity.setPdNewPropertyNoVc(dto.getPdNewPropertyNoVc());
        entity.setPdConstructionYearD(dto.getPdConstructionYearD());
        entity.setUserid(dto.getUser_id());
        entity.setPodTotalRatableValue(dto.getPodTotalRatableValue());
        entity.setPodTotalAssessmentArea(dto.getPodTotalAssessmentArea());
        entity.setPodTotalAssessmentAreaFt(dto.getPodTotalAssessmentAreaFt());
        return entity;
    }

}
