package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.BuildingTypes_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.BuildingTypes_MasterEntity.BuildingType_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingType_MasterRepository extends JpaRepository<BuildingType_MasterEntity, Integer> {
    List<BuildingType_MasterEntity> findByPropertyClassificationMaster_PcmClassidI(Integer pcmClassidI);
}
