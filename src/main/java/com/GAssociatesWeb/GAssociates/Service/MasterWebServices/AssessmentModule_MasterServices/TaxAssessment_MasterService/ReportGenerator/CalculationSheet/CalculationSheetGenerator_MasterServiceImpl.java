package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.ReportGenerator.CalculationSheet;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ConsolidatedTaxDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.PropertyUnitDetailsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ProposedRatableValueDetailsDto;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalculationSheetGenerator_MasterServiceImpl implements CalculationSheetGenerator_MasterService {
    @PersistenceContext
    private EntityManager entityManager;

    private String getBaseReportQuery() {
        return """
        
            SELECT DISTINCT
            p.pd_ward_i AS ward,                                   --0
            p.pd_zone_i AS zone,                                   --1
            p.pd_newpropertyno_vc AS newpropertyno,                --2
            p.pd_finalpropno_vc AS finalpropertyno,                --3
            p.pd_ownername_vc AS ownername,                        --4
            p.pd_occupiname_f AS occupiername,                     --5
            p.pd_surypropno_vc AS surveypropno,                    --6
            u.ud_floorno_vc AS floorno,                            --7
            r.prv_yearlyrent_fl AS rentalno,                       --8
            u.ud_usagetype_i AS usagetype,                         --9
            u.ud_constructionclass_i AS constclass,                --10
            r.prv_agefactor_vc AS agefactor,                       --11
            u.ud_carpetarea_f AS carpetarea,                       --12
            u.ud_assessmentarea_f AS totalassesablearea,           --13
            r.prv_rate_f AS ratepersqm,                            --14
            r.prv_lentingvalue_f AS total_assessment_value,        --15
            r.prv_depper_i AS deppercent,                          --16
            r.prv_depamount_f AS depamount,                        --17
            r.prv_alv_f AS amountafterdep,                         --18
            r.prv_taxvalue_f AS taxableValueByRate,                --19
            ROUND(COALESCE(r.prv_mainval_f, 0)) AS ten_percent_of_considered,  --20
            r.prv_ratablevalue_f AS ratablevaluetobeconsidered,    --21
            pr.pr_totalrv_fl AS totalratablevalue,                 --22
            COALESCE(pr.pr_residential_fl, 0) AS residential,      --23
            COALESCE(pr.pr_commercial_fl, 0) +                     
            COALESCE(pr.pr_industrial_fl, 0) +                    
            COALESCE(pr.pr_government_fl, 0) +                     
            COALESCE(pr.pr_educational_fl, 0) +                    
            COALESCE(pr.pr_religious_fl, 0) +
            COALESCE(pr.pr_mobiletower_fl, 0) +
            COALESCE(pr.pr_electricsubstation_fl, 0) +
            COALESCE(pr.pr_commercialopenplot_fl, 0) +
            COALESCE(pr.pr_industrialopenplot_fl, 0) +
            COALESCE(pr.pr_governmentopenplot_fl, 0) +
            COALESCE(pr.pr_educationlegalopenplot_fl, 0) +
            COALESCE(pr.pr_religiousopenplot_fl, 0) +
            COALESCE(pr.pr_residentialopenplot_fl, 0) AS non_residential, --24
            ROUND(pt.pt_final_tax_fl) AS totaltax,                        --25
            old.pod_totalratablevalue_i AS oldrv,                         --26
            old.pod_totaltax_fl AS oldtax,                                --27
            old.pod_oldpropno_vc AS oldpropertyno,                        --28
            old.pod_zone_i AS oldpropertyzone,                            --29
            (SELECT a.current_assessment_date FROM assessmentdate_master a LIMIT 1) AS assessmentDate, --30
            p.pd_propertyaddress_vc AS propertyAddress,                   --31
            p.pd_propertyname_vc AS propertyName,                         --32
            p.pd_layoutname_vc AS layoutName,                             --33
            p.pd_khasrano_vc AS khasraNo,                                 --34
            p.pd_plotno_vc AS plotNo,                                     --35
            p.pd_gridid_vc AS gridId,                                     --36
            p.pd_parcelid_vc AS parcelId,                                 --37
            p.pd_roadid_vc AS roadId,                                     --38
            p.pd_contactno_vc AS contactNo,                               --39
            p.pd_meterno_vc AS meterNo,                                   --40
            p.pd_consumerno_vc AS consumerNo,                             --41
            p.pd_connectiondate_dt AS connectionDate,                     --42
            p.pd_pipesize_f AS pipeSize,                                  --43
            p.pd_permissionstatus_vc AS permissionStatus,                 --44
            p.pd_permissionno_vc AS permissionNo,                         --45
            p.pd_permissiondate_dt AS permissionDate,                     --46
            p.pd_rainwaterhaverst_vc AS rainwaterHarvest,                 --47
            p.pd_solarunit_vc AS solarUnit,                               --48
            p.pd_stair_vc AS stair,                                       --49
            p.pd_lift_vc AS lift,                                         --50
            p.pd_noofrooms_i AS noOfRooms,                                --51
            p.pd_nooffloors_i AS noOfFloors,                              --52
            p.pd_indexno_vc AS indexNo,                                   --53
            p.pd_toilets_i AS toiletNo,                                   --54
            p.pd_toiletstatus_vc AS toiletStatus,                         --55
            p.pd_waterconnstatus_vc AS waterConnection,                   --56
            p.pd_waterconntype_vc AS waterConnectionType,                 --57
            old.pod_ward_i AS oldWard,                                    --58                              
            old.pod_propertytype_i AS oldPropertyType,                    --59
            old.pod_propertysubtype_i AS oldPropertySubType,              --60
            old.pod_usagetype_i AS oldPropertyUsageType,                  --61
            old.pod_constclass_vc AS constClass,                          --62
            old.pod_totalassessmentarea_fl AS totalAssessableArea,        --63
            (SELECT a.last_assessment_date FROM assessmentdate_master a LIMIT 1) AS lastAssessmentDate,--64
            p.pd_propertytype_i AS propertyType,                          --65
            p.pd_propertysubtype_i AS propertySubType,                    --66         
            p.pd_usagetype_i AS propertyUsageType,                        --67
            p.pd_usagesubtype_i AS propertyUsageSubType,                  --68
            p.pd_buildingtype_i AS buildingType,                          --69
            p.pd_buildingsubtype_i AS buildingSubType,                    --70
            p.pd_statusbuilding_vc AS buildingStatus,                     --71
            p.pd_ownertype_vc AS ownerType,                               --72
            p.pd_category_i AS ownerCategory,                             --73
            p.pd_const_age_i AS age,                                      --74
            p.pd_plotarea_f AS totalPlotArea,                             --75
            p.pd_totbuiltuparea_f AS totalBuiltupArea,                    --76
            p.pd_totcarpetarea_f AS totalCarpetArea,                      --77
            p.pd_totalexemparea_f AS totalExemption,                      --78
            p.pd_arealetout_f AS totalAreaLetOut,                         --79
            p.pd_areanotletout_f AS totalAreaNotLetOut,                   --80
            u.ud_unitno_vc AS unitNo,                                     --81
            u.ud_usagesubtype_i AS usageSubtype,                          --82
            u.ud_constyear_dt AS constDate,                               --83
            u.ud_const_age_i AS constAge,                                 --84
            u.ud_exempted_area_f AS exemptedArea,                         --85
            pr.pr_residential_fl AS residentialFl,                        --86
            pr.pr_commercial_fl AS commercialFl,                          --87
            pr.pr_religious_fl AS religiousFl,                            --88
            pr.pr_residentialopenplot_fl AS residentialOpenPlotFl,        --89
            pr.pr_commercialopenplot_fl AS commercialOpenPlotFl,          --90
            pr.pr_governmentopenplot_fl AS governmentOpenPlotFl,          --91
            pr.pr_educationlegalopenplot_fl AS educationAndLegalInstituteOpenPlotFl, --92
            pr.pr_religiousopenplot_fl AS religiousOpenPlotFl,            --93
            pr.pr_industrialopenplot_fl AS industrialOpenPlotFl,          --94
            pr.pr_educational_fl AS educationalInstituteFl,               --95
            pr.pr_government_fl AS governmentFl,                          --96
            pr.pr_industrial_fl AS industrialFl,                          --97
            pr.pr_mobiletower_fl AS mobileTowerFl,                        --98
            pr.pr_electricsubstation_fl AS electricSubstationFl,          --99
            pt.pt_cleantax_fl AS cleanTax,                               --100 
            pt.pt_edunrestax_fl AS eduNonResidentialTax,                 --101
            pt.pt_edurestax_fl AS eduResidentialTax,                     --102
            pt.pt_edutax_fl AS totalEducationTax,                        --103         
            pt.pt_environmenttax_fl AS environmentTax,                   --104
            pt.pt_egctax_fl AS egcTax,                                   --105
            pt.pt_firetax_fl AS fireTax,                                 --106
            pt.pt_lighttax_fl AS lightTax,                               --107
            pt.pt_propertytax_fl AS propertyTax,                         --108
            pt.pt_treetax_fl AS treeTax,                                 --109
            pt.pt_usercharges_fl AS userCharges,                         --110
            r.prv_temainval_fl AS maintenanceRepairByRent,               --111
            r.prv_tentantval_f AS taxableValueByRent,                    --112
            u.ud_occupiername_vc AS tenantNameVc,                         --113
            p.pd_propimage_t AS propertyImage,                           --114
            p.pd_houseplan2_t AS cadFile,                                --115
            rsum.alv AS annualUnRentalValueFl,                             --116
            rsum.yearlyrent AS annualRentalValueFl,                    --117
            rsum.considered_rv AS finalValueToConsiderFl,                --118
            rsum.sum_maintenance_rent AS maintainenceRepairRentFl,       --119
            rsum.sum_maintenance_unrent AS maintainenceRepairUnrentFl,   --120
            rsum.maintenanceRepairsConsidered AS maintainenceRepairAmountConsideredFl, --121
            rsum.considered_rv_rental AS consideredRentalFl,             --122
            rsum.considered_rv_unrental AS consideredUnrentalFl,          --123
            u.ud_occupantstatus_i AS occupantStatus,                      --124
            u.ud_areabefded_fl AS areaBefDed,                             --125
            u.ud_plotarea_fl AS plotArea                                  --126
            , COALESCE(pt.pt_watertax_fl, 0) AS waterTax                   --127
            , COALESCE(pt.pt_seweragetax_fl, 0) AS sewerageTax             --128
            , COALESCE(pt.pt_seweragebenefittax_fl, 0) AS sewerageBenefitTax --129
            , COALESCE(pt.pt_waterbenefittax_fl, 0) AS waterBenefitTax     --130
            , COALESCE(pt.pt_streettax_fl, 0) AS streetTax                 --131
            , COALESCE(pt.pt_specialconservancytax_fl, 0) AS specialConservancyTax --132
            , COALESCE(pt.pt_municipaledutax_fl, 0) AS municipalEducationTax --133
            , COALESCE(pt.pt_specialedutax_fl, 0) AS specialEducationTax   --134
            , COALESCE(pt.pt_servicecharges_fl, 0) AS serviceCharges       --135
            , COALESCE(pt.pt_miscellaneouscharges_fl, 0) AS miscellaneousCharges --136
            , COALESCE(pt.pt_tax1_fl, 0) AS tax1                           --137
            , COALESCE(pt.pt_tax2_fl, 0) AS tax2                           --138
            , COALESCE(pt.pt_tax3_fl, 0) AS tax3                           --139
            , COALESCE(pt.pt_tax4_fl, 0) AS tax4                           --140
            , COALESCE(pt.pt_tax5_fl, 0) AS tax5                           --141
            , COALESCE(pt.pt_tax6_fl, 0) AS tax6                           --142
            , COALESCE(pt.pt_tax7_fl, 0) AS tax7                           --143
            , COALESCE(pt.pt_tax8_fl, 0) AS tax8                           --144
            , COALESCE(pt.pt_tax9_fl, 0) AS tax9                           --145
            , COALESCE(pt.pt_tax10_fl, 0) AS tax10                         --146
            , COALESCE(pt.pt_tax11_fl, 0) AS tax11                         --147
            , COALESCE(pt.pt_tax12_fl, 0) AS tax12                         --148
            , COALESCE(pt.pt_tax13_fl, 0) AS tax13                         --149
            , COALESCE(pt.pt_tax14_fl, 0) AS tax14                         --150
            , COALESCE(pt.pt_tax15_fl, 0) AS tax15                         --151
            , COALESCE(pt.pt_tax16_fl, 0) AS tax16                         --152
            , COALESCE(pt.pt_tax17_fl, 0) AS tax17                         --153
            , COALESCE(pt.pt_tax18_fl, 0) AS tax18                         --154
            , COALESCE(pt.pt_tax19_fl, 0) AS tax19                         --155
            , COALESCE(pt.pt_tax20_fl, 0) AS tax20                         --156
            , COALESCE(pt.pt_tax21_fl, 0) AS tax21                         --157
            , COALESCE(pt.pt_tax22_fl, 0) AS tax22                         --158
            , COALESCE(pt.pt_tax23_fl, 0) AS tax23                         --159
            , COALESCE(pt.pt_tax24_fl, 0) AS tax24                         --160
            , COALESCE(pt.pt_tax25_fl, 0) AS tax25                         --161
            
            FROM property_details p
            JOIN unit_details u ON p.pd_newpropertyno_vc = u.pd_newpropertyno_vc
            LEFT JOIN property_rvalues r ON p.pd_newpropertyno_vc = r.prv_propertyno_vc 
                AND CAST(r.prv_unitno_vc AS INTEGER) = u.ud_unitno_vc
            LEFT JOIN (
                SELECT
                    prv_propertyno_vc,
                    SUM(COALESCE(prv_alv_f, 0)) AS alv,
                    SUM(COALESCE(prv_yearlyrent_fl, 0)) AS yearlyrent,
                    GREATEST(
                        SUM(COALESCE(prv_alv_f, 0)),
                        SUM(COALESCE(prv_yearlyrent_fl, 0))
                    ) AS considered_rv,
                    SUM(COALESCE(prv_mainval_f, 0)) AS sum_maintenance_rent,
                    SUM(COALESCE(prv_temainval_fl, 0)) AS sum_maintenance_unrent,
                    GREATEST(
                        SUM(COALESCE(prv_mainval_f, 0)),
                        SUM(COALESCE(prv_temainval_fl, 0))
                    ) AS maintenanceRepairsConsidered,
                    CASE
                        WHEN SUM(COALESCE(prv_alv_f, 0)) > SUM(COALESCE(prv_yearlyrent_fl, 0))
                            THEN SUM(COALESCE(prv_alv_f, 0))
                        ELSE 0.0
                    END AS considered_rv_rental,
                    CASE
                        WHEN SUM(COALESCE(prv_yearlyrent_fl, 0)) > SUM(COALESCE(prv_alv_f, 0))
                            THEN SUM(COALESCE(prv_yearlyrent_fl, 0))
                        ELSE 0.0
                    END AS considered_rv_unrental
                FROM property_rvalues
                GROUP BY prv_propertyno_vc
            ) rsum ON p.pd_newpropertyno_vc = rsum.prv_propertyno_vc
            LEFT JOIN property_taxdetails pt ON p.pd_newpropertyno_vc = pt.pt_newpropertyno_vc
            LEFT JOIN proposed_rvalues pr ON p.pd_newpropertyno_vc = pr.pr_newpropertyno_vc
            LEFT JOIN property_olddetails old ON CAST(NULLIF(p.prop_refno, '') AS INTEGER) = old.pod_refno_vc
            """;
    }


    @Override
    public List<AssessmentResultsDto> generateSinglePropertyReport(String newPropertyNo) {
        try {
            String queryStr = getBaseReportQuery() + " WHERE p.pd_newpropertyno_vc = :newPropertyNo";
            Query query = entityManager.createNativeQuery(queryStr);
            query.setParameter("newPropertyNo", newPropertyNo);

            List<Object[]> rows = query.getResultList();
            Map<String, AssessmentResultsDto> propertyMap = new HashMap<>();
            for (Object[] row : rows) {
                String propertyNo = row[2] != null ? row[2].toString() : "-";

                AssessmentResultsDto dto = propertyMap.get(propertyNo);

                if (dto == null) {
                    dto = mapRowToDto(row);
                    dto.setUnitDetails(new ArrayList<>()); // Initialize unit list
                    propertyMap.put(propertyNo, dto);
                }

                PropertyUnitDetailsDto unitDto = mapRowToUnit(row);
                dto.getUnitDetails().add(unitDto);
            }
            return new ArrayList<>(propertyMap.values());
        } catch (Exception e) {
            System.out.println("Error generating single property report: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generating single property report", e);
        }
    }
    public List<AssessmentResultsDto> generatePropertyCalculationReport(Integer wardNo) {
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
                    "pr.pr_electricsubstation_fl AS electricSubstationFl, " +

                    "pt.pt_cleantax_fl AS cleanTax, " +
                    "pt.pt_edunrestax_fl AS eduNonResidentialTax, " +
                    "pt.pt_edurestax_fl AS eduResidentialTax, " +
                    "pt.pt_edutax_fl AS totalEducationTax, " +
                    "pt.pt_egctax_fl AS egcTax, " +
                    "pt.pt_environmenttax_fl AS environmentTax, " +
                    "pt.pt_firetax_fl AS fireTax, " +
                    "pt.pt_lighttax_fl AS lightTax, " +
                    "pt.pt_propertytax_fl AS propertyTax, " +
                    "pt.pt_treetax_fl AS treeTax, " +
                    "pt.pt_usercharges_fl AS userCharges, " +
                    "u.ud_occupantstatus_i AS occupantStatus, " +
                    "u.ud_areabefded_fl AS areaBefDed " +

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
                dto.setPdWardI(row[0] != null ? row[0].toString() : "-"); // Ward
                dto.setPdZoneI(row[1] != null ? row[1].toString() : "-"); // Zone
                dto.setPdNewpropertynoVc(row[2] != null ? row[2].toString() : "-"); // New Property Number
                dto.setPdFinalpropnoVc(row[3] != null ? row[3].toString() : "-"); // Final Property Number
                dto.setPdOwnernameVc(row[4] != null ? row[4].toString() : "-"); // Owner Name
                dto.setPdOccupinameF(row[5] != null ? row[5].toString() : "-"); // Occupier Name
                dto.setPdSurypropnoVc(row[6] != null ? row[6].toString() : "-"); // Survey Property Number

                // Property Unit Details
                PropertyUnitDetailsDto unitDto = new PropertyUnitDetailsDto();
                unitDto.setUnitNoVc(row[81] != null ? row[81].toString() : "-");
                unitDto.setUsageSubTypeVc(row[82] != null ? row[82].toString() : "-");
                unitDto.setConstructionYearVc(row[83] != null ? row[83].toString() : "-");
                unitDto.setConstructionAgeVc(row[84] != null ? row[84].toString() : "-");
                unitDto.setExemptedAreaFl(row[85] != null ? row[85].toString() : "-");
                unitDto.setFloorNoVc(row[7] != null ? row[7].toString() : "-"); // Floor Number
                unitDto.setActualAnnualRentFl(row[8] != null ? (Double) row[8] : 0.0); // Actual Annual Rent
                unitDto.setUsageTypeVc(row[9] != null ? row[9].toString() : "-"); // Usage Type
                unitDto.setConstructionTypeVc(row[10] != null ? row[10].toString() : "-"); // Construction Class
                unitDto.setAgeFactorVc(row[11] != null ? row[11].toString() : "-"); // Age Factor
                unitDto.setAreaBefDedFl(row[125] != null ? row[125].toString() : "-"); //area before deduction
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
                unitDto.setOccupantStatusI(row[111] != null ? row[111].toString() : "-");
                unitDto.setAreaBefDedFl(row[112] != null ? row[112].toString() : "-");
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
                consolidatedTaxDto.setPropertyTaxFl(convertToDouble(row[108]));
                consolidatedTaxDto.setEducationTaxResidFl(convertToDouble(row[102]));
                consolidatedTaxDto.setEducationTaxCommFl(convertToDouble(row[101]));
                consolidatedTaxDto.setEducationTaxTotalFl(convertToDouble(row[103]));
                consolidatedTaxDto.setEgcFl(convertToDouble(row[105])); // Employment Guarantee Cess
                consolidatedTaxDto.setTreeTaxFl(convertToDouble(row[109]));
                consolidatedTaxDto.setEnvironmentalTaxFl(convertToDouble(row[104]));
                consolidatedTaxDto.setCleannessTaxFl(convertToDouble(row[100]));           // //Changed 108 to 100
                consolidatedTaxDto.setLightTaxFl(convertToDouble(row[107]));
                consolidatedTaxDto.setFireTaxFl(convertToDouble(row[106]));
                consolidatedTaxDto.setUserChargesFl(convertToDouble(row[110]));
                dto.setConsolidatedTaxes(consolidatedTaxDto);

                dto.setPdOldrvFl(row[26] != null ? (String) row[26] : null); // Old RV
                dto.setPdOldTaxVc(row[27] != null ? (String) row[27] : null); // Old Tax
                dto.setPdOldpropnoVc(row[28] != null ? (String) row[28] : null);
                dto.setOldZoneNoVc(row[29] != null ? (String) row[29] : null);
                //Adding fields for the reports
                dto.setCurrentAssessmentDateDt(row[30] != null ? (String) row[30] : null);

                dto.setPdPropertyaddressVc(row[31] != null ? row[31].toString() : "-");
                dto.setPdPropertynameVc(row[32] != null ? row[32].toString() : "-");
                dto.setPdLayoutnameVc(row[33] != null ? row[33].toString() : "-");
                dto.setPdKhasranoVc(row[34] != null ? row[34].toString() : "-");
                dto.setPdPlotnoVc(row[35] != null ? row[35].toString() : "-");
                dto.setPdGrididVc(row[36] != null ? row[36].toString() : "-");
                dto.setPdRoadidVc(row[38] != null ? row[38].toString() : "-");
                dto.setPdParcelidVc(row[37] != null ? row[37].toString() : "-");
                dto.setPdContactnoVc(row[39] != null ? row[39].toString() : "-");
                dto.setPdMeternoVc(row[40] != null ? row[40].toString() : "-");
                dto.setPdConsumernoVc(row[41] != null ? row[41].toString() : "-");
                dto.setPdConnectiondateDt(row[42] != null ? row[42].toString() : "-"); // Assuming date is stored as string
                dto.setPdPipesizeF(row[43] != null ? row[43].toString() : "-");
                dto.setPdPermissionstatusVc(row[44] != null ? row[44].toString() : "-");
                dto.setPdPermissionnoVc(row[45] != null ? row[45].toString() : "-");
                dto.setPdPermissiondateDt(row[46] != null ? row[46].toString() : "-"); // Assuming date is stored as string
                dto.setPdRainwaterhaverstVc(row[47] != null ? row[47].toString() : "-");
                dto.setPdSolarunitVc(row[48] != null ? row[48].toString() : "-");
                dto.setPdStairVc(row[49] != null ? row[49].toString() : "-");
                dto.setPdLiftVc(row[50] != null ? row[50].toString() : "-");
                dto.setPdNoofroomsI(row[51] != null ? row[51].toString() : "-");
                dto.setPdNooffloorsI(row[52] != null ? row[52].toString() : "-");
                dto.setPdIndexnoVc(row[53] != null ? row[53].toString() : "-");
                dto.setPdToiletsI(row[54] != null ? row[54].toString() : "-");
                dto.setPdToiletstatusVc(row[55] != null ? row[55].toString() : "-");
                dto.setPdWaterconnstatusVc(row[56] != null ? row[56].toString() : "-");
                dto.setPdWaterconntypeVc(row[57] != null ? row[57].toString() : "-");
                dto.setPdWaterconntypeVc(row[58] != null ? row[58].toString() : "-");
                dto.setOldWardNoVc(row[58] != null ? row[58].toString() : "-");
                dto.setOldPropertyTypeVc(row[59] != null ? row[59].toString() : "-");
                dto.setOldPropertySubTypeVc(row[60] != null ? row[60].toString() : "-");
                dto.setOldUsageTypeVc(row[61] != null ? row[61].toString() : "-");
                dto.setOldConstructionTypeVc(row[62] != null ? row[62].toString() : "-");
                dto.setOldAssessmentAreaFl(row[63] != null ? row[63].toString() : "-");
                dto.setPdLastassesdateDt(row[64] != null ? row[64].toString() : "-");
                dto.setPdPropertytypeI(row[65] != null ? row[65].toString() : "-");
                dto.setPdPropertysubtypeI(row[66] != null ? row[66].toString() : "-");
                dto.setPdUsagetypeI(row[67] != null ? row[67].toString() : "-");
                dto.setPdUsagesubtypeI(row[68] != null ? row[68].toString() : "-");
                dto.setPdBuildingtypeI(row[69] != null ? row[69].toString() : "-");
                dto.setPdBuildingsubtypeI(row[70] != null ? row[70].toString() : "-");
                dto.setPdStatusbuildingVc(row[71] != null ? row[71].toString() : "-");
                dto.setPdOwnertypeVc(row[72] != null ? row[72].toString() : "-");
                dto.setPdCategoryI(row[73] != null ? row[73].toString() : "-");
                dto.setPdConstAgeI(row[74] != null ? row[74].toString() : "-");
                dto.setPdPlotareaF(row[75] != null ? row[75].toString() : "-");
                dto.setPdTotbuiltupareaF(row[76] != null ? row[76].toString() : "-");

                dto.setPdTotcarpetareaF(row[77] != null ? row[77].toString() : "-");
                dto.setPdTotalexempareaF(row[78] != null ? row[78].toString() : "-");
                dto.setPdArealetoutF(row[79] != null ? row[79].toString() : "-");
                dto.setPdAreanotletoutF(row[80] != null ? row[80].toString() : "-");
                assessmentResults.add(dto);
            }

            return assessmentResults;
        } catch (Exception e) {
            System.out.println("Error occurred while generating the report: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generating the report", e);
        }
    }

    // Added: Paged and ordered calculation-sheet fetch for sequential, deterministic printing
    // This complements the existing non-paged variant and is invoked by the /propertyCalculationSheetReport endpoint.

    public List<AssessmentResultsDto> generatePropertyCalculationReport(Integer wardNo, int page, int size) {
        try {
            String queryStr = getBaseReportQuery() +
                    " WHERE p.pd_ward_i = :wardNo ORDER BY p.pd_finalpropno_vc";
            Query query = entityManager.createNativeQuery(queryStr);
            query.setParameter("wardNo", wardNo);
            if (page >= 0 && size > 0) {
                query.setFirstResult(page * size);
                query.setMaxResults(size);
            }

            List<Object[]> rows = query.getResultList();
            Map<String, AssessmentResultsDto> propertyMap = new HashMap<>();
            for (Object[] row : rows) {
                String propertyNo = row[2] != null ? row[2].toString() : "-";
                AssessmentResultsDto dto = propertyMap.get(propertyNo);
                if (dto == null) {
                    dto = mapRowToDto(row);
                    dto.setUnitDetails(new ArrayList<>());
                    propertyMap.put(propertyNo, dto);
                }
                PropertyUnitDetailsDto unitDto = mapRowToUnit(row);
                dto.getUnitDetails().add(unitDto);
            }
            return new ArrayList<>(propertyMap.values());
        } catch (Exception e) {
            System.out.println("Error generating paged property calculation report: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generating paged property calculation report", e);
        }
    }

    public long countPropertiesForCalculationReport(Integer wardNo) {
        String countSql = "SELECT COUNT(DISTINCT p.pd_newpropertyno_vc) FROM property_details p WHERE p.pd_ward_i = :wardNo";
        Query q = entityManager.createNativeQuery(countSql);
        q.setParameter("wardNo", wardNo);
        Object result = q.getSingleResult();
        if (result instanceof Number) {
            return ((Number) result).longValue();
        }
        return 0L;
    }

    private AssessmentResultsDto mapRowToDto(Object[] row) {
        AssessmentResultsDto dto = new AssessmentResultsDto();

        // Basic property info
        dto.setPdWardI(valueOf(row[0]));
        dto.setPdZoneI(valueOf(row[1]));
        dto.setPdNewpropertynoVc(valueOf(row[2]));
        dto.setPdFinalpropnoVc(valueOf(row[3]));
        dto.setPdOwnernameVc(valueOf(row[4]));
        dto.setPdOccupinameF(valueOf(row[5]));
        dto.setPdSurypropnoVc(valueOf(row[6]));

        // Proposed Ratable Values
        ProposedRatableValueDetailsDto proposedRatableValueDto = new ProposedRatableValueDetailsDto();
        proposedRatableValueDto.setAggregateFl(toDouble(row[22]));
        proposedRatableValueDto.setResidentialFl(toDouble(row[23]));
        proposedRatableValueDto.setNonResidentialFl(toDouble(row[24]));
        proposedRatableValueDto.setResidentialFl(toDouble(row[86]));
        proposedRatableValueDto.setCommercialFl(toDouble(row[87]));
        proposedRatableValueDto.setReligiousFl(toDouble(row[88]));
        proposedRatableValueDto.setResidentialOpenPlotFl(toDouble(row[89]));
        proposedRatableValueDto.setCommercialOpenPlotFl(toDouble(row[90]));
        proposedRatableValueDto.setGovernmentOpenPlotFl(toDouble(row[91]));
        proposedRatableValueDto.setEducationAndLegalInstituteOpenPlotFl(toDouble(row[92]));
        proposedRatableValueDto.setReligiousOpenPlotFl(toDouble(row[93]));
        proposedRatableValueDto.setIndustrialOpenPlotFl(toDouble(row[94]));
        proposedRatableValueDto.setEducationalInstituteFl(toDouble(row[95]));
        proposedRatableValueDto.setGovernmentFl(toDouble(row[96]));
        proposedRatableValueDto.setIndustrialFl(toDouble(row[97]));
        proposedRatableValueDto.setMobileTowerFl(toDouble(row[98]));
        proposedRatableValueDto.setElectricSubstationFl(toDouble(row[99]));
        dto.setProposedRatableValues(proposedRatableValueDto);

        // Consolidated Taxes
        ConsolidatedTaxDetailsDto consolidatedTaxDto = new ConsolidatedTaxDetailsDto();
        consolidatedTaxDto.setTotalTaxFl(toDouble(row[25]));
        consolidatedTaxDto.setCleannessTaxFl(toDouble(row[100]));

        consolidatedTaxDto.setEducationTaxResidFl(toDouble(row[102]));
        consolidatedTaxDto.setEducationTaxCommFl(toDouble(row[101]));
        consolidatedTaxDto.setEducationTaxTotalFl(toDouble(row[103]));
        consolidatedTaxDto.setEgcFl(toDouble(row[105]));
        consolidatedTaxDto.setEnvironmentalTaxFl(toDouble(row[104]));
        consolidatedTaxDto.setTreeTaxFl(toDouble(row[109]));
        consolidatedTaxDto.setLightTaxFl(toDouble(row[107]));
        consolidatedTaxDto.setPropertyTaxFl(toDouble(row[108]));
        consolidatedTaxDto.setFireTaxFl(toDouble(row[106]));
        consolidatedTaxDto.setUserChargesFl(toDouble(row[110]));

        // Newly appended individual tax columns (indices 127..161)
        consolidatedTaxDto.setWaterTaxFl(toDouble(row[127]));
        consolidatedTaxDto.setSewerageTaxFl(toDouble(row[128]));
        consolidatedTaxDto.setSewerageBenefitTaxFl(toDouble(row[129]));
        consolidatedTaxDto.setWaterBenefitTaxFl(toDouble(row[130]));
        consolidatedTaxDto.setStreetTaxFl(toDouble(row[131]));
        consolidatedTaxDto.setSpecialConservancyTaxFl(toDouble(row[132]));
        consolidatedTaxDto.setMunicipalEducationTaxFl(toDouble(row[133]));
        consolidatedTaxDto.setSpecialEducationTaxFl(toDouble(row[134]));
        consolidatedTaxDto.setServiceChargesFl(toDouble(row[135]));
        consolidatedTaxDto.setMiscellaneousChargesFl(toDouble(row[136]));
        consolidatedTaxDto.setTax1Fl(toDouble(row[137]));
        consolidatedTaxDto.setTax2Fl(toDouble(row[138]));
        consolidatedTaxDto.setTax3Fl(toDouble(row[139]));
        consolidatedTaxDto.setTax4Fl(toDouble(row[140]));
        consolidatedTaxDto.setTax5Fl(toDouble(row[141]));
        consolidatedTaxDto.setTax6Fl(toDouble(row[142]));
        consolidatedTaxDto.setTax7Fl(toDouble(row[143]));
        consolidatedTaxDto.setTax8Fl(toDouble(row[144]));
        consolidatedTaxDto.setTax9Fl(toDouble(row[145]));
        consolidatedTaxDto.setTax10Fl(toDouble(row[146]));
        consolidatedTaxDto.setTax11Fl(toDouble(row[147]));
        consolidatedTaxDto.setTax12Fl(toDouble(row[148]));
        consolidatedTaxDto.setTax13Fl(toDouble(row[149]));
        consolidatedTaxDto.setTax14Fl(toDouble(row[150]));
        consolidatedTaxDto.setTax15Fl(toDouble(row[151]));
        consolidatedTaxDto.setTax16Fl(toDouble(row[152]));
        consolidatedTaxDto.setTax17Fl(toDouble(row[153]));
        consolidatedTaxDto.setTax18Fl(toDouble(row[154]));
        consolidatedTaxDto.setTax19Fl(toDouble(row[155]));
        consolidatedTaxDto.setTax20Fl(toDouble(row[156]));
        consolidatedTaxDto.setTax21Fl(toDouble(row[157]));
        consolidatedTaxDto.setTax22Fl(toDouble(row[158]));
        consolidatedTaxDto.setTax23Fl(toDouble(row[159]));
        consolidatedTaxDto.setTax24Fl(toDouble(row[160]));
        consolidatedTaxDto.setTax25Fl(toDouble(row[161]));

        dto.setConsolidatedTaxes(consolidatedTaxDto);

        // Populate tax map by keys for dynamic consumption
        Map<Long, Double> taxMap = dto.getTaxKeyValueMap();
        putIfNotNull(taxMap, ReportTaxKeys.PT_PARENT, toDouble(row[108]));
        Double eduRes = toDouble(row[102]);
        Double eduComm = toDouble(row[101]);
        putIfNotNull(taxMap, ReportTaxKeys.EDUC_RES, eduRes);
        putIfNotNull(taxMap, ReportTaxKeys.EDUC_COMM, eduComm);
        if (eduRes != null || eduComm != null) {
            double sum = (eduRes != null ? eduRes : 0.0) + (eduComm != null ? eduComm : 0.0);
            putIfNotNull(taxMap, ReportTaxKeys.EDUC_PARENT, sum);
        }
        putIfNotNull(taxMap, ReportTaxKeys.EGC, toDouble(row[105]));
        putIfNotNull(taxMap, ReportTaxKeys.ENV_TAX, toDouble(row[104]));
        putIfNotNull(taxMap, ReportTaxKeys.FIRE_TAX, toDouble(row[106]));
        putIfNotNull(taxMap, ReportTaxKeys.LIGHT_TAX, toDouble(row[107]));
        putIfNotNull(taxMap, ReportTaxKeys.TREE_TAX, toDouble(row[109]));
        putIfNotNull(taxMap, ReportTaxKeys.USER_CHG, toDouble(row[110]));
        putIfNotNull(taxMap, ReportTaxKeys.CLEAN_TAX, toDouble(row[100]));

        // Extended taxes
        putIfNotNull(taxMap, ReportTaxKeys.WATER_TAX, toDouble(row[127]));
        putIfNotNull(taxMap, ReportTaxKeys.SEWERAGE_TAX, toDouble(row[128]));
        putIfNotNull(taxMap, ReportTaxKeys.SEWERAGE_BEN, toDouble(row[129]));
        putIfNotNull(taxMap, ReportTaxKeys.WATER_BEN, toDouble(row[130]));
        putIfNotNull(taxMap, ReportTaxKeys.STREET_TAX, toDouble(row[131]));
        putIfNotNull(taxMap, ReportTaxKeys.SPEC_CONS, toDouble(row[132]));
        putIfNotNull(taxMap, ReportTaxKeys.MUNICIPAL_EDU, toDouble(row[133]));
        putIfNotNull(taxMap, ReportTaxKeys.SPECIAL_EDU, toDouble(row[134]));
        putIfNotNull(taxMap, ReportTaxKeys.SERVICE_CHG, toDouble(row[135]));
        putIfNotNull(taxMap, ReportTaxKeys.MISC_CHG, toDouble(row[136]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX1, toDouble(row[137]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX2, toDouble(row[138]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX3, toDouble(row[139]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX4, toDouble(row[140]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX5, toDouble(row[141]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX6, toDouble(row[142]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX7, toDouble(row[143]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX8, toDouble(row[144]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX9, toDouble(row[145]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX10, toDouble(row[146]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX11, toDouble(row[147]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX12, toDouble(row[148]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX13, toDouble(row[149]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX14, toDouble(row[150]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX15, toDouble(row[151]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX16, toDouble(row[152]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX17, toDouble(row[153]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX18, toDouble(row[154]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX19, toDouble(row[155]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX20, toDouble(row[156]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX21, toDouble(row[157]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX22, toDouble(row[158]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX23, toDouble(row[159]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX24, toDouble(row[160]));
        putIfNotNull(taxMap, ReportTaxKeys.TAX25, toDouble(row[161]));

        // Other property info
        dto.setPdOldrvFl(valueOf(row[26]));
        dto.setPdOldTaxVc(valueOf(row[27]));
        dto.setPdOldpropnoVc(valueOf(row[28]));
        dto.setOldZoneNoVc(valueOf(row[29]));
        dto.setCurrentAssessmentDateDt(valueOf(row[30]));

        // Address and other details
        dto.setPdPropertyaddressVc(valueOf(row[31]));
        dto.setPdPropertynameVc(valueOf(row[32]));
        dto.setPdLayoutnameVc(valueOf(row[33]));
        dto.setPdKhasranoVc(valueOf(row[34]));
        dto.setPdPlotnoVc(valueOf(row[35]));
        dto.setPdGrididVc(valueOf(row[36]));
        dto.setPdRoadidVc(valueOf(row[38]));
        dto.setPdParcelidVc(valueOf(row[37]));
        dto.setPdContactnoVc(valueOf(row[39]));
        dto.setPdMeternoVc(valueOf(row[40]));
        dto.setPdConsumernoVc(valueOf(row[41]));
        dto.setPdConnectiondateDt(valueOf(row[42]));
        dto.setPdPipesizeF(valueOf(row[43]));
        dto.setPdPermissionstatusVc(valueOf(row[44]));
        dto.setPdPermissionnoVc(valueOf(row[45]));
        dto.setPdPermissiondateDt(valueOf(row[46]));
        dto.setPdRainwaterhaverstVc(valueOf(row[47]));
        dto.setPdSolarunitVc(valueOf(row[48]));
        dto.setPdStairVc(valueOf(row[49]));
        dto.setPdLiftVc(valueOf(row[50]));
        dto.setPdNoofroomsI(valueOf(row[51]));
        dto.setPdNooffloorsI(valueOf(row[52]));
        dto.setPdIndexnoVc(valueOf(row[53]));
        dto.setPdToiletsI(valueOf(row[54]));
        dto.setPdToiletstatusVc(valueOf(row[55]));
        dto.setPdWaterconnstatusVc(valueOf(row[56]));
        dto.setPdWaterconntypeVc(valueOf(row[57]));

        dto.setOldWardNoVc(valueOf(row[58]));                    //Changed 59 to 58
        dto.setOldPropertyTypeVc(valueOf(row[59]));              //Changed 60 to 59
        dto.setOldPropertySubTypeVc(valueOf(row[60]));          //Changed 61 to 60
        dto.setOldUsageTypeVc(valueOf(row[61]));                //Changed 62 to 61
        dto.setOldConstructionTypeVc(valueOf(row[62]));         //Changed 63 to 62
        dto.setOldAssessmentAreaFl(valueOf(row[63]));           //Changed 64 to 63
        dto.setPdLastassesdateDt(valueOf(row[64]));             //Changed 65 to 64

        dto.setPdPropertytypeI(valueOf(row[65]));               //Changed 66 to 65
        dto.setPdPropertysubtypeI(valueOf(row[66]));            //Changed 67 to 66
        dto.setPdUsagetypeI(valueOf(row[67]));                  //Changed 68 t0 67
        dto.setPdUsagesubtypeI(valueOf(row[68]));               //Changed 69 to 68
        dto.setPdBuildingtypeI(valueOf(row[69]));               //Changed 70 to 69
        dto.setPdBuildingsubtypeI(valueOf(row[70]));            //Changed 71 t0 70
        dto.setPdStatusbuildingVc(valueOf(row[71]));            //Changed 72 to 71
        dto.setPdOwnertypeVc(valueOf(row[72]));                 //Changed 73 to 72
        dto.setPdCategoryI(valueOf(row[73]));                   //Changed 74 to 73
        dto.setPdConstAgeI(valueOf(row[74]));                   //Changed 75 to 74
        dto.setPdPlotareaF(valueOf(row[75]));                   //Changed 76 to 75
        dto.setPdTotbuiltupareaF(valueOf(row[76]));              //Changed 77 to 76
        dto.setPdTotcarpetareaF(valueOf(row[77]));               //Changed 78 to 77
        dto.setPdTotalexempareaF(valueOf(row[78]));              //Changed 79 to 78
        dto.setPdArealetoutF(valueOf(row[79]));                 //Changed 80 to 79
        dto.setPdAreanotletoutF(valueOf(row[80]));              //Changed 81 to 80
        dto.setPdPropimageT(valueOf(row[114]));
        dto.setPdHouseplan2T(valueOf(row[115]));

        dto.setAnnualRentalValueFl(valueOf(row[117]));
        dto.setAnnualUnRentalValueFl(valueOf(row[116]));
        dto.setFinalValueToConsiderFl(valueOf(row[118]));
        dto.setMaintainenceRepairAmountConsideredFl(valueOf(row[121]));
        dto.setTaxableValueRentedFl(row[117] != null
                ? String.valueOf(((Number) row[117]).doubleValue() - (((Number) row[117]).doubleValue() * 0.10))
                : "0");

        dto.setTaxableValueUnRentedFl(row[116] != null
                ? String.valueOf(((Number) row[116]).doubleValue() - (((Number) row[116]).doubleValue() * 0.10))
                : "0");
        return dto;
    }
    private PropertyUnitDetailsDto mapRowToUnit(Object[] row) {
        PropertyUnitDetailsDto unitDto = new PropertyUnitDetailsDto();
        unitDto.setUnitNoVc(valueOf(row[81]));
        unitDto.setUsageSubTypeVc(valueOf(row[82]));
        unitDto.setConstructionYearVc(valueOf(row[83]));
        unitDto.setConstructionAgeVc(valueOf(row[84]));
        unitDto.setExemptedAreaFl(valueOf(row[85]));
        unitDto.setFloorNoVc(valueOf(row[7]));
        unitDto.setActualAnnualRentFl(toDouble(row[8]));
        unitDto.setUsageTypeVc(valueOf(row[9]));
        unitDto.setConstructionTypeVc(valueOf(row[10]));
        unitDto.setAgeFactorVc(valueOf(row[11]));
        unitDto.setCarpetAreaFl(valueOf(row[12]));
        unitDto.setTaxableAreaFl(valueOf(row[13]));
        unitDto.setRatePerSqMFl(toDouble(row[14]));
        unitDto.setRentalValAsPerRateFl(toDouble(row[15]));
        unitDto.setDepreciationRateFl(toDouble(row[16]));
        unitDto.setDepreciationAmountFl(toDouble(row[17]));
        unitDto.setAmountAfterDepreciationFl(toDouble(row[18]));
        unitDto.setTaxableValueByRateFl(toDouble(row[19]));
        unitDto.setMaintenanceRepairsFl(toDouble(row[20]));
        unitDto.setMaintenanceRepairsRentFl(toDouble(row[111]));
        unitDto.setTaxableValueByRentFl(toDouble(row[112]));
        unitDto.setTenantNameVc(valueOf(row[113]));
        unitDto.setOccupantStatusI(valueOf(row[124]));
        unitDto.setAreaBefDedFl(valueOf(row[125]));
        unitDto.setPlotAreaFl(valueOf(row[126]));




        unitDto.setTaxableValueConsideredFl(toDouble(row[21]));
        return unitDto;
    }

    private String valueOf(Object obj) {
        return obj != null ? obj.toString() : "-";
    }

    private Double toDouble(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        return null;
    }

    private Double convertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        }
        return null;
    }

    private void putIfNotNull(Map<Long, Double> map, Long key, Double value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}
