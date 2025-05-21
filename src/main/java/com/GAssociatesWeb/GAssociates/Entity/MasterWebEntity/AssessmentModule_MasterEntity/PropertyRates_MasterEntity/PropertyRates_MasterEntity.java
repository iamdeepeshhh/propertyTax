package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.PropertyRates_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "property_rates_master")
@Data
public class PropertyRates_MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String constructionTypeVc;
    private String taxRateZoneI;
    private String rateI;
}