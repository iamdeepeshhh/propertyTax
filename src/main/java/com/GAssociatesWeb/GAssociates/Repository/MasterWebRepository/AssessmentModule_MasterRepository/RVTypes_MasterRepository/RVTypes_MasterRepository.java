package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.RVTypes_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RVTypes_MasterEntity.RVTypes_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface RVTypes_MasterRepository extends JpaRepository<RVTypes_MasterEntity, Long> {
    @Query("SELECT MAX(r.rvTypeId) FROM RVTypes_MasterEntity r")
    Long findMaxId();
}