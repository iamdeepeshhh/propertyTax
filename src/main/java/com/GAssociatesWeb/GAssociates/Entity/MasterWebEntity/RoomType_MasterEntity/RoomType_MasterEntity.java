package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.RoomType_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "roomtype_master")
@Data
public class RoomType_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomtype_id")
    private Integer id;

    @Column(name = "roomtype")
    private String roomType;

    @Column(name = "roomSelected")
    private Integer roomSelected;

    @Column(name = "roomtype_marathi")
    private String roomTypeMarathi;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}