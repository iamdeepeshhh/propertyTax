package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearing_TaxComputationalService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ProposedRatableValueDetailsDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.ConsolidatedTaxes_MasterEntity.ConsolidatedTaxes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.EduCessAndEmpCess_MasterEntity.EduCessAndEmpCess_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.ConsolidatedTaxes_MasterRepository.ConsolidatedTaxes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.EduCessAndEmpCess_MasterRepository.EduCessAndEmpCess_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitUsageTypes_MasterRepository.UnitUsageSubType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.UnitDetails_Service.UnitDetails_Service;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity.UnitUsageSubType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AfterHearing_TaxCalculationServiceImpl implements AfterHearing_TaxCalculationService{
    @Autowired
    private ConsolidatedTaxes_MasterRepository consolidatedTaxesRepo;

    @Autowired
    private EduCessAndEmpCess_MasterRepository eduCessRepo;

    @Autowired
    private UnitDetails_Service unitDetailsService;

    @Autowired
    private UnitUsageSubType_MasterRepository unitUsageSubTypeRepo;

    private static final Logger log = Logger.getLogger(AfterHearing_TaxCalculationServiceImpl.class.getName());

    private static final long PT1_KEY = 1L;
    private static final long PT2_KEY = 2L;

    // ============================================================
    // 1️⃣ PROPERTY TAX CALCULATION
    // ============================================================
    public double calculatePropertyTax(ProposedRatableValueDetailsDto prv) {
        double pt1Base =
                nullToZero(prv.getResidentialFl()) +
                        nullToZero(prv.getResidentialOpenPlotFl()) +
                        nullToZero(prv.getCommercialFl()) +
                        nullToZero(prv.getCommercialOpenPlotFl()) +
                        nullToZero(prv.getIndustrialFl()) +
                        nullToZero(prv.getIndustrialOpenPlotFl()) +
                        nullToZero(prv.getGovernmentFl()) +
                        nullToZero(prv.getGovernmentOpenPlotFl()) +
                        nullToZero(prv.getElectricSubstationFl()) +
                        nullToZero(prv.getMobileTowerFl());

        double pt2Base =
                nullToZero(prv.getEducationalInstituteFl()) +
                        nullToZero(prv.getEducationAndLegalInstituteOpenPlotFl()) +
                        nullToZero(prv.getReligiousFl()) +
                        nullToZero(prv.getReligiousOpenPlotFl());

        double pt1Rate = getRateByKey(PT1_KEY);
        double pt2Rate = getRateByKey(PT2_KEY);

        double pt1Tax = Math.round(pt1Base * (pt1Rate / 100));
        double pt2Tax = Math.round(pt2Base * (pt2Rate / 100));

        log.info("PT1 Base: " + pt1Base + " | PT1 Tax: " + pt1Tax);
        log.info("PT2 Base: " + pt2Base + " | PT2 Tax: " + pt2Tax);

        return pt1Tax + pt2Tax;
    }

    // ============================================================
    // 2️⃣ EDUCATION CESS (Slab-based per category)
    // ============================================================
    public double calculateEducationCess(ProposedRatableValueDetailsDto prv) {
        double totalEduCess = 0.0;

        // 🏠 Residential
        totalEduCess += calculateEduCessForCategory(prv.getResidentialFl(), true, "Residential");

        // 🏢 Commercial
        totalEduCess += calculateEduCessForCategory(prv.getCommercialFl(), false, "Commercial");

        // ⚙️ Industrial
        totalEduCess += calculateEduCessForCategory(prv.getIndustrialFl(), false, "Industrial");

        // 📡 Mobile Tower
        totalEduCess += calculateEduCessForCategory(prv.getMobileTowerFl(), false, "Mobile Tower");

        // ⚡ Electric Substation
        totalEduCess += calculateEduCessForCategory(prv.getElectricSubstationFl(), false, "Electric Substation");

        log.info("Total Education Cess (all categories): " + totalEduCess);
        return totalEduCess;
    }

    // Breakdown for persisting Residential vs Commercial education taxes
    public Map<Long, Double> calculateEducationCessBreakdown(ProposedRatableValueDetailsDto prv) {
        double res = 0.0;
        double comm = 0.0;

        // Residential portion
        res += calculateEduCessForCategory(prv.getResidentialFl(), true, "Residential");

        // Commercial portion (non-resi buckets)
        comm += calculateEduCessForCategory(prv.getCommercialFl(), false, "Commercial");
        comm += calculateEduCessForCategory(prv.getIndustrialFl(), false, "Industrial");
        comm += calculateEduCessForCategory(prv.getMobileTowerFl(), false, "Mobile Tower");
        comm += calculateEduCessForCategory(prv.getElectricSubstationFl(), false, "Electric Substation");

        Map<Long, Double> map = new HashMap<>();
        map.put(ReportTaxKeys.EDUC_RES, (double) Math.round(res));
        map.put(ReportTaxKeys.EDUC_COMM, (double) Math.round(comm));
        return map;
    }

    private double calculateEduCessForCategory(Double categoryValue, boolean isResidential, String categoryName) {
        double value = nullToZero(categoryValue);
        if (value <= 0) return 0.0;

        Optional<EduCessAndEmpCess_MasterEntity> cessOpt = eduCessRepo.findByRatableValueRange(value);
        if (!cessOpt.isPresent()) return 0.0;

        EduCessAndEmpCess_MasterEntity cess = cessOpt.get();
        double rate = isResidential
                ? Double.parseDouble(cess.getResidentialRateFl())
                : Double.parseDouble(cess.getCommercialRateFl());

        double tax = Math.round(value * (rate / 100));
        log.info("Education Cess [" + categoryName + "] Base: " + value + " | Rate: " + rate + " | Tax: " + tax);
        return tax;
    }

    // ============================================================
    // 3️⃣ EMPLOYMENT GUARANTEE CESS (EGC) — Slab-based per category
    // ============================================================
    public double calculateEgc(ProposedRatableValueDetailsDto prv) {
        double totalEgc = 0.0;

        // Applies only to: Commercial, Industrial, Mobile Tower, Electric Substation, Petrol Pump/Auditorium/Cinema (RateType 2,3,4,8,16)

        totalEgc += calculateEgcForCategory(prv.getCommercialFl(), "Commercial");
        totalEgc += calculateEgcForCategory(prv.getIndustrialFl(), "Industrial");
        totalEgc += calculateEgcForCategory(prv.getMobileTowerFl(), "Mobile Tower");
        totalEgc += calculateEgcForCategory(prv.getElectricSubstationFl(), "Electric Substation");

        log.info("Total EGC (all applicable categories): " + totalEgc);
        return totalEgc;
    }

    private double calculateEgcForCategory(Double categoryValue, String categoryName) {
        double value = nullToZero(categoryValue);
        if (value <= 0) return 0.0;

        Optional<EduCessAndEmpCess_MasterEntity> cessOpt = eduCessRepo.findByRatableValueRange(value);
        if (!cessOpt.isPresent()) return 0.0;

        double egcRate = Double.parseDouble(cessOpt.get().getEgcRateFl());
        double egcTax = Math.round(value * (egcRate / 100));

        log.info("EGC [" + categoryName + "] Base: " + value + " | Rate: " + egcRate + " | Tax: " + egcTax);
        return egcTax;
    }

    // ============================================================
    // 4️⃣ OTHER CONSOLIDATED TAXES (Tree, Fire, etc.)
    // ============================================================
    public Map<Long, Double> calculateConsolidatedTaxes(double finalRV, double propertyTax) {

        // Skip keys handled elsewhere (to avoid duplication)
        Set<Long> skipKeys = Set.of(
                ReportTaxKeys.PT1,           // Property Tax
                ReportTaxKeys.PT2,           // Property Tax II
                ReportTaxKeys.EDUC_RES,      // Education Residential
                ReportTaxKeys.EDUC_COMM,     // Education Commercial
                ReportTaxKeys.EGC,           // Employment Guarantee Cess
                ReportTaxKeys.USER_CHG       // User Charges
        );

        List<ConsolidatedTaxes_MasterEntity> all = consolidatedTaxesRepo.findAll();

        return all.stream()
                .filter(t -> !skipKeys.contains(t.getTaxKeyL()))  // only “other” consolidated taxes
                .collect(Collectors.toMap(
                        ConsolidatedTaxes_MasterEntity::getTaxKeyL,
                        tax -> {
                            // Determine base (Ratable Value or Property Tax)
                            double base = "Property Tax".equalsIgnoreCase(tax.getApplicableonVc())
                                    ? propertyTax
                                    : finalRV;

                            double rate = 0.0;
                            try {
                                rate = Double.parseDouble(tax.getTaxRateFl());
                            } catch (NumberFormatException e) {
                                System.out.println("⚠️ Invalid tax rate for {} (key {}): {}" + tax.getTaxNameVc() + tax.getTaxKeyL() + tax.getTaxRateFl());
                            }

                            // Compute rounded tax value
                            return (double) Math.round(base * (rate / 100.0));
                        }
                ));
    }


    // ============================================================
    // 5️⃣ GRAND TOTAL
    // ============================================================
        // ============================================================
    // 5) USER CHARGES (per unit usage subtype)
    // ============================================================
    public double calculateUserCharges(String newPropertyNo, String categoryI) {
        double totalUserCharges = 0.0;
        Set<Integer> processedSubtypes = new HashSet<>();
        Map<Long, Double> maxByCategory = new HashMap<>();

        int catId = 0;
        try { if (categoryI != null && !categoryI.isBlank()) catId = Integer.parseInt(categoryI.trim()); } catch (Exception ignored) {}

        List<UnitDetails_Dto> units = unitDetailsService.getAllUnitsByProperty(newPropertyNo);
        if (units == null) return 0.0;

        for (UnitDetails_Dto u : units) {
            double assessArea = 0.0;
            try { assessArea = Double.parseDouble(Optional.ofNullable(u.getUdAssessmentAreaF()).orElse("0")); } catch (Exception ignored) {}
            if (assessArea == 0.0) continue;

            Integer subTypeId = u.getUdUsageSubtypeI();
            if (subTypeId == null) continue;

            double charge = 0.0;
            try {
                UnitUsageSubType_MasterEntity sub = unitUsageSubTypeRepo.findById(subTypeId).orElse(null);
                if (sub != null && sub.getUsm_usercharges_i() != null) {
                    charge = sub.getUsm_usercharges_i().doubleValue();
                }
            } catch (Exception ignored) {}

            if (charge <= 0) continue;

            if (catId == 4 || catId == 9) {
                long key = (long) catId;
                maxByCategory.merge(key, charge, Math::max);
            } else {
                if (!processedSubtypes.contains(subTypeId)) {
                    totalUserCharges += charge;
                    processedSubtypes.add(subTypeId);
                }
            }
        }

        if (catId == 4 || catId == 9) {
            double m4 = maxByCategory.getOrDefault(4L, 0.0);
            double m9 = maxByCategory.getOrDefault(9L, 0.0);
            totalUserCharges += Math.max(m4, m9);
        }

        return Math.round(totalUserCharges);
    }
public double calculateTotalTax(ProposedRatableValueDetailsDto prv) {
        double propertyTax = calculatePropertyTax(prv);
        double educationCess = calculateEducationCess(prv);
        double egc = calculateEgc(prv);

        // 🔹 Fetch consolidated taxes using new numeric-key method
        Map<Long, Double> consolidatedTaxes = calculateConsolidatedTaxes(prv.getAggregateFl(), propertyTax);

        double consolidatedTotal = consolidatedTaxes.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        double total = propertyTax + educationCess + egc + consolidatedTotal;

        log.info(String.format(
                "[AFTER_HEARING][TAX_TOTAL] PropertyTax=%.2f | EduCess=%.2f | EGC=%.2f | Consolidated=%.2f | Total=%.2f",
                propertyTax, educationCess, egc, consolidatedTotal, total
        ));

        return Math.round(total);
    }

    // ============================================================
    // 🧩 Helper Methods
    // ============================================================
    private double getRateByKey(long key) {
        return consolidatedTaxesRepo.findByTaxKeyL(key)
                .map(t -> Double.parseDouble(t.getTaxRateFl()))
                .orElse(0.0);
    }

    private double nullToZero(Double val) {
        return val != null ? val : 0.0;
    }
}






