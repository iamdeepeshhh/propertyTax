package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.CompleteProperty_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PropertyManagement_Service {
    CompleteProperty_Dto createCompleteProperty(CompleteProperty_Dto completeProperty_dto);


    List<PropertyDetails_Dto> searchWardandFinalPropertyNo(String finalPropertyNo, Integer wardNo);

    public CompleteProperty_Dto getCompletePropertyBySurveyNumber(String pdSuryPropNo);
    void deleteCompleteProperty(String pdNewpropertynoVc);
    public List<PropertyDetails_Dto> searchNewProperties(String surveyPropertyNo, String ownerName, Integer wardNo, String finalPropertyNo, Integer page, Integer size);

    public CompleteProperty_Dto getCompletePropertyByNewPropertyNo(String PdNewPropertyNo);

    public CompleteProperty_Dto updateCompleteProperty(CompleteProperty_Dto completePropertyDto);
    public void uploadPropertyImage(String propertyId, MultipartFile propertyImage);
    public void uploadCadImage(String propertyId, MultipartFile cadImage);
    public Map<String, String> getPropertiesCount();
}
