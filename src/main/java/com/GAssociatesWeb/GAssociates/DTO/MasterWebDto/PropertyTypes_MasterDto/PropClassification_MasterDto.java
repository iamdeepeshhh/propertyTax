package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropClassification_MasterDto {
    private Integer pcmId;
    private String propertyTypeName;
    private String language;
    private String localPropertyTypeName;
    private String buildingInp;
}
