package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AgeFactor_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AgeFactor_MasterDto.AgeFactor_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AgeFactor_MasterEntity.AgeFactor_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AgeFactor_MasterRepository.AgeFactor_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgeFactor_MasterServiceImpl implements AgeFactor_MasterService {

    @Autowired
    private AgeFactor_MasterRepository ageFactorMasterRepository;
    @Autowired
    private SequenceService sequenceService;

    @Override
    public List<AgeFactor_MasterDto> getAllAgeFactors() {
        return ageFactorMasterRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public AgeFactor_MasterDto getAgeFactorById(Integer id) {
        return ageFactorMasterRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public AgeFactor_MasterDto createAgeFactor(AgeFactor_MasterDto ageFactorMasterDto) {
        AgeFactor_MasterEntity ageFactorMaster = convertToEntity(ageFactorMasterDto);
        sequenceService.resetSequenceIfTableIsEmpty("agefactor_master","agefactor_master_afm_agefactorid_i_seq");
        ageFactorMaster = ageFactorMasterRepository.save(ageFactorMaster);
        return convertToDto(ageFactorMaster);
    }

    @Override
    public AgeFactor_MasterDto updateAgeFactor(Integer id, AgeFactor_MasterDto ageFactorMasterDto) {
        AgeFactor_MasterEntity ageFactorMaster = convertToEntity(ageFactorMasterDto);
        ageFactorMaster.setAfm_agefactorid_i(id);
        ageFactorMaster = ageFactorMasterRepository.save(ageFactorMaster);
        return convertToDto(ageFactorMaster);
    }

    @Override
    public void deleteAgeFactor(Integer id) {
        ageFactorMasterRepository.deleteById(id);
    }

    private AgeFactor_MasterDto convertToDto(AgeFactor_MasterEntity ageFactorMaster) {
        AgeFactor_MasterDto ageFactorMasterDto = new AgeFactor_MasterDto();
        ageFactorMasterDto.setAfm_agefactorid_i(ageFactorMaster.getAfm_agefactorid_i());
        ageFactorMasterDto.setAfm_agefactornameeng_vc(ageFactorMaster.getAfm_agefactornameeng_vc());
        ageFactorMasterDto.setAfm_agefactornamell_vc(ageFactorMaster.getAfm_agefactornamell_vc());
        ageFactorMasterDto.setAfm_ageminage_vc(ageFactorMaster.getAfm_ageminage_vc());
        ageFactorMasterDto.setAfm_agemaxage_vc(ageFactorMaster.getAfm_agemaxage_vc());
        ageFactorMasterDto.setAfm_created_dt(ageFactorMaster.getAfm_created_dt());
        ageFactorMasterDto.setAfm_updated_dt(ageFactorMaster.getAfm_updated_dt());
        ageFactorMasterDto.setAfm_remarks_vc(ageFactorMaster.getAfm_remarks_vc());
        //ageFactorMasterDto.setId(ageFactorMaster.getId());
        return ageFactorMasterDto;
    }

    private AgeFactor_MasterEntity convertToEntity(AgeFactor_MasterDto ageFactorMasterDto) {
        AgeFactor_MasterEntity ageFactorMaster = new AgeFactor_MasterEntity();
        ageFactorMaster.setAfm_agefactornameeng_vc(ageFactorMasterDto.getAfm_agefactornameeng_vc());
        ageFactorMaster.setAfm_agefactornamell_vc(ageFactorMasterDto.getAfm_agefactornamell_vc());
        ageFactorMaster.setAfm_ageminage_vc(ageFactorMasterDto.getAfm_ageminage_vc());
        ageFactorMaster.setAfm_agemaxage_vc(ageFactorMasterDto.getAfm_agemaxage_vc());
        ageFactorMaster.setAfm_created_dt(ageFactorMasterDto.getAfm_created_dt());
        ageFactorMaster.setAfm_updated_dt(ageFactorMasterDto.getAfm_updated_dt());
        ageFactorMaster.setAfm_remarks_vc(ageFactorMasterDto.getAfm_remarks_vc());
        //ageFactorMaster.setId(ageFactorMasterDto.getId());
        return ageFactorMaster;
    }
}