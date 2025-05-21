package com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository;

import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDeletionLog_Entity.PropertyDeletionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyDeletionLog_Repository extends JpaRepository<PropertyDeletionLog, Long> {
    List<PropertyDeletionLog> findAll();
}