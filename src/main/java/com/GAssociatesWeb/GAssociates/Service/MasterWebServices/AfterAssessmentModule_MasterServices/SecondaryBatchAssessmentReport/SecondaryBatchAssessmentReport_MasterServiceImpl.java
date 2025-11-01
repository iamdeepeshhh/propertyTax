package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.SecondaryBatchAssessmentReport;


import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearingCompleteProperty_Dto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearingPropertySummary_Dto;
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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SecondaryBatchAssessmentReport_MasterServiceImpl implements SecondaryBatchAssessmentReport_MasterService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AfterHearingCompleteProperty_Dto> generateCombinedAfterHearingReport(Integer wardNo) {
        String sql = buildSql();
        List<Tuple> tuples = executeQuery(sql, wardNo);
        List<AfterHearingCompleteProperty_Dto> flatList = mapTuplesToDtos(tuples);
        return aggregateProperties(flatList);
    }

    /**
     * Builds the static SQL block for dynamic tax columns (pt_tax1_fl → pt_tax25_fl)
     * with both BEFORE and AFTER aliases.
     */
    private String buildDynamicTaxColumns() {
        return """
        -- ========== BEFORE Dynamic Taxes ==========
        pt.pt_tax1_fl  AS before_tax1,
        pt.pt_tax2_fl  AS before_tax2,
        pt.pt_tax3_fl  AS before_tax3,
        pt.pt_tax4_fl  AS before_tax4,
        pt.pt_tax5_fl  AS before_tax5,
        pt.pt_tax6_fl  AS before_tax6,
        pt.pt_tax7_fl  AS before_tax7,
        pt.pt_tax8_fl  AS before_tax8,
        pt.pt_tax9_fl  AS before_tax9,
        pt.pt_tax10_fl AS before_tax10,
        pt.pt_tax11_fl AS before_tax11,
        pt.pt_tax12_fl AS before_tax12,
        pt.pt_tax13_fl AS before_tax13,
        pt.pt_tax14_fl AS before_tax14,
        pt.pt_tax15_fl AS before_tax15,
        pt.pt_tax16_fl AS before_tax16,
        pt.pt_tax17_fl AS before_tax17,
        pt.pt_tax18_fl AS before_tax18,
        pt.pt_tax19_fl AS before_tax19,
        pt.pt_tax20_fl AS before_tax20,
        pt.pt_tax21_fl AS before_tax21,
        pt.pt_tax22_fl AS before_tax22,
        pt.pt_tax23_fl AS before_tax23,
        pt.pt_tax24_fl AS before_tax24,
        pt.pt_tax25_fl AS before_tax25,

        -- ========== AFTER Dynamic Taxes ==========
        COALESCE(a.pt_tax1_fl,  pt.pt_tax1_fl,  0) AS after_tax1,
        COALESCE(a.pt_tax2_fl,  pt.pt_tax2_fl,  0) AS after_tax2,
        COALESCE(a.pt_tax3_fl,  pt.pt_tax3_fl,  0) AS after_tax3,
        COALESCE(a.pt_tax4_fl,  pt.pt_tax4_fl,  0) AS after_tax4,
        COALESCE(a.pt_tax5_fl,  pt.pt_tax5_fl,  0) AS after_tax5,
        COALESCE(a.pt_tax6_fl,  pt.pt_tax6_fl,  0) AS after_tax6,
        COALESCE(a.pt_tax7_fl,  pt.pt_tax7_fl,  0) AS after_tax7,
        COALESCE(a.pt_tax8_fl,  pt.pt_tax8_fl,  0) AS after_tax8,
        COALESCE(a.pt_tax9_fl,  pt.pt_tax9_fl,  0) AS after_tax9,
        COALESCE(a.pt_tax10_fl, pt.pt_tax10_fl, 0) AS after_tax10,
        COALESCE(a.pt_tax11_fl, pt.pt_tax11_fl, 0) AS after_tax11,
        COALESCE(a.pt_tax12_fl, pt.pt_tax12_fl, 0) AS after_tax12,
        COALESCE(a.pt_tax13_fl, pt.pt_tax13_fl, 0) AS after_tax13,
        COALESCE(a.pt_tax14_fl, pt.pt_tax14_fl, 0) AS after_tax14,
        COALESCE(a.pt_tax15_fl, pt.pt_tax15_fl, 0) AS after_tax15,
        COALESCE(a.pt_tax16_fl, pt.pt_tax16_fl, 0) AS after_tax16,
        COALESCE(a.pt_tax17_fl, pt.pt_tax17_fl, 0) AS after_tax17,
        COALESCE(a.pt_tax18_fl, pt.pt_tax18_fl, 0) AS after_tax18,
        COALESCE(a.pt_tax19_fl, pt.pt_tax19_fl, 0) AS after_tax19,
        COALESCE(a.pt_tax20_fl, pt.pt_tax20_fl, 0) AS after_tax20,
        COALESCE(a.pt_tax21_fl, pt.pt_tax21_fl, 0) AS after_tax21,
        COALESCE(a.pt_tax22_fl, pt.pt_tax22_fl, 0) AS after_tax22,
        COALESCE(a.pt_tax23_fl, pt.pt_tax23_fl, 0) AS after_tax23,
        COALESCE(a.pt_tax24_fl, pt.pt_tax24_fl, 0) AS after_tax24,
        COALESCE(a.pt_tax25_fl, pt.pt_tax25_fl, 0) AS after_tax25,
    """;
    }

    // ============================================================
    // SQL BUILDER (includes before + after taxes)
    // ============================================================
    private String buildSql() {

        return """
            SELECT DISTINCT
                COALESCE(apd.pd_newpropertyno_vc, p.pd_newpropertyno_vc) AS newpropertyno,
                COALESCE(apd.pd_finalpropno_vc, p.pd_finalpropno_vc) AS finalpropertyno,
                COALESCE(apd.pd_ownername_vc, p.pd_ownername_vc) AS ownername,
                COALESCE(apd.pd_occupiname_f, p.pd_occupiname_f) AS occupiername,
                COALESCE(apd.pd_surypropno_vc, p.pd_surypropno_vc) AS surveypropno,
                COALESCE(apd.pd_zone_i, p.pd_zone_i) AS zone,
                COALESCE(apd.pd_ward_i, p.pd_ward_i) AS ward,
                ro.rg_hearingstatus_vc AS hearing_status,

                COALESCE(aud.ud_floorno_vc, u.ud_floorno_vc) AS floorno,
                COALESCE(aud.ud_usagetype_i, u.ud_usagetype_i) AS usagetype,
                COALESCE(aud.ud_occupantstatus_i, u.ud_occupantstatus_i) AS occupantstatus,
                COALESCE(aud.ud_constructionclass_i, u.ud_constructionclass_i) AS constclass,
                COALESCE(aud.ud_carpetarea_f, u.ud_carpetarea_f) AS carpetarea,
                COALESCE(aud.ud_assessmentarea_f, u.ud_assessmentarea_f) AS assessmentarea,

                COALESCE(ar.prv_yearlyrent_fl, r.prv_yearlyrent_fl, 0) AS yearly_rent,
                CASE 
                    WHEN COALESCE(ar.prv_rent_fl, r.prv_rent_fl, 0) = 0 
                     AND COALESCE(ar.prv_yearlyrent_fl, r.prv_yearlyrent_fl, 0) = 0
                    THEN COALESCE(ar.prv_alv_f, r.prv_alv_f, 0)
                    ELSE 0
                END AS alv,
                (
                    COALESCE(ar.prv_yearlyrent_fl, r.prv_yearlyrent_fl, 0) +
                    CASE 
                        WHEN COALESCE(ar.prv_rent_fl, r.prv_rent_fl, 0) = 0 
                         AND COALESCE(ar.prv_yearlyrent_fl, r.prv_yearlyrent_fl, 0) = 0
                        THEN COALESCE(ar.prv_alv_f, r.prv_alv_f, 0)
                        ELSE 0
                    END
                ) AS total_alv,
                COALESCE(ar.prv_mainval_f, r.prv_mainval_f, 0) AS maintenance_value,
                COALESCE(ar.prv_taxvalue_f, r.prv_taxvalue_f, 0) AS tax_value,
                COALESCE(ar.prv_ratablevalue_f, r.prv_ratablevalue_f, 0) AS ratable_value,
                GREATEST(
                    COALESCE(ar.prv_ratablevalue_f, r.prv_ratablevalue_f, 0),
                    COALESCE(ar.prv_taxvalue_f, r.prv_taxvalue_f, 0)
                ) AS considered_rv,
                pr.pr_totalrv_fl AS before_finalrv,
                apr.pr_totalrv_fl AS after_finalrv,

                -- ========== BEFORE taxes ==========
                pt.pt_propertytax_fl AS before_propertytax,
                pt.pt_cleantax_fl AS before_cleantax,
                pt.pt_lighttax_fl AS before_lighttax,
                pt.pt_firetax_fl AS before_firetax,
                pt.pt_treetax_fl AS before_treetax,
                pt.pt_environmenttax_fl AS before_environmenttax,
                pt.pt_egctax_fl AS before_egctax,
                pt.pt_usercharges_fl AS before_usercharges,
                pt.pt_miscellaneouscharges_fl AS before_miscellaneouscharges,
                pt.pt_waterbenefittax_fl AS before_water_ben,
                pt.pt_watertax_fl AS before_water_tax,
                pt.pt_seweragebenefittax_fl AS before_sewerage_ben,
                pt.pt_seweragetax_fl AS before_sewerage_tax,
                pt.pt_streettax_fl AS before_street_tax,
                pt.pt_specialconservancytax_fl AS before_spec_cons,
                pt.pt_municipaledutax_fl AS before_municipal_edu,
                pt.pt_specialedutax_fl AS before_special_edu,
                pt.pt_edutax_fl AS before_educationtax,
                pt.pt_edurestax_fl AS before_educ_res,
                pt.pt_edunrestax_fl AS before_educ_comm,
                pt.pt_servicecharges_fl AS before_service_chg,
                pt.pt_final_tax_fl AS before_finaltax,
                
                -- ========== AFTER taxes ==========
                COALESCE(a.pt_propertytax_fl, pt.pt_propertytax_fl, 0) AS after_propertytax,
                COALESCE(a.pt_cleantax_fl, pt.pt_cleantax_fl, 0) AS after_cleantax,
                COALESCE(a.pt_lighttax_fl, pt.pt_lighttax_fl, 0) AS after_lighttax,
                COALESCE(a.pt_firetax_fl, pt.pt_firetax_fl, 0) AS after_firetax,
                COALESCE(a.pt_treetax_fl, pt.pt_treetax_fl, 0) AS after_treetax,
                COALESCE(a.pt_environmenttax_fl, pt.pt_environmenttax_fl, 0) AS after_environmenttax,
                COALESCE(a.pt_egctax_fl, pt.pt_egctax_fl, 0) AS after_egctax,
                COALESCE(a.pt_usercharges_fl, pt.pt_usercharges_fl, 0) AS after_usercharges,
                COALESCE(a.pt_miscellaneouscharges_fl, pt.pt_miscellaneouscharges_fl, 0) AS after_miscellaneouscharges,
                COALESCE(a.pt_waterbenefittax_fl, pt.pt_waterbenefittax_fl, 0) AS after_water_ben,
                COALESCE(a.pt_watertax_fl, pt.pt_watertax_fl, 0) AS after_water_tax,
                COALESCE(a.pt_seweragebenefittax_fl, pt.pt_seweragebenefittax_fl, 0) AS after_sewerage_ben,
                COALESCE(a.pt_seweragetax_fl, pt.pt_seweragetax_fl, 0) AS after_sewerage_tax,
                COALESCE(a.pt_streettax_fl, pt.pt_streettax_fl, 0) AS after_street_tax,
                COALESCE(a.pt_specialconservancytax_fl, pt.pt_specialconservancytax_fl, 0) AS after_spec_cons,
                COALESCE(a.pt_municipaledutax_fl, pt.pt_municipaledutax_fl, 0) AS after_municipal_edu,
                COALESCE(a.pt_specialedutax_fl, pt.pt_specialedutax_fl, 0) AS after_special_edu,
                COALESCE(a.pt_edutax_fl, pt.pt_edutax_fl, 0) AS after_educationtax,
                COALESCE(a.pt_edurestax_fl, pt.pt_edurestax_fl, 0) AS after_educ_res,
                COALESCE(a.pt_edunrestax_fl, pt.pt_edunrestax_fl, 0) AS after_educ_comm,
                COALESCE(a.pt_servicecharges_fl, pt.pt_servicecharges_fl, 0) AS after_service_chg,
                COALESCE(a.pt_final_tax_fl, pt.pt_final_tax_fl, 0) AS after_finaltax,
               
           """
                           + buildDynamicTaxColumns() + """
                old.pod_totalratablevalue_i AS oldrv,
                old.pod_totaltax_fl AS oldtax,
                old.pod_oldpropno_vc AS oldpropertyno,
                old.pod_zone_i AS oldzone
            FROM property_details p
            LEFT JOIN afterhearing_property_details apd
              ON p.pd_newpropertyno_vc = apd.pd_newpropertyno_vc
            JOIN (
                SELECT u.pd_newpropertyno_vc, u.ud_floorno_vc, u.ud_unitno_vc
                FROM unit_details u
                UNION
                SELECT aud.pd_newpropertyno_vc, aud.ud_floorno_vc, aud.ud_unitno_vc
                FROM afterhearing_unit_details aud
            ) ux
              ON ux.pd_newpropertyno_vc = p.pd_newpropertyno_vc
            LEFT JOIN unit_details u
              ON u.pd_newpropertyno_vc = ux.pd_newpropertyno_vc
             AND u.ud_unitno_vc = ux.ud_unitno_vc
             AND u.ud_floorno_vc = ux.ud_floorno_vc
            LEFT JOIN afterhearing_unit_details aud
              ON aud.pd_newpropertyno_vc = ux.pd_newpropertyno_vc
             AND aud.ud_unitno_vc = ux.ud_unitno_vc
             AND aud.ud_floorno_vc = ux.ud_floorno_vc
            LEFT JOIN register_objection ro
              ON ro.rg_newpropertyno_vc = p.pd_newpropertyno_vc
            LEFT JOIN property_rvalues r
              ON p.pd_newpropertyno_vc = r.prv_propertyno_vc
             AND CAST(r.prv_unitno_vc AS INTEGER) = CAST(ux.ud_unitno_vc AS INTEGER)
            LEFT JOIN afterhearing_property_rvalues ar
              ON p.pd_newpropertyno_vc = ar.prv_propertyno_vc
             AND CAST(ar.prv_unitno_vc AS INTEGER) = CAST(ux.ud_unitno_vc AS INTEGER)
            LEFT JOIN property_taxdetails pt
              ON p.pd_newpropertyno_vc = pt.pt_newpropertyno_vc
            LEFT JOIN afterhearing_property_taxdetails a
              ON p.pd_newpropertyno_vc = a.pt_newpropertyno_vc
            LEFT JOIN afterhearing_proposed_rvalues apr
              ON p.pd_newpropertyno_vc = apr.pr_newpropertyno_vc
            Left JOIN proposed_rvalues pr
              ON p.pd_newpropertyno_vc = pr.pr_newpropertyno_vc
            LEFT JOIN property_olddetails old
              ON CAST(NULLIF(p.prop_refno, '') AS INTEGER) = old.pod_refno_vc
            WHERE CAST(COALESCE(apd.pd_ward_i, p.pd_ward_i) AS INTEGER) = :wardNo
            ORDER BY COALESCE(apd.pd_finalpropno_vc, p.pd_finalpropno_vc)
        """;
    }

    private List<Tuple> executeQuery(String sql, Integer wardNo) {
        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        query.setParameter("wardNo", wardNo);
        return query.getResultList();
    }

    // ============================================================
    // MAP + AGGREGATE
    // ============================================================
    private List<AfterHearingCompleteProperty_Dto> mapTuplesToDtos(List<Tuple> tuples) {
        List<AfterHearingCompleteProperty_Dto> result = new ArrayList<>();

        for (Tuple t : tuples) {
            AfterHearingCompleteProperty_Dto dto = new AfterHearingCompleteProperty_Dto();

            PropertyDetails_Dto pd = new PropertyDetails_Dto();
            pd.setPdNewpropertynoVc(getString(t, "newpropertyno"));
            pd.setPdFinalpropnoVc(getString(t, "finalpropertyno"));
            pd.setPdZoneI(getString(t, "zone"));
            pd.setPdWardI(getString(t, "ward"));
            pd.setPdOwnernameVc(getString(t, "ownername"));
            pd.setPdOccupinameF(getString(t, "occupiername"));
            pd.setPdSurypropnoVc(getString(t, "surveypropno"));
            dto.setPropertyDetails(pd);
            // Map hearing status from register_objection if present
            dto.setHearingStatus(getString(t, "hearing_status"));

            PropertyUnitDetailsDto unit = new PropertyUnitDetailsDto();
            unit.setNewPropertyNo(getString(t, "newpropertyno"));
            unit.setFinalPropertyNo(getString(t, "finalpropertyno"));
            unit.setFloorNoVc(getString(t, "floorno"));
            unit.setUsageTypeVc(String.valueOf(getString(t, "usagetype")));
            unit.setOccupantStatusI(getString(t, "occupantstatus"));
            unit.setConstructionTypeVc(String.valueOf(getString(t, "constclass")));
            unit.setCarpetAreaFl(String.valueOf(getDouble(t, "carpetarea")));
            unit.setTaxableAreaFl(String.valueOf(getDouble(t, "assessmentarea")));
            unit.setYearlyRentFl(getDouble(t, "yearly_rent"));
            unit.setAlvFl(getDouble(t, "alv"));
            unit.setTotalAlvFl(getDouble(t, "total_alv"));
            unit.setMaintenanceValueFl(getDouble(t, "maintenance_value"));
            unit.setTaxValueFl(getDouble(t, "tax_value"));
            unit.setRatableValueFl(getDouble(t, "ratable_value"));
            unit.setConsideredRvFl(getDouble(t, "considered_rv"));

            Map<Long, Double> beforeMap = new HashMap<>();
            Map<Long, Double> afterMap = new HashMap<>();

            beforeMap.put(ReportTaxKeys.PT_PARENT,           getDouble(t, "before_propertytax"));

            beforeMap.put(ReportTaxKeys.EDUC_PARENT,    getDouble(t, "before_educationtax"));
            beforeMap.put(ReportTaxKeys.EDUC_RES,      getDouble(t, "before_educ_res"));
            beforeMap.put(ReportTaxKeys.EDUC_COMM,     getDouble(t, "before_educ_comm"));
            beforeMap.put(ReportTaxKeys.EGC,           getDouble(t, "before_egctax"));
            beforeMap.put(ReportTaxKeys.TREE_TAX,      getDouble(t, "before_treetax"));
            beforeMap.put(ReportTaxKeys.ENV_TAX,       getDouble(t, "before_environmenttax"));
            beforeMap.put(ReportTaxKeys.CLEAN_TAX,     getDouble(t, "before_cleantax"));
            beforeMap.put(ReportTaxKeys.LIGHT_TAX,     getDouble(t, "before_lighttax"));
            beforeMap.put(ReportTaxKeys.FIRE_TAX,      getDouble(t, "before_firetax"));
            beforeMap.put(ReportTaxKeys.WATER_TAX,     getDouble(t, "before_water_tax"));
            beforeMap.put(ReportTaxKeys.SEWERAGE_TAX,  getDouble(t, "before_sewerage_tax"));
            beforeMap.put(ReportTaxKeys.SEWERAGE_BEN,  getDouble(t, "before_sewerage_ben"));
            beforeMap.put(ReportTaxKeys.WATER_BEN,     getDouble(t, "before_water_ben"));
            beforeMap.put(ReportTaxKeys.STREET_TAX,    getDouble(t, "before_street_tax"));
            beforeMap.put(ReportTaxKeys.SPEC_CONS,     getDouble(t, "before_spec_cons"));
            beforeMap.put(ReportTaxKeys.MUNICIPAL_EDU, getDouble(t, "before_municipal_edu"));
            beforeMap.put(ReportTaxKeys.SPECIAL_EDU,   getDouble(t, "before_special_edu"));
            beforeMap.put(ReportTaxKeys.SERVICE_CHG,   getDouble(t, "before_service_chg"));
            beforeMap.put(ReportTaxKeys.MISC_CHG,      getDouble(t, "before_miscellaneouscharges"));
            beforeMap.put(ReportTaxKeys.USER_CHG,      getDouble(t, "before_usercharges"));

            afterMap.put(ReportTaxKeys.PT_PARENT,           getDouble(t, "after_propertytax"));

            afterMap.put(ReportTaxKeys.EDUC_PARENT,   getDouble(t, "after_educationtax"));
            afterMap.put(ReportTaxKeys.EDUC_RES,      getDouble(t, "after_educ_res"));
            afterMap.put(ReportTaxKeys.EDUC_COMM,     getDouble(t, "after_educ_comm"));
            afterMap.put(ReportTaxKeys.EGC,           getDouble(t, "after_egctax"));
            afterMap.put(ReportTaxKeys.TREE_TAX,      getDouble(t, "after_treetax"));
            afterMap.put(ReportTaxKeys.ENV_TAX,       getDouble(t, "after_environmenttax"));
            afterMap.put(ReportTaxKeys.CLEAN_TAX,     getDouble(t, "after_cleantax"));
            afterMap.put(ReportTaxKeys.LIGHT_TAX,     getDouble(t, "after_lighttax"));
            afterMap.put(ReportTaxKeys.FIRE_TAX,      getDouble(t, "after_firetax"));
            afterMap.put(ReportTaxKeys.WATER_TAX,     getDouble(t, "after_water_tax"));
            afterMap.put(ReportTaxKeys.SEWERAGE_TAX,  getDouble(t, "after_sewerage_tax"));
            afterMap.put(ReportTaxKeys.SEWERAGE_BEN,  getDouble(t, "after_sewerage_ben"));
            afterMap.put(ReportTaxKeys.WATER_BEN,     getDouble(t, "after_water_ben"));
            afterMap.put(ReportTaxKeys.STREET_TAX,    getDouble(t, "after_street_tax"));
            afterMap.put(ReportTaxKeys.SPEC_CONS,     getDouble(t, "after_spec_cons"));
            afterMap.put(ReportTaxKeys.MUNICIPAL_EDU, getDouble(t, "after_municipal_edu"));
            afterMap.put(ReportTaxKeys.SPECIAL_EDU,   getDouble(t, "after_special_edu"));
            afterMap.put(ReportTaxKeys.SERVICE_CHG,   getDouble(t, "after_service_chg"));
            afterMap.put(ReportTaxKeys.MISC_CHG,      getDouble(t, "after_miscellaneouscharges"));
            afterMap.put(ReportTaxKeys.USER_CHG,      getDouble(t, "after_usercharges"));

            beforeMap.put(ReportTaxKeys.TAX1,  getDouble(t, "before_tax1"));
            beforeMap.put(ReportTaxKeys.TAX2,  getDouble(t, "before_tax2"));
            beforeMap.put(ReportTaxKeys.TAX3,  getDouble(t, "before_tax3"));
            beforeMap.put(ReportTaxKeys.TAX4,  getDouble(t, "before_tax4"));
            beforeMap.put(ReportTaxKeys.TAX5,  getDouble(t, "before_tax5"));
            beforeMap.put(ReportTaxKeys.TAX6,  getDouble(t, "before_tax6"));
            beforeMap.put(ReportTaxKeys.TAX7,  getDouble(t, "before_tax7"));
            beforeMap.put(ReportTaxKeys.TAX8,  getDouble(t, "before_tax8"));
            beforeMap.put(ReportTaxKeys.TAX9,  getDouble(t, "before_tax9"));
            beforeMap.put(ReportTaxKeys.TAX10, getDouble(t, "before_tax10"));
            beforeMap.put(ReportTaxKeys.TAX11, getDouble(t, "before_tax11"));
            beforeMap.put(ReportTaxKeys.TAX12, getDouble(t, "before_tax12"));
            beforeMap.put(ReportTaxKeys.TAX13, getDouble(t, "before_tax13"));
            beforeMap.put(ReportTaxKeys.TAX14, getDouble(t, "before_tax14"));
            beforeMap.put(ReportTaxKeys.TAX15, getDouble(t, "before_tax15"));
            beforeMap.put(ReportTaxKeys.TAX16, getDouble(t, "before_tax16"));
            beforeMap.put(ReportTaxKeys.TAX17, getDouble(t, "before_tax17"));
            beforeMap.put(ReportTaxKeys.TAX18, getDouble(t, "before_tax18"));
            beforeMap.put(ReportTaxKeys.TAX19, getDouble(t, "before_tax19"));
            beforeMap.put(ReportTaxKeys.TAX20, getDouble(t, "before_tax20"));
            beforeMap.put(ReportTaxKeys.TAX21, getDouble(t, "before_tax21"));
            beforeMap.put(ReportTaxKeys.TAX22, getDouble(t, "before_tax22"));
            beforeMap.put(ReportTaxKeys.TAX23, getDouble(t, "before_tax23"));
            beforeMap.put(ReportTaxKeys.TAX24, getDouble(t, "before_tax24"));
            beforeMap.put(ReportTaxKeys.TAX25, getDouble(t, "before_tax25"));

            afterMap.put(ReportTaxKeys.TAX1,  getDouble(t, "after_tax1"));
            afterMap.put(ReportTaxKeys.TAX2,  getDouble(t, "after_tax2"));
            afterMap.put(ReportTaxKeys.TAX3,  getDouble(t, "after_tax3"));
            afterMap.put(ReportTaxKeys.TAX4,  getDouble(t, "after_tax4"));
            afterMap.put(ReportTaxKeys.TAX5,  getDouble(t, "after_tax5"));
            afterMap.put(ReportTaxKeys.TAX6,  getDouble(t, "after_tax6"));
            afterMap.put(ReportTaxKeys.TAX7,  getDouble(t, "after_tax7"));
            afterMap.put(ReportTaxKeys.TAX8,  getDouble(t, "after_tax8"));
            afterMap.put(ReportTaxKeys.TAX9,  getDouble(t, "after_tax9"));
            afterMap.put(ReportTaxKeys.TAX10, getDouble(t, "after_tax10"));
            afterMap.put(ReportTaxKeys.TAX11, getDouble(t, "after_tax11"));
            afterMap.put(ReportTaxKeys.TAX12, getDouble(t, "after_tax12"));
            afterMap.put(ReportTaxKeys.TAX13, getDouble(t, "after_tax13"));
            afterMap.put(ReportTaxKeys.TAX14, getDouble(t, "after_tax14"));
            afterMap.put(ReportTaxKeys.TAX15, getDouble(t, "after_tax15"));
            afterMap.put(ReportTaxKeys.TAX16, getDouble(t, "after_tax16"));
            afterMap.put(ReportTaxKeys.TAX17, getDouble(t, "after_tax17"));
            afterMap.put(ReportTaxKeys.TAX18, getDouble(t, "after_tax18"));
            afterMap.put(ReportTaxKeys.TAX19, getDouble(t, "after_tax19"));
            afterMap.put(ReportTaxKeys.TAX20, getDouble(t, "after_tax20"));
            afterMap.put(ReportTaxKeys.TAX21, getDouble(t, "after_tax21"));
            afterMap.put(ReportTaxKeys.TAX22, getDouble(t, "after_tax22"));
            afterMap.put(ReportTaxKeys.TAX23, getDouble(t, "after_tax23"));
            afterMap.put(ReportTaxKeys.TAX24, getDouble(t, "after_tax24"));
            afterMap.put(ReportTaxKeys.TAX25, getDouble(t, "after_tax25"));

            dto.setTaxKeyValueMapAfterAssess(beforeMap);
            dto.setTaxKeyValueMapAfterHearing(afterMap);

            dto.setPropertyUnitDetails(List.of(unit));
            // Capture before/after final RV from SQL aliases into a lightweight summary snapshot
            AfterHearingPropertySummary_Dto snap = new AfterHearingPropertySummary_Dto();
            snap.setBeforeFinalRvFl(getDouble(t, "before_finalrv"));
            snap.setAfterFinalRvFl(getDouble(t, "after_finalrv"));
            dto.setPropertySummary(snap);
            result.add(dto);
        }
        return result;
    }

    private List<AfterHearingCompleteProperty_Dto> aggregateProperties(List<AfterHearingCompleteProperty_Dto> list) {
        Map<String, List<AfterHearingCompleteProperty_Dto>> grouped =
                list.stream()
                        .filter(d -> d.getPropertyDetails() != null)
                        .collect(Collectors.groupingBy(d -> d.getPropertyDetails().getPdNewpropertynoVc()));

        List<AfterHearingCompleteProperty_Dto> finalList = new ArrayList<>();

        for (var entry : grouped.entrySet()) {
            AfterHearingCompleteProperty_Dto main = entry.getValue().get(0);
            List<PropertyUnitDetailsDto> allUnits = entry.getValue().stream()
                    .flatMap(d -> Optional.ofNullable(d.getPropertyUnitDetails()).orElse(List.of()).stream())
                    .collect(Collectors.toList());

            AfterHearingPropertySummary_Dto summary = new AfterHearingPropertySummary_Dto();
            summary.setNewPropertyNoVc(main.getPropertyDetails().getPdNewpropertynoVc());
            summary.setFinalPropertyNoVc(main.getPropertyDetails().getPdFinalpropnoVc());
            summary.setOwnerNameVc(main.getPropertyDetails().getPdOwnernameVc());
            summary.setWardNoVc(main.getPropertyDetails().getPdWardI());
            summary.setZoneNoVc(main.getPropertyDetails().getPdZoneI());

            summary.setTotalCarpetAreaFl(sum(allUnits, u -> parseDoubleSafe(u.getCarpetAreaFl())));
            summary.setTotalAssessmentAreaFl(sum(allUnits, u -> parseDoubleSafe(u.getTaxableAreaFl())));
            summary.setTotalYearlyRentFl(sum(allUnits, PropertyUnitDetailsDto::getYearlyRentFl));
            summary.setTotalAlvFl(sum(allUnits, PropertyUnitDetailsDto::getAlvFl));
            summary.setTotalCombinedAlvRentFl(summary.getTotalYearlyRentFl() + summary.getTotalAlvFl());
            summary.setTotalDepreciationAmountFl(sum(allUnits, PropertyUnitDetailsDto::getMaintenanceValueFl));
            summary.setTotalRatableValueFl(sum(allUnits, PropertyUnitDetailsDto::getRatableValueFl));
            summary.setConsideredRvFl(Math.max(summary.getTotalRatableValueFl(), summary.getTotalCombinedAlvRentFl()));

            // Merge tax maps (before and after) across all grouped rows so caller doesn't need to sum
            Map<Long, Double> mergedBefore = new HashMap<>();
            Map<Long, Double> mergedAfter = new HashMap<>();
            for (AfterHearingCompleteProperty_Dto d : entry.getValue()) {
                Map<Long, Double> b = Optional.ofNullable(d.getTaxKeyValueMapAfterAssess()).orElse(Map.of());
                for (Map.Entry<Long, Double> e : b.entrySet()) {
                    mergedBefore.merge(e.getKey(), Optional.ofNullable(e.getValue()).orElse(0.0), Double::sum);
                }
                Map<Long, Double> a = Optional.ofNullable(d.getTaxKeyValueMapAfterHearing()).orElse(Map.of());
                for (Map.Entry<Long, Double> e : a.entrySet()) {
                    mergedAfter.merge(e.getKey(), Optional.ofNullable(e.getValue()).orElse(0.0), Double::sum);
                }
            }
            // Set totals in summary if helpful to consumers
            summary.setTotalBeforeTaxFl(mergedBefore.values().stream().mapToDouble(Double::doubleValue).sum());
            summary.setTotalAfterTaxFl(mergedAfter.values().stream().mapToDouble(Double::doubleValue).sum());

            // Carry forward before/after final RV values captured per-row
            if (main.getPropertySummary() != null) {
                var snap = main.getPropertySummary();
                summary.setBeforeFinalRvFl(snap.getBeforeFinalRvFl());
                summary.setAfterFinalRvFl(snap.getAfterFinalRvFl());
            }

            main.setPropertyUnitDetails(allUnits);
            main.setPropertySummary(summary);
            main.setTaxKeyValueMapAfterAssess(mergedBefore);
            main.setTaxKeyValueMapAfterHearing(mergedAfter);
            finalList.add(main);
        }
        return finalList;
    }

    private double sum(List<PropertyUnitDetailsDto> list, Function<PropertyUnitDetailsDto, Double> getter) {
        return list.stream()
                .mapToDouble(u -> Optional.ofNullable(getter.apply(u)).orElse(0.0))
                .sum();
    }

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

    private Double parseDoubleSafe(String value) {
        if (value == null || value.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
