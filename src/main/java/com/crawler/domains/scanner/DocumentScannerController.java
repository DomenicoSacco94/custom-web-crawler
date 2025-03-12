package com.crawler.domains.scanner;

import com.crawler.domains.scanner.models.DocumentScanRequest;
import com.crawler.domains.scanner.models.BulkDocumentScanRequest;
import com.crawler.domains.scanner.processors.RegexpOccurrence;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/document/scan")
@Tag(name = "Document Scanner", description = "Operations related to document scanning for invalid content")
public class DocumentScannerController {

    private final DocumentScannerService scannerService;

    @Operation(summary = "Scan a document from a URL")
    @PostMapping("/url")
    public ResponseEntity<List<RegexpOccurrence>> scanDocument(@RequestBody DocumentScanRequest request) {
        List<RegexpOccurrence> occurrences = scannerService.scanDocument(request);
        return ResponseEntity.ok(occurrences);
    }

    @Operation(summary = "Scan an uploaded document")
    @PostMapping("/upload")
    public ResponseEntity<List<RegexpOccurrence>> scanUploadedDocument(
            @Parameter(description = "File to be uploaded") @RequestParam("file") MultipartFile file) {
        List<RegexpOccurrence> occurrences = scannerService.scanUploadedDocument(file);
        return ResponseEntity.ok(occurrences);
    }

    @Operation(summary = "Scan multiple documents from URLs")
    @PostMapping("/bulk")
    public ResponseEntity<Void> scanBulkDocuments(@RequestBody BulkDocumentScanRequest request) {
        scannerService.scanBulkDocuments(request);
        return ResponseEntity.ok().build();
    }
}