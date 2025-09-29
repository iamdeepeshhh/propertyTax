package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.ConsolidatedTaxes_MasterRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.ConsolidatedTaxes_MasterEntity.ConsolidatedTaxes_MasterEntity;

public interface ConsolidatedTaxes_MasterRepository extends JpaRepository<ConsolidatedTaxes_MasterEntity, Long> {
    ConsolidatedTaxes_MasterEntity findByTaxNameVc(String taxNameVc);
    boolean existsByTaxNameVc(String taxNameVc);
    Optional<ConsolidatedTaxes_MasterEntity> findByTaxKeyL(Long taxKeyL);
    List<ConsolidatedTaxes_MasterEntity> findByIsActiveBl(boolean isActiveBl);
}