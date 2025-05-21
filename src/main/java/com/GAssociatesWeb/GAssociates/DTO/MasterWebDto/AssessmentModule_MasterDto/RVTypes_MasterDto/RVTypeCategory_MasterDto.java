package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RVTypeCategory_MasterDto {
    private Long categoryId;
    private String categoryNameVc;
    private String categoryNameLocalVc;
    private String categoryDescriptionVc;
}
