package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_PropertyDetailsEntity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity.PropertyDetails_Entity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AfterHearingPropertyDetails_MasterRepository extends JpaRepository<AfterHearing_PropertyDetailsEntity, String> {

    Optional<AfterHearing_PropertyDetailsEntity> findByPdNewpropertynoVc(String pdNewpropertynoVc);

    Optional<AfterHearing_PropertyDetailsEntity> findByPdSurypropnoVc(String pdSurypropnoVc);

    boolean existsByPdNewpropertynoVc(String pdNewpropertynoVc);


    Optional<AfterHearing_PropertyDetailsEntity> findBypdNewpropertynoVc(String pdNewpropertynoVc);
    Optional<AfterHearing_PropertyDetailsEntity> findBypdSurypropnoVc(String pdSurypropnoVc);
    void deleteByPdNewpropertynoVc(String pdNewpropertynoVc);
    Optional<AfterHearing_PropertyDetailsEntity> findByPdSurypropnoVcAndPdWardI(String surveyPropertyNo, Integer wardNo);
    List<AfterHearing_PropertyDetailsEntity> findByPdOwnernameVcContainingIgnoreCaseAndPdWardI(String ownerName, Integer wardNo);
    List<AfterHearing_PropertyDetailsEntity> findByPdSurypropnoVcContainingIgnoreCase(String surveyPropertyNo);
    List<AfterHearing_PropertyDetailsEntity> findByPdOwnernameVcContainingIgnoreCase(String ownerName);
    List<AfterHearing_PropertyDetailsEntity> findByPdWardI(Integer wardNo);

    List<AfterHearing_PropertyDetailsEntity> findAllByPdWardI(int wardNumber, Pageable pageable);

    List<AfterHearing_PropertyDetailsEntity> findByPdFinalpropnoVcAndPdWardI(String pdFinalpropnoVc, Integer pdWardI);

    List<AfterHearing_PropertyDetailsEntity> findByPdFinalpropnoVcContainingIgnoreCase(String finalPropertyNo);

}
