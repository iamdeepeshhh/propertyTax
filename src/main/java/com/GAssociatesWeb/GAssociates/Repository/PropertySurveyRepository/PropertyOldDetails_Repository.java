package com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyOldDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyOldDetails_Entity.PropertyOldDetails_Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyOldDetails_Repository extends JpaRepository<PropertyOldDetails_Entity, Integer> {
    Optional<PropertyOldDetails_Entity> findByPodOldPropNoVcAndPodWardI(String podOldPropNoVc,String PodWardI);
    List<PropertyOldDetails_Entity> findByPodOldPropNoVcContaining(String oldPropNo);
    void delete(PropertyOldDetails_Entity entity);

    // Custom query methods for searching by owner name and ward number
    List<PropertyOldDetails_Entity> findByPodOwnerNameVcContainingIgnoreCaseAndPodWardI(String ownerName, String wardNo);

    // Query methods for searching by old property number
    List<PropertyOldDetails_Entity> findByPodOldPropNoVcContainingIgnoreCase(String oldPropertyNo);

    // Query methods for searching by owner name
    List<PropertyOldDetails_Entity> findByPodOwnerNameVcContainingIgnoreCase(String ownerName);

    // Query methods for searching by ward number
    List<PropertyOldDetails_Entity> findByPodWardI(String wardNo);
    //query methods to find property by refno
    Optional<PropertyOldDetails_Entity> findByPodRefNoVc(Integer podRefNoVc);

}