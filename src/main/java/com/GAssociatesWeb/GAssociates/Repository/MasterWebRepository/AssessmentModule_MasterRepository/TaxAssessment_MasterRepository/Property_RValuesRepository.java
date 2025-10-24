package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module.Property_RValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Property_RValuesRepository extends JpaRepository<Property_RValues, String > {
    List<Property_RValues> findAllByPrvPropertyNoVc(String prvPropertyNoVc);

    // ✅ Get all RValues by Property and Unit (for specific unit editing)
    List<Property_RValues> findAllByPrvPropertyNoVcAndPrvUnitNoVc(String prvPropertyNoVc, String prvUnitNoVc);

    // ✅ Optional custom query if you ever need latest financial year record
    @Query("SELECT p FROM Property_RValues p WHERE p.prvPropertyNoVc = :propertyNo ORDER BY p.createdAt DESC LIMIT 1")
    Property_RValues findLatestByPropertyNo(String propertyNo);
}
