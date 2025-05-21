package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentBatch;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity.PropertyDetails_Entity;
import org.springframework.batch.core.SkipListener;

import java.util.logging.Logger;

public class CustomSkipListener implements SkipListener <PropertyDetails_Entity, AssessmentResultsDto> {
    private static final Logger logger = Logger.getLogger(CustomSkipListener.class.getName());

    @Override
    public void onSkipInRead(Throwable t) {
        logger.warning("Skipped during reading: " + t.getMessage());
    }

    @Override
    public void onSkipInProcess(PropertyDetails_Entity item, Throwable t) {
        logger.warning("Skipped during processing: " + item + ", due to: " + t.getMessage());
    }

    @Override
    public void onSkipInWrite(AssessmentResultsDto item, Throwable t) {
        logger.warning("Skipped during writing: " + item + ", due to: " + t.getMessage());
    }
}