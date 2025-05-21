package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentDate_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentDate_MasterEntity.AssessmentDate_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentDate_MasterRepository extends JpaRepository<AssessmentDate_MasterEntity, Integer> {
    // You can add custom query methods if needed
}