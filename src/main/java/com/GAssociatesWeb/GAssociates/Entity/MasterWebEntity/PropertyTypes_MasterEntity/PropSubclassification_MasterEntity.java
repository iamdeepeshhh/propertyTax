package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "propsubclassification_master")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PropSubclassification_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "psm_subclassidi")
    private Integer psmSubclassidI;

    @Column(name = "psm_subclasseng_vc", nullable = false, length = 50)
    private String psmSubclassengVc;

    @Column(name = "psm_subclassll_vc", nullable = false, length = 50)
    private String psmSubclassllVc;

    @ManyToOne(fetch = FetchType.EAGER)//add lazy in place of eager after testing*******************
    @JoinColumn(name = "pcm_classid_i", nullable = false, foreignKey = @ForeignKey(name = "pcm_fk"))
    private PropClassification_MasterEntity propertyClassificationMaster;

    @Column(name = "psm_created_dt")
    @CreationTimestamp
    private LocalDateTime psmCreatedDt;

    @Column(name = "psm_updated_dt")
    private LocalDateTime psmUpdatedDt;


}