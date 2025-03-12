package com.crawler.integration;

import com.crawler.integration.config.AbstractIntegrationTest;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude= KafkaAutoConfiguration.class)
@Sql(scripts = "/scripts/insert-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/cleanup-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class DocumentScannerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private MockWebServer mockWebServer;

    private final int MOCKED_DOCUMENT_SERVER_PORT = 8084;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(MOCKED_DOCUMENT_SERVER_PORT);

        // Ensure the PDF file is correctly read as bytes
        byte[] pdfBytes = Files.readAllBytes(new ClassPathResource("testfiles/Testdata_Invoices.pdf").getFile().toPath());

        // Serve the PDF file using MockWebServer
        mockWebServer.enqueue(new MockResponse()
                .setBody(new Buffer().write(pdfBytes))
                .addHeader("Content-Type", "application/pdf"));
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testDocumentScanWithInvalidContent() {
        String fileUrl = mockWebServer.url("/testfiles/Testdata_Invoices.pdf").toString();
        DocumentScanRequest request = new DocumentScanRequest(fileUrl);

        var response = restTemplate.postForEntity("/v1/document/scan/url", request, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertTrue(Objects.requireNonNull(response.getBody()).contains("Blacklisted Regexp(s) detected"));
        Assertions.assertTrue(Objects.requireNonNull(response.getBody()).contains("Blacklisted Regexp(s) detected"));
    }
}