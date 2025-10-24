package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_ProposedRValuesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AfterHearingProposedRvalues_MasterRepository extends JpaRepository<AfterHearing_ProposedRValuesEntity, Integer> {

    List<AfterHearing_ProposedRValuesEntity> findByPrNewPropertyNoVc(String newPropertyNo);

}
