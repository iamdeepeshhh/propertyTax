package com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository;

import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity.PropertyDetails_Entity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PropertyDetails_Repository extends JpaRepository<PropertyDetails_Entity, String> {
    // Here, Long is assumed as the ID type. Adjust if necessary.

    // Add custom queries if needed, e.g., findByPdNewpropertynoVc
    Optional<PropertyDetails_Entity> findBypdNewpropertynoVc(String pdNewpropertynoVc);
    Optional<PropertyDetails_Entity> findBypdSurypropnoVc(String pdSurypropnoVc);
    void deleteByPdNewpropertynoVc(String pdNewpropertynoVc);
    Optional<PropertyDetails_Entity> findByPdSurypropnoVcAndPdWardI(String surveyPropertyNo, Integer wardNo);
    List<PropertyDetails_Entity> findByPdOwnernameVcContainingIgnoreCaseAndPdWardI(String ownerName, Integer wardNo);
    List<PropertyDetails_Entity> findByPdSurypropnoVcContainingIgnoreCase(String surveyPropertyNo);
    List<PropertyDetails_Entity> findByPdOwnernameVcContainingIgnoreCase(String ownerName);
    List<PropertyDetails_Entity> findByPdWardI(Integer wardNo);

    List<PropertyDetails_Entity> findAllByPdWardI(int wardNumber, Pageable pageable);

    List<PropertyDetails_Entity> findByPdFinalpropnoVcAndPdWardI(String pdFinalpropnoVc, Integer pdWardI);
    @Query("SELECT p.pdWardI, COUNT(p) FROM PropertyDetails_Entity p GROUP BY p.pdWardI ORDER BY p.pdWardI")
    List<Object[]> getWardWiseCount();

    List<PropertyDetails_Entity> findByPdFinalpropnoVcContainingIgnoreCase(String finalPropertyNo);
}
