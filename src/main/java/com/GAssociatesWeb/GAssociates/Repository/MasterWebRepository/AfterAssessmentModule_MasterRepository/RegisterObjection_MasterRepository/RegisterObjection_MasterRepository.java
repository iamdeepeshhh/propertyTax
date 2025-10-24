package com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.RegisterObjection_MasterRepository;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.RegisterObjection_Entity.RegisterObjection_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegisterObjection_MasterRepository extends JpaRepository<RegisterObjection_Entity, Long> {

    RegisterObjection_Entity findByNewPropertyNo(String NewPropNo);

    boolean existsByNewPropertyNo(String newPropertyNo);

    @Query("""
        SELECT r FROM RegisterObjection_Entity r
        WHERE 
            (:spn IS NULL OR LOWER(r.surveyNo) LIKE LOWER(CONCAT('%', :spn, '%')))
            AND (:finalPropertyNo IS NULL OR LOWER(r.finalPropertyNo) LIKE LOWER(CONCAT('%', :finalPropertyNo, '%')))
            AND (:ownerName IS NULL OR LOWER(r.ownerName) LIKE LOWER(CONCAT('%', :ownerName, '%')))
            AND (:ward IS NULL OR r.wardNo = :ward)
        ORDER BY r.createdAt DESC
    """)
    List<RegisterObjection_Entity> searchObjectionRecords(String spn, String finalPropertyNo, String ownerName, Integer ward);
}
