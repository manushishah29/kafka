//package com.example.springbatchkafka.config.batchPartition;
//
//import com.example.springbatchkafka.config.CustomMultiPartitioner;
//import com.example.springbatchkafka.config.StepConfig.CustomItemProcessor;
//import com.example.springbatchkafka.config.StepConfig.CustomItemReader;
//import com.example.springbatchkafka.entity.TestEntity;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.partition.support.Partitioner;
//import org.springframework.batch.item.ParseException;
//import org.springframework.batch.item.UnexpectedInputException;
//import org.springframework.batch.item.kafka.KafkaItemWriter;
//import org.springframework.batch.item.kafka.builder.KafkaItemWriterBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.ThreadPoolExecutor;
//
//@Configuration
//@RequiredArgsConstructor
//@Slf4j
//public class BatchConfig {
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//    private final KafkaTemplate<String, TestEntity> kafkaTemplate;
//    private final CustomItemProcessor customItemProcessor;
//    private final CustomItemReader customItemReader;
////
//
//    @Bean
//    public Job runJob() {
//        return jobBuilderFactory.get("importCustomers")
//                .flow(step1()).end().build();
//    }
//
//    public KafkaItemWriter<String, TestEntity> entityKafkaItemWriter () {
//        return new KafkaItemWriterBuilder<String, TestEntity>()
//                .kafkaTemplate(kafkaTemplate)
//                .itemKeyMapper(TestEntity::getName)
//                .build();
//    }
//
//    @Bean
//    public Partitioner partitioner() {
//        return new CustomMultiPartitioner();
//    }
//
//    @Bean
//    public Step partitionStep() throws UnexpectedInputException, ParseException {
//        return stepBuilderFactory.get("partitionStep")
//                .partitioner("slaveStep", partitioner())
//                .step(step1())
//                .gridSize(5)
//                .taskExecutor(partitionTaskExecutor())
//                .build();
//
//    }
//
//    @Bean
//    public Step step1() {
//        return this.stepBuilderFactory.get("step1")
//                .<TestEntity, TestEntity>chunk(100)
//                .reader(customItemReader)
//                .processor(customItemProcessor)
//                .writer(entityKafkaItemWriter())
//                .allowStartIfComplete(true)
//                .build();
//    }
//
// @Bean
//    public Step step2() {
//        log.info("step2");
//        return this.stepBuilderFactory.get("step2")
//                .<TestEntity, TestEntity>chunk(100)
//                .reader(customItemReader)
//                .processor(customItemProcessor)
//                .writer(entityKafkaItemWriter())
//                .allowStartIfComplete(true)
//                .build();
//    }
//
//    @Bean
//    public Step step3() {
//        log.info("step3");
//        return this.stepBuilderFactory.get("step3")
//                .<TestEntity, TestEntity>chunk(100)
//                .reader(customItemReader)
//                .processor(customItemProcessor)
//                .writer(entityKafkaItemWriter())
//                .allowStartIfComplete(true)
//                .build();
//    }
//
//
//    @Bean
//    public TaskExecutor partitionTaskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(1000);
//        executor.setMaxPoolSize(1000);
//        executor.setQueueCapacity(1000);
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.setThreadNamePrefix("partition");
//        return executor;
////        return new SimpleAsyncTaskExecutor("spring_batch_");
//    }
//
//
//}
