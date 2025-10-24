package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.SecondaryBatchAssessmentReport;


import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearingCompleteProperty_Dto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ConsolidatedTaxDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.PropertyUnitDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ProposedRatableValueDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module.Property_RValues;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SecondaryBatchAssessmentReport_MasterServiceImpl implements SecondaryBatchAssessmentReport_MasterService {

    @PersistenceContext
    private EntityManager entityManager;

    // ----------------------------------------------------
    // Public API
    // ----------------------------------------------------
    public List<AfterHearingCompleteProperty_Dto> generateCombinedAfterHearingReport(Integer wardNo) {
        String sql = buildSql();
        List<Tuple> tuples = executeQuery(sql, wardNo);
        return mapTuplesToDtos(tuples);
    }

    // ----------------------------------------------------
    // SQL Builder
    // ----------------------------------------------------
    private String buildSql() {
        StringBuilder flexBefore = new StringBuilder();
        StringBuilder flexAfter = new StringBuilder();

        for (int i = 1; i <= 25; i++) {
            flexBefore.append("pt.pt_tax").append(i).append("_fl AS before_tax").append(i).append(", ");
            flexAfter.append("COALESCE(a.pt_tax").append(i)
                    .append("_fl, pt.pt_tax").append(i).append("_fl, 0) AS after_tax")
                    .append(i).append(", ");
        }

        return """
            SELECT DISTINCT
                p.pd_newpropertyno_vc AS newpropertyno,
                p.pd_finalpropno_vc AS finalpropertyno,
                p.pd_ownername_vc AS ownername,
                p.pd_occupiname_f AS occupiername,
                p.pd_surypropno_vc AS surveypropno,
                p.pd_zone_i AS zone,
                p.pd_ward_i AS ward,

                u.ud_floorno_vc AS floorno,
                u.ud_usagetype_i AS usagetype,
                u.ud_constructionclass_i AS constclass,
                u.ud_carpetarea_f AS carpetarea,
                u.ud_assessmentarea_f AS assessmentarea,

                r.prv_rate_f AS ratepersqm,
                r.prv_lentingvalue_f AS lentingvalue,
                r.prv_depper_i AS deppercent,
                r.prv_depamount_f AS depamount,
                r.prv_alv_f AS amountafterdep,
                r.prv_ratablevalue_f AS ratablevalue,
                pr.pr_totalrv_fl AS totalratablevalue,

                -- BEFORE assessment taxes
                pt.pt_propertytax_fl AS before_propertytax,
                pt.pt_cleantax_fl AS before_cleantax,
                pt.pt_lighttax_fl AS before_lighttax,
                pt.pt_firetax_fl AS before_firetax,
                pt.pt_treetax_fl AS before_treetax,
                pt.pt_environmenttax_fl AS before_environmenttax,
                pt.pt_egctax_fl AS before_egctax,
                pt.pt_usercharges_fl AS before_usercharges,
                pt.pt_miscellaneouscharges_fl AS before_miscellaneouscharges,
                pt.pt_final_tax_fl AS before_finaltax,
                pt.pt_finalrv_fl AS before_finalrv,
            """ + flexBefore + """
                -- AFTER hearing taxes (fallback)
                COALESCE(a.pt_propertytax_fl, pt.pt_propertytax_fl, 0) AS after_propertytax,
                COALESCE(a.pt_cleantax_fl, pt.pt_cleantax_fl, 0) AS after_cleantax,
                COALESCE(a.pt_lighttax_fl, pt.pt_lighttax_fl, 0) AS after_lighttax,
                COALESCE(a.pt_firetax_fl, pt.pt_firetax_fl, 0) AS after_firetax,
                COALESCE(a.pt_treetax_fl, pt.pt_treetax_fl, 0) AS after_treetax,
                COALESCE(a.pt_environmenttax_fl, pt.pt_environmenttax_fl, 0) AS after_environmenttax,
                COALESCE(a.pt_egctax_fl, pt.pt_egctax_fl, 0) AS after_egctax,
                COALESCE(a.pt_usercharges_fl, pt.pt_usercharges_fl, 0) AS after_usercharges,
                COALESCE(a.pt_miscellaneouscharges_fl, pt.pt_miscellaneouscharges_fl, 0) AS after_miscellaneouscharges,
                COALESCE(a.pt_final_tax_fl, pt.pt_final_tax_fl, 0) AS after_finaltax,
                COALESCE(a.pt_finalrv_fl, pt.pt_finalrv_fl, 0) AS after_finalrv,
            """ + flexAfter + """
                old.pod_totalratablevalue_i AS oldrv,
                old.pod_totaltax_fl AS oldtax,
                old.pod_oldpropno_vc AS oldpropertyno,
                old.pod_zone_i AS oldzone

            FROM property_details p
            JOIN unit_details u
                ON p.pd_newpropertyno_vc = u.pd_newpropertyno_vc
            LEFT JOIN property_rvalues r
                ON p.pd_newpropertyno_vc = r.prv_propertyno_vc
               AND CAST(r.prv_unitno_vc AS INTEGER) = u.ud_unitno_vc
            LEFT JOIN proposed_rvalues pr
                ON p.pd_newpropertyno_vc = pr.pr_newpropertyno_vc
            LEFT JOIN property_taxdetails pt
                ON p.pd_newpropertyno_vc = pt.pt_newpropertyno_vc
            LEFT JOIN afterhearing_property_taxdetails a
                ON p.pd_newpropertyno_vc = a.pt_newpropertyno_vc
            LEFT JOIN property_olddetails old
                ON CAST(NULLIF(p.prop_refno, '') AS INTEGER) = old.pod_refno_vc
            WHERE CAST(p.pd_ward_i AS INTEGER) = :wardNo
            ORDER BY p.pd_finalpropno_vc
        """;
    }

    // ----------------------------------------------------
    // Query Execution
    // ----------------------------------------------------
    private List<Tuple> executeQuery(String sql, Integer wardNo) {
        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        query.setParameter("wardNo", wardNo);
        return query.getResultList();
    }

    // ----------------------------------------------------
    // Mapping Logic
    // ----------------------------------------------------
    private List<AfterHearingCompleteProperty_Dto> mapTuplesToDtos(List<Tuple> tuples) {
        List<AfterHearingCompleteProperty_Dto> result = new ArrayList<>();

        for (Tuple t : tuples) {
            AfterHearingCompleteProperty_Dto dto = new AfterHearingCompleteProperty_Dto();

            PropertyDetails_Dto propertyDetails = new PropertyDetails_Dto();
            propertyDetails.setPdNewpropertynoVc(getString(t, "newpropertyno"));
            propertyDetails.setPdFinalpropnoVc(getString(t, "finalpropertyno"));
            propertyDetails.setPdZoneI(getString(t, "zone"));
            propertyDetails.setPdWardI(getString(t, "ward"));
            propertyDetails.setPdOwnernameVc(getString(t, "ownername"));
            propertyDetails.setPdOccupinameF(getString(t, "occupiername"));
            propertyDetails.setPdSurypropnoVc(getString(t, "surveypropno"));
            dto.setPropertyDetails(propertyDetails);
            // ---------------- Property details ----------------
            List<PropertyUnitDetailsDto> propertyUnitDetails = new ArrayList<>();

            PropertyUnitDetailsDto unitDto = new PropertyUnitDetailsDto();

// --- Basic property-unit identifiers ---
            unitDto.setNewPropertyNo(getString(t, "newpropertyno"));
            unitDto.setFinalPropertyNo(getString(t, "finalpropertyno"));
            unitDto.setFloorNoVc(getString(t, "floorno"));
            unitDto.setUsageTypeVc(getString(t, "usagetype"));
            unitDto.setConstructionTypeVc(getString(t, "constclass"));
            unitDto.setCarpetAreaFl(getString(t, "carpetarea"));
            unitDto.setTaxableAreaFl(getString(t, "assessmentarea"));

// --- Ratable value and depreciation details ---
            unitDto.setRatePerSqMFl(getDouble(t, "ratepersqm"));                     // Rate per sq.m
            unitDto.setRentalValAsPerRateFl(getDouble(t, "lentingvalue"));          // Lenting (rental) value
            unitDto.setDepreciationRateFl(getDouble(t, "deppercent"));              // Depreciation rate (%)
            unitDto.setDepreciationAmountFl(getDouble(t, "depamount"));             // Depreciation amount
            unitDto.setAmountAfterDepreciationFl(getDouble(t, "amountafterdep"));   // Value after depreciation
            unitDto.setTaxableValueByRateFl(getDouble(t, "ratablevalue"));          // Ratable value (from proposed_rvalues)
            unitDto.setTaxableValueConsideredFl(getDouble(t, "totalratablevalue")); // Total considered ratable value

// --- Add to list ---
            propertyUnitDetails.add(unitDto);

// --- Set in main DTO ---
            dto.setPropertyUnitDetails(propertyUnitDetails);

            // ---------------- Tax Maps ----------------
            Map<Long, Double> beforeMap = new HashMap<>();
            Map<Long, Double> afterMap = new HashMap<>();

            // Standard taxes BEFORE (using ReportTaxKeys)
            beforeMap.put(ReportTaxKeys.PT1, getDouble(t, "before_propertytax"));
            beforeMap.put(ReportTaxKeys.CLEAN_TAX, getDouble(t, "before_cleantax"));
            beforeMap.put(ReportTaxKeys.LIGHT_TAX, getDouble(t, "before_lighttax"));
            beforeMap.put(ReportTaxKeys.FIRE_TAX, getDouble(t, "before_firetax"));
            beforeMap.put(ReportTaxKeys.TREE_TAX, getDouble(t, "before_treetax"));
            beforeMap.put(ReportTaxKeys.ENV_TAX, getDouble(t, "before_environmenttax"));
            beforeMap.put(ReportTaxKeys.EGC, getDouble(t, "before_egctax"));
            beforeMap.put(ReportTaxKeys.USER_CHG, getDouble(t, "before_usercharges"));
            beforeMap.put(ReportTaxKeys.MISC_CHG, getDouble(t, "before_miscellaneouscharges"));

            // Standard taxes AFTER
            afterMap.put(ReportTaxKeys.PT1, getDouble(t, "after_propertytax"));
            afterMap.put(ReportTaxKeys.CLEAN_TAX, getDouble(t, "after_cleantax"));
            afterMap.put(ReportTaxKeys.LIGHT_TAX, getDouble(t, "after_lighttax"));
            afterMap.put(ReportTaxKeys.FIRE_TAX, getDouble(t, "after_firetax"));
            afterMap.put(ReportTaxKeys.TREE_TAX, getDouble(t, "after_treetax"));
            afterMap.put(ReportTaxKeys.ENV_TAX, getDouble(t, "after_environmenttax"));
            afterMap.put(ReportTaxKeys.EGC, getDouble(t, "after_egctax"));
            afterMap.put(ReportTaxKeys.USER_CHG, getDouble(t, "after_usercharges"));
            afterMap.put(ReportTaxKeys.MISC_CHG, getDouble(t, "after_miscellaneouscharges"));

            // Dynamic tax1..25
            for (int i = 1; i <= 25; i++) {
                beforeMap.put(getDynamicKey(i), getDouble(t, "before_tax" + i));
                afterMap.put(getDynamicKey(i), getDouble(t, "after_tax" + i));
            }

            dto.setTaxKeyValueMapAfterAssess(beforeMap);
            dto.setTaxKeyValueMapAfterHearing(afterMap);

            result.add(dto);
        }
        return result;
    }

    // ----------------------------------------------------
    // Helpers
    // ----------------------------------------------------

    private String getString(Tuple t, String key) {
        Object v = t.get(key);
        return v != null ? v.toString() : null;
    }

    private Double getDouble(Tuple t, String key) {
        Object v = t.get(key);
        if (v == null) return 0.0;
        if (v instanceof Number n) return n.doubleValue();
        try {
            return Double.parseDouble(v.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    /** Map dynamic tax number (1–25) to correct ReportTaxKeys constant. */
    private Long getDynamicKey(int i) {
        return switch (i) {
            case 1 -> ReportTaxKeys.TAX1;
            case 2 -> ReportTaxKeys.TAX2;
            case 3 -> ReportTaxKeys.TAX3;
            case 4 -> ReportTaxKeys.TAX4;
            case 5 -> ReportTaxKeys.TAX5;
            case 6 -> ReportTaxKeys.TAX6;
            case 7 -> ReportTaxKeys.TAX7;
            case 8 -> ReportTaxKeys.TAX8;
            case 9 -> ReportTaxKeys.TAX9;
            case 10 -> ReportTaxKeys.TAX10;
            case 11 -> ReportTaxKeys.TAX11;
            case 12 -> ReportTaxKeys.TAX12;
            case 13 -> ReportTaxKeys.TAX13;
            case 14 -> ReportTaxKeys.TAX14;
            case 15 -> ReportTaxKeys.TAX15;
            case 16 -> ReportTaxKeys.TAX16;
            case 17 -> ReportTaxKeys.TAX17;
            case 18 -> ReportTaxKeys.TAX18;
            case 19 -> ReportTaxKeys.TAX19;
            case 20 -> ReportTaxKeys.TAX20;
            case 21 -> ReportTaxKeys.TAX21;
            case 22 -> ReportTaxKeys.TAX22;
            case 23 -> ReportTaxKeys.TAX23;
            case 24 -> ReportTaxKeys.TAX24;
            case 25 -> ReportTaxKeys.TAX25;
            default -> null;
        };
    }
}
