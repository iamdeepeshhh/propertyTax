package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentRealtime;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ConsolidatedTaxDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.PropertyUnitDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ProposedRatableValueDetailsDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.ConsolidatedTaxes_MasterEntity.ConsolidatedTaxes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.EduCessAndEmpCess_MasterEntity.EduCessAndEmpCess_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.PropertyRates_MasterEntity.PropertyRates_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RVTypes_MasterEntity.RVTypes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxDepreciation_MasterEntity.TaxDepreciation_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity.UnitUsageSubType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity.UnitUsageType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity.PropertyDetails_Entity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyOldDetails_Entity.PropertyOldDetails_Entity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitDetails_Entity.UnitDetails_Entity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.ConsolidatedTaxes_MasterRepository.ConsolidatedTaxes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.EduCessAndEmpCess_MasterRepository.EduCessAndEmpCess_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.PropertyRates_MasterRepository.PropertyRates_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.RVTypes_MasterRepository.RVTypes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxDepreciation_MasterRepository.TaxDepreciation_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitUsageTypes_MasterRepository.UnitUsageSubType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitUsageTypes_MasterRepository.UnitUsageType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.PropertyDetails_Repository;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.PropertyOldDetails_Repository;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.UnitDetails_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TaxAssessment_MasterServiceImpl implements TaxAssessment_MasterService{
    @Autowired
    private PropertyRates_MasterRepository propertyRateRepository;

    @Autowired
    private UnitUsageType_MasterRepository unitUsageTypeMasterRepository;

    @Autowired
    private RVTypes_MasterRepository rvTypesMasterRepository;

    @Autowired
    private PropertyDetails_Repository propertyDetailsRepository;
    @Autowired
    private ConsolidatedTaxes_MasterRepository consolidatedTaxesMasterRepository;
    @Autowired
    private UnitDetails_Repository unitDetailsRepository;
    @Autowired
    private TaxDepreciation_MasterRepository taxDepreciationMasterRepository;
    @Autowired
    private EduCessAndEmpCess_MasterRepository eduCessAndEmpCessMasterRepository;
    @Autowired
    private UnitUsageSubType_MasterRepository unitUsageSubType_masterRepository;
    @Autowired
    private PropertyOldDetails_Repository propertyOldDetails_repository;

    List<String> warnings = new ArrayList<>();

    private static final Logger logger = Logger.getLogger(TaxAssessment_MasterServiceImpl.class.getName());

    //this process is basically used for realtime tax assessment process
    @Override
    public AssessmentResultsDto performAssessment(String newPropertyNumber) {
        warnings.clear();

        Optional<PropertyDetails_Entity> propertyOpt = propertyDetailsRepository.findBypdNewpropertynoVc(newPropertyNumber);
        if (!propertyOpt.isPresent()) {
            String errorMessage = "Property not found for the given newPropertyNumber: " + newPropertyNumber;
            logger.severe(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        PropertyDetails_Entity property = propertyOpt.get();
        Integer zone = property.getPdZoneI();
        List<UnitDetails_Entity> unitDetails = unitDetailsRepository.findAllByPdNewpropertynoVc(newPropertyNumber);

        List<Map<String, Object>> unitRecords = new ArrayList<>();
        double totalRatableValue = 0.0;
        double totalUserCharges = 0.0;
        double taxableValueUnrented = 0.0;
        double annualRentalValue = 0.0;
        double annualUnRentalValue = 0.0;
        double taxableValueRented = 0.0;
        double maintenanceRepairAmountRented = 0.0;
        double maintenanceRepairAmountUnrented = 0.0;
        Map<Long, Double> maxUserChargesByCategory = new HashMap<>(); // to track the commercial usercharge as we have to consider bigger one

        Map<Long, Double> rateTypeTotals = new HashMap<>();
        Set<Integer> processedUnitSubUsagesId = new HashSet<>();//for checking which unitusage subtype is processed so that usercharges get applied once
        for (UnitDetails_Entity unit : unitDetails) {
            logger.info("Starting ratable value calculation for unit: " + unit.getUdUnitnoVc());
            Map<String, Object> unitRecord = calculateRatableValueForUnit(unit, zone);
            unitRecords.add(unitRecord);
            Double ratableValue = (Double) unitRecord.get("ratableValue");
            totalRatableValue += (ratableValue != null) ? ratableValue : 0.0;

            Long categoryId = (Long) unitRecord.get("categoryId"); //using category id to set the values to their respective proposed ratable values
            if (categoryId != null) {
                rateTypeTotals.merge(categoryId, ratableValue, Double::sum);
            }


            Object amountAfterDepreciationObj = unitRecord.get("amountAfterDepreciation");
            double amountAfterDepreciation = 0.0;
            if (amountAfterDepreciationObj != null) {
                amountAfterDepreciation = (Double) amountAfterDepreciationObj;
            }

            if (unit.getUdTenantnoI() != null && !unit.getUdTenantnoI().isEmpty()) {
                // Rented units
                double annualRent = Double.parseDouble(unit.getUdRentalnoVc()) * 12;
                annualRentalValue += annualRent;

                // Retrieve and handle 'maintenanceRepairAmountForRent'
                Object maintenanceRepairAmountForRentObj = unitRecord.get("maintenanceRepairAmountForRent");
                double maintenanceRepairAmountForRent = 0.0;
                if (maintenanceRepairAmountForRentObj != null) {
                    maintenanceRepairAmountForRent = (Double) maintenanceRepairAmountForRentObj;
                }
                maintenanceRepairAmountRented += maintenanceRepairAmountForRent;

                // Retrieve and handle 'taxableValueByActualRent'
                Object taxableValueByActualRentObj = unitRecord.get("taxableValueByActualRent");
                double taxableValueByActualRent = 0.0;
                if (taxableValueByActualRentObj != null) {
                    taxableValueByActualRent = (Double) taxableValueByActualRentObj;
                }
                taxableValueRented += taxableValueByActualRent;
            } else {
                // Unrented units
                annualUnRentalValue += amountAfterDepreciation;

                double maintenanceRepairAmount = amountAfterDepreciation * 0.10;
                maintenanceRepairAmountUnrented += maintenanceRepairAmount;

                double taxableValueAfterMaintenance = amountAfterDepreciation - maintenanceRepairAmount;
                taxableValueUnrented += taxableValueAfterMaintenance;
            }
            if (Double.parseDouble(unit.getUdAssessmentareaF()) != 0) {
                if (categoryId == 4 || categoryId == 9) {  // Specifically handling commercial and commercial open plot category
                    double currentCharge = getUserChargesForUnit(unit);
                    maxUserChargesByCategory.merge(categoryId, currentCharge, Math::max);
                } else if (!processedUnitSubUsagesId.contains(unit.getUdUsagesubtypeI())) {  // For non-commercial units
                    double userCharges = getUserChargesForUnit(unit);
                    totalUserCharges += userCharges;
                    processedUnitSubUsagesId.add(unit.getUdUsagesubtypeI());  // Mark this unit ID as processed
                }
            }

            logger.info("details:" + totalRatableValue);
            logger.info("Completed ratable value calculation for unit: " + unit.getUdUnitnoVc() + ", Ratable Value: " + totalRatableValue);
        }

        // this is added to get the usercharges correctly for commercial properties
        if (maxUserChargesByCategory.containsKey(4L) || maxUserChargesByCategory.containsKey(9L)) {
            double maxCharge4 = maxUserChargesByCategory.getOrDefault(4L, 0.0);//this is getting used for commercial property
            double maxCharge9 = maxUserChargesByCategory.getOrDefault(9L, 0.0);//this is getting used for commercial open plot
            totalUserCharges += Math.max(maxCharge4, maxCharge9);
        }

        annualRentalValue = Math.round(annualRentalValue);
        taxableValueUnrented = Math.round(taxableValueUnrented);

        totalRatableValue = Math.round(totalRatableValue);

        Map<String, Double> educationTax = calculateEducationTax(totalRatableValue, unitRecords);
        double propertyTax = calculatePropertyTax(unitRecords);
        Map<String, Double> consolidatedTaxes = calculateConsolidatedTaxes(totalRatableValue, propertyTax);
        totalUserCharges = Math.round(totalUserCharges);
        Map<String, Double> egc = calculateEgc(totalRatableValue, unitRecords);

        logger.info("Units: "+ unitRecords);
//        logger.info("education tax: "+educationTax);
//        logger.info("consolidatedTaxes: "+consolidatedTaxes);
//        logger.info("EGC: "+egc);
//        logger.info("UserCharges: "+totalUserCharges);
//        logger.info("propertyTax: "+propertyTax );
        // Convert the assessment data into AssessmentResultsDto
        AssessmentResultsDto assessmentResultsDto = new AssessmentResultsDto();

        // Set basic property information
        assessmentResultsDto.setAnnualRentalValueFl(String.valueOf(annualRentalValue));
        assessmentResultsDto.setAnnualUnRentalValueFl(String.valueOf(annualUnRentalValue));
        assessmentResultsDto.setFinalValueToConsiderFl(String.valueOf(annualRentalValue + annualUnRentalValue));

        double totalMaintenanceRepairAmount = maintenanceRepairAmountRented + maintenanceRepairAmountUnrented;
        assessmentResultsDto.setMaintainenceRepairAmountConsideredFl(String.valueOf(Math.round(totalMaintenanceRepairAmount)));

        assessmentResultsDto.setTaxableValueRentedFl(String.valueOf(taxableValueRented));
        assessmentResultsDto.setTaxableValueUnRentedFl(String.valueOf(taxableValueUnrented));
        assessmentResultsDto.setPdNewpropertynoVc(property.getPdNewpropertynoVc());
        assessmentResultsDto.setPdFinalpropnoVc(property.getPdFinalpropnoVc());
        assessmentResultsDto.setPdWardI(String.valueOf(property.getPdWardI()));
        assessmentResultsDto.setPdZoneI(String.valueOf(property.getPdZoneI()));
        assessmentResultsDto.setPdOldpropnoVc(property.getPdOldpropnoVc());
        assessmentResultsDto.setPdSurypropnoVc(property.getPdSurypropnoVc());
        assessmentResultsDto.setPdIndexnoVc(property.getPdIndexnoVc());
        assessmentResultsDto.setPdOwnernameVc(property.getPdOwnernameVc());
        assessmentResultsDto.setPdOccupinameF(property.getPdOccupinameF());
        assessmentResultsDto.setPdPropertyaddressVc(property.getPdPropertyaddressVc());
        assessmentResultsDto.setPdLayoutnameVc(property.getPdLayoutnameVc());
        assessmentResultsDto.setPdPropertynameVc(property.getPdPropertynameVc());
        assessmentResultsDto.setPdKhasranoVc(property.getPdKhasranoVc());
        assessmentResultsDto.setPdPlotnoVc(property.getPdPlotnoVc());
        assessmentResultsDto.setPdGrididVc(property.getPdGrididVc());
        assessmentResultsDto.setPdParcelidVc(property.getPdParcelidVc());
        assessmentResultsDto.setPdRoadidVc(property.getPdRoadidVc());
        assessmentResultsDto.setPdPlotareaF(property.getPdPlotareaF());
        assessmentResultsDto.setPdCategoryI(property.getPdCategoryI()); // Assuming "pdCategoryI" represents "Municipal Composition"
        assessmentResultsDto.setPdHouseplan2T(property.getPdHouseplan2T());
        assessmentResultsDto.setPdPropimageT(property.getPdPropimageT());
// Set additional fields that might be derived or calculated
        assessmentResultsDto.setPdToiletsI(property.getPdToiletsI() != null ? property.getPdToiletsI().toString() : null);
        assessmentResultsDto.setPdSeweragesVc(property.getPdSeweragesVc());
        assessmentResultsDto.setPdSeweragesType(property.getPdSeweragesType());
        assessmentResultsDto.setPdWaterconntypeVc(property.getPdWaterconntypeVc());
        assessmentResultsDto.setPdMcconnectionVc(property.getPdMcconnectionVc());
        assessmentResultsDto.setPdMeternoVc(property.getPdMeternoVc());
        assessmentResultsDto.setPdConsumernoVc(property.getPdConsumernoVc());
        assessmentResultsDto.setPdConnectiondateDt(String.valueOf(property.getPdConnectiondateDt()));
        assessmentResultsDto.setPdPipesizeF(String.valueOf(property.getPdPipesizeF()));
        assessmentResultsDto.setPdPermissionstatusVc(property.getPdPermissionstatusVc());
        assessmentResultsDto.setPdPermissionnoVc(property.getPdPermissionnoVc());
        assessmentResultsDto.setPdPermissiondateDt(String.valueOf(property.getPdPermissiondateDt()));
        assessmentResultsDto.setPdRainwaterhaverstVc(property.getPdRainwaterhaverstVc());
        assessmentResultsDto.setPdSolarunitVc(property.getPdSolarunitVc());
        assessmentResultsDto.setPdStairVc(property.getPdStairVc());
        assessmentResultsDto.setPdLiftVc(property.getPdLiftVc());
        assessmentResultsDto.setPdContactnoVc(property.getPdContactnoVc());
        assessmentResultsDto.setPreviousAssessmentDateDt(property.getPdLastassesdateDt());
        assessmentResultsDto.setCurrentAssessmentDateDt(property.getPdCurrassesdateDt());

// Old Property Details
        if (property.getPropRefno() != null && !property.getPropRefno().trim().isEmpty()) {
            try {
                // Assuming you have a method to fetch old property details by pdRefno
                Optional<PropertyOldDetails_Entity> oldDetailsOpt = propertyOldDetails_repository.findByPodRefNoVc(Integer.parseInt(property.getPropRefno().trim()));
                if (oldDetailsOpt.isPresent()) {
                    PropertyOldDetails_Entity oldDetails = oldDetailsOpt.get();
                    assessmentResultsDto.setOldWardNoVc(oldDetails.getPodWardI());
                    assessmentResultsDto.setOldZoneNoVc(oldDetails.getPodZoneI());
                    assessmentResultsDto.setOldPropertyTypeVc(oldDetails.getPodPropertyTypeI());
                    assessmentResultsDto.setOldPropertySubTypeVc(oldDetails.getPodPropertySubTypeI());
                    assessmentResultsDto.setOldUsageTypeVc(oldDetails.getPodUsageTypeI());
                   // assessmentResultsDto.s(oldDetails.getPodUsageSubTypeI());
                    assessmentResultsDto.setOldConstructionTypeVc(oldDetails.getPodConstClassVc());
                    assessmentResultsDto.setOldAssessmentAreaFl(oldDetails.getPodTotalAssessmentArea());

                }
            } catch (NumberFormatException e) {
                logger.severe("Invalid PropRefno format: " + property.getPropRefno());
                // Handle the exception, e.g., log it, return an error, or skip processing
            }
        }

// New Property Details
        assessmentResultsDto.setPdPropertytypeI(property.getPdPropertytypeI());
        assessmentResultsDto.setPdPropertysubtypeI(property.getPdPropertysubtypeI());
        assessmentResultsDto.setPdUsagetypeI(property.getPdUsagetypeI());
        assessmentResultsDto.setPdUsagesubtypeI(property.getPdUsagesubtypeI());
        assessmentResultsDto.setPdBuildingtypeI(property.getPdBuildingtypeI());
        assessmentResultsDto.setPdBuildingsubtypeI(property.getPdBuildingsubtypeI());
        assessmentResultsDto.setPdStatusbuildingVc(property.getPdStatusbuildingVc());
        assessmentResultsDto.setPdOwnertypeVc(property.getPdOwnertypeVc());
        assessmentResultsDto.setPdCategoryI(property.getPdCategoryI());
        assessmentResultsDto.setPdConstAgeI(property.getPdConstAgeI() != null ? property.getPdConstAgeI().toString() : null);
        assessmentResultsDto.setPdPlotareaF(property.getPdPlotareaF());
        assessmentResultsDto.setPdTotbuiltupareaF(property.getPdTotbuiltupareaF());
        assessmentResultsDto.setPdTotcarpetareaF(property.getPdTotcarpetareaF());
        assessmentResultsDto.setPdTotalexempareaF(property.getPdTotalexempareaF());
        assessmentResultsDto.setPdAssesareaF(property.getPdAssesareaF());
        assessmentResultsDto.setPdArealetoutF(property.getPdArealetoutF());
        assessmentResultsDto.setPdAreanotletoutF(property.getPdAreanotletoutF());
        assessmentResultsDto.setPdNoofroomsI((property.getPdNoofroomsI() != null ? property.getPdNoofroomsI().toString() : null));;
        assessmentResultsDto.setPdNooffloorsI((property.getPdNooffloorsI() != null ? property.getPdNooffloorsI().toString() : null));

        assessmentResultsDto.setWarnings(warnings);
// Derived/Calculated Fields
//        assessmentResultsDto.setAnnualRentalValueFl("calculated_value"); // Assuming it's calculated
//        assessmentResultsDto.setMaintenanceRepairAmountFl("calculated_value"); // Assuming it's calculated
//        assessmentResultsDto.setTaxableValueLeaseFl("calculated_value"); // Assuming it's calculated
//        assessmentResultsDto.setTaxableValueUnrentedFl("calculated_value");

        // Convert and set unit details
        List<PropertyUnitDetailsDto> unitDetailsDtos = unitRecords.stream().map(record -> {
            PropertyUnitDetailsDto dto = new PropertyUnitDetailsDto();
            Map<String, String> unitDetailsMap = (Map<String, String>) record.get("unit");
            dto.setUnitNoVc(unitDetailsMap.get("udUnitnoVc"));
            dto.setFloorNoVc(unitDetailsMap.get("udFloornoVc"));
            dto.setOccupantStatusI(unitDetailsMap.get("udOccupantstatusI"));
            dto.setUsageTypeVc(unitDetailsMap.get("udUsagetypeI"));
            dto.setUsageSubTypeVc(unitDetailsMap.get("udUsagesubtypeI"));
            dto.setConstructionTypeVc(unitDetailsMap.get("udConstructionclassI"));
            dto.setConstructionYearVc(unitDetailsMap.get("udConstYearDt"));
            dto.setConstructionAgeVc(unitDetailsMap.get("udConstAgeI"));
            dto.setCarpetAreaFl(unitDetailsMap.get("udCarpetareaF"));
            dto.setPlotAreaFl(unitDetailsMap.get("udPlotAreaFl"));
            dto.setExemptedAreaFl(unitDetailsMap.get("udExemptedAreaF"));
            dto.setTaxableAreaFl(unitDetailsMap.get("udAssessmentareaF"));

            dto.setRatePerSqMFl(record.get("ratePerSqM") !=null ? (Double) record.get("ratePerSqM") : 0);
            dto.setRentalValAsPerRateFl(record.get("rentalValue") != null ? (Double) record.get("rentalValue") : 0);
            dto.setRentValueByRateFl(record.get("taxableValueByRentalRate") != null ? (Double) record.get("taxableValueByRentalRate") : 0);
            dto.setDepreciationRateFl(record.get("depreciationRate") != null ? (Double) record.get("depreciationRate") : 0);
            dto.setDepreciationAmountFl(record.get("depreciationAmount") != null ? (Double) record.get("depreciationAmount") : 0);
            dto.setValueAfterDepreciationFl(record.get("taxableValueByRentalRate") != null ? (Double) record.get("taxableValueByRentalRate") : 0);
            dto.setMaintenanceRepairsFl((Double) record.get("maintenanceRepairAmount"));
            dto.setTaxableValueByRateFl(record.get("taxableValueByRentalRate") != null ? (Double) record.get("taxableValueByRentalRate") : 0);
            dto.setTenantNameVc(unitDetailsMap.get("tenantName"));
            dto.setActualMonthlyRentFl(
                    unitDetailsMap.get("udRentalnoVc") != null && !unitDetailsMap.get("udRentalnoVc").trim().isEmpty()
                            ? Double.valueOf(unitDetailsMap.get("udRentalnoVc").trim())
                            : 0.0
            );
            dto.setAgeFactorVc((String) record.get("ageFactor"));
            dto.setActualAnnualRentFl(record.get("annualRent") != null ? (Double) record.get("annualRent") : 0);
            dto.setTaxableValueByRentFl(record.get("taxableValueByActualRent") != null ? (Double) record.get("taxableValueByActualRent") : 0);
            dto.setMaintenanceRepairsRentFl(record.get("maintenanceRepairAmountForRent") != null ? (Double) record.get("maintenanceRepairAmountForRent") : 0);
            dto.setTaxableValueConsideredFl(record.get("ratableValue") != null ? (Double) record.get("ratableValue") : 0);
            dto.setAmountAfterDepreciationFl(record.get("amountAfterDepreciation") != null ? (Double) record.get("amountAfterDepreciation") : 0);
            return dto;
        }).collect(Collectors.toList());
        assessmentResultsDto.setUnitDetails(unitDetailsDtos);

        ProposedRatableValueDetailsDto proposedRatableValuesDto = new ProposedRatableValueDetailsDto();

        for (Map.Entry<Long, Double> entry : rateTypeTotals.entrySet()) {
            Long categoryId = entry.getKey();
            Double totalValue = entry.getValue();

            switch (categoryId.intValue()) {
                case 1:
                    proposedRatableValuesDto.setResidentialFl(totalValue);
                    break;
                case 4:
                    proposedRatableValuesDto.setCommercialFl(totalValue);
                    break;
                case 7:
                    proposedRatableValuesDto.setReligiousFl(totalValue);
                    break;
                case 10:
                    proposedRatableValuesDto.setResidentialOpenPlotFl(totalValue);
                    break;
                case 9:
                    proposedRatableValuesDto.setCommercialOpenPlotFl(totalValue);
                    break;
                case 13:
                    proposedRatableValuesDto.setReligiousOpenPlotFl(totalValue);
                    break;
                case 12:
                    proposedRatableValuesDto.setEducationAndLegalInstituteOpenPlotFl(totalValue);
                    break;
                case 11:
                    proposedRatableValuesDto.setGovernmentOpenPlotFl(totalValue);
                    break;
                case 14:
                    proposedRatableValuesDto.setIndustrialOpenPlotFl(totalValue);
                    break;
                case 8:
                    proposedRatableValuesDto.setIndustrialFl(totalValue);
                    break;
                case 2:
                    proposedRatableValuesDto.setMobileTowerFl(totalValue);
                    break;
                case 3:
                    proposedRatableValuesDto.setElectricSubstationFl(totalValue);
                    break;
                case 5:
                    proposedRatableValuesDto.setGovernmentFl(totalValue);
                    break;
                case 6:
                    proposedRatableValuesDto.setEducationalInstituteFl(totalValue);
                    break;
                default:
                    logger.warning("Unknown rate type ID: " + categoryId);
                    break;
            }
        }

        // Calculate the total for all proposed ratable values
        proposedRatableValuesDto.setAggregateFl(rateTypeTotals.values().stream().mapToDouble(Double::doubleValue).sum());

        // Set this in the assessmentResultsDto
        assessmentResultsDto.setProposedRatableValues(proposedRatableValuesDto);

        double totalTax = propertyTax
                + educationTax.get("Education Tax(Residential)")
                + educationTax.get("Education Tax(Commercial)")
                + egc.get("Employment Guarantee Cess (EGC)")
                + totalUserCharges
                + consolidatedTaxes.values().stream().mapToDouble(Double::doubleValue).sum();
        // Convert and set consolidated taxes
        ConsolidatedTaxDetailsDto consolidatedTaxesDto = new ConsolidatedTaxDetailsDto();
        consolidatedTaxesDto.setPropertyTaxFl(propertyTax);
        consolidatedTaxesDto.setEducationTaxResidFl(educationTax.get("Education Tax(Residential)"));
        consolidatedTaxesDto.setEducationTaxCommFl(educationTax.get("Education Tax(Commercial)"));
        consolidatedTaxesDto.setEducationTaxTotalFl(educationTax.get("Education Tax(Total)") != null ? educationTax.get("Education Tax(Total)"): 0);
        consolidatedTaxesDto.setEgcFl(egc.get("Employment Guarantee Cess (EGC)"));
        consolidatedTaxesDto.setTreeTaxFl(consolidatedTaxes.get("Tree Tax"));
        consolidatedTaxesDto.setEnvironmentalTaxFl(consolidatedTaxes.get("Environment Tax"));
        consolidatedTaxesDto.setCleannessTaxFl(consolidatedTaxes.get("Cleanness Tax"));
        consolidatedTaxesDto.setLightTaxFl(consolidatedTaxes.get("Light Tax"));
        consolidatedTaxesDto.setFireTaxFl(consolidatedTaxes.get("Fire Tax"));
        consolidatedTaxesDto.setUserChargesFl(totalUserCharges);
        consolidatedTaxesDto.setTotalTaxFl(totalTax);
        assessmentResultsDto.setConsolidatedTaxes(consolidatedTaxesDto);

//        logger.info("assessmentresults: "+assessmentResultsDto);
        return assessmentResultsDto;
    }
    private Map<String, Double> calculateConsolidatedTaxes(double ratableValue, double propertyTax) {
        List<ConsolidatedTaxes_MasterEntity> taxRates = consolidatedTaxesMasterRepository.findAll();
        Map<String, Double> taxDetails = new HashMap<>();

        for (ConsolidatedTaxes_MasterEntity taxRate : taxRates) {
            if (!taxRate.getTaxNameVc().equals("Property Tax") && !taxRate.getTaxNameVc().equals("Property Tax II")) {
                double taxAmount = 0.0;

                // Determine the base value for tax calculation based on 'appliedOn' field
                double baseValue = 0.0;
                if ("Ratable Value".equalsIgnoreCase(taxRate.getApplicableonVc())) {
                    baseValue = ratableValue;
                } else if ("Property Tax".equalsIgnoreCase(taxRate.getApplicableonVc())) {
                    baseValue = propertyTax;
                }

                if (baseValue > 0) {
                    double rate = Double.parseDouble(taxRate.getTaxRateFl());
                    taxAmount = baseValue * (rate / 100);
                }
                taxDetails.put(taxRate.getTaxNameVc(), (double) Math.round(taxAmount));
            }
        }

        return taxDetails;
    }

    private Map<String, Double> calculateEducationTax(double totalRatableValue, List<Map<String, Object>> unitRecords) {
        Map<String, Double> educationTaxDetails = new HashMap<>();

        if (totalRatableValue == 0) {
            educationTaxDetails.put("Education Tax(Residential)", 0.0);
            educationTaxDetails.put("Education Tax(Commercial)", 0.0);
            return educationTaxDetails;
        }

        Optional<EduCessAndEmpCess_MasterEntity> cessOpt = eduCessAndEmpCessMasterRepository.findByRatableValueRange(totalRatableValue);
        if (!cessOpt.isPresent()) {
            String msg = "Education Cess rate not found for ratable value range: " + totalRatableValue;
            logger.warning(msg);
            warnings.add(msg);

            educationTaxDetails.put("Education Tax(Residential)", 0.0);
            educationTaxDetails.put("Education Tax(Commercial)", 0.0);
            educationTaxDetails.put("Education Tax(Total)", 0.0);
            return educationTaxDetails;
        }

        EduCessAndEmpCess_MasterEntity cess = cessOpt.get();
        double residentialRate = Double.parseDouble(cess.getResidentialRateFl());
        double commercialRate = Double.parseDouble(cess.getCommercialRateFl());

        double residentialTax = 0.0;
        double commercialTax = 0.0;

        for (Map<String, Object> unitRecord : unitRecords) {
            RVTypes_MasterEntity rvType = (RVTypes_MasterEntity) unitRecord.get("rvType");
            Double unitRatableValue = (Double) unitRecord.get("ratableValue");
            if (unitRatableValue == null) {
                unitRatableValue = 0.0;
            }

            String applicableTaxes = rvType.getAppliedTaxesVc();

            if (applicableTaxes.contains("Education Tax(Residential)")) {
                residentialTax += unitRatableValue * (residentialRate / 100);
            } else if (applicableTaxes.contains("Education Tax(Commercial)")) {
                commercialTax += unitRatableValue * (commercialRate / 100);
            }
        }

        educationTaxDetails.put("Education Tax(Residential)", (double) Math.round(residentialTax));
        educationTaxDetails.put("Education Tax(Commercial)", (double) Math.round(commercialTax));
        educationTaxDetails.put("Education Tax(Total)",(double) Math.round(residentialTax + commercialTax));
        return educationTaxDetails;
    }

    private Map<String, Double> calculateEgc(double totalRatableValue, List<Map<String, Object>> unitRecords) {
        Map<String, Double> egcDetails = new HashMap<>();

        if (totalRatableValue == 0) {
            egcDetails.put("Employment Guarantee Cess (EGC)", 0.0);
            return egcDetails;
        }

        Optional<EduCessAndEmpCess_MasterEntity> cessOpt = eduCessAndEmpCessMasterRepository.findByRatableValueRange(totalRatableValue);
        if (!cessOpt.isPresent()) {
            String msg = "EGC rate not found for the given ratable value range: " + totalRatableValue;
            logger.warning(msg);
            warnings.add(msg);
            egcDetails.put("Employment Guarantee Cess (EGC)", 0.0);
            return egcDetails;
        }

        EduCessAndEmpCess_MasterEntity cess = cessOpt.get();
        double egcRate = Double.parseDouble(cess.getEgcRateFl());

        double egcAmount = 0.0;

        for (Map<String, Object> unitRecord : unitRecords) {
            RVTypes_MasterEntity rvType = (RVTypes_MasterEntity) unitRecord.get("rvType");
            Double unitRatableValue = (Double) unitRecord.get("ratableValue");
            if (unitRatableValue == null) {
                unitRatableValue = 0.0;
            }
            String applicableTaxes = rvType.getAppliedTaxesVc();

            if (applicableTaxes.contains("Employment Guarantee Cess (EGC)")) {
                egcAmount += unitRatableValue * (egcRate / 100);
            }
        }

        egcDetails.put("Employment Guarantee Cess (EGC)", (double) Math.round(egcAmount));
        return egcDetails;
    }

    private Map<String, Object> calculateRatableValueForUnit(UnitDetails_Entity unit, Integer zone) {
        Map<String, Object> unitRecord = new HashMap<>();
        unitRecord.put("unit", extractRelevantUnitDetails(unit));

        Integer unitAge = 0;  // Initialize unit age to 0 by default

        // Fetch property rate based on construction type and zone
        Optional<PropertyRates_MasterEntity> rateOpt = propertyRateRepository.findByConstructionTypeVcAndTaxRateZoneI(unit.getUdConstructionclassI().trim(), zone.toString().trim());
        if (!rateOpt.isPresent()) {
            String msg = "Rate not found for construction type: " + unit.getUdConstructionclassI() + " and zone: " + zone;
            warnings.add(msg);
            logger.warning(msg);
            return unitRecord;
            }

        PropertyRates_MasterEntity rate = rateOpt.get();
        //changed the further part of a code as we want to calculate the Ratable value on the
        //basis of unit usage subtype in some cases
        Long rateTypeId;  // Declaration without initial assignment

        if (unit.getUdUsagesubtypeI() != null) {
            UnitUsageSubType_MasterEntity unitUsageSubType = unitUsageSubType_masterRepository.findById(unit.getUdUsagesubtypeI())
                    .orElseThrow(() -> new IllegalArgumentException("Unit usage subtype not found for the given ID: " + unit.getUdUsagesubtypeI()));

            if (Boolean.TRUE.equals(unitUsageSubType.getUsmApplyDifferentRateVc())) {
                logger.info("boolean value:"+unitUsageSubType.getUsmApplyDifferentRateVc());
                rateTypeId = Long.parseLong(unitUsageSubType.getUsm_rvtype_vc());  // Set based on condition
            } else {
                // Use the primary usage type's rate type ID if the condition is not met
                logger.info("boolean value:"+unitUsageSubType.getUsmApplyDifferentRateVc());
                rateTypeId = Long.parseLong(unitUsageTypeMasterRepository.findById(unit.getUdUsagetypeI())
                        .orElseThrow(() -> new IllegalArgumentException("Unit usage type not found for the given ID: " + unit.getUdUsagetypeI()))
                        .getUum_rvtype_vc());
            }
        } else {
            // Default case if no subtype is specified
            rateTypeId = Long.parseLong(unitUsageTypeMasterRepository.findById(unit.getUdUsagetypeI())
                    .orElseThrow(() -> new IllegalArgumentException("Unit usage type not found for the given ID: " + unit.getUdUsagetypeI()))
                    .getUum_rvtype_vc());
        }

        RVTypes_MasterEntity rvType = rvTypesMasterRepository.findById(rateTypeId)
                .orElseThrow(() -> {
                    String msg = "RV Type not found for ID: " + rateTypeId;
                    warnings.add(msg);
                    return new IllegalArgumentException(msg);
                });


        unitRecord.put("rateTypeId", rateTypeId);
        unitRecord.put("rvType", rvType);
        unitRecord.put("categoryId", rvType.getCategory().getCategoryId());//using this to setting the ratable values in there
                                                            // proposed ratable category mentioned in the report
        double ratePerSqM = Double.parseDouble(rate.getRateI());
        ratePerSqM *= Double.parseDouble(rvType.getRateFl()); // Adjust the rate per sq.m by the multiplier
        unitRecord.put("ratePerSqM", ratePerSqM);

        logger.info("this is rate per sqm for realtime assessment:"+ ratePerSqM);
        double assessmentArea = Double.parseDouble(unit.getUdAssessmentareaF().trim());

        if (assessmentArea == 0) {
            unitRecord.put("depreciationRate", 0.0);
            unitRecord.put("depreciationAmount", 0.0);
            unitRecord.put("maintenanceRepairAmount", 0.0);
            unitRecord.put("taxableValueByRentalRate", 0.0);
            unitRecord.put("ratableValue", 0.0);
            return unitRecord;
        }

        double rentalValue = Math.round(assessmentArea * ratePerSqM);
        unitRecord.put("rentalValue", rentalValue);

        String constructionDateStr = unit.getUdConstyearDt();
        if (constructionDateStr != null && !constructionDateStr.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate constructionDate = LocalDate.parse(constructionDateStr, formatter);

                int constructionYear = constructionDate.getYear();
                int currentYear = LocalDate.now().getYear();
                unitAge = currentYear - constructionYear;
            } catch (Exception e) {
                String msg = "Invalid construction year format for unit: " + unit.getUdUnitnoVc();
                warnings.add(msg);
                logger.warning(msg);
                unitAge = 0; // fallback
            }
        } else {
            String msg = "No construction year provided for unit: " + unit.getUdUnitnoVc() + ". Assuming age = 0.";
            warnings.add(msg);
            logger.info(msg);
        }

        System.out.println(unitAge+"::Unit Age");
        unitRecord.put("unitAge", unitAge);

        // Get depreciation rate based on unit age
        Optional<TaxDepreciation_MasterEntity> depreciationOpt = taxDepreciationMasterRepository.findDepreciationByAgeAndClass(unit.getUdConstructionclassI(), unitAge);

        String ageFactor = "0";
        double maintenanceRepairAmount = 0.0;
        double taxableValueByRentalRate = rentalValue;
        if (depreciationOpt.isPresent()) {
            TaxDepreciation_MasterEntity depreciation = depreciationOpt.get();
            double depreciationRate = Double.parseDouble(depreciation.getDepreciationPercentageI());
            double depreciationAmount = Math.round(rentalValue * (depreciationRate / 100.0)); // Calculate depreciation amount

            String minAge = depreciation.getMinAgeI();
            String maxAge = depreciation.getMaxAgeI();
            ageFactor = minAge + "-" + maxAge;
            unitRecord.put("depreciationRate", depreciationRate);
            unitRecord.put("depreciationAmount", depreciationAmount);
            unitRecord.put("ageFactor", ageFactor != null ? ageFactor : "0");
            taxableValueByRentalRate = Math.round(rentalValue - depreciationAmount);
            unitRecord.put("amountAfterDepreciation", taxableValueByRentalRate);

            maintenanceRepairAmount = Math.round(taxableValueByRentalRate * 0.10);
            taxableValueByRentalRate -= maintenanceRepairAmount;

            unitRecord.put("taxableValueByRentalRate", taxableValueByRentalRate);
            unitRecord.put("maintenanceRepairAmount", maintenanceRepairAmount);
        } else {
            unitRecord.put("amountAfterDepreciation", taxableValueByRentalRate);
            unitRecord.put("ageFactor", ageFactor);
            maintenanceRepairAmount = Math.round(taxableValueByRentalRate * 0.10);
            unitRecord.put("maintenanceRepairAmount", maintenanceRepairAmount);
            taxableValueByRentalRate -= maintenanceRepairAmount;
            unitRecord.put("taxableValueByRentalRate",taxableValueByRentalRate);

            String warningMessage = "No depreciation rate found for construction class: "
                    + unit.getUdConstructionclassI() + " and age: " + unitAge + " for unit: " + unit.getUdUnitnoVc();

            logger.warning(warningMessage);
            warnings.add(warningMessage);
        }

        double ratableValue = taxableValueByRentalRate;
        if (unit.getUdTenantnoI() != null && !unit.getUdTenantnoI().isEmpty()) {
            double annualRent = Double.parseDouble(unit.getUdRentalnoVc()) * 12;
            double maintenanceRepairAmountForRent = annualRent * 0.10;
            unitRecord.put("maintenanceRepairAmountForRent", maintenanceRepairAmountForRent);
            double taxableValueByActualRent = annualRent - maintenanceRepairAmountForRent;
            logger.info("Annual rent: " + annualRent + ", Maintenance repair: " + maintenanceRepairAmountForRent + ", Taxable value by actual rent: " + taxableValueByActualRent);

            unitRecord.put("taxableValueByActualRent", taxableValueByActualRent);
            unitRecord.put("annualRent", annualRent);

            ratableValue = Math.max(taxableValueByRentalRate, taxableValueByActualRent);
        }

        ratableValue = Math.round(ratableValue);

        unitRecord.put("ratableValue", ratableValue);
        return unitRecord;
    }

    private double calculatePropertyTax(List<Map<String, Object>> unitRecords) {
        double totalPropertyTax = 0.0;
        Map<Long, Double> rateTypeRatableValues = new HashMap<>();

        // Group ratable values by rate type
        for (Map<String, Object> unitRecord : unitRecords) {
            Long rateTypeId = (Long) unitRecord.get("rateTypeId");
            Double unitRatableValue = (Double) unitRecord.get("ratableValue");
            if (unitRatableValue == null) {
                logger.warning("Ratable value is null for unit with details: " + unitRecord.get("unit"));
                unitRatableValue = 0.0;
            }
            rateTypeRatableValues.merge(rateTypeId, unitRatableValue, Double::sum);
        }

        logger.info("Grouped ratable values by rate type: " + rateTypeRatableValues);

        // Calculate property tax for each rate type
        for (Map.Entry<Long, Double> entry : rateTypeRatableValues.entrySet()) {
            Long rateTypeId = entry.getKey();
            double rateTypeRatableValue = entry.getValue();

            Optional<RVTypes_MasterEntity> rvTypeOpt = rvTypesMasterRepository.findById(rateTypeId);
            if (!rvTypeOpt.isPresent()) {
                String errorMessage = "RV Type not found for the given ID: " + rateTypeId;
                logger.severe(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
            RVTypes_MasterEntity rvType = rvTypeOpt.get();
            String applicableTaxes = rvType.getAppliedTaxesVc();

            logger.info("Rate Type ID: " + rateTypeId + ", Applicable Taxes: " + applicableTaxes);

            for (String taxName : applicableTaxes.split(",")) {
                if (taxName.trim().equals("Property Tax") || taxName.trim().equals("Property Tax II")) {
                    ConsolidatedTaxes_MasterEntity taxRate = consolidatedTaxesMasterRepository.findByTaxNameVc(taxName.trim());
                    if (taxRate == null) {
                        String warningMessage = "Tax rate not found for tax name: " + taxName;
                        logger.warning(warningMessage);
                        continue; // Skip this tax and continue with others
                    }
                    if (taxRate != null) {
                        double rate = Double.parseDouble(taxRate.getTaxRateFl());
                        double taxAmount = rateTypeRatableValue * (rate / 100);
                        totalPropertyTax += taxAmount;
                    } else {
                        logger.warning("Tax rate not found for tax name: " + taxName);
                    }
                }
            }
        }

        return totalPropertyTax;
    }

    private double getUserChargesForUnit(UnitDetails_Entity unit) {
        double userCharges = 0.0;

      // Fetching Unit Usage Subtype entity
        Optional<UnitUsageSubType_MasterEntity> usageSubTypeOpt = unitUsageSubType_masterRepository.findById(unit.getUdUsagesubtypeI());
        if (usageSubTypeOpt.isPresent()) {
            UnitUsageSubType_MasterEntity usageSubType = usageSubTypeOpt.get();

            Integer userChargesI = usageSubType.getUsm_usercharges_i();

            if (userChargesI != null) {
                // Since taxType contains the amount directly, use it as user charges
                userCharges = userChargesI.doubleValue();
            } else {
                String msg = "User charges for Unit usage subtype not found for ID: " + unit.getUdUsagesubtypeI();
                logger.warning(msg);
                warnings.add(msg);
            }
        } else {
            logger.warning("Unit usage subtype not found for ID: " + unit.getUdUsagesubtypeI());
        }

        return userCharges;
    }

    private Map<String, String> extractRelevantUnitDetails(UnitDetails_Entity unit) {
        Map<String, String> unitDetails = new HashMap<>();
        unitDetails.put("udUnitnoVc", String.valueOf(unit.getUdUnitnoVc()));
        unitDetails.put("udUsagesubtypeI", String.valueOf(unit.getUdUsagesubtypeI()));
        unitDetails.put("udFloornoVc", unit.getUdFloornoVc());
        unitDetails.put("udRentalnoVc", unit.getUdRentalnoVc());
        unitDetails.put("udPlotAreaFl", unit.getUdPlotAreaFl());
        unitDetails.put("udAssessmentareaF", unit.getUdAssessmentareaF());
        unitDetails.put("udExemptedAreaF", unit.getUdExemptedAreaF());
        unitDetails.put("udUsagetypeI", String.valueOf(unit.getUdUsagetypeI()));
        unitDetails.put("udConstAgeI", unit.getUdConstAgeI() != null ? String.valueOf(unit.getUdConstAgeI()) : "0");
        unitDetails.put("udTenantnoI", unit.getUdTenantnoI());
        unitDetails.put("udOccupiernameVc", unit.getUdOccupiernameVc());
        unitDetails.put("udOccupantstatusI", String.valueOf(unit.getUdOccupantstatusI()));
        unitDetails.put("udCarpetareaF", unit.getUdCarpetareaF());
        unitDetails.put("udAreaBefDedF", unit.getUdAreaBefDedF());
        unitDetails.put("udConstructionclassI", unit.getUdConstructionclassI());
        unitDetails.put("pdNewpropertynoVc", unit.getPdNewpropertynoVc());
        unitDetails.put("udConstYearDt", unit.getUdConstyearDt());
        unitDetails.put("tenantName", unit.getUdOccupiernameVc());
        return unitDetails;
    }

//    @Override
//    public AssessmentResultsDto dtoConversion(Map<String, Object> assessmentData) {
//        // Create the main DTO object
//        AssessmentResultsDto assessmentResultsDto = new AssessmentResultsDto();
//
//        // Set basic property information
//        assessmentResultsDto.setNewPropertyNumberVc((String) assessmentData.get("newPropertyNumber"));
//        assessmentResultsDto.setWardNoVc((String) assessmentData.get("wardNo"));
//        assessmentResultsDto.setZoneNoVc((String) assessmentData.get("zoneNo"));
//        assessmentResultsDto.setOldPropertyNumberVc((String) assessmentData.get("oldPropertyNumber"));
//
//        // Convert and set unit details
//        List<Map<String, Object>> unitRecords = (List<Map<String, Object>>) assessmentData.get("Unit Records");
//        List<PropertyUnitDetailsDto> unitDetailsDtos = unitRecords.stream().map(record -> {
//            PropertyUnitDetailsDto dto = new PropertyUnitDetailsDto();
//            Map<String, String> unitDetails = (Map<String, String>) record.get("unit");
//            dto.setUnitNoVc(unitDetails.get("udUnitnoVc"));
//            dto.setFloorNoVc(unitDetails.get("udFloornoVc"));
//            dto.setOccupantStatusI(unitDetails.get("udOccupantstatusI"));
//            dto.setUsageTypeVc(unitDetails.get("udUsagetypeI"));
//            dto.setUsageSubTypeVc(unitDetails.get("udUsagesubtypeI"));
//            dto.setConstructionTypeVc(unitDetails.get("udConstructionclassI"));
//            dto.setConstructionYearVc(unitDetails.get("udConstYearDt"));
//            dto.setConstructionAgeVc(unitDetails.get("udConstAgeI"));
//            dto.setCarpetAreaFl(unitDetails.get("udCarpetareaF"));
//            dto.setPlotAreaFl(unitDetails.get("udPlotAreaFl"));
//            dto.setExemptedAreaFl(unitDetails.get("udExemptedAreaF"));
//            dto.setTaxableAreaFl(unitDetails.get("udAssessmentareaF"));
//            dto.setRentValueByRateFl(record.get("taxableValueByRentalRate").toString());
//            dto.setDepreciationRateFl(record.get("depreciationRate").toString());
//            dto.setDepreciationAmountFl(record.get("depreciationAmount").toString());
//            dto.setValueAfterDepreciationFl(record.get("taxableValueByRentalRate").toString());
//            dto.setMaintenanceRepairsFl(record.get("maintenanceRepairAmount").toString());
//            dto.setTaxableValueByRateFl(record.get("taxableValueByRentalRate").toString());
//            dto.setTenantNameVc(unitDetails.get("udTenantnoI"));
//            dto.setActualMonthlyRentFl(unitDetails.get("udRentalnoVc"));
//            dto.setActualAnnualRentFl(record.get("annualRent") != null ? record.get("annualRent").toString() : "0");
//            dto.setTaxableValueByRentFl(record.get("taxableValueByActualRent") != null ? record.get("taxableValueByActualRent").toString() : "0");
//            dto.setTaxableValueConsideredFl(record.get("ratableValue").toString());
//            return dto;
//        }).collect(Collectors.toList());
//        assessmentResultsDto.setUnitDetails(unitDetailsDtos);
//
//        // Convert and set consolidated taxes
//        ConsolidatedTaxDetailsDto consolidatedTaxesDto = new ConsolidatedTaxDetailsDto();
//        Map<String, Double> consolidatedTaxesMap = (Map<String, Double>) assessmentData.get("Consolidated Taxes");
//        consolidatedTaxesDto.setPropertyTaxFl(assessmentData.get("Property Tax").toString());
//        consolidatedTaxesDto.setEducationTaxResidFl(((Map<String, Double>) assessmentData.get("Education Tax")).get("Education Tax(Residential)").toString());
//        consolidatedTaxesDto.setEducationTaxCommFl(((Map<String, Double>) assessmentData.get("Education Tax")).get("Education Tax(Commercial)").toString());
//        consolidatedTaxesDto.setEgcFl(((Map<String, Double>) assessmentData.get("EGC")).get("Employment Guarantee Cess (EGC)").toString());
//        consolidatedTaxesDto.setTreeTaxFl(consolidatedTaxesMap.get("Tree Tax").toString());
//        consolidatedTaxesDto.setEnvironmentalTaxFl(consolidatedTaxesMap.get("Environment Tax").toString());
//        consolidatedTaxesDto.setCleannessTaxFl(consolidatedTaxesMap.get("Cleanness Tax").toString());
//        consolidatedTaxesDto.setLightTaxFl(consolidatedTaxesMap.get("Light Tax").toString());
//        consolidatedTaxesDto.setFireTaxFl(consolidatedTaxesMap.get("Fire Tax").toString());
//        consolidatedTaxesDto.setUserFeesFl(assessmentData.get("Total User Charges").toString());
//        consolidatedTaxesDto.setTotalTaxFl(String.valueOf(consolidatedTaxesMap.values().stream().mapToDouble(Double::doubleValue).sum()));
//        assessmentResultsDto.setConsolidatedTaxes(consolidatedTaxesDto);
//
//        // Convert and set proposed ratable values
//        //ProposedRatableValueDetailsDto proposedRatableValuesDto = new ProposedRatableValueDetailsDto();
//        // Assuming you have this data in your assessmentData, populate the fields
//        // Example (fill in with actual field data as needed):
//        // proposedRatableValuesDto.setResidentFl(assessmentData.get("resident").toString());
//        // Add similar mappings for other fields...
//
//        //assessmentResultsDto.setProposedRatableValues(proposedRatableValuesDto);
//
//        return assessmentResultsDto;
//    }

    private Double nullToZero(Double value) {
        return value != null ? value : 0.0;
    }
}