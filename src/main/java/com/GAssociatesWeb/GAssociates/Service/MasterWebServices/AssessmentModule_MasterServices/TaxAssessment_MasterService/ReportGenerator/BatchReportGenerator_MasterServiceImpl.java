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
                    "(SELECT a.current_assessment_date FROM assessmentdate_master a LIMIT 1) AS assessmentDate, "+

                    "p.pd_propertyaddress_vc AS propertyAddress, " + // Property Address
                    "p.pd_propertyname_vc AS propertyName, " + // Property Name
                    "p.pd_layoutname_vc AS layoutName, " + // Layout Name
                    "p.pd_khasrano_vc AS khasraNo, " + // Khasra Number
                    "p.pd_plotno_vc AS plotNo, " + // Plot Number
                    "p.pd_gridid_vc AS gridId, " + // Grid ID
                    "p.pd_parcelid_vc AS parcelId, " + // Parcel ID
                    "p.pd_roadid_vc AS roadId, " + // Road ID
                    "p.pd_contactno_vc AS contactNo, " + // Contact Number
                    "p.pd_meterno_vc AS meterNo, " + // Meter Number
                    "p.pd_consumerno_vc AS consumerNo, " + // Consumer Number
                    "p.pd_connectiondate_dt AS connectionDate, " + // Connection Date
                    "p.pd_pipesize_f AS pipeSize, " + // Pipe Size
                    "p.pd_permissionstatus_vc AS permissionStatus, " + // Permission Status
                    "p.pd_permissionno_vc AS permissionNo, " + // Permission Number
                    "p.pd_permissiondate_dt AS permissionDate, " + // Permission Date
                    "p.pd_rainwaterhaverst_vc AS rainwaterHarvest, " + // Rainwater Harvesting
                    "p.pd_solarunit_vc AS solarUnit, " + // Solar Unit
                    "p.pd_stair_vc AS stair, " + // Stair
                    "p.pd_lift_vc AS lift, " + // Lift
                    "p.pd_noofrooms_i AS noOfRooms, " + // Number of Rooms
                    "p.pd_nooffloors_i AS noOfFloors,  " + // Number of Floors
                    "p.pd_indexno_vc AS indexNo, " +

                    "p.pd_toilets_i AS toiletNo, " +
                    "p.pd_toiletstatus_vc AS toiletStatus, " +
                    "p.pd_waterconnstatus_vc AS waterConnection, " +
                    "p.pd_waterconntype_vc AS waterConnectionType, " +
                    "old.pod_ward_i AS oldWard, " +
                    "old.pod_propertytype_i AS oldPropertyType, " +
                    "old.pod_propertysubtype_i AS oldPropertySubType, " +
                    "old.pod_usagetype_i AS oldPropertyUsageType, " +
                    "old.pod_constclass_vc AS constClass, " +
                    "old.pod_totalassessmentarea_fl AS totalAssessableArea, " +
                    "(SELECT a.last_assessment_date FROM assessmentdate_master a LIMIT 1) AS lastAssessmentDate, "+

                    "p.pd_propertytype_i AS propertyType, " +
                    "p.pd_propertysubtype_i AS propertySubType, " +
                    "p.pd_usagetype_i AS propertyUsageType, " +
                    "p.pd_usagesubtype_i AS propertyUsageSubType, " +
                    "p.pd_buildingtype_i AS buildingType," +
                    "p.pd_buildingsubtype_i AS buildingSubType," +
                    "p.pd_statusbuilding_vc AS buildingStatus," +
                    "p.pd_ownertype_vc AS ownerType," +
                    "p.pd_category_i AS ownerCategory," +
                    "p.pd_const_age_i AS age," +
                    "p.pd_plotarea_f AS totalPlotArea," +
                    "p.pd_totbuiltuparea_f AS totalBuiltupArea," +
                    "p.pd_totcarpetarea_f AS totalCarpetArea," +
                    "p.pd_totalexemparea_f AS totalExemption," +
                    "p.pd_arealetout_f AS totalAreaLetOut," +
                    "p.pd_areanotletout_f AS totalAreaNotLetOut," +

                    "u.ud_unitno_vc AS unitNo, " +
                    "u.ud_usagesubtype_i AS usageSubtype, " +
                    "u.ud_constyear_dt AS constDate, " +
                    "u.ud_const_age_i AS constAge, " +
                    "u.ud_exempted_area_f AS exemptedArea, " +

                    "pr.pr_residential_fl AS residentialFl, " +
                    "pr.pr_commercial_fl AS commercialFl, " +
                    "pr.pr_religious_fl AS religiousFl, " +
                    "pr.pr_residentialopenplot_fl AS residentialOpenPlotFl, " +
                    "pr.pr_commercialopenplot_fl AS commercialOpenPlotFl, " +
                    "pr.pr_governmentopenplot_fl AS governmentOpenPlotFl, " +
                    "pr.pr_educationlegalopenplot_fl AS educationAndLegalInstituteOpenPlotFl, " +
                    "pr.pr_religiousopenplot_fl AS religiousOpenPlotFl, " +
                    "pr.pr_industrialopenplot_fl AS industrialOpenPlotFl, " +
                    "pr.pr_educational_fl AS educationalInstituteFl, " +
                    "pr.pr_government_fl AS governmentFl, " +
                    "pr.pr_industrial_fl AS industrialFl, " +
                    "pr.pr_mobiletower_fl AS mobileTowerFl, " +
                    "pr.pr_electricsubstation_fl AS electricSubstationFl " +

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
                unitDto.setUnitNoVc(row[81] != null ? row[81].toString() : "N/A");
                unitDto.setUsageSubTypeVc(row[82] != null ? row[82].toString() : "N/A");
                unitDto.setConstructionYearVc(row[83] != null ? row[83].toString() : "N/A");
                unitDto.setConstructionAgeVc(row[84] != null ? row[84].toString() : "N/A");
                unitDto.setExemptedAreaFl(row[85] != null ? row[85].toString() : "N/A");
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
                proposedRatableValueDto.setResidentialFl(row[86] != null ? (Double) row[86] : null);
                proposedRatableValueDto.setCommercialFl(row[87] != null ? (Double) row[87] : null);
                proposedRatableValueDto.setReligiousFl(row[88] != null ? (Double) row[88] : null);
                proposedRatableValueDto.setResidentialOpenPlotFl(row[89] != null ? (Double) row[89] : null);
                proposedRatableValueDto.setCommercialOpenPlotFl(row[90] != null ? (Double) row[90] : null);
                proposedRatableValueDto.setGovernmentOpenPlotFl(row[91] != null ? (Double) row[91] : null);
                proposedRatableValueDto.setEducationAndLegalInstituteOpenPlotFl(row[92] != null ? (Double) row[92] : null);
                proposedRatableValueDto.setReligiousOpenPlotFl(row[93] != null ? (Double) row[93] : null);
                proposedRatableValueDto.setIndustrialOpenPlotFl(row[94] != null ? (Double) row[94] : null);
                proposedRatableValueDto.setEducationalInstituteFl(row[95] != null ? (Double) row[95] : null);
                proposedRatableValueDto.setGovernmentFl(row[96] != null ? (Double) row[96] : null);
                proposedRatableValueDto.setIndustrialFl(row[97] != null ? (Double) row[97] : null);
                proposedRatableValueDto.setMobileTowerFl(row[98] != null ? (Double) row[98] : null);
                proposedRatableValueDto.setElectricSubstationFl(row[99] != null ? (Double) row[99] : null);
                dto.setProposedRatableValues(proposedRatableValueDto);

                ConsolidatedTaxDetailsDto consolidatedTaxDto = new ConsolidatedTaxDetailsDto();
                consolidatedTaxDto.setTotalTaxFl(row[25] != null ? convertToDouble(row[25]) : null); // Total Tax
                dto.setConsolidatedTaxes(consolidatedTaxDto);

                dto.setPdOldrvFl(row[26] != null ? (String) row[26] : null); // Old RV
                dto.setPdOldTaxVc(row[27] != null ? (String) row[27] : null); // Old Tax
                dto.setPdOldpropnoVc(row[28] != null ? (String) row[28] : null);
                dto.setOldZoneNoVc(row[29] != null ? (String) row[29] : null);
                //Adding fields for the reports
                dto.setCurrentAssessmentDateDt(row[30] != null ? (String) row[30] : null);

                dto.setPdPropertyaddressVc(row[31] != null ? row[31].toString() : "N/A");
                dto.setPdPropertynameVc(row[32] != null ? row[32].toString() : "N/A");
                dto.setPdLayoutnameVc(row[33] != null ? row[33].toString() : "N/A");
                dto.setPdKhasranoVc(row[34] != null ? row[34].toString() : "N/A");
                dto.setPdPlotnoVc(row[35] != null ? row[35].toString() : "N/A");
                dto.setPdGrididVc(row[36] != null ? row[36].toString() : "N/A");
                dto.setPdRoadidVc(row[37] != null ? row[37].toString() : "N/A");
                dto.setPdParcelidVc(row[38] != null ? row[38].toString() : "N/A");
                dto.setPdContactnoVc(row[39] != null ? row[39].toString() : "N/A");
                dto.setPdMeternoVc(row[40] != null ? row[40].toString() : "N/A");
                dto.setPdConsumernoVc(row[41] != null ? row[41].toString() : "N/A");
                dto.setPdConnectiondateDt(row[42] != null ? row[42].toString() : "N/A"); // Assuming date is stored as string
                dto.setPdPipesizeF(row[43] != null ? row[43].toString() : "N/A");
                dto.setPdPermissionstatusVc(row[44] != null ? row[44].toString() : "N/A");
                dto.setPdPermissionnoVc(row[45] != null ? row[45].toString() : "N/A");
                dto.setPdPermissiondateDt(row[46] != null ? row[46].toString() : "N/A"); // Assuming date is stored as string
                dto.setPdRainwaterhaverstVc(row[47] != null ? row[47].toString() : "N/A");
                dto.setPdSolarunitVc(row[48] != null ? row[48].toString() : "N/A");
                dto.setPdStairVc(row[49] != null ? row[49].toString() : "N/A");
                dto.setPdLiftVc(row[50] != null ? row[50].toString() : "N/A");
                dto.setPdNoofroomsI(row[51] != null ? row[51].toString() : "N/A");
                dto.setPdNooffloorsI(row[52] != null ? row[52].toString() : "N/A");
                dto.setPdIndexnoVc(row[53] != null ? row[53].toString() : "N/A");
                dto.setPdToiletsI(row[54] != null ? row[54].toString() : "N/A");
                dto.setPdToiletstatusVc(row[55] != null ? row[55].toString() : "N/A");
                dto.setPdWaterconnstatusVc(row[56] != null ? row[56].toString() : "N/A");
                dto.setPdWaterconntypeVc(row[57] != null ? row[57].toString() : "N/A");
                dto.setPdWaterconntypeVc(row[58] != null ? row[58].toString() : "N/A");
                dto.setOldWardNoVc(row[59] != null ? row[59].toString() : "N/A");
                dto.setOldPropertyTypeVc(row[60] != null ? row[60].toString() : "N/A");
                dto.setOldPropertySubTypeVc(row[61] != null ? row[61].toString() : "N/A");
                dto.setOldUsageTypeVc(row[62] != null ? row[62].toString() : "N/A");
                dto.setOldConstructionTypeVc(row[63] != null ? row[63].toString() : "N/A");
                dto.setOldAssessmentAreaFl(row[64] != null ? row[64].toString() : "N/A");
                dto.setPdLastassesdateDt(row[65] != null ? row[65].toString() : "N/A");
                dto.setPdPropertytypeI(row[66] != null ? row[66].toString() : "N/A");
                dto.setPdPropertysubtypeI(row[67] != null ? row[67].toString() : "N/A");
                dto.setPdUsagetypeI(row[68] != null ? row[68].toString() : "N/A");
                dto.setPdUsagesubtypeI(row[69] != null ? row[69].toString() : "N/A");
                dto.setPdBuildingtypeI(row[70] != null ? row[70].toString() : "N/A");
                dto.setPdBuildingsubtypeI(row[71] != null ? row[71].toString() : "N/A");
                dto.setPdStatusbuildingVc(row[72] != null ? row[72].toString() : "N/A");
                dto.setPdOwnertypeVc(row[73] != null ? row[73].toString() : "N/A");
                dto.setPdCategoryI(row[74] != null ? row[74].toString() : "N/A");
                dto.setPdConstAgeI(row[75] != null ? row[75].toString() : "N/A");
                dto.setPdPlotareaF(row[76] != null ? row[76].toString() : "N/A");
                dto.setPdTotbuiltupareaF(row[77] != null ? row[77].toString() : "N/A");

                dto.setPdTotcarpetareaF(row[78] != null ? row[78].toString() : "N/A");
                dto.setPdTotalexempareaF(row[79] != null ? row[79].toString() : "N/A");
                dto.setPdArealetoutF(row[80] != null ? row[80].toString() : "N/A");
                dto.setPdAreanotletoutF(row[81] != null ? row[81].toString() : "N/A");
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