package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "unitusagesub_master")
@Data
public class UnitUsageSubType_MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer usm_usagesubid_i;

    @Column(name = "usm_usagetypeeng_vc", length = 150)
    private String usm_usagetypeeng_vc;

    @Column(name = "usm_usagetypell_vc", nullable = false, length = 150)
    private String usm_usagetypell_vc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uum_usageid_i", referencedColumnName = "uum_usageid_i", nullable = false, foreignKey = @ForeignKey(name = "unitusagesub_master_fkey"))
    private UnitUsageType_MasterEntity unitUsageMaster; // Corrected field name

    @Column(name = "usm_created_dt")
    private LocalDateTime usm_created_dt;

    @Column(name = "usm_updated_dt")
    private LocalDateTime usm_updated_dt;

    @Column(name = "usm_taxtype_i")
    private Integer usm_taxtype_i;

    @Column(name = "usm_applydifferentrate_vc", nullable = true)  // Assuming you want to allow nulls
    private Boolean usmApplyDifferentRateVc;

    @Column(name = "usm_rvtype_vc")
    private String usm_rvtype_vc;

    @Column(name = "usm_usercharges_i")
    private Integer usm_usercharges_i;
}
