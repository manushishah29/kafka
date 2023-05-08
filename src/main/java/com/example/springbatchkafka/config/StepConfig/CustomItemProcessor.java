package com.example.springbatchkafka.config.StepConfig;

import com.example.springbatchkafka.entity.TestEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
@StepScope
@Slf4j
public class CustomItemProcessor implements ItemProcessor<TestEntity, TestEntity> {
    @Override
    public TestEntity process(TestEntity entity) throws ParseException {
        entity.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(String.valueOf(entity.getDate())));
//        log.info("Processing result : {}", entity.toString());
        return entity;
    }
}
