package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ownercategory_master")
@Data
public class OwnerCategory_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ownercategory_id")
    private Integer id;

    @Column(name = "ownercategory")
    private String ownerCategory;

    @Column(name = "ownercategory_marathi")
    private String ownerCategoryMarathi;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}