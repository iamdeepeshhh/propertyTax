package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.BuildingTypes_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.BuildingTypes_MasterEntity.BuildingSubType_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingSubType_MasterRepository extends JpaRepository<BuildingSubType_MasterEntity, Integer> {
    List<BuildingSubType_MasterEntity> findByBuildingTypeMaster_buildingtypeid(Integer buildingTypeId);
}
