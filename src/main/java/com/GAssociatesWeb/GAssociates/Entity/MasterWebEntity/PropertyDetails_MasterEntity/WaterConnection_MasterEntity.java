package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "waterconnection_master")
@Data
public class WaterConnection_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waterconnection_id")
    private Integer waterConnectionId;

    @Column(name = "waterconnection")
    private String waterConnection;

    @Column(name = "waterconnection_marathi")
    private String waterConnectionMarathi;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters and Setters
}