package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxDepreciation_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "taxdepreciation_master")
@Data
public class TaxDepreciation_MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String constructionClassVc;
    private String minAgeI;
    private String maxAgeI;
    private String depreciationPercentageI;
}
