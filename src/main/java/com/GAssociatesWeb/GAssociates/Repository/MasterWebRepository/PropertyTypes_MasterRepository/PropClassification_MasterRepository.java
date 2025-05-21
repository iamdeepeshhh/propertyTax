package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyTypes_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropClassification_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropClassification_MasterRepository extends JpaRepository<PropClassification_MasterEntity, Integer> {
    Optional<PropClassification_MasterEntity> findById(Integer id);
}
