package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentBatch;

import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.context.annotation.Configuration;

import java.util.zip.DataFormatException;

@Configuration
public class PropertyDetailsSkipPolicy implements SkipPolicy {
    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        if(t instanceof DataFormatException && skipCount <= 5){
            return true;
        } else if (t instanceof NonCriticalProcessingException && skipCount <= 10) {
            // Skip non-critical processing errors, allowing for more skips
            return true;
        }
        return false;
    }
}
