package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitTypes_MasterEntity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "unitfloorno_master")
@Data
public class UnitFloorNo_MasterEntity {


    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // No @Id or @GeneratedValue annotation since it's manually handled

    @Id
    private String unitfloorno;

}



