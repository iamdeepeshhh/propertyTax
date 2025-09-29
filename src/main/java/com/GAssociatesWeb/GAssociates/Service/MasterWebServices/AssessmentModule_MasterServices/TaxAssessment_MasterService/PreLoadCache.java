package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.ConsolidatedTaxes_MasterEntity.ConsolidatedTaxes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.EduCessAndEmpCess_MasterEntity.EduCessAndEmpCess_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.PropertyRates_MasterEntity.PropertyRates_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RVTypes_MasterEntity.RVTypes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RvTypesAppliedTaxes_MasterEntity.RvTypesAppliedTaxes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxDepreciation_MasterEntity.TaxDepreciation_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity.UnitUsageSubType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity.UnitUsageType_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.ConsolidatedTaxes_MasterRepository.ConsolidatedTaxes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.EduCessAndEmpCess_MasterRepository.EduCessAndEmpCess_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.PropertyRates_MasterRepository.PropertyRates_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.RVTypes_MasterRepository.RVTypes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.RvTypesAppliedTaxes_MasterRepository.RvTypesAppliedTaxes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxDepreciation_MasterRepository.TaxDepreciation_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitUsageTypes_MasterRepository.UnitUsageSubType_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitUsageTypes_MasterRepository.UnitUsageType_MasterRepository;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
@Data
public class PreLoadCache {

    private final PropertyRates_MasterRepository propertyRatesRepository;
    private final TaxDepreciation_MasterRepository taxDepreciationRepository;
    private final RVTypes_MasterRepository rvTypesRepository;
    private final ConsolidatedTaxes_MasterRepository consolidatedTaxesRepository;
    private final EduCessAndEmpCess_MasterRepository eduCessAndEmpCessRepository;
    private final UnitUsageType_MasterRepository unitUsageTypeMasterRepository;
    private final UnitUsageSubType_MasterRepository unitUsageSubTypeMasterRepository;
    private final RvTypesAppliedTaxes_MasterRepository rvTypesAppliedTaxesRepository;
    private static final Logger logger = Logger.getLogger(PreLoadCache.class.getName());

    private Map<String, PropertyRates_MasterEntity> propertyRatesCache = new HashMap<>();
    private Map<String, TaxDepreciation_MasterEntity> taxDepreciationCache = new HashMap<>();
    private Map<Long, RVTypes_MasterEntity> rvTypesCache = new HashMap<>();
    @Getter
    private Map<Long, ConsolidatedTaxes_MasterEntity> consolidatedTaxesCache = new HashMap<>();
    private Map<String, EduCessAndEmpCess_MasterEntity> eduCessAndEmpCessCache = new HashMap<>();
    private Map<Long, UnitUsageType_MasterEntity> unitUsageCache = new HashMap<>();
    private Map<Long, UnitUsageSubType_MasterEntity> unitUsageSubCache = new HashMap<>();
    private Map<Long, List<Long>> rvTypesAppliedTaxesCache = new HashMap<>();

    @Autowired
    public PreLoadCache(PropertyRates_MasterRepository propertyRatesRepository,
                        TaxDepreciation_MasterRepository taxDepreciationRepository,
                        RVTypes_MasterRepository rvTypesRepository,
                        ConsolidatedTaxes_MasterRepository consolidatedTaxesRepository,
                        EduCessAndEmpCess_MasterRepository eduCessAndEmpCessRepository,
                        UnitUsageType_MasterRepository unitUsageTypeMasterRepository,
                        UnitUsageSubType_MasterRepository unitUsageSubTypeMasterRepository,
                        RvTypesAppliedTaxes_MasterRepository rvTypesAppliedTaxesRepository) {
        this.propertyRatesRepository = propertyRatesRepository;
        this.taxDepreciationRepository = taxDepreciationRepository;
        this.rvTypesRepository = rvTypesRepository;
        this.consolidatedTaxesRepository = consolidatedTaxesRepository;
        this.eduCessAndEmpCessRepository = eduCessAndEmpCessRepository;
        this.unitUsageTypeMasterRepository = unitUsageTypeMasterRepository;
        this.unitUsageSubTypeMasterRepository = unitUsageSubTypeMasterRepository;
        this.rvTypesAppliedTaxesRepository = rvTypesAppliedTaxesRepository;

        // Initialize caches to avoid null pointers
        logger.info("PreLoadCache initialized with repositories and caches initialized.");
    }

    public void preloadAllCaches() {
        logger.info("Starting cache preloading");
        preloadPropertyRates();
        preloadTaxDepreciation();
        preloadRVTypes();
        preloadConsolidatedTaxes();
        preloadEduCessAndEmpCess();
        preloadUnitUsages();
        preloadUnitUsagesSub();
        preloadRvTypesAppliedTaxes();
    }

    private void preloadPropertyRates() {
        List<PropertyRates_MasterEntity> propertyRatesList = propertyRatesRepository.findAll();
        propertyRatesCache = new HashMap<>();
        for (PropertyRates_MasterEntity rate : propertyRatesList) {
            String key = rate.getConstructionTypeVc().trim() + "-" + rate.getTaxRateZoneI().toString().trim();
            propertyRatesCache.put(key, rate);
            // Log cache insertion
        }
    }

