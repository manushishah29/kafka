package com.example.springbatchkafka.config;

import com.example.springbatchkafka.config.StepConfig.CustomItemProcessor;
import com.example.springbatchkafka.config.StepConfig.CustomItemReader;
import com.example.springbatchkafka.config.StepConfig.CustomItemWriter;
import com.example.springbatchkafka.entity.TestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomItemProcessor customItemProcessor;
    private final CustomItemReader customItemReader;
    private final CustomItemWriter customItemWriter;

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importCustomers")
                .flow(step1()).end().build();
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1")
                .<TestEntity, TestEntity>chunk(100)
                .reader(customItemReader)
                .processor(customItemProcessor)
                .writer(customItemWriter)
                .allowStartIfComplete(true)
                .build();
    }

    /* @Bean
    public Step step2() {
        log.info("step2");
        return this.stepBuilderFactory.get("step2")
                .<TestEntity, TestEntity>chunk(100)
                .reader(customItemReader)
                .processor(customItemProcessor)
                .writer(entityKafkaItemWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step step3() {
        log.info("step3");
        return this.stepBuilderFactory.get("step3")
                .<TestEntity, TestEntity>chunk(100)
                .reader(customItemReader)
                .processor(customItemProcessor)
                .writer(entityKafkaItemWriter())
                .allowStartIfComplete(true)
                .build();
    }*/

    @Bean
    public TaskExecutor partitionTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1000);
        executor.setMaxPoolSize(1000);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("partition_");
        return executor;
//        return new SimpleAsyncTaskExecutor("spring_batch_");
    }
}
