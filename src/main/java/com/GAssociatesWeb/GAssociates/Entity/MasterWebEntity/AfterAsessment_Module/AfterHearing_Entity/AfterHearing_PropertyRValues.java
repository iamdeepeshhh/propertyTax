package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "afterhearing_property_rvalues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_PropertyRValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String prv_propertyno_vc;
    private String prv_finalpropno_vc;
    private String prv_unitno_vc;
    private Double prv_rate_f;
    private Double prv_lentingvalue_f;
    private Integer prv_depper_i;
    private Double prv_depamount_f;
    private Double prv_alv_f;
    private Double prv_mainval_f;
    private Double prv_taxvalue_f;
    private String prv_tenantname_vc;
    private Double prv_rent_fl;
    private Double prv_yearlyrent_fl;
    private Double prv_temainval_fl;
    private Double prv_tentantval_f;
    private Double prv_ratablevalue_f;
    private String prv_finyear_vc;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String statusdummy;

}
