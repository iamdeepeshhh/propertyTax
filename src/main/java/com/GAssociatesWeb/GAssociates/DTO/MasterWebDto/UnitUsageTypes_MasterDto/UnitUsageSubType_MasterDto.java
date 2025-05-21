package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.UnitUsageTypes_MasterDto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UnitUsageSubType_MasterDto {
    private Integer usm_usagesubid_i;
    private String usm_usagetypeeng_vc;
    private String usm_usagetypell_vc;
    private Integer uum_usageclassid_i;
    private Timestamp usm_created_dt;
    private Timestamp usm_updated_dt;
    private Integer usm_taxtype_i;
    private Integer usm_usercharges_i;
    private String uum_usagetypeeng_vc;
    private String uum_usagetypell_vc;
    private String usm_rvtype_vc;
    private Boolean usmApplyDifferentRateVc;
}
