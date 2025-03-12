package com.crawler.domains.scanner;

import com.crawler.domains.scanner.exceptions.InvalidContentDetectedException;
import com.crawler.domains.scanner.exceptions.DocumentScanException;
import com.crawler.domains.scanner.models.DocumentScanRequest;
import com.crawler.domains.scanner.models.BulkDocumentScanRequest;
import com.crawler.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentScannerService {

    private final List<Validator> fileValidators;
    private final DocumentDownloadService documentDownloadService;
    private final FileUtils fileUtils;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String APPLICATION_PDF = "application/pdf";
    private final String KAFKA_TOPIC = "document-scan-topic";
    private final int FETCH_RATE_MILLISECONDS = 200;
    private final int THREAD_POOL_SIZE = 5;

    public void scanDocument(DocumentScanRequest request) {
        try {
            byte[] pdfBytes = documentDownloadService.downloadFile(request.getUrl());
            validatePdf(pdfBytes, request.getUrl());
        } catch (IOException e) {
            throw new DocumentScanException("Failed to process the PDF file from: " + request.getUrl(), e);
        }
    }

    public void scanUploadedDocument(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] pdfBytes = fileUtils.readInputStreamInChunks(inputStream);
            validatePdf(pdfBytes, file.getOriginalFilename());
        } catch (IOException e) {
            throw new DocumentScanException("Failed to process the uploaded PDF file", e);
        }
    }

    public void scanBulkDocuments(BulkDocumentScanRequest request) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);

        List<String> urls = request.getUrls();
        for (int i = 0; i < urls.size(); i++) {
            final String url = urls.get(i);
            scheduler.schedule(() -> kafkaTemplate.send(KAFKA_TOPIC, url), (long) i * FETCH_RATE_MILLISECONDS, TimeUnit.MILLISECONDS);
        }

        scheduler.shutdown();
    }

    private void validatePdf(byte[] pdfBytes, String documentName) throws IOException {
        fileUtils.validateFileType(pdfBytes, APPLICATION_PDF);
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            Errors errors = new MapBindingResult(new HashMap<>(), "text");
            for (Validator validator : fileValidators) {
                validator.validate(text, errors);
            }

            if (errors.hasErrors()) {
                String errorMessage = errors.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                throw new InvalidContentDetectedException(errorMessage);
            } else {
                log.info("No invalid content detected in document: {}", documentName);
            }
        }
    }
}