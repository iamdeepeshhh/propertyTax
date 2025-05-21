package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropUsageType_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropUsageType_MasterRepository extends JpaRepository<PropUsageType_MasterEntity, Integer> {

    List<PropUsageType_MasterEntity> findByPropertySubclassificationMaster_psmSubclassidI(Integer psmSubclassidI);


}