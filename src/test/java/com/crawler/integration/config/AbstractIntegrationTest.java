package com.crawler.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public abstract class AbstractIntegrationTest {

    @Container
    public static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

    @Container
    public static final GenericContainer<?> ollamaContainer = new GenericContainer<>("ollama/ollama:latest")
            .withExposedPorts(11434);

    @Container
    public static final KafkaContainer kafkaContainer = new KafkaContainer("latest");

    @BeforeAll
    static void startContainers() {

        log.info("starting test containers");

        postgresContainer.start();
        log.info("PostgreSQL container started with JDBC URL: {}", postgresContainer.getJdbcUrl());

        ollamaContainer.start();
        log.info("Ollama container started on port: {}", ollamaContainer.getMappedPort(11434));

        kafkaContainer.start();
        log.info("Kafka container started on bootstrap servers: {}", kafkaContainer.getBootstrapServers());
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // PostgreSQL properties
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.flyway.url", postgresContainer::getJdbcUrl);
        registry.add("spring.flyway.user", postgresContainer::getUsername);
        registry.add("spring.flyway.password", postgresContainer::getPassword);

        // Ollama properties
        registry.add("spring.ai.ollama.base-url", () -> "http://localhost:" + ollamaContainer.getMappedPort(11434));

        // Kafka properties
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);

        log.info("Registered PostgreSQL, Ollama, and Kafka properties.");
    }

    @AfterAll
    static void stopContainers() {
        postgresContainer.stop();
        log.info("PostgreSQL container stopped.");

        ollamaContainer.stop();
        log.info("Ollama container stopped.");

        kafkaContainer.stop();
        log.info("Kafka container stopped.");
    }
}