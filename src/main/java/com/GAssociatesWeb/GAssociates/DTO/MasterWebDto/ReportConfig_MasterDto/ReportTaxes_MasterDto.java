package com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.ReportConfig_MasterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTaxes_MasterDto {

    private Long id;                // Primary key

    private String standardNameVc;  // Standard English tax name (e.g., "Property Tax")
    private String localNameVc;     // Local/Marathi tax name (e.g., "मालमत्ता कर")

    private Integer sequenceI;      // Display order on the report

    private Long taxKeyL;           // Key linking to consolidated_taxes_master (or special like ReportTaxKeys)

    private Long parentTaxKeyL;     // Parent key (if this tax belongs under a parent)

    private Boolean isParentBl;     // Is this entry a parent tax? (true/false)
    private Boolean showTotalBl;    // If partitions exist, should total also be shown? (true/false)

    private String templateVc;      // Report template (e.g., CALCULATION_SHEET, SPECIAL_NOTICE, etc.)
    private Boolean visibleB;
}