    private void preloadTaxDepreciation() {
        List<TaxDepreciation_MasterEntity> taxDepreciationList = taxDepreciationRepository.findAll();
        taxDepreciationCache = new HashMap<>();
        for (TaxDepreciation_MasterEntity depreciation : taxDepreciationList) {
            String key = depreciation.getConstructionClassVc() + "-" + depreciation.getMinAgeI() + "-" + depreciation.getMaxAgeI();
            taxDepreciationCache.put(key, depreciation);
        }
    }

    private void preloadRVTypes() {
        List<RVTypes_MasterEntity> rvTypesList = rvTypesRepository.findAll();
        rvTypesCache = new HashMap<>();
        for (RVTypes_MasterEntity rvType : rvTypesList) {
            rvTypesCache.put(rvType.getRvTypeId(), rvType);
        }
    }

    private void preloadConsolidatedTaxes() {
        List<ConsolidatedTaxes_MasterEntity> consolidatedTaxesList = consolidatedTaxesRepository.findAll();
        consolidatedTaxesCache = new HashMap<>();
        for (ConsolidatedTaxes_MasterEntity tax : consolidatedTaxesList) {
            consolidatedTaxesCache.put(tax.getTaxKeyL(), tax);
        }
    }

    private void preloadEduCessAndEmpCess() {
        List<EduCessAndEmpCess_MasterEntity> eduCessAndEmpCessList = eduCessAndEmpCessRepository.findAll();
        eduCessAndEmpCessCache = new HashMap<>();
        for (EduCessAndEmpCess_MasterEntity cess : eduCessAndEmpCessList) {
            String key = cess.getMinTaxableValueFl() + "-" + cess.getMaxTaxableValueFl();
            eduCessAndEmpCessCache.put(key, cess);
        }
    }

    private void preloadUnitUsages() {
        List<UnitUsageType_MasterEntity> unitUsageTypeList = unitUsageTypeMasterRepository.findAll();
        unitUsageCache = new HashMap<>();
        for (UnitUsageType_MasterEntity unitUsageType : unitUsageTypeList) {
            unitUsageCache.put(Long.valueOf(unitUsageType.getUum_usageid_i()), unitUsageType);
        }
    }

    private void preloadUnitUsagesSub() {
        List<UnitUsageSubType_MasterEntity> unitUsageSubTypeList = unitUsageSubTypeMasterRepository.findAll();
        unitUsageSubCache = new HashMap<>();
        for (UnitUsageSubType_MasterEntity unitUsageSubType : unitUsageSubTypeList) {
            unitUsageSubCache.put(Long.valueOf(unitUsageSubType.getUsm_usagesubid_i()), unitUsageSubType);
        }
    }
    private void preloadRvTypesAppliedTaxes() {
        List<RvTypesAppliedTaxes_MasterEntity> mappings = rvTypesAppliedTaxesRepository.findAll();
        rvTypesAppliedTaxesCache = new HashMap<>();

        for (RvTypesAppliedTaxes_MasterEntity mapping : mappings) {
            rvTypesAppliedTaxesCache
                    .computeIfAbsent(mapping.getRvType().getRvTypeId(), k -> new ArrayList<>())
                    .add(mapping.getTaxKeyL());   // ✅ use taxKeyL instead of tax.id
        }

        logger.info("RVTypesAppliedTaxes Cache: " + rvTypesAppliedTaxesCache);
    }

    // Getter methods for cached data
    public List<Long> getAppliedTaxesForRVType(Long rvTypeId) {
        return rvTypesAppliedTaxesCache.getOrDefault(rvTypeId, new ArrayList<>());
    }
    public PropertyRates_MasterEntity getPropertyRate(String constructionType, String zone) {
        String key = constructionType.trim() + "-" + zone.trim();
        logger.info("Cache Lookup - Searching for key: " + key);  // Log key search
        return propertyRatesCache.get(key);
    }

    public TaxDepreciation_MasterEntity getTaxDepreciation(String constructionClass, String age) {
        for (String key : taxDepreciationCache.keySet()) {
            String[] parts = key.split("-");
            if (parts[0].equals(constructionClass) &&
                    Integer.parseInt(parts[1].trim()) <= Integer.parseInt(age.trim()) &&
                    Integer.parseInt(age.trim()) <= Integer.parseInt(parts[2].trim())) {
                return taxDepreciationCache.get(key);
            }
        }
        return null;
    }

    public RVTypes_MasterEntity getRVType(Long rvTypeId) {
        return rvTypesCache.get(rvTypeId);
    }

    public ConsolidatedTaxes_MasterEntity getConsolidatedTax(Long taxKey) {
        return consolidatedTaxesCache.get(taxKey);
    }

    public EduCessAndEmpCess_MasterEntity getEduCessAndEmpCess(double ratableValue) {
        for (Map.Entry<String, EduCessAndEmpCess_MasterEntity> entry : eduCessAndEmpCessCache.entrySet()) {
            String[] range = entry.getKey().split("-");
            double minValue = Double.parseDouble(range[0]);
            double maxValue = Double.parseDouble(range[1]);
            if (ratableValue >= minValue && ratableValue <= maxValue) {
                return entry.getValue();
            }
        }
        return null;
    }

    public UnitUsageType_MasterEntity getUnitUsageType(Long unitUsageId) {
        return unitUsageCache.get(unitUsageId);
    }

    public UnitUsageSubType_MasterEntity getUnitUsageSubType(Long unitUsageSubId) {
        return unitUsageSubCache.get(unitUsageSubId);
    }

}