package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RVTypes_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "rvtypes_master")
public class RVTypes_MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rvTypeId;
    private String typeNameVc;
    private String rateFl;
    @Column(name = "applied_taxes_vc", columnDefinition = "TEXT")
    private String appliedTaxesVc;
    private String descriptionVc;
    @ManyToOne(fetch = FetchType.LAZY, optional = true) // optional=true allows for null category
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = true)
    private RVTypeCategory_MasterEntity category; // before we considering to set the pr values according to ratetype id's
                            // but as sometimes rate are changed for ype but will be setted in same pr values
}
