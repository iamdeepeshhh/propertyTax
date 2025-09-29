package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentBatch;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.PropertyUnitDetailsDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module.Property_RValues;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module.Property_TaxDetails;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AssessmentModule_MasterEntity.TaxAssessment_Module.Proposed_RValues;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository.Property_RValuesRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository.Property_TaxDetailsRepository;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AssessmentModule_MasterRepository.TaxAssessment_MasterRepository.Proposed_RValuesRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Component
public class PropertyDetailsWriter implements ItemWriter<AssessmentResultsDto> {

    private static final Logger logger = Logger.getLogger(PropertyDetailsWriter.class.getName());
    @Autowired
    private Property_RValuesRepository property_rValuesRepository;
    @Autowired
    private Property_TaxDetailsRepository property_taxDetailsRepository;
    @Autowired
    private Proposed_RValuesRepository proposed_rValuesRepository;


    @Override
    @Transactional
    public void write(Chunk<? extends AssessmentResultsDto> chunk) throws Exception {
        for (AssessmentResultsDto result : chunk) {
            logger.info("Processing Assessment Result for Property: " + result.getPdNewpropertynoVc());

            // Save Property Tax Details
            savePropertyTaxDetails(result);

            // Save Proposed Rate Values
            saveProposedRateValues(result);

            // Save Unit Details
            saveUnitDetails(result);

            logger.info("---- End of Property Assessment ----\n");
        }
    }

