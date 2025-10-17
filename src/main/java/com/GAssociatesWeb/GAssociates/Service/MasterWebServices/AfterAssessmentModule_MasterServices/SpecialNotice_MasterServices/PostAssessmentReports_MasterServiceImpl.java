package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.SpecialNotice_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ConsolidatedTaxDetailsDto;
//import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.SpecialNotice_MasterRepository.AssessmentResultsRepository;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ProposedRatableValueDetailsDto;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class PostAssessmentReports_MasterServiceImpl implements PostAssessmentReports_MasterService {



    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String BASE_QUERY =
            "SELECT DISTINCT " +
                    "p.pd_noticeno_vc AS pdNoticenoVc, " +
                    "p.pd_ward_i AS pdWardI, " +
                    "p.pd_zone_i AS pdZoneI, " +
                    "p.pd_newpropertyno_vc AS pdNewpropertynoVc, " +
                    "p.pd_finalpropno_vc AS pdFinalpropnoVc, " +
                    "p.pd_ownername_vc AS pdOwnernameVc, " +
                    "p.pd_occupiname_f AS pdOccupinameF, " +
                    "p.pd_surypropno_vc AS pdSurypropnoVc, " +
                    "p.pd_propertytype_i AS pdPropertytypeI, " +
                    "p.pd_propertysubtype_i AS pdPropertysubtypeI, " +
                    "p.pd_usagetype_i AS pdUsagetypeI, " +
                    "p.pd_usagesubtype_i AS pdUsagesubtypeI, " +
                    "p.pd_propimage_t AS pdPropimageT, " +
                    "p.pd_houseplan2_t AS pdHouseplan2T, " +
                    "old.pod_oldpropno_vc AS pdOldpropnoVc, " +
                    "(SELECT a.current_assessment_date FROM assessmentdate_master a LIMIT 1) AS currentAssessmentDateDt, " +
                    "(SELECT a.last_assessment_date FROM assessmentdate_master a LIMIT 1) AS previousAssessmentDateDt, " +
                    "p.pd_assesarea_f AS pdAssesareaF, " +

                    // === Taxes ===
                    "pt.pt_propertytax_fl AS ptPropertyTaxFl, " +
                    "pt.pt_egctax_fl AS ptEgcTaxFl, " +
                    "pt.pt_treetax_fl AS ptTreeTaxFl, " +
                    "pt.pt_cleantax_fl AS ptCleanTaxFl, " +
                    "pt.pt_lighttax_fl AS ptLightTaxFl, " +
                    "pt.pt_firetax_fl AS ptFireTaxFl, " +
                    "pt.pt_usercharges_fl AS ptUserChargesFl, " +
                    "pt.pt_environmenttax_fl AS ptEnvironmentTaxFl, " +

                    "pt.pt_edurestax_fl AS ptEduResTaxFl, " +
                    "pt.pt_edunrestax_fl AS ptEduNonResTaxFl, " +
                    "pt.pt_edutax_fl AS ptEduTaxFl, " +

                    "pt.pt_watertax_fl AS ptWaterTaxFl, " +
                    "pt.pt_seweragetax_fl AS ptSewerageTaxFl, " +
                    "pt.pt_seweragebenefittax_fl AS ptSewerageBenefitTaxFl, " +
                    "pt.pt_waterbenefittax_fl AS ptWaterBenefitTaxFl, " +
                    "pt.pt_streettax_fl AS ptStreetTaxFl, " +
                    "pt.pt_specialconservancytax_fl AS ptSpecialConservancyTaxFl, " +
                    "pt.pt_municipaledutax_fl AS ptMunicipalEduTaxFl, " +
                    "pt.pt_specialedutax_fl AS ptSpecialEduTaxFl, " +
                    "pt.pt_servicecharges_fl AS ptServiceChargesFl, " +
                    "pt.pt_miscellaneouscharges_fl AS ptMiscellaneousChargesFl, " +

                    // === Flexible Reserved Taxes ===
                    "pt.pt_tax1_fl AS ptTax1Fl, " +
                    "pt.pt_tax2_fl AS ptTax2Fl, " +
                    "pt.pt_tax3_fl AS ptTax3Fl, " +
                    "pt.pt_tax4_fl AS ptTax4Fl, " +
                    "pt.pt_tax5_fl AS ptTax5Fl, " +
                    "pt.pt_tax6_fl AS ptTax6Fl, " +
                    "pt.pt_tax7_fl AS ptTax7Fl, " +
                    "pt.pt_tax8_fl AS ptTax8Fl, " +
                    "pt.pt_tax9_fl AS ptTax9Fl, " +
                    "pt.pt_tax10_fl AS ptTax10Fl, " +
                    "pt.pt_tax11_fl AS ptTax11Fl, " +
                    "pt.pt_tax12_fl AS ptTax12Fl, " +
                    "pt.pt_tax13_fl AS ptTax13Fl, " +
                    "pt.pt_tax14_fl AS ptTax14Fl, " +
                    "pt.pt_tax15_fl AS ptTax15Fl, " +
                    "pt.pt_tax16_fl AS ptTax16Fl, " +
                    "pt.pt_tax17_fl AS ptTax17Fl, " +
                    "pt.pt_tax18_fl AS ptTax18Fl, " +
                    "pt.pt_tax19_fl AS ptTax19Fl, " +
                    "pt.pt_tax20_fl AS ptTax20Fl, " +
                    "pt.pt_tax21_fl AS ptTax21Fl, " +
                    "pt.pt_tax22_fl AS ptTax22Fl, " +
                    "pt.pt_tax23_fl AS ptTax23Fl, " +
                    "pt.pt_tax24_fl AS ptTax24Fl, " +
                    "pt.pt_tax25_fl AS ptTax25Fl, " +

                    "ROUND(pt.pt_final_tax_fl) AS finalTax, " +
                    "pr.pr_residential_fl AS residential, " +
                    "pr.pr_commercial_fl AS commercial, " +
                    "pr.pr_industrial_fl AS pdBuildingvalueI, " +
                    "pr.pr_totalrv_fl AS aggregate, " +


                    "FROM property_details p " +
                    "LEFT JOIN property_taxdetails pt ON p.pd_newpropertyno_vc = pt.pt_newpropertyno_vc " +
                    "LEFT JOIN proposed_rvalues pr ON p.pd_newpropertyno_vc = pr.pr_newpropertyno_vc " +
                    "LEFT JOIN register_objection rg ON p.pd_finalpropno_vc = rg.rg_finalpropno_vc " +
                    "LEFT JOIN property_olddetails old ON CAST(NULLIF(p.prop_refno, '') AS INTEGER) = old.pod_refno_vc ";

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


    private List<AssessmentResultsDto> fetchSpecialNotices(String condition, Object[] params) {
        String query = BASE_QUERY + " WHERE " + condition + " ORDER BY p.pd_finalpropno_vc";
        return jdbcTemplate.query(query, params, new SpecialNoticeRowMapper());
    }
    @Override
    public List<AssessmentResultsDto> getSpecialNoticesByWard(int wardNo) {
        return fetchSpecialNotices("CAST(p.pd_ward_i AS INTEGER) = ?", new Object[]{wardNo});
    }

    @Override
    public List<AssessmentResultsDto> getSpecialNoticesByNewPropertyNo(String newPropertyNo) {
        return fetchSpecialNotices("p.pd_newpropertyno_vc = ?", new Object[]{newPropertyNo});
    }

    @Override
    public List<AssessmentResultsDto> getObjectionReceiptByNewPropertyNo(){
        return null;
    }

    private static class SpecialNoticeRowMapper implements RowMapper<AssessmentResultsDto> {
        @Override
        public AssessmentResultsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssessmentResultsDto dto = new AssessmentResultsDto();

            dto.setPdNoticenoVc(rs.getString("pdNoticenoVc"));
            dto.setPdWardI(rs.getString("pdWardI"));
            dto.setPdZoneI(rs.getString("pdZoneI"));
            dto.setPdNewpropertynoVc(rs.getString("pdNewpropertynoVc"));
            dto.setPdFinalpropnoVc(rs.getString("pdFinalpropnoVc"));
            dto.setPdOwnernameVc(rs.getString("pdOwnernameVc"));
            dto.setPdOccupinameF(rs.getString("pdOccupinameF"));
            dto.setPdSurypropnoVc(rs.getString("pdSurypropnoVc"));
            dto.setPdUsagetypeI(rs.getString("pdUsagetypeI"));
            dto.setPdUsagesubtypeI(rs.getString("pdUsagesubtypeI"));
            dto.setPdPropertytypeI(rs.getString("pdPropertytypeI"));
            dto.setPdPropertysubtypeI(rs.getString("pdPropertysubtypeI"));
            dto.setPdPropimageT(rs.getString("pdPropimageT"));
            dto.setPdHouseplan2T(rs.getString("pdHouseplan2T"));
            dto.setPdOldpropnoVc(rs.getString("pdOldpropnoVc"));
            dto.setCurrentAssessmentDateDt(rs.getString("currentAssessmentDateDt"));
            dto.setPreviousAssessmentDateDt(rs.getString("previousAssessmentDateDt"));
            dto.setPdAssesareaF(rs.getString("pdAssesareaF"));
            //dto.setAnnualRentalValueFl(rs.getString("annualRentalValueFl"));
           // dto.setAnnualUnRentalValueFl(rs.getString("annualUnRentalValueFl"));
            dto.setPdBuildingvalueI(rs.getString("pdBuildingvalueI"));
           // dto.setPdPlotvalueF(rs.getString("pdPlotvalueF"));

            ConsolidatedTaxDetailsDto taxDto = new ConsolidatedTaxDetailsDto();
            taxDto.setTotalTaxFl(rs.getDouble("finalTax"));
            dto.setConsolidatedTaxes(taxDto);

            // Tax key-value map (for reports)
            Map<Long, Double> taxMap = dto.getTaxKeyValueMap();
            for (Map.Entry<String, Long> entry : TAX_COLUMN_MAP.entrySet()) {
                double value = rs.getDouble(entry.getKey());
                if (!rs.wasNull()) {
                    taxMap.put(entry.getValue(), value);
                }
            }
            ProposedRatableValueDetailsDto rvDto = new ProposedRatableValueDetailsDto();
            rvDto.setResidentialFl(rs.getDouble("Residential"));
            rvDto.setCommercialFl(rs.getDouble("Commercial"));
            rvDto.setAggregateFl(rs.getDouble("aggregate"));
            dto.setProposedRatableValues(rvDto);

            return dto;
        }
    }

    private static class ObjectionReceiptRowMapper implements RowMapper<AssessmentResultsDto> {
        @Override
        public AssessmentResultsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssessmentResultsDto dto = new AssessmentResultsDto();

            // Core property/notice details
            dto.setPdFinalpropnoVc(rs.getString("rg_finalpropno_vc"));
            dto.setPdOwnernameVc(rs.getString("rg_ownername_vc"));
            dto.setPdOldpropnoVc(rs.getString("rg_oldpropno_vc"));
            dto.setPdWardI(rs.getString("rg_wardno_i") != null ? rs.getString("rg_wardno_i") : null);
            dto.setPdZoneI(rs.getString("rg_zoneno_i") != null ? rs.getString("rg_zoneno_i") : null);
            dto.setPdNoticenoVc(rs.getString("notice_no"));

            // Objection specific fields
            dto.setSnNoticeId(rs.getString("rg_applicationno_vc"));   // Application no
            dto.setPreviousAssessmentDateDt(rs.getString("rg_applicationreceiveddate_dt"));
            dto.setCurrentAssessmentDateDt(rs.getString("rg_objectiondate_dt"));

            // Additional objection data
            dto.setWarnings(List.of(
                    "Reasons: " + rs.getString("rg_reasons_vc"),
                    "Others: " + rs.getString("rg_others_vc"),
                    "Respondent: " + rs.getString("rg_respondent_vc"),
                    "Phone: " + rs.getString("rg_phoneno_vc")
            ));

            return dto;
        }
    }

    @Override
    public long getSpecialNoticeCount(Integer wardNo, String newPropertyNo) {
        String condition;
        Object[] params;

        if (wardNo != null && newPropertyNo != null) {
            condition = "CAST(p.pd_ward_i AS INTEGER) = ? AND p.pd_newpropertyno_vc = ?";
            params = new Object[]{wardNo, newPropertyNo};
        } else if (wardNo != null) {
            condition = "CAST(p.pd_ward_i AS INTEGER) = ?";
            params = new Object[]{wardNo};
        } else if (newPropertyNo != null) {
            condition = "p.pd_newpropertyno_vc = ?";
            params = new Object[]{newPropertyNo};
        } else {
            throw new IllegalArgumentException("At least wardNo or newPropertyNo must be provided");
        }

        String query = "SELECT COUNT(DISTINCT p.pd_finalpropno_vc) FROM property_details p " +
                "LEFT JOIN property_taxdetails pt ON p.pd_newpropertyno_vc = pt.pt_newpropertyno_vc " +
                "LEFT JOIN proposed_rvalues pr ON p.pd_newpropertyno_vc = pr.pr_newpropertyno_vc " +
                "LEFT JOIN property_olddetails old ON CAST(NULLIF(p.prop_refno, '') AS INTEGER) = old.pod_refno_vc " +
                "WHERE " + condition;

        return jdbcTemplate.queryForObject(query, params, Long.class);
    }



}
