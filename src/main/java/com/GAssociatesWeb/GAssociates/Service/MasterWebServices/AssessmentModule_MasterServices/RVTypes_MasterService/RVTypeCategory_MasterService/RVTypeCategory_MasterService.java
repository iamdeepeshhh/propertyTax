package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.RVTypes_MasterService.RVTypeCategory_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto.RVTypeCategory_MasterDto;

import java.util.List;

public interface RVTypeCategory_MasterService {
    public List<RVTypeCategory_MasterDto> getAllRVTypeCategories();
    void saveRVTypeCategory(RVTypeCategory_MasterDto rvTypeCategoryMasterDto);
}
