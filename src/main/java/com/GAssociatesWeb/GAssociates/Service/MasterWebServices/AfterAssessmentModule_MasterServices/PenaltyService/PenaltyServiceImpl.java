package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AfterAssessmentModule_MasterServices.PenaltyService;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.AfterAsessment_Module.PropertyTaxDetailArrears_MasterEntity.PropertyTaxDetailArrears_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.AfterAssessmentModule_MasterRepository.PropertyTaxDetailArrears_MasterRepository.PropertyTaxDetailArrears_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class PenaltyServiceImpl implements PenaltyService {

    private final PropertyTaxDetailArrears_MasterRepository arrearsRepo;

    @Autowired
    public PenaltyServiceImpl(PropertyTaxDetailArrears_MasterRepository arrearsRepo) {
        this.arrearsRepo = arrearsRepo;
    }

    @Override
    public double previewPenaltyAmount(String newPropertyNo, Double ratePercent) {
        if (newPropertyNo == null || newPropertyNo.isBlank()) return 0.0d;
        PropertyTaxDetailArrears_MasterEntity e = arrearsRepo.findByNewPropertyNo(newPropertyNo);
        if (e == null) return 0.0d;
        // Use the stored total arrears tax as the base for penalty
        double base = safe(e.getTotalTax());
        if (base <= 0.0d) return 0.0d;

        double rate = (ratePercent != null ? ratePercent : 2.0d);
        return round2(base * (rate / 100.0d));
    }

    @Override
    public double applyPenalty(String newPropertyNo, Double ratePercent) {
        if (newPropertyNo == null || newPropertyNo.isBlank()) return 0.0d;
        PropertyTaxDetailArrears_MasterEntity e = arrearsRepo.findByNewPropertyNo(newPropertyNo);
        if (e == null) return 0.0d;

        double add = previewPenaltyAmount(newPropertyNo, ratePercent);
        if (add <= 0.0d) return safe(e.getPenalty());

        double current = safe(e.getPenalty());
        e.setPenalty(round2(current + add));
        e.setUpdatedAt(LocalDateTime.now());
        arrearsRepo.save(e);
        return safe(e.getPenalty());
    }

    private static double safe(Double v) { return v == null ? 0.0d : v.doubleValue(); }

    private static double round2(double v) {
        return new BigDecimal(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
