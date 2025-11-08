package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module.Property_TaxDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Property_TaxDetailsRepository extends JpaRepository<Property_TaxDetails, String> {
    /**
     * 🔹 Find all tax records by final property number.
     */
    List<Property_TaxDetails> findAllByPtFinalPropertyNoVc(String finalPropertyNo);

   Property_TaxDetails findByPtNewPropertyNoVc(String newPropertyNoVc);

    /**
     * 🔹 Find all tax records for a specific financial year.
     */
    List<Property_TaxDetails> findAllByPtFinalYearVc(String year);
}
