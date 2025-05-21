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

    @Column(name = "standardsitenameVC")
    private String standardSiteNameVC;

    @Column(name = "localsitenameVC")
    private  String localSiteNameVC;

    @Column(name = "standarddistrictnameVC")
    private  String standardDistrictNameVC;

    @Column(name = "localdistrictnameVC")
    private  String localDistrictNameVC;

}
