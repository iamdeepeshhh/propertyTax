package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.PropertyTaxDetailArrears_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.PropertyTaxDetailArrears_MasterDto.PropertyTaxDetailArrears_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.PropertyTaxDetailArrears_MasterEntity.PropertyTaxDetailArrears_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.PropertyTaxDetailArrears_MasterRepository.PropertyTaxDetailArrears_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyTaxDetailArrears_MasterServiceImpl implements PropertyTaxDetailArrears_MasterService {
    @Autowired
    private PropertyTaxDetailArrears_MasterRepository repository;

    @Override
    public PropertyTaxDetailArrears_MasterDto saveArrears(PropertyTaxDetailArrears_MasterDto dto) {
        // Upsert by unique key: newPropertyNo + financialYear
        PropertyTaxDetailArrears_MasterEntity existing = null;
        try {
            if (dto != null && dto.getNewPropertyNo() != null && dto.getFinancialYear() != null) {
                existing = repository.findByNewPropertyNoAndFinancialYear(dto.getNewPropertyNo(), dto.getFinancialYear());
            }
        } catch (Exception ignored) {}

        PropertyTaxDetailArrears_MasterEntity entity = toEntity(dto);
        if (existing != null) {
            // preserve DB id and timestamps for update
            entity.setArrearsId(existing.getArrearsId());
            if (entity.getCreatedAt() == null) entity.setCreatedAt(existing.getCreatedAt());
        }
        repository.save(entity);
        return toDto(entity);
    }

    @Override
    public List<PropertyTaxDetailArrears_MasterDto> getAll() {
        List<PropertyTaxDetailArrears_MasterEntity> all = repository.findAll();
        return all.stream().map(PropertyTaxDetailArrears_MasterServiceImpl::toDto).toList();
    }

    @Override
    public PropertyTaxDetailArrears_MasterDto getSingleArrearsTaxDetails(String newPropertyNo) {
        return toDto(repository.findByNewPropertyNo(newPropertyNo));
    }

    @Override
    public PropertyTaxDetailArrears_MasterDto getArrearsByPropertyAndYear(String newPropertyNo, String financialYear) {
        return toDto(repository.findByNewPropertyNoAndFinancialYear(newPropertyNo, financialYear));
    }

    // 🔹 ENTITY ➜ DTO
    public static PropertyTaxDetailArrears_MasterDto toDto(PropertyTaxDetailArrears_MasterEntity entity) {
        if (entity == null) return null;

        return PropertyTaxDetailArrears_MasterDto.builder()
                .arrearsId(entity.getArrearsId())
                .ward(entity.getWard())
                .zone(entity.getZone())
                .surveyPropertyNo(entity.getSurveyPropertyNo())
                .finalPropertyNo(entity.getFinalPropertyNo())
                .oldPropertyNo(entity.getOldPropertyNo())
                .newPropertyNo(entity.getNewPropertyNo())
                .arrFinalPropertyNo(entity.getArrFinalPropertyNo())
                .ownerName(entity.getOwnerName())
                .addNewOwnerName(entity.getAddNewOwnerName())
                .occupierName(entity.getOccupierName())
                .financialYear(entity.getFinancialYear())
                .ptFinalYear(entity.getPtFinalYear())
                .ptStatus(entity.getPtStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())

                // 💰 Arrears Taxes
                .propertyTax(entity.getPropertyTax())
                .educationTax(entity.getEducationTax())
                .treeTax(entity.getTreeTax())
                .environmentTax(entity.getEnvironmentTax())
                .fireTax(entity.getFireTax())
                .electricityTax(entity.getElectricityTax())
                .userCharges(entity.getUserCharges())
                .lightTax(entity.getLightTax())
                .cleanTax(entity.getCleanTax())
                .interest(entity.getInterest())
                .waterTax(entity.getWaterTax())
                .egcTax(entity.getEgcTax())
                .totalTax(entity.getTotalTax())
                .noticeFee(entity.getNoticeFee())
                .miscellaneousTax(entity.getMiscellaneousTax())
                .otherTax(entity.getOtherTax())
                .penalty(entity.getPenalty())
                .discount(entity.getDiscount())

                // 📊 After Hearing Taxes
                .ptPropertyTax(entity.getPtPropertyTax())
                .ptEduTax(entity.getPtEduTax())
                .ptEgcTax(entity.getPtEgcTax())
                .ptTreeTax(entity.getPtTreeTax())
                .ptLightTax(entity.getPtLightTax())
                .ptFireTax(entity.getPtFireTax())
                .ptCleanTax(entity.getPtCleanTax())
                .ptEnvTax(entity.getPtEnvTax())
                .ptWaterTax(entity.getPtWaterTax())
                .ptUserCharges(entity.getPtUserCharges())
                .ptMiscellaneousCharges(entity.getPtMiscellaneousCharges())
                .ptServiceCharges(entity.getPtServiceCharges())
                .ptMunicipalEduTax(entity.getPtMunicipalEduTax())
                .ptSpecialConservancyTax(entity.getPtSpecialConservancyTax())
                .ptSpecialEduTax(entity.getPtSpecialEduTax())
                .ptStreetTax(entity.getPtStreetTax())
                .ptSewerageTax(entity.getPtSewerageTax())
                .ptSewerageBenefitTax(entity.getPtSewerageBenefitTax())
                .ptWaterBenefitTax(entity.getPtWaterBenefitTax())
                .ptFinalRatableValue(entity.getPtFinalRatableValue())
                .ptFinalTax(entity.getPtFinalTax())

                // 🔢 Dynamic Taxes
                .ptTax1(entity.getPtTax1())
                .ptTax2(entity.getPtTax2())
                .ptTax3(entity.getPtTax3())
                .ptTax4(entity.getPtTax4())
                .ptTax5(entity.getPtTax5())
                .ptTax6(entity.getPtTax6())
                .ptTax7(entity.getPtTax7())
                .ptTax8(entity.getPtTax8())
                .ptTax9(entity.getPtTax9())
                .ptTax10(entity.getPtTax10())
                .ptTax11(entity.getPtTax11())
                .ptTax12(entity.getPtTax12())
                .ptTax13(entity.getPtTax13())
                .ptTax14(entity.getPtTax14())
                .ptTax15(entity.getPtTax15())
                .ptTax16(entity.getPtTax16())
                .ptTax17(entity.getPtTax17())
                .ptTax18(entity.getPtTax18())
                .ptTax19(entity.getPtTax19())
                .ptTax20(entity.getPtTax20())
                .ptTax21(entity.getPtTax21())
                .ptTax22(entity.getPtTax22())
                .ptTax23(entity.getPtTax23())
                .ptTax24(entity.getPtTax24())
                .ptTax25(entity.getPtTax25())
                .build();
    }

    // 🔹 DTO ➜ ENTITY
    public static PropertyTaxDetailArrears_MasterEntity toEntity(PropertyTaxDetailArrears_MasterDto dto) {
        if (dto == null) return null;

        PropertyTaxDetailArrears_MasterEntity entity = new PropertyTaxDetailArrears_MasterEntity();

        entity.setArrearsId(dto.getArrearsId());
        entity.setWard(dto.getWard());
        entity.setZone(dto.getZone());
        entity.setSurveyPropertyNo(dto.getSurveyPropertyNo());
        entity.setFinalPropertyNo(dto.getFinalPropertyNo());
        entity.setOldPropertyNo(dto.getOldPropertyNo());
        entity.setNewPropertyNo(dto.getNewPropertyNo());
        entity.setArrFinalPropertyNo(dto.getArrFinalPropertyNo());
        entity.setOwnerName(dto.getOwnerName());
        entity.setAddNewOwnerName(dto.getAddNewOwnerName());
        entity.setOccupierName(dto.getOccupierName());
        entity.setFinancialYear(dto.getFinancialYear());
        entity.setPtFinalYear(dto.getPtFinalYear());
        entity.setPtStatus(dto.getPtStatus());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        // 💰 Arrears Taxes
        entity.setPropertyTax(dto.getPropertyTax());
        entity.setEducationTax(dto.getEducationTax());
        entity.setTreeTax(dto.getTreeTax());
        entity.setEnvironmentTax(dto.getEnvironmentTax());
        entity.setFireTax(dto.getFireTax());
        entity.setElectricityTax(dto.getElectricityTax());
        entity.setUserCharges(dto.getUserCharges());
        entity.setLightTax(dto.getLightTax());
        entity.setCleanTax(dto.getCleanTax());
        entity.setInterest(dto.getInterest());
        entity.setWaterTax(dto.getWaterTax());
        entity.setEgcTax(dto.getEgcTax());
        entity.setTotalTax(dto.getTotalTax());
        entity.setNoticeFee(dto.getNoticeFee());
        entity.setMiscellaneousTax(dto.getMiscellaneousTax());
        entity.setOtherTax(dto.getOtherTax());
        entity.setPenalty(dto.getPenalty());
        entity.setDiscount(dto.getDiscount());

        // 📊 After Hearing
        entity.setPtPropertyTax(dto.getPtPropertyTax());
        entity.setPtEduTax(dto.getPtEduTax());
        entity.setPtEgcTax(dto.getPtEgcTax());
        entity.setPtTreeTax(dto.getPtTreeTax());
        entity.setPtLightTax(dto.getPtLightTax());
        entity.setPtFireTax(dto.getPtFireTax());
        entity.setPtCleanTax(dto.getPtCleanTax());
        entity.setPtEnvTax(dto.getPtEnvTax());
        entity.setPtWaterTax(dto.getPtWaterTax());
        entity.setPtUserCharges(dto.getPtUserCharges());
        entity.setPtMiscellaneousCharges(dto.getPtMiscellaneousCharges());
        entity.setPtServiceCharges(dto.getPtServiceCharges());
        entity.setPtMunicipalEduTax(dto.getPtMunicipalEduTax());
        entity.setPtSpecialConservancyTax(dto.getPtSpecialConservancyTax());
        entity.setPtSpecialEduTax(dto.getPtSpecialEduTax());
        entity.setPtStreetTax(dto.getPtStreetTax());
        entity.setPtSewerageTax(dto.getPtSewerageTax());
        entity.setPtSewerageBenefitTax(dto.getPtSewerageBenefitTax());
        entity.setPtWaterBenefitTax(dto.getPtWaterBenefitTax());
        entity.setPtFinalRatableValue(dto.getPtFinalRatableValue());
        entity.setPtFinalTax(dto.getPtFinalTax());

        // 🔢 Dynamic Taxes
        entity.setPtTax1(dto.getPtTax1());
        entity.setPtTax2(dto.getPtTax2());
        entity.setPtTax3(dto.getPtTax3());
        entity.setPtTax4(dto.getPtTax4());
        entity.setPtTax5(dto.getPtTax5());
        entity.setPtTax6(dto.getPtTax6());
        entity.setPtTax7(dto.getPtTax7());
        entity.setPtTax8(dto.getPtTax8());
        entity.setPtTax9(dto.getPtTax9());
        entity.setPtTax10(dto.getPtTax10());
        entity.setPtTax11(dto.getPtTax11());
        entity.setPtTax12(dto.getPtTax12());
        entity.setPtTax13(dto.getPtTax13());
        entity.setPtTax14(dto.getPtTax14());
        entity.setPtTax15(dto.getPtTax15());
        entity.setPtTax16(dto.getPtTax16());
        entity.setPtTax17(dto.getPtTax17());
        entity.setPtTax18(dto.getPtTax18());
        entity.setPtTax19(dto.getPtTax19());
        entity.setPtTax20(dto.getPtTax20());
        entity.setPtTax21(dto.getPtTax21());
        entity.setPtTax22(dto.getPtTax22());
        entity.setPtTax23(dto.getPtTax23());
        entity.setPtTax24(dto.getPtTax24());
        entity.setPtTax25(dto.getPtTax25());

        return entity;
    }
}
