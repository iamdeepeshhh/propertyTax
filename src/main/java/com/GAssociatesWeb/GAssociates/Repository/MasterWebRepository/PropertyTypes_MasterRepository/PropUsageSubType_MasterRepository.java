package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropUsageSubType_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropUsageSubType_MasterRepository extends JpaRepository<PropUsageSubType_MasterEntity, Integer> {
    List<PropUsageSubType_MasterEntity> findByUsageId(Integer usageId);
}
