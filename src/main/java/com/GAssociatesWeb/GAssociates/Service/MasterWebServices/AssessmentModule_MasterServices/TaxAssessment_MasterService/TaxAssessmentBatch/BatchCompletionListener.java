package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentBatch;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.Service.CompletePropertySurveyService.PropertyDetails_Service.PropertyDetails_Service;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.ReportGenerator.AssessmentResultsReportGenerator.AssessmentReportPdfGenerator;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.ResultLogsCache;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

@Component
public class BatchCompletionListener implements JobExecutionListener {

    private static final Logger logger = Logger.getLogger(BatchCompletionListener.class.getName());

    private final PropertyDetails_Service propertyDetails_service;
    private final TaxAssessmentBatchProcessor processor;
    private final AssessmentReportPdfGenerator assessmentReportPdfGenerator;
    private final ResultLogsCache resultLogsCache;

    @Autowired
    public BatchCompletionListener(PropertyDetails_Service propertyDetails_service,
                                   TaxAssessmentBatchProcessor processor,
                                   AssessmentReportPdfGenerator assessmentReportPdfGenerator,
                                   ResultLogsCache resultLogsCache) {
        this.propertyDetails_service = propertyDetails_service;
        this.processor = processor;
        this.assessmentReportPdfGenerator = assessmentReportPdfGenerator;
        this.resultLogsCache = resultLogsCache;
    }
    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Batch job started. Job ID: " + jobExecution.getJobId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String wardNo = jobExecution.getJobParameters().getString("wardNo");

        if (jobExecution.getStatus().isUnsuccessful()) {
            logger.severe("❌ Batch job failed. Job ID: " + jobExecution.getJobId());
            resultLogsCache.clear(); // Only clear cache if failed
            return;
        }

        logger.info("✅ Batch job completed successfully for Ward No: " + wardNo);

        // Debug: Check what's in the cache
//        List<AssessmentResultsDto> cachedResults = resultLogsCache.getAll();

//        for (AssessmentResultsDto dto : cachedResults) {
//            logger.info("🔍 Property: " + dto.getPdNewpropertynoVc() + " has " + (dto.getWarnings() != null ? dto.getWarnings().size() : 0) + " warnings");
//        }

        // Do NOT generate or save PDF here
//        logger.info("📌 Skipping PDF generation. It will be handled via REST API.");

        // Do NOT clear cache here; let REST API clear it after PDF download
    }
}
