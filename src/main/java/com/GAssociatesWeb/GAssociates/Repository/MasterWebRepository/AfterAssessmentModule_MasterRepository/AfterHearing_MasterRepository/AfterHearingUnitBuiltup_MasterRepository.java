package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_UnitBuiltupDetailsEntity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.UnitBuiltUp_Entity.UnitBuiltUp_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AfterHearingUnitBuiltup_MasterRepository extends JpaRepository<AfterHearing_UnitBuiltupDetailsEntity, Integer> {
    List<AfterHearing_UnitBuiltupDetailsEntity> findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(String pdNewpropertynoVc, String UdFloornoVc, int udUnitnoVc);
    Optional<AfterHearing_UnitBuiltupDetailsEntity> findByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVcAndUbIdsI(
            String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc, Integer ubIdsI);
    List<AfterHearing_UnitBuiltupDetailsEntity> findByPdNewpropertynoVc(String pdNewpropertynoVc);
    void deleteByPdNewpropertynoVc(String pdNewpropertynoVc);
    int countByPdNewpropertynoVcAndUdFloornoVcAndUdUnitnoVc(String pdNewpropertynoVc, String udFloornoVc, Integer udUnitnoVc);

}
