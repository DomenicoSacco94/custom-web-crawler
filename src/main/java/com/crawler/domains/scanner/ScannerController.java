package com.crawler.domains.scanner;

import com.crawler.domains.scanner.models.BulkPageScanRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/v1")
@Tag(name = "URL Scanner", description = "Operations related to page scanning for relevant content")
public class ScannerController {

    private final ScannerService scannerService;

    @Operation(summary = "Scan the content from multiple URLs")
    @PostMapping("/scan")
    public ResponseEntity<Void> scanUrls(@Valid @RequestBody BulkPageScanRequest request) {
        scannerService.scanBulkPages(request);
        return ResponseEntity.ok().build();
    }
}