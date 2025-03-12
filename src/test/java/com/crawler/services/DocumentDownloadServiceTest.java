package com.crawler.services;

import com.crawler.domains.scanner.DocumentDownloadService;
import com.crawler.domains.scanner.exceptions.InvalidDocumentUrlException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DocumentDownloadServiceTest {

    @InjectMocks
    private DocumentDownloadService documentDownloadService;

    @Test
    public void testDownloadFileWithInvalidUrl() {
        String invalidUrl = "invalid_url";

        assertThrows(InvalidDocumentUrlException.class, () -> documentDownloadService.downloadFile(invalidUrl));
    }
}