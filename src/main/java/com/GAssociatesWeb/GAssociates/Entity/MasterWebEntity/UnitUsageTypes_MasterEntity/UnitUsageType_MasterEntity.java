package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropUsageType_MasterEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "unitusage_master")
@Data
public class UnitUsageType_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uum_usageid_i;

    @Column(name = "uum_usagetypeeng_vc", length = 150)
    private String uum_usagetypeeng_vc;

    @Column(name = "uum_usagetypell_vc", nullable = false, length = 150)
    private String uum_usagetypell_vc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pum_usageid_i", nullable = false, foreignKey = @ForeignKey(name = "unitusage_master_fkey"))
    private PropUsageType_MasterEntity propUsageTypeMaster;

    @Column(name = "uum_created_dt")
    private LocalDateTime uum_created_dt;

    @Column(name = "uum_updated_dt")
    private LocalDateTime uum_updated_dt;

    @Column(name = "uum_abbr_vc")
    private String uum_abbr_vc;

    @Column(name = "uum_type_vc")
    private String uum_type_vc;

    @Column(name = "uum_rvtype_vc")
    private String uum_rvtype_vc;

}