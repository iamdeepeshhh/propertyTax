package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.RegisterObjection_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.RegisterObjection_Dto.RegisterObjection_Dto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.RegisterObjection_Entity.RegisterObjection_Entity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.RegisterObjection_MasterRepository.RegisterObjection_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegisterObjection_MasterServiceImpl implements RegisterObjection_MasterService {

    @Autowired
    private RegisterObjection_MasterRepository repository;

    @Override
    public void saveObjection(RegisterObjection_Dto dto) {
        RegisterObjection_Entity entity = new RegisterObjection_Entity();
        entity.setWardNo(dto.getWardNo());
        entity.setFinalPropertyNo(dto.getFinalPropertyNo());
        entity.setOwnerName(dto.getOwnerName());
        entity.setReasons(dto.getReasons());
        entity.setOthers(dto.getOthers());
        entity.setRespondent(dto.getRespondent());
        entity.setUserDate(dto.getUserDate());
        entity.setApplicationReceivedDate(dto.getApplicationReceivedDate());
        entity.setPhoneNo(dto.getPhoneNo());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        // Map other fields as needed...

        repository.save(entity);
    }

}
