package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "propertyusage_master")
@Data
public class PropUsageType_MasterEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "pum_usageid_i")
        private Integer id;

        @ManyToOne(fetch = FetchType.EAGER)//add lazy after testing in place of eager
        @JoinColumn(name = "psm_subclassid_i", nullable = false)
        private PropSubclassification_MasterEntity propertySubclassificationMaster;

        @Column(name = "pum_usagetypeeng_vc", nullable = false, length = 150)
        private String usageTypeEng;

        @Column(name = "pum_usagetypell_vc", nullable = false, length = 150)
        private String usageTypeLl;

        @Column(name = "pum_created_dt")
        private LocalDateTime createdDate;

        @Column(name = "pum_updated_dt")
        private LocalDateTime updatedDate;

        @Column(name = "pum_updated")
        private String updatedBy;

}



