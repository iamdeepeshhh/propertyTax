package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.WardTypes_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ward_master")
@Data
public class Ward_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wardNo;

    @Column(name = "wardcount")
    private String countNo;

}
