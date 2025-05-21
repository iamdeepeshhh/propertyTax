package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AgeFactor_MasterDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgeFactor_MasterDto {

    private Integer afm_agefactorid_i;
    private String afm_agefactornameeng_vc;
    private String afm_agefactornamell_vc;
    private String afm_ageminage_vc;
    private String afm_agemaxage_vc;
    private LocalDateTime afm_created_dt;
    private LocalDateTime afm_updated_dt;
    private String afm_remarks_vc;
    private Integer id;
    // Getters and Setters
}