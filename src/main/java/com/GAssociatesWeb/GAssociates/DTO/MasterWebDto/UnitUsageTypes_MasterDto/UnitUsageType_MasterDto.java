package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitUsageTypes_MasterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitUsageType_MasterDto {
    private Integer uum_usageid_i;
    private String uum_usagetypeeng_vc;
    private String uum_usagetypell_vc;
    private Integer pum_usageid_i; // PropUsageType ID for referencing
    private LocalDateTime uum_created_dt;
    private LocalDateTime uum_updated_dt;
    private String uum_abbr_vc;
    private String uum_type_vc;
    private String uum_rvtype_vc;
}
