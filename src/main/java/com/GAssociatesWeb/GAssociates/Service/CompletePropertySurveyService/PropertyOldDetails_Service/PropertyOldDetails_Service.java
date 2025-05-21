package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyOldDetails_Service;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyOldDetails_Dto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyOldDetails_Entity.PropertyOldDetails_Entity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PropertyOldDetails_Service {

    PropertyOldDetails_Dto saveOrUpdatePropertyOldDetail(PropertyOldDetails_Dto dto) throws Exception;

    PropertyOldDetails_Dto updatePropertyOldDetail(Integer id, PropertyOldDetails_Dto dto);

    public Optional<PropertyOldDetails_Dto> getPropertyOldDetailByOldPropertyNo(String oldPropertyNo);

    public Optional<PropertyOldDetails_Dto> getPropertyOldDetailByOldPropertyNoAndWardNo(String oldPropertyNo, String ward);

    List<PropertyOldDetails_Dto> getAllPropertyOldDetails();

    public void deletePropertyAndUnitsByOldPropertyNoAndWardNo(String oldPropertyNo, String wardNo);

    public List<PropertyOldDetails_Dto> searchOldProperties(String oldPropertyNo, String ownerName, String wardNo);

    public void saveExcel(MultipartFile file);

    public Optional<PropertyOldDetails_Dto> getPropertyOldDetailByPodRefNo(Integer podRefNo);

    public PropertyOldDetails_Dto updatePropertyOldDetails(PropertyOldDetails_Dto dto);
}