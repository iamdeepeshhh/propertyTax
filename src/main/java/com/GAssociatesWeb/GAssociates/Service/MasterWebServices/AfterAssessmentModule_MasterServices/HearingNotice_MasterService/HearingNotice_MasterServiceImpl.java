package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.HearingNotice_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.RegisterObjection_Dto.RegisterObjection_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.RegisterObjection_Entity.RegisterObjection_Entity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.RegisterObjection_MasterRepository.RegisterObjection_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.RegisterObjection_MasterService.RegisterObjection_MasterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HearingNotice_MasterServiceImpl implements HearingNotice_MasterService {

    private final RegisterObjection_MasterRepository registerObjectionRepository;

    @Override
    public List<RegisterObjection_Dto> getHearingNoticesByWard(Integer wardNo) {
        return registerObjectionRepository.findByWardNo(wardNo)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RegisterObjection_Dto convertToDto(RegisterObjection_Entity entity) {
        RegisterObjection_Dto dto = new RegisterObjection_Dto();
        dto.setId(entity.getId());
        dto.setWardNo(entity.getWardNo());
        dto.setZoneNo(entity.getZoneNo());
        dto.setSurveyNo(entity.getSurveyNo());
        dto.setFinalPropertyNo(entity.getFinalPropertyNo());
        dto.setNewPropertyNo(entity.getNewPropertyNo());
        dto.setOwnerName(entity.getOwnerName());
        dto.setReasons(entity.getReasons());
        dto.setOthers(entity.getOthers());
        dto.setRespondent(entity.getRespondent());
        dto.setObjectionDate(entity.getObjectionDate());
        dto.setOldPropertyNo(entity.getOldPropertyNo());
        dto.setUserDate(entity.getUserDate());
        dto.setUserTime(entity.getUserTime());
        dto.setNoticeNo(entity.getNoticeNo());
        dto.setHearingDate(entity.getHearingDate());
        dto.setHearingTime(entity.getHearingTime());
        dto.setHearingStatus(entity.getHearingStatus());
        dto.setApplicationNo(entity.getApplicationNo());
        dto.setPhoneNo(entity.getPhoneNo());
        dto.setApplicationReceivedDate(entity.getApplicationReceivedDate());
        dto.setChangedValue(entity.getChangedValue());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
