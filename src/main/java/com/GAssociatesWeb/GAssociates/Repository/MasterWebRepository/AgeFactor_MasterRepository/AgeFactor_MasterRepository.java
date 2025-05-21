package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AgeFactor_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AgeFactor_MasterEntity.AgeFactor_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgeFactor_MasterRepository extends JpaRepository<AgeFactor_MasterEntity, Integer> {
}