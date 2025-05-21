package com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.AdditionalInfo_Service;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.AdditionalInfo_Dto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.AdditionalInfo_Entity.AdditionalInfo_Entity;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.AdditionalInfo_Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class AdditionalInfo_ServiceImpl implements AdditionalInfo_Service{
    @Autowired
    private AdditionalInfo_Repository additionalInfoRepository;

    @Override
    public AdditionalInfo_Dto saveAdditionalInfo(AdditionalInfo_Dto dto) {
        AdditionalInfo_Entity entity = convertToEntity(dto);
        entity = additionalInfoRepository.save(entity);
        return convertToDto(entity);
    }

    @Override
    public List<AdditionalInfo_Dto> getAllAdditionalInfos() {
        return additionalInfoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdditionalInfo_Dto getAdditionalInfoById(Integer id) {
        AdditionalInfo_Entity entity = additionalInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AdditionalInfo not found for this id :: " + id));
        return convertToDto(entity);
    }

    @Override
    public AdditionalInfo_Dto updateAdditionalInfo(Integer id, AdditionalInfo_Dto dto) {
        AdditionalInfo_Entity entity = additionalInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AdditionalInfo not found for this id :: " + id));

        // Update entity from dto
        // entity.setName(dto.getName());

        entity = additionalInfoRepository.save(entity);
        return convertToDto(entity);
    }

    @Override
    public void deleteAdditionalInfo(Integer id) {
        AdditionalInfo_Entity entity = additionalInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AdditionalInfo not found for this id :: " + id));
        additionalInfoRepository.delete(entity);
    }


    private AdditionalInfo_Entity convertToEntity(AdditionalInfo_Dto dto) {
        // Conversion logic
        AdditionalInfo_Entity entity = new AdditionalInfo_Entity();
        entity.setId(dto.getId());
        entity.setPaPropertyidVc(dto.getPaPropertyidVc());
        entity.setPaNameVc(dto.getPaNameVc());
        entity.setPaGenderVc(dto.getPaGenderVc());
        entity.setPaAgeIn(dto.getPaAgeIn());
        entity.setPaRelationVc(dto.getPaRelationVc());
        entity.setPaQualificationVc(dto.getPaQualificationVc());
        entity.setPaOccupationVc(dto.getPaOccupationVc());
        entity.setPaCitysurveynoVc(dto.getPaCitysurveynoVc());
        entity.setPaAdultnoIn(dto.getPaAdultnoIn());
        entity.setPaChildrennoIn(dto.getPaChildrennoIn());
        entity.setPaCtaxstatusVc(dto.getPaCtaxstatusVc());
        entity.setPaCtaxamountIn(dto.getPaCtaxamountIn());
        entity.setPaNroadnameVc(dto.getPaNroadnameVc());
        entity.setPaRoadtypeVc(dto.getPaRoadtypeVc());
        entity.setPaNoofpersonsVc(dto.getPaNoofpersonsVc());
        entity.setPaPrelation1Vc(dto.getPaPrelation1Vc());
        entity.setPaPname1Vc(dto.getPaPname1Vc());
        entity.setPaPgender1Vc(dto.getPaPgender1Vc());
        entity.setPaPoccupation1Vc(dto.getPaPoccupation1Vc());
        entity.setPaPage1Vc(dto.getPaPage1Vc());
        entity.setPaPqualification1Vc(dto.getPaPqualification1Vc());
        entity.setPaPrelation2Vc(dto.getPaPrelation2Vc());
        entity.setPaPname2Vc(dto.getPaPname2Vc());
        entity.setPaPgender2Vc(dto.getPaPgender2Vc());
        entity.setPaPoccupation2Vc(dto.getPaPoccupation2Vc());
        entity.setPaPage2Vc(dto.getPaPage2Vc());
        entity.setPaPqualification2Vc(dto.getPaPqualification2Vc());
        entity.setPaPrelation3Vc(dto.getPaPrelation3Vc());
        entity.setPaPname3Vc(dto.getPaPname3Vc());
        entity.setPaPgender3Vc(dto.getPaPgender3Vc());
        entity.setPaPoccupation3Vc(dto.getPaPoccupation3Vc());
        entity.setPaPage3Vc(dto.getPaPage3Vc());
        entity.setPaPqualification3Vc(dto.getPaPqualification3Vc());
        entity.setPaPrelation4Vc(dto.getPaPrelation4Vc());
        entity.setPaPname4Vc(dto.getPaPname4Vc());
        entity.setPaPgender4Vc(dto.getPaPgender4Vc());
        entity.setPaPoccupation4Vc(dto.getPaPoccupation4Vc());
        entity.setPaPage4Vc(dto.getPaPage4Vc());
        entity.setPaPqualification4Vc(dto.getPaPqualification4Vc());
        entity.setPaPrelation5Vc(dto.getPaPrelation5Vc());
        entity.setPaPname5Vc(dto.getPaPname5Vc());
        entity.setPaPgender5Vc(dto.getPaPgender5Vc());
        entity.setPaPoccupation5Vc(dto.getPaPoccupation5Vc());
        entity.setPaPage5Vc(dto.getPaPage5Vc());
        entity.setPaPqualification5Vc(dto.getPaPqualification5Vc());
        entity.setPaPrelation6Vc(dto.getPaPrelation6Vc());
        entity.setPaPname6Vc(dto.getPaPname6Vc());
        entity.setPaPgender6Vc(dto.getPaPgender6Vc());
        entity.setPaPoccupation6Vc(dto.getPaPoccupation6Vc());
        entity.setPaPage6Vc(dto.getPaPage6Vc());
        entity.setPaPqualification6Vc(dto.getPaPqualification6Vc());
        entity.setPaPrelation7Vc(dto.getPaPrelation7Vc());
        entity.setPaPname7Vc(dto.getPaPname7Vc());
        entity.setPaPgender7Vc(dto.getPaPgender7Vc());
        entity.setPaPoccupation7Vc(dto.getPaPoccupation7Vc());
        entity.setPaPage7Vc(dto.getPaPage7Vc());
        entity.setPaPqualification7Vc(dto.getPaPqualification7Vc());
        entity.setPaPrelation8Vc(dto.getPaPrelation8Vc());
        entity.setPaPname8Vc(dto.getPaPname8Vc());
        entity.setPaPgender8Vc(dto.getPaPgender8Vc());
        entity.setPaPoccupation8Vc(dto.getPaPoccupation8Vc());
        entity.setPaPage8Vc(dto.getPaPage8Vc());
        entity.setPaPqualification8Vc(dto.getPaPqualification8Vc());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    private AdditionalInfo_Dto convertToDto(AdditionalInfo_Entity entity) {
        // Conversion logic
        AdditionalInfo_Dto dto = new AdditionalInfo_Dto();
        dto.setId(entity.getId());
        dto.setPaPropertyidVc(entity.getPaPropertyidVc());
        dto.setPaNameVc(entity.getPaNameVc());
        dto.setPaGenderVc(entity.getPaGenderVc());
        dto.setPaAgeIn(entity.getPaAgeIn());
        dto.setPaRelationVc(entity.getPaRelationVc());
        dto.setPaQualificationVc(entity.getPaQualificationVc());
        dto.setPaOccupationVc(entity.getPaOccupationVc());
        dto.setPaCitysurveynoVc(entity.getPaCitysurveynoVc());
        dto.setPaAdultnoIn(entity.getPaAdultnoIn());
        dto.setPaChildrennoIn(entity.getPaChildrennoIn());
        dto.setPaCtaxstatusVc(entity.getPaCtaxstatusVc());
        dto.setPaCtaxamountIn(entity.getPaCtaxamountIn());
        dto.setPaNroadnameVc(entity.getPaNroadnameVc());
        dto.setPaRoadtypeVc(entity.getPaRoadtypeVc());
        dto.setPaNoofpersonsVc(entity.getPaNoofpersonsVc());
        dto.setPaPrelation1Vc(entity.getPaPrelation1Vc());
        dto.setPaPname1Vc(entity.getPaPname1Vc());
        dto.setPaPgender1Vc(entity.getPaPgender1Vc());
        dto.setPaPoccupation1Vc(entity.getPaPoccupation1Vc());
        dto.setPaPage1Vc(entity.getPaPage1Vc());
        dto.setPaPqualification1Vc(entity.getPaPqualification1Vc());
        dto.setPaPrelation2Vc(entity.getPaPrelation2Vc());
        dto.setPaPname2Vc(entity.getPaPname2Vc());
        dto.setPaPgender2Vc(entity.getPaPgender2Vc());
        dto.setPaPoccupation2Vc(entity.getPaPoccupation2Vc());
        dto.setPaPage2Vc(entity.getPaPage2Vc());
        dto.setPaPqualification2Vc(entity.getPaPqualification2Vc());
        dto.setPaPrelation3Vc(entity.getPaPrelation3Vc());
        dto.setPaPname3Vc(entity.getPaPname3Vc());
        dto.setPaPgender3Vc(entity.getPaPgender3Vc());
        dto.setPaPoccupation3Vc(entity.getPaPoccupation3Vc());
        dto.setPaPage3Vc(entity.getPaPage3Vc());
        dto.setPaPqualification3Vc(entity.getPaPqualification3Vc());
        dto.setPaPrelation4Vc(entity.getPaPrelation4Vc());
        dto.setPaPname4Vc(entity.getPaPname4Vc());
        dto.setPaPgender4Vc(entity.getPaPgender4Vc());
        dto.setPaPoccupation4Vc(entity.getPaPoccupation4Vc());
        dto.setPaPage4Vc(entity.getPaPage4Vc());
        dto.setPaPqualification4Vc(entity.getPaPqualification4Vc());
        dto.setPaPrelation5Vc(entity.getPaPrelation5Vc());
        dto.setPaPname5Vc(entity.getPaPname5Vc());
        dto.setPaPgender5Vc(entity.getPaPgender5Vc());
        dto.setPaPoccupation5Vc(entity.getPaPoccupation5Vc());
        dto.setPaPage5Vc(entity.getPaPage5Vc());
        dto.setPaPqualification5Vc(entity.getPaPqualification5Vc());
        dto.setPaPrelation6Vc(entity.getPaPrelation6Vc());
        dto.setPaPname6Vc(entity.getPaPname6Vc());
        dto.setPaPgender6Vc(entity.getPaPgender6Vc());
        dto.setPaPoccupation6Vc(entity.getPaPoccupation6Vc());
        dto.setPaPage6Vc(entity.getPaPage6Vc());
        dto.setPaPqualification6Vc(entity.getPaPqualification6Vc());
        dto.setPaPrelation7Vc(entity.getPaPrelation7Vc());
        dto.setPaPname7Vc(entity.getPaPname7Vc());
        dto.setPaPgender7Vc(entity.getPaPgender7Vc());
        dto.setPaPoccupation7Vc(entity.getPaPoccupation7Vc());
        dto.setPaPage7Vc(entity.getPaPage7Vc());
        dto.setPaPqualification7Vc(entity.getPaPqualification7Vc());
        dto.setPaPrelation8Vc(entity.getPaPrelation8Vc());
        dto.setPaPname8Vc(entity.getPaPname8Vc());
        dto.setPaPgender8Vc(entity.getPaPgender8Vc());
        dto.setPaPoccupation8Vc(entity.getPaPoccupation8Vc());
        dto.setPaPage8Vc(entity.getPaPage8Vc());
        dto.setPaPqualification8Vc(entity.getPaPqualification8Vc());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}