package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.WardTypes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.WardTypes_MasterEntity.Ward_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Ward_MasterRepository extends JpaRepository<Ward_MasterEntity, Long> {
    // Add custom query methods if needed
}
