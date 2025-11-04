package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.OrderSheet_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.OrderSheet_Dto.OrderSheet_Dto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.RegisterObjection_Dto.RegisterObjection_Dto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.ReportConfig_MasterDto.ReportTaxes_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_PropertyTaxDetailsEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.AfterHearing_MasterEntity.AfterHearing_ProposedRValuesEntity;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.RegisterObjection_Entity.RegisterObjection_Entity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingPropertyTaxDetails_MasterRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.RegisterObjection_MasterRepository.RegisterObjection_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.RegisterObjection_MasterService.RegisterObjection_MasterService;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxKeys;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices.ReportTaxesConfigService;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.AfterHearing_MasterRepository.AfterHearingProposedRvalues_MasterRepository;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AfterAssessment_Module.AfterHearing_Dto.AfterHearing_ProposedRValuesDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderSheet_MasterServiceImpl implements OrderSheet_MasterService {

    private final RegisterObjection_MasterService registerObjectionService;
    private final RegisterObjection_MasterRepository registerObjectionRepository;
    private final AfterHearingPropertyTaxDetails_MasterRepository afterHearingPropertyTaxRepo;
    private final ReportTaxesConfigService reportTaxesConfigService;
    private final AfterHearingProposedRvalues_MasterRepository afterHearingProposedRvRepo;

    @Override
    public OrderSheet_Dto getOrderSheetByNewPropertyNo(String newPropertyNo) {
        RegisterObjection_Dto ro = registerObjectionService.getObjection(newPropertyNo);
        if (ro == null) return null;
        OrderSheet_Dto dto = fromRegisterObjectionDto(ro);
        dto.setTaxKeyValueMap(buildTaxMap(newPropertyNo));
        dto.setProposedRValues(loadProposedRValues(newPropertyNo));
        return dto;
    }

    @Override
    public List<OrderSheet_Dto> getOrderSheetsByWard(Integer wardNo) {
        if (wardNo == null) return Collections.emptyList();
        List<RegisterObjection_Entity> rows = registerObjectionRepository.findByWardNo(wardNo);
        if (rows == null || rows.isEmpty()) return Collections.emptyList();
        return rows.stream()
                .filter(Objects::nonNull)
                .map(e -> {
                    OrderSheet_Dto dto = fromRegisterObjectionEntity(e);
                    dto.setTaxKeyValueMap(buildTaxMap(e.getNewPropertyNo()));
                    dto.setProposedRValues(loadProposedRValues(e.getNewPropertyNo()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private OrderSheet_Dto fromRegisterObjectionDto(RegisterObjection_Dto ro) {
        OrderSheet_Dto dto = new OrderSheet_Dto();
        dto.setWardNo(ro.getWardNo());
        dto.setZoneNo(ro.getZoneNo());
        dto.setSurveyNo(ro.getSurveyNo());
        dto.setFinalPropertyNo(ro.getFinalPropertyNo());
        dto.setNewPropertyNo(ro.getNewPropertyNo());
        dto.setOldPropertyNo(ro.getOldPropertyNo());
        dto.setOwnerName(ro.getOwnerName());
        dto.setApplicantName(ro.getRespondent());
        dto.setNoticeNo(ro.getNoticeNo());
        dto.setApplicationNo(ro.getApplicationNo());
        dto.setObjectionDate(ro.getObjectionDate());
        dto.setApplicationReceivedDate(ro.getApplicationReceivedDate());
        dto.setHearingDate(ro.getHearingDate());
        dto.setHearingTime(ro.getHearingTime());
        dto.setHearingStatus(ro.getHearingStatus());
        dto.setReasons(ro.getReasons());
        dto.setOthers(ro.getOthers());
        dto.setRespondent(ro.getRespondent());
        return dto;
    }

    private OrderSheet_Dto fromRegisterObjectionEntity(RegisterObjection_Entity e) {
        if (e == null) return null;
        OrderSheet_Dto dto = new OrderSheet_Dto();
        dto.setWardNo(e.getWardNo());
        dto.setZoneNo(e.getZoneNo());
        dto.setSurveyNo(e.getSurveyNo());
        dto.setFinalPropertyNo(e.getFinalPropertyNo());
        dto.setNewPropertyNo(e.getNewPropertyNo());
        dto.setOldPropertyNo(e.getOldPropertyNo());
        dto.setOwnerName(e.getOwnerName());
        dto.setApplicantName(e.getRespondent());
        dto.setNoticeNo(e.getNoticeNo());
        dto.setApplicationNo(e.getApplicationNo());
        dto.setObjectionDate(e.getObjectionDate());
        dto.setApplicationReceivedDate(e.getApplicationReceivedDate());
        dto.setHearingDate(e.getHearingDate());
        dto.setHearingTime(e.getHearingTime());
        dto.setHearingStatus(e.getHearingStatus());
        dto.setReasons(e.getReasons());
        dto.setOthers(e.getOthers());
        dto.setRespondent(e.getRespondent());
        return dto;
    }

    private Map<Long, Double> buildTaxMap(String newPropertyNo) {
        Map<Long, Double> map = new LinkedHashMap<>();
        List<ReportTaxes_MasterDto> cfgs = loadConfigs();
        AfterHearing_PropertyTaxDetailsEntity t = null;
        try {
            List<AfterHearing_PropertyTaxDetailsEntity> list = afterHearingPropertyTaxRepo.findByPtNewPropertyNoVc(newPropertyNo);
            if (list != null && !list.isEmpty()) t = list.get(0);
        } catch (Exception ignore) {}
        for (ReportTaxes_MasterDto c : cfgs) {
            map.put(c.getTaxKeyL(), amountForKey(c.getTaxKeyL(), t));
        }
        return map;
    }

    private List<ReportTaxes_MasterDto> loadConfigs() {
        try {
            List<ReportTaxes_MasterDto> cfg = reportTaxesConfigService.getVisibleConfigsByTemplate("ORDER_SHEET");
            if (cfg == null || cfg.isEmpty())
                cfg = reportTaxesConfigService.getVisibleConfigsByTemplate("ASSESSMENT_REGISTER");
            return cfg == null ? Collections.emptyList() : cfg;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Double amountForKey(Long key, AfterHearing_PropertyTaxDetailsEntity t) {
        if (t == null || key == null) return 0d;
        long k = key;
        if (k == ReportTaxKeys.PT_PARENT) return toD(t.getPtPropertyTaxFl());
        if (k == ReportTaxKeys.EDUC_PARENT) return toD(t.getPtEduTaxFl());
        if (k == ReportTaxKeys.EDUC_RES) return toD(t.getPtEduResTaxFl());
        if (k == ReportTaxKeys.EDUC_COMM) return toD(t.getPtEduNonResTaxFl());
        if (k == ReportTaxKeys.EGC) return toD(t.getPtEgcTaxFl());
        if (k == ReportTaxKeys.TREE_TAX) return toD(t.getPtTreeTaxFl());
        if (k == ReportTaxKeys.ENV_TAX) return toD(t.getPtEnvironmentTaxFl());
        if (k == ReportTaxKeys.CLEAN_TAX) return toD(t.getPtCleanTaxFl());
        if (k == ReportTaxKeys.LIGHT_TAX) return toD(t.getPtLightTaxFl());
        if (k == ReportTaxKeys.FIRE_TAX) return toD(t.getPtFireTaxFl());
        if (k == ReportTaxKeys.WATER_TAX) return toD(t.getPtWaterTaxFl());
        if (k == ReportTaxKeys.SEWERAGE_TAX) return toD(t.getPtSewerageTaxFl());
        if (k == ReportTaxKeys.SEWERAGE_BEN) return toD(t.getPtSewerageBenefitTaxFl());
        if (k == ReportTaxKeys.WATER_BEN) return toD(t.getPtWaterBenefitTaxFl());
        if (k == ReportTaxKeys.STREET_TAX) return toD(t.getPtStreetTaxFl());
        if (k == ReportTaxKeys.SPEC_CONS) return toD(t.getPtSpecialConservancyTaxFl());
        if (k == ReportTaxKeys.MUNICIPAL_EDU) return toD(t.getPtMunicipalEduTaxFl());
        if (k == ReportTaxKeys.SPECIAL_EDU) return toD(t.getPtSpecialEduTaxFl());
        if (k == ReportTaxKeys.SERVICE_CHG) return toD(t.getPtServiceChargesFl());
        if (k == ReportTaxKeys.MISC_CHG) return toD(t.getPtMiscellaneousChargesFl());
        if (k == ReportTaxKeys.USER_CHG) return toD(t.getPtUserChargesFl());
        // TAX1..TAX25 mapping via reflection
        if (k >= ReportTaxKeys.TAX1 && k <= ReportTaxKeys.TAX25) {
            int idx = (int)(k - ReportTaxKeys.TAX1 + 1); // 1..25
            try {
                var m = AfterHearing_PropertyTaxDetailsEntity.class.getDeclaredMethod("getPtTax" + idx + "Fl");
                Object val = m.invoke(t);
                return toD((Number) val);
            } catch (Exception ignore) { return 0d; }
        }
        return 0d;
    }

    private Double toD(Number n) { return n == null ? 0d : Double.parseDouble(n.toString()); }

    private java.util.List<AfterHearing_ProposedRValuesDto> loadProposedRValues(String newPropertyNo) {
        try {
            java.util.List<AfterHearing_ProposedRValuesEntity> rows = afterHearingProposedRvRepo.findByPrNewPropertyNoVc(newPropertyNo);
            if (rows == null || rows.isEmpty()) return java.util.Collections.emptyList();
            return rows.stream().map(this::convertProposedRvToDto).collect(java.util.stream.Collectors.toList());
        } catch (Exception e) { return java.util.Collections.emptyList(); }
    }

    private AfterHearing_ProposedRValuesDto convertProposedRvToDto(AfterHearing_ProposedRValuesEntity e) {
        AfterHearing_ProposedRValuesDto d = new AfterHearing_ProposedRValuesDto();
        d.setPrNewPropertyNoVc(e.getPrNewPropertyNoVc());
        d.setPrFinalPropNoVc(e.getPrFinalPropNoVc());
        d.setPrResidentialFl(e.getPrResidentialFl());
        d.setPrCommercialFl(e.getPrCommercialFl());
        d.setPrIndustrialFl(e.getPrIndustrialFl());
        d.setPrEducationalFl(e.getPrEducationalFl());
        d.setPrReligiousFl(e.getPrReligiousFl());
        d.setPrMobileTowerFl(e.getPrMobileTowerFl());
        d.setPrElectricSubstationFl(e.getPrElectricSubstationFl());
        d.setPrGovernmentFl(e.getPrGovernmentFl());
        d.setPrResidentialOpenPlotFl(e.getPrResidentialOpenPlotFl());
        d.setPrCommercialOpenPlotFl(e.getPrCommercialOpenPlotFl());
        d.setPrIndustrialOpenPlotFl(e.getPrIndustrialOpenPlotFl());
        d.setPrReligiousOpenPlotFl(e.getPrReligiousOpenPlotFl());
        d.setPrEducationAndLegalInstituteOpenPlotFl(e.getPrEducationAndLegalInstituteOpenPlotFl());
        d.setPrGovernmentOpenPlotFl(e.getPrGovernmentOpenPlotFl());
        d.setPrTotalRatableValueFl(e.getPrTotalRatableValueFl());
        d.setCreatedAt(e.getCreatedAt());
        d.setUpdatedAt(e.getUpdatedAt());
        return d;
    }
}
