package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyTypes_MasterDto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropSubUsageType_MasterDto {
    private Integer id;
    private String usageTypeEng;
    private String usageTypeLocal;
    private Integer usageId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String taxType;
}
