package com.crawler.services;

import com.crawler.domains.regexps.RegexpRepository;
import com.crawler.domains.regexps.models.Regexp;
import com.crawler.domains.scanner.DocumentDownloadService;
import com.crawler.domains.scanner.DocumentScannerService;
import com.crawler.domains.scanner.exceptions.DocumentScanException;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import com.crawler.domains.scanner.processors.DocumentPatternProcessor;
import com.crawler.utils.FileUtils;
import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentScannerServiceTest {

    @Mock
    private DocumentDownloadService documentDownloadService;

    @Mock
    private RegexpRepository regexpRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private DocumentScannerService documentScannerService;

    @BeforeEach
    public void setUp() {
        DocumentPatternProcessor documentPatternProcessor = new DocumentPatternProcessor(regexpRepository);
        Tika tika = new Tika();
        FileUtils fileUtils = new FileUtils(tika);
        documentScannerService = new DocumentScannerService(documentPatternProcessor, documentDownloadService, fileUtils, kafkaTemplate);
    }

    @Test
    public void testScanPdfDocumentWithBlacklistedRegexp() throws Exception {
        when(regexpRepository.findAllBy()).thenReturn(List.of(() -> "DE15\\s3006\\s0601\\s0505\\s7807\\s80"));
        Regexp mockRegexp = new Regexp();
        mockRegexp.setId(1L);
        mockRegexp.setPattern("DE15\\s3006\\s0601\\s0505\\s7807\\s80");
        when(regexpRepository.findByPattern(any())).thenReturn(List.of(mockRegexp));

        Path validFilePath = Paths.get("src/test/resources/testfiles/Testdata_Invoices.pdf");
        byte[] validFileContent = Files.readAllBytes(validFilePath);
        MockMultipartFile validFile = new MockMultipartFile("file", "test_pdf.pdf", "application/pdf", validFileContent);

        //todo check content
        assertDoesNotThrow(() ->
                documentScannerService.scanUploadedDocument(validFile));
    }

    @Test
    public void testScanPdfDocumentWithoutBlacklistedRegexp() throws Exception {
        when(regexpRepository.findAllBy()).thenReturn(List.of(() -> "DE15\\s3006\\s0601\\s0505\\s7807\\s80"));

        Path validFilePath = Paths.get("src/test/resources/testfiles/Testdata_no_ibans.pdf");
        byte[] validFileContent = Files.readAllBytes(validFilePath);
        MockMultipartFile validFile = new MockMultipartFile("file", "test_pdf.pdf", "application/pdf", validFileContent);

        //todo check content
        assertDoesNotThrow(() ->
                documentScannerService.scanUploadedDocument(validFile));
    }

    @Test
    public void testScanPdfDocumentWithoutBlacklist() throws Exception {
        Path validFilePath = Paths.get("src/test/resources/testfiles/Testdata_Invoices.pdf");
        byte[] validFileContent = Files.readAllBytes(validFilePath);
        MockMultipartFile validFile = new MockMultipartFile("file", "test_pdf.pdf", "application/pdf", validFileContent);

        assertDoesNotThrow(() ->
                documentScannerService.scanUploadedDocument(validFile));
    }

    @Test
    public void testScanUploadedDocumentThrowsDocumentScanException() throws Exception {
        MockMultipartFile invalidFile = mock(MockMultipartFile.class);
        when(invalidFile.getInputStream()).thenThrow(new IOException("Failed to read input stream"));

        assertThrows(DocumentScanException.class, () ->
                documentScannerService.scanUploadedDocument(invalidFile));
    }

    @Test
    public void testScanDocumentWithInvalidUrl() throws IOException {
        String invalidUrl = "http://example.com/invalid_document.pdf";
        when(documentDownloadService.downloadFile(invalidUrl)).thenThrow(new IOException("File not found"));

        DocumentScanRequest request = new DocumentScanRequest(invalidUrl);

        assertThrows(DocumentScanException.class, () ->
            documentScannerService.scanDocument(request));
    }

    @Test
    public void testScanDocumentSuccess() throws Exception {
        String validUrl = "http://example.com/valid_document.pdf";
        Path validFilePath = Paths.get("src/test/resources/testfiles/Testdata_Invoices.pdf");
        byte[] validFileContent = Files.readAllBytes(validFilePath);

        when(documentDownloadService.downloadFile(validUrl)).thenReturn(validFileContent);

        DocumentScanRequest request = new DocumentScanRequest(validUrl);

        assertDoesNotThrow(() -> documentScannerService.scanDocument(request));
    }
}