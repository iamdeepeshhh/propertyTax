package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "buildstatus_master")
@Data
public class BuildStatus_MasterEntity {

    @Id
    @Column(name = "buildstatus", nullable = false, unique = true)
    private String buildStatus;

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "buildstatus_master_id_seq")
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

}