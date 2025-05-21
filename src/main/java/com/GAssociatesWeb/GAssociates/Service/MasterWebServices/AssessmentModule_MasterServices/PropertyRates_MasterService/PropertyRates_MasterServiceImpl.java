package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.PropertyRates_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.PropertyRates_MasterDto.PropertyRates_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxDepreciation_MasterDto.TaxDepreciation_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.PropertyRates_MasterEntity.PropertyRates_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.PropertyRates_MasterRepository.PropertyRates_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PropertyRates_MasterServiceImpl implements PropertyRates_MasterService {
    @Autowired
    private PropertyRates_MasterRepository propertyRatesRepository;

    @Override
    public List<PropertyRates_MasterDto> getAllPropertyRates() {
        List<PropertyRates_MasterEntity> propertyRates = propertyRatesRepository.findAll();
        return propertyRates.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public PropertyRates_MasterDto getPropertyRateById(Long id) {
        PropertyRates_MasterEntity propertyRate = propertyRatesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PropertyRate not found with id :" + id));
        return convertToDto(propertyRate);
    }

    @Override
    public List<PropertyRates_MasterDto> addPropertyRate(Map<String, Object> payload) {
        List<PropertyRates_MasterDto> dtos = new ArrayList<>();
        String constructionClass = (String) payload.get("constructionTypeVc");
        List<Map<String, String>> dynamicRows = (List<Map<String, String>>) payload.get("dynamicRows");

        for(Map<String,String> row: dynamicRows){
            PropertyRates_MasterDto dto = new PropertyRates_MasterDto();
            dto.setConstructionTypeVc(constructionClass);
            dto.setTaxRateZoneI(row.get("taxRateZoneI"));
            dto.setRateI(row.get("rateI"));
            dtos.add(dto);
        }
        List<PropertyRates_MasterDto> savedDtos = new ArrayList<>();
        for (PropertyRates_MasterDto dto : dtos) {
            PropertyRates_MasterEntity entity = new PropertyRates_MasterEntity();
            entity.setConstructionTypeVc(dto.getConstructionTypeVc());
            entity.setTaxRateZoneI(dto.getTaxRateZoneI());
            entity.setRateI(dto.getRateI());

            entity = propertyRatesRepository.save(entity);

            dto.setId(entity.getId());
            savedDtos.add(dto);
        }

        return savedDtos;
    }

    @Override
    public PropertyRates_MasterDto updatePropertyRate(Long id, PropertyRates_MasterDto propertyRateDetails) {
        PropertyRates_MasterEntity propertyRate = propertyRatesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PropertyRate not found with id :" + id));

        propertyRate.setConstructionTypeVc(propertyRateDetails.getConstructionTypeVc());
        propertyRate.setTaxRateZoneI(propertyRateDetails.getTaxRateZoneI());
        propertyRate.setRateI(propertyRateDetails.getRateI());

        PropertyRates_MasterEntity updatedPropertyRate = propertyRatesRepository.save(propertyRate);
        return convertToDto(updatedPropertyRate);
    }

    @Override
    public void deletePropertyRate(Long id) {
        PropertyRates_MasterEntity propertyRate = propertyRatesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PropertyRate not found with id :" + id));
        propertyRatesRepository.delete(propertyRate);
    }

    private PropertyRates_MasterDto convertToDto(PropertyRates_MasterEntity entity) {
        PropertyRates_MasterDto dto = new PropertyRates_MasterDto();
        dto.setId(entity.getId());
        dto.setConstructionTypeVc(entity.getConstructionTypeVc());
        dto.setTaxRateZoneI(entity.getTaxRateZoneI());
        dto.setRateI(entity.getRateI());
        return dto;
    }

    private PropertyRates_MasterEntity convertToEntity(PropertyRates_MasterDto dto) {
        PropertyRates_MasterEntity entity = new PropertyRates_MasterEntity();
        entity.setId(dto.getId());
        entity.setConstructionTypeVc(dto.getConstructionTypeVc());
        entity.setTaxRateZoneI(dto.getTaxRateZoneI());
        entity.setRateI(dto.getRateI());
        return entity;
    }
}
