package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_PropertyTaxDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AfterHearingPropertyTaxDetails_MasterRepository extends JpaRepository<AfterHearing_PropertyTaxDetailsEntity, Integer> {

    List<AfterHearing_PropertyTaxDetailsEntity> findByPtNewPropertyNoVc(String newPropertyNo);

    void deleteByPtNewPropertyNoVc(String newPropertyNo);
}
