package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.TaxBills;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.TaxBills_MasterDto.TaxBills_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ConsolidatedTaxDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ProposedRatableValueDetailsDto;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TaxBills_MasterServiceImpl implements TaxBills_MasterService{

    private final JdbcTemplate jdbcTemplate;
    private static final String BASE_QUERY =
            "SELECT DISTINCT " +

                    // ====================== PROPERTY DETAILS ======================
                    "COALESCE(ah.pd_noticeno_vc, p.pd_noticeno_vc) AS pdNoticenoVc, " +
                    "COALESCE(ah.pd_propertyaddress_vc, p.pd_propertyaddress_vc) AS pdPropertyaddressVc, " +
                    "COALESCE(ah.pd_ward_i, p.pd_ward_i) AS pdWardI, " +
                    "COALESCE(ah.pd_zone_i, p.pd_zone_i) AS pdZoneI, " +
                    "p.pd_newpropertyno_vc AS pdNewpropertynoVc, " +
                    "p.pd_finalpropno_vc AS pdFinalpropnoVc, " + // ✅ no COALESCE
                    "COALESCE(ah.pd_ownername_vc, p.pd_ownername_vc) AS pdOwnernameVc, " +
                    "COALESCE(ah.pd_occupiname_f, p.pd_occupiname_f) AS pdOccupinameF, " +
                    "COALESCE(ah.pd_surypropno_vc, p.pd_surypropno_vc) AS pdSurypropnoVc, " +
                    "COALESCE(ah.pd_propertytype_i, p.pd_propertytype_i) AS pdPropertytypeI, " +
                    "COALESCE(ah.pd_propertysubtype_i, p.pd_propertysubtype_i) AS pdPropertysubtypeI, " +
                    "COALESCE(ah.pd_usagetype_i, p.pd_usagetype_i) AS pdUsagetypeI, " +
                    "COALESCE(ah.pd_usagesubtype_i, p.pd_usagesubtype_i) AS pdUsagesubtypeI, " +
                    "COALESCE(ah.pd_assesarea_f, p.pd_assesarea_f) AS pdAssesareaF, " +
                    "COALESCE(ah.pd_propimage_t, p.pd_propimage_t) AS pdPropimageT, " +
                    "COALESCE(ah.pd_houseplan2_t, p.pd_houseplan2_t) AS pdHouseplan2T, " +
                    "p.pd_oldpropno_vc AS pdOldpropnoVc, " +
                    "(SELECT a.current_assessment_date FROM assessmentdate_master a LIMIT 1) AS currentAssessmentDateDt, " +
                    "(SELECT a.last_assessment_date FROM assessmentdate_master a LIMIT 1) AS previousAssessmentDateDt, " +

                    // ====================== PROPERTY TAXES ======================
                    "COALESCE(aht.pt_propertytax_fl, pt.pt_propertytax_fl) AS ptPropertyTaxFl, " +
                    "COALESCE(aht.pt_egctax_fl, pt.pt_egctax_fl) AS ptEgcTaxFl, " +
                    "COALESCE(aht.pt_treetax_fl, pt.pt_treetax_fl) AS ptTreeTaxFl, " +
                    "COALESCE(aht.pt_cleantax_fl, pt.pt_cleantax_fl) AS ptCleanTaxFl, " +
                    "COALESCE(aht.pt_lighttax_fl, pt.pt_lighttax_fl) AS ptLightTaxFl, " +
                    "COALESCE(aht.pt_firetax_fl, pt.pt_firetax_fl) AS ptFireTaxFl, " +
                    "COALESCE(aht.pt_usercharges_fl, pt.pt_usercharges_fl) AS ptUserChargesFl, " +
                    "COALESCE(aht.pt_environmenttax_fl, pt.pt_environmenttax_fl) AS ptEnvironmentTaxFl, " +
                    "COALESCE(aht.pt_edurestax_fl, pt.pt_edurestax_fl) AS ptEduResTaxFl, " +
                    "COALESCE(aht.pt_edunrestax_fl, pt.pt_edunrestax_fl) AS ptEduNonResTaxFl, " +
                    "COALESCE(aht.pt_edutax_fl, pt.pt_edutax_fl) AS ptEduTaxFl, " +
                    "COALESCE(aht.pt_watertax_fl, pt.pt_watertax_fl) AS ptWaterTaxFl, " +
                    "COALESCE(aht.pt_seweragetax_fl, pt.pt_seweragetax_fl) AS ptSewerageTaxFl, " +
                    "COALESCE(aht.pt_seweragebenefittax_fl, pt.pt_seweragebenefittax_fl) AS ptSewerageBenefitTaxFl, " +
                    "COALESCE(aht.pt_waterbenefittax_fl, pt.pt_waterbenefittax_fl) AS ptWaterBenefitTaxFl, " +
                    "COALESCE(aht.pt_streettax_fl, pt.pt_streettax_fl) AS ptStreetTaxFl, " +
                    "COALESCE(aht.pt_specialconservancytax_fl, pt.pt_specialconservancytax_fl) AS ptSpecialConservancyTaxFl, " +
                    "COALESCE(aht.pt_municipaledutax_fl, pt.pt_municipaledutax_fl) AS ptMunicipalEduTaxFl, " +
                    "COALESCE(aht.pt_specialedutax_fl, pt.pt_specialedutax_fl) AS ptSpecialEduTaxFl, " +
                    "COALESCE(aht.pt_servicecharges_fl, pt.pt_servicecharges_fl) AS ptServiceChargesFl, " +
                    "COALESCE(aht.pt_miscellaneouscharges_fl, pt.pt_miscellaneouscharges_fl) AS ptMiscellaneousChargesFl, " +

                    // === Flexible Reserved Taxes ===
                    "COALESCE(aht.pt_tax1_fl, pt.pt_tax1_fl) AS ptTax1Fl, " +
                    "COALESCE(aht.pt_tax2_fl, pt.pt_tax2_fl) AS ptTax2Fl, " +
                    "COALESCE(aht.pt_tax3_fl, pt.pt_tax3_fl) AS ptTax3Fl, " +
                    "COALESCE(aht.pt_tax4_fl, pt.pt_tax4_fl) AS ptTax4Fl, " +
                    "COALESCE(aht.pt_tax5_fl, pt.pt_tax5_fl) AS ptTax5Fl, " +
                    "COALESCE(aht.pt_tax6_fl, pt.pt_tax6_fl) AS ptTax6Fl, " +
                    "COALESCE(aht.pt_tax7_fl, pt.pt_tax7_fl) AS ptTax7Fl, " +
                    "COALESCE(aht.pt_tax8_fl, pt.pt_tax8_fl) AS ptTax8Fl, " +
                    "COALESCE(aht.pt_tax9_fl, pt.pt_tax9_fl) AS ptTax9Fl, " +
                    "COALESCE(aht.pt_tax10_fl, pt.pt_tax10_fl) AS ptTax10Fl, " +
                    "COALESCE(aht.pt_tax11_fl, pt.pt_tax11_fl) AS ptTax11Fl, " +
                    "COALESCE(aht.pt_tax12_fl, pt.pt_tax12_fl) AS ptTax12Fl, " +
                    "COALESCE(aht.pt_tax13_fl, pt.pt_tax13_fl) AS ptTax13Fl, " +
                    "COALESCE(aht.pt_tax14_fl, pt.pt_tax14_fl) AS ptTax14Fl, " +
                    "COALESCE(aht.pt_tax15_fl, pt.pt_tax15_fl) AS ptTax15Fl, " +
                    "COALESCE(aht.pt_tax16_fl, pt.pt_tax16_fl) AS ptTax16Fl, " +
                    "COALESCE(aht.pt_tax17_fl, pt.pt_tax17_fl) AS ptTax17Fl, " +
                    "COALESCE(aht.pt_tax18_fl, pt.pt_tax18_fl) AS ptTax18Fl, " +
                    "COALESCE(aht.pt_tax19_fl, pt.pt_tax19_fl) AS ptTax19Fl, " +
                    "COALESCE(aht.pt_tax20_fl, pt.pt_tax20_fl) AS ptTax20Fl, " +
                    "COALESCE(aht.pt_tax21_fl, pt.pt_tax21_fl) AS ptTax21Fl, " +
                    "COALESCE(aht.pt_tax22_fl, pt.pt_tax22_fl) AS ptTax22Fl, " +
                    "COALESCE(aht.pt_tax23_fl, pt.pt_tax23_fl) AS ptTax23Fl, " +
                    "COALESCE(aht.pt_tax24_fl, pt.pt_tax24_fl) AS ptTax24Fl, " +
                    "COALESCE(aht.pt_tax25_fl, pt.pt_tax25_fl) AS ptTax25Fl, " +

                    "ROUND(COALESCE(aht.pt_final_tax_fl, pt.pt_final_tax_fl)) AS finalTax, " +

                    // ====================== PROPOSED RATABLE VALUES ======================
                    "COALESCE(ahr.pr_residential_fl, pr.pr_residential_fl) AS residentialFl, " +
                    "COALESCE(ahr.pr_commercial_fl, pr.pr_commercial_fl) AS commercialFl, " +
                    "COALESCE(ahr.pr_industrial_fl, pr.pr_industrial_fl) AS industrialFl, " +
                    "COALESCE(ahr.pr_government_fl, pr.pr_government_fl) AS governmentFl, " +
                    "COALESCE(ahr.pr_educational_fl, pr.pr_educational_fl) AS educationalInstituteFl, " +
                    "COALESCE(ahr.pr_religious_fl, pr.pr_religious_fl) AS religiousFl, " +
                    "COALESCE(ahr.pr_mobiletower_fl, pr.pr_mobiletower_fl) AS mobileTowerFl, " +
                    "COALESCE(ahr.pr_electricsubstation_fl, pr.pr_electricsubstation_fl) AS electricSubstationFl, " +
                    "COALESCE(ahr.pr_residentialopenplot_fl, pr.pr_residentialopenplot_fl) AS residentialOpenPlotFl, " +
                    "COALESCE(ahr.pr_commercialopenplot_fl, pr.pr_commercialopenplot_fl) AS commercialOpenPlotFl, " +
                    "COALESCE(ahr.pr_industrialopenplot_fl, pr.pr_industrialopenplot_fl) AS industrialOpenPlotFl, " +
                    "COALESCE(ahr.pr_governmentopenplot_fl, pr.pr_governmentopenplot_fl) AS governmentOpenPlotFl, " +
                    "COALESCE(ahr.pr_educationlegalopenplot_fl, pr.pr_educationlegalopenplot_fl) AS educationAndLegalInstituteOpenPlotFl, " +
                    "COALESCE(ahr.pr_religiousopenplot_fl, pr.pr_religiousopenplot_fl) AS religiousOpenPlotFl, " +
                    "COALESCE(ahr.pr_totalrv_fl, pr.pr_totalrv_fl) AS aggregateFl " +

                    "FROM property_details p " +
                    "LEFT JOIN property_taxdetails pt ON p.pd_newpropertyno_vc = pt.pt_newpropertyno_vc " +
                    "LEFT JOIN proposed_rvalues pr ON p.pd_newpropertyno_vc = pr.pr_newpropertyno_vc " +
                    "LEFT JOIN afterhearing_property_details ah ON p.pd_newpropertyno_vc = ah.pd_newpropertyno_vc " +
                    "LEFT JOIN afterhearing_property_taxdetails aht ON p.pd_newpropertyno_vc = aht.pt_newpropertyno_vc " +
                    "LEFT JOIN afterhearing_proposed_rvalues ahr ON p.pd_newpropertyno_vc = ahr.pr_newpropertyno_vc ";

    private static final Map<String, Long> TAX_COLUMN_MAP = Map.ofEntries(
            Map.entry("ptPropertyTaxFl", ReportTaxKeys.PT_PARENT),
            Map.entry("ptEduResTaxFl", ReportTaxKeys.EDUC_RES),
            Map.entry("ptEduNonResTaxFl", ReportTaxKeys.EDUC_COMM),
            Map.entry("ptEduTaxFl", ReportTaxKeys.EDUC_PARENT),
            Map.entry("ptEgcTaxFl", ReportTaxKeys.EGC),
            Map.entry("ptTreeTaxFl", ReportTaxKeys.TREE_TAX),
            Map.entry("ptEnvironmentTaxFl", ReportTaxKeys.ENV_TAX),
            Map.entry("ptCleanTaxFl", ReportTaxKeys.CLEAN_TAX),
            Map.entry("ptLightTaxFl", ReportTaxKeys.LIGHT_TAX),
            Map.entry("ptFireTaxFl", ReportTaxKeys.FIRE_TAX),
            Map.entry("ptWaterTaxFl", ReportTaxKeys.WATER_TAX),
            Map.entry("ptSewerageTaxFl", ReportTaxKeys.SEWERAGE_TAX),
            Map.entry("ptSewerageBenefitTaxFl", ReportTaxKeys.SEWERAGE_BEN),
            Map.entry("ptWaterBenefitTaxFl", ReportTaxKeys.WATER_BEN),
            Map.entry("ptStreetTaxFl", ReportTaxKeys.STREET_TAX),
            Map.entry("ptSpecialConservancyTaxFl", ReportTaxKeys.SPEC_CONS),
            Map.entry("ptMunicipalEduTaxFl", ReportTaxKeys.MUNICIPAL_EDU),
            Map.entry("ptSpecialEduTaxFl", ReportTaxKeys.SPECIAL_EDU),
            Map.entry("ptServiceChargesFl", ReportTaxKeys.SERVICE_CHG),
            Map.entry("ptMiscellaneousChargesFl", ReportTaxKeys.MISC_CHG),
            Map.entry("ptUserChargesFl", ReportTaxKeys.USER_CHG),

            // Flexible reserved taxes
            Map.entry("ptTax1Fl", ReportTaxKeys.TAX1),
            Map.entry("ptTax2Fl", ReportTaxKeys.TAX2),
            Map.entry("ptTax3Fl", ReportTaxKeys.TAX3),
            Map.entry("ptTax4Fl", ReportTaxKeys.TAX4),
            Map.entry("ptTax5Fl", ReportTaxKeys.TAX5),
            Map.entry("ptTax6Fl", ReportTaxKeys.TAX6),
            Map.entry("ptTax7Fl", ReportTaxKeys.TAX7),
            Map.entry("ptTax8Fl", ReportTaxKeys.TAX8),
            Map.entry("ptTax9Fl", ReportTaxKeys.TAX9),
            Map.entry("ptTax10Fl", ReportTaxKeys.TAX10),
            Map.entry("ptTax11Fl", ReportTaxKeys.TAX11),
            Map.entry("ptTax12Fl", ReportTaxKeys.TAX12),
            Map.entry("ptTax13Fl", ReportTaxKeys.TAX13),
            Map.entry("ptTax14Fl", ReportTaxKeys.TAX14),
            Map.entry("ptTax15Fl", ReportTaxKeys.TAX15),
            Map.entry("ptTax16Fl", ReportTaxKeys.TAX16),
            Map.entry("ptTax17Fl", ReportTaxKeys.TAX17),
            Map.entry("ptTax18Fl", ReportTaxKeys.TAX18),
            Map.entry("ptTax19Fl", ReportTaxKeys.TAX19),
            Map.entry("ptTax20Fl", ReportTaxKeys.TAX20),
            Map.entry("ptTax21Fl", ReportTaxKeys.TAX21),
            Map.entry("ptTax22Fl", ReportTaxKeys.TAX22),
            Map.entry("ptTax23Fl", ReportTaxKeys.TAX23),
            Map.entry("ptTax24Fl", ReportTaxKeys.TAX24),
            Map.entry("ptTax25Fl", ReportTaxKeys.TAX25)
    );

    private List<TaxBills_MasterDto> fetchTaxBills(String condition, Object[] params) {
        String query = BASE_QUERY + " WHERE " + condition + " ORDER BY p.pd_finalpropno_vc";
        List<TaxBills_MasterDto> bills = jdbcTemplate.query(query, params, new TaxBillRowMapper());

        for (TaxBills_MasterDto dto : bills) {
            // 🔹 Fetch arrears safely
            Map<String, Map<Long, Double>> arrearsMap = fetchYearWiseArrears(dto.getPdNewpropertynoVc());
            if (arrearsMap == null) arrearsMap = new java.util.LinkedHashMap<>();
            dto.setArrearsYearWiseMap(arrearsMap);

            // 🔹 Compute arrears range
            if (!arrearsMap.isEmpty()) {
                String firstYear = arrearsMap.keySet().iterator().next();
                String lastYear = null;
                for (String y : arrearsMap.keySet()) lastYear = y;
                dto.setArrearsRangeVc(firstYear.equals(lastYear) ? firstYear : firstYear + " to " + lastYear);
            }

            // 🔹 Merge current + arrears = totalTaxMap
            Map<Long, Double> merged = new java.util.HashMap<>(dto.getCurrentTaxMap());
            for (Map<Long, Double> yearlyTaxes : arrearsMap.values()) {
                for (Map.Entry<Long, Double> e : yearlyTaxes.entrySet()) {
                    merged.merge(e.getKey(), e.getValue(), Double::sum);
                }
            }
            dto.setTotalTaxMap(merged);
        }

        return bills;
    }




    public List<TaxBills_MasterDto> getTaxBillsByWard(int wardNo) {
        return fetchTaxBills("CAST(p.pd_ward_i AS INTEGER) = ?", new Object[]{wardNo});
    }

    // 🔹 Get tax bills by property
    public List<TaxBills_MasterDto> getTaxBillsByNewPropertyNo(String newPropertyNo) {
        return fetchTaxBills("p.pd_newpropertyno_vc = ?", new Object[]{newPropertyNo});
    }

    private Map<String, Map<Long, Double>> fetchYearWiseArrears(String newPropertyNo) {
        String sql = """
        SELECT 
            arrears_financialyear_dt AS financial_year_vc,

            -- Core property and cess taxes
            SUM(COALESCE(pt_propertytax_fl, arrears_propertytax_dp)) AS ptPropertyTaxFl,
            SUM(COALESCE(pt_egctax_fl, arrears_egctax_dp)) AS ptEgcTaxFl,
            SUM(COALESCE(pt_treetax_fl, arrears_treetax_dp)) AS ptTreeTaxFl,
            SUM(COALESCE(pt_envtax_fl, arrears_envtax_dp)) AS ptEnvironmentTaxFl,
            SUM(COALESCE(pt_cleantax_fl, arrears_cleantax_dp)) AS ptCleanTaxFl,
            SUM(COALESCE(pt_lighttax_fl, arrears_lighttax_dp)) AS ptLightTaxFl,
            SUM(COALESCE(pt_firetax_fl, arrears_firetax_dp)) AS ptFireTaxFl,

            -- User charges, service, misc
            SUM(COALESCE(pt_usercharges_fl, arrears_usercharges_dp)) AS ptUserChargesFl,
            SUM(COALESCE(pt_servicecharges_fl, 0)) AS ptServiceChargesFl,
            SUM(COALESCE(pt_miscellaneouscharges_fl, arrears_miscell_dp)) AS ptMiscellaneousChargesFl,

            -- Education and EGC related
            SUM(COALESCE(pt_edutax_fl, arrears_edutax_dp)) AS ptEduTaxFl,
            SUM(COALESCE(pt_municipaledutax_fl, 0)) AS ptMunicipalEduTaxFl,
            SUM(COALESCE(pt_specialedutax_fl, 0)) AS ptSpecialEduTaxFl,

            -- Water and Sewerage
            SUM(COALESCE(pt_watertax_fl, arrears_water_dp)) AS ptWaterTaxFl,
            SUM(COALESCE(pt_waterbenefittax_fl, arrears_water_dp)) AS ptWaterBenefitTaxFl,
            SUM(COALESCE(pt_seweragetax_fl, 0)) AS ptSewerageTaxFl,
            SUM(COALESCE(pt_seweragebenefittax_fl, 0)) AS ptSewerageBenefitTaxFl,

            -- Street and conservancy
            SUM(COALESCE(pt_streettax_fl, 0)) AS ptStreetTaxFl,
            SUM(COALESCE(pt_specialconservancytax_fl, 0)) AS ptSpecialConservancyTaxFl,

            -- Reserved and flexible tax columns (pt_tax1_fl → pt_tax25_fl)
            SUM(COALESCE(pt_tax1_fl, 0)) AS ptTax1Fl,
            SUM(COALESCE(pt_tax2_fl, 0)) AS ptTax2Fl,
            SUM(COALESCE(pt_tax3_fl, 0)) AS ptTax3Fl,
            SUM(COALESCE(pt_tax4_fl, 0)) AS ptTax4Fl,
            SUM(COALESCE(pt_tax5_fl, 0)) AS ptTax5Fl,
            SUM(COALESCE(pt_tax6_fl, 0)) AS ptTax6Fl,
            SUM(COALESCE(pt_tax7_fl, 0)) AS ptTax7Fl,
            SUM(COALESCE(pt_tax8_fl, 0)) AS ptTax8Fl,
            SUM(COALESCE(pt_tax9_fl, 0)) AS ptTax9Fl,
            SUM(COALESCE(pt_tax10_fl, 0)) AS ptTax10Fl,
            SUM(COALESCE(pt_tax11_fl, 0)) AS ptTax11Fl,
            SUM(COALESCE(pt_tax12_fl, 0)) AS ptTax12Fl,
            SUM(COALESCE(pt_tax13_fl, 0)) AS ptTax13Fl,
            SUM(COALESCE(pt_tax14_fl, 0)) AS ptTax14Fl,
            SUM(COALESCE(pt_tax15_fl, 0)) AS ptTax15Fl,
            SUM(COALESCE(pt_tax16_fl, 0)) AS ptTax16Fl,
            SUM(COALESCE(pt_tax17_fl, 0)) AS ptTax17Fl,
            SUM(COALESCE(pt_tax18_fl, 0)) AS ptTax18Fl,
            SUM(COALESCE(pt_tax19_fl, 0)) AS ptTax19Fl,
            SUM(COALESCE(pt_tax20_fl, 0)) AS ptTax20Fl,
            SUM(COALESCE(pt_tax21_fl, 0)) AS ptTax21Fl,
            SUM(COALESCE(pt_tax22_fl, 0)) AS ptTax22Fl,
            SUM(COALESCE(pt_tax23_fl, 0)) AS ptTax23Fl,
            SUM(COALESCE(pt_tax24_fl, 0)) AS ptTax24Fl,
            SUM(COALESCE(pt_tax25_fl, 0)) AS ptTax25Fl,

            -- Total final tax
            ROUND(SUM(COALESCE(pt_final_tax_fl, arrears_totaltax_dp))) AS ptFinalTaxFl
        FROM arrears_property_taxdetails
        WHERE arrears_newpropertyno_vc = ?
        GROUP BY arrears_financialyear_dt
        ORDER BY arrears_financialyear_dt
    """;

        return jdbcTemplate.query(sql, new Object[]{newPropertyNo}, rs -> {
            Map<String, Map<Long, Double>> yearWise = new LinkedHashMap<>();

            while (rs.next()) {
                String year = rs.getString("financial_year_vc");
                Map<Long, Double> taxMap = new HashMap<>();

                // Map columns dynamically to tax keys (every field included)
                Map<String, Long> columnKeyMap = new HashMap<>(Map.ofEntries(                        Map.entry("ptPropertyTaxFl", ReportTaxKeys.PT_PARENT),
                        Map.entry("ptEgcTaxFl", ReportTaxKeys.EGC),
                        Map.entry("ptTreeTaxFl", ReportTaxKeys.TREE_TAX),
                        Map.entry("ptEnvironmentTaxFl", ReportTaxKeys.ENV_TAX),
                        Map.entry("ptCleanTaxFl", ReportTaxKeys.CLEAN_TAX),
                        Map.entry("ptLightTaxFl", ReportTaxKeys.LIGHT_TAX),
                        Map.entry("ptFireTaxFl", ReportTaxKeys.FIRE_TAX),
                        Map.entry("ptUserChargesFl", ReportTaxKeys.USER_CHG),
                        Map.entry("ptServiceChargesFl", ReportTaxKeys.SERVICE_CHG),
                        Map.entry("ptMiscellaneousChargesFl", ReportTaxKeys.MISC_CHG),
                        Map.entry("ptEduTaxFl", ReportTaxKeys.EDUC_PARENT),
                        Map.entry("ptMunicipalEduTaxFl", ReportTaxKeys.MUNICIPAL_EDU),
                        Map.entry("ptSpecialEduTaxFl", ReportTaxKeys.SPECIAL_EDU),
                        Map.entry("ptWaterTaxFl", ReportTaxKeys.WATER_TAX),
                        Map.entry("ptWaterBenefitTaxFl", ReportTaxKeys.WATER_BEN),
                        Map.entry("ptSewerageTaxFl", ReportTaxKeys.SEWERAGE_TAX),
                        Map.entry("ptSewerageBenefitTaxFl", ReportTaxKeys.SEWERAGE_BEN),
                        Map.entry("ptStreetTaxFl", ReportTaxKeys.STREET_TAX),
                        Map.entry("ptSpecialConservancyTaxFl", ReportTaxKeys.SPEC_CONS),
                        Map.entry("ptFinalTaxFl", ReportTaxKeys.TOTAL_TAX)
                ));

                // Include reserved pt_tax1_fl … pt_tax25_fl
                for (Map.Entry<String, Long> entry : TAX_COLUMN_MAP.entrySet()) {
                    if (entry.getKey().startsWith("ptTax")) {
                        columnKeyMap.put(entry.getKey(), entry.getValue());
                    }
                }

                // Loop and populate
                for (Map.Entry<String, Long> entry : columnKeyMap.entrySet()) {
                    double value = rs.getDouble(entry.getKey());
                    if (!rs.wasNull()) taxMap.put(entry.getValue(), value);
                }

                yearWise.put(year, taxMap);
            }

            return yearWise;
        });
    }

    private static class TaxBillRowMapper implements RowMapper<TaxBills_MasterDto> {
        @Override
        public TaxBills_MasterDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            TaxBills_MasterDto dto = new TaxBills_MasterDto();

            // 🏠 Property info
            dto.setPdWardI(rs.getString("pdWardI"));
            dto.setPdZoneI(rs.getString("pdZoneI"));
            dto.setPdFinalpropnoVc(rs.getString("pdFinalpropnoVc"));
            dto.setPdNewpropertynoVc(rs.getString("pdNewpropertynoVc"));
            dto.setPdOwnernameVc(rs.getString("pdOwnernameVc"));
            dto.setPdOccupinameF(rs.getString("pdOccupinameF"));
            dto.setPdSurypropnoVc(rs.getString("pdSurypropnoVc"));
            dto.setPdOldpropnoVc(rs.getString("pdOldpropnoVc"));
            dto.setPdAssesareaF(Double.valueOf(rs.getString("pdAssesareaF")));
            dto.setPdPropertyaddressVc(rs.getString("pdPropertyaddressVc"));
//            dto.setFinalTaxFl(rs.getDouble("finalTax"));

            // 📊 Proposed Ratable Values
            ProposedRatableValueDetailsDto rvDto = new ProposedRatableValueDetailsDto();
            rvDto.setResidentialFl(rs.getDouble("residentialFl"));
            rvDto.setCommercialFl(rs.getDouble("commercialFl"));
            rvDto.setIndustrialFl(rs.getDouble("industrialFl"));
            rvDto.setGovernmentFl(rs.getDouble("governmentFl"));
            rvDto.setEducationalInstituteFl(rs.getDouble("educationalInstituteFl"));
            rvDto.setReligiousFl(rs.getDouble("religiousFl"));
            rvDto.setMobileTowerFl(rs.getDouble("mobileTowerFl"));
            rvDto.setElectricSubstationFl(rs.getDouble("electricSubstationFl"));
            rvDto.setResidentialOpenPlotFl(rs.getDouble("residentialOpenPlotFl"));
            rvDto.setCommercialOpenPlotFl(rs.getDouble("commercialOpenPlotFl"));
            rvDto.setIndustrialOpenPlotFl(rs.getDouble("industrialOpenPlotFl"));
            rvDto.setGovernmentOpenPlotFl(rs.getDouble("governmentOpenPlotFl"));
            rvDto.setEducationAndLegalInstituteOpenPlotFl(rs.getDouble("educationAndLegalInstituteOpenPlotFl"));
            rvDto.setReligiousOpenPlotFl(rs.getDouble("religiousOpenPlotFl"));
            rvDto.setAggregateFl(rs.getDouble("aggregateFl"));
            dto.setProposedRatableValueDetailsDto(rvDto); // ✅ use correct setter name

            // 💰 Map individual taxes
            Map<Long, Double> taxMap = new java.util.HashMap<>();
            for (Map.Entry<String, Long> entry : TAX_COLUMN_MAP.entrySet()) {
                double value = rs.getDouble(entry.getKey());
                if (!rs.wasNull()) {
                    taxMap.put(entry.getValue(), value);
                }
            }
            dto.setCurrentTaxMap(taxMap);

            return dto;
        }
    }



}
