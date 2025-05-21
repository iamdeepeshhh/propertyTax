package com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository;

import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyOldDetails_Entity.PropertyOldDetails_Entity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyOldDetails_Entity.UnitOldDetails_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UnitOldDetails_Repository extends JpaRepository<UnitOldDetails_Entity, Long> {
    List<UnitOldDetails_Entity> findByPropertyOldDetails_PodRefNoVc(Integer podRefNoVc);
    @Modifying
    @Transactional
    @Query("delete from UnitOldDetails_Entity u where u.propertyOldDetails.podRefNoVc = ?1")
    void deleteByPropertyOldDetailsRefNo(String refNo);

}
