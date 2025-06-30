package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.SpecialNotice_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ConsolidatedTaxDetailsDto;
//import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.SpecialNotice_MasterRepository.AssessmentResultsRepository;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ProposedRatableValueDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class SpecialNotice_MasterServiceImpl implements SpecialNotice_MasterService {



    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<AssessmentResultsDto> getSpecialNoticesByWard(int wardNo) {
        String query = "SELECT DISTINCT " +
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
                "u.ud_usagetype_i AS pdUsagetypeI, " +
                "u.ud_usagesubtype_i AS pdUsagesubtypeI, " +
                "p.pd_propimage_t AS pdPropimageT, " +
                "p.pd_houseplan2_t AS pdHouseplan2T, " +
                "old.pod_oldpropno_vc AS pdOldpropnoVc, " +
                "(SELECT a.current_assessment_date FROM assessmentdate_master a LIMIT 1) AS currentAssessmentDateDt, " +
                "(SELECT a.last_assessment_date FROM assessmentdate_master a LIMIT 1) AS previousAssessmentDateDt, " +
                "old.pod_totalassessmentarea_fl AS pdAssesareaF, " +
                "pt.pt_propertytax_fl AS propertyTax, " +
                "pt.pt_egctax_fl AS egcTax, " +
                "pt.pt_treetax_fl AS treeTax, " +
                "pt.pt_cleantax_fl AS cleanTax, " +
                "pt.pt_lighttax_fl AS lightTax, " +
                "pt.pt_edutax_fl AS educationTaxTotal, " +
                "pt.pt_final_tax_fl AS finalTax, " +
                "pr.pr_residential_fl AS residential, " +
                "pr.pr_commercial_fl AS commercial, " +
                "pr.pr_industrial_fl AS pdBuildingvalueI " +
                //"pr.pr_annual_rent_fl AS annualRentalValueFl, " +
                //"pr.pr_unrent_fl AS annualUnRentalValueFl " +


                "FROM property_details p " +
                "JOIN unit_details u ON p.pd_newpropertyno_vc = u.pd_newpropertyno_vc " +
                "LEFT JOIN property_taxdetails pt ON p.pd_newpropertyno_vc = pt.pt_newpropertyno_vc " +
                "LEFT JOIN proposed_rvalues pr ON p.pd_newpropertyno_vc = pr.pr_newpropertyno_vc " +
                "LEFT JOIN property_olddetails old ON CAST(NULLIF(p.prop_refno, '') AS INTEGER) = old.pod_refno_vc " +
                "WHERE CAST(p.pd_ward_i AS INTEGER) = ? " +
                "ORDER BY p.pd_finalpropno_vc";

        return jdbcTemplate.query(query, new Object[]{wardNo}, new AssessmentResultsRowMapper());
    }

    private static class AssessmentResultsRowMapper implements RowMapper<AssessmentResultsDto> {
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
            taxDto.setPropertyTaxFl(rs.getDouble("propertyTax"));
            taxDto.setEgcFl(rs.getDouble("egcTax"));
            taxDto.setTreeTaxFl(rs.getDouble("treeTax"));
            taxDto.setCleannessTaxFl(rs.getDouble("cleanTax"));
            taxDto.setLightTaxFl(rs.getDouble("lightTax"));
            taxDto.setEducationTaxTotalFl(rs.getDouble("educationTaxTotal"));
            taxDto.setFireTaxFl(rs.getDouble("finalTax"));
            dto.setConsolidatedTaxes(taxDto);

            ProposedRatableValueDetailsDto rvDto = new ProposedRatableValueDetailsDto();
            rvDto.setResidentialFl(rs.getDouble("Residential"));
            rvDto.setCommercialFl(rs.getDouble("Commercial"));
            dto.setProposedRatableValues(rvDto);

            return dto;
        }
    }
}
