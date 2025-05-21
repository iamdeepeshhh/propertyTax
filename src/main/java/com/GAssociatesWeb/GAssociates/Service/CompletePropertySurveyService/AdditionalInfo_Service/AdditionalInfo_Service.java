package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.AdditionalInfo_Service;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.AdditionalInfo_Dto;

import java.util.List;

public interface AdditionalInfo_Service {
    AdditionalInfo_Dto saveAdditionalInfo(AdditionalInfo_Dto additionalInfoDTO);

    List<AdditionalInfo_Dto> getAllAdditionalInfos();

    AdditionalInfo_Dto getAdditionalInfoById(Integer id);

    AdditionalInfo_Dto updateAdditionalInfo(Integer id, AdditionalInfo_Dto additionalInfoDTO);

    void deleteAdditionalInfo(Integer id);
}
