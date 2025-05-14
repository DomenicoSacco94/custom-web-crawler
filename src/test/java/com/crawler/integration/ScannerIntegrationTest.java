package com.crawler.integration;

import com.crawler.domains.facts.models.FactDTO;
import com.crawler.domains.scanner.models.BulkPageScanRequest;
import com.crawler.integration.config.AbstractIntegrationTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
@Sql(scripts = "/scripts/insert-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/cleanup-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ScannerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        byte[] pdfBytes = Files.readAllBytes(new ClassPathResource("testfiles/Testdata_Invoices.pdf").getFile().toPath());

        mockWebServer.enqueue(new MockResponse()
                .setBody(new Buffer().write(pdfBytes))
                .addHeader("Content-Type", "application/pdf"));
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testDocumentScanWithPattern() {
        String fileUrl = mockWebServer.url("/testfiles/Testdata_Invoices.pdf").toString();
        BulkPageScanRequest request = new BulkPageScanRequest(List.of(fileUrl), 1L);

        var response = restTemplate.postForEntity("/v1/scan", request, Void.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Awaitility.await()
                .atMost(2, TimeUnit.MINUTES)
                .pollInterval(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    ResponseEntity<List<FactDTO>> factsResponse = restTemplate.exchange(
                            "/v1/facts",
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<>() {
                            }
                    );

                    assertEquals(HttpStatus.OK, factsResponse.getStatusCode());
                    assertNotNull(factsResponse.getBody());
                    assertFalse(factsResponse.getBody().isEmpty());

                    // Ensure at least one FactDTO has non-empty attributes
                    boolean hasNonEmptyFact =
                            factsResponse.getBody().stream()
                                    .filter(Objects::nonNull)
                                    .anyMatch(fact -> !fact.getInferredText().isEmpty() && !fact.getConsequences().isEmpty());

                    assertTrue(hasNonEmptyFact, "No FactDTO with non-empty attributes found.");
                });
    }
}