    private void savePropertyTaxDetails(AssessmentResultsDto result) {
        Property_TaxDetails property_taxDetails = new Property_TaxDetails();
        property_taxDetails.setPtNewPropertyNoVc(result.getPdNewpropertynoVc());
        property_taxDetails.setPtFinalPropertyNoVc(result.getPdFinalpropnoVc());
        property_taxDetails.setPtPropertyTaxFl(nullToZero(result.getConsolidatedTaxes().getPropertyTaxFl()));
        property_taxDetails.setPtEgcTaxFl(nullToZero(result.getConsolidatedTaxes().getEgcFl()));
        property_taxDetails.setPtEduResTaxFl(nullToZero(result.getConsolidatedTaxes().getEducationTaxResidFl()));
        property_taxDetails.setPtEduNonResTaxFl(nullToZero(result.getConsolidatedTaxes().getEducationTaxCommFl()));
        property_taxDetails.setPtEduTaxFl(nullToZero(result.getConsolidatedTaxes().getEducationTaxTotalFl()));
        property_taxDetails.setPtCleanTaxFl(nullToZero(result.getConsolidatedTaxes().getCleannessTaxFl()));
        property_taxDetails.setPtFireTaxFl(nullToZero(result.getConsolidatedTaxes().getFireTaxFl()));
        property_taxDetails.setPtTreeTaxFl(nullToZero(result.getConsolidatedTaxes().getTreeTaxFl()));
        property_taxDetails.setPtUserChargesFl(nullToZero(result.getConsolidatedTaxes().getUserChargesFl()));
        property_taxDetails.setPtEnvironmentTaxFl(nullToZero(result.getConsolidatedTaxes().getEnvironmentalTaxFl()));
        property_taxDetails.setPtLightTaxFl(nullToZero(result.getConsolidatedTaxes().getLightTaxFl()));

        property_taxDetails.setPtWaterTaxFl(nullToZero(result.getConsolidatedTaxes().getWaterTaxFl()));
        property_taxDetails.setPtSewerageTaxFl(nullToZero(result.getConsolidatedTaxes().getSewerageTaxFl()));
        property_taxDetails.setPtSewerageBenefitTaxFl(nullToZero(result.getConsolidatedTaxes().getSewerageBenefitTaxFl()));
        property_taxDetails.setPtWaterBenefitTaxFl(nullToZero(result.getConsolidatedTaxes().getWaterBenefitTaxFl()));
        property_taxDetails.setPtStreetTaxFl(nullToZero(result.getConsolidatedTaxes().getStreetTaxFl()));
        property_taxDetails.setPtSpecialConservancyTaxFl(nullToZero(result.getConsolidatedTaxes().getSpecialConservancyTaxFl()));
        property_taxDetails.setPtMunicipalEduTaxFl(nullToZero(result.getConsolidatedTaxes().getMunicipalEducationTaxFl()));
        property_taxDetails.setPtSpecialEduTaxFl(nullToZero(result.getConsolidatedTaxes().getSpecialEducationTaxFl()));
        property_taxDetails.setPtServiceChargesFl(nullToZero(result.getConsolidatedTaxes().getServiceChargesFl()));
        property_taxDetails.setPtMiscellaneousChargesFl(nullToZero(result.getConsolidatedTaxes().getMiscellaneousChargesFl()));

// Reserved taxes (Tax1 to Tax25)
        property_taxDetails.setPtTax1Fl(nullToZero(result.getConsolidatedTaxes().getTax1Fl()));
        property_taxDetails.setPtTax2Fl(nullToZero(result.getConsolidatedTaxes().getTax2Fl()));
        property_taxDetails.setPtTax3Fl(nullToZero(result.getConsolidatedTaxes().getTax3Fl()));
        property_taxDetails.setPtTax4Fl(nullToZero(result.getConsolidatedTaxes().getTax4Fl()));
        property_taxDetails.setPtTax5Fl(nullToZero(result.getConsolidatedTaxes().getTax5Fl()));
        property_taxDetails.setPtTax6Fl(nullToZero(result.getConsolidatedTaxes().getTax6Fl()));
        property_taxDetails.setPtTax7Fl(nullToZero(result.getConsolidatedTaxes().getTax7Fl()));
        property_taxDetails.setPtTax8Fl(nullToZero(result.getConsolidatedTaxes().getTax8Fl()));
        property_taxDetails.setPtTax9Fl(nullToZero(result.getConsolidatedTaxes().getTax9Fl()));
        property_taxDetails.setPtTax10Fl(nullToZero(result.getConsolidatedTaxes().getTax10Fl()));
        property_taxDetails.setPtTax11Fl(nullToZero(result.getConsolidatedTaxes().getTax11Fl()));
        property_taxDetails.setPtTax12Fl(nullToZero(result.getConsolidatedTaxes().getTax12Fl()));
        property_taxDetails.setPtTax13Fl(nullToZero(result.getConsolidatedTaxes().getTax13Fl()));
        property_taxDetails.setPtTax14Fl(nullToZero(result.getConsolidatedTaxes().getTax14Fl()));
        property_taxDetails.setPtTax15Fl(nullToZero(result.getConsolidatedTaxes().getTax15Fl()));
        property_taxDetails.setPtTax16Fl(nullToZero(result.getConsolidatedTaxes().getTax16Fl()));
        property_taxDetails.setPtTax17Fl(nullToZero(result.getConsolidatedTaxes().getTax17Fl()));
        property_taxDetails.setPtTax18Fl(nullToZero(result.getConsolidatedTaxes().getTax18Fl()));
        property_taxDetails.setPtTax19Fl(nullToZero(result.getConsolidatedTaxes().getTax19Fl()));
        property_taxDetails.setPtTax20Fl(nullToZero(result.getConsolidatedTaxes().getTax20Fl()));
        property_taxDetails.setPtTax21Fl(nullToZero(result.getConsolidatedTaxes().getTax21Fl()));
        property_taxDetails.setPtTax22Fl(nullToZero(result.getConsolidatedTaxes().getTax22Fl()));
        property_taxDetails.setPtTax23Fl(nullToZero(result.getConsolidatedTaxes().getTax23Fl()));
        property_taxDetails.setPtTax24Fl(nullToZero(result.getConsolidatedTaxes().getTax24Fl()));
        property_taxDetails.setPtTax25Fl(nullToZero(result.getConsolidatedTaxes().getTax25Fl()));
        property_taxDetails.setPtFinalTaxFl(nullToZero(result.getConsolidatedTaxes().getTotalTaxFl()));
        property_taxDetailsRepository.save(property_taxDetails);
    }

