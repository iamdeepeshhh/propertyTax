package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.Remarks_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Remarks_Master")
public class Remarks_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String remark;
}