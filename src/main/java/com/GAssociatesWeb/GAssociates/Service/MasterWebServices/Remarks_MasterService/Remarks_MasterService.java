package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.Remarks_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.Remarks_MasterDto.Remarks_MasterDto;

import java.util.List;

public interface Remarks_MasterService {

    Remarks_MasterDto createRemark(Remarks_MasterDto remarksMasterDto);

    List<Remarks_MasterDto> getAllRemarks();
}

