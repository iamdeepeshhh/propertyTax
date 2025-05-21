package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.RoomType_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.RoomType_MasterDto.RoomType_MasterDto;

import java.util.List;

public interface RoomType_MasterService {
    List<RoomType_MasterDto> findAll();
    RoomType_MasterDto findById(Integer id);
    RoomType_MasterDto addRoomType(RoomType_MasterDto roomTypeMasterDto);
    void deleteById(Integer id);
}
