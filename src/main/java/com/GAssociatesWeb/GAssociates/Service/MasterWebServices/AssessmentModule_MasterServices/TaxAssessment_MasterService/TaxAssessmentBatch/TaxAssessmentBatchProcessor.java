package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentBatch;

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
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitDetails_Entity.UnitDetails_Entity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitUsageTypes_MasterRepository.UnitUsageSubType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.UnitDetails_Repository;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.PreLoadCache;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class TaxAssessmentBatchProcessor implements ItemProcessor<PropertyDetails_Entity, AssessmentResultsDto> {

    @Autowired
    private PreLoadCache preLoadCache;
    @Autowired
    private UnitDetails_Repository unitDetailsRepository;
    @Autowired
    private UnitUsageSubType_MasterRepository unitUsageSubTypeMasterRepository;

    private static final Logger logger = Logger.getLogger(TaxAssessmentBatchProcessor.class.getName());

    @Override
    public AssessmentResultsDto process(PropertyDetails_Entity property) throws Exception {

        Integer zone = property.getPdZoneI();
        List<UnitDetails_Entity> unitDetails = fetchUnitDetails(property.getPdNewpropertynoVc());

        List<Map<String, Object>> unitRecords = new ArrayList<>();
        double totalRatableValue = 0.0;
        double totalUserCharges = 0.0;
        double annualRentalValue = 0.0;
        double taxableValueUnrented = 0.0;

        Map<Long, Double> rateTypeTotals = new HashMap<>();
        Map<Long, Double> maxUserChargesByCategory = new HashMap<>(); //added to track max commercial user charges
        Set<Integer> processedUnitSubUsagesId = new HashSet<>();
        for (UnitDetails_Entity unit : unitDetails) {
            // Log unit details
            Map<String, Object> unitRecord = calculateRatableValueForUnit(unit, zone);
            unitRecords.add(unitRecord);

            Double ratableValue = (Double) unitRecord.get("ratableValue");
            Long categoryId = (Long) unitRecord.get("categoryId");

            logger.info("Ratable value for unit " + unit.getUdUnitnoVc() + ": " + ratableValue + "rvtype:" + ">>>>" + unitRecord.get("rvType"));

            totalRatableValue += (ratableValue != null) ? ratableValue : 0.0;

            if (categoryId != null && ratableValue != null) {
                rateTypeTotals.merge(categoryId, ratableValue, Double::sum);
            }

            if (unit.getUdTenantnoI() != null && !unit.getUdTenantnoI().isEmpty()) {
                double annualRent = Double.parseDouble(unit.getUdRentalnoVc()) * 12;
                logger.info("Annual rent for unit " + unit.getUdUnitnoVc() + ": " + annualRent);
                annualRentalValue += annualRent;
            } else {
                Double amountAfterDepreciation = (Double) unitRecord.get("amountAfterDepreciation");
                taxableValueUnrented += (amountAfterDepreciation != null) ? amountAfterDepreciation : 0.0;
            }

            if (Double.parseDouble(unit.getUdAssessmentareaF()) != 0) {
                if (categoryId == 4 || categoryId == 9) {  // Specifically handling commercial category and comercial open plot category
                    double currentCharge = getUserChargesForUnit(unit);
                    maxUserChargesByCategory.merge(categoryId, currentCharge, Math::max);
                } else if (!processedUnitSubUsagesId.contains(unit.getUdUsagesubtypeI())) {  // For non-commercial units
                    double userCharges = getUserChargesForUnit(unit);
                    totalUserCharges += userCharges;
                    processedUnitSubUsagesId.add(unit.getUdUsagesubtypeI());  // Mark this unit ID as processed
                }
            }
        }

        if (maxUserChargesByCategory.containsKey(4L) || maxUserChargesByCategory.containsKey(9L)) { // this condition checks and add the value
            double maxCharge4 = maxUserChargesByCategory.getOrDefault(4L, 0.0);//this is getting used for commercial property
            double maxCharge9 = maxUserChargesByCategory.getOrDefault(9L, 0.0);//this is getting used for commercial open plot
            totalUserCharges += Math.max(maxCharge4, maxCharge9);
        }

        logger.info("Total ratable value for property " + property.getPdNewpropertynoVc() + ": " + totalRatableValue);
        logger.info("Total annual rental value for property " + property.getPdNewpropertynoVc() + ": " + annualRentalValue);
        logger.info("Total taxable value for unrented units in property " + property.getPdNewpropertynoVc() + ": " + taxableValueUnrented);
        logger.info("Total user charges for property " + property.getPdNewpropertynoVc() + ": " + totalUserCharges);

        // Process education tax, property tax, consolidated taxes, etc.
        Map<String, Double> educationTax = calculateEducationTax(Math.max(0.0,totalRatableValue), unitRecords);
        double propertyTax = calculatePropertyTax(unitRecords);
        Map<String, Double> consolidatedTaxes = calculateConsolidatedTaxes(Math.max(0.0,totalRatableValue), propertyTax);
        Map<String, Double> egc = calculateEgc(Math.max(0.0,totalRatableValue), unitRecords);

        // Build final results and log the assessment result
        AssessmentResultsDto result = buildAssessmentResultsDto(property, Math.max(0.0,totalRatableValue), annualRentalValue,
                taxableValueUnrented, totalUserCharges, educationTax, propertyTax, consolidatedTaxes, egc, unitRecords,rateTypeTotals);

        return result;
    }

    // Method to fetch unit details
    public List<UnitDetails_Entity> fetchUnitDetails(String propertyNumber) {
        return unitDetailsRepository.findAllByPdNewpropertynoVc(propertyNumber);
    }
    // Method to calculate ratable value for a unit
    public Map<String, Object> calculateRatableValueForUnit(UnitDetails_Entity unit, Integer zone) {
        Map<String, Object> unitRecord = new HashMap<>();
        unitRecord.put("unit", extractRelevantUnitDetails(unit));

        Integer unitAge = 0;  // Initialize unit age to 0 by default

        // Fetch Property Rate based on Construction Type and Zone
        String propertyRateKey = unit.getUdConstructionclassI().trim() + "-" + zone;
        PropertyRates_MasterEntity rate = preLoadCache.getPropertyRate(unit.getUdConstructionclassI().trim(), zone.toString().trim());
        if (rate == null) {
            String errorMessage = "Rate not found for construction type: " + unit.getUdConstructionclassI().trim() + " and zone: " + zone;
            logger.warning(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Long usageTypeId = Long.valueOf(unit.getUdUsagetypeI());
        UnitUsageType_MasterEntity unitUsageType = preLoadCache.getUnitUsageType(usageTypeId);
        if (unitUsageType == null) {
            String errorMessage = "Unit Usage Type not found for ID: " + usageTypeId;
            logger.warning(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // Determine RV Type ID based on unit usage subtype
        Long rvTypeId = null;
        if (unit.getUdUsagesubtypeI() != null) {
            UnitUsageSubType_MasterEntity unitUsageSubType = preLoadCache.getUnitUsageSubType(Long.valueOf(unit.getUdUsagesubtypeI()));
            if (unitUsageSubType == null) {
                logger.warning("Unit usage subtype not found for the given ID: " + unit.getUdUsagesubtypeI());
                throw new IllegalArgumentException("Unit usage subtype not found.");
            }
            if (Boolean.TRUE.equals(unitUsageSubType.getUsmApplyDifferentRateVc())) {
                rvTypeId = Long.parseLong(unitUsageSubType.getUsm_rvtype_vc());
            }
        }
        if (rvTypeId == null) {  // Fallback if no subtype or no different rate specified
            rvTypeId = Long.parseLong(unitUsageType.getUum_rvtype_vc());
        }

        // Fetch RV Type entity from cache using RV Type ID
        RVTypes_MasterEntity rvType = preLoadCache.getRVType(rvTypeId);
        if (rvType == null) {
            String errorMessage = "RV Type not found for the given ID: " + rvTypeId;
            logger.warning(errorMessage);
            throw new IllegalArgumentException("RV Type not found.");
        }

        unitRecord.put("rvType", rvType);
        unitRecord.put("rateTypeId", rvTypeId);
        unitRecord.put("categoryId", rvType.getCategory().getCategoryId());//using this to setting the ratable values in there

        double ratePerSqM = Double.parseDouble(rate.getRateI());
        ratePerSqM *= Double.parseDouble(rvType.getRateFl()); // Adjust the rate per sq.m by the multiplier
        unitRecord.put("ratePerSqM", ratePerSqM);

        logger.info("this is rate for batch:"+ratePerSqM);

        double assessmentArea = (unit.getUdAssessmentareaF() != null) ? Double.parseDouble(unit.getUdAssessmentareaF()) : 0.0;

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
        unitRecord.put("depreciationRate", 0.0); // Default value if no depreciation found
        unitRecord.put("depreciationAmount", 0.0); // Default value if no depreciation found
        unitRecord.put("maintenanceRepairAmount", 0.0); // Default value if no depreciation found

        // Handle missing construction year by setting unit age to 0
        String constructionDateStr = unit.getUdConstyearDt();
        if (constructionDateStr != null && !constructionDateStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate constructionDate = LocalDate.parse(constructionDateStr, formatter);

            int constructionYear = constructionDate.getYear();
            int currentYear = LocalDate.now().getYear();
            unitAge = currentYear - constructionYear;
        } else {
            logger.info("No construction year provided for unit. Setting unit age to 0.");
        }

        unitRecord.put("unitAge", unitAge);

        String ageFactor = "0";
        // Get depreciation rate based on unit age
        TaxDepreciation_MasterEntity depreciation = preLoadCache.getTaxDepreciation(unit.getUdConstructionclassI(), unitAge.toString());
        double taxableValueByRentalRate = rentalValue;
        double maintenanceRepairAmount = 0.0;
        if (depreciation != null) {
            double depreciationRate = Double.parseDouble(depreciation.getDepreciationPercentageI());
            double depreciationAmount = Math.round(rentalValue * (depreciationRate / 100.0)); // Calculate depreciation amount

            String minAge = depreciation.getMinAgeI();
            String maxAge = depreciation.getMaxAgeI();
            ageFactor = minAge + "-" + maxAge;
            System.out.println("This is age factor "+ ageFactor);
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
            logger.warning("No depreciation rate found for construction class: " + unit.getUdConstructionclassI() + " and age: " + unitAge);
        }

        double ratableValue = taxableValueByRentalRate;

        if (unit.getUdTenantnoI() != null && !unit.getUdTenantnoI().isEmpty()) {
            double annualRent = Double.parseDouble(unit.getUdRentalnoVc()) * 12;
            double maintenanceRepairAmountForRent = annualRent * 0.10;
            double taxableValueByActualRent = annualRent - maintenanceRepairAmountForRent;

            logger.info("Annual rent: " + annualRent + ", Maintenance repair: " + maintenanceRepairAmountForRent + ", Taxable value by actual rent: " + taxableValueByActualRent);
            unitRecord.put("maintenanceRepairAmountForRent", maintenanceRepairAmountForRent);
            unitRecord.put("taxableValueByActualRent", taxableValueByActualRent);
            unitRecord.put("annualRent", annualRent);

            ratableValue = Math.max(taxableValueByRentalRate, taxableValueByActualRent);
        }

        ratableValue = Math.round(ratableValue);
        unitRecord.put("ratableValue", ratableValue);

        return unitRecord;
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

    // Method to calculate education tax
    public Map<String, Double> calculateEducationTax(double totalRatableValue, List<Map<String, Object>> unitRecords) {
        Map<String, Double> educationTaxDetails = new HashMap<>();

        double residentialRate = 0.0;
        double commercialRate = 0.0;

        if (totalRatableValue == 0) {
            educationTaxDetails.put("Education Tax(Residential)", 0.0);
            educationTaxDetails.put("Education Tax(Commercial)", 0.0);
            educationTaxDetails.put("Education Tax(Total)", 0.0);
            return educationTaxDetails;
        }

        EduCessAndEmpCess_MasterEntity cess = preLoadCache.getEduCessAndEmpCess(totalRatableValue);
        if (cess == null) {
            String errorMessage = "Cess rate not found for the given ratable value range: " + totalRatableValue;
            logger.severe(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

         residentialRate = Double.parseDouble(cess.getResidentialRateFl());
         commercialRate = Double.parseDouble(cess.getCommercialRateFl());

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

        educationTaxDetails.put("Education Tax(Residential)", Math.round(residentialTax * 100.0) / 100.0);
        educationTaxDetails.put("Education Tax(Commercial)", Math.round(commercialTax * 100.0) / 100.0);
        educationTaxDetails.put("Education Tax(Total)", Math.round((residentialTax + commercialTax) * 100.0) / 100.0);

        return educationTaxDetails;
    }

    // Method to calculate property tax
    public double calculatePropertyTax(List<Map<String, Object>> unitRecords) {
        double totalPropertyTax = 0.0;
        Map<Long, Double> rateTypeRatableValues = new HashMap<>();

        // Group ratable values by rate type (RV type)
        for (Map<String, Object> unitRecord : unitRecords) {
            Long rateTypeId = (Long) unitRecord.get("rateTypeId");
            Double unitRatableValue = (Double) unitRecord.get("ratableValue");

            if (unitRatableValue == null) {
                logger.warning("Ratable value is null for unit with details: " + unitRecord.get("unit"));
                unitRatableValue = 0.0;
            }

            // Skip units with null rateTypeId
            if (rateTypeId == null) {
                logger.warning("rateTypeId is null for unit: " + unitRecord.get("unit"));
                continue;  // Skip this unit and move to the next
            }

            rateTypeRatableValues.merge(rateTypeId, unitRatableValue, Double::sum);
        }

        logger.info("Grouped ratable values by rate type: " + rateTypeRatableValues);

        // Calculate property tax for each rate type
        for (Map.Entry<Long, Double> entry : rateTypeRatableValues.entrySet()) {
            Long rateTypeId = entry.getKey();
            double rateTypeRatableValue = entry.getValue();

            RVTypes_MasterEntity rvType = preLoadCache.getRVType(rateTypeId);
            if (rvType == null) {
                String errorMessage = "RV Type not found for the given ID: " + rateTypeId;
                logger.severe(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }

            String applicableTaxes = rvType.getAppliedTaxesVc();

            for (String taxName : applicableTaxes.split(",")) {
                if (taxName.trim().equals("Property Tax") || taxName.trim().equals("Property Tax II")) {
                    ConsolidatedTaxes_MasterEntity taxRate = preLoadCache.getConsolidatedTax(taxName.trim());
                    if (taxRate == null) {
                        String warningMessage = "Tax rate not found for tax name: " + taxName;
                        logger.warning(warningMessage);
                        continue; // Skip this tax and continue with others
                    }

                    double rate = Double.parseDouble(taxRate.getTaxRateFl());
                    double taxAmount = rateTypeRatableValue * (rate / 100);
                    totalPropertyTax += Math.round(taxAmount * 100.0) / 100.0;
                }
            }
        }

        return totalPropertyTax;
    }

    // Method to calculate consolidated taxes
    public Map<String, Double> calculateConsolidatedTaxes(double ratableValue, double propertyTax) {
        Map<String, Double> taxDetails = new HashMap<>();
        List<ConsolidatedTaxes_MasterEntity> taxRates = preLoadCache.getConsolidatedTaxesCache().values().stream().toList();

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
                taxDetails.put(taxRate.getTaxNameVc(), Math.round(taxAmount * 100.0) / 100.0);
            }
        }

        return taxDetails;
    }

    // Method to calculate Employment Guarantee Cess (EGC)
    public Map<String, Double> calculateEgc(double totalRatableValue, List<Map<String, Object>> unitRecords) {
        Map<String, Double> egcDetails = new HashMap<>();
        double egcRate = 0.0;
        if (totalRatableValue == 0) {
            egcDetails.put("Employment Guarantee Cess (EGC)", 0.0);
            return egcDetails;
        }

        // Find the EGC rate based on the total ratable value
        EduCessAndEmpCess_MasterEntity cess = preLoadCache.getEduCessAndEmpCess(totalRatableValue);
        if (cess == null) {
            String errorMessage = "Cess rate not found for the given ratable value range: " + totalRatableValue;
            logger.severe(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        egcRate = Double.parseDouble(cess.getEgcRateFl());

        double egcAmount = 0.0;

        // Calculate EGC for each unit
        for (Map<String, Object> unitRecord : unitRecords) {
            RVTypes_MasterEntity rvType = (RVTypes_MasterEntity) unitRecord.get("rvType");
            Double unitRatableValue = (Double) unitRecord.get("ratableValue");
            if (unitRatableValue == null) {
                unitRatableValue = 0.0;
            }
            String applicableTaxes = rvType.getAppliedTaxesVc();

            // Check if the EGC applies to this unit
            if (applicableTaxes.contains("Employment Guarantee Cess (EGC)")) {
                egcAmount += unitRatableValue * (egcRate / 100);
            }
        }

        // Round the EGC amount to two decimal places and add to the map
        egcDetails.put("Employment Guarantee Cess (EGC)", Math.round(egcAmount * 100.0) / 100.0);
        return egcDetails;
    }

    // Method to calculate user charges for a unit
    public double getUserChargesForUnit(UnitDetails_Entity unit) {
        double userCharges = 0.0;

        // Fetching Unit Usage Subtype entity
        Optional<UnitUsageSubType_MasterEntity> usageSubTypeOpt = unitUsageSubTypeMasterRepository.findById(unit.getUdUsagesubtypeI());
        if (usageSubTypeOpt.isPresent()) {
            UnitUsageSubType_MasterEntity usageSubType = usageSubTypeOpt.get();

            Integer userChargesI = usageSubType.getUsm_usercharges_i();

            if (userChargesI != null) {
                // Since taxType contains the amount directly, use it as user charges
                userCharges = userChargesI.doubleValue();
            } else {
                logger.warning("Tax type (user charges) is null for unit usage subtype ID: " + unit.getUdUsagesubtypeI());
            }
        } else {
            logger.warning("Unit usage subtype not found for ID: " + unit.getUdUsagesubtypeI());
        }

        return userCharges;
    }

    // Method to build the final AssessmentResultsDto
    private AssessmentResultsDto buildAssessmentResultsDto(
            PropertyDetails_Entity property,
            double totalRatableValue,
            double annualRentalValue,
            double taxableValueUnrented,
            double totalUserCharges,
            Map<String, Double> educationTax,
            double propertyTax,
            Map<String, Double> consolidatedTaxes,
            Map<String, Double> egc,
            List<Map<String, Object>> unitRecords,
            Map<Long, Double> rateTypeTotals
    ) {
        // Create a new DTO object to hold the final result
        AssessmentResultsDto assessmentResultsDto = new AssessmentResultsDto();

        // Setting basic property information
        assessmentResultsDto.setPdNewpropertynoVc(property.getPdNewpropertynoVc());
        assessmentResultsDto.setPdFinalpropnoVc(property.getPdFinalpropnoVc());
        assessmentResultsDto.setPdWardI(String.valueOf(property.getPdWardI()));
        assessmentResultsDto.setPdZoneI(String.valueOf(property.getPdZoneI()));
        assessmentResultsDto.setPdOwnernameVc(property.getPdOwnernameVc());
        assessmentResultsDto.setPdOccupinameF(property.getPdOccupinameF());
        assessmentResultsDto.setPdPropertyaddressVc(property.getPdPropertyaddressVc());

        // Final value to consider, which is the sum of annual rental value and taxable value of unrented units
        double finalValueToConsider = annualRentalValue + taxableValueUnrented;
        assessmentResultsDto.setFinalValueToConsiderFl(String.valueOf(finalValueToConsider));

        // Maintenance and repair amount, calculated as 10% of the final value
        double maintenanceAmount = finalValueToConsider * 0.10;
        assessmentResultsDto.setMaintainenceRepairAmountConsideredFl(String.valueOf(maintenanceAmount));

        // Adjust rental values based on repairs (90% of rental and unrented values)
        double annualRentalValueAfterSubtraction = annualRentalValue * 0.90;
        double annualUnRentalValueAfterSubtraction = taxableValueUnrented * 0.90;
        assessmentResultsDto.setTaxableValueRentedFl(String.valueOf(annualRentalValueAfterSubtraction));
        assessmentResultsDto.setTaxableValueUnRentedFl(String.valueOf(annualUnRentalValueAfterSubtraction));

        // Processing unit records
        List<PropertyUnitDetailsDto> unitDetailsDtos = unitRecords.stream().map(unitRecord -> {
            PropertyUnitDetailsDto unitDto = new PropertyUnitDetailsDto();
            Map<String, String> unitDetails = (Map<String, String>) unitRecord.get("unit");

            // Unit details
            unitDto.setNewPropertyNo(unitDetails.get("pdNewpropertynoVc"));
            unitDto.setUnitNoVc(unitDetails.get("udUnitnoVc"));
            unitDto.setFloorNoVc(unitDetails.get("udFloornoVc"));
            unitDto.setOccupantStatusI(unitDetails.get("udOccupantstatusI"));
            unitDto.setUsageTypeVc(unitDetails.get("udUsagetypeI"));
            unitDto.setUsageSubTypeVc(unitDetails.get("udUsagesubtypeI"));
            unitDto.setConstructionTypeVc(unitDetails.get("udConstructionclassI"));
            unitDto.setConstructionYearVc(unitDetails.get("udConstYearDt"));
            unitDto.setConstructionAgeVc(unitDetails.get("udConstAgeI"));
            unitDto.setCarpetAreaFl(unitDetails.get("udCarpetareaF"));
            unitDto.setPlotAreaFl(unitDetails.get("udPlotAreaFl"));
            unitDto.setExemptedAreaFl(unitDetails.get("udExemptedAreaF"));
            unitDto.setTaxableAreaFl(unitDetails.get("udAssessmentareaF"));
            unitDto.setTenantNameVc(unitDetails.get("tenantName"));
            unitDto.setActualMonthlyRentFl(parseDoubleOrDefault(unitDetails.get("udRentalnoVc"), 0.0));

            // Handling both `taxableValueByRentalRate` and `taxableValueByActualRent`
            unitDto.setTaxableValueByRateFl(unitRecord.get("taxableValueByRentalRate") != null ? (Double) unitRecord.get("taxableValueByRentalRate") : 0);
            unitDto.setTaxableValueByRentFl(unitRecord.get("taxableValueByActualRent") != null ? (Double) unitRecord.get("taxableValueByActualRent") : 0);

            // Determining which value to consider, choosing the higher of `taxableValueByRentalRate` and `taxableValueByActualRent`
            double ratableValue = Math.max(
                    (unitRecord.get("taxableValueByRentalRate") != null ? (Double) unitRecord.get("taxableValueByRentalRate") : 0.0),
                    (unitRecord.get("taxableValueByActualRent") != null ? (Double) unitRecord.get("taxableValueByActualRent") : 0.0)
            );
            unitDto.setTaxableValueConsideredFl(ratableValue);

            // Depreciation and maintenance repair amount
            unitDto.setRatePerSqMFl(unitRecord.get("ratePerSqM") != null ? (Double) unitRecord.get("ratePerSqM") : 0);
            unitDto.setRentalValAsPerRateFl(unitRecord.get("rentalValue") != null ? (Double) unitRecord.get("rentalValue"):0);
            unitDto.setDepreciationRateFl(unitRecord.get("depreciationRate") != null ? (Double) unitRecord.get("depreciationRate") : 0);
            unitDto.setDepreciationAmountFl(unitRecord.get("depreciationAmount") != null ? (Double) unitRecord.get("depreciationAmount") : 0);
            unitDto.setValueAfterDepreciationFl(unitRecord.get("amountAfterDepreciation") != null ? (Double) unitRecord.get("amountAfterDepreciation") : 0);
            unitDto.setMaintenanceRepairsFl((Double) unitRecord.get("maintenanceRepairAmount"));
            unitDto.setMaintenanceRepairsRentFl(unitRecord.get("maintenanceRepairAmountForRent") != null ? (Double) unitRecord.get("maintenanceRepairAmountForRent") : 0);
            unitDto.setActualAnnualRentFl(unitRecord.get("annualRent") != null ? (Double) unitRecord.get("annualRent") : 0);
            unitDto.setAgeFactorVc(unitRecord.get("ageFactor") != null ? (String) unitRecord.get("ageFactor") : "0");
            return unitDto;
        }).collect(Collectors.toList());

        // Set unit details in the main DTO
        assessmentResultsDto.setUnitDetails(unitDetailsDtos);

        if (rateTypeTotals == null || rateTypeTotals.isEmpty()) {
            logger.warning("rateTypeTotals is null or empty!");
        }

        // Proposed ratable value details
        ProposedRatableValueDetailsDto proposedRatableValuesDto = new ProposedRatableValueDetailsDto();
        for (Map.Entry<Long, Double> entry : rateTypeTotals.entrySet()) {
            logger.info("check for prvalues3");
            Long categoryId = entry.getKey();
            Double totalValue = entry.getValue();

           // logger.info("this is ratetype:" + rateTypeTotals.entrySet());
            // Map rate type IDs to their corresponding fields
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
                    logger.warning("Unknown category ID: " + categoryId);
                    break;
            }
        }
        proposedRatableValuesDto.setFinalPropertyNoVc(assessmentResultsDto.getPdFinalpropnoVc());
        proposedRatableValuesDto.setAggregateFl(rateTypeTotals.values().stream().mapToDouble(Double::doubleValue).sum());
        assessmentResultsDto.setProposedRatableValues(proposedRatableValuesDto);

        // Consolidated taxes
        ConsolidatedTaxDetailsDto consolidatedTaxDetailsDto = new ConsolidatedTaxDetailsDto();
        consolidatedTaxDetailsDto.setPropertyTaxFl(propertyTax);
        consolidatedTaxDetailsDto.setFinalPropertyNo(assessmentResultsDto.getPdFinalpropnoVc());
        consolidatedTaxDetailsDto.setEducationTaxResidFl(educationTax.get("Education Tax(Residential)"));
        consolidatedTaxDetailsDto.setEducationTaxCommFl(educationTax.get("Education Tax(Commercial)"));
        consolidatedTaxDetailsDto.setEducationTaxTotalFl(educationTax.get("Education Tax(Total)"));
        consolidatedTaxDetailsDto.setEgcFl(egc.get("Employment Guarantee Cess (EGC)"));
        consolidatedTaxDetailsDto.setTreeTaxFl(consolidatedTaxes.get("Tree Tax"));
        consolidatedTaxDetailsDto.setEnvironmentalTaxFl(consolidatedTaxes.get("Environment Tax"));
        consolidatedTaxDetailsDto.setCleannessTaxFl(consolidatedTaxes.get("Cleanness Tax"));
        consolidatedTaxDetailsDto.setLightTaxFl(consolidatedTaxes.get("Light Tax"));
        consolidatedTaxDetailsDto.setFireTaxFl(consolidatedTaxes.get("Fire Tax"));
        consolidatedTaxDetailsDto.setUserChargesFl(totalUserCharges);
        consolidatedTaxDetailsDto.setTotalTaxFl(propertyTax
                + educationTax.get("Education Tax(Residential)")
                + educationTax.get("Education Tax(Commercial)")
                + egc.get("Employment Guarantee Cess (EGC)")
                + totalUserCharges
                + consolidatedTaxes.values().stream().mapToDouble(Double::doubleValue).sum());
        assessmentResultsDto.setConsolidatedTaxes(consolidatedTaxDetailsDto);

        return assessmentResultsDto;
    }

    public static double parseDoubleOrDefault(String value, double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Double.parseDouble(value);
    }
}