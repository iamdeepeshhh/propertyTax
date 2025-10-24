package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module.Proposed_RValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Proposed_RValuesRepository extends JpaRepository<Proposed_RValues, String> {
    @Query("SELECT p FROM Proposed_RValues p WHERE p.PrNewPropertyNoVc = :newPropertyNo")
    List<Proposed_RValues> findByPrNewPropertyNoVc(@Param("newPropertyNo") String newPropertyNo);

}
