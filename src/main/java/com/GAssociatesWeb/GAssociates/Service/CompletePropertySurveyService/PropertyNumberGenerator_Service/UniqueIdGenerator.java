package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyNumberGenerator_Service;

import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniqueIdGenerator {
    @Autowired
    private SequenceService sequenceService;

    @PersistenceContext
    private EntityManager entityManager;

    public String generateUniquePropertyNo(String wardNo, String zoneNo) {
        String sequenceName = "property_details_pd_newpropertyno_vc_seq"; // Ensure this sequence is initialized
        Long nextVal = sequenceService.getNextSequenceValueLong(sequenceName);
        return String.format("%s%s%05d", wardNo, zoneNo, nextVal); // Format as WARD-ZONE-00001
    }

    public Long generateUniqueIdForUnitDetails() {
        String sequenceName = "unit_details_id_seq"; // Ensure this sequence is initialized
        return sequenceService.getNextSequenceValueLong(sequenceName);
    }

    public Long generateUniqueIdForUnitBuiltUp() {
        String sequenceName = "unit_builtup_id_seq"; // Ensure this sequence is initialized
        return sequenceService.getNextSequenceValueLong(sequenceName);
    }

    public void assignFinalPropertyNumbers(String wardNo) {
        try {
            int wardNumber = Integer.parseInt(wardNo); // Convert String to Integer
            entityManager.createNativeQuery("SELECT assign_final_property_number(:wardNumber)")
                    .setParameter("wardNumber", wardNumber)
                    .getSingleResult(); // Execute the query to call the function
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ward number: " + wardNo, e);
        }
    }
}
