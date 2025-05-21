package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyDetails_Service;


import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import org.springframework.web.multipart.MultipartFile;

public interface PropertyDetails_Service {
    PropertyDetails_Dto createProperty(PropertyDetails_Dto propertyDetailsDto);

    PropertyDetails_Dto updateProperty(PropertyDetails_Dto propertyDetailsDto);
    public PropertyDetails_Dto getPropertyByPdSurypropnoVc(String pdSurypropnoVc);
    void deleteProperty(String pdNewpropertynoVc);

    public PropertyDetails_Dto findPropertyDetailsByNewPropertyNo(String newPropertyNo);
    public void uploadCadImage(String newPropertyNo, MultipartFile cadImage);
    public void uploadPropertyImage(String newPropertyNo, MultipartFile propertImage);
}
