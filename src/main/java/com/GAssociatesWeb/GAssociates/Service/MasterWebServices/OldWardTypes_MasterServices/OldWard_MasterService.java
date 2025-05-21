package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.OldWardTypes_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.OldWardTypes_MasterDto.OldWard_MasterDto;

import java.util.List;

public interface OldWard_MasterService {
    OldWard_MasterDto saveOldWardMaster(OldWard_MasterDto oldWardMasterDTO);
    OldWard_MasterDto findOldWardMasterByOldwardno(Integer oldwardno);
    List<OldWard_MasterDto> findAllOldWardMasters();
    void deleteOldWardMasterByOldwardno(Integer oldwardno);
}
