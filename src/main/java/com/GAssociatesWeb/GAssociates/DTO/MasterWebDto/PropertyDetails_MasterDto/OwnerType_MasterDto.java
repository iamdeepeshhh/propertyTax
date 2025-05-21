package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerType_MasterDto {
    private Integer ownertypeId;
    private String ownerType;
    private String ownerTypeMarathi;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
