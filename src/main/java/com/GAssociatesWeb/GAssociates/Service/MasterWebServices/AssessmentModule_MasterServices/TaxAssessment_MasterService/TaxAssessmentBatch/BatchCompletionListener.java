package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentBatch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class BatchCompletionListener implements JobExecutionListener {

    private static final Logger logger = Logger.getLogger(BatchCompletionListener.class.getName());

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Batch job started. Job ID: " + jobExecution.getJobId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().isUnsuccessful()) {
            logger.severe("Batch job failed. Job ID: " + jobExecution.getJobId());
        } else {
            logger.info("Batch job completed successfully. Job ID: " + jobExecution.getJobId());
            // Additional actions upon successful completion, such as sending a notification
        }
    }
}
