package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class CouncilDetails_MasterDto {

    private Long id;
    private String standardName;
    private String localName;
    private String imageBase64; // Primary council image Base64
    private String image2Base64; // Secondary council image Base64
    private String qrImageBase64; // QR image Base64

    // Field created by Himanshu for standardization of site and district and localization of sites and district

    private String standardSiteNameVC;
    private  String localSiteNameVC;
    private  String standardDistrictNameVC;
    private  String localDistrictNameVC;

    // Additional images
    private String chiefOfficerSignBase64;
    private String companySignBase64;
}
