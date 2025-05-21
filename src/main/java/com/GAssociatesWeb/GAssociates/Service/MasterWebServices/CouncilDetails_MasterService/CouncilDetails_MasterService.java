package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.CouncilDetails_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.CouncilDetails_MasterDto;

public interface CouncilDetails_MasterService {
    public void saveCouncilDetails(CouncilDetails_MasterDto dto);
    public CouncilDetails_MasterDto getSingleCouncilDetails();
    public boolean deleteCouncilDetailById(Long id);
}
