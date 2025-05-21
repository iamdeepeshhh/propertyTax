package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "ownertype_master")
@Data
public class OwnerType_MasterEntity {

    @Id
    private Integer ownertypeId;

    @Column(name = "ownertype")
    private String ownerType;

    @Column(name = "ownertype_marathi")
    private String ownerTypeMarathi;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters and Setters
}
