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
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.ResultLogsCache;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys;
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
    private ResultLogsCache resultLogsCache;
    @Autowired
    private UnitDetails_Repository unitDetailsRepository;
    @Autowired
    private UnitUsageSubType_MasterRepository unitUsageSubTypeMasterRepository;

    List<String> warnings = new ArrayList<>();

    private static final Logger logger = Logger.getLogger(TaxAssessmentBatchProcessor.class.getName());

    @Override
    public AssessmentResultsDto process(PropertyDetails_Entity property) throws Exception {
        warnings.clear();
        warnings.add("property number:"+ property.getPdFinalpropnoVc());
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
        Map<Long, Double> educationTaxMap = calculateEducationTax(Math.max(0.0,totalRatableValue), unitRecords);
        Map<Long, Double> propertyTaxMap = calculatePropertyTax(unitRecords);
        double propertyTax = propertyTaxMap.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.PT_PARENT, 0.0);
        Map<Long, Double> consolidatedTaxes = calculateConsolidatedTaxes(Math.max(0.0,totalRatableValue), propertyTax);
        Map<Long, Double> egc = calculateEgc(Math.max(0.0,totalRatableValue), unitRecords);

        // Build final results and log the assessment result
        AssessmentResultsDto result = buildAssessmentResultsDto(property, Math.max(0.0,totalRatableValue), annualRentalValue,
                taxableValueUnrented, totalUserCharges, educationTaxMap, propertyTaxMap, propertyTax, consolidatedTaxes, egc, unitRecords,rateTypeTotals);

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
            String warning = "Rate not found for construction type: " + unit.getUdConstructionclassI().trim() + " and zone: " + zone;
            logger.warning(warning);
            warnings.add(warning);
            unitRecord.put("rateTypeId", null);
            unitRecord.put("rvType", null);
            unitRecord.put("categoryId", null);
            unitRecord.put("ratePerSqM", 0.0);
            unitRecord.put("rentalValue", 0.0);
            unitRecord.put("ratableValue", 0.0);
            unitRecord.put("amountAfterDepreciation", 0.0);
            unitRecord.put("taxableValueByRentalRate", 0.0);
            unitRecord.put("maintenanceRepairAmount", 0.0);
            return unitRecord;
        }

        Long usageTypeId = Long.valueOf(unit.getUdUsagetypeI());
        UnitUsageType_MasterEntity unitUsageType = preLoadCache.getUnitUsageType(usageTypeId);
        if (unitUsageType == null) {
            String warning = "Unit Usage Type not found for ID: " + usageTypeId + " for unit: " + unit.getUdUnitnoVc();
            logger.warning(warning);
            warnings.add(warning);
            unitRecord.put("rateTypeId", null);
            unitRecord.put("rvType", null);
            unitRecord.put("categoryId", null);
            unitRecord.put("ratePerSqM", 0.0);
            unitRecord.put("rentalValue", 0.0);
            unitRecord.put("ratableValue", 0.0);
            unitRecord.put("amountAfterDepreciation", 0.0);
            unitRecord.put("taxableValueByRentalRate", 0.0);
            unitRecord.put("maintenanceRepairAmount", 0.0);
            return unitRecord;
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
            String warning = "RV Type not found for RVType ID: " + rvTypeId + " for unit: " + unit.getUdUnitnoVc();
            logger.warning(warning);
            warnings.add(warning);

            unitRecord.put("rateTypeId", rvTypeId);
            unitRecord.put("rvType", null);
            unitRecord.put("categoryId", null);
            unitRecord.put("ratePerSqM", 0.0);
            unitRecord.put("rentalValue", 0.0);
            unitRecord.put("ratableValue", 0.0);
            unitRecord.put("amountAfterDepreciation", 0.0);
            unitRecord.put("taxableValueByRentalRate", 0.0);
            unitRecord.put("maintenanceRepairAmount", 0.0);
            return unitRecord;
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
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate constructionDate = LocalDate.parse(constructionDateStr.trim(), formatter);
                int constructionYear = constructionDate.getYear();
                int currentYear = LocalDate.now().getYear();
                unitAge = currentYear - constructionYear;
            } catch (Exception e) {
                String warning = "Invalid construction year format for unit: " + unit.getUdUnitnoVc() + " | value: " + constructionDateStr;
                logger.warning(warning);
                warnings.add(warning);
                unitAge = 0;
            }
        } else {
            String warning = "Missing construction year for unit: " + unit.getUdUnitnoVc();
            logger.warning(warning);
            warnings.add(warning);
            unitAge = 0;
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
    // Method to calculate education tax using RVType applied taxes
    public Map<Long, Double> calculateEducationTax(
            double totalRatableValue,
            List<Map<String, Object>> unitRecords) {

        Map<Long, Double> educationTaxMap = new HashMap<>();

        // Default 0 values
        educationTaxMap.put(ReportTaxKeys.EDUC_RES, 0.0);
        educationTaxMap.put(ReportTaxKeys.EDUC_COMM, 0.0);

        if (totalRatableValue == 0) {
            return educationTaxMap;
        }

        // Fetch cess rates for the given ratable value
        EduCessAndEmpCess_MasterEntity cess = preLoadCache.getEduCessAndEmpCess(totalRatableValue);
        if (cess == null) {
            logger.warning("⚠ Cess rate not found for RV: " + totalRatableValue + " — Education tax skipped.");
            return educationTaxMap;
        }

        double residentialRate = Double.parseDouble(cess.getResidentialRateFl());
        double commercialRate  = Double.parseDouble(cess.getCommercialRateFl());

        double residentialTax = 0.0;
        double commercialTax  = 0.0;

        for (Map<String, Object> unitRecord : unitRecords) {
            RVTypes_MasterEntity rvType = (RVTypes_MasterEntity) unitRecord.get("rvType");
            if (rvType == null) continue;

            Double unitRatableValue = (Double) unitRecord.get("ratableValue");
            if (unitRatableValue == null) unitRatableValue = 0.0;

            // ✅ Get applied tax keys from cache instead of string names
            List<Long> appliedTaxKeys = preLoadCache.getAppliedTaxesForRVType(rvType.getRvTypeId());

            if (appliedTaxKeys.contains(ReportTaxKeys.EDUC_RES)) {
                residentialTax += unitRatableValue * (residentialRate / 100);
            }
            if (appliedTaxKeys.contains(ReportTaxKeys.EDUC_COMM)) {
                commercialTax += unitRatableValue * (commercialRate / 100);
            }
        }

        // Round and store results
        educationTaxMap.put(ReportTaxKeys.EDUC_RES, Math.round(residentialTax * 100.0) / 100.0);
        educationTaxMap.put(ReportTaxKeys.EDUC_COMM, Math.round(commercialTax * 100.0) / 100.0);

        return educationTaxMap;
    }


    // Method to calculate property tax
    // Method to calculate Property Tax using RVType applied tax keys
    public Map<Long, Double> calculatePropertyTax(List<Map<String, Object>> unitRecords) {
        Map<Long, Double> propertyTaxMap = new HashMap<>();

        // Initialize with 0
        propertyTaxMap.put(ReportTaxKeys.PT1, 0.0);
        propertyTaxMap.put(ReportTaxKeys.PT2, 0.0);
        propertyTaxMap.put(ReportTaxKeys.PT_PARENT, 0.0);

        // Group ratable values by RVType
        Map<Long, Double> rateTypeRatableValues = new HashMap<>();
        for (Map<String, Object> unitRecord : unitRecords) {
            Long rateTypeId = (Long) unitRecord.get("rateTypeId");
            Double unitRatableValue = (Double) unitRecord.get("ratableValue");

            if (rateTypeId == null) continue;
            if (unitRatableValue == null) unitRatableValue = 0.0;

            rateTypeRatableValues.merge(rateTypeId, unitRatableValue, Double::sum);
        }

        // Loop over each RVType’s ratable value
        for (Map.Entry<Long, Double> entry : rateTypeRatableValues.entrySet()) {
            Long rvTypeId = entry.getKey();
            double rvTypeRatableValue = entry.getValue();

            RVTypes_MasterEntity rvType = preLoadCache.getRVType(rvTypeId);
            if (rvType == null) continue;

            // ✅ Get applied tax keys from cache
            List<Long> appliedTaxKeys = preLoadCache.getAppliedTaxesForRVType(rvTypeId);

            for (Long taxKey : appliedTaxKeys) {
                if (taxKey.equals(ReportTaxKeys.PT1) || taxKey.equals(ReportTaxKeys.PT2)) {
                    ConsolidatedTaxes_MasterEntity taxRate = preLoadCache.getConsolidatedTax(taxKey);
                    if (taxRate == null) continue;

                    double rate = Double.parseDouble(taxRate.getTaxRateFl());
                    double taxAmount = rvTypeRatableValue * (rate / 100);

                    propertyTaxMap.merge(taxKey, taxAmount, Double::sum);
                    propertyTaxMap.merge(ReportTaxKeys.PT_PARENT, taxAmount, Double::sum);
                }
            }
        }

        // Round values
        propertyTaxMap.replaceAll((k, v) -> Math.round(v * 100.0) / 100.0);

        return propertyTaxMap;
    }

    // Method to calculate consolidated taxes
    private Map<Long, Double> calculateConsolidatedTaxes(double ratableValue, double propertyTax) {
        return preLoadCache.getConsolidatedTaxesCache().values().stream()
        // skip property tax keys, handled separately
        .filter(tax -> !Set.of(
                ReportTaxKeys.PT1,
                ReportTaxKeys.PT2,
                ReportTaxKeys.PT_PARENT
        ).contains(tax.getTaxKeyL()))
        // collect into map keyed by taxKeyL
        .collect(Collectors.toMap(
                ConsolidatedTaxes_MasterEntity::getTaxKeyL,
                tax -> {
                    double baseValue = switch (tax.getApplicableonVc().toLowerCase()) {
                        case "ratable value" -> ratableValue;
                        case "property tax" -> propertyTax;
                        default -> 0.0;
                    };

                    if (baseValue <= 0) return 0.0;

                    double rate = Double.parseDouble(tax.getTaxRateFl());
                    return Math.round(baseValue * (rate / 100.0) * 100.0) / 100.0;
                }
        ));
    }


    // Method to calculate Employment Guarantee Cess (EGC)
    // Method to calculate Employment Guarantee Cess (EGC) using applied tax keys
    public Map<Long, Double> calculateEgc(double totalRatableValue, List<Map<String, Object>> unitRecords) {
        Map<Long, Double> egcMap = new HashMap<>();
        egcMap.put(ReportTaxKeys.EGC, 0.0);

        if (totalRatableValue == 0) {
            return egcMap;
        }

        // Find the EGC rate based on the total ratable value
        EduCessAndEmpCess_MasterEntity cess = preLoadCache.getEduCessAndEmpCess(totalRatableValue);
        if (cess == null) {
            logger.warning("⚠ Cess rate not found for RV: " + totalRatableValue + " — EGC skipped.");
            return egcMap;
        }

        double egcRate = Double.parseDouble(cess.getEgcRateFl());
        double egcAmount = 0.0;

        // Calculate EGC for each unit
        for (Map<String, Object> unitRecord : unitRecords) {
            RVTypes_MasterEntity rvType = (RVTypes_MasterEntity) unitRecord.get("rvType");
            if (rvType == null) continue;

            Double unitRatableValue = (Double) unitRecord.get("ratableValue");
            if (unitRatableValue == null) unitRatableValue = 0.0;

            // ✅ Get applied tax keys for this RV type
            List<Long> appliedTaxKeys = preLoadCache.getAppliedTaxesForRVType(rvType.getRvTypeId());

            // If EGC applies, calculate for this unit
            if (appliedTaxKeys.contains(ReportTaxKeys.EGC)) {
                egcAmount += unitRatableValue * (egcRate / 100);
            }
        }

        egcMap.put(ReportTaxKeys.EGC, Math.round(egcAmount * 100.0) / 100.0);
        return egcMap;
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
            Map<Long, Double> educationTaxMap,
            Map<Long, Double> propertyTaxMap,
            double propertyTax,
            Map<Long, Double> consolidatedTaxes,
            Map<Long, Double> egc,
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
        assessmentResultsDto.setWarnings(warnings);

        if (!warnings.isEmpty()) {
            resultLogsCache.addDto(assessmentResultsDto);
        }
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
        consolidatedTaxDetailsDto.setPropertyTaxFl(roundUp(propertyTax));
        consolidatedTaxDetailsDto.setFinalPropertyNo(assessmentResultsDto.getPdFinalpropnoVc());
        consolidatedTaxDetailsDto.setEducationTaxResidFl(roundUp(educationTaxMap.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.EDUC_RES, 0.0)));
        consolidatedTaxDetailsDto.setEducationTaxCommFl(roundUp(educationTaxMap.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.EDUC_COMM, 0.0)));
        consolidatedTaxDetailsDto.setEducationTaxTotalFl(roundUp(
                educationTaxMap.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.EDUC_RES, 0.0)
                        + educationTaxMap.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.EDUC_COMM, 0.0)));
        consolidatedTaxDetailsDto.setEgcFl(roundUp(egc.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.EGC, 0.0)));
        consolidatedTaxDetailsDto.setTreeTaxFl(roundUp(consolidatedTaxes.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.TREE_TAX, 0.0)));
        consolidatedTaxDetailsDto.setEnvironmentalTaxFl(roundUp(consolidatedTaxes.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.ENV_TAX, 0.0)));
        consolidatedTaxDetailsDto.setCleannessTaxFl(roundUp(consolidatedTaxes.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.CLEAN_TAX, 0.0)));
        consolidatedTaxDetailsDto.setLightTaxFl(roundUp(consolidatedTaxes.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.LIGHT_TAX, 0.0)));
        consolidatedTaxDetailsDto.setFireTaxFl(roundUp(consolidatedTaxes.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.FIRE_TAX, 0.0)));
        consolidatedTaxDetailsDto.setUserChargesFl(roundUp(totalUserCharges));
        consolidatedTaxDetailsDto.setTotalTaxFl(roundUp(
                propertyTax
                        + educationTaxMap.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.EDUC_RES, 0.0)
                        + educationTaxMap.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.EDUC_COMM, 0.0)
                        + egc.getOrDefault(com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys.EGC, 0.0)
                        + totalUserCharges
                        + consolidatedTaxes.values().stream().mapToDouble(Double::doubleValue).sum()));
        assessmentResultsDto.setConsolidatedTaxes(consolidatedTaxDetailsDto);

        // Build taxKey map for storage (aligns with realtime)
        Map<Long, Double> taxKeyValueMap = new java.util.HashMap<>();
        Long PT1 = ReportTaxKeys.PT1;
        Long PT2 = ReportTaxKeys.PT2;
        Long PT_PARENT = ReportTaxKeys.PT_PARENT;
        Long EDUC_RES = ReportTaxKeys.EDUC_RES;
        Long EDUC_COMM = ReportTaxKeys.EDUC_COMM;
        Long EDUC_PARENT = ReportTaxKeys.EDUC_PARENT;
        Long EGC = ReportTaxKeys.EGC;
        Long USER_CHG = ReportTaxKeys.USER_CHG;

        // Property tax keys
        double pt1Val = propertyTaxMap.getOrDefault(PT1, 0.0);
        double pt2Val = propertyTaxMap.getOrDefault(PT2, 0.0);
        taxKeyValueMap.put(PT1, pt1Val);
        taxKeyValueMap.put(PT2, pt2Val);
        taxKeyValueMap.put(PT_PARENT, pt1Val + pt2Val);

        // Education keys
        double edRes = educationTaxMap.getOrDefault(EDUC_RES, 0.0);
        double edComm = educationTaxMap.getOrDefault(EDUC_COMM, 0.0);
        taxKeyValueMap.put(EDUC_RES, edRes);
        taxKeyValueMap.put(EDUC_COMM, edComm);
        taxKeyValueMap.put(EDUC_PARENT, edRes + edComm);

        // EGC
        taxKeyValueMap.put(EGC, egc.getOrDefault(EGC, 0.0));

        // Consolidated taxes (other)
        consolidatedTaxes.forEach(taxKeyValueMap::put);

        // User charges
        taxKeyValueMap.put(USER_CHG, totalUserCharges);

        assessmentResultsDto.setTaxKeyValueMap(taxKeyValueMap);

        return assessmentResultsDto;
    }

    public static double parseDoubleOrDefault(String value, double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Double.parseDouble(value);
    }
    public static double roundUp(double value) {

        return Math.round(value);
    }
}
