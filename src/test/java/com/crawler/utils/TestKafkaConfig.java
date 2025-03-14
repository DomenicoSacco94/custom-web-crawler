package com.crawler.utils;

import com.crawler.domains.occurrences.models.OccurrenceDTO;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestKafkaConfig {

    @Bean(name = "testKafkaTemplate")
    @Primary
    public KafkaTemplate<String, OccurrenceDTO> kafkaTemplate() {
        return mock(KafkaTemplate.class);
    }
}