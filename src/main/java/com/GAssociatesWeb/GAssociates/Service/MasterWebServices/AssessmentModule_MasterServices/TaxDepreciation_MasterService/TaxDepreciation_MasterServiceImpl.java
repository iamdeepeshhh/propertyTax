package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxDepreciation_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxDepreciation_MasterDto.TaxDepreciation_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxDepreciation_MasterEntity.TaxDepreciation_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxDepreciation_MasterRepository.TaxDepreciation_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaxDepreciation_MasterServiceImpl implements TaxDepreciation_MasterService {

    @Autowired
    private TaxDepreciation_MasterRepository repository;

    @Override
    public List<TaxDepreciation_MasterDto> addDepreciationRates(Map<String, Object> payload) {
        List<TaxDepreciation_MasterDto> dtos = new ArrayList<>();
        String constructionClass = (String) payload.get("constructionClassVc");
        List<Map<String, String>> dynamicRows = (List<Map<String, String>>) payload.get("dynamicRows");

        for (Map<String, String> row : dynamicRows) {
            TaxDepreciation_MasterDto dto = new TaxDepreciation_MasterDto();
            dto.setConstructionClassVc(constructionClass);
            dto.setMinAgeI(row.get("minAgeI"));
            dto.setMaxAgeI(row.get("maxAgeI"));
            dto.setDepreciationPercentageI(row.get("depreciationPercentageI"));
            dtos.add(dto);
        }

        List<TaxDepreciation_MasterDto> savedDtos = new ArrayList<>();
        for (TaxDepreciation_MasterDto dto : dtos) {
            TaxDepreciation_MasterEntity entity = new TaxDepreciation_MasterEntity();
            entity.setConstructionClassVc(dto.getConstructionClassVc());
            entity.setMinAgeI(dto.getMinAgeI());
            entity.setMaxAgeI(dto.getMaxAgeI());
            entity.setDepreciationPercentageI(dto.getDepreciationPercentageI());

            entity = repository.save(entity);

            dto.setId(entity.getId());
            savedDtos.add(dto);
        }

        return savedDtos;
    }

    @Override
    public List<TaxDepreciation_MasterDto> getAllDepreciationRates() {
        return repository.findAll().stream().map(entity -> {
            TaxDepreciation_MasterDto dto = new TaxDepreciation_MasterDto();
            dto.setId(entity.getId());
            dto.setConstructionClassVc(entity.getConstructionClassVc());
            dto.setMinAgeI(entity.getMinAgeI());
            dto.setMaxAgeI(entity.getMaxAgeI());
            dto.setDepreciationPercentageI(entity.getDepreciationPercentageI());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteDepreciationRate(Long id) {
        repository.deleteById(id);
    }
}
