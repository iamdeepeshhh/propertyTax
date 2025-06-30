package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "afterhearing_property_taxdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_PropertyTaxDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String pt_newpropertyno_vc;
    private String pt_finalpropno_vc;
    private Double pt_propertytax_fl;
    private Double pt_egctax_fl;
    private Double pt_treetax_fl;
    private Double pt_envtax_fl;
    private Double pt_lighttax_fl;
    private Double pt_cleantax_fl;
    private Double pt_firetax_fl;
    private Double pt_usercharges_fl;
    private Double pt_edurestax_fl;
    private Double pt_edunrestax_fl;
    private Double pt_edutax_fl;
    private Double pt_final_tax_fl;
    private String pt_finalyear_vc;
    private Double pt_finalrv_fl;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
