package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.ConstructionClass_MasterDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConstructionClass_MasterDto {
    private Integer ccm_conclassid_i;
    private String ccm_classnameeng_vc;
    private String ccm_classnamell_vc;
    private LocalDateTime ccm_created_dt;
    private LocalDateTime ccm_updated_dt;
    private String ccm_remarks_vc;
    private Integer ccm_percentageofdeduction_i;
   // private Integer id;
}
