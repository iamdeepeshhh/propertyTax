package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.ReportConfig_MasterDto.ReportTaxes_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.ReportConfigs_MasterModule.ReportTaxes_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.ReportConfigs_MasterRepository.ReportTaxes_MasterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportTaxesConfigServiceImpl implements ReportTaxesConfigService {

    private final ReportTaxes_MasterRepository repository;
    private final ModelMapper modelMapper;

    public ReportTaxesConfigServiceImpl(ReportTaxes_MasterRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ReportTaxes_MasterDto> getVisibleConfigsByTemplate(String templateVc) {
        List<ReportTaxes_MasterEntity> rows = repository.findByTemplateVcAndVisibleBIsTrueOrderBySequenceIAsc(templateVc);
        return rows.stream().map(e -> {
            ReportTaxes_MasterDto dto = new ReportTaxes_MasterDto();
            dto.setId(e.getId());
            dto.setStandardNameVc(e.getStandardNameVc());
            dto.setLocalNameVc(e.getLocalNameVc());
            dto.setSequenceI(e.getSequenceI());
            dto.setTaxKeyL(e.getTaxKeyL());
            dto.setParentTaxKeyL(e.getParentTaxKeyL());
//            dto.setIsParentBl(null); // not stored explicitly; can be inferred where needed
            dto.setShowTotalBl(e.getShowTotalB());
            dto.setTemplateVc(e.getTemplateVc());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ReportTaxes_MasterDto> getAllConfigsByTemplate(String templateVc) {
        List<ReportTaxes_MasterEntity> rows = repository.findByTemplateVcOrderBySequenceIAsc(templateVc);
        return rows.stream().map(e -> {
            ReportTaxes_MasterDto dto = new ReportTaxes_MasterDto();
            dto.setId(e.getId());
            dto.setStandardNameVc(e.getStandardNameVc());
            dto.setLocalNameVc(e.getLocalNameVc());
            dto.setSequenceI(e.getSequenceI());
            dto.setTaxKeyL(e.getTaxKeyL());
            dto.setParentTaxKeyL(e.getParentTaxKeyL());
//            dto.setIsParentBl(null);
            dto.setShowTotalBl(e.getShowTotalB());
            dto.setTemplateVc(e.getTemplateVc());
            dto.setVisibleB(e.getVisibleB());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ReportTaxes_MasterDto> getAllConfigs() {
        return repository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public void saveOrUpdateReportConfigTaxes(List<ReportTaxes_MasterDto> configs) {
        for (ReportTaxes_MasterDto dto : configs) {
            // Check if record exists
            ReportTaxes_MasterEntity existing =
                    repository.findByTaxKeyLAndTemplateVc(dto.getTaxKeyL(), dto.getTemplateVc())
                            .orElse(null);

            if (existing != null) {
                // Update fields
                existing.setStandardNameVc(dto.getStandardNameVc());
                existing.setLocalNameVc(dto.getLocalNameVc());
                existing.setSequenceI(dto.getSequenceI());
                existing.setParentTaxKeyL(dto.getParentTaxKeyL());
                existing.setShowTotalB(dto.getShowTotalBl());
                existing.setVisibleB(dto.getVisibleB());
                repository.save(existing);
            } else {
                // Insert new
                ReportTaxes_MasterEntity entity = convertToEntity(dto);
                repository.save(entity);
            }
        }
    }

    @Override
    public void insertMissingOnly(List<ReportTaxes_MasterDto> configs) {
        for (ReportTaxes_MasterDto dto : configs) {
            boolean exists = repository.findByTaxKeyLAndTemplateVc(dto.getTaxKeyL(), dto.getTemplateVc()).isPresent();
            if (!exists) {
                ReportTaxes_MasterEntity entity = convertToEntity(dto);
                repository.save(entity);
            }
        }
    }

    private ReportTaxes_MasterEntity convertToEntity(ReportTaxes_MasterDto dto) {
        return new ReportTaxes_MasterEntity(
                null, // ID auto-generated
                dto.getStandardNameVc(),
                dto.getLocalNameVc(),
                dto.getSequenceI(),
                dto.getTaxKeyL(),
                dto.getParentTaxKeyL(),
                dto.getShowTotalBl(),
                dto.getVisibleB(),
                dto.getTemplateVc()
        );
    }

    private ReportTaxes_MasterDto convertToDto(ReportTaxes_MasterEntity e) {
        ReportTaxes_MasterDto dto = new ReportTaxes_MasterDto();
        dto.setId(e.getId());
        dto.setStandardNameVc(e.getStandardNameVc());
        dto.setLocalNameVc(e.getLocalNameVc());
        dto.setSequenceI(e.getSequenceI());
        dto.setTaxKeyL(e.getTaxKeyL());
        dto.setParentTaxKeyL(e.getParentTaxKeyL());
        dto.setShowTotalBl(e.getShowTotalB());
        dto.setTemplateVc(e.getTemplateVc());
        dto.setVisibleB(e.getVisibleB());
        return dto;
    }
}
