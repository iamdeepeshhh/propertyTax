package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.CouncilDetails_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CouncilDetails_MasterEntity {

    @Id
    private Long id;

    @Column(name = "standardname", nullable = false)
    private String standardName;

    @Column(name = "localname")
    private String localName;

    @Column(name = "image_base64", columnDefinition = "TEXT")
    private String imageBase64; // Store the image as a Base64 string

    @Column(name = "image2_base64", columnDefinition = "TEXT")
    private String image2Base64; // Secondary council image

    @Column(name = "qr_image_base64", columnDefinition = "TEXT")
    private String qrImageBase64; // QR image

    @Column(name = "standardsitenameVC")
    private String standardSiteNameVC;

    @Column(name = "localsitenameVC")
    private  String localSiteNameVC;

    @Column(name = "standarddistrictnameVC")
    private  String standardDistrictNameVC;

    @Column(name = "localdistrictnameVC")
    private  String localDistrictNameVC;

    @Column(name = "chief_officer_sign_base64", columnDefinition = "TEXT")
    private String chiefOfficerSignBase64;

    @Column(name = "company_sign_base64", columnDefinition = "TEXT")
    private String companySignBase64;

}
