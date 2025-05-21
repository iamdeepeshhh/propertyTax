package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.OldWard_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "oldward_master")
@Data
public class OldWard_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oldwardno;

    @Column(name = "oldwardcount")
    private String countNo;
}
