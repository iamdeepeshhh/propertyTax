package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxDepreciation_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxDepreciation_MasterEntity.TaxDepreciation_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaxDepreciation_MasterRepository extends JpaRepository<TaxDepreciation_MasterEntity, Long> {
    @Query("SELECT tdm FROM TaxDepreciation_MasterEntity tdm WHERE tdm.constructionClassVc = :constructionClassVc AND :age BETWEEN CAST(tdm.minAgeI AS int) AND CAST(tdm.maxAgeI AS int)")
    Optional<TaxDepreciation_MasterEntity> findDepreciationByAgeAndClass(@Param("constructionClassVc") String constructionClassVc, @Param("age") int age);
}
