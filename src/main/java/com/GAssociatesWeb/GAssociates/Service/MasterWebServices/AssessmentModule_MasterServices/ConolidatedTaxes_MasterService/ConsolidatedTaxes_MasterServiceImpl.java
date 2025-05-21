package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.ConolidatedTaxes_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.ConsolidatedTaxes_MasterDto.ConsolidatedTaxes_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.ConsolidatedTaxes_MasterEntity.ConsolidatedTaxes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.ConsolidatedTaxes_MasterRepository.ConsolidatedTaxes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsolidatedTaxes_MasterServiceImpl implements ConsolidatedTaxes_MasterService {

    private final ConsolidatedTaxes_MasterRepository consolidatedTaxes_masterRepository;
    private final SequenceService sequenceService;

    @Autowired
    public ConsolidatedTaxes_MasterServiceImpl(ConsolidatedTaxes_MasterRepository consolidatedTaxes_masterRepository, SequenceService sequenceService) {
        this.consolidatedTaxes_masterRepository = consolidatedTaxes_masterRepository;
        this.sequenceService = sequenceService;
    }

    @Override
    public List<ConsolidatedTaxes_MasterDto> getAllTaxes() {
        return consolidatedTaxes_masterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConsolidatedTaxes_MasterDto getTaxById(Long id) {
        return consolidatedTaxes_masterRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public ConsolidatedTaxes_MasterDto createTax(ConsolidatedTaxes_MasterDto taxDto) {
        ConsolidatedTaxes_MasterEntity tax = convertToEntity(taxDto);
        sequenceService.resetSequenceIfTableIsEmpty("consolidated_taxes_master","consolidated_taxes_master_id_seq");
        return convertToDto(consolidatedTaxes_masterRepository.save(tax));
    }

    @Override
    public ConsolidatedTaxes_MasterDto updateTax(Long id, ConsolidatedTaxes_MasterDto taxDto) {
        if (consolidatedTaxes_masterRepository.existsById(id)) {
            ConsolidatedTaxes_MasterEntity tax = convertToEntity(taxDto);
            tax.setId(id);
            return convertToDto(consolidatedTaxes_masterRepository.save(tax));
        }
        return null;
    }

    @Override
    public void deleteTax(Long id) {
        consolidatedTaxes_masterRepository.deleteById(id);
    }

    private ConsolidatedTaxes_MasterDto convertToDto(ConsolidatedTaxes_MasterEntity tax) {
        ConsolidatedTaxes_MasterDto dto = new ConsolidatedTaxes_MasterDto();
        dto.setId(tax.getId());
        dto.setTaxNameVc(tax.getTaxNameVc());
        dto.setTaxRateFl(tax.getTaxRateFl());
        dto.setApplicableonVc(tax.getApplicableonVc());
        return dto;
    }

    private ConsolidatedTaxes_MasterEntity convertToEntity(ConsolidatedTaxes_MasterDto dto) {
        ConsolidatedTaxes_MasterEntity tax = new ConsolidatedTaxes_MasterEntity();
        tax.setTaxNameVc(dto.getTaxNameVc());
        tax.setTaxRateFl(dto.getTaxRateFl().trim());
        tax.setApplicableonVc(dto.getApplicableonVc());
        return tax;
    }
}
