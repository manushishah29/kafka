package com.example.springbatchkafka.config.StepConfig;

import com.example.springbatchkafka.entity.TestEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Configuration
@Slf4j
@RequiredArgsConstructor
@StepScope
public class CustomItemWriter implements ItemWriter<TestEntity> {
    private final KafkaTemplate<String, TestEntity> kafkaTemplate;

    @Value("#{jobParameters['topicName']}")
    private String topicName;

    @Override
    public void write(List<? extends TestEntity> items) throws ExecutionException, InterruptedException {
        int numThreads = 5;
        List<TestEntity> dataList = new ArrayList<>(items);
        int batchSize = dataList.size() / numThreads;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numThreads; i++) {
            int start = i * batchSize;
            int end = (i == numThreads - 1) ? dataList.size() : (i + 1) * batchSize;
            List<TestEntity> subset = dataList.subList(start, end);
            executor.execute(new InsertDataRunnable(subset, topicName, kafkaTemplate));
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

//        int numThreads = 10;
//        List<TestEntity> dataList = new ArrayList<>(items);
//        int batchSize = dataList.size() / numThreads;
//        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
//        for (int i = 0; i < numThreads; i++) {
//            int start = i * batchSize;
//            int end = (i == numThreads - 1) ? dataList.size() : (i + 1) * batchSize;
//            List<TestEntity> subset = dataList.subList(start, end);
//            executor.execute(new InsertDataRunnable(subset, outputFile, kafkaTemplate));
//        }
//        executor.shutdown();
        //        List<List<TestEntity>> partitions = partition(dataList, numThreads);

        //create partition
//        InsertDataRunnable task = null;
//        for (List<TestEntity> partition : partitions) {
//            task = new InsertDataRunnable(partition);
//        }
//
//        futures.add(executor.submit(task));
//        List<TestEntity> results = new ArrayList<>();
//        for (Future<List<TestEntity>> future : futures) {
//            results.addAll(future.get());
//        }
//
//        topicsName.forEach(topic -> {
//            results.forEach(result -> {
//                kafkaTemplate.send(topic, result);
//            });
//        });
//    }
//
//    private List<List<TestEntity>> partition(List<TestEntity> list, int numPartitions) {
//        List<List<TestEntity>> partitions = new ArrayList<>();
//        int partitionSize = (int) Math.ceil(list.size() / (double) numPartitions);
//        for (int i = 0; i < list.size(); i += partitionSize) {
//            partitions.add(list.subList(i, Math.min(i + partitionSize, list.size())));
//        }
//        return partitions;
    }

    private static class InsertDataRunnable implements Runnable {
        private final List<TestEntity> dataList;

        private final String topicName;

        private final KafkaTemplate<String, TestEntity> kafkaTemplate;

        public InsertDataRunnable(List<TestEntity> dataList, String topicName, KafkaTemplate<String, TestEntity> kafkaTemplate) {
            this.dataList = dataList;
            this.topicName = topicName;
            this.kafkaTemplate = kafkaTemplate;
        }

        @Override
        public void run() {
            this.dataList.forEach(data -> {
                kafkaTemplate.send(topicName, data);
                log.info("Thread name: " + Thread.currentThread().getName() + " data: " + data.toString() + "");
            });
        }

//        @Override
//        public List<TestEntity> call() {
//            this.dataList.forEach(data -> {
//                log.info("Thread name: " + Thread.currentThread().getName() + " data: " + data.toString() + "");
//            });
//            return dataList;
//        }
    }
}