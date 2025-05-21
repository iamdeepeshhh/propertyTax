package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "propertyusagesub_master")
@Data
public class PropUsageSubType_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pusm_usagesubid_i")
    private Integer id;

    @Column(name = "pusm_usagetypeeng_vc", nullable = false)
    private String usageTypeEng;

    @Column(name = "pusm_usagetypell_vc", nullable = false)
    private String usageTypeLocal;

    @Column(name = "pusm_usageid_i")
    private Integer usageId;

    @Column(name = "pusm_created_dt")
    private LocalDateTime createdDate;

    @Column(name = "pusm_updated_dt")
    private LocalDateTime updatedDate;

    @Column(name = "pusm_taxtype_vc")
    private String taxType;

    // Getters and Setters
}