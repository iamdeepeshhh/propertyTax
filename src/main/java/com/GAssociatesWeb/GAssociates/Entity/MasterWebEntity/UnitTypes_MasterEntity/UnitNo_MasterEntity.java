package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.UnitTypes_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "unitno_master")
@Data
public class UnitNo_MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String unitno;
}
