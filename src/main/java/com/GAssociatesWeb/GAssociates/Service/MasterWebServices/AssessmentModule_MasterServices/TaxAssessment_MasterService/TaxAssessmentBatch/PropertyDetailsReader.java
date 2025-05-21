package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentBatch;

import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity.PropertyDetails_Entity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class PropertyDetailsReader {

    @Bean
    @StepScope
    public JpaPagingItemReader<PropertyDetails_Entity> reader(EntityManagerFactory entityManagerFactory,
                                                              @Value("#{jobParameters['wardNo']}") String wardNo) {
        return new JpaPagingItemReaderBuilder<PropertyDetails_Entity>()
                .name("propertyDetailsReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT p FROM PropertyDetails_Entity p WHERE p.pdWardI = :wardNo")
                .parameterValues(Collections.singletonMap("wardNo", wardNo))
                .pageSize(10)
                .build();
    }
}