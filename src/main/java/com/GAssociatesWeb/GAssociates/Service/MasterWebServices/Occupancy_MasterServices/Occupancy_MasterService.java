package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.Occupancy_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.Occupancy_MasterDto.Occupancy_MasterDto;

import java.util.List;
import java.util.Optional;

public interface Occupancy_MasterService {
    Occupancy_MasterDto saveOccupancy_Master(Occupancy_MasterDto dto);
    Occupancy_MasterDto findOccupancy_MasterById(Integer id);
    List<Occupancy_MasterDto> findAllOccupancy_Masters();
    void deleteOccupancy_MasterById(Integer id);
    public Optional<Occupancy_MasterDto> findById(Integer id);
}
