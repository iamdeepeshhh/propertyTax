package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "afterhearing_unit_builtup")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_UnitBuiltupDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String pd_newpropertyno_vc;
    private String ud_floorno_vc;
    private Integer ud_unitno_vc;
    private Integer ub_ids_i;
    private String ub_roomtype_vc;
    private String ub_length_fl;
    private String ub_breadth_fl;
    private String ub_exemptionst_vc;
    private String ub_exemlength_fl;
    private String ub_exembreadth_fl;
    private String ub_exemarea_fl;
    private String ub_carpetarea_fl;
    private String ub_assesarea_fl;
    private LocalDate ud_timestamp_dt;
    private String ub_legalst_vc;
    private String ub_legalarea_fl;
    private String ub_illegalarea_fl;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String ud_unitremark_vc;
    private String ub_measuretype_vc;
    private String ub_orgassessarea_vc;
    private String ub_dedpercent_i;
    private String ub_areabeforededuction_fl;
    private String plotarea;
}
