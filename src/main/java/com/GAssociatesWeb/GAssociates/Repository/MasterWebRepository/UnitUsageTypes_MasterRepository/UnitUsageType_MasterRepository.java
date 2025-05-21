package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitUsageTypes_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity.UnitUsageType_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitUsageType_MasterRepository extends JpaRepository<UnitUsageType_MasterEntity, Integer> {
    List<UnitUsageType_MasterEntity> findByPropUsageTypeMaster_Id(Integer propUsageId);
}