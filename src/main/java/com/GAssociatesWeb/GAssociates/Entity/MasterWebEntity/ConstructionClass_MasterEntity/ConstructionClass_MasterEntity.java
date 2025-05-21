package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.ConstructionClass_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "constructionclass_master")
@Data
public class ConstructionClass_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ccm_seq")
    @SequenceGenerator(name = "ccm_seq", sequenceName = "constructionclass_master_ccm_conclassid_i_seq", allocationSize = 1)
    @Column(name = "ccm_conclassid_i")
    private Integer ccm_conclassid_i;

    @Column(name = "ccm_classnameeng_vc")
    private String ccm_classnameeng_vc;

    @Column(name = "ccm_classnamell_vc")
    private String ccm_classnamell_vc;

    @Column(name = "ccm_percentageofdeduction_i")
    private Integer ccm_percentageofdeduction_i;

    @Column(name = "ccm_created_dt")
    private LocalDateTime ccm_created_dt;

    @Column(name = "ccm_updated_dt")
    private LocalDateTime ccm_updated_dt;

    @Column(name = "ccm_remarks_vc")
    private String ccm_remarks_vc;

//    @Column(name = "id")
//    private Integer id;
    // Constructors, getters, and setters
}