package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.RegisterObjection_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.RegisterObjection_Entity.RegisterObjection_Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegisterObjection_MasterRepository extends JpaRepository<RegisterObjection_Entity, Long> {

    RegisterObjection_Entity findByNewPropertyNo(String NewPropNo);

    boolean existsByNewPropertyNo(String newPropertyNo);
}
