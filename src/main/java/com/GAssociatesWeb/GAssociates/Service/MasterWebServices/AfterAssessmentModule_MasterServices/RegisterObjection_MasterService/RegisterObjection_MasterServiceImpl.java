package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.RegisterObjection_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.RegisterObjection_Dto.RegisterObjection_Dto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.RegisterObjection_Entity.RegisterObjection_Entity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.RegisterObjection_MasterRepository.RegisterObjection_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterObjection_MasterServiceImpl implements RegisterObjection_MasterService {

    @Autowired
    private RegisterObjection_MasterRepository repository;

    public boolean existsObjection(String newPropertyNo){
       return  repository.existsByNewPropertyNo(newPropertyNo);
    }


    @Override
    public void saveObjection(RegisterObjection_Dto dto) {


            RegisterObjection_Entity entity = new RegisterObjection_Entity();
            entity.setWardNo(dto.getWardNo());
            entity.setFinalPropertyNo(dto.getFinalPropertyNo());
            entity.setSurveyNo(dto.getSurveyNo());
            entity.setNoticeNo(dto.getNoticeNo());
            entity.setOwnerName(dto.getOwnerName());
            entity.setNewPropertyNo(dto.getNewPropertyNo());
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




    public RegisterObjection_Dto getObjection(String newPropertyNo){

//        RegisterObjection_Entity byApplicationNo = repository.findByNewPropertyNo(newPropertyNo);

        RegisterObjection_Dto dto = toDto(repository.findByNewPropertyNo(newPropertyNo));

      return dto;
    }

    public static RegisterObjection_Dto toDto(RegisterObjection_Entity entity) {
        if (entity == null) {
            return null;
        }

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
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setApplicationReceivedDate(entity.getApplicationReceivedDate());
        dto.setChangedValue(entity.getChangedValue());
        return dto;
    }

    @Override
    public List<RegisterObjection_Dto> getList() {
        List<RegisterObjection_Entity> all = repository.findAll();
        List<RegisterObjection_Dto> collect = all.stream().map(RegisterObjection_MasterServiceImpl::toDto).toList();
        return collect;
    }

    private static class ObjectionReceiptRowMapper implements RowMapper<AssessmentResultsDto> {
        @Override
        public AssessmentResultsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssessmentResultsDto dto = new AssessmentResultsDto();

            // Core property/notice details
            dto.setPdFinalpropnoVc(rs.getString("rg_finalpropno_vc"));
            dto.setPdOwnernameVc(rs.getString("rg_ownername_vc"));
            dto.setPdOldpropnoVc(rs.getString("rg_oldpropno_vc"));
            dto.setPdWardI(rs.getString("rg_wardno_i") != null ? rs.getString("rg_wardno_i") : null);
            dto.setPdZoneI(rs.getString("rg_zoneno_i") != null ? rs.getString("rg_zoneno_i") : null);
            dto.setPdNoticenoVc(rs.getString("notice_no"));

            // Objection specific fields
            dto.setSnNoticeId(rs.getString("rg_applicationno_vc"));   // Application no
            dto.setPreviousAssessmentDateDt(rs.getString("rg_applicationreceiveddate_dt"));
            dto.setCurrentAssessmentDateDt(rs.getString("rg_objectiondate_dt"));

            // Additional objection data
            dto.setWarnings(List.of(
                    "Reasons: " + rs.getString("rg_reasons_vc"),
                    "Others: " + rs.getString("rg_others_vc"),
                    "Respondent: " + rs.getString("rg_respondent_vc"),
                    "Phone: " + rs.getString("rg_phoneno_vc")
            ));

            return dto;
        }
    }





}
