package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.ConsolidatedTaxes_MasterDto.ConsolidatedTaxes_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto.RVTypeCategory_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto.RVTypes_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.ReportConfig_MasterDto.ReportTaxes_MasterDto;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.ConolidatedTaxes_MasterService.ConsolidatedTaxes_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.RVTypes_MasterService.RVTypeCategory_MasterService.RVTypeCategory_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.RVTypes_MasterService.RVTypes_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxesConfigService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DefaultTypesAndTaxes {

    @Autowired
    private RVTypes_MasterService rvTypes_MasterService;

    @Autowired
    private ReportTaxesConfigService reportTaxesConfigService;

    @Autowired
    private RVTypeCategory_MasterService rvTypeCategory_MasterService;

    @Autowired
    private ConsolidatedTaxes_MasterService consolidatedTaxes_MasterService;

    @PostConstruct
    public void init() {
        try {
            if (rvTypeCategory_MasterService.getAllRVTypeCategories().isEmpty()) {
                List<RVTypeCategory_MasterDto> defaultCategories = Arrays.asList(
                        new RVTypeCategory_MasterDto(1L, "Residential", "निवासी", "Used for residential properties"),
                        new RVTypeCategory_MasterDto(2L, "Mobile Tower", "मोबाईल टॉवर", "Used for mobile towers"),
                        new RVTypeCategory_MasterDto(3L, "Electric Substation", "इलेक्ट्रिक सब-स्टेशन", "Used for electric substations"),
                        new RVTypeCategory_MasterDto(4L, "Commercial", "वाणिज्य", "Used for commercial enterprises"),
                        new RVTypeCategory_MasterDto(5L, "Government", "शासकीय", "Used for government properties"),
                        new RVTypeCategory_MasterDto(6L, "Educational Institute", "शैक्षणिक संस्थान", "Used for educational institutions"),
                        new RVTypeCategory_MasterDto(7L, "Religious", "धार्मिक", "Used for religious facilities"),
                        new RVTypeCategory_MasterDto(8L, "Industrial", "औद्योगिक", "Used for industrial sites"),
                        new RVTypeCategory_MasterDto(9L, "Commercial Open Plot", "वाणिज्य खुला भूखंड", "Open plots for commercial use"),
                        new RVTypeCategory_MasterDto(10L, "Residential Open Plot", "निवासी खुला भूखंड", "Open plots for residential use"),
                        new RVTypeCategory_MasterDto(11L, "Government Open Plot", "शासकीय खुला भूखंड", "Open plots for government use"),
                        new RVTypeCategory_MasterDto(12L, "Educational and Legal Institute Open Plot", "शैक्ष.व वैधा संस्थान खुला भूखंड", "Open plots for educational and legal institutions"),
                        new RVTypeCategory_MasterDto(13L, "Religious Open Plot", "धार्मिक खुला भूखंड", "Open plots for religious facilities"),
                        new RVTypeCategory_MasterDto(14L, "Industrial Open Plot", "औद्योगिक खुला भूखंड", "Open plots for industrial use"),
                        new RVTypeCategory_MasterDto(15L, "Public Open Plot", "सार्वजनिक खुला भूखंड", "Open plots for Public use")
                );

                defaultCategories.forEach(rvTypeCategory_MasterService::saveRVTypeCategory);
            }

            if (rvTypes_MasterService.getAllRVTypes().isEmpty()) {
                List<RVTypes_MasterDto> defaultRVTypes = Arrays.asList(
                        new RVTypes_MasterDto(null, "Rate Type 1", "0.0", "NA", "Used for Residential", null, 1L),
                        new RVTypes_MasterDto(null, "Rate Type 2", "0.0", "NA", "Used for Mobile Tower", null, 2L),
                        new RVTypes_MasterDto(null, "Rate Type 3", "0.0", "NA", "Used for Electric Substation", null, 3L),
                        new RVTypes_MasterDto(null, "Rate Type 4", "0.0", "NA", "Used for Commercial", null, 4L),
                        new RVTypes_MasterDto(null, "Rate Type 5", "0.0", "NA", "Used for Government", null, 5L),
                        new RVTypes_MasterDto(null, "Rate Type 6", "0.0", "NA", "Used for Educational", null, 6L),
                        new RVTypes_MasterDto(null, "Rate Type 7", "0.0", "NA", "Used for Religious", null, 7L),
                        new RVTypes_MasterDto(null, "Rate Type 8", "0.0", "NA", "Used for Industrial", null, 8L),
                        new RVTypes_MasterDto(null, "Rate Type 9", "0.0", "NA", "Used for Commercial Open Plot", null, 9L),
                        new RVTypes_MasterDto(null, "Rate Type 10", "0.0", "NA", "Used for Residential Open Plot", null, 10L),
                        new RVTypes_MasterDto(null, "Rate Type 11", "0.0", "NA", "Used for Government Open Plot", null, 11L),
                        new RVTypes_MasterDto(null, "Rate Type 12", "0.0", "NA", "Used for Educational Open Plot", null, 12L),
                        new RVTypes_MasterDto(null, "Rate Type 13", "0.0", "NA", "Used for Religious Open Plot", null, 13L),
                        new RVTypes_MasterDto(null, "Rate Type 14", "0.0", "NA", "Used for Industrial Open Plot", null, 14L),
                        new RVTypes_MasterDto(null, "Rate Type 15", "0.0", "NA", "Used for Public Open Plot", null, 15L)
                );

                for (RVTypes_MasterDto rvType : defaultRVTypes) {
                    rvTypes_MasterService.saveRVType(rvType);
                }
            }

//            if (consolidatedTaxes_MasterService.getAllTaxes().isEmpty()) {
            List<ConsolidatedTaxes_MasterDto> defaultTaxes = Arrays.asList(
                    new ConsolidatedTaxes_MasterDto(null, "Property Tax", "2.0", "Ratable Value", true, "PROPERTY_TAX", "General property tax", ReportTaxKeys.PT1, 1),
                    new ConsolidatedTaxes_MasterDto(null, "Education Tax - Residential", "NA", "NA", true, "ECESS_RESIDENTIAL", "Slab-based education tax for residential", ReportTaxKeys.EDUC_RES, 2),
                    new ConsolidatedTaxes_MasterDto(null, "Education Tax - Commercial", "NA", "NA", true, "ECESS_COMMERCIAL", "Slab-based education tax for commercial", ReportTaxKeys.EDUC_COMM, 3),
                    new ConsolidatedTaxes_MasterDto(null, "Employment Guarantee Cess", "NA", "NA", true, "EGCESS", "Slab-based employment guarantee cess", ReportTaxKeys.EGC, 4),
                    new ConsolidatedTaxes_MasterDto(null, "Property Tax II", "2.0", "Ratable Value", true, "PROPERTY_TAX_II", "Additional property tax", ReportTaxKeys.PT2, 17),
                    new ConsolidatedTaxes_MasterDto(null, "Tree Tax", "1.0", "Ratable Value", true, "TREE_TAX", "Tree plantation/maintenance", ReportTaxKeys.TREE_TAX, 16),
                    new ConsolidatedTaxes_MasterDto(null, "Environment Tax", "1.0", "Ratable Value", true, "ENV_TAX", "Environmental protection tax", ReportTaxKeys.ENV_TAX, 4),
                    new ConsolidatedTaxes_MasterDto(null, "Cleanness Tax", "1.0", "Ratable Value", true, "CLEAN_TAX", "Sanitation and cleaning tax", ReportTaxKeys.CLEAN_TAX, 5),
                    new ConsolidatedTaxes_MasterDto(null, "Light Tax", "1.0", "Ratable Value", true, "LIGHT_TAX", "Public lighting tax", ReportTaxKeys.LIGHT_TAX, 6),
                    new ConsolidatedTaxes_MasterDto(null, "Fire Tax", "2.0", "Ratable Value", true, "FIRE_TAX", "Fire protection services", ReportTaxKeys.FIRE_TAX, 7),
                    new ConsolidatedTaxes_MasterDto(null, "Water Tax", "0.0", "Ratable Value", true, "WATER_TAX", "Municipal water supply", ReportTaxKeys.WATER_TAX, 8),
                    new ConsolidatedTaxes_MasterDto(null, "Sewerage Tax", "0.0", "Ratable Value", true, "SEWERAGE_TAX", "Wastewater sewerage charge", ReportTaxKeys.SEWERAGE_TAX, 9),
                    new ConsolidatedTaxes_MasterDto(null, "Sewerage Benefit Tax", "0.0", "Ratable Value", false, "SEWERAGE_BEN", "Sewerage infra benefit", ReportTaxKeys.SEWERAGE_BEN, 10),
                    new ConsolidatedTaxes_MasterDto(null, "Water Benefit Tax", "0.0", "Ratable Value", false, "WATER_BEN", "Water infra benefit", ReportTaxKeys.WATER_BEN, 11),
                    new ConsolidatedTaxes_MasterDto(null, "Street Tax", "0.0", "Ratable Value", false, "STREET_TAX", "Road and street maintenance tax", ReportTaxKeys.STREET_TAX, 12),
                    new ConsolidatedTaxes_MasterDto(null, "Special Conservancy Tax", "0.0", "Ratable Value", false, "SPEC_CONS", "Cleaning for large societies/markets", ReportTaxKeys.SPEC_CONS, 13),
                    new ConsolidatedTaxes_MasterDto(null, "Municipal Education Tax", "0.0", "Ratable Value", false, "MUNICIPAL_EDU", "Municipal education funding", ReportTaxKeys.MUNICIPAL_EDU, null),
                    new ConsolidatedTaxes_MasterDto(null, "Special Education Tax", "0.0", "Ratable Value", false, "SPECIAL_EDU", "Special education cess", ReportTaxKeys.SPECIAL_EDU, null),
                    new ConsolidatedTaxes_MasterDto(null, "Service Charges", "0.0", "Ratable Value", false, "SERVICE_CHG", "Other municipal services", ReportTaxKeys.SERVICE_CHG, 14),
                    new ConsolidatedTaxes_MasterDto(null, "Miscellaneous Charges", "0.0", "Ratable Value", false, "MISC_CHG", "Minor charges and miscellaneous recovery", ReportTaxKeys.MISC_CHG, 15),

                    // Reserved Flexible Taxes (all with 0.0)
                    new ConsolidatedTaxes_MasterDto(null, "Tax1", "0.0", "Ratable Value", false, "TAX1", "Flexible/Reserve tax slot 1", ReportTaxKeys.TAX1, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax2", "0.0", "Ratable Value", false, "TAX2", "Flexible/Reserve tax slot 2", ReportTaxKeys.TAX2, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax3", "0.0", "Ratable Value", false, "TAX3", "Flexible/Reserve tax slot 3", ReportTaxKeys.TAX3, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax4", "0.0", "Ratable Value", false, "TAX4", "Flexible/Reserve tax slot 4", ReportTaxKeys.TAX4, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax5", "0.0", "Ratable Value", false, "TAX5", "Flexible/Reserve tax slot 5", ReportTaxKeys.TAX5, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax6", "0.0", "Ratable Value", false, "TAX6", "Flexible/Reserve tax slot 6", ReportTaxKeys.TAX6, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax7", "0.0", "Ratable Value", false, "TAX7", "Flexible/Reserve tax slot 7", ReportTaxKeys.TAX7, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax8", "0.0", "Ratable Value", false, "TAX8", "Flexible/Reserve tax slot 8", ReportTaxKeys.TAX8, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax9", "0.0", "Ratable Value", false, "TAX9", "Flexible/Reserve tax slot 9", ReportTaxKeys.TAX9, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax10", "0.0", "Ratable Value", false, "TAX10", "Flexible/Reserve tax slot 10", ReportTaxKeys.TAX10, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax11", "0.0", "Ratable Value", false, "TAX11", "Flexible/Reserve tax slot 11", ReportTaxKeys.TAX11, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax12", "0.0", "Ratable Value", false, "TAX12", "Flexible/Reserve tax slot 12", ReportTaxKeys.TAX12, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax13", "0.0", "Ratable Value", false, "TAX13", "Flexible/Reserve tax slot 13", ReportTaxKeys.TAX13, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax14", "0.0", "Ratable Value", false, "TAX14", "Flexible/Reserve tax slot 14", ReportTaxKeys.TAX14, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax15", "0.0", "Ratable Value", false, "TAX15", "Flexible/Reserve tax slot 15", ReportTaxKeys.TAX15, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax16", "0.0", "Ratable Value", false, "TAX16", "Flexible/Reserve tax slot 16", ReportTaxKeys.TAX16, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax17", "0.0", "Ratable Value", false, "TAX17", "Flexible/Reserve tax slot 17", ReportTaxKeys.TAX17, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax18", "0.0", "Ratable Value", false, "TAX18", "Flexible/Reserve tax slot 18", ReportTaxKeys.TAX18, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax19", "0.0", "Ratable Value", false, "TAX19", "Flexible/Reserve tax slot 19", ReportTaxKeys.TAX19, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax20", "0.0", "Ratable Value", false, "TAX20", "Flexible/Reserve tax slot 20", ReportTaxKeys.TAX20, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax21", "0.0", "Ratable Value", false, "TAX21", "Flexible/Reserve tax slot 21", ReportTaxKeys.TAX21, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax22", "0.0", "Ratable Value", false, "TAX22", "Flexible/Reserve tax slot 22", ReportTaxKeys.TAX22, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax23", "0.0", "Ratable Value", false, "TAX23", "Flexible/Reserve tax slot 23", ReportTaxKeys.TAX23, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax24", "0.0", "Ratable Value", false, "TAX24", "Flexible/Reserve tax slot 24", ReportTaxKeys.TAX24, null),
                    new ConsolidatedTaxes_MasterDto(null, "Tax25", "0.0", "Ratable Value", false, "TAX25", "Flexible/Reserve tax slot 25", ReportTaxKeys.TAX25, null)
            );


            for (ConsolidatedTaxes_MasterDto tax : defaultTaxes) {
                ConsolidatedTaxes_MasterDto existing = consolidatedTaxes_MasterService.findByTaxName(tax.getTaxNameVc());

                if (existing != null) {
                    boolean updateRequired = false;


                    // ✅ Add taxKeyVc condition
                    if (existing.getTaxKeyL() == null) {
                        existing.setTaxKeyL(tax.getTaxKeyL());
                        updateRequired = true;
                    }


                    if (updateRequired) {
                        consolidatedTaxes_MasterService.updateTax(existing.getId(), existing);
                    }
                } else {
                    consolidatedTaxes_MasterService.createTax(tax);
                }
            }


//          These datasets below are getting used for inserting the taxes for each report view
            List<ReportTaxes_MasterDto> reportsTaxes = Arrays.asList(
                    // ===================== PROPERTY TAX =====================
                    new ReportTaxes_MasterDto(null, "Property Tax", "मालमत्ता कर",
                            1, ReportTaxKeys.PT_PARENT, null, true, true, "CALCULATION_SHEET", true),

                    // ===================== EDUCATION CESS =====================
                    new ReportTaxes_MasterDto(null, "Education Cess", "शिक्षण कर",
                            4, ReportTaxKeys.EDUC_PARENT, null, true, true, "CALCULATION_SHEET", true),

                    new ReportTaxes_MasterDto(null, "Residential", "निवासी",
                            5, ReportTaxKeys.EDUC_RES, ReportTaxKeys.EDUC_PARENT, true, true, "CALCULATION_SHEET", true),
                    new ReportTaxes_MasterDto(null, "Commercial", "अनिवासी",
                            6, ReportTaxKeys.EDUC_COMM, ReportTaxKeys.EDUC_PARENT, true, true, "CALCULATION_SHEET", true),

                    // ===================== STANDARD TAXES =====================
                    new ReportTaxes_MasterDto(null, "Employment Guarantee Cess", "रो.ह.यो कर",
                            7, ReportTaxKeys.EGC, null, true, true, "CALCULATION_SHEET", true),

                    new ReportTaxes_MasterDto(null, "Tree Tax", "वृक्ष कर",
                            8, ReportTaxKeys.TREE_TAX, null, true, true, "CALCULATION_SHEET", true),

                    new ReportTaxes_MasterDto(null, "Environment Tax", "पर्यावरण कर",
                            9, ReportTaxKeys.ENV_TAX, null, true, true, "CALCULATION_SHEET", true),

                    new ReportTaxes_MasterDto(null, "Cleanness Tax", "स्वच्छता कर",
                            10, ReportTaxKeys.CLEAN_TAX, null, true, true, "CALCULATION_SHEET", true),

                    new ReportTaxes_MasterDto(null, "Light Tax", "दिवाबत्ती कर",
                            11, ReportTaxKeys.LIGHT_TAX, null, true, true, "CALCULATION_SHEET", true),

                    new ReportTaxes_MasterDto(null, "Fire Tax", "अग्निशमन कर",
                            12, ReportTaxKeys.FIRE_TAX, null, true, true, "CALCULATION_SHEET", true),

                    new ReportTaxes_MasterDto(null, "Water Tax", "पाणी कर",
                            13, ReportTaxKeys.WATER_TAX, null, true, true, "CALCULATION_SHEET", false),

                    new ReportTaxes_MasterDto(null, "Sewerage Tax", "मलजल कर",
                            14, ReportTaxKeys.SEWERAGE_TAX, null, true, true, "CALCULATION_SHEET", false),

                    new ReportTaxes_MasterDto(null, "Sewerage Benefit Tax", "मलजल लाभ कर",
                            15, ReportTaxKeys.SEWERAGE_BEN, null, true, true, "CALCULATION_SHEET", false),

                    new ReportTaxes_MasterDto(null, "Water Benefit Tax", "पाणी लाभ कर",
                            16, ReportTaxKeys.WATER_BEN, null, true, true, "CALCULATION_SHEET", false),

                    new ReportTaxes_MasterDto(null, "Street Tax", "पथ कर",
                            17, ReportTaxKeys.STREET_TAX, null, true, true, "CALCULATION_SHEET", false),

                    new ReportTaxes_MasterDto(null, "Special Conservancy Tax", "विशेष सफाई कर",
                            18, ReportTaxKeys.SPEC_CONS, null, true, true, "CALCULATION_SHEET", false),

                    new ReportTaxes_MasterDto(null, "Municipal Education Tax", "मनपा शिक्षण उपकर",
                            19, ReportTaxKeys.MUNICIPAL_EDU, null, true, true, "CALCULATION_SHEET", false),

                    new ReportTaxes_MasterDto(null, "Special Education Tax", "विशेष शिक्षण कर",
                            20, ReportTaxKeys.SPECIAL_EDU, null, true, true, "CALCULATION_SHEET", false),

                    new ReportTaxes_MasterDto(null, "Service Charges", "सेवा शुल्क",
                            21, ReportTaxKeys.SERVICE_CHG, null, true, true, "CALCULATION_SHEET", false),

                    new ReportTaxes_MasterDto(null, "Miscellaneous Charges", "किरकोळ शुल्क",
                            22, ReportTaxKeys.MISC_CHG, null, true, true, "CALCULATION_SHEET", false),

                    // ===================== FLEXIBLE TAXES (Tax1..Tax25) =====================
                    new ReportTaxes_MasterDto(null, "Tax1", "कर १", 23, ReportTaxKeys.TAX1, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax2", "कर २", 24, ReportTaxKeys.TAX2, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax3", "कर ३", 25, ReportTaxKeys.TAX3, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax4", "कर ४", 26, ReportTaxKeys.TAX4, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax5", "कर ५", 27, ReportTaxKeys.TAX5, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax6", "कर ६", 28, ReportTaxKeys.TAX6, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax7", "कर ७", 29, ReportTaxKeys.TAX7, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax8", "कर ८", 30, ReportTaxKeys.TAX8, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax9", "कर ९", 31, ReportTaxKeys.TAX9, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax10", "कर १०", 32, ReportTaxKeys.TAX10, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax11", "कर ११", 33, ReportTaxKeys.TAX11, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax12", "कर १२", 34, ReportTaxKeys.TAX12, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax13", "कर १३", 35, ReportTaxKeys.TAX13, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax14", "कर १४", 36, ReportTaxKeys.TAX14, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax15", "कर १५", 37, ReportTaxKeys.TAX15, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax16", "कर १६", 38, ReportTaxKeys.TAX16, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax17", "कर १७", 39, ReportTaxKeys.TAX17, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax18", "कर १८", 40, ReportTaxKeys.TAX18, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax19", "कर १९", 41, ReportTaxKeys.TAX19, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax20", "कर २०", 42, ReportTaxKeys.TAX20, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax21", "कर २१", 43, ReportTaxKeys.TAX21, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax22", "कर २२", 44, ReportTaxKeys.TAX22, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax23", "कर २३", 45, ReportTaxKeys.TAX23, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax24", "कर २४", 46, ReportTaxKeys.TAX24, null, false, true, "CALCULATION_SHEET", false),
                    new ReportTaxes_MasterDto(null, "Tax25", "कर २५", 47, ReportTaxKeys.TAX25, null, false, true, "CALCULATION_SHEET", false),

                    // ===================== USER CHARGES =====================
                    new ReportTaxes_MasterDto(null, "User Charges", "उपयोगकर्ता शुल्क व इतर",
                            48, ReportTaxKeys.USER_CHG, null, true, true, "CALCULATION_SHEET", true),


                    // ===================== SPECIAL NOTICE =====================
                    new ReportTaxes_MasterDto(null, "Property Tax", "मालमत्ता कर",
                            1, ReportTaxKeys.PT_PARENT, null, true, true, "SPECIAL_NOTICE", true),

                    new ReportTaxes_MasterDto(null, "Education Cess", "शिक्षण कर",
                            4, ReportTaxKeys.EDUC_PARENT, null, true, true, "SPECIAL_NOTICE", true),

                    new ReportTaxes_MasterDto(null, "Residential", "निवासी",
                            5, ReportTaxKeys.EDUC_RES, ReportTaxKeys.EDUC_PARENT, false, false, "SPECIAL_NOTICE", true),
                    new ReportTaxes_MasterDto(null, "Commercial", "अनिवासी",
                            6, ReportTaxKeys.EDUC_COMM, ReportTaxKeys.EDUC_PARENT, false, false, "SPECIAL_NOTICE", true),

                    new ReportTaxes_MasterDto(null, "Employment Guarantee Cess", "रो.ह.यो कर",
                            7, ReportTaxKeys.EGC, null, true, true, "SPECIAL_NOTICE", true),

                    new ReportTaxes_MasterDto(null, "Tree Tax", "वृक्ष कर",
                            8, ReportTaxKeys.TREE_TAX, null, true, true, "SPECIAL_NOTICE", true),

                    new ReportTaxes_MasterDto(null, "Environment Tax", "पर्यावरण कर",
                            9, ReportTaxKeys.ENV_TAX, null, true, true, "SPECIAL_NOTICE", true),

                    new ReportTaxes_MasterDto(null, "Cleanness Tax", "स्वच्छता कर",
                            10, ReportTaxKeys.CLEAN_TAX, null, true, true, "SPECIAL_NOTICE", true),

                    new ReportTaxes_MasterDto(null, "Light Tax", "दिवाबत्ती कर",
                            11, ReportTaxKeys.LIGHT_TAX, null, true, true, "SPECIAL_NOTICE", true),

                    new ReportTaxes_MasterDto(null, "Fire Tax", "अग्निशमन कर",
                            12, ReportTaxKeys.FIRE_TAX, null, true, true, "SPECIAL_NOTICE", true),

                    new ReportTaxes_MasterDto(null, "Water Tax", "पाणी कर",
                            13, ReportTaxKeys.WATER_TAX, null, true, true, "SPECIAL_NOTICE", false),

                    new ReportTaxes_MasterDto(null, "Sewerage Tax", "मलजल कर",
                            14, ReportTaxKeys.SEWERAGE_TAX, null, true, true, "SPECIAL_NOTICE", false),

                    new ReportTaxes_MasterDto(null, "Sewerage Benefit Tax", "मलजल लाभ कर",
                            15, ReportTaxKeys.SEWERAGE_BEN, null, true, true, "SPECIAL_NOTICE", false),

                    new ReportTaxes_MasterDto(null, "Water Benefit Tax", "पाणी लाभ कर",
                            16, ReportTaxKeys.WATER_BEN, null, true, true, "SPECIAL_NOTICE", false),

                    new ReportTaxes_MasterDto(null, "Street Tax", "पथ कर",
                            17, ReportTaxKeys.STREET_TAX, null, true, true, "SPECIAL_NOTICE", false),

                    new ReportTaxes_MasterDto(null, "Special Conservancy Tax", "विशेष सफाई कर",
                            18, ReportTaxKeys.SPEC_CONS, null, true, true, "SPECIAL_NOTICE", false),

                    new ReportTaxes_MasterDto(null, "Municipal Education Tax", "मनपा शिक्षण उपकर",
                            19, ReportTaxKeys.MUNICIPAL_EDU, null, true, true, "SPECIAL_NOTICE", false),

                    new ReportTaxes_MasterDto(null, "Special Education Tax", "विशेष शिक्षण कर",
                            20, ReportTaxKeys.SPECIAL_EDU, null, true, true, "SPECIAL_NOTICE", false),

                    new ReportTaxes_MasterDto(null, "Service Charges", "सेवा शुल्क",
                            21, ReportTaxKeys.SERVICE_CHG, null, true, true, "SPECIAL_NOTICE", false),

                    new ReportTaxes_MasterDto(null, "Miscellaneous Charges", "किरकोळ शुल्क",
                            22, ReportTaxKeys.MISC_CHG, null, true, true, "SPECIAL_NOTICE", false),
                    // ---- Flexible Reserved Taxes ----
                    new ReportTaxes_MasterDto(null, "Tax1", "कर १", 23, ReportTaxKeys.TAX1, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax2", "कर २", 24, ReportTaxKeys.TAX2, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax3", "कर ३", 25, ReportTaxKeys.TAX3, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax4", "कर ४", 26, ReportTaxKeys.TAX4, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax5", "कर ५", 27, ReportTaxKeys.TAX5, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax6", "कर ६", 28, ReportTaxKeys.TAX6, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax7", "कर ७", 29, ReportTaxKeys.TAX7, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax8", "कर ८", 30, ReportTaxKeys.TAX8, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax9", "कर ९", 31, ReportTaxKeys.TAX9, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax10", "कर १०", 32, ReportTaxKeys.TAX10, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax11", "कर ११", 33, ReportTaxKeys.TAX11, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax12", "कर १२", 34, ReportTaxKeys.TAX12, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax13", "कर १३", 35, ReportTaxKeys.TAX13, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax14", "कर १४", 36, ReportTaxKeys.TAX14, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax15", "कर १५", 37, ReportTaxKeys.TAX15, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax16", "कर १६", 38, ReportTaxKeys.TAX16, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax17", "कर १७", 39, ReportTaxKeys.TAX17, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax18", "कर १८", 40, ReportTaxKeys.TAX18, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax19", "कर १९", 41, ReportTaxKeys.TAX19, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax20", "कर २०", 42, ReportTaxKeys.TAX20, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax21", "कर २१", 43, ReportTaxKeys.TAX21, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax22", "कर २२", 44, ReportTaxKeys.TAX22, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax23", "कर २३", 45, ReportTaxKeys.TAX23, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax24", "कर २४", 46, ReportTaxKeys.TAX24, null, false, true, "SPECIAL_NOTICE", false),
                    new ReportTaxes_MasterDto(null, "Tax25", "कर २५", 47, ReportTaxKeys.TAX25, null, false, true, "SPECIAL_NOTICE", false),

                    // ---- User Charges ----
                    new ReportTaxes_MasterDto(null, "User Charges", "वापर शुल्क",
                            48, ReportTaxKeys.USER_CHG, null, true, true, "SPECIAL_NOTICE", true),


                    //-- ASSESSMENTS_REGISTER

                    new ReportTaxes_MasterDto(null, "Property Tax", "मालमत्ता कर",
                            1, ReportTaxKeys.PT_PARENT, null, true, true, "ASSESSMENT_REGISTER", true),

                    // ===================== EDUCATION CESS =====================
                    new ReportTaxes_MasterDto(null, "Education Cess", "शिक्षण कर",
                            4, ReportTaxKeys.EDUC_PARENT, null, true, true, "ASSESSMENT_REGISTER", true),

                    new ReportTaxes_MasterDto(null, "Residential", "निवासी",
                            5, ReportTaxKeys.EDUC_RES, ReportTaxKeys.EDUC_PARENT, true, true, "ASSESSMENT_REGISTER", true),
                    new ReportTaxes_MasterDto(null, "Commercial", "अनिवासी",
                            6, ReportTaxKeys.EDUC_COMM, ReportTaxKeys.EDUC_PARENT, true, true, "ASSESSMENT_REGISTER", true),

                    // ===================== STANDARD TAXES =====================
                    new ReportTaxes_MasterDto(null, "Employment Guarantee Cess", "रो.ह.यो कर",
                            7, ReportTaxKeys.EGC, null, true, true, "ASSESSMENT_REGISTER", true),

                    new ReportTaxes_MasterDto(null, "Tree Tax", "वृक्ष कर",
                            8, ReportTaxKeys.TREE_TAX, null, true, true, "ASSESSMENT_REGISTER", true),

                    new ReportTaxes_MasterDto(null, "Environment Tax", "पर्यावरण कर",
                            9, ReportTaxKeys.ENV_TAX, null, true, true, "ASSESSMENT_REGISTER", true),

                    new ReportTaxes_MasterDto(null, "Cleanness Tax", "स्वच्छता कर",
                            10, ReportTaxKeys.CLEAN_TAX, null, true, true, "ASSESSMENT_REGISTER", true),

                    new ReportTaxes_MasterDto(null, "Light Tax", "दिवाबत्ती कर",
                            11, ReportTaxKeys.LIGHT_TAX, null, true, true, "ASSESSMENT_REGISTER", true),

                    new ReportTaxes_MasterDto(null, "Fire Tax", "अग्निशमन कर",
                            12, ReportTaxKeys.FIRE_TAX, null, true, true, "ASSESSMENT_REGISTER", true),

                    new ReportTaxes_MasterDto(null, "Water Tax", "पाणी कर",
                            13, ReportTaxKeys.WATER_TAX, null, true, true, "ASSESSMENT_REGISTER", false),

                    new ReportTaxes_MasterDto(null, "Sewerage Tax", "मलजल कर",
                            14, ReportTaxKeys.SEWERAGE_TAX, null, true, true, "ASSESSMENT_REGISTER", false),

                    new ReportTaxes_MasterDto(null, "Sewerage Benefit Tax", "मलजल लाभ कर",
                            15, ReportTaxKeys.SEWERAGE_BEN, null, true, true, "ASSESSMENT_REGISTER", false),

                    new ReportTaxes_MasterDto(null, "Water Benefit Tax", "पाणी लाभ कर",
                            16, ReportTaxKeys.WATER_BEN, null, true, true, "ASSESSMENT_REGISTER", false),

                    new ReportTaxes_MasterDto(null, "Street Tax", "पथ कर",
                            17, ReportTaxKeys.STREET_TAX, null, true, true, "ASSESSMENT_REGISTER", false),

                    new ReportTaxes_MasterDto(null, "Special Conservancy Tax", "विशेष सफाई कर",
                            18, ReportTaxKeys.SPEC_CONS, null, true, true, "ASSESSMENT_REGISTER", false),

                    new ReportTaxes_MasterDto(null, "Municipal Education Tax", "मनपा शिक्षण उपकर",
                            19, ReportTaxKeys.MUNICIPAL_EDU, null, true, true, "ASSESSMENT_REGISTER", false),

                    new ReportTaxes_MasterDto(null, "Special Education Tax", "विशेष शिक्षण कर",
                            20, ReportTaxKeys.SPECIAL_EDU, null, true, true, "ASSESSMENT_REGISTER", false),

                    new ReportTaxes_MasterDto(null, "Service Charges", "सेवा शुल्क",
                            21, ReportTaxKeys.SERVICE_CHG, null, true, true, "ASSESSMENT_REGISTER", false),

                    new ReportTaxes_MasterDto(null, "Miscellaneous Charges", "किरकोळ शुल्क",
                            22, ReportTaxKeys.MISC_CHG, null, true, true, "ASSESSMENT_REGISTER", false),

                    // ===================== FLEXIBLE TAXES (Tax1..Tax25) =====================
                    new ReportTaxes_MasterDto(null, "Tax1", "कर १", 23, ReportTaxKeys.TAX1, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax2", "कर २", 24, ReportTaxKeys.TAX2, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax3", "कर ३", 25, ReportTaxKeys.TAX3, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax4", "कर ४", 26, ReportTaxKeys.TAX4, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax5", "कर ५", 27, ReportTaxKeys.TAX5, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax6", "कर ६", 28, ReportTaxKeys.TAX6, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax7", "कर ७", 29, ReportTaxKeys.TAX7, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax8", "कर ८", 30, ReportTaxKeys.TAX8, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax9", "कर ९", 31, ReportTaxKeys.TAX9, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax10", "कर १०", 32, ReportTaxKeys.TAX10, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax11", "कर ११", 33, ReportTaxKeys.TAX11, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax12", "कर १२", 34, ReportTaxKeys.TAX12, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax13", "कर १३", 35, ReportTaxKeys.TAX13, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax14", "कर १४", 36, ReportTaxKeys.TAX14, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax15", "कर १५", 37, ReportTaxKeys.TAX15, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax16", "कर १६", 38, ReportTaxKeys.TAX16, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax17", "कर १७", 39, ReportTaxKeys.TAX17, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax18", "कर १८", 40, ReportTaxKeys.TAX18, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax19", "कर १९", 41, ReportTaxKeys.TAX19, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax20", "कर २०", 42, ReportTaxKeys.TAX20, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax21", "कर २१", 43, ReportTaxKeys.TAX21, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax22", "कर २२", 44, ReportTaxKeys.TAX22, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax23", "कर २३", 45, ReportTaxKeys.TAX23, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax24", "कर २४", 46, ReportTaxKeys.TAX24, null, false, true, "ASSESSMENT_REGISTER", false),
                    new ReportTaxes_MasterDto(null, "Tax25", "कर २५", 47, ReportTaxKeys.TAX25, null, false, true, "ASSESSMENT_REGISTER", false),

                    // ===================== USER CHARGES =====================
                    new ReportTaxes_MasterDto(null, "User Charges", "उपयोगकर्ता शुल्क व इतर",
                            48, ReportTaxKeys.USER_CHG, null, true, true, "ASSESSMENT_REGISTER", true),

                    // ===================== PROPERTY TAX =====================
                    new ReportTaxes_MasterDto(null, "Property Tax", "एकत्रित मालमत्ता कर",
                            1, ReportTaxKeys.PT_PARENT, null, true, true, "TAX_BILL", true),

                    // ===================== EDUCATION CESS =====================
                    new ReportTaxes_MasterDto(null, "Education Cess", "शिक्षण कर",
                            2, ReportTaxKeys.EDUC_PARENT, null, true, true, "TAX_BILL", true),

                    new ReportTaxes_MasterDto(null, "Residential", "निवासी",
                            3, ReportTaxKeys.EDUC_RES, ReportTaxKeys.EDUC_PARENT, true, true, "TAX_BILL", false),

                    new ReportTaxes_MasterDto(null, "Commercial", "अनिवासी",
                            4, ReportTaxKeys.EDUC_COMM, ReportTaxKeys.EDUC_PARENT, true, true, "TAX_BILL", false),

                    // ===================== STANDARD TAXES =====================
                    new ReportTaxes_MasterDto(null, "Employment Guarantee Cess", "रोजगार हमी योजन कर",
                            5, ReportTaxKeys.EGC, null, true, true, "TAX_BILL", true),

                    new ReportTaxes_MasterDto(null, "Tree Tax", "वृक्ष कर",
                            6, ReportTaxKeys.TREE_TAX, null, true, true, "TAX_BILL", true),

                    new ReportTaxes_MasterDto(null, "Environment Tax", "पर्यावरण कर",
                            7, ReportTaxKeys.ENV_TAX, null, true, true, "TAX_BILL", true),

                    new ReportTaxes_MasterDto(null, "Cleanness Tax", "स्वच्छता कर",
                            8, ReportTaxKeys.CLEAN_TAX, null, true, true, "TAX_BILL", true),

                    new ReportTaxes_MasterDto(null, "Light Tax", "दिवाबत्ती कर",
                            9, ReportTaxKeys.LIGHT_TAX, null, true, true, "TAX_BILL", true),

                    new ReportTaxes_MasterDto(null, "Fire Tax", "अग्निशमन कर",
                            10, ReportTaxKeys.FIRE_TAX, null, true, true, "TAX_BILL", true),

                    // ===================== WATER & SEWERAGE TAXES =====================
                    new ReportTaxes_MasterDto(null, "Water Tax", "पाणी कर",
                            11, ReportTaxKeys.WATER_TAX, null, true, true, "TAX_BILL", false),

                    new ReportTaxes_MasterDto(null, "Sewerage Tax", "मलजल कर",
                            12, ReportTaxKeys.SEWERAGE_TAX, null, true, true, "TAX_BILL", false),

                    new ReportTaxes_MasterDto(null, "Sewerage Benefit Tax", "मलजल लाभ कर",
                            13, ReportTaxKeys.SEWERAGE_BEN, null, true, true, "TAX_BILL", false),

                    new ReportTaxes_MasterDto(null, "Water Benefit Tax", "पाणी लाभ कर",
                            14, ReportTaxKeys.WATER_BEN, null, true, true, "TAX_BILL", false),

                    // ===================== STREET & SPECIAL TAXES =====================
                    new ReportTaxes_MasterDto(null, "Street Tax", "पथ कर",
                            15, ReportTaxKeys.STREET_TAX, null, true, true, "TAX_BILL", false),

                    new ReportTaxes_MasterDto(null, "Special Conservancy Tax", "विशेष सफाई कर",
                            16, ReportTaxKeys.SPEC_CONS, null, true, true, "TAX_BILL", false),

                    // ===================== EDUCATIONAL TAX VARIANTS =====================
                    new ReportTaxes_MasterDto(null, "Municipal Education Tax", "मनपा शिक्षण उपकर",
                            17, ReportTaxKeys.MUNICIPAL_EDU, null, true, true, "TAX_BILL", false),

                    new ReportTaxes_MasterDto(null, "Special Education Tax", "विशेष शिक्षण कर",
                            18, ReportTaxKeys.SPECIAL_EDU, null, true, true, "TAX_BILL", false),

                    // ===================== SERVICE & MISC CHARGES =====================
                    new ReportTaxes_MasterDto(null, "Service Charges", "सेवा शुल्क",
                            19, ReportTaxKeys.SERVICE_CHG, null, true, true, "TAX_BILL", false),

                    new ReportTaxes_MasterDto(null, "Miscellaneous Charges", "किरकोळ शुल्क",
                            20, ReportTaxKeys.MISC_CHG, null, true, true, "TAX_BILL", false),

                    // ===================== FLEXIBLE TAXES (Tax1–Tax25) =====================
                    new ReportTaxes_MasterDto(null, "Tax1", "कर १", 21, ReportTaxKeys.TAX1, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax2", "कर २", 22, ReportTaxKeys.TAX2, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax3", "कर ३", 23, ReportTaxKeys.TAX3, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax4", "कर ४", 24, ReportTaxKeys.TAX4, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax5", "कर ५", 25, ReportTaxKeys.TAX5, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax6", "कर ६", 26, ReportTaxKeys.TAX6, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax7", "कर ७", 27, ReportTaxKeys.TAX7, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax8", "कर ८", 28, ReportTaxKeys.TAX8, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax9", "कर ९", 29, ReportTaxKeys.TAX9, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax10", "कर १०", 30, ReportTaxKeys.TAX10, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax11", "कर ११", 31, ReportTaxKeys.TAX11, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax12", "कर १२", 32, ReportTaxKeys.TAX12, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax13", "कर १३", 33, ReportTaxKeys.TAX13, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax14", "कर १४", 34, ReportTaxKeys.TAX14, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax15", "कर १५", 35, ReportTaxKeys.TAX15, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax16", "कर १६", 36, ReportTaxKeys.TAX16, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax17", "कर १७", 37, ReportTaxKeys.TAX17, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax18", "कर १८", 38, ReportTaxKeys.TAX18, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax19", "कर १९", 39, ReportTaxKeys.TAX19, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax20", "कर २०", 40, ReportTaxKeys.TAX20, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax21", "कर २१", 41, ReportTaxKeys.TAX21, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax22", "कर २२", 42, ReportTaxKeys.TAX22, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax23", "कर २३", 43, ReportTaxKeys.TAX23, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax24", "कर २४", 44, ReportTaxKeys.TAX24, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Tax25", "कर २५", 45, ReportTaxKeys.TAX25, null, false, true, "TAX_BILL", false),
                    new ReportTaxes_MasterDto(null, "Penalty", "शास्ती", 49, ReportTaxKeys.PENALTY, null, false, true, "TAX_BILL", true),
                    // ===================== USER CHARGES =====================
                    new ReportTaxes_MasterDto(null, "User Charges", "उपयोगकर्ता शुल्क व इतर",
                            46, ReportTaxKeys.USER_CHG, null, true, true, "TAX_BILL", true)


                    );

            if (!reportsTaxes.isEmpty()) {
                // Only insert defaults for missing tax keys; do not update existing user-edited rows
                reportTaxesConfigService.insertMissingOnly(reportsTaxes);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Initialization failed: " + e.getMessage());
        }
    }
}
