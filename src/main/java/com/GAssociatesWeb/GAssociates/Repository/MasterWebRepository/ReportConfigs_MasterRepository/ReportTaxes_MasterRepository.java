package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.ReportConfigs_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.ReportConfigs_MasterModule.ReportTaxes_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportTaxes_MasterRepository extends JpaRepository<ReportTaxes_MasterEntity, Long> {

    List<ReportTaxes_MasterEntity> findByTemplateVcAndVisibleBIsTrueOrderBySequenceIAsc(String templateVc);
    List<ReportTaxes_MasterEntity> findByTemplateVcOrderBySequenceIAsc(String templateVc);
    Optional<ReportTaxes_MasterEntity> findByTaxKeyLAndTemplateVc(Long taxKeyL, String templateVc);

}
