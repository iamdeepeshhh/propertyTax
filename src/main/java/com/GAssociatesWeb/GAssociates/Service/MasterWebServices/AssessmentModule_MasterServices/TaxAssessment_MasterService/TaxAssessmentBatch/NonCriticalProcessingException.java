package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentBatch;

public class NonCriticalProcessingException extends Exception{
    public NonCriticalProcessingException(String message) {
        super(message);
    }

    public NonCriticalProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
