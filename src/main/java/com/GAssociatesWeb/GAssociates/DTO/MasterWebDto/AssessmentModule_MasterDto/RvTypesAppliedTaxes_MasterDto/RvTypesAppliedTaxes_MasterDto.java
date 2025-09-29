package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RvTypesAppliedTaxes_MasterDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RvTypesAppliedTaxes_MasterDto {

    private Long id;
    private Long rvtypeId;
    private Long taxKeyL;
    private LocalDateTime createdDt;
    private LocalDateTime updatedDt;

    // Optionally include nested DTOs if you need full details:
    // private RVTypes_MasterDto rateType;
    // private ConsolidatedTaxes_MasterDto tax;

    public RvTypesAppliedTaxes_MasterDto() {}

    public RvTypesAppliedTaxes_MasterDto(Long id, Long rvtypeId, Long taxKeyL,
                                         LocalDateTime createdDt, LocalDateTime updatedDt) {
        this.id = id;
        this.rvtypeId = rvtypeId;
        this.taxKeyL = taxKeyL;
        this.createdDt = createdDt;
        this.updatedDt = updatedDt;
    }
}