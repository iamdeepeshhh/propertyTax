package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.EduCessAndEmpCess_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="cess_master")
public class EduCessAndEmpCess_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String minTaxableValueFl;
    private String maxTaxableValueFl;
    private String residentialRateFl;
    private String commercialRateFl;
    private String egcRateFl;

}
