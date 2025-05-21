package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.EduCessAndEmpCess_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.EduCessAndEmpCess_MasterEntity.EduCessAndEmpCess_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EduCessAndEmpCess_MasterRepository extends JpaRepository<EduCessAndEmpCess_MasterEntity, Long> {
    @Query("SELECT c FROM EduCessAndEmpCess_MasterEntity c WHERE :ratableValue BETWEEN CAST(c.minTaxableValueFl AS double) AND CAST(c.maxTaxableValueFl AS double)")
    Optional<EduCessAndEmpCess_MasterEntity> findByRatableValueRange(@Param("ratableValue") double ratableValue);

}