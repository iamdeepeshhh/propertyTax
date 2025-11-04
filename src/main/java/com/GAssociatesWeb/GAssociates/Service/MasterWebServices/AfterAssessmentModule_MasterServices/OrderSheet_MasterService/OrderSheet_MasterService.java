package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.OrderSheet_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.OrderSheet_Dto.OrderSheet_Dto;

import java.util.List;

public interface OrderSheet_MasterService {
    OrderSheet_Dto getOrderSheetByNewPropertyNo(String newPropertyNo);
    List<OrderSheet_Dto> getOrderSheetsByWard(Integer wardNo);
}
