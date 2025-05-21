package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.BuildingTypes_MasterDto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BuildingSubType_MasterDto {
    private Integer buildingsubtypeid;
    private String bstBuildingsubtypeengVc;
    private String bstBuildingsubtypellVc;
    private Integer buildingtypeid; // For linking to BuildingType_MasterEntity
    private String btBuildingtypellVc;
    private Timestamp bstCreatedDt;
    private Timestamp bstUpdatedDt;
    private String bstRemarksVc;
    private String temcustomb;
}
