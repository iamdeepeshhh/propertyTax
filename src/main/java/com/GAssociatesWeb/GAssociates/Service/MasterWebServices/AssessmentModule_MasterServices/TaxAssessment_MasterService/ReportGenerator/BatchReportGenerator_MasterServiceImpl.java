package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.ReportGenerator;

import com.GAssociatesWeb.GAssociates.Controller.MasterWebController;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ConsolidatedTaxDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.PropertyUnitDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ProposedRatableValueDetailsDto;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository.Property_RValuesRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository.Property_TaxDetailsRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository.Proposed_RValuesRepository;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.PropertyDetails_Repository;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.PropertyOldDetails_Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;

@Service
public class BatchReportGenerator_MasterServiceImpl implements BatchReportGenerator_MasterService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<AssessmentResultsDto> generatePropertyReport(Integer wardNo) {
        try {
            String queryStr = "SELECT DISTINCT " +

                    "p.pd_ward_i AS ward, " +
                    "p.pd_zone_i AS zone, " +
                    "p.pd_newpropertyno_vc AS newpropertyno, " +
                    "p.pd_finalpropno_vc AS finalpropertyno, " +
                    "p.pd_ownername_vc AS ownername, " +
                    "p.pd_occupiname_f AS occupiername, " +
                    "p.pd_surypropno_vc AS surveypropno, " +  // Included survey property number
                    "u.ud_floorno_vc AS floorno, " +
                    "r.prv_yearlyrent_fl AS rentalno, " +
                    "u.ud_usagetype_i AS usagetype, " +
                    "u.ud_constructionclass_i AS constclass, " +
                    "r.prv_agefactor_vc AS agefactor, " +
                    "u.ud_carpetarea_f AS carpetarea, " +
                    "u.ud_assessmentarea_f AS totalassesablearea, " +
                    "r.prv_rate_f AS ratepersqm, " +
                    "r.prv_lentingvalue_f AS total_assessment_value, " +
                    "r.prv_depper_i AS deppercent, " +
                    "r.prv_depamount_f AS depamount, " +
                    "r.prv_alv_f AS amountafterdep, " +

                    "GREATEST(COALESCE(r.prv_yearlyrent_fl, 0), COALESCE(r.prv_alv_f, 0)) AS taxablevaluetobeconsidered, " +
                    "ROUND(GREATEST(COALESCE(r.prv_mainval_f, 0), COALESCE(r.prv_temainval_fl, 0))) AS ten_percent_of_considered, " +

                    "r.prv_ratablevalue_f AS ratablevaluetobeconsidered, " +
                    "pr.pr_totalrv_fl AS totalratablevalue, " +

                    "COALESCE(pr.pr_residential_fl, 0) AS residential, " +

                    "COALESCE(pr.pr_commercial_fl, 0) + " +
                    "COALESCE(pr.pr_industrial_fl, 0) + " +
                    "COALESCE(pr.pr_government_fl, 0) + " +
                    "COALESCE(pr.pr_educational_fl, 0) + " +
                    "COALESCE(pr.pr_religious_fl, 0) + " +
                    "COALESCE(pr.pr_mobiletower_fl, 0) + " +
                    "COALESCE(pr.pr_electricsubstation_fl, 0) + " +
                    "COALESCE(pr.pr_commercialopenplot_fl, 0) + " +
                    "COALESCE(pr.pr_industrialopenplot_fl, 0) + " +
                    "COALESCE(pr.pr_governmentopenplot_fl, 0) + " +
                    "COALESCE(pr.pr_educationlegalopenplot_fl, 0) + " +
                    "COALESCE(pr.pr_religiousopenplot_fl, 0) + " +
                    "COALESCE(pr.pr_residentialopenplot_fl, 0) AS non_residential, " +

                    "ROUND(pt.pt_final_tax_fl) AS totaltax, " +
                    "old.pod_totalratablevalue_i AS oldrv, " +
                    "old.pod_totaltax_fl AS oldtax, " +
                    "old.pod_oldpropno_vc AS oldpropertyno, " +
                    "old.pod_zone_i AS oldpropertyzone, " +
                    "u.ud_unitno_vc AS unitNo " +

                    "FROM property_details p " +
                    "JOIN unit_details u ON p.pd_newpropertyno_vc = u.pd_newpropertyno_vc " +
                    "LEFT JOIN property_rvalues r ON p.pd_newpropertyno_vc = r.prv_propertyno_vc " +
                    "AND CAST(r.prv_unitno_vc AS INTEGER) = u.ud_unitno_vc " +
                    "LEFT JOIN property_taxdetails pt ON p.pd_newpropertyno_vc = pt.pt_newpropertyno_vc " +
                    "LEFT JOIN proposed_rvalues pr ON p.pd_newpropertyno_vc = pr.pr_newpropertyno_vc " +
                    "LEFT JOIN property_olddetails old ON CAST(NULLIF(p.prop_refno, '') AS INTEGER) = old.pod_refno_vc " +
                    "WHERE CAST(p.pd_ward_i AS INTEGER) = :wardNo " +
                    "ORDER BY p.pd_finalpropno_vc";

            Query query = entityManager.createNativeQuery(queryStr);
            query.setParameter("wardNo", wardNo);

            List<Object[]> results = query.getResultList();
            List<AssessmentResultsDto> assessmentResults = new ArrayList<>();

//            for (Object[] row : results) {
//                for (Object field : row) {
//                    System.out.print(field + "\t"); // Print each field in the row with a tab separator
//                }
//                System.out.println(); // Newline after each row
//            }

            for (Object[] row : results) {
                AssessmentResultsDto dto = new AssessmentResultsDto();

                // Mapping data to DTO
                dto.setPdWardI(row[0] != null ? row[0].toString() : "N/A"); // Ward
                dto.setPdZoneI(row[1] != null ? row[1].toString() : "N/A"); // Zone
                dto.setPdNewpropertynoVc(row[2] != null ? row[2].toString() : "N/A"); // New Property Number
                dto.setPdFinalpropnoVc(row[3] != null ? row[3].toString() : "N/A"); // Final Property Number
                dto.setPdOwnernameVc(row[4] != null ? row[4].toString() : "N/A"); // Owner Name
                dto.setPdOccupinameF(row[5] != null ? row[5].toString() : "N/A"); // Occupier Name
                dto.setPdSurypropnoVc(row[6] != null ? row[6].toString() : "N/A"); // Survey Property Number

                // Property Unit Details
                PropertyUnitDetailsDto unitDto = new PropertyUnitDetailsDto();
                unitDto.setFloorNoVc(row[7] != null ? row[7].toString() : "N/A"); // Floor Number
                unitDto.setActualAnnualRentFl(row[8] != null ? (Double) row[8] : 0.0); // Actual Annual Rent
                unitDto.setUsageTypeVc(row[9] != null ? row[9].toString() : "N/A"); // Usage Type
                unitDto.setConstructionTypeVc(row[10] != null ? row[10].toString() : "N/A"); // Construction Class
                unitDto.setAgeFactorVc(row[11] != null ? row[11].toString() : "N/A"); // Age Factor
                unitDto.setCarpetAreaFl(row[12] != null ? row[12].toString() : null); // Carpet Area
                unitDto.setTaxableAreaFl(row[13] != null ? row[13].toString() : null); // Total Assessable Area
                unitDto.setRatePerSqMFl(row[14] != null ? convertToDouble(row[14]) : null); // Rate per SqM
                unitDto.setRentalValAsPerRateFl(row[15] != null ? convertToDouble(row[15]) : null); // Annual Rent
                unitDto.setDepreciationRateFl(row[16] != null ? convertToDouble(row[16]) : null); // Depreciation Percentage
                unitDto.setDepreciationAmountFl(row[17] != null ? convertToDouble(row[17]) : null); // Depreciation Amount
                unitDto.setAmountAfterDepreciationFl(row[18] != null ? convertToDouble(row[18]) : null); // Amount after Depreciation
                unitDto.setTaxableValueByRateFl(row[19] != null ? convertToDouble(row[19]) : null); // Taxable Value by Rate
                unitDto.setMaintenanceRepairsFl(row[20] != null ? convertToDouble(row[20]) : null);
                unitDto.setUnitNoVc(row[30] != null ? row[30].toString(): null);
                unitDto.setTaxableValueConsideredFl(row[21] != null ? convertToDouble(row[21]) : null); // Considered Taxable Value
                unitDto.setNewPropertyNo(dto.getPdNewpropertynoVc());
                dto.setUnitDetails(List.of(unitDto));

                // Consolidated Tax Details

                //consolidatedTaxDto.setTotalTaxFl(row[22] != null ? convertToDouble(row[22]) : null); // Total Tax



                // Proposed Ratable Value
                ProposedRatableValueDetailsDto proposedRatableValueDto = new ProposedRatableValueDetailsDto();
                proposedRatableValueDto.setAggregateFl(row[22] != null ? convertToDouble(row[22]) : 0);
                proposedRatableValueDto.setResidentialFl(row[23] != null ? convertToDouble(row[23]) : 0);
                proposedRatableValueDto.setNonResidentialFl(row[24] != null ? convertToDouble(row[24]) : 0);

                dto.setProposedRatableValues(proposedRatableValueDto);

                ConsolidatedTaxDetailsDto consolidatedTaxDto = new ConsolidatedTaxDetailsDto();
                consolidatedTaxDto.setTotalTaxFl(row[25] != null ? convertToDouble(row[25]) : null); // Total Tax
                dto.setConsolidatedTaxes(consolidatedTaxDto);

                dto.setPdOldrvFl(row[26] != null ? (String) row[26] : null); // Old RV
                dto.setPdOldTaxVc(row[27] != null ? (String) row[27] : null); // Old Tax
                dto.setPdOldpropnoVc(row[28] != null ? (String) row[28] : null);
                assessmentResults.add(dto);
            }

            return assessmentResults;
        } catch (Exception e) {
            System.out.println("Error occurred while generating the report: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generating the report", e);
        }
    }

    private Double convertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        }
        return null;
    }

}