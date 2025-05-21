package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyDetails_MasterRepository;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity.BuildStatus_MasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BuildStatus_MasterRepository extends JpaRepository<BuildStatus_MasterEntity, String> {
    // Custom repository methods can be added here if needed

    BuildStatus_MasterEntity findByBuildStatus(String buildStatus);
    @Query("SELECT COUNT(b) FROM BuildStatus_MasterEntity b")
    Integer findMaxId();
}
