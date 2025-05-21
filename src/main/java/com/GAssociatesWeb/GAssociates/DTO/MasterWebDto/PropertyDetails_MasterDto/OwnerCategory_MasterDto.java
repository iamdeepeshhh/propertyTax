package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerCategory_MasterDto {
    private Integer id;
    private String ownerCategory;
    private String ownerCategoryMarathi;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
