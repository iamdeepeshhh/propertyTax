package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class CouncilDetails_MasterDto {

    private Long id;
    private String standardName;
    private String localName;
    private String imageBase64; // Store the image as a Base64 string

    // Field created by Himanshu for standardization of site and district and localization of sites and district

    private String standardSiteNameVC;
    private  String localSiteNameVC;
    private  String standardDistrictNameVC;
    private  String localDistrictNameVC;
}
