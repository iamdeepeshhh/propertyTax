package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.RVTypes_MasterEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rvtypescategory_master")
public class RVTypeCategory_MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name_vc", nullable = false)
    private String categoryNameVc;

    @Column(name = "category_name_local_vc")
    private String categoryNameLocalVc;

    @Column(name = "description_vc")
    private String categoryDescriptionVc;
}
