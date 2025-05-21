package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyDetails_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity.Zone_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Zone_MasterRepository extends JpaRepository<Zone_MasterEntity, Integer> {
    @Query("SELECT MAX(z.zoneno) FROM Zone_MasterEntity z")
    Integer findMaxZoneNo();
}
