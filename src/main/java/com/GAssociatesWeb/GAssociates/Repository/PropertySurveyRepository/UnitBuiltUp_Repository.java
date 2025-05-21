package com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository;

import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitBuiltUp_Entity.UnitBuiltUp_Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitBuiltUp_Repository extends JpaRepository<UnitBuiltUp_Entity, Long> {
    // Queries to fetch built-up details by unit or property identifiers
    List<UnitBuiltUp_Entity> findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(String pdNewpropertynoVc,String UdFloornoVc, int udUnitnoVc);
    Optional<UnitBuiltUp_Entity> findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVcAndUbIdsI(
            String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc, Integer ubIdsI);
    List<UnitBuiltUp_Entity> findByPdNewpropertynoVc(String pdNewpropertynoVc);
    void deleteByPdNewpropertynoVc(String pdNewpropertynoVc);
    int countByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc);

}