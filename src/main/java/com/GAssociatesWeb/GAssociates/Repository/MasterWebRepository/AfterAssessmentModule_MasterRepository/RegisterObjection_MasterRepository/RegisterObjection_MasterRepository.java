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
            (:spn IS NULL OR LOWER(CAST(r.surveyNo AS string)) LIKE CONCAT('%', LOWER(CAST(:spn AS string)), '%'))
            AND (:finalPropertyNo IS NULL OR LOWER(CAST(r.finalPropertyNo AS string)) LIKE CONCAT('%', LOWER(CAST(:finalPropertyNo AS string)), '%'))
            AND (:ownerName IS NULL OR LOWER(CAST(r.ownerName AS string)) LIKE CONCAT('%', LOWER(CAST(:ownerName AS string)), '%'))
            AND (:ward IS NULL OR r.wardNo = :ward)
        ORDER BY r.createdAt DESC
    """)
    List<RegisterObjection_Entity> searchObjectionRecords(String spn, String finalPropertyNo, String ownerName, Integer ward);

    // 🔹 Fetch all objections in a specific ward
    List<RegisterObjection_Entity> findByWardNo(Integer wardNo);

    // 🔹 Fetch a single record using notice number (for print notice)
    RegisterObjection_Entity findByNoticeNo(String noticeNo);

    // 🔹 Optional: fetch by final property number (if you need it later)
    RegisterObjection_Entity findByFinalPropertyNo(String finalPropertyNo);
}
