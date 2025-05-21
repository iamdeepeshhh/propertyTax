package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropUsageType_MasterDto {
    private Integer propertySubTypeId;
    private Integer propertyUsageTypeId;// ID of the selected property subtype
    private String usageTypeName; // Usage type name entered by the user
    private String localUsagetypeName; // Local name for the property usage subtype

}
