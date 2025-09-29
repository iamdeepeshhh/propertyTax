package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.RvTypesAppliedTaxes_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RvTypesAppliedTaxes_MasterEntity.RvTypesAppliedTaxes_MasterEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RvTypesAppliedTaxes_MasterRepository extends JpaRepository<RvTypesAppliedTaxes_MasterEntity, Long> {
    List<RvTypesAppliedTaxes_MasterEntity> findByRvtypeId(Long rvtypeId);
    boolean existsByRvtypeIdAndTaxId(Long rvtypeId, Long taxId);
    Optional<RvTypesAppliedTaxes_MasterEntity> findByRvtypeIdAndTaxId(Long rvtypeId, Long taxId);

    @Transactional
    void deleteByRvtypeId(Long rvtypeId);
}