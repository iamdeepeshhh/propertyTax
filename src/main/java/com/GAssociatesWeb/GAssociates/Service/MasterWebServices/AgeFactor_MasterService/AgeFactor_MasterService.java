package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AgeFactor_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AgeFactor_MasterDto.AgeFactor_MasterDto;

import java.util.List;

public interface AgeFactor_MasterService {
    List<AgeFactor_MasterDto> getAllAgeFactors();
    AgeFactor_MasterDto getAgeFactorById(Integer id);
    AgeFactor_MasterDto createAgeFactor(AgeFactor_MasterDto ageFactorMasterDto);
    AgeFactor_MasterDto updateAgeFactor(Integer id, AgeFactor_MasterDto ageFactorMasterDto);
    void deleteAgeFactor(Integer id);
}