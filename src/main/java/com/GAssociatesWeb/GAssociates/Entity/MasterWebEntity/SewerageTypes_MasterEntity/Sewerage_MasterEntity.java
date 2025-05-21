package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.SewerageTypes_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sewarage_master")
@Data
public class Sewerage_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // or another strategy that fits your DB
    private Integer id;
    private String sewerage;

}