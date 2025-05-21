package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropSubclassification_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropSubclassification_MasterRepository extends JpaRepository<PropSubclassification_MasterEntity, Integer> {
    List<PropSubclassification_MasterEntity> findByPropertyClassificationMaster_PcmClassidI(Integer pcmClassidI);
}
