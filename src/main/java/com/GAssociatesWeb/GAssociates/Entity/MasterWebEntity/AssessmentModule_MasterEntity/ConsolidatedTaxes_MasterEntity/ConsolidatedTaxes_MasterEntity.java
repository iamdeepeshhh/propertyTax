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

    private Boolean isActiveBl = true;
//    private String taxNameLocalVc;//for Local language
//    private String taxNameStandardVc;//for Standard generic language

//    @Enumerated(EnumType.STRING)
//    private TaxTypeEnum taxType;

    private String descriptionVc;//optional field for giving tax description
    private Long taxKeyL;//
//    private Integer positionI;
}
