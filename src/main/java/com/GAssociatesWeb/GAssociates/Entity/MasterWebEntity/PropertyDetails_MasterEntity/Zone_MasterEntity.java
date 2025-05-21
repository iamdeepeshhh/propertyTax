package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity;


import jakarta.persistence.*;

import lombok.Data;


import java.time.LocalDateTime;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@Entity
@Table(name = "zone_master")
public class Zone_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true) // This enforces uniqueness on the zoneno field
    private Integer zoneno;

    private String deletedAt;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

}

