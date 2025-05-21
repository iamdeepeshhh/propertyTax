package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropSubClassification_MasterDto {
    private Integer propertyTypeId;
    private String propertySubtypeName;
    private String language;
    private String localPropertySubtypeName;
    private Integer propertySubClassificationId;
}
