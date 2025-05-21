package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.ConsolidatedTaxes_MasterDto.ConsolidatedTaxes_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto.RVTypeCategory_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto.RVTypes_MasterDto;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.ConolidatedTaxes_MasterService.ConsolidatedTaxes_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.RVTypes_MasterService.RVTypeCategory_MasterService.RVTypeCategory_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.RVTypes_MasterService.RVTypes_MasterService;
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

            if (consolidatedTaxes_MasterService.getAllTaxes().isEmpty()) {
                List<ConsolidatedTaxes_MasterDto> defaultTaxes = Arrays.asList(
                        new ConsolidatedTaxes_MasterDto(null, "Tree Tax", "1.0", "Ratable Value"),
                        new ConsolidatedTaxes_MasterDto(null, "Environment Tax", "1.0", "Ratable Value"),
                        new ConsolidatedTaxes_MasterDto(null, "Cleanness Tax", "1.0", "Ratable Value"),
                        new ConsolidatedTaxes_MasterDto(null, "Light Tax", "1.0", "Ratable Value"),
                        new ConsolidatedTaxes_MasterDto(null, "Fire Tax", "2.0", "Ratable Value"),
                        new ConsolidatedTaxes_MasterDto(null, "Property Tax", "2.0", "Ratable Value"),
                        new ConsolidatedTaxes_MasterDto(null, "Property Tax II", "2.0", "Ratable Value")
                );

                for (ConsolidatedTaxes_MasterDto tax : defaultTaxes) {
                    consolidatedTaxes_MasterService.createTax(tax);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            System.err.println("Initialization failed: " + e.getMessage());
        }
    }
}