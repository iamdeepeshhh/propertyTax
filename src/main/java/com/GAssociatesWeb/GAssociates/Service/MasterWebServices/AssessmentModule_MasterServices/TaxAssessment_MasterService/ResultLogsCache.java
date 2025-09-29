package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResultLogsCache {
    private final List<AssessmentResultsDto> warningsList = new ArrayList<>();

    // Add AssessmentResultDto if it contains warnings
    public synchronized void addDto(AssessmentResultsDto dto) {
        if (dto.getWarnings() != null && !dto.getWarnings().isEmpty()) {
            warningsList.add(dto);

        } else {
            System.out.println("📋 No warnings for property: " + dto.getPdNewpropertynoVc());
        }
    }

    // Retrieve a copy of the warning DTOs
    public synchronized List<AssessmentResultsDto> getAll() {
        return new ArrayList<>(warningsList);
    }

    // Clear the cache (useful after PDF generation)
    public synchronized void clear() {
        warningsList.clear();
    }
}
