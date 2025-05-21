package com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository;

import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.AdditionalInfo_Entity.AdditionalInfo_Entity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalInfo_Repository extends JpaRepository<AdditionalInfo_Entity, Integer> {
    // You can define custom query methods here if needed
}
