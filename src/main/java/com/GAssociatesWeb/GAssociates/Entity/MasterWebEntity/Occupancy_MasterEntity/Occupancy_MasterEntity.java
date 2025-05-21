package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.Occupancy_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "occupancy_master")
@Data
public class Occupancy_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "occupancy_id", nullable = false)
    private Integer occupancy_id;

    @Column(name = "occupancy")
    private String occupancy;

    @Column(name = "occupancy_marathi")
    private String occupancy_marathi;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;
}
