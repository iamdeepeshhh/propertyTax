package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_UnitDetailsEntity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitDetails_Entity.UnitDetails_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AfterHearingUnitDetails_MasterRepository extends JpaRepository<AfterHearing_UnitDetailsEntity, Integer> {

    AfterHearing_UnitDetailsEntity findByPdNewpropertynoVc(String pdNewpropertynoVc);

    void deleteByPdNewpropertynoVc(String pdNewpropertynoVc);

    List<AfterHearing_UnitDetailsEntity> findAllByPdNewpropertynoVc(String pdNewpropertynoVc);

    // Correcting the method name by ensuring proper camelCase and consistent naming conventions
    Optional<AfterHearing_UnitDetailsEntity> findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(
            String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc);


}
