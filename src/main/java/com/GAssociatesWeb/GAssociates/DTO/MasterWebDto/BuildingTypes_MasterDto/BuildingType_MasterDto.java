package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.BuildingTypes_MasterDto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BuildingType_MasterDto {
    private String btBuildingtypeengVc;
    private String temcustom;
    private Integer pcmClassidI;
    private String pcmProptypellVc;
    private String btBuildingtypellVc;
    private Timestamp btCreatedDt;
    private Timestamp btUpdatedDt;
    private String btRemarksVc;
    private Integer btBuildingTypeId;
}
