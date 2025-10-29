package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.AfterHearing_Services.AfterHearing_TaxComputationalService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.ProposedRatableValueDetailsDto;

import java.util.Map;

public interface AfterHearing_TaxCalculationService {
    double calculatePropertyTax(ProposedRatableValueDetailsDto proposed);

    double calculateEducationCess(ProposedRatableValueDetailsDto proposed);

    double calculateEgc(ProposedRatableValueDetailsDto proposed);

    double calculateTotalTax(ProposedRatableValueDetailsDto proposed);
    public Map<Long, Double> calculateConsolidatedTaxes(double finalRV, double propertyTax);
}
