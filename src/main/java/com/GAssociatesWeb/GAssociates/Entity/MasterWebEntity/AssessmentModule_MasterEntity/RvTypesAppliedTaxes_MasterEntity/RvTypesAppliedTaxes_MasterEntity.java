package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RvTypesAppliedTaxes_MasterEntity;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.ConsolidatedTaxes_MasterEntity.ConsolidatedTaxes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RVTypes_MasterEntity.RVTypes_MasterEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "rvtypes_applied_taxes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"rvtype_id", "tax_id"})
})
public class RvTypesAppliedTaxes_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rvtype_id", nullable = false)
    private Long rvtypeId;

    @Column(name = "tax_keyl", nullable = false)
    private Long taxKeyL;

    // ✅ Fetch full tax details via relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_keyl", insertable = false, updatable = false)
    private ConsolidatedTaxes_MasterEntity tax;

    // ✅ Fetch full rate type details via relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rvtype_id", insertable = false, updatable = false)
    private RVTypes_MasterEntity rvType;

    @Column(name = "created_dt")
    private LocalDateTime createdDt;

    @Column(name = "updated_dt")
    private LocalDateTime updatedDt;
}