    private void saveProposedRateValues(AssessmentResultsDto result) {
        Proposed_RValues proposed_rValues = new Proposed_RValues();
        proposed_rValues.setPrNewPropertyNoVc(result.getPdNewpropertynoVc());
        proposed_rValues.setPrResidentialFl(nullToZero(result.getProposedRatableValues().getResidentialFl()));
        proposed_rValues.setPrCommercialFl(nullToZero(result.getProposedRatableValues().getCommercialFl()));
        proposed_rValues.setPrReligiousFl(nullToZero(result.getProposedRatableValues().getReligiousFl()));
        proposed_rValues.setPrGovernmentFl(nullToZero(result.getProposedRatableValues().getGovernmentFl()));
        proposed_rValues.setPrEducationalFl(nullToZero(result.getProposedRatableValues().getEducationalInstituteFl()));
        proposed_rValues.setPrElectricSubstationFl(nullToZero(result.getProposedRatableValues().getElectricSubstationFl()));
        proposed_rValues.setPrIndustrialFl(nullToZero(result.getProposedRatableValues().getIndustrialFl()));
        proposed_rValues.setPrMobileTowerFl(nullToZero(result.getProposedRatableValues().getMobileTowerFl()));
        proposed_rValues.setPrResidentialOpenPlotFl(nullToZero(result.getProposedRatableValues().getResidentialOpenPlotFl()));
        proposed_rValues.setPrCommercialOpenPlotFl(nullToZero(result.getProposedRatableValues().getCommercialOpenPlotFl()));
        proposed_rValues.setPrReligiousOpenPlotFl(nullToZero(result.getProposedRatableValues().getReligiousOpenPlotFl()));
        proposed_rValues.setPrGovernmentOpenPlotFl(nullToZero(result.getProposedRatableValues().getGovernmentOpenPlotFl()));
        proposed_rValues.setPrEducationAndLegalInstituteOpenPlotFl(nullToZero(result.getProposedRatableValues().getEducationAndLegalInstituteOpenPlotFl()));
        proposed_rValues.setPrIndustrialOpenPlotFl(nullToZero(result.getProposedRatableValues().getIndustrialOpenPlotFl()));
        proposed_rValues.setPrTotalRatableValueFl(nullToZero(result.getProposedRatableValues().getAggregateFl()));
        proposed_rValues.setPrFinalPropNoVc(result.getPdFinalpropnoVc());
        proposed_rValuesRepository.save(proposed_rValues);
    }

    private void saveUnitDetails(AssessmentResultsDto result) {
        for (PropertyUnitDetailsDto unit : result.getUnitDetails()) {
            Property_RValues property_rValues = new Property_RValues();
            property_rValues.setPrvPropertyNoVc(unit.getNewPropertyNo());
            property_rValues.setPrvUnitNoVc(unit.getUnitNoVc());
            property_rValues.setPrvRatePerSqMFl(nullToZero(unit.getRatePerSqMFl()));
            property_rValues.setPrvRentalValAsPerRateFl(nullToZero(unit.getRentalValAsPerRateFl()));
            property_rValues.setPrvDepreciationRateFl(unit.getDepreciationRateFl() != null ? unit.getDepreciationRateFl().intValue() : 0);
            property_rValues.setPrvDepreciationAmountFl(nullToZero(unit.getDepreciationAmountFl()));
            property_rValues.setPrvAmountAfterDepreciationFl(nullToZero(unit.getValueAfterDepreciationFl()));
            property_rValues.setPrvMaintenanceRepairsFl(nullToZero(unit.getMaintenanceRepairsFl()));
            property_rValues.setPrvTaxableValueByRateFl(nullToZero(unit.getTaxableValueByRateFl()));
            property_rValues.setPrvTenantNameVc(unit.getTenantNameVc());
            property_rValues.setPrvActualMonthlyRentFl(nullToZero(unit.getActualMonthlyRentFl()));
            property_rValues.setPrvActualAnnualRentFl(nullToZero(unit.getActualAnnualRentFl()));
            property_rValues.setPrvMaintenanceRepairsRentFl(nullToZero(unit.getMaintenanceRepairsRentFl()));
            property_rValues.setPrvTaxableValueByRentFl(nullToZero(unit.getTaxableValueByRentFl()));
            property_rValues.setPrvTaxableValueConsideredFl(nullToZero(unit.getTaxableValueConsideredFl()));
            property_rValues.setPrvFinalPropNoVc(result.getPdFinalpropnoVc());
            property_rValues.setPrvAgeFactorVc(unit.getAgeFactorVc());
            property_rValuesRepository.save(property_rValues);
        }
    }

    private Double nullToZero(Double value) {
        return value != null ? value : 0.0;
    }
}
