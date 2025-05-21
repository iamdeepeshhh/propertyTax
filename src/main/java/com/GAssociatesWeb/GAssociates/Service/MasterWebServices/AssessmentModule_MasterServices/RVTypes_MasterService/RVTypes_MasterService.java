package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.RVTypes_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.RVTypes_MasterDto.RVTypes_MasterDto;

import java.util.List;

public interface RVTypes_MasterService {
    RVTypes_MasterDto saveRVType(RVTypes_MasterDto rvTypeDto);

    List<RVTypes_MasterDto> getAllRVTypes();

    RVTypes_MasterDto getRVTypeById(Long id);

    void deleteRVTypeById(Long id);
    public RVTypes_MasterDto updateRVType(Long id, RVTypes_MasterDto rvTypeDto);
}
