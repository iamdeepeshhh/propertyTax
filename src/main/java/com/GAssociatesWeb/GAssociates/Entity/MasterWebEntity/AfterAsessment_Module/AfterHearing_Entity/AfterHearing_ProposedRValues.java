package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "afterhearing_unit_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHearing_ProposedRValues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String prorv_propertyno_vc;
    private String prorv_finalpropno_vc;
    private Double prorv_rv_res;
    private Double prorv_rv_mob;
    private Double prorv_rv_ele;
    private Double prorv_rv_com;
    private Double prorv_rv_gov;
    private Double prorv_rv_edu;
    private Double prorv_rv_reg;
    private Double prorv_rv_comop;
    private Double prorv_rv_resop;
    private Double prorv_rv_indus;
    private Double prorv_final_rv;
    private String prorv_finalyear_dt;
    private Double propletout;
    private Double propnotletout;
    private String prorv_finyear_vc;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Double alvletout;
    private Double alvnotletout;
    private Double prorv_rv_regop;
    private Double prorv_rv_govop;
    private Double prorv_rv_eduop;
    private Double prorv_rv_indusop;
    private Integer noofcolvalues;
}
