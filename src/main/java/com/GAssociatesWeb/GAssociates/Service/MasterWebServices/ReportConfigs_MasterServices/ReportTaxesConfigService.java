package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.ReportConfig_MasterDto.ReportTaxes_MasterDto;

import java.util.List;

public interface ReportTaxesConfigService {
    List<ReportTaxes_MasterDto> getVisibleConfigsByTemplate(String templateVc);
    List<ReportTaxes_MasterDto> getAllConfigsByTemplate(String templateVc);
    public void saveOrUpdateReportConfigTaxes(List<ReportTaxes_MasterDto> configs);
    public List<ReportTaxes_MasterDto> getAllConfigs();
    // Inserts only when missing for (taxKey, template); never updates existing rows
    public void insertMissingOnly(List<ReportTaxes_MasterDto> configs);
}
