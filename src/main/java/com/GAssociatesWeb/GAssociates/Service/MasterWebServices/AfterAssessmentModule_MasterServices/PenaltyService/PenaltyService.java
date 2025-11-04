package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.PenaltyService;

/**
 * Separate service for computing and applying penalties on arrears.
 * Keeps concerns isolated from the core Arrears service.
 */
public interface PenaltyService {

    /**
     * Preview the penalty amount for the given property using the provided rate.
     * If {@code ratePercent} is null, a default of 2.0 is used.
     *
     * @param newPropertyNo property identifier
     * @param ratePercent   percentage rate (e.g., 2.0 for 2%)
     * @return computed penalty amount (rounded to 2 decimals); 0 if not found/invalid
     */
    double previewPenaltyAmount(String newPropertyNo, Double ratePercent);

    /**
     * Apply the penalty to the property and persist the change by increasing arrears penalty field.
     * Returns the new total penalty stored after apply.
     * If {@code ratePercent} is null, a default of 2.0 is used.
     *
     * @param newPropertyNo property identifier
     * @param ratePercent   percentage rate (e.g., 2.0 for 2%)
     * @return new stored penalty total (rounded to 2 decimals); 0 if not found/invalid
     */
    double applyPenalty(String newPropertyNo, Double ratePercent);
}

