package com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.UnitDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitDetails_Entity.UnitDetailsId;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitDetails_Entity.UnitDetails_Entity;
import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitDetails_Repository extends JpaRepository<UnitDetails_Entity, Long> {
    // List or other return types for custom queries based on unit details
    UnitDetails_Entity findByPdNewpropertynoVc(String pdNewpropertynoVc);

    void deleteByPdNewpropertynoVc(String pdNewpropertynoVc);

    List<UnitDetails_Entity> findAllByPdNewpropertynoVc(String pdNewpropertynoVc);

    // Correcting the method name by ensuring proper camelCase and consistent naming conventions
    Optional<UnitDetails_Entity> findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(
            String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc);


    ///Dto conversion is needed
}