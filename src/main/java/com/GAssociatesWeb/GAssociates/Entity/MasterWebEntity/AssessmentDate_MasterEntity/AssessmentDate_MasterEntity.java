package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentDate_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "assessmentdate_master")
public class AssessmentDate_MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assessmentId;
    private String firstAssessmentDate;
    private String lastAssessmentDate;
    private String currentAssessmentDate;
}
