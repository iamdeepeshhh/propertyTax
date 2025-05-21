package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Entity
@Table(name = "propertyclassification_master")
@Data // Generates getters, setters, toString, equals, and hashCode methods
public class PropClassification_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pcm_classidi")
    private Integer pcmClassidI;

    @Column(name = "pcm_proptypeeng_vc", nullable = false, length = 50)
    private String pcmProptypeengVc;

    @Column(name = "pcm_proptypell_vc", nullable = false, length = 50)
    private String pcmProptypellVc;

    @Column(name = "pcm_created_dt")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime pcmCreatedDt;

    @Column(name = "pcm_updated_dt")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime pcmUpdatedDt;

    @Column(name = "pcm_building_vc")
    private String pcmBuildingVc;


    // Constructors, Getters, and Setters
}
