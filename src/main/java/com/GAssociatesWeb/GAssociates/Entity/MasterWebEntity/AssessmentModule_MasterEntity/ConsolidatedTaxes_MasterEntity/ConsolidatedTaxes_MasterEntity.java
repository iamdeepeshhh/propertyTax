package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.ConsolidatedTaxes_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "consolidated_taxes_master")
public class ConsolidatedTaxes_MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taxNameVc;
    private String taxRateFl;
    private String applicableonVc;
}
