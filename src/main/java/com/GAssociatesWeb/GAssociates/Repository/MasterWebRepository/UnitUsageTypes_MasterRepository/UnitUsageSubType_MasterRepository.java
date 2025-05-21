package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.UnitUsageTypes_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitUsageTypes_MasterEntity.UnitUsageSubType_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitUsageSubType_MasterRepository extends JpaRepository<UnitUsageSubType_MasterEntity, Integer> {
    @Query("SELECT u FROM UnitUsageSubType_MasterEntity u WHERE u.unitUsageMaster.uum_usageid_i = :usageId")
    List<UnitUsageSubType_MasterEntity> findByUnitUsageMaster_Uum_usageid_i(@Param("usageId") Integer usageId);
}