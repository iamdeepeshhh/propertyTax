package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.RoomType_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.RoomType_MasterEntity.RoomType_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomType_MasterRepository  extends JpaRepository<RoomType_MasterEntity, Integer> {
    // Add custom queries if needed
}