package com.crawler.services;

import com.crawler.domains.scanner.DocumentDownloadService;
import com.crawler.domains.scanner.exceptions.InvalidDocumentUrlException;
import com.crawler.utils.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentDownloadServiceTest {

    @InjectMocks
    private DocumentDownloadService documentDownloadService;

    @Mock
    private FileUtils fileUtils;

    @Test
    public void testDownloadFileWithInvalidUrl() {
        String invalidUrl = "invalid_url";

        assertThrows(InvalidDocumentUrlException.class, () -> documentDownloadService.downloadAndExtractText(invalidUrl));
    }

    @Test
    public void testDownloadPdfFile() throws IOException {
        String pdfUrl = "http://example.com/document.pdf";
        byte[] pdfBytes = new byte[]{1, 2, 3}; // Mock PDF content
        String extractedText = "Sample PDF text";

        URLConnection connection = mock(URLConnection.class);
        when(connection.getContentType()).thenReturn("application/pdf");
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(pdfBytes));
        when(fileUtils.readInputStreamInChunks(any())).thenReturn(pdfBytes);
        when(fileUtils.extractTextFromPdf(pdfBytes)).thenReturn(extractedText);

        String result = documentDownloadService.downloadAndExtractText(pdfUrl);

        assertEquals(extractedText, result);
    }

    @Test
    public void testDownloadHtmlFile() throws IOException {
        String htmlUrl = "http://example.com/page.html";
        String htmlContent = "<html><body>Sample HTML text</body></html>";
        Document document = Jsoup.parse(htmlContent);

        URLConnection connection = mock(URLConnection.class);
        when(connection.getContentType()).thenReturn("text/html");
        when(Jsoup.connect(htmlUrl).get()).thenReturn(document);

        String result = documentDownloadService.downloadAndExtractText(htmlUrl);

        assertEquals("Sample HTML text", result);
    }
}