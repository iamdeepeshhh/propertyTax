package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.TaxAssessmentBatch;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.AssessmentModule_MasterDto.TaxAssessment_MasterDto.AssessmentResultsDto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity.PropertyDetails_Entity;
import com.GAssociatesWeb.GAssociates.Service.MasterWebServices.AssessmentModule_MasterServices.TaxAssessment_MasterService.PreLoadCache;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.logging.Logger;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private PropertyDetailsReader propertyDetailsReader;

    @Autowired
    private TaxAssessmentBatchProcessor taxAssessmentBatchProcessor;

    @Autowired
    private PropertyDetailsWriter propertyDetailsWriter;


    @Autowired
    private BatchCompletionListener batchCompletionListener;
    private static final Logger logger = Logger.getLogger(JobConfiguration.class.getName());


    @Bean
    public Job processPropertyDetailsJob(JobRepository jobRepository, Step propertyDetailsStep, JobExecutionListener listener) {
        logger.info("Starting property details batch job...");
       // preLoadCache.preloadAllCaches();
        return new JobBuilder("processPropertyDetailsJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // Automatically generate a new job parameter
                .listener(listener)
                .start(propertyDetailsStep)
                .build();
    }

    @Bean
    public Step propertyDetailsStep(JobRepository jobRepository, ItemReader<PropertyDetails_Entity> reader,
                                    ItemProcessor<PropertyDetails_Entity, AssessmentResultsDto> processor,
                                    ItemWriter<AssessmentResultsDto> writer, PropertyDetailsSkipPolicy skipPolicy) {

        return new StepBuilder("propertyDetailsStep", jobRepository)
                .<PropertyDetails_Entity, AssessmentResultsDto>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(skipPolicy)
                .skip(Exception.class)
                .skipLimit(100)
                .build();
    }
}