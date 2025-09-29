package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.ConolidatedTaxes_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.ConsolidatedTaxes_MasterDto.ConsolidatedTaxes_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.ConsolidatedTaxes_MasterEntity.ConsolidatedTaxes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.ConsolidatedTaxes_MasterRepository.ConsolidatedTaxes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.ReportConfigs_MasterRepository.ReportTaxes_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsolidatedTaxes_MasterServiceImpl implements ConsolidatedTaxes_MasterService {

    private final ConsolidatedTaxes_MasterRepository consolidatedTaxes_masterRepository;
    private final SequenceService sequenceService;
    private final ReportTaxes_MasterRepository reportTaxes_masterRepository;


    @Autowired
    public ConsolidatedTaxes_MasterServiceImpl(ConsolidatedTaxes_MasterRepository consolidatedTaxes_masterRepository, SequenceService sequenceService, ReportTaxes_MasterRepository reportTaxes_MasterRepository) {
        this.consolidatedTaxes_masterRepository = consolidatedTaxes_masterRepository;
        this.sequenceService = sequenceService;

        reportTaxes_masterRepository = reportTaxes_MasterRepository;
    }

    @Override
    public List<ConsolidatedTaxes_MasterDto> getAllTaxes() {
        return consolidatedTaxes_masterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConsolidatedTaxes_MasterDto findByTaxName(String taxNameVc) {
        ConsolidatedTaxes_MasterEntity entity = consolidatedTaxes_masterRepository.findByTaxNameVc(taxNameVc);
        return entity != null ? convertToDto(entity) : null;
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
        return consolidatedTaxes_masterRepository.findById(id)
        .map(existing -> {
            // Only update each field if a new value is provided
            if (taxDto.getTaxNameVc() != null && !taxDto.getTaxNameVc().trim().isEmpty()) {
                existing.setTaxNameVc(taxDto.getTaxNameVc().trim());
            }

            if (taxDto.getTaxRateFl() != null && !taxDto.getTaxRateFl().trim().isEmpty()) {
                existing.setTaxRateFl(taxDto.getTaxRateFl().trim());
            }

            if (taxDto.getApplicableonVc() != null && !taxDto.getApplicableonVc().trim().isEmpty()) {
                existing.setApplicableonVc(taxDto.getApplicableonVc().trim());
            }

            if (taxDto.getIsActiveBl() != null) {
                existing.setIsActiveBl(taxDto.getIsActiveBl());  // boolean, so no empty string issue

            }

            if (taxDto.getDescriptionVc() != null && !taxDto.getDescriptionVc().trim().isEmpty()) {
                existing.setDescriptionVc(taxDto.getDescriptionVc().trim());
            }

            if (taxDto.getTaxKeyL() != null) {
                existing.setTaxKeyL(taxDto.getTaxKeyL());  // Long, no trim needed
            }

            // persist and return the updated entity
            return convertToDto(consolidatedTaxes_masterRepository.save(existing));
        })
        .orElse(null);
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
        //fields added after dynamically population of taxes
        dto.setIsActiveBl(tax.getIsActiveBl());

        dto.setDescriptionVc(tax.getDescriptionVc());
        dto.setTaxKeyL(tax.getTaxKeyL());

        return dto;
    }

    private ConsolidatedTaxes_MasterEntity convertToEntity(ConsolidatedTaxes_MasterDto dto) {
        ConsolidatedTaxes_MasterEntity tax = new ConsolidatedTaxes_MasterEntity();
        tax.setTaxNameVc(dto.getTaxNameVc());
        tax.setTaxRateFl(dto.getTaxRateFl().trim());
        tax.setApplicableonVc(dto.getApplicableonVc());
        //fields added after dynamically population of taxes
        tax.setIsActiveBl(dto.getIsActiveBl());

        tax.setDescriptionVc(dto.getDescriptionVc());
        tax.setTaxKeyL(dto.getTaxKeyL());

        return tax;
    }
}
