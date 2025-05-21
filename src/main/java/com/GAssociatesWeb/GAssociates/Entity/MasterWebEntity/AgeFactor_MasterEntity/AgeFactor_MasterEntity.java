package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AgeFactor_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "agefactor_master")
public class AgeFactor_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "afm_seq")
    @SequenceGenerator(name = "afm_seq", sequenceName = "agefactor_master_afm_agefactorid_i_seq", allocationSize = 1)
    private Integer afm_agefactorid_i;

    @Column(name = "afm_agefactornameeng_vc", nullable = false)
    private String afm_agefactornameeng_vc;

    @Column(name = "afm_agefactornamell_vc", nullable = false)
    private String afm_agefactornamell_vc;

    @Column(name = "afm_ageminage_vc", nullable = false)
    private String afm_ageminage_vc;

    @Column(name = "afm_agemaxage_vc", nullable = false)
    private String afm_agemaxage_vc;

    @Column(name = "afm_created_dt")
    private LocalDateTime afm_created_dt;

    @Column(name = "afm_updated_dt")
    private LocalDateTime afm_updated_dt;

    @Column(name = "afm_remarks_vc", length = 150)
    private String afm_remarks_vc;

    // Getters and Setters
}