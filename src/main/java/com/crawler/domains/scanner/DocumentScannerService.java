package com.crawler.domains.scanner;

import com.crawler.domains.occurrences.OccurrenceService;
import com.crawler.domains.scanner.exceptions.DocumentScanException;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import com.crawler.domains.scanner.models.BulkDocumentScanRequest;
import com.crawler.domains.scanner.processors.DocumentPatternProcessor;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import com.crawler.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentScannerService {

    private final DocumentPatternProcessor patternValidator;
    private final DocumentDownloadService documentDownloadService;
    private final OccurrenceService occurrenceService;
    private final FileUtils fileUtils;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String APPLICATION_PDF = "application/pdf";
    public static final String KAFKA_DOCUMENT_SCAN_TOPIC = "document-scan-topic";
    private static final  int FETCH_RATE_MILLISECONDS = 200;
    private static final  int THREAD_POOL_SIZE = 15;

    public List<OccurrenceDTO> scanDocument(DocumentScanRequest request) {
        try {
            String documentUrl = request.getUrl();
            byte[] pdfBytes = documentDownloadService.downloadFile(documentUrl);
            List<OccurrenceDTO> occurrences = scanPdf(pdfBytes);
            occurrences.forEach(occurrence -> {
                occurrence.setUrl(documentUrl);
                occurrenceService.save(occurrence);
            });
            return occurrences;
        } catch (IOException e) {
            throw new DocumentScanException("Failed to process the PDF file from: " + request.getUrl(), e);
        }
    }

    public List<OccurrenceDTO> scanUploadedDocument(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] pdfBytes = fileUtils.readInputStreamInChunks(inputStream);
            return scanPdf(pdfBytes);
        } catch (IOException e) {
            throw new DocumentScanException("Failed to process the uploaded PDF file", e);
        }
    }

    public void scanBulkDocuments(BulkDocumentScanRequest request) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);

        List<String> urls = request.getUrls();
        for (int i = 0; i < urls.size(); i++) {
            final String url = urls.get(i);
            scheduler.schedule(() -> kafkaTemplate.send(KAFKA_DOCUMENT_SCAN_TOPIC, url), (long) i * FETCH_RATE_MILLISECONDS, TimeUnit.MILLISECONDS);
        }

        scheduler.shutdown();
    }

    private List<OccurrenceDTO> scanPdf(byte[] pdfBytes) throws IOException {
        fileUtils.validateFileType(pdfBytes, APPLICATION_PDF);
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            return patternValidator.detectPatterns(text);
        }
    }
}